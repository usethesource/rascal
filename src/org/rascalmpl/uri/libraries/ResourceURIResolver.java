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
package org.rascalmpl.uri.libraries;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import org.rascalmpl.uri.ISourceLocationInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;

/**
 * A resource scheme where the <hostname> is a fully qualified class name and all
 * paths are loaded relative to the resource root of the given class. The given classes
 * are loaded via the current top-level system classloader which was also used to load the
 * current ResourceURIResolver class.
 */
public class ResourceURIResolver implements ISourceLocationInput {

    protected String getPath(ISourceLocation uri) {
        String path = uri.getPath();
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.contains("//")) {
            path = path.replaceAll("//","/");
        }
        return "/" + path;
    }
    
    public boolean exists(ISourceLocation uri) {
        Class<?> clz = getClass(uri);
        if (clz != null) {
            return clz.getResource(getPath(uri)) != null;
        }
        else {
            return false;
        }
            
    }

    private Class<?> getClass(ISourceLocation uri) {
        String className = uri.getAuthority();
        
        if (className == null || className.isEmpty()) {
            return null;
        }
        
        try {
            return getClass().getClassLoader().loadClass(className);
        }
        catch (ClassNotFoundException e) {
            return null;
        }
    }

    public InputStream getInputStream(ISourceLocation uri) throws IOException {
        Class<?> clz = getClass(uri);
        if (clz == null) {
            throw new FileNotFoundException("class not found: " + uri.getAuthority());
        }
        InputStream resourceAsStream = clz.getResourceAsStream(getPath(uri));
        if (resourceAsStream != null) {
            return resourceAsStream;
        }
        throw new FileNotFoundException(uri.toString());
    }

    public String scheme() {
        return "resource";
    }

    public boolean isDirectory(ISourceLocation uri) {
      try {
        return URIResolverRegistry.getInstance().isDirectory(resolve(uri));
      } catch (IOException e) {
        return false;
      }
    }

    public boolean isFile(ISourceLocation uri) {
        try {
            return URIResolverRegistry.getInstance().isFile(resolve(uri));
        } catch (IOException e) {
            return false;
        }
    }

    protected ISourceLocation resolve(ISourceLocation uri) throws IOException {
        try {
            URL res = getClass(uri).getResource(getPath(uri));
            if(res == null) {
                throw new FileNotFoundException(getPath(uri));
            }
            if (res.getProtocol().equals("jar") && res.getPath().startsWith("file:/")) {
              return ValueFactoryFactory.getValueFactory().sourceLocation("jar", null, res.getPath().substring("file:".length()));
            }
            return ValueFactoryFactory.getValueFactory().sourceLocation(URIUtil.fromURL(res));
        } catch (URISyntaxException e) {
            assert false;
            throw new IOException(e);
        }
    }
    
    public long lastModified(ISourceLocation uri) throws IOException {
        return URIResolverRegistry.getInstance().lastModified(resolve(uri));
    }

    @Override
    public String[] list(ISourceLocation uri) throws IOException {
        return URIResolverRegistry.getInstance().listEntries(resolve(uri)); 
    }
    
    public boolean supportsHost() {
        return true;
    }

    @Override
    public Charset getCharset(ISourceLocation uri) throws IOException {
        return URIResolverRegistry.getInstance().getCharset(resolve(uri));
    }
}
