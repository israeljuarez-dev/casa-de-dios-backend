package com.casadedios.backend.common.export.service;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ExcelExportService {

    ByteArrayOutputStream export() throws IOException;

    String getFileName();

    default void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException _) {
            }
        }
    }
}
