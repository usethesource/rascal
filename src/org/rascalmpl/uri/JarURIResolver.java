/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
package org.rascalmpl.uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class JarURIResolver implements ISourceLocationInput {

  private static final Cache<String, JarTreeHierachy> fsCache = Caffeine.newBuilder()
      .weigher((String e, JarTreeHierachy v) -> (int) (v.totalSize() / 1024))
      .maximumWeight((Runtime.getRuntime().maxMemory() / 100) / 1024) // let's never consume more
                                                                      // than 1% of the memory
      .expireAfterAccess(10, TimeUnit.MINUTES) // 10 minutes after last access, drop it
      .softValues().build();

  public JarURIResolver() {
    super();
  }

  protected File getJar(ISourceLocation uri) throws IOException {
    String path = uri.getPath();
    if (path == null) {
      path = uri.toString();
    }
    int bang = path.indexOf('!');
    if (bang != -1) {
      return new File(path.substring(path.indexOf("/"), bang));
    }
    else {
      throw new IOException("The jar and the internal path should be separated with a !");
    }
  }

  protected String getPath(ISourceLocation uri) {
    String path = uri.getPath();
    if (path == null) {
      path = uri.toString();
    }
    int bang = path.indexOf('!');

    if (bang != -1) {
      path = path.substring(bang + 1);
      while (path.startsWith("/")) {
        path = path.substring(1);
      }
      return path;
    }
    else {
      return "";
    }
  }

  public InputStream getInputStream(ISourceLocation uri) throws IOException {
    File jar = getJar(uri);
    String path = getPath(uri);

    @SuppressWarnings("resource")
    // jarFile's are closed the moment when there are no more references to it.
    JarFile jarFile = new JarFile(jar);
    JarEntry jarEntry = jarFile.getJarEntry(path);
    return jarFile.getInputStream(jarEntry);
  }

  public boolean exists(ISourceLocation uri) {
    try {
      File jar = getJar(uri);
      String path = getPath(uri);

      if (path == null || path.isEmpty() || path.equals("/")) {
        return true;
      }
      return getFileHierchyCache(jar).exists(path);
    }
    catch (IOException e) {
      return false;
    }
  }

  protected JarTreeHierachy getFileHierchyCache(final File jar) {
    return fsCache.get(jar.getAbsolutePath() + jar.lastModified(), j -> new JarFileTreeHierachy(jar));
  }

  public boolean isDirectory(ISourceLocation uri) {
    try {
      if (uri.getPath() != null && (uri.getPath().endsWith(".jar!") || uri.getPath().endsWith(".jar!/"))) {
        // if the uri is the root of a jar, and it ends with a ![/], it should be considered a
        // directory
        return true;
      }
      File jar = getJar(uri);
      String path = getPath(uri);

      if (!path.endsWith("/")) {
        path = path + "/";
      }

      return getFileHierchyCache(jar).isDirectory(path);
    }
    catch (IOException e) {
      return false;
    }
  }

  public boolean isFile(ISourceLocation uri) {
    try {
      File jar = getJar(uri);
      String path = getPath(uri);
      return getFileHierchyCache(jar).isFile(path);
    }
    catch (IOException e) {
      return false;
    }
  }

  public long lastModified(ISourceLocation uri) throws IOException {
    File jar = getJar(uri);
    String path = getPath(uri);
    return getFileHierchyCache(jar).getLastModified(path);
  }

  @Override
  public String[] list(ISourceLocation uri) throws IOException {
    File jar = getJar(uri);
    String path = getPath(uri);

    if (!path.endsWith("/") && !path.isEmpty()) {
      path = path + "/";
    }
    try {
      return getFileHierchyCache(jar).directChildren(path);
    }
    catch (IOException e) {
      return new String[0];
    }
  }

  public String scheme() {
    return "jar";
  }

  public boolean supportsHost() {
    return false;
  }

  @Override
  public Charset getCharset(ISourceLocation uri) throws IOException {
    // TODO need to see if we can detect the charset inside a jar
    return null;
  }

  @Override
  public boolean supportsToFileURI() {
	  return false;
  }

  @Override
  public URI toFileURI(ISourceLocation uri) {
	  throw new UnsupportedOperationException("Cannot convert jar to File URI");
  }
}
