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

@NotBlank(message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
@Size(
        min = ValidNewsImage.MIN_IMAGE_LENGTH,
        max = ValidNewsImage.MAX_IMAGE_LENGTH,
        message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT
)
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNewsImage {
    int MIN_IMAGE_LENGTH = 3;

    int MAX_IMAGE_LENGTH = 130;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
