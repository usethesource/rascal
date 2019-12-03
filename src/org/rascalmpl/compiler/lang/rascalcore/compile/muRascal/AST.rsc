module lang::rascalcore::compile::muRascal::AST

import Message;
import List;
import Set;
import String;
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
                       lrel[str name, AType funType, str scope, list[loc] ofunctions, list[loc] oconstructors] overloaded_functions,
                       map[AType, map[str,AType]] commonKeywordFields,
                       AGrammar grammar,
                       rel[str,str] importGraph,
                       loc src)
            ;
            
MuModule errorMuModule(str name, set[Message] messages, loc src) = muModule(name, (), messages, [], [], {}, {}, [], [], [], [], (), grammar({}, ()), {}, src);
          
// All information related to a function declaration. This can be a top-level
// function, or a nested or anomyous function inside a top level function. 
         
public data MuFunction =					
                muFunction(str name, 
                           str uniqueName,
                           AType ftype,
                           list[MuExp] formals,
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
          //| muConstrCompanion(str fuid)                         // Companion function for constructor with keyword parameters
          
          	// Variables and temporaries
          | muResetLocs(list[int] positions)					// Reset value of selected local variables to undefined (null)
          | muVar(str name, str fuid, int pos, AType atype)		// Variable: retrieve its value
          | muTmpIValue(str name, str fuid, AType atype)	    // Temporary variable introduced by compiler
          | muTmpNative(str name, str fuid, NativeKind nkind)   // Temporary variable introduced by compiler
             
          | muVarKwp(str name, str fuid, AType atype)           // Keyword parameter
          
          // Call and return    		
          | muCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs)     // Call a function
          
          | muOCall3(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs, loc src)       
                                                                // Call an overloaded declared *Rascal function 
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
          
          | muCheckMemo(AType funType, list[MuExp] args, MuExp body)
          | muMemoReturn0(AType funtype, list[MuExp] args)
          | muMemoReturn1(AType funtype, list[MuExp] args, MuExp functionResult)
               
          // Keyword parameters of functions             
          | muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)
                                                                // Build map of actual keyword parameters
          | muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)  
          
          | muIsKwpDefined(MuExp var, str kwpName)
          
          | muGetKwFieldFromConstructor(AType resultType, MuExp var, str fieldName)
          | muGetFieldFromConstructor(AType resultType, AType consType, MuExp var, str fieldName)
         
          | muGetKwp(MuExp var, AType atype, str kwpName)
          | muHasKwp(MuExp var, str kwpName)
          
          | muInsert(MuExp exp, AType atype)				    // Insert statement
              
          // Get and assign values
          
          | muAssign(MuExp var, MuExp exp)                      // Assign a value to a variable
          | muVarInit(MuExp var, MuExp exp)                     // Introduce variable and assign a value to it
          | muConInit(MuExp var, MuExp exp)                     // Create a constant
          | muVarDecl(MuExp var)                                 // Introduce a variable
          
          | muGetAnno(MuExp exp, AType resultType, str annoName)
          | muGuardedGetAnno(MuExp exp, AType resultType, str annoName)
          | muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
          
          // Fields of data constructors
          | muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGuardedGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGetKwField(AType resultType, AType consType, MuExp exp, str fieldName)

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
          | muFailEnd(str label)                              // Fail statement at end of backtracking scope
          
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
          | muEqual(MuExp exp1, MuExp exp2)     
          
          | muHasTypeAndArity(AType atype, int arity, MuExp exp)
          | muHasNameAndArity(AType atype, AType consType, MuExp nameExp, int arity, MuExp exp)
          | muValueIsSubType(MuExp exp, AType tp)
          | muValueIsSubTypeOfValue(MuExp exp2, MuExp exp1)
          | muIsDefinedValue(MuExp exp)
          | muGetDefinedValue(MuExp exp, AType tp)
          | muHasField(MuExp exp, AType tp, str fieldName)
          | muIsInitialized(MuExp exp)
          
          | muSubscript(MuExp exp, MuExp idx)
          
          // Operations on native integers
          | muIncNativeInt(MuExp var, MuExp inc)
          | muSubNativeInt(MuExp exp1, MuExp exp2)
          | muAddNativeInt(MuExp exp1, MuExp exp2)
          | muSize(MuExp exp, AType atype)
          | muEqualNativeInt(MuExp exp1, MuExp exp2)
          | muLessNativeInt(MuExp exp1, MuExp exp2)
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
    = descendantDescriptor(bool useConcreteFingerprint, set[AType] reachable_atypes, set[AType] reachable_aprods, map[AType,AProduction] definitions)
    ;
    
data MuCatch = muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body);    

data MuCase = muCase(int fingerprint, MuExp exp);

// ==== Utilities =============================================================

bool isClosureName(str name)
    = findFirst(name, "$CLOSURE") >= 0;

bool isMainName(str name)
    = startsWith(name, "main");

bool isOuterScopeName(str name)
    = isEmpty(name);
    
str getFunctionName(MuFunction fun){
    if(isOuterScopeName(fun.scopeIn)) return fun.name;
    if(isClosureName(fun.name)) return "<fun.scopeIn>_<fun.uniqueName>";
    return "<fun.scopeIn>_<fun.uniqueName>";
}

str getUniqueFunctionName(MuFunction fun){
    if(isOuterScopeName(fun.scopeIn)){
        return isMainName(fun.name) ? fun.name :  fun.uniqueName;
    }
    return "<fun.scopeIn>_<fun.uniqueName>";
}

// Normalize expression to statement

MuExp toStat(muIfExp(c, t, f)) = muIfelse(c, toStat(t), toStat(f));
default MuExp toStat(MuExp exp) = exp;

// Identity on expressions

MuExp identity(MuExp exp) = exp;

set[str] varExp = {"muModuleVar", "muVar", "muTmpIValue", "muTmpNative"};

bool isVarOrTmp(MuExp exp)
    = getName(exp) in varExp;

// Produces NativeBool
   
bool producesNativeBool(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] args, loc src)){
    if(name in {/*"equal",*/ "notequal", "is", "subset"}) return true;
    fail producesNativeBool;
}

bool producesNativeBool(muTmpNative(_,_,nativeBool()))
    = true;
    
default bool producesNativeBool(MuExp exp)
    = getName(exp) in {"muEqual", "muEqualNativeInt", "muNotNegativeNativeInt", "muIsKwpDefined", "muHasKwp", "muHasKwpWithValue", /*"muHasType",*/ "muHasTypeAndArity",
                  "muHasNameAndArity", "muValueIsSubType", "muValueIsSubTypeOfValue", "muLessNativeInt", "muGreaterEqNativeInt", "muAndNativeBool", "muNotNativeBool",
                  "muRegExpFind",  "muIsDefinedValue", "muIsInitialized", "muHasField"};

// Produces NativeInt

bool producesNativeInt(muTmpNative(_,_,nativeInt()))
    = true;
                     
default bool producesNativeInt(MuExp exp)
    = getName(exp) in {"muSize", "muAddNativeInt", "muSubNativeInt", "muRegExpBegin", "muRegExpEnd"};

// Produces NativeGuardedIValue

bool producesNativeGuardedIValue(muTmpNative(_,_,nativeGuardedIValue))
    = true;   
 
 bool producesNativeGuardedIValue(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] exps, loc src))
    = name in { "guarded_subscript", "guarded_field_project"};
    
default bool producesNativeGuardedIValue(MuExp exp)
   = getName(exp) in {"muGuardedGetAnno", "muGuardedGetField"};
    
// Get the result type of a MuExp
AType getType(muVar(str name, str fuid, int pos, AType atype)) = atype;
AType getType(muTmpIValue(str name, str fuid, AType atype)) = atype;
AType getType(muVarKwp(str name, str fuid, AType atype)) = atype;
AType getType(muCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs)) = getResultType(atype);   
AType getType(muOCall3(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs, loc src))
    = getResultType(atype);                                                               
AType getType(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] exps, loc src)) 
    = result;
AType getType(muCallJava(str name, str class, AType funType, int reflect, list[MuExp] args, str enclosingFun)) =  getResultType(funType); 
AType getType(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)) = alub(getType(thenPart), getType(elsePart));

default AType getType(MuExp exp) = avalue();
         
// ==== Simplification rules ==================================================

// ---- leaveWithReturn --------------------------------------------------------

bool leaveWithReturn(MuExp exp) = leaveWithReturn(exp, leaveWithReturn);

bool leaveWithReturn(muReturn0(), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muReturn1(_, _), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muFailReturn(_), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muCheckMemo(_,_,_), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muMemoReturn0(_,_), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muMemoReturn1(_,_,_), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muThrow(_,_), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muBlock([*exps1, exp2]), bool(MuExp) leave) 
    = leave(exp2, leave);
bool leaveWithReturn(muValueBlock(AType t, [*exps1, exp2]), bool(MuExp) leave) 
    = leave(exp2, leave);
bool leaveWithReturn(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart), bool(MuExp) leave)
    = leave(thenPart, leave) && leave(elsePart, leave);
bool leaveWithReturn( muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint), bool(MuExp) leave) 
    = all(c <- cases, leave(c.exp, leave)) && leave(defaultExp, leave);
bool leaveWithReturn(muEnter(str label, MuExp body), bool(MuExp) leave) 
    = leave(body, leave);
//bool leaveWithReturn(muFail(str label), bool(MuExp) leave) = true; // <==
bool leaveWithReturn(muSucceed(str label), bool(MuExp) leave) 
    = true;
bool leaveWithReturn(muTry(MuExp exp, MuCatch \catch, MuExp \finally), bool(MuExp) leave) 
    = leave(exp, leave) && leave(\catch, leave);
bool leaveWithReturn(muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body), bool(MuExp) leave) 
    = leave(body, leave);
default bool leaveWithReturn(MuExp exp, bool(MuExp) leave) 
    = false;

bool leaveFlow(MuExp exp) 
    = leaveFlow(exp, leaveFlow);
    
bool leaveFlow(muFail(str label), bool(MuExp) leave) 
    = true;    
bool leaveFlow(muEnter(str label, MuExp body), bool(MuExp) leave) 
    = false;
default bool leaveFlow(MuExp exp, bool(MuExp) leave) 
    = leaveWithReturn(exp, leaveFlow);

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
    if(!isEmpty(post) && leaveFlow(exp)) return muBlock([*pre, exp]);
    fail;
}

MuExp muBlock([*MuExp pre, MuExp exp, muFailReturn(AType t), *MuExp post]){
    if(leaveFlow(exp)) return muBlock([*pre, exp]);
    fail;
}

MuExp muBlock([*MuExp pre, MuExp exp, muFailCase(), *MuExp post]){
    if(leaveFlow(exp)) return muBlock([*pre, exp]);
    fail;
}

MuExp muBlock([*MuExp pre, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), *MuExp post])
    = muBlock([*pre, muIfelse(cond, thenPart, elsePart), *post]);
    
// ---- muValueBlock ----------------------------------------------------------
    
MuExp muValueBlock(AType t, [*MuExp pre, muBlock(list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t, [*pre, *elems, *post, last]);
    
MuExp muValueBlock(AType t1, [*MuExp pre, muValueBlock(AType t2, list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t2, [*pre, *elems, *post, last]);
    
MuExp muValueBlock(AType t, [*MuExp pre, MuExp exp, *MuExp post]){
    if(!isEmpty(post) && leaveFlow(exp)) return muValueBlock(t, [*pre, exp]);
    fail;
} 

MuExp muValueBlock(AType t1, [*MuExp pre, muReturn1(AType t2, MuExp exp)])
    = muBlock([*pre, muReturn1(t2, exp)]);
    
 //MuExp muValueBlock(AType t, [*MuExp pre, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)])
 //   =  pre == [] ?  muIfExp(cond, thenPart, elsePart)  : muValueBlock(t, [*pre, muIfExp(cond, thenPart, elsePart)]);
    
// ---- muReturn1 -------------------------------------------------------------

MuExp muReturn1(AType t1, muReturn1(AType t2, MuExp exp)) = muReturn1(t2, exp);

MuExp muReturn1(AType t1, muCheckMemo(AType funtype, list[MuExp] args, MuExp body))
    =  muCheckMemo(funtype, args, body);
    
MuExp muReturn1(AType t1, muMemoReturn1(AType funtype, list[MuExp] args, MuExp functionResult))
    =  muMemoReturn1(funtype, args, functionResult);

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

MuExp muReturn1(AType t, muIf(MuExp cond, MuExp thenPart)){
    return  muIf(cond, muReturn1(t, thenPart));
}
    
MuExp muReturn1(AType t, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muIfelse(cond,muReturn1(t, thenPart), muReturn1(t, elsePart));
    
//MuExp muReturn1(AType t, muWhileDo(str label, MuExp cond, MuExp body)){
//    return  muWhileDo(label, cond, muReturn1(t, body));
//    }
    
//MuExp muReturn1(AType t, muDoWhile(str label, MuExp body, MuExp cond)){
//    return  muDoWhile(label, muReturn1(t, body), cond);
//    }

MuExp muReturn1(AType t, muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp))
    = muForRangeInt(label, var, ifirst, istep, last, muReturn1(t, exp));
    
MuExp muReturn1(AType t, fo: muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body))
    = addReturnFalse(t, fo);
 
MuExp addReturnFalse(AType t, bl: muBlock([*exps, muReturn1(abool(), muCon(false))])) = bl;

default MuExp addReturnFalse(AType t, MuExp exp) = muBlock([exp, (t == abool() || t == avalue()) ? muReturn1(abool(), muCon(false)) : muFailReturn(t)]);

MuExp muReturn1(AType t, muEnter(str btscope, MuExp exp)){
    boolOrValue = t == abool() || t == avalue();
    println("getType: <getType(exp)>");
    exp2 = muEnter(btscope, visit(exp) { 
                                    case muSucceed(btscope) => muReturn1(abool(), muCon(true))
                                    case muFail(btscope) => boolOrValue ? muReturn1(abool(), muCon(false)) : muFailReturn(ft)
                                    case muFailEnd(btscope) => boolOrValue ? muReturn1(abool(), muCon(false)) : muFailReturn(ft)
            });
    iprintln(exp2);
    return leaveWithReturn(exp2) ? exp2 : addReturnFalse(t, exp2);
} 
   
MuExp muReturn1(AType t, muSucceed(str btscope))
    = muReturn1(abool(), muCon(true));

MuExp muReturn1(AType t, muContinue(str btscope))
    =   muContinue(btscope);
     
MuExp muReturn1(AType t, muFail(str btscope)){
   return muFailReturn(t);
}

MuExp muReturn1(AType t, muFailCase())
    = muFailCase();

MuExp muReturn1(AType t, muFailEnd(str btscope)){
   return muFailReturn(t);
}

MuExp muReturn1(AType t, muFailReturn(AType t))
    = muFailReturn(t);
    
MuExp muReturn1(AType t, muTry(MuExp exp, MuCatch \catch, MuExp \finally))
    = muTry(muReturn1(t, exp), \catch, \finally);
    
MuExp muReturn1(AType t, muThrow(MuExp exp, loc src))
    = muThrow(exp, src);
    
MuExp muReturn1(AType t, sw: muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint))
    = any(c <- cases, !leaveFlow(c.exp)) || !leaveFlow(defaultExp)
      ? muBlock([muSwitch(exp, [muCase(c.fingerprint, muReturn1(t, c.exp)) | c <- cases], muReturn1(t, defaultExp), useConcreteFingerprint)
                //, muFailReturn(t)
                ])
      : sw;
    
// ---- muFailReturn ----------------------------------------------------------

MuExp muFailReturn(AType t) 
    = muReturn1(abool(), muCon(false))
    when t == abool();
    
// ---- muThrow ---------------------------------------------------------------


    
// ---- muConInit/muVarInit/muConInit -----------------------------------------

MuExp muAssign(MuExp var1, MuExp var2)
    = muBlock([])
      when var2 has name, var1.name == var2.name,var2 has fuid, var1.fuid == var2.fuid, var2 has pos, var1.pos == var2.pos;

// ----

MuExp muConInit(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muConInit(var, exp)]);

MuExp muVarInit(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muVarInit(var, exp)]);
    
MuExp muAssign(MuExp var, muBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);
    
// ---- 
 
MuExp muConInit(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muConInit(var, exp)]);
    
MuExp muVarInit(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muVarInit(var, exp)]);
    
MuExp muAssign(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);

// ----

MuExp muConInit(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);
    
MuExp muVarInit(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);

MuExp muAssign(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);

// ----

MuExp muConInit(MuExp var, muWhileDo(str label, MuExp cond, MuExp body))
    = muBlock([muWhileDo(label, cond, body), muConInit(var, muCon([]))]);
    
MuExp muVarInit(MuExp var, muWhileDo(str label, MuExp cond, MuExp body))
    = muBlock([muWhileDo(label, cond, body), muVarInit(var, muCon([]))]);

MuExp muAssign(MuExp var, muWhileDo(str label, MuExp cond, MuExp body))
    = muBlock([muWhileDo(label, cond, body), muAssign(var, muCon([]))]);
    
// ---- 

MuExp muConInit(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
    = muBlock([ muVarInit(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);

MuExp muVarInit(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
    = muBlock([ muVarInit(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);   

MuExp muAssign(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
    = muBlock([ muAssign(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);
    
// ----    

 MuExp muConInit(var, muEnter(str btscope, MuExp exp))
    = muBlock([muVarInit(var, muCon(false)), muEnter(btscope, visit(exp) { case muSucceed(btscope) => muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]) })]);

MuExp muVarInit(var, muEnter(str btscope, MuExp exp))
    = muBlock([muVarInit(var, muCon(false)), muEnter(btscope, visit(exp) { case muSucceed(btscope) => muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]) })]);     
     
MuExp muAssign(var, muEnter(str btscope, MuExp exp))
    = muBlock([muAssign(var, muCon(false)), muEnter(btscope, visit(exp) { case muSucceed(btscope) => muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]) })]);

// ----

MuExp muConInit(MuExp var, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muBlock([muVarDecl(var), muIfExp(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);

MuExp muVarInit(MuExp var, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muBlock([muVarDecl(var), muIfExp(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);   

// ---- 

MuExp muConInit(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muBlock([muVarDecl(var), muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);
    
MuExp muVarInit(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muBlock([muVarDecl(var), muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);
    
MuExp muAssign(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart));   
         
// ----
MuExp muAssign(MuExp var1, muIfEqualOrAssign(MuExp var2, MuExp other, MuExp body))
    = muIfEqualOrAssign(var2, other, muBlock([muAssign(var1, other), body]));

// ----

MuExp muConInit(MuExp var, muSucceed(str btscope))
    = muBlock([muConInit(var, muCon(true)), muSucceed(btscope)]);

MuExp muVarInit(MuExp var, muSucceed(str btscope))
    = muBlock([muVarInit(var, muCon(true)), muSucceed(btscope)]);   
    
MuExp muAssign(MuExp var, muSucceed(str btscope))
    = muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]);

// ----

MuExp muConInt(MuExp var, muFail(btscope))
    = muBlock([muConInit(var, muCon(false))]);
MuExp muConInt(MuExp var, muFailEnd(btscope))
    = muBlock([muConInit(var, muCon(false))]);
    
MuExp muVarInt(MuExp var, muFail(btscope))
    = muBlock([muVarInit(var, muCon(false))]);   
MuExp muVarInt(MuExp var, muFailEnd(btscope))
    = muBlock([muVarInit(var, muCon(false))]); 
     
MuExp muAssign(MuExp var, muFail(btscope))
    = muBlock([muAssign(var, muCon(false))]);
MuExp muAssign(MuExp var, muFailEnd(btscope))
    = muBlock([muAssign(var, muCon(false))]);
    
// ---- muIfthen ---------------------------------------------------------------
    
MuExp muIfthen(muCon(true), MuExp thenPart) = thenPart;

MuExp muIfthen(muCon(false), MuExp thenPart) = muBlock([]);

// ---- muIfExp ---------------------------------------------------------------

MuExp muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)
    = muIfelse(cond, thenPart, elsePart)
    when leaveFlow(thenPart) || leaveFlow(elsePart) || muBlock(_) := thenPart || muBlock(_) := elsePart;

MuExp muIfExp(MuExp cond, MuExp thenPart, muFailReturn(AType t))
    = muIfelse(cond, thenPart, muFailReturn(t));
    
MuExp muIfExp(muValueBlock(AType t, [*MuExp exps, MuExp exp]), MuExp thenPart, MuExp elsePart)
    = muValueBlock(t, [*exps, muIfExp(exp, thenPart, elsePart)]);
    
//MuExp muIfExp(MuExp cond, muValueBlock([*MuExp exps, MuExp exp]), MuExp elsePart)
//    = muValueBlock(t, cond, muValueBlock([*exps, muIfExp(cond, exp, elsePart), elsePart);
//    
//MuExp muIfExp(MuExp cond, MuExp thenPart, muValueBlock([*MuExp exps, MuExp exp]))
//    = muValueBlock(t, cond, thenPart, muValueBlock([*exps, muIfExp(cond, thenPart, exp), elsePart));
    
    
MuExp muIfExp(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart;

MuExp muIfExp(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

MuExp muIfExp(muIfExp(MuExp cond, MuExp thenPart1, MuExp elsePart1), MuExp thenPart2, MuExp elsePart2)
    = muIfExp(cond, muIfExp(thenPart1, thenPart2, elsePart2), muIfExp(elsePart1, thenPart2, elsePart2));

// ---- muIfelse --------------------------------------------------------------

MuExp muIfelse(MuExp cond, MuExp thenPart, muBlock([])) = muIf(cond, thenPart);
MuExp muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart) = thenPart when thenPart == elsePart;
MuExp muIfelse(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart;
MuExp muIfelse(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

MuExp muIfelse(MuExp cond, muCon(_), muCon(_)) = muBlock([]);

MuExp muIfelse(MuExp cond, muIfExp(cond1, thenPart1, elsePart1), MuExp elsePart) 
    = muIfelse(cond, muIfelse(cond1, thenPart1, elsePart1), elsePart);
    
MuExp muIfelse(MuExp cond, MuExp thenPart, muIfExp(cond1, thenPart1, elsePart1)) 
    = muIfelse(cond, thenPart, muIfelse(cond1, thenPart1, elsePart1));
    
MuExp muIfelse(muValueBlock(AType t, [*MuExp exps, MuExp exp]), MuExp thenPart, MuExp elsePart)
    = muBlock([*exps, muIfelse(exp, thenPart, elsePart)]); 

//MuExp muIfelse(muEnter(str btscope, MuExp exp), MuExp thenPart, MuExp elsePart)
//    = muIfelse(muEnter(btscope, exp1), thenPart, elsePart)
//    when exp1 := visit(exp) { case muSucceed(btscope) => muBlock(
//                              case muFail(btscope) => muCon(false)
//                            },
//         exp1 != exp;
    
//MuExp muReturn1(AType t, muEnter(str btscope, MuExp exp))
//    = muEnter(btscope, addReturnFalse(visit(exp) { case muSucceed(btscope) => muReturn1(abool(), muCon(true))
//                                                   case muFail(btscope) => muReturn1(abool(), muCon(false))
//                                                   case muFailEnd(btscope) => muReturn1(abool(), muCon(false))
//                                                 }));

// ---- muEnter ---------------------------------------------------------------

MuExp muEnter(str btscope, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muEnter(btscope, muIfelse(cond, thenPart, elsePart));


MuExp muEnter(str label, muForAll(label, MuExp var, AType iterType, MuExp iterable, MuExp body))
    = muForAll(label, var, iterType, iterable, body);
    
// ---- muForAll --------------------------------------------------------------

MuExp muForAll(str label, MuExp var, AType iterType, muValueBlock(AType t, [*MuExp exps, MuExp iterable]), MuExp body)
    =  muValueBlock(iterType, [*exps, muForAll(label, var, iterType, iterable, body)]);
       
//MuExp muForAll(str label, str varName, str fuid, MuExp iterable, MuExp body)
//    = muForAll(label, varName, fuid, iterable, body1)
//    when body1 := visit(body) { case muSucceed(str btscope) => muSucceed(label) } && body1 != body;

// ---- muRegExpCompile -------------------------------------------------------
 
MuExp muRegExpCompile(muValueBlock(AType t, [*MuExp exps, MuExp regExp]), MuExp subject)
    = muValueBlock(t, [ *exps, muRegExpCompile(regExp, subject)]);
          
//public bool isOverloadedFunction(muOFun(str _)) = true;
////public bool isOverloadedFunction(muOFun(str _, str _)) = true;
//public default bool isOverloadedFunction(MuExp _) = false;

// ============ Flattening Rules ================================================
// TODO: shoud go to separate module (does not work in interpreter)

bool shouldFlatten(MuExp arg) 
    =     muValueBlock(t, elems) := arg 
       || muAssign(MuExp var, MuExp exp) := arg
       || muVarInit(MuExp var, MuExp exp) := arg
       || muConInit(MuExp var, MuExp exp) := arg
       || muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl) := arg
       || muSetField(AType resultType, AType baseType, MuExp baseExp, value fieldIdentity, MuExp repl) := arg
       || muEnter(btscope, exp) := arg 
       || muIfelse(cond, thenPart, elsePart) := arg 
       || muWhileDo(str ab, MuExp cond, MuExp body) := arg
       || muBlock(elems) := arg 
       //|| muIfExp(cond, thenPart, elsePart) := arg && (shouldFlatten(thenPart) || shouldFlatten(elsePart))
       ;
 
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
                lst = elems[-1];
                <flLst, auxLst, preLst, postLst> = flattenArgs([lst]);
                auxVars += auxLst;
                pre += preLst;
                newArgs += postLst;
            } else if(muBlock(elems) := arg){
                if(!isEmpty(elems)){
                    pre += elems[0..-1];
                    lst = elems[-1];
                    <flLst, auxLst, preLst, postLst> = flattenArgs([lst]);
                    auxVars += auxLst;
                    pre += preLst;
                    newArgs += postLst;
                 }
            } else if(muAssign(MuExp var, MuExp exp) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([exp]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muAssign(var, size(post1) == 1? pos1[0] : muValueBlock(avalue(), post1));
            } else if(muVarInit(MuExp var, MuExp exp) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([exp]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muVarInit(var, size(post1) == 1? pos1[0] : muValueBlock(avalue(), post1));
            } else if(muConInit(MuExp var, MuExp exp) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([exp]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muConInit(var, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([repl]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muSetAnno(exp, resultType, annoName, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(muSetField(AType resultType, AType baseType, MuExp baseExp, value fieldIdentity, MuExp repl) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([repl]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muSetField(resultType, baseType, baseExp, fieldIdentity, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(me: muEnter(btscope, exp) := arg){
                nauxVars += 1;
                aux = muTmpIValue("$aux<nauxVars>", "", abool());
                auxVars += muVarInit(aux, muCon(false));
                pre += muAssign(aux, me);
                newArgs += aux;
            } else if(muIfelse(cond, thenPart, elsePart) := arg){
                <flCond, auxCond, preCond, postCond> = flattenArgs([thenPart]);
                <flThen, auxThen, preThen, postThen> = flattenArgs([thenPart]);
                <flElse, auxElse, preElse, postElse> = flattenArgs([elsePart]);
                if(preCond != preThen && preCond != preElse) pre += preCond;
                pre += preThen + preElse;
                //pre += preCond + preThen + preElse;
                newArgs += muIfExp(size(postCond) == 1 ? postCond[0] : muValueBlock(avalue(), postCond), 
                                   size(postThen) == 1 ? postThen[0] : muValueBlock(avalue(), postThen), 
                                   size(postElse) == 1 ? postElse[0] : muValueBlock(avalue(), postElse));
            } else if(me: muWhileDo(str ab, MuExp cond, MuExp body) := arg){
                nauxVars += 1;
                aux = muTmpIValue("$aux<nauxVars>", "", alist(avalue()));
                auxVars += muVarInit(aux, muCon([]));
                pre += muAssign(aux, me);
                newArgs += aux;
               
            //} else if(muIfExp(cond, thenPart, elsePart) := arg){
            //    <flThen, auxThen, preThen, postThen> = flattenArgs([thenPart]);
            //    <flElse, auxElse, preElse, postElse> = flattenArgs([elsePart]);
            //    pre += preThen + preElse;
            //    newArgs += muIfExp(cond, size(postThen) == 1 ? postThen[0] : muValueBlock(avalue(), postThen), 
            //                             size(postElse) == 1 ? postElse[0] : muValueBlock(avalue(), postElse));
            } else {
                newArgs += arg;
            }
        }
        return <true, auxVars, pre, newArgs>;
    } else {
       return <false, [], [], args>;
    }
}

MuExp ifElse2ifExp(muIfelse(cond, thenPart, elsePart)) = muIfExp(cond, thenPart, elsePart);
default MuExp ifElse2ifExp(MuExp e) = e;
 
MuExp muCall(MuExp fun, AType t, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs) 
    = muValueBlock(t, auxVars + pre + muCall(fun, t, flatArgs, kwargs))
when <true, auxVars, pre, flatArgs> := flattenArgs(args) && !isEmpty(pre);

AType getResultType(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = ret;
AType getResultType(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = adt;
AType getResultType(overloadedAType(rel[loc, IdRole, AType] overloads)) = lubList(toList(overloads<2>));
default AType getResultType(AType t) = t;
     
MuExp muOCall3(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs, loc src)
    = muValueBlock(getResultType(atype), auxVars + pre + muOCall3(fun, atype, flatArgs, kwargs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args), bprintln(atype) && !isEmpty(pre);

MuExp muCallPrim3(str op, AType result, list[AType] details, list[MuExp] args, loc src)
    = muValueBlock(result, auxVars + pre + muCallPrim3(op, result, details, flatArgs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args) && !isEmpty(pre);

MuExp muCallJava(str name, str class, AType funType, int reflect, list[MuExp] args, str enclosingFun)
    = muValueBlock(funType.ret, auxVars + pre + muCallJava(name, class, funType, reflect, flatArgs, enclosingFun))
when <true, auxVars, pre, flatArgs> := flattenArgs(args) && !isEmpty(pre);

MuExp muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)
    = muValueBlock(auxVars + pre + muKwpActuals([<kwpActuals[i].kwpName, flatArgs[i]> | int i <- index(kwpActuals)]))
    when <true, auxVars, pre, flatArgs> := flattenArgs(kwpActuals<1>) && !isEmpty(pre);
      
MuExp muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)
    = muValueBlock(auxVars + pre + muKwpMap([<dflt.kwName, dflt.atype, flatArgs[i]> | int i <- index(defaults), dflt := defaults[i]]))
    when <true, auxVars, pre, flatArgs> := flattenArgs(defaults<2>) && !isEmpty(pre);  
    
//muHasKwpWithValue?

MuExp muAssign(MuExp var, MuExp exp)
    = muValueBlock(getType(exp), auxVars + pre + muAssign(var, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);

MuExp muVarInit(MuExp var, MuExp exp)
    = muValueBlock(getType(exp), auxVars + pre + muVarInit(var, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);   
    
MuExp muConInit(MuExp var, MuExp exp)
    = muValueBlock(getType(exp), auxVars + pre + muConInit(var, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);
       
MuExp muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
    = muValueBlock(resultType, auxVars + pre + muSetAnno(exp, resultType, annoName, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([repl]) && !isEmpty(pre);      

MuExp muInsert(AType t, MuExp arg)
    = muValueBlock(t, auxVars + pre + muInsert(t, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([arg]);
    
// TODO: defaultExp?
MuExp muVisit(MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
   = muValueBlock(auxVars + pre + muVisit(flatArgs[0], cases, defaultExp, vdescriptor))
    when <true, auxVars, pre, flatArgs> := flattenArgs([subject]) && !isEmpty(pre);

//muSwitch
MuExp muSwitch(MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)        // switch over cases for specific value
    = muValueBlock(auxVars + pre + muSwitch(flatArgs[0], cases, defaultExp, useConcreteFingerprint))
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);

//muThrow

MuExp muThrow(muValueBlock(AType t, list[MuExp] exps), loc src)
    = muValueBlock(t, exps[0..-1] + muThrow(exps[-1], src));


//muTry

MuExp muGetField(AType resultType, AType baseType, muValueBlock(AType t, [*MuExp pre, MuExp last]), str fieldName)
    = muValueBlock(resultType, [*pre, muGetField(resultType, baseType, last, fieldName)]);


MuExp muGetKwField(AType resultType, AType baseType, muValueBlock(AType t, [*MuExp pre, MuExp last]), str fieldName)
   = muValueBlock(resultType, [*pre, muGetKwField(resultType, baseType, last, fieldName)]);

MuExp muSetField(AType resultType, AType baseType, muValueBlock(AType t, [*MuExp pre, MuExp last]), value fieldIdentity, MuExp repl)
   = muValueBlock(resultType, [*pre, muSetField(resultType, baseType, last, fieldIdentity, repl)]);

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
