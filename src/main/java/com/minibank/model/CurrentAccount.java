package com.minibank.model;

public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;
    private final long overdraftLimit;

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit, String accountNumber) {
        super(ownerName, openingBalance, accountNumber);
        this.overdraftLimit = overdraftLimit;
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        if (amount <= 0) {
            return false;
        }
        try {
            return Math.subtractExact(getBalance(), amount) >= -overdraftLimit;
        } catch (ArithmeticException exception) {
            return false;
        }
    }
}