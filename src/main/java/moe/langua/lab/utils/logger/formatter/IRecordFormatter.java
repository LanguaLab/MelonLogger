package moe.langua.lab.utils.logger.formatter;

import moe.langua.lab.utils.logger.utils.LogRecord;

public interface IRecordFormatter {
    String format(LogRecord logRecord);
}
