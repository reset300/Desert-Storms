package com.BreadRes.desertstormwarming.commands;

import com.BreadRes.desertstormwarming.logic.SandstormManager;
import com.BreadRes.desertstormwarming.logic.SandstormPhase;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SandstormCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sandstorm")
            .then(Commands.literal("start")
                .then(Commands.literal("phase")
                    .then(Commands.literal("1").executes(ctx -> startPhase(SandstormPhase.PHASE_1)))
                    .then(Commands.literal("2").executes(ctx -> startPhase(SandstormPhase.PHASE_2)))
                    .then(Commands.literal("3").executes(ctx -> startPhase(SandstormPhase.PHASE_3)))
                    .then(Commands.literal("4").executes(ctx -> startPhase(SandstormPhase.PHASE_4)))
                    .then(Commands.literal("5").executes(ctx -> startPhase(SandstormPhase.PHASE_5)))
                )
            )
            .then(Commands.literal("stop")
                .executes(ctx -> {
                    SandstormManager.stop();
                    return 1;
                })
            )
        );
    }

    private static int startPhase(SandstormPhase phase) {
        SandstormManager.start(phase);
        return 1;
    }
}
