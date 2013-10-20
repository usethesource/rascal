module lang::rascal::tests::Locations

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

test bool canChangePath(loc l, str s) = (l[path = s]).path ==  (startsWith(s,"/") ? s : "/" + s);
test bool canChangePath(loc l, str s) { l.path = s; return l.path ==  (startsWith(s,"/") ? s : "/" + s); }

test bool canChangeQuery(loc l, str s) = (l[query = s]).query ==  s;
test bool canChangeQuery(loc l, str s) { l.query = s; return l.query ==  s; }

test bool canChangeFragment(loc l, str s) = (l[fragment = s]).fragment ==  s;
test bool canChangeFragment(loc l, str s) { l.fragment = s; return l.fragment ==  s; }


list[int] validHostChars = (validSchemeChars - [singleChar("+"), singleChar(".")]);
str createValidHost(str s) {
	if (s == "")
		return "a";
	return ("a." | it + stringChar(validHostChars[c % size(validHostChars)]) | c <- chars(s)) + "a.com";
}

test bool canChangeHost(loc l, str s) = (l[scheme="http"][authority="a"][host = createValidHost(s)]).host ==  createValidHost(s);
test bool canChangeHost(loc l, str s) { l.scheme="http"; l.authority = "a"; l.host = createValidHost(s); return l.host ==  createValidHost(s); }

test bool canChangeUser(loc l, str s) = (l[scheme="http"][authority="a@a.com"][user = s]).user ==  s;
test bool canChangeUser(loc l, str s) { l.scheme="http"; l.authority = "a@a.com"; l.user = s; if ( l.user ==  s) { return true; } else {println("<l.user> != <s>"); return false; } }

test bool validURIAuthority(loc l, str s) = l[authority = s].uri != "";
test bool validURIPath(loc l, str s) = l[path = s].uri != "";
test bool validURIQuery(loc l, str s) = l[query = s].uri != "";
test bool validURIFragment(loc l, str s) = l[fragment = s].uri != "";
