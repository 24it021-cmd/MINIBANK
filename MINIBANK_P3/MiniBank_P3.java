<<<<<<< HEAD
import java.util.Scanner;

public class MiniBank_P3 {
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

        accounts[0] = new Account("Riya", 5000);
        accounts[1] = new Account("Mihir");
        accounts[2] = new Account("Rahul", 3000);

        accounts[0].deposit(2000);
        accounts[0].withdraw(3000);

        for (int i = 0; i < accounts.length; i++) {
            System.out.println(accounts[i]);
        }

        Account a1 = accounts[0];
        Account a2 = accounts[0];

        System.out.println(a1.equals(a2));
        if (a1 instanceof Account) {
            System.out.println(true);
        }
        Customer c = new Customer("Riya", "riya@gmail.com", "9876543210");
        Customer.Address address =
                new Customer.Address("Main Road", "Ahmedabad", "380001");
        c.setAddress(address);
        Customer c2 = c.clone();
        System.out.println(c.getAddress().getCity());
        System.out.println(c2.getName());
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

public class MiniBank_P3 {
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

        accounts[0] = new Account("Riya", 5000);
        accounts[1] = new Account("Mihir");
        accounts[2] = new Account("Rahul", 3000);

        accounts[0].deposit(2000);
        accounts[0].withdraw(3000);

        for (int i = 0; i < accounts.length; i++) {
            System.out.println(accounts[i]);
        }

        Account a1 = accounts[0];
        Account a2 = accounts[0];

        System.out.println(a1.equals(a2));
        if (a1 instanceof Account) {
            System.out.println(true);
        }
        Customer c = new Customer("Riya", "riya@gmail.com", "9876543210");
        Customer.Address address =
                new Customer.Address("Main Road", "Ahmedabad", "380001");
        c.setAddress(address);
        Customer c2 = c.clone();
        System.out.println(c.getAddress().getCity());
        System.out.println(c2.getName());
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