package com.example.scope;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class AopInCustomScopeTest {

	@Inject
	AopInCustomScope.MyBean customScopeInterfaceBean;

	@Inject
	AopInCustomScope.MyBeanImpl customScopeImplBean;

	@Inject
	BeanContext beanContext;

	@Test
	void aop_interfaceVsImplementation() {
		assertThat(customScopeImplBean.getCachedId())
				.isEqualTo(customScopeInterfaceBean.getCachedId());
	}

	@Test
	void aop_interfaceDoubleInvocation() {
		assertThat(customScopeInterfaceBean.getCachedId())
				.isEqualTo(customScopeInterfaceBean.getCachedId());
	}

	@Test
	void aop_implDoubleInvocation() {
		assertThat(customScopeImplBean.getCachedId())
				.isEqualTo(customScopeImplBean.getCachedId());
	}

	@Test
	void aop_interfaceVsBeanOfImplType() {
		var ofType = beanContext.getBeansOfType(AopInCustomScope.MyBeanImpl.class);

		assertThat(ofType)
				.singleElement()
				.returns(customScopeInterfaceBean.getCachedId(), AopInCustomScope.MyBean::getCachedId);
	}

	@Test
	void aop_interfaceVsBeanOfInterfaceType() {
		var ofType = beanContext.getBeansOfType(AopInCustomScope.MyBean.class);

		assertThat(ofType)
				.singleElement()
				.returns(customScopeInterfaceBean.getCachedId(), AopInCustomScope.MyBean::getCachedId);
	}

	@Test
	void aop_implVsBeanOfImplType() {
		var ofType = beanContext.getBeansOfType(AopInCustomScope.MyBeanImpl.class);

		assertThat(ofType)
				.singleElement()
				.returns(customScopeImplBean.getCachedId(), AopInCustomScope.MyBean::getCachedId);
	}
}
