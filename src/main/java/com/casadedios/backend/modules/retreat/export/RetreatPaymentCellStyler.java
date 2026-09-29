package com.casadedios.backend.modules.retreat.export;

import com.casadedios.backend.common.export.theme.ExcelColorPalette;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import com.casadedios.backend.modules.retreat.enums.PaymentStatusEnum;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

final class RetreatPaymentCellStyler {

    private RetreatPaymentCellStyler() {}

    static CellStyle paidStyle(Workbook workbook) {
        return buildStyle(workbook, ExcelColorPalette.SUCCESS_CONTAINER, ExcelColorPalette.ON_SUCCESS_CONTAINER);
    }

    static CellStyle partialStyle(Workbook workbook) {
        return buildStyle(workbook, ExcelColorPalette.WARNING_CONTAINER, ExcelColorPalette.ON_WARNING_CONTAINER);
    }

    static CellStyle pendingStyle(Workbook workbook) {
        return buildStyle(workbook, ExcelColorPalette.ERROR_CONTAINER, ExcelColorPalette.ON_ERROR_CONTAINER);
    }

    static String paymentLabel(RetreatEnrollmentResponseDto enrollment) {
        return switch (enrollment.paymentStatus()) {
            case PAID -> "Pagado";
            case PENDING -> "No pagado";
            case PARTIAL -> "Falta S/ " + enrollment.amountPending();
        };
    }

    static CellStyle styleFor(Workbook workbook, PaymentStatusEnum status) {
        return switch (status) {
            case PAID -> paidStyle(workbook);
            case PARTIAL -> partialStyle(workbook);
            case PENDING -> pendingStyle(workbook);
        };
    }

    private static CellStyle buildStyle(Workbook workbook, org.apache.poi.xssf.usermodel.XSSFColor bg, org.apache.poi.xssf.usermodel.XSSFColor text) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true);
        font.setColor(text);
        font.setFontHeightInPoints((short) 10);

        style.setFont(font);
        style.setFillForegroundColor(bg);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.HAIR);
        style.setBorderTop(BorderStyle.HAIR);
        style.setBorderLeft(BorderStyle.HAIR);
        style.setBorderRight(BorderStyle.HAIR);
        style.setBottomBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setTopBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setLeftBorderColor(ExcelColorPalette.OUTLINE_VARIANT);
        style.setRightBorderColor(ExcelColorPalette.OUTLINE_VARIANT);

        return style;
    }
}