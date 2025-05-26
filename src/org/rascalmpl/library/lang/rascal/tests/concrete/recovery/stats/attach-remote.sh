#!/bin/sh
# Attach to a remote screen
host=$1
id=$2
ssh -t root@$host screen -x $id

