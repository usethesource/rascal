module lang::rascalcore::compile::muRascal::AST

import Message;
import List;
import Node;   
import ParseTree;

import lang::rascalcore::check::AType;

/*
 * Abstract syntax for muRascal.
 * 
 * Position in the compiler pipeline: Rascal -> muRascal -> RVM
 */

// All information related to one Rascal module

public data MuModule =											
              muModule(str name, 
              		   map[str,str] tags,
                       set[Message] messages,
                       list[str] imports,
                       list[str] extends,
              		   map[str,AType] types, 
              		   map[AType, AProduction] symbol_definitions,
                       list[MuFunction] functions, 
                       list[MuVariable] variables, 
                       list[MuExp] initialization,
                       int nlocals_in_initializations,
                       map[str,int] resolver,
                       lrel[str name, AType funType, str scope, list[str] ofunctions, list[str] oconstructors] overloaded_functions,
                       map[AType, AProduction] grammar,
                       rel[str,str] importGraph,
                       loc src)
            ;
            
MuModule errorMuModule(str name, set[Message] messages, loc src) = muModule(name, (), messages, [], [], (), (), [], [], [], 0, (), [], (), {}, src);
          
// All information related to a function declaration. This can be a top-level
// function, or a nested or anomyous function inside a top level function. 
         
public data MuFunction =					
                muFunction(str qname, str uqname, AType ftype, list[str] argNames, /*AType kwType,*/ str scopeIn, int nformals, int nlocals, bool isVarArgs, bool isPublic,
                           bool simpleArgs, loc src, list[str] modifiers, map[str,str] tags, bool isConcreteArg, int abstractFingerprint, int concreteFingerprint,
                           MuExp body)
              | muCoroutine(str qname, str uqname, str scopeIn, int nformals, int nlocals, loc src, list[int] refs, MuExp body)
          ;
          
// A global (module level) variable.
          
public data MuVariable =
            muVariable(str name)
          ;
          
// A declared Rascal type
          
//public data MuType =
//            muType(list[AType] symbols)  
//          ;

// All executable Rascal code is tranlated to the following muExps.
          
public data MuExp = 
			// Elementary expressions
			muBool(bool b)										// muRascal Boolean constant
		  | muInt(int n)										// muRascal integer constant
          | muCon(value c)										// Rascal Constant: an arbitrary IValue
          
          | muFun1(str fuid)							        // *muRascal* function constant: functions at the root
          | muFun2(str fuid, str scopeIn)                       // *muRascal* function constant: nested functions and closures
          
          | muOFun(str fuid)                                    // *Rascal* function, i.e., overloaded function at the root
          
          | muConstr(str fuid) 									// Constructor
          
          	// Variables
          | muLoc(str name, int pos)							// Local variable, with position in current scope
          | muResetLocs(list[int] positions)					// Reset value of selected local variables to undefined (null)
          | muVar(str name, str fuid, int pos)					// Variable: retrieve its value
          | muTmp(str name, str fuid)							// Temporary variable introduced by front-end
                 
          // Keyword parameters
          | muLocKwp(str name)                                  // Local keyword parameter
          | muVarKwp(str fuid, str name)                        // Keyword parameter
             
          | muTypeCon(AType tp)								// Type constant
          
          // Call/Apply/return    		
          | muCall(MuExp fun, list[MuExp] largs)                 // Call a *muRascal function
          | muApply(MuExp fun, list[MuExp] largs)                // Partial *muRascal function application
          
          | muOCall3(MuExp fun, list[MuExp] largs, loc src)       // Call a declared *Rascal function

          | muOCall4(MuExp fun, AType types,                    // Call a dynamic *Rascal function
          					   list[MuExp] largs, loc src)
          
    
          | muCallPrim2(str name, loc src)                       // Call a Rascal primitive function (with empty list of arguments)
          | muCallPrim3(str name, list[MuExp] exps, loc src)	 // Call a Rascal primitive function
          
          | muCallMuPrim(str name, list[MuExp] exps)			// Call a muRascal primitive function
  
          | muCallJava(str name, str class, 
          			   AType parameterTypes,
          			   AType keywordTypes,
          			   int reflect,
          			   list[MuExp] largs)						// Call a Java method in given class
 
          | muReturn0()											// Return from a function without value
          | muReturn1(MuExp exp)								// Return from a function with value
          
          | muFilterReturn()									// Return for filer statement
          
          | muKwpDefaults(lrel[str name, AType tp, MuExp defaultExp] kwpDefaults)
          | muKwpActuals(lrel[str name, MuExp exp] kwpActuals)
          
          | muInsert(MuExp exp)									// Insert statement
              
          // Assignment, If and While
              
          | muAssignLoc(str name, int pos, MuExp exp)			// Assign a value to a local variable
          | muAssign(str name, str fuid, int pos, MuExp exp)	// Assign a value to a variable
          | muAssignTmp(str name, str fuid, MuExp exp)			// Assign to temporary variable introduced by front-end
          
          // Keyword parameters
          | muAssignLocKwp(str name, MuExp exp)
          | muAssignKwp(str fuid, str name, MuExp exp)
                    														
          | muIfelse(str btscope, MuExp cond, MuExp thenPart,			     // If-then-else expression
          						 MuExp elsePart)
         // | muIfelseBT(str btscope, MuExp cond, MuExp thenPart,    // If-then-else expression with faillabel
         //                        MuExp elsePart)
          | muIf(MuExp cond, MuExp thenPart)
          						 
          | muWhile(str bclabel, MuExp cond, MuExp body)	   // While-Do expression with break/continue label
          
          | muForEach(str btscope, str varName, str fuid, MuExp iterable, MuExp body)
         // | muForEachBT(str btscope, str varName, str fuid, MuExp iterable, MuExp body)
          
          | muForRange(str btscope, str varName, str fuid, MuExp first, MuExp second, MuExp last, MuExp exp)
          //| muForRangeBT(str btscope, str varName, str fuid, MuExp first, MuExp second, MuExp last, MuExp exp)
          | muTypeSwitch(MuExp exp, list[MuTypeCase] type_cases, MuExp \default)  		// switch over cases for specific type
         	
          | muSwitch(MuExp exp, bool useConcreteFingerprint, list[MuCase] cases, MuExp defaultExp)		// switch over cases for specific value
          
          | muEndCase()                                         // Marks the exit point of a case
		  | muBreak(str label)									// Break statement
		  | muContinue(str label)								// Continue statement
		  | muFail(str label)									// Fail statement
		  | muFailReturn()										// Failure from function body
                    
           // Multi-expressions
          
          | muBlock(list[MuExp] exps)  							// A list of expressions, only last value remains
          | muBlockWithTmps(lrel[str name, str fuid] tmps, lrel[str name, str fuid] tmpRefs, list[MuExp] exps)
                                                                // A block with scoped temporary variables and temporaries used as reference
          | muMulti(MuExp exp)		 							// Expression that can produce multiple values
          | muOne1(MuExp exp)                                   // Expression that always produces only the first value
          
          // Exceptions
          
          | muThrow(MuExp exp, loc src)
          
          // Exception handling try/catch
          
          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
          
          | muVisit(bool direction, bool fixedpoint, bool progress, bool rebuild, MuExp descriptor, MuExp phi, MuExp subject, MuExp refHasMatch, MuExp refBeenChanged, MuExp refLeaveVisit, MuExp refBegin, MuExp refEnd)

          | muEqual(MuExp exp1, MuExp exp2)
          | muSubscript(MuExp exp, MuExp idx)
          | muHasType(str typeName, MuExp exp)
          | muHasTypeAndArity(str typeName, int arity, MuExp exp)
          | muHasNameAndArity(str name, int arity, MuExp exp)
          | muValueIsSubType(MuExp exp, AType tp)
          | muInc(str tmpName, str fuid, MuExp inc)
          | muSub(MuExp exp1, MuExp exp2)
          | muAdd(MuExp exp1, MuExp exp2)
          | muSize(MuExp exp)
          | muGreaterEq(MuExp exp1, MuExp exp2)
          | muAnd(MuExp exp1, MuExp exp2)
          | muSubList(MuExp lst, MuExp len)
          | muPrintln(str s)
          | muGetKeywordArg(MuExp exp, str kwname)
          | muHasKeywordArg(MuExp exp, str kwname)
          ;
          
// Simplification rules

MuExp muBlock([MuExp exp]) = exp;

MuExp muReturn1(muReturn1(MuExp exp)) = muReturn1(exp);

MuExp muIfelse("", MuExp cond, MuExp thenPart, muBlock([])) = muIf(cond, thenPart);
MuExp muIfelse(str btscope, MuExp cond, MuExp thenPart, MuExp elsePart) = thenPart when thenPart == elsePart;
MuExp muIfelse(str btscope, muCon(true), MuExp thenPart, MuExp elsePart) = thenPart;
MuExp muIfelse(str btscope, muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;
MuExp muIfelse(str btscope, MuExp cond, muCon(true), MuExp elsePart) = cond;


public MuExp muMulti(muOne1(MuExp exp)) = muOne1(exp);
public MuExp muOne1(muMulti(MuExp exp)) = muOne1(exp);
 
data MuCatch = muCatch(str id, str fuid, AType \type, MuExp body);    

data MuTypeCase = muTypeCase(str name, MuExp exp);
data MuCase = muCase(int fingerprint, MuExp exp);	  
//       	  
//// Auxiliary constructors that are removed by the preprocessor: parse tree -> AST.
//// They will never be seen by later stages of the compiler.
//
//data Identifier =
//				  fvar(str var)
//				| ivar(str var)
//				| rvar(str var)
//				| mvar(str var)
//				;
//
//public data MuPreModule =
//            preMod(str name, list[TypeDeclaration] types, list[Function] functions)
//          ;
//
//public data TypeDeclaration = preTypeDecl(str \type);
//
//public data VarDecl = preVarDecl1(Identifier id)
//                    | preVarDecl2(Identifier id, MuExp initializer)
//                    ;
//                    
//public data Guard = preGuard1(MuExp exp)
//                  | preGuard3(list[VarDecl] locals, str sep, MuExp exp)
//                  ;
//
///*
// * The field 'comma' is a work around given the current semantics of implode 
// */
//public data Function =				
//               preFunction(lrel[str,int] funNames, str name, list[Identifier] formals, 
//                           lrel[list[VarDecl] vardecls, str s] locals, list[MuExp] body, bool comma)
//             | preCoroutine(lrel[str,int] funNames, str name, list[Identifier] formals, 
//                            list[Guard] guard, lrel[list[VarDecl] vardecls, str s] locals, list[MuExp] body, bool comma)
//          ;
//
//
//public data MuExp =
//              preIntCon(str txt)
//            | preStrCon(str txt)  
//            | preTypeCon(str txt)
//            | preVar(Identifier id)
//            | preVar(lrel[str name,int formals] funNames, Identifier id)
//            | preFunNN(str modName, str name, int nformals)
//            | preFunN(lrel[str,int] funNames, str name, int nformals)
//            | preList(list[MuExp] exps)
//            | preAssignLoc(Identifier id, MuExp exp)
//            | preAssign(lrel[str,int] funNames, Identifier id, MuExp exp)
//       
//            | preIfthen(MuExp cond, list[MuExp] thenPart, bool comma)
//            
//            | preMuCallPrim1(str name)                                // Call a Rascal primitive function (with empty list of arguments)
//            | preMuCallPrim2(str name, list[MuExp] exps)				// Call a Rascal primitive function
//            | preThrow(MuExp exp)
//            
//            | preAddition(MuExp lhs, MuExp rhs)
//            | preSubtraction(MuExp lhs, MuExp rhs)
//            | preMultiplication(MuExp lhs, MuExp rhs)
//            | preDivision(MuExp lhs, MuExp rhs)
//            | preModulo(MuExp lhs, MuExp rhs)
//            | prePower(MuExp lhs, MuExp rhs)
//                 
//            | preLess(MuExp lhs, MuExp rhs)
//            | preLessEqual(MuExp lhs, MuExp rhs)
//            | preEqual(MuExp lhs, MuExp rhs)
//            | preNotEqual(MuExp lhs, MuExp rhs)
//            | preGreater(MuExp lhs, MuExp rhs)
//            | preGreaterEqual(MuExp lhs, MuExp rhs)
//            | preAnd(MuExp lhs, MuExp rhs)
//            | preOr(MuExp lhs, MuExp rhs)
//       
//            | preIs(MuExp exp, str typeName)
//            
//            | preLocDeref(Identifier id)
//            | preVarDeref(lrel[str s,int i] funNames, Identifier id)
//            | preLocRef(Identifier id)
//            | preVarRef(lrel[str,int] funNames, Identifier id)
//            
//            | preAssignLocDeref(Identifier id, MuExp exp)
//            | preAssignVarDeref(lrel[str,int] funNames, Identifier id, MuExp exp)
//            
//            | preIfelse(MuExp cond, list[MuExp] thenPart, bool comma1, list[MuExp] elsePart, bool comma2)
//            | preWhile(MuExp cond, list[MuExp] body, bool comma)
//            | preIfelse(str label, MuExp cond, list[MuExp] thenPart, bool comma1, list[MuExp] elsePart, bool comma2)
//            | preWhile(str label, MuExp cond, list[MuExp] body, bool comma)
//            | preTypeSwitch(MuExp exp, lrel[MuTypeCase,bool] sepTypeCases, MuExp \default, bool comma)
// //           | preSwitch(MuExp exp, lrel[MuCase,bool] sepCases, MuExp \default, bool comma)
//            | preBlock(list[MuExp] exps, bool comma)
//            
//            | preSubscript(MuExp arr, MuExp index)
//            | preAssignSubscript(MuExp arr, MuExp index, MuExp exp)
//           ;
           
public bool isOverloadedFunction(muOFun(str _)) = true;
//public bool isOverloadedFunction(muOFun(str _, str _)) = true;
public default bool isOverloadedFunction(MuExp _) = false;


//--------------- constant folding rules ----------------------------------------
// TODO:
// - These rules should go to a separate module
// - Introduce a library function applyPrim(str name, list[value] args) to simplify these rules and cover more cases


bool allConstant(list[MuExp] args) { b = isEmpty(args) || all(a <- args, muCon(_) := a); /*println("allConstant: <args> : <b>"); */return b; }

// muBool, muInt?        
          
// Rascal primitives

// Integer addition

MuExp muCallPrim3("int_add_int", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 + n2);

MuExp muCallPrim3("int_add_int", [muCallPrim3("int_add_int", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("int_add_int", [e, muCon(n1 + n2)], src2);

MuExp muCallPrim3("int_add_int", [muCon(int n1), muCallPrim3("int_add_int", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("int_add_int", [muCon(n1 + n2), e], src2);

// Integer subtraction
 
MuExp muCallPrim3("int_subtract_int", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 - n2);

MuExp muCallPrim3("int_subtract_int", [muCallPrim3("int_subtract_int", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("int_subtract_int", [e, muCon(n1 - n2)], src2);

MuExp muCallPrim3("int_subtract_int", [muCon(int n1), muCallPrim3("int_subtract_int", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("int_subtract_int", [muCon(n1 - n2), e], src2);      

// Integer multiplication

MuExp muCallPrim3("int_product_int", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 * n2);

MuExp muCallPrim3("int_product_int", [muCallPrim3("int_product_int", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("int_product_int", [e, muCon(n1 * n2)], src2);

MuExp muCallPrim3("int_product_int", [muCon(int n1), muCallPrim3("int_product_int", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("int_product_int", [muCon(n1 * n2), e], src2);

// String concatenation

MuExp muCallPrim3("str_add_str", [muCon(str s1), muCon(str s2)], loc src) = muCon(s1 + s2);

MuExp muCallPrim3("str_add_str", [muCallPrim3("str_add_str", [MuExp e, muCon(str s1)], loc src1), muCon(str s2)], loc src2) =
      muCallPrim3("str_add_str", [e, muCon(s1 + s2)], src2);

MuExp muCallPrim3("str_add_str", [muCon(str s1), muCallPrim3("str_add_str", [muCon(str s2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("str_add_str", [muCon(s1 + s2), e], src2);

// Create composite datatypes

MuExp muCallPrim3("list_create", list[MuExp] args, loc src) = muCon([a | muCon(a) <- args]) 
      when allConstant(args);

MuExp muCallPrim3("set_create", list[MuExp] args, loc src) = muCon({a | muCon(a) <- args}) 
      when allConstant(args);
 
// TODO: do not generate constant in case of multiple keys     
MuExp muCallPrim3("map_create", list[MuExp] args, loc src) = muCon((args[i].c : args[i+1].c | int i <- [0, 2 .. size(args)]))
      when allConstant(args);
      
MuExp muCallPrim3("tuple_create", [muCon(v1)], loc src) = muCon(<v1>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2)], loc src) = muCon(<v1, v2>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3)], loc src) = muCon(<v1, v2, v3>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4)], loc src) = muCon(<v1, v2, v3, v4>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5)], loc src) = muCon(<v1, v2, v3, v4, v5>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6)], loc src) = muCon(<v1, v2, v3, v4, v5, v6>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8), muCon(v9) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8, v9>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8), muCon(v9),  muCon(v10) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8, v9, v10>);

MuExp muCallPrim3("node_create", [muCon(str name), *MuExp args, muCallMuPrim("make_mmap", [])], loc src) = muCon(makeNode(name, [a | muCon(a) <- args]))  
      when allConstant(args);


//// muRascal primitives
//
////MuExp muCallMuPrim(str name, list[MuExp] exps) = x;
