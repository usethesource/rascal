/*******************************************************************************
 * Copyright (c) 2015-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.core.uri.jar;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.rascalmpl.core.uri.FileTree;

public class JarFileTree extends FileTree {

  public JarFileTree(File jar) {
    super();
    totalSize = 0;
    try (JarFile jarFile = new JarFile(jar)) {
      for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
        JarEntry je = e.nextElement();
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
