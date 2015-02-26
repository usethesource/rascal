module experiments::Compiler::Examples::RascalExtraction

import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Benchmark;
import IO;
import ValueIO;
import util::Reflective;

value main(list[value] args) {
	//moduleLoc = |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Rascal2muRascal/RascalExpression.rsc|;
	moduleLoc = |project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst3.rsc|;
	m = parse(#start[Module], moduleLoc).top;
	t = cpuTime();
	declarations = [];
	functions = [];
	statements = [];
	expressions = [];
	calls = [];
	patterns = [];
	concretes = [];
	concreteHoles = [];
	names = [];
	comments = [];
	chars = [];
	
	for(int i <- [0..1]){
		//declarations = [ d | /Declaration d := m];
		//functions = [f | /FunctionDeclaration f := m ];
		//statements = [ s | /Statement s := m ];
		//expressions = [ e | /Expression e := m ];
		//calls = [ e | /Expression e := m, e is callOrTree];
		//patterns = [ p | /Pattern p := m ];
		//concretes = [  c | /Concrete c := m ];
		//concreteHoles = [  c | /ConcreteHole c := m ];
		//names = [ n | /Name n := m ];
		comments = [c | /Comment c := m ];
		//chars = [c | /Char c := m ];
	}
	println("comments:");for(n <- comments) println("<n>");
	println("Time = <(cpuTime() - t)/1000000>");
	//writeTextValueFile(|home:///names-<inCompiledMode() ? "comp" : "itp">.txt|, names);
	return [
			//size(declarations), 
			//size(functions),
			//size(statements), 
			//size(expressions), 
			//size(calls), 
			//size(patterns), 
			//size(concretes),
			//size(concreteHoles),
			//size(names)
			size(comments)
			//size(chars)
			];
}

void compare(){
	itp = readTextValueFile(#list[value], |home:///names-itp.txt|);
	comp = readTextValueFile(#list[value], |home:///names-comp.txt|);
	println(diff(itp, comp));
	println("itp[85] = <itp[85]> ");
	println("cmp[85] = <comp[85]> ");
}