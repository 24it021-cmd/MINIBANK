package model;

public class TransactionBuffer {

    private int[] buffer;
    private int count = 0;

    public TransactionBuffer(int size) {
        buffer = new int[size];
    }

    public synchronized void add(int value) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }

        buffer[count] = value;
        count++;

        System.out.println("Added: " + value);

        notify();
    }

    public synchronized int remove() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int value = buffer[0];

        for (int i = 1; i < count; i++) {
            buffer[i - 1] = buffer[i];
        }

        count--;

        notify();

        return value;
    }
}
