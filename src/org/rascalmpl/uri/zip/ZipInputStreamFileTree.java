/*******************************************************************************
 * Copyright (c) 2015-2021 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.uri.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.rascalmpl.uri.FileTree;

public class ZipInputStreamFileTree extends FileTree {

  private static class IndexFSEntry extends FSEntry {
    public int position;

    public IndexFSEntry(long lastModified, int position) {
      super(lastModified);
      this.position = position;
    }

  }

  public ZipInputStreamFileTree(InputStream in) {
    super();
    totalSize = 0;

    try (ZipInputStream stream = new ZipInputStream(in)) {
      ZipEntry next = null;
      int pos = 0;

      while ((next = stream.getNextEntry()) != null) {
        if (!next.isDirectory()) {
          String name = next.getName();
          totalSize += 16 + (name.length() * 2);
          fs.put(name, new IndexFSEntry(next.getTime(), pos++));
        }
        else {
          pos++; // we don't store directories
        }
      }
    }
    catch (IOException e) {
      throwMe = e;
      fs.clear();
    }
  }

  public int getPosition(String path) {
    IndexFSEntry ent = (IndexFSEntry) fs.get(path);
    if (ent == null) {
      return -1;
    }
    return ent.position;
  }

}
