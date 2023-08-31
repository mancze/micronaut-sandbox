package com.example;

import io.micronaut.context.annotation.*;
import io.micronaut.http.ssl.DefaultSslConfiguration;
import io.micronaut.http.ssl.SslConfiguration;
import io.micronaut.runtime.Micronaut;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.function.Function;

public class Application {

	private static final Function<String, String> DECRYPTOR = (encrypted) -> encrypted.equals("_encrypted_") ? "1234" : encrypted;

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

	@Context
	static class KeyStoreDumper {
		KeyStoreDumper(ReplacedKeyStoreConfiguration storeConfiguration) {
			System.out.printf("keystore bean: %s%n", storeConfiguration.getClass().getName());
			System.out.printf("keystore path: %s%n", storeConfiguration.getPath().orElse(null));
			System.out.printf("keystore password: %s%n", storeConfiguration.getPassword().orElse(null));
		}
	}

	@Secondary
	@BootstrapContextCompatible
	@ConfigurationProperties("micronaut.ssl.key-store")
	@Replaces(DefaultSslConfiguration.DefaultKeyStoreConfiguration.class)
	static class ReplacedKeyStoreConfiguration extends DefaultSslConfiguration.DefaultKeyStoreConfiguration {

		@Inject
		ReplacedKeyStoreConfiguration() {
		}

		@Override
		public Optional<String> getPassword() {
			return super.getPassword()
					.map(DECRYPTOR);
		}
	}

	@Primary
	@BootstrapContextCompatible
	@ConfigurationProperties("micronaut.ssl.key-store")
	@Replaces(DefaultSslConfiguration.DefaultKeyStoreConfiguration.class)
	static class ExtendBaseClassKeyStoreConfiguration extends SslConfiguration.KeyStoreConfiguration {
		@Inject
		ExtendBaseClassKeyStoreConfiguration() {
		}

		@Override
		public Optional<String> getPassword() {
			return super.getPassword()
					.map(DECRYPTOR);
		}
	}
}
