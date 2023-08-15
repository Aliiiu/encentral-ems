package actions;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.esl.internship.staffsync.authentication.impl.jwt.JwtUtil;
import com.esl.internship.staffsync.authentication.model.RoutePermissions;
import com.esl.internship.staffsync.authentication.model.RouteRole;
import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import security.WebAuth;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author DEMILADE
 * @dateCreated 09/08/2023
 * @description Authentication Action class
 */
@Transactional
public class AuthAction extends Action<WebAuth> {

    @Inject
    JwtUtil jwtUtil;

    @Inject
    JPAApi jpaApi;

    @Inject
    IEmployeeApi iEmployeeApi;

    @Inject
    IAuthentication iAuthentication;




    /**
     * @param ctx Http Context object
     * @return Async Result
     * @author Demilade
     * @dateCreated 09/08/2023
     * @description Method to add current user employee details to context object
     */
    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        Optional<String> tokenOptional = ctx.request().getHeaders().get("Authorization");

        if (tokenOptional.isEmpty()) {
            return CompletableFuture.completedFuture(unauthorized("Invalid token"));
        }

        String token = tokenOptional.get();
        CompletableFuture<Result> completableFuture = jpaApi.withTransaction(()->{
            try {
                DecodedJWT jwt = jwtUtil.verifyToken(token);
                Date expirationDate = jwt.getExpiresAt();

                if (expirationDate == null || expirationDate.before(new Date())) {
                    return CompletableFuture.completedFuture(unauthorized("Token has expired"));
                }

                String employeeId = jwt.getClaim("employeeId").asString();
                Employee employee = iEmployeeApi.getEmployeeById(employeeId).orElseThrow();
                if (!employee.getEmployeeActive()) {
                    return CompletableFuture.completedFuture(forbidden("Employee's account is currently restricted"));
                }

                List<RoutePermissions> employeePermissions = iAuthentication.getCurrentEmployeePermissions(employeeId);
                List<RoutePermissions> requiredPermissions = new ArrayList<>(Arrays.asList(configuration.permissions()));
                List<RouteRole> requiredRoles = new ArrayList<>(Arrays.asList(configuration.roles()));
                boolean hasRequiredPermissions = employeePermissions.containsAll(requiredPermissions);

                if ( !hasRequiredPermissions || !requiredRoles.contains(employee.getRoleId()) ) {
                    return CompletableFuture.completedFuture(unauthorized("Unauthorized access"));
                }
                ctx.args.put("currentEmployee", employee);
                return delegate.call(ctx);
            } catch (Exception e) {
                return CompletableFuture.completedFuture(unauthorized("Invalid token"));
            }
        }).toCompletableFuture();
        return completableFuture;
    }
}
