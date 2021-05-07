/*******************************************************************************
 * Copyright (c) 2009-2021 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.uri.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.rascalmpl.uri.FileTree;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class ZipFileResolver  {

    protected final Cache<ISourceLocation, FileTree> fsCache = Caffeine.newBuilder()
            .weigher((ISourceLocation e, FileTree v) -> (int) (v.totalSize() / 1024))
            .maximumWeight((Runtime.getRuntime().maxMemory() / 100) / 1024) // let's never consume more
            // than 1% of the memory
            .expireAfterAccess(10, TimeUnit.MINUTES) // 10 minutes after last access, drop it
            .softValues().build();

    protected FileTree getFileHierchyCache(ISourceLocation zip) {
        try {
            final File zipFile = new File(zip.getPath());
            return fsCache.get(URIUtil.changeQuery(zip, "mod=" + zipFile.lastModified()), j -> new ZipFileTree(zipFile));
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream(ISourceLocation zip, String path) throws IOException {
        @SuppressWarnings("resource")
        // zipFile's are closed the moment when there are no more references to it.
        ZipFile zipFile = new ZipFile(zip.getPath());
        ZipEntry zipEntry = zipFile.getEntry(path);
        if (zipEntry == null) {
            throw new FileNotFoundException(path + " inside " + zip);
        }
        return zipFile.getInputStream(zipEntry);
    }

    public boolean exists(ISourceLocation zip, String path) {
        if (path == null || path.isEmpty() || path.equals("/")) {
            return true;
        }
        return getFileHierchyCache(zip).exists(path);
    }

    public boolean isDirectory(ISourceLocation zip, String path) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return getFileHierchyCache(zip).isDirectory(path);
    }

    public boolean isFile(ISourceLocation zip, String path) {
        return getFileHierchyCache(zip).isFile(path);
    }

    public long lastModified(ISourceLocation zip, String path) throws IOException {
        return getFileHierchyCache(zip).getLastModified(path);
    }

    public String[] list(ISourceLocation zip, String path) throws IOException {
        if (!path.endsWith("/") && !path.isEmpty()) {
            path = path + "/";
        }
        try {
            return getFileHierchyCache(zip).directChildren(path);
        }
        catch (IOException e) {
            return new String[0];
        }
    }
}
