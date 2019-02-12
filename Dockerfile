FROM openjdk:8u171

# Mount host directory containing repositories to analyze here
VOLUME /repos

# Mount host output directory here
VOLUME /var/opt/codeshovel/output

# Last commit to start running the analysis from
ENV START_COMMIT ${START_COMMIT}

# The name of the repository directory in the /repos directory
ENV REPO_NAME ${REPO_NAME}

# The file extension to search for. Defaults to .java
ENV FILE_EXT ${FILE_EXT:-.java}

# A space-separated list of relative (from REPO_NAME) paths to search files with FILE_EXT. Defaults to all subdirectories
ENV SEARCH_PATHS ${SEARCH_PATHS:-.}

ENV OUTPUT_DIR /var/opt/codeshovel/output
ENV WRITE_RESULTS true
ENV WRITE_GITLOG false
ENV WRITE_SEMANTIC_DIFFS false

RUN apt-get update && apt-get -y install maven

WORKDIR /opt/codeshovel
COPY ./ ./
RUN mvn verify

CMD cd "/repos/${REPO_NAME}" && \
    find ${SEARCH_PATHS} -type f -name "*${FILE_EXT}" \
           -exec bash -c 'timeout 15m java -classpath "/opt/codeshovel/target/*" com.felixgrund.codeshovel.execution.DockerExecution ${1#./} ${START_COMMIT} || echo ^^^[$?]${1#./}' - {} \;

