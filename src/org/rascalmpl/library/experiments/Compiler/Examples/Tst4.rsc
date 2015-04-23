module experiments::Compiler::Examples::Tst4

import lang::rascal::tests::types::StaticTestingUtils;

value main(list[value] args){
	makeModule("MMM", "data Figure (real shrink = 1.0, str fillColor = \"white\", str lineColor = \"black\")  =  emptyFigure() 
  					| ellipse(Figure inner = emptyFigure()) 
  					| box(Figure inner = emptyFigure());

 				value main(list[value] args) = (!(ellipse(inner=emptyFigure(fillColor=\"red\")).fillColor == \"white\"));");
	return checkOK("true;", importedModules=["MMM"]);
} 
	