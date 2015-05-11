/*******************************************************************************
 * Copyright (c) 2015-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileHierachy {

  private static class FSEntry {
    public long lastModified;
    public FSEntry(long lastModified) {
      this.lastModified = lastModified;
    }
  }

  // perhaps the string could be split into folders and some smart interning
  // but for now, this works.
  private final TreeMap<String, FSEntry> fs;
  private final long timeStamp;
  private final long totalSize;

  public JarFileHierachy(File jar) {
    this.fs = new TreeMap<String, FSEntry>();
    this.timeStamp = jar.lastModified();
    long totalSize = 0;
    try(JarFile jarFile = new JarFile(jar)) {
      for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
        JarEntry je = e.nextElement();
        if (je.isDirectory()) {
          continue;
        }
        String name = je.getName();
        totalSize += 8 + (name.length() * 2);
        fs.put(name, new FSEntry(je.getTime()));
      }
    } catch (IOException e) {
    }
    this.totalSize = totalSize;
  }

  public boolean exists(String path) {
    String result = fs.ceilingKey(path);
    if (result == null) {
      return false;
    }
    if (result.equals(path)) {
      return true;
    }
    // it might be a directory
    if (!path.endsWith("/")) {
      path += "/";
    }
    return result.startsWith(path);
  }
  public boolean isDirectory(String path) {
    String result = fs.ceilingKey(path);
    if (result == null) {
      return false;
    }
    if (result.equals(path)) {
      // it's a file
      return false;
    }
    if (!path.endsWith("/")) {
      path += "/";
    }
    return result.startsWith(path);
  }

  public boolean isFile(String path) {
    return fs.containsKey(path);
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public long getLastModified(String path) throws FileNotFoundException {
    FSEntry result = fs.get(path);
    if (result == null) {
      throw new FileNotFoundException(path);
    }
    return result.lastModified;
  }

  public String[] directChildren(String path) {
    NavigableMap<String, FSEntry> contents = fs.tailMap(path, true);
    int offset = path.length();
    Set<String> result = new HashSet<>();
    for (String subPath : contents.keySet()) {
      if (!subPath.startsWith(path)) {
        break;
      }
      int nextSlash = subPath.indexOf('/', offset);
      if (nextSlash != -1) {
        result.add(subPath.substring(offset, nextSlash));
      }
      else {
        result.add(subPath.substring(offset));
      }
    }
    return result.toArray(new String[0]);
  }

  public long totalSize() {
    return totalSize;
  }

}
