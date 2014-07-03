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

/*
  Notes:
  
  import lang::java::jdt::m3::Core;
  import lang::java::jdt::m3::AST;
  
  m = createM3FromEclipseProject(|project://pdb.values|);
  a = createAstsFromEclipseProject(|project://pdb.values|, true);
  styleChecker(m, a, checkers = {names, emptyCatch});
*/
alias Checker = list[Message] (node ast, M3 model);

private set[Checker] active() = {
  names,
  emptyCatch
};  

@doc{For testing on the console; we should assume only a model for the current AST is in the model}
list[Message] styleChecker(M3 model, set[node] asts, set[Checker] checkers = active())
  = [*checker(a, model) | a <- asts, Checker checker <- checkers];
   
@doc{For integration into OSSMETER, we get the models and the ASTs per file}   
list[Message] styleChecker(map[loc, M3] models, map[loc, node] asts, set[Checker] checkers = active()) 
  = [*checker(asts[f], models[f]) | f <- models, checker <- checkers];  
  

data Message = nonStandardIdentifier(loc pos, str id, str regex);

list[Message] names(node ast, M3 model) {
  rel[loc name, loc src] decls = model@declarations;
  
  // this is just to demo a use of the M3 model, it may be better to extract identifiers from the
  // AST.
  return [nonStandardIdentifier(pos, n.path, "[A-Za-z][A-Za-z0-9]*") | <n,pos> <- decls, /[A-Za-z][A-Za-z0-9]*$/ !:= n.path];
}

data Message = emptyCatchBlock(loc pos);

list[Message] emptyCatch(node ast, M3 model) {
  bool isEmpty(empty()) = true;
  bool isEmpty(block([])) = true;
  default bool isEmpty(Statement _) = false;

  return [emptyCatchBlock(a@src) | /a:\catch(_,body) := ast, isEmpty(body)];
}

