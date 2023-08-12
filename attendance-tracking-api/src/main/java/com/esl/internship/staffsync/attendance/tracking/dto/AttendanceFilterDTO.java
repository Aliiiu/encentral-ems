package com.esl.internship.staffsync.attendance.tracking.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class AttendanceFilterDTO {

    @NotNull(message= "Id field cannot be null")
    private String Id;

    @NotNull(message="Start date cannot be null")
    private Date startDate;

    private Date endDate = new Date();

    @AssertTrue(message = "End date must be greater than start date")
    public boolean isEndDateGreaterThanStartDate() {
        return endDate == null || endDate.after(startDate);
    }

    public AttendanceFilterDTO() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
