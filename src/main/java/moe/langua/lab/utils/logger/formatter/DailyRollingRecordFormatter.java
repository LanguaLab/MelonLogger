package moe.langua.lab.utils.logger.formatter;

import moe.langua.lab.utils.logger.utils.LogRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyRollingRecordFormatter implements RecordFormatter {
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public String format(LogRecord logRecord) {
        return "| " + dateFormat.format(new Date(logRecord.timestamp)) + " | " + logRecord.level.name() + " | " + logRecord.content+"\n";
    }
}
