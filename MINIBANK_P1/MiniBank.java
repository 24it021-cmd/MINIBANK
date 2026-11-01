<<<<<<< HEAD
import java.util.Scanner;
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
=======
import java.util.Scanner;
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
>>>>>>> 9bf9ae966cd62b73ad752a49c4372f8d7a3495c9
}