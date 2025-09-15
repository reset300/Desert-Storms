package com.BreadRes.desertstormwarming.client;

import com.BreadRes.desertstormwarming.logic.SandstormManager;
import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import com.BreadRes.desertstormwarming.sounds.SandstormSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SandstormSoundScheduler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static int ticksUntilNextSound = 0;
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !SandstormManager.isActive()) return;
        if (mc.player == null || mc.level == null) return;

        if (ticksUntilNextSound > 0) {
            ticksUntilNextSound--;
            return;
        }

        SandstormPhase phase = SandstormManager.getPhase();
        SoundEvent sound = SandstormSounds.pickRandomSoundFor(phase);
        if (sound != null) {
            mc.level.playLocalSound(mc.player.getX(), mc.player.getY(), mc.player.getZ(), sound,
                    SoundSource.WEATHER, 1.0f, 1.0f, false);

            int soundDuration = SandstormSounds.getSoundDurationTicks(sound);
            int buffer = 40 + random.nextInt(80);
            ticksUntilNextSound = soundDuration + buffer;
        }
    }
}
