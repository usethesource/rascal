/** 
 * Copyright (c) 2020, Jurgen J. Vinju, NWO-I CWI
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
package org.rascalmpl.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * A ClassLoader which finds classes and resources via a
 * classloader list. Classes are resolved in order of the occurrence
 * of classloaders in the list.
 */
public class ListClassLoader extends ClassLoader {
    private final List<ClassLoader> path;
    private final Stack<String> stack = new Stack<>();

    public ListClassLoader(List<ClassLoader> path, ClassLoader parent) {
        super(parent);
        this.path = Collections.unmodifiableList(path);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (stack.contains(name)) {
            // we're already looking for this and could not find it apparently
            return null;
        }
        
        for (ClassLoader l : path) {
            try {
                stack.push(name);
                return l.loadClass(name);
            }
            catch (ClassNotFoundException e) {
                // this is normal, try next loader
                continue;
            }
            finally {
                stack.pop();
            }
        }
        
        return null;
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
