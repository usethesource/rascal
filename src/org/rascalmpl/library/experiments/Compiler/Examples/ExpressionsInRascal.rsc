module experiments::Compiler::Examples::ExpressionsInRascal

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Benchmark;
import IO;
import util::Reflective;

value main(list[value] args) {
	moduleLoc = |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Rascal2muRascal/RascalExpression.rsc|;
	m = parse(#start[Module], moduleLoc).top;
	t = cpuTime();
	new = [];
	for(int i <- [0..10]){
		new = [ e | /Expression e := m ];
	}	
	println("Time = <(cpuTime() - t)/1000000>");
	return size(new);
}