@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl}

module lang::saf::Render

import vis::Figure;
import vis::Render;

import util::Math;
import Map;
import IO;

Figure dot(num x, num y){ return point(align(x, y));}

public Figure hand(num x, num y) = point(align(x,y));
public Figure punchHand(num x, num y) = box(shrink(0.2, 0.2), align(x,y));
public Figure blockHand(num x, num y) = box(shrink(0.1, 0.7), align(x,y));

public Figure mirror(Figure fig) = 
  visit (fig) {
    case halign(x) => halign(1.0 - x)
  };


public Figure arms(num fromx, num fromy, num tox, num toy, Figure(num, num) hand) =
  overlay([dot(fromx, fromy), hand(tox, toy)], shapeConnected(true));

public map[str, Figure] ARMS = (
  "normal": arms(0.25, 0.25, 0.75, 0.25, hand),
  "punch_high": arms(0.25, 0.375, 0.875, 0.125, punchHand), 
  "punch_low": arms(0.25, 0.125, 0.875, 0.375, punchHand),
  "block": arms(0.25, 0.375, 0.875, 0.125, blockHand), 
  "block_low": arms(0.25, 0.125, 0.875, 0.375, blockHand)
);

public Figure legs(num tox, num toy, Figure(num, num) foot) =
  overlay([dot(0.0, 1.0), dot(0.5, 0.5), foot(tox, toy)], shapeConnected(true));

public Figure foot(num x, num y) = dot(x, y);
public Figure kickFoot(num x, num y) = ellipse(shrink(0.2, 0.2), align(x,y));

public map[str, Figure] LEGS = (
  "normal": legs(1.0, 1.0, foot),
  "kick_high": legs(1.0, 0.0, kickFoot), 
  "kick_low": legs(1.0, 0.5, kickFoot)
);


public Figure body(Figure arms) {
  romp = overlay([dot(0.5, 0.0), dot(0.5, 1.0)], shapeConnected(true));
  return overlay([arms, romp]);
}

public Figure groin(Figure legs) {
  g = overlay([dot(0.5, 0.0), dot(0.5, 0.5)], shapeConnected(true));
  return overlay([legs, g]);
}

public Figure stickMan(str name, Figure arms, Figure legs) {
  // TODO: health color.
  head = ellipse(text(name), bottom());
  return vcat([head, body(arms), groin(legs)],aspectRatio(1.0/3.0));
}

public Figure stickMan(str name, str fight) {
  a = ARMS[(fight in ARMS) ? fight : "normal"];
  l = LEGS[(fight in LEGS) ? fight : "normal"];
  return stickMan(name, a, l);
}


public void main() {
  n = "chuck";
  figs = [ stickMan(n, ARMS["normal"], LEGS["normal"])]
       + [ stickMan(n, ARMS[a], LEGS["normal"]) | a <- ARMS, a != "normal" ]
       + [ stickMan(n, ARMS["normal"], LEGS[l]) | l <- LEGS, l != "normal" ];
  render(hcat([box(f, size(60, 150), gap(10), resizable(false)) | f <- figs]));
}



