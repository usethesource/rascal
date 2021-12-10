module lang::rascalcore::compile::muRascal::AST

import Message;
import List;
import Set;
import String;
import Node;
import ParseTree;
import IO;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::compile::muRascal::Primitives;
//import lang::rascalcore::grammar::definition::Grammar;

/*
 * Abstract syntax for muRascal.
 * 
 * Position in the compiler pipeline: Rascal -> muRascal -> Rascal2Java
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
                       map[AType, map[str,AType]] commonKeywordFields,
                       AGrammar grammar,
                       loc src)
            ;
            
MuModule errorMuModule(str name, set[Message] messages, loc src) = muModule(name, (), messages, [], [], {}, {}, [], [], [], (), grammar({}, ()), src);
          
// All information related to a function declaration. This can be a top-level
// function, or a nested or anomyous function inside a top level function. 
         
public data MuFunction =					
                muFunction(str name, 
                           str uniqueName,
                           AType ftype,
                           list[MuExp] formals,
                           lrel[str name, AType atype, MuExp defaultExp] kwpDefaults, 
                           str scopeIn,
                           bool isVarArgs,
                           bool isPublic,
                           bool isMemo,
                           set[MuExp] externalRefs,
                           set[MuExp] localRefs,
                           set[MuExp] keywordParameterRefs,
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
    | nativeITree()
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
MuExp muTmpITree(str name, str fuid)                = muTmpNative(name, fuid, nativeITree());

    
// All executable Rascal code is translated to the following muExps.
          
public data MuExp = 
            //muCon(value c)						                // Rascal Constant: an arbitrary IValue
            muNoValue()                                         // Absent value in optional construct
          | muATypeCon(AType atype, map[AType,set[AType]] defs) // AType as constant
          
          | muComment(str text)                                 // Add comment to code
          | muFun(loc uid, AType atype)                         // Non-overloaded Rascal function
           
          | muOFun(list[loc] uids, AType atype)                 // Possibly overloaded Rascal function
          
          | muConstr(AType ctype) 					        	// Constructor
          
          | muComposedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType)
          | muAddedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType)
          
          	// Variables and temporaries
          | muVar(str name, str fuid, int pos, AType atype)		// Variable: retrieve its value
          | muTmpIValue(str name, str fuid, AType atype)	    // Temporary variable introduced by compiler
          | muTmpNative(str name, str fuid, NativeKind nkind)   // Temporary variable introduced by compiler
             
          | muVarKwp(str name, str fuid, AType atype)           // Keyword parameter
          
          // Call and return    		
          //| muCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs)     // Call a function
          
          | muOCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs, loc src)       
                                                                // Call an overloaded declared *Rascal function 
                                                                // Compose fun1 o fun2, i.e., compute fun1(fun2(args))
          | muPrim(str name, AType result, list[AType] details, list[MuExp] exps, loc src)	 // Call a Rascal primitive
           
          | muCallJava(str name, str class, AType funType,
          			   list[MuExp] args, str enclosingFun)		// Call a Java method in given class
 
          | muReturn0()											// Return from a function without value
          | muReturn1(AType result, MuExp exp)			        // Return from a function with value with given type
          
          | muReturn0FromVisit()                                // Return from visit without value
          | muReturn1FromVisit(AType result, MuExp exp)         // Return from visit with value with given type
          
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
          | muTreeGetProduction(MuExp tree)
          | muTreeGetArgs(MuExp tree)
          | muGetKwp(MuExp var, AType atype, str kwpName)
          | muHasKwp(MuExp var, str kwpName)
          
          | muInsert(AType atype, MuExp exp)				    // Insert statement
              
          // Get and assign values
          
          | muAssign(MuExp var, MuExp exp)                      // Assign a value to a variable
          | muVarInit(MuExp var, MuExp exp)                     // Introduce variable and assign a value to it
          | muConInit(MuExp var, MuExp exp)                     // Create a constant
          | muVarDecl(MuExp var)                                // Introduce a variable
          
          | muGetAnno(MuExp exp, AType resultType, str annoName)
          | muGuardedGetAnno(MuExp exp, AType resultType, str annoName)
          //| muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
          
          // Fields of data constructors
          | muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGuardedGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName)
          | muGuardedGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName)

          | muSetField(AType resultType, AType baseTtype, MuExp baseExp, value fieldIdentity, MuExp repl)
          
          // Conditionals and iterations
                    														
          | muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart)// If-then-else statement
          | muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart) // conditional expression
        
          | muIf(MuExp cond, MuExp thenPart)
          						 
          | muWhileDo(str label, MuExp cond, MuExp body)	    // While-Do expression with break/continue label
          | muDoWhile(str label, MuExp body, MuExp cond)
          | muBreak(str label)                                  // Break statement
          | muContinue(str label)                               // Continue statement
          
          | muNot(MuExp exp)                                    // Boolean not
        
          | muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, bool yieldWhenExhausted = false)
          | muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, bool yieldWhenExhausted = false)
          | muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, bool yieldWhenExhausted = false)
          | muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, bool yieldWhenExhausted = false)
         
          // Backtracking
          
          | muEnter(str btscope, MuExp exp, bool yieldWhenExhausted = false)
                                                                // Enter a backtracking scope
          | muSucceed(str btscope)                              // Succeed in current backtracking scope
          | muFail(str label)                                   // Fail in current backtracking scope                      
          
          //  Visit
          | muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
          | muDescendantMatchIterator(MuExp subject, DescendantDescriptor descriptor)
          | muSucceedVisitCase(str visitName)                   // Marks a success exit point of a visit case
        
          // Switch
          | muSwitch(str switchName, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)		// switch over cases for specific value
          
          | muFailCase(str switchName)                          // Marks the failure exit point of a switch or visit case
          | muSucceedSwitchCase(str switchName)                 // Marks a success exit point of a switch case
                 
           // Multi-expressions
          | muBlock(list[MuExp] exps)                           // A list of expressions that does not deliver a value
          | muValueBlock(AType result, list[MuExp] exps)  	    // A list of expressions, only last value remains
                                                             
          // Exceptions
          
          | muThrow(MuExp exp, loc src)
          //| muBuiltinRuntimeExceptionThrow(str exceptionName, list[MuExp] args)
          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
          
          // Auxiliary operations used in generated code
          
          | muReturnFirstSucceeds(list[str] formals, list[MuExp] exps) // return first exp that does not generate CallFailed
          
          // Various tests
          | muRequireNonNegativeBound(MuExp bnd)                // Abort if exp is false
          | muEqual(MuExp exp1, MuExp exp2)    
          | muMatch(MuExp exp1, MuExp exp2)                     // equal that ignores keyword parameters
          | muMatchAndBind(MuExp exp, AType tp)                 // match and bind type parameters
          | muValueIsComparableWithInstantiatedType(MuExp exp, AType tp)
          | muValueIsSubtypeOfInstantiatedType(MuExp exp, AType tp)
          
          | muHasTypeAndArity(AType atype, int arity, MuExp exp)
          | muHasNameAndArity(AType atype, AType consType, MuExp nameExp, int arity, MuExp exp)
          | muValueIsSubtypeOf(MuExp exp, AType tp)
          | muValueIsComparable(MuExp exp, AType tp)
          | muValueIsSubtypeOfValue(MuExp exp2, MuExp exp1)
          | muIsDefinedValue(MuExp exp)
          | muGetDefinedValue(MuExp exp, AType tp)
          | muHasField(MuExp exp, AType tp, str fieldName, set[AType] consesWithField)
          | muIsInitialized(MuExp exp)
          
          // Operations on native integers
          | muIncNativeInt(MuExp var, MuExp inc)
          | muSubNativeInt(MuExp exp1, MuExp exp2)
          | muAddNativeInt(MuExp exp1, MuExp exp2)
          | muMulNativeInt(MuExp exp1, MuExp exp2)
          | muAbsNativeInt(MuExp exp)
          | muSize(MuExp exp, AType atype)
          | muEqualNativeInt(MuExp exp1, MuExp exp2)
          | muLessNativeInt(MuExp exp1, MuExp exp2)
          | muGreaterEqNativeInt(MuExp exp1, MuExp exp2)
          | muToNativeInt(MuExp exp)
          
          // Operations on native booleans
          | muAndNativeBool(MuExp exp1, MuExp exp2)
          | muNotNativeBool(MuExp exp)
        
          // Operations on lists
          | muSubscript(MuExp exp, MuExp idx)
          | muSubList(MuExp lst, MuExp from, MuExp len)
          | muConcreteSubList(MuExp lst, MuExp from, MuExp len, MuExp delta)
          
          // Regular expressions
          | muRegExpCompile(MuExp regExp, MuExp subject)
          | muRegExpBegin(MuExp matcher)
          | muRegExpEnd(MuExp matcher)
          | muRegExpFind(MuExp matcher)
          | muRegExpSetRegion(MuExp matcher, int begin, int end)
          | muRegExpSetRegionInVisit(MuExp matcher)
          | muRegExpSetMatchedInVisit(MuExp matcher)
          | muStringSetMatchedInVisit(int end)
          | muRegExpGroup(MuExp matcher, int n)
          
          // String templates
          | muTemplate(str initial)
          | muTemplateBeginIndent(MuExp template, str indent)
          | muTemplateEndIndent(MuExp template, str unindent)
          | muTemplateAdd(MuExp template, AType atype, value val)
          | muTemplateClose(MuExp template)
          
          // Parse Trees
          | muTreeAppl(MuExp prod, list[MuExp] args, loc src)
          | muTreeAppl(MuExp prod, MuExp argList, loc src)
          | muTreeChar(int char)
          | muTreeUnparse(MuExp tree)
          | muTreeUnparseToLowerCase(MuExp tree)
          
          // Type parameters
          | muTypeParameterMap(set[AType] parameters)
          ;
          
 data VisitDescriptor
    = visitDescriptor(bool direction, bool fixedpoint, bool progress, bool rebuild, DescendantDescriptor descendant)
    ;
    
data DescendantDescriptor
    = descendantDescriptor(bool useConcreteFingerprint, set[AType] reachable_atypes, set[AType] reachable_aprods, map[AType,AProduction] definitions)
    ;
    
data MuCatch = muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body);    

data MuCase = muCase(int fingerprint, MuExp exp);


// ==== Checks ================================================================

MuExp muVar(str name, str _fuid, int _pos, AType _atype){
    assert !isEmpty(name);
    fail;
}

MuExp muTmpIValue(str name, str _fuid, AType _atype){
    assert !isEmpty(name);
    fail;
}

MuExp muTmpNative(str name, str _fuid, NativeKind _nkind) {
    assert !isEmpty(name);
    fail;
}
             
MuExp muVarKwp(str name, str _fuid, AType _atype) {
    assert !isEmpty(name);
    fail;
}

// ==== Utilities =============================================================

bool isSyntheticFunctionName(str name)
    = contains(name, "$");
    
bool isClosureName(str name)
    = findFirst(name, "$CLOSURE") >= 0;

bool isMainName("main") = true;
default bool isMainName(str _) = false;

bool isOuterScopeName(str name)
    = isEmpty(name);
    
str getFunctionName(MuFunction fun){
    if(isOuterScopeName(fun.scopeIn)) return fun.name;
    if(isClosureName(fun.name)) return fun.name; // "<fun.scopeIn>_<fun.uniqueName>";
    //return "<fun.scopeIn>_<fun.uniqueName>";
    return fun.uniqueName;
}

str getUniqueFunctionName(MuFunction fun){
    if(isOuterScopeName(fun.scopeIn)){
        return /*isMainName(fun.name) ? fun.name : */fun.uniqueName;
    }
    return /*isClosureName(fun.name) ? fun.uniqueName : */"<fun.scopeIn>_<fun.uniqueName>";
    //return fun.uniqueName;
}

set[str] varExp = {"muModuleVar", "muVar", "muTmpIValue", "muTmpNative"};

bool isVarOrTmp(MuExp exp)
    = getName(exp) in varExp;

// Produces NativeBool
   
bool producesNativeBool(muPrim(str name, AType result, list[AType] details, list[MuExp] args, loc src)){
    if(name in {"is", "subset"}) return true;
    fail producesNativeBool;
}

bool producesNativeBool(muTmpNative(_,_,nativeBool()))
    = true;
    
bool producesNativeBool(muIfExp(MuExp cond, MuExp thenExp, MuExp elseExp))
    = producesNativeBool(thenExp) && producesNativeBool(elseExp);
    
default bool producesNativeBool(MuExp exp)
    = getName(exp) in {"muEqual", "muMatch", "muMatchAndBind", "muEqualNativeInt", "muIsKwpDefined", "muHasKwp", "muHasKwpWithValue", "muHasTypeAndArity",
                  "muHasNameAndArity", "muValueIsSubtypeOf", "muValueIsComparable", "muValueIsComparableWithInstantiatedType", 
                  "muValueIsSubtypeOfInstantiatedType", "muValueIsSubtypeOfValue", "muLessNativeInt", "muGreaterEqNativeInt", "muAndNativeBool", "muNotNativeBool",
                  "muRegExpFind",  "muIsDefinedValue", "muIsInitialized", "muHasField"};

// Produces NativeInt

bool producesNativeInt(muTmpNative(_,_,nativeInt()))
    = true;
                     
default bool producesNativeInt(MuExp exp)
    = getName(exp) in {"muSize", "muAddNativeInt", "muMulNativeInt", "muAbsNativeInt", "muSubNativeInt", "muToNativeInt", "muRegExpBegin", "muRegExpEnd"};
    
// Produces nativeStr

default bool producesNativeStr(MuExp exp)
    = getName(exp) in {"muTreeUnparse"};


// Produces NativeGuardedIValue

bool producesNativeGuardedIValue(muTmpNative(_,_,nativeGuardedIValue()))
    = true;   
 
 bool producesNativeGuardedIValue(muPrim(str name, AType result, list[AType] details, list[MuExp] exps, loc src))
    = name in { "guarded_subscript", "guarded_field_project"};
    
default bool producesNativeGuardedIValue(MuExp exp)
   = getName(exp) in {"muGuardedGetAnno", "muGuardedGetField", "muGuardedGetKwField"};
   

// Produces NativeITree

bool producesNativeGuardedIValue(muTmpNative(_,_,nativeITree()))
    = true;


default bool producesNativeGuardedIValue(MuExp exp)
    = false;
    
// Get the result type of a MuExp

AType getType(muCon(value v)) = symbol2atype(typeOf(v));
AType getType(muVar(str name, str fuid, int pos, AType atype)) = atype;
AType getType(muTmpIValue(str name, str fuid, AType atype)) = atype;
AType getType(muVarKwp(str name, str fuid, AType atype)) = atype;
//AType getType(muCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs)) = getResultType(atype);   
AType getType(muOCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs, loc src))
    = getResultType(atype);                                                               
AType getType(muPrim(str name, AType result, list[AType] details, list[MuExp] exps, loc src)) 
    = result;
AType getType(muCallJava(str name, str class, AType funType, list[MuExp] args, str enclosingFun)) =  getResultType(funType); 
AType getType(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)) = alub(getType(thenPart), getType(elsePart));

AType getType(muGetKwFieldFromConstructor(AType resultType, MuExp var, str fieldName)) = resultType;
AType getType(muGetFieldFromConstructor(AType resultType, AType consType, MuExp var, str fieldName)) = resultType;
AType getType(muSubList(MuExp lst, MuExp from, MuExp len)) = getType(lst);
AType getType(muConcreteSubList(MuExp lst, MuExp from, MuExp len, MuExp delta)) = getType(lst);

AType getType(muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)) = resultType;
AType getType(muGuardedGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)) = resultType;
AType getType(muGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName)) = resultType;
AType getType(muGuardedGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName)) = resultType;
AType getType(muSetField(AType resultType, AType baseTtype, MuExp baseExp, value fieldIdentity, MuExp repl)) = resultType;
         

AType getType(muTreeAppl(MuExp prod, list[MuExp] args, loc src)) = aadt("Tree", [], dataSyntax());
AType getType(muTreeAppl(MuExp prod, MuExp argList, loc src)) = aadt("Tree", [], dataSyntax());
AType getType(muTreeChar(int char)) = aadt("Tree", [], dataSyntax());
          
AType getType(muTreeGetProduction(MuExp tree)) = aadt("Tree", [], dataSyntax());
AType getType(muTreeGetArgs(MuExp tree)) = alist(aadt("Tree", [], dataSyntax()));
AType getType(muTreeUnparse(MuExp tree)) = astr();

AType getType(muValueBlock(AType result, list[MuExp] exps)) = result;

AType getType(muMatch(MuExp exp1, MuExp exp2)) = abool();
AType getType(muValueIsSubtypeOf(MuExp exp, AType tp)) = abool();
AType getType(muMatchAndBind(MuExp exp, AType tp)) = abool();
AType getType(muValueIsComparableWithInstantiatedType(MuExp exp, AType tp)) = abool();
AType getType(muValueIsSubtypeOfInstantiatedType(MuExp exp, AType tp)) = abool();

default AType getType(MuExp exp) = avalue();
         
// ==== Simplification rules ==================================================

// ---- exitViaReturn --------------------------------------------------------
// All control path end with a return

bool exitViaReturn(MuExp exp){
    res = exitViaReturn1(exp);
    //if(!res)
    //    println("<exp> ==\> <res>");
    
    return res;
}

bool exitViaReturn1(muReturn0()) 
    = true;
bool exitViaReturn1(muReturn1(_, _)) 
    = true;
bool exitViaReturn1(muFailReturn(_)) 
    = true;
bool exitViaReturn1(muCheckMemo(_,_,_)) 
    = true;
bool exitViaReturn1(muMemoReturn0(_,_)) 
    = true;
bool exitViaReturn1(muMemoReturn1(_,_,_)) 
    = true;
bool exitViaReturn1(muThrow(_,_)) 
    = true;

//bool exitViaReturn1(muFail(str label)) 
//    = false;
//
//bool exitViaReturn1(muSucceed(str label)) 
//    = false; 
//    
//bool exitViaReturn1(muBreak(str label)) {
//    return false;  
//}    
//bool exitViaReturn1(muContinue(str label)) {
//    return false;    
//}   
bool exitViaReturn1(me: muEnter(str enter, MuExp exp)){
    return noSequentialExit(me) && exitViaReturn(exp);  
}

bool exitViaReturn1(muBlock([*exps1, exp2])) 
    = exitViaReturn(exp2);
    
bool exitViaReturn1(muValueBlock(AType t, [*exps1, exp2])) 
    = exitViaReturn(exp2);
    
bool exitViaReturn1(muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart))
    = exitViaReturn(thenPart) && exitViaReturn(elsePart);

//bool exitViaReturn1(muIf(MuExp cond, MuExp thenPart))
//    = false;
        
bool exitViaReturn1(muWhileDo(str label, muCon(true), MuExp body)){
    return exitViaReturn(body);
}    
bool exitViaReturn1(muDoWhile(str label,  MuExp body, muCon(false)))
    = exitViaReturn(body);  

bool exitViaReturn1(muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont))
    =  exitViaReturn(body) || exitViaReturn(falseCont);

bool exitViaReturn1(muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont))
    =  exitViaReturn(body) || exitViaReturn(falseCont);

bool exitViaReturn1(muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont))
    =  exitViaReturn(body) || exitViaReturn(falseCont);

bool exitViaReturn1(muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont))
    =  exitViaReturn(body) || exitViaReturn(falseCont);
            
bool exitViaReturn1( muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)) {
    return all(c <- cases, exitViaReturn(c.exp)) && exitViaReturn(defaultExp);
}

bool exitViaReturn1( muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)) {
    return all(c <- cases, exitViaReturn(c.exp)) && exitViaReturn(defaultExp);
}

bool exitViaReturn1(muTry(MuExp exp, MuCatch \catch, MuExp \finally))
    = exitViaReturn(exp) && exitViaReturn(\catch.body) || exitViaReturn(\finally);

bool exitViaReturn1(muCatch(MuExp _thrown_as_exception, MuExp _, MuExp body)) 
    = exitViaReturn(body);
    
bool exitViaReturn1(muEnter(str label, MuExp exp))
    = exitViaReturn(exp);
    
default bool exitViaReturn1(MuExp exp) {
   //println("default exitViaReturn1: <exp>");
   return false;
}
// ---- noSequentialExit ------------------------------------------------------
//
// Determine that an expression does not execute beyond its own code, i.e. falls through

bool hasSequentialExit(MuExp exp) = !noSequentialExit(exp);

// There is no sequential exit from this expression

bool trace = false;

bool reportResult(MuExp exp, bool res){
    if(trace) { iprintln(exp); println(" ===\> <res>"); }
    return res;
}

bool noSequentialExit(m: muFail(str label)) {
    res =/*false; */ true; //label notin entered;
    return reportResult(m, res);
}
bool noSequentialExit(m: muFailCase(_)){
    res = false;
    return reportResult(m, res);
}

bool noSequentialExit(m: muSucceedSwitchCase(str switchName)){
    res = false;
    return reportResult(m, res);
 }
      
bool noSequentialExit(m: muSucceed(str label)) {
    res = /*false; */ true; //label in entered; 
    return reportResult(m, res);
}    
bool noSequentialExit(m: muBreak(str label)) {
    res = true; //label in entered; 
    return reportResult(m, res);
}    

bool noSequentialExit(m: muContinue(str label)) {
    res = true; //label in entered; 
    return reportResult(m, res);
}   

bool noSequentialExit(m:muReturn0()) {
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m:muReturn1(_, _)){ 
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m:muInsert(t, exp1)){
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m:muFailReturn(_)){
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m: muCheckMemo(_,_,_)){
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m: muMemoReturn0(_,_)){
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m:muMemoReturn1(_,_,_)) {
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m: muThrow(_,_)){ 
    res = true;
    return reportResult(m, res);
}

bool noSequentialExit(m: muEnter(str enter, MuExp exp)){
    res = noFailOrSucceed(enter, exp) && noSequentialExit(exp);// || exitViaReturn(exp);
    return reportResult(m, res);
}
    
bool noSequentialExit(m: muBlock([*exps1, exp2])) {
    res = /*(!isEmpty(exps1) && noSequentialExit(muBlock(exps1))) || */ noSequentialExit(exp2);
    return reportResult(m, res);
}

bool noSequentialExit(m: muBlock([])) {
    res = false;
    return reportResult(m, res);
}
    
bool noSequentialExit(m: muValueBlock(AType t, [*exps1, exp2])) {
    res = noSequentialExit(exp2);
    return reportResult(m, res);
}
    
bool noSequentialExit(m: muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart)){
    res = noSequentialExit(thenPart) && noSequentialExit(elsePart);
    return reportResult(m, res);
}
    
bool noSequentialExit(m: muWhileDo(str label, muCon(true), MuExp body)){
    res = false; //noSequentialExit(body, label + entered);
    return reportResult(m, res);
} 
 
bool noSequentialExit(m: muDoWhile(str label,  MuExp body, muCon(false))){
    res = noSequentialExit(body) && exitViaReturn(body);
    return reportResult(m, res); 
}

bool noSequentialExit(m: muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont)){
    //res = (noFailOrSucceed(label, body) && noSequentialExit(body)) || (noFailOrSucceed(label, falseCont) && noSequentialExit(falseCont));
    res = (noFailOrSucceed(label, falseCont) && noSequentialExit(falseCont));
    return reportResult(m, res);
}

bool noSequentialExit(m: muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont)){
    res = /*(noFailOrSucceed(label, body) && noSequentialExit(body)) || */ (noFailOrSucceed(label, falseCont) && noSequentialExit(falseCont));
    return reportResult(m, res);
}

bool noSequentialExit(m: muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont)){
    res = /*(noFailOrSucceed(label, body) && noSequentialExit(body)) ||*/ (noFailOrSucceed(label, falseCont) && noSequentialExit(falseCont));
    return reportResult(m, res);
}

bool noSequentialExit(m: muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont)){
    res = /*(noFailOrSucceed(label, body) && noSequentialExit(body)) ||*/ (noFailOrSucceed(label, falseCont) &&  noSequentialExit(falseCont));
    return reportResult(m, res);
}

                
bool noSequentialExit(m: muSwitch(str switchName, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)) {
    res = any(c <- cases, noSequentialExit(c.exp)) || noSequentialExit(defaultExp);
    return reportResult(m, res);
}

bool noSequentialExit(m: muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)) {
    res = any(c <- cases, noSequentialExit(c.exp)) || noSequentialExit(defaultExp);
    return reportResult(m, res);
}

bool noSequentialExit(m: muTry(MuExp exp, MuCatch \catch, MuExp \finally)){
    res = noSequentialExit(exp) && noSequentialExit(\catch.body) && noSequentialExit(\finally);
    return reportResult(m, res);
}

bool noSequentialExit(muCatch(MuExp _thrown_as_exception, MuExp _, MuExp body)) {
    res = noSequentialExit(body);
    return reportResult(body, res);
}
    
default bool noSequentialExit(MuExp exp){
    res = false;
    return reportResult(exp, res);
}

bool noFailOrSucceed(str enter, MuExp exp){
    visit(exp){
        case muFail(enter): return false;
        case muSucceed(enter): return false;
    }
    return true;
}

// ---- block -----------------------------------------------------------------

MuExp muBlock([MuExp exp]) = exp;

MuExp muBlock([ *exps1, muBlock([*exps2]), *exps3 ])
    = muBlock([ *exps1, *exps2, *exps3 ]);
    
MuExp muBlock([ *exps1, exp0, *exps2])
    = muBlock([*exps1, exp0])
    when !isEmpty(exps2),
         muInsert(_, _) := exp0 || noSequentialExit(exp0);
         
MuExp muBlock([*MuExp pre, muValueBlock(AType t, list[MuExp] elems), *MuExp post])
    = muBlock([*pre, *elems, *post]);

MuExp muBlock([*MuExp pre, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), *MuExp post])
    = muBlock([*pre, muIfElse(cond, thenPart, elsePart), *post]);
    
// ---- muValueBlock ----------------------------------------------------------

MuExp muValueBlock(AType t, [MuExp exp]) = exp;
    
MuExp muValueBlock(AType t, [*MuExp pre, muBlock(list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t, [*pre, *elems, *post, last]);
    
MuExp muValueBlock(AType t1, [*MuExp pre, muValueBlock(AType t2, list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t2, [*pre, *elems, *post, last]);

MuExp muValueBlock(AType t1, [*MuExp pre, muReturn1(AType t2, MuExp exp)])
    = muBlock([*pre, muReturn1(t2, exp)]);
    
 //MuExp muValueBlock(AType t, [*MuExp pre, muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart)])
 //   =  pre == [] ?  muIfExp(cond, thenPart, elsePart)  : muValueBlock(t, [*pre, muIfExp(cond, thenPart, elsePart)]);
    
// ---- muReturn1 -------------------------------------------------------------

MuExp muReturn1(AType t1, muReturn1(AType t2, MuExp exp)) {
    return muReturn1(t2, exp);
}

MuExp muReturn1(AType t1, muCheckMemo(AType funtype, list[MuExp] args, MuExp body)){
    return muCheckMemo(funtype, args, body);
}
    
MuExp muReturn1(AType t1, muMemoReturn1(AType funtype, list[MuExp] args, MuExp functionResult)){
    return muMemoReturn1(funtype, args, functionResult);
}

//MuExp muReturn1(AType t, muBlock([MuExp exp])){
//    return muReturn1(t, exp);
//}
    
MuExp muReturn1(AType t, muBlock([*MuExp exps, MuExp exp])){
     return muBlock([*exps, muReturn1(t, exp)]);
}    
MuExp muReturn1(AType t1, muValueBlock(AType t2, [*MuExp exps, MuExp exp])){
     return muBlock([ *exps, muReturn1(t1, exp) ]);
}    
MuExp muReturn1(AType t, muAssign(MuExp var, MuExp exp)){
    return muBlock([muAssign(var, exp), muReturn1(t, var)]);
}
    
MuExp muReturn1(AType t, muVarInit(MuExp var, MuExp exp)){
    return muBlock([muVarInit(var, exp), muReturn1(t, var)]);
}
    
MuExp muReturn1(AType t, muConInit(MuExp var, MuExp exp)){
    return muBlock([muConInit(var, exp), muReturn1(t, var)]);
}
    
MuExp muReturn1(AType t, muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart)){
    return muIfElse(cond, muReturn1(t, thenPart), muReturn1(t, elsePart));
}

MuExp muReturn1(AType t, muIf(MuExp cond, MuExp thenPart)){
    return  muIf(cond, muReturn1(t, thenPart));
}
    
MuExp muReturn1(AType t, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)){
    return muIfElse(cond,muReturn1(t, thenPart), muReturn1(t, elsePart));
}
    
MuExp muReturn1(AType t, muWhileDo(str label, MuExp cond, MuExp body)){
    return  addReturn(t, false, muWhileDo(label, cond, muReturn1(t, body)));
}
    
//MuExp muReturn1(AType t, muDoWhile(str label, MuExp body, MuExp cond)){
//    return  muDoWhile(label, muReturn1(t, body), cond);
//    }
         
MuExp muReturn1(AType t, muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal))
    = muForRange(label, var, first, second, last, insertReturn(body, [<"range", label>], (t == abool() || t == avalue()), exhaustedVal), insertReturn(falseCont, [<"range", label>], (t == abool() || t == avalue()), exhaustedVal));
              
MuExp muReturn1(AType t, muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal)){
    return muForRangeInt(label, var, ifirst, istep, last, insertReturn(body, [<"range", label>], (t == abool() || t == avalue()), exhaustedVal), insertReturn(falseCont, [<"range", label>], (t == abool() || t == avalue()), exhaustedVal));
}    
MuExp muReturn1(AType t, fo: muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal)){
    return muForAll(label, var, iterType, iterable, insertReturn(body, [<"all", label>] , (t == abool() || t == avalue()), exhaustedVal), insertReturn(falseCont,  [<"all", label>] , (t == abool() || t == avalue()), exhaustedVal));
} 

MuExp muReturn1(AType t, fo: muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal)){
    return muForAny(label, var, iterType, iterable, insertReturn(body, [<"any", label>], (t == abool() || t == avalue()), exhaustedVal), insertReturn(falseCont,  [<"any", label>], (t == abool() || t == avalue()), exhaustedVal));
} 
 
MuExp muReturn1(AType t, muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)){
    dflt = muReturn1(t, defaultExp);
    //if(any(c <- cases, !exitWithReturn(c))){
    //    dflt = muIfthen(execDefault, dflt);
    //}
    return muSwitch(label, exp, [muCase(c.fingerprint, muReturn1(t, c.exp)) | c <- cases], dflt, useConcreteFingerprint);
}
    
private MuExp addReturn(AType t, bool result, bl: muBlock([*exps, muReturn1(abool(), muCon(result))])) = bl;

private default MuExp addReturn(AType t, bool result, MuExp exp) {
    return muBlock([exp, (t == abool() || t == avalue()) ? muReturn1(abool(), muCon(result)) : muFailReturn(t)]);
}

MuExp insertReturn(MuExp exp, lrel[str,str] entered, bool asBool, bool _exhaustedVal){
//iprintln(exp);
    return
          top-down visit(exp) { 
                case muSucceed(enter): {    //if(endsWith(enter, "_DESC")) insert muReturn1(abool(), muCon(true));
                                            //if(entered[0] == <"all", enter>) fail;
                                            //if(entered[0] == <"any", enter>) insert muReturn1(abool(), muCon(true));
                                            if(enter == entered<1>[-1] /*&& !endsWith(enter, "_DESC")*/) insert muBlock([ muComment("insertReturn: muSucceed"), muReturn1(abool(), muCon(true))]);
                                            
                                            //if(!(enter in entered)) insert muReturn1(abool(), muCon(true));
                                            //if(!(enter == entered[0] || endsWith(enter, "_DESC"))) fail;
                                            //insert muReturn1(abool(), muCon(true));
                                       }
                case muFail(enter):    {    ;
                                            //if(entered[0] == <"all", enter>) fail;
                                            //if(entered[0] == <"any", enter>) fail; //insert muReturn1(abool(), muCon(true));
                                            
                                            if(enter == entered<1>[-1])   //enter == entered[-1] //<<<
                                                insert muBlock([ muComment("insert return mufail"), asBool ? muReturn1(abool(), muCon(false)) : muFailReturn(abool())]);
                                       }
               
                //case muEnter(str enter1, MuExp exp1, yieldWhenExhausted=exhaustedVal1): {
                //    insert muEnter(enter1, insertReturn(exp1, <"enter",enter1> + entered, asBool, exhaustedVal), yieldWhenExhausted=exhaustedVal1);
                //    }
                //case muBlock([*exps1, exp]) => muBlock([*exps1, insertReturn(exp, entered, asBool,exhaustedVal)])
                //case muValueBlock(t, [*exps1, exp]) => muValueBlock(t, [*exps1, insertReturn(exp, entered, asBool, exhaustedVal)])
                //case muWhileDo(str enter1, MuExp cond, MuExp body) => muWhileDo(enter1, cond, insertReturn(body, <"while",enter1> + entered, asBool, exhaustedVal)) //when enter1 in entered
                //case muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart) => muIfElse(cond, insertReturn(thenPart, entered, asBool, exhaustedVal), insertReturn(elsePart, entered, asBool, exhaustedVal))
                //case muIf(MuExp cond, MuExp thenPart) => muIf(cond, insertReturn(thenPart, entered, asBool, exhaustedVal))
                //case muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal1):{ 
                //     insert muForAll(label, var, iterType, iterable, insertReturn(body, <"all", label> + entered, asBool, exhaustedVal), insertReturn(falseCont, <"all", label> + entered, asBool, exhaustedVal), yieldWhenExhausted = exhaustedVal1);
                //     }
                //case muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal1):{ 
                //     //if(label notin entered) fail;
                //     insert muForAny(label, var, iterType, iterable, insertReturn(body, <"any", label> + entered, asBool, exhaustedVal), insertReturn(falseCont, <"any", label> + entered, asBool, exhaustedVal),yieldWhenExhausted = exhaustedVal1);
                //     }
                //case muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal1) =>
                //     muForRange(label, var, first, second, last, insertReturn(body, label + entered, asBool, exhaustedVal), falseCont, yieldWhenExhausted = exhaustedVal1) when label in entered
                //case muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal1) =>
                //     muForRangeInt(label, var, ifirst, istep, last, insertReturn(body, label + entered, asBool, exhaustedVal), falseCont, yieldWhenExhausted = exhaustedVal1) when label in entered
            }
}

MuExp removeDeadCode(MuExp exp)
    = removeDeadCode(exp, []);
    
MuExp removeDeadCode(MuExp exp, list[str] entered){
    //println("Before removeDeadCode:"); iprintln(exp);
    res =  top-down-break visit(exp){
        case muBlock([*MuExp pre, MuExp exp2, *MuExp post]) => muBlock([*pre, exp2]) 
             when !isEmpty(post), 
                  muSucceedSwitchCase(_) := post[-1] ? noSequentialExit(exp2) : exitViaReturn(exp2), bprintln("muBlock removes: <post>")
        //case muValueBlock(AType t, [*MuExp pre, MuExp exp2, *MuExp post]) => muValueBlock(t, [*pre, exp2]) when !isEmpty(post), noSequentialExit(exp2, entered)
        case muEnter(str enter1, MuExp exp1, yieldWhenExhausted = exhaustedVal) => muEnter(enter1, removeDeadCode(exp1, enter1 + entered), yieldWhenExhausted = exhaustedVal)
        case muWhileDo(str enter1, MuExp cond, MuExp body) => muWhileDo(enter1, cond, removeDeadCode(body, enter1 + entered))
        case muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint) =>
             muSwitch(label, exp, [muCase(c.fingerprint, removeDeadCode(c.exp, entered)) | c <- cases], removeDeadCode(defaultExp, entered), useConcreteFingerprint)
        //case muCase(int fingerPrint, muBlock([*MuExp pre, MuExp exp2, muSucceedSwitchCase(_)])) => muCase(fingerPrint, muBlock([*pre, exp2]))
        //    when noSequentialExit(exp2, entered)
        case muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) => 
             muForAll(label, var, iterType, iterable, removeDeadCode(body, entered), removeDeadCode(falseCont, entered), yieldWhenExhausted = exhaustedVal) when label in entered
             
        case muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) => 
             muForAny(label, var, iterType, iterable, removeDeadCode(body, entered), removeDeadCode(falseCont, entered), yieldWhenExhausted = exhaustedVal) when label in entered
        case muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
             muForRange(label, var, first, second, last, removeDeadCode(body, label + entered), falseCont, yieldWhenExhausted = exhaustedVal) when label in entered
        case muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
             muForRangeInt(label, var, ifirst, istep, last, removeDeadCode(body, label + entered), falseCont, yieldWhenExhausted = exhaustedVal) when label in entered    
    }
    //println("After removeDeadCode:"); iprintln(res);
    return res;
}

MuExp muReturn1(AType t, me:muEnter(str btscope, MuExp exp, yieldWhenExhausted=exhaustedVal)){
    ires = insertReturn(exp, [<"enter", btscope>], t == abool() || t == avalue(), exhaustedVal);
    res = muEnter(btscope, ires, yieldWhenExhausted=exhaustedVal);
    nos = noSequentialExit(res);
    return noSequentialExit(res) ? res : muBlock([muComment("muReturn1/muEnter"), addReturn(t, exhaustedVal, res)]);
} 
    
MuExp muReturn1(AType t, muSucceed(str btscope))
    = /*muReturn1(abool(), muCon(true)); */ muSucceed(btscope);
    

MuExp muReturn1(AType t, muFail(str btscope)){
    return /*muReturn1(abool(), muCon(false)); */ muFail(btscope);
}

MuExp muReturn1(AType t, muBreak(str btscope)){
    return muBreak(btscope);
}

MuExp muReturn1(AType t, muContinue(str btscope)){
    return muContinue(btscope);
}
     
MuExp muReturn1(AType t, mf: muFailCase(str switchName)){
    return mf;
}

MuExp muReturn1(AType t, muFailReturn(AType t)){
    return muFailReturn(t);
}
    
MuExp muReturn1(AType t, muTry(MuExp exp, MuCatch \catch, MuExp \finally)){
    return muTry(muReturn1(t, exp), \catch, \finally); // TODO try? finaly?
}
    
MuExp muReturn1(AType t, muThrow(MuExp exp, loc src)){
    return muThrow(exp, src);
}    
    
// ---- muFailReturn ----------------------------------------------------------

MuExp muFailReturn(AType t) 
    = muReturn1(abool(), muCon(false))
    when t == abool();
    
// ---- muThrow ---------------------------------------------------------------
    
// ---- muConInit/muVarInit/muConInit -----------------------------------------

MuExp muAssign(MuExp var1, MuExp var2)
    = muBlock([])
      when (var2 has name && var1.name == var2.name && var2 has fuid && var1.fuid == var2.fuid && var2 has pos && var1.pos == var2.pos);

MuExp muAssign(MuExp var1, b: muBreak(_)) = b;
MuExp muAssign(MuExp var1, c: muContinue(_)) = c;


// ----

MuExp muConInit(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muConInit(var, exp)]);

MuExp muVarInit(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muVarInit(var, exp)]);
    
MuExp muAssign(MuExp var, muBlock([*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);
    
// ---- 
 
MuExp muConInit(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muConInit(var, exp)]);
    
MuExp muVarInit(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muVarInit(var, exp)]);
    
MuExp muAssign(MuExp var, muValueBlock(AType t, [*MuExp exps, MuExp exp]))
    = muBlock([*exps, muAssign(var, exp)]);

// ----

MuExp insertAssignBool(MuExp var, MuExp exp, set[str] entered){
    return top-down-break visit(exp) { 
                case muSucceed(enter) => muBlock([muAssign(var, muCon(true)), muSucceed(enter)]) when enter in entered
                case muFail(enter) => muBlock([muAssign(var, muCon(false)), muFail(enter)])
                case muEnter(str enter1, MuExp exp1, yieldWhenExhausted = exhaustedVal) => muEnter(enter1, insertAssignBool(var, exp1, entered + enter1), yieldWhenExhausted = exhaustedVal)
                case muForAll(str label, MuExp var2, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
                     muForAll(label, var2, iterType, iterable, insertAssignBool(var, body, entered + label), falseCont, yieldWhenExhausted = exhaustedVal)
                case muForRange(str label, MuExp var2, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
                     muForRange(label, var2, first, second, last, insertAssignBool(var, body, entered + label), falseCont, yieldWhenExhausted = exhaustedVal)
                case muForRangeInt(str label, MuExp var2, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
                     muForRangeInt(label, var2, ifirst, istep, last, insertAssignBool(var, body, entered + label), falseCont, yieldWhenExhausted = exhaustedVal)
            };
}

//MuExp muConInit(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
//    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);    
    
//MuExp muVarInit(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
//    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);
//
//MuExp muAssign(MuExp var, muForRangeInt(str label, MuExp loopVar, int ifirst, int istep, MuExp last, MuExp exp))
//    = muBlock([ muAssign(var, muCon(false)), muForRangeInt(label, loopVar, ifirst, istep, last, muAssign(var, exp)) ]);

// ----

MuExp muConInit(MuExp var, muWhileDo(str label, MuExp cond, MuExp body))
    = muBlock([muVarDecl(var), muWhileDo(label, cond, muAssign(var, body))]);
    
MuExp muVarInit(MuExp var, muWhileDo(str label, MuExp cond, MuExp body))
    = muBlock([muVarDecl(var), muWhileDo(label, cond, muAssign(var, body))]);

MuExp muAssign(MuExp var, muWhileDo(str label, MuExp cond, MuExp body))
    = muWhileDo(label, cond, muAssign(var, body));
    
// ---- 

//MuExp muConInit(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
//    = muBlock([ muVarInit(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);
//
//MuExp muVarInit(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
//    = muBlock([ muVarInit(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);   
//
//MuExp muAssign(MuExp var, muForAll(str label, MuExp loopVar, AType iterType, MuExp iterable, MuExp body))
//    = muBlock([ muAssign(var, muCon(false)), muForAll(label, loopVar, iterType, iterable, muAssign(var, body)) ]);
    
// ----    

 MuExp muConInit(var, muEnter(str btscope, MuExp exp))
    = muBlock([muVarInit(var, muCon(false)), muEnter(btscope, insertAssignBool(var, exp, {btscope}))]);
    
MuExp muVarInit(var, muEnter(str btscope, MuExp exp))
    = muBlock([muVarInit(var, muCon(false)), muEnter(btscope, insertAssignBool(var, exp, {btscope}))]);     
 
MuExp muAssign(var, me:muEnter(str btscope, MuExp exp))
    = muBlock([muAssign(var, muCon(false)), muEnter(btscope, insertAssignBool(var, exp, {btscope}))]); 

// ----

MuExp muConInit(MuExp var, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muBlock([muVarDecl(var), muIfExp(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);

MuExp muVarInit(MuExp var, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muBlock([muVarDecl(var), muIfExp(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);   

// ---- 

MuExp muConInit(MuExp var, muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muBlock([muVarDecl(var), muIfElse(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);
    
MuExp muVarInit(MuExp var, muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muBlock([muVarDecl(var), muIfElse(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);
    
MuExp muAssign(MuExp var, muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muIfElse(cond, muAssign(var, thenPart), muAssign(var, elsePart));   
    
// ----
MuExp muConInit(MuExp var, muIf(MuExp cond, MuExp thenPart))
    =  muConInit(var, muIfExp(cond, thenPart, muCon(false))) when var.atype == abool();
    
MuExp muVarInit(MuExp var, muIf(MuExp cond, MuExp thenPart))
    =  muVarInit(var, muIfExp(cond, thenPart, muCon(false))) when var.atype == abool();

MuExp muVarInit(MuExp var, muIf(MuExp cond, MuExp thenPart))
    =  muVarInit(var, muIfExp(cond, thenPart, var)) when var.atype != abool();
    
MuExp muAssign(MuExp var, muIf(MuExp cond, MuExp thenPart))
    =  muAssign(var, muIfExp(cond, thenPart, muCon(false))) when var.atype == abool();
    
MuExp muAssign(MuExp var, muIf(MuExp cond, MuExp thenPart))
    =  muAssign(var, muIfExp(cond, thenPart, var)) when var.atype != abool();
         
// ----

MuExp muConInit(MuExp var, muSucceed(str btscope))
    = muBlock([muConInit(var, muCon(true)), muSucceed(btscope)]);

MuExp muVarInit(MuExp var, muSucceed(str btscope))
    = muBlock([muVarInit(var, muCon(true)), muSucceed(btscope)]);   
    
MuExp muAssign(MuExp var, muSucceed(str btscope))
    = muBlock([muAssign(var, muCon(true)), muSucceed(btscope)]);

// ----

MuExp muConInt(MuExp var, muFail(_))
    = muBlock([muConInit(var, muCon(false))]);

    
MuExp muVarInt(MuExp var, muFail(_))
    = muBlock([muVarInit(var, muCon(false))]);   
     
MuExp muAssign(MuExp var, muFail(btscope))
    = muBlock([muAssign(var, muCon(false))]);
    
// ---- muIf ------------------------------------------------------------------
    
//MuExp muIf(muCon(true), MuExp thenPart) = thenPart;
//
//MuExp muIf(muCon(false), MuExp thenPart) = muBlock([]);
//
//MuExp muIf(me:muEnter(enter, cond), MuExp thenPart)   
//      = muEnter(enter, insertThenPart(cond, thenPart, {enter}));

// ---- muIfExp ---------------------------------------------------------------

MuExp muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)
    = muIfElse(cond, thenPart, elsePart)
    when noSequentialExit(thenPart) || noSequentialExit(elsePart) || muBlock(_) := thenPart || muBlock(_) := elsePart;

MuExp muIfExp(MuExp cond, MuExp thenPart, muFailReturn(AType t))
    = muIfElse(cond, thenPart, muFailReturn(t));
    
MuExp muIfExp(muValueBlock(AType t, [*MuExp exps, MuExp exp]), MuExp thenPart, MuExp elsePart)
    = muValueBlock(t, [*exps, muIfExp(exp, thenPart, elsePart)]);
    
//MuExp muIfExp(MuExp cond, muValueBlock([*MuExp exps, MuExp exp]), MuExp elsePart)
//    = muValueBlock(t, cond, muValueBlock([*exps, muIfExp(cond, exp, elsePart), elsePart);
//    
//MuExp muIfExp(MuExp cond, MuExp thenPart, muValueBlock([*MuExp exps, MuExp exp]))
//    = muValueBlock(t, cond, thenPart, muValueBlock([*exps, muIfExp(cond, thenPart, exp), elsePart));
    
    
//MuExp muIfExp(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart;

MuExp muIfExp(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

MuExp muIfExp(muIfExp(MuExp cond, MuExp thenPart1, MuExp elsePart1), MuExp thenPart2, MuExp elsePart2)
    = muIfExp(cond, muIfExp(thenPart1, thenPart2, elsePart2), muIfExp(elsePart1, thenPart2, elsePart2));
    
// ---- muNot -----------------------------------------------------------------

MuExp muNot(muEnter(str enter, MuExp exp, yieldWhenExhausted=b)){
    return muEnter(enter, muNot(exp), yieldWhenExhausted=!b); //<==== !b
}  
 
MuExp muNot(muBlock([])) = muBlock([]);

MuExp muNot(muBlock([*exps, exp]))
    = muBlock([*exps, muNot(exp)]);
    
MuExp muNot(muValueBlock(AType t, [*exps, exp]))
    = muValueBlock(t, [*exps, muNot(exp)]);
    
MuExp muNot(muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muIfExp(cond, muNot(thenPart), muNot(elsePart));

MuExp muNot(muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart))
    = muIfElse(cond, muNot(thenPart), muNot(elsePart));
    
MuExp muNot(muIf(MuExp cond, MuExp thenPart))
    = muIf(cond, muNot(thenPart));

MuExp muNot(muWhileDo(str label, MuExp cond, MuExp body))
    = muWhileDo(label, cond, muNot(body));
    
MuExp muNot(fo: muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont))
    = muForAny(label, var, iterType, iterable, muNot(body), muNot(falseCont)/*, yieldWhenExhausted = !fo.yieldWhenExhausted*/);
    
MuExp muNot(fa: muForAny(str label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont))
    = muForAll(label, var, iterType, iterable, muNot(body), muNot(falseCont)/*, yieldWhenExhausted = !fa.yieldWhenExhausted*/);
    
MuExp muNot(fr: muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont))
    = muForRange(label, var, first, second, last, muNot(body), muNot(falseCont)/*, yieldWhenExhausted = !fr.yieldWhenExhausted*/);
    
MuExp muNot(fr: muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont))
    = muForRangeInt(label, var, ifirst, istep, last, muNot(body), muNot(falseCont)/*, yieldWhenExhausted = !fr.yieldWhenExhausted*/);
    
MuExp muNot(muReturn1(AType t, MuExp exp)){
    return muReturn1(t, muNot(exp));
}
MuExp muNot(muFail(str label)) = muSucceed(label);
MuExp muNot(muSucceed(str label)) = muFail(label);
//MuExp muNot(muCon(bool b)) = muCon(!b);

// ---- muIfElse --------------------------------------------------------------

MuExp insertThenPart(MuExp exp, MuExp thenPart, set[str] entered){
    return top-down-break visit(exp) { 
                case muSucceed(enter) => muBlock([thenPart, muSucceed(enter)]) when enter in entered
                //case muFail(enter) => muBlock([muAssign(var, muCon(false)), muFail(enter)])
                case muEnter(str enter1, MuExp exp1) => muEnter(enter1, insertThenPart(exp1, thenPart, entered))
                case muForAll(str label, MuExp var2, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
                     muForAll(label, var2, iterType, iterable, insertThenPart(body, thenPart, entered), falseCont, yieldWhenExhausted = exhaustedVal)
                case muForRange(str label, MuExp var2, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
                     muForRange(label, var2, first, second, last, insertThenPart(body, thenPart, entered), falseCont, yieldWhenExhausted = exhaustedVal)
                case muForRangeInt(str label, MuExp var2, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal) =>
                     muForRangeInt(label, var2, ifirst, istep, last, insertThenPart(body, thenPart, entered), falseCont, yieldWhenExhausted = exhaustedVal)
            };
}

//MuExp muIfElse(MuExp cond, MuExp thenPart, MuExp elsePart) = thenPart when thenPart == elsePart;
//MuExp muIfElse(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart
//    when /muFail(_) !:= thenPart;
//MuExp muIfElse(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

//MuExp muIfElse(MuExp cond, muCon(_), muCon(_)) = muBlock([]);

MuExp muIfElse(MuExp cond, MuExp thenPart, muBlock([])) = muIf(cond, thenPart);

MuExp muIfElse(MuExp cond, muIfExp(cond1, thenPart1, elsePart1), MuExp elsePart) 
    = muIfElse(cond, muIfElse(cond1, thenPart1, elsePart1), elsePart);
    
MuExp muIfElse(MuExp cond, MuExp thenPart, muIfExp(cond1, thenPart1, elsePart1)) 
    = muIfElse(cond, thenPart, muIfElse(cond1, thenPart1, elsePart1));
    
MuExp muIfElse(muValueBlock(AType t, [*MuExp exps, MuExp exp]), MuExp thenPart, MuExp elsePart)
    = muBlock([*exps, muIfElse(exp, thenPart, elsePart)]); 

MuExp muIfElse(me:muEnter(enter, cond, yieldWhenExhausted = exhaustedVal), MuExp thenPart, MuExp elsePart)   
      = muBlock([muEnter(enter, insertThenPart(cond, thenPart, {enter}), yieldWhenExhausted = exhaustedVal), elsePart]);
              
MuExp muIfElse(muForAll(str label, MuExp var2, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal), MuExp thenPart, MuExp elsePart)
    = muBlock([muForAll(label, var2, iterType, iterable, insertThenPart(body, thenPart, {label}), falseCont, yieldWhenExhausted = exhaustedVal), elsePart]);  
    
MuExp muIfElse(muForRange(str label, MuExp var2, MuExp first, MuExp second, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal), MuExp thenPart, MuExp elsePart)
        = muBlock([muForRange(label, var2, first, second, last, insertThenPart(body, thenPart, {label}), falseCont, yieldWhenExhausted = exhaustedVal), elsePart]);
    
MuExp muIfElse(muForRangeInt(str label, MuExp var2, int ifirst, int istep, MuExp last, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal), MuExp thenPart, MuExp elsePart)
        = muBlock([muForRangeInt(label, var2, ifirst, istep, last, insertThenPart(body, thenPart, {label}), falseCont, yieldWhenExhausted = exhaustedVal), elsePart]);

// ---- muEnter ---------------------------------------------------------------

MuExp muEnter(str btscope, MuExp exp=muCon(false))
    = muEnter(btscope, exp);

MuExp muEnter(str btscope, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muEnter(btscope, muIfElse(cond, thenPart, elsePart));
    
MuExp muEnter(str enter, MuExp exp) = exp when containsEnter(enter, exp);

bool containsEnter(str enter, MuExp exp){
    visit(exp){
        case muEnter(enter, MuExp _exp1): return true;
        case muForAll(enter, MuExp _var, AType _iterType, MuExp _iterable, MuExp _body, MuExp _falseCont): return true;
        case muForAny(enter, MuExp _var, AType _iterType, MuExp _iterable, MuExp _body, MuExp _falseCont): return true;
        case muForRange(enter, MuExp _var, MuExp _first, MuExp _second, MuExp _last, MuExp _body, MuExp _falseCont): return true;
        case muForRangeInt(enter, MuExp _var, int _ifirst, int _istep, MuExp _last, MuExp _body, MuExp _falseCont): return true;
    }
    return false;
}
    
MuExp muEnter(str label, muForAll(label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal))
    = muForAll(label, var, iterType, iterable, body, falseCont, yieldWhenExhausted = exhaustedVal);
    
MuExp muEnter(str label, muForAny(label, MuExp var, AType iterType, MuExp iterable, MuExp body, MuExp falseCont, yieldWhenExhausted = exhaustedVal))
    = muForAny(label, var, iterType, iterable, body, falseCont, yieldWhenExhausted = exhaustedVal);
    
// ---- muForAll --------------------------------------------------------------

MuExp muForAll(str label, MuExp var, AType iterType, muValueBlock(AType _, [*MuExp exps, MuExp iterable]), MuExp body, MuExp falseCont, bool yieldWhenExhausted = false)
    =  muValueBlock(iterType, [*exps, muForAll(label, var, iterType, iterable, body, falseCont, yieldWhenExhausted = yieldWhenExhausted)]);
       
//MuExp muForAll(str label, str varName, str fuid, MuExp iterable, MuExp body)
//    = muForAll(label, varName, fuid, iterable, body1)
//    when body1 := visit(body) { case muSucceed(str btscope) => muSucceed(label) } && body1 != body;

// ---- muForAny --------------------------------------------------------------

MuExp muForAny(str label, MuExp var, AType iterType, muValueBlock(AType _, [*MuExp exps, MuExp iterable]), MuExp body, MuExp falseCont, bool yieldWhenExhausted = false)
    =  muValueBlock(iterType, [*exps, muForAny(label, var, iterType, iterable, body, falseCont, yieldWhenExhausted = yieldWhenExhausted)]);
    
// ---- muRegExpCompile -------------------------------------------------------
 
MuExp muRegExpCompile(muValueBlock(AType t, [*MuExp exps, MuExp regExp]), MuExp subject)
    = muValueBlock(t, [ *exps, muRegExpCompile(regExp, subject)]);

// ============ Flattening Rules ================================================
// TODO: shoud go to separate module (does not work in interpreter)

bool shouldFlatten(MuExp arg) 
    =     muValueBlock(_t, _elems) := arg 
       || muAssign(MuExp _var, MuExp _exp) := arg
       || muVarInit(MuExp _var, MuExp _exp) := arg
       || muConInit(MuExp _var, MuExp _exp) := arg
       || muInsert( AType _tp, MuExp _exp) := arg
       //|| muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl) := arg
       || muSetField(AType _resultType, AType _baseType, MuExp _baseExp, value _fieldIdentity, MuExp _repl) := arg
       || muEnter(_btscope, MuExp _exp) := arg 
       || muIfElse(_cond, _thenPart, _elsePart) := arg 
       || muWhileDo(str _ab, MuExp _cond, MuExp _body) := arg
       || muBlock(_elems) := arg 
       //|| muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor) := arg
       || muIfExp(_, thenPart, elsePart) := arg && (shouldFlatten(thenPart) || shouldFlatten(elsePart))
       ;
 
int nauxVars = -1;
         
// Rascal primitives
tuple[bool flattened, list[MuExp] auxVars, list[MuExp] pre, list[MuExp] post] flattenArgs(list[MuExp] args){
    if(!isEmpty(args) && any(arg <- args , shouldFlatten(arg))){
        pre = [];
        newArgs = [];
        auxVars = [];
        for(MuExp arg <- args){
            if(muValueBlock(_, elems) := arg){
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
                 newArgs += muAssign(var, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(muVarInit(MuExp var, MuExp exp) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([exp]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muVarInit(var, size(post1) == 1 ? post1[0] : muValueBlock(avalue(), post1));
            } else if(muConInit(MuExp var, MuExp exp) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([exp]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muConInit(var, size(post1) == 1 ? post1[0] : muValueBlock(avalue(), post1));
            } else if(muInsert(AType tp, MuExp exp) := arg){
                <fl1, aux1, pre1, post1> = flattenArgs([exp]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muInsert(tp, size(post1) == 1 ? post1[0] : muValueBlock(avalue(), post1));
            //} else if (muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor) := arg){
            //   ;  
            } else if(muSetField(AType resultType, AType baseType, MuExp baseExp, value fieldIdentity, MuExp repl) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([repl]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muSetField(resultType, baseType, baseExp, fieldIdentity, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(me: muEnter(_, _) := arg){
                nauxVars += 1;
                aux = muTmpIValue("$aux<nauxVars>", "xxx", abool());
                auxVars += muVarInit(aux, muCon(false));
                pre += muAssign(aux, me);
                newArgs += aux;
            } else if(muIfElse(_, thenPart, elsePart) := arg){
                <flCond, auxCond, preCond, postCond> = flattenArgs([thenPart]);
                <flThen, auxThen, preThen, postThen> = flattenArgs([thenPart]);
                <flElse, auxElse, preElse, postElse> = flattenArgs([elsePart]);
                if(preCond != preThen && preCond != preElse) pre += preCond;
                pre += preThen + preElse;
                //pre += preCond + preThen + preElse;
                newArgs += muIfExp(size(postCond) == 1 ? postCond[0] : muValueBlock(avalue(), postCond), 
                                   size(postThen) == 1 ? postThen[0] : muValueBlock(avalue(), postThen), 
                                   size(postElse) == 1 ? postElse[0] : muValueBlock(avalue(), postElse));
            } else if(me: muWhileDo(str _, MuExp _, MuExp _) := arg){
                nauxVars += 1;
                aux = muTmpIValue("$aux<nauxVars>", "xxx", alist(avalue()));
                auxVars += muVarInit(aux, muCon([]));
                pre += muAssign(aux, me);
                newArgs += aux;
               
            } else if(muIfExp(cond, thenPart, elsePart) := arg){
                <flThen, auxThen, preThen, postThen> = flattenArgs([thenPart]);
                <flElse, auxElse, preElse, postElse> = flattenArgs([elsePart]);
                pre += preThen + preElse;
                newArgs += muIfExp(cond, size(postThen) == 1 ? postThen[0] : muValueBlock(avalue(), postThen), 
                                         size(postElse) == 1 ? postElse[0] : muValueBlock(avalue(), postElse));
            } else {
                newArgs += arg;
            }
        }
        return <true, auxVars, pre, newArgs>;
    } else {
       return <false, [], [], args>;
    }
}

MuExp ifElse2ifExp(muIfElse(cond, thenPart, elsePart)) = muIfExp(cond, thenPart, elsePart);
default MuExp ifElse2ifExp(MuExp e) = e;
 
//MuExp muCall(MuExp fun, AType t, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs) 
//    = muValueBlock(t, auxVars + pre + muCall(fun, t, flatArgs, kwargs))
//when <true, auxVars, pre, flatArgs> := flattenArgs(args) && !isEmpty(pre);

AType getResultType(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = ret;
AType getResultType(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = adt;
AType getResultType(overloadedAType(rel[loc, IdRole, AType] overloads)) = lubList(toList(overloads<2>));
default AType getResultType(AType t) = t;
     
MuExp muOCall(MuExp fun, AType atype, list[MuExp] args, lrel[str kwpName, MuExp exp] kwargs, loc src)
    = muValueBlock(getResultType(atype), auxVars + pre + muOCall(fun, atype, flatArgs, kwargs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args), !isEmpty(pre);

MuExp muPrim(str op, AType result, list[AType] details, list[MuExp] args, loc src)
    = muValueBlock(result, auxVars + pre + muPrim(op, result, details, flatArgs, src))
when <true, auxVars, pre, flatArgs> := flattenArgs(args) && !isEmpty(pre);

MuExp muCallJava(str name, str class, AType funType, list[MuExp] args, str enclosingFun)
    = muValueBlock(funType.ret, auxVars + pre + muCallJava(name, class, funType, flatArgs, enclosingFun))
when <true, auxVars, pre, flatArgs> := flattenArgs(args) && !isEmpty(pre);

MuExp muKwpActuals(lrel[str kwpName, MuExp exp] kwpActuals)
    = muValueBlock(avalue(), auxVars + pre + muKwpActuals([<kwpActuals[i].kwpName, flatArgs[i]> | int i <- index(kwpActuals)])) // TODO: make type more precise
    when <true, auxVars, pre, flatArgs> := flattenArgs(kwpActuals<1>) && !isEmpty(pre);
      
MuExp muKwpMap(lrel[str kwName, AType atype, MuExp defaultExp] defaults)
    = muValueBlock(avalue(), auxVars + pre + muKwpMap([<dflt.kwName, dflt.atype, flatArgs[i]> | int i <- index(defaults), dflt := defaults[i]])) // TODO: make type more precise
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
       
//MuExp muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
//    = muValueBlock(resultType, auxVars + pre + muSetAnno(exp, resultType, annoName, flatArgs[0]))
//    when <true, auxVars, pre, flatArgs> := flattenArgs([repl]) && !isEmpty(pre);      

MuExp muInsert(AType t, MuExp arg)
    = muValueBlock(t, auxVars + pre + muInsert(t, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([arg]);
    
// muVisit
MuExp muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
   = muValueBlock(avalue(), auxVars + pre + muVisit(visitName, flatArgs[0], cases, defaultExp, vdescriptor))
    when <true, auxVars, pre, flatArgs> := flattenArgs([subject]) && !isEmpty(pre);

//muSwitch
MuExp muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)        // switch over cases for specific value
    = muValueBlock(avalue(), auxVars + pre + muSwitch(label, flatArgs[0], cases, defaultExp, useConcreteFingerprint)) //TODO: make type more precise
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);

//muThrow

MuExp muThrow(muValueBlock(AType t, list[MuExp] exps), loc src)
    = muValueBlock(t, exps[0..-1] + muThrow(exps[-1], src));

//muTry

MuExp muTreeAppl(MuExp prod, muValueBlock(AType _, [*MuExp pre, MuExp last]), loc src)
   = muValueBlock(aadt("Tree",[],dataSyntax()), [*pre, muTreeAppl(prod, last, src)]);
   
MuExp muTreeAppl(MuExp prod, args:[*_, muValueBlock(AType _, [*MuExp _, MuExp _]), *_], loc src) {
   preWork = [];
   results = for(a <- args) {
      if (muValueBlock(AType _, [*MuExp block, MuExp last]) := a) {
        preWork += block;
        append last;
      }
      else {
        append a;
      }
   }
   
   return muValueBlock(aadt("Tree",[],dataSyntax()), [*preWork, muTreeAppl(prod, results, src)]);
}
   
MuExp muGetField(AType resultType, AType baseType, muValueBlock(AType _, [*MuExp pre, MuExp last]), str fieldName)
    = muValueBlock(resultType, [*pre, muGetField(resultType, baseType, last, fieldName)]);


MuExp muGetKwField(AType resultType, AType baseType, muValueBlock(AType _, [*MuExp pre, MuExp last]), str fieldName, str moduleName)
   = muValueBlock(resultType, [*pre, muGetKwField(resultType, baseType, last, fieldName, moduleName)]);

MuExp muSetField(AType resultType, AType baseType, muValueBlock(AType _, [*MuExp pre, MuExp last]), value fieldIdentity, MuExp repl)
   = muValueBlock(resultType, [*pre, muSetField(resultType, baseType, last, fieldIdentity, repl)]);

MuExp muValueIsSubtypeOf(MuExp exp, AType tp) = muCon(true) when !isVarOrTmp(exp) && exp has atype && exp.atype == tp;
MuExp muValueIsComparable(MuExp exp, AType tp) = muCon(true) when !isVarOrTmp(exp) && exp has atype && exp.atype == tp;

MuExp muTemplateAdd(MuExp template, AType atype, MuExp exp)
   = muValueBlock(astr(), auxVars + pre + muTemplateAdd(template, atype, flatArgs[0]))
   when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);