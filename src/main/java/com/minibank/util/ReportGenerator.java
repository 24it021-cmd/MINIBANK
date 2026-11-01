package com.minibank.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generate(Path logsDirectory, Path reportFile) throws IOException {
        Files.createDirectories(logsDirectory);
        long deposits = 0;
        long withdrawals = 0;
        StringBuilder report = new StringBuilder("MiniBank End-of-Day Report\n");
        try (DirectoryStream<Path> files = Files.newDirectoryStream(logsDirectory, "*.log")) {
            for (Path file : files) {
                BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
                report.append(file.getFileName()).append(" | ").append(attributes.size())
                        .append(" bytes | ").append(attributes.lastModifiedTime()).append('\n');
                try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 3) {
                            continue;
                        }
                        try {
                            long amount = Long.parseLong(parts[parts.length - 1]);
                            if (parts[0].equals("DEPOSIT")) {
                                deposits = Math.addExact(deposits, amount);
                            } else if (parts[0].equals("WITHDRAW")) {
                                withdrawals = Math.addExact(withdrawals, amount);
                            }
                        } catch (NumberFormatException ignored) {
                            continue;
                        }
                    }
                }
            }
        }
        report.append("Total deposits: ").append(deposits).append('\n')
                .append("Total withdrawals: ").append(withdrawals).append('\n')
                .append("Net change: ").append(deposits - withdrawals).append('\n');
        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report.toString(), StandardCharsets.UTF_8);
        return report.toString();
    }
}