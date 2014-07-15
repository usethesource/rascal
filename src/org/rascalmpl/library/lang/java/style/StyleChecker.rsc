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

import lang::java::style::NamingConventions;	

alias Checker = list[Message] (node ast, M3 model);

private set[Checker] active() = {
  namingConventions,
  emptyCatch
};  

@doc{For testing on the console; we should assume only a model for the current AST is in the model}
list[Message] styleChecker(M3 model, set[node] asts, set[Checker] checkers = active())
  = [*checker(a, model) | a <- asts, Checker checker <- checkers];
   
@doc{For integration into OSSMETER, we get the models and the ASTs per file}   
list[Message] styleChecker(map[loc, M3] models, map[loc, node] asts, set[Checker] checkers = active()) 
  = [*checker(asts[f], models[f]) | f <- models, checker <- checkers];  
  
data Message = emptyCatchBlock(loc pos);

list[Message] emptyCatch(node ast, M3 model) {
  bool isEmpty(empty()) = true;
  bool isEmpty(block([])) = true;
  default bool isEmpty(Statement _) = false;

  return [emptyCatchBlock(a@src) | /a:\catch(_,body) := ast, isEmpty(body)];
}

value main(loc dir = |project://style-check-tests|){
  
  m3model = createM3FromEclipseProject(dir);
  asts = createAstsFromDirectory(dir, true);
  return styleChecker(m3model, asts, checkers = {namingConventions, emptyCatch});
}  

