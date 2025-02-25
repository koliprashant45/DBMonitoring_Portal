package com.dbmonitor.app.util;

import java.time.LocalDate;

public class LocalDateRange {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public LocalDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isValid() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
