module lang::rascal::tests::basic::Locations

import String;
import List;
import Set;
import Relation;
import ListRelation;
import IO;
import util::Math;
import Location;
import util::FileSystem;

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

@expected{MalFormedURI}
test bool noOpaqueURI2() = loc _ := |home:://this:is:opaque|;

test bool canChangeScheme1(loc l, str s) = (l[scheme = createValidScheme(s)]).scheme ==  createValidScheme(s);
test bool canChangeScheme2(loc l, str s) { l.scheme = createValidScheme(s); return l.scheme ==  createValidScheme(s); }

test bool canChangeAuthority1(loc l, str s) = (l[authority = s]).authority ==  s;
test bool canChangeAuthority2(loc l, str s) { l.authority = s; return l.authority ==  s; }

str fixPath(str s) = visit (s)  { case /\/\/+/ => "/" };

test bool canChangePath1(loc l, str s) = (l[path = s]).path ==  fixPath(startsWith(s,"/") ? s : "/" + s);
test bool canChangePath2(loc l, str s) { l.path = s; return l.path ==  fixPath(startsWith(s,"/") ? s : "/" + s); }

test bool canChangeQuery1(loc l, str s) = (l[query = s]).query ==  s;
test bool canChangeQuery2(loc l, str s) { l.query = s; return l.query ==  s; }

test bool canChangeFragment1(loc l, str s) = (l[fragment = s]).fragment ==  s;
test bool canChangeFragment2(loc l, str s) { l.fragment = s; return l.fragment ==  s; }

list[int] validHostChars = (validSchemeChars - [singleChar("+"), singleChar(".")]);
str createValidHost(str s) {
	if (s == "")
		return "a";
	return ("a.a" | it + stringChar(validHostChars[c % size(validHostChars)]) | c <- chars(s)) + "a.com";
}

test bool canChangeHost1(loc l, str s) = (l[scheme="http"][authority="a"][host = createValidHost(s)]).host ==  createValidHost(s);
test bool canChangeHost2(loc l, str s) { l.scheme="http"; l.authority = "a"; l.host = createValidHost(s); return l.host ==  createValidHost(s); }

test bool canChangeUser1(loc l, str s) = contains(s, "@") || (l[scheme="http"][authority="a@a.com"][user = s]).user ==  s;
test bool canChangeUser2(loc l, str s) { if (contains(s, "@")) return true; l.scheme="http"; l.authority = "a@a.com"; l.user = s; if ( l.user ==  s) { return true; } else {println("<l.user> != <s>"); return false; } }

test bool validURIAuthority(loc l, str s) = l[authority = s].uri != "";
test bool validURIPath(loc l, str s) = l[path = s].uri != "";
test bool validURIQuery(loc l, str s) = l[query = s].uri != "";
test bool validURIFragment(loc l, str s) = l[fragment = s].uri != "";

str fixPathAddition(str s) = replaceAll(s, "/", "");

test bool pathAdditions1(list[str] ss) 
  =  (|tmp:///ba| | it + t  | s <- ss, t := fixPathAddition(s), t != "" ).path 
  == ("/ba" | it + "/" + t  | s <- ss, t := fixPathAddition(s), t != "" );
  
test bool pathAdditions2(loc l, str s) = s == "" || (l + fixPathAddition(s)).path == ((endsWith(l.path, "/") ? l.path : l.path + "/") + fixPathAddition(s)) ;

test bool testParent(loc l, str s) = s == "" || ((l + replaceAll(s, "/","_")).parent + "/") == (l[path=l.path] + "/");
test bool testWindowsParent(str s) = s == "" || (|file:///c:/| + replaceAll(s,"/","_")).parent == |file:///c:/|;
test bool testFile(loc l, str s) {
	s = replaceAll(s, "/","_");
    if (s == "")
      return true;
	return (l + s).file == s;
}

test bool supportSquareBraces(loc l) {
	newAuth = l.authority + "]";
	newL = l[authority = newAuth];
	stringable = "<newL>";
	return newL.authority == newAuth;
}

test bool noFile()    = |tmp://X|.file == "";
test bool rootPath()  = |tmp://X|.path == "/";
test bool rootPath3() = |tmp:///|.path == "/";
test bool rootPath4() = |tmp://X/|.path == "/";

test bool top0(loc x) = x.top == x.top.top;
test bool top1(loc x) = "<x.top>" == "|" + x.uri + "|";
test bool top2(loc x) = toLocation(x.uri) == x.top;

@ignore
test bool splicePathEncoded()         = str x := " " && |tmp:///<x>.rsc| == |tmp:///| + "<x>.rsc";
@ignore
test bool spliceArbPathEncoded(str x) = |tmp:///<x>.rsc| == |tmp:///| + "<x>.rsc";

test bool enclosingTest1() = |tmp:///x.src|(5,10,<0,0>,<0,0>) < |tmp:///x.src|(2,20,<0,0>,<0,0>);
test bool enclosingTest2() = |tmp:///x.src|(5,10,<0,0>,<0,0>) <= |tmp:///x.src|(2,20,<0,0>,<0,0>);
test bool enclosingTest3() = |tmp:///x.src|(5,10,<0,0>,<0,0>) <= |tmp:///x.src|(5,10,<0,0>,<0,0>);
test bool enclosingTest4() = |tmp:///x.src|(5,10,<1,2>,<1,12>) <= |tmp:///x.src|(5,10,<0,0>,<0,0>);
test bool enclosingTest5() = |tmp:///x.src|(5,10,<0,0>,<0,0>) <= |tmp:///x.src|(5,10,<1,2>,<1,12>);
test bool enclosingTest6() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(5,10,<0,0>,<0,0>));
test bool enclosingTest7() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(5,11,<0,0>,<0,0>));
test bool enclosingTest8() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(4,10,<0,0>,<0,0>));
test bool enclosingTest9() = !(|tmp:///x.src|(4,11,<0,0>,<0,0>) <= |tmp:///x.src|(4,10,<0,0>,<0,0>));

test bool offSetLengthEnclosing(int aOffset, int aLength, int bOffset, int bLength)
  = (abs(aOffset) < toInt(pow(2,31)) 
  && abs(aOffset) + abs(aLength) < toInt(pow(2,31))
  && abs(bOffset) < toInt(pow(2,31)) 
  && abs(bOffset) + abs(bLength) < toInt(pow(2,31))
  && abs(aOffset) >= abs(bOffset) 
  && abs(aOffset) <= abs(bOffset) + abs(bLength) 
  && abs(aOffset) + abs(aLength) <= abs(bOffset) + abs(bLength))
  ==>
  |tmp:///x.rsc|(abs(aOffset), abs(aLength),<0,0>,<0,0>) <= |tmp:///x.rsc|(abs(bOffset), abs(bLength),<0,0>,<0,0>);
  

// Simulate a list of 1000 lines each of length < 1000;
public list[int] lineSizes = [ arbInt(1000) | int _ <- [1 .. 1000] ];   

public int maxIndex = (0 | it + lineSizes[i] | int i <- index(lineSizes));        

// Turn an index in the above list into a line/column pair
tuple[int line, int column] getLineAndColumn(int idx){
    int pos = 0;

    for(int i <- index(lineSizes)){
        if(pos + lineSizes[i] >= idx) return <i, idx - pos>;
        pos += lineSizes[i];
    }
    throw "getLineAndColumn: <idx> out of range [0 .. <maxIndex>]";
}

// Build a location for an area from index f to index t.
loc buildLoc(int f, int t, str base = "base.src"){
    return |test:///<base>|(f, t-f, getLineAndColumn(f), getLineAndColumn(t));
}

// Restrict i to legal index values
int restrict(int i){
    if(i < 0) i = -i;
    if(i > maxIndex) return arbInt(maxIndex);
    return i;
}

// Get a location from index f to index t.
loc getLoc(int f, int t, str base = "base.src"){
    f = restrict(f); t = restrict(t);
    return (f <= t) ? buildLoc(f, t, base=base) : buildLoc(t, f, base=base);
}

// Test the comparison operators

test bool less1(int f1, int t1, int f2, int t2){
    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
    return l1.offset >  l2.offset && l1.offset + l1.length <= l2.offset + l2.length ||
           l1.offset >= l2.offset && l1.offset + l1.length <  l2.offset + l2.length
           ? l1 < l2 : !(l1 < l2);
}

test bool less2(int f, int t){
    f = restrict(f); t = restrict(t);
    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
    return l1 < l2; // path is lexicographically less, other attributes are equal
}

test  bool lessequal1(int f1, int t1, int f2, int t2){
    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
    return l1 == l2 ||
           l1.offset >  l2.offset && l1.offset + l1.length <= l2.offset + l2.length ||
           l1.offset >= l2.offset && l1.offset + l1.length <  l2.offset + l2.length
           ? l1 <= l2 : !(l1 <= l2);
}

test bool lessequal2(int f, int t){
    f = restrict(f); t = restrict(t);
    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
    return l1 <= l2;    // path is lexicographically less, other attributes are equal
}

@ignoreCompiler{FIXME}
test  bool greater1(int f1, int t1, int f2, int t2){
    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
    return l1.offset <  l2.offset && l1.offset + l1.length >= l2.offset + l2.length ||
           l1.offset <= l2.offset && l1.offset + l1.length >  l2.offset + l2.length
           ? l1 > l2 : !(l1 > l2);
}

test bool greater2(int f, int t){
    f = restrict(f); t = restrict(t);
    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
    return !(l1 > l2);
}

@ignoreCompiler{FIXME}
test  bool greaterequal1(int f1, int t1, int f2, int t2){
    l1 = getLoc(f1, t1); l2 = getLoc(f2, t2);
    return l1 == l2 ||
           l1.offset <  l2.offset && l1.offset + l1.length >= l2.offset + l2.length ||
           l1.offset <= l2.offset && l1.offset + l1.length >  l2.offset + l2.length
           ? l1 >= l2 : !(l1 >= l2);
}

test bool greaterequal2(int f, int t){
    f = restrict(f); t = restrict(t);
    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
    return !(l1 >= l2);
}

test bool equal1(int f, int t){
    f = restrict(f); t = restrict(t);
    l1 = getLoc(f, t); l2 = getLoc(f, t);
    return l1 == l2;
}

test bool equal2(int f, int t){
    f = restrict(f); t = restrict(t);
    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
    return !(l1 == l2); 
}

// Create a list of n different locations

list[loc] getLocs(int n){
    locs = [getLoc(arbInt(maxIndex), arbInt(maxIndex)) | int _ <- [0 .. n]];
    return[ locs[i] | int i <- [0..n], !any(int j <- [0..n], i != j, locs[i] == locs[j]) ];
}

// Use loc in a set
test bool locInSet(){
    locs = getLocs(100);
    return size(locs) == size(toSet(locs));
}
// Use loc in a map
test bool locInMap(){
    locs = getLocs(100);
    m = (locs[i] : locs[i].offset | int i <- index(locs));
    return all(k <- m, m[k] == k.offset);
}

// Use loc in a relation
test bool locInRel(){
    locs = getLocs(100);
    m = {<locs[i], locs[i].offset> | int i <- index(locs)};
    return all(k <- domain(m), m[k] == {k.offset});
}

// Use loc in a list relation
test bool locInLRel(){
    locs = getLocs(100);
    m = [<locs[i], locs[i].offset> | int i <- index(locs)];
    return all(k <- domain(m), m[k] == [k.offset]);
}

// Locations library

// Make two locations with a gap inbetween. When gap is negative they will overlap.
// Always return a pair of locations <l1, l2> where l1 starts before l2.

tuple[loc, loc] makeLocsWithGap(int gap){
    sign = gap > 0 ? 1 : -1;
    absgap =  min(abs(gap), maxIndex/2);
    m1 = 1 + arbInt(maxIndex - absgap - 2); // 1 <= m1 <= maxIndex - 2
    m2 = m1 + sign * absgap;            
    
    llen = arbInt(m1);
    l = getLoc(m1 - llen, m1);
    
    rlen = m2 == maxIndex ? 0 : arbInt(maxIndex - m2);
    r = getLoc(m2, m2 + rlen);
    
    if (l.offset == r.offset && r.length == 0) {
      return <r, l>;
    }
    else if (l.offset >= r.offset) {
      return <r, l>;
    } 
    else {
      return <l, r>;
    }
}

bool report(loc l1, loc l2, bool expected){
    if(!expected){
        println("Not expected: <l1>, <l2>");
        return false;
    }
    return true;
}

// isLexicallyLess

bool isLexicallyLess1(int f, int t){
    l1 = getLoc(f, t, base="base1.src"); l2 = getLoc(f, t, base="base2.src");
    return report(l1, l2, isLexicallyLess(l1, l2));
}

bool isLexicallyLess1(int _){
    <l1, l2> = makeLocsWithGap(10);
    return report(l1, l2, isLexicallyLess(l1, l2));
}

test bool isSameFile1(){
    l = |C:///a|;
    r = |C:///a#1|;
    return isSameFile(l, r);
}

test bool isSameFile2(){
    l = |C:///a|;
    r = |C:///b#1|;
    return !isSameFile(l, r);
}

test bool isSameFile3(loc l){
    return isSameFile(l, l);
}

test bool isSameFile4(loc l){
    return !isSameFile(l[scheme="A"], l[scheme="B"]);
}

test bool isSameFile5(loc l){
    return !isSameFile(l[authority="A"], l[authority="B"]);
}

// isContainedIn

test bool isContainedIn1(int f, int len){
    f1 = restrict(f); t1 = restrict(f1 + len);
    len1 = t1 - f1;
    delta = (t1-f1)/2;
    l1 = getLoc(f1, t1); l2 = getLoc(f1 + delta, t1 - delta);
    return report(l1, l2, delta > 0 ==> isContainedIn(l2, l1));
}

test bool isContainedIn2(int f, int len){
    f1 = restrict(f); t1 = restrict(f1 + len);
    len1 = t1 - f1;
    delta = (t1-f1)/2;
    l1 = getLoc(f1, t1, base="base1.src"); l2 = getLoc(f1 + delta, t1 - delta,  base="base2.src");
    return report(l1, l2, !isContainedIn(l2, l1));
}

// beginsBefore

@ignore{unknown}
test bool beginsBefore1(int _){
    <l1, l2> = makeLocsWithGap(-10);
    return report(l1, l2, beginsBefore(l1, l2));
}

test bool beginsBefore2(int _){
    <l1, l2> = makeLocsWithGap(10);
    return report(l1, l2, !beginsBefore(l2, l1));
}

// isBefore

test bool isBefore1(int _){
    <l1, l2> = makeLocsWithGap(10);
    return report(l1, l2, isBefore(l1, l2));
}

test bool isBefore2(int _){
    <l1, l2> = makeLocsWithGap(10);
    return report(l1, l2, !isBefore(l2, l1));
}

// isImmediatelyBefore
@ignore{Fails intermittently}
test bool isImmediatelyBefore1(int _){
    <l1, l2> = makeLocsWithGap(0);
    return report(l1, l2, isImmediatelyBefore(l1, l2));
}

@ignore{Fails intermittently}
test bool isImmediatelyBefore2(int _){
    <l1, l2> = makeLocsWithGap(0);
    return report(l1, l2, !isImmediatelyBefore(l2, l1));
}

// beginsAfter
@ignore{Until #1693 has been solved}
test bool beginsAfter1(int _){
    <l1, l2> = makeLocsWithGap(-10);
    return report(l1, l2, beginsAfter(l2, l1));
}

// isAfter

test bool isAfter1(int _){
    <l1, l2> = makeLocsWithGap(10);
    return report(l1, l2, isAfter(l2, l1));
}

// isImmediatelyAfter
@ignore{Fails intermittently}
test bool isImmediatelyAfter1(int _){
    <l1, l2> = makeLocsWithGap(0);
    return report(l1, l2, isImmediatelyAfter(l2, l1));
}

// isOverlapping

test bool isOverlapping1(int _){
   <l1, l2> = makeLocsWithGap(-1);
    return report(l1, l2, isOverlapping(l1, l2));
}

test bool isOverlapping2(int _){
   <l1, l2> = makeLocsWithGap(10);
    return !isOverlapping(l1, l2);
}

// cover

test bool isCover1(int _){
   <l1, l2> = makeLocsWithGap(10);
   u = cover([l1, l2]);
   return report(l1, l2, isContainedIn(l1, u) && isContainedIn(l2, u));
}

test bool isCover2(int _){
   <l1, l2> = makeLocsWithGap(-10);
   u = cover([l1, l2]);
   return report(l1, l2, isContainedIn(l1, u) && isContainedIn(l2, u));
}

test bool isCover3(int f, int t){
   f = restrict(f); t = restrict(t);
   l = getLoc(f, t);
   u = cover([l, l, l, l]);
   return report(l, l, l == u);
}

test bool trailingSlashFile1() {
    withSlash = |project://rascal/src/org/rascalmpl/library/|;
    withoutSlash = |project://rascal/src/org/rascalmpl/library|;

    return withSlash.file == withoutSlash.file;
}

test bool trailingSlashFile2() {
    withSlash = |project://rascal/src/org/rascalmpl/library/|;
    withoutSlash = |project://rascal/src/org/rascalmpl/library|;

    withoutSlash.file = "libs";
    withSlash.file = "libs";

    return withSlash.file == withoutSlash.file
        && withSlash.parent == withoutSlash.parent
        ;
}

test bool testRelativize() 
    = relativize(|file:///a/b|, |file:///a/b/c.txt|)
        == |relative:///c.txt|;

test bool testFailedRelativize()
    = relativize(|file:///b/b|, |file:///a/b/c.txt|)
        == |file:///a/b/c.txt|;

test bool trailingSlashRelativize1() 
    = relativize(|file:///library/|, |file:///library|)
        == relativize(|file:///library/|, |file:///library/|);

test bool trailingSlashRelativize2() 
    = relativize(|file:///library|, |file:///library/|)
        == relativize(|file:///library|, |file:///library|);

test bool extensionSetWithMoreDots1()
    = |file:///a.txt/b|[extension="aap"] == |file:///a.txt/b.aap|;

test bool extensionSetWithMoreDots2()
    = |file:///a.txt/b.noot|[extension="aap"] == |file:///a.txt/b.aap|;

test bool extensionSetWithSlash()
    = |file:///a/b.noot/|[extension="aap"] == |file:///a/b.aap/|;

test bool extensionSetWithSlashAndMoreDots()
    = |file:///a.txt/b.noot/|[extension="aap"] == |file:///a.txt/b.aap/|;

test bool extensionGetWithMoreDot1() 
    = |file:///a.txt/b|.extension == "";

test bool extensionGetWithMoreDots2()
    = |file:///a.txt/b.noot|.extension == "noot";

test bool extensionGetWithSlash()
    = |file:///a/b.noot/|.extension == "noot";

test bool extensionGetSimple()
    = |file:///a/b.noot|.extension == "noot";

test bool extensionGetRoot()
    = |file:///b.noot|.extension == "noot";

test bool extensionGetNoRoot()
    = |file:///b|.extension == "";

test bool extensionNoPath()
    = |file:///|.extension == "";

test bool extensionSetRoot()
    = |file:///b.noot|[extension="aap"] == |file:///b.aap|;

test bool extensionSetSimple()
    = |file:///a/b.noot|[extension="aap"] == |file:///a/b.aap|;


// we don't want backslashes in windows
test bool correctTempPathResolverOnWindows() = /\\/ !:= resolveLocation(|tmp:///|).path;

private data MavenLocalRepositoryPath
    = path(str groupId, str artifactId, str version)
    | error(str cause)
    ;

private MavenLocalRepositoryPath parseMavenLocalRepositoryPath(loc jar) {
    if (jar.extension != "jar") {
        return error("jar should have jar extension");
    }

    groupId    = replaceAll(jar.parent.parent.parent.path[1..], "/", ".");
    artifactId = jar.parent.parent.file;
    version    = jar.parent.file;
    file       = jar.file;

    if (file != "<artifactId>-<version>.jar") {
        return error("This is not a repository release jar; filename should be ArtifactId-Version.jar: <jar.file>");
    }

    if (/!/ := artifactId) {
        return error("ArtifactId contains exclamation mark: <artifactId>");
    }

    return path(groupId, artifactId, version);
}

test bool mvnSchemeTest() {
    debug = false;
    jarFiles = find(|mvn:///|, "jar");

    // check whether the implementation of the scheme holds the contract specified in the assert
    for (jar <- jarFiles, path(groupId, artifactId, version) := parseMavenLocalRepositoryPath(jar)) {
        mvnLoc = |mvn://<groupId>!<artifactId>!<version>|;
        assert resolveLocation(mvnLoc) == resolveLocation(jar) : "<resolveLocation(mvnLoc)> != <resolveLocation(jar)>
                                                                 '  jar: <jar>
                                                                 '  mvnLoc: <mvnLoc>";
    }

    // report on all the failed attempts
    for (debug, jar <- jarFiles, error(msg) := parseMavenLocalRepositoryPath(jar)) {
        println(msg);
    }


    return true;
}