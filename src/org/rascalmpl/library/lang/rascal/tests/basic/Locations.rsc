module lang::rascal::tests::basic::Locations

import String;
import List;
import IO;

int singleChar(str s) = charAt(s,0);
list[int] validSchemeChars = [singleChar("a")..singleChar("z")] + [singleChar("A")..singleChar("Z")] 
	+ [singleChar("0")..singleChar("9")] + [singleChar("+"), singleChar("-"), singleChar(".")]
	;
str createValidScheme(str s) {
	if (s == "")
		return "a";
	return ("a" | it + stringChar(validSchemeChars[c % size(validSchemeChars)]) | c <- chars(s));
}

test bool canChangeScheme(loc l, str s) = (l[scheme = createValidScheme(s)]).scheme ==  createValidScheme(s);
test bool canChangeScheme(loc l, str s) { l.scheme = createValidScheme(s); return l.scheme ==  createValidScheme(s); }

test bool canChangeAuthority(loc l, str s) = (l[authority = s]).authority ==  s;
test bool canChangeAuthority(loc l, str s) { l.authority = s; return l.authority ==  s; }

str fixPath(str s) = visit (s)  { case /\/\/+/ => "/" };
test bool canChangePath(loc l, str s) = (l[path = s]).path ==  fixPath(startsWith(s,"/") ? s : "/" + s);
test bool canChangePath(loc l, str s) { l.path = s; return l.path ==  fixPath(startsWith(s,"/") ? s : "/" + s); }

test bool canChangeQuery(loc l, str s) = (l[query = s]).query ==  s;
test bool canChangeQuery(loc l, str s) { l.query = s; return l.query ==  s; }

test bool canChangeFragment(loc l, str s) = (l[fragment = s]).fragment ==  s;
test bool canChangeFragment(loc l, str s) { l.fragment = s; return l.fragment ==  s; }

list[int] validHostChars = (validSchemeChars - [singleChar("+"), singleChar(".")]);
str createValidHost(str s) {
	if (s == "")
		return "a";
	return ("a.a" | it + stringChar(validHostChars[c % size(validHostChars)]) | c <- chars(s)) + "a.com";
}

test bool canChangeHost(loc l, str s) = (l[scheme="http"][authority="a"][host = createValidHost(s)]).host ==  createValidHost(s);
test bool canChangeHost(loc l, str s) { l.scheme="http"; l.authority = "a"; l.host = createValidHost(s); return l.host ==  createValidHost(s); }

test bool canChangeUser(loc l, str s) = contains(s, "@") || (l[scheme="http"][authority="a@a.com"][user = s]).user ==  s;
test bool canChangeUser(loc l, str s) { if (contains(s, "@")) return true; l.scheme="http"; l.authority = "a@a.com"; l.user = s; if ( l.user ==  s) { return true; } else {println("<l.user> != <s>"); return false; } }

test bool validURIAuthority(loc l, str s) = l[authority = s].uri != "";
test bool validURIPath(loc l, str s) = l[path = s].uri != "";
test bool validURIQuery(loc l, str s) = l[query = s].uri != "";
test bool validURIFragment(loc l, str s) = l[fragment = s].uri != "";

str removeLeadingSlash(str s) = s[0] == "/" ? s[1..] : s;

test bool pathAdditions(list[str] ss) = (|tmp:///ba| | it + s  | s <- ss, s != "" ).path == ("/ba" | it + "/" + removeLeadingSlash(s)  | s <- ss, s != "" );
test bool pathAdditions(loc l, str s) = s == "" || (l + s).path == ((endsWith(l.path, "/") ? l.path : l.path + "/") + removeLeadingSlash(s)) ;

test bool testParent(loc l, str s) = s == "" || ((l + replaceAll(s, "/","_")).parent + "/") == (l[path=l.path] + "/");
test bool testWindowsParent(str s) = s == "" || (|file:///c:/| + replaceAll(s,"/","_")).parent == |file:///c:/|;
test bool testFile(loc l, str s) {
	s = replaceAll(s, "/","_");
	return (l + s).file == s;
}

test bool supportSquareBraces(loc l) {
	newAuth = l.authority + "]";
	newL = l[authority = newAuth];
	stringable = "<newL>";
	return newL.authority == newAuth;
}

test bool testExtension(loc l, str s, str s2) {
	s2 = replaceAll(s2, ".","_");
	s2 = replaceAll(s2, "/","_");
	if (endsWith(s, "/")) {
		s += "a";	
	}
	return (l + "<s>.<s2>").extension == s2;
}