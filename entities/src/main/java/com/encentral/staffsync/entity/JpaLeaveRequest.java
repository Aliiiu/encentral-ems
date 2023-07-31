package com.encentral.staffsync.entity;

import com.encentral.staffsync.entity.enums.LeaveRequestStatus;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;


/**
 * The persistent class for the leave_request database table.
 * 
 */
@Entity
@Table(name="leave_request")
public class JpaLeaveRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="leave_request_id", unique=true, nullable=false, length=64)
	private String leaveRequestId;

	@Column(name="approval_status", nullable=false, length=2147483647)
	@Enumerated(EnumType.STRING)
	private LeaveRequestStatus approvalStatus;

	@Column(name="date_created", nullable=false)
	private Timestamp dateCreated;

	@Column(name="date_modified", nullable=false)
	private Timestamp dateModified;

	@Column(nullable=false)
	private Integer duration;

	@Column(length=2147483647)
	private String reason;

	@Column(length=2147483647)
	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name="start_date", nullable=false)
	private Date startDate;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="approver_id")
	private JpaEmployee approver;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	//bidirectional many-to-one association to JpaOption
	@ManyToOne
	@JoinColumn(name="option_value", nullable=false)
	private JpaOption leaveType;

	public JpaLeaveRequest() {
	}

	public String getLeaveRequestId() {
		return this.leaveRequestId;
	}

	public void setLeaveRequestId(String leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
	}

	public LeaveRequestStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(LeaveRequestStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Timestamp getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Timestamp getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public JpaEmployee getApprover() {
		return approver;
	}

	public void setApprover(JpaEmployee approver) {
		this.approver = approver;
	}

	public JpaEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	public JpaOption getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(JpaOption leaveType) {
		this.leaveType = leaveType;
	}

	@Override
	public int hashCode() {
		if (this.getLeaveRequestId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getLeaveRequestId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaLeaveRequest leaveRequest = (JpaLeaveRequest) obj;
		return Objects.equals(this.getLeaveRequestId(), leaveRequest.getLeaveRequestId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Employee", this.getEmployee().getEmployeeEmail())
				.add("Duration", this.getDuration())
				.add("Status", this.getApprovalStatus())
				.toString();
	}

}