package com.esl.internship.staffsync.performance.evaluation.impl;

import com.esl.internship.staffsync.attendance.tracking.api.IAttendanceTracking;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.DateUtility;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.performance.evaluation.api.IPerformanceEvaluationApi;
import com.esl.internship.staffsync.commons.util.AuxDateRangeDTO;
import com.esl.internship.staffsync.performance.evaluation.model.AttendanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.DailyPerformanceOverview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Time;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultPerformanceEvaluationApiImpl implements IPerformanceEvaluationApi {

    @Inject
    IAttendanceTracking iAttendanceTrackingApi;

    @Inject
    IEmployeeApi iEmployeeApi;

    @Inject
    JPAApi jpaApi;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;
    private static final QJpaAttendance qJpaAttendance = QJpaAttendance.jpaAttendance;

    @Override
    public AttendanceOverview getAttendanceSummaryOfEmployee(String employeeId, AuxDateRangeDTO auxDateRangeDTO) {

        LocalDate startDate, endDate;
        startDate = auxDateRangeDTO.getStartDate();
        endDate = auxDateRangeDTO.getEndDate();

        int totalWorkingDays = getTotalWorkingDaysBetweenDateRange(startDate, endDate);
        int totalEmployingWorkingDays = getTotalEmployeeWorkingDaysBetweenDateRange(
                employeeId, DateUtility.convertToDate(startDate), DateUtility.convertToDate(endDate)
        );

        AttendanceOverview overview = new AttendanceOverview();
        overview.setDaysAbsent(totalWorkingDays - totalEmployingWorkingDays);
        overview.setDaysPresent(totalEmployingWorkingDays);
        overview.setStartDate(startDate);
        overview.setEndDate(endDate);

        return overview;
    }

    @Override
    public Response<DailyPerformanceOverview> getTheCurrentDayPerformanceOverview(String employeeId) {
        JpaEmployee employee = getJpaEmployee(employeeId);
        Response<DailyPerformanceOverview> response = new Response<>();

        if (employee == null)
            return response.putError("employeeId", "Employee not found!");
        JpaDepartment department = employee.getDepartment();

        if (department == null)
            return response.putError("employeeId", "Employee does not belong to any working department");

        int expectedWorkingHours = department.getWorkingHours();

        DailyPerformanceOverview performanceOverview = calculateDailyPerformanceMetrics(
                new Date(), expectedWorkingHours
        );

        return response.setValue(performanceOverview);
    }

    @Override
    public List<DailyPerformanceOverview> getPerformanceOverviewBetweenTimePeriod(String employeeId, AuxDateRangeDTO auxDateRangeDTO) {
        JpaEmployee employee = getJpaEmployee(employeeId);
        Response<DailyPerformanceOverview> response = new Response<>();

        if (employee == null)
            return new ArrayList<>();
        JpaDepartment department = employee.getDepartment();

        if (department == null)
            return new ArrayList<>();

        int expectedWorkingHours = department.getWorkingHours();

        return calculateDailyPerformanceMetricBetweenDays(auxDateRangeDTO, expectedWorkingHours);
    }

    private int getTotalEmployeeWorkingDaysBetweenDateRange(String employeeId, Date startDate, Date endDate) {
        return new JPAQueryFactory(jpaApi.em())
                .selectDistinct(qJpaAttendance.attendanceDate)
                .from(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
                .where(qJpaAttendance.attendanceDate.goe(startDate))
                .where(qJpaAttendance.attendanceDate.loe(endDate))
                .where(qJpaAttendance.checkInTime.isNotNull())
                .fetch()
                .stream()
                .map(DateUtility::convertToLocalDate)
                .collect(Collectors.toSet())
                .size();
    }

    private int getTotalWorkingDaysBetweenDateRange(LocalDate startDate, LocalDate endDate) {

        int workingDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (dateIsWorkingDay(currentDate))
                workingDays++;

            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }

        return workingDays;
    }

    private List<LocalDate> getAllWorkingDaysBetweenDateRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (dateIsWorkingDay(currentDate))
                dates.add(currentDate);

            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }

        return dates;

    }

    private List<JpaAttendance> getAllAttendancesForTheDay(Date date) {
        Date startOfTheDay = DateUtility.getTheStartOfTheDay(date);
        Date endOfTheDay = DateUtility.getTheEndOfTheDay(date);

        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaAttendance)
                .where(qJpaAttendance.attendanceDate.goe(startOfTheDay))
                .where(qJpaAttendance.attendanceDate.loe(endOfTheDay))
                .orderBy(qJpaAttendance.checkInTime.asc())
                .fetch();
    }

    private double calculateTotalHoursWorked(List<JpaAttendance> attendances) {
        return  attendances
                .stream()
                .mapToDouble(
                        attendance -> DateUtility.getDurationBetweenTwoTimePeriod(
                                attendance.getCheckInTime(), attendance.getCheckOutTime()
                        )
                )
                .sum();
    }

    private DailyPerformanceOverview calculateDailyPerformanceMetrics(Date date, int expectedDailyWorkingHours) {


        int totalCheckIns = 0;
        int totalCheckOuts = 0;
        double totalWorkingHours = 0;

        List<JpaAttendance> attendances = getAllAttendancesForTheDay(date);

        Time lastCheckoutTime = null;

        for (JpaAttendance attendance : attendances) {
            if (attendance.getCheckInTime() != null)
                totalCheckIns++;
            if (attendance.getCheckOutTime() != null) {
                totalCheckOuts++;
                lastCheckoutTime = attendance.getCheckOutTime();
            }

            totalWorkingHours += DateUtility.getDurationBetweenTwoTimePeriod(
                    attendance.getCheckInTime(), attendance.getCheckOutTime()
            );
        }

        DailyPerformanceOverview performanceOverview = new DailyPerformanceOverview();

        performanceOverview.setTotalCheckIns(totalCheckIns);
        performanceOverview.setTotalCheckOuts(totalCheckOuts);
        performanceOverview.setTotalHoursWorked(totalWorkingHours);
        performanceOverview.setDay(DateUtility.convertToLocalDate(date));
        performanceOverview.setPerformanceRating(
                (totalWorkingHours / expectedDailyWorkingHours) * 100
        );

        performanceOverview.setLastCheckOutTime((lastCheckoutTime == null) ? null : lastCheckoutTime.toLocalTime());
        performanceOverview.setFirstCheckInTime(attendances.get(0).getCheckInTime().toLocalTime());

        return performanceOverview;

    }

    private List<DailyPerformanceOverview> calculateDailyPerformanceMetricBetweenDays(AuxDateRangeDTO auxDateRangeDTO, int expectedDailyWorkingHours) {
        LocalDate startDate, endDate;
        startDate = auxDateRangeDTO.getStartDate();
        endDate = auxDateRangeDTO.getEndDate();

        List<DailyPerformanceOverview> overviews = new ArrayList<>();
        List<LocalDate> workingDays = getAllWorkingDaysBetweenDateRange(startDate, endDate);

        if (auxDateRangeDTO.isAscending()) {

            for (LocalDate date : workingDays) {
                DailyPerformanceOverview performanceOverview =
                        calculateDailyPerformanceMetrics(
                                DateUtility.convertToDate(date), expectedDailyWorkingHours
                        );
                overviews.add(performanceOverview);
            }
        } else {
            for (int i = workingDays.size() - 1; i > -1; i--) {
                DailyPerformanceOverview performanceOverview =
                        calculateDailyPerformanceMetrics(
                                DateUtility.convertToDate(workingDays.get(i)), expectedDailyWorkingHours
                        );
                overviews.add(performanceOverview);
            }
        }

        return overviews;
    }

    /**
     * @author ALIU
     * @dateCreated 18/08/2023
     * @description A helper method to fetch an employee record from the database
     *
     * @param employeeId ID of the employee to fetch
     *
     * @return JpaEmployee An employee record or null if not found
     */
    private JpaEmployee getJpaEmployee(String employeeId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaEmployee)
                .where(qJpaEmployee.employeeId.eq(employeeId))
                .fetchOne();
    }

    private boolean dateIsWorkingDay(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }

        // TODO: Check for other approved non-working days by the company (eg. Holidays)

        return true;
    }
}
