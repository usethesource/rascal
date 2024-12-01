@license{
  Copyright (c) 2009-2022 CWI
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
@synopsis{Library functions for input/output.}
@description{
The following input/output functions are defined:
(((TOC)))
}
module IO

import Exception;

@synopsis{All functions in this module that have a charset parameter use this as default.}
private str DEFAULT_CHARSET = "UTF-8";

@synopsis{Register a logical file scheme including the resolution method via a table.}
@description{
Logical source location schemes, such as `|java+interface://JRE/java/util/List|` are used for
precise qualified names of artifacts while abstracting from their physical location in a specific part
of a file on disk or from some webserver or source repository location.

Using this function you can create your own schemes. The authority field is used for scoping the 
names you wish to resolve to certain projects. This way one name can resolve to different locations 
in different projects.
}
@benefits{
*  Logical source locations are supported by IDE features such as hyperlinks
*  Logical source locations are supported by all IO functions as well
}
@pitfalls{
* Repeated calls to registerLocations for the same `scheme` and `authority` will overwrite the `m` map.
* The registry is an intentional memory leak; so make sure you use it wisely. See also ((unregisterLocations)).
* When the files references by the physical locations are being written to (edited, removed), then you
may expect problems. The registry is not automatically invalidated.
}
@javaClass{org.rascalmpl.library.Prelude}
java void registerLocations(str scheme, str authority, map[loc logical, loc physical] m);

@synopsis{Undo the effect of ((registerLocations))}
@description{
For debugging or for memory management you may wish to remove a lookup table.
}
@javaClass{org.rascalmpl.library.Prelude}
java void unregisterLocations(str scheme, str authority);

@javaClass{org.rascalmpl.library.Prelude}
java loc resolveLocation(loc l);

@javaClass{org.rascalmpl.library.Prelude}
@synopsis{Finds files in the deployment locations of the configuration of the current runtime}
@description{
* For the interpreter this looks at the source path and finds any file in there.
* For the compiler this looks at the classpath and finds resources in there.  

The goal of `findResources` is to access additional files that are distributed
with a Rascal program. For example, the `index.html` file that comes with a server implemented
using ((util::Webserver)) would be located using this function. Such an additional file is
essential to the functioning of the given Rascal code.

There is a difference between Rascal programs under development and Rascal programs deployed
(in a jar file). The idea is that calls to this function _behave the same_ regardless of this 
mode. To make this true, the implementation of `findResources` makes the following
assumptions:
* In interpreter mode, the additional files are stored relative to the source roots of the current project. 
This means that all source roots are searched as configured for the current interpreter. It is possible
that multiple files match the query, hence the result set. Because jar files and target folders are typically
added to the source roots by the configuration code for Rascal's IDEs, this works out smoothly also for deployed projects.
* In compiled mode, the additional files are stored relative to the roots of the classpath. This 
means they are jar files and target folders of active projects. Your Maven project configuration should
take care to _copy_ all relevant resources from source folders to target folders and to package those
relative to the root of the jar for this to work in development mode. For deployment mode, the setup 
works through the resources API of the JVM that uses the Java ClassLoader API.
}
@benefits{
By satisfying the above two assumptions it is possible to write Rascal code that uses `findResources` that
is portable between interpreter and compiled mode, _and_ does not change between development
and deployed mode either.
}
@pitfalls{
* Avoid nesting resources in sub-folders in jar files (see Maven resources plugin) since nested files will not be found.
* Inside jar files the root for module names should be the same as for resources
* Inside source folders, the names of resources should be relative to the same root as the Rascal module names
* `findResources` searches exhaustively for all files with the given name; which may come at an expensive for projects with
large search paths. It makes sense to keep this in a constant module variable that is initialized only once.
* Name clashes are bound to happen. Prepare to, at least, throw a meaningful error message in the case of name clashes. E.g. `findResources("index.html")`
is _very_ likely to produce more than one result. Either choose more unique names, or filter the result in a meaningful manner,
or throw an exception that explains the situation to the user of your code. Specifically library project maintainers have to consider
this happenstance. The recommendation is to nest resources next to qualified module names, like so: if `lang/x/myLanguage/Syntax.rsc` 
is a module name, then `lang/x/myLanguage/examples/myExampleFile.mL` is an example resource location.
}
java set[loc] findResources(str fileName);
set[loc] findResources(loc path) = findResources(path.path) when path.scheme == "relative";

@synopsis{Search for a single resource instance, and fail if no or multiple instances are found}
@description{
This is a utility wrapper around ((findResources)).
It processes the result set of ((findResources)) to:
* return a singleton location if the `fileName`` was found.
* throw an IO exception if no instances of `fileName` was found.
* throw an IO exception if multiple instances of `fileName` were found.
}
@benefits{
* Save some code to unpack of the set that ((findResources)) produces.
}
@pitfalls{
* ((getResource)) searches for all instances in the entire run-time context of the current 
module. So if the search path (classpath) grows, new similar files may be added that match and this
function will start throwing IO exceptions. If you can influence the `fileName`, then make sure
to pick a name that is always going to be unique for the current project. 
}
loc getResource(str fileName) throws IO {
  result = findResources(fileName);

  switch (result) {
    case {}: 
      throw IO("<fileName> not found");
    case {loc singleton}: 
      return singleton;
    default:
      throw IO("<fileName> found more than once: <result>");
  }
}
 
@synopsis{Append a value to a file.}
@description{
Append a textual representation of some values to an existing or a newly created file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

The existing file can be stored using any character set possible, if you know the character set, please use ((appendToFileEnc)).
Else the same method of deciding the character set is used as in ((readFile)).
}
@pitfalls{
*  The same encoding pitfalls as the ((readFile)) function.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void appendToFile(loc file, value V..., str charset=DEFAULT_CHARSET, bool inferCharset=!(charset?))
throws PathNotFound, IO;

@synopsis{Append a value to a file.}
@description{
Append a textual representation of some values to an existing or a newly created file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

Files are encoded using the charset provided.
}
@deprecated{
Use `appendToFile(file, V, charset=DEFAULT_CHARSET)` instead.
}
public void appendToFileEnc(loc file, str charset, value V...) throws PathNotFound, IO
  = appendToFile(file, V, charset=charset, inferCharset=false);

@synopsis{Returns all available character sets.}
@javaClass{org.rascalmpl.library.Prelude}
public java set[str] charsets();

@synopsis{Returns whether this charset can be used for encoding (use with ((writeFile)))}
@javaClass{org.rascalmpl.library.Prelude}
public java set[str] canEncode(str charset);

@synopsis{Print a value and return true.}
@description{
Print a value and return `true`. This is useful for debugging complex Boolean expressions or comprehensions.
The only difference between this function and ((IO-println)) is that its return type is `bool` rather than `void`.
}
@examples{
```rascal-shell
import IO;
bprintln("Hello World");
```
}
public bool bprintln(value arg) 
{
  println(arg);
  return true;
}

@synopsis{Check whether a given location exists.}
@description{
Check whether a certain location exists, i.e., whether an actual file is associated with it.
}
@examples{
```rascal-shell
import IO;
```

Does the library file `IO.rsc` exist?
```rascal-shell,continue
exists(|std:///IO.rsc|);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool exists(loc file);


@synopsis{Find a named file in a list of locations.}
@examples{
```rascal-shell
import IO;
```
Find the file `IO.rsc` in the standard library:
```rascal-shell,continue
find("IO.rsc", [|std:///|]);
```
}
public loc find(str name, list[loc] path) throws PathNotFound {
  if (dir <- path, f := dir + "/<name>", exists(f)) { 
    return f;
  }
  throw PathNotFound({dir + "/<name>" | dir <- path});
}

@synopsis{Check whether a given location is a directory.}
@description{
Check whether the location `file` is a directory.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isDirectory(loc file);

@synopsis{Print an indented representation of a value.}
@description{
See ((IO-iprintExp)) for a version that returns its argument as result
and ((IO-iprintln)) for a version that adds a newline
and ((IO-iprintToFile)) for a version that prints to a file.
}
@examples{
```rascal-shell
import IO;
iprint(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java void iprint(value arg, int lineLimit = 1000); 

@synopsis{Print an indented representation of a value to the specified location.}
@description{
See ((IO-iprint)) for a version that displays the result on the console
and ((IO-iprintExp)) for a version that returns its argument as result
and ((IO-iprintln)) for a version that adds a newline.
}
@examples{
```rascal-shell
import IO;
iprintToFile(|file:///tmp/fruits.txt|, ["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java void iprintToFile(loc file, value arg, str charset=DEFAULT_CHARSET); 

@javaClass{org.rascalmpl.library.Prelude}
public java str iprintToString(value arg);


@synopsis{Print an indented representation of a value and returns the value as result.}
@description{
See ((IO-iprintlnExp)) for a version that adds a newline.
}
@examples{
```rascal-shell
import IO;
iprintExp(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
```
}
public &T iprintExp(&T v) {
	iprint(v);
	return v;
}


@synopsis{Print an indented representation of a value followed by a newline and returns the value as result.}
@description{
See ((IO-iprintExp)) for a version that does not add a newline.
}
@examples{
```rascal-shell
import IO;
iprintlnExp(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
```
}
public &T iprintlnExp(&T v) {
	iprintln(v);
	return v;
}



@synopsis{Print a indented representation of a value and add a newline at the end.}
@description{
See ((IO-iprintlnExp)) for a version that returns its argument as result
and ((IO-iprint)) for a version that does not add a newline.

By default we only print the first 1000 lines, if you want to print larger values, either 
use ((ValueIO-writeTextValueFile)) or change the limit with the lineLimit parameter.
}
@examples{
```rascal-shell
import IO;
iprintln(["fruits", ("spider" : 8, "snake" : 0), [10, 20, 30]]);
iprintln([ {"hi"} | i <- [0..1000]], lineLimit = 10);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java void iprintln(value arg, int lineLimit = 1000); 


@synopsis{Check whether a given location is actually a file (and not a directory).}
@description{
Check whether location `file` is actually a file.
}
@javaClass{org.rascalmpl.library.Prelude}
public java bool isFile(loc file);



@synopsis{Last modification date of a location.}
@description{
Returns last modification time of the file at location `file`.
}
@examples{
```rascal-shell
import IO;
```
Determine the last modification date of the Rascal standard library:
```rascal-shell,continue
lastModified(|std:///IO.rsc|);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime lastModified(loc file);


@synopsis{Creation datetime of a location.}
@description{
Returns the creation time of the file at location `file`.
}
@examples{
```rascal-shell
import IO;
```
Determine the last modification date of the Rascal standard library:
```rascal-shell,continue
created(|std:///IO.rsc|);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java datetime created(loc file);



@synopsis{Set the modification date of a file to `now` or create the file if it did not exist yet}
@javaClass{org.rascalmpl.library.Prelude}
java void touch(loc file);


@synopsis{Set the modification date of a file to the timestamp}
@javaClass{org.rascalmpl.library.Prelude}
java void setLastModified(loc file, datetime timestamp);


@synopsis{List the entries in a directory.}
@description{
List the entries in directory `file`.
}
@examples{
```rascal-shell
import IO;
```
List all entries in the standard library:
```rascal-shell,continue
listEntries(|std:///|);
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] listEntries(loc file);



@synopsis{Create a new directory.}
@description{
Create a directory at location `file`.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void mkDirectory(loc file)
throws PathNotFound, IO;


@synopsis{Print a value without subsequent newline.}
@description{
Print a value on the output stream.
See ((IO-println)) for a version that adds a newline
and ((IO-printExp)) for a version that returns its argument as value.
}
@examples{
Note that the only difference with ((IO-println)) is that no newline is added after the value is printed
```rascal-shell
import IO;
print("Hello World");
```

NOTE: Since `print` does not add a newline, the prompt `ok` appears at a weird place, i.e., 
glued to the output of `print`.
}

@javaClass{org.rascalmpl.library.Prelude}
public java void print(value arg);


@synopsis{Print a value and return it as result.}
@examples{
```rascal-shell
import IO;
printExp(3.14);
printExp("The value of PI is approximately ", 3.14);
```
}
public &T printExp(&T v) {
	print("<v>");
	return v;
}

public &T printExp(str msg, &T v) {
	print("<msg><v>");
	return v;
}


@synopsis{Print a value to the output stream and add a newline.}
@description{
Print a value on the output stream followed by a newline.
See ((IO-print)) for a version that does not add a newline
and ((IO-printlnExp)) for a version that returns its argument as value.
}
@examples{
```rascal-shell
import IO;
println("Hello World");
```
Introduce variable S and print it:
```rascal-shell,continue
S = "Hello World";
println(S);
```
Introduce variable L and print it:
```rascal-shell,continue
L = ["a", "b", "c"];
println(L);
```
Use a string template to print several values:
```rascal-shell,continue
println("<S>: <L>");
```
Just print a newline
```rascal-shell,continue
println();
```
}
@javaClass{org.rascalmpl.library.Prelude}
public java void println(value arg);

@javaClass{org.rascalmpl.library.Prelude}
public java void println();


@synopsis{Print a value followed by a newline and return it as result.}
@examples{
```rascal-shell
import IO;
printlnExp(3.14);
printlnExp("The value of PI is approximately ", 3.14);
```
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


@synopsis{Raw print of a value.}
@description{

}
@pitfalls{
This function is only available for internal use in the Rascal development team.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void rprint(value arg);

    

@synopsis{Raw print of a value followed by newline.}
@description{

}
@pitfalls{
This function is only available for internal use in the Rascal development team.
}
@javaClass{org.rascalmpl.library.Prelude}
public java void rprintln(value arg);


@synopsis{Read the contents of a location and return it as string value.}
@description{
Return the contents of a file location as a single string.
Also see ((readFileLines)).
}
@encoding{
A text file can be encoded in many different character sets, most common are UTF8, ISO-8859-1, and ASCII.
If you know the encoding of the file, please use the ((readFileEnc)) and ((readFileLinesEnc)) overloads.
If you do not know, we try to detect this. This detection is explained below:

*  If the implementation of the used scheme in the location
   (e.g.,`|project:///|`) defines the charset of the file then this is used.
*  Otherwise if the file contains a UTF8/16/32 [BOM](http://en.wikipedia.org/wiki/Byte_order_mark),
   then this is used.
*  As a last resort the IO library uses heuristics to determine if UTF-8 or UTF-32 could work:
   **  Are the first 32 bytes valid UTF-8? Then use UTF-8.
   **  Are the first 32 bytes valid UTF-32? Then use UTF-32.
*  Finally, we fall back to the system default (as given by the Java Runtime Environment).

*To summarize*, we use UTF-8 by default, except if the text that the location points to has available meta-data, the file contains a BOM, or
the first 32 bytes of the file are not valid UTF-8.
}
@pitfalls{
*  In case encoding is not known, we try to estimate as best as we can.
*  We default to UTF-8, if the file was not encoded in UTF-8 but the first characters were valid UTF-8, 
  you might get an decoding error or just strange looking characters.
}
@javaClass{org.rascalmpl.library.Prelude}
public java str readFile(loc file, str charset=DEFAULT_CHARSET, bool inferCharset=!(charset?))
throws PathNotFound, IO;

@synopsis{Read the contents of a location and return it as string value.}
@description{
Return the contents (decoded using the Character set supplied) of a file location as a single string.
Also see ((readFileLinesEnc)).
}
@deprecated{
Use `readFile(file, inferCharset=false, charset=DEFAULT_CHARSET)` instead.
}
public str readFileEnc(loc file, str charset) throws PathNotFound, IO
  = readFile(file, inferCharset=false, charset=charset);

@synopsis{Read the content of a file and return it as a base-64 encoded string.}
@description {
}
@javaClass{org.rascalmpl.library.Prelude}
public java str readBase64(loc file, bool includePadding=true)
throws PathNotFound, IO;

@deprecated{
Use readBase64 instead.
}
public str uuencode(loc file) = readBase64(file);

@synopsis{Decode a base-64 encoded string and write the resulting bytes to a file.}
@description {
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeBase64(loc file, str content)
throws PathNotFound, IO;

@deprecated{
Use writeBase64 instead.
}
public void uudecode(loc file, str content) = writeBase64(file, content);

@synopsis{Read the content of a file and return it as a base-32 encoded string.}
@description {
}
@javaClass{org.rascalmpl.library.Prelude}
public java str readBase32(loc file, bool includePadding=true)
throws PathNotFound, IO;

@synopsis{Decode a base-32 encoded string and write the resulting bytes to a file.}
@description {
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeBase32(loc file, str content)
throws PathNotFound, IO;

@synopsis{Read the contents of a file and return it as a list of bytes.}
@javaClass{org.rascalmpl.library.Prelude}
public java list[int] readFileBytes(loc file)
throws PathNotFound, IO;



@synopsis{Read the contents of a file location and return it as a list of strings.}
@description{
Return the contents of a file location as a list of lines.
Also see ((readFile)).
}
@encoding{
Look at ((readFile)) to understand how this function chooses the character set. If you know the character set used, please use ((readFileLinesEnc)).
}
@pitfalls{
*  In case encoding is not known, we try to estimate as best as we can (see [readFile]).
*  We default to UTF-8, if the file was not encoded in UTF-8 but the first characters were valid UTF-8, 
  you might get an decoding error or just strange looking characters (see ((readFile))).
}
@javaClass{org.rascalmpl.library.Prelude}
public java list[str] readFileLines(loc file, str charset=DEFAULT_CHARSET)
throws PathNotFound, IO;

@synopsis{Writes a list of strings to a file, where each separate string is ended with a newline}
@benefits{
* mirrors ((readFileLines)) in its functionality
}
@pitfalls{
* if the individual elements of the list also contain newlines, the output may have more lines than list elements
}
public void writeFileLines(loc file, list[str] lines, str charset=DEFAULT_CHARSET) {
  writeFile(file, "<for (str line <- lines) {><line>
                  '<}>",
                  charset=charset);
}

@synopsis{Read the contents of a file location and return it as a list of strings.}
@description{
Return the contents (decoded using the Character set supplied) of a file location as a list of lines.
Also see ((readFileLines)).
}
@deprecated{
Use `readFileLines(file, charset=DEFAULT_CHARSET)` instead.
}
public list[str] readFileLinesEnc(loc file, str charset)
throws PathNotFound, IO
  = readFileLines(file, charset=charset);


@javaClass{org.rascalmpl.library.Prelude}
public java void remove(loc file, bool recursive=true) throws IO;


@synopsis{Write values to a file.}
@description{
Write a textual representation of some values to a file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

Files are encoded in UTF-8, in case this is not desired, use ((writeFileEnc)).
}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeFile(loc file, value V..., str charset=DEFAULT_CHARSET)
throws PathNotFound, IO;

@synopsis{Write a list of bytes to a file.}
@javaClass{org.rascalmpl.library.Prelude}
public java void writeFileBytes(loc file, list[int] bytes)
throws PathNotFound, IO;


@synopsis{Write values to a file.}
@description{
Write a textual representation of some values to a file:

*  If a value is a simple string, the quotes are removed and the contents are de-escaped.
*  If a value has a non-terminal type, the parse tree is unparsed to produce a value.
*  All other values are printed as-is.
*  Each value is terminated by a newline character.

Files are encoded using the charset provided.
}
@deprecated{
Use `writeFile(file, charset=...)` instead.
}
public void writeFileEnc(loc file, str charset, value V...) throws PathNotFound, IO
  = writeFile(file, V, charset=charset);


@synopsis{Read the contents of a location and return its MD5 hash.}
@description{
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
@deprecated{
Use `readBase64` instead.
}
public java str toBase64(loc file, bool includePadding=true)
throws PathNotFound, IO;

@javaClass{org.rascalmpl.library.Prelude}
java void copy(loc source, loc target, bool recursive=false, bool overwrite=true) throws IO;

@deprecated{
Use the `copy` function instead
}
void copyFile(loc source, loc target) {
  copy(source, target, recursive=false, overwrite=true);
}

@deprecated{
Use the `copy` function instead
}
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
