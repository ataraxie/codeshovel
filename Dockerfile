FROM openjdk:8u171

ARG REPO_DIR=/codeshovel-repos
ARG OUTPUT_DIR=/usr/codeshovel/output

VOLUME ["${REPO_DIR}", "${OUTPUT_DIR}"]

# Do NOT override these values
# You can set where the files are stored on the host using --volume (-v) parameters
ENV REPO_DIR "${REPO_DIR}"
ENV OUTPUT_DIR "${OUTPUT_DIR}"

RUN apt-get update && apt-get -y install maven

COPY . /usr/codeshovel
WORKDIR /usr/codeshovel
RUN mvn verify

CMD cd "${REPO_DIR}/${REPO}" && find . -type f -name "*.java" -exec bash -c 'export TARGET_FILE_PATH="${1:2}" && java -classpath "/usr/codeshovel/target/*" com.felixgrund.codeshovel.MiningTestJava' - {} \;

# WORKDIR /usr/codeshovel/target/
# CMD ["java", "-classpath", "*", "com.felixgrund.codeshovel.MiningTestJava"]
