package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.client.SandstormClientEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LightningFlashPacket {
    private final int duration;

    public LightningFlashPacket(int duration) {
        this.duration = duration;
    }

    public static void encode(LightningFlashPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.duration);
    }

    public static LightningFlashPacket decode(FriendlyByteBuf buf) {
        return new LightningFlashPacket(buf.readInt());
    }

    public static void handle(LightningFlashPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SandstormClientEffects.triggerLightningFlash(msg.duration);
        });
        ctx.get().setPacketHandled(true);
    }
}