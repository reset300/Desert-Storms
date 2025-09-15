package com.BreadRes.desertstormwarming.sounds;

import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import com.BreadRes.desertstormwarming.registry.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.BreadRes.desertstormwarming.registry.ModSoundEvents.*;

public class SandstormSounds {
    private static final Map<SandstormPhase, List<SoundEvent>> PHASE_SOUNDS = Map.of(
            SandstormPhase.PHASE_1, List.of(STORM_1_1.get(), STORM_1_2.get(), STORM_1_3.get(), STORM_1_4.get()),
            SandstormPhase.PHASE_2, List.of(STORM_2_1.get(), STORM_2_2.get(), STORM_2_3.get(), STORM_2_4.get()),
            SandstormPhase.PHASE_3, List.of(STORM_3_1.get(), STORM_3_2.get(), STORM_3_3.get(), STORM_3_4.get()),
            SandstormPhase.PHASE_4, List.of(STORM_4_1.get(), STORM_4_2.get(), STORM_4_4.get()),
            SandstormPhase.PHASE_5, List.of(STORM_5_1.get(), STORM_5_2.get(), STORM_5_3.get(), STORM_5_4.get(), STORM_5_5.get(), STORM_5_6.get())
    );

    public static final List<SoundEvent> PHASE_1 = List.of(
            ModSoundEvents.STORM_1_1.get(),
            ModSoundEvents.STORM_1_2.get(),
            ModSoundEvents.STORM_1_3.get(),
            ModSoundEvents.STORM_1_4.get()
    );

    public static final List<SoundEvent> PHASE_2 = List.of(
            ModSoundEvents.STORM_2_1.get(),
            ModSoundEvents.STORM_2_2.get(),
            ModSoundEvents.STORM_2_3.get(),
            ModSoundEvents.STORM_2_4.get()
    );

    public static final List<SoundEvent> PHASE_3 = List.of(
            ModSoundEvents.STORM_3_1.get(),
            ModSoundEvents.STORM_3_2.get(),
            ModSoundEvents.STORM_3_3.get(),
            ModSoundEvents.STORM_3_4.get()
    );

    public static final List<SoundEvent> PHASE_4 = List.of(
            ModSoundEvents.STORM_4_1.get(),
            STORM_4_2.get(),
            ModSoundEvents.STORM_4_4.get()
    );

    public static final List<SoundEvent> PHASE_5_COMMON = List.of(
            ModSoundEvents.STORM_5_1.get(),
            ModSoundEvents.STORM_5_3.get(),
            ModSoundEvents.STORM_5_4.get(),
            ModSoundEvents.STORM_5_5.get(),
            ModSoundEvents.STORM_5_6.get()
    );

    public static final SoundEvent PHASE_5_RARE = ModSoundEvents.STORM_5_2.get();

    public static List<SoundEvent> getSoundsForPhase(SandstormPhase phase) {
        return switch (phase) {
            case PHASE_1 -> PHASE_1;
            case PHASE_2 -> PHASE_2;
            case PHASE_3 -> PHASE_3;
            case PHASE_4 -> PHASE_4;
            case PHASE_5 -> PHASE_5_COMMON;
            default -> List.of();
        };
    }

    public static SoundEvent getRareSound(SandstormPhase phase) {
        return phase == SandstormPhase.PHASE_5 ? PHASE_5_RARE : null;
    }
    public static int getSoundDurationTicks(SoundEvent sound) {
        return switch (sound.getLocation().getPath()) {
            case "storm/1_1" -> 2834;
            case "storm/1_2" -> 2240;
            case "storm/1_3" -> 865;
            case "storm/1_4" -> 2240;

            case "storm/2_1" -> 764;
            case "storm/2_2" -> 854;
            case "storm/2_3" -> 649;
            case "storm/2_4" -> 738;

            case "storm/3_1" -> 3226;
            case "storm/3_2", "storm/3_3", "storm/3_4" -> 2057;

            case "storm/4_1", "storm/4_2" -> 2057;
            case "storm/4_4" -> 1225;

            case "storm/5_1" -> 1359;
            case "storm/5_2" -> 1298;
            case "storm/5_3", "storm/5_4" -> 1010;
            case "storm/5_5" -> 515;
            case "storm/5_6" -> 844;

            default -> 1200;
        };
    }


    public static SoundEvent pickRandomSoundFor(SandstormPhase phase) {
        List<SoundEvent> list = PHASE_SOUNDS.getOrDefault(phase, List.of());
        return list.isEmpty() ? null : list.get(new Random().nextInt(list.size()));
    }

}
