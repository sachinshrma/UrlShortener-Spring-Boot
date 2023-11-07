FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; java -Djarmode=layertools -jar ../*.jar extract)

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
ARG EXTRACTED=target/dependency
COPY --from=build ${EXTRACTED}/dependencies/ ./
COPY --from=build ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]