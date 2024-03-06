package net.les.forge.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.les.forge.config.Config;
import net.les.forge.util.ColorConverter;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.floor;
import static java.lang.Math.min;
import static net.les.forge.config.Config.*;
import static net.les.forge.config.Config.OverlayPosition.*;
import static net.les.forge.config.Config.OverlayPosition.BOTTOM_RIGHT;
import static net.les.forge.gui.SubtitleOverlayGui.lastPosX;
import static net.les.forge.gui.SubtitleOverlayGui.lastPosY;
import static net.minecraft.util.Mth.clampedLerp;

public class SubtitleDragGui extends Screen {
    private boolean isButtonPressed = false;
    private static final List<SubtitleOverlay.Subtitle> previewSubtitles = Lists.newArrayList();

    static {

        Component component0 = new TextComponent("Example Subtitle");
        Component component1 = new TextComponent("Bigggggggg ol' Example Subtitle");
        SubtitleOverlay subtitleOverlay = new SubtitleOverlay(Minecraft.getInstance());
        SubtitleOverlay.Subtitle subtitle0 = subtitleOverlay.new Subtitle(component0, new Vec3(0, 0, 0));
        previewSubtitles.add(subtitle0);

        SubtitleOverlay.Subtitle subtitle1 = subtitleOverlay.new Subtitle(component1, new Vec3(0, 0, 0));
        previewSubtitles.add(subtitle1);

    }

    public static boolean isGuiOpen = false;
    private boolean dragging;
    private double lastMouseX;
    private double lastMouseY;
    private boolean initialShowSubtitles;
    private int initialScale;
    private int initialBackgroundAlpha;
    private Button scaleButton;
    private Button alphaButton;
    private Button showSubtitlesButton;
    private Button resetValues;

    private final Logger logger = Logger.getLogger("SubtitleDragGui");

    public SubtitleDragGui(Component arg) {
        super(arg);
    }


    public void initGui() {
        super.init();
        buttons.clear();
        //Store values for later
        initialShowSubtitles = showSubtitles.get();
        initialScale = scale.get();
        initialBackgroundAlpha = backgroundAlpha.get();
        Window res =  Minecraft.getInstance().getWindow();


        Component modStatusText = new TextComponent("Mod Status: ")
                .append(showSubtitles.get()
                        ? new TextComponent("Enabled").withStyle(ChatFormatting.DARK_GREEN)
                        : new TextComponent("Disabled").withStyle(ChatFormatting.DARK_RED)
                );
        this.addButton(new Button(
                Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 100,
                Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 20,
                200,
                20,
                modStatusText,
                button -> {
                    showSubtitles.set(!showSubtitles.get());
                    SPEC.save();
                    button.setMessage(new TextComponent("Mod Status: ")
                            .append(showSubtitles.get()
                                    ? new TextComponent("Enabled").withStyle(ChatFormatting.DARK_GREEN)
                                    : new TextComponent("Disabled").withStyle(ChatFormatting.DARK_RED)
                            ));
                }
        ));
        Component scaleText = new TextComponent("Scale: ").append(String.valueOf(Config.scale.get()));

        scaleButton = new Slider(
                Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 100,
                Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 40,
                scaleText,
                0.5D,
                10,
                initialScale,
                button -> {
                    button.setMessage(new TextComponent("Scale: ").append(String.valueOf(Config.scale.get())));
                },
                new Slider.ISlider() {
                    @Override
                    public void onChangeSliderValue(Slider slider) {
                        Config.scale.set((int) slider.getValue());
                        SPEC.save();
                    }
                }
        );

        scaleButton.setWidth(200);
        buttons.add(scaleButton);

        Component alphaText = new TextComponent("Alpha: ").append(String.valueOf(backgroundAlpha.get()));
        alphaButton = new Slider(
                Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 100,
                Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 60,
                alphaText,
                0.5D,
                10,
                initialScale,
                button -> {
                button.setMessage(new TextComponent("Alpha: ").append(String.valueOf(backgroundAlpha.get())));
                    },
                new Slider.ISlider() {
                    @Override
                    public void onChangeSliderValue(Slider slider) {
                        Config.backgroundAlpha.set((int) slider.getValue());
                        SPEC.save();
                    }
                }
        );

        alphaButton.setWidth(200);
        buttons.add(alphaButton);

        Component resetText = new TextComponent("Reset Values To Default");
        resetValues = new Button(
                Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 100,
                Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 80,
                200, // width
                20,
                resetText,
                button -> {
                    button.setMessage(new TextComponent("Reset Values To Default"));
                    double xPos;
                    double yPos;
                    xPos = lastPosX;
                    yPos = lastPosY;
                    xPosition.set(xPos);
                    yPosition.set(yPos);
                    int setAlpha = backgroundAlpha.get();
                    boolean setShow = showSubtitles.get();
                    int setScale = Config.scale.get();

                    backgroundAlpha.set(setAlpha);
                    showSubtitles.set(setShow);
                    Config.scale.set(setScale);
                    SPEC.save();
                }
        );

        this.addButton(resetValues);
    }

        @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (AbstractWidget guiButton : buttons) {
                if (guiButton.isMouseOver(mouseX, mouseY)) return true;
                if (guiButton == resetValues)
                {
                    guiButton.setMessage(new TextComponent("Reset Values To Default"));
                    double xPos;
                    double yPos;
                    xPos = lastPosX;
                    yPos = lastPosY;
                    xPosition.set(xPos);
                    yPosition.set(yPos);
                    int setAlpha = backgroundAlpha.get();
                    boolean setShow = showSubtitles.get();
                    int setScale = Config.scale.get();

                    backgroundAlpha.set(setAlpha);
                    showSubtitles.set(setShow);
                    Config.scale.set(setScale);
                    SPEC.save();
                }
            }
            this.dragging = true;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
            return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int action) {
        super.mouseReleased(mouseX, mouseY, action);
        this.dragging = false;
        SPEC.save();
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        if (this.dragging) {
            double diff = (mouseX - this.lastMouseX);
            double xPos = xPosition.get();
            double yPos =  yPosition.get();
            xPos = xPos + diff;
            xPosition.set(xPos);
            yPos = (yPos + (mouseY - this.lastMouseY));
            Config.yPosition.set(yPos);
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
        }
    }
    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);

        int captionIndex = 0;
        int maxLength = 0;
        Iterator<SubtitleOverlay.Subtitle> iterator = previewSubtitles.iterator();

        while(iterator.hasNext()) {
            SubtitleOverlay.Subtitle subtitleoverlaygui$subtitle = iterator.next();
            maxLength = Math.max(maxLength, Minecraft.getInstance().font.width(previewSubtitles.get(1).getText()));
        }

        maxLength = maxLength + Minecraft.getInstance().font.width("<") +
                Minecraft.getInstance().font.width(" ") +
                Minecraft.getInstance().font.width(">") +
                Minecraft.getInstance().font.width(" ");
        if (showSubtitles.get()) {
        for(SubtitleOverlay.Subtitle subtitle : previewSubtitles) {
            Component caption = subtitle.getText();

            int halfMaxLength = maxLength / 2;

            int subtitleHeight = Minecraft.getInstance().font.lineHeight / 2;
            int subtitleWidth =  Minecraft.getInstance().font.width(caption);

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

            int resolutionX = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int resolutionY = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            Enum<?> position = Config.overlayPosition.get();

            int verticalSpacing = 1;
            int horizontalSpacing = 2;
            int subtitleSpacing = 10 * scale.get();
            double xPos = Config.xPosition.get();
            double yPos = Config.yPosition.get();

            Logger log = Logger.getLogger("SubtitleOverlayDragGui");
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
                yPos += ((double) resolutionY / 2) - (((double) (previewSubtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
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
                yPos += ((double) resolutionY / 2) - (((double) (previewSubtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
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

            fill(stack, -halfMaxLength - 1, -subtitleHeight - 1, halfMaxLength + 1, subtitleHeight + 1,
                    255 << 24 | backgroundColor);

            RenderSystem.enableBlend();

            Minecraft.getInstance().font.draw(stack, caption, (float)(-subtitleWidth / 2), (float)(-subtitleHeight), fadeAway + fontColor);
            RenderSystem.popMatrix();
            ++captionIndex;
        }
        RenderSystem.disableBlend();
        }
    }

    @Override
    public void onClose() {
        if (initialShowSubtitles != showSubtitles.get() ||
                initialScale != scale.get() ||
                initialBackgroundAlpha != backgroundAlpha.get()) {

            showSubtitles.set(showSubtitles.get());
            scale.set(scale.get());
            backgroundAlpha.set(backgroundAlpha.get());
            SPEC.save();
        }
        isGuiOpen = false;
        super.onClose();
    }


    @Override
    public boolean isPauseScreen() {
        return false  ;
    }
}
