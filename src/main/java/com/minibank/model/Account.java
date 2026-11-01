package com.minibank.model;

import com.minibank.exception.AccountNotFoundException;
import com.minibank.exception.BankException;
import com.minibank.exception.InsufficientFundsException;
import com.minibank.exception.InvalidAmountException;
import com.minibank.model.annotation.Id;
import com.minibank.model.annotation.Positive;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong ACCOUNT_COUNTER = new AtomicLong();

    @Id
    private final String accountNumber;
    private final String ownerName;
    @Positive
    private long balance;
    private boolean active;

    protected Account(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, nextAccountNumber());
    }

    protected Account(String ownerName) {
        this(ownerName, 0);
    }

    protected Account(String ownerName, long openingBalance, String accountNumber) {
        this.accountNumber = Objects.requireNonNull(accountNumber, "accountNumber");
        this.ownerName = Objects.requireNonNull(ownerName, "ownerName");
        this.balance = openingBalance;
        this.active = true;
        reserveAccountNumber(accountNumber);
    }

    private static String nextAccountNumber() {
        return "AC" + String.format("%04d", ACCOUNT_COUNTER.incrementAndGet());
    }

    public static void reserveAccountNumber(String accountNumber) {
        if (accountNumber != null && accountNumber.matches("AC\\d+")) {
            long number = Long.parseLong(accountNumber.substring(2));
            ACCOUNT_COUNTER.accumulateAndGet(number, Math::max);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void close() {
        active = false;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        balance = Math.addExact(balance, amount);
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (!active || !canWithdraw(amount)) {
            throw new InsufficientFundsException(Math.max(0, amount - balance));
        }
        balance = Math.subtractExact(balance, amount);
        return true;
    }

    public void transfer(Account destination, long amount) throws BankException {
        if (destination == null) {
            throw new AccountNotFoundException("null");
        }
        if (destination == this) {
            throw new InvalidAmountException(amount);
        }
        Account first = compareTo(destination) < 0 ? this : destination;
        Account second = first == this ? destination : this;
        synchronized (first) {
            synchronized (second) {
                try {
                    withdraw(amount);
                    destination.deposit(amount);
                } catch (BankException exception) {
                    throw exception;
                }
            }
        }
    }

    public abstract boolean canWithdraw(long amount);

    @Override
    public int compareTo(Account other) {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public final boolean equals(Object other) {
        return this == other || other instanceof Account account
                && accountNumber.equals(account.accountNumber);
    }

    @Override
    public final int hashCode() {
        return accountNumber.hashCode();
    }

    @Override
    public synchronized String toString() {
        return accountNumber + " | " + ownerName + " | " + balance + " | active=" + active;
    }
}