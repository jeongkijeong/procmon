#!/bin/sh

export PROJ_HOME=$HOME/app/procmon
export PROJ_NAME=procmon.jar

clear
$JAVA_HOME/java -server -Xms1G -Xmx1G -XX:+UseG1GC -Duser.timezone=GMT+09:00 -jar $PROJ_HOME/$PROJ_NAME $PROJ_HOME/conf/server.properties $PROJ_HOME/conf/logback.xml $PROC_NAME

