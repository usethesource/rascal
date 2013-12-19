module experiments::Compiler::RVM::Interpreter::ParsingTools

import ParseTree;


@javaClass{org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ParsingTools}
@reflect{Uses ctx}
public java Tree parseFragment(str name, Tree tree, loc uri, map[Symbol, Production] grammar);