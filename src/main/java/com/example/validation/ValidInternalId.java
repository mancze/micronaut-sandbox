package com.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target(
		{
				ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE_USE
		}
)
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Positive
@Max(Integer.MAX_VALUE)
public @interface ValidInternalId {
}
