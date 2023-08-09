package com.esl.internship.staffsync.leave.management.dto;

import javax.validation.constraints.NotNull;

public class EditLeaveRequestDTO {

    @NotNull(message = "Leave request id field cannot be null")
    private String leaveRequestId;

    private String remarks = "";

    public EditLeaveRequestDTO() {
    }

    public String getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(String leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
