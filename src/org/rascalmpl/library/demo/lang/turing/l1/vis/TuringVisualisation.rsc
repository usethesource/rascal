module demo::lang::turing::l1::vis::TuringVisualisation

import List;
import Integer;
import vis::Render;
import vis::Figure;
import vis::KeySym;
import demo::lang::turing::l1::ide::Compile;
import demo::lang::turing::l1::ast::Turing;
import demo::lang::turing::l1::interpreter::Interpreter;

private int stepDelay = 150;

private int instructionsPerCollumn = 42;

public void visInterpreter(Program prog){
	state = initialState(prog,[false | i <- [1..16]]);
	bool running = false;
	tape = hcat([box(computeFigure(Figure() { return (state.pos == i) ? ellipse(fillColor("red"),shrink(0.5)) : space(); }),
		 fillColor(Color () {
		 	return state.tape[i] ? color("green") : color("white"); }), onMouseDown(bool (int z, map[KeyModifier,bool] m) { if(z == 1){state.tape[i] = !state.tape[i];} else { state.pos=i; } return false; })
		 ) | i <- [0..size(state.tape)-1]],vsize(60));
	progView = hcat([vcat([
		box(text("<i+1>\t<compile(state.prog[i])>",left()), fillColor(Color () { return (i + 1 == state.line) ? color("green") : color("white"); }), 
				onMouseDown(bool (int z, map[KeyModifier,bool] m) { state.line = i + 1; return false; }))
		| i <- [j*instructionsPerCollumn..min((j+1)*instructionsPerCollumn,size(state.prog))-1]],vresizable(false),top())
		| j <- [0..size(state.prog)/instructionsPerCollumn]]);
	view = vcat([
			hcat([text(str() { return isFinished(state) ? "finished!" : (running ? "pause" : "run");})
				,button("go!",void () { if(!isFinished(state)) running = !running; })],[ vresizable(false)]),progView,tape],
			timer(TimerAction(TimerInfo info) { if(stopped(_) := info && running){ return restart(stepDelay); } else { return noChange();}},
				void() { state = interpreterStep(state); if(isFinished(state)) running =false; }));
	render(view);
}