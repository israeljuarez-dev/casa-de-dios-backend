package com.casadedios.backend.common.export.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExcelResponseFactory {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String EXTENSION = ".xlsx";

    private final Clock clock;

    public ResponseEntity<byte[]> build(ByteArrayOutputStream outputStream, String fileName) {
        byte[] excelBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(XLSX_MEDIA_TYPE);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build());
        headers.setContentLength(excelBytes.length);

        log.info("Archivo Excel '{}' generado exitosamente", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    public String fileNameWithDate(String prefix) {
        return prefix + "_" + LocalDate.now(clock).format(DATE_FORMAT) + EXTENSION;
    }
}
