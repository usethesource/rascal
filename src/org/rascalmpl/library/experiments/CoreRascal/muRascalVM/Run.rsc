module experiments::CoreRascal::muRascalVM::Run

import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Syntax;
import experiments::CoreRascal::muRascalVM::Implode;

import ParseTree;
import util::IDE;

import IO;

@javaClass{org.rascalmpl.library.experiments.CoreRascal.RVM.Execute}
@reflect{Executes muRascalVM programs}
public java tuple[value,int] executeProgram(RVMProgram program, bool debug, int repeat);

public void execute(experiments::CoreRascal::muRascalVM::Syntax::RascalVM tree, loc selection) {
	ast = implode(#experiments::CoreRascal::muRascalVM::AST::RascalVM, tree);
	out = executeProgram(ast.directives, 1);
	println(out);	
}

set[Contribution] contributions = 
	{ menu(menu("muRascalVM", [ action("Run", execute) ])) };

@doc{Registers the muRascalVM language, .rvm}
public void registerLanguage() {
	registerLanguage("muRascalVM", "rvm", experiments::CoreRascal::muRascalVM::Syntax::RascalVM (str src, loc l) { return parse(#experiments::CoreRascal::muRascalVM::Syntax::RascalVM, src, l); });
	registerContributions("muRascalVM", contributions);
}
