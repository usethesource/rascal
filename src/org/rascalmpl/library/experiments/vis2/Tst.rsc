module experiments::vis2::Tst

import ParseTree;
import util::Cursor;
import IO;

layout Whitespace = [\t-\n\r\ ]*;
layout Id = [A-Za-z][A-Za-z0-9]*;
syntax StateMachine = State* states;
syntax State = "state" Id id Transition* transitions;
syntax Transition = Id from "=\>" Id to;

str example = "state closed";

data D = constructor(list[int] ns, int m);

void tst(){
	d = constructor([1,2,3],20);
	d1 = makeCursor(d);
	println("d1: <toPath(d1.ns[0])>");				// ==> d1: [argument(0),element(0)]
	
	tree = parse(#StateMachine, example);
	tree1 = makeCursor(tree);
	println("tree1: <toPath(tree1.states)>");	// ==> tree1: [argument("args"),element(0)]
}
