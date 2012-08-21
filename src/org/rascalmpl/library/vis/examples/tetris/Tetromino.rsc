@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}

// see http://en.wikipedia.org/wiki/Tetromino
module vis::examples::tetris::Tetromino

import List;
import util::Math;


// we define tetromino shapes as a list of the (local) coordinates of its blocks 
alias Block = tuple[int row,int col];
alias Blocks = list[Block];

Blocks mirrorCols(Blocks b, int maxCol) = [ <r,maxCol - c> | <r,c> <- b];
Blocks mirrorRows(Blocks b, int maxRow) = [<maxRow - r,c>  | <r,c> <- b];
Blocks transpose(Blocks b)              = [ <c,r>          | <r,c> <- b];    

// A tetromino is defined by it's shape, all tetrominos are defined below
data Shape = shape(Blocks blocks, int size, int nrRotations);

// CW = clockwise, CCW = counterClockWise
Blocks rotateCW (Blocks t, int size) = transpose(mirrorRows(t,size));    
Blocks rotateCCW(Blocks t, int size) = transpose(mirrorCols(t,size));    

list[Blocks] rotations(Shape s) {
    Blocks t = s.blocks;
    return for(_ <- [1..s.nrRotations]){
        append t;
        t = rotateCW(t,s.size-1);
    }
}    
/* if a tetromino does not fit after rotation, we try to move the tetromino 
  by different offsets. The rule is that an offset is valid if it overlaps
  with at least one block of the tetromino before rotation.
  
  this allows wall-kicks, floor kicks and twists
  see http://tetris.wikia.com/wiki/Wall_kick */

alias Offset =  tuple[int row,int col];
alias Offsets = list[Offset];

list[Offset] offsets(int maxRow, int maxCol) = 
    [ <i,j> | i <- [-maxRow .. maxRow], j <- [-maxCol .. maxCol]];

int distSqrd(Offset a) = a.row*a.row + a.col*a.col;
// we try offsets with a smaller distance first, 
// then on a higher row (=lower on board)
bool hasHigherPriority(Offset a, Offset b) {
    dista = distSqrd(a);
    distb = distSqrd(b);
    return (dista != distb) ? dista < distb : a.row > b.row;
}
Offsets prioritizedOffsets(int maxRow, int maxCol) = 
    sort(offsets(maxRow,maxCol), hasHigherPriority);
Offsets prioritizedOffsets(int size) = prioritizedOffsets(size, size);
    
public Block  move(Block a, Offset off)   = < a.row + off.row, a.col + off.col>;
public Blocks move(Blocks as, Offset off) = [ move(a,off) | a <- as]; 

bool overlaps(Blocks a, Blocks b) = !isEmpty(a & b);

bool isLegalOffset(Blocks beforeRotate, Blocks afterRotate,
                   Offset offset) =
    overlaps(beforeRotate, move(afterRotate,offset));
    
Offsets legalOffsets(Blocks beforeRot, Blocks afterRot, int size) =
    [ off | off <- prioritizedOffsets(size), 
      isLegalOffset(beforeRot, afterRot, off) ]; 

/* An orientation is a rotated state of the tetromino,
   it includes all the data to rotate the tetromino, namely the position
   of the block and the valid offsets */
data Orientation = orientation(
    Blocks blocks, 
    list[Offset] offsetsAfterCW,
    list[Offset] offsetsAfterCCW);
    
// both sorted so that i+1 is the CW rotation of i 
alias Rotations = list[Blocks];
alias Orientations = list[Orientation];

Orientation makeOrientation(list[Blocks] rots, int i, int tsize) =
    orientation(rots[i], 
                legalOffsets(rots[i-1 mod size(rots)],rots[i],tsize), 
                legalOffsets(rots[i+1 mod size(rots)],rots[i],tsize));

data Tetromino = 
    tetromino(list[Orientation] orientations, int size);

Tetromino makeTetromino(Shape s) {
    rts = rotations(s);
    return tetromino([ makeOrientation(rts,i,s.size) | i <- index(rts)],s.size);
}

// Below we define all tetrominos
Shape iS = shape([ <0,2>, <1,2>, <2,2>, <3,2>],4,2);
Shape lS = shape([ <0,1>, <1,1>, <2,1>, <2,2>],3,4);
Shape zS = shape([ <1,0>, <1,1>, <0,1>, <0,2>],3,2);
Shape oS = shape([ <0,0>, <0,1>, <1,0>, <1,1>],2,1);
Shape tS = shape([ <1,0>, <1,1>, <1,2>, <0,1>],3,4);
Shape jS = shape([ <0,1>, <1,1>, <2,1>, <2,0>],3,4);
Shape sS = shape([ <0,0>, <0,1>, <1,1>, <1,2>],3,2);    
list[Shape] shapes =[iS,lS,zS,oS,tS,jS,sS];
public list[Tetromino] tetrominos = [ makeTetromino(s) | s <- shapes];

public int randomTetromino() = arbInt(size(tetrominos));

// code for showing a conconical representation of a tetromino
// i.e. the next or stored tetromino
public int maxTetrominoWidth = 3;
public int maxTetrominoHeight = 4;
alias CanconicalTetrominoRep = tuple[Blocks blocks, int width,int height];

public CanconicalTetrominoRep getCanconicalRep(int tetromino){
    blocks = shapes[tetromino].blocks;
    rows = [r | <r,_> <- blocks];
    cols = [c | <_,c> <- blocks];
    <minRow,maxRow> = <min(rows),max(rows)>;
    <minCol,maxCol> = <min(cols),max(cols)>;
    return <move(blocks,<-minRow,-minCol>),maxRow-minRow+1, maxCol - minCol+1>;
}
