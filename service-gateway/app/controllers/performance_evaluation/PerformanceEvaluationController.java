package controllers.performance_evaluation;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.commons.service.response.Response;
import com.esl.internship.staffsync.commons.util.AuxDateRangeDTO;
import com.esl.internship.staffsync.commons.util.MyObjectMapper;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.performance.evaluation.api.IPerformanceEvaluationApi;
import com.esl.internship.staffsync.performance.evaluation.dto.EvaluationFeedbackDTO;
import com.esl.internship.staffsync.performance.evaluation.model.AttendanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.DailyPerformanceOverview;
import com.esl.internship.staffsync.performance.evaluation.model.PerformanceEvaluation;
import io.swagger.annotations.*;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static com.esl.internship.staffsync.commons.util.ImmutableValidator.validate;

@Api(value = "Performance Evaluation")
@Transactional
public class PerformanceEvaluationController extends Controller {

    @Inject
    IPerformanceEvaluationApi iPerformanceEvaluationApi;

    @Inject
    IEmployeeApi iEmployeeApi;

    @Inject
    MyObjectMapper objectMapper;

    @Inject
    IAuthentication iAuthentication;

    @ApiOperation("Get Attendance Overview")
    @ApiResponses({
            @ApiResponse(code = 200, response = AttendanceOverview.class, message = "Attendance Overview")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Date Range DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.commons.util.AuxDateRangeDTO",
                    dataTypeClass = AuxDateRangeDTO.class
            ),
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAttendanceSummaryOfEmployee() {

        final var dateRangeDTOForm = validate(request().body().asJson(), AuxDateRangeDTO.class);

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        if (dateRangeDTOForm.hasError) {
            return badRequest(dateRangeDTOForm.error);
        }

        return ok(objectMapper.toJsonString(iPerformanceEvaluationApi.getAttendanceSummaryOfEmployee(employee.getEmployeeId(), dateRangeDTOForm.value)));
    }

    @ApiOperation("Get current day Performance Overview")
    @ApiResponses({
            @ApiResponse(code = 200, response = DailyPerformanceOverview.class, message = "Performance Overview")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin, RouteRole.user})
    public Result getCurrentDayPerformanceOverview() {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();
        Response<DailyPerformanceOverview> response = iPerformanceEvaluationApi.getTheCurrentDayPerformanceOverview(employee.getEmployeeId());

        if (response.requestHasErrors())
            return badRequest(response.getErrorsAsJsonString());
       return ok(objectMapper.toJsonString(response.getValue()));
    }

    @ApiOperation("Get Performance Overview between Date Range")
    @ApiResponses({
            @ApiResponse(code = 200, response = DailyPerformanceOverview.class, message = "Performance Overview", responseContainer = "List")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Date Range DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.commons.util.AuxDateRangeDTO",
                    dataTypeClass = AuxDateRangeDTO.class
            ),
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin, RouteRole.user})
    public Result getPerformanceOverviewBetweenTimePeriod() {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        final var dateRangeDTOForm = validate(request().body().asJson(), AuxDateRangeDTO.class);

        if (dateRangeDTOForm.hasError) {
            return badRequest(dateRangeDTOForm.error);
        }
        return ok(objectMapper.toJsonString(iPerformanceEvaluationApi.getPerformanceOverviewBetweenTimePeriod(employee.getEmployeeId(), dateRangeDTOForm.value)));
    }

    @ApiOperation("Get Performance Evaluation between date range")
    @ApiResponses({
            @ApiResponse(code = 200, response = PerformanceEvaluation.class, message = "Performance Evaluation")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Date Range DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.commons.util.AuxDateRangeDTO",
                    dataTypeClass = AuxDateRangeDTO.class
            ),
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin})
    public Result getEmployeePerformanceBetweenDatePeriod(String employeeId) {
        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        final var dateRangeDTOForm = validate(request().body().asJson(), AuxDateRangeDTO.class);

        if (dateRangeDTOForm.hasError) {
            return badRequest(dateRangeDTOForm.error);
        }

        PerformanceEvaluation evaluation = iPerformanceEvaluationApi.getEmployeePerformanceBetweenDatePeriod(employee.getEmployeeId(), dateRangeDTOForm.value);

        return ok(objectMapper.toJsonString(evaluation));
    }


    @ApiOperation("Get Performance Evaluation by Id")
    @ApiResponses({
            @ApiResponse(code = 200, response = PerformanceEvaluation.class, message = "Performance Evaluation")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin})
    public Result getEmployeeEvaluationById(String performanceEvaluationId) {

        Optional<PerformanceEvaluation> response = iPerformanceEvaluationApi.getEmployeeEvaluationById(performanceEvaluationId);
        return response.map(performanceEvaluation -> ok(objectMapper.toJsonString(performanceEvaluation))).orElseGet(() -> notFound("Performance Evaluation record not found"));

    }

    @ApiOperation("Get last Performance Evaluation")
    @ApiResponses({
            @ApiResponse(code = 200, response = PerformanceEvaluation.class, message = "Performance Evaluation")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin, RouteRole.user})
    public Result getLastEmployeeEvaluation() {

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        Optional<PerformanceEvaluation> response = iPerformanceEvaluationApi.getLastEmployeeEvaluationBy(employee.getEmployeeId());
        return response.map(performanceEvaluation -> ok(objectMapper.toJsonString(performanceEvaluation))).orElseGet(() -> notFound("No Performance Evaluation found"));

    }

    @ApiOperation("Get All Performance Evaluation of Employee")
    @ApiResponses({
            @ApiResponse(code = 200, response = PerformanceEvaluation.class, message = "Performance Evaluation")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin, RouteRole.user})
    public Result getAllEmployeeEvaluations() {

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        List<PerformanceEvaluation> response = iPerformanceEvaluationApi.getAllEmployeeEvaluations(employee.getEmployeeId());

        return ok(objectMapper.toJsonString(response));

    }

    @ApiOperation("Get All Performance Evaluation for an Employee (Admin)")
    @ApiResponses({
            @ApiResponse(code = 200, response = PerformanceEvaluation.class, message = "Performance Evaluation")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin})
    public Result getAllEmployeeEvaluationsOf(String employeeId) {

        List<PerformanceEvaluation> response = iPerformanceEvaluationApi.getAllEmployeeEvaluations(employeeId);

        return ok(objectMapper.toJsonString(response));

    }

    @ApiOperation("Post feedback on Performance Evaluation")
    @ApiResponses({
            @ApiResponse(code = 200, response = boolean.class, message = "Feedback Posted")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "body",
                    value = "Date Range DTO",
                    paramType = "body",
                    required = true,
                    dataType = "com.esl.internship.staffsync.performance.evaluation.dto.EvaluationFeedbackDTO",
                    dataTypeClass = EvaluationFeedbackDTO.class
            ),
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin})
    public Result postFeedbackOnEvaluation(String performanceEvaluationId) {

        Employee employee = iAuthentication.getContextCurrentEmployee().orElseThrow();

        final var evaluationFeedbackDTOForm = validate(request().body().asJson(), EvaluationFeedbackDTO.class);

        if (evaluationFeedbackDTOForm.hasError) {
            return badRequest(evaluationFeedbackDTOForm.error);
        }
        return ok(objectMapper.toJsonString(iPerformanceEvaluationApi.postFeedbackOnEvaluation(performanceEvaluationId, employee.getEmployeeId(),evaluationFeedbackDTOForm.value)));
    }

    @ApiOperation("Delete Evaluation")
    @ApiResponses({
            @ApiResponse(code = 200, response = boolean.class, message = "Feedback Deleted")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Authorization",
                    value = "Authorization",
                    paramType = "header",
                    required = true,
                    dataType = "string",
                    dataTypeClass = String.class
            )
    })
    @WebAuth(permissions = {RoutePermissions.read_attendance}, roles = {RouteRole.admin})
    public Result deleteEmployeeEvaluation(String performanceEvaluationId) {

        return ok(objectMapper.toJsonString(iPerformanceEvaluationApi.deleteEmployeeEvaluation(performanceEvaluationId)));
    }

}
