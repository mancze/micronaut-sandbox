package com.example;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.session.Session;
import io.micronaut.session.http.CookieHttpSessionIdGenerator;
import io.micronaut.session.http.CookieHttpSessionStrategy;
import io.micronaut.session.http.HttpSessionConfiguration;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.Instant;

@Controller(SessionController.BASE_PATH)
@Secured(SecurityRule.IS_ANONYMOUS)
class SessionController {

	static final String BASE_PATH = "/session";

	@Inject
	SessionController() {
	}

	@Get
	String index(Session session) {
		if (!session.contains("footprint")) {
			session.put("footprint", Instant.now());
		}

		return "Hello World!<br>Your session started at %s.<br>Your session isNew: %s".formatted(
				session.get("footprint").orElse(null),
				session.isNew()
		);
	}

	@Singleton
	@Replaces(CookieHttpSessionStrategy.class)
	@Requires(property = "parameters.workaround", value = "true")
	static class NoReissuingCookieHttpSessionStrategy extends CookieHttpSessionStrategy {

		@Inject
		NoReissuingCookieHttpSessionStrategy(HttpSessionConfiguration configuration, CookieHttpSessionIdGenerator cookieHttpSessionIdGenerator) {
			super(configuration, cookieHttpSessionIdGenerator);
		}

		@Override
		public void encodeId(HttpRequest<?> request, MutableHttpResponse<?> response, Session session) {
			// TODO: better logic to determine if session cookie needs to be encoded into response
			// e.g. check if request's entry session is different (none, different id) from the session argument
			if (!session.contains("__sessionCookieEncoded")) {
				super.encodeId(request, response, session);
				session.put("__sessionCookieEncoded", true);
			}
		}
	}
}
