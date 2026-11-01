package model;

public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String ownerName, long balance) {
        super(ownerName, balance);
    }

    public double interestRate() {
        return 7.0;
    }

    public boolean canWithdraw(long amount) {
        return false;
    }
}
