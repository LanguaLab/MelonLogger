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

    public enum Level {
        DEBUG(0), FINE(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

        public final int level;

        Level(int level) {
            this.level = level;
        }

        public static Level getFromName(String name){
            name = name.toLowerCase();
            switch (name){
                case "debug":
                    return DEBUG;
                case "fine":
                    return FINE;
                case "info":
                    return INFO;
                case "warn":
                    return WARN;
                case "error":
                    return ERROR;
                case "fatal":
                    return FATAL;
                default:
                    return null;
            }
        }
    }
}
