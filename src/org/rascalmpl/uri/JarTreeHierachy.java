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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;

public abstract class JarTreeHierachy {

  protected static class FSEntry {
    public long lastModified;

    public FSEntry(long lastModified) {
      this.lastModified = lastModified;
    }
  }

  // perhaps the string could be split into folders and some smart interning
  // but for now, this works.
  protected final NavigableMap<String, FSEntry> fs;
  protected long totalSize;
  protected IOException throwMe;

  public JarTreeHierachy() {
    fs = new TreeMap<String, FSEntry>();
    totalSize = 0;
    throwMe = null;
  }

  public boolean exists(String path) {
    if (throwMe != null) {
      return false;
    }
    // since we only store files, but they are sorted
    // the ceilingKey will return either the first file in the directory
    // or the actual file itself
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
    if (throwMe != null) {
      return false;
    }
    // since we only store files, but they are sorted
    // the ceilingKey will return either the first file in the directory
    // or the actual file itself
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
    if (throwMe != null) {
      return false;
    }
    return fs.containsKey(path);
  }

  public long getLastModified(String path) throws IOException {
    if (throwMe != null) {
      throw throwMe;
    }
    FSEntry result = fs.get(path);
    if (result == null) {
      throw new FileNotFoundException(path);
    }
    return result.lastModified;
  }

  private static final String biggestChar = new String(new int[] {Character.MAX_CODE_POINT}, 0, 1);

  public String[] directChildren(String path) throws IOException {
    if (throwMe != null) {
      throw throwMe;
    }
    assert path.endsWith("/");

    NavigableMap<String, FSEntry> contents = fs.tailMap(path, true);
    String end = fs.higherKey(path + biggestChar); // the last key
    int offset = path.length();
    ArrayList<String> result = new ArrayList<>();
    String previousDir = "+"; // never valid

    for (String subPath : contents.keySet()) {
      if (subPath == end) {
        break;
      }
      int nextSlash = subPath.indexOf('/', offset);
      if (nextSlash != -1) {
        if (!subPath.startsWith(previousDir, offset)) {
          previousDir = subPath.substring(offset, nextSlash);
          result.add(previousDir);
          previousDir = previousDir + "/"; // to make sure the starts with doesn't match same prefix
                                           // dirs
        }
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
