module experiments::vis2::Tst2

import IO;

import lang::json::IO;

data D = d(int n, str opt = "abc");

void main(list[value] args){
	s = toJSON(d(3));
	println(s);
	println(fromJSON(#D, s));
}
 