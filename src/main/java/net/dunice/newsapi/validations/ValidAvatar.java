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

@NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
@Size(
        min = ValidAvatar.MIN_AVATAR_LENGTH,
        max = ValidAvatar.MAX_AVATAR_LENGTH,
        message = ValidationConstants.USER_AVATAR_NOT_VALID
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAvatar {
    int MIN_AVATAR_LENGTH = 3;

    int MAX_AVATAR_LENGTH = 130;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
