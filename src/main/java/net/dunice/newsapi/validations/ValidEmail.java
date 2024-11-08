package net.dunice.newsapi.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.dunice.newsapi.constants.ValidationConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(
        min = ValidEmail.MIN_EMAIL_LENGTH,
        max = ValidEmail.MAX_EMAIL_LENGTH,
        message = ValidationConstants.EMAIL_SIZE_NOT_VALID
)
@NotNull(message = ValidationConstants.USER_EMAIL_NOT_NULL)
@Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    int MIN_EMAIL_LENGTH = 3;

    int MAX_EMAIL_LENGTH = 100;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
