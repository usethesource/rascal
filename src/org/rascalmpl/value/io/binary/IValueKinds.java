/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
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
package org.rascalmpl.value.io.binary;

public class IValueKinds {
	public static final boolean BOOLEAN_COMPOUND = false;
	
	public static final boolean DATETIME_COMPOUND = false;
	public static final boolean INTEGER_COMPOUND = false;
	public static final boolean SOURCELOCATION_COMPOUND = false;
	public static final boolean NUMBER_COMPOUND = false;
	public static final boolean RATIONAL_COMPOUND = true; // not for type!
	public static final boolean REAL_COMPOUND = false;
	public static final boolean STRING_COMPOUND = false;
	public static final boolean VALUE_COMPOUND = false;
	public static final boolean VOID_COMPOUND = false; 
	
	public static final boolean ADT_COMPOUND = true;
	public static final boolean ALIAS_COMPOUND = true;
	public static final boolean PARAMETER_COMPOUND = true;
	public static final boolean CONSTRUCTOR_COMPOUND = true;
	public static final boolean LIST_COMPOUND = true;
	public static final boolean MAP_COMPOUND = true;
	public static final boolean NODE_COMPOUND = true;  // not for type!
    public static final boolean EXTERNAL_COMPOUND = true;
	/*
	public static final boolean FUNCTION_COMPOUND = true;
	public static final boolean NONTERMINAL_COMPOUND = false;
	public static final boolean OVERLOADED_COMPOUND = true; 
	public static final boolean REIFIED_COMPOUND = true; 
	*/
	public static final boolean SET_COMPOUND = true; 
	public static final boolean TUPLE_COMPOUND = true;

}
