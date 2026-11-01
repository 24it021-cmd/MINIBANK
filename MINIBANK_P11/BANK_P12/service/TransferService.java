package service;

import model.Account;
import exception.BankException;

public class TransferService {

    public static void transfer(Account from, Account to, long amount)
            throws BankException {

        Account first = from;
        Account second = to;

        if (from.getAccountNumber().compareTo(to.getAccountNumber()) > 0) {
            first = to;
            second = from;
        }

        long start = System.currentTimeMillis();

        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
                System.out.println(Thread.currentThread().getName()
                        + " transfer completed");
            }
        }

        long time = System.currentTimeMillis() - start;

        if (time > 1000) {
            System.out.println("Warning: Long running transfer");
        }
    }
}
