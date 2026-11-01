package com.minibank.repository;

import com.minibank.model.Account;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AccountStore {
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final List<String> transactionHistory = new java.util.concurrent.CopyOnWriteArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> sorted = getAll();
        sorted.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return sorted;
    }

    public void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public ConcurrentHashMap<String, Account> backingMap() {
        return accounts;
    }
}