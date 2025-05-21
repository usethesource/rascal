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
* From `x:///a/b` and `x:///a/b/c` this makes `relative:///c`.
* If the outside does not envelop the inside, then the original loc is returned.
}
@javaClass{org.rascalmpl.library.Prelude}
java loc relativize(loc outside, loc inside);

@synopsis{Find the first `haystack` folder the `needle` can be found in and relativize it, or fail.}
@description{
* From `[|x:///a/b|]` as haystack and `|x:///a/b/c|` as needle this makes `|relative:///c|`.
* If none of the `haystack` locations contain the `needle`, a `PathNotFound` exception is thrown.
}
loc relativize(list[loc] haystack, loc needle) throws PathNotFound {
    if (h <- haystack, loc r := relativize(h, needle), r != needle) {
        return r;
    }
    throw PathNotFound(needle);
}

@synsopis{Concatenate a relative path to a given surrounding path}
@description{
* `relative` must be of scheme `relative:///` or `SchemeNotSupported` will be thrown
* ((resolve)) is the opposite of ((relativize))
* the return value does not necessarily exist
}
loc resolve(loc outside, loc relative) = outside + relative.path when relative.scheme == "relative";
default loc resolve(loc _, loc relative) {
    throw SchemeNotSupported(relative);
}

@synopsis{Find the right folder in which a relative location is to be found and return the complete path}
@description{
* `relative` must be of scheme `relative:///`
* ((resolve)) is the opposite of ((relativize))
* if a file can not be found in any of the `haystack` folders, then `PathNotFound`` is thrown.
* if `force` is true then a location relative to the first element of the haystack will be returned, even if the file was not found anywhere in the haystack.
}
loc resolve(list[loc] haystack, loc relative, bool force = false) throws PathNotFound {
    assert relative.scheme == "relative";
    assert haystack != [];

    for (loc outside <- haystack, loc candidate := resolve(outside, relative), exists(candidate)) {
        return candidate;
    }

    if (force && haystack != []) {
        return resolve(haystack[0], relative);
    }

    throw PathNotFound(relative);
}

@synopsis{Shortens an absolute path to a jar inside the local maven repository.}
@javaClass{org.rascalmpl.library.Prelude}
java loc mavenize(loc jar);

@synopsis{If the location points to a jar file, then this modifies the scheme and the path to point _inside_ of the jar.}
@javaClass{org.rascalmpl.library.Prelude}
java loc jarify(loc jar);

@synopsis{Check that two locations refer to the same file.}    
@javaClass{org.rascalmpl.library.Prelude}
java bool isSameFile(loc l, loc r);

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

@javaClass{org.rascalmpl.library.Prelude}
java bool isStrictlyContainedIn(loc inner, loc outer);


@synopsis{Is a location textually contained in another location?}
@description{
Containment between two locations `inner` and `outer` holds when


- `inner` and `outer` are equal, or
- `inner` is strictly contained in `outer`.
}

@javaClass{org.rascalmpl.library.Prelude}
java bool isContainedIn(loc inner, loc outer);


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
@javaClass{org.rascalmpl.library.Prelude}
java bool isOverlapping(loc l, loc r);


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
