package com.esl.internship.staffsync.performance.evaluation.api;

import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.AuxDateRangeDTO;
import com.esl.internship.staffsync.performance.evaluation.model.AttendanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.DailyPerformanceOverview;

import java.util.List;

public interface IPerformanceEvaluationApi {

    AttendanceOverview getAttendanceSummaryOfEmployee(String employeeId, AuxDateRangeDTO auxDateRangeDTO);

    Response<DailyPerformanceOverview> getTheCurrentDayPerformanceOverview(String employeeId);

    List<DailyPerformanceOverview> getPerformanceOverviewBetweenTimePeriod(String employeeId, AuxDateRangeDTO auxDateRangeDTO);

}
