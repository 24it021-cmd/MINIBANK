
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Positive {
    String message() default "must be > 0";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

class Account {

    @Id
    private String accountNumber;

    @MaxLength(30)
    private String ownerName;

    @Positive
    private long balance;

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
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

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Owner: " + ownerName
                + ", Balance: Rs." + balance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Account)) {
            return false;
        }

        Account other = (Account) obj;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}

class AnnotationValidator {

    public static String[] validate(Object obj) {

        ArrayList<String> errors = new ArrayList<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                if (field.isAnnotationPresent(Positive.class)) {

                    Positive p = field.getAnnotation(Positive.class);

                    if (value instanceof Number
                            && ((Number) value).doubleValue() <= 0) {
                        errors.add(field.getName() + " " + p.message());
                    }
                }

                if (field.isAnnotationPresent(MaxLength.class)) {

                    MaxLength m = field.getAnnotation(MaxLength.class);

                    if (value instanceof String
                            && ((String) value).length() > m.value()) {
                        errors.add(field.getName()
                                + " length must be at most " + m.value());
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Unable to access field: " + field.getName());
            }
        }

        return errors.toArray(new String[0]);
    }
}

public class MiniBank {

    public static void main(String[] args) {

        System.out.println("===== MINIBANK APPLICATION =====");

        Account acc = new Account("AC001", "Tanvi", -5000);

        System.out.println(acc);

        System.out.println("\n===== ANNOTATION VALIDATION =====");

        String[] errors = AnnotationValidator.validate(acc);

        if (errors.length == 0) {
            System.out.println("Account is valid");
        } else {
            for (String error : errors) {
                System.out.println("Validation Error: " + error);
            }
        }

        System.out.println("\n===== WITHDRAW RULE =====");

        WithdrawRule rule = (account, amount) ->
                amount > 0 && account.getBalance() >= amount;

        System.out.println("Withdrawal Allowed: "
                + rule.allow(acc, 1000));

        System.out.println("\nThank you for using MiniBank!");
    }
}
