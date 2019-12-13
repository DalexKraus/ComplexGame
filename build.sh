# Buildscript for the game.
# Compiles all files into bytecode and generates a jar file
# Afterwards, the generated jar file will be executed in a new termial.
# The new terminal is handy to distinguish the maven output from the engine's.

echo "Build Script - Version 1.0 [rev. 13.12.2019]"
#Use 4 threads for compiling
mvn -T 4 install -f pom.xml

#find jar file

echo "Finding jar file ..."
for jarfile in target/*.jar; do
  if [[ $jarfile == *"ComplexGame"* ]] && [[ $jarfile == *"dependencies"* ]]; then
    echo "Found jarfile $jarfile"
    echo "Executing ..."
    java -jar "$jarfile"
    jar_found=1
    sleep 10
  fi
done

if [[ ! -v jar_found ]]; then
  echo "No compiled jar file found."
fi
