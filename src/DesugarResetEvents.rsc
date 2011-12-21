module demo::lang::MissGrant::DesugarResetEvents

import  demo::lang::MissGrant::AST;

public Controller desugarResetEvents(Controller ctl) =
  visit (ctl) {
    case state(n, as, ts) => state(n, as, ts + [ transition(e, "idle") | e <- ctl.resets ])
  };
