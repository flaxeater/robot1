#!/bin/bash
dir=$(pwd)
cd ../..
javac -deprecation -g -source 1.5 -encoding UTF-8 -classpath /usr/lib/jvm/java-6-sun-1.6.0.26/jre/lib/rt.jar:libs/robocode.jar:/home/ccrabtree/robocode/robots $dir/RadionAtascar.java
cd $dir
