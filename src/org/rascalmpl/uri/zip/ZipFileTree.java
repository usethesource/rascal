/*******************************************************************************
 * Copyright (c) 2015-2021 CWI All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: * Davy Landman - Davy.Landman@cwi.nl - CWI * Jurgen Vinju - Jurgen.Vinju@cwi.nl -
 * CWI
 *******************************************************************************/
package org.rascalmpl.uri.zip;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.rascalmpl.uri.FileTree;

public class ZipFileTree extends FileTree {
  public ZipFileTree(File jar) {
    super();
    totalSize = 0;
    try (ZipFile jarFile = new ZipFile(jar)) {
      for (Enumeration<? extends ZipEntry> e = jarFile.entries(); e.hasMoreElements();) {
        ZipEntry je = e.nextElement();
        if (je.isDirectory()) {
          continue;
        }
        String name = je.getName();
        totalSize += 8 + (name.length() * 2);
        fs.put(name, new FSEntry(je.getTime()));
      }
    }
    catch (IOException e1) {
      throwMe = e1;
    }
  }
}
