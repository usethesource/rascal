/**************************************************
 * Copyright (c) 2022, NWO-I Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *   
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *   
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *   
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 ****************************************************/
package org.rascalmpl.uri.file;

import java.io.IOException;

import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public abstract class AliasedFileResolver implements ILogicalSourceLocationResolver {

    private final String scheme;
    protected final ISourceLocation root;

    AliasedFileResolver(String scheme, String rootPath) {
        this.scheme = scheme;
        this.root = FileURIResolver.constructFileURI(rootPath);
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        return URIUtil.getChildLocation(root, input.getPath());
    }

    @Override
    public String scheme() {
        return scheme;
    }

    @Override
    public String authority() {
        return "";
    }
    
}
