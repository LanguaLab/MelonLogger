package moe.langua.lab.utils.logger.utils;

public class LogRecordProcessingChain {
    private int size;
    private Node waiting;
    private Node latest;

    public LogRecordProcessingChain() {
        waiting = new Node(null);
        latest = waiting;
    }

    public boolean hasNext() {
        return waiting.hasNext();
    }

    public LogRecord getNext() {
        synchronized (this) {
            if (waiting.next == null) return null;
            waiting = waiting.next;
            size--;
            return waiting.record;
        }
    }

    public void addRecord(LogRecord record) {
        synchronized (this) {
            latest = new Node(latest, record);
            size++;
        }
    }

    public int size() {
        return size;
    }

    private static class Node {
        private final LogRecord record;
        private Node next = null;

        private Node(Node previous, LogRecord record) {
            this.record = record;
            previous.next = this;
        }

        private Node(LogRecord record) {
            this.record = record;
        }

        private boolean hasNext() {
            return next != null;
        }
    }
}
