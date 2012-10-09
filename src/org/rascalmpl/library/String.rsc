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

@doc{
Synopsis: Center a string in given space.

Description:
# Center string `s` in string of length `n` using spaces.
# Center string `s` in string of length `n` using `pad` as padding character.

Examples:
<screen>
import String;
center("abc", 10);
center("abc", 10, "x");
</screen>
}

public str center(str s, int n)
{
  return format(s, "center", n, " ");
}

public str center(str s, int n, str pad)
{
  return format(s, "center", n, pad);
}

@doc{
Synopsis: Return character in a string by its index position.

Description:
Return the character at position `i` in string `s` as integer character code.
Also see [$String/stringChar] that converts character codes back to string.

Examples:
<screen>
import String;
charAt("abc", 0);
stringChar(charAt("abc", 0));
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java int charAt(str s, int i) throws out_of_range(str msg);

@doc{
Synopsis: Return characters of a string.
Description:
Return a list of the characters of `s` as integer character codes.
Also see [$String/stringChars] that converts character codes back to string.

Examples:
<screen>
import String;
chars("abc");
stringChars(chars("abc")) == "abc";
</screen>
}
public list[int] chars(str s) = [ charAt(s,i) | i <- [0..size(s)-1]];

@doc{
Synopsis: Check that a string contains another string.

Description:
Check whether the string `find` occurs as substring in the string `subject`.

Examples:
<screen>
import String;
contains("abracadabra", "bra");
contains("abracadabra", "e");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool contains(str input, str find);

@doc{
Synopsis: Check whether a string ends with a given substring.

Description:
Yields `true` if string `subject` ends with the string `suffix`.

Examples:
<screen>
import String;
endsWith("Hello.rsc", ".rsc");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool endsWith(str subject, str suffix);

@doc{
Synopsis: Replace single characters in a string.

Description:
Return a copy of `subject` in which each single character key in replacements
has been replaced by its associated value.

Examples:
<screen>
import String;
import IO;
escape("abracadabra", ("a" : "AA", "c" : "C"));
L = escape("\"Good Morning\", he said", ("\"": "\\\""));
println(L);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str escape(str subject, map[str,str] mapping);


@doc{
Synopsis: Find all occurrences of a string in another string.

Description:
Find all occurrences of string `find` in string `subject`.
The result is a (possible empty) list of positions where `find` matches.

See also [findFirst] and [findLast].

Examples:
<screen>
import String;
findAll("abracadabra", "a");
findAll("abracadabra", "bra");
findAll("abracadabra", "e");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[int] findAll(str subject, str find);

@doc{
Synopsis: Find the first occurrence of a string in another string.

Description:
Find the first occurrence of string `find` in string `subject`.
The result is either a position in `subject` or `-1` when `find` is not found.

Also see [findAll] and [findLast].

Examples:
<screen>
import String;
findFirst("abracadabra", "a");
findFirst("abracadabra", "bra");
findFirst("abracadabra", "e");
</screen>       
}
@javaClass{org.rascalmpl.library.Prelude}
public java int findFirst(str subject, str find);

@doc{
Synopsis: Find the last occurrence of a string in another string.

Description:
Find the last occurrence of string `find` in string `subject`.
The result is either a position in `subject` or `-1` when `find` is not found.

Also see [findAll] and [findFirst].

Examples:
<screen>
import String;
findLast("abracadabra", "a");
findLast("abracadabra", "bra");
findLast("abracadabra", "e");
</screen> 
}
@javaClass{org.rascalmpl.library.Prelude}
public java int findLast(str subject, str find);

@doc{
Synopsis: Check whether a string is empty.

Description:
Returns `true` if string `s` is empty.

Examples:
<screen>
import String;
isEmpty("");
isEmpty("abc");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(str s);

@doc{
Synopsis: Left alignment of string in given space.

Description:
# Left align string `s` in string of length `n` using spaces.
# Left align string `s` in string of length `n` using `pad` as pad character.

Examples:
<screen>
import String;
left("abc", 10);
left("abc", 10, "x");
</screen>
}
public str left(str s, int n)
{
  return format(s, "left", n, " ");
}

public str left(str s, int n, str pad)
{
  return format(s, "left", n, pad);
}

@doc{
Synopsis: Replace all occurrences of a string in another string.

Description:
Return a copy of `subject` in which all occurrences of `find` (if any) have been replaced by `replacement`.
Also see [replaceFirst] and [replaceLast].

Examples:
<screen>
import String;
replaceAll("abracadabra", "a", "A");
replaceAll("abracadabra", "ra", "RARA");
replaceAll("abracadabra", "cra", "CRA");
</screen>

Pitfalls:
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).

}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceAll(str subject, str find, str replacement);

@doc{
Synopsis: Replace the first occurrence of a string in another string.

Description:
Return a copy of `subject` in which the first occurrence of `find` (if it exists) has been replaced by `replacement`.
Also see [replaceAll] and [replaceLast].

Examples:
<screen>
import String;
replaceFirst("abracadabra", "a", "A");
replaceFirst("abracadabra", "ra", "RARA");
replaceFirst("abracadabra", "cra", "CRA");
</screen>

Pitfalls:
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceFirst(str subject, str find, str replacement);

@doc{
Synopsis: Replace the last occurrence of a string in another string.

Description:
Return a copy of `subject` in which the last occurrence of `find` (if it exists) has been replaced by `replacement`.
Also see [replaceFirst] and [replaceLast].

Examples:
<screen>
import String;
replaceLast("abracadabra", "a", "A");
replaceLast("abracadabra", "ra", "RARA");
replaceLast("abracadabra", "cra", "CRA");
</screen>

Pitfalls:
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceLast(str subject, str find, str replacement);

@doc{
Synopsis: Return a string with all characters in reverse order.

Description:
Returns string with all characters of string `s` in reverse order.

Examples:
<screen>
import String;
reverse("abc");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str reverse(str s);


@doc{
Synopsis: Right align s in string of length n using space.

Examples:
<screen>
import String;
right("abc", 10);
right("abc", 10, "x");
</screen>
}
@doc{
Synopsis: Right alignment of a string value in a given space.

Description:
# Right align string `s` in string of length `n` using spaces.
# Right align string `s` in string of length `n` using `pad` as pad character.

Examples:
<screen>
import String;
right("abc", 10);
right("abc", 10, "x");
</screen>
}
public str right(str s, int n)
{
  return format(s, "right", n, " ");
}

public str right(str s, int n, str pad)
{
  return format(s, "right", n, pad);
}


@doc{
Synopsis: Determine length of a string value.

Description:
Returns the length (number of characters) in string `s`.

Examples:
<screen>
import String;
size("abc");
size("");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java int size(str s);

@doc{
Synopsis: Check whether a string starts with a given prefix.

Description:
Yields `true` if string `subject` starts with the string `prefix`.

Examples:
<screen>
import String;
startsWith("Hello.rsc", "Hell");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool startsWith(str subject, str prefix);


@doc{
Synopsis: Convert a character code into a string. 
}
@javaClass{org.rascalmpl.library.Prelude}
public java str stringChar(int char) throws IllegalCharacter(int character);

@doc{
Synopsis: Convert a list of character codes into a string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str stringChars(list[int] chars) throws IllegalCharacter(int character);

@doc{
Synopsis: Check that a given integer value is a valid Unicode code point.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isValidCharacter(int ch);

@doc{
Synopsis: Extract a substring from a string value.

Description:
# Yields substring of string `s` from index `begin` to the end of the string.
# Yields substring of string `s` from index `begin` to (but not including) index `end`.

Examples:
<screen>
import String;
substring("abcdef", 2);
substring("abcdef", 2, 4);
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str substring(str s, int begin);

@javaClass{org.rascalmpl.library.Prelude}
public java str substring(str s, int begin, int end);


@doc{
Synopsis: Convert a string value to integer.

Description:
# Converts string `s` to integer. 
# Convert string `s` to integer using radix `r`.


Throws `IllegalArgument` when `s` cannot be converted.

Examples:
<screen errors>
import String;
toInt("11");
toInt("11", 8);
// Now try an erroneous argument:
toInt("abc");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java int toInt(str s) throws IllegalArgument;

@javaClass{org.rascalmpl.library.Prelude}
public java int toInt(str s, int r) throws IllegalArgument;

@doc{
Synopsis: Convert the characters in a string value to lower case.

Description:
Convert all characters in string `s` to lowercase. Also see [toUpperCase].

Examples:
<screen>
import String;
toLowerCase("AaBbCc123");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toLowerCase(str s);


@doc{
Synopsis: Convert a string value to real.

Description:
Converts string `s` to a real. Throws `IllegalArgument` when `s` cannot be converted.

Examples:
<screen errors>
import String;
toReal("2.5e-3");
toReal("123");
toReal("abc");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java real toReal(str s);

@doc{
Synopsis:Convert the characters in a string value to upper case.

Description:
Converts all characters in string `s` to upper case.

Also see [toLowerCase].

Examples:
<screen>
import String;
toUpperCase("AaBbCc123");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toUpperCase(str s);

@doc{
Synopsis: Returns string with leading and trailing whitespace removed.

Examples:
<screen>
import String;
trim("  jelly
beans  ");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str trim(str s);

@doc{
Synopsis: Squeeze repeated occurrences of characters.
Description: 
Squeeze repeated occurrences in `src` of characters in `charSet` removed.
See [Apache](http://commons.apache.org/lang/api-2.6/index.html?org/apache/commons/lang/text/package-summary.html)
for the allowed syntax in `charSet`.

Examples:
<screen>
import String;
squeeze("hello", "el");
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
public java str squeeze(str src, str charSet);


@doc{
Synopsis: Split a string into a list of strings based on a literal separator.
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] split(str sep, str src);

@javaClass{org.rascalmpl.library.Prelude}
public java str capitalize(str src);

@javaClass{org.rascalmpl.library.Prelude}
public java str uncapitalize(str src);

@doc{
Synopsis: word wrap a string to fit in a certain width.
Description:
  Inserts newlines in a string in order to fit the string 
  in a certain width. It only breaks on spaces (' '). 
}
@javaClass{org.rascalmpl.library.Prelude}
public java str wrap(str src, int wrapLength);


/* 
 * Return string of length n, with s placed according to dir (left/center/right) and padded with pad.
 * Used to implement:left, center and right above.
 */ 
@javaClass{org.rascalmpl.library.Prelude}
private java str format(str s, str dir, int n, str pad);

@doc{
Synopsis: Determine if a string matches the given (Java-syntax) regular expression.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool rexpMatch(str s, str re);


