@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}

module lang::saf::Check

import Message;
import IO;
import lang::saf::Implode;
import lang::saf::Parse;
import lang::saf::AST;
import lang::saf::Config;


// todo: non-empty behavior etc. Last should always be "always"?

public set[Message] check(Fighter fighter) {
  errs = checkDuplicateAttributes(fighter);
  for (/Cond c <- fighter) {
    <_, errs> = checkCond(c, errs);
  }
  return errs + checkIdentifiers(fighter);
}
  
public set[Message] checkDuplicateAttributes(Fighter fighter) {
  done = {};
  errs = {};
  for (a:attribute(n, s) <- fighter.specs) {
    if (n in done) 
      errs += {error("Duplicate attribute", a@location)};
    else
      done += {n};
  }
  return errs;
}
  

public set[Message] checkIdentifiers(Fighter fighter) =
  { error("Invalid strength", a@location) |  /a:attribute(n, _) := fighter, n notin STRENGTHS }
  + { error("Invalid condition", c@location) | /c:const(n) := fighter, n notin CONDITIONS }
  + { error("Invalid move", a@location) | /behavior(_, /a:action(n), _) := fighter, n notin MOVES }
  + { error("Invalid fight", a@location) | /behavior(_, _, /a:action(n)) := fighter, n notin FIGHTS };


public tuple[list[str], set[Message]] checkCond(const(n), set[Message] errs) = <[n], errs>;

public tuple[list[str], set[Message]] checkCond(a:and(c1, c2), set[Message] errs) {
  // TODO: make a parameter; or move to config module.
  contradictions = {
    <"near", "far">,
    <"much_stronger", "even">,
    <"much_stronger", "weaker">,
    <"much_stronger", "much_weaker">,    
    <"stronger", "even">,
    <"stronger", "weaker">,
    <"stronger", "much_weaker">,
    <"weaker", "even">,
    <"much_weaker", "even">
  };
  contradictions += contradictions<1,0>; // reflexive closure
  <ns1, errs> = checkCond(c1, errs);
  <ns2, errs> = checkCond(c2, errs);
  ns = ns1 + ns2;
  for (n1 <- ns1, n2 <- ns2) {
    if (<n1, n2> in contradictions) {
      errs += {error("Possible contradiction between <n1> and <n2>", a@location)}; 
    }
    else {
      ns -= [n1, n2];
    }
  }
  return <ns, errs>;
} 

public tuple[list[str], set[Message]] checkCond(o:or(c1, c2), set[Message] errs) {
  <ns1, errs> = checkCond(c1, errs);
  <ns2, errs> = checkCond(c2, errs);
  return <ns1 + ns2, errs>;
}

public void testCheck() {
  wrong = [
  "err1{stronger and weaker[stand punch_low]}",
  "err2{near and far[stand punch_low]}",
  "err3{much_stronger and even[stand punch_low]}",
  "err4{much_stronger and weaker[stand punch_low]}",
  "err5{stronger and even[stand punch_low]}",
  "err6{stronger and much_weaker[stand punch_low]}",
  "err7{weaker and even[stand punch_low]}",
  "err8{much_weaker and even[stand punch_low]}",
  "err9{far or weaker and stronger[stand punch_low]}",
  "err10{stronger and weaker or far[stand punch_low]}",
  "err11{stronger and weaker or far or near[stand punch_low]}",
  "err12{far or near or weaker and stronger[stand punch_low] }",
  "err13{far and near or weaker or stronger or near or far and stronger and even[stand punch_low]}",
  "err14{kickReach = 9 kickReach = 9 far [stand punch_low]}"
  ];
  right = [
  "cor1{stronger or weaker[stand punch_low]}",
  "cor2{near or far[stand punch_low]}",
  "cor3{much_stronger or even[stand punch_low]}",
  "cor4{much_stronger or weaker[stand punch_low]}",
  "cor5{stronger or even[stand punch_low]}",
  "cor6{stronger or much_weaker[stand punch_low]}",
  "cor7{weaker or even[stand punch_low]}",
  "cor8{much_weaker or even[stand punch_low]}",
  "cor9{far and weaker or stronger[stand punch_low]}",
  "cor10{stronger or weaker and far[stand punch_low]}",
  "cor11{stronger or weaker and far or near[stand punch_low]}",
  "cor12{far or near or weaker or stronger[stand punch_low]}",
  "cor13{far or near or weaker or stronger and near or far and stronger or even[stand punch_low]}",
  "cor14{always [choose(stand crouch run_towards run_away) punch_low]}",
  "cor15{always [choose(stand crouch) choose(punch_low kick_high)]}",
  "cor16{far or near and weaker [choose(run_towards walk_towards) choose(punch_low kick_low)]}"
  ];
  
  for (f <- wrong) {
    errs = check(implode(parse(f)));
    if (errs == {}) {
      println("expected errors for: <f>");
    }
  }

  for (f <- right) {
    errs = check(implode(parse(f)));
    if (errs != {}) {
      println("expected no errors for: <f>");
      println("\t<errs>");
    }
  }

}