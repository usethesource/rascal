@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}

module vis::examples::tetris::TetrisState

import vis::examples::tetris::PlayField;
import vis::examples::tetris::PlacedTetromino;
import vis::examples::tetris::Tetromino;
import util::Maybe;
import util::Math;
import List;
import Number;

// my take on the infinte spin controversy (see wikipedia tetris):finite spin :)
int defaultSpinMax = 25; 
int defaultRows = 20;
int defaultColumns = 10;
int numberNext = 40;
int defaultInvisibleLines = 2;

data TetrisState = 
	tetrisState(
		int score, 
		int level,
		PlayField playField,
		PlayField screen,
		PlacedTetromino cur,
		list[int] next,
		Maybe[int] stored,
		bool swapped,
		bool gameOver,
		int spinCounter,
		int maxSpinCounter,
		int nrInvisibleLines
	);
	
data SpinAction 
	= acRotateCW()
	| acRotateCCW()
	| acLeft()
	| acRight();

data NormalAction 
	= acDown()
	| acSwap()
	| acDrop();

data Action 
	= spinAction(SpinAction saction)
	| normalAction(NormalAction naction);
	
public TetrisState 
	initialState(int rows, int columns,int maxSpin, int nrInvisbleLines) {
	pf = emptyPF(rows + nrInvisbleLines,columns);
	currentTetromino = initialPT(randomTetromino(),pf,nrInvisbleLines);
	next = [ randomTetromino() | i <- [1  ..  numberNext]];
	s = tetrisState(   0,0,pf,[],currentTetromino,
	                   next,nothing(),false,false,0,maxSpin,nrInvisbleLines);
	return update(s);
}

public TetrisState initialState() = initialState(defaultRows,defaultColumns,
										defaultSpinMax,defaultInvisibleLines);

public TetrisState performAction(TetrisState s, Action action){
	switch(action){
		case spinAction(a) : s = performSpinAction(s,a);
		case normalAction(a) : s = performNormalAction(s,a);
	}
	return update(s);
}

public TetrisState performSpinAction(TetrisState s,SpinAction action){
	if (s.spinCounter >= s.maxSpinCounter) {
		return s;
	}
	old = s.cur;
	switch(action){
		case acRotateCW()  : s.cur = rotateCW (s.playField,s.cur);
		case acRotateCCW() : s.cur = rotateCCW(s.playField,s.cur);
		case acLeft()      : s.cur = moveLeft (s.playField,s.cur);
		case acRight()     : s.cur = moveRight(s.playField,s.cur);
	}
	if(old != s.cur){
		s.spinCounter+=1;
	} 
	return s;
}

public TetrisState performNormalAction(TetrisState s, NormalAction action){
	switch(action){
		case acDown() : return down(s); 
		case acSwap() : return swap(s);
		case acDrop() : return s[cur = drop(s.cur,s.playField)]; 
	}
}

TetrisState down(TetrisState s){
	if(supported(s.cur,s.playField)){
		s.playField = add(s.playField,s.cur);
		s = placeNext(s);
		if(!s.gameOver){
			fullLines = fullRows(s.playField);
			s.playField = removeRows(s.playField,fullLines);
			s.score += pointsGained(size(fullLines));
			s.swapped = false;
			s.spinCounter = 0;
		}
	} else {
		s.cur = moveDown(s.playField,s.cur);
	}
	return s;
}

TetrisState placeNext(TetrisState s){
	s.cur = initialPT(head(s.next),s.playField,s.nrInvisibleLines);
	s.next = tail(s.next) + [randomTetromino()];
	s.gameOver = !canPlace(s.cur,s.playField);
	return s;
}

TetrisState swap(TetrisState s){
	if(s.swapped) return s;
	switch(s.stored){
		case nothing() : {
			s.stored = just(s.cur.tetromino);
			s = placeNext(s);
		}
		case just(storedTetromino) : {
			tmp = s.cur.tetromino;
			s.cur = initialPT(storedTetromino,s.playField,s.nrInvisibleLines);
			s.stored = just(tmp);
		}
	}
	s.swapped = true;
	s.spinCounter = 0;
	return s;
}

TetrisState update(TetrisState s){
	s.screen = addPrediction(s.playField,drop(s.cur,s.playField));
	s.screen = add(s.screen,s.cur);
	s.screen = removeInvisibleLines(s.screen,s.nrInvisibleLines);
	s.level = getLevel(s.score);
	return s;
}

PlayField removeInvisibleLines(PlayField b, int n) =  drop(n,b);

public int pointsGained(int nrLines) =
	visit(nrLines){
			case 1 => 10
			case 2 => 25
			case 3 => 50
			case 4 =>100
	};

public int getLevel(int score) = score / 150;
	
