module lang::kanren::mini::Test

import lang::kanren::mini::MiniKanren;
import lang::kanren::mini::Goals;

test bool testAllFailure() {
  q = fresh();
  return infer(q, [failure]) == [];
}


test bool testAllEquality(value v) {
  q = fresh();
  return infer(q, [equ(v, q)]) == [v];
}


test bool testAllWithFail(value x){
  q = fresh();
  return infer(q, [\all([failure, equ(x, q)])]) == [];
}

test bool testAllWithSucceed(value x) {
  q = fresh();
  return infer(q, [\all([succeed, equ(x, q)])]) == [x];
}


test bool testEqComm(value v) {
  q = fresh();
  return infer(q, [\all([equ(v, q)])]) ==
      infer(q, [\all([equ(q, v)])]);
}

test bool testUnbound(Var x, Var y) {
  q = fresh();
  return infer(q, [\all([equ([x, y], q)])]) == [["_.0", "_.1"]];
}

test bool testUnboundRepeated() {
  q = fresh();
  x = fresh();
  y = x;
  x = fresh();
  return infer(q, [\all([equ([y, x, y], q)])]) 
    == [["_.0", "_.1", "_.0"]];
}

test bool testAnyFail1() {
  q = fresh(); x = fresh();
  return infer(q, [equ(x == q, q)]) == [false]; 
}

test bool testAnyFail2() {
  q = fresh();
  return infer(q, [\any([\all([failure, succeed]),
                  \all([succeed, failure])])]) == [];
}

test bool testAnyFail3() {
  q = fresh();
  return infer(q, [\any([\all([failure, failure]),
                         \all([succeed, succeed])])]) 
                         == ["_.0"];
}


test bool testAnyFail4() {
  q = fresh();
  return infer(q, [\any([\all([succeed, succeed]),
                         \all([failure, failure])])]) 
                          == ["_.0"];
}


test bool testAllSolutions(str x, str y) {
  q = fresh();
  r = infer(q, [\any([\all([equ(x, q), succeed]),
                         \all([equ(y, q), succeed])])], n = 2);

  //println("R = <r>");                        
                          
  return r == [x, y];
}

test bool testPairo() {
  q = fresh();
  r = infer(q, [\all([pairo([q, q]), equ(true, q)])]);
  return r == [true];
}

test bool testPairoEmpty() {
  q = fresh();
  r = infer(q, [\all([pairo([]), equ(true, q)])]);
  return r == [];
}

test bool testListo1() {
  q = fresh();
  r = infer(q, [listo(["a", ["b", [q, ["d", []]]]])]);
  return r == ["_.0"];
}

test bool testListo2() {
  q = fresh();
  r = infer(q, [listo(["a", ["b", ["c", q]]])], n = 5);
  return r == [[],
                  ["_.0", []],
                  ["_.0", ["_.1", []]],
                  ["_.0", ["_.1", ["_.2", []]]],
                  ["_.0", ["_.1", ["_.2", ["_.3", []]]]]];
}

test bool testNodes1() {
  q = fresh();
  r = infer(q, [equ("f"(23, q), "f"(q, 23))]);
  return r == [23];
}


test bool testAnyo1() {
  q = fresh();
  r = infer(q, [anyo(\any([equ(1, q), equ(2, q), equ(3, q)]))], n = 10);
  return r == [1,2,3,1,2,3,1,2,3,1];
}
