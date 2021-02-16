#!/bin/bash

# brew install coreutils (for gtimeout)
# brew install gnu-time (for gtime)

# 1) Build FinerGit according to the instructions in FinerGit/README.md
# 2) Clone repos with codeshovel/bin/cloneRepositories.sh to ~/tmp/codeshovelRepos
# 3) Create ~/tmp/finergitRepos/
# 4) Copy this script to the FinerGit dir and run from there


# checkstyle			    1:50
# commons-io			    0:30
# commons-lang		    1:26
# elasticsearch			  15 minute timeout
# flink					      9:33
# hadoop				      4 GB memory
# hibernate-orm			  4 GB memory
# hibernate-search		3:35
# intellij-community	15 minute timeout
# javaparser			    1:27
# jetty.project			  5:52
# jgit					      1:27
# junit4				      0:14
# junit5				      1:01
# lucene-solr			    15 minute timeout
# okhttp				      0:49
# pmd					        2:32
# spring-boot			    4:35
# spring-framework		8:08

# bare repos:         10.74 GB
# finergit repos:     16.15 GB (for repos that finished)

printf "Clearing Stale Repos\n"

rm -rf ~/tmp/finergitRepos/
mkdir ~/tmp/finergitRepos/

FGMEMORY=-Xmx16g
FGTIME=60m

date; printf "Starting FinerGit Jobs\n"
printf "FinerGit Memory Limit: $FGMEMORY\n"
printf "FinerGit Time Limit: $FGTIME\n"

date; printf "start checking checkstyle\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/checkstyle --des ~/tmp/finergitRepos/ckeckstyle

date; printf "done.\n checking commons-io\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/commons-io --des ~/tmp/finergitRepos/commons-io

date; printf "done.\n checking commons-lang\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/commons-lang --des ~/tmp/finergitRepos/commons-lang

date; printf "done.\n checking elasticsearch\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/elasticsearch --des ~/tmp/finergitRepos/elasticsearch

date; printf "done.\n checking flink\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/flink --des ~/tmp/finergitRepos/flink

date; printf "done.\n checking hadoop\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/hadoop --des ~/tmp/finergitRepos/hadoop

date; printf "done.\n checking hibernate-orm\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/hibernate-orm --des ~/tmp/finergitRepos/hibernate-orm

date; printf "done.\n checking hibernate-search\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/hibernate-search --des ~/tmp/finergitRepos/hibernate-search

date; printf "done.\n checking intellij-community\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/intellij-community --des ~/tmp/finergitRepos/intellij-community

date; printf "done.\n checking javaparser\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/javaparser --des ~/tmp/finergitRepos/javaparser

date; printf "done.\n checking jetty.project\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/jetty.project --des ~/tmp/finergitRepos/jetty.project

date; printf "done.\n checking jgit\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/jgit --des ~/tmp/finergitRepos/jgit

date; printf "done.\n checking junit4\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/junit4 --des ~/tmp/finergitRepos/junit4

date; printf "done.\n checking junit5\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/junit5 --des ~/tmp/finergitRepos/junit5

date; printf "done.\n checking lucene-solr\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/lucene-solr --des ~/tmp/finergitRepos/lucene-solr

date; printf "done.\n checking okhttp\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/okhttp --des ~/tmp/finergitRepos/okhttp

date; printf "done.\n checking pmd\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/pmd --des ~/tmp/finergitRepos/pmd

date; printf "done.\n checking spring-boot\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/spring-boot --des ~/tmp/finergitRepos/spring-boot

date; printf "done.\n checking spring-framework\n"
gtimeout -s SIGKILL $FGTIME gtime -f "Took \t%E" java $FGMEMORY -jar ./build/libs/FinerGit-all.jar create --src ~/tmp/codeshovelRepos/spring-framework --des ~/tmp/finergitRepos/spring-framework

date; printf "DONE.\n All checks complete.\n"
