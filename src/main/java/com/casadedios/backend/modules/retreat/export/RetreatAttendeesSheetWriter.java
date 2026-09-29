package com.casadedios.backend.modules.retreat.export;

import com.casadedios.backend.common.export.theme.ExcelStyleFactory;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

final class RetreatAttendeesSheetWriter {

    private RetreatAttendeesSheetWriter() {}

    static int write(Sheet sheet, Workbook workbook, int startRow, List<RetreatEnrollmentResponseDto> enrollments, boolean withSectionTitle) {
        CellStyle sectionStyle = ExcelStyleFactory.createHeaderStyle(workbook);
        CellStyle rowLight = ExcelStyleFactory.createRowStyleLight(workbook);
        CellStyle rowDark = ExcelStyleFactory.createRowStyleDark(workbook);

        int currentRow = startRow;

        if (withSectionTitle) {
            Row sectionRow = sheet.createRow(currentRow);
            Cell sectionCell = sectionRow.createCell(0);
            sectionCell.setCellValue("ASISTENTES");
            sectionCell.setCellStyle(sectionStyle);
            currentRow++;
        }

        Row headerRow = sheet.createRow(currentRow);
        writeHeaders(headerRow, sectionStyle);
        currentRow++;

        int rowNum = currentRow;
        boolean alternate = false;

        for (RetreatEnrollmentResponseDto enrollment : enrollments) {
            Row row = sheet.createRow(rowNum);
            CellStyle style = alternate ? rowDark : rowLight;

            addCell(row, 0, rowNum - currentRow + 1, style);
            addCell(row, 1, enrollment.firstName(), style);
            addCell(row, 2, enrollment.lastName(), style);
            addCell(row, 3, formatPhone(enrollment.phoneCodeNumber(), enrollment.phoneNumber()), style);

            Cell paymentCell = row.createCell(4);
            paymentCell.setCellValue(RetreatPaymentCellStyler.paymentLabel(enrollment));
            paymentCell.setCellStyle(RetreatPaymentCellStyler.styleFor(workbook, enrollment.paymentStatus()));

            rowNum++;
            alternate = !alternate;
        }

        return rowNum;
    }

    private static void writeHeaders(Row headerRow, CellStyle style) {
        String[] headers = {"N°", "Nombres", "Apellidos", "Celular", "Estado de pago"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private static void addCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private static String formatPhone(String code, String number) {
        if (number == null) return "";
        return (code != null ? code : "") + " " + number;
    }
}
