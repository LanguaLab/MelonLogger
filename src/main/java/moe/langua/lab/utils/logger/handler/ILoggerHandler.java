package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.formatter.IRecordFormatter;
import moe.langua.lab.utils.logger.formatter.SimpleRecordFormatter;
import moe.langua.lab.utils.logger.utils.LogRecord;

public interface ILoggerHandler {
    IRecordFormatter formatter = new SimpleRecordFormatter();

    void log(LogRecord logRecord);

    void stop();
}
