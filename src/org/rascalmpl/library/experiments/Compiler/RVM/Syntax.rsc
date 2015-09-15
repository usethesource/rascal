module experiments::Compiler::RVM::Syntax

import ParseTree;
//import util::IDE;

layout RVMLayoutList
  = RVMLayout* !>> [\t-\n \r \ ] !>> "//" !>> "/*";

lexical RVMLayout
  = RVMComment 
  | [\t-\n \r \ ];
    
lexical RVMComment
  = "/*" (![*] | [*] !>> [/])* "*/" 
  | "//" ![\n]* [\n];
  
lexical Integer = [0-9]+;
lexical Identifier = ( [A-Za-z0-9][A-Za-z0-9 _]* ) \ Keywords;
	
// list of the VM opcodes (mnemonic)
lexical Opcode = 
		  "LOADCON"      // pushes a constant onto the stack
		| "LOADLOC"      // loads the value of a local variable onto the stack;
		| "LOADVAR"      // loads the value of a global variable onto the stack;
 
		| "STORELOC"    // stores the value from the top into a local variable
		| "STOREVAR"    // stores the value from the top into a global variable
		
		| "LABEL"       // labels instructions
	
		| "CALLPRIM"    // calls a primitive operation on a number of arguments, pops them and pushes the result onto the top
		| "CALL"        // calls a user-defined function on a number of arguments, pops them and pushes the result onto the top
	
	    | "RETURN_0"
		| "RETURN1"	    // returns from a function popping the current stack frame 
	
		| "JMP"         // jumps to the specified location
		| "JMPTRUE"     // jumps to the specified location if the top value on the stack is true
		| "JMPFALSE"    // jumps to the specified location if the top value on the stack is false
		
		| "HALT"
	
		// co-routine specific instructions
		| "CREATE"
		| "CREATEDYN"
		| "INIT"
		| "NEXT0"
		| "NEXT1"
		| "YIELD0"
		| "YIELD1"
		| "HASNEXT"
		;
	
keyword Keywords = 
		  "I-CONST"
		| "REL-CONST"
		| "RAT-CONST"
		| "B-CONST"
		| "FUNCTION"
		
		| "LOADCON"
		| "LOADLOC"
		| "LOADVAR"
		| "STORELOC"
		| "STOREVAR"
		| "LABEL"
		| "CALLPRIM"
		| "CALL"
		| "RETURN0"
		| "RETURN1"
		| "JMP"
		| "JMPTRUE"
		| "JMPFALSE"
		| "HALT"
		| "CREATE"
		| "CREATEDYN"
		| "INIT"
		| "NEXT0"
		| "NEXT1"
		| "YIELD0"
		| "YIELD1"
		| "HASNEXT"
		;

syntax Operand = [\ ] << Identifier;

syntax Instruction = instruction: Opcode opcode Operand* operands;

syntax Directive =
		  intconst:  "I-CONST"   Identifier value
		| relconst:  "REL-CONST" Identifier value
		| ratconst:  "RAT-CONST" Identifier value
		| boolconst: "B-CONST"   Identifier value
		| function : "FUNCTION"  Identifier name Integer nformals Integer nlocals Integer maxStack Instruction+ instructions
		;
	
syntax RascalVM = vm: { Directive ";"}+ directives ";" Instruction* instructions;
