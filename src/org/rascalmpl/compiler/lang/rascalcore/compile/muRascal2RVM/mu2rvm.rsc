module lang::rascalcore::compile::muRascal2RVM::mu2rvm

import IO;
//import Type;
import List;
import Set;
import Map;
//import ListRelation;
//import Node;
import Message;
import String;
import lang::rascalcore::compile::RVM::AST;

import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::Checker;

import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
//import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;
import lang::rascalcore::compile::muRascal2RVM::ToplevelType;
import lang::rascalcore::compile::muRascal2RVM::StackValidator;

import lang::rascalcore::compile::muRascal2RVM::PeepHole;

alias INS = list[Instruction];

// Unique label generator

private int nlabel = -1;
private str nextLabel() { nlabel += 1; return "L<nlabel>"; }
private str nextSeqLabel() { nlabel += 1; return "SEQ<nlabel>"; }

private set[str] usedLabels = {};

private str functionScope = "";                 // scope name of current function, used to distinguish local and non-local variables
private str surroundingFunctionScope = "";      // scope name of surrounding function

public void setFunctionScope(str scopeId){
    functionScope = scopeId;
}

private MuFunction currentFunction;

private map[str,int] nlocal = ();                       // number of local per scope

private map[str,int] minNlocal = ();   

private map[str,str] scopeIn = ();                      // scope nesting

int get_nlocals() = nlocal[functionScope];     

set[str] usedOverloadedFunctions = {};

set[str] usedFunctions = {}; 

void set_nlocals(int n) {
    nlocal[functionScope] = n;
}

FailInfo failInfo = <{}, {}>;

private bool optimize = true;

void init(bool optimizeFlag){
    nlabel = -1;
    usedLabels = {};
    functionScope = "";
    surroundingFunctionScope = "";
    nlocal = ();
    minNLOcal = ();
    scopeIn = ();
    localNames = ();
    temporaries = [];
    //temporaries = ();
    tryBlocks = [];
    finallyBlocks = [];
    catchAsPartOfTryBlocks = [];
    catchBlocks = [[]];
    currentCatchBlock = 0;
    finallyBlock = [];
    exceptionTable = [];
    
    usedOverloadedFunctions = {};
    usedFunctions = {};
    
    optimize = optimizeFlag;
    failInfo = <{}, {}>;
}

// Map names of <fuid, pos> pairs to local variable names ; Note this info could also be collected in Rascal2muRascal

private map[int, str] localNames = ();

// Systematic label generation related to loops

str mkContinue(str loopname) = "CONTINUE_<loopname>";
str mkBreak(str loopname) = "BREAK_<loopname>";
str mkFail(str loopname) = "FAIL_<loopname>";
str mkElse(str branchname) = "ELSE_<branchname>";

// Exception handling: labels to mark the start and end of 'try', 'catch' and 'finally' blocks 
str mkTryFrom(str label) = "TRY_FROM_<label>";
str mkTryTo(str label) = "TRY_TO_<label>";
str mkCatchFrom(str label) = "CATCH_FROM_<label>";
str mkCatchTo(str label) = "CATCH_TO_<label>";
str mkFinallyFrom(str label) = "FINALLY_FROM_<label>";
str mkFinallyTo(str label) = "FINALLY_TO_<label>";

// Manage locals

private int newLocal() {
    n = nlocal[functionScope];
    nlocal[functionScope] = n + 1;
    return n;
}

private int newLocal(str fuid) {
    n = nlocal[fuid];
    nlocal[fuid] = n + 1;
    //println("newLocal:  nlocal[<fuid> = <nlocal[fuid]>");
    return n;
}

// Manage temporaries
private lrel[str name, str fuid, int pos] temporaries = [];

private str asUnwrappedThrown(str name) = name + "_unwrapped";

private int createTmp(str name, str fuid){
   newTmp = minNlocal[fuid] + size(temporaries[_,fuid]);
   
   if(newTmp < nlocal[fuid]){ // Can we reuse a tmp?
      temporaries += <name, fuid, newTmp>;
      return newTmp;
   } else {                    // No, create a new tmp
      newTmp = newLocal(fuid);
      temporaries += <name,fuid, newTmp>;
      return -newTmp;  // Return negative to signal a freshly allocated tmp
   }
}

private int createTmpCoRo(str fuid){
    co = createTmp("CORO", fuid);
    return co >= 0 ? co : -co;
}

private void destroyTmpCoRo(str fuid){
    destroyTmp("CORO", fuid);
}

private int getTmp(str name, str fuid){
   if([*int prev, int n] := temporaries[name,fuid]){
      return n;
   }
   println("*** Unknown temp <name>, <fuid> ***");
   n = createTmp(name, fuid);
   return n >= 0 ? n : -n;
   //throw "Unknown temp <name>, <fuid>";    
}

private void destroyTmp(str name, str fuid){
    if(fuid != functionScope) return;
    if([*int prev, int n] := temporaries[name,fuid]){
       temporaries -= <name, fuid, n>; 
       return;
    }
    throw "Non-existing temp <name>, <fuid>";     
}

INS tr(muBlockWithTmps(lrel[str name, str fuid] tmps, lrel[str name, str fuid] tmpRefs, list[MuExp] exps), Dest d, CDest c) {
    // Create ordinary tmps (they are always initialized in the generated code)
    for(<nm, fd> <- tmps){
        createTmp(nm, fd);
    }
    
    // Create tmps that are used as reference (they may need to be reset here)
    resetCode = [ fd == functionScope ? RESETLOC(pos) : RESETVAR(fd, pos) | <nm, fd> <- tmpRefs, int pos := createTmp(nm, fd) , pos >= 0 ];
    
    code = resetCode +  tr(muBlock(exps), d, c);
    for(<nm, fd> <- tmps + tmpRefs){
        destroyTmp(nm, fd);
    }
    return code;
}

// Does an expression produce a value? (needed for cleaning up the stack)

//bool producesValue(muLab(_)) = false;

bool producesValue(muWhile(str label, MuExp cond, list[MuExp] body)) = false;

bool producesValue(muBreak(_)) = false;
bool producesValue(muContinue(_)) = false;
bool producesValue(muFail(_)) = false;
bool producesValue(muFailReturn()) = false;

bool producesValue(muReturn0()) = false;
bool producesValue(muGuard(_)) = false;
//bool producesValue(muYield0()) = false;
//bool producesValue(muExhaust()) = false;

//bool producesValue(muNext1(MuExp coro)) = false;
default bool producesValue(MuExp exp) = true;

// Management needed to compute exception tables

// An EEntry's handler is defined for ranges 
// this is needed to inline 'finally' blocks, which may be defined in different 'try' scopes, 
// into 'try', 'catch' and 'finally' blocks
alias EEntry = tuple[lrel[str,str] ranges, AType \type, str \catch, MuExp \finally];

// Stack of 'try' blocks (needed as 'try' blocks may be nested)
list[EEntry] tryBlocks = [];
list[EEntry] finallyBlocks = [];

// Functions to manage the stack of 'try' blocks
void enterTry(str from, str to, AType \type, str \catch, MuExp \finally) {
    tryBlocks = <[<from, to>], \type, \catch, \finally> + tryBlocks;
    finallyBlocks = <[<from, to>], \type, \catch, \finally> + finallyBlocks;
}
void leaveTry() {
    tryBlocks = tail(tryBlocks);
}
void leaveFinally() {
    finallyBlocks = tail(finallyBlocks);
}

// Get the label of a top 'try' block
EEntry topTry() = top(tryBlocks);

// 'Catch' blocks may also throw an exception, which must be handled by 'catch' blocks of surrounding 'try' block
list[EEntry] catchAsPartOfTryBlocks = [];

void enterCatchAsPartOfTryBlock(str from, str to, AType \type, str \catch, MuExp \finally) {
    catchAsPartOfTryBlocks = <[<from, to>], \type, \catch, \finally> + catchAsPartOfTryBlocks;
}
void leaveCatchAsPartOfTryBlocks() {
    catchAsPartOfTryBlocks = tail(catchAsPartOfTryBlocks);
}

EEntry topCatchAsPartOfTryBlocks() = top(catchAsPartOfTryBlocks);


// Instruction block of all the 'catch' blocks within a function body in the same order in which they appear in the code
list[INS] catchBlocks = [[]];
int currentCatchBlock = 0;

INS finallyBlock = [];

// As we use label names to mark try blocks (excluding 'catch' clauses)
list[EEntry] exceptionTable = [];

/*********************************************************************/
/*      Identify fail statements                                     */
/*                                                                   */
/* For the efficient translation of muIf and muWhile it is necessary */
/* to know whether their then/else part, resp. body contain          */
/* unhandled "fail" statements.                                      */
/* We do here a single pass to collect FailInfo:                     */
/* - a set of active fail labels                                     */
/* - a set of if/while labels whose parts are affected by an         */
/*   active fail statement                                           */
/* During code generation, the global "failInfo" holds the current   */
/* value                                                             */
/*********************************************************************/

alias FailInfo = tuple[set[str] active, set[str] labels];

FailInfo findFail(list[MuExp] exps){
    all_active = {};
    all_labels = {};
    for(exp <- exps){
       <active, labels> = findFail(exp);
       all_active += active;
       all_labels += labels;
    }
    return <all_active, all_labels>;
}

FailInfo findFail(muIfelse(str label, MuExp cond, list[MuExp] thenPart,  list[MuExp] elsePart)){
    <then_active, then_labels> = findFail(thenPart);
    <else_active, else_labels> = findFail(elsePart);
    all_active =  then_active + else_active;
    all_labels = then_labels + else_labels;
    return label in all_active ? <all_active - label, all_labels + label>
                               : <all_active, all_labels>;
}

FailInfo findFail(muWhile(str label, MuExp cond, list[MuExp] body)){
    <body_active, body_labels> = findFail(body);
    return label in body_active ? <body_active - label, body_labels + label>
                                : <body_active, body_labels>;
}

FailInfo findFail(muBlock(list[MuExp] exps) ) = 
    findFail(exps);

FailInfo findFail(muBlockWithTmps(lrel[str name, str fuid] tmps, lrel[str name, str fuid] tmpRefs, list[MuExp] exps)) =
    findFail(exps);

FailInfo findFail(muFail(str label)) = <{label}, {}>;

FailInfo findFail(muCall(MuExp fun, list[MuExp] largs)) = findFail(largs);
FailInfo findFail(muApply(MuExp fun, list[MuExp] largs)) = findFail(fun + largs);
FailInfo findFail(muOCall3(MuExp fun, list[MuExp] largs, loc src) ) = findFail(fun + largs);
FailInfo findFail(muOCall4(MuExp fun, AType types, list[MuExp] largs, loc src) ) = findFail(fun + largs);
FailInfo findFail(muCallPrim3(str name, list[MuExp] exps, loc src)) = findFail(exps);
FailInfo findFail(muCallMuPrim(str name, list[MuExp] exps)) = findFail(exps);
FailInfo findFail(muCallJava(str name, str class, AType parameterTypes, AType keywordTypes, int reflect, list[MuExp] largs)) = findFail(largs);
FailInfo findFail(muReturn1(MuExp exp)) = findFail(exp);
FailInfo findFail(muInsert(MuExp exp)  ) = findFail(exp);
FailInfo findFail(muAssignLoc(str name, int pos, MuExp exp)) = findFail(exp);
FailInfo findFail(muAssign(str name, str fuid, int pos, MuExp exp)) = findFail(exp);
FailInfo findFail(muAssignTmp(str name, str fuid, MuExp exp)) = findFail(exp);
FailInfo findFail(muAssignLocKwp(str name, MuExp exp)) = findFail(exp);
FailInfo findFail(muAssignKwp(str fuid, str name, MuExp exp)) = findFail(exp);
FailInfo findFail(muAssignLocDeref(str name, int pos, MuExp exp)) = findFail(exp);
FailInfo findFail(muAssignVarDeref(str name, str fuid, int pos, MuExp exp)  ) = findFail(exp);
FailInfo findFail(muCreate2(MuExp coro, list[MuExp] largs) ) = findFail(largs);
FailInfo findFail(muNext1(MuExp exp)) = findFail(exp);
FailInfo findFail(muNext2(MuExp exp1, list[MuExp] largs) ) = findFail(largs);
FailInfo findFail(muYield1(MuExp exp)) = findFail(exp);
FailInfo findFail(muYield2(MuExp exp, list[MuExp] exps) ) = findFail(exps);
FailInfo findFail(muGuard(MuExp exp)) = findFail(exp);          
FailInfo findFail(muBlock(list[MuExp] exps)) = findFail(exps);
FailInfo findFail(muBlockWithTmps(lrel[str name, str fuid] tmps, lrel[str name, str fuid] tmpRefs, list[MuExp] exps)) = findFail(exps);
FailInfo findFail(muMulti(MuExp exp)) = findFail(exp); 
FailInfo findFail(muOne1(MuExp exp)) = findFail(exp);
FailInfo findFail(muThrow(MuExp exp, loc src)) = findFail(exp);
FailInfo findFail(muTry(MuExp exp, MuCatch \catch, MuExp \finally)) = findFail([exp, \catch.body, \finally]);
FailInfo findFail(muVisit(bool direction, bool fixedpoint, bool progress, bool rebuild, 
                          MuExp descriptor, MuExp phi, MuExp subject, MuExp refHasMatch, 
                          MuExp refBeenChanged, MuExp refLeaveVisit, MuExp refBegin, MuExp refEnd)) = 
    findFail([phi, subject]);
FailInfo findFail(muTypeSwitch(MuExp exp, list[MuTypeCase] type_cases, MuExp \default)) = 
    findFail([exp, \default] + [tcase.exp | tcase <- type_cases]);
FailInfo findFail(muSwitch(MuExp exp, bool useConcreteFingerprint, list[MuCase] cases, MuExp defaultExp)) =
    findFail([exp, defaultExp] + [mucase.exp | mucase <- cases ]);
   
default FailInfo findFail(MuExp exp) = <{}, {}>;
   
/*********************************************************************/
/*      Translate a muRascal library module                          */
/*********************************************************************/

list[RVMDeclaration] mulib2rvm(MuModule muLib){
    list[RVMDeclaration] functions = [];
    
    for(fun <- muLib.functions) {
        currentFunction = fun;
        setFunctionScope(fun.qname);
        set_nlocals(fun.nlocals);
        usedOverloadedFunctions = {};
        usedFunctions = {};
        failInfo = findFail(fun.body);
        body = peephole(tr(fun.body, stack(), returnDest()));
        <maxSP, exceptions> = validate(fun.src, body, []);
        required_frame_size = get_nlocals() + maxSP;
        functions += (fun is muCoroutine) ? COROUTINE(fun.qname, fun. uqname, fun.scopeIn, fun.nformals, get_nlocals(), (), fun.refs, fun.src, required_frame_size, body, [], usedOverloadedFunctions, usedFunctions)
                                          : FUNCTION(fun.qname, fun.uqname, fun.ftype, /*fun.kwType,*/ fun.scopeIn, fun.nformals, get_nlocals(), (), false, false, false, false, false, (), fun.src, required_frame_size, 
                                                     false, 0, 0, body, [], usedOverloadedFunctions, usedFunctions);
    }
    return functions;
}

/*********************************************************************/
/*      Translate a (generated) muRascal module                      */
/*********************************************************************/

// Translate a muRascal module

RVMModule mu2rvm(muModule(str module_name, 
                           map[str,str] tags,
                           set[Message] messages, 
                           list[str] imports,
                           list[str] extends, 
                           map[str,AType] types,  
                           map[AType, AProduction] symbol_definitions,
                           list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initializations, 
                           int nlocals_in_initializations,
                           map[str,int] resolver,
                           lrel[str name, AType funType, str scope, list[str] ofunctions, list[str] oconstructors] overloaded_functions, 
                           map[AType, AProduction] grammar, 
                           rel[str,str] importGraph,
                           loc src), 
                  bool listing=false,
                  bool verbose=true,
                  bool optimize=true){
 
  init(optimize);
  if(any(m <- messages, error(_,_) := m)){
    return errorRVMModule(module_name, messages, src);
  }
 
  main_fun = getUID(module_name,[],"MAIN",2);
  module_init_fun = getUID(module_name,[],"#<module_name>_init",2);
  ftype = afunc(avalue(),[alist(avalue())],[]);
  fun_names = { fun.qname | MuFunction fun <- functions };
  if(main_fun notin fun_names) {
     main_fun = getFUID(module_name,"main",ftype,0);
     module_init_fun = getFUID(module_name,"#<module_name>_init",ftype,0);
  }
 
  funMap = ();
  nlabel = -1;
  nlocal =   ( fun.qname : fun.nlocals | MuFunction fun <- functions ) 
           + ( module_init_fun : 2 + size(variables) + nlocals_in_initializations);     // Initialization function, 2 for arguments
  minNlocal = nlocal;
  temporaries = [];
    
  //if(verbose) println("mu2rvm: Compiling module <module_name>");
  
  for(fun <- functions) {
    scopeIn[fun.qname] = fun.scopeIn;
  }
 
  for(fun <- functions){
    currentFunction = fun;
    functionScope = fun.qname;
    surroundingFunctionScope = fun.scopeIn;
    localNames = (fun is muCoroutine) ? () : (i : fun.argNames[i] | i <- index(fun.argNames));
    exceptionTable = [];
    catchBlocks = [[]];
    
    usedOverloadedFunctions = {};
    usedFunctions = {};
   
    //println("*** " + functionScope);
    //iprintln(fun.body);
    
    failInfo = findFail(fun.body);
    
    code = tr(fun.body, nowhere(), returnDest());

     // Append catch blocks to the end of the function body code
    catchBlockCode = [ *catchBlock | INS catchBlock <- catchBlocks ];
    
    code = code /*+ [LABEL("FAIL_<fun.uqname>"), FAILRETURN()]*/ + catchBlockCode;
    
    //code = peephole(code);
    
    //iprintln(code);
    //
    //println("Used overloaded Functions:");
    //for(uf <- usedOverloadedFunctions){
    //    println(uf);
    //}
    //
    //println("Used non-overloaded Functions:");
    //for(uf <- usedFunctions){
    //    println(uf);
    //}
    //println("--------------------------");
     
     lrel[str from, str to, AType \type, str target, int fromSP] exceptions = 
        [ <range.from, range.to, entry.\type, entry.\catch, 0>
        | tuple[lrel[str,str] ranges, AType \type, str \catch, MuExp _] entry <- exceptionTable,                                    
          tuple[str from, str to] range <- entry.ranges
        ];
  
    <maxStack, exceptions> = validate(fun.src, code, exceptions);
    required_frame_size = nlocal[functionScope] + maxStack; // estimate_stack_size(fun.body);
    
    funMap += (fun is muCoroutine) ? (fun.qname : COROUTINE(fun.qname, 
                                                            fun.uqname, 
                                                            fun.scopeIn, 
                                                            fun.nformals, 
                                                            nlocal[functionScope], 
                                                            localNames, 
                                                            fun.refs, 
                                                            fun.src, 
                                                            required_frame_size, 
                                                            code, 
                                                            exceptions,
                                                            usedOverloadedFunctions,
                                                            usedFunctions))
                                   : (fun.qname : FUNCTION(fun.qname, 
                                                           fun.uqname, 
                                                           fun.ftype, 
                                                           fun.scopeIn, 
                                                           fun.nformals, 
                                                           nlocal[functionScope], 
                                                           localNames, 
                                                           fun.isVarArgs, 
                                                           fun.isPublic,
                                                           "default" in fun.modifiers,
                                                           "test" in fun.modifiers,
                                                           fun.simpleArgs,
                                                           fun.tags,
                                                           fun.src, 
                                                           required_frame_size, 
                                                           fun.isConcreteArg,
                                                           fun.abstractFingerprint,
                                                           fun.concreteFingerprint,
                                                           code, 
                                                           exceptions,
                                                           usedOverloadedFunctions,
                                                           usedFunctions));
  
    if(listing){
        println("===================== <fun.qname>");
        iprintln(fun);
        println("--------------------- <fun.qname>");
        iprintln(funMap[fun.qname]);
    }
  }
  
  functionScope = module_init_fun;
  usedOverloadedFunctions = {};
  usedFunctions = {};
  failInfo = findFail(initializations);
  code = trvoidblock(initializations, returnDest()); // compute code first since it may generate new locals!
  <maxSP, dummy_exceptions> = validate(|init:///|, code, []);
  if(size(code) > 0){
     funMap += ( module_init_fun : FUNCTION(module_init_fun, "init", ftype, /*atuple(atypeList([])),*/ "" /*in the root*/, 2, nlocal[module_init_fun], (), 
                                            false, true, false, false, true, (), src, maxSP + nlocal[module_init_fun], false, 0, 0,
                                    [*code, 
                                     LOADCON(true),
                                     RETURN1(),
                                     HALT()
                                    ],
                                    [], usedOverloadedFunctions, usedFunctions));
  }                                  
 
  if(listing){
    println("===================== INIT: (nlocals_in_initializations = <nlocals_in_initializations>):");
    iprintln(initializations);
    println("--------------------- INIT");
    iprintln(funMap[module_init_fun]);
  }
  
  res = rvmModule(module_name, (module_name: tags), messages, imports, extends, types, symbol_definitions, orderedDeclarations(funMap), [], resolver, overloaded_functions, importGraph, src);
  return res;
}

list[RVMDeclaration] orderedDeclarations(map[str,RVMDeclaration] funMap) =
    [ funMap[fname] | fname <- sort(toList(domain(funMap))) ];

/*********************************************************************/
/*      Top of stack optimization framework                          */
/*********************************************************************/
data Dest = 
      accu()
    | stack()
    | local(int pos)
    | localref(int pos)
    | localkwp(str name)
    | var(str fuid, int pos)
    | varref(str fuid, int pos) 
    | varkwp(str fuid, str name)
    | con(value v)
    | nowhere()
    ;

INS plug(Dest d1, Dest d2) = [ ] when d1 == d2;

INS plug(accu(), accu()) = [ ];
INS plug(accu(), stack()) = [ PUSHACCU() ];
INS plug(accu(), local(pos)) = [ STORELOC(pos)];
INS plug(accu(), localref(pos)) = [ STORELOCDEREF(pos)];
INS plug(accu(), localkwp(name)) = [ STORELOCKWP(name)];
INS plug(accu(), var(fuid, pos)) = [ STOREVAR(fuid, pos)];
INS plug(accu(), varref(fuid, pos)) = [ STOREVARDEREF(fuid, pos)];
INS plug(accu(), varkwp(fuid, name)) = [ STOREVARKWP(fuid, name)];
INS plug(accu(), nowhere()) = [ ];

INS plug(con(v), accu()) = [ LOADCON(v) ];
INS plug(con(v), stack()) = [ PUSHCON(v) ];
INS plug(con(v), local(pos)) = [ LOADCON(v), STORELOC(pos)];
INS plug(con(v), localref(pos)) = [ LOADCON(v), STORELOCDEREF(pos)];
INS plug(con(v), localkwp(name)) = [ LOADCON(v), STORELOCKWP(name)];
INS plug(con(v), var(fuid, pos)) = [ LOADCON(v), STOREVAR(fuid, pos)];
INS plug(con(v), varref(fuid, pos)) = [ LOADCON(v), STOREVARDEREF(fuid, pos)];
INS plug(con(v), varkwp(fuid, name)) = [ LOADCON(v), STOREVARKWP(fuid, name)];
INS plug(con(v), nowhere()) = [ ];

INS plug(local(pos), accu()) = [ LOADLOC(pos) ];
INS plug(local(pos), stack()) = [ PUSHLOC(pos) ];
INS plug(local(pos1), local(pos2)) = pos1 == pos2 ? [] : [ LOADLOC(pos1), STORELOC(pos2) ];
INS plug(local(pos1), localref(pos2)) = [ LOADLOC(pos1), STORELOCDEREF(pos2) ];
INS plug(local(pos), localkwp(name)) = [ LOADLOC(pos), STORELOCKWP(name) ];
INS plug(local(pos1), var(fuid, pos2)) = [ LOADLOC(pos1), STOREVAR(fuid, pos2)];
INS plug(local(pos1), varref(fuid, pos2)) = [ LOADLOC(pos1), STOREVARDEREF(fuid, pos2)];
INS plug(local(pos), varkwp(fuid, name)) = [ LOADLOC(pos), STOREVARKWP(fuid, name)];
INS plug(local(pos), nowhere()) = [ ];

INS plug(localref(pos), accu()) = [ LOADLOCREF(pos) ];
INS plug(localref(pos), stack()) = [ PUSHLOCREF(pos) ];
INS plug(localref(pos1), local(pos2)) = [ LOADLOCREF(pos1), STORELOC(pos2) ];
INS plug(localref(pos1), localref(pos2)) = [ LOADLOCREF(pos1), STORELOCDEREF(pos2) ];
INS plug(localref(pos1), localkwp(name)) = [ LOADLOCREF(pos1), STORELOCKWP(name) ];
INS plug(localref(pos1), var(fuid, pos2)) = [ LOADLOCREF(pos1), STOREVAR(fuid, pos2)];
INS plug(localref(pos1), varref(fuid, pos2)) = [ LOADLOCREF(pos1), STOREVARDEREF(fuid, pos2)];
INS plug(localref(pos1), varkwp(fuid, name)) = [ LOADLOCREF(pos1), STOREVARKWP(fuid, name)];
INS plug(localref(pos), nowhere()) = [ ];

INS plug(localkwp(name), accu()) = [ LOADLOCKWP(name) ];
INS plug(localkwp(name), stack()) = [ PUSHLOCKWP(name) ];
INS plug(localkwp(name), local(pos)) = [ LOADLOCKWP(name), STORELOC(pos) ];
INS plug(localkwp(name), localref(pos)) = [ LOADLOCKWP(name), STORELOCDEREF(pos) ];
INS plug(localkwp(name1), localkwp(name2)) = name1 == name2 ? [ ] : [ LOADLOCKWP(name1), STORELOCKWP(name2) ];
INS plug(localkwp(name), var(fuid, pos)) = [ LOADLOCKWP(name), STOREVAR(fuid, pos) ];
INS plug(localkwp(name1), varref(fuid2, pos2)) = [ LOADLOCKWP(name1), STOREVARDEREF(fuid2, pos2) ];
INS plug(localkwp(name), nowhere()) = [ ];

INS plug(var(fuid, pos), accu()) = [ LOADVAR(fuid, pos) ];
INS plug(var(fuid, pos), stack()) = [ PUSHVAR(fuid, pos) ];
INS plug(var(fuid, pos1), local(pos2)) = [LOADVAR(fuid, pos1), STORELOC(pos2) ];
INS plug(var(fuid, pos1), localref(pos2)) = [LOADVAR(fuid, pos1), STORELOCDEREF(pos2) ];
INS plug(var(fuid, pos1), localkwp(name)) = [LOADVAR(fuid, pos1), STORELOCKWP(name) ];
INS plug(var(fuid1, pos1), var(fuid2, pos2)) = fuid1 == fuid2 && pos1 == pos2 ? [] : [LOADVAR(fuid1, pos1), STOREVAR(fuid2, pos2) ];
INS plug(var(fuid1, pos1), varref(fuid2, pos2)) = [LOADVAR(fuid1, pos1), STOREVARDEREF(fuid2, pos2) ];
INS plug(var(fuid1, pos1), varkwp(fuid2, name2)) = [LOADVAR(fuid1, pos1), STOREVARKWP(fuid2, name2) ];
INS plug(var(fuid, pos), nowhere()) = [ ];

INS plug(varref(fuid, pos), accu()) = [ LOADVARREF(fuid, pos) ];
INS plug(varref(fuid, pos), stack()) = [ PUSHVARREF(fuid, pos) ];
INS plug(varref(fuid, pos1), local(pos2)) = [ LOADVARREF(fuid, pos1), STORELOC(pos2) ];
INS plug(varref(fuid, pos1), localref(pos2)) = [ LOADVARREF(fuid, pos1), STORELOCDEREF(pos2) ];
INS plug(varref(fuid, pos1), localkwp(name2)) = [ LOADVARREF(fuid, pos1), STORELOCKWP(name2) ];
INS plug(varref(fuid1, pos1), var(fuid2, pos2)) = [ LOADVARREF(fuid1, pos1), STOREVAR(fuid2, pos2) ];
INS plug(varref(fuid1, pos1), varref(fuid2, pos2)) = [ LOADVARREF(fuid1, pos1), STOREVARDEREF(fuid2, pos2) ];
INS plug(varref(fuid, pos), nowhere()) = [ ];

INS plug(varkwp(fuid, pos), accu()) = [ LOADVARKWP(fuid, pos) ];
INS plug(varkwp(fuid, pos), stack()) = [ PUSHVARKWP(fuid, pos) ];
INS plug(varkwp(fuid, pos1), local(pos2)) = [ LOADVARKWP(fuid, pos1), STORELOC(pos2) ];
INS plug(varkwp(fuid1, pos1), var(fuid2, pos2)) = [ LOADVARKWP(fuid1, pos1), STOREVAR(fuid2, pos2) ];
INS plug(varkwp(fuid, pos), nowhere()) = [ ];

INS plug(stack(), accu()) = [ POPACCU() ];
INS plug(stack(), local(pos)) = [ POPACCU(), STORELOC(pos) ];
INS plug(stack(), var(fuid, pos)) = [ POPACCU(), STOREVAR(fuid, pos) ];
INS plug(stack(), nowhere()) = [ POP() ];

INS plug(Dest d, nowhere()) = [];

default INS plug(Dest d1, Dest d2) { 
    if(d1 == d2) return [];
    throw "canot plug from <d1> to <d2>";
}

data CDest =
      noDest()
    | labelDest(str name)
    | returnDest()
    | branchDest(str trueDest, str falseDest)
    ;

Instruction jmp(labelDest(name)) { usedLabels += name; return JMP(name); }
Instruction jmp(returnDest()) = RETURN1();

Instruction jmpfalse(labelDest(name)) { usedLabels += name; return JMPFALSE(name); }


Instruction jmptrue(returnDest()) = RETURN1();
Instruction jmptrue(labelDest(name)) { usedLabels += name; return JMPTRUE(name); }
default Instruction jmptrue(CDest c) { throw "jmptrue to <c>"; }

INS appendJmp(INS instructions, Instruction jmpIns){
    if(size(instructions) == 0){
        return [ jmpIns ];
    }
    lastIns = instructions[-1];
    if(RETURN1() := lastIns){  // TODO: consider more return types
        return instructions;
    }
    return instructions + jmpIns;
}

/*********************************************************************/
/*      Translate lists of muRascal expressions                      */
/*********************************************************************/

//INS tr(list[MuExp] exps, CDest c) = [ *tr(exp, stack(), c) | exp <- exps ];

INS tr_arg(MuExp exp, Dest d){
    seq = nextSeqLabel();
    println("tr_arg: <seq>, <labelDest(seq)>");
    code = tr(exp, d, labelDest(seq));
    return seq in usedLabels ? [ *code, LABEL(seq) ] : code;
}

INS tr_arg_stack(MuExp exp) = tr_arg(exp, stack());

INS tr_arg_accu(MuExp exp) = tr_arg(exp, accu());

INS tr_arg_nowhere(MuExp exp) = tr_arg(exp, nowhere());

INS tr_arg_return(MuExp exp) = tr(exp, accu(), returnDest());

INS tr_args_stack(list[MuExp] exps){
    ins = [];
    for(exp <- exps){
        seq = nextSeqLabel();
        code = tr(exp, stack(), labelDest(seq));
        ins += seq in usedLabels ? [ *code, LABEL(seq) ] : code;
    }
    return ins;
}

INS tr_args_accu(list[MuExp] exps){
    ins = [];
    for(exp <- exps[0..-1]){
        seq = nextSeqLabel();
        code = tr(exp, stack(), labelDest(seq));
        ins += seq in usedLabels ? [ *code, LABEL(seq) ] : code;
    }
    seq = nextSeqLabel();
    code = tr(exps[-1], accu(), labelDest(seq));
    ins += seq in usedLabels ? [ *code, LABEL(seq) ] : code;
    return ins;
}

INS tr_and_pop(muBlock([])) = [];

default INS tr_and_pop(MuExp exp, CDest c) = tr(exp, nowhere(), c);

INS trblock(list[MuExp] exps, Dest d, CDest c) {
  
  if(d == nowhere() && size(exps) > 0 && muCon(_) := exps[-1]){
    exps = exps[0..-1];
  }
  if(size(exps) == 0){
     return plug(con(666), d); // TODO: throw "Non void block cannot be empty";
  }
  ins = [];
  for(exp <- exps[0..-1]){
        seq = nextSeqLabel();
        code = tr_and_pop(exp, labelDest(seq));
        if(size(code) > 0 && (code[-1] == JMP(seq) || code[-1] == JMPTRUE(seq) || code[-1] == JMPFALSE(seq))){
           code = code[0..-1];
        }
        ins += seq in usedLabels ? [ *code, LABEL(seq) ] : code;
  }
  ins += tr(exps[-1], d, c);
  if(!producesValue(exps[-1])){
    ins += plug(con(666), d);
  }
  return ins;
}

INS trvoidblock(list[MuExp] exps, CDest c){
    return trblock(exps, nowhere(), c);
}

INS tr(muBlock(list[MuExp] exps), Dest d, CDest c) = trblock(exps, d, c);


/*********************************************************************/
/*      Translate a single muRascal expression                       */
/*********************************************************************/

INS tr(MuExp exp, CDest c) = tr(exp, stack(), c);

default INS tr(MuExp e, Dest d, CDest c) { throw "Unsupported MuExp <e>"; }

// Literals and type constants

INS tr(muBool(bool b), Dest d, CDest c) = plug(con(b), d); //LOADBOOL(b) + plug(accu(), d);

INS tr(muInt(int n), Dest d, CDest c) = LOADINT(n) + plug(accu(), d);

INS tr(muCon(value v), Dest d, CDest c) = plug(con(v), d);

INS tr(muTypeCon(AType sym), Dest d, CDest c) = d == stack() ? [PUSHTYPE(sym)] : LOADTYPE(sym) + plug(accu(), d);

// muRascal functions

INS tr(muFun1(str fuid), Dest d, CDest c) {
    usedFunctions += fuid;
    return PUSH_ROOT_FUN(fuid) + plug(stack(), d);
}

INS tr(muFun2(str fuid, str scopeIn), Dest d, CDest c) {
    usedFunctions += fuid;
    return PUSH_NESTED_FUN(fuid, scopeIn) + plug(stack(), d);
}

// Rascal functions

INS tr(muOFun(str fuid), Dest d, CDest c) {
    usedOverloadedFunctions += fuid;
    return PUSHOFUN(fuid) + plug(stack(), d);
}

INS tr(muConstr(str fuid), Dest d, CDest c) = PUSHCONSTR(fuid) + plug(stack(), d);

// Variables and assignment

INS tr(muVar(str id, str fuid, int pos), Dest d, CDest c) {
   
    if(fuid == functionScope){
       localNames[pos] = id;
       return plug(local(pos), d);
    } else {
       return plug(var(fuid, pos), d);
    }
}

INS tr(muLoc(str id, int pos), Dest d, CDest c) { localNames[pos] = id; return plug(local(pos), d);}

INS tr(muResetLocs(list[int] positions), Dest d, CDest c) { return [RESETLOCS(positions)];}

INS tr(muTmp(str id, str fuid), Dest d, CDest c) = fuid == functionScope ? plug(local(getTmp(id,fuid)), d) : plug(var(fuid,getTmp(id,fuid)), d);

INS tr(muLocKwp(str name), Dest d, CDest c) = plug(localkwp(name), d);
INS tr(muVarKwp(str fuid, str name), Dest d, CDest c) = fuid == functionScope ? plug(localkwp(name), d) : plug(varkwp(fuid, name), d);

INS tr(muLocDeref(str name, int pos), Dest d, CDest c) = [ LOADLOCDEREF(pos) ] + plug(accu(), d);
//INS tr(muVarDeref(str name, str fuid, int pos), Dest d) = fuid == functionScope ? plug(localref(pos), d) : plug(varref(fuid, pos), d);

INS tr(muVarDeref(str name, str fuid, int pos), Dest d, CDest c) = [ fuid == functionScope ? LOADLOCDEREF(pos) : LOADVARDEREF(fuid, pos) ]+ plug(accu(), d);


INS tr(muLocRef(str name, int pos), Dest d, CDest c) =  plug(localref(pos), d);
INS tr(muVarRef(str name, str fuid, int pos), Dest d, CDest c) = fuid == functionScope ? plug(localref(pos), d) : plug(varref(fuid, pos), d);
INS tr(muTmpRef(str name, str fuid), Dest d, CDest c) = fuid == functionScope ? plug(localref(getTmp(name,fuid)), d) : plug(varref(fuid,getTmp(name,fuid)), d);

INS tr(muAssignLocDeref(str id, int pos, MuExp exp), Dest d, CDest c) = [ *tr_arg_accu(exp), STORELOCDEREF(pos), *plug(accu(), d) ];
INS tr(muAssignVarDeref(str id, str fuid, int pos, MuExp exp), Dest d, CDest c) = [ *tr_arg_accu(exp), fuid == functionScope ? STORELOCDEREF(pos) : STOREVARDEREF(fuid, pos),  *plug(accu(), d)  ];

INS tr(muAssign(str id, str fuid, int pos, MuExp exp), Dest d, CDest c) { 
     if(fuid == functionScope){
        localNames[pos] = id; 
        return [ *tr_arg_accu(exp), STORELOC(pos), *plug(accu(), d) ];
     } else {
        return [*tr_arg_accu(exp), STOREVAR(fuid, pos), *plug(accu(), d) ];
     }
}     
INS tr(muAssignLoc(str id, int pos, MuExp exp), Dest d, CDest c) { 
    localNames[pos] = id;
    return [*tr_arg_accu(exp), STORELOC(pos), *plug(accu(), d) ];
}
INS tr(muAssignTmp(str id, str fuid, MuExp exp), Dest d, CDest c) = [*tr_arg_accu(exp), fuid == functionScope ? STORELOC(getTmp(id,fuid)) : STOREVAR(fuid,getTmp(id,fuid)) ] + plug(accu(), d);

INS tr(muAssignLocKwp(str name, MuExp exp), Dest d, CDest c) = [ *tr_arg_accu(exp), STORELOCKWP(name), *plug(accu(), d) ];
INS tr(muAssignKwp(str fuid, str name, MuExp exp), Dest d, CDest c) = [ *tr_arg_accu(exp), fuid == functionScope ? STORELOCKWP(name) : STOREVARKWP(fuid,name) ] + plug(accu(), d);

// Calls

// Constructor

//INS tr(muCallConstr(str fuid, list[MuExp] args), Dest d, CDest c) = [ *tr_args_stack(args), CALLCONSTR(fuid, size(args)), *plug(accu(), d) ];

// muRascal functions

INS tr(muCall(MuExp fun, list[MuExp] args), Dest d, CDest c) = trMuCall(fun, args, d, c);
 
INS trMuCall(muFun1(str fuid), list[MuExp] args, Dest d, CDest c) {
    usedFunctions += fuid;
    return [*tr_args_stack(args), CALL(fuid, size(args)), *plug(accu(), d)];
}

INS trMuCall(muConstr(str fuid), list[MuExp] args, Dest d, CDest c) = [*tr_args_stack(args), CALLCONSTR(fuid, size(args)), *plug(accu(), d)];

default INS trMuCall(MuExp fun, list[MuExp] args, Dest d, CDest c) = [*tr_args_stack(args), *tr_arg_stack(fun), CALLDYN(size(args)), *plug(accu(), d)];

// Partial application of muRascal functions

 INS tr(muApply(MuExp fun, list[MuExp] args), Dest d, CDest c) = trMuApply(fun, args, d, c);
 
INS trMuApply(muFun1(str fuid), list[MuExp] args: [], Dest d, CDest c) {
    usedFunctions += fuid;
    return [ PUSH_ROOT_FUN(fuid), *plug(stack(), d) ];
}

INS trMuApply(muFun1(str fuid), list[MuExp] args, Dest d, CDest c) {
    usedFunctions += fuid;
    return [ *tr_args_stack(args), APPLY(fuid, size(args)), *plug(stack(), d) ];
}

INS trMuApply(muConstr(str fuid), list[MuExp] args, Dest d, CDest c) { throw "Partial application is not supported for constructor calls!"; }

INS trMuApply(muFun2(str fuid, str scopeIn), list[MuExp] args: [], Dest d, CDest c) {
    usedFunctions += fuid;
    return [ PUSH_NESTED_FUN(fuid, scopeIn), *plug(stack(), d) ];
}

default INS trMuApply(MuExp fun, list[MuExp] args, Dest d, CDest c) = [ *tr_args_stack(args), *tr_arg_stack(fun), APPLYDYN(size(args)), *plug(stack(), d)  ];

// Rascal functions

INS tr(muOCall3(muOFun(str fuid), list[MuExp] args, loc src), Dest d, CDest c) {
    usedOverloadedFunctions += fuid;
    return [*tr_args_stack(args), OCALL(fuid, size(args), src), *plug(accu(), d)];
}

INS tr(muOCall4(MuExp fun, AType types, list[MuExp] args, loc src), Dest d, CDest c) 
    = [ *tr_args_stack(args),
        *tr_arg_stack(fun), 
        OCALLDYN(types, size(args), src),
        *plug(accu(), d)
      ];
        
// Visit
INS tr(muVisit(bool direction, bool fixedpoint, bool progress, bool rebuild, MuExp descriptor, MuExp phi, MuExp subject, MuExp refHasMatch, MuExp refBeenChanged, MuExp refLeaveVisit, MuExp refBegin, MuExp refEnd), Dest d, CDest c)
    = [ *tr_arg_stack(phi),
        *tr_arg_stack(subject),
        *tr_arg_stack(refHasMatch),
        *tr_arg_stack(refBeenChanged),
        *tr_arg_stack(refLeaveVisit),
        *tr_arg_stack(refBegin),
        *tr_arg_stack(refEnd),
        *tr_arg_stack(descriptor),
        VISIT(direction, fixedpoint, progress, rebuild),
        *plug(stack(), d)
      ];


// Calls to Rascal primitives that are directly translated to RVM instructions

INS tr(muCallPrim3(str name, list[MuExp] args, loc src), Dest d, CDest c) = trMuCallPrim3(name, args, src, d, c);

INS trMuCallPrim3("println", list[MuExp] args, loc src, Dest d, CDest c) = [*tr_args_stack(args), PRINTLN(size(args))];

INS trMuCallPrim3("subtype", list[MuExp] args, loc src, Dest d, CDest c) = 
    [*tr_args_accu(args), SUBTYPE(), *plug(accu(), d)];
    
INS trMuCallPrim3("typeOf", list[MuExp] args, loc src, Dest d, CDest c) = 
    [*tr_args_accu(args), TYPEOF(), *plug(accu(), d)];
    
INS trMuCallPrim3("check_memo", list[MuExp] args, loc src, Dest d, CDest c) = 
    [CHECKMEMO(), *plug(accu(), d)];

INS trMuCallPrim3("subtype_value_type", list[MuExp] args: [exp1,  muTypeCon(AType tp)], loc src, Dest d, CDest c) = 
    [*tr_arg_accu(exp1), VALUESUBTYPE(tp), *plug(accu(), d)];

default INS trMuCallPrim3(str name, list[MuExp] args, loc src, Dest d, CDest c) {
  n = size(args);
  if(name in {"node_create", "list_create", "set_create", "tuple_create", "map_create", 
                "listwriter_add", "setwriter_add", "mapwriter_add", "str_add_str", "template_open",
                "tuple_field_project", "adt_field_update", "rel_field_project", "lrel_field_project", "map_field_project",
                "list_slice_replace", "list_slice_add", "list_slice_subtract", "list_slice_product", "list_slice_divide", 
                "list_slice_intersect", "str_slice_replace", "node_slice_replace", "list_slice", 
                "rel_subscript", "lrel_subscript" }){ // varyadic MuPrimitives
        return  d == stack() ? [*tr_args_stack(args), PUSHCALLPRIMN(name, n, src)]
                             : [*tr_args_stack(args), CALLPRIMN(name, n, src), *plug(accu(), d)];
    }
    
    switch(n){
        case 0: return d == stack() ? [ PUSHCALLPRIM0(name, src) ]
                                    : CALLPRIM0(name, src) + plug(accu(), d);
                                   
        case 1: return d == stack() ? [*tr_args_accu(args), PUSHCALLPRIM1(name,src)]
                                    : [*tr_args_accu(args), CALLPRIM1(name,src), *plug(accu(), d)];
                                   
        case 2: return d == stack() ? [*tr_args_accu(args), PUSHCALLPRIM2(name,src)]
                                    : [*tr_args_accu(args), CALLPRIM2(name,src), *plug(accu(), d)];
                                    
        default: return d == stack() ? [*tr_args_stack(args), PUSHCALLPRIMN(name, n, src)]
                                     : [*tr_args_stack(args), CALLPRIMN(name, n, src), *plug(accu(), d)];
   }
}

// Calls to MuRascal primitives that are directly translated to RVM instructions

INS tr(muCallMuPrim(str name, list[MuExp] args), Dest d, CDest c) = trMuCallMuPrim(name, args, d, c);

INS trMuCallMuPrim("println", list[MuExp] args, Dest d, CDest c) = [*tr_args_accu(args), PRINTLN(size(args))];

INS trMuCallMuPrim("subscript_array_mint", list[MuExp] args, Dest d, CDest c) = 
    [*tr_args_accu(args), SUBSCRIPTARRAY(), *plug(accu(), d)];
    
INS trMuCallMuPrim("subscript_list_mint", list[MuExp] args, Dest d, CDest c) = 
    [*tr_args_accu(args), SUBSCRIPTLIST(), *plug(accu(), d)];
    
INS trMuCallMuPrim("less_mint_mint", list[MuExp] args, Dest d, CDest c) =
    [*tr_args_accu(args), LESSINT(), *plug(accu(), d)];
       
INS trMuCallMuPrim("greater_equal_mint_mint", list[MuExp] args, Dest d, CDest c) = 
    [*tr_args_accu(args), GREATEREQUALINT(), *plug(accu(), d)];

INS trMuCallMuPrim("addition_mint_mint", list[MuExp] args, Dest d, CDest c) = 
    [*tr_args_accu(args), ADDINT(), *plug(accu(), d)];
    
INS trMuCallMuPrim("subtraction_mint_mint", list[MuExp] args, Dest d, CDest c) = 
    [*tr_args_accu(args), SUBTRACTINT(), *plug(accu(), d)];
    
INS trMuCallMuPrim("and_mbool_mbool", list[MuExp] args, Dest d, CDest c) = 
    [*tr_args_accu(args), ANDBOOL(), *plug(accu(), d)];

INS trMuCallMuPrim("check_arg_type_and_copy", [muCon(int pos1), muTypeCon(AType tp), muCon(int pos2)], Dest d, CDest c) = 
    CHECKARGTYPEANDCOPY(pos1, tp, pos2) + plug(accu(), d);
    
INS trMuCallMuPrim("make_mmap", [], Dest d, CDest c) =  PUSHEMPTYKWMAP() + plug(stack(), d);
    
default INS trMuCallMuPrim(str name, list[MuExp] args, Dest d, CDest c) {
   n = size(args);
   if(name in {"make_array", "make_mmap", "copy_and_update_keyword_mmap"}){ // varyadic MuPrimtives
        return d == stack() ? [*tr_args_stack(args), PUSHCALLMUPRIMN(name, n)]
                            : [*tr_args_stack(args), CALLMUPRIMN(name, n), *plug(accu(), d)];
    }
    
    switch(n){
        case 0: return d == stack() ? [ PUSHCALLMUPRIM0(name) ]
                                    : [ CALLMUPRIM0(name), *plug(accu(), d) ];
                                    
        case 1: return d == stack() ? [*tr_args_accu(args), PUSHCALLMUPRIM1(name)]
                                    : [*tr_args_accu(args), CALLMUPRIM1(name), *plug(accu(), d)];
                                    
        case 2: return d == stack() ? [*tr_args_accu(args), PUSHCALLMUPRIM2(name)]
                                    : [*tr_args_accu(args), CALLMUPRIM2(name), *plug(accu(), d)];
                                    
        default: return d == stack() ? [*tr_args_stack(args), PUSHCALLMUPRIMN(name, n)]
                                     : [*tr_args_stack(args), CALLMUPRIMN(name, n), *plug(accu(), d)];
   }
}

INS tr(muCallJava(str name, str class, AType parameterTypes, AType keywordTypes, int reflect, list[MuExp] args), Dest d, CDest c) = 
    [ *tr_args_stack(args), CALLJAVA(name, class, parameterTypes, keywordTypes, reflect), *plug(stack(), d) ];

// Return

INS tr(muReturn0(), Dest d, CDest c) = [currentFunction is muCoroutine ? CORETURN0() : RETURN0()];

INS tr(muReturn1(MuExp exp), Dest d, CDest c) {
    if(muTmp(_,_) := exp) {
        inlineMuFinally(d, c);
        return [*finallyBlock, *tr_arg_accu(exp), currentFunction is muCoroutine ? CORETURN1(1) : RETURN1()];
    }
    return [*tr_arg_return(exp), currentFunction is muCoroutine ? CORETURN1(1) : RETURN1()];
}

INS tr(muFailReturn(), Dest d, CDest c) = [ FAILRETURN() ];

INS tr(muFilterReturn(), Dest d, CDest c) = [ FILTERRETURN() ];

// Coroutines

INS tr(muCreate1(muFun1(str fuid)), Dest d, CDest c) {
    usedFunctions += fuid;
    return [ CREATE(fuid, 0), *plug(accu(), d) ];
}

INS tr(muCreate1(MuExp exp), Dest d, CDest c) = [ *tr_arg_stack(exp), CREATEDYN(0), *plug(accu(), d) ];

INS tr(muCreate2(muFun1(str fuid), list[MuExp] args), Dest d, CDest c) {
    usedFunctions += fuid;
    return [ *tr_args_stack(args), CREATE(fuid, size(args)), *plug(accu(), d) ];
}

INS tr(muCreate2(MuExp coro, list[MuExp] args), Dest d, CDest c) = [ *tr_args_stack(args + coro),  CREATEDYN(size(args)), *plug(accu(), d) ];  // note the order! 

INS tr(muNext1(MuExp coro), Dest d, CDest c) = [*tr_arg_accu(coro), NEXT0(), *plug(accu(), d)];
INS tr(muNext2(MuExp coro, list[MuExp] args), Dest d, CDest c) = [*tr_args_stack(args), *tr_arg_accu(coro),  NEXT1(), *plug(accu(), d)]; // note the order!

INS tr(muYield0(), Dest d, CDest c) = [YIELD0(), *plug(stack(), d)];
INS tr(muYield1(MuExp exp), Dest d, CDest c) = [*tr_arg_stack(exp), YIELD1(1), *plug(stack(), d)];
INS tr(muYield2(MuExp exp, list[MuExp] exps), Dest d, CDest c) = [ *tr_args_stack(exp + exps), YIELD1(size(exps) + 1), *plug(stack(), d) ];

INS tr(lang::rascalcore::compile::muRascal::AST::muExhaust(), Dest d, CDest c) = [ EXHAUST() ];

INS tr(muGuard(MuExp exp), Dest d, CDest c) = [ *tr_arg_accu(exp), GUARD() ];

// Exceptions

INS tr(muThrow(MuExp exp, loc src), Dest d, CDest c) = [ *tr_arg_stack(exp), THROW(src) ];

// Temporary fix for Issue #781
AType filterExceptionType(AType s) = s == aadt("RuntimeException",[], dataSyntax()) ? avalue() : s;

INS tr(muTry(MuExp exp, MuCatch \catch, MuExp \finally), Dest d, CDest c) {
    
    // Mark the begin and end of the 'try' and 'catch' blocks
    str tryLab = nextLabel();
    str catchLab = nextLabel();
    str finallyLab = nextLabel();
    
    str try_from      = mkTryFrom(tryLab);
    str try_to        = mkTryTo(tryLab);
    str catch_from    = mkCatchFrom(catchLab); // used to jump
    str catch_to      = mkCatchTo(catchLab);   // used to mark the end of a 'catch' block and find a handler catch
    
    // Mark the begin of 'catch' blocks that have to be also translated as part of 'try' blocks 
    str catchAsPartOfTry_from = mkCatchFrom(nextLabel()); // used to find a handler catch
    
    // There might be no surrounding 'try' block for a 'catch' block
    if(!isEmpty(tryBlocks)) {
        // Get the outer 'try' block
        EEntry currentTry = topTry();
        // Enter the current 'catch' block as part of the outer 'try' block
        enterCatchAsPartOfTryBlock(catchAsPartOfTry_from, catch_to, currentTry.\type, currentTry.\catch, currentTry.\finally);
    }
    
    // Enter the current 'try' block; also including a 'finally' block
    enterTry(try_from, try_to, \catch.\type, catch_from, \finally); 
    
    // Translate the 'try' block; inlining 'finally' blocks where necessary
    code = [ LABEL(try_from), *tr(exp, d, labelDest(try_to)) ];
    
    if(code == [LABEL(try_from)]){ // Avoid non-empty try blocks for the benefit of JVM byte code generation
        code = [LABEL(try_from), LOADCON(123)];
    }
    
    oldFinallyBlocks = finallyBlocks;
    leaveFinally();
    
    // Fill in the 'try' block entry into the current exception table
    currentTry = topTry();
    exceptionTable += <currentTry.ranges, filterExceptionType(currentTry.\type), currentTry.\catch, currentTry.\finally>;
    
    leaveTry();
    
    // Translate the 'finally' block; inlining 'finally' blocks where necessary
    code = code + [ LABEL(try_to), *trMuFinally(\finally, nowhere()/*stack()*/, c) ];
    
    // Translate the 'catch' block; inlining 'finally' blocks where necessary
    // 'Catch' block may also throw an exception, and if it is part of an outer 'try' block,
    // it has to be handled by the 'catch' blocks of the outer 'try' blocks
    
    oldTryBlocks = tryBlocks;
    tryBlocks = catchAsPartOfTryBlocks;
    finallyBlocks = oldFinallyBlocks;
    
    trMuCatch(\catch, catch_from, catchAsPartOfTry_from, catch_to, try_to, /*nowhere()*/d, c);
        
    // Restore 'try' block environment
    catchAsPartOfTryBlocks = tryBlocks;
    tryBlocks = oldTryBlocks;
    finallyBlocks = tryBlocks;
    
    // Fill in the 'catch' block entry into the current exception table
    if(!isEmpty(tryBlocks)) {
        EEntry currentCatchAsPartOfTryBlock = topCatchAsPartOfTryBlocks();
        exceptionTable += <currentCatchAsPartOfTryBlock.ranges, filterExceptionType(currentCatchAsPartOfTryBlock.\type), currentCatchAsPartOfTryBlock.\catch, currentCatchAsPartOfTryBlock.\finally>;
        leaveCatchAsPartOfTryBlocks();
    }

    return code;// + plug(stack(), d);
}

void trMuCatch(m: muCatch(str id, str fuid, AType \type, MuExp exp), str from, str fromAsPartOfTryBlock, str to, str jmpto, Dest d, CDest c) {
    
    //println("trMuCatch:");
    //println("catchBlocks = <catchBlocks>");
    //println("currentCatchBlock = <currentCatchBlock>");
    //iprintln(m);
    createTmp(id, fuid);
    createTmp(asUnwrappedThrown(id), fuid);
    oldCatchBlocks = catchBlocks;
    oldCurrentCatchBlock = currentCatchBlock;
    currentCatchBlock = size(catchBlocks);
    catchBlocks = catchBlocks + [[]];
    catchBlock = [];
    
    str catchAsPartOfTryNewLab = nextLabel();
    str catchAsPartOfTryNew_from = mkCatchFrom(catchAsPartOfTryNewLab);
    str catchAsPartOfTryNew_to = mkCatchTo(catchAsPartOfTryNewLab);
    
    // Copy 'try' block environment of the 'catch' block; needed in case of nested 'catch' blocks
    catchAsPartOfTryBlocks = [ < [<catchAsPartOfTryNew_from, catchAsPartOfTryNew_to>],
                                 entry.\type, entry.\catch, entry.\finally > | EEntry entry <- catchAsPartOfTryBlocks ];
    
    if(muBlock([]) := exp) {
    	// Avoid empty catch block for the benefit of the JVM bytecode generator
        catchBlock = [ LABEL(from), POP(), PUSHCON(123), POP(), LABEL(to), JMP(jmpto) ];
    } else {
        catchBlock = [ LABEL(from), 
                       // store a thrown value
                       POPACCU(),
                       fuid == functionScope ? STORELOC(getTmp(id,fuid)) : STOREVAR(fuid,getTmp(id,fuid)),
                       // load a thrown value,
                       fuid == functionScope ? LOADLOC(getTmp(id,fuid))  : LOADVAR(fuid,getTmp(id,fuid)), PUSHACCU(),
                       // unwrap it and store the unwrapped one in a separate local variable 
                       fuid == functionScope ? UNWRAPTHROWNLOC(getTmp(asUnwrappedThrown(id),fuid)) : UNWRAPTHROWNVAR(fuid,getTmp(asUnwrappedThrown(id),fuid)),
                       *tr(exp, /*nowhere()*/d, labelDest(jmpto)), LABEL(to), JMP(jmpto) ];
    }
    
    if(!isEmpty(catchBlocks[currentCatchBlock])) {
        catchBlocks[currentCatchBlock] = [ LABEL(catchAsPartOfTryNew_from), *catchBlocks[currentCatchBlock], LABEL(catchAsPartOfTryNew_to) ];
        for(currentCatchAsPartOfTryBlock <- catchAsPartOfTryBlocks) {
            exceptionTable += <currentCatchAsPartOfTryBlock.ranges, filterExceptionType(currentCatchAsPartOfTryBlock.\type), currentCatchAsPartOfTryBlock.\catch, currentCatchAsPartOfTryBlock.\finally>;
        }
    } else {
        catchBlocks = oldCatchBlocks;
    }
    
    currentCatchBlock = oldCurrentCatchBlock;
    
    // 'catchBlock' is always non-empty 
    //println("currentCatchBlock = <currentCatchBlock>");
    //println("catchBlocks = <catchBlocks>");
    
    catchBlocks[currentCatchBlock] = [ LABEL(fromAsPartOfTryBlock), *catchBlocks[currentCatchBlock], *catchBlock ];
    destroyTmp(id, fuid);
    destroyTmp(asUnwrappedThrown(id), fuid);
}

// TODO: Re-think the way empty 'finally' blocks are translated
INS trMuFinally(MuExp \finally, Dest d, CDest c) = (muBlock([]) := \finally) ? [ /*LOADCON(666), POP()*/ ] : tr(\finally, /*nowhere()*/d, c);

void inlineMuFinally(Dest d, CDest c) {
    
    finallyBlock = [];

    str finallyLab   = nextLabel();
    str finally_from = mkFinallyFrom(finallyLab);
    str finally_to   = mkFinallyTo(finallyLab);
    
    // Stack of 'finally' blocks to be inlined
    list[MuExp] finallyStack = [ entry.\finally | EEntry entry <- finallyBlocks ];
    
    // Make a space (hole) in the current (potentially nested) 'try' blocks to inline a 'finally' block
    if(isEmpty([ \finally | \finally <- finallyStack, !(muBlock([]) := \finally) ])) {
        return;
    }
    tryBlocks = [ <[ *head, <from,finally_from>, <finally_to + "_<size(finallyBlocks) - 1>",to>], 
                   tryBlock.\type, tryBlock.\catch, tryBlock.\finally> | EEntry tryBlock <- tryBlocks, 
                                                                         [ *tuple[str,str] head, <from,to> ] := tryBlock.ranges ];
    
    oldTryBlocks = tryBlocks;
    oldCatchAsPartOfTryBlocks = catchAsPartOfTryBlocks;
    oldFinallyBlocks = finallyBlocks;
    oldCurrentCatchBlock = currentCatchBlock;
    oldCatchBlocks = catchBlocks;
    
    // Translate 'finally' blocks as 'try' blocks: mark them with labels
    tryBlocks = []; 
    for(int i <- [0..size(finallyStack)]) {
        // The last 'finally' does not have an outer 'try' block
        if(i < size(finallyStack) - 1) {
            EEntry outerTry = finallyBlocks[i + 1];
            tryBlocks = tryBlocks + [ <[<finally_from, finally_to + "_<i>">], outerTry.\type, outerTry.\catch, outerTry.\finally> ];
        }
    }
    finallyBlocks = tryBlocks;
    catchAsPartOfTryBlocks = [];
    currentCatchBlock = size(catchBlocks);
    catchBlocks = catchBlocks + [[]];
    
    finallyBlock = [ LABEL(finally_from) ];
    for(int i <- [0..size(finallyStack)]) {
        finallyBlock = [ *finallyBlock, *trMuFinally(finallyStack[i], nowhere()/*stack()*/, c), LABEL(finally_to + "_<i>") ];
        if(i < size(finallyStack) - 1) {
            EEntry currentTry = topTry();
            // Fill in the 'catch' block entry into the current exception table
            exceptionTable += <currentTry.ranges, filterExceptionType(currentTry.\type), currentTry.\catch, currentTry.\finally>;
            leaveTry();
            leaveFinally();
        }
    }
    
    tryBlocks = oldTryBlocks;
    catchAsPartOfTryBlocks = oldCatchAsPartOfTryBlocks;
    finallyBlocks = oldFinallyBlocks;
    if(isEmpty(catchBlocks[currentCatchBlock])) {
        catchBlocks = oldCatchBlocks;
    }
    currentCatchBlock = oldCurrentCatchBlock;
    
}

// Control flow

bool containsFail(list[MuExp] exps) = optimize ? /muFail(_) := exps : false;

bool containsFail(str label) = label in failInfo.labels;

// If

INS tr(muIfelse(str label, MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart), Dest d, CDest c) {
    if(cond == muCon(true)){
      if(!containsFail(label)){
         return trblock(thenPart, d, c);
      }
    }
    if(cond == muCon(false)){
       if(!containsFail(label)){
          return trblock(elsePart, d, c);
       }
    }
    
    if(label == "") {
        label = nextLabel();
    };
    
    failLab = containsFail(label) /*|| containsFail(elsePart)*/ ? mkFail(label) : "";
    elseLab = mkElse(label);

    coro = needsCoRo(cond) ? createTmpCoRo(functionScope) : -1;
    
    thenPartCode = trblock(thenPart, d, c);
    elsePartCode = trblock(elsePart, d, c);
    res = elsePartCode == [] 
          ? [ *tr_cond(cond, coro, nextLabel(), failLab, c), *thenPartCode ]
          : (([JMP(lab)] := elsePartCode)
            ? [ *tr_cond(cond, coro, nextLabel(), failLab, labelDest(lab)), 
                *appendJmp(thenPartCode, jmp(c)) ]
            :  [ *tr_cond(cond, coro, nextLabel(), failLab, labelDest(elseLab)), 
                 *appendJmp(thenPartCode, jmp(c)),
                 LABEL(elseLab),
                 *elsePartCode
               ]);
    if(coro > 0){
       destroyTmpCoRo(functionScope);
    }
    return res;
}

// While

INS tr(muWhile(str label, MuExp cond, list[MuExp] body), Dest d, CDest c) {
    if(label == ""){
        label = nextLabel();
    }
    continueLab = mkContinue(label);
    failLab = containsFail(label) ? mkFail(label) : "";
    breakLab = mkBreak(label);
    coro = needsCoRo(cond) ? createTmpCoRo(functionScope) : -1;
    res = [ *tr_cond(cond, coro, continueLab, failLab, c),                      
            *trvoidblock(body, labelDest(continueLab)),         
            JMP(continueLab),
            LABEL(breakLab)     
          ];
    if(coro > 0){
       destroyTmpCoRo(functionScope);
    }
    return res;
}

INS tr(muBreak(str label), Dest d, CDest c) = [ jmp(labelDest(mkBreak(label))) ];

INS tr(muContinue(str label), Dest d, CDest c) = [ jmp(labelDest(mkContinue(label))) ];

INS tr(muFail(str label), Dest d, CDest c) = [ JMP(mkFail(label)) ];

INS tr(muTypeSwitch(MuExp exp, list[MuTypeCase] cases, MuExp defaultExp), Dest d, CDest c){
   defaultLab = nextLabel();
   continueLab = mkContinue(defaultLab);
   labels = [defaultLab | i <- index(toplevelTypes) ];
   caseCode =  [];
   for(cs <- cases){
       caseLab = defaultLab + "_" + cs.name;
       labels[getToplevelType(cs.name)] = caseLab;
       caseCode += [ LABEL(caseLab), *tr_arg_stack(cs.exp), JMP(continueLab) ];
   };
   caseCode += [LABEL(defaultLab), *tr_arg_stack(defaultExp), JMP(continueLab) ];
   return [ *tr_arg_accu(exp), TYPESWITCH(labels), *caseCode, LABEL(continueLab) ];
}

// muSwitch
// Each case contains a muExp in which the exit points are marked with muCaseEnd() (or a return and then nothing special
// is needed).
// First translate muCaseEnd to the auxiliary muJmpDefault (in order to make the name of the default label known
// during the translation of the case).

data MuExp = muJmpDefault(CDest cont);

INS tr(muJmpDefault(CDest cont), Dest d, CDest c) = [jmp(cont)];

INS tr(muSwitch(MuExp exp, bool useConcreteFingerprint, list[MuCase] cases, MuExp defaultExp), Dest d, CDest c){
   defaultLab = nextLabel();
   labels = ();
   caseCode =  [];
   
   INS defaultCode = tr(defaultExp, d, c);
   
   for(cs <- cases){
        caseLab = defaultLab + "_<cs.fingerprint>";
        labels[cs.fingerprint] = caseLab;
        caseExp = visit(cs.exp){ case muEndCase() => muJmpDefault(isEmpty(defaultCode) ? c : labelDest(defaultLab)) };
        caseCode += [ LABEL(caseLab), *appendJmp(tr(caseExp, d, c), jmp(c)) ];
   }
  
   if(size(cases) > 0){ 
        if(caseCode[-1] == JMP(defaultLab)){
            caseCode = caseCode[0..-1];
        }
        caseCode += [LABEL(defaultLab),  *defaultCode ];
       
        return [ *tr_arg_accu(exp), SWITCH(labels, defaultLab, useConcreteFingerprint), *caseCode ];
    } else {
        return [ *tr_arg_nowhere(exp), *defaultCode ];
    }   
}

// Multi/One/All/Or outside conditional context
    
INS tr(e:muMulti(MuExp exp), Dest d, CDest c) =
     [ *tr_arg_stack(exp),
       CREATEDYN(0),
       NEXT0(),
       *plug(accu(), d)
     ];

INS tr(e:muOne1(MuExp exp), Dest d, CDest c) =
    [ *tr_arg_stack(exp),
       CREATEDYN(0),
       NEXT0(),
       *plug(accu(), d)
     ];

// The above list of muExps is exhaustive, no other cases exist

default INS tr(MuExp e, Dest d, CDest c) { throw "mu2rvm: Unknown node in the muRascal AST: <e>"; }

/*********************************************************************/
/*      End of muRascal expressions                                  */
/*********************************************************************/


/*********************************************************************/
/*      Translate conditions                                         */
/*********************************************************************/

/*
 * The contract of tr_cond is as follows:
 * - continueLab: continue searching for more solutions for this condition
 *   (is created by the caller, but inserted in the code generated by tr_cond)
 * - failLab: continue searching for more solutions for this condition (multi expressions) or jump to falseLab when no more solutions exist (backtrack-free expressions).
 * - falseLab: location to jump to when no more solutions exist.
 *   (is created by the caller and only jumped to by code generated by tr_cond.)
 *
 * The generated code falls through to subsequent instructions when the condition is true, and jumps to falseLab otherwise.
 */

// muOne: explore one successful evaluation

INS tr_cond(muOne1(MuExp exp), int coro, str continueLab, str failLab, CDest falseDest) =
      [ LABEL(continueLab), *(isEmpty(failLab) ? [] : [ LABEL(failLab) ]) ]
    + [ *tr_arg_stack(exp), 
        CREATEDYN(0), 
        NEXT0(), 
        jmpfalse(falseDest)
      ];

// muMulti: explore all successful evaluations

INS tr_cond(muMulti(MuExp exp), int coro, str continueLab, str failLab, CDest falseDest) {
    res =  [ *tr(exp, stack(), falseDest),
             CREATEDYN(0),
             STORELOC(coro),
             *[ LABEL(continueLab), *(isEmpty(failLab) ? [] : [ LABEL(failLab)] ) ],
             LOADLOC(coro),
             NEXT0(),
             jmpfalse(falseDest)
           ];
     return res;
}

default INS tr_cond(MuExp exp, int coro, str continueLab, str failLab, CDest falseDest) {
      if(exp == muCon(true)){
        return isEmpty(failLab) ? [ LABEL(continueLab) ]
                                : [ JMP(continueLab), LABEL(failLab), jmp(falseDest), LABEL(continueLab) ];
      } else {
        return isEmpty(failLab) ? [ LABEL(continueLab),  *tr_arg_accu(exp), jmpfalse(falseDest) ]
                                : [ JMP(continueLab), LABEL(failLab), jmp(falseDest), LABEL(continueLab),  *tr_arg_accu(exp), jmpfalse(falseDest) ];
      }
}
    
bool needsCoRo(muMulti(MuExp exp)) = true;
default bool needsCoRo(MuExp _) = false;