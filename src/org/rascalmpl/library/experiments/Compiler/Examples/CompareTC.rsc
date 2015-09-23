module experiments::Compiler::Examples::CompareTC

import Message;
import util::Reflective;
import IO;
import util::Benchmark;

import lang::rascal::types::CheckTypes;
import lang::rascal::\syntax::Rascal;

import experiments::Compiler::Profile; 

public set[Message] checkModule(loc moduleLoc) {
	c = newConfiguration();
	try {
		pt = parseModule(moduleLoc);
		if (pt has top && Module m := pt.top) {
			t = cpuTime();
			//startProfile();
			c = checkModule(m, c, forceCheck=true);
			//stopProfile();
			//reportProfile();
			println("Time = <(cpuTime() - t)/1000000>");
		} else {
			throw "Unexpected parse result for module to check <moduleLoc>"; 
		}
	} catch perror : {
		throw "Could not parse and prepare config for base module to check: <perror>";
	}
	
	return c.messages;
}

value main(){
	
	m = |std:///lang/rascal/types/CheckerConfig.rsc|;
	return checkModule(m);
}	