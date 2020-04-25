package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.utils.LogRecord;

public class ConsoleLogHandler implements ILoggerHandler {
    private final LogRecord.Level recordLevel;

    public ConsoleLogHandler(LogRecord.Level level) {
        this.recordLevel = level;
    }

    @Override
    public void log(LogRecord logRecord) {
        if (logRecord.level.level() < recordLevel.level()) return;
        System.out.println(formatter.format(logRecord));
    }
}
