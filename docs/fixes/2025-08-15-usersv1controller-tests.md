# Fix: UsersV1Controller tests

- Removed broad stubbing from @BeforeEach to avoid UnnecessaryStubbingException
- Added per-test stubbing of userService.findUserByUsernameOrEmail("testuser") for tests that call controller
- Verified by running `mvn -DfailIfNoTests=false test` (module: user-service): all tests PASS

