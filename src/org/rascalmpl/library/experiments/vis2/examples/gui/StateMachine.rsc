module experiments::vis2::examples::gui::StateMachine

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;
import ParseTree;
import util::Cursor;
import IO;

layout Whitespace = [\t-\n\r\ ]*;
layout Id = [A-Za-z][A-Za-z0-9]*;

syntax StateMachine = State* states;

syntax State = "state" Id id Transition* transitions;

syntax Transition = Id from "=\>" Id to;

Figure visModel(Model m) = visStateMachine(m.tree);

Figure visStateMachine(StateMachine sm) =
	vcat(align=topLeft, figs=[visState(s) | s <- sm.states]);
	
Figure visState(State s) =
	vcat(figs = [ text("state <s.id>", size=<30,20>),
				  *[ visTransition(tr) | tr <- s.transitions ]
				]);
				
Figure visTransition(Transition t) = 
	hcat(figs=[ box(size=<100,30>, fig=strInput(size=<100,20>, event=on("submit", bind(t.from)))),
				box(size=<100,30>, fig=strInput(size=<100,20>, event=on("submit", bind(t.to))))
			  ], gap=<5,5>);

data Model = model(Tree tree);

str sm_example =
	"state closed
	'	open =\> opened
	'	lock =\> locked
	'
	'state opened
	'	close =\> closed";

void sm(){

	example = parse(#StateMachine, sm_example);
	
	render("sm", #StateMachine, model(example), visModel);
}
