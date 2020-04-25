package moe.langua.lab.utils.logger.utils;

public class LogRecord {
    public final LogRecord.Level level;
    public final String content;
    public final long timestamp;

    public LogRecord(LogRecord.Level level, String content, long timestamp) {
        this.level = level;
        this.content = content;
        this.timestamp = timestamp;
    }

    public enum Level {
        DEBUG(0), FINE(1), INFO(2), WARNING(3), ERROR(4), FATAL(5);

        int level;

        Level(int level) {
            this.level = level;
        }

        public int level() {
            return level;
        }

    }
}
