module demo::lang::turing::l1::interpreter::Interpreter

import List;
import demo::lang::turing::l1::ast::Turing;

alias Tape = list[bool];

alias TuringState = tuple[list[Statement] prog, int line, Tape tape, int pos];

public TuringState interpreterStep(TuringState state) {
	int oldLine = state.line;
	state = state[line = state.line + 1];
	bool currentState = state.tape[state.pos];
	switch(state.prog[oldLine - 1]) { 
		case jumpAlways(l) : state.line = l;
		case jumpSet(l): if(currentState)  state.line = l;
		case jumpUnset(l): if(!currentState) state.line= l;
		case writeSet(): if (currentState) throw "already set"; else state.tape[state.pos] = true;
		case writeUnset(): if (!currentState) throw "already unset"; else state.tape[state.pos] = false;
		case moveForward() : state.pos += 1;
		case moveBackward(): state.pos -= 1;
		default: throw "Unknown case";
	}
	if (state.pos >= size(state.tape)) throw "moving beyond the tape";
	if (state.pos < 0) throw "moving beyond the tape";
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
