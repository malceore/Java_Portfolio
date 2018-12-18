javac cscie97/asn2/housemate/model/*.java
javac cscie97/asn2/housemate/test/*.java

java -cp . cscie97.asn2.housemate.test.TestDriver housesetup_copy.txt
java -cp . cscie97.asn2.housemate.test.TestScriptDriver
java -cp . cscie97.asn2.housemate.test.FunctionalTestDriver

rm cscie97/asn2/housemate/test/*.class
rm cscie97/asn2/housemate/model/*.class
