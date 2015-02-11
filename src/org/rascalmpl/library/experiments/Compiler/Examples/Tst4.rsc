module experiments::Compiler::Examples::Tst4

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Benchmark;
import IO;
import util::Reflective;


value main(list[value] args) {
	moduleLoc = |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Rascal2muRascal/RascalExpression.rsc|;
	m = parse(#start[Module], moduleLoc).top;
	t = cpuTime();
	new = [ e | /Expression e := m ];
	println("size = <size(new)>");
	return (cpuTime() - t)/1000000;
}
