package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import repository.Repository;
import util.TransactionLog;
import util.Validator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** Application service: validates input, uses the repository, and records transactions. */
public class BankService {
    private final Repository<Account> repository;
    private final ConcurrentHashMap<String, Account> sharedAccounts;
    private final Path logFile;
    private final List<String> inMemoryHistory = new ArrayList<>();
    private static final DateTimeFormatter TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public BankService() {
        this(new Repository<>(), new ConcurrentHashMap<>(), Path.of("data", "logs", "transactions.log"));
    }

    public BankService(Repository<Account> repository,
                       ConcurrentHashMap<String, Account> sharedAccounts,
                       Path logFile) {
        this.repository = repository;
        this.sharedAccounts = sharedAccounts;
        this.logFile = logFile;
        for (Account account : repository.findAll()) {
            sharedAccounts.put(account.getAccountNumber(), account);
        }
    }

    public synchronized Account openAccount(String type, String ownerName, long openingBalance)
            throws BankException {
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new BankException("Owner name cannot be empty.");
        }
        if (!Validator.validBalance(openingBalance)) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }
        Account account;
        String normalized = type == null ? "" : type.trim().toUpperCase();
        switch (normalized) {
            case "SAVINGS":
                account = new SavingsAccount(ownerName.trim(), openingBalance, 500);
                break;
            case "CURRENT":
                account = new CurrentAccount(ownerName.trim(), openingBalance, 1000);
                break;
            case "FIXED":
            case "FIXED DEPOSIT":
                account = new FixedDepositAccount(ownerName.trim(), openingBalance);
                break;
            default:
                throw new BankException("Account type must be Savings, Current, or Fixed.");
        }
        repository.save(account);
        sharedAccounts.put(account.getAccountNumber(), account);
        record("ACCOUNT_OPEN," + openingBalance + "," + account.getAccountNumber() + "," + clean(ownerName));
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException {
        Account account = requireAccount(accountNumber);
        if (!Validator.validAmount(amount)) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        account.deposit(amount);
        record("DEPOSIT," + amount + "," + accountNumber + "," + clean(account.getOwnerName()));
    }

    public void withdraw(String accountNumber, long amount) throws BankException {
        Account account = requireAccount(accountNumber);
        if (!Validator.validAmount(amount)) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        account.withdraw(amount);
        record("WITHDRAW," + amount + "," + accountNumber + "," + clean(account.getOwnerName()));
    }

    public void transfer(String fromNumber, String toNumber, long amount) throws BankException {
        Account from = requireAccount(fromNumber);
        Account to = requireAccount(toNumber);
        if (from.equals(to)) {
            throw new BankException("Source and destination accounts must be different.");
        }
        if (!Validator.validAmount(amount)) {
            throw new InvalidAmountException("Transfer amount must be greater than zero.");
        }
        from.transfer(to, amount);
        record("WITHDRAW," + amount + "," + fromNumber + ",TRANSFER TO " + toNumber);
        record("DEPOSIT," + amount + "," + toNumber + ",TRANSFER FROM " + fromNumber);
    }

    public String statement(String accountNumber) throws BankException {
        Account account = requireAccount(accountNumber);
        StringBuilder result = new StringBuilder();
        result.append("Statement for ").append(account.getOwnerName()).append(" (")
                .append(account.getAccountNumber()).append(")\nCurrent balance: ")
                .append(account.getBalance()).append("\nTransactions:\n");
        synchronized (inMemoryHistory) {
            boolean found = false;
            for (String line : inMemoryHistory) {
                String[] fields = line.split(",", 4);
                if (fields.length >= 3 && fields[2].equals(accountNumber)) {
                    result.append(line).append('\n');
                    found = true;
                }
            }
            if (!found) result.append("No transactions recorded in this session.\n");
        }
        return result.toString();
    }

    public ArrayList<Account> listAccounts() {
        ArrayList<Account> accounts = repository.findAll();
        accounts.sort(Comparator.comparing(Account::getAccountNumber));
        return accounts;
    }

    public Account findAccount(String accountNumber) {
        return repository.findById(accountNumber);
    }

    public synchronized void replaceAll(Account[] accounts) {
        for (Account account : repository.findAll()) {
            repository.delete(account.getAccountNumber());
        }
        sharedAccounts.clear();
        if (accounts != null) {
            for (Account account : accounts) {
                if (account != null) {
                    repository.save(account);
                    sharedAccounts.put(account.getAccountNumber(), account);
                }
            }
            Account.updateCounterFrom(accounts);
        }
    }

    public Account[] snapshot() {
        return repository.findAll().toArray(new Account[0]);
    }

    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }

    public Path getLogFile() { return logFile; }

    private Account requireAccount(String accountNumber) throws AccountNotFoundException {
        Account account = repository.findById(accountNumber);
        if (account == null) throw new AccountNotFoundException("Account not found: " + accountNumber);
        return account;
    }

    private void record(String line) {
        String complete = line + "," + TIME.format(LocalDateTime.now());
        synchronized (inMemoryHistory) { inMemoryHistory.add(complete); }
        try {
            TransactionLog.append(logFile, complete);
        } catch (IOException e) {
            throw new IllegalStateException("Transaction completed but log could not be written: " + e.getMessage(), e);
        }
    }

    private static String clean(String value) { return value == null ? "" : value.replace(',', ' '); }
}
