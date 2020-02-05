/** 

 * Copyright (c) 2018, Davy Landman, SWAT.engineering
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A concurrent, non-blocking, Object Pool, which stores the object in SoftReferences to avoid OOM. <br>
 * <br>
 * It also evicts entries if they are not accessed for a configurable time. This is to reduce the memory pressure, as SoftReferences are only cleared in case the memory runs out. <br>
 * <br>
 * This class is useful if you have objects that are expensive to initialize, are not thread-safe, but you want to reuse instances (possibly over several threads). 
 * For some cases, a ThreadLocal is good enough, yet they are never cleared, and if you are running inside a large thread pool, you will get a lot of unused instances.
 * 
 * @author Davy Landman
 */
public class ConcurrentSoftReferenceObjectPool<T> {

	
	private final Deque<TimestampedSoftReference<T>> availableObjects = new ConcurrentLinkedDeque<>();
	private final ReferenceQueue<T> cleanedReferences = new ReferenceQueue<>();

	// We cache the size of the Deque, as the size operation is not constant time on the ConcurrentLinkedDeqeue
	private final AtomicInteger queueSize = new AtomicInteger(0);
	private final AtomicInteger live = new AtomicInteger(0);
	// In case of a heavy load on the object pool, this semaphore signals the cleanup to run more often 
	private final Semaphore returnSignal = new Semaphore(0, true);

    private final long timeout;
    private final Supplier<T> initializeObject;
    private final int keepAlive;
    private final int maxAlive;
	
    /**
     * Construct an thread-safe, non-blocking, object pool with soft references. The objects can also be cleared after a specific time of access.
     * @param timeout access timeout after which objects are cleared (LRU alike property)
     * @param timeoutUnit the unit of the  timeout parameter.
     * @param keepAlive how many objects should always be kept in the pool. These objects will not respect clearing caused by the timeout parameter.
     * @param maxAlive how many objects should be alive at max. This will turn the it into a blocking pool, that waits until one is available instead of initializing a new object. Set it to Integer.MAX_VALUE to make sure it never blocks
     * @param initializeObject a function that generates a new object for this pool. <strong> Make sure this code is thread-safe! It can be called from different threads, in parallel </strong>
     */
	public ConcurrentSoftReferenceObjectPool(long timeout, TimeUnit timeoutUnit, int keepAlive, int maxAlive, Supplier<T> initializeObject) {
        if (timeout <= 0) {
	        throw new IllegalArgumentException("Timeout should be > 0");
	    }
	    if (keepAlive < 0) {
	        throw new IllegalArgumentException("keepAlive argument should be 0 or higher");
	    }
	    if (maxAlive < keepAlive) {
	        throw new IllegalArgumentException("maxAlive should be not be lower than keepAlive");
	    }
	    this.maxAlive = maxAlive;
        this.keepAlive = keepAlive;
        this.timeout = timeoutUnit.toNanos(timeout);
        this.initializeObject = Objects.requireNonNull(initializeObject);

        Thread cleanup = new Thread(new CleanupRunner<>(this));
        cleanup.setName("Cleanup thread for: " + this);
        cleanup.setDaemon(true);
        cleanup.start();
	}
	
	/**
	 * Run a function against an object in this pool, and immediatly return it.
	 */
	public <R> R useAndReturn(Function<T, R> func) {
	    TimestampedSoftReference<T> obj = null;
	    T worker = null;
	    while (obj == null || (worker = obj.get()) == null) {
	         obj = checkoutObject();
	    }
	    try {
	        return func.apply(worker);
	    }
	    finally {
	        returnObject(obj);
	    }
	}
	
	public void prepare(int howMany) {
	    // schedular for the initialization tasks, will close after not being used anymore
	    ThreadPoolExecutor pool = new ThreadPoolExecutor(Math.min(4, howMany), Math.min(4, howMany),
	            1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	    pool.allowCoreThreadTimeOut(true); // so we don't have to schedule a shutdown
	    for (int i = 0; i < Math.min(maxAlive, howMany); i++) {
	        pool.execute(() -> {
	            TimestampedSoftReference<T> newEntry = new TimestampedSoftReference<>(initializeObject.get(), cleanedReferences);
                live.incrementAndGet();
                returnObject(newEntry);
	        });
	    }
	}
	
	public boolean healthCheck() {
	    for (int i = 0; i < 10; i++) {
	        // it could be that we are just in the middle of an off by one window, so loop a few times to make sure that's not the case
	        if (this.queueSize.get() == this.availableObjects.size()) {
	            return true;
	        }
	    }
	    return false;
	}

    private void returnObject(TimestampedSoftReference<T> obj) {
        if (queueSize.get() < maxAlive) {
            // only return it if we don't have more than queueSize alive
            availableObjects.addFirst(obj);
            queueSize.incrementAndGet();
            returnSignal.release();
        }
        else {
            live.decrementAndGet();
        }
    }


    private TimestampedSoftReference<T> checkoutObject() {
        TimestampedSoftReference<T> result;
        while ((result = availableObjects.pollFirst()) != null) {
            queueSize.decrementAndGet();
            if (result.get() != null) {
                // this get also activates the 
                return result;
            }
        }
        if (live.get() < maxAlive) {
            // we have to construct a new one, as the queue is empty
            live.incrementAndGet();
            return new TimestampedSoftReference<>(initializeObject.get(), cleanedReferences);
        }
        else {
            // we have to wait until a new one is released and try again.
            try {
                returnSignal.acquire();
            }
            catch (InterruptedException e) {
                // just pass it along
                Thread.currentThread().interrupt();
            }
            return checkoutObject();
        }
    }
    
    /**
     * Cleanup the object pool, either because a soft reference was cleared, or because a access timeout occured.
     */
    private static final class CleanupRunner<T> implements Runnable {
        /**
         * Copy of the actual pool to make sure 
         */
        private final Semaphore returnSignal;
        private final long timeout;

        /**
         * A weak reference to make sure this thread can detect nobody has a reference to the pool anymore
         */
        private final WeakReference<ConcurrentSoftReferenceObjectPool<T>> targetPool;

        public CleanupRunner(ConcurrentSoftReferenceObjectPool<T> targetPool) {
            this.returnSignal = targetPool.returnSignal;
            this.timeout = targetPool.timeout;
            this.targetPool = new WeakReference<>(targetPool);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void run() {
            try {
                while (true) {
                    // sleep either the timeout period, or until 100 objects have been returned in that span (could indicate a big load)
                    returnSignal.tryAcquire(100, timeout, TimeUnit.NANOSECONDS);

                    final ConcurrentSoftReferenceObjectPool<T> target = targetPool.get();
                    if (target == null) {
                        // we were the only ones holding a reference to this pool, so the reference got cleared out
                        // meaning we can stop cleaning
                        return;
                    }
                    synchronized (target.cleanedReferences) {
                        // cleanup cleared items
                        TimestampedSoftReference<T> cleared;
                        while ((cleared = (TimestampedSoftReference<T>)target.cleanedReferences.poll()) != null) {
                            // remove from the queue if it was still in it, start at the back, as most likely the oldest get cleared first
                            if (target.availableObjects.removeLastOccurrence(cleared)) {
                                target.queueSize.decrementAndGet();
                                target.live.decrementAndGet();
                            }
                        }
                    }

                    long outdatedTimeStamp = System.nanoTime() - timeout;
                    while (target.queueSize.get() > target.keepAlive) {
                        // try to see if we can clear more objects in the pool, if they are old enough
                        TimestampedSoftReference<T> last = target.availableObjects.pollLast();
                        if (last == null || last.accessTimestamp > outdatedTimeStamp) {
                            // since the queue is ordered by acces time, we stop at the first object that is not old enough to be removed
                            if (last != null) {
                                // put it back at the end of the queue
                                target.availableObjects.addLast(last);
                            }
                            break;
                        }
                        last.clear();
                        target.queueSize.decrementAndGet();
                        target.live.decrementAndGet();
                    }
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

	private static final class TimestampedSoftReference<S> extends SoftReference<S> {
		private volatile long accessTimestamp;

		public TimestampedSoftReference(S referent, ReferenceQueue<? super S> q) {
			super(referent, q);
			accessTimestamp = System.nanoTime();
		}
		
		@Override
		public S get() {
			S result = super.get();
			if (result != null) {
				touch();
			}
			return result;
		}
		
		public void touch() {
			accessTimestamp = System.nanoTime();
		}
	}

}
