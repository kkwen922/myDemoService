FROM java:8
VOLUME /tmp
ADD HelloHostService-1.0-SNAPSHOT.jar hello.jar
ENTRYPOINT ["java","-jar","/hello.jar"]
EXPOSE 9990