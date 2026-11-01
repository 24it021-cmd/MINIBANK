package util;

public class Validator {

    public static boolean validAmount(long amount) {
        return amount > 0;
    }

    public static boolean validBalance(long balance) {
        return balance >= 0;
    }
}
