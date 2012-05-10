module demo::lang::turing::l1::interpreter::Interpreter

import List;
import demo::lang::turing::l1::ast::Turing;

private int tapeSize = 16;

alias Tape = list[bool];

alias TuringState = tuple[list[Statement] prog, int line, Tape tape, int pos];

public TuringState interpreterStep(TuringState state) {
	int oldLine = state.line;
	state = state[line = state.line + 1];
	switch(state.prog[oldLine - 1]) { 
		case jumpAlways(l) : state.line = l;
		case jumpSet(l): if(state.tape[state.pos])  state.line = l;
		case jumpUnset(l): if(!state.tape[state.pos]) state.line= l;
		case writeSet(): state.tape[state.pos] = true;
		case writeUnset(): state.tape[state.pos] = false;
		case moveForward() : state.pos += 1;
		case moveBackward(): state.pos -= 1;
		default: throw "Unknown case";
	}
	return state;
}
 
public TuringState initialState(Program prog, Tape input) =
	<prog.statements, 1, input, 0>;

public bool isFinished(TuringState s) = s.line > size(s.prog);

public Tape interpreter(Program prog, Tape input) {
	TuringState state = initialState(prog,input);
	while(!isFinished(state)) state = interpreterStep(state);
	return state.tape;
}