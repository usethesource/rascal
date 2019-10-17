module lang::rascalcore::compile::util::Location

import IO;
import List;
import Exception;

// Some operations on location that may end in the standard library;
@doc{
.Synopsis
Check that two locations refer to the same file.
}
bool sameFile(loc l1, loc l2)
    = l1.scheme == l2.scheme && l1.path == l2.path;

@doc{
.Synopsis
Get the textual content a location refers to.
}
str getContent(loc l)
    = readFile(l);

@doc{
.Synopsis
Is a location textually contained in another location?
}

bool isContainedIn(loc inner, loc outer)
    = inner.path == outer.path && (!outer.offset? || inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length);

@doc{
.Synopsis
Refers a location to text before the text referred to by another location?
}
bool isBefore(loc l, loc r)
    = l.path == r.path && (l.offset <= r.offset && (l.offset + l.length) <= r.offset);

@doc{
.Synopsis
Refers a location to text _immediately_ before the text referred to by another location?
}
bool isImmediatelyBefore(loc l, loc r)
    = l.path == r.path && (l.offset <= r.offset && l.offset + l.length == r.offset);

@doc{
.Synopsis
Refers a location to text after the text referred to by another location?
}
bool isAfter(loc l, loc r)
    = isBefore(r, l);

@doc{
.Synopsis
Refers a location to text _immediately_ after the text referred to by another location?
}
bool isImmediatelyAfter(loc l, loc r)
    = isImmediatelyBefore(r, l);

@doc{
.Synopsis
Refer two locations to text that overlaps?
}
bool isOverlapping(loc l, loc r)
    = l.path == r.path && ((l.offset <= r.offset && l.offset + l.length > r.offset) ||
                          (r.offset <= l.offset && r.offset + r.length > l.offset));

@doc{
.Synopsis
Refer two locations to text that is disjoint, i.e., does not overlap?
}
bool isDisjoint(loc l, loc r)
    = isBefore(l, r) || isAfter(l, r);

@doc{
.Synopsis
Take the union of a list of locations
.Description
Create a new location that refers to the smallest text area that overlaps with the given locations.
The given locations should refer to the same file.
}
loc union(list[loc] locs){
    if(isEmpty(locs)){
        throw IllegalArgument(locs, "Union of empty list of locations");
    }
    locs = sort(locs, isBefore);
    first = locs[0];
    last = locs[-1];
    path = first.path;
    scheme = first.scheme;
    for(l <- locs){
        if(l.scheme != scheme || l.path != path){
            throw IllegalArgument(locs, "Union of locations with different scheme or path");
         }
    }
    return |<scheme>:///<path>|(first.offset, last.offset + last.length - first.offset, 
                                <first.begin.line, first.begin.column>,
                                <last.end.line, last.end.column>);
}
