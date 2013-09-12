set -x 
# Build Kojo
# ./sbt.sh clean package

# Create installer
rm -rf installerbuild

mkdir -p installerbuild/lib
cd installer
scala cp-staging-jars.scala
cd ..

cp lib/jdi.jar installerbuild
cp lib/jdi-win.jar installerbuild

# run IzPack to create installer
cp -var installer/* installerbuild/
cd installerbuild
/home/lalit/IzPack/bin/compile install.xml
mv install.jar kojo2install.jar
echo installerbuild/kojo2install.jar is the Kojo installer.
