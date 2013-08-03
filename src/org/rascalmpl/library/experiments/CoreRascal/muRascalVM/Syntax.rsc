module experiments::CoreRascal::muRascalVM::Syntax

import ParseTree;
import util::IDE;

layout LAYOUTLIST
  = LAYOUT* !>> [\t-\n \r \ ] !>> "//" !>> "/*";

lexical LAYOUT
  = Comment 
  | [\t-\n \r \ ];
    
lexical Comment
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
	
		| "RETURN"	    // returns from a function popping the current stack frame 
	
		| "JMP"         // jumps to the specified location
		| "JMPTRUE"     // jumps to the specified location if the top value on the stack is true
		| "JMPFALSE"    // jumps to the specified location if the top value on the stack is false
		
		| "HALT"
	
		// co-routine specific instructions
		| "CREATE"
		| "RESUME"
		| "YIELD"
		;
	
keyword Keywords = 
		  "CONST"
		| "FUNCTION"
		
		| "LOADCON"
		| "LOADLOC"
		| "LOADVAR"
		| "STORELOC"
		| "STOREVAR"
		| "LABEL"
		| "CALLPRIM"
		| "CALL"
		| "RETURN"
		| "JMP"
		| "JMPTRUE"
		| "JMPFALSE"
		| "HALT"
		| "CREATE"
		| "RESUME"
		| "YIELD"
		;

syntax Operand = [\ ] << Identifier;

syntax Instruction = instruction: Opcode opcode Operand* operands;

syntax Directive =
		  const:     "CONST"    Identifier value 
		| function : "FUNCTION" Identifier name Integer scope Integer nlocals Integer nformals Integer maxStack Instruction+ instructions
		;
	
syntax RascalVM = vm: { Directive ";"}+ directives ";" Instruction* instructions;

@doc{Registers the muRascalVM language, .rvm}
public void registerLanguage() {
	registerLanguage("muRascalVM", "rvm", RascalVM (str src, loc l) { return parse(#RascalVM, src, l); });
}