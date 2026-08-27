package com.casadedios.backend.cellgroup.export;

import com.casadedios.backend.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.common.export.service.ExcelExportService;
import com.casadedios.backend.common.export.theme.ExcelStyleFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CellGroupExcelExporter implements ExcelExportService {

    private static final String SHEET_NAME = "Células";

    private final List<CellGroupResponseDto> cellGroups;

    @Override
    public ByteArrayOutputStream export() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            sheet.getPrintSetup().setLeftToRight(true);

            createHeader(sheet, workbook);
            fillData(sheet, workbook);
            autoSizeColumns(sheet);
            freezeHeader(sheet);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            log.info("Reporte de {} células generado exitosamente", cellGroups.size());

            return outputStream;
        }
    }

    @Override
    public String getFileName() {
        return "Células_" + LocalDate.now(java.time.ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private void createHeader(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = ExcelStyleFactory.createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(28);

        String[] headers = {
                "N°",
                "Nombre Célula",
                "Líder",
                "Día de Reunión",
                "Hora de Reunión",
                "Lugar",
                "Total Miembros"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillData(Sheet sheet, Workbook workbook) {
        CellStyle rowLight = ExcelStyleFactory.createRowStyleLight(workbook);
        CellStyle rowDark  = ExcelStyleFactory.createRowStyleDark(workbook);

        int rowNum    = 1;
        boolean alternate = false;

        for (CellGroupResponseDto cellGroup : cellGroups) {
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(22);

            CellStyle currentRow = alternate ? rowDark : rowLight;

            fillCellGroupRow(row, cellGroup, rowNum, currentRow);

            rowNum++;
            alternate = !alternate;
        }
    }

    private void fillCellGroupRow(Row row, CellGroupResponseDto cellGroup, int rowNumber, CellStyle style) {
        addCell(row, 0, rowNumber, style);
        addCell(row, 1, cellGroup.name(), style);
        addCell(row, 2, formatLeader(cellGroup), style);
        addCell(row, 3, cellGroup.meetingDaySpanishName(), style);
        addCell(row, 4, formatTime(cellGroup), style);
        addCell(row, 5, cellGroup.location(), style);
        addCell(row, 6, cellGroup.memberCount(), style);
    }

    private String formatLeader(CellGroupResponseDto cellGroup) {
        if (cellGroup.leader() == null) return "";
        return cellGroup.leader().firstName() + " " + cellGroup.leader().lastName();
    }

    private String formatTime(CellGroupResponseDto cellGroup) {
        if (cellGroup.meetingTime() == null) return "";
        return cellGroup.meetingTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private void addCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex, CellType.STRING);
        if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }

    private void autoSizeColumns(Sheet sheet) {
        int columnCount = sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 800);
        }
    }

    private void freezeHeader(Sheet sheet) {
        sheet.createFreezePane(0, 1);
    }
}
