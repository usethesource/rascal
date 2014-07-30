module experiments::vis2::StateMachine

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;
import ParseTree;

layout Whitespace = [\t-\n\r\ ]*;

layout Id = [A-Za-z][A-Za-z0-9]*;

syntax StateMachine = State* states;

syntax State = "state" Id id Transition* transitions;

syntax Transition = Id from "=\>" Id to;

Figure visStateMachine(StateMachine sm) =
	vcat(align=topLeft, figs=[visState(s) | s <- sm.states]);
	
Figure visState(State s) =
	vcat(figs = [ text("state <s.id>"),
				  *[ visTransition(tr) | tr <- s.transitions ]
				]);
				
Figure visTransition(Transition t) = 
	hcat(figs=[ box(size=<100,40>, fig=text("<t.from>")),
				text(" =\> "),
				box(size=<100,40>, fig=text("<t.to>"))
			  ], gap=<5,5>);

str example =
	"state closed
	'	open =\> opened
	'	lock =\> locked
	'
	'state opened
	'	close =\> closed";

void sm(){

	tree = parse(#StateMachine, example);
	render("sm", visStateMachine(tree));
}
