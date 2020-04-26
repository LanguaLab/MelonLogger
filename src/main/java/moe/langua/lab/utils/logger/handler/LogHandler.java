package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.formatter.RecordFormatter;
import moe.langua.lab.utils.logger.formatter.SimpleRecordFormatter;
import moe.langua.lab.utils.logger.utils.LogRecord;

public interface LogHandler {
    RecordFormatter formatter = new SimpleRecordFormatter();

    void log(LogRecord logRecord);

    void stop();
}
