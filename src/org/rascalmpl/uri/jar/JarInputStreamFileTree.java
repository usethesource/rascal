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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import org.rascalmpl.uri.zip.CompressedFSTree;
import org.rascalmpl.uri.zip.EntryEnumerator;
import org.rascalmpl.uri.zip.IndexedFSEntry;

public class JarInputStreamFileTree extends CompressedFSTree {

  public JarInputStreamFileTree(InputStream in, long created, long lastModified) {
        super(new IndexedFSEntry(created, lastModified), openStream(in));
    }

    private static EntryEnumerator openStream(InputStream jarStream) {
        return () -> {
            var stream = new JarInputStream(jarStream);
            return new EntryEnumerator.CloseableIterator() {
                JarEntry next = null;

                @Override
                public void close() throws IOException {
                    stream.close();
                }


                @Override
                public boolean hasNext() throws IOException {
                    if (next == null) {
                        next = stream.getNextJarEntry();
                    }
                    return next != null;
                }



                @Override
                public ZipEntry next() throws IOException {
                    if (!hasNext()) {
                        throw new EOFException("No more entries in the stream");
                    }
                    var result = next;
                    next = null;
                    return result;
                }
                
            };
        };
    }
}
