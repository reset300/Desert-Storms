package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.BurymodMain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(BurymodMain.MODID, "main"),
        () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
    );
    public static <T> void sendToClient(T message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void register() {
        int id = 0;

        INSTANCE.registerMessage(id++, StartSandstormPacket.class,
            StartSandstormPacket::encode,
            StartSandstormPacket::decode,
            StartSandstormPacket::handle);
        INSTANCE.registerMessage(id++, UpdateSandstormPacket.class,
                UpdateSandstormPacket::encode,
                UpdateSandstormPacket::decode,
                UpdateSandstormPacket::handle);
        INSTANCE.registerMessage(id++, StopSandstormPacket.class,
                StopSandstormPacket::encode,
                StopSandstormPacket::decode,
                StopSandstormPacket::handle);
        INSTANCE.registerMessage(id++, LightningShakePacket.class,
                LightningShakePacket::encode, LightningShakePacket::decode, LightningShakePacket::handle);
        INSTANCE.registerMessage(id++, LightningFlashPacket.class,
                LightningFlashPacket::encode, LightningFlashPacket::decode, LightningFlashPacket::handle);
        INSTANCE.registerMessage(id++, PhaseChangePacket.class, PhaseChangePacket::encode, PhaseChangePacket::decode, PhaseChangePacket::handle);
    }
}
