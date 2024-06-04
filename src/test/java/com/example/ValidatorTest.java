package com.example;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(rebuildContext = true)
public class ValidatorTest {
	@Inject
	@Client("/")
	HttpClient client;

	@Test
	@Property(name = "micronaut.validator.enabled", value = "false")
	void getBean_withValidationDisabled() {
		testIt();
	}

	@Test
	@Property(name = "micronaut.validator.enabled", value = "true")
	void getBean_withValidationEnabled() {
		testIt();
	}

	void testIt() {
		var bean = client.toBlocking().retrieve(HttpRequest.GET("?internalId=42"), Application.MyBean.class);

		assertThat(bean.internalId()).isEqualTo(42);
	}
}
