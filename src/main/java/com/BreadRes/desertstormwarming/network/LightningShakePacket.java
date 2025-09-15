package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.client.SandstormClientEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LightningShakePacket {
    private final float intensity;
    private final int duration;

    public LightningShakePacket(float intensity, int duration) {
        this.intensity = intensity;
        this.duration = duration;
    }

    public static void encode(LightningShakePacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.intensity);
        buf.writeInt(msg.duration);
    }

    public static LightningShakePacket decode(FriendlyByteBuf buf) {
        return new LightningShakePacket(buf.readFloat(), buf.readInt());
    }

    public static void handle(LightningShakePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SandstormClientEffects.triggerLightningShake(msg.intensity, msg.duration);
        });
        ctx.get().setPacketHandled(true);
    }
}