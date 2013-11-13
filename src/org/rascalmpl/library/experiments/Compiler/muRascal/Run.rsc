module experiments::Compiler::muRascal::Run

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::Implode;

import ParseTree;
import util::IDE;
import vis::Figure;

import IO;

public void execute(experiments::Compiler::muRascal::Syntax::Module tree, loc selection) {
	ast = implode(#experiments::Compiler::muRascal::AST::Module, tree);
	out = executeProgram(ast.directives);
	println(out);	
}

set[Contribution] contributions = 
	{ menu(menu("muRascal", [ action("Run", execute) ])),
	  categories( ("IValue" : { italic(), foregroundColor(gray(0.4)) }, "Reference" : { bold(), foregroundColor(gray(0.4)) }, "IType" : { foregroundColor(color("purple",1.0)) } )) };

@doc{Registers the muRascal language, .mu}
public void registerMuRascal() {
	registerLanguage("muRascal", "mu", experiments::Compiler::muRascal::Syntax::Module (str src, loc l) { return parse(#experiments::Compiler::muRascal::Syntax::Module, src, l); });
	registerContributions("muRascal", contributions);
}
