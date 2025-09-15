package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.client.SandstormClientEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopSandstormPacket {
    public static void encode(StopSandstormPacket msg, FriendlyByteBuf buf) {}

    public static StopSandstormPacket decode(FriendlyByteBuf buf) {
        return new StopSandstormPacket();
    }

    public static void handle(StopSandstormPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(SandstormClientEffects::handleStop);
        ctx.get().setPacketHandled(true);
    }
}