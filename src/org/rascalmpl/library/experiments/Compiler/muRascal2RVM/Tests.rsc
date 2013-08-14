module experiments::Compiler::muRascal2RVM::Tests

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal2RVM::mu2rvm;
import Ambiguity;

import Prelude;
import ParseTree;
import IO;

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;


void main(){
    MuModule m = parse(Library);
    
	println("parsed: <m>");
	
	iprintln(mu2rvm(m));
}