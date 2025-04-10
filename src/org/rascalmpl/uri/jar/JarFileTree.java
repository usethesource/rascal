/*******************************************************************************
 * Copyright (c) 2015-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Davy Landman - Davy.Landman@cwi.nl - CWI
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.uri.jar;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.rascalmpl.uri.zip.CompressedFSTree;
import org.rascalmpl.uri.zip.EntryEnumerator;
import org.rascalmpl.uri.zip.IndexedFSEntry;

public class JarFileTree extends CompressedFSTree {

  public JarFileTree(File jar) {
        super(IndexedFSEntry.forFile(jar), openJar(jar));
    }

    private static EntryEnumerator openJar(File jar) {
        return () -> {
            var jarFile = new JarFile(jar);
            var actual = jarFile.entries();
            return new EntryEnumerator.CloseableIterator() {
                @Override
                public void close() throws IOException {
                    jarFile.close();
                }

                @Override
                public boolean hasNext() {
                    return actual.hasMoreElements();
                }

                @Override
                public ZipEntry next() {
                    return actual.nextElement();
                }
            };
        };
    }

}
