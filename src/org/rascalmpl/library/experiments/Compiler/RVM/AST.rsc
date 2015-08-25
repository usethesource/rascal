module experiments::Compiler::RVM::AST

import Type;
import Message;
import ParseTree;

public data Declaration = 
		  FUNCTION(str qname,
		  		   str uqname, 
		  		   Symbol ftype, 
		  		   str scopeIn, 
		  		   int nformals, 
		  		   int nlocals,
		  		   map[int, str] localNames,
		  		   bool isVarArgs,
		  		   bool isPublic,
		  		   bool isDefault,
		  		   loc src,
		  		   int maxStack,
		  		   bool isConcreteArg, 
		  		   int abstractFingerprint, 
		  		   int concreteFingerprint,
		  		   list[Instruction] instructions,
		  		   lrel[str from, str to, Symbol \type, str target, int fromSP] exceptions)
		  		   
	    | COROUTINE(str qname, 
	                str uqname,
		  		    str scopeIn, 
		  		    int nformals, 
		  		    int nlocals, 
		  		    map[int, str] localNames,
		  		    list[int] refs,
		  		    loc src,
		  		    int maxStack, 
		  		    list[Instruction] instructions,
		  		    lrel[str from, str to, Symbol \type, str target, int fromSP] exceptions)
		;

public data RVMProgram = 
		  rvm(str name,
		  	  map[str,str] tags,
		      set[Message] messages,
			  list[str] imports,
			  list[str] extends,
              map[str,Symbol] types, 
              map[Symbol, Production] symbol_definitions,
              map[str, Declaration] declarations, 
              list[Instruction] initialization, 
              map[str,int] resolver, 
              lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] overloaded_functions,
              rel[str,str] importGraph,
              loc src)
        ;

RVMProgram errorRVMProgram(str name, set[Message] messages, loc src) = rvm(name, (), messages, [], [], (), (), (), [], (), [], {}, src);

public data Instruction =
          LOADBOOL(bool bval)						// Push a (Java) boolean
        | LOADINT(int nval)  						// Push a (Java) integer
	   	| LOADCON(value val)						// Push an IValue
	   	| LOADTREE(Tree tree)						// Unused, but forces Tree to be part of the type RVMProgram
	   												// This is necessary to guarantee correct (de)serialization and can be removed
	   												// when (de)serialization has been improved.
	   	| LOADTYPE(Symbol \type)					// Push a type constant
	   	
	   	| LOADFUN(str fuid)                         // Push a named *muRascal function
		| LOAD_NESTED_FUN(str fuid, str scopeIn)    // Push a named nested *muRascal function of a named inner *muRascal function
		| LOADCONSTR(str fuid)						// Push a constructor function
		
		| LOADOFUN(str fuid)                        // Push a named *Rascal function
		
		| LOADLOC(int pos)							// Push value of local variable
		| STORELOC(int pos)							// Store value on top-of-stack in the local variable (value remains on stack)
		| RESETLOCS(list[int] positions)			// Reset selected local variables to undefined (null)
				
		| LOADLOCKWP(str name)                      // Load value of a keyword parameter
		| STORELOCKWP(str name)                     // Store value on top-of-stack in the keyword parameter (value remains on stack)
		
		| UNWRAPTHROWNLOC(int pos)                  // Unwrap a thrown value on top-of-stack, and store the unwrapped value in the local variable (value removed from the stack)
		| UNWRAPTHROWNVAR(str fuid, int pos)        // Unwrap a thrown value on top-of-stack, and store the unwrapped value in the variable (value removed from the stack)
	   	
		| LOADVAR(str fuid, int pos)                // Push a variable from an outer scope
		| STOREVAR(str fuid, int pos)               // Store value on top-of-stack in variable in surrounding scope (value remains on stack)
		
		| LOADVARKWP(str fuid, str name)            // Load a keyword parameter from an outer scope
		| STOREVARKWP(str fuid, str name)           // Store value on top-of-stack in the keyword parameter of a surrounding scope (value remains on stack)

		| LOADMODULEVAR(str fuid)          			// Push a variable from a global module scope
		| STOREMODULEVAR(str fuid)         			// Store value on  top-of-stack in variable in global module scope (value remains on stack)
		
		| LOADLOCREF(int pos)						// Push a reference to a local variable
		| LOADLOCDEREF(int pos)						// Push value of a local variable identified by reference on stack 
		| STORELOCDEREF(int pos)					// Store value at stack[sp - 2] in local variable identified by reference at stack[sp -1] (value remains on stack)
			
		| LOADVARREF(str fuid, int pos)			    // Push a reference to a variable in a surrounding scope
		| LOADVARDEREF(str fuid, int pos)           // Push value of a variable in outer scope identified by reference on stack 
		| STOREVARDEREF(str fuid, int pos)          // Store value at stack[sp - 2] in outer variable identified by reference at stack[sp -1] (value remains on stack)		
		
		| CALL(str fuid, int arity)					// Call a named *muRascal* function
		| CALLDYN(int arity)						// Call a *muRascal* function on stack
		
		// Partial function application
		| APPLY(str fuid, int arity)                // Apply partially a named *muRascal* function
		| APPLYDYN(int arity)                       // Apply partially a top-of-stack *muRascal* function 
				
		| CALLCONSTR(str fuid, int arity /*, loc src*/)	// Call a constructor
		
		| OCALL(str fuid, int arity, loc src)		// Call a named *Rascal* function
		| OCALLDYN(Symbol types, int arity, loc src)// Call a *Rascal* function on stack
		
		| CALLMUPRIM(str name, int arity)			// Call a muRascal primitive (see Compiler.RVM.Interpreter.MuPrimitive)
		| CALLPRIM(str name, int arity, loc src)	// Call a Rascal primitive (see Compiler.RVM.Interpreter.RascalPrimitive)
		| CALLJAVA(str name, str class, 
		           Symbol parameterTypes,
		           Symbol keywordTypes,
		           int reflect)			            // Call a Java method
		
		| RETURN0()									// Return from function without value
		| RETURN1(int arity)						// Return from function with value
		| FAILRETURN()								// Failure return from function
		| FILTERRETURN()							// Return for filter statement
		
		| THROW(loc src)                            // Throws a value
		
		| LABEL(str label)							// Define a label (is associated with next instruction)
		| JMP(str label)							// Jump to a labelled instruction
		| JMPTRUE(str label)						// Jump to labelled instruction when top-of-stack is true (stack is popped)
		| JMPFALSE(str label)						// Jump to labelled instruction when top-of-stack is false (stack is popped)
													// TODO: JMPTRUE and JMPFALSE currently act on Java booleans and Rascal booleans; this has to be split
		| TYPESWITCH(list[str] labels)				// Switch on type. Takes the type of the value on the stack and jumps to the corresponding label in the list
		| SWITCH(map[int,str] caseLabels, str caseDefault, bool useConcreteFingerprint)
										 			// Switch on arbitrary value. Takes the "fingerprint" of the value on the stack, 
													// finds associated label in map and jumps to it. When 	useConcreteFingerprint is true, fingerprints are computed based
													// non-terminals rather than Tree constructors		
													
		| JMPINDEXED(list[str] labels)				// Computed jump. Takes an integer i from the stack and jumps to the i-th label in the list
		
		| CREATE(str fuid, int arity)               // Creates a co-routine instance 
		| CREATEDYN(int arity)					    // Creates a co-routine instance from the co-routine on top-of-stack.
		| NEXT0()									// Next operation (without argument) on co-routine on top-of-stack
		| NEXT1()									// Next operation (with argument) on co-routine on top-of-stack
		| YIELD0()									// Yield from co-routine without value
		| YIELD1(int arity)							// Yield from co-routine with value
		| EXHAUST()                                 // Return from a coroutine disallowing further resumption;
		| GUARD()                                   // Suspends the current coroutine instance during initialization if true,
		                                            // or terminates it returning false;
		
		| PRINTLN(int arity)						// Print arity values on the stack (TODO: may disappear)
		
		| POP()										// Pop one value from the stack
		
		| HALT()									// Halt execution of the RVM program
		| SUBSCRIPTARRAY()							// Fetch array element with given index (mint)
		| SUBSCRIPTLIST()							// Fetch list element with given index (mint)
		| LESSINT()									// Less between two mints
		| GREATEREQUALINT()							// Greater-equal between two mints
		| ADDINT()									// Add two mints
		| SUBTRACTINT()								// Subtract two mints
		| ANDBOOL()									// and between two mbools.
		
		| TYPEOF()									// Get type of top element
		| SUBTYPE()									// Subtype between top two IValues
		| CHECKARGTYPEANDCOPY(
			int pos1, Symbol \type, int pos2)		// Check the type of argument at pos1 and assign to pos2
		
		// Delimited continuations (experimental)
		| LOADCONT(str fuid)
		| RESET()
		| SHIFT()
		
		// Visit
		| VISIT(bool direction, bool fixedpoint, 
		        bool progress, bool rebuild)		// Visit expression
		        
		| CHECKMEMO()								// Check args of memo function
;
	
