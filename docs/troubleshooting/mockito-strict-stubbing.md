# Troubleshooting: Mockito UnnecessaryStubbingException

Symptoms:
- UnnecessaryStubbingException in tests

Cause:
- A stub defined in setup is not used by a test when using strict stubbing

Solutions:
- Move stubs to the specific tests that need them (preferred)
- Or switch to lenient stubbing selectively: `lenient().when(...).thenReturn(...)`
- Or set global relaxed strictness (not recommended) via Mockito settings

