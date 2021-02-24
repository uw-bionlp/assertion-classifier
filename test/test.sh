#!/bin/bash

: ${PROJECT_PATH=`cd $(dirname $0); pwd`}

export CLASSPATH=\
$PROJECT_PATH/lib/assertionclassifier-lite-1.0.jar:\
$PROJECT_PATH/lib/trove-3.0.0.jar:\
$PROJECT_PATH/lib/opennlp-tools-1.5.0.jar:\
$PROJECT_PATH/lib/opennlp-maxent-3.0.0.jar:\
$PROJECT_PATH/build/:\
$CLASSPATH

java -ea -Xms256M -Xmx2500M \
    -DCONFIGFILE="$PROJECT_PATH/assertcls.properties" \
    -DASSERTRESOURCES="$PROJECT_PATH/assert-resources/" \
    -DLIBLINEAR_PATH="/assert-resources/liblinear-1.93" \
    Test
    

