module demo::lang::MissGrant::ToRelation

import  demo::lang::MissGrant::AST;

alias TransRel = rel[str state, str eventToken,  str toState];
alias ActionRel = rel[str state, str commandToken];


public TransRel transRel(Controller ctl) = { <s1, e, s2> | /state(s1, _, ts) <- ctl, transition(e, s2) <- ts };

public ActionRel commands(Controller ctl) = {<s, a> | /state(s, as, _) <- ctl, a <- as }; 
