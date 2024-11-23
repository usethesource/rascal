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

public final class M3Constants {

    //---------------------------------------------
    // Constants
    //---------------------------------------------
    
    public final static String CLASS_SCHEME = "java+class";
    public final static String COMP_UNIT_SCHEME = "java+compilationUnit";
    public final static String CONSTRUCTOR_SCHEME = "java+constructor";
    public final static String ENUM_SCHEME = "java+enum";
    public final static String ENUM_CONSTANT_SCHEME = "java+enumConstant";
    public final static String FIELD_SCHEME = "java+field";
    public final static String INITIALIZER_SCHEME = "java+initializer";
    public final static String INTERFACE_SCHEME = "java+interface";
    public final static String METHOD_SCHEME = "java+method";
    public final static String ARRAY_METHOD_SCHEME = "java+arrayMethod";
    public final static String PACKAGE_SCHEME = "java+package";
    public final static String PARAMETER_SCHEME = "java+parameter";
    public final static String PRIMITIVE_TYPE_SCHEME = "java+primitiveType";
    public final static String FILE_SCHEME = "file";
    public final static String JAR_SCHEME = "jar";
    public final static String UNRESOLVED_SCHEME = "unresolved";
    public final static String UNKNOWN_SCHEME = "unknown";
    
    // Constant with the name of consructor methods in Java bytecode
    public final static String COMPILED_CONSTRUCTOR_NAME = "<init>";
    public final static String COMPILED_STATIC_CONSTRUCTOR_NAME = "<clinit>";

    // Constants with M3-specific names
    public final static String M3_STATIC_CONSTRUCTOR_NAME = "$initializer";
    
    public final static String OBJECT_CLASS_PATH = Object.class.getName().replace(".", "/");
    public final static String ENUM_CLASS_PATH = Enum.class.getName().replace(".", "/");
    
    
    //---------------------------------------------
    // Private Constructor
    //---------------------------------------------
    
    private M3Constants() {
        throw new AssertionError("M3Constants should not be instantiated.", null);
    }
}
