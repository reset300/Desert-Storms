package com.BreadRes.desertstormwarming.logic;

import com.BreadRes.desertstormwarming.config.StormConfig;
import com.BreadRes.desertstormwarming.network.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SandstormManager {
    private static final Random random = new Random();
    private static int directionTimer = 0;
    private static int tickCounter = 0;
    private static boolean active = false;
    private static SandstormPhase currentPhase = SandstormPhase.PHASE_1;
    private static int remainingTime = 0;
    private static float targetAngle = 0f;
    private static float currentAngle = 0f;
    private static int soundTimer = 0;
    private static int autoTriggerTimer = 0;
    private static int glassBreakingTimer = 0;

    public static boolean isActive() { return active; }
    public static SandstormPhase getPhase() { return currentPhase; }
    public static int getRemainingTime() { return remainingTime; }
    public static float getCurrentAngle() { return currentAngle; }

    public static Vec3 getCurrentWindVec() {
        double rad = Math.toRadians(currentAngle);
        double strength = currentPhase.getWindStrength();
        return new Vec3(Math.cos(rad), 0, Math.sin(rad)).normalize().scale(strength);
    }

    public static void start(SandstormPhase phase) {
        currentPhase = phase;
        remainingTime = randomDuration(currentPhase);
        active = true;
        targetAngle = random.nextFloat() * 360f;
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                new StartSandstormPacket(currentPhase, targetAngle, remainingTime));
    }

    public static void setPhase(SandstormPhase phase) { currentPhase = phase; }
    public static void setAngle(float angle) { targetAngle = angle; }
    public static void setDuration(int duration) { remainingTime = duration; }
    public static void setActive(boolean value) { active = value; }

    public static void syncStormStateWithPlayer(ServerPlayer player) {
        if (isActive()) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    new StartSandstormPacket(currentPhase, currentAngle, remainingTime));
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    new UpdateSandstormPacket(currentPhase, currentAngle, remainingTime));
        } else {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                    new StopSandstormPacket());
        }
    }

    public static void stop() {
        setActive(false);
        remainingTime = 0;
        glassBreakingTimer = 0;
        PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new StopSandstormPacket());
    }

    public static boolean isWellCovered(Player player) {
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

    public static boolean isInStormBiome(Entity entity) {
        if (entity instanceof Player player) {
            return SandstormUtils.getDesertProximity(player) > 0.1f;
        } else {
            BlockPos pos = entity.blockPosition();
            for (ServerPlayer player : entity.level().getServer().getPlayerList().getPlayers()) {
                if (player.level() == entity.level()) {
                    double distance = player.position().distanceTo(entity.position());
                    if (distance <= 128 && SandstormUtils.getDesertProximity(player) > 0.1f) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static float getWindStrength() {
        return isActive() ? currentPhase.getWindStrength() : 0f;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.LevelTickEvent event) {
        if (!(event.level instanceof ServerLevel level)) return;
        if (event.phase != TickEvent.Phase.END) return;

        if (StormConfig.ENABLE_AUTO_TRIGGER.get()) {
            handleAutoTrigger(level);
        }

        if (!active) return;

        directionTimer++;

        if (remainingTime <= 0) {
            int maxPhase = StormConfig.MAX_STORM_PHASE.get();
            int currentPhaseNum = currentPhase.ordinal() + 1;
            SandstormPhase oldPhase = currentPhase;

            if (currentPhaseNum >= maxPhase) {
                stop();
                return;
            } else if (currentPhaseNum == 1) {
                if (random.nextInt(2) == 0) {
                    stop();
                    return;
                } else {
                    currentPhase = SandstormPhase.PHASE_2;
                    remainingTime = randomDuration(currentPhase);
                }
            } else {
                if (random.nextInt(10) < 3) {
                    currentPhase = currentPhase.getPreviousPhase();
                } else {
                    currentPhase = currentPhase.getNextPhase();
                    if (currentPhase.ordinal() + 1 > maxPhase) {
                        stop();
                        return;
                    }
                }
                remainingTime = randomDuration(currentPhase);
            }

            if (oldPhase != currentPhase) {
                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new PhaseChangePacket(currentPhase));
            }

            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                    new UpdateSandstormPacket(currentPhase, currentAngle, remainingTime));
            return;
        }

        if (isActive()) {
            remainingTime--;
            if (remainingTime <= 0) {

            } else {
                PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(),
                        new UpdateSandstormPacket(currentPhase, currentAngle, remainingTime));
            }
        }

        if (directionTimer >= 6000) {
            directionTimer = 0;
            targetAngle = random.nextFloat() * 360f;
        }

        float delta = wrapDegrees(targetAngle - currentAngle);
        currentAngle += delta * 0.05f;

        try {
            for (var entity : level.getEntities().getAll()) {
                if (entity == null) continue;

                if (entity instanceof LivingEntity livingEntity) {
                    if (!livingEntity.isAlive() || livingEntity.isSpectator()) continue;
                    if (!isInStormBiome(livingEntity)) continue;

                    boolean isProtected = false;

                    if (livingEntity instanceof Player player) {
                        isProtected = checkCreateArmorProtection(player);

                        if (!isWellCovered(player)) {
                            if (!isProtected) {
                                applyStormEffects(player, level);
                            }
                            applyDamage(player, level);
                        }
                    } else {
                        applyDamage(livingEntity, level);
                    }

                    if (StormConfig.ENABLE_LIGHTNING.get() && currentPhase.ordinal() >= SandstormPhase.PHASE_4.ordinal()) {
                        handleLightning(livingEntity, level);
                    }
                }

                if (!(entity instanceof ItemEntity) && !entity.isRemoved() && StormConfig.ENABLE_WIND_EFFECTS.get()) {
                    if (isInStormBiome(entity)) {
                        applyWindToEntity(entity, level);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing entities: " + e.getMessage());
        }

        if (currentPhase.ordinal() >= SandstormPhase.PHASE_4.ordinal()) {
            handleGlassBreaking(level);
        }
    }

    private static void handleGlassBreaking(ServerLevel level) {
        if (!StormConfig.ENABLE_GLASS_BREAKING.get()) return;

        glassBreakingTimer++;
        int breakingInterval = currentPhase == SandstormPhase.PHASE_5 ? 60 : 100;

        if (glassBreakingTimer >= breakingInterval) {
            glassBreakingTimer = 0;

            for (ServerPlayer player : level.players()) {
                if (!isInStormBiome(player)) continue;

                BlockPos playerPos = player.blockPosition();
                int radius = 8;

                for (int x = -radius; x <= radius; x++) {
                    for (int y = -3; y <= 3; y++) {
                        for (int z = -radius; z <= radius; z++) {
                            if (level.random.nextFloat() > 0.02f) continue;

                            BlockPos pos = playerPos.offset(x, y, z);
                            BlockState state = level.getBlockState(pos);

                            if (isGlassBlock(state)) {
                                if (level.canSeeSky(pos) || level.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) > 10) {
                                    level.destroyBlock(pos, true);
                                    level.playSound(null, pos, net.minecraft.sounds.SoundEvents.GLASS_BREAK,
                                            net.minecraft.sounds.SoundSource.BLOCKS, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                }
            }
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

    private static void applyWindToEntity(Entity entity, ServerLevel level) {
        Vec3 windVec = getCurrentWindVec();
        float windStrength = getWindStrength();

        double windMultiplier = 0.05;

        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof Player player) {
                boolean hasNetheriteBoots = isNetheriteBoots(player.getItemBySlot(EquipmentSlot.FEET));
                boolean hasFullNetherite = hasFullArmor(player) && isFullNetherite(player);

                if (hasNetheriteBoots && hasFullNetherite) {
                    return;
                }

                windMultiplier = player.isShiftKeyDown() ? 0.015 : 0.05;
            } else {
                windMultiplier = 0.03;
            }
        } else {
            windMultiplier = 0.08;
        }

        Vec3 windForce = windVec.normalize().scale(windMultiplier * Math.pow(windStrength, 1.2));
        entity.push(windForce.x, 0, windForce.z);
        entity.hasImpulse = true;
    }

    private static boolean checkCreateArmorProtection(Player player) {
        if (!StormConfig.ENABLE_CREATE_INTEGRATION.get()) return false;

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);

        boolean hasCreateHelmet = isCreateArmorPiece(helmet);
        boolean hasCreateChestplate = isCreateArmorPiece(chestplate)
                && hasAirTank(chestplate);

        if (hasCreateHelmet && hasCreateChestplate && isFullyCharged(chestplate)) {
            int airConsumption = currentPhase.getAirConsumption();
            if (airConsumption > 0 && player.tickCount % 20 == 0) {
                consumeAir(chestplate, airConsumption);
            }
            return true;
        }

        return false;
    }

    private static void applyStormEffects(Player player, ServerLevel level) {
        if (StormConfig.IGNORE_ARMOR_FOR_EFFECTS.get() || !hasFullArmor(player)) {
            if (currentPhase.hasFatigue()) {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60, 0, false, false));
            }
            if (currentPhase.hasHunger()) {
                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60, 0, false, false));
            }
            if (currentPhase.hasBlindness()) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
            }
            if (currentPhase.hasSlowness()) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, false, false));
            }
            if (currentPhase.hasWeakness()) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 0, false, false));
            }
        }
    }

    private static void applyDamage(LivingEntity entity, ServerLevel level) {
        if (!StormConfig.ENABLE_STORM_DAMAGE.get()) return;

        boolean isPlayer = entity instanceof Player;
        Player player = isPlayer ? (Player) entity : null;

        boolean hasFullArmor = isPlayer ? hasFullArmor(player) : false;
        boolean hasPartialArmor = isPlayer ? hasPartialArmor(player) : false;
        boolean hasAnyArmor = hasFullArmor || hasPartialArmor;

        int playerDamageInterval = StormConfig.getPlayerDamageInterval(currentPhase.ordinal() + 1);
        int armorDamageInterval = StormConfig.getArmorDamageInterval(currentPhase.ordinal() + 1);
        int playerDamage = StormConfig.getPlayerDamage(currentPhase.ordinal() + 1);
        int armorDamage = StormConfig.getArmorDamage(currentPhase.ordinal() + 1);

        if (hasAnyArmor && armorDamage > 0 && entity.tickCount % armorDamageInterval == 0) {
            damageArmor(entity, armorDamage);
        }

        if (playerDamage > 0 && entity.tickCount % playerDamageInterval == 0) {
            if (hasFullArmor) {

            } else if (hasPartialArmor) {
                entity.hurt(level.damageSources().generic(), playerDamage);
            } else {
                entity.hurt(level.damageSources().generic(), playerDamage);
            }
        }
    }

    private static void applyWindEffects(LivingEntity entity) {
        if (entity instanceof Player player) {
            boolean hasNetheriteBoots = isNetheriteBoots(player.getItemBySlot(EquipmentSlot.FEET));
            boolean hasFullNetherite = hasFullArmor(player) && isFullNetherite(player);

            if (hasNetheriteBoots && hasFullNetherite) {
                return;
            }
        }

        Vec3 wind = getCurrentWindVec();
        double speed = 0.5 * Math.pow(currentPhase.getWindStrength(), 1.3);

        if (entity instanceof Player player && player.isShiftKeyDown()) {
            speed *= 0.3;
        }

        entity.setDeltaMovement(entity.getDeltaMovement().add(wind.normalize().scale(speed)));
    }

    private static void handleLightning(LivingEntity entity, ServerLevel level) {
        int lightningChance = switch (currentPhase) {
            case PHASE_4 -> 60;
            case PHASE_5 -> 30;
            default -> 200;
        };

        if (level.random.nextInt(lightningChance) == 0) {
            BlockPos entityPos = entity.blockPosition();
            BlockPos strikePos = findSafeLightningPos(level, entityPos);

            if (strikePos != null) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                if (bolt != null) {
                    bolt.moveTo(strikePos.getX(), strikePos.getY(), strikePos.getZ());
                    level.addFreshEntity(bolt);

                    for (Player nearbyPlayer : level.players()) {
                        if (nearbyPlayer instanceof ServerPlayer serverPlayer) {
                            double distance = nearbyPlayer.position().distanceTo(new Vec3(strikePos.getX(), strikePos.getY(), strikePos.getZ()));
                            if (distance <= 50) {
                                float baseIntensity = currentPhase == SandstormPhase.PHASE_5 ? 4.0f : 2.5f;
                                float shakeIntensity = (float) Math.max(0.2f, baseIntensity - (distance / 12.0f));
                                int shakeDuration = (int) Math.max(8, 25 - (distance / 2.0f));

                                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                                        new LightningShakePacket(shakeIntensity, shakeDuration));

                                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                                        new LightningFlashPacket(80));
                            }
                        }
                    }
                }
            }
        }
    }

    private static BlockPos findSafeLightningPos(ServerLevel level, BlockPos entityPos) {
        for (int attempts = 0; attempts < 10; attempts++) {
            BlockPos testPos = entityPos.offset(
                    level.random.nextInt(30) - 15,
                    0,
                    level.random.nextInt(30) - 15
            );

            if (level.canSeeSky(testPos)) {
                return testPos;
            }
        }
        return null;
    }

    private static boolean hasFullArmor(Player player) {
        return !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty() &&
                !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() &&
                !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty() &&
                !player.getItemBySlot(EquipmentSlot.FEET).isEmpty();
    }

    private static boolean hasPartialArmor(Player player) {
        return !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty() ||
                !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ||
                !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty() ||
                !player.getItemBySlot(EquipmentSlot.FEET).isEmpty();
    }

    private static boolean isFullNetherite(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem().toString().contains("netherite") &&
                player.getItemBySlot(EquipmentSlot.CHEST).getItem().toString().contains("netherite") &&
                player.getItemBySlot(EquipmentSlot.LEGS).getItem().toString().contains("netherite") &&
                player.getItemBySlot(EquipmentSlot.FEET).getItem().toString().contains("netherite");
    }

    private static boolean isCreateArmorPiece(ItemStack stack) {
        if (stack.isEmpty()) return false;
        String itemId = stack.getItem().toString().toLowerCase();
        String registryName = "";

        try {
            registryName = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().toLowerCase();
        } catch (Exception e) {
            registryName = itemId;
        }

        return registryName.contains("create") && registryName.contains("diving") ||
                itemId.contains("copper_diving") || itemId.contains("netherite_diving") ||
                registryName.contains("copperbacktank") || registryName.contains("netheritebacktank");
    }

    private static boolean hasAirTank(ItemStack chestplate) {
        if (!isCreateArmorPiece(chestplate)) return false;
        return chestplate.hasTag() && chestplate.getTag().contains("Air");
    }

    private static boolean isFullyCharged(ItemStack chestplate) {
        if (!hasAirTank(chestplate)) return false;
        int currentAir = chestplate.getTag() != null ? chestplate.getTag().getInt("Air") : 0;
        return currentAir > 10;
    }

    private static void consumeAir(ItemStack chestplate, int amount) {
        if (!hasAirTank(chestplate) || chestplate.getTag() == null) return;

        int currentAir = chestplate.getTag().getInt("Air");
        int newAir = Math.max(0, currentAir - amount);
        chestplate.getTag().putInt("Air", newAir);
    }

    private static boolean isNetheriteBoots(ItemStack boots) {
        if (boots.isEmpty()) return false;
        String registryName = "";

        try {
            registryName = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(boots.getItem()).toString().toLowerCase();
        } catch (Exception e) {
            registryName = boots.getItem().toString().toLowerCase();
        }

        return boots.getItem().toString().contains("netherite_boots") ||
                (registryName.contains("create") && registryName.contains("netherite") && registryName.contains("diving"));
    }

    private static void handleAutoTrigger(ServerLevel level) {
        autoTriggerTimer++;
        int intervalTicks = StormConfig.AUTO_TRIGGER_INTERVAL.get() * 1200;

        if (autoTriggerTimer >= intervalTicks) {
            autoTriggerTimer = 0;

            if (isActive()) return;

            boolean hasPlayersInDesert = level.players().stream()
                    .anyMatch(p -> SandstormUtils.getDesertProximity(p) > 0.1f);

            if (!hasPlayersInDesert) return;

            boolean isRaining = level.isRaining() || level.isThundering();

            if (isRaining) {
                int phaseToTrigger = Math.min(StormConfig.RAIN_TRIGGER_PHASE.get(), StormConfig.MAX_STORM_PHASE.get());
                SandstormPhase phase = SandstormPhase.values()[phaseToTrigger - 1];
                start(phase);
            } else {
                if (random.nextBoolean()) {
                    int phaseToTrigger = Math.max(1, StormConfig.RAIN_TRIGGER_PHASE.get() - 1);
                    phaseToTrigger = Math.min(phaseToTrigger, StormConfig.MAX_STORM_PHASE.get());
                    SandstormPhase phase = SandstormPhase.values()[phaseToTrigger - 1];
                    start(phase);
                }
            }
        }
    }

    private static void damageArmor(LivingEntity entity, int amount) {
        List<EquipmentSlot> armorSlots = Arrays.asList(
                EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET
        );

        List<EquipmentSlot> filledSlots = armorSlots.stream()
                .filter(slot -> !entity.getItemBySlot(slot).isEmpty())
                .collect(Collectors.toList());

        if (filledSlots.isEmpty()) return;

        EquipmentSlot slot = filledSlots.get(random.nextInt(filledSlots.size()));
        ItemStack stack = entity.getItemBySlot(slot);

        if (!stack.isEmpty()) {
            stack.hurtAndBreak(amount, entity, e -> {
                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.broadcastBreakEvent(slot);
                }
            });
        }
    }

    private static float wrapDegrees(float angle) {
        angle %= 360f;
        if (angle >= 180f) angle -= 360f;
        if (angle < -180f) angle += 360f;
        return angle;
    }

    private static int randomDuration(SandstormPhase phase) {
        return random.nextInt(phase.getMaxDuration() - phase.getMinDuration() + 1) + phase.getMinDuration();
    }

    public static int getTickCounter() { return tickCounter; }
    public static void setTickCounter(int tickCounter) { SandstormManager.tickCounter = tickCounter; }
}