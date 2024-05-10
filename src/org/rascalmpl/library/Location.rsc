@license{
  Copyright (c) 2019 SWAT.engineering
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@swat.engineering - SWAT.engineering}


@synopsis{Library functions for source locations.}
@description{
The following library functions are defined for source locations:
(((TOC)))

A source location `l` refers to a text fragment in another file or resource. To ease the description we will
talk about _`l` 's text_ instead of _the text `l` refers to_.
}
module Location

import IO;
import List;
import Set;
import String;
import Exception;


@synopsis{Extracts a path relative to a parent location.}
@description{
So from `x:///a/b` and `x:///a/b/c` this makes `relative:///c`.
If the outside does not envelop the inside, then the original loc is returned.
}
@javaClass{org.rascalmpl.library.Prelude}
java loc relativize(loc outside, loc inside);

@synopsis{Find the first `haystack` folder the `needle` can be found in and relativize it, or fail.}
loc relativize(list[loc] haystack, loc needle) {
    if (h <- haystack, loc r := relativize(h, needle), r != needle) {
        return r;
    }
    else {
        fail relativize;
    }
}

@synopsis{Names of file system path syntaxes that can be found in the wild.}
@description{
* `generic` captures all unix-like systems like the BSD family, SunOs, Solaris, Irix, etc.
* `mac_osx` captures modern Mac's that also have a unix-like filesystem but with different exceptions to permissable characters and case-sensitivity.
* `windows` is for all DOS-based filesystems and what came after, with the slashes in the other direction.
* `linux` is for all the filesystems in Linux distributions.

Use this as a parameter to ((locFromFileSystem)) to choose how to parse a string as a filesystem path.
}
data FileSystemSyntax
    = generic()
    | mac_osx()
    | windows()
    | linux()
    ;

@javaClass{org.rascalmpl.library.Prelude}
@synopsis{Utility to retrieve the current notation for file system paths.}
@description{
This is uses to configure the default syntax parameter of ((locFromFileSystem)).
}
java FileSystemSyntax currentFileSystem();

@synopsis{Converts the OS-specific string representation of a file or directory PATH to a `loc`.}
@description{
This converts a string that hold a path to a file, written in a specific notation for paths, to
a canonical `loc` in URI format.

* if `legalize` is true, then every path segment is legalized by replacing offending characters to `_`
* if `legalize` is false, and offending characters appear between the path separators, then an IO exception is thrown.
* if the requested file system syntax is either case insensitive or not case preserving (or both), then all uppercase characters will be replaced by lowercase characters.
* on windows systems the drive letter `C:` is added if a drive letter is missing.
* on all the other systems, if the path starts with a path separator, it is taken as absolute in `file:///`. Otherwise the root will be `cwd:///`.
}
// Wait for bootstrap
// @examples{
// ```rascal-shell
// import Location;
// locFromFileSystem("C:\\Documents\\Newsletters\\Summer2018.pdf", \syntax=windows())
// ```
// }
@benefits{
* After conversion there are many utility functions that operate safely and portably on `loc` values. See ((module:IO)) and ((module:util::FileSystem)) for examples.
* ((module:util::ShellExec)) features `loc`-based versions for passing the names of binaries and the names of file parameters on the commandlines as `loc` values.
* The file names identified by the path strings do not need to exist. They could typically be names in a CSV file or a spreadsheet data source, for which
no reflection exists in the mounted drives of the current (virtual) computer. Consider them "data", until passed in the functions of ((module:IO)), for example.
}
@pitfalls{
* Delaying this conversion until _just before_ file ((module:IO)), misses out on:
   * efficiency; loc values have internal sharing and memoization features.
   * portability: OS-specific string paths can break on other machines.
   * equational reasoning: ((locFromFileSystem)) has canonicalizing features to remove common cases of aliases (such as uppercase vs lowercase).
* Path syntax does not support `..` or `.` notation, simply because this conversion does not require
the paths to even exist on the current system. The `..` notation remains part of the name of a file accordingly, without being interpreted against a mounted file system.
}
@javaClass{org.rascalmpl.library.Prelude}
java loc locFromFileSystem(str pathString, FileSystemSyntax \syntax=currentFileSystem(), bool legalize=false);

@synopsis{Check that two locations refer to the same file.}    
bool isSameFile(loc l, loc r) = l.top[fragment=""] == r.top[fragment=""];

@synopsis{Compare two location values lexicographically.}
@description{
When the two locations refer to different files, their paths are compared as string.
When they refer to the same file, their offsets are compared when present.
}
@pittfalls{
This ordering regards the location value itself as opposed to the text it refers to.
}
bool isLexicallyLess(loc l, loc r)
    = isSameFile(l, r) ? (l.offset ? 0) < (r.offset ? 0) : l.top < r.top;




@synopsis{Get the textual content a location refers to.}
str getContent(loc l)
    = readFile(l);


@synopsis{Is a location textually (strictly) contained in another location?}
@description{
Strict containment between two locations `inner` and `outer` holds when


- `outer` 's text begins before `inner` 's text, or
- `outer` 's text ends after `inner` 's text, or
- both.
}

bool isStrictlyContainedIn(loc inner, loc outer){
    if(inner == outer){
        return false;
    }
    if(isSameFile(inner, outer)){
       if(inner.offset?){
          if(outer.offset?){
            return    inner.offset == outer.offset && inner.offset + inner.length <  outer.offset + outer.length
                   || inner.offset >  outer.offset && inner.offset + inner.length <= outer.offset + outer.length;
          } else {
            return inner.offset > 0;
          }
       }
    }
    return false;
}


@synopsis{Is a location textually contained in another location?}
@description{
Containment between two locations `inner` and `outer` holds when


- `inner` and `outer` are equal, or
- `inner` is strictly contained in `outer`.
}

bool isContainedIn(loc inner, loc outer){
    if(isSameFile(inner, outer)){
       if(inner.offset?){
          if(outer.offset?){
            return (inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length);
          } else {
            return true;
          }
       } else {
         return !outer.offset?;
       }
    }
    return false;
}


@synopsis{Begins a location's text before (but may overlap with) another location's text?}
bool beginsBefore(loc l, loc r)
    = isSameFile(l, r) && l.offset < r.offset;
    

@synopsis{Begins and ends a location's text before another location's text?}
@description{
`isBefore(l, r)` holds when `l` 's text occurs textually before `r` 's text.
}
bool isBefore(loc l, loc r)
    = isSameFile(l, r)  && l.offset + l.length <= r.offset;


@synopsis{Occurs a location's text _immediately_ before another location's text?}
@description{
`isImmediatelyBefore(l, r)` holds when `l` 's text occurs textually before, and is adjacent to, `r` 's text.
}
bool isImmediatelyBefore(loc l, loc r)
    = isSameFile(l, r) && l.offset + l.length == r.offset;
 
 
@synopsis{Begins a location's text after (but may overlap with) another location's text?

Description
`beginsAfter(l, r)` holds when `l` 's text begins after `r` 's text. No assumption is made about the end of both texts.
In other words, `l` 's text may end before or after the end of `r` 's text.}
bool beginsAfter(loc l, loc r)
    = isSameFile(l, r) && l.offset > r.offset;
       

@synopsis{Is a location's text completely after another location's text?}
bool isAfter(loc l, loc r)
    = isBefore(r, l);


@synopsis{Is a location's text _immediately_ after another location's text?}
bool isImmediatelyAfter(loc l, loc r)
    = isImmediatelyBefore(r, l);


@synopsis{Refer two locations to text that overlaps?}
bool isOverlapping(loc l, loc r)
    = isSameFile(l, r) && (  (l.offset <= r.offset && l.offset + l.length > r.offset) 
                          || (r.offset <= l.offset && r.offset + r.length > l.offset)
                          );


@synopsis{Compute a location that textually covers the text of a list of locations.}
@description{
Create a new location that refers to the smallest text area that overlaps with the text of the given locations.
The given locations should all refer to the same file but they may be overlapping or be contained in each other.
}
loc cover(list[loc] locs){
    n = size(locs);
    if(n == 0){
         throw IllegalArgument(locs, "Cover of empty list of locations");
    } else if(n == 1){
        return locs[0];
    } else {
        locs = [ l | l <- locs, !any(m <- locs, m != l, isContainedIn(l, m)) ];
        locs = sort(locs, beginsBefore);
        loc first = locs[0];
        loc last = locs[-1];
 
        tops = {l.top | l <- locs};
        if(size(tops) > 1){
            throw IllegalArgument(locs, "Cover of locations with different scheme, authority or path");
        }
        if(first.begin? && last.end?){
            return first.top(first.offset, last.offset + last.length - first.offset, 
                           <first.begin.line, first.begin.column>,
                           <last.end.line, last.end.column>);
        } else if(first.offset? && last.offset?){
            return first.top(first.offset, last.offset + last.length - first.offset);
        } else {
            return first.top;
        }
    }
}
