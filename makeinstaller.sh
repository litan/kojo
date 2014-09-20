set -x 
# Build Kojo
# ./sbt.sh clean package

# Create installer
rm -rf installerbuild

mkdir -p installerbuild/lib
cd installer
scala cp-staging-jars.scala
cd ..

# run IzPack to create installer
cp -var installer/* installerbuild/
cd installerbuild
/home/lalit/IzPack/bin/compile install.xml
mv install.jar kojoInstall.jar
echo installerbuild/kojoInstall.jar is the Kojo installer.
