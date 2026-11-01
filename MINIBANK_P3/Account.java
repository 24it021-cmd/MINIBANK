<<<<<<< HEAD
public class Account {
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static int counter = 0;

    public Account(String ownerName, long balance) {
        counter++;
        accountNumber = "AC" + String.format("%04d", counter);
        this.ownerName = ownerName;
        this.balance = balance;
        active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance = balance + amount;
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && amount <= balance) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account a = (Account) o;
            return accountNumber.equals(a.accountNumber);
        }
        return false;
    }

    public int hashCode() {
        return accountNumber.hashCode();
    }
=======
public class Account {
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static int counter = 0;

    public Account(String ownerName, long balance) {
        counter++;
        accountNumber = "AC" + String.format("%04d", counter);
        this.ownerName = ownerName;
        this.balance = balance;
        active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance = balance + amount;
        }
    }

    public boolean withdraw(long amount) {
        if (amount > 0 && amount <= balance) {
            balance = balance - amount;
            return true;
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance;
    }

    public boolean equals(Object o) {
        if (o instanceof Account) {
            Account a = (Account) o;
            return accountNumber.equals(a.accountNumber);
        }
        return false;
    }

    public int hashCode() {
        return accountNumber.hashCode();
    }
>>>>>>> 9bf9ae966cd62b73ad752a49c4372f8d7a3495c9
}