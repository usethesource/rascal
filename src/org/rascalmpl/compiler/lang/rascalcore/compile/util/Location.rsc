module lang::rascalcore::compile::util::Location

import IO;
import List;
import Exception;

// Some operations on location that may end in the standard library;

bool sameFile(loc l1, loc l2)
    = l1.path == l2.path;

str getContent(loc l)
    = readFile(l);

// Is inner location textually contained in outer location?
bool isContainedIn(loc inner, loc outer)
    = inner.path == outer.path && (!outer.offset? || inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length);

bool isBefore(loc l, loc r)
    = l.path == r.path && (l.offset <= r.offset && (l.offset + l.length) <= r.offset);

bool isImmediatelyBefore(loc l, loc r)
    = l.path == r.path && (l.offset <= r.offset && l.offset + l.length == r.offset);

bool isAfter(loc l, loc r)
    = isBefore(r, l);

bool isImmediatelyAfter(loc l, loc r)
    = isImmediatelyBefore(r, l);

bool isOverlapping(loc l, loc r)
    = l.path == r.path && ((l.offset <= r.offset && l.offset + l.length > r.offset) ||
                          (r.offset <= l.offset && r.offset + r.length > l.offset));

bool isDisjoint(loc l, loc r)
    = isBefore(l, r) || isAfter(l, r);

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
