# Compile
javac cscie97/asn4/housemate/controller/*.java
javac cscie97/asn4/housemate/test/*.java
javac cscie97/asn4/housemate/model/*.java
javac cscie97/asn4/knowledge/engine/*.java
javac cscie97/asn4/housemate/entitlement/*.java

# Run
#java -cp . cscie97.asn4.housemate.test.FunctionalTestDriver
java -cp . cscie97.asn4.housemate.test.TestDriver housesetup_a3.txt

# Clean up
rm cscie97/asn4/housemate/controller/*.class
rm cscie97/asn4/housemate/test/*.class
rm cscie97/asn4/housemate/model/*.class
rm cscie97/asn4/knowledge/engine/*.class
rm cscie97/asn4/housemate/entitlement/*.class
