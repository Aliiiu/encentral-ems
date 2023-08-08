package com.esl.internship.staffsync.leave.management.dto;

public class EditLeaveRequestDTO {
    private String leaveRequestId;
    private String remarks ="";

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
