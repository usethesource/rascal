@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}

// a placed tetromino is a tetromino placed on the board
module vis::examples::tetris::PlacedTetromino

import vis::examples::tetris::Tetromino;
import vis::examples::tetris::PlayField;
import List;

data PlacedTetromino = 
   placedTetromino(Coordinate location, int tetromino, int rotState);
   
Blocks blockIndexes(PlacedTetromino t) =
   move(getTetromino(t).orientations[t.rotState].blocks, t.location);

Tetromino getTetromino(PlacedTetromino t)     = tetrominos[t.tetromino];
Orientation getOrientation(PlacedTetromino t) = 
   getTetromino(t).orientations[t.rotState];

// place tetromino at the top in the center
// such that the bottom of the tetromino is visible
public PlacedTetromino 
initialPT(int tetromino, PlayField b, int nrInvisbleLines) {
   res = placedTetromino(<0, nrCols(b)/2 - (tetrominos[tetromino].size/2)>,
               tetromino,0);
   while(!any(<r,c> <- blockIndexes(res),r >= nrInvisbleLines)){ 
      res = move(res,<1,0>);
   }
   return res;
} 

PlacedTetromino move(PlacedTetromino t, Offset off) =  
   t[location = move(t.location,off)];
PlacedTetromino down(PlacedTetromino t)            = move(t,<1,0>);
PlacedTetromino rotate(PlacedTetromino t, int rot) =
   t[rotState = t.rotState + rot mod size(getTetromino(t).orientations)]; 
PlacedTetromino rotateCW(PlacedTetromino t)        = rotate(t,1);
PlacedTetromino rotateCCW(PlacedTetromino t)       = rotate(t,-1);

public bool canPlace(PlacedTetromino t, PlayField b) =
	 isEmpty(b,blockIndexes(t));
public bool supported(PlacedTetromino t,PlayField b) = !canPlace(down(t),b);

PlacedTetromino tryPlace(PlayField b, PlacedTetromino fallback,
                   PlacedTetromino t, list[Offset] offsets){
   for(off <- offsets){
      moved = move(t,off);
      if (canPlace(moved,b)) {
         return moved;
      }
   }
   return fallback; // no offset possible
}

PlacedTetromino moveOnField(PlayField b, PlacedTetromino t, Offset off) =
   tryPlace(b,t,t, [off]);

public PlacedTetromino moveDown(PlayField b, PlacedTetromino t) =
    moveOnField(b,t,<1,0>);
public PlacedTetromino moveLeft(PlayField b, PlacedTetromino t) = 
   moveOnField(b,t,<0,-1>);
public PlacedTetromino moveRight(PlayField b, PlacedTetromino t) = 
   moveOnField(b,t,<0,1>);
public PlacedTetromino rotateCW(PlayField b, PlacedTetromino t) = 
   tryPlace(b, t, rotateCW(t), getOrientation(rotateCW(t)).offsetsAfterCW);
public PlacedTetromino rotateCCW(PlayField b, PlacedTetromino t) = 
   tryPlace(b, t, rotateCCW(t), getOrientation(rotateCCW(t)).offsetsAfterCCW);
   
public PlacedTetromino drop(PlacedTetromino t,PlayField b) {
   while (!supported(t,b)) { t = down(t); }
   return t;
}   

public PlayField add(PlayField b, PlacedTetromino t) = 
   add(b, blockIndexes(t),block(t.tetromino));
public PlayField addPrediction(PlayField b, PlacedTetromino t) = 
   add(b, blockIndexes(t),prediction(t.tetromino));
