#!/bin/bash

# 1) Build FinerGit according to the instructions in FinerGit/README.md
# 2) Clone repos with codeshovel/bin/cloneRepositories.sh to ~/tmp/codeshovelRepos
# 3) Create ~/tmp/finergitRepos/
# 4) Copy this script to the FinerGit dir and run from there


# checkstyle			1:50
# commons-io			0:30
# commons-lang			1:26
# elasticsearch			15 minute timeout
# flink					9:33
# hadoop				4 GB memory 
# hibernate-orm			4 GB memory
# hibernate-search		3:35
# intellij-community	15 minute timeout
# javaparser			1:27
# jetty.project			5:52
# jgit					1:27
# junit4				0:14
# junit5				1:01
# lucene-solr			15 minute timeout
# okhttp				0:49
# pmd					2:32
# spring-boot			4:35
# spring-framework		8:08

# bare repos: 10.74 GB
# finergit repos: 16.15 GB (for repos that finished) 

echo "Clearing Stale Repos"

rm -rf ~/tmp/finergitRepos/
mkdir ~/tmp/finergitRepos/

date; echo "Starting FinerGit Jobs"

date; echo "start check checkstyle"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/checkstyle --des ~/tmp/finergitRepos/ckeckstyle

date; echo "done check commons-io"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/commons-io --des ~/tmp/finergitRepos/commons-io

date; echo "done check commons-lang"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/commons-lang --des ~/tmp/finergitRepos/commons-lang

date; echo "done check elasticsearch"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/elasticsearch --des ~/tmp/finergitRepos/elasticsearch

date; echo "done check flink"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/flink --des ~/tmp/finergitRepos/flink

date; echo "done check hadoop"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/hadoop --des ~/tmp/finergitRepos/hadoop

date; echo "done check hibernate-orm"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/hibernate-orm --des ~/tmp/finergitRepos/hibernate-orm

date; echo "done check hibernate-search"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/hibernate-search --des ~/tmp/finergitRepos/hibernate-search

date; echo "done check intellij-community"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/intellij-community --des ~/tmp/finergitRepos/intellij-community

date; echo "done check javaparser"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/javaparser --des ~/tmp/finergitRepos/javaparser

date; echo "done check jetty.project"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/jetty.project --des ~/tmp/finergitRepos/jetty.project

date; echo "done check jgit"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/jgit --des ~/tmp/finergitRepos/jgit

date; echo "done check junit4"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/junit4 --des ~/tmp/finergitRepos/junit4

date; echo "done check junit5"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/junit5 --des ~/tmp/finergitRepos/junit5

date; echo "done check lucene-solr"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/lucene-solr --des ~/tmp/finergitRepos/lucene-solr

date; echo "done check okhttp"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/okhttp --des ~/tmp/finergitRepos/okhttp

date; echo "done check pmd"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/pmd --des ~/tmp/finergitRepos/pmd

date; echo "done check spring-boot"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/spring-boot --des ~/tmp/finergitRepos/spring-boot

date; echo "done check spring-framework"
gtimeout -s SIGKILL 15m gtime -f "Took \t%E" java -Xmx4g -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/spring-framework --des ~/tmp/finergitRepos/spring-framework

date; echo "DONE checking"
