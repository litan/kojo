#!/usr/bin/env bash
java -version
set -x 
# Build Kojo
rm -rf dist
./sbt.sh clean test buildDist

# Create staging area
rm -rf installerbuild
mkdir -p installerbuild/lib
cd installer
scala cp-staging-jars.scala
cd ..

cp -va installer/* installerbuild/
cd installerbuild
rm *.*
rm -rf Uninstaller
cp licenses/Kojo-license.txt .
cd ..
rm -rf Kojo-z
mv installerbuild Kojo-z
cd Kojo-z
ln -s ~/tools/winxp-jre-8u152/jre
cd ..
rm Kojo.zip
zip -r Kojo.zip Kojo-z/*
