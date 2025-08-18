# Error: UsersV1Controller tests failures

- Context: Unit tests on user-service
- Symptoms:
  - UnnecessaryStubbingException from Mockito in UsersV1ControllerTest
  - RuntimeException("Authenticated user not found") during updateCurrentProfile and uploadAvatar tests

## Cause
- Strict stubbing complains if a stub set in @BeforeEach is unused in a test
- Tests invoking controller methods must stub userService.findUserByUsernameOrEmail(principalName) per-test; otherwise controller throws

## Affected files
- src/test/java/com/raved/user/controller/v1/UsersV1ControllerTest.java

