package moe.langua.lab.utils.logger;

import moe.langua.lab.utils.logger.handler.LogHandler;
import moe.langua.lab.utils.logger.utils.LogRecord;
import moe.langua.lab.utils.logger.utils.LogRecordProcessingChain;

import java.util.ArrayList;

public class MelonLogger {
    private static final MelonLogger instance = new MelonLogger();
    private final LogRecordProcessingChain logRecordProcessingChain = new LogRecordProcessingChain();
    private final ArrayList<LogHandler> handlers = new ArrayList<>();
    private final Thread logWorker;
    private boolean stopped = false;

    private MelonLogger() {
        logWorker = new Thread(() -> {
            while (true) {
                if (stopped && logRecordProcessingChain.size() == 0) return;
                if (!logRecordProcessingChain.hasNext()) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogRecord record = logRecordProcessingChain.getNext();
                    for (LogHandler handler : handlers) {
                        handler.log(record);
                    }
                }
            }
        },"MelonLogger-worker");
        logWorker.setDaemon(true);
        logWorker.start();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public static MelonLogger getLogger() {
        return instance;
    }

    public void addHandler(LogHandler handler) {
        if (!handlers.contains(handler)) handlers.add(handler);
    }

    public void log(LogRecord.Level level, String content) {
        LogRecord record = new LogRecord(level, content, System.currentTimeMillis());
        logRecordProcessingChain.addRecord(record);
    }

    public void stop() {
        stopped = true;
        try {
            logWorker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (LogHandler handler : handlers) {
            handler.stop();
        }
    }
}
