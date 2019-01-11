#!/bin/bash

commd=$1

source env.sh

#
# ./build.sh
#
if [ -z $commd ]
then
  echo compiling C code ...
  pushd ./C
  ./compile.sh
  popd

  echo compiling Java ...
  javac -g -classpath $JARS:./java:. java/tbox/keccak256/*.java
  javac -g -classpath $JARS:./java:. java/tbox/*.java

  echo making dist ...
  pushd ./java
  jar -cf ../lib/tbox.jar ./tbox/*.class ./tbox/keccak256/*.class
  popd
fi

#
# ./build.sh clean
#
if [ "$commd" = "clean" ]
then
  echo cleaning...
  rm C/*.o C/tbox_*.h
  rm java/tbox/keccak256/*.class
  rm java/tbox/*.class
  rm lib/tbox.jar
  rm lib/libtbox.so
fi
