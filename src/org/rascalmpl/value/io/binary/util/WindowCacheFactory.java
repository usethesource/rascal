/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.value.io.binary.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Since we are constructing and deconstructing a lot of windows, use this factory to build them.
 * For caching reasons, also return the windows to this factory, so they can be reused again.
 *
 */
public class WindowCacheFactory {
	static private class InstanceHolder {
		static final WindowCacheFactory sInstance = new WindowCacheFactory();
	}
	
	public static WindowCacheFactory getInstance() {
		return InstanceHolder.sInstance;
	}
	
	// the cache expires after 60 seconds
	private final static long EXPIRE_AFTER = TimeUnit.SECONDS.toNanos(60);
	
	// or when more memory is needed.
	private static final class LastUsedTracker<T> extends SoftReference<T> {
        private final long lastUsed;

        public LastUsedTracker(T obj, ReferenceQueue<T> queue) {
            super(obj, queue);
	        this.lastUsed = System.nanoTime();
        } 
        
        public boolean clearIfOlderThan(long timeStamp) {
            if (timeStamp > lastUsed) {
                clear();
                return true;
            }
            return false;
        }
        
        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
	}
	
	private static final class SoftPool<T> {
	    private final Deque<LastUsedTracker<T>> dequeue = new ConcurrentLinkedDeque<>();
	    private final ReferenceQueue<T> references = new ReferenceQueue<>();
	    
	    public void performHouseKeeping() {
	        synchronized (references) {
	            Object cleared;
	            while ((cleared = references.poll()) != null) {
	                System.err.println("Clearing: " + cleared);
	                dequeue.removeLastOccurrence(cleared);
	            }
            }
	    }

        public SoftReference<T> poll() {
            return dequeue.poll();
        }

        public void push(T o) {
            dequeue.push(new LastUsedTracker<>(o, references));
        }

        public Iterator<LastUsedTracker<T>> descendingIterator() {
            return dequeue.descendingIterator();
        }
	}

	private final Semaphore scheduleCleanups = new Semaphore(0);
    private final Map<Integer, SoftPool<TrackLastRead<Object>>> lastReads = new ConcurrentHashMap<>();
    private final Map<Integer, SoftPool<TrackLastWritten<Object>>> lastWrittenReference = new ConcurrentHashMap<>();
    private final Map<Integer, SoftPool<TrackLastWritten<Object>>> lastWrittenObject = new ConcurrentHashMap<>();
    
    private final TrackLastRead<Object> disabledReadWindow = new TrackLastRead<Object>() {
        @Override
        public Object lookBack(int howLongBack) { throw new IllegalArgumentException(); }
        @Override
        public void read(Object obj) { }
    };
    private TrackLastWritten<Object> disabledWriteWindow = new TrackLastWritten<Object>() {
        @Override
        public void write(Object obj) { }
        @Override
        public int howLongAgo(Object obj) { return -1; }
    };
    
    @SuppressWarnings("unchecked")
    public <T> TrackLastRead<T> getTrackLastRead(int size) {
        if (size == 0) {
            return (TrackLastRead<T>) disabledReadWindow;
        }
        return (TrackLastRead<T>) computeIfAbsent(lastReads, size, LinearCircularLookupWindow::new);
    }

    @SuppressWarnings("unchecked")
    public <T> TrackLastWritten<T> getTrackLastWrittenReferenceEquality(int size) {
        if (size == 0) {
            return (TrackLastWritten<T>) disabledWriteWindow;
        }
        return (TrackLastWritten<T>) computeIfAbsent(lastWrittenReference, size, (s) -> OpenAddressingLastWritten.referenceEquality(s));
    }
    @SuppressWarnings("unchecked")
    public <T> TrackLastWritten<T> getTrackLastWrittenObjectEquality(int size) {
        if (size == 0) {
            return (TrackLastWritten<T>) disabledWriteWindow;
        }
        return (TrackLastWritten<T>) computeIfAbsent(lastWrittenObject, size, (s) -> OpenAddressingLastWritten.objectEquality(s));
    }
    
    @SuppressWarnings("unchecked")
    public <T> void returnTrackLastRead(TrackLastRead<T> returned) {
        if (returned != disabledReadWindow) {
            clearAndReturn(lastReads, (TrackLastRead<Object>) returned);
        }
    }
    @SuppressWarnings("unchecked")
    public <T> void returnTrackLastWrittenReferenceEquality(TrackLastWritten<T> returned) {
        if (returned != disabledWriteWindow) {
            clearAndReturn(lastWrittenReference, (TrackLastWritten<Object>) returned);
        }
    }
    @SuppressWarnings("unchecked")
    public <T> void returnTrackLastWrittenObjectEquality(TrackLastWritten<T> returned) {
        if (returned != disabledWriteWindow) {
            clearAndReturn(lastWrittenObject, (TrackLastWritten<Object>) returned);
        }
    }
    
    

    private <T> void clearAndReturn(Map<Integer, SoftPool<T>> cache, T returned) {
        if (returned instanceof ClearableWindow) {
            ClearableWindow clearable = (ClearableWindow)returned;
            if (clearable.size() >= 1000) {
                clearable.clear();
                SoftPool<T> entries = cache.computeIfAbsent(clearable.size(), i -> new SoftPool<>());
                entries.push(returned);
                scheduleCleanups.release();
            }
        }
    }

    private static <T> T computeIfAbsent(Map<Integer, SoftPool<T>> cache, int size, Function<Integer, T> constructNew) {
        SoftPool<T> reads = cache.computeIfAbsent(size, i -> new SoftPool<>());
        SoftReference<T> tracker;
        while ((tracker = reads.poll()) != null) {
            T result = tracker.get();
            if (result != null) {
                return result;
            }
            System.err.println("A reference was cleared!");
        }
        return constructNew.apply(size);
    }
    
    private WindowCacheFactory() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        // we either wait at max the EXPIRE_AFTER time, or we have enough updates to the maps that some cleaning might be nice
                        scheduleCleanups.tryAcquire(1000, EXPIRE_AFTER, TimeUnit.NANOSECONDS);
                        cleanMap(lastReads);
                        cleanMap(lastWrittenReference);
                        cleanMap(lastWrittenObject);
                    }
                }
                catch (InterruptedException e) {
                }
            }

            private <T> void cleanMap(Map<Integer, SoftPool<T>> cache) {
                long cleanBefore = System.nanoTime() - EXPIRE_AFTER;
                for (SoftPool<T> v : cache.values()) {
                    Iterator<LastUsedTracker<T>> it = v.descendingIterator();
                    while (it.hasNext()) {
                        LastUsedTracker<T> current = it.next();
                        if (current.clearIfOlderThan(cleanBefore)) {
                            System.err.println("Removing old one");
                            it.remove();
                        }
                        else {
                            break; // end of the chain of oudated stuff reached
                        }
                    }
                    v.performHouseKeeping();
                }
            }
        });
        t.setName("Cleanup Window caches");
        t.setDaemon(true);
        t.start();
    }

}
