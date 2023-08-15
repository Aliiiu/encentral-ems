package com.esl.internship.staffsync.attendance.tracking.impl;

import com.esl.internship.staffsync.attendance.tracking.api.IAttendanceTracking;
import com.esl.internship.staffsync.attendance.tracking.dto.AttendanceFilterDTO;
import com.esl.internship.staffsync.attendance.tracking.dto.SystemFilterDTO;
import com.esl.internship.staffsync.attendance.tracking.model.Attendance;
import com.esl.internship.staffsync.attendance.tracking.model.AttendanceReport;
import com.esl.internship.staffsync.commons.util.DateUtility;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.entities.enums.EmployeeStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.attendance.tracking.model.AttendanceMapper.INSTANCE;

public class DefaultAttendanceTrackingImpl implements IAttendanceTracking {

    private static final QJpaAttendance qJpaAttendance = QJpaAttendance.jpaAttendance;

    private static final QJpaDepartment qJpaDepartment = QJpaDepartment.jpaDepartment;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    @Inject
    JPAApi jpaApi;

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to check a user in
     *
     * @param employeeId Employee id
     *
     * @return boolean indicating success
     */
    @Override
    public boolean checkIn(String employeeId) {
        AtomicBoolean isTransactionSuccessful = new AtomicBoolean(false);
        jpaApi.withTransaction(em -> {
            JpaAttendance jpaAttendance = new JpaAttendance();
            JpaEmployee jpaEmployee = new JpaEmployee();
            jpaEmployee.setEmployeeId(employeeId);

            jpaAttendance.setAttendanceId(UUID.randomUUID().toString());
            jpaAttendance.setEmployee(jpaEmployee);
            jpaAttendance.setAttendanceDate(DateUtility.convertToDate(LocalDate.now()));
            jpaAttendance.setCheckInTime(new Time(new Date().getTime()));
            em.persist(jpaAttendance);
            isTransactionSuccessful.set(true);
            return true;
        });
        return isTransactionSuccessful.get();
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to check a user out
     *
     * @param employeeId Employee id
     *
     * @return boolean indicating success
     */
    @Override
    public boolean checkOut(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
                .where(qJpaAttendance.checkOutTime.eq((Time) null))
                .set(qJpaAttendance.checkOutTime, new Time(new Date().getTime()))
                .execute() == 1;
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch a single attendance record
     *
     * @param attendanceId Attendance id
     *
     * @return Optional containing attendance object
     */
    @Override
    public Optional<Attendance> getAttendance(String attendanceId) {
        JpaAttendance jpaAttendance = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.attendanceId.eq(attendanceId))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.jpaAttendanceToAttendance(jpaAttendance));
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch attendance objects for an employee within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance objects
     */
    @Override
    public List<Attendance> getEmployeeAttendance(AttendanceFilterDTO attendanceFilterDTO) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(attendanceFilterDTO.getId()))
                .where(qJpaAttendance.attendanceDate.after(attendanceFilterDTO.getStartDate()))
                .where(qJpaAttendance.attendanceDate.before(attendanceFilterDTO.getEndDate()))
                .fetch()
                .stream()
                .map(INSTANCE::jpaAttendanceToAttendance)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch all attendance objects created by a user
     *
     * @param employeeId Employee id
     *
     * @return List of attendance objects
     */
    @Override
    public List<Attendance> getAllEmployeeAttendance(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
                .fetch()
                .stream()
                .map(INSTANCE::jpaAttendanceToAttendance)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch attendance report objects for an employee within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance report objects
     */
    @Override
    public List<AttendanceReport> getEmployeeAttendanceReport(AttendanceFilterDTO attendanceFilterDTO) {
        List<Attendance> attendanceList = getEmployeeAttendance(attendanceFilterDTO);
        return aggregateAttendanceRecords(attendanceList);
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch aggregated attendance report objects for an employee within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return Attendance report object for employee
     */
    @Override
    public Optional<AttendanceReport> getEmployeeWorkingHours(AttendanceFilterDTO attendanceFilterDTO) {
        List<Attendance> attendanceList = getEmployeeAttendance(attendanceFilterDTO);
        return Optional.ofNullable(computeAttendanceReport(attendanceList).get(0)) ;
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch aggregated attendance report objects for a department within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance report objects
     */
    @Override
    public List<AttendanceReport> getDepartmentWorkingHours(AttendanceFilterDTO attendanceFilterDTO) {
        List<Attendance> attendanceList = getDepartmentAttendance(attendanceFilterDTO);
        return computeAttendanceReport(attendanceList);
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch aggregated attendance report objects for all employees within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance report objects
     */
    @Override
    public List<AttendanceReport> getWorkingHours(SystemFilterDTO attendanceFilterDTO) {
        List<Attendance> attendanceList = getSystemAttendance(attendanceFilterDTO);
        return computeAttendanceReport(attendanceList);
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch attendance objects for a department within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance objects
     */
    @Override
    public List<Attendance> getDepartmentAttendance(AttendanceFilterDTO attendanceFilterDTO) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.employee.department.departmentId.eq(attendanceFilterDTO.getId()))
                .where(qJpaAttendance.attendanceDate.after(attendanceFilterDTO.getStartDate()))
                .where(qJpaAttendance.attendanceDate.before(attendanceFilterDTO.getEndDate()))
                .fetch()
                .stream()
                .map(INSTANCE::jpaAttendanceToAttendance)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch all attendance objects for a department
     *
     * @param departmentId Department id
     *
     * @return List of attendance objects
     */
    @Override
    public List<Attendance> getAllDepartmentAttendance(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.employee.department.departmentId.eq(departmentId))
                .fetch()
                .stream()
                .map(INSTANCE::jpaAttendanceToAttendance)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch attendance report objects for a department within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance report objects
     */
    @Override
    public List<AttendanceReport> getDepartmentAttendanceReport(AttendanceFilterDTO attendanceFilterDTO) {
        List<Attendance> attendanceList = getDepartmentAttendance(attendanceFilterDTO);
        return aggregateAttendanceRecords(attendanceList);
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch attendance objects within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance objects
     */
    @Override
    public List<Attendance> getSystemAttendance(SystemFilterDTO attendanceFilterDTO) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.attendanceDate.after(attendanceFilterDTO.getStartDate()))
                .where(qJpaAttendance.attendanceDate.before(attendanceFilterDTO.getEndDate()))
                .fetch()
                .stream()
                .map(INSTANCE::jpaAttendanceToAttendance)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch all attendance objects
     *
     * @return List of attendance objects
     */
    @Override
    public List<Attendance> getAllSystemAttendance() {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .fetch()
                .stream()
                .map(INSTANCE::jpaAttendanceToAttendance)
                .collect(Collectors.toList());
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to fetch attendance report objects within a time range
     *
     * @param attendanceFilterDTO DTO containing id and filter date params
     *
     * @return List of attendance report objects
     */
    @Override
    public List<AttendanceReport> getSystemAttendanceReport(SystemFilterDTO attendanceFilterDTO) {
        List<Attendance> attendanceList = getSystemAttendance(attendanceFilterDTO);
        return aggregateAttendanceRecords(attendanceList);
    }

    /**
     * @author ALIU
     * @dateCreated 11/08/2023
     * @description Method to delete an attendance record by id
     *
     * @param attendanceId Attendance id
     *
     * @return Boolean indicating success
     */
    @Override
    public boolean deleteAttendanceRecord(String attendanceId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaAttendance)
                .where(qJpaAttendance.attendanceId.eq(attendanceId))
                .execute() == 1;
    }

    /**
     * @author ALIU
     * @dateCreated 11/08/2023
     * @description Method to delete all an employee's attendance record
     *
     * @param employeeId Employee id
     *
     * @return Boolean indicating success
     */
    @Override
    public boolean deleteEmployeeAttendanceRecord(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
                .execute() >= 1;
    }

    /**
     * @author ALIU
     * @dateCreated 11/08/2023
     * @description Method to check if employee has signed in
     *
     * @param employeeId Employee id
     *
     * @return Boolean indicating if employee has signed in
     */
    @Override
    public boolean checkIfOpenAttendanceExists(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
                .where(qJpaAttendance.checkOutTime.eq((Time) null))
                .fetch().size() != 0;
    }

    /**
     * @author ALIU
     * @dateCreated 11/08/2023
     * @description Method to check if employee's account is active
     *
     * @param employeeId Employee id
     *
     * @return Boolean indicating if employee status is active
     */
    @Override
    public boolean checkEmployeeStatus(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .where(qJpaEmployee.currentStatus.eq(EmployeeStatus.ACTIVE))
                .fetchOne() != null;
    }

    /**
     * @author ALIU
     * @dateCreated 11/08/2023
     * @description Method to check if employee's account exists
     *
     * @param employeeId Employee id
     *
     * @return Boolean indicating if employee exists
     */
    @Override
    public boolean checkIfEmployeeExists(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne() != null;
    }

    /**
     * @author ALIU
     * @dateCreated 11/08/2023
     * @description Method to check if department exists
     *
     * @param departmentId Department id
     *
     * @return Boolean indicating if department exists
     */
    @Override
    public boolean checkIfDepartmentExists(String departmentId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaDepartment)
                .where(qJpaDepartment.departmentId.eq(departmentId))
                .fetchOne() != null;
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to aggregate a list of attendance records based on date and employee id
     *
     * @param attendanceList List of attendance records
     *
     * @return A list of attendance report objects, each representing a working day for an employee
     */
    private List<AttendanceReport> aggregateAttendanceRecords(List<Attendance> attendanceList) {
        Map<List<String>, List<Attendance>> aggregatedRecords = attendanceList.stream()
                .collect(Collectors.groupingBy(attendance -> Arrays.asList(attendance.getEmployeeId(), attendance.getAttendanceDate().toString())));

        List<AttendanceReport> attendanceReportList = new ArrayList<>();
        for (Map.Entry<List<String>, List<Attendance>> entry : aggregatedRecords.entrySet()) {
            List<String> keys = entry.getKey();
            List<Attendance> employeeAttendance = entry.getValue();

            // Compute working hours here
            long workingTime = 0;
            AttendanceReport attendanceReport = new AttendanceReport();
            for (Attendance record : employeeAttendance) {
                workingTime += record.getCheckOutTime().getTime() - record.getCheckInTime().getTime();
            }
            long[] workTime = DateUtility.convertDateToHoursAndMinutes(workingTime);

            attendanceReport.setEmployeeId(keys.get(0));
            attendanceReport.setAttendanceDate(employeeAttendance.get(0).getAttendanceDate());
            attendanceReport.setDepartmentName(employeeAttendance.get(0).getDepartmentName());
            attendanceReport.setHours(workTime[0]);
            attendanceReport.setMinutes(workTime[1]);
            attendanceReport.setFirstName(employeeAttendance.get(0).getFirstName());
            attendanceReport.setLastName(employeeAttendance.get(0).getLastName());
            attendanceReportList.add(attendanceReport);
        }
        return attendanceReportList;
    }

    /**
     * @author ALIU
     * @dateCreated 10/08/2023
     * @description Method to aggregate a list of attendance records based on employee id
     *
     * @param attendanceList List of attendance records
     *
     * @return A list of attendance report objects, each representing a working period for an employee
     */
    private List<AttendanceReport> computeAttendanceReport(List<Attendance> attendanceList) {
        Map<String, List<Attendance>> aggregatedRecords = attendanceList.stream()
                .collect(Collectors.groupingBy(Attendance::getEmployeeId));

        List<AttendanceReport> attendanceReportList = new ArrayList<>();
        for (Map.Entry<String, List<Attendance>> entry : aggregatedRecords.entrySet()) {
            String employeeId = entry.getKey();
            List<Attendance> employeeAttendance = entry.getValue();

            // Compute working hours here
            long workingTime = 0;
            AttendanceReport attendanceReport = new AttendanceReport();
            for (Attendance record : employeeAttendance) {
                workingTime += record.getCheckOutTime().getTime() - record.getCheckInTime().getTime();
            }
            long[] workTime = DateUtility.convertDateToHoursAndMinutes(workingTime);

            attendanceReport.setEmployeeId(employeeId);
            attendanceReport.setAttendanceDate(employeeAttendance.get(0).getAttendanceDate());
            attendanceReport.setDepartmentName(employeeAttendance.get(0).getDepartmentName());
            attendanceReport.setHours(workTime[0]);
            attendanceReport.setMinutes(workTime[1]);
            attendanceReport.setFirstName(employeeAttendance.get(0).getFirstName());
            attendanceReport.setLastName(employeeAttendance.get(0).getLastName());
            attendanceReportList.add(attendanceReport);
        }
        return attendanceReportList;
    }
}
