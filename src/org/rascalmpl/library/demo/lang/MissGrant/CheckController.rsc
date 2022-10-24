module demo::lang::MissGrant::CheckController

import demo::lang::MissGrant::AST;
import Map;
import Message;

/* To check

Errors
- no duplicate event/command decls
- events, commands are declared
- reset events must be in events
- no reset events used in transitions
- statemachine is deterministic

Warnings
- dead states
- unused action

*/

public list[Message] checkController(Controller ctl) {
  list[Message] errors = [];
  
  env = ();
  errors += for (e:event(n, t) <- ctl.events) {
     if (n in env) { 
       append error("Duplicate event", e.location);
     }
     else {
       if (t in invert(env)) 
         append error("Duplicate event token", e.location);
       env[n] = t;
     }
  } 
  
  env = ();
  errors += for (c:command(n, t) <- ctl.commands) {
     if (n in env) 
       append error("Duplicate command", c.location);
     if (t in invertUnique(env)) 
       append error("Duplicate command token", c.location);
      env[n] = t;
  }
  
  seen = {};
  errors += for (s:state(n, _, _) <- ctl.states) {
     if (n in env) 
       append error("Duplicate state", s.location);
     seen += {n};
  }
  
  errors += for (e <- ctl.resets, s <- ctl.states, t:transition(e, _) <- s.transitions) 
     append error("Reset event used in transition", t.location);
  
  errors += err1: for (s <- ctl.states) {
    seen = {};
    for (t:transition(e, _) <- s.transitions) {
      if (e in seen) 
        append err1: error("Non-determinism", t.location);
      seen += {e};
    }
  }
  
  cmds = definedCommands(ctl);
  evs = definedEvents(ctl);
  sts = definedStates(ctl);
  
  errors += for (e <- ctl.resets, e notin evs) 
    append error("Undeclared reset event", ctl.location);
  
  errors += err2: for (s <- ctl.states) {
  	for (a <- s.actions, a notin cmds)
      append err2: error("Undeclared action used", s.location);
    for (t:transition(e, _) <- s.transitions, e notin evs)  
      append err2: error("Undeclared event", t.location);
    for (t:transition(_, s2) <- s.transitions, s2 notin sts)  
      append err2: error("Undeclared state", t.location);
  }
  
  g = stateGraph(ctl)+;
  s0 = initial(ctl);
  errors += for (s:state(n, _, _) <- ctl.states) {
    if (n notin g[s0.name], s != s0)
      append warning("Unreachable state", s.location);
  }
  
  as = usedActions(ctl);
  errors += for (c:command(n, _) <- ctl.commands) {
    if (n notin as) 
      append warning("Unused command", c.location);
  }
  
  es = usedEvents(ctl);
  errors += for (e:event(n, _) <- ctl.events) {
    if (n notin es, n notin ctl.resets) 
      append warning("Unused event", e.location);
  }
  
  
  return errors;
}
