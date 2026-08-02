package com.casadedios.backend.common.export.theme;

import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelColorPalette {

    private ExcelColorPalette() {}

    public static final XSSFColor PRIMARY = new XSSFColor(new byte[]{(byte) 83, (byte) 97, (byte) 56}, null);
    public static final XSSFColor ON_PRIMARY = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null);
    public static final XSSFColor PRIMARY_CONTAINER = new XSSFColor(new byte[]{(byte) 107, (byte) 122, (byte) 79}, null);
    public static final XSSFColor ON_PRIMARY_CONTAINER = new XSSFColor(new byte[]{(byte) 250, (byte) 255, (byte) 232}, null);
    public static final XSSFColor PRIMARY_FIXED = new XSSFColor(new byte[]{(byte) 216, (byte) 233, (byte) 181}, null);
    public static final XSSFColor PRIMARY_FIXED_DIM = new XSSFColor(new byte[]{(byte) 188, (byte) 205, (byte) 155}, null);
    public static final XSSFColor ON_PRIMARY_FIXED = new XSSFColor(new byte[]{(byte) 19, (byte) 31, (byte) 1}, null);
    public static final XSSFColor ON_PRIMARY_FIXED_VARIANT = new XSSFColor(new byte[]{(byte) 62, (byte) 75, (byte) 37}, null);

    public static final XSSFColor SECONDARY = new XSSFColor(new byte[]{(byte) 65, (byte) 98, (byte) 118}, null);
    public static final XSSFColor ON_SECONDARY = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null);
    public static final XSSFColor SECONDARY_CONTAINER = new XSSFColor(new byte[]{(byte) 193, (byte) 229, (byte) 252}, null);
    public static final XSSFColor ON_SECONDARY_CONTAINER = new XSSFColor(new byte[]{(byte) 69, (byte) 103, (byte) 123}, null);
    public static final XSSFColor SECONDARY_FIXED = new XSSFColor(new byte[]{(byte) 196, (byte) 231, (byte) 255}, null);
    public static final XSSFColor SECONDARY_FIXED_DIM = new XSSFColor(new byte[]{(byte) 168, (byte) 203, (byte) 226}, null);
    public static final XSSFColor ON_SECONDARY_FIXED = new XSSFColor(new byte[]{(byte) 0, (byte) 30, (byte) 44}, null);
    public static final XSSFColor ON_SECONDARY_FIXED_VARIANT = new XSSFColor(new byte[]{(byte) 40, (byte) 75, (byte) 94}, null);

    public static final XSSFColor TERTIARY = new XSSFColor(new byte[]{(byte) 87, (byte) 95, (byte) 70}, null);
    public static final XSSFColor ON_TERTIARY = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null);
    public static final XSSFColor TERTIARY_CONTAINER = new XSSFColor(new byte[]{(byte) 111, (byte) 120, (byte) 93}, null);
    public static final XSSFColor ON_TERTIARY_CONTAINER = new XSSFColor(new byte[]{(byte) 250, (byte) 255, (byte) 232}, null);
    public static final XSSFColor TERTIARY_FIXED = new XSSFColor(new byte[]{(byte) 221, (byte) 230, (byte) 198}, null);
    public static final XSSFColor TERTIARY_FIXED_DIM = new XSSFColor(new byte[]{(byte) 193, (byte) 202, (byte) 171}, null);
    public static final XSSFColor ON_TERTIARY_FIXED = new XSSFColor(new byte[]{(byte) 23, (byte) 30, (byte) 10}, null);
    public static final XSSFColor ON_TERTIARY_FIXED_VARIANT = new XSSFColor(new byte[]{(byte) 66, (byte) 74, (byte) 50}, null);

    public static final XSSFColor SURFACE = new XSSFColor(new byte[]{(byte) 249, (byte) 250, (byte) 247}, null);
    public static final XSSFColor SURFACE_BRIGHT = new XSSFColor(new byte[]{(byte) 249, (byte) 250, (byte) 247}, null);
    public static final XSSFColor SURFACE_DIM = new XSSFColor(new byte[]{(byte) 217, (byte) 218, (byte) 216}, null);
    public static final XSSFColor SURFACE_CONTAINER_LOWEST = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null);
    public static final XSSFColor SURFACE_CONTAINER_LOW = new XSSFColor(new byte[]{(byte) 243, (byte) 244, (byte) 241}, null);
    public static final XSSFColor SURFACE_CONTAINER = new XSSFColor(new byte[]{(byte) 237, (byte) 238, (byte) 235}, null);
    public static final XSSFColor SURFACE_CONTAINER_HIGH = new XSSFColor(new byte[]{(byte) 231, (byte) 232, (byte) 230}, null);
    public static final XSSFColor SURFACE_CONTAINER_HIGHEST = new XSSFColor(new byte[]{(byte) 226, (byte) 227, (byte) 224}, null);
    public static final XSSFColor ON_SURFACE = new XSSFColor(new byte[]{(byte) 25, (byte) 28, (byte) 27}, null);
    public static final XSSFColor ON_SURFACE_VARIANT = new XSSFColor(new byte[]{(byte) 69, (byte) 72, (byte) 62}, null);
    public static final XSSFColor SURFACE_VARIANT = new XSSFColor(new byte[]{(byte) 226, (byte) 227, (byte) 224}, null);

    public static final XSSFColor OUTLINE = new XSSFColor(new byte[]{(byte) 118, (byte) 120, (byte) 109}, null);
    public static final XSSFColor OUTLINE_VARIANT = new XSSFColor(new byte[]{(byte) 198, (byte) 200, (byte) 186}, null);

    public static final XSSFColor ERROR = new XSSFColor(new byte[]{(byte) 186, (byte) 26, (byte) 26}, null);
    public static final XSSFColor ON_ERROR = new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}, null);
    public static final XSSFColor ERROR_CONTAINER = new XSSFColor(new byte[]{(byte) 255, (byte) 218, (byte) 214}, null);
    public static final XSSFColor ON_ERROR_CONTAINER = new XSSFColor(new byte[]{(byte) 147, (byte) 0, (byte) 10}, null);

    public static final XSSFColor BACKGROUND = new XSSFColor(new byte[]{(byte) 249, (byte) 250, (byte) 247}, null);
    public static final XSSFColor ON_BACKGROUND = new XSSFColor(new byte[]{(byte) 25, (byte) 28, (byte) 27}, null);

    public static final XSSFColor INVERSE_SURFACE = new XSSFColor(new byte[]{(byte) 46, (byte) 49, (byte) 47}, null);
    public static final XSSFColor INVERSE_ON_SURFACE = new XSSFColor(new byte[]{(byte) 240, (byte) 241, (byte) 238}, null);
    public static final XSSFColor INVERSE_PRIMARY = new XSSFColor(new byte[]{(byte) 188, (byte) 205, (byte) 155}, null);

    public static final XSSFColor SURFACE_TINT = new XSSFColor(new byte[]{(byte) 85, (byte) 99, (byte) 59}, null);
}
