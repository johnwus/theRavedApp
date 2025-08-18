# Error: Mockito cannot mock class on Java 24 (Byte Buddy support)

- Service: user-service
- Context: Running unit tests on JDK 24
- Symptoms:
  - Mockito cannot mock this class: com.raved.user.security.JwtTokenProvider
  - Mockito cannot mock this class: com.raved.user.event.UserEventPublisher
  - Mockito cannot mock this class: com.raved.user.util.PasswordEncoderUtil
  - Root cause: Mockito inline mock maker uses Byte Buddy that doesn't yet fully support Java 24 classes for certain inlining cases.

## Stack highlights
- Could not modify all classes [java.lang.Object, ...]
- Byte Buddy could not instrument all classes; Java 24 (68) not supported by current Byte Buddy

## Affected tests
- AuthServiceImplTest
- ProfileServiceImplTest


