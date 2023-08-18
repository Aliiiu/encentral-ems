package com.esl.internship.staffsync.commons.util;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

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

    public LocalDate getStartDate() {
        if (datePointA.isBefore(datePointB))
            return datePointA;
        return datePointB;
    }

    public LocalDate getEndDate() {
        if (datePointA.isAfter(datePointB))
            return datePointA;
        return datePointB;
    }

    public boolean isAscending() {
        return datePointA.isBefore(datePointB);
    }

    public boolean isDescending() {
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
