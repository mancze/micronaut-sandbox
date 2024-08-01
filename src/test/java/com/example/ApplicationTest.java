package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.cookie.Cookie;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class ApplicationTest {

	@Inject
	@Client("/")
	HttpClient client;

	@Test
	void get_shouldReturnHelloWorld() {
		var actual = client.toBlocking().retrieve("/", String.class);
		assertEquals("Hello, World!", actual);
	}

	/**
	 * Malformed or malicious inputs should not cause internal server error.
	 */
	@Test
	void get_shouldNotReturnInternalServerError_whenMalformedCookieIsSent() {
		var request = HttpRequest.GET("/").cookie(Cookie.of("SESSION", "_invalid_"));

		var responseException = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(request));
		assertNotEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseException.getStatus());
	}
}
