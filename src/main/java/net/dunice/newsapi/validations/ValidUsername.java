package net.dunice.newsapi.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import net.dunice.newsapi.constants.ValidationConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(
        min = ValidUsername.MIN_USERNAME_LENGTH,
        max = ValidUsername.MAX_USERNAME_LENGTH,
        message = ValidationConstants.USERNAME_SIZE_NOT_VALID
)
@NotBlank(message = ValidationConstants.USERNAME_HAS_TO_BE_PRESENT)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    int MIN_USERNAME_LENGTH = 3;

    int MAX_USERNAME_LENGTH = 25;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
