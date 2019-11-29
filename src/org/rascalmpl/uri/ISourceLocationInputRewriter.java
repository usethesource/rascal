/** 
 * Copyright (c) 2019, Jurgen J. Vinju, Centrum Wiskunde & Informatica (NWOi - CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */ 
package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationInputRewriter extends ISourceLocationInput {
    
    URIResolverRegistry reg();
    ISourceLocation rewrite(ISourceLocation uri) throws IOException;
    
    @Override
    default InputStream getInputStream(ISourceLocation uri) throws IOException {
        return reg().getInputStream(rewrite(uri));
    }
    
    @Override
    default FileChannel getReadableFileChannel(ISourceLocation uri) throws IOException {
        return reg().getReadableFileChannel(rewrite(uri));
    }
    
    @Override
    default Charset getCharset(ISourceLocation uri) throws IOException {
        return reg().getCharset(rewrite(uri));
    }
    
    @Override
    default boolean exists(ISourceLocation uri) {
        try {
            return reg().exists(rewrite(uri));
        } 
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    default long lastModified(ISourceLocation uri)  throws IOException {
        return reg().lastModified(rewrite(uri));
    }
    
    @Override
    default boolean isDirectory(ISourceLocation uri) {
        try {
            return reg().isDirectory(rewrite(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    default boolean isFile(ISourceLocation uri)  {
        try {
            return reg().isFile(rewrite(uri));
        }
        catch (IOException e) {
            return false;
        }
    }
    
    @Override
    default String[] list(ISourceLocation uri)  throws IOException {
        return reg().listEntries(rewrite(uri));
    }

    @Override
    String scheme();
}
