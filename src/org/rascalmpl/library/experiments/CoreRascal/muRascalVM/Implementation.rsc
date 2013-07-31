module experiments::CoreRascal::muRascalVM::Implementation

import experiments::CoreRascal::muRascalVM::Syntax;
import experiments::CoreRascal::muRascalVM::AST;

@doc{Stack structure}
public data Stack = 
		  stack(value top, Stack rest)
		| \empty()
		;

public Stack s = Stack::\empty();

@doc{Base stack operations}
public value pop() = 
	{ top = s.top; 
	  s = s.rest; // pops the top element from the stack
	  sp -= 1;    // maintains the stack pointer
	  top;
	};
public void push(value elem) = 
	{ println("PUSH: <elem>");
	  sp += 1;            // maintains the stack pointer 
	  s = stack(elem, s); // pushes an element onto the stack
	};

@doc{Instrucions}
public alias Code = list[Instruction];
public Code code = [];

@doc{Labels}
public map[int,int] labels = ();

// Conventional counters and pointers
public int pc = 0; // program counter
public int sp = 0; // stack pointer
public int fp = 0; // frame pointer; local variables are accessed by an offset relative to 'fp'

public int pc_start = 7;

public void testit() {
	while(true) {
		interpret();
		if(pc == pc_start)
			break;
	}
	println();
}

public void interpret() {
	pc_c = pc; // current program counter
	pc = (pc == size(code) - 1) ? pc_start : pc + 1;   // next-instruction move
	switch(code[pc_c].opcode) {
		case ICONST: { 
						push(code.operands[0]);
						return;
					 }
		case LABEL: {
						l = pop();
						// not finished...
					}
		case CALL: {	
						println("CALL...");
						addr = pop();
						
						push(fp); // pushes the current frame pointer (dynamic link)
						push(pc); // pushes the current program counter to return
						
						fp = sp;   // new frame pointer			
						pc = addr; // new program counter
						
						return;
						
				   }
		case RETURN: {
						ret_addr = pop();
						dyn_link = pop();
						pop();
						fp = dyn_link;
						pc = ret_addr;
						
						return;
					 }
		case ALLOC: {
						n = pop();
						for(int i <- [0..n])
							push(0);
						return;	
					}
		case DEALLOC: {
						n = pop();
						for(int i <- [0..n])
							pop();
						return;
					  }
	}
}

