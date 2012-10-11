@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl}

module lang::saf::Config

import lang::saf::AST;
import util::Math;
import IO;

public int ARENASIZE     = 60;  //Size of the arena in m.
public int FIGHTDURATION = 100; //Battle duration in turns.
public int DELAY         = 100; //Duration between battle turns in ms.
public int NEARDISTANCE  = 9;   //Opponent distance regarded as near.
public int WALKDISTANCE  = 1;   //Distance a fighter can walk.
public int RUNDISTANCE   = 8;   //Distance a fighter can run.
public int MUCHPOWER     = 9;   //Power difference ragarded as much power.


public int signum(int n) {
  if (n < 0) {
    return -1;
  }
  if (n > 0) {
    return 1;
  }
  return 0;
}

/// TODO: must be in other module
data FState = fstate(Fighter fighter,
              str color,
              int health,
              int position,
              bool crouch,
              bool jump,
              str fight,
              str move,
              str result);

public int getAttr(FState f, str name) = getAttr(f.fighter, name);
public str state2str(FState s) = "<f.name>:p<f.position>\th<f.health>";
//////

public set[str] CONDITIONS = {"stronger", "weaker", "much_stronger", 
  "much_weaker", "even", "near", "far", "always"};


public set[str] MOVES = {"jump", "crouch", "stand", "run_towards", 
  "run_away", "walk_towards", "walk_away"};

public set[str] FIGHTS = {"punch_low", "punch_high", "kick_low", 
  "kick_high", "block_low", "block_high"};
  
public set[str] STRENGTHS = {"punchReach", "kickReach", "kickPower", "punchPower"};

public int distance(FState f1, FState f2) = abs(f2.position - f1.position); 
public int totalPower(FState f) = kickPower(f) + punchPower(f);
public bool isDead(FState f) = f.health <= 0;
public bool inPunchReach(FState f1, FState f2) = distance(f1, f2) <= getAttr(f1, "punchReach");
public bool inKickReach(FState f1, FState f2) = distance(f1, f2) <= getAttr(f1, "kickReach");
public bool stronger(FState f1, FState f2) = totalPower(f1) > totalPower(f2);
public bool muchStronger(FState f1, FState f2) = totalPower(f1) > totalPower(f2) + MUCHPOWER;
public bool weaker(FState f1, FState f2) = totalPower(f1) < totalPower(f2);
public bool muchWeaker(FState f1, FState f2) = totalPower(f1) + MUCHPOWER < totalPower(f2);
public bool even(FState f1, FState f2) = totalPower(f1) == totalPower(f2);
public bool near(FState f1, FState f2) = distance(f1, f2) <= NEARDISTANCE;
public bool far(FState f1, FState f2) = distance(f1, f2) > NEARDISTANCE;
public bool always(FState f1, FState f2) = true;

public int punchPower(FState f) = getAttr(f, "punchPower");
public int kickPower(FState f) = getAttr(f, "kickPower");

public bool dispatchCondition("stronger", FState f1, FState f2) = stronger(f1, f2);
public bool dispatchCondition("weaker", FState f1, FState f2) = weaker(f1, f2);
public bool dispatchCondition("much_stronger", FState f1, FState f2) = muchStronger(f1, f2);
public bool dispatchCondition("much_weaker", FState f1, FState f2) = muchWeaker(f1, f2);
public bool dispatchCondition("even", FState f1, FState f2) = even(f1, f2);
public bool dispatchCondition("near", FState f1, FState f2) = near(f1, f2);
public bool dispatchCondition("far", FState f1, FState f2) = far(f1, f2);
public bool dispatchCondition("always", FState f1, FState f2) = true;

public default bool dispatchCondition(str c, FState f1, FState f2) { throw "Invalid condition: <c>"; }

int dirTo(FState f1, FState f2) = signum(f2.position - f1.position);
FState moveTo  (FState f1, FState f2, int dist) = f1[ position = f1.position + dirTo(f1,f2) * dist]; 
FState moveFrom(FState f1, FState f2, int dist) = f1[ position = f1.position - dirTo(f1,f2) * dist]; 

public FState dispatchMove("run_towards", FState f1, FState f2)  = moveTo(f1,f2,RUNDISTANCE);
public FState dispatchMove("run_away", FState f1, FState f2)     = moveFrom(f1,f2,RUNDISTANCE);
public FState dispatchMove("walk_towards", FState f1, FState f2) = moveTo(f1,f2,WALKDISTANCE);
public FState dispatchMove("walk_away", FState f1, FState f2)    = moveFrom(f1,f2,WALKDISTANCE); 
public FState dispatchMove("crouch", FState f1, FState f2)       = f1[crouch = true]; 
public FState dispatchMove("jump", FState f1, FState f2)         = f1[jump = true]; 
public FState dispatchMove("stand", FState f1, FState f2)         = f1; 


public default FState dispatchCondition(str c, FState f1, FState f2) = f1;

public tuple[FState,FState] dispatchFight("punch_low", FState f1, FState f2) =
  engage(f1, f2, "jump", "block_low", inPunchReach, punchPower);

public tuple[FState,FState] dispatchFight("punch_high", FState f1, FState f2) =
  engage(f1, f2, "crouch", "block", inPunchReach, punchPower);

public tuple[FState,FState] dispatchFight("kick_low", FState f1, FState f2) =
  engage(f1, f2, "jump", "block", inKickReach, kickPower);

public tuple[FState,FState] dispatchFight("kick_high", FState f1, FState f2) =
  engage(f1, f2, "crouch", "block_high", inKickReach, kickPower);
  
public default tuple[FState,FState] dispatchFight(str _, FState f1, FState f2) = <f1, f2>;


public tuple[FState,FState] engage(FState f1, FState f2, str counterMove, str counterAction, bool(FState, FState) inReach, int(FState) power) {
  if (inReach(f1, f2) && f2.move != counterMove) {
      if (f2.fight == counterAction) {
        f1.result = "block"; 
      }
      else {
        f1.result = "hit";
        f2.health -= power(f1);
        if (f2.health < 0) {
          f2.health = 0;
        }
      }
  }
  return <f1, f2>;
}

int constrainInt(int low, int high, int v) = max(low,min(v,high));

public tuple[FState,FState] constrain(FState left, FState right) {
	left.position = constrainInt(0,right.position-1,left.position);
	right.position = constrainInt(left.position+1, ARENASIZE,right.position);
	return <left,right>;
}
