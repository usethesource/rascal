/** 
 * Copyright (c) 2017, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.core.uri;

import io.usethesource.vallang.ISourceLocation;

public class SourceLocationURICompare {
    
    private static interface URIIterator {
        String current(ISourceLocation loc);
        URIIterator next(ISourceLocation loc);
    }
    
    private final static URIIterator SCHEME = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return SCHEME_SEP; }
        public String current(ISourceLocation loc) { return loc.getScheme(); }
    };
    
    private final static URIIterator SCHEME_SEP = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return loc.hasAuthority() ? AUTHORITY : AUTHORITY_SEP; }
        public String current(ISourceLocation loc) { return "://"; }
    };
    
    private final static URIIterator AUTHORITY = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return AUTHORITY_SEP; }
        public String current(ISourceLocation loc) { return loc.getAuthority(); }
    };
    
    private final static URIIterator AUTHORITY_SEP = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return loc.hasPath() ? PATH : (loc.hasQuery() ? QUERY_PRE : (loc.hasFragment() ? FRAGMENT_PRE : null)); }
        public String current(ISourceLocation loc) { return "/"; }
    };
    
    private final static URIIterator PATH = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return (loc.hasQuery() ? QUERY_PRE : (loc.hasFragment() ? FRAGMENT_PRE : null)); }
        public String current(ISourceLocation loc) { return loc.getPath(); }
    };
    
    private final static URIIterator QUERY_PRE = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return QUERY; }
        public String current(ISourceLocation loc) { return "?"; }
    };
    
    private final static URIIterator QUERY = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return loc.hasFragment() ? FRAGMENT_PRE : null; }
        public String current(ISourceLocation loc) { return loc.getQuery(); }
    };
    
    private final static URIIterator FRAGMENT_PRE = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return FRAGMENT; }
        public String current(ISourceLocation loc) { return "#"; }
    };
    
    private final static URIIterator FRAGMENT = new URIIterator() {
        public URIIterator next(ISourceLocation loc) { return null; }
        public String current(ISourceLocation loc) { return loc.getFragment(); }
    };

    
    public static int compare(ISourceLocation a, ISourceLocation b) {
        URIIterator left = SCHEME;
        URIIterator right = SCHEME;
        while (left != null && right != null) {
            String leftChunk = left.current(a);
            String rightChunk = right.current(b);
            if (leftChunk != rightChunk) {
                int result = leftChunk.compareTo(rightChunk);
                if (result != 0) {
                    return result;
                }
            }
            left = left.next(a);
            right = right.next(b);
        }
        if (left == null && right == null) {
            return 0;
        }
        if (right != null) {
            // left was shorter but equal for shared part 
            return -1;
        }
        return 1;
   }
}