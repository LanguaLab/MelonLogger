package moe.langua.lab.utils.logger.test;

import moe.langua.lab.utils.logger.MelonLogger;
import moe.langua.lab.utils.logger.handler.ConsoleLogHandler;
import moe.langua.lab.utils.logger.utils.LogRecord;

import java.util.Random;

public class TestField {
    public static void main(String[] args) throws InterruptedException {
        MelonLogger logger = MelonLogger.getLogger();
        logger.addHandler(new ConsoleLogHandler(LogRecord.Level.DEBUG));
        Random random = new Random();
        Thread.sleep(10000);
        //A huge LogRecordProcessingChain will be created here
        for (int i = 0; i < 0x4FFFFF; i++) logger.log(LogRecord.Level.INFO, "" + random.nextInt());
        Thread.sleep(10000);
        logger.log(LogRecord.Level.FINE, "wwwwwwwww");
        logger.log(LogRecord.Level.FATAL, "stop...!");
    }
}
