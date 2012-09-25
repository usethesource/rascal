module demo::lang::turing::l1::vis::TuringVisualisation

import List;
import util::Math;
import vis::Render;
import vis::Figure;
import vis::KeySym;
import util::Prompt;
import demo::lang::turing::l1::ide::Compile;
import demo::lang::turing::l1::ast::Turing;
import demo::lang::turing::l1::interpreter::Interpreter;


private int instructionsPerCollumn = 30;
private int tapeSize = 42;

public void visInterpreter(Program prog){
	int fps = 12;
	bool hadChange = true;
	state = initialState(prog,[false | i <- [1..tapeSize]]);
	bool running = false;
	tape = hcat([box(computeFigure(Figure() { return (state.pos == i) ? ellipse(fillColor("red"),shrink(0.5)) : space(); }),
		 fillColor(Color () {
		 	return state.tape[i] ? color("green") : color("white"); }), onMouseDown(bool (int z, map[KeyModifier,bool] m) { if(z == 1){state.tape[i] = !state.tape[i];} else { state.pos=i; } return false; })
		 ) | i <- [0..size(state.tape)-1]],vsize(60),vresizable(false));
	progView = hcat([vcat([
		box(text("<i+1>\t<compile(state.prog[i])>",left()), fillColor(Color () { return (i + 1 == state.line) ? color("green") : color("white"); }), 
				onMouseDown(bool (int z, map[KeyModifier,bool] m) { state.line = i + 1; return false; }))
		| i <- [j*instructionsPerCollumn..min((j+1)*instructionsPerCollumn,size(state.prog))-1]],vresizable(false),top())
		| j <- [0..size(state.prog)/instructionsPerCollumn]]);
	slide = hcat([scaleSlider(int() { return 1; } , int () { return 35; }, int() { return fps; }, void (int n){fps=n;},width(200)),text(str() { return " <fps> fps";})]);
	view = vcat([
			hcat([text(str() { return isFinished(state) ? "finished!" : (running ? "pause" : "run");})
				,button("go!",void () { if(!isFinished(state)) running = !running; }),slide],[ vresizable(false)]),progView,tape],
			timer(TimerAction(TimerInfo info) { if((!hadChange || stopped(_) := info) && running){ hadChange = true; return restart((1000000/fps)/1000); } 
												else { return noChange();}},
				void() { try {state = interpreterStep(state); if(isFinished(state)) running =false; } catch str e: {running=false; alert(e);} }));
	render(view);
}