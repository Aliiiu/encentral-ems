package com.encentral.staffsync.entity;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;


/**
 * The persistent class for the attendance database table.
 * 
 */
@Entity
@Table(name="attendance")
public class JpaAttendance implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="attendance_id", unique=true, nullable=false, length=64)
	private String attendanceId;

	@Temporal(TemporalType.DATE)
	@Column(name="attendance_date", nullable=false)
	private Date attendanceDate;

	@Column(name="check_in_time", nullable=false)
	private Time checkInTime;

	@Column(name="check_out_time")
	private Time checkOutTime;

	//bidirectional many-to-one association to JpaEmployee
	@ManyToOne
	@JoinColumn(name="employee_id", nullable=false)
	private JpaEmployee employee;

	public JpaAttendance() {
	}

	public String getAttendanceId() {
		return this.attendanceId;
	}

	public void setAttendanceId(String attendanceId) {
		this.attendanceId = attendanceId;
	}

	public Date getAttendanceDate() {
		return this.attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public Time getCheckInTime() {
		return this.checkInTime;
	}

	public void setCheckInTime(Time checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Time getCheckOutTime() {
		return this.checkOutTime;
	}

	public void setCheckOutTime(Time checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public JpaEmployee getEmployee() {
		return this.employee;
	}

	public void setEmployee(JpaEmployee employee) {
		this.employee = employee;
	}

	@Override
	public int hashCode() {
		if (this.getAttendanceId() == null)
			return super.hashCode();
		return Objects.hashCode(this.getAttendanceId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		JpaAttendance attendance = (JpaAttendance) obj;
		return Objects.equals(this.getAttendanceId(), attendance.getAttendanceId());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this.getClass())
				.add("Date", this.getAttendanceDate())
				.add("Check-in", this.getCheckInTime())
				.add("Check-out", this.getCheckOutTime())
				.add("Employee", this.getEmployee().getEmployeeEmail())
				.toString();
	}

}