package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Reads transaction logs and creates an end-of-day summary report. */
public class ReportGenerator {
    private ReportGenerator() { }

    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        long totalDeposits = 0L;
        long totalWithdrawals = 0L;
        List<String> fileDetails = new ArrayList<>();

        Files.createDirectories(logsFolder);
        try (DirectoryStream<Path> entries = Files.newDirectoryStream(logsFolder)) {
            for (Path file : entries) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes = Files.readAttributes(
                        file, BasicFileAttributes.class);
                fileDetails.add(file.getFileName()
                        + " | size=" + attributes.size() + " bytes"
                        + " | lastModified=" + Instant.ofEpochMilli(
                                attributes.lastModifiedTime().toMillis()));

                try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length < 2) {
                            continue;
                        }

                        String type = parts[0].trim().toUpperCase();
                        try {
                            long amount = Long.parseLong(parts[1].trim());
                            if (amount < 0) {
                                continue;
                            }
                            if (type.equals("DEPOSIT")) {
                                totalDeposits += amount;
                            } else if (type.equals("WITHDRAW") || type.equals("WITHDRAWAL")) {
                                totalWithdrawals += amount;
                            }
                        } catch (NumberFormatException ignored) {
                            // Ignore malformed transaction lines.
                        }
                    }
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("MINIBANK END-OF-DAY REPORT\n");
        report.append("==========================\n");
        report.append("Total deposits: ").append(totalDeposits).append('\n');
        report.append("Total withdrawals: ").append(totalWithdrawals).append('\n');
        report.append("Net transaction total (deposits - withdrawals): ")
                .append(totalDeposits - totalWithdrawals).append("\n\n");
        report.append("LOG FILE DETAILS\n");
        report.append("----------------\n");
        if (fileDetails.isEmpty()) {
            report.append("No log files found.\n");
        } else {
            for (String detail : fileDetails) {
                report.append(detail).append('\n');
            }
        }

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report.toString(), StandardCharsets.UTF_8);
        return report.toString();
    }
}
