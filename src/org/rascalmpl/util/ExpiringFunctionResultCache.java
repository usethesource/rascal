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
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.Nullable;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.util.SecondsTicker;

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

    private final Cache<Object, TResult> entries;
    private final ReferenceQueue<Parameters> cleared;

    /**
     * Construct a cache of function results that expire after (optional) certain thresholds
     * @param secondsTimeout clear entries that haven't been used for this amount of seconds, <= 0 disables this expiration behavoir
     * @param maxEntries starts clearing out entries after the table gets "full". <= 0 disables this threshold
     */
    public ExpiringFunctionResultCache(int secondsTimeout, int maxEntries) {
        entries = configure(secondsTimeout, maxEntries)
            .softValues()
            .build();
        this.cleared =  new ReferenceQueue<>();
        ExpiringFunctionResultCache.scheduleCleanup(this);
    }

    private static Caffeine<Object, Object> configure(int secondsTimeout, int maxEntries) {
        var result = Caffeine.newBuilder();
        if (secondsTimeout > 0) {
            result = result.ticker(ExpiringFunctionResultCache::simulateNanoTicks)
                .expireAfterAccess(Duration.ofSeconds(secondsTimeout));
        }
        if (maxEntries > 0) {
            result = result.maximumSize(maxEntries);
        }
        return result.scheduler(Scheduler.systemScheduler());
    }

    private static long simulateNanoTicks() {
        return TimeUnit.SECONDS.toNanos(SecondsTicker.current());
    }
    
    /**
     * Get cached function result
     * @param args positional arguments
     * @param kwParameters (nullable) keyword arguments
     * @return either cached result or null in case of no entry
     */
    public @Nullable TResult lookup(IValue[] args, @Nullable Map<String, IValue> kwParameters) {
        return entries.getIfPresent(Parameters.forLookup(args, kwParameters));
    }
    
    /**
     * Store result of the function call in the map
     * @param args
     * @param kwParameters
     * @param result
     * @return
     */
    public TResult store(IValue[] args, @Nullable Map<String, IValue> kwParameters, TResult result) {
        return entries.get(
            new ParametersRef(Parameters.forStorage(args, kwParameters), cleared), 
            k -> result
        );
    }
    
    /**
     * Clear entries and try to help GC with cleaning up memory
     */
    public void clear() {
        entries.invalidateAll();
    }

    private static void scheduleCleanup(@UnknownInitialization ExpiringFunctionResultCache<?> cache) {
        doCleanup(new WeakReference<>(cache));
    }

    private static void doCleanup(WeakReference<ExpiringFunctionResultCache<?>> cache) {
        var actualCache = cache.get();
        if (actualCache == null) {
            return;
        }
        // we schedule the next run
        CompletableFuture
            .delayedExecutor(5, TimeUnit.SECONDS)
            .execute(() -> doCleanup(cache));

        // then we do the actual cleanup
        synchronized (actualCache.cleared) {
            Reference<?> gced;
            while ((gced = actualCache.cleared.poll()) != null) {
                actualCache.entries.invalidate(gced);
            }
        }
    }


    /**
     * Main class that joins both the positional and keyword parameters
     * 
     * It has two modes, one for lookup, and one for storage, only for storage do we copy the array
     */
    private static class Parameters {
        private final int storedHash;
        private final IValue[] params;
        private final Map<String, IValue> keyArgs;

        
        private Parameters(IValue[] params, Map<String, IValue> keyArgs) {
            this.params = params;
            this.keyArgs = keyArgs;
            this.storedHash = (1 + (31 * Arrays.hashCode(this.params))) + keyArgs.hashCode();
        }

        public static Parameters forLookup(IValue[] params, @Nullable Map<String, IValue> keyArgs) {
            if (keyArgs == null || keyArgs.isEmpty()) {
                keyArgs = Collections.emptyMap();
            }
            return new Parameters(params, keyArgs);
        }

        public static Parameters forStorage(IValue[] params, @Nullable Map<String, IValue> keyArgs) {
            if (keyArgs == null || keyArgs.isEmpty()) {
                keyArgs = Collections.emptyMap();
            }
            var newParams = new IValue[params.length];
            System.arraycopy(params, 0, newParams, 0, params.length);
            return new Parameters(newParams, keyArgs);
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
            if (obj instanceof ParametersRef && ((ParametersRef) obj).hashCode == storedHash) {
                // key in the map, so we have to look inside of it
                return equals(((ParametersRef)obj).get());
            }
            if (obj instanceof Parameters) {
                Parameters other = (Parameters)obj;
                return other.storedHash == this.storedHash
                    && Arrays.equals(params, other.params)
                    && keyArgs.equals(other.keyArgs)
                    ;
            }
            return false;
        }
    }

    /** 
     * Small SoftReference wrapper that adds equality, and supports comparing with the raw Parameters object so that both can be used in a get
    */
    private static class ParametersRef extends SoftReference<Parameters> {
        private final int hashCode;

        public ParametersRef(Parameters obj, ReferenceQueue<? super Parameters> queue) {
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
            if (obj instanceof Parameters) {
                return obj.equals(this);
            }
            if (obj instanceof ParametersRef && ((ParametersRef) obj).hashCode == hashCode) {
                Parameters our = get();
                return our != null && our.equals(((ParametersRef)obj).get());
            }
            return false;
        }

        
    }

}
