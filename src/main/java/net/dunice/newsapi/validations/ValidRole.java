package net.dunice.newsapi.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.dunice.newsapi.errors.ValidationConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(
        min = ValidRole.MIN_ROLE_LENGTH,
        max = ValidRole.MAX_ROLE_LENGTH,
        message = ValidationConstants.USER_ROLE_NOT_VALID
)
@NotBlank(message = ValidationConstants.USER_ROLE_NOT_NULL)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {
    int MIN_ROLE_LENGTH = 3;

    int MAX_ROLE_LENGTH = 25;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}