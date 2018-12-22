module lang::rascalcore::compile::muRascal::AST

import Message;
import List;
import Node;   
import ParseTree;
import IO;

import lang::rascalcore::check::AType;
import lang::rascalcore::grammar::definition::Grammar;

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
              		   set[AType] ADTs, 
              		   set[AType] constructors,
                       list[MuFunction] functions, 
                       list[MuModuleVar] module_variables, 
                       list[MuExp] initialization,
                       int nlocals_in_initializations,
                       lrel[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloaded_functions,
                       AGrammar grammar,
                       rel[str,str] importGraph,
                       loc src)
            ;
            
MuModule errorMuModule(str name, set[Message] messages, loc src) = muModule(name, (), messages, [], [], {}, {}, [], [], [], 0, [], grammar({}, ()), src);
          
// All information related to a function declaration. This can be a top-level
// function, or a nested or anomyous function inside a top level function. 
         
public data MuFunction =					
                muFunction(str qname, 
                           str uqname,
                           AType ftype,
                           list[str] argNames,
                           lrel[str name, AType atype, MuExp defaultExp] kwpDefaults, 
                           str scopeIn,
                           int nformals, 
                           int nlocals, 
                           bool isVarArgs,
                           bool isPublic,
                           list[MuExp] externalVars,
                           loc src,
                           list[str] modifiers,
                           map[str,str] tags,
                           MuExp body)
           ;
          
// A global (module level) variable.
          
public data MuModuleVar =
            muModuleVar(AType atype, str name)
          ;

// All executable Rascal code is tranlated to the following muExps.
          
public data MuExp = 
	     // Constants
			muBool(bool b)										// muRascal Boolean constant
		  | muInt(int n)										// muRascal integer constant
          | muCon(value c)						               // Rascal Constant: an arbitrary IValue
          | muTypeCon(AType tp)                                 // AType constant
          
          | muFun1(loc uid /*str fuid*/)   			            // *muRascal* function constant: functions at the root
           
          | muOFun(str fuid)                                    // *Rascal* function, i.e., overloaded function at the root
          
          | muConstr(AType ctype) 					        	// Constructor
          | muConstrCompanion(str fuid)                         // Companion function for constructor with keyword parameters
          
          	// Variables
          | muResetLocs(list[int] positions)					// Reset value of selected local variables to undefined (null)
          | muVar(str name, str fuid, int pos, AType atype)		// Variable: retrieve its value
          //| muLoc(str name, int pos)
          | muTmp(str name, str fuid, AType atype)			    // Temporary variable introduced by front-end
          | muTmpInt(str name, str fuid)                        // Temporary integer variable introduced by front-end
          | muTmpBool(str name, str fuid)                       // Temporary boolean variable introduced by front-end
          | muTmpWriter(str name, str fuid)                     // Temporary list/set/map writer variable introduced by front-end
          | muTmpMatcher(str name, str fuid)                    // Temporary regexp matcher
          | muTmpStrWriter(str name, str fuid)                  // Temporary string write
          | muTmpDescendantIterator(str name, str fuid)         // Temporary descendant iterator
          | muTmpTemplate(str name, str fuid)                   // Temporary string template
          | muTmpException(str name, str fuid)                  // Temporary exception
       
          | muVarKwp(str name, str fuid, AType atype)           // Keyword parameter
          
          // Call/Apply/return    		
          | muCall(MuExp fun, list[MuExp] args)                 // Call a *muRascal function
          
          | muOCall3(MuExp fun, AType atype, list[MuExp] args, loc src)       // Call a declared *Rascal function 
                                                                // Compose fun1 o fun2, i.e., compute fun1(fun2(args))
          | muCallPrim2(str name, loc src)                       // Call a Rascal primitive function (with empty list of arguments)
          | muCallPrim3(str name, list[MuExp] exps, loc src)	 // Call a Rascal primitive function
           
          | muCallJava(str name, str class, AType funType,
          			   int reflect,
          			   list[MuExp] args, str enclosingFun)						// Call a Java method in given class
 
          | muReturn0()											// Return from a function without value
          | muReturn1(MuExp exp)			                    // Return from a function with value
          
          | muReturn0FromVisit()                                // Return from visit without value
          | muReturn1FromVisit(MuExp exp)                       // Return from visit with value
          
          | muFilterReturn()									// Return for filter statement
                             
          | muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)               // Build map of actual keyword parameters
          | muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)  // 
          
          | muIsKwpDefined(MuExp var, str kwpName)
          | muGetKwpFromConstructor(MuExp var, AType atype, str kwpName)
          | muGetKwp(MuExp var, AType atype, str kwpName)
          | muHasKwp(MuExp var, str kwpName)
          //| muHasKwpWithValue(MuExp, str kwpName, MuExp exp)
          
          | muInsert(MuExp exp)									// Insert statement
              
          // Assignment, If and While
              
          
          | muAssign(MuExp var, MuExp exp)                      // Assign a value to a variable
          | muVarInit(MuExp var, MuExp exp)                     // Assign a value to a variable
          | muConInit(MuExp var, MuExp exp)                     // Create a constant
          
          | muIfEqualOrAssign(MuExp var, MuExp other, MuExp body)
                    														
          | muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)// If-then-else statement
          | muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart) // conditional expression
        
          | muIf(MuExp cond, MuExp thenPart)
          						 
          | muWhileDo(str label, MuExp cond, MuExp body)	         // While-Do expression with break/continue label
          | muDoWhile(str label, MuExp body, MuExp cond)
          //| muForAny(str label, MuExp var, MuExp iterable, MuExp body)
          | muForAll(str label, MuExp var, MuExp iterable, MuExp body)
          | muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp)
          | muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp)
         
          // Backtracking
          
          | muEnter(str btscope, MuExp exp)                     // Enter a backtracking scope
          | muSucceed(str btscope)
          | muFail(str label)                                 // Fail statement
          
          | muVisit(MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
          | muDescendantMatchIterator(MuExp subject, DescendantDescriptor ddescriptor)
        
          | muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)		// switch over cases for specific value
          
          | muFailCase()                                        // Marks the failure exit point of a switch or visit case
          | muSucceedSwitchCase()                              // Marks a success exit point of a switch case
       
          | muSucceedVisitCase()                               // Marks a success exit point of a visit case
      
		  | muBreak(str label)									// Break statement
		  | muContinue(str label)								// Continue statement
		 
		  | muFailReturn()										// Failure from function body
                    
           // Multi-expressions
          | muBlock(list[MuExp] exps)                           // A list of expressions that does not deliver a value
          | muValueBlock(list[MuExp] exps)  				    // A list of expressions, only last value remains
                                                             
          // Exceptions
          
          | muThrow(MuExp exp, loc src)
          
          // Exception handling try/catch
          
          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
          | muRequire(MuExp exp, str msg, loc src)              // Abort is exp is false
          | muCheckArgTypeAndCopy(str name, int fromPos, AType atype, int toPos)
          | muEqual(MuExp exp1, MuExp exp2)
          | muEqualInt(MuExp exp1, MuExp exp2)
          | muSubscript(MuExp exp, MuExp idx)
          //| muHasType(str typeName, MuExp exp)
          | muHasTypeAndArity(AType atype, int arity, MuExp exp)
          | muHasNameAndArity(AType atype, str name, int arity, MuExp exp)
          | muValueIsSubType(MuExp exp, AType tp)
          | muValueIsSubTypeOfValue(MuExp exp2, MuExp exp1)
          | muIncVar(MuExp var, MuExp inc)
          | muSubInt(MuExp exp1, MuExp exp2)
          | muAddInt(MuExp exp1, MuExp exp2)
          | muSize(MuExp exp, AType atype)
          | muGreaterEqInt(MuExp exp1, MuExp exp2)
          | muAnd(MuExp exp1, MuExp exp2)
          | muNot(MuExp exp)
          | muNotNegative(MuExp exp)
          | muSubList(MuExp lst, MuExp from, MuExp len)
          
          | muFieldAccess(str kind, AType consType, MuExp exp, str fieldName)
          | muKwpFieldAccess(str kind, AType consType, MuExp exp, str fieldName)

          | muFieldUpdate(str kind, AType atype, MuExp exp1, str fieldName, MuExp exp2)

          | muRegExpCompile(MuExp regExp, MuExp subject)
          | muRegExpBegin(MuExp matcher)
          | muRegExpEnd(MuExp matcher)
          | muRegExpFind(MuExp matcher)
          | muRegExpSetRegion(MuExp matcher, int begin, int end)
          | muRegExpGroup(MuExp matcher, int n)
          
          | muTemplate(str initial)
          | muTemplateBeginIndent(MuExp template, str indent)
          | muTemplateEndIndent(MuExp template, str unindent)
          | muTemplateAdd(MuExp template, value val)
          | muTemplateClose(MuExp template)
          ;
          
 data VisitDescriptor
    = visitDescriptor(bool direction, bool fixedpoint, bool progress, bool rebuild, DescendantDescriptor descendant)
    ;
    
data DescendantDescriptor
    = descendantDescriptor(bool useConcreteFingerprint /*reachable_syms, reachable_prods,*/  /*,getDefinitions()*/)
    ;
    
data MuCatch = muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body);    

data MuCase = muCase(int fingerprint, MuExp exp);

// ==== Utilities =============================================================

set[str] varExp = {"muModuleVar", "muVar", "muTmp", "muTmpInt", 
                   "muTmpBool", "muTmpWriter", "muTmpMatcher", 
                   "muTmpStrWriter", "muTmpTemplate", "muTmpException"};

bool isVarOrTmp(MuExp exp)
    = getName(exp) in varExp;
    
bool producesNativeBool(muCallPrim3(str name, list[MuExp] args, loc src)){
    if(name in {"equal", "notequal"}) return true;
    fail producesNativeBool;
}

bool producesNativeBool(MuExp exp)
    = getName(exp) in {"muTmpBool", "muEqual", "muEqualInt", "muNotNegative", "muIsKwpDefined", "muHasKwp", "muHasKwpWithValue", /*"muHasType",*/ "muHasTypeAndArity",
                  "muHasNameAndArity", "muValueIsSubType", "muValueIsSubTypeOfValue", "muGreaterEqInt", "muAnd", "muNot",
                  "muRegExpFind" };
                  
bool producesNativeInt(MuExp exp)
    = getName(exp) in {"muTmpInt", "muSize", "muAddInt", "muSubInt", "muRegExpBegin", "muRegExpEnd"};
     
// ==== Simplification rules ==================================================

MuExp muBlock([MuExp exp]) = exp;

MuExp muBlock([ *exps1, muBlock([*exps2]), *exps3 ])
    = muBlock([ *exps1, *exps2, *exps3 ]);
    
MuExp muBlock([ *exps1, muReturn1(exp), *exps2 ])
    = muBlock([ *exps1, muReturn1(exp) ])
    when !isEmpty(exps2); 
    
MuExp muBlock([ *exps1, muInsert(exp), *exps2])
    = muBlock([*exps1, muInsert(exp)])
    when !isEmpty(exps2);     

MuExp muBlock([*MuExp pre, muValueBlock(list[MuExp] elems), *MuExp post])
    = muBlock([*pre, *elems, *post]);
    
MuExp muValueBlock([*MuExp pre, muBlock(list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock([*pre, *elems, *post, last]);
    
MuExp muValueBlock([*MuExp pre, muValueBlock(list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock([*pre, *elems, *post, last]);

// ---- muReturn1 -------------------------------------------------------------

MuExp muReturn1(muReturn1(MuExp exp)) = muReturn1(exp);

MuExp muReturn1(muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muReturn1(exp)]);
    
MuExp muReturn1(muValueBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muReturn1(exp)]);
    
MuExp muReturn1(muAssign(MuExp var, MuExp exp))
    = muBlock([muAssign(var, exp), muReturn1(var)]);

MuExp muReturn1(muIfEqualOrAssign(MuExp var, MuExp other, MuExp body))
    = muReturn1(muEqual(var, other));
    
MuExp muReturn1(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muIfelse(cond, muReturn1(thenPart), muReturn1(elsePart));

MuExp muReturn1(muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp))
    = muForRangeInt(label, var, ifirst, istep, last, muReturn1(exp));
    
MuExp muReturn1(muForAll(str label, MuExp var, MuExp iterable, MuExp body))
    = muForAll(label, var, iterable, muReturn1(body));

MuExp addReturnFalse(bl: muBlock([*exps, muReturn1(muCon(false))])) = bl;

default MuExp addReturnFalse(MuExp exp) = muBlock([exp, muReturn1(muCon(false))]);

MuExp muReturn1(muEnter(str btscope, MuExp exp))
    = muEnter(btscope, addReturnFalse(visit(exp) { case muSucceed(btscope) => muReturn1(muCon(true))
                                                   //case muFail(btscope) => muReturn1(muCon(false))
                                                 }));
    
MuExp muReturn1(muSucceed(str btscope))
    = muReturn1(muCon(true));

MuExp muReturn1(muContinue(str btscope))
    =   muContinue(btscope);
     
MuExp muReturn1(muFail(str btscope)){
   return  muContinue(btscope);// : muReturn1(muCon(false));
}

MuExp muReturn1(muFailReturn())
    = muFailReturn();
    
MuExp muReturn1(muTry(MuExp exp, MuCatch \catch, MuExp \finally))
    = muTry(muReturn1(exp), \catch, \finally);
    
MuExp muReturn1(muThrow(MuExp exp, loc src))
    = muThrow(exp, src);
    
// ---- muThrow ---------------------------------------------------------------


    
// ---- muAssign --------------------------------------------------------------

MuExp muAssign(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);

MuExp muAssign(MuExp var, muValueBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);

MuExp muAssign(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);
    
MuExp muAssign(MuExp var, muForAll(str label, MuExp loopVar, MuExp iterable, MuExp body))
    = muBlock([ muAssign(var, muCon(false)), muForAll(label, loopVar, iterable, muAssign(var, body)) ]);
    
MuExp muAssign(var, muEnter(str btscope, MuExp exp))
    = muEnter(btscope, visit(exp) { case muSucceed(btscope) => muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]) });
 
MuExp muAssign(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart));

MuExp muAssign(MuExp var1, muIfEqualOrAssign(MuExp var2, MuExp other, MuExp body))
    = muIfEqualOrAssign(var2, other, muBlock([muAssign(var1, other), body]));
    
MuExp muAssign(MuExp var, muSucceed(str btscope))
    = muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]);
    
MuExp muAssign(MuExp var, muFail(btscope))
    = muBlock([muAssign(var, muCon(false))]);
    
MuExp muConInit(MuExp var, muValueBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muConInit(var, exp)]);

MuExp muVarInit(MuExp var, muValueBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muVarInit(var, exp)]);


// ---- muIfelse --------------------------------------------------------------

MuExp muIfelse(MuExp cond, MuExp thenPart, muBlock([])) = muIf(cond, thenPart);
MuExp muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart) = thenPart when thenPart == elsePart;
MuExp muIfelse(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart;
MuExp muIfelse(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

//MuExp muEnter(str btscope, muIfelse(str btscope2, MuExp cond, MuExp thenPart, MuExp elsePart))
//    =  muIfelse(btscope, cond, muEnter(btscope, thenPart), muEnter(btscope, elsePart));


MuExp muEnter(str label, muForAll(label, MuExp var, MuExp iterable, MuExp body))
    = muForAll(label, var, iterable, body);
    
MuExp muForAll(str label, str varName, str fuid, MuExp iterable, MuExp body)
    = muForAll(label, varName, fuid, iterable, body1)
    when body1 := visit(body) { case muSucceed(str btscope) => muSucceed(label) } && body1 != body;
 
MuExp muRegExpCompile(muValueBlock([*MuExp exps, MuExp regExp]), MuExp subject)
    = muValueBlock([ *exps, muRegExpCompile(regExp, subject)]);
          
//public bool isOverloadedFunction(muOFun(str _)) = true;
////public bool isOverloadedFunction(muOFun(str _, str _)) = true;
//public default bool isOverloadedFunction(MuExp _) = false;

// ============ Flattening Rules ================================================
// TODO: shoud go to separate module (does not work in interpreter)

bool shouldFlatten(MuExp arg) 
    =  muValueBlock(elems) := arg || muEnter(btscope, exp) := arg;
 
int nauxVars = -1;
         
// Rascal primitives
tuple[bool flattened, list[MuExp] auxVars, list[MuExp] pre, list[MuExp] post] flattenArgs(list[MuExp] args){
    if(!isEmpty(args) && any(arg <- args , shouldFlatten(arg))){
        pre = [];
        newArgs = [];
        auxVars = [];
        for(MuExp arg <- args){
            if(muValueBlock(elems) := arg){
                pre += elems[0..-1];
                newArgs += elems[-1];
            } else if(me: muEnter(btscope, exp) := arg){
                nauxVars += 1;
                aux = muTmp("$aux<nauxVars>", "", abool());
                auxVars += muVarInit(aux, muCon(false));
                pre += muAssign(aux, me);
                newArgs += aux;
            } else {
                newArgs += arg;
            }
        }
        return <true, auxVars, pre, newArgs>;
    } else {
       return <false, [], [], args>;
    }
}

MuExp  muCall(MuExp fun, list[MuExp] args) 
    = muValueBlock(auxVars + pre + muCall(fun, flatArgs))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

MuExp muOCall3(MuExp fun, AType atype, list[MuExp] args, loc src)
    = muValueBlock(auxVars + pre + muOCall3(fun, atype, flatArgs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

MuExp muCallPrim3(str op, list[MuExp] args, loc src)
    = muValueBlock(auxVars + pre + muCallPrim3(op, flatArgs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

MuExp muCallJava(str name, str class, AType parameterTypes, AType keywordTypes, int reflect, list[MuExp] args)
    = muValueBlock(auxVars + pre + muCallJava(name, class, parameterTypes, keywordTypes, reflect, flatArgs))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

MuExp muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)
    = muValueBlock(auxVars + pre + muKwpActuals([<kwpActuals[i].kwpName, flatArgs[i]> | int i <- index(kwpActuals)]))
    when <true, auxVars, pre, flatArgs> := flattenArgs(kwpActuals<1>);
      
MuExp muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)
    = muValueBlock(auxVars + pre + muKwpMap([<dflt.kwName, dflt.atype, flatArgs[i]> | int i <- index(defaults), dflt := defaults[i]]))
    when <true, auxVars, pre, flatArgs> := flattenArgs(defaults<1>);  
    
//muHasKwpWithValue?

MuExp muInsert(MuExp arg)
    = muValueBlock(auxVars + pre + muInsert(flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([arg]);
    
// TODO: defaultExp?
MuExp muVisit(MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
   = muValueBlock(auxVars + pre + muVisit(flatArgs[0], cases, defaultExp, vdescriptor))
    when <true, auxVars, pre, flatArgs> := flattenArgs([subject]);

//muSwitch
MuExp muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)        // switch over cases for specific value
    = muValueBlock(auxVars + pre + muSwitch(flatArgs[0], cases, defaultExp, useConcreteFingerprint))
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]);

//muThrow
//muTry
//muFieldAccess(str kind, AType consType, MuExp exp, str fieldName)
//muKwpFieldAccess(str kind, AType consType, MuExp exp, str fieldName)
//muFieldUpdate(str kind, AType atype, MuExp exp1, str fieldName, MuExp exp2)
  

MuExp muValueIsSubType(MuExp exp, AType tp) = muCon(true) when !isVarOrTmp(exp) && exp has atype && exp.atype == tp;

//============== constant folding rules =======================================
// TODO:
// - These rules should go to a separate module (but the interpreter does not like this)
// - Introduce a library function applyPrim(str name, list[value] args) to simplify these rules and cover more cases

bool allConstant(list[MuExp] args) { b = isEmpty(args) || all(a <- args, muCon(_) := a); /*println("allConstant: <args> : <b>"); */return b; }

// Integer addition

MuExp muCallPrim3("aint_add_aint", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 + n2);

MuExp muCallPrim3("aint_add_aint", [muCallPrim3("aint_add_aint", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("aint_add_aint", [e, muCon(n1 + n2)], src2);

MuExp muCallPrim3("aint_add_aint", [muCon(int n1), muCallPrim3("aint_add_aint", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("aint_add_aint", [muCon(n1 + n2), e], src2);

// Integer subtraction
 
MuExp muCallPrim3("aint_subtract_aint", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 - n2);

MuExp muCallPrim3("aint_subtract_aint", [muCallPrim3("aint_subtract_aint", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("aint_subtract_aint", [e, muCon(n1 - n2)], src2);

MuExp muCallPrim3("aint_subtract_aint", [muCon(int n1), muCallPrim3("aint_subtract_aint", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("aint_subtract_aint", [muCon(n1 - n2), e], src2);      

// Integer multiplication

MuExp muCallPrim3("aint_product_aint", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 * n2);

MuExp muCallPrim3("aint_product_aint", [muCallPrim3("aint_product_aint", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("aint_product_aint", [e, muCon(n1 * n2)], src2);

MuExp muCallPrim3("aint_product_aint", [muCon(int n1), muCallPrim3("aint_product_aint", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("aint_product_aint", [muCon(n1 * n2), e], src2);

// String concatenation

MuExp muCallPrim3("astr_add_astr", [muCon(str s1), muCon(str s2)], loc src) = muCon(s1 + s2);

MuExp muCallPrim3("astr_add_astr", [muCallPrim3("astr_add_astr", [MuExp e, muCon(str s1)], loc src1), muCon(str s2)], loc src2) =
      muCallPrim3("astr_add_astr", [e, muCon(s1 + s2)], src2);

MuExp muCallPrim3("astr_add_astr", [muCon(str s1), muCallPrim3("astr_add_astr", [muCon(str s2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("astr_add_astr", [muCon(s1 + s2), e], src2);

// Create composite datatypes

MuExp muCallPrim3("alist_create", list[MuExp] args, loc src) = muCon([a | muCon(a) <- args]) 
      when allConstant(args);

MuExp muCallPrim3("aset_create", list[MuExp] args, loc src) = muCon({a | muCon(a) <- args}) 
      when allConstant(args);
 
// TODO: do not generate constant in case of multiple keys     
MuExp muCallPrim3("amap_create", list[MuExp] args, loc src) = muCon((args[i].c : args[i+1].c | int i <- [0, 2 .. size(args)]))
      when allConstant(args);
      
MuExp muCallPrim3("atuple_create", [muCon(v1)], loc src) = muCon(<v1>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2)], loc src) = muCon(<v1, v2>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3)], loc src) = muCon(<v1, v2, v3>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4)], loc src) = muCon(<v1, v2, v3, v4>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5)], loc src) = muCon(<v1, v2, v3, v4, v5>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6)], loc src) = muCon(<v1, v2, v3, v4, v5, v6>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8), muCon(v9) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8, v9>);
MuExp muCallPrim3("atuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8), muCon(v9),  muCon(v10) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8, v9, v10>);

//MuExp muCallPrim3("anode_create", [muCon(str name), *MuExp args, muCallMuPrim("make_mmap", [])], loc src) = muCon(makeNode(name, [a | muCon(a) <- args]))  
//      when allConstant(args);
