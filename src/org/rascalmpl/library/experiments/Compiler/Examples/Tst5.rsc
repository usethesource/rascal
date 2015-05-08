@bootstrapParser
module experiments::Compiler::Examples::Tst5

import IO;

alias  INTEGER = int;
alias MAP = map[INTEGER, str];

data DATA = d(INTEGER n);

alias ADATA = DATA;

ADATA f(ADATA x) = x;

value main(list[value] args) {
	iprintln(#DATA);
	return true;
}	