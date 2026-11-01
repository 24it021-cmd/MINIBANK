import exception.BankException;
import model.Account;
import service.BankService;
import service.TransactionProcessor;
import util.ReportGenerator;
import util.StatePersister;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/** MiniBank capstone: menu, service layer, persistence, concurrent batches and reports. */
public class MiniBank {
    private static final Path DATA_FILE = Path.of("data", "accounts.dat");
    private static final Path LOG_FOLDER = Path.of("data", "logs");
    private static final Path REPORT_FILE = Path.of("data", "end-of-day-report.txt");

    public static void main(String[] args) {
        BankService bank = new BankService();
        loadAccounts(bank);

        try (Scanner input = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                System.out.print("Choose an option: ");
                String choice = input.nextLine().trim();
                try {
                    switch (choice) {
                        case "1": openAccount(input, bank); break;
                        case "2": deposit(input, bank); break;
                        case "3": withdraw(input, bank); break;
                        case "4": transfer(input, bank); break;
                        case "5": statement(input, bank); break;
                        case "6": listAccounts(bank); break;
                        case "7": runBatch(input, bank); break;
                        case "8": printReport(); break;
                        case "9":
                            saveAccounts(bank);
                            System.out.println("Accounts saved. Goodbye!");
                            running = false;
                            break;
                        default: System.out.println("Invalid option. Choose 1-9.");
                    }
                } catch (BankException | IOException | NumberFormatException ex) {
                    System.out.println("Operation failed: " + ex.getMessage());
                } catch (IllegalStateException ex) {
                    System.out.println("Operation failed: " + ex.getMessage());
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========== MINIBANK (BANK_P14) ==========");
        System.out.println("1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Account statement");
        System.out.println("6. List accounts");
        System.out.println("7. Process concurrent batch deposits");
        System.out.println("8. Generate and display end-of-day report");
        System.out.println("9. Save and exit");
    }

    private static void openAccount(Scanner in, BankService bank) throws BankException {
        System.out.print("Owner name: "); String name = in.nextLine().trim();
        System.out.print("Account type (Savings/Current/Fixed): "); String type = in.nextLine().trim();
        long balance = readLong(in, "Opening balance: ");
        Account account = bank.openAccount(type, name, balance);
        System.out.println("Account opened: " + account + " | type=" + account.getClass().getSimpleName());
    }

    private static void deposit(Scanner in, BankService bank) throws BankException {
        String id = readText(in, "Account number: ");
        long amount = readLong(in, "Deposit amount: ");
        bank.deposit(id, amount);
        System.out.println("Deposit successful. New balance: " + bank.findAccount(id).getBalance());
    }

    private static void withdraw(Scanner in, BankService bank) throws BankException {
        String id = readText(in, "Account number: ");
        long amount = readLong(in, "Withdrawal amount: ");
        bank.withdraw(id, amount);
        System.out.println("Withdrawal successful. New balance: " + bank.findAccount(id).getBalance());
    }

    private static void transfer(Scanner in, BankService bank) throws BankException {
        String from = readText(in, "From account number: ");
        String to = readText(in, "To account number: ");
        long amount = readLong(in, "Transfer amount: ");
        bank.transfer(from, to, amount);
        System.out.println("Transfer successful.");
    }

    private static void statement(Scanner in, BankService bank) throws BankException {
        System.out.println(bank.statement(readText(in, "Account number: ")));
    }

    private static void listAccounts(BankService bank) {
        ArrayList<Account> accounts = bank.listAccounts();
        if (accounts.isEmpty()) System.out.println("No accounts found.");
        else for (Account account : accounts) System.out.println(account + " | type=" + account.getClass().getSimpleName());
    }

    private static void runBatch(Scanner in, BankService bank) throws BankException {
        String id = readText(in, "Account number for deposits: ");
        if (bank.findAccount(id) == null) throw new exception.AccountNotFoundException("Account not found: " + id);
        int count = (int) readLong(in, "Number of concurrent deposits (1-1000): ");
        long amount = readLong(in, "Amount per deposit: ");
        if (count < 1 || count > 1000 || amount <= 0) throw new IllegalArgumentException("Use 1-1000 deposits and a positive amount.");

        TransactionProcessor processor = new TransactionProcessor(bank.getSharedAccounts());
        CountDownLatch done = new CountDownLatch(count);
        AtomicReference<RuntimeException> failure = new AtomicReference<>();
        for (int i = 0; i < count; i++) {
            processor.submit(() -> {
                try { bank.deposit(id, amount); }
                catch (BankException | RuntimeException ex) { failure.compareAndSet(null, new RuntimeException(ex)); }
                finally { done.countDown(); }
            });
        }
        try { done.await(); }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Batch was interrupted.", ex);
        } finally { processor.stop(); }
        if (failure.get() != null) throw new IllegalStateException("A batch transaction failed: " + failure.get().getCause().getMessage());
        System.out.println("Processed " + count + " deposits safely. New balance: " + bank.findAccount(id).getBalance());
    }

    private static void printReport() throws IOException {
        System.out.println(ReportGenerator.generate(LOG_FOLDER, REPORT_FILE));
        System.out.println("Report saved to: " + REPORT_FILE);
    }

    private static void loadAccounts(BankService bank) {
        if (!Files.exists(DATA_FILE)) {
            System.out.println("No saved account file found. Starting with an empty bank.");
            return;
        }
        try {
            Account[] saved = StatePersister.load(DATA_FILE);
            bank.replaceAll(saved);
            System.out.println("Loaded " + saved.length + " account(s) from " + DATA_FILE);
        } catch (IOException | ClassNotFoundException | RuntimeException ex) {
            System.out.println("Could not load saved accounts: " + ex.getMessage());
        }
    }

    private static void saveAccounts(BankService bank) throws IOException {
        StatePersister.save(bank.snapshot(), DATA_FILE);
    }

    private static String readText(Scanner in, String prompt) {
        System.out.print(prompt); return in.nextLine().trim();
    }

    private static long readLong(Scanner in, String prompt) {
        System.out.print(prompt); return Long.parseLong(in.nextLine().trim());
    }
}
