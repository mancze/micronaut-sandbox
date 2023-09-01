package com.example;

import io.micronaut.http.ssl.DefaultSslConfiguration;
import io.micronaut.http.ssl.SslConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(startApplication = false)
class ApplicationTest {

	@Inject
	DefaultSslConfiguration defaultSslConfiguration;

	@Inject
	Application.ReplacedKeyStoreConfiguration keyStoreConfiguration;

	@Test
	void keyStoreConfiguration_shouldHaveExpectedValues() {
		assertThat(keyStoreConfiguration.getClass().getName()).isEqualTo("com.example.Application$ReplacedKeyStoreConfiguration");
		assertKeyStoreConfiguration(keyStoreConfiguration);
	}

	@Test
	void defaultSslConfiguration_shouldUseReplacedBean() {
		assertThat(defaultSslConfiguration.getKeyStore().getClass().getName()).isEqualTo("com.example.Application$ReplacedKeyStoreConfiguration");
	}

	private void assertKeyStoreConfiguration(SslConfiguration.KeyStoreConfiguration keyStoreConfiguration1) {
		assertThat(keyStoreConfiguration1.getPath()).hasValue("classpath:keystore.p12");
		assertThat(keyStoreConfiguration1.getPassword()).hasValue("1234");
	}
}
