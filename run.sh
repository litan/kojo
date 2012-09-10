#!/bin/bash
FILES=./lib/*
CLASSPATH=./target/scala-2.9.2/kojolite_2.9.2-1.0.jar
for f in $FILES
do
  CLASSPATH=${CLASSPATH}:$f
done

java -Xmx512m -cp ${CLASSPATH} -Dide.run=true net.kogics.kojo.lite.Main $*
