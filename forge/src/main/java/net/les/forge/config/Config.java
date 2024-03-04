package net.les.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

import static net.les.forge.config.Config.OverlayPosition.BOTTOM_LEFT;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> example_integer;
    public static final ForgeConfigSpec.ConfigValue<String> example_string;
    public static final ForgeConfigSpec.ConfigValue<Integer> xPosition;
    public static final ForgeConfigSpec.ConfigValue<Integer> yPosition;
    public static final ForgeConfigSpec.EnumValue<Config.OverlayPosition> overlayPosition;
    public static final ForgeConfigSpec.ConfigValue<Integer> backgroundRed;
    public static final ForgeConfigSpec.ConfigValue<Integer> backgroundGreen;
    public static final ForgeConfigSpec.ConfigValue<Integer> backgroundBlue;
    public static final ForgeConfigSpec.ConfigValue<Integer> backgroundAlpha;
    public static final ForgeConfigSpec.ConfigValue<Integer> fontRed;
    public static final ForgeConfigSpec.ConfigValue<Integer> fontGreen;
    public static final ForgeConfigSpec.ConfigValue<Integer> fontBlue;
    public static final ForgeConfigSpec.ConfigValue<Integer> scale;
    public static final ForgeConfigSpec.ConfigValue<Boolean> showSubtitles;
    public static enum OverlayPosition {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    };



    static {
        BUILDER.push("Levviata's Enhanced Subtitles Configuration");

        example_integer = BUILDER.comment("This is an integer. Default value is 3.").define("Example Integer", 3);
        example_string = BUILDER.comment("This is a string. Default value is \"Cy4\".").define("Example String", "Cy4");

        xPosition = BUILDER.comment("This is the x offset of the subtitle. Default value is 0.").defineInRange("X Position", 0, -1000, 1000);
        yPosition = BUILDER.comment("This is the y offset of the subtitle. Default value is 0.").defineInRange("Y Position", 0, -1000, 1000);

        overlayPosition = BUILDER.comment("This is the position of the overlay. Default value is BOTTOM_RIGHT. \nValid values are TOP_LEFT, TOP_CENTER, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT.")
                .defineEnum("Position", Enum.valueOf(OverlayPosition.class, BOTTOM_LEFT.toString()));
        scale = BUILDER.comment("This is the scale of the subtitles. Default value is 1.").defineInRange("Scale", 1, 0, 10);
        showSubtitles = BUILDER.comment("Whether to show the subtitles or not. Default value is true.").define("Show Subtitles", true);

        backgroundRed = BUILDER.comment("This is the red value of the background. Default value is 0.").defineInRange("Background Red", 0, 0, 255);
        backgroundGreen = BUILDER.comment("This is the green value of the background. Default value is 0.").defineInRange("Background Green", 0, 0, 255);
        backgroundBlue = BUILDER.comment("This is the blue value of the background. Default value is 0.").defineInRange("Background Blue", 0, 0, 255);
        backgroundAlpha = BUILDER.comment("This is the alpha value of the background. Default value is 0.").defineInRange("Background Alpha", 255, 0, 255);

        fontRed = BUILDER.comment("This is the red value of the font. Default value is 255.").defineInRange("Font Red", 255, 0, 255);
        fontGreen = BUILDER.comment("This is the green value of the font. Default value is 255.").defineInRange("Font Green", 255, 0, 255);
        fontBlue = BUILDER.comment("This is the blue value of the font. Default value is 255.").defineInRange("Font Blue", 255, 0, 255);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}