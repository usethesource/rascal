module experiments::vis2::Tst

import IO;

alias CallBack[&T] = &T(&T);

int incr(int i) = i + 1;

str dup(str s) = s + s;

map[str, value] callbacks1 = ("INCR" : incr, "DUP": dup);

//map[str, CallBack[&T]] callbacks2 = ();
//callbacks2["INCR"] = incr;

//
//map[CallBack[&T], str] seen_callbacks = (incr : "C");
//
//value main(list[value] args){
//	//callbacks["C"] = incr;
//	println("<callbacks["C"](3)>");
//	
//	//seen_callbacks[incr] = "C";
//	
//	println("<seen_callbacks>");
//	println("<seen_callbacks[incr]>");
//
//}