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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.util.locations.impl.ArrayLineOffsetMap;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import io.usethesource.vallang.ISourceLocation;

public class ColumnMaps {
    private final LoadingCache<ISourceLocation, LineColumnOffsetMap> currentEntries;
    private final Map<ISourceLocation, Consumer<ISourceLocationChanged>> activeWatches = new ConcurrentHashMap<>();

    public ColumnMaps(Function<ISourceLocation, String> getContents, boolean clearOnFileModification) {
        currentEntries = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .softValues()
            .<ISourceLocation, LineColumnOffsetMap>removalListener((k, ignored1, ignored2) -> {
                if (clearOnFileModification && k != null) {
                    cleanupWatch(k);
                }
            })
            .build(l -> {
                var result = ArrayLineOffsetMap.build(getContents.apply(l));
                if (clearOnFileModification) {
                    registerWatch(l);
                }
                return result;
            });
    }

    private void registerWatch(ISourceLocation l) throws IOException {
        // in URI Resolver the callback is the "id" of the watch
        // so we have to keep it around to be able to cleanup the watch later
        Consumer<ISourceLocationChanged> clearEntry = c -> clear(l);
        if (activeWatches.putIfAbsent(l, clearEntry) == null) {
            URIResolverRegistry.getInstance().watch(l, false, clearEntry);
        }
    }

    private void cleanupWatch(ISourceLocation k) {
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
        return currentEntries.get(sloc.top());
    }

    public void clear(ISourceLocation sloc) {
        currentEntries.invalidate(sloc.top());
    }

}
