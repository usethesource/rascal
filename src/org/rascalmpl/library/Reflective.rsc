module Reflective

import ParseTree;

@javaClass{org.rascalmpl.library.Reflective}
@reflect{Uses Evaluator to get back the parse tree for the given path}
public Tree java getModuleParseTree(str modulePath);