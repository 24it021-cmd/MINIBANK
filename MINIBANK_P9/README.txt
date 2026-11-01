MiniBank Practical 9

Folder structure:
MiniBank.java
model/
util/
service/
exception/
MANIFEST.MF

Compile:
javac MiniBank.java model/*.java util/*.java service/*.java exception/*.java

Run:
java MiniBank

Create JAR:
jar cfm minibank.jar MANIFEST.MF MiniBank.class model/*.class util/*.class service/*.class exception/*.class

Run JAR:
java -jar minibank.jar

Java JDK 17 or later is required.
