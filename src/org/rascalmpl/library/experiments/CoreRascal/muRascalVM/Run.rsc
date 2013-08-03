module experiments::CoreRascal::muRascalVM::Run

import experiments::CoreRascal::muRascalVM::AST;

@javaClass{org.rascalmpl.library.experiments.CoreRascal.RVM.RVM}
@reflect{Executes muRascalVM programs}
public java int executeProgram(list[Directive] program);

