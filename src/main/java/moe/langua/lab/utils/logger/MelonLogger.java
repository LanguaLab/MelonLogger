package moe.langua.lab.utils.logger;

import moe.langua.lab.utils.logger.handler.LogHandler;
import moe.langua.lab.utils.logger.utils.LogRecord;
import moe.langua.lab.utils.logger.utils.NamedDaemonThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class MelonLogger {
    private static final MelonLogger instance = new MelonLogger();
    private final ArrayList<LogHandler> handlers = new ArrayList<>();
    private final ExecutorService workerThread = newSingleThreadExecutor(new NamedDaemonThreadFactory("loggerWorker"));

    private MelonLogger() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    public static MelonLogger getLogger() {
        return instance;
    }

    public void addHandler(LogHandler handler) {
        synchronized (handlers) {
            if (!handlers.contains(handler)) handlers.add(handler);
        }
    }

    public void log(LogRecord.Level level, String content) {
        LogRecord record = new LogRecord(level, content, System.currentTimeMillis());
        workerThread.submit(() -> {
            synchronized (handlers) {
                handlers.forEach((logHandler) -> logHandler.log(record));
            }
        });
    }

    public void debug(String content) {
        log(LogRecord.Level.DEBUG, content);
    }

    public void info(String content) {
        log(LogRecord.Level.INFO, content);
    }

    public void warn(String content) {
        log(LogRecord.Level.WARN, content);
    }

    public void stop() {
        log(LogRecord.Level.INFO, "Preparing to exit...");
        List<Runnable> runnableList = workerThread.shutdownNow();
        for (Runnable x : runnableList) {
            x.run();
        }
        for (LogHandler handler : handlers) {
            handler.stop();
        }
    }
}
