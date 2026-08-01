package com.casadedios.backend.disciple.util;

import com.casadedios.backend.disciple.dto.response.BirthdayAlertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DiscipleDateCalculator {

    private static final Locale SPANISH_PERU = Locale.of("es", "PE");

    private final Clock clock;

    public Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) return null;
        return Period.between(birthDate, LocalDate.now(clock)).getYears();
    }

    public BirthdayAlertDto calculateBirthdayAlert(LocalDate birthDate) {
        if (birthDate == null) return null;

        // Fecha actual en horario de America/Lima
        LocalDate today = LocalDate.now(clock);

        // Obtenemos la próxima fecha de cumpleaños
        LocalDate nextBirthday = calculateNextBirthday(birthDate);

        // Calculamos los días restantes para el próximo cumpleaños
        long daysUntil = ChronoUnit.DAYS.between(today, nextBirthday);

        return BirthdayAlertDto.builder()
                .isToday(isBirthdayToday(daysUntil))
                .isTomorrow(isBirthdayTomorrow(daysUntil))
                .wasYesterday(isBirthdayWasYesterday(birthDate, today))
                .withinCurrentMonth(isWithinCurrentMonth(birthDate, today))
                .withinCurrentWeek(isWithinWeek(nextBirthday, today))
                .daysUntilNextBirthday(daysUntil)
                .nextBirthday(nextBirthday)
                .dayOfWeek(formatDayOfWeek(nextBirthday))
                .build();
    }

    // Calcula la próxima fecha de cumpleaños: si el de este año ya pasó, toma el del próximo año
    private LocalDate calculateNextBirthday(LocalDate birthDate) {
        if (birthDate == null) return null;

        LocalDate today = LocalDate.now(clock);
        LocalDate nextBirthday = birthDate.withYear(today.getYear());

        if (nextBirthday.isBefore(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        return nextBirthday;
    }

    // Si faltan 0 días para el cumpleaños, entonces el cumpleaños es hoy
    private boolean isBirthdayToday(long daysUntil) {
        return daysUntil == 0;
    }

    // Si falta 1 día para el cumpleaños, entonces el cumpleaños es mañana
    private boolean isBirthdayTomorrow(long daysUntil) {
        return daysUntil == 1;
    }

    // Obtenemos true si el mes y el día del cumpleaños coinciden con ayer
    private boolean isBirthdayWasYesterday(LocalDate birthDate, LocalDate today) {
        LocalDate yesterday = today.minusDays(1);
        return MonthDay.from(birthDate).equals(MonthDay.from(yesterday));
    }

    // Verificamos si el mes del cumpleaños coincide con el mes actual
    private boolean isWithinCurrentMonth(LocalDate birthDate, LocalDate today) {
        return Month.from(birthDate).equals(Month.from(today));
    }

    // Verificar si una fecha pertenece a la semana actual de today, tomando como inicio el lunes y como fin el domingo.
    private boolean isWithinWeek(LocalDate date, LocalDate today) {
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return !date.isBefore(monday) && !date.isAfter(sunday);
    }

    // Obtenemos el nombre del día de la semana en español
    private String formatDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, SPANISH_PERU);
    }
}
