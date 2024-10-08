@bootstrapParser
module lang::rascalcore::compile::Examples::RascalExtraction

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Benchmark;
import IO;
import ValueIO;
import util::Reflective;

value main() = compareAll();

value compareAll() {
	moduleLoc = |std:///experiments/Compiler/Rascal2muRascal/RascalExpression.rsc|;
	//moduleLoc = |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst3.rsc|;
	m = parse(#start[Module], moduleLoc).top;
	t = cpuTime();
	declarations = [];
	functions = [];
	statements = [];
	expressions = [];
	calls = [];
	patterns = [];
	
	for(int _ <- [0..10]){
		declarations = [ d | /Declaration d := m];
		functions = [f | /FunctionDeclaration f := m ];
		statements = [ s | /Statement s := m ];
		expressions = [ e | /Expression e := m ];
		int cnt = 0;
		visit(m) { case Expression e: cnt += 1; };
		calls = [ e | /Expression e := m, e is callOrTree];
		patterns = [ p | /Pattern p := m ];
	}
	println("Time = <(cpuTime() - t)/1000000>");
	//writeTextValueFile(|home:///names-<inCompiledMode() ? "comp" : "itp">.txt|, names);
	return [
			size(declarations), 
			size(functions),
			size(statements), 
			size(expressions),
			size(calls), 
			size(patterns)
			];
}

void compare(){
	itp = readTextValueFile(#list[value], |home:///names-itp.txt|);
	comp = readTextValueFile(#list[value], |home:///names-comp.txt|);
	println(diff(itp, comp));
	println("itp[85] = <itp[85]> ");
	println("cmp[85] = <comp[85]> ");
}