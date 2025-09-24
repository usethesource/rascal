/*
 * Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.locations;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.util.locations.impl.ArrayLineOffsetMap;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;

import io.usethesource.vallang.ISourceLocation;

public class ColumnMaps {
    private final LoadingCache<ISourceLocation, Optional<LineColumnOffsetMap>> currentEntries;
    private final Map<ISourceLocation, Consumer<ISourceLocationChanged>> activeWatches = new ConcurrentHashMap<>();

    public ColumnMaps(Function<ISourceLocation, String> getContents) {
        currentEntries = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .softValues()
            .<ISourceLocation, Optional<LineColumnOffsetMap>>removalListener((loc, ignored, cause) -> {
                if (cause != RemovalCause.REPLACED) {
                    // this does not create a race because removals are only reported 
                    // after a while
                    unwatch(loc);
                }
            })
            .build(l -> {
                String content = getContents.apply(l);
                Optional<LineColumnOffsetMap> result = content == null ? Optional.empty() :  Optional.of(ArrayLineOffsetMap.build(contents));
                watch(l);
                return result;
            });
    }

    private void watch(ISourceLocation l) {
        // in URI Resolver the callback is the "id" of the watch
        // so we have to keep it around to be able to remove the watch later
        Consumer<ISourceLocationChanged> clearEntry = c -> clear(l);
        if (activeWatches.putIfAbsent(l, clearEntry) == null) {
            // we won the race, so lets register the watch now
            try {
                URIResolverRegistry.getInstance().watch(l, false, clearEntry);
            } catch (IOException e) {
                // swallowed since we don't want the column maps to break on unsupported watches
            }
        }
    }

    private void unwatch(ISourceLocation k) {
        var callback = activeWatches.remove(k);
        if (callback != null) {
            try {
                URIResolverRegistry.getInstance().unwatch(k, false, callback);
            } catch (IOException e) {
                // swallowed
            }
        }
    }

    public LineColumnOffsetMap get(ISourceLocation sloc) {
        return currentEntries.get(sloc.top()).orElse(null);
    }

    public void clear(ISourceLocation sloc) {
        currentEntries.invalidate(sloc.top());
    }

}
