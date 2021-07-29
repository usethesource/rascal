@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}
@doc{
.Synopsis
Library functions for input/output.

.Description

The following input/output functions are defined:
loctoc::[1]
}
module IO

import Exception;

@doc{
.Synopsis
register a logical file scheme including the resolution method via a table.

.Description

Logical source location schemes, such as `|java+interface://JRE/java/util/List|` are used for
precise qualified names of artifacts while abstracting from their physical location in a specific part
of a file on disk or from some webserver or source repository location.

Using this function you can create your own schemes. The authority field is used for scoping the 
names you wish to resolve to certain projects. This way one name can resolve to different locations 
in different projects.


.Benefits

*  Logical source locations are supported by IDE features such as hyperlinks
*  Logical source locations are supported by all IO functions as well

.Pitfalls

*  repeated calls to registerLocations for the same `scheme` and `authority` will overwrite the `m` map.
*  the registry is an intentional memory leak; so make sure you use it wisely.
*  when the files references by the physical locations are being written to (edited, removed), then you
may expect problems. The registry is not automatically invalidated.
}
@javaClass{org.rascalmpl.library.Prelude}
java void registerLocations(str scheme, str authority, map[loc logical, loc physical] m);

@doc{
.Synopsis
undo the effect of [registerLocations]

.Description

For debugging or for memory management you may wish to remove a lookup table.
}
@javaClass{org.rascalmpl.library.Prelude}
java void unregisterLocations(str scheme, str authority);

@javaClass{org.rascalmpl.library.Prelude}
java loc resolveLocation(loc l);

@doc{
.Synopsis
Append a value to a file.

.Description
Append a textual representation of some values to an existing or a newly created file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

.Encoding

The existing file can be stored using any character set possible, if you know the character set, please use <<appendToFileEnc>>.
Else the same method of deciding the character set is used as in <<readFile>>.

.Pitfalls

*  The same encoding pitfalls as the <<readFile>> function.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void appendToFile(loc file, value V...)
throws PathNotFound, IO;

@doc{
.Synopsis
Append a value to a file.

.Description
Append a textual representation of some values to an existing or a newly created file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

Files are encoded using the charset provided.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void appendToFileEnc(loc file, str charset, value V...)
throws PathNotFound, IO;

@doc{
.Synopsis
Returns all available character sets.
}
@javaClass{org.rascalmpl.library.Prelude}
public java set[str] charsets();

@doc{
.Synopsis
Returns whether this charset can be used for encoding (use with <<writeFile>>)
}
@javaClass{org.rascalmpl.library.Prelude}
public java set[str] canEncode(str charset);


@doc{
.Synopsis
Print a value and return true.

.Description
Print a value and return `true`. This is useful for debugging complex Boolean expressions or comprehensions.
The only difference between this function and <<IO-println>> is that its return type is `bool` rather than `void`.

.Examples
[source,rascal-shell]
----
import IO;
bprintln("Hello World");
----
}
public bool bprintln(value arg) 
{
  println(arg);
  return true;
}

@doc{
.Synopsis
Check whether a given location exists.

.Description
Check whether a certain location exists, i.e., whether an actual file is associated with it.

.Examples

[source,rascal-shell]
----
import IO;
----
Does the library file `IO.rsc` exist?
[source,rascal-shell,continue]
----
exists(|std:///IO.rsc|);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool exists(loc file);


@doc{
.Synopsis
Find a named file in a list of locations.

.Examples

[source,rascal-shell]
----
import IO;
----
Find the file `IO.rsc` in the standard library:
[source,rascal-shell,continue]
----
find("IO.rsc", [|std:///|]);
----
}
public loc find(str name, list[loc] path) throws PathNotFound {
  if (dir <- path, f := dir + "/<name>", exists(f)) { 
    return f;
  }
  throw PathNotFound({dir + "/<name>" | dir <- path});
}

@doc{
.Synopsis
Check whether a given location is a directory.

.Description
Check whether the location `file` is a directory.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isDirectory(loc file);

@doc{
.Synopsis
Print an indented representation of a value.

.Description
See <<IO-iprintExp>> for a version that returns its argument as result
and <<IO-iprintln>> for a version that adds a newline
and <<IO-iprintToFile>> for a version that prints to a file.

.Examples

[source,rascal-shell]
----
import IO;
iprint(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java void iprint(value arg, int lineLimit = 1000); 

@doc{
.Synopsis
Print an indented representation of a value to the specified location.

.Description
See <<IO-iprint>> for a version that displays the result on the console
and <<IO-iprintExp>> for a version that returns its argument as result
and <<IO-iprintln>> for a version that adds a newline.

.Examples

[source,rascal-shell]
----
import IO;
iprintToFile(|file:///tmp/fruits.txt|, ["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java void iprintToFile(loc file, value arg); 

@javaClass{org.rascalmpl.library.Prelude}
public java str iprintToString(value arg);

@doc{
.Synopsis
Print an indented representation of a value and returns the value as result.

.Description
See <<IO-iprintlnExp>> for a version that adds a newline.

.Examples

[source,rascal-shell]
----
import IO;
iprintExp(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
----
}
public &T iprintExp(&T v) {
	iprint(v);
	return v;
}

@doc{
.Synopsis
Print an indented representation of a value followed by a newline and returns the value as result.

.Description
See <<IO-iprintExp>> for a version that does not add a newline.

.Examples

[source,rascal-shell]
----
import IO;
iprintlnExp(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
----
}
public &T iprintlnExp(&T v) {
	iprintln(v);
	return v;
}


@doc{
.Synopsis
Print a indented representation of a value and add a newline at the end.

.Description
See <<IO-iprintlnExp>> for a version that returns its argument as result
and <<IO-iprint>> for a version that does not add a newline.

By default we only print the first 1000 lines, if you want to print larger values, either 
use <<ValueIO-writeTextValueFile>> or change the limit with the lineLimit parameter.

.Examples

[source,rascal-shell]
----
import IO;
iprintln(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
iprintln([ {"hi"} | i <- [0..1000]], lineLimit = 10);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java void iprintln(value arg, int lineLimit = 1000); 

@doc{
.Synopsis
Check whether a given location is actually a file (and not a directory).

.Description
Check whether location `file` is actually a file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isFile(loc file);


@doc{
.Synopsis
Last modification date of a location.

.Description
Returns last modification time of the file at location `file`.

.Examples
[source,rascal-shell]
----
import IO;
----
Determine the last modification date of the Rascal standard library:
[source,rascal-shell,continue]
----
lastModified(|std:///IO.rsc|);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime lastModified(loc file);

@doc{
.Synopsis
Creation datetime of a location.

.Description
Returns the creation time of the file at location `file`.

.Examples
[source,rascal-shell]
----
import IO;
----
Determine the last modification date of the Rascal standard library:
[source,rascal-shell,continue]
----
created(|std:///IO.rsc|);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime created(loc file);


@doc{
.Synopsis
Set the modification date of a file to `now` or create the file if it did not exist yet
 }
@javaClass{org.rascalmpl.library.Prelude}
java void touch(loc file);

@doc{ 
.Synopsis
Set the modification date of a file to the timestamp
 }
@javaClass{org.rascalmpl.library.Prelude}
java void setLastModified(loc file, datetime timestamp);

@doc{
.Synopsis
List the entries in a directory.

.Description
List the entries in directory `file`.

.Examples

[source,rascal-shell,error]
----
import IO;
----
List all entries in the standard library:
[source,rascal-shell,continue,error]
----
listEntries(|std:///|);
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] listEntries(loc file);


@doc{
.Synopsis
Create a new directory.

.Description
Create a directory at location `file`.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void mkDirectory(loc file)
throws PathNotFound, IO;

@doc{
.Synopsis
Print a value without subsequent newline.

.Description
Print a value on the output stream.
See <<IO-println>> for a version that adds a newline
and <<IO-printExp>> for a version that returns its argument as value.


.Examples

Note that the only difference with <<IO-println>> is that no newline is added after the value is printed
[source,rascal-shell]
----
import IO;
print("Hello World");
----

NOTE: Since `print` does not add a newline, the prompt `ok` appears at a weird place, i.e., 
glued to the output of `print`.
}

@javaClass{org.rascalmpl.library.Prelude}
public java void print(value arg);

@doc{
.Synopsis
Print a value and return it as result.

.Examples
[source,rascal-shell]
----
import IO;
printExp(3.14);
printExp("The value of PI is approximately ", 3.14);
----
}
public &T printExp(&T v) {
	print("<v>");
	return v;
}

public &T printExp(str msg, &T v) {
	print("<msg><v>");
	return v;
}

@doc{
.Synopsis
Print a value to the output stream and add a newline.

.Description
Print a value on the output stream followed by a newline.
See <<IO-print>> for a version that does not add a newline
and <<IO-printlnExp>> for a version that returns its argument as value.

.Examples
[source,rascal-shell]
----
import IO;
println("Hello World");
----
Introduce variable S and print it:
[source,rascal-shell,continue]
----
S = "Hello World";
println(S);
----
Introduce variable L and print it:
[source,rascal-shell,continue]
----
L = ["a", "b", "c"];
println(L);
----
Use a string template to print several values:
[source,rascal-shell,continue]
----
println("<S>: <L>");
----
Just print a newline
[source,rascal-shell,continue]
----
println();
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java void println(value arg);

@javaClass{org.rascalmpl.library.Prelude}
public java void println();

@doc{
.Synopsis
Print a value followed by a newline and return it as result.

.Examples
[source,rascal-shell]
----
import IO;
printlnExp(3.14);
printlnExp("The value of PI is approximately ", 3.14);
----
NOTE: Since `printExp` does no produce a newline after its output, the result prompt `real: 3.14` is glued to the
output of `printExp`.
}
public &T printlnExp(&T v) {
	println("<v>");
	return v;
}

public &T printlnExp(str msg, &T v) {
	println("<msg><v>");
	return v;
}

@doc{
.Synopsis
Raw print of a value.

.Description


.Pitfalls
This function is only available for internal use in the Rascal development team.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void rprint(value arg);

    
@doc{
.Synopsis
Raw print of a value followed by newline.

.Description

.Pitfalls
This function is only available for internal use in the Rascal development team.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void rprintln(value arg);

@doc{
.Synopsis
Read the contents of a location and return it as string value.

.Description
Return the contents of a file location as a single string.
Also see <<readFileLines>>.

.Encoding

A text file can be encoded in many different character sets, most common are UTF8, ISO-8859-1, and ASCII.
If you know the encoding of the file, please use the <<readFileEnc>> and <<readFileLinesEnc>> overloads.
If you do not know, we try to detect this. This detection is explained below:

*  If the implementation of the used scheme in the link:/Rascal#Values-Location[location] 
   (e.g.,`|project:///|`) defines the charset of the file then this is used.
*  Otherwise if the file contains a UTF8/16/32 http://en.wikipedia.org/wiki/Byte_order_mark[BOM], 
   then this is used.
*  As a last resort the IO library uses heuristics to determine if UTF-8 or UTF-32 could work:
   **  Are the first 32 bytes valid UTF-8? Then use UTF-8.
   **  Are the first 32 bytes valid UTF-32? Then use UTF-32.
*  Finally, we fall back to the system default (as given by the Java Runtime Environment).

*To summarize*, we use UTF-8 by default, except if the link:/Rascal#Values-Location[location] has available meta-data, the file contains a BOM, or
the first 32 bytes of the file are not valid UTF-8.

.Pitfalls

*  The second version of `readFile` with a string argument is __deprecated__.
*  In case encoding is not known, we try to estimate as best as we can.
*  We default to UTF-8, if the file was not encoded in UTF-8 but the first characters were valid UTF-8, 
  you might get an decoding error or just strange looking characters.

}
@javaClass{org.rascalmpl.library.Prelude}
public java str readFile(loc file)
throws PathNotFound, IO;

@doc{
.Synopsis
Read the contents of a location and return it as string value.

.Description
Return the contents (decoded using the Character set supplied) of a file location as a single string.
Also see <<readFileLinesEnc>>.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str readFileEnc(loc file, str charset)
throws PathNotFound, IO;

@javaClass{org.rascalmpl.library.Prelude}
public java str uuencode(loc file)
throws PathNotFound, IO;


@doc{
.Synopsis
Read the contents of a file and return it as a list of bytes.
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[int] readFileBytes(loc file)
throws PathNotFound, IO;


@doc{
.Synopsis
Read the contents of a file location and return it as a list of strings.

.Description
Return the contents of a file location as a list of lines.
Also see <<readFile>>.

.Encoding 

Look at <<readFile>> to understand how this function chooses the character set. If you know the character set used, please use <<readFileLinesEnc>>.

.Pitfalls

*  In case encoding is not known, we try to estimate as best as we can (see [readFile]).
*  We default to UTF-8, if the file was not encoded in UTF-8 but the first characters were valid UTF-8, 
  you might get an decoding error or just strange looking characters (see <<readFile>>).
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] readFileLines(loc file)
throws PathNotFound, IO;

@doc{
.Synopsis
Read the contents of a file location and return it as a list of strings.

.Description
Return the contents (decoded using the Character set supplied) of a file location as a list of lines.
Also see <<readFileLines>>.
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] readFileLinesEnc(loc file, str charset)
throws PathNotFound, IO;


@javaClass{org.rascalmpl.library.Prelude}
public java void remove(loc file, bool recursive=true) throws IO;

@doc{
.Synopsis
Write values to a file.

.Description
Write a textual representation of some values to a file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

Files are encoded in UTF-8, in case this is not desired, use <<writeFileEnc>>.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeFile(loc file, value V...)
throws PathNotFound, IO;

@doc{
.Synopsis
Write a list of bytes to a file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeFileBytes(loc file, list[int] bytes)
throws PathNotFound, IO;

@doc{
.Synopsis
Write values to a file.

.Description
Write a textual representation of some values to a file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

Files are encoded using the charset provided.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeFileEnc(loc file, str charset, value V...)
throws PathNotFound, IO;

@doc{
.Synopsis
Read the contents of a location and return its MD5 hash.

.Description
MD5 hash the contents of a file location.
}

@javaClass{org.rascalmpl.library.Prelude}
public java str md5HashFile(loc file)
throws PathNotFound, IO;

@javaClass{org.rascalmpl.library.Prelude}
public java str md5Hash(value v);

@javaClass{org.rascalmpl.library.Prelude}
public java str createLink(str title, str target);


@javaClass{org.rascalmpl.library.Prelude}
public java str toBase64(loc file)
throws PathNotFound, IO;

@javaClass{org.rascalmpl.library.Prelude}
java void copy(loc source, loc target, bool recursive=false, bool overwrite=true) throws IO;

@deprecated{use the `copy` function instead}
void copyFile(loc source, loc target) {
  copy(source, target, recursive=false, overwrite=true);
}

@deprecated{use the `copy` function instead}
void copyDirectory(loc source, loc target) {
  copy(source, target, recursive=true, overwrite=true);
}

@javaClass{org.rascalmpl.library.Prelude}
java void move(loc source, loc target, bool overwrite=true) throws IO;

@javaClass{org.rascalmpl.library.Prelude}
java loc arbLoc();

data LocationChangeEvent
    = changeEvent(loc src, LocationChangeType changeType, LocationType \type);

data LocationChangeType
    = created() 
	| deleted() 
	| modified();

data LocationType
    = file() 
	| directory();

@javaClass{org.rascalmpl.library.Prelude}
java void watch(loc src, bool recursive, void (LocationChangeEvent event) watcher);

@javaClass{org.rascalmpl.library.Prelude}
java void unwatch(loc src, bool recursive, void (LocationChangeEvent event) watcher);
