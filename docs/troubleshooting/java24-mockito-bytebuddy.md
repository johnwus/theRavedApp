# Troubleshooting: Mockito and Java 24 (Byte Buddy) issues

Symptoms:
- `Mockito cannot mock this class` errors
- Byte Buddy message: `Java 24 (68) is not supported by the current version of Byte Buddy`

Workarounds:
- Prefer fakes/test doubles instead of inline mocks for problematic classes
- Inject via reflection if fields are not exposed
- Alternatively, downgrade test JDK to 21 (LTS) for full compatibility, or upgrade Byte Buddy/Mockito when available

Commands used to verify:
- `mvn -DfailIfNoTests=false test`

Related fixes:
- See ../fixes/2025-08-15-user-service-tests-java24-mockito.md

