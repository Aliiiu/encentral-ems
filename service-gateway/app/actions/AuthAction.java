package actions;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.esl.internship.staffsync.authentication.impl.jwt.JwtUtil;
import com.esl.internship.staffsync.authentication.model.permissions.RequiredPermissions;
import play.db.jpa.Transactional;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import security.WebAuth;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author DEMILADE
 * @dateCreated 09/08/2023
 * @description Authentication Action class
 */
@Transactional
public class AuthAction extends Action.Simple {

    @Inject
    JwtUtil jwtUtil;

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
        try {
            DecodedJWT jwt = jwtUtil.verifyToken(token);
            Date expirationDate = jwt.getExpiresAt();

            if (expirationDate == null || expirationDate.before(new Date())) {
                return CompletableFuture.completedFuture(unauthorized("Token has expired"));
            }

            String employeeId = jwt.getClaim("employeeId").asString();
            //Get employee object

            //Check if user is active

            //return CompletableFuture.completedFuture(forbidden("User is not active"));


            //Get Roles and permissions from employee object

            //Get List of permissions from @WebAuth
            WebAuth webAuth = WebAuth.class.getAnnotation(WebAuth.class);
            RequiredPermissions[] requiredPermissions = webAuth.permissions();

            String[] permissions = Arrays.stream(requiredPermissions)
                    .map(RequiredPermissions::name)
                    .toArray(String[]::new);

            //Check if employee object has all permissions
            //permissions.containsAll(Arrays.asList(requiredPermissions))
//            if (true){
//
//            }
//            else {
//                return CompletableFuture.completedFuture(forbidden("Unauthorized access"));
//            }

            String firstname = jwt.getClaim("firstname").asString();
            String lastname = jwt.getClaim("lastname").asString();
            String roleId = jwt.getClaim("roleId").asString();
            String employeeMail = jwt.getClaim("employeeEmail").asString();


            ctx.args.put("employeeId", employeeId);
            ctx.args.put("firstname", firstname);
            ctx.args.put("lastname", lastname);
            ctx.args.put("roleId", roleId);
            ctx.args.put("employeeMail", employeeMail);

            return delegate.call(ctx);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(unauthorized("Invalid token"));
        }
    }
}
