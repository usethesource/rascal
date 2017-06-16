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
    
    private static final int EXECUTABLE_ID = 1;
    private static final int CODEBLOCK_ID = 2;
    private static final int FUNCTION_ID = 3;
    private static final int OVERLOADED_FUNCTION_ID = 4;
    private static final int NESTED_VALUE_ID = 5;
    private static final int NESTED_TYPE_ID = 5;
    
    public static class Executable {
        public static final int ID = EXECUTABLE_ID;
        public static final int RASCAL_MAGIC = 1;
        public static final int RASCAL_VERSION = 2;
        public static final int RASCAL_RUNTIME_VERSION = 3;
        public static final int RASCAL_COMPILER_VERSION = 4;
        public static final int ERRORS = 5;
        public static final int MODULE_NAME = 6;
        public static final int MODULE_TAGS = 7;
        public static final int SYMBOL_DEFINITIONS = 8;
        public static final int FUNCTION_STORE = 9;
        public static final int FUNCTION_MAP = 10;
        public static final int CONSTRUCTOR_STORE = 11;
        public static final int CONSTRUCTOR_MAP = 12;
        public static final int OVERLOADED_STORE = 13;
        public static final int RESOLVER = 14;
        public static final int INITIALIZERS = 15;
        public static final int UID_MODULE_INIT = 16;
        public static final int UID_MODULE_MAIN = 17;
        public static final int JVM_BYTE_CODE = 18;
        public static final int FULLY_QUALIFIED_DOTTED_NAME = 19;
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
        public static final int SIMPLEARGS = 32;
    }
 
    public static class OverloadedFunction {
        public static final int ID = OVERLOADED_FUNCTION_ID;
        public static final int NAME = 1;
        public static final int FUN_TYPE = 2;
        public static final int FUNCTIONS = 3;
        public static final int CONSTRUCTORS = 4;
        public static final int FUN_IN = 5;
        public static final int SCOPE_IN = 6;
        public static final int ALL_CONCRETE_FUNCTION_ARGS = 7;
        public static final int ALL_CONCRETE_CONSTRUCTOR_ARGS = 8;
        public static final int FILTERED_FUNCTIONS = 9;
        public static final int FILTERED_CONSTRUCTORS = 10;
    }
    
    public static class NestedValue {
        public static final int ID = NESTED_VALUE_ID;
        public static final int BACK_REFERENCE = 1;
        public static final int VALUE = 2;
    }

    public static class NestedType {
        public static final int ID = NESTED_TYPE_ID;
        public static final int BACK_REFERENCE = 1;
        public static final int VALUE = 2;
    }
}
