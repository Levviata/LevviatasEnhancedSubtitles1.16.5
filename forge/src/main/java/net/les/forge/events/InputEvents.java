package net.les.forge.events;

import com.google.common.collect.Lists;
import net.les.LESMod;
import net.les.forge.gui.SubtitleDragGui;
import net.les.forge.init.KeybindsInit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@EventBusSubscriber(modid = LESMod.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
        if (KeybindsInit.openGui.consumeClick()) {
            Component styledComponent = new TextComponent("SubtitleDragGui").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF)).withBold(true));
            SubtitleDragGui subtitleDragGui = new SubtitleDragGui(styledComponent);

            Minecraft.getInstance().setScreen(subtitleDragGui);
            subtitleDragGui.initGui();
            SubtitleDragGui.isGuiOpen = true;

        }
     }
}