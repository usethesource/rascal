@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl}

module lang::saf::Run

import lang::saf::AST;
import lang::saf::Parse;
import lang::saf::Implode;
import lang::saf::Render;
import lang::saf::Config; // TODO: remove cycle

import util::Math;
import List;
import IO;
import vis::Figure;
import vis::Render;

public bool eval(and(c1, c2), FState f1, FState f2) = eval(c1, f1, f2) && eval(c2, f1, f2);  
public bool eval(or(c1, c2), FState f1, FState f2) = eval(c1, f1, f2) || eval(c2, f1, f2);
public bool eval(const(n), FState f1, FState f2) = dispatchCondition(n, f1, f2);

public tuple[str move, str fight] pickBehavior(FState f1, FState f2) {
  bs = [ <m, f> | behavior(c, mv, ft) <- f1.fighter.specs, eval(c, f1, f2),
         /str m <- mv , /str f <- ft]; // zip choose nodes
  n = arbInt(size(bs));
  return bs[n];
}

public tuple[FState, FState] step(FState f1, FState f2) {
  <f1.move, f1.fight> = pickBehavior(f1, f2);
  <f2.move, f2.fight> = pickBehavior(f2, f1);
  // TODO: do move/fight sequentially
  f1 = dispatchMove(f1.move, f1, f2);
  <f1,f2> = constrain(f1,f2);
  f2 = dispatchMove(f2.move, f2, f1);
  <f1,f2> = constrain(f1,f2);
  <f1, f2> = dispatchFight(f1.fight, f1, f2);
  <f2, f1> = dispatchFight(f2.fight, f2, f1);
  return <f1, f2>;
}

public Figure state2figure(FState f) {
  str c = "black";
  if(f.health > 50)
    c = "green";
  else if(f.health <= 50 && f.health > 20)
    c = "orange";
  else if(f.health <= 20 && f.health >= 0) 
    c = "red";

  label = "<f.fighter.name>:                                 
          'Health  : <f.health>
          'Position: <f.position>
          'Move    : <f.move>
          'Fight   : <f.fight>
          'Result  : <f.result>\n";

  return box(
    vcat([
        text(label),        
        vcat([box(hshrink(0.5),vshrink((100.0 - f.health) / 100.0)),
            box(hshrink(0.5), vshrink((f.health)/100.0),fillColor(c))])]),
    lineColor("silver"),
    lineWidth(5));
}

public Figure fighter2figure(FState f, FProperty props ...) {
  man = stickMan(f.fighter.name, f.fight);
  man.props += [vshrink(0.70)];
  switch(f.move)
  {
    case "jump": man.props += [top()];
    case "crouch": man.props += [bottom()];
  }
  return space(man,props);
}


real FIGHTER_SHRINK = 0.2;

public Figure positionedFighters(FState f1, FState f2, FProperty props ...) {
	leftShrink = (toReal(f1.position-1) / toReal(ARENASIZE)) * (1.0 - 2.0 * FIGHTER_SHRINK);
	rightShrink = (toReal(ARENASIZE - f2.position) / toReal(ARENASIZE))  * (1.0 - 2 * FIGHTER_SHRINK);
	areaRight = toReal(f1.position + 1);
	return hcat([
		space(hshrink(leftShrink)),
		fighter2figure(f1,hshrink(FIGHTER_SHRINK)),
		space(),
		mirror(fighter2figure(f2,hshrink(FIGHTER_SHRINK))),
		space(hshrink(rightShrink))
	],props);
  return x;
}


public Figure arena(FState f1, FState f2, str log) =
	hcat([ state2figure(f1),positionedFighters(f1,f2,hshrink(0.6)),state2figure(f2)]);

public void mainTest(str path) {
  game(|project://<path>/challenging.saf|, |project://<path>/unbeatable.saf|);
}

public void game(loc src1, loc src2) {
  stickManWidth = 6;
  f1 = implode(parse(src1));
  f2 = implode(parse(src2));
  game(f1, f2);
}

public void game(Fighter f1, Fighter f2) {
  fs1 = fstate(f1, "red", 100, 1, false, false, "", "", "");
  fs2 = fstate(f2, "teal", 100, ARENASIZE , false, false, "", "", "");
  
  str log = "";
  
  void doStep() {
    <fs1, fs2> = step(fs1, fs2);
    log += "Executed step: <fs1.move> / <fs2.move>; <fs1.fight> / <fs2.fight>\n";
  }
  
  TimerAction myTimer(stopped(n)) = isDead(fs1) || isDead(fs2) ? stop() : restart(DELAY - n);
  default TimerAction myTimer(_) = noChange();
  
  Figure newArena() = arena(fs1, fs2, log); 
  
  render( title("Super Awesome Fighters!", 
        box(computeFigure(Figure() { return arena(fs1, fs2, log); } ),
        timer(myTimer, doStep),
        lineWidth(0))));
}