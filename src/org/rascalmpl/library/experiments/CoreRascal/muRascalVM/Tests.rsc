module experiments::CoreRascal::muRascalVM::Tests

import experiments::CoreRascal::muRascalVM::AST;
import experiments::CoreRascal::muRascalVM::Implementation;
import experiments::CoreRascal::muRascalVM::Implode;
import IO;

public loc exmpl1 = |project://RascalStandardLibrary/src/experiments/CoreRascal/muRascalVM/programs/Example1.vmrsc|;

public void testit() {
	RascalVM code = parse(readFile(exmpl1));
	
	setStartInstruction(0);
	setInstructions(code.instructions);
	
	while(true) {
		interpret();
		if(pc == pc_start)
			break;
	}
	println("Current stack: <s>");
}
