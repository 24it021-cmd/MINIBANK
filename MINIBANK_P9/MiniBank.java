import java.util.Scanner;
import model.*;
import exception.*;
import service.*;
import util.*;

import static util.Validator.validAmount;
public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo b = new BankInfo("MiniBank", "Charusat");

        System.out.println(b.name());
        System.out.println(b.branch());

        Account account = new SavingsAccount("KRISHA", 0, 0);

        System.out.println("\nWithout synchronization:");

        AccountWorker[] workers = new AccountWorker[10];
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            workers[i] = new AccountWorker(account, 1000, 1);
            threads[i] = new Thread(workers[i], "Worker-" + (i + 1));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Balance: " + account.getBalance());

        account = new SavingsAccount("KRISHA", 0, 0);

        System.out.println("\nWith synchronization:");

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new AccountWorker(account, 1000, 1),
                    "Worker-" + (i + 1));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Balance: " + account.getBalance());

        TransactionProcessor processor = new TransactionProcessor();

        for (int i = 0; i < 5; i++) {
            processor.submit(() -> {
                try {
                    account.deposit(100);
                } catch (InvalidAmountException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        for (int i = 0; i < 2; i++) {
            processor.submit(() -> {
                try {
                    account.withdraw(50);
                } catch (BankException e) {
                    System.out.println(e.getMessage());
                }
            });
        }

        processor.stop();

        TransactionBuffer buffer = new TransactionBuffer(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.add(i);
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Processed transaction: " + buffer.remove());
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }, "Consumer");

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        Account accountA = new SavingsAccount("A", 5000, 0);
        Account accountB = new SavingsAccount("B", 5000, 0);

        Thread t1 = new Thread(() -> {
            try {
                TransferService.transfer(accountA, accountB, 100);
            } catch (BankException e) {
                System.out.println(e.getMessage());
            }
        }, "Transfer-1");

        Thread t2 = new Thread(() -> {
            try {
                TransferService.transfer(accountB, accountA, 100);
            } catch (BankException e) {
                System.out.println(e.getMessage());
            }
        }, "Transfer-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Exit");

        int ch = sc.nextInt();

        String msg = switch (ch) {
            case 1 -> "Open Account - later lab";
            case 2 -> "Deposit - later lab";
            case 3 -> "Withdraw - later lab";
            case 4 -> "Transfer - later lab";
            case 5 -> "Thank you for using MiniBank";
            default -> "Invalid choice";
        };

        System.out.println(msg);

        sc.close();
    }
}
