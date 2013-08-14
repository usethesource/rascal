module experiments::Compiler::RVM::Implode

import experiments::Compiler::RVM::Syntax;
import experiments::Compiler::RVM::AST;

import ParseTree;

public RascalVM parse(str s) = implode(#experiments::Compiler::RVM::AST::RascalVM,
						  		  	   parse( #experiments::Compiler::RVM::Syntax::RascalVM, s));