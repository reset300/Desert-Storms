package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.logic.SandstormManager;
import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateSandstormPacket {
    private final SandstormPhase phase;
    private final float angle;
    private final int duration;

    public UpdateSandstormPacket(SandstormPhase phase, float angle, int duration) {
        this.phase = phase;
        this.angle = angle;
        this.duration = duration;
    }

    public static void encode(UpdateSandstormPacket msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.phase);
        buf.writeFloat(msg.angle);
        buf.writeInt(msg.duration);
    }

    public static UpdateSandstormPacket decode(FriendlyByteBuf buf) {
        return new UpdateSandstormPacket(
            buf.readEnum(SandstormPhase.class),
            buf.readFloat(),
            buf.readInt()
        );
    }

    public static void handle(UpdateSandstormPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SandstormManager.setPhase(msg.phase);
            SandstormManager.setAngle(msg.angle);
            SandstormManager.setDuration(msg.duration);
        });
        ctx.get().setPacketHandled(true);
    }
}
