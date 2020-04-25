package moe.langua.lab.utils.logger;

import moe.langua.lab.utils.logger.handler.ILoggerHandler;
import moe.langua.lab.utils.logger.utils.LogRecord;
import moe.langua.lab.utils.logger.utils.LogRecordProcessingChain;

import java.io.File;
import java.util.ArrayList;

public class MelonLogger {
    private static final MelonLogger instance = new MelonLogger(new File(new File("").getAbsolutePath() + "/logs"));
    private final File dataRoot;
    private final LogRecordProcessingChain logChain = new LogRecordProcessingChain();
    private final ArrayList<ILoggerHandler> handlers = new ArrayList<>();
    private boolean stopped = false;
    private Thread logWorker;

    private MelonLogger(File dataRoot) {
        this.dataRoot = dataRoot;
        Thread logWorker = new Thread(() -> {
            while (true) {
                if (stopped && logChain.size() == 0) return;
                if (!logChain.hasNext()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogRecord record = logChain.getNext();
                    for (ILoggerHandler handler : handlers) {
                        handler.log(record);
                    }
                }
            }
        });
        logWorker.setDaemon(true);
        logWorker.start();
        this.logWorker = logWorker;
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public static MelonLogger getLogger() {
        return instance;
    }

    public void addHandler(ILoggerHandler handler) {
        if (!handlers.contains(handler)) handlers.add(handler);
    }

    public void log(LogRecord.Level level, String content) {
        LogRecord record = new LogRecord(level, content, System.currentTimeMillis());
        logChain.addRecord(record);
    }

    public void stop() {
        stopped = true;
        try {
            logWorker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
