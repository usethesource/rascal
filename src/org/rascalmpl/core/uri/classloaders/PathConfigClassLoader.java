/** 
 * Copyright (c) 2017, Jurgen J. Vinju, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.core.uri.classloaders;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.rascalmpl.core.library.util.PathConfig;
import org.rascalmpl.core.uri.URIResolverRegistry;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * A ClassLoader which finds classes and resources in the
 * classloader location list of a PathConfig. @see IClassloaderLocationResolver
 * for more information on how we transform ISourceLocations to Classloaders. 
 */
public class PathConfigClassLoader extends ClassLoader {
    private final List<ClassLoader> path;

    public PathConfigClassLoader(PathConfig pcfg, ClassLoader parent) {
        super(parent);
        this.path = initialize(pcfg);
    }
    
    private List<ClassLoader> initialize(PathConfig pcfg) {
        try {
            URIResolverRegistry reg = URIResolverRegistry.getInstance();
            IList locs = pcfg.getClassloaders();
            List<ClassLoader> result = new ArrayList<>(locs.length());

            // TODO: group URLClassLoaders into a single instance 
            // to enhance class loading performance
            for (IValue loc : locs) {
                // because they all get `this` as the parent, the classloaders will be able to refer to each
                // other like in normal JVM classpath also works. The order of lookup is defined by the current 
                // for-loop. 
                result.add(reg.getClassLoader((ISourceLocation) loc, this));
            }

            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        for (ClassLoader l : path) {
            try {
                return l.loadClass(name);
            }
            catch (ClassNotFoundException e) {
                // this is normal, try next loader
                continue;
            }
        }
        
        // is caught by the parent.loadClass(name, resolve) method
        throw new ClassNotFoundException(name);
    }
    
    @Override
    public URL getResource(String name) {
        for (ClassLoader l : path) {
            URL url = l.getResource(name);
            
            if (url != null) {
                return url;
            }
        }
        
        return null;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        List<URL> result = new ArrayList<>(path.size());
        
        for (ClassLoader l : path) {
            Enumeration<URL> e = l.getResources(name);
            while (e.hasMoreElements()) {
                result.add(e.nextElement());
            }
        }
        
        return new Enumeration<URL>() {
            final Iterator<URL> it = result.iterator();
            
            @Override
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            @Override
            public URL nextElement() {
                return it.next();
            }
        };
    }
}
