@doc{this module is under construction}
@contributor{Jurgen Vinju}
@contributor{Paul Klint}
@contributor{Ashim Shahi}
@contributor{Bas Basten}
module lang::java::style::StyleChecker

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import IO;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

import lang::java::style::BlockChecks;
import lang::java::style::ClassDesign;
import lang::java::style::Coding;
import lang::java::style::Metrics;
import lang::java::style::NamingConventions;
import lang::java::style::SizeViolations;

alias Checker = list[Message] (node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations);

private set[Checker] active() = {
  blockChecks,
  classDesignChecks,
  codingChecks,
  metricsChecks,
  namingConventionsChecks,
  sizeViolationsChecks
};  

@doc{For testing on the console; we should assume only a model for the current AST is in the model}
list[Message] styleChecker(M3 model, set[node] asts, set[Checker] checkers = active()){
	msgs = [];
    for(ast <- asts){
    	classDeclarations = getAllClassDeclarations(ast);
		classDeclarations = getAllMethodDeclarations(ast);
		msgs += [*checker(ast, model, classDeclarations, classDeclarations) | Checker checker <- checkers];
    }
    return msgs;
 }
   
@doc{For integration into OSSMETER, we get the models and the ASTs per file}   
list[Message] styleChecker(map[loc, M3] models, map[loc, node] asts, set[Checker] checkers = active()) 
  = [*checker(asts[f], models[f]) | f <- models, checker <- checkers];  
  

list[Message] main(loc dir = |project://java-checkstyle-tests|){
  
  m3model = createM3FromEclipseProject(dir);
  asts = createAstsFromDirectory(dir, true);
  return styleChecker(m3model, asts, checkers = active());
} 

