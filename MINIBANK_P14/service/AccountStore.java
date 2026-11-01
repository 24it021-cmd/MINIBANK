package service;

import model.Account;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/** Stores accounts by account number and keeps a simple transaction history. */
public class AccountStore {
    private final HashMap<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        accounts.put(account.getAccountNumber(), account);
        transactionHistory.add("ACCOUNT_ADDED: " + account.getAccountNumber());
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void recordTransaction(String transaction) {
        if (transaction == null || transaction.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction cannot be empty");
        }
        transactionHistory.add(transaction);
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = getAll();
        sorted.sort(Comparator.comparingLong(Account::getBalance)
                .thenComparing(Account::getAccountNumber));
        return sorted;
    }

    public int size() {
        return accounts.size();
    }
}
