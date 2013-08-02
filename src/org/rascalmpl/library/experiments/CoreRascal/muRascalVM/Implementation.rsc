module experiments::CoreRascal::muRascalVM::Implementation

import experiments::CoreRascal::muRascalVM::Syntax;
import experiments::CoreRascal::muRascalVM::AST;

import List;
import IO;

@doc{Stack structure}
public alias Element = value;
public data Stack = 
		  stack(Element top, Stack next)
		| \empty()
		;

public Stack s = Stack::\empty();

@doc{Base stack operations}
public Element pop() = 
	{ 
	  println("POP from: <s>");
	  Element top = s.top; 
	  s = s.next; // pops the top element from the stack
	  sp -= 1;    // maintains the stack pointer
	  top;
	};
public void load(int offset_sp)
	{
	  println("loading...");
	  stck = s;
	  if(offset_sp != 0) 
	  	for(int i <- [0..offset_sp + 1])
	  		stck = stck.next;
	  push(stck.top);
	  println("loaded...<s>");
	}
public void store(int offset_sp, Element val)
	{
	  stck = s;
	  list[Element] tops = [];
	  for(int i <- [0..offset_sp + 1]) {
	  	tops  = tops + [ stck.top ];
	  	stck = stck.next;
	  }
	  tops = tops + [ val ];
	  next = stck;
	  for(int i <- [0..size(tops)])
	  	next = stack(tops[size(tops) - 1 - i], next);
	  s = next;
	}
public Element peek(Stack s) =
	{
		s.top;
	};
public void popFrame()
	{
		;
	}
public void push(Element elem)
	{ 
	  println("PUSH: <elem>");
	  sp += 1;            // maintains the stack pointer 
	  s = stack(elem, s); // pushes an element onto the stack
	}

@doc{Instrucions}
public alias Code = list[Instruction];
public Code code = [];

@doc{Labels}
public map[int,int] labels = ();

// Conventional counters and pointers
// public int maxSize = 0;
public int pc = 0; // program counter

public int sp = 0; // stack pointer
public int fp = 0; // frame pointer; local variables are accessed by an offset relative to 'fp'; 'fp' <= 'sp' 

public int pc_start = 0;

void setStartInstruction(int pc) { pc_start = pc; }
void setInstructions(list[Instruction] instructions) { code = instructions; }

public void interpret() {
	pc_current = pc;                                   // current program counter
	pc = (pc == size(code) - 1) ? pc_start : pc + 1;   // next-instruction move
	switch(code[pc_current].opcode) {
		case ICONST: { 
						push(code[pc_current].operands[0]); // instruction with one parameter
						return;
					 }
		case LOAD: {
						println("LOAD...");
						offset = pop();
						println("Input to LOAD: <offset>");
						if(int of := offset) {
							offset_fp = fp + /*3 +*/ of;
							offset_sp = sp - offset_fp; 
							load(offset_sp); // offset == 0 => the first local variable, if any
						}
				   }
		case STORE: {
						println("STORE...");
						offset = pop();
						val = pop();
						if(int of := offset) {
							offset_fp = fp + /*3 +*/ of;
							offset_sp = sp - offset_fp; 
							store(offset_sp, val); // offset == 0 => the first local variable, if any
						}
					}
		case LABEL: {
						l = pop();
						// not finished...
					}
		case CALL: {	
						println("CALL...");
						addr = pop();
						
						fp = sp;   // new frame pointer
						
						push(fp); // pushes the current frame pointer (dynamic link)
						push(pc); // pushes the current program counter to return
									
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

