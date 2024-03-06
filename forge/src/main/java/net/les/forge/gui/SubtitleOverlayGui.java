package net.les.forge.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.les.forge.config.Config;
import net.les.forge.util.ColorConverter;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.floor;
import static java.lang.Math.log;
import static net.les.forge.config.Config.OverlayPosition.*;
import static net.les.forge.config.Config.scale;
import static net.les.forge.config.Config.showSubtitles;
import static net.les.forge.gui.SubtitleDragGui.isGuiOpen;
import static net.minecraft.util.Mth.clampedLerp;

public class SubtitleOverlayGui extends Gui implements SoundEventListener
{
    private final Minecraft minecraft;
    Logger log = Logger.getLogger("SubtitleOverlayGui");
    private boolean isListening;
    private static final List<SubtitleOverlay.Subtitle> subtitles = Lists.newArrayList();
    public static int lastPosX;
    public static int lastPosY;
    public SubtitleOverlayGui(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }
    /*static {
        List<SubtitleOverlay.Subtitle> value;
        try {
            Field subtitlesField = ObfuscationReflectionHelper.findField(SubtitleOverlay.class, "field_184070_f");

            // Set the field accessible
            subtitlesField.setAccessible(true);

            // Assuming 'subtitleOverlayInstance' is an instance of SubtitleOverlay
            SubtitleOverlay subtitleOverlayInstance = new SubtitleOverlay(Minecraft.getInstance());
            value = (List<SubtitleOverlay.Subtitle>) subtitlesField.get(subtitleOverlayInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        subtitles = value;
    }*/

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES) {
            event.setCanceled(true); // This prevents the default subtitle rendering.
            PoseStack stack = event.getMatrixStack();
            if (!isGuiOpen) {
                SubtitleOverlayGui subtitleOverlayGui = new SubtitleOverlayGui(Minecraft.getInstance());
                subtitleOverlayGui.render(stack);
            }
        }
    }

    public void render(PoseStack stack) {
        if (!this.isListening && this.minecraft.options.showSubtitles) {
            this.minecraft.getSoundManager().addListener(this);
            this.isListening = true;
        } else if (this.isListening && !this.minecraft.options.showSubtitles) {
            this.minecraft.getSoundManager().removeListener(this);
            this.isListening = false;
        }

        if (this.isListening && !subtitles.isEmpty()) {
            RenderSystem.pushMatrix();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            assert this.minecraft.player != null;
            Vec3 vector3d = new Vec3(this.minecraft.player.getX(), this.minecraft.player.getEyeY(), this.minecraft.player.getZ());
            Vec3 vector3d1 = (new Vec3(0.0D, 0.0D, -1.0D)).xRot(-this.minecraft.player.xRot * ((float)Math.PI / 180F)).yRot(-this.minecraft.player.yRot * ((float)Math.PI / 180F));
            Vec3 vector3d2 = (new Vec3(0.0D, 1.0D, 0.0D)).xRot(-this.minecraft.player.xRot * ((float)Math.PI / 180F)).yRot(-this.minecraft.player.yRot * ((float)Math.PI / 180F));
            Vec3 vector3d3 = vector3d1.cross(vector3d2);
            int captionIndex = 0;
            int maxLength = 0;
            Iterator<SubtitleOverlay.Subtitle> iterator = subtitles.iterator();

            while(iterator.hasNext()) {
                SubtitleOverlay.Subtitle subtitleoverlaygui$subtitle = iterator.next();
                if (subtitleoverlaygui$subtitle.getTime() + 3000L <= Util.getMillis()) {
                    iterator.remove();
                } else {
                    maxLength = Math.max(maxLength, this.minecraft.font.width(subtitleoverlaygui$subtitle.getText()));
                }
            }

            maxLength = maxLength + this.minecraft.font.width("<") +
                    this.minecraft.font.width(" ") +
                    this.minecraft.font.width(">") +
                    this.minecraft.font.width(" ");

            for(SubtitleOverlay.Subtitle subtitle: subtitles) {
                Component caption = subtitle.getText();
                Vec3 vector3d4 = subtitle.getLocation().subtract(vector3d).normalize();
                double d0 = -vector3d3.dot(vector3d4);
                double d1 = -vector3d1.dot(vector3d4);
                boolean flag = d1 > 0.5D;
                int halfMaxLength = maxLength / 2;

                int subtitleHeight = this.minecraft.font.lineHeight / 2;
                int subtitleWidth = this.minecraft.font.width(caption);

                int fadeAwayCalculation = (int) floor(clampedLerp(255.0D, 75.0D, (double)((float)(Util.getMillis() - subtitle.getTime()) / 3000.0F)));
                int fadeAway = fadeAwayCalculation << 16 | fadeAwayCalculation << 8 | fadeAwayCalculation;

                int backgroundRed = Config.backgroundRed.get();
                int backgroundGreen = Config.backgroundGreen.get();
                int backgroundBlue = Config.backgroundBlue.get();

                int fontRed = Config.fontRed.get();
                int fontGreen = Config.fontGreen.get();
                int fontBlue = Config.fontBlue.get();

                int backgroundAlpha = Config.backgroundAlpha.get();

                int backgroundColor = ColorConverter.colorToDecimal(backgroundRed, backgroundGreen, backgroundBlue);
                int fontColor = ColorConverter.colorToDecimal(fontRed, fontGreen, fontBlue);

                RenderSystem.pushMatrix();

                Minecraft minecraft = Minecraft.getInstance();
                int resolutionX = minecraft.getWindow().getGuiScaledWidth();
                int resolutionY = minecraft.getWindow().getGuiScaledHeight();

                Enum<?> position = Config.overlayPosition.get();

                int verticalSpacing = 1;
                int horizontalSpacing = 2;
                int subtitleSpacing = 10 * scale.get();
                double xPos = Config.xPosition.get();
                double yPos = Config.yPosition.get();


                    // ... existing switch statement ...
                //if there's any invalid input just show it in the bottom right
                if (position.equals(BOTTOM_CENTER)) {
                    xPos += (double) resolutionX / 2;
                    yPos += (resolutionY - 75) - (captionIndex * subtitleSpacing);
                    log.info("bottom center");
                } else if (position.equals(BOTTOM_LEFT)) {
                    xPos += halfMaxLength + horizontalSpacing;
                    yPos += (resolutionY - 30) - (captionIndex * subtitleSpacing);
                    log.info("bottom left");
                } else if (position.equals(MIDDLE_CENTER)) {
                    xPos += halfMaxLength + horizontalSpacing;
                    yPos += ((double) resolutionY / 2) - (((double) (subtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
                    log.info("center left");
                } else if (position.equals(TOP_LEFT)) {
                    xPos += halfMaxLength + horizontalSpacing;
                    yPos += (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                    log.info("top left");
                } else if (position.equals(TOP_CENTER)) {
                    xPos += (double) resolutionX / 2;
                    yPos += (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                    log.info("top center");
                } else if (position.equals(TOP_RIGHT)) {
                    xPos += resolutionY - halfMaxLength - 2;
                    yPos += (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                    log.info("top right");
                } else if (position.equals(MIDDLE_RIGHT)) {
                    xPos += resolutionX - halfMaxLength - horizontalSpacing;
                    yPos += ((double) resolutionY / 2) - (((double) (subtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
                    log.info("center right");
                } else if (position.equals(BOTTOM_RIGHT)) {
                    xPos += resolutionX - halfMaxLength - 2;
                    yPos += (resolutionY - 30) - (captionIndex * subtitleSpacing);
                    log.info("bottom right");
                } else {
                    //if there's any invalid input just show it in the bottom right
                    xPos += resolutionX - halfMaxLength - 2;
                    yPos += (resolutionY - 30) - (captionIndex * subtitleSpacing);
                    log.info("Invalid Input: " + position);
                }

                xPos = Mth.clamp(xPos, 0, resolutionX - ((double) subtitleWidth / 2));
                yPos = Mth.clamp(yPos, 0, resolutionY - (subtitleHeight));

                log.info("xPos: " + xPos + " yPos: " + yPos);
                RenderSystem.translatef((float) xPos, (float) yPos, 0.0F);

                RenderSystem.scalef(scale.get(), scale.get(), 1.0F);
                log.info("scale: " + scale.get());

                fill(stack, -halfMaxLength - 1, -subtitleHeight - 1, halfMaxLength + 1, subtitleHeight + 1,
                        255 << 24 | backgroundColor);
                RenderSystem.enableBlend();

                if (!flag) {
                    if (d0 > 0.0D) {
                        this.minecraft.font.draw(stack, ">", (float)(halfMaxLength - this.minecraft.font.width(">")), (float)(-subtitleHeight), fadeAway + fontColor);
                    } else if (d0 < 0.0D) {
                        this.minecraft.font.draw(stack, "<", (float)(-halfMaxLength), (float)(-subtitleHeight), fadeAway + fontColor);
                    }
                }

                this.minecraft.font.draw(stack, caption, (float)(-subtitleWidth / 2), (float)(-subtitleHeight), fadeAway + fontColor);
                RenderSystem.popMatrix();
                ++captionIndex;
            }
            RenderSystem.disableBlend();
        }
        RenderSystem.popMatrix();
    }

    @Override
    public void onPlaySound(SoundInstance arg, WeighedSoundEvents arg2) {
        if (arg2.getSubtitle() == null) {
            return;
        }
        Component text = arg2.getSubtitle();
        if (!subtitles.isEmpty()) {
            for (SubtitleOverlay.Subtitle caption : subtitles) {
                if (!caption.getText().equals(text)) continue;
                caption.refresh(new Vec3(arg.getX(), arg.getY(), arg.getZ()));
                return;
            }
        }
        SubtitleOverlay subtitleOverlay = new SubtitleOverlay(minecraft);
        SubtitleOverlay.Subtitle subtitle = subtitleOverlay.new Subtitle(text, new Vec3(arg.getX(), arg.getY(), arg.getZ()));
        subtitles.add(subtitle);
    }
}