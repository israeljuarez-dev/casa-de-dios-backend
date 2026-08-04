package com.casadedios.backend.disciple.export;

import com.casadedios.backend.common.export.service.ExcelExportService;
import com.casadedios.backend.common.export.theme.ExcelStyleFactory;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;
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
public class DiscipleExcelExporter  implements ExcelExportService {

    private static final String SHEET_NAME = "Discípulos";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final List<DiscipleResponseDto> disciples;

    @Override
    public ByteArrayOutputStream export() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setLeftToRight(true);

            createHeader(sheet, workbook);
            fillData(sheet, workbook);
            autoSizeColumns(sheet);
            freezeHeader(sheet);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            log.info("Reporte de {} discípulos generado exitosamente", disciples.size());

            return outputStream;
        }
    }

    @Override
    public String getFileName() {
        return "Discípulos_" + LocalDate.now(java.time.ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private void createHeader(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = ExcelStyleFactory.createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(28);

        String[] headers = {
                "N°",
                "Nombres",
                "Apellidos",
                "Fecha Nacimiento",
                "Edad",
                "Profesión",
                "Teléfono",
                "Dirección",
                "DNI",
                "Estado Civil",
                "Pareja/Cónyuge",
                "Nivel Espiritual"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillData(Sheet sheet, Workbook workbook) {
        CellStyle rowLight = ExcelStyleFactory.createRowStyleLight(workbook);
        CellStyle rowDark = ExcelStyleFactory.createRowStyleDark(workbook);
        CellStyle dateLight = ExcelStyleFactory.createDateStyleLight(workbook);
        CellStyle dateDark = ExcelStyleFactory.createDateStyleDark(workbook);
        CellStyle boolLight = ExcelStyleFactory.createBooleanStyleLight(workbook);
        CellStyle boolDark = ExcelStyleFactory.createBooleanStyleDark(workbook);

        int rowNum = 1;
        boolean alternate = false;

        for (DiscipleResponseDto disciple : disciples) {
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(22);

            CellStyle currentRow = alternate ? rowDark : rowLight;
            CellStyle currentDate = alternate ? dateDark : dateLight;
            CellStyle currentBool = alternate ? boolDark : boolLight;

            fillDiscipleRow(row, disciple, rowNum, currentRow, currentDate, currentBool);

            rowNum++;

            alternate = !alternate;
        }
    }

    private void fillDiscipleRow(
            Row row,
            DiscipleResponseDto disciple,
            int rowNumber,
            CellStyle rowStyle,
            CellStyle dateStyle,
            CellStyle boolStyle
    ) {
        addCell(row, 0, rowNumber, rowStyle);
        addCell(row, 1, disciple.firstName(), rowStyle);
        addCell(row, 2, disciple.lastName(), rowStyle);
        addCell(row, 3, formatBirthDate(disciple.birthDate()), dateStyle);
        addCell(row, 4, disciple.age(), rowStyle);
        addCell(row, 5, disciple.occupation(), rowStyle);
        addCell(row, 6, formatPhone(disciple.phoneCodeNumber(), disciple.phoneNumber()), rowStyle);
        addCell(row, 7, disciple.address(), rowStyle);
        addCell(row, 8, disciple.dni(), rowStyle);
        addCell(row, 9, disciple.maritalStatus() != null ? disciple.maritalStatus().getDisplayName() : "", rowStyle);
        addCell(row, 10, disciple.coupleName(), rowStyle);
        addCell(row, 11, disciple.spiritualLevel() != null ? disciple.spiritualLevel().getDisplayName() : "", rowStyle);
    }

    private String formatBirthDate(java.time.LocalDate birthDate) {
        return birthDate != null ? birthDate.format(DATE_FORMATTER) : "";
    }

    private String formatPhone(String phoneCodeNumber, String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) return "";
        if (phoneCodeNumber == null || phoneCodeNumber.isBlank()) return phoneNumber;

        String prefix = phoneCodeNumber.startsWith("+") ? phoneCodeNumber : "+" + phoneCodeNumber;
        return prefix + " " + phoneNumber;
    }

    private String formatMaritalStatus(com.casadedios.backend.disciple.enums.MaritalStatus status) {
        return status != null ? status.name() : "";
    }

    private String formatSpiritualLevel(com.casadedios.backend.disciple.enums.SpiritualLevel level) {
        return level != null ? level.name() : "";
    }

    private String formatBoolean(boolean value) {
        return value ? "Sí" : "No";
    }

    private String formatInviter(com.casadedios.backend.disciple.dto.response.DiscipleInviterResponseDto inviter) {
        return inviter != null ? inviter.firstName() + " " + inviter.lastName() : "";
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
