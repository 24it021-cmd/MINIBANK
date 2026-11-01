package com.minibank.util;

import com.minibank.model.Account;

public final class StatementFormatter {
    private StatementFormatter() {
    }

    public static String buildStatement(Account account) {
        StringBuilder statement = new StringBuilder();
        statement.append("MiniBank Account Statement\n")
                .append("Account: ").append(account.getAccountNumber()).append('\n')
                .append("Owner: ").append(account.getOwnerName()).append('\n')
                .append("Balance: ").append(account.getBalance()).append(" rupees\n")
                .append("Interest rate: ").append(account.interestRate()).append("%\n")
                .append("Yearly interest: ").append(account.yearlyInterest()).append(" rupees\n")
                .append("Status: ").append(account.isActive() ? "Active" : "Closed");
        return statement.toString();
    }
}