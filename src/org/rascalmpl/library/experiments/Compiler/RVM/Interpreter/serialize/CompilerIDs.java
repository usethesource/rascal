/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

public class CompilerIDs {
    
    private static final int FUNCTION_ID = 1;
    private static final int CODEBLOCK_ID = 2;
    
    public static class Function {
        public static final int ID = FUNCTION_ID;
        public static final int NAME = 1;
        public static final int FTYPE = 2;
        public static final int KWTYPE = 3;
        public static final int SCOPE_ID = 4;
        public static final int FUN_IN = 5;
        public static final int SCOPE_IN = 6;
        public static final int NFORMALS = 7;
        public static final int NLOCALS = 8;
        public static final int IS_DEFAULT = 9;
        public static final int IS_TEST = 10;
        
        public static final int TAGS = 11;
        
        public static final int MAX_STACK = 12;
        public static final int CODEBLOCK = 13;
        public static final int CONSTANT_STORE = 14;
        public static final int TYPE_CONSTANT_STORE = 15;
        public static final int CONCRETE_ARG = 16;
        public static final int ABSTRACT_FINGERPRINT = 17;
        public static final int CONCRETE_FINGERPRINT = 18;
        public static final int FROMS = 19;
        public static final int TOS = 20;
        public static final int TYPES = 21;
        public static final int HANDLERS = 22;
        public static final int FROM_SPS = 23;
        public static final int LAST_HANDLER = 24;
        public static final int FUN_ID = 25;
        public static final int IS_COROUTINE = 26;
        public static final int REFS = 27;
        public static final int IS_VARARGS = 28;
        public static final int SRC = 29;
        public static final int LOCAL_NAMES = 30;
        public static final int CONTINUATION_POINTS = 31;    
    }
    
    public static class CodeBlock {
        public static final int ID = CODEBLOCK_ID;
        public static final int NAME = 1;
        public static final int FINAL_CONSTANT_STORE = 2;
        public static final int FINAL_TYPECONSTANT_STORE = 3;
        public static final int FUNCTION_MAP = 4;
        public static final int RESOLVER = 5;
        public static final int CONSTRUCTOR_MAP = 6;
        public static final int FINAL_CODE = 7;
    }

}
