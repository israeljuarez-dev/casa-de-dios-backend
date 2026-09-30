package com.casadedios.backend.modules.inventory.export;

import com.casadedios.backend.common.export.service.ExcelExportService;
import com.casadedios.backend.common.export.theme.ExcelStyleFactory;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemDonorResponseDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class InventoryItemExcelExporter implements ExcelExportService {

    private static final String SHEET_NAME = "Inventario";

    private static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final List<String> HEADERS = List.of(
            "N°",
            "Nombre",
            "Descripción",
            "Categoría",
            "Cantidad",
            "Costo Unitario",
            "Origen",
            "Donante",
            "Fecha de Registro"
    );

    private final List<InventoryItemResponseDto> items;

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

            log.info("Reporte de {} ítems de inventario generado exitosamente", items.size());

            return outputStream;
        }
    }

    @Override
    public String getFileName() {
        return "Inventario_" + LocalDate.now(LIMA_ZONE).format(FILE_DATE_FORMATTER);
    }

    private void createHeader(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = ExcelStyleFactory.createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(28);

        for (int columnIndex = 0; columnIndex < HEADERS.size(); columnIndex++) {
            Cell cell = headerRow.createCell(columnIndex);
            cell.setCellValue(HEADERS.get(columnIndex));
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillData(Sheet sheet, Workbook workbook) {
        CellStyle rowLight = ExcelStyleFactory.createRowStyleLight(workbook);
        CellStyle rowDark = ExcelStyleFactory.createRowStyleDark(workbook);
        CellStyle dateLight = ExcelStyleFactory.createDateStyleLight(workbook);
        CellStyle dateDark = ExcelStyleFactory.createDateStyleDark(workbook);

        int rowNum = 1;
        boolean alternate = false;

        for (InventoryItemResponseDto item : items) {
            Row row = sheet.createRow(rowNum);

            CellStyle currentRow = alternate ? rowDark : rowLight;
            CellStyle currentDate = alternate ? dateDark : dateLight;

            fillItemRow(row, item, rowNum, currentRow, currentDate);

            rowNum++;
            alternate = !alternate;
        }
    }

    private void fillItemRow(Row row, InventoryItemResponseDto item, int rowNumber, CellStyle rowStyle, CellStyle dateStyle) {
        addCell(row, 0, rowNumber, rowStyle);
        addCell(row, 1, item.name(), rowStyle);
        addCell(row, 2, item.description(), rowStyle);
        addCell(row, 3, item.category(), rowStyle);
        addCell(row, 4, item.quantity(), rowStyle);
        addCell(row, 5, item.cost(), rowStyle);
        addCell(row, 6, item.sourceType().getDisplayName(), rowStyle);
        addCell(row, 7, formatDonor(item.donor()), rowStyle);
        addCell(row, 8, item.registeredAt().format(DATE_FORMATTER), dateStyle);
    }

    private String formatDonor(InventoryItemDonorResponseDto donor) {
        return donor != null ? donor.firstName() + " " + donor.lastName() : "";
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
