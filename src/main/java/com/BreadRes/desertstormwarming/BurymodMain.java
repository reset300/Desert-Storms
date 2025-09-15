package com.BreadRes.desertstormwarming;


import com.BreadRes.desertstormwarming.client.SandstormClientEffects;
import com.BreadRes.desertstormwarming.client.SandstormDebugBlocker;
import com.BreadRes.desertstormwarming.commands.SandstormCommand;
import com.BreadRes.desertstormwarming.commands.ScreenShakeTestCommand;
import com.BreadRes.desertstormwarming.config.StormConfig;
import com.BreadRes.desertstormwarming.logic.SandstormManager;
import com.BreadRes.desertstormwarming.network.PacketHandler;
import com.BreadRes.desertstormwarming.registry.ModSoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixins;

@Mod("sandstorm")
public class BurymodMain {

    public static final String MODID = "sandstorm";

    public BurymodMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SandstormManager.class);
        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
        PacketHandler.register();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(SandstormClientEffects.class);
            MinecraftForge.EVENT_BUS.register(SandstormDebugBlocker.class);
        });
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, StormConfig.SPEC);
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        SandstormCommand.register(event.getDispatcher());
        ScreenShakeTestCommand.register(event.getDispatcher());
    }
    @Mod.EventBusSubscriber(modid = BurymodMain.MODID)
    public class PlayerJoinHandler {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
            if (!(event.getEntity() instanceof ServerPlayer player)) return;

            if (SandstormManager.isActive()) {
                SandstormManager.syncStormStateWithPlayer(player);
            }
        }
    }


}
