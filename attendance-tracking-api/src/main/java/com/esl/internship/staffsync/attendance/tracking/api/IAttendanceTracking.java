package com.esl.internship.staffsync.attendance.tracking.api;

import com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO;
import com.esl.internship.staffsync.attendance.tracking.dto.SystemFilterDTO;
import com.esl.internship.staffsync.attendance.tracking.model.Attendance;
import com.esl.internship.staffsync.attendance.tracking.model.AttendanceReport;

import java.util.List;
import java.util.Optional;

public interface IAttendanceTracking {
    boolean checkIn(String employeeId);

    boolean checkOut(String employeeId);

    Optional<Attendance> getAttendance(String attendanceId);

    List<Attendance> getEmployeeAttendance(AttendanceFilterDTO attendanceFilterDTO);

    List<Attendance> getAllEmployeeAttendance(String employeeId);

    List<AttendanceReport> getEmployeeAttendanceReport(AttendanceFilterDTO attendanceFilterDTO);

    List<Attendance> getDepartmentAttendance(AttendanceFilterDTO attendanceFilterDTO);

    List<Attendance> getAllDepartmentAttendance(String departmentId);

    List<AttendanceReport> getDepartmentAttendanceReport(AttendanceFilterDTO attendanceFilterDTO);

    List<Attendance> getSystemAttendance(SystemFilterDTO attendanceFilterDTO);

    List<Attendance> getAllSystemAttendance();

    List<AttendanceReport> getSystemAttendanceReport(SystemFilterDTO attendanceFilterDTO);

    boolean deleteAttendanceRecord(String attendanceId);

    boolean deleteEmployeeAttendanceRecord(String employeeId);

    boolean checkIfOpenAttendanceExists(String employeeId);

    boolean checkEmployeeStatus(String employeeId);

    Optional<AttendanceReport> getEmployeeWorkingHours(AttendanceFilterDTO attendanceFilterDTO);

    List<AttendanceReport> getDepartmentWorkingHours(AttendanceFilterDTO attendanceFilterDTO);

    List<AttendanceReport> getWorkingHours(SystemFilterDTO attendanceFilterDTO);

    boolean checkIfEmployeeExists(String employeeId);

    boolean checkIfDepartmentExists(String departmentId);
}
