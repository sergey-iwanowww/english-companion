FROM openjdk:17-jdk-slim as builder
COPY  './build/libs/telegram-bot-1.0.0-SNAPSHOT.jar' /spring-boot/app.jar

RUN cd /spring-boot && java -Djarmode=layertools -jar app.jar extract

FROM openjdk:17-jdk-slim

EXPOSE 8080

ENV LANG=ru_RU.UTF-8
ENV JAVA_ARGS -Djava.security.egd=file:/dev/./urandom -Duser.country=RU -Duser.language=ru --enable-preview

CMD java $JAVA_OPTIONS $JAVA_ARGS org.springframework.boot.loader.JarLauncher

COPY  --from=builder /spring-boot/dependencies/ ./
RUN echo '.'
COPY  --from=builder /spring-boot/spring-boot-loader/ ./
RUN echo '..'
COPY  --from=builder /spring-boot/snapshot-dependencies/ ./
RUN echo '...'
COPY  --from=builder /spring-boot/application/ ./


