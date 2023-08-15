package com.esl.internship.staffsync.commons.util;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@EndDateAfterStartDate
public class DateRangeDTO {

    @NotNull(message = "Start date cannot be null")
    private Date startDate;

    @Future(message = "End cate must be greater than current date")
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Optional<Date> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
