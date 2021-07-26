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
          
          | muFun(loc uid, AType atype)                         // *muRascal* function constant: functions at the root
           
          //| muOFun(str name, AType atype)  
          | muOFun(list[loc] uids, AType atype)                         // *Rascal* function, i.e., overloaded function at the root
          
          | muConstr(AType ctype) 					        	// Constructor
          
          | muComposedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType)
          | muAddedFun(MuExp left, MuExp right, AType leftType, AType rightType, AType resultType)
          //| muConstrCompanion(str fuid)                       // Companion function for constructor with keyword parameters
          
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
          | muTreeGetProduction(MuExp tree)
          | muTreeGetArgs(MuExp tree)
          | muGetKwp(MuExp var, AType atype, str kwpName)
          | muHasKwp(MuExp var, str kwpName)
          
          | muInsert(AType atype, MuExp exp)				    // Insert statement
              
          // Get and assign values
          
          | muAssign(MuExp var, MuExp exp)                      // Assign a value to a variable
          | muVarInit(MuExp var, MuExp exp)                     // Introduce variable and assign a value to it
          | muConInit(MuExp var, MuExp exp)                     // Create a constant
          | muVarDecl(MuExp var)                                 // Introduce a variable
          
          | muGetAnno(MuExp exp, AType resultType, str annoName)
          | muGuardedGetAnno(MuExp exp, AType resultType, str annoName)
          //| muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
          
          // Fields of data constructors
          | muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGuardedGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName)
          | muGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName)
          | muGuardedGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName)

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
          | muSucceed(str btscope)                            // Succeed in current backtracking scope
          | muFail(str label)                                 // Fail in current backtracking scope                      
          
          //  Visit
          | muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
          | muDescendantMatchIterator(MuExp subject, DescendantDescriptor ddescriptor)
          | muSucceedVisitCase(str visitName)                 // Marks a success exit point of a visit case
        
          // Switch
          | muSwitch(str switchName, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)		// switch over cases for specific value
          
          | muFailCase(str switchName)                        // Marks the failure exit point of a switch or visit case
          | muSucceedSwitchCase(str switchName)               // Marks a success exit point of a switch case
                 
           // Multi-expressions
          | muBlock(list[MuExp] exps)                         // A list of expressions that does not deliver a value
          | muValueBlock(AType result, list[MuExp] exps)  	  // A list of expressions, only last value remains
                                                             
          // Exceptions
          
          | muThrow(MuExp exp, loc src)
          | muBuiltinRuntimeExceptionThrow(str exceptionName, list[MuExp] args)
          | muTry(MuExp exp, MuCatch \catch, MuExp \finally)
          
          // Auxiliary operations used in generated code
          
          // Various tests
          | muRequireNonNegativeBound(MuExp bnd)              // Abort if exp is false
          | muEqual(MuExp exp1, MuExp exp2)    
          | muMatch(MuExp exp1, MuExp exp2)                     // equal that ignores keyword parameters
          
          | muHasTypeAndArity(AType atype, int arity, MuExp exp)
          | muHasNameAndArity(AType atype, AType consType, MuExp nameExp, int arity, MuExp exp)
          | muValueIsSubType(MuExp exp, AType tp)
          | muValueIsComparable(MuExp exp, AType tp)
          | muValueIsSubTypeOfValue(MuExp exp2, MuExp exp1)
          | muIsDefinedValue(MuExp exp)
          | muGetDefinedValue(MuExp exp, AType tp)
          | muHasField(MuExp exp, AType tp, str fieldName, set[AType] consesWithField)
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
          | muToNativeInt(MuExp exp)
          
          // Operations on native booleans
          | muAndNativeBool(MuExp exp1, MuExp exp2)
          | muNotNativeBool(MuExp exp)
        //  | muNotNegativeNativeInt(MuExp exp)
          | muSubList(MuExp lst, MuExp from, MuExp len)
          
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

MuExp muVar(str name, str fuid, int pos, AType atype){
    assert !isEmpty(name);
    fail;
}

MuExp muTmpIValue(str name, str fuid, AType atype){
    assert !isEmpty(name);
    fail;
}

MuExp muTmpNative(str name, str fuid, NativeKind nkind) {
    assert !isEmpty(name);
    fail;
}
             
MuExp muVarKwp(str name, str fuid, AType atype) {
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
        return isMainName(fun.name) ? fun.name : fun.uniqueName;
    }
    return /*isClosureName(fun.name) ? fun.uniqueName : */"<fun.scopeIn>_<fun.uniqueName>";
    //return fun.uniqueName;
}

set[str] varExp = {"muModuleVar", "muVar", "muTmpIValue", "muTmpNative"};

bool isVarOrTmp(MuExp exp)
    = getName(exp) in varExp;

// Produces NativeBool
   
bool producesNativeBool(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] args, loc src)){
    if(name in {/*"equal", "notequal",*/"is", "subset"}) return true;
    fail producesNativeBool;
}

bool producesNativeBool(muTmpNative(_,_,nativeBool()))
    = true;
    
bool producesNativeBool(muIfExp(MuExp cond, MuExp thenExp, MuExp elseExp))
    = producesNativeBool(thenExp) && producesNativeBool(elseExp);
    
default bool producesNativeBool(MuExp exp)
    = getName(exp) in {"muEqual", "muMatch", "muEqualNativeInt", /*"muNotNegativeNativeInt",*/ "muIsKwpDefined", "muHasKwp", "muHasKwpWithValue", /*"muHasType",*/ "muHasTypeAndArity",
                  "muHasNameAndArity", "muValueIsSubType", "muValueIsComparable", "muValueIsSubTypeOfValue", "muLessNativeInt", "muGreaterEqNativeInt", "muAndNativeBool", "muNotNativeBool",
                  "muRegExpFind",  "muIsDefinedValue", "muIsInitialized", "muHasField"};

// Produces NativeInt

bool producesNativeInt(muTmpNative(_,_,nativeInt()))
    = true;
                     
default bool producesNativeInt(MuExp exp)
    = getName(exp) in {"muSize", "muAddNativeInt", "muSubNativeInt", "muToNativeInt", "muRegExpBegin", "muRegExpEnd"};
    
// Produces nativeStr

default bool producesNativeStr(MuExp exp)
    = getName(exp) in {"muTreeUnparse"};


// Produces NativeGuardedIValue

bool producesNativeGuardedIValue(muTmpNative(_,_,nativeGuardedIValue))
    = true;   
 
 bool producesNativeGuardedIValue(muCallPrim3(str name, AType result, list[AType] details, list[MuExp] exps, loc src))
    = name in { "guarded_subscript", "guarded_field_project"};
    
default bool producesNativeGuardedIValue(MuExp exp)
   = getName(exp) in {"muGuardedGetAnno", "muGuardedGetField", "muGuardedGetKwField"};
    
// Get the result type of a MuExp
AType getType(muCon(bool b)) = abool();
AType getType(muCon(int n)) = aint();
AType getType(muCon(real r)) = areal();
AType getType(muCon(rat q)) = arat();
AType getType(muCon(loc l)) = aloc();
AType getType(muCon(str s)) = astr();
AType getType(muCon(datetime dt)) = adatetime();
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

AType getType(muGetKwFieldFromConstructor(AType resultType, MuExp var, str fieldName)) = resultType;
AType getType(muGetFieldFromConstructor(AType resultType, AType consType, MuExp var, str fieldName)) = resultType;

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

default AType getType(MuExp exp) = avalue();
         
// ==== Simplification rules ==================================================

// ---- exitViaReturn --------------------------------------------------------
// All control path end with a return

bool exitViaReturn(MuExp exp){
    res = exitViaReturn1(exp);
    //println("<exp> ==\> <res>");
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

bool exitViaReturn1(muFail(str label)) 
    = false;

bool exitViaReturn1(muSucceed(str label)) 
    = false; 
    
bool exitViaReturn1(muBreak(str label)) {
    return false;  
}    
bool exitViaReturn1(muContinue(str label)) {
    return false;    
}   
bool exitViaReturn1(muEnter(str enter, MuExp exp)){
    return exitViaReturn(exp);  
}

bool exitViaReturn1(muBlock([*exps1, exp2])) 
    = exitViaReturn(exp2);
    
bool exitViaReturn1(muValueBlock(AType t, [*exps1, exp2])) 
    = exitViaReturn(exp2);
    
bool exitViaReturn1(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    = exitViaReturn(thenPart) && exitViaReturn(elsePart);
    
bool exitViaReturn1(muWhileDo(str label, muCon(true), MuExp body)){
    return exitViaReturn(body);
}    
bool exitViaReturn1(muDoWhile(str label,  MuExp body, muCon(false)))
    = exitViaReturn(body);  
    
bool exitViaReturn1( muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)) {
    return all(c <- cases, exitViaReturn(c.exp)) && exitViaReturn(defaultExp);
}

bool exitViaReturn1( muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)) {
    return all(c <- cases, exitViaReturn(c.exp)) && exitViaReturn(defaultExp);
}

bool exitViaReturn1(muTry(MuExp exp, MuCatch \catch, MuExp \finally))
    = exitViaReturn(exp) && exitViaReturn(\catch.body) || exitViaReturn(\finally);

bool exitViaReturn1(muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body)) 
    = exitViaReturn(body);
    
default bool exitViaReturn1(MuExp exp) 
    = false;

// ---- noSequentialExit ------------------------------------------------------

bool hasSequentialExit(MuExp exp) = !noSequentialExit(exp);

// There is no sequential exit from this expression

bool noSequentialExit(MuExp exp)
    = noSequentialExit(exp, []);

bool noSequentialExit(muFail(str label), list[str] entered) 
    = true; //label notin entered;

bool noSequentialExit(muFailCase(_,_), list[str] entered)
    = false;

bool noSequentialExit(muSucceedSwitchCase(str switchName), list[str] entered)
    = false;
      
bool noSequentialExit(muSucceed(str label), list[str] entered) 
    = true; //label in entered; 
    
bool noSequentialExit(muBreak(str label), list[str] entered) {
    return true; //label in entered;  
}    
bool noSequentialExit(muContinue(str label), list[str] entered) {
    return true; //label in entered;    
}   

bool noSequentialExit(muReturn0(), list[str] entered) 
    = true;
bool noSequentialExit(muReturn1(_, _), list[str] entered) 
    = true;

bool noSequentialExit(muInsert(t, exp1), list[str] entered)
    = true;
 
bool noSequentialExit(muFailReturn(_), list[str] entered) 
    = true;
bool noSequentialExit(muCheckMemo(_,_,_), list[str] entered) 
    = true;
bool noSequentialExit(muMemoReturn0(_,_), list[str] entered) 
    = true;
bool noSequentialExit(muMemoReturn1(_,_,_), list[str] entered) 
    = true;
bool noSequentialExit(muThrow(_,_), list[str] entered) 
    = true;

bool noSequentialExit(muEnter(str enter, MuExp exp), list[str] entered){
    return exitViaReturn(exp); //noSequentialExit(exp, entered);  //exitViaReturn(exp); //false; //noSequentialExit(exp, entered);  
}
    
bool noSequentialExit(muBlock([*exps1, exp2]), list[str] entered) 
    = noSequentialExit(exp2, entered);
    
bool noSequentialExit(muValueBlock(AType t, [*exps1, exp2]), list[str] entered) 
    = noSequentialExit(exp2, entered);
    
bool noSequentialExit(muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart), list[str] entered)
    = noSequentialExit(thenPart, entered) && noSequentialExit(elsePart, entered);
    
bool noSequentialExit(muWhileDo(str label, muCon(true), MuExp body), list[str] entered){
    return false; //noSequentialExit(body, label + entered);
}    
bool noSequentialExit(muDoWhile(str label,  MuExp body, muCon(false)), list[str] entered)
    = noSequentialExit(body, label + entered);  
    
bool noSequentialExit( muSwitch(str switchName, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint), list[str] entered) {
    return any(c <- cases, noSequentialExit(c.exp, entered)) || noSequentialExit(defaultExp, entered);
}

bool noSequentialExit( muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor), list[str] entered) {
    return any(c <- cases, noSequentialExit(c.exp, entered)) || noSequentialExit(defaultExp, entered);
}

bool noSequentialExit(muTry(MuExp exp, MuCatch \catch, MuExp \finally), list[str] entered)
    = noSequentialExit(exp, entered) && noSequentialExit(\catch.body, entered) && noSequentialExit(\finally, entered);

bool noSequentialExit(muCatch(MuExp thrown_as_exception, MuExp thrown, MuExp body), list[str] entered) 
    = noSequentialExit(body, entered);
    
default bool noSequentialExit(MuExp exp, list[str] entered) 
    = false;

// ---- muFail ----------------------------------------------------------------

//MuExp muFail(str label) = muFailCase()
//    when /^CASE[0-9]+$/ := label;

// ---- block -----------------------------------------------------------------

MuExp muBlock([MuExp exp]) = exp;

MuExp muBlock([ *exps1, muBlock([*exps2]), *exps3 ])
    = muBlock([ *exps1, *exps2, *exps3 ]);
    
//MuExp muBlock([ *exps1, muReturn1(t, exp), *exps2 ])
//    = muBlock([ *exps1, muReturn1(t, exp) ])
//    when !isEmpty(exps2); 
//    
//MuExp muBlock([ *exps1, muInsert(t, exp), *exps2])
//    = muBlock([*exps1, muInsert(t, exp)])
//    when !isEmpty(exps2); 
    
MuExp muBlock([ *exps1, exp0, *exps2])
    = muBlock([*exps1, exp0])
    when !isEmpty(exps2),
        muInsert(t, exp1) := exp0 ||
        noSequentialExit(exp0);
         
MuExp muBlock([*MuExp pre, muValueBlock(AType t, list[MuExp] elems), *MuExp post])
    = muBlock([*pre, *elems, *post]);

MuExp muBlock([*MuExp pre, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart), *MuExp post])
    = muBlock([*pre, muIfelse(cond, thenPart, elsePart), *post]);
    
// ---- muValueBlock ----------------------------------------------------------

MuExp muValueBlock(AType t, [MuExp exp]) = exp;
    
MuExp muValueBlock(AType t, [*MuExp pre, muBlock(list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t, [*pre, *elems, *post, last]);
    
MuExp muValueBlock(AType t1, [*MuExp pre, muValueBlock(AType t2, list[MuExp] elems), *MuExp post, MuExp last])
    = muValueBlock(t2, [*pre, *elems, *post, last]);

MuExp muValueBlock(AType t1, [*MuExp pre, muReturn1(AType t2, MuExp exp)])
    = muBlock([*pre, muReturn1(t2, exp)]);
    
 //MuExp muValueBlock(AType t, [*MuExp pre, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)])
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
    //if(isEmpty(exps)){
    //    fail; 
    //} else 
    //if(muEnter(_,_) := exps[-1]){
    //     x = exps[0..-1];
    //     return muBlock([*exps[0..-1], muReturn1(t, exps[-1]), muReturn1(t, exp)]);
    //} else {
        return muBlock([*exps, muReturn1(t, exp)]);
   // }
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

MuExp muReturn1(AType t, muIfEqualOrAssign(MuExp var, MuExp other, MuExp body)){
    return muReturn1(t, muEqual(var, other));
}
    
MuExp muReturn1(AType t, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart)){
    return muIfelse(cond, muReturn1(t, thenPart), muReturn1(t, elsePart));
}

MuExp muReturn1(AType t, muIf(MuExp cond, MuExp thenPart)){
    return  muIf(cond, muReturn1(t, thenPart));
}
    
MuExp muReturn1(AType t, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)){
    return muIfelse(cond,muReturn1(t, thenPart), muReturn1(t, elsePart));
}
    
MuExp muReturn1(AType t, muWhileDo(str label, MuExp cond, MuExp body)){
    return  muWhileDo(label, cond, muReturn1(t, body));
}
    
//MuExp muReturn1(AType t, muDoWhile(str label, MuExp body, MuExp cond)){
//    return  muDoWhile(label, muReturn1(t, body), cond);
//    }

//MuExp muReturn1(AType t, muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp))
//    = muForRangeInt(label, var, ifirst, istep, last, muReturn1(t, exp));
//    
//MuExp muReturn1(AType t, fo: muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body))
//    = addReturnFalse(t, fo);

MuExp muReturn1(AType t, muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)){
    dflt = muReturn1(t, defaultExp);
    //if(any(c <- cases, !exitWithReturn(c))){
    //    dflt = muIfthen(execDefault, dflt);
    //}
    return muSwitch(label, exp, [muCase(c.fingerprint, muReturn1(t, c.exp)) | c <- cases], dflt, useConcreteFingerprint);
}
//
//MuExp muReturn1(abool(), muSucceed(str label))
//    = muReturn1(abool(), muCon(true));
    
private MuExp addReturn(AType t, bool result, bl: muBlock([*exps, muReturn1(abool(), muCon(result))])) = bl;

private default MuExp addReturn(AType t, bool result, MuExp exp) = muBlock([exp, (t == abool() || t == avalue()) ? muReturn1(abool(), muCon(result)) : muFailReturn(t)]);

MuExp insertReturn(MuExp exp, list[str] entered, bool asBool){
    return
          top-down-break visit(exp) { 
                case muSucceed(enter): {
                    //println("enter: <enter>");
                    //println("entered[-1]: <entered[-1]>");
                    if(enter != entered[-1]) fail;
                        insert muReturn1(abool(), muCon(true));
                    }
                case muFail(enter) => asBool ? {
                    muReturn1(abool(), muCon(false));
                    } : muFailReturn(t) when enter == entered[-1] //<<<
                case muEnter(str enter1, MuExp exp1) => muEnter(enter1, insertReturn(exp1, enter1 + entered, asBool))
//                case muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart) => 
//                      muIfelse(cond, insertReturn(thenPart, entered, asBool), insertReturn(elsePart, entered, asBool))
//                case muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart) => 
//                      muIfExp(cond, insertReturn(thenPart, entered, asBool), insertReturn(elsePart, entered, asBool))
//
                case muWhileDo(str enter1, MuExp cond, MuExp body) => muWhileDo(enter1, cond, insertReturn(body, enter1 + entered, asBool)) //when enter1 in entered
                case muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body):{ 
                     //if(label notin entered) fail;
                     insert muForAll(label, var, iterType, iterable, insertReturn(body, label + entered, asBool));
                     }
                case muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp2) =>
                     muForRange(label, var, first, second, last, insertReturn(exp2, label + entered, asBool)) when label in entered
                case muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp2) =>
                     muForRangeInt(label, var, ifirst, istep, last, insertReturn(exp2, label + entered, asBool)) when label in entered
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
        case muEnter(str enter1, MuExp exp1) => muEnter(enter1, removeDeadCode(exp1, enter1 + entered))
        case muWhileDo(str enter1, MuExp cond, MuExp body) => muWhileDo(enter1, cond, removeDeadCode(body, enter1 + entered))
        case muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint) =>
             muSwitch(label, exp, [muCase(c.fingerprint, removeDeadCode(c.exp, entered)) | c <- cases], removeDeadCode(defaultExp, entered), useConcreteFingerprint)
        //case muCase(int fingerPrint, muBlock([*MuExp pre, MuExp exp2, muSucceedSwitchCase(_)])) => muCase(fingerPrint, muBlock([*pre, exp2]))
        //    when noSequentialExit(exp2, entered)
        case muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body) => 
             muForAll(label, var, iterType, iterable, removeDeadCode(body, /*label + */entered)) when label in entered
        case muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp2) =>
             muForRange(label, var, first, second, last, removeDeadCode(exp2, label + entered)) when label in entered
        case muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp2) =>
             muForRangeInt(label, var, ifirst, istep, last, removeDeadCode(exp2, label + entered)) when label in entered    
    }
    //println("After removeDeadCode:"); iprintln(res);
    return res;
}

//MuExp negate(list[str] entered, MuExp exp){
//    return visit(exp) { 
//                case muSucceed(enter1) => muFail(enter1) when enter1 in entered
//                case muFail(enter1) => muSucceed(enter1) when enter1 in entered
//                //case muReturn1(abool(), muCon(bool b)) => muReturn1(abool(), muCon(!b))
//            };
//}
MuExp negate(MuExp exp, list[str] entered){
    res =  top-down-break visit(exp){
            case muEnter(str enter1, MuExp exp1) => muEnter(enter1, negate(exp1, enter1 + entered))
            case muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body) => 
                  muForAll(label, var, iterType, iterable, negate(body, entered))
            
            case muSucceed(enter1) => muFail(enter1) when enter1 in entered
            case muFail(enter1) => muSucceed(enter1) when enter1 in entered
          };
    return res;
}


MuExp muReturn1(AType t, me:muEnter(str btscope, MuExp exp)){
    //return muEnter(btscope, muReturn1(t, exp));
    res = muReturn1(t, insertReturn(exp, [btscope], t == abool() || t == avalue()));
    //iprintln(res);
    res = noSequentialExit(res) ? res : addReturn(t, false, res); 
    return muEnter(btscope, res);
} 

MuExp muReturn1(AType t, mf:muForAll(str label, MuExp var, AType iterType, MuExp iterable, MuExp body)){
    return muForAll(label, var, iterType, iterable, muReturn1(t, body));
    //return muForAll(label, var, iterType, iterable, insertReturn(body, [label], t == abool() || t == avalue()));
}

MuExp muReturn1(AType t, mf:muForRange(str label, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp)){
   return muForRange(label, var, first, second, last, muReturn1(t, exp)); 
   //return muForRange(label, var, first, second, last, insertReturn(exp, [label], t == abool() || t == avalue())); 
}

MuExp muReturn1(AType t, muForRangeInt(str label, MuExp var, int ifirst, int istep, MuExp last, MuExp exp)){
   return muForRangeInt(label, var, ifirst, istep, last, muReturn1(t, exp));
   //return muForRangeInt(label, var, ifirst, istep, last, insertReturn(exp, [label], t == abool() || t == avalue()));
}
    
MuExp muReturn1(AType t, muSucceed(str btscope))
    = muSucceed(btscope);
    

MuExp muReturn1(AType t, muFail(str btscope)){
    return muFail(btscope);
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
                case muEnter(str enter1, MuExp exp1) => muEnter(enter1, insertAssignBool(var, exp1, entered + enter1))
                case muForAll(str label, MuExp var2, AType iterType, MuExp iterable, MuExp body) =>
                     muForAll(label, var2, iterType, iterable, insertAssignBool(var, body, entered + label))
                case muForRange(str label, MuExp var2, MuExp first, MuExp second, MuExp last, MuExp exp2) =>
                     muForRange(label, var2, first, second, last, insertAssignBool(var, exp2, entered + label))
                case muForRangeInt(str label, MuExp var2, int ifirst, int istep, MuExp last, MuExp exp2) =>
                     muForRangeInt(label, var2, ifirst, istep, last, insertAssignBool(var, exp2, entered + label))
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

MuExp muConInit(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muBlock([muVarDecl(var), muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);
    
MuExp muVarInit(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muBlock([muVarDecl(var), muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart))]);
    
MuExp muAssign(MuExp var, muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muIfelse(cond, muAssign(var, thenPart), muAssign(var, elsePart));   
    
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

    
MuExp muVarInt(MuExp var, muFail(btscope))
    = muBlock([muVarInit(var, muCon(false))]);   
     
MuExp muAssign(MuExp var, muFail(btscope))
    = muBlock([muAssign(var, muCon(false))]);
    
// ---- muIfthen ---------------------------------------------------------------
    
MuExp muIfthen(muCon(true), MuExp thenPart) = thenPart;

MuExp muIfthen(muCon(false), MuExp thenPart) = muBlock([]);

MuExp muIfthen(me:muEnter(enter, cond), MuExp thenPart)   
      = muEnter(enter, insertThenPart(cond, thenPart, {enter}));

// ---- muIfExp ---------------------------------------------------------------

MuExp muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart)
    = muIfelse(cond, thenPart, elsePart)
    when noSequentialExit(thenPart) || noSequentialExit(elsePart) || muBlock(_) := thenPart || muBlock(_) := elsePart;

MuExp muIfExp(MuExp cond, MuExp thenPart, muFailReturn(AType t))
    = muIfelse(cond, thenPart, muFailReturn(t));
    
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
    

// ---- muIfelse --------------------------------------------------------------

MuExp insertThenPart(MuExp exp, MuExp thenPart, set[str] entered){
    return top-down-break visit(exp) { 
                case muSucceed(enter) => muBlock([thenPart, muSucceed(enter)]) when enter in entered
                //case muFail(enter) => muBlock([muAssign(var, muCon(false)), muFail(enter)])
                case muEnter(str enter1, MuExp exp1) => muEnter(enter1, insertThenPart(exp1, thenPart, entered))
                case muForAll(str label, MuExp var2, AType iterType, MuExp iterable, MuExp body) =>
                     muForAll(label, var2, iterType, iterable, insertThenPart(body, thenPart, entered))
                case muForRange(str label, MuExp var2, MuExp first, MuExp second, MuExp last, MuExp exp2) =>
                     muForRange(label, var2, first, second, last, insertThenPart(exp2, thenPart, entered))
                case muForRangeInt(str label, MuExp var2, int ifirst, int istep, MuExp last, MuExp exp2) =>
                     muForRangeInt(label, var2, ifirst, istep, last, insertThenPart(exp2, thenPart, entered))
            };
}

//MuExp muIfelse(MuExp cond, MuExp thenPart, MuExp elsePart) = thenPart when thenPart == elsePart;
//MuExp muIfelse(muCon(true), MuExp thenPart, MuExp elsePart) = thenPart
//    when /muFail(_) !:= thenPart;
//MuExp muIfelse(muCon(false), MuExp thenPart, MuExp elsePart) = elsePart;

//MuExp muIfelse(MuExp cond, muCon(_), muCon(_)) = muBlock([]);

MuExp muIfelse(MuExp cond, MuExp thenPart, muBlock([])) = muIf(cond, thenPart);

MuExp muIfelse(MuExp cond, muIfExp(cond1, thenPart1, elsePart1), MuExp elsePart) 
    = muIfelse(cond, muIfelse(cond1, thenPart1, elsePart1), elsePart);
    
MuExp muIfelse(MuExp cond, MuExp thenPart, muIfExp(cond1, thenPart1, elsePart1)) 
    = muIfelse(cond, thenPart, muIfelse(cond1, thenPart1, elsePart1));
    
MuExp muIfelse(muValueBlock(AType t, [*MuExp exps, MuExp exp]), MuExp thenPart, MuExp elsePart)
    = muBlock([*exps, muIfelse(exp, thenPart, elsePart)]); 

MuExp muIfelse(me:muEnter(enter, cond), MuExp thenPart, MuExp elsePart)   
      = muBlock([muEnter(enter, insertThenPart(cond, thenPart, {enter})), elsePart]);
              
MuExp muIfelse(muForAll(str label, MuExp var2, AType iterType, MuExp iterable, MuExp body), MuExp thenPart, MuExp elsePart)
    = muBlock([muForAll(label, var2, iterType, iterable, insertThenPart(body, thenPart, {label})), elsePart]);  
    
MuExp muIfelse(muForRange(str label, MuExp var2, MuExp first, MuExp second, MuExp last, MuExp exp2), MuExp thenPart, MuExp elsePart)
        = muBlock([muForRange(label, var2, first, second, last, insertThenPart(exp2, thenPart, {label})), elsePart]);
    
MuExp muIfelse(muForRangeInt(str label, MuExp var2, int ifirst, int istep, MuExp last, MuExp exp2), MuExp thenPart, MuExp elsePart)
        = muBlock([muForRangeInt(label, var2, ifirst, istep, last, insertThenPart(exp2, thenPart, {label})), elsePart]);

// ---- muEnter ---------------------------------------------------------------

MuExp muEnter(str btscope, MuExp exp=false)
    = muEnter(btscope, exp);

MuExp muEnter(str btscope, muIfExp(MuExp cond, MuExp thenPart, MuExp elsePart))
    =  muEnter(btscope, muIfelse(cond, thenPart, elsePart));
    
MuExp muEnter(str enter, MuExp exp) = exp when containsEnter(enter, exp);

bool containsEnter(str enter, MuExp exp){
    visit(exp){
        case muEnter(enter, MuExp exp1): return true;
        case muForAll(enter, MuExp var, AType iterType, MuExp iterable, MuExp body): return true;
        case muForRange(enter, MuExp var, MuExp first, MuExp second, MuExp last, MuExp exp1): return true;
        case muForRangeInt(enter, MuExp var, int ifirst, int istep, MuExp last, MuExp exp1): return true;
    }
    return false;
}
    
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
       || muInsert( AType tp, MuExp exp) := arg
       //|| muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl) := arg
       || muSetField(AType resultType, AType baseType, MuExp baseExp, value fieldIdentity, MuExp repl) := arg
       || muEnter(btscope, exp) := arg 
       || muIfelse(cond, thenPart, elsePart) := arg 
       || muWhileDo(str ab, MuExp cond, MuExp body) := arg
       || muBlock(elems) := arg 
       //|| muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor) := arg
       || muIfExp(cond, thenPart, elsePart) := arg && (shouldFlatten(thenPart) || shouldFlatten(elsePart))
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
            //} else if(muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl) := arg){
            //     <fl1, aux1, pre1, post1> = flattenArgs([repl]);
            //     auxVars += aux1;
            //     pre += pre1;
            //     newArgs += muSetAnno(exp, resultType, annoName, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(muSetField(AType resultType, AType baseType, MuExp baseExp, value fieldIdentity, MuExp repl) := arg){
                 <fl1, aux1, pre1, post1> = flattenArgs([repl]);
                 auxVars += aux1;
                 pre += pre1;
                 newArgs += muSetField(resultType, baseType, baseExp, fieldIdentity, size(post1) == 1? post1[0] : muValueBlock(avalue(), post1));
            } else if(me: muEnter(btscope, exp) := arg){
                nauxVars += 1;
                aux = muTmpIValue("$aux<nauxVars>", "xxx", abool());
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
when <true, auxVars, pre, flatArgs> := flattenArgs(args), !isEmpty(pre);

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
       
//MuExp muSetAnno(MuExp exp, AType resultType, str annoName, MuExp repl)
//    = muValueBlock(resultType, auxVars + pre + muSetAnno(exp, resultType, annoName, flatArgs[0]))
//    when <true, auxVars, pre, flatArgs> := flattenArgs([repl]) && !isEmpty(pre);      

MuExp muInsert(AType t, MuExp arg)
    = muValueBlock(t, auxVars + pre + muInsert(t, flatArgs[0]))
    when <true, auxVars, pre, flatArgs> := flattenArgs([arg]);
    
// muVisit
MuExp muVisit(str visitName, MuExp subject, list[MuCase] cases, MuExp defaultExp, VisitDescriptor vdescriptor)
   = muValueBlock(auxVars + pre + muVisit(visitName, flatArgs[0], cases, defaultExp, vdescriptor))
    when <true, auxVars, pre, flatArgs> := flattenArgs([subject]) && !isEmpty(pre);

//muSwitch
MuExp muSwitch(str label, MuExp exp, list[MuCase] cases, MuExp defaultExp, bool useConcreteFingerprint)        // switch over cases for specific value
    = muValueBlock(auxVars + pre + muSwitch(label, flatArgs[0], cases, defaultExp, useConcreteFingerprint))
    when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);

//muThrow

MuExp muThrow(muValueBlock(AType t, list[MuExp] exps), loc src)
    = muValueBlock(t, exps[0..-1] + muThrow(exps[-1], src));


//muTry

MuExp muTreeAppl(MuExp prod, muValueBlock(AType t, [*MuExp pre, MuExp last]), loc src)
   = muValueBlock(aadt("Tree",[],dataSyntax()), [*pre, muTreeAppl(prod, last, src)]);
   
MuExp muTreeAppl(MuExp prod, args:[*pre, muValueBlock(AType t, [*MuExp block, MuExp last]), *post], loc src) {
   preWork = [];
   results = for(a <- args) {
      if (muValueBlock(AType t, [*MuExp block, MuExp last]) := a) {
        preWork += block;
        append last;
      }
      else {
        append a;
      }
   }
   
   return muValueBlock(aadt("Tree",[],dataSyntax()), [*preWork, muTreeAppl(prod, results, src)]);
}
   
MuExp muGetField(AType resultType, AType baseType, muValueBlock(AType t, [*MuExp pre, MuExp last]), str fieldName)
    = muValueBlock(resultType, [*pre, muGetField(resultType, baseType, last, fieldName)]);


MuExp muGetKwField(AType resultType, AType baseType, muValueBlock(AType t, [*MuExp pre, MuExp last]), str fieldName, str moduleName)
   = muValueBlock(resultType, [*pre, muGetKwField(resultType, baseType, last, fieldName, moduleName)]);

MuExp muSetField(AType resultType, AType baseType, muValueBlock(AType t, [*MuExp pre, MuExp last]), value fieldIdentity, MuExp repl)
   = muValueBlock(resultType, [*pre, muSetField(resultType, baseType, last, fieldIdentity, repl)]);

MuExp muValueIsSubType(MuExp exp, AType tp) = muCon(true) when !isVarOrTmp(exp) && exp has atype && exp.atype == tp;
MuExp muValueIsComparable(MuExp exp, AType tp) = muCon(true) when !isVarOrTmp(exp) && exp has atype && exp.atype == tp;

MuExp muTemplateAdd(MuExp template, AType atype, MuExp exp)
   = muValueBlock(astr(), auxVars + pre + muTemplateAdd(template, atype, flatArgs[0]))
   when <true, auxVars, pre, flatArgs> := flattenArgs([exp]) && !isEmpty(pre);

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
