MiniBank Practical 10

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

Practical 10 topics:
- ExecutorService
- Fixed thread pool
- Runnable tasks
- wait() and notify()
- Producer-consumer
- Deadlock prevention
- Consistent lock ordering
- Thread-safe Account
