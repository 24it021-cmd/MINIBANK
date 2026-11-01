import java.util.Scanner;
import model.*;
import exception.*;
import service.BankService;

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

        Account[] accounts = new Account[3];

        accounts[0] = new SavingsAccount("Riya", 1000, 500);
        accounts[1] = new CurrentAccount("Mihir", 3000, 2000);
        accounts[2] = new FixedDepositAccount("Rahul", 10000);

        try {
            accounts[0].withdraw(5000);
        } catch (InsufficientFundsException e) {
            System.out.println("Not enough balance. Shortfall: " + e.getShortfall());
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Withdrawal operation completed");
        }

        try {
            accounts[0].deposit(-100);
        } catch (InvalidAmountException e) {
            System.out.println(e.getMessage());
        }

        try {
            accounts[0].transfer(accounts[1], 500);
            System.out.println("Transfer successful");
        } catch (BankException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Transfer operation completed");
        }

        BankService.showAccount(accounts[0]);

        System.out.println(validAmount(500));

        try (TestResource r = new TestResource()) {
            System.out.println("Resource is being used");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int ch;

        do {
            System.out.println("\n1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            ch = sc.nextInt();

            String msg = switch (ch) {
                case 1 -> "Open Account - later lab";
                case 2 -> "Deposit - later lab";
                case 3 -> "Withdraw - later lab";
                case 4 -> "Transfer - later lab";
                case 5 -> "Thank you for using MiniBank";
                default -> "Invalid choice";
            };

            System.out.println(msg);

        } while (ch != 5);

        sc.close();
    }

    static class TestResource implements AutoCloseable {
        public void close() {
            System.out.println("Resource closed");
        }
    }
}
