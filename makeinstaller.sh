#!/usr/bin/env bash
java -version
set -x 
# Build Kojo
./sbt.sh clean test package

# Create installer
rm -rf installerbuild

mkdir -p installerbuild/lib
cd installer
scala cp-staging-jars.scala
cd ..

# run IzPack to create installer
cp -var installer/* installerbuild/
cd installerbuild

# Still need Java 6 for IzPack to get around "unable to compress" warning
source ~/bin/activate-java-16.sh
~/tools/IzPack/bin/compile install.xml

mv install.jar kojoInstall.jar
echo jar installer generated -- installerbuild/kojoInstall.jar



