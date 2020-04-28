package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.formatter.DailyRollingRecordFormatter;
import moe.langua.lab.utils.logger.formatter.RecordFormatter;
import moe.langua.lab.utils.logger.utils.LogRecord;
import moe.langua.lab.utils.logger.utils.LogRecordProcessingChain;
import moe.langua.lab.utils.logger.utils.ConfiguredGZIPOutputStream;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyRollingFileLogHandler implements LogHandler {
    private static final RecordFormatter formatter = new DailyRollingRecordFormatter();
    private final File dataRoot;
    private final Thread typist;
    private final LogRecordProcessingChain logRecordProcessingChain = new LogRecordProcessingChain();
    private final File logFile;
    private final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    private final LogRecord.Level minimalRecordLevel;
    private FileWriter writer;

    private final static long millsADay = 86400000;
    private long nextRoll;
    private boolean stopped = false;

    public DailyRollingFileLogHandler(LogRecord.Level level, File dataRoot) throws IOException {
        this.minimalRecordLevel = level;
        this.dataRoot = dataRoot;
        this.logFile = new File(dataRoot.getAbsolutePath() + "/latest.log");
        nextRoll = (System.currentTimeMillis() / millsADay) * millsADay;
        typist = new Thread(() -> {
            while (true) {
                if (stopped && logRecordProcessingChain.size() == 0) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                if (!logRecordProcessingChain.hasNext()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    LogRecord record = logRecordProcessingChain.getNext();
                    try {
                        write(formatter.format(record));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        typist.setDaemon(true);
        typist.start();
    }

    private void write(String content) throws IOException {
        getWriter().append(content);
        writer.flush();
    }

    private FileWriter getWriter() throws IOException {
        long now = System.currentTimeMillis();
        if (now > nextRoll) {
            roll();
        }
        return writer;
    }

    private void roll() throws IOException {
        if (logFile.length() != 0) {
            byte[] buffer = new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream(getGzipFile());
            ConfiguredGZIPOutputStream gzipOutputStream = new ConfiguredGZIPOutputStream(fileOutputStream);
            FileInputStream fileInput = new FileInputStream(logFile);
            int bytes_read;
            while ((bytes_read = fileInput.read(buffer)) > 0) {
                gzipOutputStream.write(buffer, 0, bytes_read);
            }
            fileInput.close();
            gzipOutputStream.finish();
            gzipOutputStream.close();
            logFile.delete();
            logFile.createNewFile();
        }
        if (writer == null) writer = new FileWriter(logFile,false);
        while (nextRoll<System.currentTimeMillis()){
            nextRoll += millsADay;
        }
    }

    private File getGzipFile() throws IOException {
        long time = nextRoll - millsADay;
        String basicName = dateFormat.format(new Date(time));
        File logGZipFile = new File(dataRoot.getAbsolutePath() + "/" + basicName + ".log.gz");
        long gzipFileNumber = 0;
        while (logGZipFile.exists()) {
            logGZipFile = new File(dataRoot.getAbsolutePath() + "/" + basicName + "-" + (++gzipFileNumber) + ".log.gz");
        }
        logGZipFile.createNewFile();
        return logGZipFile;
    }

    @Override
    public void log(LogRecord logRecord) {
        if(logRecord.recordLevel.level<minimalRecordLevel.level) return;
        logRecordProcessingChain.addRecord(logRecord);
    }

    @Override
    public void stop() {
        try {
            stopped = true;
            typist.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
