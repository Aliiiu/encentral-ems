package com.esl.internship.staffsync.performance.evaluation.dto;

import javax.validation.constraints.NotNull;

import java.util.Date;

public class DateFilterDTO {

    @NotNull(message = "A Date point is required!")
    private Date datePointA;

    private Date datePointB = new Date();

    public Date getStartDate() {
        if (datePointA.before(datePointB))
            return datePointA;
        return datePointB;
    }

    public Date getEndDate() {
        if (datePointA.after(datePointB))
            return datePointA;
        return datePointB;
    }

    public boolean isAscending() {
        return datePointA.before(getDatePointB());
    }

    public boolean isDescending() {
        return datePointA.after(getDatePointB());
    }

    public Date getDatePointA() {
        return datePointA;
    }

    public void setDatePointA(Date datePointA) {
        this.datePointA = datePointA;
    }

    public Date getDatePointB() {
        return datePointB;
    }

    public void setDatePointB(Date datePointB) {
        this.datePointB = datePointB;
    }
}
