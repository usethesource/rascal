/** 
 * Copyright (c) 2019, Lina Ochoa, Centrum Wiskunde & Informatica (NWOi - CWI) 
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
package org.rascalmpl.library.lang.java.m3.internal;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;


public interface NodeResolver {    
    
    IConstructor resolveLanguageVersion(ClassNode node);
    
    /**
     * Returns the location of a bytecode node given a bytecode 
     * object and its parent logical location.
     * E.g. Method node and a |java+class:///...| location. 
     * @param node - bytecode object
     * @param parent - parent logical location
     * @return location of the bytecode node
     */
    ISourceLocation resolveBinding(Object node, ISourceLocation parent);
    
    /**
     * Returns a location of a method node given its name, 
     * descriptor, and parent location (class or interface) 
     * @param name - name of the method
     * @param desc - bytecode descriptor of the method
     * @param clazz - parent logical location
     * @return location of the method node
     */
    ISourceLocation resolveMethodBinding(String name, String desc, ISourceLocation clazz);
    
    /**
     * Returns the Rascal constructor of a bytecode node 
     * given a bytecode object and its parent logical location.
     * @param node - bytecode object
     * @param parent - parent logical location
     * @return Rascal constructor (type symbol)
     */
    IConstructor resolveType(Object node, ISourceLocation uri);
    
    /**
     * Returns an ASM ClassReader from a compilation unit location 
     * or name. 
     * @param className - class/comilation unit name/path (<pkg>/<name>)
     * @param uri - source location of the JAR file
     * @return ASM ClassReader, null if the compilation unit is not found
     */
    ClassReader buildClassReader(String className);
    
    /**
     * Returns an ASM ClassReader from an input stream.
     * @param classStream - class/compilation unit input stream 
     * @return ASM ClassReader, null if the compilation unit is not found
     */
    ClassReader buildClassReader(InputStream classStream) throws IOException;

    String resolveClassScheme(ClassNode node);
}
