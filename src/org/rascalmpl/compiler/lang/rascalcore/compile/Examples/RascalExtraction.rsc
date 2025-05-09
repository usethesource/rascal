@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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