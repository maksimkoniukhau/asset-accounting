package com.maks.assetaccounting.vaadin.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class LocalDateZonedDateTimeConverter implements Converter<LocalDate, ZonedDateTime> {

    @Override
    public Result<ZonedDateTime> convertToModel(final LocalDate value, final ValueContext context) {
        try {
            return Result.ok(ZonedDateTime.of(value, LocalTime.now(), ZoneId.systemDefault()));
        } catch (NumberFormatException e) {
            return Result.error("Must enter a date");
        }
    }

    @Override
    public LocalDate convertToPresentation(final ZonedDateTime value, final ValueContext context) {
        return LocalDate.from(value != null ? value : ZonedDateTime.now());
    }
}
