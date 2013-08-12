module experiments::CoreRascal::muRascal::AST

import Prelude;

/*
 * Abstract syntax for muRascal.
 * 
 * Position in the compiler pipeline: Rascal -> muRascal -> RVM
 */

// All information related to one Rascal module

public data MuModule =											
            muModule(str name, list[Symbol] types, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initialization);
          
// All information related to a function declaration. This can be a top-level
// function, or a nested or anomyous function inside a top level function. 
         
public data MuFunction =					
            muFunction(str name, int scope, int nformal, int nlocal, list[MuExp] body)
          ;
          
// A global (module level) variable.
          
public data MuVariable =
            muVariable(str name)
          ;
          
// A declared Rascal type
          
//public data MuType =
//            muType(list[Symbol] symbols)  
//          ;

// All executable Rascal code is tranlated to the following muExps.
          
public data MuExp = 
			// Elementary expressions
			
            muCon(value c)										// Constant: an arbitrary IValue
          | muLab(str name)										// Label
          | muFun(str name)										// Function constant: functions at the root
          | muFun(str name, int scope)                          // Function constant: nested functions and closures
          | muConstr(str name) 									// Constructors
          
          	// Variables
          	
          | muVar(str id, int scope, int pos)					// Variable: retrieve its value
          | muVarRef(str id, int scope, int pos) 				// Call-by-reference: a variable that refers to a value location
          | muRefVar(str id, int scope, int pos) 				// Call-by-reference: expression that returns a value location
             
          | muTypeCon(Symbol tp)								// Type constant
     
     		// Call/return
     		
          | muCall(MuExp fun, list[MuExp] args)					// Call a function
          | muCall(str fname, list[MuExp] args)					// Call a named function: usually from the muRascal runtime library
          | muCallConstr(str cname, list[MuExp] args) 			// Call a constructor
          | muCallPrim(str name, MuExp exp1)					// Call a primitive function with one argument TODO: REDUCE THESE
          | muCallPrim(str name, MuExp exp1, MuExp exp2)		// Call a primitive function with two arguments
          | muCallPrim(str name, list[MuExp] exps)				// Call a primitive function with two arguments
          | muReturn()											// Return from function without value
          | muReturn(MuExp exp)									// Return from function with value
              
           // Assignment, If and While
              
          | muAssign(str id, int scope, int pos, MuExp exp)		// Assign a value to a variable
          | muAssignRef(str id, int scope, int pos, MuExp exp) 	// Call-by-reference assignment: 
          														// the left-hand side is a variable that refers to a value location
          														
          | muIfelse(MuExp cond, list[MuExp] thenPart,			// If-then-else expression
          						 list[MuExp] elsePart)
          						 
          | muWhile(MuExp cond, list[MuExp] body)				// While expression
          
          | muLabeled(str name, list[MuExp] MuExp)				// Labeled list of expressions
          
            // Coroutines
            
          | muCreate(str fname)									// Create a coroutine using a named function
          | muCreate(str fname, list[MuExp] args)				// EXPERIMENTAL
          | muCreate(MuExp exp)									// Create a coroutine using a computed function
          
          | muInit(MuExp coro)									// Initialize a coroutine, no arguments
          | muInit(MuExp coro, list[MuExp] args)				// Initialize a coroutine, with arguments
          
          | muHasNext(MuExp exp)								// HasNext on a coroutine
          
          | muNext(MuExp exp)									// Next on coroutine, no arguments
          | muNext(MuExp exp1, list[MuExp] args)				// Next on coroutine, with arguments
          
          | muYield()											// Yield from coroutine, without value
          | muYield(MuExp exp)									// Yield from coroutine, with value
          
           // Multi-expressions
            
          | muMulti(MuExp exp)		 					// Expression that can produce multiple values
          
          	// Miscellaneous
       
          | muNote(str txt)										// Note that is propagated to generated code and printed during execution
       	  ;
