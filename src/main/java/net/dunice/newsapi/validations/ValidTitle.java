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

@NotBlank(message = ValidationConstants.NEWS_TITLE_NOT_NULL)
@Size(
        min = ValidTitle.MIN_TITLE_LENGTH,
        max = ValidTitle.MAX_TITLE_LENGTH,
        message = ValidationConstants.NEWS_TITLE_SIZE
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTitle {
    int MIN_TITLE_LENGTH = 3;

    int MAX_TITLE_LENGTH = 160;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}