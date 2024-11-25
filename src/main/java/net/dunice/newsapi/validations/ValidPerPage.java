package net.dunice.newsapi.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import net.dunice.newsapi.constants.ValidationConstants;
import net.dunice.newsapi.entities.NewsEntity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Positive(message = ValidationConstants.PER_PAGE_MIN_NOT_VALID)
@Max(value = NewsEntity.MAX_PER_PAGE_NEWS, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100)
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPerPage {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

