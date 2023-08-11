package security;


import actions.AuthAction;
import com.esl.internship.staffsync.authentication.model.permissions.RequiredPermissions;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(AuthAction.class)
@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WebAuth {

    RequiredPermissions[] permissions();

    boolean requireEmployeeId() default false;
}
