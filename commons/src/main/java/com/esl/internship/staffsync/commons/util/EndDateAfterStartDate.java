package com.esl.internship.staffsync.commons.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndDateAfterStartDateValidator.class)
@Documented
public @interface EndDateAfterStartDate {

    String message() default "End date must be after start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
