module lang::rascalcore::compile::Examples::Tst4

import Map;
import Message;

import List;
//import analysis::graphs::Graph;
import Map;

data Controller = controller(list[Event] events, 
                      list[str] resets, 
                      list[Command] commands,
                      list[State] states);

data State = state(str name, list[str] actions, list[Transition] transitions);

data Command = command(str name, str token);
data Event = event(str name, str token);
data Transition = transition(str event, str state);


anno loc Controller@location;
anno loc State@location;
anno loc Command@location;
anno loc Event@location;
anno loc Transition@location;

data ControllerState = 
    controllerState(
        Controller ctl,
        str curStateName, 
        StateEnv stateEnv,
        map[str,str] eventNameToToken,
        map[str,str]  commandNameToToken,
        map[str,str]  eventTokenToName,
        map[str,str]  commandTokenToName
    );

alias StateEnv = map[str, State];

private State initial(Controller ctl) = ctl.states[0];

//private Graph[str] stateGraph(Controller ctl) = 
//  { <s1, s2> | /state(s1, _, ts) <- ctl, transition(_, s2) <- ts };

private set[str] usedEvents(Controller ctl) = { x | /transition(x, _) <- ctl };

private set[str] usedActions(Controller ctl) = { a |  /state(_, as, _) <- ctl, a <- as };

private set[str] definedCommands(Controller ctl) = { n | command(n, _) <- ctl.commands };

private set[str] definedEvents(Controller ctl) = { n | event(n, _) <- ctl.events };

private set[str] definedStates(Controller ctl) = { n | state(n, _, _) <- ctl.states };


public list[Message] checkController(Controller ctl) {
  list[Message] errors = [];
  
  env = ();
  errors += for (e:event(n, t) <- ctl.events) {
     if (n in env) { 
       append error("Duplicate event", e@location);
     }
     else {
       if (t in invert(env)) 
         append error("Duplicate event token", e@location);
       env[n] = t;
     }
  }
  
  errors += for (e <- ctl.resets, s <- ctl.states, t:transition(e, _) <- s.transitions) 
     append error("Reset event used in transition", t@location);
  
  return errors;
}
 