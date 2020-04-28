package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.utils.LogRecord;

public class ConsoleLogHandler implements LogHandler {
    private final LogRecord.Level recordLevel;

    public ConsoleLogHandler(LogRecord.Level level) {
        this.recordLevel = level;
    }

    @Override
    public void log(LogRecord logRecord) {
        if (logRecord.recordLevel.level < recordLevel.level) return;
        System.out.println(formatter.format(logRecord));
    }

    @Override
    public void stop() {
        //nothing to do
    }
}
