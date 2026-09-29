package com.casadedios.backend.modules.retreat.export;

import com.casadedios.backend.common.export.service.ExcelExportService;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RetreatAttendeesWithPaymentExcelExporter implements ExcelExportService {

    private final String retreatName;
    private final List<RetreatEnrollmentResponseDto> enrollments;

    @Override
    public ByteArrayOutputStream export() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Asistentes - Pagos");
            sheet.getPrintSetup().setLeftToRight(true);

            RetreatAttendeesSheetWriter.write(sheet, workbook, 0, enrollments, false);

            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 800);
            }
            sheet.createFreezePane(0, 1);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            log.info("Reporte de pagos de {} asistentes del encuentro '{}' generado exitosamente", enrollments.size(), retreatName);

            return outputStream;
        }
    }

    @Override
    public String getFileName() {
        return "Asistentes_Pagos_" + retreatName.replaceAll("\\s+", "_");
    }
}