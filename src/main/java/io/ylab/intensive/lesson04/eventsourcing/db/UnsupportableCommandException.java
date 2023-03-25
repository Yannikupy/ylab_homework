package io.ylab.intensive.lesson04.eventsourcing.db;

public class UnsupportableCommandException extends Exception {
    public UnsupportableCommandException(String message) {
        super(message);
    }
}
