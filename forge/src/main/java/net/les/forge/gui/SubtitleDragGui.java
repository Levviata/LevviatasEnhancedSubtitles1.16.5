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
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.client.gui.widget.Slider;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.floor;
import static java.lang.Math.min;
import static net.les.forge.config.Config.*;
import static net.les.forge.gui.SubtitleOverlayGui.lastPosX;
import static net.les.forge.gui.SubtitleOverlayGui.lastPosY;
import static net.minecraft.util.Mth.clampedLerp;

public class SubtitleDragGui extends Screen {
    private Minecraft mc = Minecraft.getInstance();
    private boolean isButtonPressed = false;
    private static final List<SubtitleOverlay.Subtitle> previewSubtitles = Lists.newArrayList();

    static {
        Component component0 = new Component() {
            @Override
            public Style getStyle() {
                return null;
            }

            @Override
            public String getContents() {
                return "Example Subtitle";
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
        };
        Component component1 = new Component() {
            @Override
            public Style getStyle() {
                return null;
            }

            @Override
            public String getContents() {
                return "Big ol' Example Subtitle";
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
        };
        SubtitleOverlay subtitleOverlay = new SubtitleOverlay(Minecraft.getInstance());
        SubtitleOverlay.Subtitle subtitle0 = subtitleOverlay.new Subtitle(component0, new Vec3(0, 0, 0));
        previewSubtitles.add(subtitle0);

        SubtitleOverlay.Subtitle subtitle1 = subtitleOverlay.new Subtitle(component1, new Vec3(0, 0, 0));
        previewSubtitles.add(subtitle1);

    }

    public static boolean isGuiOpen = false;
    private boolean dragging;
    private int lastMouseX;
    private int lastMouseY;
    private boolean initialShowSubtitles;
    private int initialScale;
    private int initialBackgroundAlpha;

    private Logger logger = Logger.getLogger("SubtitleDragGui");

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
        assert minecraft != null;
        Window res = minecraft.getWindow();

        this.addButton(new Button(
                this.width / 2 - 100, // x position
                this.height / 2 - 20,
                200, // width
                20,
                new Component() {
                    @Override
                    public Style getStyle() {
                        return null;
                    }

                    @Override
                    public String getContents() {
                        return TextColor.fromLegacyFormat(ChatFormatting.YELLOW) + "Mod Status: " +
                                (showSubtitles.get() ?
                                        TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN) + "Enabled" :
                                        TextColor.fromLegacyFormat(ChatFormatting.DARK_RED) + "Disabled");
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
                }, button -> {
            showSubtitles.set(!showSubtitles.get());
            SPEC.save();
        }));

        Button scale = new Slider(
                this.width / 2 - 100, // x position
                this.height / 2 - 20,
                new Component() {
                    @Override
                    public Style getStyle() {
                        return null;
                    }

                    @Override
                    public String getContents() {
                        return "Scale: " + Config.scale.get();
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
                },
                0.5D,
                10,
                initialScale,
                button -> {
                },
                new Slider.ISlider() {
                    @Override
                    public void onChangeSliderValue(Slider slider) {
                        Config.scale.set((int) slider.getValue());
                        SPEC.save();
                    }
                }
        );

        scale.setWidth(200);
        buttons.add(scale);

        Button alpha = new Slider(
                this.width / 2 - 100, // x position
                this.height / 2 - 40,
                new Component() {
                    @Override
                    public Style getStyle() {
                        return null;
                    }

                    @Override
                    public String getContents() {
                        return "Alpha: " + Config.scale.get();
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
                },
                0.5D,
                10,
                initialScale,
                button -> {
                },
                new Slider.ISlider() {
                    @Override
                    public void onChangeSliderValue(Slider slider) {
                        Config.scale.set((int) slider.getValue());
                        SPEC.save();
                    }
                }
        );

        alpha.setWidth(200);
        buttons.add(alpha);

        this.addButton(new Button(
                this.width / 2 - 100, // x position
                this.height / 2 - 20,
                200, // width
                20,
                new Component() {
                    @Override
                    public Style getStyle() {
                        return null;
                    }

                    @Override
                    public String getContents() {
                        return TextColor.fromLegacyFormat(ChatFormatting.YELLOW) + "Reset Values To Default: ";
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
                }, button -> {
            button.setMessage(new Component() {
                @Override
                public Style getStyle() {
                    return null;
                }

                @Override
                public String getContents() {
                    return "Reset Values To Default";
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

            // Reset values to default
            int xPos;
            int yPos;
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

            this.buttons.clear();
            initGui();
        }));

    }

        @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (AbstractWidget guiButton : buttons) {
                if (guiButton.isMouseOver(mouseX, mouseY)) return true;
            }
            this.dragging = true;
            this.lastMouseX = (int) mouseX;
            this.lastMouseY = (int) mouseY;
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
            int diff = (int) (mouseX - this.lastMouseX);
            int xPos = xPosition.get();
            int yPos =  xPosition.get();
            xPos = xPos + diff;
            xPosition.set(xPos);
            yPos = (int) (yPos + (mouseY - this.lastMouseY));
            Config.yPosition.set(yPos);
            this.lastMouseX = (int) mouseX;
            this.lastMouseY = (int) mouseY;
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        int captionIndex = 0;
        int maxLength = 0;
        Iterator<SubtitleOverlay.Subtitle> iterator = previewSubtitles.iterator();

        while(iterator.hasNext()) {
            SubtitleOverlay.Subtitle subtitleoverlaygui$subtitle = iterator.next();
            assert minecraft != null;
            maxLength = Math.max(maxLength, minecraft.font.width(previewSubtitles.get(1).getText()));
        }
        if (showSubtitles.get()) {


        for(SubtitleOverlay.Subtitle subtitle : previewSubtitles) {
            Component caption = subtitle.getText();

            int halfMaxLength = maxLength / 2;

            assert this.minecraft != null;
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
            int resolutionX = minecraft.getWindow().getGuiScaledHeight();
            int resolutionY = minecraft.getWindow().getGuiScaledWidth();

            String position = Config.overlayPosition.toString();

            int verticalSpacing = 1;
            int horizontalSpacing = 2;
            int subtitleSpacing = 10 * scale.get();
            int xPos = Config.xPosition.get();
            int yPos = Config.yPosition.get();
            Logger log = Logger.getLogger("SubtitleConfigGui");

            // ... existing switch statement ...
            switch (position) {
                case "BOTTOM_CENTER":
                    xPos += resolutionX / 2;
                    yPos += (resolutionY - 75) - (captionIndex * subtitleSpacing);
                    log.info("bottom center");
                    break;
                case "BOTTOM_LEFT":
                    xPos += halfMaxLength + horizontalSpacing;
                    yPos += (resolutionY - 30) - (captionIndex * subtitleSpacing);
                    log.info("bottom left");
                    break;
                case "CENTER_LEFT":
                    xPos += halfMaxLength + horizontalSpacing;
                    yPos += (resolutionY / 2) - (((previewSubtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
                    log.info("center left");
                    break;
                case "TOP_LEFT":
                    xPos += halfMaxLength + horizontalSpacing;
                    yPos += (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                    log.info("top left");
                    break;
                case "TOP_CENTER":
                    xPos += resolutionX / 2;
                    yPos += (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                    log.info("top center");
                    break;
                case "TOP_RIGHT":
                    xPos += resolutionY - halfMaxLength - 2;
                    yPos += (captionIndex * subtitleSpacing + 5 + verticalSpacing);
                    log.info("top right");
                    break;
                case "CENTER_RIGHT":
                    xPos += resolutionX - halfMaxLength - horizontalSpacing;
                    yPos += (resolutionY / 2) - (((previewSubtitles.size() - 1) / 2) - captionIndex) * subtitleSpacing;
                    log.info("center right");
                    break;
                default: //if there's any invalid input just show it in the bottom right
                    xPos += resolutionX - halfMaxLength - 2;
                    yPos += (resolutionY - 30) - (captionIndex * subtitleSpacing);
                    log.info("bottom right");
                    break;
            }

            xPos = Mth.clamp(xPos, 0, resolutionX - (subtitleWidth / 2));
            yPos = Mth.clamp(yPos, 0, resolutionY - (subtitleHeight));

            log.info("xPos: " + xPos + " yPos: " + yPos);
            RenderSystem.translatef(xPos, yPos, 0.0F);

            RenderSystem.scalef(scale.get(), scale.get(), 1.0F);

            fill(stack, -halfMaxLength - 1, -subtitleHeight - 1, halfMaxLength + 1, subtitleHeight + 1,
                    255 << 24 | backgroundColor);

            RenderSystem.enableBlend();

            this.minecraft.font.draw(stack, caption, (float)(-subtitleWidth / 2), (float)(-subtitleHeight), fadeAway + fontColor);
            RenderSystem.popMatrix();
            ++captionIndex;
        }
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        super.render(stack, mouseX, mouseY, partialTicks);
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
