package com.example;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest(rebuildContext = true, startApplication = false)
public class ValidatorTest {

	@Inject
	Validator validator;

	@Test
	void BUG_REPRODUCTION_CASE_validateValidBean_shouldNotFail() {
		var bean = new ValidatedBean("foobar", 42);

		assertThat(validator.validate(bean)).isEmpty();
	}

	@Test
	void validateValidBean_workaround() {
		var validBean = new ValidatedBean("foobar", 42);

		warmUpValidator(validBean);

		assertThat(validator.validate(validBean)).isEmpty();
	}

	@Test
	void validateInvalidBean_shouldFail() {
		var invalidBean = new ValidatedBean("foobar", -123);

		warmUpValidator(invalidBean);

		assertThat(validator.validate(invalidBean))
				.singleElement()
				.returns(-123, ConstraintViolation::getInvalidValue);
	}


	/**
	 * Workaround for the bug
	 */
	private void warmUpValidator(ValidatedBean validBean) {
		try {
			validator.validate(validBean);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
