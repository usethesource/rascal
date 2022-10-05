module demo::lang::turing::l2::format::Format

import demo::lang::turing::l2::ast::Turing;
import lang::box::util::Box;
import List;

public Box turing2box(Program p) = stats2box(p.statements);

// TODO: maybe move to L1 formatter
public Box stat2box(writeSet()) = KW(L("W1"));
public Box stat2box(writeUnset()) = KW(L("W0"));
public Box stat2box(moveForward()) = KW(L("MF"));
public Box stat2box(moveBackward()) = KW(L("MB"));

public Box stat2box(jumpAlwaysLabel(n)) = H([KW(L("J_")), VAR(L(n))])[@hs=1];
public Box stat2box(jumpSetLabel(n)) = H([KW(L("J1")), VAR(L(n))])[@hs=1];
public Box stat2box(jumpUnsetLabel(n)) = H([KW(L("J0")), VAR(L(n))])[@hs=1];

public Box stat2box(loop(n, ss)) 
  = V([
     H([KW(L("REP")), L("<n>"), L("{")])[@hs=1],
     I([stats2box(ss)]),
     L("}")
    ]);
       
public Box stats2box(list[Statement] ss) {
  result = while (ss != []) {
    <h, ss> = pop(ss);
    if (h is label) {
      kids = takeWhile(ss, bool(Statement s) { return !(s is label); });
      ss = drop(size(kids), ss);
      append V([H([KW(L("L")), VAR(L(h.name))]), 
                I([V([stat2box(k) | k <- kids])])]);
    }
    else {
      append stat2box(h);
    }
  }
  return V(result);
}
       
