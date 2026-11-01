package model;

public class AccountWorker implements Runnable {

    private Account account;
    private int times;
    private long amount;

    public AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");

        for (int i = 0; i < times; i++) {
            try {
                account.deposit(amount);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(Thread.currentThread().getName() + " finished");
    }
}
