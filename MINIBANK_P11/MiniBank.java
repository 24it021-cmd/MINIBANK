import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.ReportGenerator;
import util.StatePersister;
import util.TransactionLog;
import java.io.IOException;
import java.nio.file.Path;
public class MiniBank {
    public static void main(String[] args) {
        Path dataFolder = Path.of("data");
        Path stateFile = dataFolder.resolve("accounts.dat");
        Path logsFolder = dataFolder.resolve("logs");
        Path reportFile = dataFolder.resolve("end-of-day-report.txt");
        Path todayLog = logsFolder.resolve("transactions.log");
        try {
            System.out.println("=== MiniBank: Persistence and Reporting ===");
            Account[] accounts = {
                    new SavingsAccount("Krisha", 5000, 1000),
                    new CurrentAccount("Aarav", 3000, 2000),
                    new FixedDepositAccount("Diya", 10000)
            };
            StatePersister.save(accounts, stateFile);
            System.out.println("Accounts saved to: " + stateFile);

            Account[] loadedAccounts = StatePersister.load(stateFile);
            System.out.println("\nAccounts loaded from file:");
            for (Account account : loadedAccounts) {
                System.out.println(account + " | type="
                        + account.getClass().getSimpleName());
            }
            Customer customer = new Customer("Krisha", "krisha@example.com", "9999999999");
            customer.setAddress(new Customer.Address("College Road", "Anand", "388001"));
            System.out.println("\nSerializable Customer class ready: " + customer.getCustomerId());
            TransactionLog.append(todayLog, "DEPOSIT,1000");
            TransactionLog.append(todayLog, "WITHDRAW,250");
            TransactionLog.append(todayLog, "DEPOSIT,500");
            TransactionLog.append(todayLog, "WITHDRAW,100");
            System.out.println("\nTransaction log appended to: " + todayLog);
            String report = ReportGenerator.generate(logsFolder, reportFile);
            System.out.println("\n" + report);
            System.out.println("Report saved to: " + reportFile);
        } catch (IOException e) {
            System.err.println("File operation failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load saved account classes: " + e.getMessage());
        }
    }
}
