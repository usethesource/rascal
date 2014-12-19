module experiments::Compiler::Profile

import IO;

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java void startProfile();

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java void stopProfile();

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java set[loc] getProfile();

void reportProfile(set[loc] covered){
	for(src <- covered){
		println(src);
	}
}