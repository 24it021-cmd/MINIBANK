
interface Transactable {
    void deposit(long amount);
    boolean withdraw(long amount);
}
interface InterestBearing {
    double interestRate();
    long getBalance();
    default double yearlyInterest() {
        return getBalance() * interestRate() / 100;
    }
}
@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

interface Premium {
}
class Account implements Transactable, InterestBearing, Premium {
    private String accountNumber;
    private String ownerName;
    private long balance;
    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful: Rs." + amount);
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double interestRate() {
        return 4.0;
    }

    public long getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }
}

class BankUtils {
    public static void printMessage(String message) {
        System.out.println(message);  }}
public class MiniBank {
    public static void main(String[] args) {
        Account acc = new Account("AC001", "Tanvi", 10000);
        BankUtils.printMessage("===== MINIBANK APPLICATION =====");
        System.out.println("Account Number: " + acc.getAccountNumber());
        System.out.println("Owner Name: " + acc.getOwnerName());
        System.out.println("Opening Balance: Rs." + acc.getBalance());
        System.out.println("\n===== DEPOSIT =====");
        acc.deposit(2000);
        System.out.println("Current Balance: Rs." + acc.getBalance());
        System.out.println("\n===== ANONYMOUS CLASS =====");
        WithdrawRule rule1 = new WithdrawRule() {
            public boolean allow(Account account, long amount) {
                return amount > 0 && account.getBalance() >= amount; }};
        System.out.println("Withdrawal Allowed: " + rule1.allow(acc, 3000));
        System.out.println("\n===== LAMBDA EXPRESSION =====");
        WithdrawRule rule2 = (account, amount) ->
                amount > 0 && account.getBalance() >= amount;
        System.out.println("Withdrawal Allowed: " + rule2.allow(acc, 5000));
        System.out.println("\n===== WITHDRAWAL =====");
        if (acc.withdraw(3000)) {
            System.out.println("Withdrawal Successful");
        } else {
            System.out.println("Insufficient Balance");}
        System.out.println("Remaining Balance: Rs." + acc.getBalance());
        System.out.println("\n===== INTEREST DETAILS =====");
        System.out.println("Interest Rate: " + acc.interestRate() + "%");
        System.out.println("Yearly Interest: Rs." + acc.yearlyInterest());
        System.out.println("\n===== PREMIUM ACCOUNT =====");
        System.out.println("Premium Member: " + (acc instanceof Premium));
        BankUtils.printMessage("\nThank you for using MiniBank!");
    }}
