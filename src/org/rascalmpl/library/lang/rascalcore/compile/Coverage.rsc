module lang::rascalcore::compile::Coverage

import IO;

@javaClass{org.rascalmpl.library.experiments.Compiler.Coverage}
@reflect{Use Rascal Execution Context to access RVM}
java void startCoverage();

@javaClass{org.rascalmpl.library.experiments.Compiler.Coverage}
@reflect{Use Rascal Execution Context to access RVM}
java void stopCoverage();

@javaClass{org.rascalmpl.library.experiments.Compiler.Coverage}
@reflect{Use Rascal Execution Context to access RVM}
java set[loc] getCoverage();

void reportCoverage(set[loc] covered){
	for(src <- covered){
		println(src);
	}
}