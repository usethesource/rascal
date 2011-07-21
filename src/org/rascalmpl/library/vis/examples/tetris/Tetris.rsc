@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Atze van der Ploeg - ploeg@cwi.nl - CWI}
module vis::examples::tetris::Tetris

import vis::examples::tetris::TetrisState;
import vis::Figure;
import vis::Render;
import vis::KeySym;
import List;
import Integer;
import ToString;
import IO;
import ValueIO;
import Time;

alias HighScore = tuple[str,int];
alias HighScores = list[HighScore];

loc highScoresFile = |file:///tmp/atzeTetrisHighScores|;
int nrHighScores = 6;
int minSpinTime = 250;

data TetrisVisState = 
	tetrisVisState(
		TetrisState logicState,
		int lastTime,
		bool dropped,
		bool highScoreEntered,
		bool paused,
		list[HighScore] highScores,
		int expectedTimerDuration
	);
	
str pauseText = "\<Paused!\>
				
Tetris for Rascal!!

Original Tetris by Alexey Pajitnov
Rascal version by Atze van der Ploeg

Move mouse over game to start!

Keys: 
Left/Right: arrows left/right
Rotate: arrow up
Down : arrow down
Drop : Enter
Swap : Tab
Restart: F1";
	
	

// This is actually standardized :) see wikipedia
list[str] tetrisColorNames = ["cyan","blue","orange","yellow","green","purple","red"];
list[Color] tetrisColors = [color(c) | c <-tetrisColorNames ];
list[Color] predictedColors = [interpolateColor(tetrisColors[i],color("black"),0.55) | i <- [0 .. size(tetrisColorNames)-1]];

HighScores readHighScores(){
	if(exists(highScoresFile)){
		return readBinaryValueFile(#HighScores, highScoresFile);
	} else {
		return [<"No one",0> | i <- [1 .. 10]];
	}
}


int timeTillDrop(TetrisState state){
	return  max(20, 1500 - state.level * 50);
} 

void writeHighScores(HighScores highScores){
	writeBinaryValueFile(highScoresFile, highScores);
}


Color getColor(int defaultColor,CellState s){
	switch(s){
		case empty() : return color("black");
		case predicted(i) : return predictedColors[i];
		case full() : return tetrisColors[defaultColor];
		case full(i) : return tetrisColors[i];
	}
}

Maybe[Action] keyToAction(KeySym key){
	switch(key){
		case keyArrowDown() : return just(advanceGravity());
		case keyArrowLeft() : return just(moveLeft());
		case keyArrowRight() : return just(moveRight());
		case keyArrowUp() : return just(rotateClockwise());
		case keyTab() : return just(swapStored());
		case keyEnter() : return just(drop());
	}
	return nothing();
}

Figure board(Board b,int color,FProperty props...){
	return grid([[[box(fillColor(getColor(color,cell))) | cell <- row]] | row <- b],props);
}

Figure next(list[Tetromino] next,FProperty props...){
	return box(vcat([text("Next: ",fontColor("white"))] +
		[board(n.board,n.color,hshrink(nrColumns(n.board)*(1.0/3.0)),vshrink((1.0/16.0) * nrRows(n.board))) |
		 n <- take(4,next)],vgrow(1.1),hshrink(0.6)),[fillColor("black")] + props);
}


Figure stored(TetrisState state,FProperty props...){
	switch(state.stored){
		case nothing() : {
			return box(text("\<nothing\>",fontColor("white")),[fillColor("black")] + props);
		}
		case just(t):{
			return box(
				board(t.board,t.color,hshrink(nrColumns(t.board)*(1.0/3.0)),vshrink((1.0/4.0) * nrRows(t.board))),
			[fillColor("black")] + props);
		}
	}
}

Figure spinCounter(TetrisState state,FProperty props...){
	return box(
			vcat([text("Spin left:",fontColor("white"))] +
		[box(fillColor(i > state.spinCounter ? "red" : "black")) | i <- [1 .. state.maxSpinCounter]]
		),[fillColor("black")] + props);
}

bool newHighScore(TetrisVisState state){
	return size(state.highScores) < nrHighScores || state.logicState.score >= state.highScores[nrHighScores-1][1];
}


Figure highScoresFig(TetrisVisState state, void (str) callback,FProperty props...){
	if(state.logicState.gameOver && !state.highScoreEntered && newHighScore(state)){
		return vcat([text("Enter your \n name for\n HIGHSCORE"),textfield("",callback)],vgrow(1.1));
	} else {
		return box(grid([ [text("<name>"),text("<highScore>")] | <name,highScore> <- take(nrHighScores,state.highScores)],vgrow(1.05)), 
		[fillColor("black"),stdFontColor("white")] + props);
	}
}

Figure sideBarLeft(TetrisVisState state,  void (str) callback,FProperty props...){
	return vcat([
		stored(state.logicState,vshrink(1.0/5.0),hshrink(3.0/5.0)),
		text("Score <state.logicState.score>"),
		text("Level: <state.logicState.level>"),
		spinCounter(state.logicState,vshrink(0.4)),
		text("High scores:"),
		highScoresFig(state,callback)]);
}

Figure mainScreen(TetrisVisState state,  FProperty props...){
	if(state.logicState.gameOver){
		newHigh = newHighScore(state) ? "\n NEW HIGHSCORE!" :"";
		gameOverMsg = box(
			text("GAME OVER!\n score <state.logicState.score>" + newHigh + "\n press F1 to restart!", fontColor("white"),fontSize(20))
			,[fillColor("black"),("white"),grow(1.3),resizable(false)] + props);
		return overlay([board(state.logicState.boardWithCurrentTetromino,0),gameOverMsg],props);
	} else {
		return board(state.logicState.boardWithCurrentTetromino,0,props);
	}
}




Figure game(TetrisVisState state, void (str) callback, FProperty props...){
	if(state.paused && !state.logicState.gameOver){
		return box(text(pauseText
				,fontColor("white")),fillColor("black"));
	} else {		 
		return hcat([	sideBarLeft(state,callback,hshrink(0.25)),
						mainScreen(state,hshrink(0.5)),
						next(state.logicState.next,hshrink(0.25))],
				[hgrow(1.1)]+props);
	}
}

TetrisVisState initialVisState() {
	TetrisState logicState = initialState();
	return tetrisVisState(
		logicState,
		0,
		false,
		false,
		true,
		readHighScores(),
		timeTillDrop(logicState)
	);
}

public Figure tetris(){
	TetrisVisState state = initialVisState();
	
	void enterHighScore(str name){
		if(state.highScoreEntered) return;
		state.highScores+=[<name,state.logicState.score>];
		state.highScores = sort(state.highScores,bool (HighScore l,HighScore r) { return l[1] >= r[1]; });
		writeHighScores(state.highScores);
		state.highScoreEntered = true;
	}
	
	void keyDown(KeySym key, map[KeyModifier,bool] modifierMap){
		currentAction = keyToAction(key);
		switch(currentAction){
			case nothing() : ;
			case just(action) : state.logicState = nextState(state.logicState, action);
		 }
		 if(keyF1() := key) {
		 	restart();
		 	return;
		 }
		 if(just(advanceGravity()) := currentAction){
		 	state.dropped = true;
		 } else {
		 	state.dropped = false;
		 }
		 if(just(drop()) := currentAction || state.logicState.spinCounter == state.logicState.maxSpinCounter){
		 	state.expectedTimerDuration = 0;
		 }
	}
	
	void restart(){
		state = initialVisState();
		state.paused = false;
	}
	
	int handleDropTimer(){
		state.logicState = nextState(state.logicState,advanceGravity());
		state.dropped = true;
		return 0;
	}
	
	Figure timers(Figure inner) { 
		if(state.logicState.gameOver || state.paused) return point();
		prevTime = state.lastTime;
		state.lastTime = getMilliTime();
		duration = state.lastTime - prevTime;
		int newDuration;
		if(state.dropped){
			newDuration = timeTillDrop(state.logicState);
			if(duration > state.expectedTimerDuration){
				newDuration-=duration - state.expectedTimerDuration;
			}
		} else {
			newDuration = max(minSpinTime,state.expectedTimerDuration - duration);
		}
		if(newDuration < 0){
			newDuration = 1;
		}
		state.expectedTimerDuration = newDuration; 
		return _timer(state.expectedTimerDuration,handleDropTimer,inner,[]);
	}		
	
	
	void pause(){
		state.paused = true;
	}
	
	void resume(){
		state.paused =false;
	}
	
	readHighScores();
	state.lastTime = getMilliTime();
	return title("Tetris!", computeFigure( Figure () { return hcat([timers(point()),game(state,enterHighScore)]);}),
		onKeyDown(keyDown),onMouseOver(resume),onMouseOff(pause),hshrink(0.6));
}
	 
	 
public void playTetris(){
	render(tetris());
}
	