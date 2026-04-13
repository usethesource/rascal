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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import org.checkerframework.checker.nullness.qual.Nullable;

public class FSEntry {
    final long created;

    volatile long lastModified;
    volatile long size;


    public FSEntry(long created, long lastModified, long size) {
        this.created = created;
        this.lastModified = lastModified;
        this.size = size;
    }

    public FSEntry(@Nullable FileTime created, long lastModified, long size) {
        this(created == null ? lastModified : created.toMillis(), lastModified, size);
    }

    public long getCreated() {
        return created;
    }
    public long getLastModified() {
        return lastModified;
    }

    public long getSize() {
        return size;
    }

    public static FSEntry forFile(Path file) {
        try {
            var attr = Files.readAttributes(file, BasicFileAttributes.class);
            return new FSEntry(attr.creationTime().toMillis(), attr.lastModifiedTime().toMillis(), attr.size());
        }
        catch (IOException e) {
            return new FSEntry(0, 0, 0);
        }
    }
}
