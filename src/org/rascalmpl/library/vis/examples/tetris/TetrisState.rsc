@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}
module vis::examples::tetris::TetrisState

import Integer;
import List;
import Number;
import IO;

data Maybe[&A] = nothing() | just(&A val);
data RotationDirection = clockwise() | counterClockwise();

public int defaultSpinMax = 25; // my take on the infinte spin controversy (see wikipedia tetris): finite spin :)

data CellState 	= empty()
				| predicted(int color)
				| full()
				| full(int color);
						

alias Board = list[list[CellState]]; // stored row[column]
alias Coordinate = tuple[num,num];

data Tetromino = tetromino(Board board, Coordinate rotationPoint, int color);

data PlacedTetromino = placedTetromino(Coordinate location, Tetromino tetromino); // location specifies the location of center!

data TetrisState = 
	tetrisState(
		int score, 
		int level,
		Board board,
		Board boardWithCurrentTetromino,
		PlacedTetromino currentTetromino,
		list[Tetromino] next,
		Maybe[Tetromino] stored,
		bool alreadySwapped,
		bool gameOver,
		int spinCounter,
		int maxSpinCounter
	);
	
data Action = 
	  advanceGravity()
	| rotateClockwise()
	| rotateCounterClockwise()
	| moveLeft()
	| moveRight()
	| swapStored()
	| drop();
	


PlacedTetromino initialPlacedTetromino(Tetromino t, Board b){
	return  placedTetromino(<nrRows(t.board) - (nrRows(t.board) - t.rotationPoint[0]),(nrColumns(b)-1)/2.0>,t);
}

public int nrRows(Board b){ 
	return size(b); 
}

public int nrColumns(Board b){ 
	return size(b[0]); 
}

Coordinate addCoordinates(Coordinate lhs, Coordinate rhs){ 
	return <lhs[0] + rhs[0],lhs[1] + rhs[1]>; 
}

Coordinate substractCoordinates(Coordinate lhs, Coordinate rhs){
	 return <lhs[0] - rhs[0],lhs[1] - rhs[1]>; 
}

CellState getLocation(Board b,Coordinate c){ 
	return b[toInt(c[0])][toInt(c[1])] ? empty(); 
}

Board setLocation(Board b,Coordinate c,CellState state){
	b[toInt(c[0])][toInt(c[1])] = state;
	return b;
}

Tetromino randomTetromino(){
	return allTetrominos[arbInt(size(allTetrominos))];
}

Board mirrorTetrominoBoard(Board t){
	return for(row <- [0 .. nrRows(t)-1]){
		append for(column <- [0 .. nrColumns(t)-1]){
			append t[row][nrColumns(t)-1 - column];
		}
	}
}

TetrominoShape mirrorTetrominoShape(TetrominoShape t){
	t.board = mirrorTetrominoBoard(t.board);
	t.rotationPoint[1] = nrColumns(t.board) - t.rotationPoint[1];
	return t;
}

data TetrominoShape = tetrominoShape(Board board,Coordinate rotationPoint);

TetrominoShape iTetrominoShape = tetrominoShape([[ full()] | i <- [1 .. 4]] , <2.0,0.5>);
TetrominoShape lTetrominoShape = tetrominoShape([ [full(),empty()] | i <- [1 .. 2]] + [[full(),full()]],<1.5,1.0>);
TetrominoShape jTetrominoShape = mirrorTetrominoShape(lTetrominoShape);
TetrominoShape zTetrominoShape = tetrominoShape([ [full(),full(),empty()] , [empty(),full(),full()]], <1.0,1.5>);
TetrominoShape sTetrominoShape = mirrorTetrominoShape(zTetrominoShape);
TetrominoShape oTetrominoShape = tetrominoShape([ [full(),full()], [full(),full()]],<1.0,1.0>);
TetrominoShape tTetrominoShape = tetrominoShape([ [full(),full(),full()], [empty(),full(),empty()]],<0.5,1.5>);

list[TetrominoShape] allTetrominosShapes =
	 [iTetrominoShape,jTetrominoShape,lTetrominoShape,oTetrominoShape,sTetrominoShape,tTetrominoShape,zTetrominoShape];
	 
public list[Tetromino] allTetrominos = [ tetromino(allTetrominosShapes[i].board,allTetrominosShapes[i].rotationPoint,i) | i <- [0 .. size(allTetrominosShapes)-1]];

public int getTetrominoIndex(Tetromino t){
	for(i <- size(allTetrominos), allTetrominos[i] == t){
		return i;
	}
}

int defaultRows = 20;
int defaultColumns = 10;

Board emptyBoard(int rows,int columns) {
	return  [ [[ empty() | column <- [1  ..  columns]]] | row <- [1  ..  rows]]; 
}

public Board toFixedSize(Tetromino t){
	return for( row <- [0  ..  3]){
		append for(col <- [0  ..  2]){
			append t.board[row][col] ?  empty();
		}
	}
}

public TetrisState initialState(int rows, int columns) {
	board = emptyBoard(rows,columns);
	currentTetromino = initialPlacedTetromino(randomTetromino(),board);
	boardWithTetromino = addTetrominoToBoard(currentTetromino,board);
	next = [ randomTetromino() | i <- [1  ..  40]];
	stored = nothing();
	s = tetrisState(0,0,board,boardWithTetromino,currentTetromino,next,stored,false,false,0,defaultSpinMax);
	s = addPredictionToBoard(s);
	s.boardWithCurrentTetromino = addTetrominoToBoard(s.currentTetromino,s.boardWithCurrentTetromino);
	return s;
}

public TetrisState initialState() {
	return initialState(defaultRows,defaultColumns);
}

public tuple[num,num] rotatePoint(int nrRowsFrom,int nrColumnsFrom, tuple[num,num] from,RotationDirection rotdir,bool inclusive){
	fromRow = from[0];
	fromColumn = from[1];
	nrColumnsTo = nrRowsFrom;
	nrRowsTo = nrColumnsFrom;
	offset = inclusive? 0 : 1;
	num toColumn = (clockwise() := rotdir) ? (nrColumnsTo - offset) - fromRow : fromRow;
	num toRow = (clockwise() := rotdir) ? fromColumn : (nrRowsTo -offset) - fromColumn;
	return <toRow,toColumn>;
}

public Board rotateBoard(Board b,RotationDirection rotdir){
	nrRowsFrom = nrRows(b);
	nrColumnsFrom = nrColumns(b);
	Board result = [[[empty() | column <- [0  ..  nrRowsFrom-1]]] | row <- [0  ..  nrColumnsFrom-1] ];
	for(fromRow <- [0 .. nrRowsFrom-1], fromColumn <- [0 .. nrColumnsFrom-1]){
			<toRow,toColumn> = rotatePoint(nrRowsFrom,nrColumnsFrom, <fromRow,fromColumn>,rotdir,false);
			result = setLocation(result,<toRow,toColumn>,b[fromRow][fromColumn]);
	}
	return result;
}

public Tetromino rotateTetromino(Tetromino t, RotationDirection rotdir){
	t.rotationPoint = rotatePoint(nrRows(t.board),nrColumns(t.board),t.rotationPoint,rotdir,true);
	t.board = rotateBoard(t.board,rotdir);
	return t;
}

PlacedTetromino down(PlacedTetromino t){
	t.location = addCoordinates(t.location,<1.0,0.0>);
	return t;
}

bool onBoard(Board b, Coordinate c){
	return c[0] < nrRows(b) && c[1] >= 0 && c[1] < nrColumns(b) && c[0] >= 0 ; 
}

Coordinate cellLocationOfPlacedTetromino(PlacedTetromino t,Coordinate cell){
	cell = addCoordinates(cell,<0.5,0.5>);
	cellCenterFromCenter = substractCoordinates(cell,t.tetromino.rotationPoint);
	return addCoordinates(cellCenterFromCenter,t.location);
}

bool canPlaceTetromino(PlacedTetromino t, Board b){
	for(row <- [0 .. nrRows(t.tetromino.board)-1], column <- [0 .. nrColumns(t.tetromino.board)-1]){
		cellLocation = cellLocationOfPlacedTetromino(t,<row,column>);
		if(!onBoard(b,cellLocation) || 
			(full(c) := getLocation(b,cellLocation) && full() := t.tetromino.board[row][column])){
			return false;
		}
	}
	return true;
} 

public list[Coordinate] rotationOffsets(Tetromino t){
	int maxOffset = abs(nrRows(t.board) - nrColumns(t.board));
	result = [];
	// try moving on the same row first, then try changing row
	for(rowOffset <- ([0] + mix([1 .. maxOffset],[-1 .. -maxOffset]))){
		result += [<rowOffset,columnOffset> | columnOffset <- ([0] + mix([1 .. maxOffset],[-1 .. -maxOffset]))];
	}
	return result;
}

Maybe[PlacedTetromino] tryRotateTetromino(PlacedTetromino t, Board b,RotationDirection rotdir){
	t.tetromino = rotateTetromino(t.tetromino,rotdir);
	offsets = rotationOffsets(t.tetromino);
	for(off <- offsets){
			tHere = t;
			tHere.location = addCoordinates(tHere.location,off);
			if(canPlaceTetromino(tHere,b)) return just(tHere);
	}
	return nothing();
}

bool tetrominoSupported(PlacedTetromino t,Board b){
	return !canPlaceTetromino(down(t),b);
}

Board addTetrominoToBoard(PlacedTetromino t, Board b,CellState (int) toCell){
	for(row <- [0 .. nrRows(t.tetromino.board)-1], column <- [0 .. nrColumns(t.tetromino.board)-1]){
		cellLocation = cellLocationOfPlacedTetromino(t,<row,column>);
		if(full() := t.tetromino.board[row][column] &&  onBoard(b,cellLocation))
			b = setLocation(b,cellLocation,toCell(t.tetromino.color));
	}
	return b;
}	

Board addTetrominoToBoard(PlacedTetromino t, Board b){
	return addTetrominoToBoard(t, b,full);
}

public list[int] fullLinesIndexes(Board b){
	return for(row <- [0 .. nrRows(b)-1]){
		rowFull = true;
		for(column <- [0 .. nrColumns(b)-1]){
			if(empty() := b[row][column]){
				rowFull = false;
			}
		}
		if(rowFull){
			append row;
		}
	}
}

Board removeLines(Board b,list[int] lines){
	if(lines == [] ) return b;
	stillToRemove = lines;
	result = for(row <- [0 .. nrRows(b)-1]) {
		if(stillToRemove != [] && row == head(stillToRemove)){
			stillToRemove = tail(stillToRemove);
		} else {
			append b[row];
		}
	}
	newLines = [ [[empty() | col <- [1 .. nrColumns(b)]]] | row <- [1 .. size(lines)]];
	return newLines + result;
}  
	

list[int] consecutiveLinesFull(list[int] fullLinesIndexes){
	if(size(fullLinesIndexes)==0) return [];
	int i = 0;
	int prev = fullLinesIndexes[0];
	int count = 1;
	result = [];
	while(i < size(fullLinesIndexes)){
		while(i < size(fullLinesIndexes) && prev == fullLinesIndexes[i]){
			count+=1;
			i+=1;
		}
		result+=[count];
		count = 1;
		if(i < size(fullLinesIndexes)) prev = fullLinesIndexes[i];
	}
	return result;
}

int pointsGained(list[int] fullLinesIndexes){
	int result = 0;
	for(nrConsecutiveLinesFull <- consecutiveLinesFull(fullLinesIndexes)){
		switch(nrConsecutiveLinesFull){
			case 1: result+=10;
			case 2: result+=25;
			case 3: result+=50;
			case 4: result+=100;
		}
	}
	return result;
}
	
PlacedTetromino drop(TetrisState s){
	while(!tetrominoSupported(s.currentTetromino,s.board)){
		s.currentTetromino = down(s.currentTetromino);
	}
	return s.currentTetromino;
}
		

TetrisState advanceGravity(TetrisState s){
	if(tetrominoSupported(s.currentTetromino,s.board)){
		if(!canPlaceTetromino(s.currentTetromino,s.board)){
			s.gameOver=true;
			return s;
		}
		s.board = addTetrominoToBoard(s.currentTetromino,s.board);
		s = placeNext(s);
		fullLines = fullLinesIndexes(s.board);
		s.board = removeLines(s.board,fullLines);
		s.score += pointsGained(fullLines);
		s.level = getLevel(s.score);
		s.alreadySwapped = false;
		s.spinCounter = 0;
	} else {
		s.currentTetromino = down(s.currentTetromino);
	}
	return s;
}

TetrisState tryRotateTetromino(TetrisState s,RotationDirection rotdir){
	if(just(rotatedTetromino) := tryRotateTetromino(s.currentTetromino,s.board,rotdir)){
		s.currentTetromino = rotatedTetromino;
	}
	return s;
}

TetrisState tryMoveTetromino(TetrisState s,Coordinate offset){
	newTetromino = s.currentTetromino;
	newTetromino.location = addCoordinates(newTetromino.location,offset);
	if(canPlaceTetromino(newTetromino,s.board)){
		s.currentTetromino = newTetromino;
	}
	return s;
}

TetrisState placeNext(TetrisState s){
	s.currentTetromino = initialPlacedTetromino(head(s.next),s.board);
	s.next = tail(s.next) + [randomTetromino()];
	return s;
}

TetrisState trySwapTetrominos(TetrisState s){
	if(s.alreadySwapped) return s;
	switch(s.stored){
		case nothing() : {
			s.stored = just(s.currentTetromino.tetromino);
			s = placeNext(s);
			
		}
		case just(storedTetromino) : {
			tmp = s.currentTetromino.tetromino;
			s.currentTetromino = initialPlacedTetromino(storedTetromino,s.board);
			s.stored = just(tmp);
		}
	}
	s.alreadySwapped = true;
	s.spinCounter = 0;
	return s;
}

TetrisState addPredictionToBoard(s){
	prediction = drop(s);
	s.boardWithCurrentTetromino = addTetrominoToBoard(prediction,s.board,predicted);
	return s;
}
	
int getLevel(int score){
	return score / 150;
}
	
public TetrisState nextState(TetrisState s,Action action){
	
	switch(action){
		case advanceGravity() : s = advanceGravity(s);
		case swapStored() : s = trySwapTetrominos(s);
		case drop() : s.currentTetromino = drop(s);
	}
	if(s.spinCounter < s.maxSpinCounter){
		switch(action){
			case rotateClockwise() : {
				s = tryRotateTetromino(s,clockwise());
				s.spinCounter+=1;
			}
			case rotateCounterClockwise() : {
				 s = tryRotateTetromino(s,counterClockwise());
				 s.spinCounter +=1;
			}
			case moveLeft() : {
				s = tryMoveTetromino(s,<0,-1>);
				s.spinCounter +=1;
			}
			case moveRight() : {
				s = tryMoveTetromino(s,<0,1>);
				s.spinCounter+=1;
			}
		}
	}
	s = addPredictionToBoard(s);
	s.boardWithCurrentTetromino = addTetrominoToBoard(s.currentTetromino,s.boardWithCurrentTetromino);
	return s;
}