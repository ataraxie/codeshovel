FROM openjdk:8u171

ARG REPO_DIR=/codeshovel-repos

VOLUME ["/usr/codeshovel/output", "${REPO_DIR}"]

ENV REPO_DIR=/codeshovel-repos

RUN apt-get update && apt-get -y install maven

COPY . /usr/codeshovel
WORKDIR /usr/codeshovel
RUN mvn verify
WORKDIR /usr/codeshovel/target/
CMD ["java", "-classpath", "*", "com.felixgrund.codeshovel.MiningTestJava"]
#CMD ["java", "target/test-classes/com/felixgrund/codeshovel/MiningTestJava.class"]
# /usr/codeshovel/target# java -classpath "*" com.felixgrund.codeshovel.MiningTestJava