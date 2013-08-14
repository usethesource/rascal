module experiments::CoreRascal::muRascal::Tests

import experiments::CoreRascal::muRascal::AST;
import experiments::CoreRascal::muRascal::Implode;

import experiments::CoreRascal::muRascal::Syntax;
import Ambiguity;

import Prelude;
import ParseTree;
import IO;

public loc Library = |std:///experiments/CoreRascal/muRascal/Library.mu|;


void main(){
    code = parse(Library);
    
	println("parsed: <code>");
}