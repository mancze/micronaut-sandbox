package com.example;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.runtime.Micronaut;

import java.util.List;
import java.util.Optional;

public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

	@Controller
	public static class MyController {

		@Get("/list-nullable")
		String list(
				@QueryValue(value = "v", defaultValue = "")
				@Nullable List<String> values
		) {
			return "values == null: %s\n".formatted(values == null)
					+ "values: %s\n".formatted(values)
					+ "values.size(): %s".formatted(values == null ? "N/A" : values.size());
		}

		@Get("/list-optional")
		String list(
				@QueryValue(value = "v", defaultValue = "")
				Optional<List<String>> values
		) {
			return "values.isPresent(): %s\n".formatted(values.isPresent())
					+ "values.orElse(null): %s\n".formatted(values.orElse(null))
					+ "values.size(): %s".formatted(values.map(List::size).map(Object::toString).orElse("N/A"));
		}
	}

}
