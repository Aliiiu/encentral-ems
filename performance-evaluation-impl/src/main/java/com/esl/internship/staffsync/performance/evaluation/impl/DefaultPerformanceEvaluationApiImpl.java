package com.esl.internship.staffsync.performance.evaluation.impl;

import com.esl.internship.staffsync.attendance.tracking.api.IAttendanceTracking;
import com.esl.internship.staffsync.entities.JpaAttendance;
import com.esl.internship.staffsync.entities.QJpaAttendance;
import com.esl.internship.staffsync.performance.evaluation.api.IPerformanceEvaluationApi;
import com.esl.internship.staffsync.performance.evaluation.dto.DateFilterDTO;
import com.esl.internship.staffsync.performance.evaluation.model.AttendanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.DailyPerformanceOverview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class DefaultPerformanceEvaluationApiImpl implements IPerformanceEvaluationApi {

    @Inject
    IAttendanceTracking iAttendanceTrackingApi;

    @Inject
    JPAApi jpaApi;

    private static final QJpaAttendance qJpaAttendance = QJpaAttendance.jpaAttendance;

    @Override
    public AttendanceOverview getAttendanceSummaryOfEmployee(String employeeId, DateFilterDTO dateFilterDTO) {
        return null;
    }

    @Override
    public DailyPerformanceOverview getTheCurrentDayPerformanceOverview(String employeeId) {
        return null;
    }

    @Override
    public List<DailyPerformanceOverview> getPerformanceOverviewBetweenTimePeriod(String employeeId, DateFilterDTO dateFilterDTO) {
        return null;
    }

    private void getAttendanceBetweenDateRange(String employeeId, Date startDate, Date endDate) {
        new JPAQueryFactory(jpaApi.em())
                .selectDistinct(qJpaAttendance.attendanceDate)
                .from(qJpaAttendance)
                .where(qJpaAttendance.employee.employeeId.eq(employeeId))
                .where(qJpaAttendance.attendanceDate.goe(startDate))
                .where(qJpaAttendance.attendanceDate.loe(endDate))
                .where(qJpaAttendance.checkInTime.isNotNull())
                .fetch();
    }
}
