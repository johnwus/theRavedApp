# Config Server

Spring Cloud Config Server for TheRavedApp. Serves configuration from a Git repository in production and from the classpath (`resources/config/`) in development.

## Build
```
mvn -DskipTests -pl server/config-server package
```

## Run (local JVM)
Development (native files):
```
set SPRING_PROFILES_ACTIVE=development
mvn -DskipTests -pl server/config-server spring-boot:run
```

Production (Git backend):
```
set CONFIG_GIT_URI=https://github.com/johnwus/theRavedApp-config
set CONFIG_GIT_USERNAME=<github-username>
set CONFIG_GIT_PASSWORD=<github-PAT>
set SPRING_PROFILES_ACTIVE=production
mvn -DskipTests -pl server/config-server spring-boot:run
```

## Docker
Build image:
```
docker build -t raved/config-server:latest server/config-server
```

Run (development):
```
docker run -p 8888:8888 ^
  -e SPRING_PROFILES_ACTIVE=development ^
  raved/config-server:latest
```

Run (production, Git backend):
```
docker run -p 8888:8888 ^
  -e SPRING_PROFILES_ACTIVE=production ^
  -e CONFIG_GIT_URI=https://github.com/johnwus/theRavedApp-config ^
  -e CONFIG_GIT_USERNAME=<github-username> ^
  -e CONFIG_GIT_PASSWORD=<github-PAT> ^
  raved/config-server:latest
```

## Verify endpoints
- http://localhost:8888/user-service/dev
- http://localhost:8888/api-gateway/prod