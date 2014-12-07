module experiments::Compiler::Examples::Tst

import lang::rascal::grammar::ParserGenerator;
import ParseTree;

value main(list[value] args) = newGenerate("GeneratedG0Parser", "G0Parser", G0);

 //  {p | /Production p := GEXP, prod(_,_,_) := p || regular(_) := p};