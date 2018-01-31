module lang::rascalcore::compile::RVM::AST

import Message;
import ParseTree;

/*
 * Abstract syntax for RVM (Rascal Virtual Machine).
 * 
 * Position in the compiler pipeline: Rascal -> muRascal -> RVM
 */
 
// Declarations for functions and coroutines

public data RVMDeclaration = 
		  FUNCTION(str qname,
		  		   str uqname, 
		  		   Symbol ftype, 
		  		   Symbol kwType,
		  		   str scopeIn, 
		  		   int nformals, 
		  		   int nlocals,
		  		   map[int, str] localNames,
		  		   bool isVarArgs,
		  		   bool isPublic,
		  		   bool isDefault,
		  		   bool isTest,
		  		   bool simpleArgs,
		  		   map[str,str] tags,
		  		   loc src,
		  		   int maxStack,
		  		   bool isConcreteArg, 
		  		   int abstractFingerprint, 
		  		   int concreteFingerprint,
		  		   list[Instruction] instructions,
		  		   lrel[str from, str to, Symbol \type, str target, int fromSP] exceptions,
		  		   set[str] usedOverloadedFunctions,
		  		   set[str] usedFunctions)
		  		   
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
		  		    lrel[str from, str to, Symbol \type, str target, int fromSP] exceptions,
                    set[str] usedOverloadedFunctions,
                    set[str] usedFunctions)
		;

// A single RVMmodule is a container for declarations
// Each Rascal module is mapped to one RVMModule

public data RVMModule = 
		  rvmModule(str name,
		  	  map[str, map[str,str]] module_tags,
		      set[Message] messages,
			  list[str] imports,
			  list[str] extends,
              map[str,Symbol] types, 
              map[Symbol, Production] symbol_definitions,
              list[RVMDeclaration] declarations, // map[str, Declaration] declarations, 
              list[Instruction] initialization, 
              map[str,int] resolver, 
              lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] overloaded_functions,
              rel[str,str] importGraph,
              loc src)
        ;

RVMModule errorRVMModule(str name, set[Message] messages, loc src) = rvmModule(name, (), messages, [], [], (), (), [], [], (), [], {}, src);

// A program is a completely linked collection of modules and is ready for loading.
// A top-level Rascal module (that contains a main function) is mapped to an RVMProgram

public data RVMProgram =
            rvmProgram(
                RVMModule  main_module,
                map[str, map[str,str]] imported_module_tags,
                map[str,Symbol] imported_types,
                list[RVMDeclaration] imported_declarations,
                 map[str,int] imported_overloading_resolvers,
                lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] imported_overloaded_functions
                )
         ; 

RVMProgram errorRVMProgram(RVMModule rvmModule) = rvmProgram(rvmModule, (), (), [], (), []);
RVMProgram errorRVMProgram(set[Message] messages) = rvmProgram(errorRVMModule("XXX", messages, |unknown:///|), (), (), [], (), []);

public data Instruction =

          LOADBOOL(bool bval)						// Push a (Java) boolean
        | LOADINT(int nval)  						// Push a (Java) integer
	   	| LOADCON(value val)						// Load an IValue in accu
	   	| PUSHCON(value val)                        // Push an IValue
	   	| LOADTREE(Tree tree)						// Unused, but forces Tree to be part of the type RVMModule
	   												// This is necessary to guarantee correct (de)serialization and can be removed
	   												// when (de)serialization has been improved.
	   	| LOADTYPE(Symbol \type)					// Load a type constant in accu
	   	| PUSHTYPE(Symbol \type)                    // Push a type constant
	   	
	   	| PUSH_ROOT_FUN(str fuid)                   // Push a named *muRascal function
		| PUSH_NESTED_FUN(str fuid, str scopeIn)    // Push a named nested *muRascal function of a named inner *muRascal function
		| PUSHCONSTR(str fuid)						// Push a constructor function
		
		| PUSHOFUN(str fuid)                        // Push a named *Rascal function
		
		| PUSHACCU()                                // Push accumulator on the stack
		| POPACCU()                                 // Load top of stack in accumulator; pop the stack
		
		| LOADLOC(int pos)                          // Load value of local varibale in accu
		| PUSHLOC(int pos)							// Push value of local variable
		| STORELOC(int pos)							// Store value on top-of-stack in the local variable (value remains on stack)
		| RESETLOCS(list[int] positions)			// Reset selected local variables to undefined (null)
		| RESETLOC(int pos)                         // Reset a local variable to undefined (null)
				
		| LOADLOCKWP(str name)                      // Load value of a keyword parameter in accu
		| PUSHLOCKWP(str name)                      // Push value of a keyword parameter
		| STORELOCKWP(str name)                     // Store value on top-of-stack in the keyword parameter (value remains on stack)
		
		| UNWRAPTHROWNLOC(int pos)                  // Unwrap a thrown value on top-of-stack, and store the unwrapped value in the local variable (value removed from the stack)
		| UNWRAPTHROWNVAR(str fuid, int pos)        // Unwrap a thrown value on top-of-stack, and store the unwrapped value in the variable (value removed from the stack)
	   	
		| LOADVAR(str fuid, int pos)                // Load a variable from an outer scope in accu
		| PUSHVAR(str fuid, int pos)                // Push a variable from an outer scope
		| STOREVAR(str fuid, int pos)               // Store value on top-of-stack in variable in surrounding scope (value remains on stack)
		| RESETVAR(str fuid, int pos)               // Reset a variable form an outer scope to undefined (null)
		
		| LOADVARKWP(str fuid, str name)            // Load a keyword parameter from an outer scope in accu
		| PUSHVARKWP(str fuid, str name)            // Push a keyword parameter from an outer scope
		| STOREVARKWP(str fuid, str name)           // Store value on top-of-stack in the keyword parameter of a surrounding scope (value remains on stack)
		
		| LOADLOCREF(int pos)						// Load a reference to a local variable in accu
		| PUSHLOCREF(int pos)                       // Push a reference to a local variable
		| LOADLOCDEREF(int pos)						// Load value of a local variable identified by reference in accu
		| PUSHLOCDEREF(int pos)                     // Push value of a local variable identified by reference on stack 
		| STORELOCDEREF(int pos)					// Store value at stack[sp - 2] in local variable identified by reference at stack[sp -1] (value remains on stack)
			
		| LOADVARREF(str fuid, int pos)			    // Load a reference to a variable in a surrounding scope in accu
		| PUSHVARREF(str fuid, int pos)             // Push a reference to a variable in a surrounding scope
		| LOADVARDEREF(str fuid, int pos)           // Load value of a variable in outer scope identified by reference in accu
		| PUSHVARDEREF(str fuid, int pos)           // Push value of a variable in outer scope identified by reference on stack 
		| STOREVARDEREF(str fuid, int pos)          // Store value at stack[sp - 2] in outer variable identified by reference at stack[sp -1] (value remains on stack)		
		
		| CALL(str fuid, int arity)					// Call a named *muRascal* function
		| CALLDYN(int arity)						// Call a *muRascal* function on stack
		
		// Partial function application
		| APPLY(str fuid, int arity)                // Apply partially a named *muRascal* function
		| APPLYDYN(int arity)                       // Apply partially a top-of-stack *muRascal* function 
				
		| CALLCONSTR(str fuid, int arity /*, loc src*/)	// Call a constructor
		
		| OCALL(str fuid, int arity, loc src)		// Call a named *Rascal* function
		| OCALLDYN(Symbol types, int arity, loc src)// Call a *Rascal* function on stack
		
//		| CALLMUPRIM(str name, int arity)			// Call a muRascal primitive (see Compiler.RVM.Interpreter.MuPrimitive) 
		                                           /*OBSOLETE*/
		
		| CALLMUPRIM0(str name)                     // Call a muRascal primitive, arity 0, result in accu
		| CALLMUPRIM1(str name)                     // Call a muRascal primitive, arity 1, result in accu 
		| CALLMUPRIM2(str name)                     // Call a muRascal primitive, arity 2, result in accu
		| CALLMUPRIM3(str name)                     // Call a muRascal primitive, arity 3, result in accu
		| CALLMUPRIMN(str name, int arity)          // Call a muRascal primitive, arity arity, result in accu
		
		| PUSHCALLMUPRIM0(str name)                 // Call a muRascal primitive, arity 0, push result on stack
        | PUSHCALLMUPRIM1(str name)                 // Call a muRascal primitive, arity 1, push result on stack
        | PUSHCALLMUPRIM2(str name)                 // Call a muRascal primitive, arity 2, push result on stack
        | PUSHCALLMUPRIM3(str name)                 // Call a muRascal primitive, arity 3, push result on stack
        | PUSHCALLMUPRIMN(str name, int arity)      // Call a muRascal primitive, arity arity, push result on stack
		
		| CALLPRIM0(str name, loc src)              // Call a Rascal primitive, result in accu
		| CALLPRIM1(str name, loc src)              // Call a Rascal primitive, result in accu
		| CALLPRIM2(str name, loc src)              // Call a Rascal primitive, result in accu
		| CALLPRIM3(str name, loc src)              // Call a Rascal primitive, result in accu
		| CALLPRIM4(str name, loc src)              // Call a Rascal primitive, result in accu
		| CALLPRIM5(str name, loc src)              // Call a Rascal primitive, result in accu
		| CALLPRIMN(str name, int arity, loc src)   // Call a Rascal primitive, result in accu
		
		| PUSHCALLPRIM0(str name, loc src)          // Call a Rascal primitive, push result on stack
        | PUSHCALLPRIM1(str name, loc src)          // Call a Rascal primitive, push result on stack
        | PUSHCALLPRIM2(str name, loc src)          // Call a Rascal primitive, push result on stack
        | PUSHCALLPRIM3(str name, loc src)          // Call a Rascal primitive, push result on stack
        | PUSHCALLPRIM4(str name, loc src)          // Call a Rascal primitive, push result on stack
        | PUSHCALLPRIM5(str name, loc src)          // Call a Rascal primitive, push result on stack
        | PUSHCALLPRIMN(str name, int arity, loc src)  // Call a Rascal primitive, push result on stack
		
		
		| CALLJAVA(str name, str class, 
		           Symbol parameterTypes,
		           Symbol keywordTypes,
		           int reflect)			            // Call a Java method
		
		| RETURN0()									// Return from function without value
		| RETURN1()						            // Return from function with value
		| FAILRETURN()								// Failure return from function
		| FILTERRETURN()							// Return for filter statement
		
		| CORETURN0()                               // Return from coroutine without value
        | CORETURN1(int arity)                      // Return from coroutine with value
		
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
		| SUBTYPE()									// Subtype between top two Types on the stack
		| CHECKARGTYPEANDCOPY(
			int pos1, Symbol \type, int pos2)		// Check the type of argument at pos1 and assign to pos2
		
		// Visit
		| VISIT(bool direction, bool fixedpoint, 
		        bool progress, bool rebuild)		// Visit expression
		        
		| CHECKMEMO()								// Check args of memo function
		| PUSHEMPTYKWMAP()                          // Push an empty keyword map
		| VALUESUBTYPE(Symbol \type)                // Check that type of top element is subtype of given type
;
	
