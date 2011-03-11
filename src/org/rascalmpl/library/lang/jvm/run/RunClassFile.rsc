module lang::jvm::run::RunClassFile

@doc{Register the class file, and its dependencies and run it's main method}
@javaClass{org.rascalmpl.library.lang.jvm.run.RunClassFile}
@reflect{Uses URI Resolver Registry}
public void java runClassFile(loc path,loc dependencies...)
throws PathNotFound(loc), IOError(str msg);
