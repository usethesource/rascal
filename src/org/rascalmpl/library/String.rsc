@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@contributor{Vadim Zaytsev - vadim@grammarware.net - SWAT, CWI}
@doc{
.Synopsis
Library functions for strings.

.Description

For operators on strings see [String]((Rascal:Values-String)) in the Rascal Language Reference.

The following functions are defined for strings:
(((TOC)))
}
module String

extend Exception;
import List;

@doc{
.Synopsis
Center a string in given space.

.Description

*  Center string `s` in string of length `n` using spaces.
*  Center string `s` in string of length `n` using `pad` as padding character.

.Examples
[source,rascal-shell]
----
import String;
center("abc", 10);
center("abc", 10, "x");
----
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
.Synopsis
Return character in a string by its index position.

.Description
Return the character at position `i` in string `s` as integer character code.
Also see ((String-stringChar)) that converts character codes back to string.

.Examples
[source,rascal-shell]
----
import String;
charAt("abc", 0);
stringChar(charAt("abc", 0));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java int charAt(str s, int i) throws IndexOutOfBounds;

@doc{
.Synopsis
Return characters of a string.
.Description
Return a list of the characters of `s` as integer character codes.
Also see ((String-stringChars)) that converts character codes back to string.

.Examples
[source,rascal-shell]
----
import String;
chars("abc");
stringChars(chars("abc")) == "abc";
----
}
public list[int] chars(str s) = [ charAt(s,i) | i <- [0..size(s)]];

@doc{
.Synopsis
Check that a string contains another string.

.Description
Check whether the string `find` occurs as substring in the string `subject`.

.Examples
[source,rascal-shell]
----
import String;
contains("abracadabra", "bra");
contains("abracadabra", "e");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool contains(str input, str find);

@doc{
.Synopsis
Replace escaped characters by the escaped character itself (using Rascal escape conventions).
}
str deescape(str s)  {
    res = visit(s) { 
        case /^\\<c: [\" \' \< \> \\]>/ => c
        case /^\\t/ => "\t"
        case /^\\n/ => "\n"
        case /^\\u<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
        case /^\\U<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
        case /^\\a<hex:[0-7][0-9a-fA-F]>/ => stringChar(toInt("0x<hex>"))
        }; 
    return res;
}

@doc{
.Synopsis
Check whether a string ends with a given substring.

.Description
Yields `true` if string `subject` ends with the string `suffix`.

.Examples
[source,rascal-shell]
----
import String;
endsWith("Hello.rsc", ".rsc");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool endsWith(str subject, str suffix);

@doc{
.Synopsis
Replace single characters in a string.

.Description
Return a copy of `subject` in which each single character key in replacements
has been replaced by its associated value.

.Examples
[source,rascal-shell]
----
import String;
import IO;
escape("abracadabra", ("a" : "AA", "c" : "C"));
L = escape("\"Good Morning\", he said", ("\"": "\\\""));
println(L);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str escape(str subject, map[str,str] mapping);


@doc{
.Synopsis
Find all occurrences of a string in another string.

.Description
Find all occurrences of string `find` in string `subject`.
The result is a (possible empty) list of positions where `find` matches.

See also ((findFirst)) and ((findLast)).

.Examples
[source,rascal-shell]
----
import String;
findAll("abracadabra", "a");
findAll("abracadabra", "bra");
findAll("abracadabra", "e");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[int] findAll(str subject, str find);

@doc{
.Synopsis
Find the first occurrence of a string in another string.

.Description
Find the first occurrence of string `find` in string `subject`.
The result is either a position in `subject` or `-1` when `find` is not found.

Also see ((findAll)) and ((findLast)).

.Examples
[source,rascal-shell]
----
import String;
findFirst("abracadabra", "a");
findFirst("abracadabra", "bra");
findFirst("abracadabra", "e");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java int findFirst(str subject, str find);

@doc{
.Synopsis
Find the last occurrence of a string in another string.

.Description
Find the last occurrence of string `find` in string `subject`.
The result is either a position in `subject` or `-1` when `find` is not found.

Also see ((findAll)) and ((findFirst)).

.Examples
[source,rascal-shell]
----
import String;
findLast("abracadabra", "a");
findLast("abracadabra", "bra");
findLast("abracadabra", "e");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java int findLast(str subject, str find);

@doc{
.Synopsis
Check whether a string is empty.

.Description
Returns `true` if string `s` is empty.

.Examples
[source,rascal-shell]
----
import String;
isEmpty("");
isEmpty("abc");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(str s);

@doc{
.Synopsis
Generate a arbitrary string.

.Description
Returns a string of maximum `n` length, with arbitrary characters.

.Examples
[source,rascal-shell]
----
import String;
arbString(3);
arbString(10);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str arbString(int n);

@doc{
.Synopsis
Left alignment of string in given space.

.Description

*  Left align string `s` in string of length `n` using spaces.
*  Left align string `s` in string of length `n` using `pad` as pad character.

.Examples
[source,rascal-shell]
----
import String;
left("abc", 10);
left("abc", 10, "x");
----
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
.Synopsis
Replace all occurrences of a string in another string.

.Description
Return a copy of `subject` in which all occurrences of `find` (if any) have been replaced by `replacement`.
Also see ((replaceFirst)) and ((replaceLast)).

.Examples
[source,rascal-shell]
----
import String;
replaceAll("abracadabra", "a", "A");
replaceAll("abracadabra", "ra", "RARA");
replaceAll("abracadabra", "cra", "CRA");
----

.Pitfalls
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).

}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceAll(str subject, str find, str replacement);

@doc{
.Synopsis
Replace the first occurrence of a string in another string.

.Description
Return a copy of `subject` in which the first occurrence of `find` (if it exists) has been replaced by `replacement`.
Also see ((replaceAll)) and ((replaceLast)).

.Examples
[source,rascal-shell]
----
import String;
replaceFirst("abracadabra", "a", "A");
replaceFirst("abracadabra", "ra", "RARA");
replaceFirst("abracadabra", "cra", "CRA");
----

.Pitfalls
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceFirst(str subject, str find, str replacement);

@doc{
.Synopsis
Replace the last occurrence of a string in another string.

.Description
Return a copy of `subject` in which the last occurrence of `find` (if it exists) has been replaced by `replacement`.
Also see ((replaceFirst)) and ((replaceLast)).

.Examples
[source,rascal-shell]
----
import String;
replaceLast("abracadabra", "a", "A");
replaceLast("abracadabra", "ra", "RARA");
replaceLast("abracadabra", "cra", "CRA");
----

.Pitfalls
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceLast(str subject, str find, str replacement);

@doc{
.Synopsis
Return a string with all characters in reverse order.

.Description
Returns string with all characters of string `s` in reverse order.

.Examples
[source,rascal-shell]
----
import String;
reverse("abc");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str reverse(str s);


@doc{
.Synopsis
Right align s in string of length n using space.

.Examples
[source,rascal-shell]
----
import String;
right("abc", 10);
right("abc", 10, "x");
----
}
@doc{
.Synopsis
Right alignment of a string value in a given space.

.Description

*  Right align string `s` in string of length `n` using spaces.
*  Right align string `s` in string of length `n` using `pad` as pad character.

.Examples
[source,rascal-shell]
----
import String;
right("abc", 10);
right("abc", 10, "x");
----
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
.Synopsis
Determine length of a string value.

.Description
Returns the length (number of characters) in string `s`.

.Examples
[source,rascal-shell]
----
import String;
size("abc");
size("");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java int size(str s);

@doc{
.Synopsis
Check whether a string starts with a given prefix.

.Description
Yields `true` if string `subject` starts with the string `prefix`.

.Examples
[source,rascal-shell]
----
import String;
startsWith("Hello.rsc", "Hell");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool startsWith(str subject, str prefix);


@doc{
.Synopsis
Convert a character code into a string. 
}
@javaClass{org.rascalmpl.library.Prelude}
public java str stringChar(int char) throws IllegalArgument;

@doc{
.Synopsis
Convert a list of character codes into a string.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str stringChars(list[int] chars) throws IllegalArgument;

@doc{
.Synopsis
Check that a given integer value is a valid Unicode code point.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isValidCharacter(int ch);

@doc{
.Synopsis
Extract a substring from a string value.

.Description

*  Yields substring of string `s` from index `begin` to the end of the string.
*  Yields substring of string `s` from index `begin` to (but not including) index `end`.

.Examples
[source,rascal-shell]
----
import String;
substring("abcdef", 2);
substring("abcdef", 2, 4);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str substring(str s, int begin);

@javaClass{org.rascalmpl.library.Prelude}
public java str substring(str s, int begin, int end);


@doc{
.Synopsis
Convert a string value to integer.

.Description

*  Converts string `s` to integer. 
*  Convert string `s` to integer using radix `r`.


Throws `IllegalArgument` when `s` cannot be converted.

.Examples
[source,rascal-shell,error]
----
import String;
toInt("11");
toInt("11", 8);
----
Now try an erroneous argument:
[source,rascal-shell,continue,error]
----
toInt("abc");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java int toInt(str s) throws IllegalArgument;

@javaClass{org.rascalmpl.library.Prelude}
public java int toInt(str s, int r) throws IllegalArgument;

@doc{
.Synopsis
Convert the characters in a string value to lower case.

.Description
Convert all characters in string `s` to lowercase. Also see ((toUpperCase)).

.Examples
[source,rascal-shell]
----
import String;
toLowerCase("AaBbCc123");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toLowerCase(str s);


@doc{
.Synopsis
Convert a string value to real.

.Description
Converts string `s` to a real. Throws `IllegalArgument` when `s` cannot be converted.

.Examples
[source,rascal-shell,error]
----
import String;
toReal("2.5e-3");
toReal("123");
toReal("abc");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java real toReal(str s);

@doc{
.Synopsis
Convert the characters in a string value to upper case.

.Description
Converts all characters in string `s` to upper case.

Also see ((toLowerCase)).

.Examples
[source,rascal-shell]
----
import String;
toUpperCase("AaBbCc123");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toUpperCase(str s);

@doc{
.Synopsis
Returns string with leading and trailing whitespace removed.

.Examples
[source,rascal-shell]
----
import String;
trim("  jelly
beans  ");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str trim(str s);

@doc{
.Synopsis
Squeeze repeated occurrences of characters.
.Description
Squeeze repeated occurrences in `src` of characters in `charSet` removed.
See http://commons.apache.org/lang/api-2.6/index.html?org/apache/commons/lang/text/package-summary.html[Apache]
for the allowed syntax in `charSet`.

.Examples
[source,rascal-shell]
----
import String;
squeeze("hello", "el");
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str squeeze(str src, str charSet);


@doc{
.Synopsis
Split a string into a list of strings based on a literal separator.
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] split(str sep, str src);

@javaClass{org.rascalmpl.library.Prelude}
public java str capitalize(str src);

@javaClass{org.rascalmpl.library.Prelude}
public java str uncapitalize(str src);

@javaClass{org.rascalmpl.library.Prelude}
public java str toBase64(str src);

@javaClass{org.rascalmpl.library.Prelude}
public java str fromBase64(str src);

@doc{
.Synopsis
Word wrap a string to fit in a certain width.

.Description
Inserts newlines in a string in order to fit the string in a certain width. It only breaks on spaces (' '). 
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
.Synopsis
Determine if a string matches the given (Java-syntax) regular expression.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool rexpMatch(str s, str re);

@doc{
.Synopsis
Convert a string value to a (source code) location.

.Description

*  Converts string `s` to a location.
*  If the scheme is not provided, it is assumed to be `cwd`.

.Examples
[source,rascal-shell]
----
import String;
toLocation("http://grammarware.net");
toLocation("document.xml");
----
}
public loc toLocation(str s) = (/<car:.*>\:\/\/<cdr:.*>/ := s) ? |<car>://<cdr>| : |cwd:///<s>|;

@doc{
.Synopsis
Substitute substrings in a string based on a substitution map from location to string.

.Examples
[source,rascal-shell]
----
import String;
substitute("abc", (|stdin:///|(1,1): "d"))
----
}
str substitute(str src, map[loc,str] s) { 
    int shift = 0;
    str subst1(str src, loc x, str y) {
        delta = size(y) - x.length;
        src = src[0..x.offset+shift] + y + src[x.offset+x.length+shift..];
        shift += delta;
        return src; 
    }
    order = sort([ k | k <- s ], bool(loc a, loc b) { return a.offset < b.offset; });
    return ( src | subst1(it, x, s[x]) | x <- order );
}
