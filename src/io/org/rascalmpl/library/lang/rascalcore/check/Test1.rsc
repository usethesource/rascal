module lang::rascalcore::check::Test1

import String;
import List;
import Set;
import Relation;
import ListRelation;
import IO;
import util::Math;

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

@ignoreInterpreter{Renaming}
@expected{InvalidURI}
test bool noOpaqueURI() = loc l := |home:://this:is:opaque|;

@ignoreCompiler{Remove-after-transtion-to-compiler: Renaming}
@expected{MalFormedURI}
test bool noOpaqueURI() = loc l := |home:://this:is:opaque|;

test bool canChangeScheme1(loc l, str s) = (l[scheme = createValidScheme(s)]).scheme ==  createValidScheme(s);
test bool canChangeScheme2(loc l, str s) { l.scheme = createValidScheme(s); return l.scheme ==  createValidScheme(s); }

//test bool canChangeAuthority1(loc l, str s) = (l[authority = s]).authority ==  s;
//test bool canChangeAuthority2(loc l, str s) { l.authority = s; return l.authority ==  s; }

//str fixPath(str s) = visit (s)  { case /\/\/+/ => "/" };
//
//test bool canChangePath1(loc l, str s) = (l[path = s]).path ==  fixPath(startsWith(s,"/") ? s : "/" + s);
//test bool canChangePath2(loc l, str s) { l.path = s; return l.path ==  fixPath(startsWith(s,"/") ? s : "/" + s); }
//
//test bool canChangeQuery1(loc l, str s) = (l[query = s]).query ==  s;
//test bool canChangeQuery2(loc l, str s) { l.query = s; return l.query ==  s; }
//
//test bool canChangeFragment1(loc l, str s) = (l[fragment = s]).fragment ==  s;
//test bool canChangeFragment2(loc l, str s) { l.fragment = s; return l.fragment ==  s; }
//
//list[int] validHostChars = (validSchemeChars - [singleChar("+"), singleChar(".")]);
//str createValidHost(str s) {
//    if (s == "")
//        return "a";
//    return ("a.a" | it + stringChar(validHostChars[c % size(validHostChars)]) | c <- chars(s)) + "a.com";
//}
//
//test bool canChangeHost1(loc l, str s) = (l[scheme="http"][authority="a"][host = createValidHost(s)]).host ==  createValidHost(s);
//test bool canChangeHost2(loc l, str s) { l.scheme="http"; l.authority = "a"; l.host = createValidHost(s); return l.host ==  createValidHost(s); }
//
//test bool canChangeUser1(loc l, str s) = contains(s, "@") || (l[scheme="http"][authority="a@a.com"][user = s]).user ==  s;
//test bool canChangeUser2(loc l, str s) { if (contains(s, "@")) return true; l.scheme="http"; l.authority = "a@a.com"; l.user = s; if ( l.user ==  s) { return true; } else {println("<l.user> != <s>"); return false; } }
//
//test bool validURIAuthority(loc l, str s) = l[authority = s].uri != "";
//test bool validURIPath(loc l, str s) = l[path = s].uri != "";
//test bool validURIQuery(loc l, str s) = l[query = s].uri != "";
//test bool validURIFragment(loc l, str s) = l[fragment = s].uri != "";
//
//str fixPathAddition(str s) = replaceAll(s, "/", "");
//
//test bool pathAdditions1(list[str] ss) 
//  =  (|tmp:///ba| | it + t  | s <- ss, t := fixPathAddition(s), t != "" ).path 
//  == ("/ba" | it + "/" + t  | s <- ss, t := fixPathAddition(s), t != "" );
//  
//test bool pathAdditions2(loc l, str s) = s == "" || (l + fixPathAddition(s)).path == ((endsWith(l.path, "/") ? l.path : l.path + "/") + fixPathAddition(s)) ;
//
//test bool testParent(loc l, str s) = s == "" || ((l + replaceAll(s, "/","_")).parent + "/") == (l[path=l.path] + "/");
//test bool testWindowsParent(str s) = s == "" || (|file:///c:/| + replaceAll(s,"/","_")).parent == |file:///c:/|;
//test bool testFile(loc l, str s) {
//    s = replaceAll(s, "/","_");
//    return (l + s).file == s;
//}
//
//test bool supportSquareBraces(loc l) {
//    newAuth = l.authority + "]";
//    newL = l[authority = newAuth];
//    stringable = "<newL>";
//    return newL.authority == newAuth;
//}
//
//test bool noFile()    = |tmp://X|.file == "";
//test bool rootPath()  = |tmp://X|.path == "/";
//test bool rootPath3() = |tmp:///|.path == "/";
//test bool rootPath4() = |tmp://X/|.path == "/";
//
//test bool top0(loc x) = x.top == x.top.top;
//test bool top1(loc x) = "<x.top>" == "|" + x.uri + "|";
//test bool top2(loc x) = toLocation(x.uri) == x.top;
//
//@ignore
//test bool splicePathEncoded()         = str x := " " && |tmp:///<x>.rsc| == |tmp:///| + "<x>.rsc";
//@ignore
//test bool spliceArbPathEncoded(str x) = |tmp:///<x>.rsc| == |tmp:///| + "<x>.rsc";
//
//test bool enclosingTest1() = |tmp:///x.src|(5,10,<0,0>,<0,0>) < |tmp:///x.src|(2,20,<0,0>,<0,0>);
//test bool enclosingTest2() = |tmp:///x.src|(5,10,<0,0>,<0,0>) <= |tmp:///x.src|(2,20,<0,0>,<0,0>);
//test bool enclosingTest3() = |tmp:///x.src|(5,10,<0,0>,<0,0>) <= |tmp:///x.src|(5,10,<0,0>,<0,0>);
//test bool enclosingTest4() = |tmp:///x.src|(5,10,<1,2>,<1,12>) <= |tmp:///x.src|(5,10,<0,0>,<0,0>);
//test bool enclosingTest5() = |tmp:///x.src|(5,10,<0,0>,<0,0>) <= |tmp:///x.src|(5,10,<1,2>,<1,12>);
//test bool enclosingTest6() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(5,10,<0,0>,<0,0>));
//test bool enclosingTest7() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(5,11,<0,0>,<0,0>));
//test bool enclosingTest8() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(4,10,<0,0>,<0,0>));
//test bool enclosingTest9() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(4,10,<0,0>,<0,0>));
//
//test bool offSetLengthEnclosing(int aOffset, int aLength, int bOffset, int bLength)
//  = (abs(aOffset) < toInt(pow(2,31)) 
//  && abs(aOffset) + abs(aLength) < toInt(pow(2,31))
//  && abs(bOffset) < toInt(pow(2,31)) 
//  && abs(bOffset) + abs(bLength) < toInt(pow(2,31))
//  && abs(aOffset) >= abs(bOffset) 
//  && abs(aOffset) <= abs(bOffset) + abs(bLength) 
//  && abs(aOffset) + abs(aLength) <= abs(bOffset) + abs(bLength))
//  ==>
//  |tmp:///x.rsc|(abs(aOffset), abs(aLength),<0,0>,<0,0>) <= |tmp:///x.rsc|(abs(bOffset), abs(bLength),<0,0>,<0,0>);
//  
//
//// Simulate a list of 1000 lines each of length < 1000;
//public list[int] lineSizes = [ arbInt(1000) | int i <- [1 .. 1000] ];   
//
//public int maxIndex = (0 | it + lineSizes[i] | int i <- index(lineSizes));        
//
//// Turn an index in the above list into a line/column pair
//tuple[int line, int column] getLineAndColumn(int idx){
//    int pos = 0;
//
//    for(int i <- index(lineSizes)){
//        if(pos + lineSizes[i] >= idx) return <i, idx - pos>;
//        pos += lineSizes[i];
//    }
//    throw "getLineAndColumn: <idx> out of range [0 .. <maxIndex>]";
//}
//
//// Build a location for an area from index f to index t.
//loc buildLoc(int f, int t, str base = "base.src"){
//    return |test:///<base>|(f, t-f, getLineAndColumn(f), getLineAndColumn(t));
//}
//
//// Restrict i to legal index values
//int restrict(int i){
//    if(i < 0) i = -i;
//    if(i > maxIndex) return arbInt(maxIndex);
//    return i;
//}
//
//// Get a location from index f to index t.
//loc getLoc(int f, int t, str base = "base.src"){
//    f = restrict(f); t = restrict(t);
//    return (f <= t) ? buildLoc(f, t, base=base) : buildLoc(t, f, base=base);
//}
//
//// Test the comparison operators
//
//test bool less1(int f1, int t1, int f2, int t2){
//    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
//    return l1.offset >  l2.offset && l1.offset + l1.length <= l2.offset + l2.length ||
//           l1.offset >= l2.offset && l1.offset + l1.length <  l2.offset + l2.length
//           ? l1 < l2 : !(l1 < l2);
//}
//
//test bool less2(int f, int t){
//    f = restrict(f); t = restrict(t);
//    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
//    return l1 < l2; // path is lexicographically less, other attributes are equal
//}
//
//test  bool lessequal1(int f1, int t1, int f2, int t2){
//    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
//    return l1 == l2 ||
//           l1.offset >  l2.offset && l1.offset + l1.length <= l2.offset + l2.length ||
//           l1.offset >= l2.offset && l1.offset + l1.length <  l2.offset + l2.length
//           ? l1 <= l2 : !(l1 <= l2);
//}
//
//test bool lessequal2(int f, int t){
//    f = restrict(f); t = restrict(t);
//    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
//    return l1 <= l2;    // path is lexicographically less, other attributes are equal
//}
//
//@ignoreCompiler{unknown}
//test  bool greater1(int f1, int t1, int f2, int t2){
//    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
//    return l1.offset <  l2.offset && l1.offset + l1.length >= l2.offset + l2.length ||
//           l1.offset <= l2.offset && l1.offset + l1.length >  l2.offset + l2.length
//           ? l1 > l2 : !(l1 > l2);
//}
//
//test bool greater2(int f, int t){
//    f = restrict(f); t = restrict(t);
//    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
//    return !(l1 > l2);
//}
//
//@ignoreCompiler{unknown}
//test  bool greaterequal1(int f1, int t1, int f2, int t2){
//    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
//    return l1 == l2 ||
//           l1.offset <  l2.offset && l1.offset + l1.length >= l2.offset + l2.length ||
//           l1.offset <= l2.offset && l1.offset + l1.length >  l2.offset + l2.length
//           ? l1 >= l2 : !(l1 >= l2);
//}
//
//test bool greaterequal2(int f, int t){
//    f = restrict(f); t = restrict(t);
//    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
//    return !(l1 >= l2);
//}
//
//test bool equal1(int f, int t){
//    f = restrict(f); t = restrict(t);
//    l1 = getLoc(f, t); l2 = getLoc(f, t);
//    return l1 == l2;
//}
//
//test bool equal2(int f, int t){
//    f = restrict(f); t = restrict(t);
//    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
//    return !(l1 == l2); 
//}
//
//// Create a list of n different locations
//
//list[loc] getLocs(int n){
//    locs = [getLoc(arbInt(maxIndex), arbInt(maxIndex)) | int i <- [0 .. n]];
//    return[ locs[i] | int i <- [0..n], !any(int j <- [0..n], i != j, locs[i] == locs[j]) ];
//}
//
//// Use loc in a set
//test bool locInSet(){
//    locs = getLocs(100);
//    return size(locs) == size(toSet(locs));
//}
//// Use loc in a map
//test bool locInMap(){
//    locs = getLocs(100);
//    m = (locs[i] : locs[i].offset | int i <- index(locs));
//    return all(k <- m, m[k] == k.offset);
//}
//
//// Use loc in a relation
//test bool locInRel(){
//    locs = getLocs(100);
//    m = {<locs[i], locs[i].offset> | int i <- index(locs)};
//    return all(k <- domain(m), m[k] == {k.offset});
//}
//
//// Use loc in a list relation
//test bool locInLRel(){
//    locs = getLocs(100);
//    m = [<locs[i], locs[i].offset> | int i <- index(locs)];
//    return all(k <- domain(m), m[k] == [k.offset]);
//}