/*******************************************************************************
 * Copyright (c) 2015-2025 CWI
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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ZipInputStreamFileTree extends CompressedFSTree {

    public ZipInputStreamFileTree(InputStream in, long created, long lastModified) {
        super(new IndexedFSEntry(created, lastModified), openStream(in));
    }

    private static EntryEnumerator openStream(InputStream zipStream) {
        return () -> {
            var stream = new ZipInputStream(zipStream);
            return new EntryEnumerator.CloseableIterator() {
                ZipEntry next = null;

                @Override
                public void close() throws IOException {
                    stream.close();
                }


                @Override
                public boolean hasNext() throws IOException {
                    if (next == null) {
                        next = stream.getNextEntry();
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
