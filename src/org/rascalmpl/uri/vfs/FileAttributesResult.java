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
package org.rascalmpl.uri.vfs;

import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.uri.FileAttributes;

public class FileAttributesResult {
    private boolean exists;
    private FileType type;
    private long ctime;
    private long mtime;
    private long size;
    private @Nullable FilePermission permissions;

    public FileAttributesResult(boolean exists, int type, long ctime, long mtime, long size, int permissions) {
        this.exists = exists;
        this.type = FileType.fromValue(type);
        this.ctime = ctime;
        this.mtime = mtime;
        this.size = size;
        this.permissions = FilePermission.fromValue(permissions);
    }

    public FileAttributesResult(boolean exists, FileType type, long ctime, long mtime, long size, @Nullable FilePermission permissions) {
        this.exists = exists;
        this.type = type;
        this.ctime = ctime;
        this.mtime = mtime;
        this.size = size;
        this.permissions = permissions;
    }

    public FileAttributes getFileAttributes() {
        return new FileAttributes(exists, type == FileType.File, ctime, mtime, true, permissions != null, size);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof FileAttributesResult) {
            var other = (FileAttributesResult)obj;
            return exists == other.exists
                && type == other.type
                && ctime == other.ctime
                && mtime == other.mtime
                && size == other.size
                && permissions == other.permissions;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exists, type, ctime, mtime, size, permissions);
    }

    @Override
    public String toString() {
        return "FileStatResult [exists="+ exists + " type=" + type + " ctime=" + ctime + " mtime=" + mtime + " size=" + size + " permissions=" + permissions + "]";
    }

    public enum FileType {
        Unknown(0), File(1), Directory(2), SymbolicLink(64);

        private final int value;

        private FileType(int val) {
            assert val == 0 || val == 1 || val == 2 || val == 64;
            this.value = val;
        }

        public int getValue() {
            return value;
        }

        public static FileType fromValue(int val) {
            switch (val) {
                case 0: return Unknown;
                case 1: return File;
                case 2: return Directory;
                case 64: return SymbolicLink;
                default: throw new IllegalArgumentException("Unknown FileType value " + val);
            }
        }
    }

    public enum FilePermission {
        Readonly(1);
        private final int value;
        private FilePermission(int val) {
            assert val == 1;
            this.value = val;
        }

        public int getValue() {
            return value;
        }

        public static @Nullable FilePermission fromValue(int val) {
            switch (val) {
                case 1: return Readonly;
                default: return null;
            }
        }
    }
}
