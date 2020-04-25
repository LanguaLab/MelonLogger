package moe.langua.lab.utils.logger.formatter;

import moe.langua.lab.utils.logger.utils.LogRecord;
import moe.langua.lab.utils.logger.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleRecordFormatter implements IRecordFormatter {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy | HH:mm:ss");

    @Override
    public String format(LogRecord logRecord) {
        String recordString = "| " + dateFormat.format(new Date(logRecord.timestamp)) + " | " + logRecord.level.name() + " | " + logRecord.content;
        switch (logRecord.level) {
            case DEBUG:
                recordString = Utils.color(recordString, Utils.Color.WHITE, Utils.Color.GRAY);
                break;
            case FINE:
                recordString = Utils.color(recordString, Utils.Color.LIGHT_BLUE, Utils.Color.GRAY);
                break;
            case INFO:
                recordString = Utils.color(recordString, Utils.Color.GRAY, Utils.Color.GRAY);
                break;
            case WARNING:
                recordString = Utils.color(recordString, Utils.Color.YELLOW, Utils.Color.GRAY);
                break;
            case ERROR:
            case FATAL:
                recordString = Utils.color(recordString, Utils.Color.RED, Utils.Color.GRAY);
                break;
        }
        return recordString;
    }
}
