/*
 * Copyright (c) 2025, Swat.engineering
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
package org.rascalmpl.uri.watch;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

import engineering.swat.watch.DaemonThreadPool;
import io.usethesource.vallang.ISourceLocation;

/**
 * This is a companion class to the {@link URIResolverRegistry} that contains all the logic around watches and simulated watches
 * to reduce the class size of the uri registry
 */
public class WatchRegistry {
    /** schemes with native support */
    private final Map<String, ISourceLocationWatcher> watchers = new ConcurrentHashMap<>();
    /** simulate recursive watches active on schemes with native support */
    private final Map<ISourceLocation, SimulatedRecursiveWatcher> simulatedRecursiveWatchers = new ConcurrentHashMap<>();

    /** simulated locations that are watched */
    private final NavigableMap<ISourceLocation, Set<SimulatedWatchEntry>> internalWatchers = new ConcurrentSkipListMap<>(makeISourceLocationComparator());

    /** keep track of original wrappers vs translating wrappers to provide the correct instance to unwatch */
    private final Map<WatchKey, KeyedWeakReference> wrappedHandlers = new ConcurrentHashMap<>();

    private final URIResolverRegistry reg;
    public final ReferenceQueue<? super Consumer<ISourceLocationChanged>> clearedReferences = new ReferenceQueue<>();
    private final UnaryOperator<ISourceLocation> resolver;
    private volatile @Nullable ISourceLocationWatcher fallback;

    public WatchRegistry(URIResolverRegistry reg, UnaryOperator<ISourceLocation> resolver) {
        this.reg = reg;
        this.resolver = resolver;
        cleanup();
    }


    public void registerNative(String scheme, ISourceLocationWatcher watcher) {
        watchers.put(scheme, watcher);
    }
    public void setFallback(ISourceLocationWatcher fallback) {
        this.fallback = fallback;
    }

    private ISourceLocation safeResolve(ISourceLocation loc) {
        return resolver.apply(loc);
    }
    
    private static Comparator<ISourceLocation> makeISourceLocationComparator() {
        return Comparator
            .comparing(ISourceLocation::getScheme)
            .thenComparing(ISourceLocation::getAuthority)
            .thenComparing(ISourceLocation::getPath)
            .thenComparing(ISourceLocation::getQuery);
    }

    public void watch(ISourceLocation loc, boolean recursive, final Consumer<ISourceLocationChanged> callback)
        throws IOException {
        var resolvedLoc = safeResolve(loc);
        var watcher = watchers.getOrDefault(resolvedLoc.getScheme(), fallback);
        if (watcher != null) {
            startNormalWatch(loc, recursive, callback, resolvedLoc, watcher);
        }
        else {
            startSimulatedWatch(loc, recursive, callback, resolvedLoc);
        }
    }

    private void startSimulatedWatch(ISourceLocation loc, boolean recursive, Consumer<ISourceLocationChanged> callback,
        ISourceLocation resolvedLoc) {
        internalWatchers
            .computeIfAbsent(resolvedLoc, k -> ConcurrentHashMap.newKeySet())
            .add(new SimulatedWatchEntry(recursive, translateWatchEvents(callback, loc, resolvedLoc, recursive)));
    }

    private ISourceLocation startNormalWatch(ISourceLocation loc, boolean recursive,
        final Consumer<ISourceLocationChanged> callback, ISourceLocation resolvedLoc, ISourceLocationWatcher watcher)
        throws IOException {
        // a watcher should be able to deal with single file watches
        // but not a recursive one
        if (recursive && !reg.isDirectory(resolvedLoc)) {
            loc = URIUtil.getParentLocation(loc);
            resolvedLoc = safeResolve(loc);
        }
        var translator = translateWatchEvents(callback, loc, resolvedLoc, recursive);
        if ((recursive && watcher.supportsRecursiveWatch()) || !recursive) {
            watcher.watch(resolvedLoc, translator, recursive);
        }
        else {
            // we have to simulate a recursive watch based on an non recursive watch
            // this is always a limit, as we have to deal with removes & additions
            // but we do our best to simulate the recursive behavior
            simulatedRecursiveWatchers
                .computeIfAbsent(resolvedLoc, rl -> new SimulatedRecursiveWatcher(rl, translator, watcher, exec))
                .add(translator);
        }
        return loc;
    }

    private Consumer<ISourceLocationChanged> translateWatchEvents(Consumer<ISourceLocationChanged> original, ISourceLocation originalLoc, ISourceLocation resolvedLoc, boolean recursive) {
        if (resolvedLoc.equals(originalLoc)) {
            return original;
        }
        // for the unwatch we have to keep track of all the handlers we've passed along
        // and we don't want to overwrite existing handlers for the same pair of arguments
        // but we also don't want duplicate entries, or cleared entries

        Consumer<ISourceLocationChanged> newHandler = changes -> {
            // we resolved logical resolvers in order to use native watchers as much as possible
            // for efficiency sake, but this breaks the logical URI abstraction. We have to undo
            // this renaming before we trigger the callback.
            ISourceLocation relative = URIUtil.relativize(resolvedLoc, changes.getLocation());
            ISourceLocation unresolved = URIUtil.getChildLocation(originalLoc, relative.getPath());
            original.accept(ISourceLocationWatcher.makeChange(unresolved, changes.getChangeType()));
        };
        var key = new WatchKey(originalLoc, recursive, original, clearedReferences);
        while (true) {
            var stored = wrappedHandlers.compute(key, (k, v) -> {
                if (v == null || v.get() == null) {
                    return new KeyedWeakReference(k, newHandler, clearedReferences);
                }
                return v;
            });
            var actualResult = stored.get();
            if (actualResult != null) {
                // we didn't loose the race to the GC
                // so we have an new handler that is bound to this key
                return actualResult;
            }
        }
    }

    public void unwatch(ISourceLocation loc, boolean recursive, Consumer<ISourceLocationChanged> callback)
        throws IOException {
        var actualCallback = wrappedHandlers.remove(new WatchKey(loc, recursive, callback, clearedReferences));
        if (actualCallback != null) {
            var notClearedYet = actualCallback.get();
            if (notClearedYet != null) {
                callback = notClearedYet;
            }
        }

        loc = safeResolve(loc);

        var watcher = watchers.getOrDefault(loc.getScheme(), fallback);
        if (watcher != null) {
            var simulated = simulatedRecursiveWatchers.get(loc);
            if (simulated != null) {
                simulated.remove(callback);
            }
            else {
                watcher.unwatch(loc, callback, recursive);
            }
        }
        else {
            var entries = internalWatchers.get(loc);
            if (entries != null) {
                final var finalCallback = callback;
                entries.removeIf(p -> p.isRecursive() == recursive && p.getHandler() == finalCallback);
            }
        }
    }

    /** a private daemon thread thread-pool */
    private final ExecutorService exec = DaemonThreadPool.buildConstrainedCached("simulated-watches", Math.max(2, Math.min(6, Runtime.getRuntime().availableProcessors() - 2)));

    public void notifySimulatedWatchers(ISourceLocation loc, ISourceLocationChanged event) {
        if (watchers.containsKey(loc.getScheme())) {
            // the registered watcher will do the callback itself
            return;
        }
        // there might be multiple watches active that would hit this location, so we have to go down the tree to find them
        // and stop as soon as we've moved past them
        var possibleMatches = internalWatchers.headMap(loc, true)
            .descendingMap()
            .entrySet()
            .iterator();
        while (possibleMatches.hasNext()) {
            var watchedEntry = possibleMatches.next();
            var watchedPath = watchedEntry.getKey();
            var exactMatch = watchedPath.equals(loc);
            var childMatch = URIUtil.isParentOf(watchedPath, loc);
            if (exactMatch || childMatch) {
                var onlyRecursive = !exactMatch && !isDirectParentOf(watchedPath, loc);
                for (var c : watchedEntry.getValue()) {
                    if (!onlyRecursive || c.isRecursive()) {
                        // we schedule the call in the background
                        exec.submit(() -> c.getHandler().accept(event));
                    }
                }
            }
            // now we might continue for other higher up entries
            // but only as long as we share some path prefix
            var watchedParent = URIUtil.getParentLocation(watchedPath);
            if (!watchedParent.equals(loc) && !URIUtil.isParentOf(watchedParent, loc)) {
                break;
            }
            
        }
    }

    private boolean isDirectParentOf(ISourceLocation parent, ISourceLocation child) {
        return URIUtil.relativize(parent, URIUtil.getParentLocation(child)).getPath().equals("/");
    }


    private void cleanup() {
        try {
            cleanupInternalWatchers();
            cleanupSimulatedWatchers();
            cleanupWrappedHandlers();
        }
        finally {
            CompletableFuture.delayedExecutor(1, TimeUnit.MINUTES, exec)
                .execute(this::cleanup);
        }
    }


    private void cleanupSimulatedWatchers() {
        for (var e: simulatedRecursiveWatchers.entrySet()) {
            if (e.getValue().isEmpty()) {
                var k = e.getKey();
                var v = e.getValue(); 
                if (simulatedRecursiveWatchers.remove(k, v) && !v.isEmpty()) {
                    // lost the race between cleanup and a new watch
                    simulatedRecursiveWatchers.merge(k, v, (a, b) -> a.merge(b));
                }
                else {
                    v.close();
                }
            }
        }
    }


    private void cleanupInternalWatchers() {
        for (var e : internalWatchers.entrySet()) {
            if (e.getValue().isEmpty()) {
                // take a copy in case we loose a race
                var k = e.getKey();
                var v = e.getValue(); 
                if (internalWatchers.remove(k, v) && !v.isEmpty()) {
                    // lost the race between cleanup and a new watch
                    internalWatchers.merge(k, v, (a,b) -> { a.addAll(b); return a; });
                }
            }
        }
    }

    private void cleanupWrappedHandlers() {
        var toClear = new ArrayList<WatchKey>();
        synchronized(clearedReferences) {
            Reference<?> entry;
            while ((entry = clearedReferences.poll()) != null) {
                if (entry instanceof WatchKey) {
                    toClear.add((WatchKey)entry);
                }
                else {
                    toClear.add(((KeyedWeakReference)entry).key);
                }
            }
        }
        toClear.forEach(wrappedHandlers::remove);
    }

    public boolean hasNativeSupport(String scheme) {
        return watchers.containsKey(scheme);
    }

    private static class SimulatedWatchEntry {
        private final boolean recursive;
        private final Consumer<ISourceLocationChanged> handler;

        public SimulatedWatchEntry(boolean recursive, Consumer<ISourceLocationChanged> handler) {
            this.recursive = recursive;
            this.handler = handler;
        }

        public boolean isRecursive() {
            return recursive;
        }

        public Consumer<ISourceLocationChanged> getHandler() {
            return handler;
        }
    }

    private static class KeyedWeakReference extends WeakReference<Consumer<ISourceLocationChanged>> {
        private final WatchKey key;

        public KeyedWeakReference(WatchKey key, Consumer<ISourceLocationChanged> handler, ReferenceQueue<? super Consumer<ISourceLocationChanged>> queue) {
            super(handler, queue);
            this.key = key;
        }
        
    }

    private static class WatchKey extends WeakReference<Consumer<ISourceLocationChanged>> {
        private final ISourceLocation loc;
        private final boolean recursive;
        private final int hash;

        public WatchKey(ISourceLocation loc, boolean recursive, Consumer<ISourceLocationChanged> handler, ReferenceQueue<? super Consumer<ISourceLocationChanged>> queue) {
            super(handler, queue);
            this.hash = Objects.hash(loc, recursive, handler);
            this.loc = loc;
            this.recursive = recursive;
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof WatchKey)) {
                return false;
            }
            WatchKey other = (WatchKey) obj;
            return hash == other.hash
                && recursive == other.recursive
                && Objects.equals(loc, other.loc) 
                && get() != null
                && Objects.equals(get(), other.get());
        }

    

    }

}
