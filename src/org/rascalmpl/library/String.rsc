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

@synopsis{Library functions for strings.}
@description{
The following library functions are defined for strings:
(((TOC)))
}
module String

extend Exception;
import List;


@synopsis{Center a string in given space.}
@description{
*  Center string `s` in string of length `n` using spaces.
*  Center string `s` in string of length `n` using `pad` as padding character.
}
@examples{
```rascal-shell
import String;
center("abc", 10);
center("abc", 10, "x");
```
}

public str center(str s, int n)
{
  return format(s, "center", n, " ");
}

public str center(str s, int n, str pad)
{
  return format(s, "center", n, pad);
}


@synopsis{Return character in a string by its index position.}
@description{
Return the character at position `i` in string `s` as integer character code.
Also see ((String-stringChar)) that converts character codes back to string.
}
@examples{
```rascal-shell
import String;
charAt("abc", 0);
stringChar(charAt("abc", 0));
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int charAt(str s, int i) throws IndexOutOfBounds;


@synopsis{Return characters of a string.}
@description{
Return a list of the characters of `s` as integer character codes.
Also see ((String-stringChars)) that converts character codes back to string.
}
@examples{
```rascal-shell
import String;
chars("abc");
stringChars(chars("abc")) == "abc";
```
}
public list[int] chars(str s) = [ charAt(s,i) | i <- [0..size(s)]];


@synopsis{Check that a string contains another string.}
@description{
Check whether the string `find` occurs as substring in the string `subject`.
}
@examples{
```rascal-shell
import String;
contains("abracadabra", "bra");
contains("abracadabra", "e");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool contains(str input, str find);


@synopsis{Replace escaped characters by the escaped character itself (using Rascal escape conventions).}
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


@synopsis{Check whether a string ends with a given substring.}
@description{
Yields `true` if string `subject` ends with the string `suffix`.
}
@examples{
```rascal-shell
import String;
endsWith("Hello.rsc", ".rsc");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool endsWith(str subject, str suffix);


@synopsis{Replace single characters in a string.}
@description{
Return a copy of `subject` in which each single character key in replacements
has been replaced by its associated value.
}
@examples{
```rascal-shell
import String;
import IO;
escape("abracadabra", ("a" : "AA", "c" : "C"));
L = escape("\"Good Morning\", he said", ("\"": "\\\""));
println(L);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str escape(str subject, map[str,str] mapping);



@synopsis{Find all occurrences of a string in another string.}
@description{
Find all occurrences of string `find` in string `subject`.
The result is a (possible empty) list of positions where `find` matches.

See also ((findFirst)) and ((findLast)).
}
@examples{
```rascal-shell
import String;
findAll("abracadabra", "a");
findAll("abracadabra", "bra");
findAll("abracadabra", "e");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[int] findAll(str subject, str find);


@synopsis{Find the first occurrence of a string in another string.}
@description{
Find the first occurrence of string `find` in string `subject`.
The result is either a position in `subject` or `-1` when `find` is not found.

Also see ((findAll)) and ((findLast)).
}
@examples{
```rascal-shell
import String;
findFirst("abracadabra", "a");
findFirst("abracadabra", "bra");
findFirst("abracadabra", "e");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int findFirst(str subject, str find);


@synopsis{Find the last occurrence of a string in another string.}
@description{
Find the last occurrence of string `find` in string `subject`.
The result is either a position in `subject` or `-1` when `find` is not found.

Also see ((findAll)) and ((findFirst)).
}
@examples{
```rascal-shell
import String;
findLast("abracadabra", "a");
findLast("abracadabra", "bra");
findLast("abracadabra", "e");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int findLast(str subject, str find);


@synopsis{Check whether a string is empty.}
@description{
Returns `true` if string `s` is empty.
}
@examples{
```rascal-shell
import String;
isEmpty("");
isEmpty("abc");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(str s);


@synopsis{Generate a arbitrary string.}
@description{
Returns a string of maximum `n` length, with arbitrary characters.
}
@examples{
```rascal-shell
import String;
arbString(3);
arbString(10);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str arbString(int n);


@synopsis{Left alignment of string in given space.}
@description{
*  Left align string `s` in string of length `n` using spaces.
*  Left align string `s` in string of length `n` using `pad` as pad character.
}
@examples{
```rascal-shell
import String;
left("abc", 10);
left("abc", 10, "x");
```
}
public str left(str s, int n)
{
  return format(s, "left", n, " ");
}

public str left(str s, int n, str pad)
{
  return format(s, "left", n, pad);
}


@synopsis{Replace all occurrences of a string in another string.}
@description{
Return a copy of `subject` in which all occurrences of `find` (if any) have been replaced by `replacement`.
Also see ((replaceFirst)) and ((replaceLast)).
}
@examples{
```rascal-shell
import String;
replaceAll("abracadabra", "a", "A");
replaceAll("abracadabra", "ra", "RARA");
replaceAll("abracadabra", "cra", "CRA");
```
}
@pitfalls{
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceAll(str subject, str find, str replacement);


@synopsis{Replace the first occurrence of a string in another string.}
@description{
Return a copy of `subject` in which the first occurrence of `find` (if it exists) has been replaced by `replacement`.
Also see ((replaceAll)) and ((replaceLast)).
}
@examples{
```rascal-shell
import String;
replaceFirst("abracadabra", "a", "A");
replaceFirst("abracadabra", "ra", "RARA");
replaceFirst("abracadabra", "cra", "CRA");
```
}
@pitfalls{
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceFirst(str subject, str find, str replacement);


@synopsis{Replace the last occurrence of a string in another string.}
@description{
Return a copy of `subject` in which the last occurrence of `find` (if it exists) has been replaced by `replacement`.
Also see ((replaceFirst)) and ((replaceLast)).
}
@examples{
```rascal-shell
import String;
replaceLast("abracadabra", "a", "A");
replaceLast("abracadabra", "ra", "RARA");
replaceLast("abracadabra", "cra", "CRA");
```
}
@pitfalls{
Note that `find` is a string (as opposed to, for instance, a regular expression in Java).
}
@javaClass{org.rascalmpl.library.Prelude}
public java str replaceLast(str subject, str find, str replacement);


@synopsis{Return a string with all characters in reverse order.}
@description{
Returns string with all characters of string `s` in reverse order.
}
@examples{
```rascal-shell
import String;
reverse("abc");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str reverse(str s);



@synopsis{Right align s in string of length n using space.}
@examples{
```rascal-shell
import String;
right("abc", 10);
right("abc", 10, "x");
```
}

@synopsis{Right alignment of a string value in a given space.}
@description{
*  Right align string `s` in string of length `n` using spaces.
*  Right align string `s` in string of length `n` using `pad` as pad character.
}
@examples{
```rascal-shell
import String;
right("abc", 10);
right("abc", 10, "x");
```
}
public str right(str s, int n)
{
  return format(s, "right", n, " ");
}

public str right(str s, int n, str pad)
{
  return format(s, "right", n, pad);
}



@synopsis{Determine length of a string value.}
@description{
Returns the length (number of characters) in string `s`.
}
@examples{
```rascal-shell
import String;
size("abc");
size("");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int size(str s);


@synopsis{Check whether a string starts with a given prefix.}
@description{
Yields `true` if string `subject` starts with the string `prefix`.
}
@examples{
```rascal-shell
import String;
startsWith("Hello.rsc", "Hell");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool startsWith(str subject, str prefix);



@synopsis{Convert a character code into a string.}
@javaClass{org.rascalmpl.library.Prelude}
public java str stringChar(int char) throws IllegalArgument;


@synopsis{Convert a list of character codes into a string.}
@javaClass{org.rascalmpl.library.Prelude}
public java str stringChars(list[int] chars) throws IllegalArgument;


@synopsis{Check that a given integer value is a valid Unicode code point.}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isValidCharacter(int ch);


@synopsis{Extract a substring from a string value.}
@description{
*  Yields substring of string `s` from index `begin` to the end of the string.
*  Yields substring of string `s` from index `begin` to (but not including) index `end`.
}
@examples{
```rascal-shell
import String;
substring("abcdef", 2);
substring("abcdef", 2, 4);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str substring(str s, int begin);

@javaClass{org.rascalmpl.library.Prelude}
public java str substring(str s, int begin, int end);



@synopsis{Convert a string value to integer.}
@description{
*  Converts string `s` to integer. 
*  Convert string `s` to integer using radix `r`.


Throws `IllegalArgument` when `s` cannot be converted.
}
@examples{
```rascal-shell
import String;
toInt("11");
toInt("11", 8);
```
Now try an erroneous argument:
```rascal-shell,continue,error
toInt("abc");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java int toInt(str s) throws IllegalArgument;

@javaClass{org.rascalmpl.library.Prelude}
public java int toInt(str s, int r) throws IllegalArgument;


@synopsis{Convert the characters in a string value to lower case.}
@description{
Convert all characters in string `s` to lowercase. Also see ((toUpperCase)).
}
@examples{
```rascal-shell
import String;
toLowerCase("AaBbCc123");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toLowerCase(str s);



@synopsis{Convert a string value to real.}
@description{
Converts string `s` to a real. Throws `IllegalArgument` when `s` cannot be converted.
}
@examples{
```rascal-shell,error
import String;
toReal("2.5e-3");
toReal("123");
toReal("abc");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java real toReal(str s);


@synopsis{Convert the characters in a string value to upper case.}
@description{
Converts all characters in string `s` to upper case.

Also see ((toLowerCase)).
}
@examples{
```rascal-shell
import String;
toUpperCase("AaBbCc123");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str toUpperCase(str s);


@synopsis{Returns string with leading and trailing whitespace removed.}
@examples{
```rascal-shell
import String;
trim("  jelly
beans  ");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str trim(str s);


@synopsis{Squeeze repeated occurrences of characters.}
@description{
Squeeze repeated occurrences in `src` of characters in `charSet` removed.
See http://commons.apache.org/lang/api-2.6/index.html?org/apache/commons/lang/text/package-summary.html[Apache]
for the allowed syntax in `charSet`.
}
@examples{
```rascal-shell
import String;
squeeze("hello", "el");
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java str squeeze(str src, str charSet);



@synopsis{Split a string into a list of strings based on a literal separator.}
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


@synopsis{Word wrap a string to fit in a certain width.}
@description{
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


@synopsis{Determine if a string matches the given (Java-syntax) regular expression.}
@javaClass{org.rascalmpl.library.Prelude}
@deprecated{
use `/re/ := s` instead
}
public java bool rexpMatch(str s, str re);


@synopsis{Convert a string value to a (source code) location.}
@description{
*  Converts string `s` to a location.
*  If the scheme is not provided, it is assumed to be `cwd`.
}
@examples{
```rascal-shell
import String;
toLocation("http://grammarware.net");
toLocation("document.xml");
```
}
@deprecated{Use ((Locations::fromOSPath))}
public loc toLocation(str s) = (/<car:.*>\:\/\/<cdr:.*>/ := s) ? |<car>://<cdr>| : |cwd:///<s>|;


@synopsis{Substitute substrings in a string based on a substitution map from location to string.}
@examples{
```rascal-shell
import String;
substitute("abc", (|stdin:///|(1,1): "d"))
```
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
