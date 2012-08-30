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
import vis::examples::tetris::PlayField;
import vis::examples::tetris::Tetromino;
import vis::Figure;
import vis::Render;
import vis::KeySym;
import util::Maybe;
import List;
import util::Math;
import ToString;
import IO;
import ValueIO;
import Time;

alias HighScore  = tuple[str name,int score];
alias Highscores = list[HighScore];
 // does not work on windows
loc highscoresFile = |home:///rascaltetrishighscores.bin|;
int nrHighscores = 6;
int minSpinTime = 250; // milliseconds
int nrNext = 4;
int minDropTime = 15;

str pauseText = 
"\<Paused!\>
				
Tetris for Rascal!!

Original Tetris by Alexey Pajitnov
Rascal version by Atze van der Ploeg

Move mouse over game to start!

Keys: 
Left/Right: arrows left/right
Rotate(counterclockwise): z
Rotate(clockwise): x
Down : arrow down
Drop : Space
Swap : a
Restart: F1";

// This is actually standardized :) see wikipedia
list[Color] blockColors = 
	[ color(c) | c <- ["cyan","blue","orange","yellow","green","purple","red"]];
Color predictedColor(Color blockColor) = 
	interpolateColor(blockColor,color("black"),0.55);
list[Color] predictedColors = [ predictedColor(b) | b <- blockColors];

Highscores readHighscores(){
	if(exists(highscoresFile)){
		return readBinaryValueFile(#Highscores, highscoresFile);
	} else {
		return [<"No one",0> | i <- [1 .. nrHighscores]];
	}
}
void writeHighscores(Highscores highscores) { 
	try{
		writeBinaryValueFile(highscoresFile, highscores);
	} catch e : {
		println("Error writing highscores!");
		iprintln(e);
	}
}
bool newHighScore(Highscores highscores,int score) = 
	score >= highscores[nrHighscores-1].score;
bool highScoreOrd(HighScore l,HighScore r) = l.score >= r.score; 
bool isValidName(str name) = name != "";
Highscores addHighScore(Highscores s, HighScore n) {
	 s = sort([n] + s, highScoreOrd);
	 writeHighscores(s);
	 return s;
}

int timeTillDrop(TetrisState state) = max(minDropTime, 1500 - state.level*120);

Color getColor(PlayFieldState s){
	switch(s){
		case prediction(i) : return predictedColors[i];
		case block(i)      : return blockColors[i];
		default            : return color("black");
	}
}

Maybe[Action] keyToAction(KeySym key){
	switch(key){
		case keyArrowDown()    : return just(normalAction(acDown()));
		case keyArrowLeft()    : return just(spinAction(  acLeft()));
		case keyArrowRight()   : return just(spinAction(  acRight()));
		case keyPrintable("z") : return just(spinAction(  acRotateCCW()));
		case keyArrowUp()      : return just(spinAction(  acRotateCW()));
		case keyPrintable("x") : return just(spinAction(  acRotateCW()));
		case keyPrintable("a") : return just(normalAction(acSwap()));
		case keyEnter()        : return just(normalAction(acDrop()));
		case keyPrintable(" ") : return just(normalAction(acDrop()));
		default                : return nothing();
	}
}

Figure tetrominoFigure(int tetromino){
	<blocks,nrR,nrC> = getCanconicalRep(tetromino);
	Color getColor(int r, int c) = 
		<r,c> in blocks ? blockColors[tetromino] : color("black");
	elems = for (r <- [0..nrR-1]) {
		append for (c <- [0..nrC-1]) {
			append box(fillColor(getColor(r,c)));
		}
	}
	hshrinks = toReal(nrC) / toReal(maxTetrominoWidth);
	vshrinks = toReal(nrR) / toReal(maxTetrominoHeight);
	as = toReal(maxTetrominoWidth) / toReal(maxTetrominoHeight);
	return space(grid(elems,shrink(hshrinks,vshrinks)),aspectRatio(as));
}


list[Figure] allTetrominoFigures= [tetrominoFigure(t) | t <- index(tetrominos)];

public Figure tetris(){
	state = initialState();
	paused = true;
	justPerfomedAction = true;
	dropped = false;
	highscores = readHighscores();
	highscoreEntered = false;
	
	void enterHighscore(str name){
		if(!highscoreEntered) {
			highscores = addHighScore(highscores,<name,state.score>);
			highscoreEntered = true;
		}
	}
	
	void restart(){
		state = initialState();
		justPerfomedAction = false;
		dropped = false;
		highscoreEntered = false;
	}
	
	bool keyDown(KeySym key, map[KeyModifier,bool] modifierMap){
		if (paused) return true;
		currentAction = keyToAction(key);
		if (!state.gameOver && just(action) := currentAction) {
			oldSpin = state.spinCounter;
			state = performAction(state, action);
			justPerfomedAction = oldSpin != state.spinCounter;
			dropped = normalAction(acDrop()) == action;
			return true;
		 }
		 if(keyF1() := key) {
		 	restart();
		 	return true;
		 }
		 return false;
	}
	
	void pause() { paused = true;  }
	void resume(){ paused = false; }
	bool showPause() = paused && !state.gameOver; 
	bool isGameOver() = state.gameOver;
	bool nothingStored() = nothing() == state.stored;;
	void handleTimer(){ state = performAction(state, normalAction(acDown())); }
	bool mayEnterHighscore() = 
		!highscoreEntered && newHighScore(highscores,state.score);

	TimerAction initTimer(TimerInfo info){
		if(state.gameOver){ 
			return stop(); 
		} else if(dropped){   // allow a little time to move a tetromino after
			dropped = false; // drop
			justPerfomedAction = false;
			return restart(minSpinTime);
		} else if(justPerfomedAction){
			/* if an action was performed then the remaining time till
			   gravity (down) should stay the same
			   however if the remaining time < minSpinTime
			   then we allow a little extra time to manouver
			   this is the same behaviour as can be seen in for
			   example tetris DS */
			justPerfomedAction = false;
			switch(info) {
				case stopped(timeElapsed) : 
					return restart(
							max(minSpinTime,timeTillDrop(state) - timeElapsed));
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
	Figure playFieldElem(int r,int c) = 
		box(fillColor(Color (){ return getColor(getPF(state.screen,<r,c>)); }));
		
	playFieldElems = for(r <- rowIndexes(state.screen)){
		append for(c <- colIndexes(state.screen)){
			append playFieldElem(r,c);
		}
	}
	playFieldAS = toReal(nrCols(state.screen)) / toReal(nrRows(state.screen));
	playFieldFig =  grid(playFieldElems, aspectRatio(playFieldAS)
						,timer(initTimer,handleTimer));
	
	Figure tetrominoFig(int () which) = 
		box(fswitch(which, allTetrominoFigures),grow(1.1),fillColor("black"));
	
	storedTetrominoFig = 
		boolFig(nothingStored,
				text("Nothing"),
				tetrominoFig(int () { return state.stored.val; }));
	storedFig = vcat([text("stored:",bottom()),
					  box(storedTetrominoFig,fillColor("black"))]);
					  
	Figure highScoreFig(int i) = 
		hcat([text(str () { return highscores[i][0]; },left()), 
			  text(str () { return "<highscores[i][1]>"; },right())]);
	highScoresFig = box(
		vcat([highScoreFig(i) | i <- [0..nrHighscores-1]],vgrow(1.03)),
		fillColor("black"),grow(1.1));
		
	Color() spinColor(int i) =
		Color() { 
		 	return  (i  >= state.spinCounter) ? color("red") : color("black"); 
		 };
	spinFig = box(
		vcat([box(fillColor(spinColor(i)))
		    | i <- [0..state.maxSpinCounter-1]]),
		fillColor("black"),aspectRatio(0.5)); 	
		
	leftBarFig = vcat([storedFig,
					   text(str () { return "Level: <state.level>";}),
					   text(str () { return "Score: <state.score>";}),
					   text("Spin Left:"),
					   spinFig,
					   text("High scores:"),
					   highScoresFig],hshrink(0.25),vgrow(1.05));
	
	Figure nextFig(int i) = tetrominoFig(int () { return state.next[i]; });
	nextsFig = vcat([ nextFig(i) | i <- [0..nrNext-1]],vgrow(1.2));
	rightBarFig = vcat([text("next:",bottom()), nextsFig],
					   hshrink(0.15));
					   
	enterHighScoreFig = 
		vcat([text("Enter your name for HIGHSCORE!"),
			 textfield("",enterHighscore, isValidName,
			 		fillColor("white"),fontColor("black"),vresizable(false))
		],vgrow(1.03));
	gameOverFig =  
		box(vcat([text("GAME OVER!",fontSize(25)),
				  ifFig(mayEnterHighscore, enterHighScoreFig),
				  text("press F1 to restart")],
			vgrow(1.2),vshrink(0.5))
		,fillColor(color("darkblue",0.6)),lineColor(color("darkblue",0.6)));
		
	gameFig = hcat([leftBarFig, playFieldFig, rightBarFig]);
	mainFig  = overlay([gameFig,ifFig(isGameOver,gameOverFig)]);
	
	pauseFig = box(text(pauseText),fillColor("black"),shrink(0.9));

	return box(vcat([text("Rascal Tetris!",fontSize(20),top()),
					boolFig(showPause, pauseFig, mainFig)],vgrow(1.02)),
			onMouseEnter(resume),onMouseExit(pause),onKeyDown(keyDown),
			std(fontColor("white")),std(fillColor("darkblue")),
			aspectRatio(18.0/20.0),grow(1.03)); 
}
	 
public void playTetris() = render(tetris());
	
