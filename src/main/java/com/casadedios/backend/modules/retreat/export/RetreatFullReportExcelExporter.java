package com.casadedios.backend.modules.retreat.export;

import com.casadedios.backend.common.export.service.ExcelExportService;
import com.casadedios.backend.common.export.theme.ExcelStyleFactory;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatStaffResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RetreatFullReportExcelExporter implements ExcelExportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final RetreatResponseDto retreat;
    private final List<RetreatEnrollmentResponseDto> enrollments;
    private final List<RetreatStaffResponseDto> staff;

    @Override
    public ByteArrayOutputStream export() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Encuentro");
            sheet.getPrintSetup().setLeftToRight(true);

            int rowNum = writeRetreatInfo(sheet, workbook);
            rowNum = writeStaffSection(sheet, workbook, rowNum + 2);
            RetreatAttendeesSheetWriter.write(sheet, workbook, rowNum + 2, enrollments, true);

            autoSizeColumns(sheet);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            log.info("Reporte completo del encuentro '{}' generado exitosamente", retreat.name());

            return outputStream;
        }
    }

    @Override
    public String getFileName() {
        return "Encuentro_" + retreat.name().replaceAll("\\s+", "_");
    }

    private int writeRetreatInfo(Sheet sheet, Workbook workbook) {
        CellStyle titleStyle = ExcelStyleFactory.createHeaderStyle(workbook);
        CellStyle valueStyle = ExcelStyleFactory.createRowStyleLight(workbook);

        addLabelValueRow(sheet, 0, "Nombre del encuentro", retreat.name(), titleStyle, valueStyle);
        addLabelValueRow(sheet, 1, "Fecha de inicio", retreat.startDate().atZone(ZoneId.systemDefault()).format(DATE_FORMAT), titleStyle, valueStyle);
        addLabelValueRow(sheet, 2, "Fecha de fin", retreat.endDate().atZone(ZoneId.systemDefault()).format(DATE_FORMAT), titleStyle, valueStyle);

        return 2;
    }

    private void addLabelValueRow(Sheet sheet, int rowIndex, String label, String value, CellStyle labelStyle, CellStyle valueStyle) {
        Row row = sheet.createRow(rowIndex);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(valueStyle);
    }

    private int writeStaffSection(Sheet sheet, Workbook workbook, int startRow) {
        CellStyle sectionStyle = ExcelStyleFactory.createHeaderStyle(workbook);
        CellStyle rowLight = ExcelStyleFactory.createRowStyleLight(workbook);
        CellStyle rowDark = ExcelStyleFactory.createRowStyleDark(workbook);

        Row sectionRow = sheet.createRow(startRow);
        Cell sectionCell = sectionRow.createCell(0);
        sectionCell.setCellValue("STAFF");
        sectionCell.setCellStyle(sectionStyle);

        int headerRowNum = startRow + 1;
        Row headerRow = sheet.createRow(headerRowNum);
        writeHeaders(headerRow, sectionStyle, "N°", "Nombres", "Apellidos", "Celular");

        int rowNum = headerRowNum + 1;
        boolean alternate = false;
        for (RetreatStaffResponseDto member : staff) {
            Row row = sheet.createRow(rowNum);
            CellStyle style = alternate ? rowDark : rowLight;

            addCell(row, 0, rowNum - headerRowNum, style);
            addCell(row, 1, member.firstName(), style);
            addCell(row, 2, member.lastName(), style);
            addCell(row, 3, formatPhone(member.phoneCodeNumber(), member.phoneNumber()), style);

            rowNum++;
            alternate = !alternate;
        }

        return rowNum;
    }

    private void writeHeaders(Row headerRow, CellStyle style, String... headers) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void addCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private String formatPhone(String code, String number) {
        if (number == null) return "";
        return (code != null ? code : "") + " " + number;
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 800);
        }
    }
}