package com.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target(
		{
				ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE_USE
		}
)
@Retention(RetentionPolicy.RUNTIME)
@Size(max = 32)
public @interface ValidatedByFactory {
}
