module LogicalLocation

// Note: this is a duplicate of LogicalLocation in typepal
// TODO: remove the one in TypePal

extend Location;

bool isLexicallyLess(loc l, loc r, map[loc,loc] m)
    = isLexicallyLess(l in m ? m[l] : l, r in m ? m[r] : r);

bool isStrictlyContainedIn(loc inner, loc outer, map[loc,loc] m)
    = isStrictlyContainedIn(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

bool isContainedIn(loc inner, loc outer, map[loc,loc] m)
    = isContainedIn(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

// bool beginsBefore(loc inner, loc outer, map[loc,loc] m)
//     = beginsBefore(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

bool isBefore(loc inner, loc outer, map[loc,loc] m)
    = isBefore(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

bool isImmediatelyBefore(loc inner, loc outer, map[loc,loc] m)
    = isImmediatelyBefore(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

 bool beginsAfter(loc inner, loc outer, map[loc,loc] m)
    = beginsAfter(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

bool isAfter(loc inner, loc outer, map[loc,loc] m)
    = beginsAfter(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

bool isImmediatelyAfter(loc inner, loc outer, map[loc,loc] m)
    = isImmediatelyAfter(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

bool isOverlapping(loc inner, loc outer, map[loc,loc] m)
    = isOverlapping(inner in m ? m[inner] : inner, outer in m ? m[outer] : outer);

loc cover(list[loc] locs, map[loc,loc] m)
    = cover([l in m ? m[l] : l | l <- locs]);
