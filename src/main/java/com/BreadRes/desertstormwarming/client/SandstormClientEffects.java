package com.BreadRes.desertstormwarming.client;

import com.mojang.blaze3d.shaders.FogShape;
import me.flashyreese.mods.sodiumextra.client.gui.SodiumExtraGameOptions;
import java.lang.reflect.Field;
import java.io.File;
import java.util.Arrays;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;
import team.lodestar.lodestone.systems.easing.Easing;
import com.BreadRes.desertstormwarming.BurymodMain;
import com.BreadRes.desertstormwarming.config.StormConfig;
import com.BreadRes.desertstormwarming.logic.SandstormManager;
import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import com.BreadRes.desertstormwarming.logic.SandstormUtils;
import com.BreadRes.desertstormwarming.sounds.SandstormSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(modid = BurymodMain.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SandstormClientEffects {
    private static SodiumExtraGameOptions.RenderSettings.FogType originalFogType = null;
    private static boolean originalMultiDimensionFogControl = false;
    private static boolean fogSettingsStored = false;
    private static SodiumExtraGameOptions sodiumExtraConfig = null;
    private static int stormSoundTimer = 0;
    private static final Minecraft mc = Minecraft.getInstance();
    private static int lightningFlashTicks = 0;
    private static SoundInstance currentStormSound = null;
    private static boolean soundFadingOut = false;
    private static float currentSoundVolume = 1.0f;

    public static void handleStart(SandstormPhase phase, float angle, int duration) {
        SandstormManager.setPhase(phase);
        SandstormManager.setAngle(angle);
        SandstormManager.setDuration(duration);
        SandstormManager.setActive(true);
        soundFadingOut = false;
        currentSoundVolume = 1.0f;
    }

    public static void handleStop() {
        SandstormManager.setActive(false);
        SandstormManager.stop();

        if (currentStormSound != null) {
            mc.getSoundManager().stop(currentStormSound);
            currentStormSound = null;
        }

        soundFadingOut = false;
        currentSoundVolume = 1.0f;
        stormSoundTimer = 0;
    }

    public static void handlePhaseChange(SandstormPhase newPhase) {
        if (currentStormSound != null) {
            mc.getSoundManager().stop(currentStormSound);
            currentStormSound = null;
        }
        stormSoundTimer = 0;
        SandstormManager.setPhase(newPhase);
    }

    public static boolean isWellCoveredClient(Player player) {
        if (StormConfig.USE_SKYLIGHT_SYSTEM.get()) {
            BlockPos pos = player.blockPosition();
            int skyLight = player.level().getBrightness(net.minecraft.world.level.LightLayer.SKY, pos);

            if (skyLight > 8) {
                return false;
            }

            for (int y = pos.getY() + 1; y <= player.level().getMaxBuildHeight(); y++) {
                BlockPos checkPos = new BlockPos(pos.getX(), y, pos.getZ());
                BlockState state = player.level().getBlockState(checkPos);

                if (!state.isAir() && !isGlassBlock(state)) {
                    return true;
                }
            }
            return false;
        } else {
            return player.blockPosition().getY() < StormConfig.OLD_LOGIC_HEIGHT.get();
        }
    }

    private static boolean isGlassBlock(BlockState state) {
        net.minecraft.world.level.block.Block block = state.getBlock();
        return block == net.minecraft.world.level.block.Blocks.GLASS ||
                block == net.minecraft.world.level.block.Blocks.WHITE_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.ORANGE_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.MAGENTA_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.LIGHT_BLUE_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.YELLOW_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.LIME_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.PINK_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.GRAY_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.LIGHT_GRAY_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.CYAN_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.PURPLE_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.BLUE_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.BROWN_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.GREEN_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.RED_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.BLACK_STAINED_GLASS ||
                block == net.minecraft.world.level.block.Blocks.GLASS_PANE ||
                block.toString().toLowerCase().contains("glass");
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!SandstormManager.isActive()) return;

        float proximity = SandstormUtils.getDesertProximity(mc.player);
        if (proximity <= 0) return;

        GuiGraphics gui = event.getGuiGraphics();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        int centerX = w / 2;

        SandstormPhase phase = SandstormManager.getPhase();
        int seconds = SandstormManager.getRemainingTime() / 20;
        int min = seconds / 60;
        int sec = seconds % 60;

        String timeText = String.format("%02d:%02d", min, sec);
        String text = Component.translatable("sandstorm.ui.storm_active",
                String.valueOf(phase.ordinal() + 1), timeText).getString();
        gui.drawString(mc.font, text, 10, h - 20, 0xF4C542);

        ResourceLocation FRAME = new ResourceLocation(BurymodMain.MODID, "textures/gui/storm_compass_frame.png");
        ResourceLocation NEEDLE = new ResourceLocation(BurymodMain.MODID, "textures/gui/storm_compass_needle.png");

        int compassX = centerX - 8;
        int compassY = h - 40;
        gui.blit(FRAME, compassX, compassY, 0, 0, 16, 16, 16, 16);

        PoseStack pose = gui.pose();
        pose.pushPose();
        pose.translate(centerX, h - 32, 0);
        pose.mulPose(Axis.ZP.rotationDegrees(-SandstormManager.getCurrentAngle()));
        gui.blit(NEEDLE, -8, -8, 0, 0, 16, 16, 16, 16);
        pose.popPose();

        if (lightningFlashTicks > 0 && !isWellCoveredClient(mc.player)) {
            float maxDuration = 80.0f;
            float progress = (80.0f - lightningFlashTicks) / maxDuration;

            float alpha;
            if (progress < 0.1f) {
                alpha = progress / 0.1f;
            } else {
                alpha = 1.0f - ((progress - 0.1f) / 0.9f);
            }
            alpha = Math.max(0f, Math.min(1f, alpha * alpha));

            pose.pushPose();
            gui.fill(0, 0, w, h, ((int) (alpha * 200) << 24) | 0xFFFFFF);
            lightningFlashTicks--;
            pose.popPose();
        } else if (lightningFlashTicks > 0) {
            lightningFlashTicks--;
        }
    }

    public static void triggerLightningFlash(int duration) {
        lightningFlashTicks = duration;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFog(ViewportEvent.RenderFog event) {
        if (!SandstormManager.isActive()) return;

        assert mc.player != null;
        float proximity = SandstormUtils.getDesertProximity(mc.player);
        if (proximity <= 0.01) return;

        float range = getFogRange(SandstormManager.getPhase()) * proximity;

        event.setNearPlaneDistance(0f);
        event.setFarPlaneDistance(range);
        event.setCanceled(true);

        RenderSystem.setShaderFogStart(0f);
        RenderSystem.setShaderFogEnd(range);
        RenderSystem.setShaderFogShape(FogShape.SPHERE);

        float f = getFogDarkness(SandstormManager.getPhase());
        RenderSystem.setShaderFogColor(0.85f * f * proximity,
                0.65f * f * proximity,
                0.3f * f * proximity,
                1.0f);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (!SandstormManager.isActive()) return;

        assert mc.player != null;
        float proximity = SandstormUtils.getDesertProximity(mc.player);
        if (proximity <= 0.01) return;

        float f = getFogDarkness(SandstormManager.getPhase());

        event.setRed(0.85f * f * proximity);
        event.setGreen(0.65f * f * proximity);
        event.setBlue(0.3f * f * proximity);
    }

    private static float lightningShakeIntensity = 0f;
    private static int lightningShakeDuration = 0;
    private static float windShakeTimer = 0f;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        LocalPlayer player = mc.player;
        if (player == null) return;

        if (SandstormManager.isActive() && !fogSettingsStored) {
            storeAndOverrideSodiumExtraFog();
            fogSettingsStored = true;
        } else if (!SandstormManager.isActive() && fogSettingsStored) {
            restoreSodiumExtraFog();
            fogSettingsStored = false;
        }

        float armorProtection = getArmorProtection(player);
        float windReduction = Math.min(0.7f, armorProtection);

        if (lightningShakeDuration > 0) {
            float effectiveIntensity = lightningShakeIntensity * (1.0f - armorProtection * 0.3f);
            ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(3)
                    .setIntensity(effectiveIntensity, effectiveIntensity * 0.7f, effectiveIntensity * 0.3f)
                    .setEasing(Easing.EXPO_OUT));

            lightningShakeDuration--;
            lightningShakeIntensity *= 0.85f;

            if (lightningShakeDuration <= 0) {
                lightningShakeIntensity = 0f;
            }
        }

        if (SandstormManager.isActive()) {
            SandstormPhase phase = SandstormManager.getPhase();
            float proximity = SandstormUtils.getDesertProximity(player);

            if (proximity > 0.3f && phase.ordinal() >= 2) {
                windShakeTimer += 0.1f;
                if (windShakeTimer >= 60) {
                    windShakeTimer = 0f;
                    float windShake = phase.windStrength * proximity * 0.15f * (1.0f - windReduction);
                    if (windShake > 0.02f) {
                        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(10)
                                .setIntensity(windShake, windShake * 0.5f, windShake * 0.2f)
                                .setEasing(Easing.SINE_IN_OUT));
                    }
                }
            }
        } else {
            windShakeTimer = 0f;
        }

        handleStormSounds(player, armorProtection);

        if (!SandstormManager.isActive()) return;
        if (player.isCreative() || player.isSpectator()) return;

        float proximity = SandstormUtils.getDesertProximity(player);
        if (proximity <= 0.1f) return;

        BlockPos pos = player.blockPosition();
        if (isWellCoveredClient(player)) return;

        Vec3 wind = SandstormManager.getCurrentWindVec();
        double base = SandstormManager.getPhase().windStrength * (1.0f - windReduction);
        double speed = 0.5 * Math.pow(base, 1.3) * proximity;
        if (player.isShiftKeyDown()) speed *= 0.3;

        if (speed > 0.01) {
            player.setDeltaMovement(player.getDeltaMovement().add(wind.normalize().scale(speed)));
        }

        float particleReduction = armorProtection * 0.3f;
        float effectiveParticleRate = 0.1f * proximity * (1.0f - particleReduction);

        if (mc.level.random.nextFloat() < effectiveParticleRate) {
            int particleCount = (int) (8 * proximity * (1.0f - particleReduction));
            Vec3 windVec = SandstormManager.getCurrentWindVec();
            float windStrength = SandstormManager.getWindStrength();

            for (int i = 0; i < particleCount; i++) {
                double px = player.getX() + (mc.level.random.nextDouble() - 0.5) * 30;
                double py = player.getY() + mc.level.random.nextDouble() * 15 + 5;
                double pz = player.getZ() + (mc.level.random.nextDouble() - 0.5) * 30;

                double windMultiplier = 1.5 + mc.level.random.nextDouble();
                double vx = windVec.x() * windMultiplier * windStrength * 0.5;
                double vz = windVec.z() * windMultiplier * windStrength * 0.5;
                double vy = -0.05 - mc.level.random.nextDouble() * 0.05;

                vx += (mc.level.random.nextDouble() - 0.5) * 0.1;
                vz += (mc.level.random.nextDouble() - 0.5) * 0.1;

                mc.level.addParticle(
                        new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SAND.defaultBlockState()),
                        px, py, pz, vx, vy, vz
                );
            }
        }
    }

    private static void handleStormSounds(LocalPlayer player, float armorProtection) {
        if (mc.level == null || mc.isPaused()) return;

        if (mc.isPaused()) {
            return;
        }
        float proximity = SandstormUtils.getDesertProximity(player);
        boolean isInDesert = proximity > 0.1f;
        boolean stormActive = SandstormManager.isActive();

        if (soundFadingOut) {
            currentSoundVolume -= 0.02f;
            if (currentSoundVolume <= 0) {
                if (currentStormSound != null) {
                    mc.getSoundManager().stop(currentStormSound);
                    currentStormSound = null;
                }
                soundFadingOut = false;
                currentSoundVolume = 0f;
            }
            return;
        }

        if (!stormActive) {
            if (currentStormSound != null) {
                mc.getSoundManager().stop(currentStormSound);
                currentStormSound = null;
            }
            stormSoundTimer = 0;
            currentSoundVolume = 1.0f;
            return;
        }

        if (!isInDesert) {
            if (currentStormSound != null) {
                mc.getSoundManager().stop(currentStormSound);
                currentStormSound = null;
            }
            stormSoundTimer = 0;
            return;
        }

        SandstormPhase phase = SandstormManager.getPhase();
        boolean soundStillPlaying = currentStormSound != null && mc.getSoundManager().isActive(currentStormSound);

        if (!soundStillPlaying && stormSoundTimer <= 0) {
            float baseVolume = Math.min(0.8f, proximity * (1.0f - armorProtection * 0.3f)) * currentSoundVolume;
            playNextStormSound(phase, baseVolume);
        }

        if (stormSoundTimer > 0) {
            stormSoundTimer--;
        }
    }

    private static void playNextStormSound(SandstormPhase phase, float volume) {
        if (currentStormSound != null) {
            mc.getSoundManager().stop(currentStormSound);
            currentStormSound = null;
        }

        if (!SandstormManager.isActive() || soundFadingOut) return;

        float proximity = SandstormUtils.getDesertProximity(mc.player);
        if (proximity <= 0.1f) return;

        SoundEvent sound = SandstormSounds.pickRandomSoundFor(phase);

        if (sound == null) {
            stormSoundTimer = 100;
            return;
        }

        float pitch = 0.9f + mc.level.random.nextFloat() * 0.2f;
        currentStormSound = SimpleSoundInstance.forLocalAmbience(sound, volume, pitch);
        mc.getSoundManager().play(currentStormSound);

        int nextSoundDelay = getNextSoundDelay(phase);
        stormSoundTimer = nextSoundDelay;

        if (phase == SandstormPhase.PHASE_5 && mc.level.random.nextFloat() < 0.15f) {
            SoundEvent rare = SandstormSounds.getRareSound(phase);
            if (rare != null) {
                mc.execute(() -> {
                    try {
                        Thread.sleep(2000);
                        if (SandstormManager.isActive() && SandstormUtils.getDesertProximity(mc.player) > 0.1f && !soundFadingOut) {
                            SoundInstance rareSound = SimpleSoundInstance.forLocalAmbience(rare, volume * 0.8f, pitch);
                            mc.getSoundManager().play(rareSound);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }

    private static int getNextSoundDelay(SandstormPhase phase) {
        return switch (phase) {
            case PHASE_1 -> 600;
            case PHASE_2 -> 400;
            case PHASE_3 -> 200;
            case PHASE_4 -> 150;
            case PHASE_5 -> 100;
            default -> 300;
        };
    }

    private static void restoreSodiumExtraFog() {
        if (sodiumExtraConfig != null && sodiumExtraConfig.renderSettings != null && originalFogType != null) {
            try {
                sodiumExtraConfig.renderSettings.fogType = originalFogType;
                sodiumExtraConfig.renderSettings.multiDimensionFogControl = originalMultiDimensionFogControl;

                try {
                    sodiumExtraConfig.writeChanges();
                } catch (Exception e) {
                    System.out.println("Could not save restored Sodium Extra config");
                }

                System.out.println("Restored original Sodium Extra fog settings");
            } catch (Exception e) {
                System.out.println("Could not restore Sodium Extra fog settings: " + e.getMessage());
            }
        }

        originalFogType = null;
        fogSettingsStored = false;
        sodiumExtraConfig = null;
    }

    private static void storeAndOverrideSodiumExtraFog() {
        try {
            sodiumExtraConfig = getSodiumExtraConfigInstance();

            if (sodiumExtraConfig != null && sodiumExtraConfig.renderSettings != null) {
                originalFogType = sodiumExtraConfig.renderSettings.fogType;
                originalMultiDimensionFogControl = sodiumExtraConfig.renderSettings.multiDimensionFogControl;

                sodiumExtraConfig.renderSettings.fogType = SodiumExtraGameOptions.RenderSettings.FogType.DEFAULT;
                sodiumExtraConfig.renderSettings.multiDimensionFogControl = false;

                try {
                    sodiumExtraConfig.writeChanges();
                } catch (Exception e) {
                    System.out.println("Could not save Sodium Extra config, but fog override is active");
                }

                System.out.println("Successfully overrode Sodium Extra fog settings for sandstorm");
            }
        } catch (Exception e) {
            System.out.println("Could not access Sodium Extra config: " + e.getMessage());
        }
    }

    private static float getArmorProtection(Player player) {
        long armorPieces = Arrays.stream(EquipmentSlot.values())
                .filter(s -> s.getType() == EquipmentSlot.Type.ARMOR)
                .filter(s -> !player.getItemBySlot(s).isEmpty())
                .count();

        return (float) armorPieces * 0.25f;
    }

    public static void triggerLightningShake(float intensity, int duration) {
        lightningShakeIntensity = intensity;
        lightningShakeDuration = duration;
    }

    private static SodiumExtraGameOptions getSodiumExtraConfigInstance() {
        try {
            Class<?> sodiumExtraClientMod = Class.forName("me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod");

            Field[] fields = sodiumExtraClientMod.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == SodiumExtraGameOptions.class) {
                    field.setAccessible(true);
                    Object value = field.get(null);
                    if (value instanceof SodiumExtraGameOptions) {
                        return (SodiumExtraGameOptions) value;
                    }
                }
            }

            String[] possibleFieldNames = {"config", "CONFIG", "options", "OPTIONS", "gameOptions", "GAME_OPTIONS"};
            for (String fieldName : possibleFieldNames) {
                try {
                    Field configField = sodiumExtraClientMod.getDeclaredField(fieldName);
                    configField.setAccessible(true);
                    Object value = configField.get(null);
                    if (value instanceof SodiumExtraGameOptions) {
                        return (SodiumExtraGameOptions) value;
                    }
                } catch (Exception e) {
                }
            }

            File configFile = new File(Minecraft.getInstance().gameDirectory, "config/sodium-extra-options.json");
            if (configFile.exists()) {
                return SodiumExtraGameOptions.load(configFile);
            }

        } catch (Exception e) {
            System.out.println("Could not find Sodium Extra config: " + e.getMessage());
        }

        return null;
    }

    @SubscribeEvent
    public static void renderSandstormOnHorizon(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        if (!SandstormManager.isActive()) return;
        if (SandstormUtils.getDesertProximity(mc.player) > 0) return;

        BlockPos desert = SandstormUtils.findClosestDesert(mc.player, 256);
        if (desert == null) return;

        double dx = desert.getX() + 0.5 - mc.player.getX();
        double dz = desert.getZ() + 0.5 - mc.player.getZ();
        float angle = (float)Math.toDegrees(Math.atan2(dz, dx)) - mc.player.getYRot();

        float alpha = 0.3f;
        float distance = 0.9f;

        PoseStack pose = event.getPoseStack();
        pose.pushPose();
        pose.translate(0, 0, -200);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        Matrix4f matrix = pose.last().pose();

        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        int r = 215, g = 170, b = 90, a = (int)(alpha * 255);

        float size = 200f;
        buf.vertex(matrix, -size, -size, 0).color(r, g, b, a).endVertex();
        buf.vertex(matrix, -size,  size, 0).color(r, g, b, a).endVertex();
        buf.vertex(matrix,  size,  size, 0).color(r, g, b, a).endVertex();
        buf.vertex(matrix,  size, -size, 0).color(r, g, b, a).endVertex();
        Tesselator.getInstance().end();

        RenderSystem.disableBlend();
        pose.popPose();
    }

    private static float getFogRange(SandstormPhase phase) {
        return switch (phase) {
            case PHASE_1 -> 48f;
            case PHASE_2 -> 36f;
            case PHASE_3 -> 24f;
            case PHASE_4 -> 12f;
            case PHASE_5 -> 6f;
            default -> 64f;
        };
    }

    private static float getFogDarkness(SandstormPhase phase) {
        return switch (phase) {
            case PHASE_1 -> 1.0f;
            case PHASE_2 -> 0.8f;
            case PHASE_3 -> 0.6f;
            case PHASE_4 -> 0.4f;
            case PHASE_5 -> 0.25f;
            default -> 1.0f;
        };
    }
}