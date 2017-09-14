#!/bin/sh
location=/home/hadoop/platform/platform/src/main/java/io/github/jhipster/sample/web/rest/temp
find $location -mtime +1 -type f -name '*' -exec rm -rf {} \;
