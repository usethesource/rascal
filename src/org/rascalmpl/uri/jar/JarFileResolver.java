/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
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
package org.rascalmpl.uri.jar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.rascalmpl.uri.FileAttributes;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.zip.CompressedFSTree;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.usethesource.vallang.ISourceLocation;

public class JarFileResolver  {

    protected final Cache<ISourceLocation, CompressedFSTree> fsCache = Caffeine.newBuilder()
            .weigher((ISourceLocation e, CompressedFSTree v) -> (int) (v.getTotalSize() / 1024))
            .maximumWeight((Runtime.getRuntime().maxMemory() / 100) / 1024) // let's never consume more
            // than 1% of the memory
            .expireAfterAccess(10, TimeUnit.MINUTES) // 10 minutes after last access, drop it
            .softValues().build();


    private File jarFile(ISourceLocation jar) {
        return new File(jar.getPath());
    }

    protected CompressedFSTree getFileHierchyCache(ISourceLocation jar) throws IOException {
        try {
            var jarFile = jarFile(jar);
            var lastModified = jarFile.lastModified();
            if (lastModified == 0) {
                throw new FileNotFoundException("The file " + jar + "does not exist or is a directory");
            }
            return fsCache.get(URIUtil.changeQuery(jar, "mod=" + lastModified), j -> new JarFileTree(jarFile));
        }
        catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream(ISourceLocation jar, String path) throws IOException {
        @SuppressWarnings("resource")
        // jarFile's are closed the moment when there are no more references to it.
        JarFile jarFile = new JarFile(jar.getPath());
        JarEntry jarEntry = jarFile.getJarEntry(path);
        if (jarEntry == null) {
            throw new FileNotFoundException(path + " inside " + jar);
        }
        return jarFile.getInputStream(jarEntry);
    }

    public boolean exists(ISourceLocation jar, String path) throws IOException {
        if (path == null || path.isEmpty() || path.equals("/")) {
            return jarFile(jar).isFile();
        }
        return getFileHierchyCache(jar).exists(path);
    }

    public boolean isDirectory(ISourceLocation jar, String path) throws IOException {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return getFileHierchyCache(jar).isDirectory(path);
    }

    public boolean isFile(ISourceLocation jar, String path) throws IOException {
        return getFileHierchyCache(jar).isFile(path);
    }

    public long lastModified(ISourceLocation jar, String path) throws IOException {
        return getFileHierchyCache(jar).lastModified(path);
    }

    public long created(ISourceLocation jar, String path) throws IOException {
        return getFileHierchyCache(jar).created(path);
    }

    public String[] list(ISourceLocation jar, String path) throws IOException {
        if (!path.endsWith("/") && !path.isEmpty()) {
            path = path + "/";
        }
        
        return getFileHierchyCache(jar).directChildren(path);
    }

    public FileAttributes stat(ISourceLocation zip, String path) throws IOException {
        return getFileHierchyCache(zip).stat(path);
    }

    public long size(ISourceLocation jar, String path) throws IOException {
        return getFileHierchyCache(jar).size(path);
    }

}
