package actions;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.esl.internship.staffsync.authentication.impl.jwt.JwtUtil;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthAction extends Action.Simple {

    @Inject
    JwtUtil jwtUtil;

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
