module experiments::vis2::Tst

import IO; 
import util::Cursor;

data M200 = m200(int counter = 666);

void main(list[value] args){
	m = makeCursor(m200());
	println("isCursor(m.counter) = <isCursor(m.counter)>");
}
