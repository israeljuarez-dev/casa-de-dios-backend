package com.casadedios.backend.common.documentation.resource;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Hidden
@Slf4j
@RequestMapping("/documentation")
public class DocumentationController {

    @GetMapping
    public void redirectToDocumentation(HttpServletResponse response) {
        try {
            response.sendRedirect("/swagger-ui.html");
        } catch (IOException e) {
            log.error("No se pudo redirigir a la documentación", e);
        }
    }
}
