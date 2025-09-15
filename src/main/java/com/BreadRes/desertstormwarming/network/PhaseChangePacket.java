package com.BreadRes.desertstormwarming.network;

import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PhaseChangePacket {
    private final SandstormPhase phase;

    public PhaseChangePacket(SandstormPhase phase) {
        this.phase = phase;
    }

    public static void encode(PhaseChangePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.phase.ordinal());
    }

    public static PhaseChangePacket decode(FriendlyByteBuf buf) {
        return new PhaseChangePacket(SandstormPhase.values()[buf.readInt()]);
    }

    public static void handle(PhaseChangePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                com.BreadRes.desertstormwarming.client.SandstormClientEffects.handlePhaseChange(packet.phase);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}