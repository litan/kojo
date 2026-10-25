#!/usr/bin/env bash
set -ex
java -version
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
rm -rf Kojo-z Kojo-z??
mv installerbuild Kojo-z
rm -f Kojo.zip Kojo??.zip
zip -r Kojo.zip Kojo-z/*
