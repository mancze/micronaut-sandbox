package com.example.scope;

import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class CustomScopeDoubleInitTest {

	@Inject
	CustomScopeDoubleInit.Foo threadLocalInterfaceBean;

	@Inject
	CustomScopeDoubleInit.FooImpl threadLocalImplBean;

	@Inject
	CustomScopeDoubleInit.Bar customScopeInterfaceBean;

	@Inject
	CustomScopeDoubleInit.BarImpl customScopeImplBean;

	@Inject
	BeanContext beanContext;

	@AfterEach
	void tearDown() {
		CustomScopeDoubleInit.COUNTER.clear();
	}

	@Test
	void threadLocal_interfaceVsImplementation() {
		assertThat(threadLocalImplBean.getId())
				.isEqualTo(threadLocalInterfaceBean.getId());
	}

	@Test
	void threadLocal_interfaceVsBeanOfImplType() {
		var ofType = beanContext.getBeansOfType(CustomScopeDoubleInit.FooImpl.class);

		assertThat(ofType)
				.singleElement()
				.returns(threadLocalInterfaceBean.getId(), CustomScopeDoubleInit.Foo::getId);
	}

	@Test
	void threadLocal_interfaceVsBeanOfInterfaceType() {
		var ofType = beanContext.getBeansOfType(CustomScopeDoubleInit.Foo.class);

		assertThat(ofType)
				.singleElement()
				.returns(threadLocalInterfaceBean.getId(), CustomScopeDoubleInit.Foo::getId);
	}

	@Test
	void threadLocal_implVsBeanOfImplType() {
		var ofType = beanContext.getBeansOfType(CustomScopeDoubleInit.FooImpl.class);

		assertThat(ofType)
				.singleElement()
				.returns(threadLocalImplBean.getId(), CustomScopeDoubleInit.Foo::getId);
	}

	@Test
	void customScope_interfaceVsImplementation() {
		assertThat(customScopeImplBean.getId())
				.isEqualTo(customScopeInterfaceBean.getId());
	}

	@Test
	void customScope_interfaceVsBeanOfImplType() {
		var ofType = beanContext.getBeansOfType(CustomScopeDoubleInit.BarImpl.class);

		assertThat(ofType)
				.singleElement()
				.returns(customScopeInterfaceBean.getId(), CustomScopeDoubleInit.Bar::getId);
	}

	@Test
	void customScope_interfaceVsBeanOfInterfaceType() {
		var ofType = beanContext.getBeansOfType(CustomScopeDoubleInit.Bar.class);

		assertThat(ofType)
				.singleElement()
				.returns(customScopeInterfaceBean.getId(), CustomScopeDoubleInit.Bar::getId);
	}

	@Test
	void customScope_implVsBeanOfImplType() {
		var ofType = beanContext.getBeansOfType(CustomScopeDoubleInit.BarImpl.class);

		assertThat(ofType)
				.singleElement()
				.returns(customScopeImplBean.getId(), CustomScopeDoubleInit.Bar::getId);
	}
}
