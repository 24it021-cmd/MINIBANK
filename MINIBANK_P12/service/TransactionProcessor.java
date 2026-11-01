package service;

import model.Account;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/** Thread-safe shared account registry plus a worker pool for transactions. */
public class TransactionProcessor {
    private final ConcurrentHashMap<String, Account> sharedAccounts;
    private final ExecutorService executor;

    public TransactionProcessor() {
        this(new ConcurrentHashMap<String, Account>());
    }

    public TransactionProcessor(ConcurrentHashMap<String, Account> sharedAccounts) {
        if (sharedAccounts == null) {
            throw new IllegalArgumentException("Shared accounts map cannot be null");
        }
        this.sharedAccounts = sharedAccounts;
        this.executor = Executors.newFixedThreadPool(4);
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        sharedAccounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    public ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(sharedAccounts.values());
    }

    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }

    public void submit(Runnable task) {
        executor.execute(task);
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
