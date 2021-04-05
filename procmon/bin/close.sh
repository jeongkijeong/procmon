#!/bin/sh

PROC_NAME=procmon.jar
PROJ_NAME=$HOME/app/procmon
PROC_LIST=`ps -ef | grep $PROJ_NAME | grep $PROC_NAME | grep -v grep | awk '{print $2 }'`

for PID in $PROC_LIST
do
    kill $PID
    echo $PROJ_NAME 'shutdown succeeded.('$PID')'
done
