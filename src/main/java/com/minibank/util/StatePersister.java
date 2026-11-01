package com.minibank.util;

import com.minibank.model.Account;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class StatePersister {
    private StatePersister() {
    }

    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException {
        if (!Files.exists(file)) {
            return new Account[0];
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) input.readObject();
        } catch (ClassNotFoundException exception) {
            throw new IOException("Saved account data could not be loaded", exception);
        }
    }
}