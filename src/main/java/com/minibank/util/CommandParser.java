package com.minibank.util;

import com.minibank.model.Command;
import com.minibank.model.TransactionType;

public final class CommandParser {
    private CommandParser() {
    }

    public static Command parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Command cannot be blank");
        }
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected: TYPE ACCOUNT_NUMBER AMOUNT");
        }
        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown transaction type: " + parts[0], exception);
        }
        if (!Validator.isValidAmount(parts[2])) {
            throw new IllegalArgumentException("Amount must be a positive whole number");
        }
        return new Command(type, parts[1], Long.parseLong(parts[2]));
    }
}