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

import io.usethesource.vallang.ISourceLocation;

/**
 * Any scheme that can produce a ClassLoader for a given location can implement this 
 * interface. 
 * 
 * The fastest solution is to find a way to produce a URLClassLoader instance.
 * That instance will be merged with other URLClassLoaders in a single indexed class
 * file cache by {@see SourceLocationClassLoader}.
 * 
 * The next best thing is to return the ClassLoader that fits the modularity
 * scheme (Eclipse plugins, OSGI bundles, Maven ClassRealms, etc) at hand. 
 * 
 * Finally, if you do not implementa {@see IClassloaderLocationResolver} for your scheme, 
 * the {@see URIResolverRegistry} will produce a default generic ClassLoader based on
 * {@see ISourceLocationInput.getInputStream()}.
 */
public interface IClassloaderLocationResolver {
    /**
     * @return the scheme this resolver supports
     */
    String scheme();
    
    /**
     * Produce a classloader for the given location.
     * 
     * @param  loc a location with loc.getScheme().equals(this.getScheme())
     * @param  parent Classloader to defer to when a needed class is not provided directly by the returned classloader
     * 
     * @return a classloader corresponding to the given loc, with parent classloader `parent` and never null.
     * @throws IOException when the location can not be resolved even though the scheme matches, 
     *         or something else goes wrong while loading the classloader itself.
     */
    ClassLoader getClassLoader(ISourceLocation loc, ClassLoader  parent) throws IOException;
}
