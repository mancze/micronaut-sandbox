package com.example;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.*;

public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

	@Controller
	static class MyController {
		@Get
		MyBean bean(@ValidInternalId int internalId) {
			return new MyBean(internalId);
		}
	}

	@Serdeable
	@Introspected
	public record MyBean(@ValidInternalId int internalId) {
	}

	@Documented
	@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
	@Retention(RetentionPolicy.RUNTIME)
	@NotNull
	@Positive
	@Max(Integer.MAX_VALUE)
	public @interface ValidInternalId {
	}

}
