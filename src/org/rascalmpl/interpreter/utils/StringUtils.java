/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class StringUtils {
	
	public static String unescapeSingleQuoteAndBackslash(String str) { //TODO not Unicode safe!
		char[] chars = str.toCharArray();
		StringBuffer result = new StringBuffer();
		
		
		for (int i = 0; i < chars.length; i++) {
			char b = chars[i];
			switch (b) {
			case '\\':
				if(i >= chars.length - 1){
					b = '\\';
				} else {
					switch (chars[++i]) {
					case '\\':
						b = '\\'; 
						break;
					case '\'':
						b = '\''; 
						break;
					}
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
				
				case 'a':
					StringBuilder a = new StringBuilder();
					a.append(chars[++i]);
					a.append(chars[++i]);
					b = (char) Integer.parseInt(a.toString(), 16);
					break;
				case 'u':
					StringBuilder u = new StringBuilder();
					u.append(chars[++i]);
					u.append(chars[++i]);
					u.append(chars[++i]);
					u.append(chars[++i]);
					b = (char) Integer.parseInt(u.toString(), 16);
					break;
				case 'U':
					StringBuilder U = new StringBuilder();
					U.append(chars[++i]);
					U.append(chars[++i]);
					U.append(chars[++i]);
					U.append(chars[++i]);
					U.append(chars[++i]);
					U.append(chars[++i]);
					int cp = Integer.parseInt(U.toString(), 16);
					result.appendCodePoint(cp);
					continue;
				default:
				    b = '\\';	
				}
			}
			
			result.append(b);
		}
		
		return result.toString();
	}
	
	private final static Pattern getLastIdentifier = Pattern.compile(".*?([\\\\]?[_a-zA-Z][\\-_a-zA-Z0-9]*)\\s*$");

	public static class OffsetLengthTerm {
	  public OffsetLengthTerm(int offset, int length, String term) {
	    this.offset = offset;
	    this.length = length;
	    this.term = term;
    }
	  public int offset;
	  public int length;
	  public String term;
	}
	public static OffsetLengthTerm findRascalIdentifierAtOffset(String buffer, int offset) {
		Matcher m = getLastIdentifier.matcher(buffer);
		int split = findSplitPoint(offset, buffer);
		if (split > 0) {
		  m.region(0, split + 1);
		}
		if (m.matches()) {
			String originalTerm = m.group(1).trim();
			return new OffsetLengthTerm(m.start(1), originalTerm.length(), originalTerm);
		}
		return null;
	}
	
	private static boolean validRascalIdentifier(char c) {
		return (c >= 'A' && c <= 'Z') 
			|| (c >= 'a' && c <= 'z')
			|| (c >= '0' && c <= '9')
			|| c == '_' || c == '-'
			;
	}

	private static int findSplitPoint(int currentCursorPosition, String currentConsoleInput) {
		for (int i = currentCursorPosition; i < currentConsoleInput.length(); i++) {
			if (!validRascalIdentifier(currentConsoleInput.charAt(i)))
				return i - 1;
		}
		return -1;
	}
	
	
}
