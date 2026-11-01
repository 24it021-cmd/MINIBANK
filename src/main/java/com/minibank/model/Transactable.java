package com.minibank.model;

import com.minibank.exception.InsufficientFundsException;
import com.minibank.exception.InvalidAmountException;

public interface Transactable {
    void deposit(long amount) throws InvalidAmountException;

    boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException;
}