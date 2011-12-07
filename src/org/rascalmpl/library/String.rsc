@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
module String

import Origins;

@doc{Convert a character code into a string}
@javaClass{org.rascalmpl.library.String}
public java str stringChar(int char);

@doc{Convert a list of character codes into a string}
@javaClass{org.rascalmpl.library.String}
public java str stringChars(list[int] chars);

@doc{Return the character at position i in string s.}
@javaClass{org.rascalmpl.library.String}
public java int charAt(str s, int i) throws out_of_range(str msg);

@doc{Returns true if string s ends with given string suffix.}
@javaClass{org.rascalmpl.library.String}
public java bool endsWith(str s, str suffix);

@doc{Returns string with leading and trailing whitespace removed.}
@javaClass{org.rascalmpl.library.String}
public java str trim(str s);

@doc{Return string of length n, with s placed according to dir (left/center/right) and padded with pad}
@javaClass{org.rascalmpl.library.String}
private java str format(str s, str dir, int n, str pad);

@doc{Center s in string of length n using spaces}
public str center(str s, int n)
{
  return format(s, "center", n, " ");
}

@doc{Center s in string of length n using pad}
public str center(str s, int n, str pad)
{
  return format(s, "center", n, pad);
}

@doc{Is string empty?}
@javaClass{org.rascalmpl.library.String}
public java bool isEmpty(str s);

@doc{Left align s in string of length n using spaces}
public str left(str s, int n)
{
  return format(s, "left", n, " ");
}

@doc{Left align s in string of length n using pad}
public str left(str s, int n, str pad)
{
  return format(s, "left", n, pad);
}

@doc{Replace all occurrences of "find" in "input" by "replacement"}
@javaClass{org.rascalmpl.library.String}
public java str replaceAll(str input, str find, str replacement);

@doc{Replace the first occurrence of "find" in "input" by "replacement"}
@javaClass{org.rascalmpl.library.String}
public java str replaceFirst(str input, str find, str replacement);

@doc{Replace the last occurrence of "find" in "input" by "replacement"}
@javaClass{org.rascalmpl.library.String}
public java str replaceLast(str input, str find, str replacement);

@doc{Does string "input" contain the substring "find"?}
@javaClass{org.rascalmpl.library.String}
public java bool contains(str input, str find);

@doc{Find all occurrences of "find" in "input".}
@javaClass{org.rascalmpl.library.String}
public java list[int] findAll(str input, str find);

@doc{Find the first occurrence of "find" in "input".}
@javaClass{org.rascalmpl.library.String}
public java int findFirst(str input, str find);

@doc{Find last occurrence of "find" in "input".}
@javaClass{org.rascalmpl.library.String}
public java int findLast(str input, str find);

/*
@doc{Replace all occurrences of "find" in "input" by "replacement"}
public str replaceAll(str input, str find, str replacement) {
	return visit (input) { 
	  case find => "<replacement>"
	}	
}

@doc{Replace the first occurrence of "find" in "input" by "replacement"}
public str replaceFirst(str input, str find, str replacement) {
	if(/^<pre:.*?><find><post:.*>$/ := input) {	
		return "<pre><replacement><post>";
	}	
	
	return input;
}


@doc{Replace the last occurrence of "find" in "input" by "replacement"}
public str replaceLast(str input, str find, str replacement) {
	if(/^<pre:.*><find><post:.*?>$/ := input) {	
		return "<pre><replacement><post>";
	}	
	
	return input;
}
*/

@doc{Right align s in string of length n using spaces}
public str right(str s, int n)
{
  return format(s, "right", n, " ");
}

@doc{Return string with all characters in reverse order.}
@javaClass{org.rascalmpl.library.String}
public java str reverse(str s);

@doc{Right align s in string of length n using pad}
public str right(str s, int n, str pad)
{
  return format(s, "right", n, pad);
}

@doc{Return the length of string s.}
@javaClass{org.rascalmpl.library.String}
public java int size(str s);

@doc{Return true if string s starts with the string prefix.}
@javaClass{org.rascalmpl.library.String}
public java bool startsWith(str s, str prefix);

@doc{Extract a substring from string s from begin to the end of s}
@javaClass{org.rascalmpl.library.String}
public java str substring(str s, int begin);

@doc{Extract a substring from string s from begin to end}
@javaClass{org.rascalmpl.library.String}
public java str substring(str s, int begin, int end);

@doc{Convert a string s to integer}
@javaClass{org.rascalmpl.library.String}
public java int toInt(str s);

@doc{Convert a string s to integer using radix r}
@javaClass{org.rascalmpl.library.String}
public java int toInt(str s, int r);

@doc{Convert all characters in string s to lowercase.}
@javaClass{org.rascalmpl.library.String}
public java str toLowerCase(str s);

@doc{Convert a string s to a real}
@javaClass{org.rascalmpl.library.String}
public java real toReal(str s);

@doc{Convert all characters in string s to uppercase.}
@javaClass{org.rascalmpl.library.String}
public java str toUpperCase(str s);

@doc{Convert all characters in string s to uppercase.}
@javaClass{org.rascalmpl.library.String}
public java str escape(str s, map[str,str] mapping);

@javaClass{org.rascalmpl.library.String}
public java list[tuple[str string, node origin]] origins(str s);
