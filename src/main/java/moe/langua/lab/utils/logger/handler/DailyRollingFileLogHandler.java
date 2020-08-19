package moe.langua.lab.utils.logger.handler;

import moe.langua.lab.utils.logger.formatter.DailyRollingRecordFormatter;
import moe.langua.lab.utils.logger.formatter.RecordFormatter;
import moe.langua.lab.utils.logger.utils.ConfiguredGZIPOutputStream;
import moe.langua.lab.utils.logger.utils.LogRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyRollingFileLogHandler implements LogHandler {
    private final int minimumLevel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final RecordFormatter formatter = new DailyRollingRecordFormatter();
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final int bufferSizeInByte;
    private final File dataRoot;
    private final File latestLogFile;
    private FileOutputStream latestLogFileOutputStream;
    private long nextRoll;

    private void build() throws IOException{
        if (!latestLogFile.createNewFile() && !latestLogFile.isFile()) {
            throw new IOException(latestLogFile.getAbsolutePath() + " should be a file, but found a directory.");
        }
        if (latestLogFile.length() != 0) {
            packLatestLogFileToGzip(dateFormat.format(latestLogFile.lastModified()));
            if (!latestLogFile.delete() || !latestLogFile.createNewFile())
                throw new IOException("Failed to reset " + latestLogFile.getAbsolutePath());
        }
        latestLogFileOutputStream = new FileOutputStream(latestLogFile, true);
        long days = (System.currentTimeMillis()) / 86400000;
        nextRoll = (days + 1) * 86400000;
    }

    public DailyRollingFileLogHandler(LogRecord.Level minimumLevel, File dataRoot) throws IOException {
        this.minimumLevel = minimumLevel.level;
        this.dataRoot = new File(dataRoot.getAbsolutePath());
        this.bufferSizeInByte = 65536;
        this.latestLogFile = new File(dataRoot, "latest.log");
        build();
    }

    public DailyRollingFileLogHandler(LogRecord.Level minimumLevel, File dataRoot, int bufferSizeInByte) throws IOException {
        this.minimumLevel = minimumLevel.level;
        this.dataRoot = new File(dataRoot.getAbsolutePath());
        this.bufferSizeInByte = bufferSizeInByte;
        this.latestLogFile = new File(dataRoot, "latest.log");
        build();
    }

    private synchronized void write(LogRecord logRecord) throws IOException {
        long now = System.currentTimeMillis();
        if (now >= nextRoll) roll(now);
        buffer.write(formatter.format(logRecord).getBytes(StandardCharsets.UTF_8));
        if(buffer.size()>bufferSizeInByte){
            buffer.writeTo(latestLogFileOutputStream);
            buffer.reset();
        }
    }

    private void roll(long timeStamp) throws IOException {
        buffer.writeTo(latestLogFileOutputStream);
        buffer.reset();
        latestLogFileOutputStream.close();
        packLatestLogFileToGzip(dateFormat.format(new Date(nextRoll - 10000)));
        if (!latestLogFile.delete() || !latestLogFile.createNewFile())
            throw new IOException("Failed to reset " + latestLogFile.getAbsolutePath());
        latestLogFileOutputStream = new FileOutputStream(latestLogFile);
        long days = timeStamp / 86400000;
        nextRoll = (days + 1) * 86400000;
    }

    private void packLatestLogFileToGzip(String fileName) throws IOException {
        File gzipFile = new File(dataRoot, fileName + ".gz");
        long differ = 0;
        while (!gzipFile.createNewFile()) {
            gzipFile = new File(dataRoot, fileName + "-" + differ++ + ".gz");
        }
        FileOutputStream fileOutputStream = new FileOutputStream(gzipFile, false);
        ConfiguredGZIPOutputStream configuredGZIPOutputStream = new ConfiguredGZIPOutputStream(fileOutputStream);
        FileInputStream logFileInputStream = new FileInputStream(latestLogFile);
        byte[] buffer = new byte[0xFFFF];
        int bytesRead;
        while ((bytesRead = logFileInputStream.read(buffer)) > 0) {
            configuredGZIPOutputStream.write(buffer, 0, bytesRead);
        }
        configuredGZIPOutputStream.close();
        logFileInputStream.close();
    }

    @Override
    public void log(LogRecord logRecord) {
        if (logRecord.recordLevel.level < minimumLevel) return;
        try {
            write(logRecord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            buffer.writeTo(latestLogFileOutputStream);
            buffer.reset();
            latestLogFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
