package com.casadedios.backend.common.export.theme;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class ExcelStyleFactory {

    private ExcelStyleFactory() {}

    public static CellStyle createHeaderStyle(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 12);

        style.setFont(font);
        style.setFillForegroundColor(ExcelColorPalette.PRIMARY);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // Usar los colores RGBA para los bordes
        style.setBottomBorderColor(ExcelColorPalette.OUTLINE);
        style.setTopBorderColor(ExcelColorPalette.OUTLINE);
        style.setLeftBorderColor(ExcelColorPalette.OUTLINE);
        style.setRightBorderColor(ExcelColorPalette.OUTLINE);

        return style;
    }

    public static CellStyle createRowStyleLight(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(ExcelColorPalette.ON_SURFACE);

        style.setFont(font);
        style.setFillForegroundColor(ExcelColorPalette.SURFACE_CONTAINER_LOWEST);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.HAIR);
        style.setBorderTop(BorderStyle.HAIR);
        style.setBorderLeft(BorderStyle.HAIR);
        style.setBorderRight(BorderStyle.HAIR);

        // Usar los colores RGBA para los bordes
        style.setBottomBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setTopBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setLeftBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setRightBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setWrapText(true);

        return style;
    }

    public static CellStyle createRowStyleDark(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(ExcelColorPalette.ON_SURFACE);


        style.setFont(font);
        style.setFillForegroundColor(ExcelColorPalette.SURFACE_CONTAINER_LOW);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.HAIR);
        style.setBorderTop(BorderStyle.HAIR);
        style.setBorderLeft(BorderStyle.HAIR);
        style.setBorderRight(BorderStyle.HAIR);

        // Usar los colores RGBA para los bordes
        style.setBottomBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setTopBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setLeftBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setRightBorderColor(ExcelColorPalette.OUTLINE_VARIANT);

        style.setWrapText(true);

        return style;
    }

    public static CellStyle createDateStyleLight(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) createRowStyleLight(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    public static CellStyle createDateStyleDark(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) createRowStyleDark(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    public static CellStyle createBooleanStyleLight(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) createRowStyleLight(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(ExcelColorPalette.PRIMARY);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle createBooleanStyleDark(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) createRowStyleDark(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(ExcelColorPalette.PRIMARY);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }
}
