package com.minibank.exception;

public class InvalidAmountException extends BankException {
    private static final long serialVersionUID = 1L;

    public InvalidAmountException(long amount) {
        super("Amount must be positive: " + amount);
    }
}