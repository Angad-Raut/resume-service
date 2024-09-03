FROM openjdk:17
EXPOSE 1994
ADD target/resume-details.jar resume-details.jar
ENTRYPOINT ["java","-jar","resume-details.jar"]