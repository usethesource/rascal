module lang::rascalcore::compile::muRascal::AST

import Message;
import List;
import Set;
import Node;   
import ParseTree;
import IO;

import lang::rascalcore::check::AType;
//import lang::rascalcore::grammar::definition::Grammar;

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
            
MuModule errorMuModule(str name, set[Message] messages, loc src) = muModule(name, (), messages, [], [], {}, {}, [], [], [], 0, [], grammar({}, ()), {}, src);
          
// All information related to a function declaration. This can be a top-level
// function, or a nested or anomyous function inside a top level function. 
         
public data MuFunction =					
                muFunction(str qname, 
                           str uqname,
                           AType ftype,
                           list[MuExp] formals,
                           //list[str] argNames,
                           lrel[str name, AType atype, MuExp defaultExp] kwpDefaults, 
                           str scopeIn,
                           int nformals, 
                           int nlocals, 
                           bool isVarArgs,
                           bool isPublic,
                           bool isMemo,
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

// Kind of temporary variables introduced by the compiler

data NativeKind
    = nativeInt()
    | nativeBool()
    | nativeListWriter()
    | nativeSetWriter()
    | nativeMapWriter()
    | nativeMatcher()
    | nativeStrWriter()
    | nativeDescendantIterator()
    | nativeTemplate()
    | nativeException()
    | nativeGuardedIValue()
    ;
    
MuExp muTmpInt(str name, str fuid)                  = muTmpNative(name, fuid, nativeInt());
MuExp muTmpBool(str name, str fuid)                 = muTmpNative(name, fuid, nativeBool());
MuExp muTmpListWriter(str name, str fuid)           = muTmpNative(name, fuid, nativeListWriter());
MuExp muTmpSetWriter(str name, str fuid)            = muTmpNative(name, fuid, nativeSetWriter());
MuExp muTmpMapWriter(str name, str fuid)            = muTmpNative(name, fuid, nativeMapWriter());
MuExp muTmpMatcher(str name, str fuid)              = muTmpNative(name, fuid, nativeMatcher());
MuExp muTmpStrWriter(str name, str fuid)            = muTmpNative(name, fuid, nativeStrWriter());
MuExp muTmpDescendantIterator(str name, str fuid)   = muTmpNative(name, fuid, nativeDescendantIterator());
MuExp muTmpTemplate(str name, str fuid)             = muTmpNative(name, fuid, nativeTemplate());
MuExp muTmpException(str name, str fuid)            = muTmpNative(name, fuid, nativeException());
MuExp muTmpGuardedIValue(str name, str fuid)        = muTmpNative(name, fuid, nativeGuardedIValue());

    
// All executable Rascal code is tranlated to the following muExps.
          
public data MuExp = 
            muCon(value c)						                // Rascal Constant: an arbitrary IValue
          | muNoValue()                                         // Absent value in optional construct
          | muATypeCon(AType atype, map[AType,set[AType]] defs) // AType as constant
          
          | muFun1(loc uid /* str fuid*/)   			        // *muRascal* function constant: functions at the root
           
          | muOFun(str fuid)                                    // *Rascal* function, i.e., overloaded function at the root
          
          | muConstr(AType ctype) 					        	// Constructor
          | muConstrCompanion(str fuid)                         // Companion function for constructor with keyword parameters
          
          	// Variables and temporaries
          | muResetLocs(list[int] positions)					// Reset value of selected local variables to undefined (null)
          | muVar(str name, str fuid, int pos, AType atype)		// Variable: retrieve its value
          | muTmpIValue(str name, str fuid, AType atype)	    // Temporary variable introduced by compiler
          | muTmpNative(str name, str fuid, NativeKind nkind)   // Temporary variable introduced by compiler
             
          | muVarKwp(str name, str fuid, AType atype)           // Keyword parameter
          
          // Call and return    		
          | muCall(MuExp fun, AType atype, list[MuExp] args)     // Call a *muRascal function
          
          | muOCall3(MuExp fun, AType atype, list[MuExp] args, loc src)       
                                                                // Call a declared *Rascal function 
                                                                // Compose fun1 o fun2, i.e., compute fun1(fun2(args))
          | muCallPrim3(str name, AType result, list[AType] details, list[MuExp] exps, loc src)	 // Call a Rascal primitive
           
          | muCallJava(str name, str class, AType funType,
          			   int reflect,
          			   list[MuExp] args, str enclosingFun)		// Call a Java method in given class
 
          | muReturn0()											// Return from a function without value
          | muReturn1(AType result, MuExp exp)			        // Return from a function with value
          
          | muReturn0FromVisit()                                // Return from visit without value
          | muReturn1FromVisit(AType result, MuExp exp)         // Return from visit with value
          
          | muFilterReturn()									// Return for filter statement
          | muFailReturn(AType funType)                         // Failure from function body
          
          | muCheckMemo(AType funType, list[MuExp] args/*, map[str,value] kwargs*/, MuExp body)
          | muMemoReturn(AType funtype, list[MuExp] args, MuExp functionResult)
               
          // Keyword parameters              
          | muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)
                                                                // Build map of actual keyword parameters
          | muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)  
          
          | muIsKwpDefined(MuExp var, str kwpName)
          | muGetKwpFromConstructor(MuExp var, AType atype, str kwpName)
          | muGetKwp(MuExp var, AType atype, str kwpName)
          | muHasKwp(MuExp var, str kwpName)
          //| muHasKwpWithValue(MuExp, str kwpName, MuExp exp)
          
          | muInsert(MuExp exp, AType atype)				    // Insert statement
              
          // Get and assign values
          
          | muAssign(MuExp var, MuExp exp)                      // Assign a value to a variable
          | muVarInit(MuExp var, MuExp exp)                     // Assign a value to a variable
          | muConInit(MuExp var, MuExp exp)                     // Create a constant
          
          | muGetAnno(MuExp exp, AType resultType, str annoName)
          | muGuardedGetAnno(MuExp exp, AType resultType, str annoName)
          | muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
          
          | muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGuardedGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muKwpGetField(AType resultType, AType consType, MuExp exp, str fieldName)

          | muSetField(AType resultType, AType baseTtype, MuExp baseExp, value fieldIdentity, MuExp repl)
          
          // conditionals and iterations
          
          | muIfEqualOrAssign(MuExp var, MuExp other, MuExp body)
                    														
          | muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)// If-then-else statement
          | muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart) // conditional expression
        
          | muIf(MuExp cond, MuExp thenPart)
          						 
          | muWhileDo(str label, MuExp cond, MuExp body)	    // While-Do expression with break/continue label
          | muDoWhile(str label, MuExp body, MuExp cond)
          | muBreak(str label)                                  // Break statement
          | muContinue(str label)                               // Continue statement
          
        
          | muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body)
          | muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp)
          | muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp)
         
          // Backtracking
          
          | muEnter(str btscope, MuExp exp)                   // Enter a backtracking scope
          | muSucceed(str btscope)
          | muFail(str label)                                 // Fail statement
          
          //  Visit
          | muVisit(MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
          | muDescendantMatchIterator(MuExp subject, DescendantDescriptor ddescriptor)
          | muSucceedVisitCase()                              // Marks a success exit point of a visit case
        
          // Switch
          | muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)		// switch over cases for specific value
          
          | muFailCase()                                      // Marks the failure exit point of a switch or visit case
          | muSucceedSwitchCase()                             // Marks a success exit point of a switch case
                 
           // Multi-expressions
          | muBlock(list[MuExp] exps)                         // A list of expressions that does not deliver a value
          | muValueBlock(AType result, list[MuExp] exps)  				  // A list of expressions, only last value remains
                                                             
          // Exceptions
          
          | muThrow(MuExp exp, loc src)
          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
          
          // Auxiliary operations used in generated code
          
          // Various tests
          | muRequire(MuExp exp, str msg, loc src)              // Abort if exp is false
          
          | muCheckArgTypeAndCopy(str name, int fromPos, AType atype, int toPos)
          | muEqual(MuExp exp1, MuExp exp2)
          | muEqualNativeInt(MuExp exp1, MuExp exp2)
          
          //| muHasType(str typeName, MuExp exp)
          | muHasTypeAndArity(AType atype, int arity, MuExp exp)
          | muHasNameAndArity(AType atype, str name, int arity, MuExp exp)
          | muValueIsSubType(MuExp exp, AType tp)
          | muValueIsSubTypeOfValue(MuExp exp2, MuExp exp1)
          | muIsDefinedValue(MuExp exp)
          | muGetDefinedValue(MuExp exp, AType tp)
          | muHasField(MuExp exp, AType tp, str fieldName)
          
          | muSubscript(MuExp exp, MuExp idx)
          
          // Operations on native integers
          | muIncNativeInt(MuExp var, MuExp inc)
          | muSubNativeInt(MuExp exp1, MuExp exp2)
          | muAddNativeInt(MuExp exp1, MuExp exp2)
          | muSize(MuExp exp, AType atype)
          | muGreaterEqNativeInt(MuExp exp1, MuExp exp2)
          
          // Operations on native booleans
          | muAndNativeBool(MuExp exp1, MuExp exp2)
          | muNotNativeBool(MuExp exp)
          | muNotNegativeNativeInt(MuExp exp)
          | muSubList(MuExp lst, MuExp from, MuExp len)
          
          // Regular expressions
          | muRegExpCompile(MuExp regExp, MuExp subject)
          | muRegExpBegin(MuExp matcher)
          | muRegExpEnd(MuExp matcher)
          | muRegExpFind(MuExp matcher)
          | muRegExpSetRegion(MuExp matcher, int begin, int end)
          | muRegExpGroup(MuExp matcher, int n)
          
          // String templates
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

set[str] varExp = {"muModuleVar", "muVar", "muTmpIValue", "muTmpNative"};

bool isVarOrTmp(MuExp exp)
    = getName(exp) in varExp;
    
bool producesNativeBool(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] args, loc src)){
    if(name in {"equal", "notequal", "is", "subset"}) return true;
    fail producesNativeBool;
}

bool producesNativeBool(muTmpNative(_,_,nativeBool()))
    = true;
    
default bool producesNativeBool(MuExp exp)
    = getName(exp) in {"muEqual", "muEqualNativeInt", "muNotNegativeNativeInt", "muIsKwpDefined", "muHasKwp", "muHasKwpWithValue", /*"muHasType",*/ "muHasTypeAndArity",
                  "muHasNameAndArity", "muValueIsSubType", "muValueIsSubTypeOfValue", "muGreaterEqNativeInt", "muAndNativeBool", "muNotNativeBool",
                  "muRegExpFind",  "muIsDefinedValue", "muHasField"};

bool producesNativeInt(muTmpNative(_,_,nativeInt()))
    = true;
                     
default bool producesNativeInt(MuExp exp)
    = getName(exp) in {"muSize", "muAddNativeInt", "muSubNativeInt", "muRegExpBegin", "muRegExpEnd"};
         
// ==== Simplification rules ==================================================

// ---- endsWithReturn --------------------------------------------------------

bool endsWithReturn(muReturn0()) = true;
bool endsWithReturn(muReturn1(_, _)) = true;
bool endsWithReturn(muFailReturn(_)) = true;
bool endsWithReturn(muCheckMemo(_,_,_)) = true;
bool endsWithReturn(muMemoReturn(_,_,_)) = true;
bool endsWithReturn(muThrow(_,_)) = true;
bool endsWithReturn(muBlock([*exps1, exp2])) = true when endsWithReturn(exp2);
bool endsWithReturn(muValueBlock(AType t, [*exps1, exp2])) = true when endsWithReturn(exp2);
bool endsWithReturn(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)) = true when endsWithReturn(thenPart), endsWithReturn(elsePart);
bool endsWithReturn(muDoWhile(str label, MuExp body, muCon(false))) = endsWithReturn(body);
bool endsWithReturn(muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body)) = endsWithReturn(body);
bool endsWithReturn(muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp)) = endsWithReturn(exp);
bool endsWithReturn(muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp)) = endsWithReturn(exp);
bool endsWithReturn(muEnter(str label, MuExp body)) = endsWithReturn(body);

default bool endsWithReturn(MuExp exp) = false;


// ---- block -----------------------------------------------------------------

MuExp muBlock([MuExp exp]) = exp;

MuExp muBlock([ *exps1, muBlock([*exps2]), *exps3 ])
    = muBlock([ *exps1, *exps2, *exps3 ]);
    
MuExp muBlock([ *exps1, muReturn1(t, exp), *exps2 ])
    = muBlock([ *exps1, muReturn1(t, exp) ])
    when !isEmpty(exps2); 
    
MuExp muBlock([ *exps1, muInsert(t, exp), *exps2])
    = muBlock([*exps1, muInsert(t, exp)])
    when !isEmpty(exps2);     

MuExp muBlock([*MuExp pre, muValueBlock(AType t, list[MuExp] elems), *MuExp post])
    = muBlock([*pre, *elems, *post]);
    
MuExp muBlock([*MuExp pre, MuExp exp, *MuExp post]){
    if(!isEmpty(post) && endsWithReturn(exp)) return muBlock([*pre, exp]);
    fail;
}
    
// ---- muValueBlock ----------------------------------------------------------
    
MuExp muValueBlock(AType t, [*MuExp pre, muBlock(list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t, [*pre, *elems, *post, last]);
    
MuExp muValueBlock(AType t1, [*MuExp pre, muValueBlock(AType t2, list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t2, [*pre, *elems, *post, last]);
    
MuExp muValueBlock(AType t, [*MuExp pre, MuExp exp, *MuExp post]){
    if(!isEmpty(post) && endsWithReturn(exp)) return muValueBlock(t, [*pre, exp]);
    fail;
} 

MuExp muValueBlock(AType t1, [*MuExp pre, muReturn1(AType t2, MuExp exp)])
    = muBlock([*pre, muReturn1(t2, exp)]);
    
 MuExp muValueBlock(AType t, [ite: muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)])
    =  ite;
    
// ---- muReturn1 -------------------------------------------------------------

MuExp muReturn1(AType t1, muReturn1(AType t2, MuExp exp)) = muReturn1(t2, exp);

MuExp muReturn1(AType t, muBlock([MuExp exp])){
    return muReturn1(t, exp);
}
    
MuExp muReturn1(AType t, muBlock([*MuExp exps, MuExp exp])){
    if(isEmpty(exps)) fail; else return muBlock([*exps, muReturn1(t, exp)]);
}    
MuExp muReturn1(AType t1, muValueBlock(AType t2, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muReturn1(t1, exp)]);
    
MuExp muReturn1(AType t, muAssign(MuExp var, MuExp exp))
    = muBlock([muAssign(var, exp), muReturn1(t, var)]);
    
MuExp muReturn1(AType t, muVarInit(MuExp var, MuExp exp)){
    return muBlock([muVarInit(var, exp), muReturn1(t, var)]);
}
    
MuExp muReturn1(AType t, muConInit(MuExp var, MuExp exp))
    = muBlock([muConInit(var, exp), muReturn1(t, var)]);

MuExp muReturn1(AType t, muIfEqualOrAssign(MuExp var, MuExp other, MuExp body))
    = muReturn1(t, muEqual(var, other));
    
MuExp muReturn1(AType t, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muIfelse(cond, muReturn1(t, thenPart), muReturn1(t, elsePart));
    
MuExp muReturn1(AType t, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muIfelse(cond,muReturn1(t, thenPart), muReturn1(t, elsePart));
    
MuExp muReturn1(AType t, muWhileDo(str label, MuExp cond, MuExp body)){
    return  muWhileDo(label, cond, muReturn1(t, body));
    }
    
MuExp muReturn1(AType t, muDoWhile(str label, MuExp body, MuExp cond)){
    return  muDoWhile(label, muReturn1(t, body), cond);
    }

MuExp muReturn1(AType t, muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp))
    = muForRangeInt(label, var, ifirst, istep, last, muReturn1(t, exp));
    
MuExp muReturn1(AType t, muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body))
    = muForAll(label, var, iterType, iterable, muReturn1(t, body));

MuExp addReturnFalse(bl: muBlock([*exps, muReturn1(abool(), muCon(false))])) = bl;

default MuExp addReturnFalse(MuExp exp) = muBlock([exp, muReturn1(abool(), muCon(false))]);

MuExp muReturn1(AType t, muEnter(str btscope, MuExp exp))
    = muEnter(btscope, addReturnFalse(visit(exp) { case muSucceed(btscope) => muReturn1(abool(), muCon(true))
                                                   //case muFail(btscope) => muReturn1(abool(), muCon(false))
                                                 }));
    
MuExp muReturn1(AType t, muSucceed(str btscope))
    = muReturn1(abool(), muCon(true));

MuExp muReturn1(AType t, muContinue(str btscope))
    =   muContinue(btscope);
     
MuExp muReturn1(AType t, muFail(str btscope)){
   return  muContinue(btscope);// : muReturn1(muCon(false));
}

MuExp muReturn1(AType t, muFailReturn(AType t))
    = muFailReturn(t);
    
MuExp muReturn1(AType t, muTry(MuExp exp, MuCatch \catch, MuExp \finally))
    = muTry(muReturn1(t, exp), \catch, \finally);
    
MuExp muReturn1(AType t, muThrow(MuExp exp, loc src))
    = muThrow(exp, src);
    
// ---- muThrow ---------------------------------------------------------------


    
// ---- muAssign --------------------------------------------------------------

MuExp muAssign(MuExp var1, MuExp var2)
    = muBlock([])
      when var2 has name, var1.name == var2.name,var2 has fuid, var1.fuid == var2.fuid, var2 has pos, var1.pos == var2.pos;

MuExp muAssign(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);

MuExp muAssign(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);

MuExp muAssign(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);
    
MuExp muAssign(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
    = muBlock([ muAssign(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);
    
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
    
MuExp muConInit(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muConInit(var, exp)]);

MuExp muVarInit(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muVarInit(var, exp)]);

// ---- muIfExp ---------------------------------------------------------------
MuExp muIfExp(MuExp cond, MuExp thenPart, muFailReturn(AType t))
    = muIfelse(cond, thenPart, muFailReturn(t));

// ---- muIfelse --------------------------------------------------------------

MuExp muIfelse(MuExp cond, MuExp thenPart, muBlock([])) = muIf(cond, thenPart);
MuExp muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart) = thenPart when thenPart == elsePart;
MuExp muIfelse(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart;
MuExp muIfelse(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

MuExp muIfelse(muValueBlock(AType t, [*MuExp exps, MuExp exp]), MuExp thenPart, MuExp elsePart)
    = muBlock([*exps, muIfelse(exp, thenPart, elsePart)]);

//MuExp muEnter(str btscope, muIfelse(str btscope2, MuExp cond, MuExp thenPart, MuExp elsePart))
//    =  muIfelse(btscope, cond, muEnter(btscope, thenPart), muEnter(btscope, elsePart));


MuExp muEnter(str label, muForAll(label, MuExp var, AType iterType, MuExp iterable, MuExp body))
    = muForAll(label, var, iterType, iterable, body);
    
MuExp muForAll(str label, str varName, str fuid, MuExp iterable, MuExp body)
    = muForAll(label, varName, fuid, iterable, body1)
    when body1 := visit(body) { case muSucceed(str btscope) => muSucceed(label) } && body1 != body;
 
MuExp muRegExpCompile(muValueBlock(AType t, [*MuExp exps, MuExp regExp]), MuExp subject)
    = muValueBlock(t, [ *exps, muRegExpCompile(regExp, subject)]);
          
//public bool isOverloadedFunction(muOFun(str _)) = true;
////public bool isOverloadedFunction(muOFun(str _, str _)) = true;
//public default bool isOverloadedFunction(MuExp _) = false;

// ============ Flattening Rules ================================================
// TODO: shoud go to separate module (does not work in interpreter)

bool shouldFlatten(MuExp arg) 
    =  muValueBlock(t, elems) := arg || muEnter(btscope, exp) := arg;
 
int nauxVars = -1;
         
// Rascal primitives
tuple[bool flattened, list[MuExp] auxVars, list[MuExp] pre, list[MuExp] post] flattenArgs(list[MuExp] args){
    if(!isEmpty(args) && any(arg <- args , shouldFlatten(arg))){
        pre = [];
        newArgs = [];
        auxVars = [];
        for(MuExp arg <- args){
            if(muValueBlock(t, elems) := arg){
                pre += elems[0..-1];
                newArgs += elems[-1];
            } else if(me: muEnter(btscope, exp) := arg){
                nauxVars += 1;
                aux = muTmpIValue("$aux<nauxVars>", "", abool());
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

MuExp muCall(MuExp fun, AType t, list[MuExp] args) 
    = muValueBlock(t, auxVars + pre + muCall(fun, t, flatArgs))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

AType getResultType(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = ret;
AType getResultType(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = adt;
AType getResultType(overloadedAType(rel[loc, IdRole, AType] overloads)) = lubList(toList(overloads<2>));
default AType getResultType(AType t) = t;
     
MuExp muOCall3(MuExp fun, AType atype, list[MuExp] args, loc src)
    = muValueBlock(getResultType(atype), auxVars + pre + muOCall3(fun, atype, flatArgs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args), bprintln(atype);

MuExp muCallPrim3(str op, AType result, list[AType] details, list[MuExp] args, loc src)
    = muValueBlock(result, auxVars + pre + muCallPrim3(op, result, details, flatArgs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

MuExp muCallJava(str name, str class, AType funType, int reflect, list[MuExp] args, str enclosingFun)
    = muValueBlock(funType.ret, auxVars + pre + muCallJava(name, class, funType, reflect, flatArgs, enclosingFun))
when <true, auxVars, pre, flatArgs> := flattenArgs(args);

MuExp muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)
    = muValueBlock(auxVars + pre + muKwpActuals([<kwpActuals[i].kwpName, flatArgs[i]> | int i <- index(kwpActuals)]))
    when <true, auxVars, pre, flatArgs> := flattenArgs(kwpActuals<1>);
      
MuExp muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)
    = muValueBlock(auxVars + pre + muKwpMap([<dflt.kwName, dflt.atype, flatArgs[i]> | int i <- index(defaults), dflt := defaults[i]]))
    when <true, auxVars, pre, flatArgs> := flattenArgs(defaults<2>);  
    
//muHasKwpWithValue?

MuExp muInsert(AType t, MuExp arg)
    = muValueBlock(t, auxVars + pre + muInsert(t, flatArgs[0]))
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
//muGetField(str kind, AType consType, MuExp exp, str fieldName)
//muKwpGetField(str kind, AType consType, MuExp exp, str fieldName)
//muSetField(str kind, AType atype, MuExp exp1, str fieldName, MuExp exp2)
  

MuExp muValueIsSubType(MuExp exp, AType tp) = muCon(true) when !isVarOrTmp(exp) && exp has atype && exp.atype == tp;

//============== constant folding rules =======================================
// TODO:
// - These rules should go to a separate module (but the interpreter does not like this)
// - Introduce a library function applyPrim(str name, list[value] args) to simplify these rules and cover more cases

bool allConstant(list[MuExp] args) { b = isEmpty(args) || all(a <- args, muCon(_) := a); /*println("allConstant: <args> : <b>"); */return b; }

// Integer addition

MuExp muCallPrim3("add", aint(), [aint(), aint()], [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 + n2);

MuExp muCallPrim3("add", aint(), [aint(), aint()], [muCallPrim3("add", aint(), [aint(), aint()], [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("add", aint(), [aint(), aint()], [e, muCon(n1 + n2)], src2);

MuExp muCallPrim3("add", aint(), [aint(), aint()], [muCon(int n1), muCallPrim3("add", aint(), [aint(), aint()], [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("add", aint(), [aint(), aint()], [muCon(n1 + n2), e], src2);

// Integer subtraction
 
MuExp muCallPrim3("subtract", aint(), [aint(), aint()], [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 - n2);

MuExp muCallPrim3("subtract", aint(), [aint(), aint()], [muCallPrim3("subtract", aint(), [aint(), aint()], [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("subtract", aint(), [aint(), aint()], [e, muCon(n1 - n2)], src2);

MuExp muCallPrim3("subtract", aint(), [aint(), aint()], [muCon(int n1), muCallPrim3("subtract", aint(), [aint(), aint()], [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("subtract", aint(), [aint(), aint()], [muCon(n1 - n2), e], src2);      

// Integer multiplication

MuExp muCallPrim3("product", aint(), [aint(), aint()], [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 * n2);

MuExp muCallPrim3("product",aint(), [aint(), aint()], [muCallPrim3("product", aint(), [aint(), aint()], [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("product", aint(), [aint(), aint()], [e, muCon(n1 * n2)], src2);

MuExp muCallPrim3("product", aint(), [aint(), aint()], [muCon(int n1), muCallPrim3("product", aint(), [aint(), aint()], [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("product",aint(),  [aint(), aint()], [muCon(n1 * n2), e], src2);

// String concatenation

MuExp muCallPrim3("add", astr(), [astr(), astr()], [muCon(str s1), muCon(str s2)], loc src) = muCon(s1 + s2);

MuExp muCallPrim3("add",astr(), [astr(), astr()], [muCallPrim3("add", astr(), [astr(), astr()], [MuExp e, muCon(str s1)], loc src1), muCon(str s2)], loc src2) =
      muCallPrim3("add", astr(),[astr(), astr()], [e, muCon(s1 + s2)], src2);

MuExp muCallPrim3("add", astr(), [astr(), astr()], [muCon(str s1), muCallPrim3("add", astr(), [astr(), astr()], [muCon(str s2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("add", astr(), [astr(), astr()], [muCon(s1 + s2), e], src2);

// Create composite datatypes

MuExp muCallPrim3("create_list", AType r, [AType lst, AType elm], list[MuExp] args, loc src) = muCon([a | muCon(a) <- args]) 
      when allConstant(args);

MuExp muCallPrim3("create_set", AType r, [AType s, AType e], list[MuExp] args, loc src) = muCon({a | muCon(a) <- args}) 
      when allConstant(args);
 
// TODO: do not generate constant in case of multiple keys     
MuExp muCallPrim3("create_map", AType r, [AType k, AType v], list[MuExp] args, loc src) = muCon((args[i].c : args[i+1].c | int i <- [0, 2 .. size(args)]))
      when allConstant(args);
 
 // TODO chanto type args     
MuExp muCallPrim3("create", ["atuple"], [muCon(v1)], loc src) = muCon(<v1>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2)], loc src) = muCon(<v1, v2>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3)], loc src) = muCon(<v1, v2, v3>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4)], loc src) = muCon(<v1, v2, v3, v4>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5)], loc src) = muCon(<v1, v2, v3, v4, v5>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6)], loc src) = muCon(<v1, v2, v3, v4, v5, v6>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8), muCon(v9) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8, v9>);
MuExp muCallPrim3("create", ["atuple"], [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5), muCon(v6), muCon(v7), muCon(v8), muCon(v9),  muCon(v10) ], loc src) = muCon(<v1, v2, v3, v4, v5, v6, v7, v8, v9, v10>);

//MuExp muCallPrim3("anode_create", [muCon(str name), *MuExp args, muCallMuPrim("make_mmap", [])], loc src) = muCon(makeNode(name, [a | muCon(a) <- args]))  
//      when allConstant(args);
