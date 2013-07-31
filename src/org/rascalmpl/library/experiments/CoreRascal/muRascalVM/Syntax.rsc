module experiments::CoreRascal::muRascalVM::Syntax

import ParseTree;
import util::IDE;

layout Whitespace = [\ \t\n]*;

layout LAYOUTLIST
  = LAYOUT* !>> [\t-\n \r \ ] !>> "//" !>> "/*";

lexical LAYOUT
  = Comment 
  | [\t-\n \r \ ];
    
lexical Comment
  = "/*" (![*] | [*] !>> [/])* "*/" 
  | "//" ![\n]* [\n];
	
// list of the VM opcodes (mnemonic)
lexical Opcode = 
	  "iconst"      // pushes the integer constant onto the stack
	| "rconst"      // pushes the real constant onto the stack
	
	| "load"        // loads the value from the variables onto the stack
	| "store"       // store the value from the top into a variable
	
	| "label"       // labels instructions
	
	| "call-prim"   // calls a primitive operation on a number of arguments, pops them and pushes the result onto the top
	| "call"        // calls a user-defined function on a number of arguments, pops them and pushes the result onto the top
	
	| "return"
	| "yield"
	
	| "alloc"       // allocates memory slots 
	| "de-alloc"    // de-allocates memory slots
	
	| "jump"
	| "jump-cond"
	
	| "to-local"
	| "to-global"
	
	;
	
lexical Operand = [0-9]+;

syntax Instruction = instruction: Opcode opcode Operand* operands;
	
syntax RascalVM = vm: Instruction+ instructions;

@doc{Registers the muRascalVM language, .vmrsc}
public void registerLanguage() {
	registerLanguage("muRascalVM", "vmrsc", RascalVM (str src, loc l) { return parse(#RascalVM, src, l); });
}