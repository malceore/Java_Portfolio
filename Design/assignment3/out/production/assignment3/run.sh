# Compile
javac cscie97/asn3/housemate/controller/*.java
javac cscie97/asn3/housemate/test/*.java
javac cscie97/asn3/housemate/model/*.java
javac cscie97/asn3/knowledge/engine/*.java

# Run
#java -cp . cscie97.asn3.housemate.test.FunctionalTestDriver
java -cp . cscie97.asn3.housemate.test.TestDriver housesetup_a3.txt

# Clean up
rm cscie97/asn3/housemate/controller/*.class
rm cscie97/asn3/housemate/test/*.class
rm cscie97/asn3/housemate/model/*.class
rm cscie97/asn3/knowledge/engine/*.class
