package com.esl.internship.staffsync.performance.evaluation.model;

import java.time.LocalDate;

public class AttendanceOverview {
    private int daysAbsent;

    private int daysPresent;

    private LocalDate startDate;

    private LocalDate endDate;


    public int getDaysAbsent() {
        return daysAbsent;
    }

    public void setDaysAbsent(int daysAbsent) {
        this.daysAbsent = daysAbsent;
    }

    public int getDaysPresent() {
        return daysPresent;
    }

    public void setDaysPresent(int daysPresent) {
        this.daysPresent = daysPresent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
