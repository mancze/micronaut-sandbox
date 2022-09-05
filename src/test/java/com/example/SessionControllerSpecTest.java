package com.example;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class SessionControllerSpecTest {

	@Inject
	@Client(SessionController.BASE_PATH)
	HttpClient client;

	@Test
	void subsequentRequests_ShouldNotYieldSetCookieHeader() {
		var firstResponse = Mono.from(client.exchange(getRequest(), String.class))
				.blockOptional().orElseThrow();

		assertThat(firstResponse.header(HttpHeaders.SET_COOKIE))
				.describedAs(firstResponse.body())
				.isNotBlank();

		MutableHttpRequest<?> secondRequest = getRequest().cookies(firstResponse.getCookies().getAll());
		var secondResponse = Mono.from(client.exchange(secondRequest, String.class))
				.blockOptional().orElseThrow();

		assertThat(secondResponse.header(HttpHeaders.SET_COOKIE))
				.describedAs(secondResponse.body())
				.isNull();
	}

	private MutableHttpRequest<?> getRequest() {
		return HttpRequest.GET("/");
	}

}
