package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.utils.LogRecord;

public class ConsoleLogHandler implements LogHandler {
    private final int mininumLevel;

    public ConsoleLogHandler(LogRecord.Level mininumLevel) {
        this.mininumLevel = mininumLevel.level;
    }

    @Override
    public void log(LogRecord logRecord) {
        if (logRecord.recordLevel.level < mininumLevel) return;
        System.out.println(formatter.format(logRecord));
    }

    @Override
    public void stop() {
        //nothing to do
    }
}
