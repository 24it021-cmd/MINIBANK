<<<<<<< HEAD
public class CurrentAccount extends Account {
    private long overdraftLimit;

    public CurrentAccount(String ownerName, long balance, long overdraftLimit) {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public double interestRate() {
        return 0.0;
    }

    public boolean canWithdraw(long amount) {
        return amount > 0 && getBalance() - amount >= -overdraftLimit;
    }
=======
public class CurrentAccount extends Account {
    private long overdraftLimit;

    public CurrentAccount(String ownerName, long balance, long overdraftLimit) {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public double interestRate() {
        return 0.0;
    }

    public boolean canWithdraw(long amount) {
        return amount > 0 && getBalance() - amount >= -overdraftLimit;
    }
>>>>>>> 9bf9ae966cd62b73ad752a49c4372f8d7a3495c9
}