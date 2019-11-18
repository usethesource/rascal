module lang::rascalcore::compile::Examples::Tst2

import String;
import List;
import Set;
import Relation;
import ListRelation;
import IO;
import util::Math;
import Location;

int singleChar(str s) = charAt(s,0);

list[int] makeValidSchemeChars() = [singleChar("a")..singleChar("z")] + [singleChar("A")..singleChar("Z")] 
    + [singleChar("0")..singleChar("9")] + [singleChar("+"), singleChar("-"), singleChar(".")]
    ;
    
list[int] validSchemeChars = [singleChar("a")..singleChar("z")] + [singleChar("A")..singleChar("Z")] 
    + [singleChar("0")..singleChar("9")] + [singleChar("+"), singleChar("-"), singleChar(".")]
    ;

str createValidScheme(str s) {
    if (s == "")
        return "a";
    return ("a" | it + stringChar(validSchemeChars[c % size(validSchemeChars)]) | c <- chars(s));
}

list[int] validHostChars = (validSchemeChars - [singleChar("+"), singleChar(".")]);
str createValidHost(str s) {
    if (s == "")
        return "a";
    return ("a.a" | it + stringChar(validHostChars[c % size(validHostChars)]) | c <- chars(s)) + "a.com";
}

test bool canChangeHost1(loc l, str s) = (l[scheme="http"][authority="a"][host = createValidHost(s)]).host ==  createValidHost(s);
