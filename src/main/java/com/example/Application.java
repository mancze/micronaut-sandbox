package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.runtime.Micronaut;
import org.reactivestreams.Publisher;

public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
	}

	@Controller
	public static class MyController {
		@Post("/greed")
		@Consumes(MediaType.ALL)
		String greed(@Body String who) {
			return "Hello, %s!".formatted(who);
		}

		@Post("/greed-filtered")
		@Consumes(MediaType.ALL)
		String greedFiltered(@Body String who) {
			return "Hello, %s!".formatted(who);
		}
	}

	@Filter(patterns = "/greed-filtered")
	public static class MyFilter implements HttpServerFilter {
		@Override
		public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
			return chain.proceed(request.mutate());
		}
	}

}
