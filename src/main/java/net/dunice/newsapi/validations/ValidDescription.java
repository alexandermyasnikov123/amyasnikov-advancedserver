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

@NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_NOT_NULL)
@Size(
        min = ValidDescription.MIN_DESCRIPTION_LENGTH,
        max = ValidDescription.MAX_DESCRIPTION_LENGTH,
        message = ValidationConstants.NEWS_DESCRIPTION_SIZE
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDescription {
    int MIN_DESCRIPTION_LENGTH = 3;

    int MAX_DESCRIPTION_LENGTH = 160;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
