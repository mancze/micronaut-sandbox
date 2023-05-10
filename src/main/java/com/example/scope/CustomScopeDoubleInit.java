package com.example.scope;

import io.micronaut.context.scope.AbstractConcurrentCustomScope;
import io.micronaut.context.scope.CreatedBean;
import io.micronaut.inject.BeanIdentifier;
import io.micronaut.runtime.context.scope.ThreadLocal;
import jakarta.inject.Inject;
import jakarta.inject.Scope;
import jakarta.inject.Singleton;

import java.lang.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class CustomScopeDoubleInit {

	static final Map<Class<?>, Integer> COUNTER = new ConcurrentHashMap<>();

	@Inject
	CustomScopeDoubleInit() {
	}

	static void objectCreated(Object object) {
		var objectNumber = COUNTER.merge(object.getClass(), 1, Integer::sum);
		System.out.printf("Created %s #%d (hashcode: %s)%n", object.getClass().getSimpleName(), objectNumber, object.hashCode());
	}

	interface Foo {

		double getId();
	}

	@ThreadLocal
	static class FooImpl implements Foo {
		public final double id = Math.random();

		@Inject
		FooImpl() {
			objectCreated(this);
		}

		@Override
		public double getId() {
			return id;
		}
	}

	interface Bar {
		double getId();
	}

	@MyScope
	static class BarImpl implements Bar {
		public final double id = Math.random();

		@Inject
		BarImpl() {
			objectCreated(this);
		}

		@Override
		public double getId() {
			return id;
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
