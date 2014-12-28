module demo::lang::MissGrant::Step

import demo::lang::MissGrant::AST;
import demo::lang::MissGrant::ToRelation;

import Set;

alias Output = tuple[str state, list[str] commands];

Output merge(Output a, Output b) = <b.state, a.commands + b.commands>;

Output eval(TransRel trans, ActionRel commands, str init, list[str] tokens) =
//   (<init,[]> | merge(step(trans,commands, it.state, token)) | token <- tokens);
   (<init,[]> | merge(it, step(trans, commands, it[0], token)) | token <- tokens);

Output step(TransRel trans, ActionRel commands, str state, str token) 
  = <c, toList(commands[c])>
  when c <- trans[state, token];
