package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.runtime.Micronaut;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

	@Controller
	static class MyController {

		@Get()
		@Secured(SecurityRule.IS_ANONYMOUS)
		public String pub() {
			return "Hello, World!";
		}

	}

}
