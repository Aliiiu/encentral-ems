package com.esl.internship.staffsync.performance.evaluation.api;

import com.esl.internship.staffsync.performance.evaluation.dto.DateFilterDTO;
import com.esl.internship.staffsync.performance.evaluation.model.AttendanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.DailyPerformanceOverview;

import java.util.List;

public interface IPerformanceEvaluationApi {

    AttendanceOverview getAttendanceSummaryOfEmployee(String employeeId, DateFilterDTO dateFilterDTO);

    DailyPerformanceOverview getTheCurrentDayPerformanceOverview(String employeeId);

    List<DailyPerformanceOverview> getPerformanceOverviewBetweenTimePeriod(String employeeId, DateFilterDTO dateFilterDTO);

}
