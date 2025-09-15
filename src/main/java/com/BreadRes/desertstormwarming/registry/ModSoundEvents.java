package com.BreadRes.desertstormwarming.registry;

import com.BreadRes.desertstormwarming.BurymodMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BurymodMain.MODID);

    public static final RegistryObject<SoundEvent> STORM_1_1 = register("storm_1_1");
    public static final RegistryObject<SoundEvent> STORM_1_2 = register("storm_1_2");
    public static final RegistryObject<SoundEvent> STORM_1_3 = register("storm_1_3");
    public static final RegistryObject<SoundEvent> STORM_1_4 = register("storm_1_4");

    public static final RegistryObject<SoundEvent> STORM_2_1 = register("storm_2_1");
    public static final RegistryObject<SoundEvent> STORM_2_2 = register("storm_2_2");
    public static final RegistryObject<SoundEvent> STORM_2_3 = register("storm_2_3");
    public static final RegistryObject<SoundEvent> STORM_2_4 = register("storm_2_4");

    public static final RegistryObject<SoundEvent> STORM_3_1 = register("storm_3_1");
    public static final RegistryObject<SoundEvent> STORM_3_2 = register("storm_3_2");
    public static final RegistryObject<SoundEvent> STORM_3_3 = register("storm_3_3");
    public static final RegistryObject<SoundEvent> STORM_3_4 = register("storm_3_4");

    public static final RegistryObject<SoundEvent> STORM_4_1 = register("storm_4_1");
    public static final RegistryObject<SoundEvent> STORM_4_2 = register("storm_4_2");
    public static final RegistryObject<SoundEvent> STORM_4_4 = register("storm_4_4");

    public static final RegistryObject<SoundEvent> STORM_5_1 = register("storm_5_1");
    public static final RegistryObject<SoundEvent> STORM_5_2 = register("storm_5_2");
    public static final RegistryObject<SoundEvent> STORM_5_3 = register("storm_5_3");
    public static final RegistryObject<SoundEvent> STORM_5_4 = register("storm_5_4");
    public static final RegistryObject<SoundEvent> STORM_5_5 = register("storm_5_5");
    public static final RegistryObject<SoundEvent> STORM_5_6 = register("storm_5_6");

    private static RegistryObject<SoundEvent> register(String name) {
        ResourceLocation id = new ResourceLocation(BurymodMain.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
}