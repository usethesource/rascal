module experiments::Compiler::muRascal::Tests

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;

import experiments::Compiler::muRascal::Syntax;
import Ambiguity;

import Prelude;
import ParseTree;
import IO;

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;


void main(){
    code = parse(Library);
    
	println("parsed: <code>");
}