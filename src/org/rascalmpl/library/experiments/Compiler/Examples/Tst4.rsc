module experiments::Compiler::Examples::Tst4

import IO;

value main(list[value] args) {
	return
	"<for(i <- [0..5]){>
	'  <for(j <- [0..5]){><i>/<j><}><}>
	";
}