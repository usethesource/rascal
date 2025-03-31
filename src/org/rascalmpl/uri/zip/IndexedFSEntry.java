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
package org.rascalmpl.uri.zip;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.zip.ZipEntry;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.uri.fs.FSEntry;

public class IndexedFSEntry extends FSEntry {
    final int offset;

    public IndexedFSEntry(long created, long lastModified, int offset) {
        super(created, lastModified);
        this.offset = offset;
    }

    public IndexedFSEntry(@Nullable FileTime created, long lastModified, int offset) {
        this(created == null ? lastModified : created.toMillis(), lastModified, offset);
    }

    public IndexedFSEntry(long created, long lastModified) {
        this(created, lastModified, -1);
    }

    public IndexedFSEntry(FSEntry e) {
        this(e.getCreated(), e.getLastModified());
    }


    public IndexedFSEntry(ZipEntry ze, int offset) {
        this(ze.getCreationTime(), ze.getTime(), offset);
    }

    public static IndexedFSEntry forFile(File file) {
        return forFile(file.toPath());
    }

    public static IndexedFSEntry forFile(Path file) {
        return new IndexedFSEntry(FSEntry.forFile(file));
    }
    
}
