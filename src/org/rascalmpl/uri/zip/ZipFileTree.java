/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *     * Davy Landman - Davy.Landman@cwi.nl - CWI
 *     * Jurgen Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.uri.zip;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileTree extends CompressedFSTree {
    public ZipFileTree(File jar) {
        super(IndexedFSEntry.forFile(jar), openZip(jar));
    }

    private static EntryEnumerator openZip(File zipFile) {
        return () -> {
            var zip = new ZipFile(zipFile);
            var actual = zip.entries();
            return new EntryEnumerator.CloseableIterator() {
                @Override
                public void close() throws IOException {
                    zip.close();
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
