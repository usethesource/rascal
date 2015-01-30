module experiments::Compiler::Examples::Tst2

import String;
import IO;

bool canChangeUser2(loc l, str s) { 
	if (contains(s, "@")) return true; 
	l.scheme="http"; 
	l.authority = "a@a.com";
	println("l.user = <l.user>"); 
	l.user = s; 
	if ( l.user ==  s) { return true; } else {println("<l.user> != <s>"); return false; } }

value main(list[value] args) = canChangeUser2(|tmp:///|, "y");