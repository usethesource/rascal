module demo::lang::MissGrant::Rename

import demo::lang::MissGrant::MissGrant;
import util::Prompt;

import ParseTree;

// this code is bulky because it avoids using concrete syntax

public str rename(Controller ctl, loc sel) {
  newName = prompt("Enter new name: ");
  newId = parse(#Id, newName);
  if (treeFound(Event e) := treeAt(#Event, sel, ctl)) {
    ctl = renameEvent(ctl, e.name, newId);
  }
  else if (treeFound(State s) := treeAt(#State, sel, ctl)) {
    ctl = renameState(ctl, s.name, newId);
  }
  else if (treeFound(Command c) := treeAt(#Command, sel, ctl)) {
    ctl = renameCommand(ctl, c.name, newId);
  }
  else {
    alert("No state, event or command selected");
  }
  return "<ctl>";
}


private Controller renameEvent(Controller ctl, Id oldName, Id newName) {
  // DOES NOT WORK: parse trees are not enumerable?
  if (/Event e <- ctl, e.name == newName) {
    alert("Event <newName> already exists");
    return ctl;
  } 
  return visit (ctl) {
    case Transition t: {
      if (oldName == t.event) {
        t.event = newName;
      }
      insert t;
    }
    case Event e: {
      if (oldName == e.name) {
        e.name = newName;
      }
      insert e;
    }
    case ResetEvents rs => visit (rs) {
      case Id i => newName when oldName == i 
    }
  }
}

private Controller renameCommand(Controller ctl, Id oldName, Id newName) {
  if (/Command c <- ctl, c.name == newName) {
    alert("Command <newName> already exists");
    return ctl;
  } 
  return visit (ctl) {
    case Command c: {
      if (oldName == c.name) {
        c.name = newName;
      }
      insert c;
    }
    case State s => visit (s) {
       case Actions a => visit (a) { 
          case Id i => newName when oldName == i
       }
    }
  }
}

private Controller renameState(Controller ctl, Id oldName, Id newName) {
  if (/State s <- ctl, s.name == newName) {
    alert("State <newName> already exists");
    return ctl;
  } 
  return visit (ctl) {
    case Transition t: {
      if (oldName == t.state) {
        t.state = newName;
      }
      insert t;
    }
    case State s: {
      if (oldName == s.name) {
        s.name = newName;
      }
      insert s;
    }
  }
}

