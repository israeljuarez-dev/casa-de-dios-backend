package com.casadedios.backend.modules.retreat.export;

import com.casadedios.backend.common.export.service.ExcelExportService;
import com.casadedios.backend.common.export.theme.ExcelStyleFactory;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RetreatAttendeesExcelExporter implements ExcelExportService {

    private final String retreatName;
    private final List<RetreatEnrollmentResponseDto> enrollments;

    @Override
    public ByteArrayOutputStream export() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Asistentes");
            sheet.getPrintSetup().setLeftToRight(true);

            createHeader(sheet, workbook);
            fillData(sheet, workbook);
            autoSizeColumns(sheet);
            sheet.createFreezePane(0, 1);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            log.info("Reporte de {} asistentes del encuentro '{}' generado exitosamente", enrollments.size(), retreatName);

            return outputStream;
        }
    }

    @Override
    public String getFileName() {
        return "Asistentes_" + retreatName.replaceAll("\\s+", "_");
    }

    private void createHeader(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = ExcelStyleFactory.createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);
        String[] headers = {"N°", "Nombres", "Apellidos", "Celular"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillData(Sheet sheet, Workbook workbook) {
        CellStyle rowLight = ExcelStyleFactory.createRowStyleLight(workbook);
        CellStyle rowDark = ExcelStyleFactory.createRowStyleDark(workbook);

        int rowNum = 1;
        boolean alternate = false;
        for (RetreatEnrollmentResponseDto enrollment : enrollments) {
            Row row = sheet.createRow(rowNum);
            CellStyle style = alternate ? rowDark : rowLight;

            addCell(row, 0, rowNum, style);
            addCell(row, 1, enrollment.firstName(), style);
            addCell(row, 2, enrollment.lastName(), style);
            addCell(row, 3, (enrollment.phoneCodeNumber() != null ? enrollment.phoneCodeNumber() : "") + " " + enrollment.phoneNumber(), style);

            rowNum++;
            alternate = !alternate;
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

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 800);
        }
    }
}