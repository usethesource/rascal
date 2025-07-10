package org.rascalmpl.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Construct a thread pool that will not keep the JVM alive (aka daemon threads)
 */
public class DaemonThreadPool {
    private DaemonThreadPool() {}
    private static final int MAX_CACHED = Math.max(2, Runtime.getRuntime().availableProcessors() - 2);

    public static ExecutorService buildCached(String name, int max) {
        return new ThreadPoolExecutor(
            0, Math.min(MAX_CACHED, max), 
            60, TimeUnit.SECONDS, 
            new LinkedBlockingQueue<>(),
            buildFactory(name)
        );
    }

    private static ThreadFactory buildFactory(String name) {
        return new ThreadFactory() {
            private final AtomicInteger id = new AtomicInteger(0);
            private final ThreadGroup group = new ThreadGroup(name);

            @Override
            public Thread newThread(Runnable r) {
                var t = new Thread(group, r, name + "-" + id.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        };
    }
    
}
