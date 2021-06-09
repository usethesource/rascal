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
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationOutputRewriter extends ISourceLocationOutput {

    URIResolverRegistry reg();
    ISourceLocation rewrite(ISourceLocation uri) throws IOException;
    
    @Override
    default OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
        ISourceLocation parent = rewrite(URIUtil.getParentLocation(uri));
        return URIResolverRegistry.getInstance().getOutputStream(URIUtil.getChildLocation(parent, URIUtil.getLocationName(uri)), append);
    }

    @Override
    default void mkDirectory(ISourceLocation uri) throws IOException {
        ISourceLocation parent = rewrite(URIUtil.getParentLocation(uri));
        URIResolverRegistry.getInstance().mkDirectory(URIUtil.getChildLocation(parent, URIUtil.getLocationName(uri)));
    }
    
    @Override
    default FileChannel getWritableOutputStream(ISourceLocation uri, boolean append) throws IOException {
        return reg().getWriteableFileChannel(rewrite(uri), append);
    }

    @Override
    default void remove(ISourceLocation uri) throws IOException {
        reg().remove(rewrite(uri), false);
    }
    
    @Override
    default Charset getCharset(ISourceLocation uri) throws IOException {
        return reg().getCharset(rewrite(uri));
    }
    
    @Override
    String scheme();
}
