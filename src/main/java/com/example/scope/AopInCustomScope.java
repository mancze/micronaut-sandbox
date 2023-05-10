package com.example.scope;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.context.scope.AbstractConcurrentCustomScope;
import io.micronaut.context.scope.CreatedBean;
import io.micronaut.inject.BeanIdentifier;
import jakarta.inject.Inject;
import jakarta.inject.Scope;
import jakarta.inject.Singleton;

import java.lang.annotation.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class AopInCustomScope {

	@Inject
	AopInCustomScope() {
	}

	@DefaultImplementation(MyBeanImpl.class)
	interface MyBean {
		UUID getCachedId();
	}

	@MyScope
	@CacheConfig("my")
	static class MyBeanImpl implements MyBean {

		@Inject
		MyBeanImpl() {
		}

		@Cacheable
		public UUID getCachedId() {
			return UUID.randomUUID();
		}
	}

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
	@Scope
	@interface MyScope {
	}

	/**
	 * Scopes lives as singleton bean itself so all beans under this custom scope are expected to exist as singletons - single instance should be created.
	 */
	@Singleton
	static class MyCustomScope extends AbstractConcurrentCustomScope<MyScope> {
		private final ConcurrentHashMap<BeanIdentifier, CreatedBean<?>> scopeMap = new ConcurrentHashMap<>();

		@Inject
		MyCustomScope() {
			super(MyScope.class);
		}

		@Override
		protected Map<BeanIdentifier, CreatedBean<?>> getScopeMap(boolean forCreation) {
			return scopeMap;
		}

		@Override
		public boolean isRunning() {
			return true;
		}

		@Override
		public void close() {
			scopeMap.clear();
		}
	}
}
