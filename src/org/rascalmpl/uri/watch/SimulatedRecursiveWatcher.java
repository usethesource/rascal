package org.rascalmpl.uri.watch;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import org.rascalmpl.uri.ISourceLocationWatcher;
import org.rascalmpl.uri.ISourceLocationWatcher.ISourceLocationChanged;
import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.ISourceLocation;

/**
 * Some ISourceLocationWatchers do not support recursive watches, this class approximates that feature on top of the native one
 */
public class SimulatedRecursiveWatcher implements Closeable {

    private volatile boolean closed = false;
    private final ISourceLocationWatcher nativeWatcher;
    private final List<Consumer<ISourceLocationChanged>> subscriptions = new CopyOnWriteArrayList<>();
    private final Set<ISourceLocation> activeWatches = ConcurrentHashMap.newKeySet();
    private final Executor exec;

    public SimulatedRecursiveWatcher(ISourceLocation rootLocation, Consumer<ISourceLocationChanged> initialConsumer,
        ISourceLocationWatcher nativeWatcher, Executor exec) {
        this.nativeWatcher = nativeWatcher;
        this.exec = exec;
        subscriptions.add(initialConsumer);
        try {
            nativeWatcher.watch(rootLocation, this::handler, false);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        registerChildWatches(rootLocation);
    }

    private void handler(ISourceLocationChanged event) {
        if (closed) {
            return;
        }
        for (var s : subscriptions) {
            exec.execute(() -> s.accept(event));
        }

        var loc = event.getLocation();
        if (event.isCreated() && event.isDirectory()) {
            registerChildWatches(loc);
        }
        else if (event.isDeleted() && activeWatches.contains(loc)) {
            try {
                activeWatches.remove(loc);
                nativeWatcher.unwatch(loc, this::handler, false);
            }
            catch (Exception ignored) { }
        }
    }

    private void registerChildWatches(ISourceLocation loc) {
        if (closed) {
            return;
        }
        exec.execute(() -> {
            var reg = URIResolverRegistry.getInstance();
            Deque<ISourceLocation> todo = new ArrayDeque<>();
            todo.push(loc);
            while (!todo.isEmpty()) {
                var current = todo.pop();
                if (!activeWatches.contains(current)) {
                    try {
                        nativeWatcher.watch(current, this::handler, false);
                        activeWatches.add(current);
                        for (var e: reg.list(current)) {
                            if (reg.isDirectory(e)) {
                                todo.push(e);
                            }
                        }
                    }
                    catch (IOException e) {
                    }
                }
            }
        });
    }

    public void add(Consumer<ISourceLocationChanged> c) {
        subscriptions.add(c);
    }

    public void remove(Consumer<ISourceLocationChanged> c) {
        subscriptions.remove(c);
    }

    public boolean isEmpty() {
        return subscriptions.isEmpty();
    }

    public SimulatedRecursiveWatcher merge(SimulatedRecursiveWatcher b) {
        subscriptions.addAll(b.subscriptions);
        try {
            b.close();
        } catch (Exception ignore) {}
        return this;
    }

    @Override
    public void close() {
        closed = true;
        for (var a: activeWatches) {
            try {
                nativeWatcher.unwatch(a, this::handler, false);
            }
            catch (IOException e) {
            }
        }
        activeWatches.clear();
    }

}
