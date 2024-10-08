module lang::rascalcore::compile::Profile

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java void startProfile();

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java void stopProfile();

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java lrel[loc,int] getProfile();

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java void reportProfile();

@javaClass{org.rascalmpl.library.experiments.Compiler.Profile}
@reflect{Use Rascal Execution Context to access RVM}
java void reportProfile(lrel[loc,int] profileData);