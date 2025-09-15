package com.BreadRes.desertstormwarming.client;

import com.BreadRes.desertstormwarming.BurymodMain;
import com.BreadRes.desertstormwarming.logic.SandstormManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BurymodMain.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SandstormDebugBlocker {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (SandstormManager.isActive()
                && Minecraft.getInstance().options.renderDebug
                && event.getOverlay().id().toString().contains("debug")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        if (!SandstormManager.isActive()) return;

        if (!mc.options.renderDebug) return;

        var biomeKey = mc.level.registryAccess()
                .registryOrThrow(net.minecraft.core.registries.Registries.BIOME)
                .getResourceKey(mc.level.getBiome(mc.player.blockPosition()).value())
                .orElse(null);

        if (biomeKey == null || !biomeKey.location().getPath().contains("desert"))return;

        GuiGraphics gui = event.getGuiGraphics();
        int x = 10;
        int y = 10;
        int color = 0xFFFF5555;

        String debugText = Component.translatable("sandstorm.debug.blocked").getString();
        gui.drawString(mc.font, debugText, x, y, color);
    }

}
