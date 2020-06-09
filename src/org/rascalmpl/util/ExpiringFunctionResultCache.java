/** 
 * Copyright (c) 2020, Davy Landman, swat.engineering 
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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.usethesource.vallang.IValue;

/**
 * Cache of Rascal function results. <br/>
 * <br/>
 * Can be configured in multiple expiring strategies, but will always use SoftReferences to avoid OOM. <br/>
 * <br/>
 * This class is safe to use from multiple threads, use the result of
 * {@link ExpiringFunctionResultCache#store(IValue[], Map, Object)} if you want to make sure you all agree on the same
 * result.
 * 
 * @param <TResult> the type of values stored as result
 */
public class ExpiringFunctionResultCache<TResult> {
    /**
     * Entries are inserted by the interpreter/compiler but cleared by the cleanup thread <br/>
     * <br/>
     * The keys in this map are bit special, we use a different class for lookup and for storing. 
     * This is primarily to avoid allocation SoftReferences and a fresh map purely for lookup.
     */
    private final ConcurrentMap<Object, ValueSoftReference<TResult>> entries;

    /**
     * A queue of all the SoftReferences cleared, we later iterate through them to cleanup the entries in the map
     */
    @SuppressWarnings("rawtypes")
    private final ReferenceQueue cleared;

    /**
     * Threshold for when to expire entries by time
     */
    private int secondsTimeout;

    /**
     * Cache of the oldest entry in the map, to avoid unneeded iterating of the entry map
     */
    private volatile int lastOldest = 0;
    
    /**
     * Threshold for maximum entries in the cache
     */
    private int maxEntries;
    

    /**
     * Construct a cache of function results that expire after (optional) certain thresholds
     * @param secondsTimeout clear entries that haven't been used for this amount of seconds, <= 0 disables this expiration behavoir
     * @param maxEntries starts clearing out entries after the table gets "full". <= 0 disables this threshold
     */
    @SuppressWarnings("rawtypes")
    public ExpiringFunctionResultCache(int secondsTimeout, int maxEntries) {
        this.entries =  new ConcurrentHashMap<>();
        this.cleared =  new ReferenceQueue();
        this.secondsTimeout = secondsTimeout <= 0 ? Integer.MAX_VALUE : secondsTimeout;
        this.maxEntries = maxEntries <= 0 ? Integer.MAX_VALUE : maxEntries;
        Cleanup.Instance.register(this);
    }
    
    /**
     * Get cached function result
     * @param args positional arguments
     * @param kwParameters (nullable) keyword arguments
     * @return either cached result or null in case of no entry
     */
    public @Nullable TResult lookup(IValue[] args, @Nullable Map<String, IValue> kwParameters) {
        ValueSoftReference<TResult> result = entries.get(new KeyTuple(args, kwParameters, false));
        if (result != null) {
            return result.use();
        }
        return null;
    }
    
    /**
     * Store result of the function call in the map
     * @param args
     * @param kwParameters
     * @param result
     * @return
     */
    public TResult store(IValue[] args, @Nullable Map<String, IValue> kwParameters, TResult result) {
        final KeyTuple key = new KeyTuple(args, kwParameters, true);
        final KeyTupleSoftReference keyRef = new KeyTupleSoftReference(key, cleared);
        final ValueSoftReference<TResult> value = new ValueSoftReference<>(result, keyRef, cleared);
        while (true) {
            // we "race" to insert our mapping
            ValueSoftReference<TResult> stored = entries.putIfAbsent(keyRef, value);
            if (stored == null) {
                // new entry, so we won the race
                return result;
            }
            TResult otherResult = stored.use();
            if (otherResult != null) {
                // we officially lost the race
                // other entry still had a valid value, so we return that instead
                return otherResult;
            }
            // the entry has been cleared, so we need to remove it and try the race again
            entries.remove(stored.key, stored);
        }
    }
    
    /**
     * Clear entries and try to help GC with cleaning up memory
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Internal method only, used by cleanup thread
     */
    private void cleanup() {
        removePartiallyClearedEntries();
        removeExpiredEntries();
        removeOverflowingEntires();
    }

    private void removePartiallyClearedEntries() {
        Map<KeyTupleSoftReference, Object> toCleanup = new IdentityHashMap<>(); // we want reference equality, since it could be that both the key & the value are cleared in the same period
        synchronized (cleared) {
            Reference<?> gced = null;
            do {
                gced = cleared.poll();
                if (gced instanceof KeyTupleSoftReference) {
                    toCleanup.putIfAbsent((KeyTupleSoftReference) gced, gced);
                }
                else if (gced instanceof ValueSoftReference) {
                    toCleanup.putIfAbsent(((ValueSoftReference<?>) gced).key, gced);
                }
            }
            while (gced != null);
        }

        toCleanup.keySet().forEach(entries::remove);
    }


    private void removeExpiredEntries() {
        int currentTick = SecondsTicker.current();
        int oldestTick = currentTick - secondsTimeout;
        // to avoid iterating over all the values everytime, we keep around the oldest use we saw the last time we iterated
        if (lastOldest < oldestTick) {
            // there might be an expired entry (or it could have been cleared)
            int newOldest = currentTick; // we calculate the oldest entry that is kept in the cache
            Iterator<ValueSoftReference<TResult>> it = entries.values().iterator();
            while (it.hasNext()) {
                ValueSoftReference<TResult> cur = it.next();
                int lastUsed = cur.lastUsed;

                if (lastUsed < oldestTick) {
                    it.remove();
                }
                else if (lastUsed < newOldest) {
                    newOldest = lastUsed;
                }
            }
            lastOldest = newOldest;
        }
    }

    private void removeOverflowingEntires() {
        int toRemove = entries.size() - maxEntries;
        toRemove += toRemove / 4; // always cleanout 25% more than needed
        if (toRemove > 0) {
            // we have to clear some entries, since we don't keep a sorted tree based on the usage
            // we'll just randomly clear
            Iterator<Entry<Object, ValueSoftReference<TResult>>> it = entries.entrySet().iterator();
            while (toRemove > 0 && it.hasNext()) {
                it.next();
                it.remove();
                toRemove--;
            }
        }
        
    }


    private static class KeyTuple {
        private final int storedHash;
        private final IValue[] params;
        private final Map<String, IValue> keyArgs;

        public KeyTuple(IValue[] params, Map<String, IValue> keyArgs, boolean copy) {
            this.storedHash =  (1 + (31 * Arrays.hashCode(params))) + keyArgs.hashCode();
            if (copy) {
                this.params = new IValue[params.length];
                System.arraycopy(params, 0, this.params, 0, params.length);
                if (keyArgs == Collections.EMPTY_MAP) {
                    this.keyArgs = keyArgs;
                }
                else {
                    this.keyArgs = new HashMap<>(keyArgs);
                }
            }
            else {
                this.params = params;
                this.keyArgs = keyArgs;
            }
        }
        
        @Override
        public int hashCode() {
            return storedHash;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof KeyTupleSoftReference && ((KeyTupleSoftReference) obj).hashCode == storedHash) {
                // key in the map, so we have to look inside of it
                return equals(((KeyTupleSoftReference)obj).get());
            }
            if (obj instanceof KeyTuple) {
                KeyTuple other = (KeyTuple)obj;
                return other.storedHash == this.storedHash
                    && Arrays.equals(params, other.params)
                    && keyArgs.equals(other.keyArgs)
                    ;
            }
            return false;
        }
    }

    private static class KeyTupleSoftReference extends SoftReference<KeyTuple> {
        private final int hashCode;

        @SuppressWarnings("unchecked")
        public KeyTupleSoftReference(KeyTuple obj, @SuppressWarnings("rawtypes") ReferenceQueue queue) {
            super(obj, queue);
            this.hashCode = obj.storedHash;
        }
        
        @Override
        public int hashCode() {
            return hashCode;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof KeyTuple) {
                return obj.equals(this);
            }
            if (obj instanceof KeyTupleSoftReference && ((KeyTupleSoftReference) obj).hashCode == hashCode) {
                KeyTuple our = get();
                return our != null && our.equals(((KeyTupleSoftReference)obj).get());
            }
            return false;
        }

        
    }

    private static class ValueSoftReference<T> extends SoftReference<T> {
        private volatile int lastUsed;
        private final KeyTupleSoftReference key;

        @SuppressWarnings({"unchecked"})
        public ValueSoftReference(T obj, KeyTupleSoftReference key, @SuppressWarnings("rawtypes") ReferenceQueue queue) {
            super(obj, queue);
            this.lastUsed = SecondsTicker.current();
            this.key = key;
        } 
        
        public T use() {
            lastUsed = SecondsTicker.current();
            return get();
        }
    }
    

    /**
     * Cleanup singleton that wraps {@linkplain CleanupThread}
     */
    private static enum Cleanup {
        Instance;

        private final CleanupThread thread;

        private Cleanup() {
            thread = new CleanupThread();
            thread.start();
        }

        public void register(@UnknownInitialization ExpiringFunctionResultCache<?> cache) {
            thread.register(cache);
        }

    }

    private static class CleanupThread extends Thread {
        private final ConcurrentLinkedQueue<WeakReference<ExpiringFunctionResultCache<?>>> caches = new ConcurrentLinkedQueue<>();
        
        public CleanupThread() {
            super("Cleanup Thread for " + ExpiringFunctionResultCache.class.getName());
            setDaemon(true);
        }

        @SuppressWarnings("initialization") // passed in reference might not be completly initialized
        public void register(@UnknownInitialization ExpiringFunctionResultCache<?> cache) {
            caches.add(new WeakReference<>(cache));
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    return;
                }
                try {
                    Iterator<WeakReference<ExpiringFunctionResultCache<?>>> it = caches.iterator();
                    while (it.hasNext()) {
                        ExpiringFunctionResultCache<?> cur = it.next().get();
                        if (cur == null) {
                            it.remove();
                        }
                        else {
                            cur.cleanup();
                        }
                    }
                }
                catch (Throwable e) {
                    System.err.println("Cleanup thread failed with: " + e.getMessage());
                    e.printStackTrace(System.err);
                }
            }
        }
    }


}
