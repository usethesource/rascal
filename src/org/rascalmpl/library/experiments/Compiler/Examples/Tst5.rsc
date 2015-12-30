module experiments::Compiler::Examples::Tst5
 
import lang::rascal::types::AbstractType;
import ParseTree;

value f() = isNonTerminalType(\iter-star(sort("Toplevel")));