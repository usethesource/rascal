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
package org.rascalmpl.uri.zip;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.rascalmpl.uri.FileTree;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

public class ZipInputStreamResolver extends ZipFileResolver {
	private final URIResolverRegistry CTX;
	
	public ZipInputStreamResolver(URIResolverRegistry registry) {
	    this.CTX = registry;
    }
	
	@Override
    protected FileTree getFileHierchyCache(ISourceLocation zipLocation) {
        try {
            final ISourceLocation lookupKey = URIUtil.changeQuery(zipLocation, "mod=" + CTX.lastModified(zipLocation));
            return fsCache.get(lookupKey, j -> {
                try {
                    return new ZipInputStreamFileTree(CTX.getInputStream(zipLocation));
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ZipEntry skipToEntry(ZipInputStream stream, ISourceLocation jarLocation, String subPath) throws IOException {
        int pos = ((ZipInputStreamFileTree)getFileHierchyCache(jarLocation)).getPosition(subPath);

        if (pos != -1) {
            ZipEntry entry;
            do {
                entry = stream.getNextEntry();
            }
            while (pos-- > 0);

            return entry;
        }

        return null;
    }

    @Override
    public InputStream getInputStream(ISourceLocation zipLocation, String subPath) throws IOException {
        final ZipInputStream stream = new ZipInputStream(CTX.getInputStream(zipLocation));
        if (skipToEntry(stream, zipLocation, subPath) != null) {
            // ideally we could just return the stream
            // except that the JarInputStream doesn't hold the InputStream contract.
            // Mainly, the read(byte[],int,int) works correctly, as that it stops at 
            // the boundary of the file but the read() doesn't limit itself to the end 
            // of the nested file, but works on the whole stream.
            return new InputStream() {

                @Override
                public int read() throws IOException {
                    byte[] result = new byte[1];
                    if (stream.read(result, 0, 1) != -1) {
                        return result[0] & 0xFF;
                    }
                    return -1;
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return stream.read(b, off, len);
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return stream.read(b, 0, b.length);
                }

                @Override
                public void close() throws IOException {
                    stream.close();
                }

                @Override
                public int available() throws IOException {
                    return stream.available();
                }

                @Override
                public long skip(long n) throws IOException {
                    return stream.skip(n);
                }
            };
        }
        try {
            stream.close();
        }
        catch (IOException e) {
        }
        return null;
    }
}
