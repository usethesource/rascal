module demo::lang::turing::l2::format::Format

import demo::lang::turing::l2::ast::Turing;
import lang::box::util::Box;
import List;

// fool the type system...(work around)
public default Box stat2box(Statement s) = L("");

public Box turing2box(Program p) = stats2box(p.statements);

public Box stats2box(list[Statement] ss) {
  result = while (ss != []) {
    <h, ss> = headTail(ss);
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

public Box stat2box(jumpAlwaysLabel(n)) = H([KW(L("J_")), VAR(L(n))])[@hs=1];
public Box stat2box(jumpSetLabel(n)) = H([KW(L("J1")), VAR(L(n))])[@hs=1];
public Box stat2box(jumpUnsetLabel(n)) = H([KW(L("J0")), VAR(L(n))])[@hs=1];

public Box stat2box(loop(n, ss)) 
  = V([
     H([KW(L("REP")), L("<n>"), L("{")])[@hs=1],
     I([stats2box(ss)]),
     L("}")
    ]);
       

// TODO: maybe move to L1 formatter
public Box stat2box(writeSet()) = KW(L("W1"));
public Box stat2box(writeUnset()) = KW(L("W0"));
public Box stat2box(moveForward()) = KW(L("MF"));
public Box stat2box(moveBackward()) = KW(L("MB"));



// BUG: not allowed
//public Box turing2box([label(l1), *stats1, label(l2), *stats2]) 
//  = V([
//      I([
//         H([KW(L("L")), VAR(L(n))])[@hs=1],
//         *turing2box(stats1)
//         ]),
//      *tail
//     ])
//  when
//     V(tail) := turing2box([label(l2), *stats2]);

//
//public Box stats2box(list[Statement] ss) 
// = V([stats2box(stats1), stats2box([label(l1), *stats2])])
// when
//    [*stats1, label(l1), *stats2] := ss,
//    [*_, label(_), *_] !:= stats1; // no before label


//public Box stats2box(list[Statement] ss) {
//  outer = V([]);
//  pop = false;
//  indent = I([]);
//  lastLabel = L("");
//  indenting = false;
//  for (s <- ss) {
//    if (s is label) {
//      if (indenting) {
//        outer.v += V([lastLabel,indent]);
//        indent = I([]);
//      }
//      lastLabel = H([KW(L("L")), VAR(L(s.name))]);
//    }
//    else {
//      b = stat2box(s);
//      if (indenting) {
//        indent.i += [b];
//      }
//      else {
//        outer.v += [b];
//      }
//    }
//  }
//  return outer;
//}


//public Box stats2box(list[Statement] ss) 
//  = V([H([KW(L("L")), VAR(L(l1))])[@hs=1], I([stats2box(stats1)]), stats2box([label(l2), *stats2])])
//  when
//     [label(l1), *stats1, label(l2), *stats2] := ss,
//     [*_, label(_), *_] !:= stats1; // no in-between label
//     
//public Box stats2box(list[Statement] ss) 
//  = V([H([KW(L("L")), VAR(L(l1))])[@hs=1], I([stats2box(stats1)])])
//  when
//     [label(l1), *stats1] := ss,
//     [*_, label(_), *_] !:= stats1; // no next label
//
//   
//   
