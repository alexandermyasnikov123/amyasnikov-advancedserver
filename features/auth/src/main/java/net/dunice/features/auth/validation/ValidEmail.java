package net.dunice.features.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.dunice.features.auth.constants.AuthConstants;
import net.dunice.features.core.dtos.constants.ValidationMessages;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Size(
        min = AuthConstants.MIN_EMAIL_LENGTH,
        max = AuthConstants.MAX_EMAIL_LENGTH,
        message = ValidationMessages.EMAIL_SIZE_NOT_VALID
)
@NotNull(message = ValidationMessages.USER_EMAIL_NOT_NULL)
@NotBlank(message = ValidationMessages.USER_EMAIL_NOT_VALID)
@Email(message = ValidationMessages.USER_EMAIL_NOT_VALID)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
