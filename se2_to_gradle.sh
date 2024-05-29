mkdir -p src/main/java
mkdir -p src7test/java
cp -r src/de src/main/java
cp -r src/de src/test/java
find src/main/java -type f -name "*Test.java" -exec rm {} \;
find src/test/java -type f ! -name "*Test.java" -exec rm {} \;
rm -r src/de
