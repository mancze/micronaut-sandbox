package com.example;

import com.example.validation.ValidInternalId;
import com.example.validation.ValidatedByFactory;
import io.micronaut.core.annotation.Introspected;

@Introspected
public record ValidatedBean(
		@ValidatedByFactory
		String value,
		@ValidInternalId
		int internalId) {
}
