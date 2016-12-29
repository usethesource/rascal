module experiments::Compiler::Examples::CompareTC

import Message;
import util::Reflective;
import IO;
import util::Benchmark;

import lang::rascal::types::CheckTypes;
import lang::rascal::\syntax::Rascal;

import experiments::Compiler::Profile; 

public set[Message] checkModule(loc moduleLoc, PathConfig pcfg) {
	c = newConfiguration(pcfg);
	try {
		m = parseModule(moduleLoc);
		c = checkModule(m, c, forceCheck=true);
	} catch perror : {
		throw "Could not parse and prepare config for base module to check: <perror>";
	}
	
	return c.messages;
}

value main(){
	
	m = |std:///lang/rascal/types/CheckerConfig.rsc|;
	return checkModule(m, pathConfig());
}	