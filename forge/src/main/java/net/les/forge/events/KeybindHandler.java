package net.les.forge.events;

import net.les.forge.gui.SubtitleDragGui;
import net.les.forge.init.Keybindings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class KeybindHandler{
    public static void init(){
        //TODO
    }
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (Keybindings.myCustomKey.isDown()) {
                SubtitleDragGui handler = new SubtitleDragGui(new Component() {
                    @Override
                    public Style getStyle() {
                        return null;
                    }

                    @Override
                    public String getContents() {
                        return "SubtitleDragGui";
                    }

                    @Override
                    public List<Component> getSiblings() {
                        return null;
                    }

                    @Override
                    public MutableComponent plainCopy() {
                        return null;
                    }

                    @Override
                    public MutableComponent copy() {
                        return null;
                    }

                    @Override
                    public FormattedCharSequence getVisualOrderText() {
                        return null;
                    }
                });

                Minecraft.getInstance().setScreen(handler);
                handler.initGui();
                SubtitleDragGui.isGuiOpen = true;
            }
        }
    }
}
