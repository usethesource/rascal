module lang::rascalcore::compile::util::Location

import IO;

// Some operations on location that may end in the standard library;

bool sameFile(loc l1, l2)
    = l1.path == l2.path;

// Is inner location textually contained in outer location?
bool isContainedIn(loc inner, loc outer){
    return inner.path == outer.path && (!outer.offset? || inner.offset >= outer.offset && inner.offset + inner.length <= outer.offset + outer.length);
}

bool isBefore(loc l, loc r){
    res =  l.path == r.path && (l.offset <= r.offset && (l.offset + l.length) <= r.offset);
    //println("isBefore(<l>,<r>) ==\> <res>");
    return res;
}

bool isImmediatelyBefore(loc l, loc r){
    return l.path == r.path && (l.offset <= r.offset && l.offset + l.length <= r.offset);
}

bool isAfter(loc l, loc r){
    return isBefore(r, l);
}

bool isImmediatelyAfter(loc l, loc r)
    = isImmediatelyBefore(r, l);

bool isOverlapping(loc l1, loc l2){
    return l.path == r.path && ((l.offset <= r.offset && l.offset + l.length > r.offset) ||
                                (r.offset <= l.offset && r.offset + r.length > l.offset));
}