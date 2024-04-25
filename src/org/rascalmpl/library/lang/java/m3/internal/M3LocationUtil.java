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

import java.net.URISyntaxException;

import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValueFactory;

public class M3LocationUtil {
     private static final IValueFactory valueFactory = ValueFactoryFactory.getValueFactory();
    
     /**
     * Creates a location. Existing exceptions are captured as a 
     * RuntimeException.
     * @param scheme - location scheme
     * @param authority - location authority
     * @param path - location path
     * @return location
     */
    public static ISourceLocation makeLocation(String scheme, String authority, String path) { 
        try {
            return valueFactory.sourceLocation(scheme, authority, path);
        } 
        catch (URISyntaxException | UnsupportedOperationException e) {
            throw new RuntimeException("Error while creating an ISourceLocation", e);
        }
    }
    
    /**
     * Creates a location. 
     * @param uri - location
     * @param offset - text starting point
     * @param length - length of text
     * @return location
     */
    public static ISourceLocation makeLocation(ISourceLocation uri, int offset, int length) {
        return valueFactory.sourceLocation(uri, offset, length);
    }
    
    /**
     * Extends the path of a given location with additional information.
     * Existing exceptions are captured as a RuntimeException.
     * E.g. uri: |jar+file://path/to/file.jar!|, path: "path/to/folder"
     * returns |jar+file://path/to/file.jar!path/to/folder|
     * @param uri - location
     * @param path - additional path
     * @return extended location
     */
    public static ISourceLocation extendPath(ISourceLocation uri, String path) {
        return URIUtil.getChildLocation(uri, path);
    }
    
    /**
     * Changes the path of a location.
     * @param uri - location
     * @param path - additional path
     * @return modified location
     */
    public static ISourceLocation changePath(ISourceLocation uri, String path) {
        try {
            return URIUtil.changePath(uri, path);
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Changing path error with URIUtil", e);
        }
    }
    
    /**
     * Returns the name of the current location.
     * @param path - location path
     * @return name of current location
     */
    public static IString getLocationName(String path) {
        return valueFactory.string(path.substring(path.lastIndexOf("/") + 1));
    }
    
    /**
     * Returns the name of the current location.
     * @param loc - location
     * @return name of current location
     */
    public static IString getLocationName(ISourceLocation loc) {
        String path = loc.getPath();
        return getLocationName(path);
    }
}
