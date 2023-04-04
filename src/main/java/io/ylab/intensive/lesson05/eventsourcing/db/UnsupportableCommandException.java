package io.ylab.intensive.lesson05.eventsourcing.db;

public class UnsupportableCommandException extends Exception {
    public UnsupportableCommandException(String message) {
        super(message);
    }
}
