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
package org.rascalmpl.uri.fs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NotDirectoryException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import org.apache.commons.io.FileExistsException;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Track a set of files (and directories) in memory. 
 * This is used by the in memory filesystem, and also by the jar & zip resolvers
 * 
 */
public class FileSystemTree<T extends FSEntry> {
    private final Directory<T> root;
    protected volatile IOException delayedException;

    public FileSystemTree(T root) {
        this.root = new Directory<>(root, "");
    }

    void throwDelayed() throws IOException {
        if (delayedException != null) {
            throw delayedException;
        }
    }

    public void addFile(String path, T entry, BiFunction<Long, Long, T> inferDirectory) throws IOException { 
        root.add(path, entry, inferDirectory, true);
    }

    public void addDirectory(String path, T entry, BiFunction<Long, Long, T> inferDirectory) throws IOException {
        root.add(path, entry, inferDirectory, false);
    }

    public void replaceFile(String path, UnaryOperator<T> replacer) throws IOException {
        root.replaceFile(path, replacer);
    }

    public void remove(String path) throws IOException {
        try {
            root.remove(path);
        } catch (FileNotFoundException ignored) {
            // file not found exceptions are no problem, the file/dir was apparantly already removed
        }
    }


    public T getEntry(String path) throws IOException {
        throwDelayed();
        var result = root.getEntry(path);
        if (result == null) {
            throw new FileNotFoundException(path + " could not be found");
        }
        if (result.file != null) {
            return result.file;
        }
        return result.directory.self;
    }


    public long lastModified(String path) throws IOException {
        throwDelayed();
        return getEntry(path).lastModified;
    }
    public long created(String path) throws IOException {
        throwDelayed();
        return getEntry(path).created;
    }


    public boolean exists(String path) {
        try {
            return root.getEntry(path) != null;
        }
        catch (IOException e) {
            return false;
        }
    }
    public boolean isFile(String path) {
        try {
            var result = root.getEntry(path);
            return result != null && result.file != null;
        }
        catch (IOException e) {
            return false;
        }
    }
    public boolean isDirectory(String path) {
        try {
            var result = root.getEntry(path);
            return result != null && result.directory != null;
        }
        catch (IOException e) {
            return false;
        }
    }

    public void touch(String path, long newTimestamp) throws IOException {
        getEntry(path).lastModified = newTimestamp;
    }

    public boolean isEmpty() {
        return root.children.isEmpty();
    }

    public String[] directChildren(String path) throws IOException {
        throwDelayed();
        var entry = root.getEntry(path);
        if (entry == null) {
            throw new FileNotFoundException(path);
        }
        if (entry.directory == null) {
            throw new NotDirectoryException(path);
        }
        return entry.directory.children.keySet().toArray(String[]::new);
    }


    @FunctionalInterface
    private interface ThrowableEntryAction<T extends FSEntry, R> {
        R apply(Directory<T> directory, String entry) throws IOException;
    }

    private static class Directory<T extends FSEntry> {
        private final T self;
        private final String prefix;
        private final ConcurrentMap<String, Child<T>> children = new ConcurrentHashMap<>();

        public Directory(T self, String prefix) {
            this.self = self;
            this.prefix = prefix;
        }

        public void add(String path, T entry, BiFunction<Long, Long, T> inferDirectory, boolean isFile) throws IOException { 
            add(parsePath(path), entry, inferDirectory, isFile);
        }

        public void add(Iterator<String> path, T entry, BiFunction<Long, Long, T> inferDirectory, boolean isFile) throws IOException { 
            // we cannot use execute, as we also have to make infered directories on the way
            var currentPart = path.next();
            if (path.hasNext() || !isFile) {
                // it's a directory
                var child = children.computeIfAbsent(currentPart, s -> 
                    new Child<>(new Directory<>(inferDirectory.apply(entry.created, entry.created), prefix + "/" + s)));
                if (child.directory == null) {
                    throw new NotDirectoryException(prefix + "/" + currentPart);
                }
                if (path.hasNext()) {
                    child.directory.add(path, entry, inferDirectory, isFile);
                }
            }
            else {
                var existing = children.putIfAbsent(currentPart, new Child<>(entry));
                if (existing != null) {
                    throw new FileExistsException(prefix + "/" + currentPart);
                }
                self.lastModified = Math.max(self.lastModified, entry.lastModified);
            }
        }

        private <R> R execute(String path, ThrowableEntryAction<T, R> operation) throws IOException {
            return execute(parsePath(path), operation);
        }
        private <R> R execute(Iterator<String> path, ThrowableEntryAction<T, R> operation) throws IOException {
            if (!path.hasNext()) {
                return operation.apply(this, "");
            }
            var childPath = path.next();
            if (path.hasNext()) {
                var result = children.get(childPath);
                if (result == null) {
                    throw new FileNotFoundException(prefix + "/" + childPath);
                }
                // we're looking for a subdirectory
                if (result.directory == null) {
                    throw new NotDirectoryException(prefix + "/" + childPath);
                }
                return result.directory.execute(path, operation);
            }
            // we're at the right place
            return operation.apply(this, childPath);

        }

        public @Nullable Child<T> getEntry(String path) throws IOException {
            return execute(path, (d, e) -> e.isEmpty() ? new Child<>(d) : d.children.get(e));
        }
        

        public void replaceFile(String path, UnaryOperator<T> replacer) throws IOException {
            execute(path, (d, e) ->  {
                var result = d.children.computeIfPresent(e, (s, c) -> {
                    if (c.file == null) {
                        throw new IllegalArgumentException(s + " is not a file");
                    }
                    return new Child<>(replacer.apply(c.file));
                });
                if (result == null) {
                    throw new FileNotFoundException(path);
                }
                return null;
            });
        }

        public void remove(String path) throws IOException {
            execute(path, (d, e) -> {
                var removed = d.children.computeIfPresent(e, (n, old) -> {
                    if (old.directory != null && !old.directory.children.isEmpty()) {
                        // we cannot delete an non-empty directory
                        return old;
                    }
                    // otherwise delete it
                    return null;
                });
                if (removed != null) {
                    throw new DirectoryNotEmptyException(path);
                }
                d.self.lastModified = Math.max(d.self.lastModified, System.currentTimeMillis());
                return null;
            });
        }

        private static Iterator<String> parsePath(String path) {
            return new Iterator<String>() {
                String nextString = null;
                int prevIndex = nextNonSlash(path, 0);
                @Override
                public boolean hasNext() {
                    if (nextString == null && prevIndex != -1 && prevIndex < path.length() ) {
                        int nextIndex = path.indexOf('/', prevIndex);
                        if (nextIndex == -1) {
                            nextString =  path.substring(prevIndex);
                            prevIndex = -1;
                        }
                        else {
                            nextString = path.substring(prevIndex, nextIndex);
                            prevIndex = nextNonSlash(path, nextIndex + 1);
                        }
                    }
                    return nextString != null;
                }

                @Override
                public String next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    var result = nextString;
                    nextString = null;
                    return result;
                }
            };
        }

        private static int nextNonSlash(String path, int offset) {
            int result = offset;
            while (result < path.length() && path.charAt(result) == '/') {
                result++;
            }
            return result;
        }
        
    }

    private static class Child<T extends FSEntry> {
        private final @Nullable T file;
        private final @Nullable Directory<T> directory;

        private Child(@Nullable T file, @Nullable Directory<T> directory) {
            this.file = file;
            this.directory = directory;
        }

        public Child(T file) {
            this(file, null);
        }
        public Child(Directory<T> dir) {
            this(null, dir);
        }

    }



}


