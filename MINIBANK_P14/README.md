# MiniBank — BANK_P14 Capstone

## Features
- Menu-driven bank operations: open account, deposit, withdraw, transfer, statement and list accounts.
- `BankService` combines the generic `Repository<Account>`, validation and checked bank exceptions.
- Serialized account state is loaded from `data/accounts.dat` at startup and saved on exit.
- Append-only transaction logs are written to `data/logs/transactions.log`.
- Concurrent batch deposits use `TransactionProcessor` and the shared `ConcurrentHashMap`.
- End-of-day report reads log files and writes `data/end-of-day-report.txt`.
- JUnit 5 tests cover deposits, withdrawals over the account limit and concurrent deposits.

## Requirements
JDK 17 or later. Maven is required to run the JUnit tests.

## Run directly with Java
From this folder, compile the application sources:

```powershell
javac -d out MiniBank.java model/*.java model/annotation/*.java service/*.java util/*.java exception/*.java repository/*.java
java -cp out MiniBank
```

## Build runnable JAR
```powershell
javac -d out MiniBank.java model/*.java model/annotation/*.java service/*.java util/*.java exception/*.java repository/*.java
jar --create --file MiniBank.jar --main-class MiniBank -C out .
java -jar MiniBank.jar
```

## Run JUnit tests
```powershell
mvn test
```

The JUnit tests are in `src/test/java/BankServiceTest.java`.

## Menu
1. Open account
2. Deposit
3. Withdraw
4. Transfer
5. Account statement
6. List accounts
7. Process concurrent batch deposits
8. Generate and display end-of-day report
9. Save and exit

All amounts are represented as whole-number units for this teaching exercise. Savings accounts retain their minimum balance rule; current accounts use their configured overdraft limit; fixed-deposit accounts do not allow withdrawals.
