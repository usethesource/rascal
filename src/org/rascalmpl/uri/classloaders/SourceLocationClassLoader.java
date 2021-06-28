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
package org.rascalmpl.uri.classloaders;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.rascalmpl.uri.URIResolverRegistry;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

/**
 * A ClassLoader which finds classes and resources via a
 * classloader location list. @see IClassloaderLocationResolver
 * for more information on how we transform ISourceLocations to Classloaders. 
 */
public class SourceLocationClassLoader extends ClassLoader {
    private final List<ClassLoader> path;
    private final Stack<SearchItem> stack = new Stack<>();

    public SourceLocationClassLoader(IList classpath, ClassLoader parent) {
        super(parent);
        this.path = initialize(classpath);
    }
    
    private List<ClassLoader> initialize(IList locs) {
        URIResolverRegistry reg = URIResolverRegistry.getInstance();
        ArrayList<URL> fileLocations = new ArrayList<>(locs.length());
        List<ClassLoader> result = new ArrayList<>(locs.length());

        // to enhance class loading performance we group file URL jars
        // into a single URLClassLoader
        for (IValue elem : locs) {
            try {
                // for efficiency's sake, we try to resolve as many locations to the file:/// scheme
                ISourceLocation loc = reg.logicalToPhysical((ISourceLocation) elem);

                // because they all get `this` as the parent, the classloaders will be able to refer to each
                // other like in normal JVM classpath also works. The order of lookup is defined by the current 
                // for-loop. 
                ClassLoader loader = reg.getClassLoader((ISourceLocation) loc, this);

                if (loader instanceof URLClassLoader) {
                    // we collect the URLs of URLClassloaders for later use.
                    for (URL url : ((URLClassLoader) loader).getURLs()) {
                        fileLocations.add(url);
                    }
                }
                else {
                    result.add(loader);
                }
            }
            catch (IOException e) {
                // this may happen and should have been reported earlier by the scheme registration code in {@see URIResolverRegistry}
                // do nothing for now
            }
        }

        // and here we group all URLs into a single URLClassLoader
        if (!fileLocations.isEmpty()) {
            // important: `this` is the parent
            result.add(new URLClassLoader(fileLocations.toArray(new URL[0]), this));
        }

        return result;
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader l : path) {
            SearchItem item = new SearchItem(l, name);

            try {
                if (stack.contains(item)) {
                    // we fix an infinite recursion here; if we are already
                    continue;
                }
                else {
                    try {
                        stack.push(item);
                        return l.loadClass(name);
                    }
                    finally {
                        stack.pop();
                    }
                }
            }
            catch (ClassNotFoundException e) {
                // this is normal, try next loader
                continue;
            }
        }
        
        throw new ClassNotFoundException(name);
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

    private static class SearchItem {
        private final ClassLoader loader;
        private final String className;

        public SearchItem(ClassLoader loader, String className) {
            this.loader = loader;
            this.className = className;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass().equals(getClass())) {
                SearchItem other = (SearchItem) obj;

                return other.loader == loader && other.className.equals(className);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return 17 + 7 * loader.hashCode() + 19 * className.hashCode();
        }
    }
}
