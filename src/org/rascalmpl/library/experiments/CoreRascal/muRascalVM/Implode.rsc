module experiments::CoreRascal::muRascalVM::Implode

import experiments::CoreRascal::muRascalVM::Syntax;
import experiments::CoreRascal::muRascalVM::AST;

import ParseTree;

public RascalVM parse(str s) = implode(#experiments::CoreRascal::muRascalVM::AST::RascalVM,
						  		  	   parse( #experiments::CoreRascal::muRascalVM::Syntax::RascalVM, s));