## Micronaut Documentation

### Actual

```shell
sh gradlew clean test

# ...
# > Task :test
#
# SessionControllerSpecTest > subsequentRequests_ShouldNotYieldSetCookieHeader() FAILED
#     org.opentest4j.AssertionFailedError at SessionControllerSpecTest.java:37
#
# 2 tests completed, 1 failed
#
# > Task :test FAILED
# ...
```

### Expected

```shell
MICRONAUT_ENVIRONMENTS=workaround sh gradlew clean test

# ...
# BUILD SUCCESSFUL in 3s
# 4 actionable tasks: 1 executed, 3 up-to-date
# ...
```
