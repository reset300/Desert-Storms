package com.BreadRes.desertstormwarming.commands;

import com.BreadRes.desertstormwarming.client.SandstormClientEffects;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ScreenShakeTestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("testshake")
            .then(Commands.argument("intensity", FloatArgumentType.floatArg(0.1f, 10.0f))
                .then(Commands.argument("duration", IntegerArgumentType.integer(1, 100))
                    .executes(ctx -> {
                        float intensity = FloatArgumentType.getFloat(ctx, "intensity");
                        int duration = IntegerArgumentType.getInteger(ctx, "duration");

                        SandstormClientEffects.triggerLightningShake(intensity, duration);

                        ctx.getSource().sendSuccess(() ->
                                        Component.translatable("sandstorm.command.shake.success", intensity, duration).withStyle(ChatFormatting.GOLD),
                                false);
                        
                        return 1;
                    })
                )
            )
            .executes(ctx -> {
                SandstormClientEffects.triggerLightningShake(2.0f, 30);
                ctx.getSource().sendSuccess(() ->
                                Component.translatable("sandstorm.command.shake.default").withStyle(ChatFormatting.GOLD),
                        false); return 1;
            })
        );
    }
}