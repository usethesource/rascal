/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.utils;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.TreeAdapter;

public final class StringUtils {
	
	// TODO remove this one
	public static String unescape(String str, AbstractAST ast, Environment env) {
		byte[] bytes = str.getBytes();
		StringBuffer result = new StringBuffer();
		
		for (int i = 1; i < bytes.length - 1; i++) {
			char b = (char) bytes[i];
			switch (b) {
			/*
			 * Replace <var> by var's value.
			 */
			case '<':
				StringBuffer var = new StringBuffer();
				char varchar;
				while((varchar = (char) bytes[++i]) != '>'){
					var.append(varchar);
				}
				Result<IValue> val = env.getVariable(var.toString());
				String replacement;
				if(val == null || val.getValue() == null) {
					throw new UninitializedVariableError(var.toString(), ast);
				}
				
				if(val.getType().isStringType()){
					replacement = ((IString)val.getValue()).getValue();
				} 
				else if (val.getType() instanceof NonTerminalType) {
					replacement = TreeAdapter.yield((IConstructor) val.getValue());
				} 
				else {
					replacement = val.getValue().toString();
				}
//				replacement = replacement.replaceAll("<", "\\\\<"); TODO: maybe we need this after all?
				result.append(replacement);
				continue;
			case '\\':
				switch (bytes[++i]) {
				case '\\':
					b = '\\'; 
					break;
				case 'n':
					b = '\n'; 
					break;
				case '"':
					b = '"'; 
					break;
				case 't':
					b = '\t'; 
					break;
				case 'b':
					b = '\b'; 
					break;
				case 'f':
					b = '\f'; 
					break;
				case 'r':
					b = '\r'; 
					break;
				case '<':
					b = '<'; 
					break;
				case '>':
					b = '>';
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					b = (char) (bytes[i] - '0');
					if (i < bytes.length - 1 && Character.isDigit(bytes[i+1])) {
						b = (char) (b * 8 + (bytes[++i] - '0'));
						
						if (i < bytes.length - 1 && Character.isDigit(bytes[i+1])) {
							b = (char) (b * 8 + (bytes[++i] - '0'));
						}
					}
					break;
				case 'u':
					// TODO unicode escape
					break;
				default:
				    b = '\\';	
				}
			}
			
			result.append(b);
		}
		
		return result.toString();
	}
	
	public static String unescapeSingleQuoteAndBackslash(String str) {
		char[] chars = str.toCharArray();
		StringBuffer result = new StringBuffer();
		
		
		for (int i = 0; i < chars.length; i++) {
			char b = chars[i];
			switch (b) {
			case '\\':
				switch (chars[++i]) {
				case '\\':
					b = '\\'; 
					break;
				case '\'':
					b = '\''; 
					break;
				}
			}
			result.append(b);
		}
		return result.toString();
	}
	
	
	public static String unquote(String str) {
		return str.substring(1, str.length() - 1);
	}

	public static String unescapeBase(String str) {
		char[] chars = str.toCharArray();
		StringBuffer result = new StringBuffer();
		
		for (int i = 0; i < chars.length; i++) {
			char b = chars[i];
			switch (b) {
			case '\\':
				switch (chars[++i]) {
				case '\\':
					result.append('\\');
					b = '\\'; 
					break;
				case 'n':
					b = '\n'; 
					break;
				case '"':
					b = '"'; 
					break;
				case '\'':
					result.append('\\');
					b = '\'';
					break;
				case 't':
					b = '\t'; 
					break;
				case 'b':
					b = '\b'; 
					break;
				case 'f':
					b = '\f'; 
					break;
				case 'r':
					b = '\r'; 
					break;

				case '<':
					b = '<'; 
					break;
				case '>':
					b = '>';
					break;
				
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					b = (char) (chars[i] - '0');
					if (i < chars.length - 1 && Character.isDigit(chars[i+1])) {
						b = (char) (b * 8 + (chars[++i] - '0'));
						
						if (i < chars.length - 1 && Character.isDigit(chars[i+1])) {
							b = (char) (b * 8 + (chars[++i] - '0'));
						}
					}
					break;
				case 'u':
					// TODO unicode escape
					break;
				default:
				    b = '\\';	
				}
			}
			
			result.append(b);
		}
		
		return result.toString();
	}
	
}
