#!/bin/bash

if [ -z "$1" ]
  then
    echo "Usage: clone-repositories.sh CODESHOVEL_REPO_DIR"
    exit 1
fi

git -C "$1" clone https://github.com/checkstyle/checkstyle.git
git -C "$1" clone https://github.com/apache/commons-lang.git
git -C "$1" clone https://github.com/apache/flink.git
git -C "$1" clone https://github.com/hibernate/hibernate-orm.git
git -C "$1" clone https://github.com/javaparser/javaparser.git
git -C "$1" clone https://github.com/eclipse/jgit.git
git -C "$1" clone https://github.com/junit-team/junit4.git
git -C "$1" clone https://github.com/junit-team/junit5.git
git -C "$1" clone https://github.com/square/okhttp.git
git -C "$1" clone https://github.com/spring-projects/spring-framework.git
git -C "$1" clone https://github.com/apache/commons-io.git
git -C "$1" clone https://github.com/elastic/elasticsearch.git
git -C "$1" clone https://github.com/apache/hadoop.git
git -C "$1" clone https://github.com/hibernate/hibernate-search.git
git -C "$1" clone https://github.com/JetBrains/intellij-community.git
git -C "$1" clone https://github.com/eclipse/jetty.project.git
git -C "$1" clone https://github.com/apache/lucene-solr.git
git -C "$1" clone https://github.com/mockito/mockito.git
git -C "$1" clone https://github.com/pmd/pmd.git
git -C "$1" clone https://github.com/spring-projects/spring-boot.git
