package moe.langua.lab.utils.logger.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedDaemonThreadFactory implements ThreadFactory {
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final String groupName;

    public NamedDaemonThreadFactory(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread target = new Thread(r, groupName+" #" + atomicInteger.getAndAdd(1));
        target.setDaemon(true);
        return target;
    }
}
