module demo::lang::MissGrant::Step

import demo::lang::MissGrant::AST;
import demo::lang::MissGrant::ToRelation;

import Set;

alias Output = tuple[str state, list[str] commands];

public tuple[str,list[str]] addCommands(tuple[str,list[str]] a, tuple[str,list[str]] b) = <b[0],a[1] + b[1]>;

public Output eval(TransRel trans, ActionRel commands, str init, list[str] tokens) =
   (<init,[]> | addCommands(step(trans,commands,it[0],\token)) | \token <- tokens);

public Output step(TransRel trans, ActionRel commands, str init,str \token) {
  if(c <- trans[init,\token]) {
    return <c, toList(commands[c])>;
  } 
  else {
    return <c,[]>;
  }
}
