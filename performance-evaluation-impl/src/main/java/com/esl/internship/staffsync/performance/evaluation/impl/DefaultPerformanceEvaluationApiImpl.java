package com.esl.internship.staffsync.performance.evaluation.impl;

import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.DateUtility;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.performance.evaluation.api.IPerformanceEvaluationApi;
import com.esl.internship.staffsync.commons.util.AuxDateRangeDTO;
import com.esl.internship.staffsync.performance.evaluation.dto.EvaluationFeedbackDTO;
import com.esl.internship.staffsync.performance.evaluation.model.AttendanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.DailyPerformanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.PerformanceEvaluation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.performance.evaluation.model.PerformanceEvaluationMapper.INSTANCE;

public class DefaultPerformanceEvaluationApiImpl implements IPerformanceEvaluationApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaEmployee qJpaEmployee = QJpaEmployee.jpaEmployee;

    private static final QJpaAttendance qJpaAttendance = QJpaAttendance.jpaAttendance;

    private static final QJpaPerformanceEvaluation qJpaPerformanceEvaluation = QJpaPerformanceEvaluation.jpaPerformanceEvaluation;

    @Override
    public AttendanceOverview getAttendanceSummaryOfEmployee(String employeeId, AuxDateRangeDTO auxDateRangeDTO) {

        LocalDate startDate, endDate;
        startDate = auxDateRangeDTO.startDate();
        endDate = auxDateRangeDTO.endDate();

        int totalWorkingDays = getTotalWorkingDaysBetweenDateRange(startDate, endDate);
        int totalEmployingWorkingDays = getTotalEmployeeWorkingDaysBetweenDateRange(
                employeeId, DateUtility.convertToDate(startDate), DateUtility.convertToDate(endDate)
        );

        int absentDays = totalWorkingDays - totalEmployingWorkingDays;

        AttendanceOverview overview = new AttendanceOverview();
        overview.setDaysAbsent(Math.max(absentDays, 0));
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
                employeeId, new Date(), expectedWorkingHours
        );

        return response.setValue(performanceOverview);
    }

    @Override
    public List<DailyPerformanceOverview> getPerformanceOverviewBetweenTimePeriod(String employeeId, AuxDateRangeDTO auxDateRangeDTO) {
        JpaEmployee employee = getJpaEmployee(employeeId);

        if (employee == null)
            return new ArrayList<>();
        JpaDepartment department = employee.getDepartment();

        if (department == null)
            return new ArrayList<>();

        int expectedWorkingHours = department.getWorkingHours();

        return calculateDailyPerformanceMetricBetweenDays(
                employeeId, auxDateRangeDTO, expectedWorkingHours
        );
    }

    @Override
    public PerformanceEvaluation getEmployeePerformanceBetweenDatePeriod(String employeeId, AuxDateRangeDTO auxDateRangeDTO) {

        JpaPerformanceEvaluation jpaPerformanceEvaluation = getJpaPerformanceEvaluationByDateRange(employeeId, auxDateRangeDTO.startDate(), auxDateRangeDTO.endDate());

        if (jpaPerformanceEvaluation != null)
            return INSTANCE.mapPerformanceEvaluation(jpaPerformanceEvaluation);


        JpaPerformanceEvaluation evaluation = new JpaPerformanceEvaluation();
        JpaEmployee employee = getJpaEmployee(employeeId);

        int workingHours = employee.getDepartment().getWorkingHours();

        evaluation.setEmployee(employee);
        evaluation.setLeavePerformance(calculateLeavePerformance(employee));
        double attendanceAccuracy = calculateAttendancePerformance(employeeId, auxDateRangeDTO, workingHours);
        if (attendanceAccuracy < 0)
            evaluation.setAttendanceAccuracy(null);
        else
            evaluation.setAttendanceAccuracy(attendanceAccuracy);
        evaluation.setDateCreated(Timestamp.from(Instant.now()));
        evaluation.setEvaluationStartDate(DateUtility.convertToDate(auxDateRangeDTO.startDate()));
        evaluation.setEvaluationEndDate(DateUtility.convertToDate(auxDateRangeDTO.endDate()));

        if (evaluation.getAttendanceAccuracy() != null) {
            evaluation.setPerformanceEvaluationId(UUID.randomUUID().toString());
            jpaApi.em().persist(evaluation);
        }

        return INSTANCE.mapPerformanceEvaluation(evaluation);
    }

    @Override
    public Optional<PerformanceEvaluation> getEmployeeEvaluationById(String performanceEvaluationId) {
        return Optional.ofNullable(INSTANCE.mapPerformanceEvaluation(getJpaPerformanceEvaluationById(performanceEvaluationId)));
    }

    @Override
    public Optional<PerformanceEvaluation> getLastEmployeeEvaluationBy(String employeeId) {
        return Optional.ofNullable(INSTANCE.mapPerformanceEvaluation(new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaPerformanceEvaluation)
                .where(qJpaPerformanceEvaluation.employee.employeeId.eq(employeeId))
                .orderBy(qJpaPerformanceEvaluation.dateCreated.desc())
                .fetchFirst()));
    }

    @Override
    public List<PerformanceEvaluation> getAllEmployeeEvaluations(String employeeId) {
        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaPerformanceEvaluation)
                .where(qJpaPerformanceEvaluation.employee.employeeId.eq(employeeId))
                .orderBy(qJpaPerformanceEvaluation.dateCreated.desc())
                .fetch()
                .stream()
                .map(INSTANCE::mapPerformanceEvaluation)
                .collect(Collectors.toList());
    }

    @Override
    public boolean postFeedbackOnEvaluation(String performanceEvaluationId, String evaluatorId, EvaluationFeedbackDTO evaluationFeedbackDTO) {
        JpaPerformanceEvaluation performanceEvaluation = getJpaPerformanceEvaluationById(performanceEvaluationId);

        if (performanceEvaluation == null)
            return false;
        JpaEmployee evaluator = getJpaEmployee(evaluatorId);

        if (evaluator == null)
            return false;


        if (performanceEvaluation.getEvaluator() != null && performanceEvaluation.getEvaluator().getEmployeeId().equals(evaluator.getEmployeeId())) {
            performanceEvaluation.setFeedback(evaluationFeedbackDTO.getFeedback());
        } else {
            JpaPerformanceEvaluation newEvaluation = new JpaPerformanceEvaluation();
            newEvaluation.setPerformanceEvaluationId(UUID.randomUUID().toString());
            newEvaluation.setFeedback(evaluationFeedbackDTO.getFeedback());
            newEvaluation.setEvaluator(evaluator);
            newEvaluation.setEvaluationStartDate(performanceEvaluation.getEvaluationStartDate());
            newEvaluation.setEvaluationEndDate(performanceEvaluation.getEvaluationEndDate());
            newEvaluation.setLeavePerformance(performanceEvaluation.getLeavePerformance());
            newEvaluation.setAttendanceAccuracy(performanceEvaluation.getAttendanceAccuracy());
            newEvaluation.setEmployee(performanceEvaluation.getEmployee());
            newEvaluation.setDateCreated(Timestamp.from(Instant.now()));

            jpaApi.em().persist(newEvaluation);
        }
        return true;
    }

    @Override
    public boolean deleteEmployeeEvaluation(String performanceEvaluationId) {
        return new JPAQueryFactory(jpaApi.em())
                .delete(qJpaPerformanceEvaluation)
                .where(qJpaPerformanceEvaluation.performanceEvaluationId.eq(performanceEvaluationId))
                .execute() == 1;
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

    private List<JpaAttendance> getAllEmployeeAttendancesForTheDay(String employeeId, Date date) {
        Date startOfTheDay = DateUtility.getTheStartOfTheDay(date);
        Date endOfTheDay = DateUtility.getTheEndOfTheDay(date);

        return new JPAQueryFactory(jpaApi.em())
                .selectFrom(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
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

    private DailyPerformanceOverview calculateDailyPerformanceMetrics(String employeeId, Date date, int expectedDailyWorkingHours) {


        int totalCheckIns = 0;
        int totalCheckOuts = 0;
        double totalWorkingHours = 0;

        List<JpaAttendance> attendances = getAllEmployeeAttendancesForTheDay(employeeId, date);

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
        performanceOverview.setFirstCheckInTime((attendances.size() == 0) ? null : attendances.get(0).getCheckInTime().toLocalTime());

        return performanceOverview;

    }

    private List<DailyPerformanceOverview> calculateDailyPerformanceMetricBetweenDays(String employeeId, AuxDateRangeDTO auxDateRangeDTO, int expectedDailyWorkingHours) {
        LocalDate startDate, endDate;
        startDate = auxDateRangeDTO.startDate();
        endDate = auxDateRangeDTO.endDate();

        List<DailyPerformanceOverview> overviews = new ArrayList<>();
        List<LocalDate> workingDays = getAllWorkingDaysBetweenDateRange(startDate, endDate);

        if (auxDateRangeDTO.dateIsAscending()) {

            for (LocalDate date : workingDays) {
                DailyPerformanceOverview performanceOverview =
                        calculateDailyPerformanceMetrics(
                                employeeId,
                                DateUtility.convertToDate(date),
                                expectedDailyWorkingHours
                        );
                overviews.add(performanceOverview);
            }
        } else {
            for (int i = workingDays.size() - 1; i > -1; i--) {
                DailyPerformanceOverview performanceOverview =
                        calculateDailyPerformanceMetrics(
                                employeeId,
                                DateUtility.convertToDate(workingDays.get(i)),
                                expectedDailyWorkingHours
                        );
                overviews.add(performanceOverview);
            }
        }

        return overviews;
    }

    private double calculateLeavePerformance(JpaEmployee employee) {
        return 100 - (((double) employee.getLeaveDays() / employee.getEntitledYearlyLeaveDays()) * 100);
    }

    private double calculateAttendancePerformance(String employeeId, AuxDateRangeDTO auxDateRangeDTO, int expectedWorkingHours) {
        List<DailyPerformanceOverview> overviews = calculateDailyPerformanceMetricBetweenDays(employeeId, auxDateRangeDTO, expectedWorkingHours);
        double totalHoursWorked = 0;
        for (DailyPerformanceOverview overview: overviews) {
            totalHoursWorked += overview.getTotalHoursWorked();
        }

        if (overviews.size() == 0)
            return -1;

        return (totalHoursWorked / (expectedWorkingHours * overviews.size())) * 100;
    }

    /**
     * @author ALIU
     * @dateCreated 18/08/2023
     * @description Get JpaPerformanceEvaluation by ID
     *
     * @param performanceEvaluationId ID of the evaluation record to fetch
     *
     * @return JpaPerformanceEvaluation An evaluation record or null if not found
     */
    private JpaPerformanceEvaluation getJpaPerformanceEvaluationById(String performanceEvaluationId) {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaPerformanceEvaluation)
                .where(qJpaPerformanceEvaluation.performanceEvaluationId.eq(performanceEvaluationId))
                .fetchOne();
    }

    /**
     * @author ALIU
     * @dateCreated 18/08/2023
     * @description Get JpaPerformanceEvaluation by Date range
     *
     * @param employeeId Id of employee
     * @param startDate Evaluation starting Date
     * @param endDate Evaluation End Date
     *
     * @return JpaPerformanceEvaluation An evaluation record or null if not found
     */
    private JpaPerformanceEvaluation getJpaPerformanceEvaluationByDateRange(String employeeId, LocalDate startDate, LocalDate endDate) {
        List<JpaPerformanceEvaluation> pe = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaPerformanceEvaluation)
                .where(qJpaPerformanceEvaluation.employee.employeeId.eq(employeeId))
                .where(qJpaPerformanceEvaluation.evaluationStartDate.goe(DateUtility.convertToDate(startDate)))
                .where(qJpaPerformanceEvaluation.evaluationEndDate.loe(DateUtility.convertToDate(endDate)))
                .fetch();

        for (JpaPerformanceEvaluation evaluation : pe) {
            if (DateUtility.dateEquals(evaluation.getEvaluationStartDate(), startDate) &&
                            DateUtility.dateEquals(evaluation.getEvaluationEndDate(), endDate))
                return evaluation;
        }
        return null;
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
