module experiments::Compiler::Examples::Tst2

import ParseTree;
import Grammar;
import lang::rascal::grammar::definition::Characters;
//import lang::rascal::grammar::definition::Literals;
//import lang::rascal::grammar::analyze::Dependency;
//import lang::rascal::format::Escape;
//import IO;
//import Set;
//import List;
//import String;
//import ValueIO;
//import analysis::graphs::Graph;
//import Relation;

value main(list[value] args) = 
complement(\char-class([range(9,9),range(10,10),range(13,13),range(32,32)]));
