module experiments::vis2::Tst2

import IO;
import util::Cursor;

data D = d(int n, str opt = "abc");

value main(list[value] args){
	x = makeCursor(d(13, opt="def"));
	println("x.n = <toPath(x.n)>");
	println("x.opt = <toPath(x.opt)>");
	return true;
}
 