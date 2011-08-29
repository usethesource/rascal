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
	return  max(minSpinTime, 1500 - state.level * 120);
} 

void writeHighScores(HighScores highScores){
	writeBinaryValueFile(highScoresFile, highScores);
}


Color getColor(CellState s){
	switch(s){
		case empty() : return color("black");
		case predicted(i) : return predictedColors[i];
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

Figure tetrominoFigure(Tetromino t){
	c = tetrisColors[t.color];
	return grid([
				[[box(fillColor((empty() := e )? color("black") : c)) | e <- row]]
				| row <- t.board] 
				,shrink(toReal(nrColumns(t.board)) / 3.0, toReal(nrRows(t.board))/4.0));
}

bool newHighScore(HighScores highscores,int score){
	println(highscores);
	println(score);
	println(highscores[nrHighScores-1][1]);
	println(score >= highscores[nrHighScores-1][1]);
	return score >= highscores[nrHighScores-1][1];
}


public list[Figure] allTetrominoFigures = [tetrominoFigure(t) | t <- allTetrominos];

public Figure tetris(){
	state = initialState();
	paused = true;
	justPerfomedAction = true;
	dropped = false;
	highScores = readHighScores();
	highscoreEntered = false;
	
	
	void enterHighscore(str name){
		println(name);
		if(highscoreEntered) return;
		highScores=[<name,state.score>] + highScores;
		highScores = sort(highScores,bool (HighScore l,HighScore r) { return l[1] >= r[1]; });
		writeHighScores(highScores);
		highscoreEntered = true;
	}
	
	void restart(){
		state = initialState();
		justPerfomedAction = false;
		dropped = false;
		highscoreEntered = false;
	}
	
	int handleDropTimer(){
		state.logicState = nextState(state.logicState,advanceGravity());
		state.dropped = true;
		return 0;
	}
	
	bool keyDown(KeySym key, bool down, map[KeyModifier,bool] modifierMap){
		if(!down || paused) return true;
		currentAction = keyToAction(key);
		switch(currentAction){
			case nothing() : ;
			case just(action) : {
				state = nextState(state, action);
				if(state.spinCounter < state.maxSpinCounter){
					justPerfomedAction = true;
				} 
			}
		 }
		 if(keyF1() := key) {
		 	restart();
		 	return true;
		 }
		 if(just(drop()) := currentAction){
		 	dropped = true;
		 } 
		 return !(nothing() := currentAction);
	}
	
	void setPause(bool mouseOn){
		paused = !mouseOn;
	}
	
	void handleTimer(){
		 state = nextState(state, advanceGravity());
	}

	TimerAction initTimer(TimerInfo info){
		if(state.gameOver){ 
			return stop(); 
		}
		if(dropped){
			dropped = false;
			justPerfomedAction = false;
			return restart(minSpinTime);
		} else if(justPerfomedAction){
			justPerfomedAction = false;
			switch(info) {
				case stopped(timeElapsed) : return restart(max(minSpinTime,timeTillDrop(state) - timeElapsed));
				case running(timeLeft) : {
					if(timeLeft > minSpinTime){
						return noChange();
					} else {
						return restart(minSpinTime);
					}
				}
			}
		} else if(stopped(timeElapsed) := info){
			return restart(timeTillDrop(state)- timeElapsed);
		} else {
			return noChange();
		}
	}
	
	return
		box(vcat([text("Rascal Tetris!",fontSize(20),top()),
			overlay([
				boolFig(bool () { return paused && !state.gameOver; },
					box(text(pauseText),fillColor("black"),grow(1.2)),
					hcat([
						vcat([
							vcat([text("stored:",bottom()),
								box(
									boolFig(bool () { return nothing() := state.stored; },
										text("Nothing"),
										fswitch(int () { return state.stored.val.color; },allTetrominoFigures)
									)
								,aspectRatio(3.0/4.0),grow(1.3),fillColor("black"))
							]),
							text(str () { return "Level: <state.level>";}),
							text(str () { return "Score: <state.score>";}),
							box(vcat([box(fillColor(Color () { return (state.spinCounter < i) ? color("red") : color("black");})) | i <- [0..state.maxSpinCounter-1]],vgrow(1.005)),fillColor("black"),hshrink(0.5)),
							text("High scores:"),
							box(vcat([
								hcat([text(str () { return highScores[i][0]; },left()), text(str () { return "<highScores[i][1]>"; },right())]) 
							| i <- [0..nrHighScores]],vgrow(1.03)),fillColor("black"),grow(1.1))
						],hshrink(0.25),vgrow(1.05)),
						grid([
							[[box(fillColor(Color () { return getColor(state.boardWithCurrentTetromino[row][col]); } )) | col <- [0..nrColumns(state.board)-1]]]
							| row <- [0..nrRows(state.board)-1]],
							aspectRatio(0.5), timer(initTimer,handleTimer )),
						vcat([text("next",bottom())] + 
							[ box(fswitch(int () { return state.next[i].color; },allTetrominoFigures),aspectRatio(3.0/4.0),grow(1.1),fillColor("black")) | i <- [0..3]]
						,vgrow(1.2),hshrink(0.15))
					])
				),
				ifFig(bool () { return state.gameOver; },
					 box(
					 	vcat([
					 		text("GAME OVER!",fontSize(25)),
					 		ifFig(bool () { return !highscoreEntered && newHighScore(highScores,state.score); },
					 			 vcat([text("Enter your name for HIGHSCORE!"),
					 			 	textfield("",enterHighscore, bool (str s) { return s == ""; }, fillColor("white"),fontColor("black"),vresizable(false))
					 			 ],vgrow(1.03))),
					 		text("press F1 to restart")
					 	],vgrow(1.2),vshrink(0.5))
					,fillColor(color("darkblue",0.6)),lineColor(color("darkblue",0.6)))
				)
				])
			],vgrow(1.01))
		, onMouseMove(setPause),onKey(keyDown),std(fontColor("white")),std(fillColor("darkblue")),aspectRatio(18.0/20.0),grow(1.03));

}
	 
	 
public void playTetris(){
	render(tetris());
}
	