module experiments::Compiler::Coverage

@javaClass{org.rascalmpl.library.experiments.Compiler.Coverage}
@reflect{Use Rascal Execution Context to access RVM}
void startCoverage();

@javaClass{org.rascalmpl.library.experiments.Compiler.Coverage}
@reflect{Use Rascal Execution Context to access RVM}
void stopCoverage();

@javaClass{org.rascalmpl.library.experiments.Compiler.Coverage}
@reflect{Use Rascal Execution Context to access RVM}
set[loc] getCoverage();

void reportCoverage(set[loc] covered){
	for(src <- covered){
		println(src);
	}
}