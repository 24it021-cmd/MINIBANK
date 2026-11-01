package model;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public abstract class Account implements Transactable, InterestBearing {
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static int counter = 0;

    public Account(String ownerName, long balance) {
        counter++;
        accountNumber = "AC" + String.format("%04d", counter);
        this.ownerName = ownerName;
        this.balance = balance;
        active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        balance = balance + amount;
    }

    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (!canWithdraw(amount)) {
            long shortfall = amount - balance;
            if (shortfall < 0) {
                shortfall = 0;
            }
            throw new InsufficientFundsException("Insufficient funds", shortfall);
        }

        balance = balance - amount;
        return true;
    }

    public void transfer(Account to, long amount) throws BankException {
        Account first = this;
        Account second = to;

        if (to == null) {
            throw new BankException("Account not found");
        }

        if (this.getAccountNumber().compareTo(to.getAccountNumber()) > 0) {
            first = to;
            second = this;
        }

        synchronized (first) {
            synchronized (second) {
                this.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account a = (Account) o;
            return accountNumber.equals(a.accountNumber);
        }
        return false;
    }

    public int hashCode() {
        return accountNumber.hashCode();
    }
}
