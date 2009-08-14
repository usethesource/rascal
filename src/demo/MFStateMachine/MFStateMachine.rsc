module demo::MFStateMachine::MFStateMachine
import IO;
import List;
import Relation;
import Graph;
import Set;

alias event = str;
alias state = str;
alias command = str;

// Abstract syntax for the state machine language
data trans = trans(event e, state s);
data Decl = eventDef(list[event] names) 
	|   commandDef(list[command] names)
        |   initialDef(state name)
	|   stateDef(state name, list[command] cmds, list[trans] trns);
alias MachineDef = list[Decl];


// Internal representation of state machines

// A state machine is a tuple of...
alias Machine = tuple[rel[state,event,state],            // state transitions
                      rel[state,command],          // commands
                      state];                      // initial state

// All states in the machine
public set[state] statesOf(Machine m) {
  return carrier(m[0]<0,2>) + {initialOf(m)};
}

// Connections: yields a relation between states, where there
// is a transition between one state and the other.
public rel[state,state] connOf(Machine m) {
  return m[0]<0,2>;
}

// Events in the machine
public set[event] eventsOf(Machine m) {
  return m[0]<1>;
}

// Transitions, from state to state, triggered by event
public rel[state,event,state] transitionsOf(Machine m) {
  return m[0];
}

// State, command pairs in the machine
public rel[state,command] stateCommandsOf(Machine m) {
  return m[1];
}

// Commands in the machine
public set[command] commandsOf(Machine m) {
  return range(m[1]);
}

// Initial state of the machine
public state initialOf(Machine m) {
  return m[2];
}



public void ppMachine(MachineDef m) {
  top-down-break visit(m) {
	case commandDef(cmds): {
		println("commands");
		for(str s <- cmds)
		  println("  ", s);
		println("end\n");
  	}
	case eventDef(evts): {
		println("events");
		for(str s <- evts)
		  println("  ", s);
		println("end\n");
  	}
	case stateDef(n, acts, trns): {
		println("state ", n);
		if(acts != [])
		  println("  actions ", acts);
		for(trans(evt, stt) <- trns)
		  println("  ", evt, " => ", stt);
		println("end\n");
	}
        case initialDef(n):
		println("initial ", n);
  }
  return;
}

data MachineException = ErrorsDetected(int num);

/** straightforward approach to compiling a machine:
 *  - determine names of all valid states, commands, and events
 *  - visit the tree again and, check that everything is declared, and
 *    add definitions to the internal representation.
 *  - then do some checks to see that the machine is sane
 */
public Machine compileMachine(MachineDef md) {
  set[command] validCmds = {};
  set[event] validEvts = {};
  set[state] validStts = {};
  rel[state, event, state] transitions = {};
  rel[state, command] commands = {}; 
  state currentState;
  state init = "";
  int errors = 0;

  visit(md) {
    case commandDef(cmds):
	validCmds = validCmds + toSet(cmds);
    case eventDef(evts):
	validEvts = validEvts + toSet(evts);
    case stateDef(name, cmds, trns): 
	validStts = validStts + {name};
  }

  top-down visit(md) {
    case trans(evt, st): {
	if(!(evt in validEvts)) {
	  println("Error: Event ", evt, " in transition from ", currentState, " to ", st, " not declared.");
	  errors += 1;
        }
	if(!(st in validStts)) {
	  println("Error: State ", st, " in transition from ", currentState, " on ", evt, " not declared.");
	  errors += 1;
        }
	transitions += {<currentState, evt, st>}; 
    }
    case stateDef(name, cmds, trns): {
 	currentState = name;
	if(cmds != []) for(str c <- cmds)
	  if(!(c in validCmds)) {
	    println("Error: Command ", c, " in state ", name, " not declared.");
            errors += 1;
          }
	  else
	    commands += {<name, c>};
    }
    case initialDef(name): {
        if(init != "") {
		println("Error: Initial state already defined.");
		errors += 1;
	}
	if(!(name in validStts)) {
		println("State ", name, " not declared.");
		errors += 1;
	}
	else
		init = name;
    }
  }

  Machine m = <transitions, commands, init>;

  // final semantic checking of state machine
  if(initialOf(m) == "") {
	println("No initial state declared.");
	errors += 1;
  }
  else {
    unreach = unreachables(m);
    if(unreach != {}) {
      println("Error: The following states are unreachable from the initial one: <unreach>");
      println(" ");
      errors += 1;
    }
    
    nd = ndEvents(m);
    if(nd != {}) {
      println("Error: Non-deterministic events:");
      for(<state s, event e> <- nd)
         println("<s>:<e> => ", transitionsOf(m)[s,e]);
      println(" ");
      errors += 1;
    }
  }

  if(errors > 0)
    throw ErrorsDetected(errors);

  // do a final check of machine integrity -- errors detected here 
  // indicate implementation errors
  checkIntegrity(m);

  return m;
}

public Machine compileMachine2(MachineDef md) {
  set[command] validCommands = {};
  set[event] validEvents = {};
  set[state] validStates = {};
  rel[state, event, state] transitions = {};
  rel[state, command] commands = {}; 
  state currentState;
  state init = "";
  int errors = 0;

  for(d <- md)
    switch(d) {
        case commandDef(cmds):
  	  validCommands += toSet(cmds);
        case eventDef(evts):
	  validEvents += toSet(evts);
        case stateDef(name, cmds, trns): 
	  validStates += {name};
    }

  for(stateDef(name, cmds, trns) <- md) {
    commands += {<name, c> | str c <- cmds};
    transitions += {<name,evt,st> | trans(evt, st) <- trns};
  }

  is = [s | initialDef(s) <- md];
  if(size(is) == 1)
    init = is[0];
  else if(size(is) == 0) {
    println("Initial state not declared.");
    errors += 1;
    init = "";
  }
  else {
    println("Multiple initial states declared: ", is);
    errors += 1;
    init = is[0];
  }


  Machine m = <transitions, commands, init>;

  // check correspondence between declaration and use
  toCheck = [<"states", statesOf(m), validStates>,
             <"events", eventsOf(m), validEvents>,
             <"commands", commandsOf(m), validCommands>];
  for(<str n,set[str] us, set[str] ds> <- toCheck) {
    if((us - ds) != {}) {
      println("Error: The following <n> were not declared: ", us - ds);
      errors += 1;
    }
    if((ds - us) != {})
      println("Warning: The following <n> were declared but not used: ", ds - us);
  }
 
  // final semantic checking of state machine
  unreach = unreachables(m);
  if(unreach != {}) {
    println("Error: The following states are unreachable from the initial one: <unreach>");
    println(" ");
    errors += 1;
  }
    
  nd = ndEvents(m);
  if(nd != {}) {
    println("Error: Non-deterministic events:");
    for(<state s, event e> <- nd)
       println("<s>:<e> => ", transitionsOf(m)[s,e]);
    println(" ");
    errors += 1;
  }
  
  if(errors > 0)
    throw ErrorsDetected(errors);

  // do a final check of machine integrity -- errors detected here 
  // indicate implementation errors
  checkIntegrity(m);

  return m;
}

// Unreachable states in a machine (not reachable from initial state)
public set[state] unreachables(Machine m) {
  return statesOf(m) - reach(connOf(m), {initialOf(m)});
}

// Non-deterministic state/events -- any state/event pair that lead to more
// than one new state
public rel[state,event] ndEvents(Machine m) {
  rel[state,event] result = {};
  ts = transitionsOf(m);
  for(<state s, event e> <- ts<0,1>)
    if(size(ts[s,e]) > 1)
      result += {<s,e>};
  return result;
}
 
// Check that the data invariant for our internal representation holds.
public void checkIntegrity(Machine m) {
  // Transitions must be deterministic
  ts = transitionsOf(m);
  for(<state s, event e> <- ts<0,1>)
    assert size(ts[s,e]) == 1; 

  // States with commands should be defined elsewhere as well
  assert stateCommandsOf(m)<0> <= statesOf(m);
}



public void toJava(Machine m) {
  for(e <- eventsOf(m))
    println("Event <e>Event = new Event(\"<e>\", \"\");");

  println(" ");

  for(c <- commandsOf(m))
    println("Command <c>Command = new Command(\"<c>\", \"\");");

  println(" ");

  for(s <- statesOf(m))
    println("State <s> State = new State(\"<s>\");");

  println(" ");

  for(<state f, event e, state t> <- transitionsOf(m)) 
      println("<f>State.addTransition(<e>Event, <t>State);");

  println(" ");

  for(<state s, command c> <- stateCommandsOf(m))
      println("<s>State.addCommand(<c>Command);");

  println(" ");

  i = initialOf(m);
  println("StateMachine machine = new StateMachine(<i>);");
}

///////////////////////////////////////////////////
// Example machines

// basis for other machines
MachineDef basicMachine = [
	commandDef(["unlockPanel", "lockPanel", "lockDoor", "unlockDoor"]),
	eventDef(["doorClosed", "drawOpened", "lightOn", "doorOpened", "panelClosed"]),
	stateDef("idle", ["unlockDoor", "lockPanel"], [trans("doorClosed", "active")]),
	stateDef("active", [], [trans("drawOpened", "waitingForLight"),
			     trans("lightOn", "waitingForDraw")]),
	stateDef("waitingForLight", [], [trans("lightOn", "unlockedPanel")]),
	stateDef("waitingForDraw", [], [trans("drawOpened", "unlockedPanel")]),
	stateDef("unlockedPanel", ["unlockPanel", "lockDoor"], 
			       [trans("panelClosed", "idle")])];

// Simple gothic security machine for Miss Grant
MachineDef myMachine = basicMachine + [initialDef("idle")];

// With a non-deterministic event
MachineDef ndMachine = basicMachine + [
	stateDef("ndstate", [], [trans("lightOn", "idle"),
                                 trans("lightOn", "unlockedPanel")]),
        initialDef("ndstate")];

// With an unreachable state
MachineDef unMachine = myMachine + [stateDef("foo", [], [trans("lightOn", "idle")])];

// Should also check undeclared states/events/commands, multiple declarations, etc.
