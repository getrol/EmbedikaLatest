FROM ubuntu:20.04
COPY . /home/EmbedikaProject
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
RUN apt install sqlite3 -y
WORKDIR /home/EmbedikaProject
CMD ["java", "-jar", "carservice.jar"]
