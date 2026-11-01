import java.util.Scanner;
public class MiniBank_P5 {
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

        accounts[0] = new SavingsAccount("Riya", 5000, 1000);
        accounts[1] = new CurrentAccount("Mihir", 3000, 2000);
        accounts[2] = new FixedDepositAccount("Rahul", 10000);

        for (int i = 0; i < accounts.length; i++) {
            System.out.println(accounts[i]);
            System.out.println(accounts[i].interestRate());
        }

        if (accounts[0] instanceof SavingsAccount s) {
            System.out.println(s.getBalance());
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
}