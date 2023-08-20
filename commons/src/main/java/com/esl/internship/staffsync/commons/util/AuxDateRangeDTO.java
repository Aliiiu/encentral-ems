package com.esl.internship.staffsync.commons.util;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * @author ALIU
 * @dateCreated 18/08/2023
 * @description An auxiliary date range data transfer object
 * that will require at least a single date point
 * with the option to have ascending or descending ordering of date
 */
public class AuxDateRangeDTO {

    @NotNull(message = "A Date point is required!")
    private LocalDate datePointA;

    private LocalDate datePointB;

    public LocalDate startDate() {
        if (datePointA.isBefore(datePointB))
            return datePointA;
        return datePointB;
    }

    public LocalDate endDate() {
        if (datePointA.isAfter(datePointB))
            return datePointA;
        return datePointB;
    }

    public boolean dateIsAscending() {
        return datePointA.isBefore(datePointB);
    }

    public boolean dateIsDescending() {
        return datePointA.isAfter(datePointB);
    }

    public LocalDate getDatePointA() {
        return datePointA;
    }

    public void setDatePointA(LocalDate datePointA) {
        this.datePointA = datePointA;
    }

    public LocalDate getDatePointB() {
        return datePointB;
    }

    public void setDatePointB(LocalDate datePointB) {
        if (datePointB == null)
            datePointB = LocalDate.now();
        this.datePointB = datePointB;
    }

}

