package net.dunice.newsapi.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Positive;
import net.dunice.newsapi.constants.ValidationConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Positive(message = ValidationConstants.PAGE_SIZE_NOT_VALID)
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPage {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
