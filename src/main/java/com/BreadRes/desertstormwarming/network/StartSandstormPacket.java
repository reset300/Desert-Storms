package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.client.SandstormClientEffects;
import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StartSandstormPacket {
    private final SandstormPhase phase;
    private final float angle;
    private final int duration;

    public StartSandstormPacket(SandstormPhase phase, float angle, int duration) {
        this.phase = phase;
        this.angle = angle;
        this.duration = duration;
    }

    public static void encode(StartSandstormPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.phase);
        buf.writeFloat(msg.angle);
        buf.writeInt(msg.duration);
    }

    public static StartSandstormPacket decode(FriendlyByteBuf buf) {
        return new StartSandstormPacket(
                buf.readEnum(SandstormPhase.class),
                buf.readFloat(),
                buf.readInt()
        );
    }

    public static void handle(StartSandstormPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
                SandstormClientEffects.handleStart(msg.phase, msg.angle, msg.duration);
        });
        ctx.get().setPacketHandled(true);
    }
}
