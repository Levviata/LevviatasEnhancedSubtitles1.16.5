package net.levviata.les.forge.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.levviata.les.forge.utils.Vector3d;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.floor;
import static net.minecraft.util.Mth.clampedLerp;

public class SubtitleOverlayGui extends Gui implements SoundEventListener
{
    private final Minecraft minecraft;
    private boolean isListening;
    private static List<SubtitleOverlay.Subtitle> subtitles = Lists.newArrayList();
    public SubtitleOverlayGui(Minecraft minecraft) {
        super(minecraft);
        this.minecraft = minecraft;
    }
    static {
        Field subtitlesField = null;
        try {
            subtitlesField = SubtitleOverlay.class.getDeclaredField("field_184070_f");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        subtitlesField.setAccessible(true);
        try {
            subtitles = (List<SubtitleOverlay.Subtitle>) subtitlesField.get(new SubtitleOverlay(Minecraft.getInstance()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) throws IllegalAccessException, NoSuchFieldException {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PoseStack stack = event.getMatrixStack();
            float partialTicks = mc.getDeltaFrameTime();

            if (mc.options.showSubtitles && !subtitles.isEmpty()) {
                mc.getSoundManager().removeListener(new SubtitleOverlay(mc));
                render(stack);
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

        if (this.isListening && !this.subtitles.isEmpty()) {
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

                int i1 = 9;
                int SubtitleHeight = i1 / 2;

                int subtitleWidth = this.minecraft.font.width(caption);
                int fadeAwayCalculation = (int) floor(clampedLerp(255.0D, 75.0D, (double)((float)(Util.getMillis() - subtitle.getTime()) / 3000.0F)));
                int fadeAway = fadeAwayCalculation << 16 | fadeAwayCalculation << 8 | fadeAwayCalculation;
                RenderSystem.pushMatrix();

                RenderSystem.translatef((float)this.minecraft.getWindow().getGuiScaledWidth() - (float) halfMaxLength - 2.0F, (float)(this.minecraft.getWindow().getGuiScaledHeight() - 30) - (float) (captionIndex * (i1 + 1)), 0.0F);

                RenderSystem.scalef(1.0F, 1.0F, 1.0F);

                fill(stack, -halfMaxLength - 1, -SubtitleHeight - 1, halfMaxLength + 1, SubtitleHeight + 1, this.minecraft.options.getBackgroundColor(0.8F));
                RenderSystem.enableBlend();

                if (!flag) {
                    if (d0 > 0.0D) {
                        this.minecraft.font.draw(stack, ">", (float)(halfMaxLength - this.minecraft.font.width(">")), (float)(-SubtitleHeight), fadeAway + -16777216);
                    } else if (d0 < 0.0D) {
                        this.minecraft.font.draw(stack, "<", (float)(-halfMaxLength), (float)(-SubtitleHeight), fadeAway + -16777216);
                    }
                }

                this.minecraft.font.draw(stack, caption, (float)(-subtitleWidth / 2), (float)(-SubtitleHeight), fadeAway + -16777216);
                RenderSystem.popMatrix();
                ++captionIndex;
            }

            RenderSystem.disableBlend();
            RenderSystem.popMatrix();
        }
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

