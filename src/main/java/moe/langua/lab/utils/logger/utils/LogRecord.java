package moe.langua.lab.utils.logger.utils;

public class LogRecord {
    public final LogRecord.Level recordLevel;
    public final String content;
    public final long timestamp;

    public LogRecord(LogRecord.Level recordLevel, String content, long timestamp) {
        this.recordLevel = recordLevel;
        this.content = content;
        this.timestamp = timestamp;
    }

    public enum Level implements Comparable<Level>{
        DEBUG(0), FINE(1), INFO(2), WARNING(3), ERROR(4), FATAL(5);

        public final int level;

        Level(int level) {
            this.level = level;
        }
    }
}
