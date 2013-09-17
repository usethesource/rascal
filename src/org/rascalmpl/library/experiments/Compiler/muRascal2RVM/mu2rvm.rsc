module experiments::Compiler::muRascal2RVM::mu2rvm

import Prelude;

import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::muRascal2RVM::StackSize;

alias INS = list[Instruction];

// Unique label generator

int nlabel = -1;
str nextLabel() { nlabel += 1; return "L<nlabel>"; }

str functionScope = "";
int nlocal = 0;

// Systematic label generation related to loops

str mkContinue(str loopname) = "CONTINUE_<loopname>";
str mkBreak(str loopname) = "BREAK_<loopname>";
str mkFail(str loopname) = "FAIL_<loopname>";
str mkElse(str loopname) = "ELSE_<loopname>";

int defaultStackSize = 25;

int newLocal() {
    n = nlocal;
    nlocal += 1;
    return n;
}

map[str,int] temporaries = ();

int getTmp(str name){
   if(temporaries[name]?)
   		return temporaries[name];
   n = newLocal();
   temporaries[name] = n;
   return n;		
}

// Does an expression produce a value? (needed for cleaning up the stack)

bool producesValue(muWhile(str label, MuExp cond, list[MuExp] body)) = false;
bool producesValue(muDo(str label, list[MuExp] body,  MuExp cond)) = false;
bool producesValue(muReturn()) = false;
bool producesValue(muNext(MuExp coro)) = false;
default bool producesValue(MuExp exp) = true;

/*********************************************************************/
/*      Translate a muRascal module                                  */
/*********************************************************************/

// Translate a muRascal module

RVMProgram mu2rvm(muModule(str module_name, list[loc] imports, map[str,Symbol] types, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initializations, map[str,int] resolver, lrel[str,list[str],list[str]] overloaded_functions), bool listing=false){
  funMap = ();
  nLabel = -1;
  temporaries = ();
  
  println("mu2rvm: Compiling module <module_name>");
 
  for(fun <- functions){
    functionScope = fun.qname;
    nlocal = fun.nlocals;
    if(listing){
    	iprintln(fun);
    }
    code = tr(fun.body);
    required_frame_size = nlocal + estimate_stack_size(fun.body);
    funMap += (fun.qname : FUNCTION(fun.qname, fun.ftype, fun.scopeIn, fun.nformals, nlocal, required_frame_size, code));
  }
  
  main_fun = getUID(module_name,[],"main",1);
  module_init_fun = getUID(module_name,[],"#module_init_main",1);
  ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
  if(!funMap[main_fun]?) {
  	 main_fun = getFUID(module_name,"main",ftype,0);
  	 module_init_fun = getFUID(module_name,"#module_init_main",ftype,0);
  }
  
  funMap += (module_init_fun : FUNCTION(module_init_fun, ftype, "" /*in the root*/, 1, size(variables) + 1, defaultStackSize, 
  									[*tr(initializations), 
  									 LOADLOC(0), 
  									 CALL(main_fun,1), // No overloading of main
  									 RETURN1(),
  									 HALT()
  									]));
 
  main_testsuite = getUID(module_name,[],"testsuite",1);
  module_init_testsuite = getUID(module_name,[],"#module_init_testsuite",1);
  if(!funMap[main_testsuite]?) { 						
  	 main_testsuite = getFUID(module_name,"testsuite",ftype,0);
  	 module_init_testsuite = getFUID(module_name,"#module_init_testsuite",ftype,0);
  }
  funMap += (module_init_testsuite : FUNCTION(module_init_testsuite, ftype, "" /*in the root*/, 1, size(variables) + 1, defaultStackSize, 
  										[*tr(initializations), 
  									 	 LOADLOC(0), 
  									 	 CALL(main_testsuite,1), // No overloading of main
  									 	 RETURN1(),
  									 	 HALT()
  										 ]));
  res = rvm(module_name, imports, types, funMap, [], resolver, overloaded_functions);
  if(listing){
    for(fname <- funMap)
  		iprintln(funMap[fname]);
  }
  return res;
}


/*********************************************************************/
/*      Translate lists of muRascal expressions                      */
/*********************************************************************/


INS  tr(list[MuExp] exps) = [ *tr(exp) | exp <- exps ];

INS tr_and_pop(MuExp exp) = producesValue(exp) ? [*tr(exp), POP()] : tr(exp);

INS trblock(list[MuExp] exps) {
  if(size(exps) == 0){
     return [LOADCON(666)]; // TODO: throw "Non void block cannot be empty";
  }
  ins = [*tr_and_pop(exp) | exp <- exps[0..-1]];
  return ins + tr(exps[-1]);
}

//default INS trblock(MuExp exp) = tr(exp);

INS trvoidblock(list[MuExp] exps){
  if(size(exps) == 0)
     return [];
  ins = [*tr_and_pop(exp) | exp <- exps];
  return ins;
}

INS tr(muBlock([MuExp exp])) = tr(exp);
default INS tr(muBlock(list[MuExp] exps)) = trblock(exps);


/*********************************************************************/
/*      Translate a single muRascal expression                       */
/*********************************************************************/

// Literals and type constants
INS tr(muBool(bool b)) = [LOADBOOL(b)];
INS tr(muCon("true")) = [LOADCON(true)];
INS tr(muCon("false")) = [LOADCON(false)];

INS tr(muInt(int n)) = [LOADINT(n)];
default INS tr(muCon(value c)) = [LOADCON(c)];

INS tr(muTypeCon(Symbol sym)) = [LOADTYPE(sym)];

// muRascal functions
INS tr(muFun(str fuid)) = [LOADFUN(fuid)];
INS tr(muFun(str fuid, str scopeIn)) = [LOAD_NESTED_FUN(fuid, scopeIn)];

// Rascal functions
INS tr(muOFun(str fuid)) = [ LOADOFUN(fuid) ];

INS tr(muConstr(str fuid)) = [LOADCONSTR(fuid)];

// Variables and assignment

INS tr(muVar(str id, str fuid, int pos)) = [fuid == functionScope ? LOADLOC(pos) : LOADVAR(fuid, pos)];
INS tr(muLoc(str id, int pos)) = [LOADLOC(pos)];
INS tr(muTmp(str id)) = [LOADLOC(getTmp(id))];

INS tr(muLocDeref(str name, int pos)) = [ LOADLOCDEREF(pos) ];
INS tr(muVarDeref(str name, str fuid, int pos)) = [ fuid == functionScope ? LOADLOCDEREF(pos) : LOADVARDEREF(fuid, pos) ];

INS tr(muLocRef(str name, int pos)) = [ LOADLOCREF(pos) ];
INS tr(muVarRef(str name, str fuid, int pos)) = [ fuid == functionScope ? LOADLOCREF(pos) : LOADVARREF(fuid, pos) ];

INS tr(muAssignLocDeref(str id, int pos, MuExp exp)) = [ *tr(exp), STORELOCDEREF(pos) ];
INS tr(muAssignVarDeref(str id, str fuid, int pos, MuExp exp)) = [ *tr(exp), fuid == functionScope ? STORELOCDEREF(pos) : STOREVARDEREF(fuid, pos) ];

INS tr(muAssign(str id, str fuid, int pos, MuExp exp)) = [*tr(exp), fuid == functionScope ? STORELOC(pos) : STOREVAR(fuid, pos)];
INS tr(muAssignLoc(str id, int pos, MuExp exp)) = [*tr(exp), STORELOC(pos) ];
INS tr(muAssignTmp(str id, MuExp exp)) = [*tr(exp), STORELOC(getTmp(id)) ];

// Calls

// Constructor
INS tr(muCallConstr(str fuid, list[MuExp] args)) = [ *tr(args), CALLCONSTR(fuid, size(args)) ];

// muRascal functions
INS tr(muCall(muFun(str fuid), list[MuExp] args)) = [*tr(args), CALL(fuid, size(args))];
INS tr(muCall(muConstr(str fuid), list[MuExp] args)) = [*tr(args), CALLCONSTR(fuid, size(args))];
INS tr(muCall(MuExp fun, list[MuExp] args)) = [*tr(args), *tr(fun), CALLDYN(size(args))];

// Rascal functions
INS tr(muOCall(muOFun(str fuid), list[MuExp] args)) = [*tr(args), OCALL(fuid, size(args))];
INS tr(muOCall(MuExp fun, set[Symbol] types, list[MuExp] args)) 
	= { list[MuExp] targs = [ muTypeCon(t) | t <- types ]; 
		[ *tr(targs), CALLMUPRIM("make_array",size(targs)), // this order is to optimize the stack size
		  *tr(args), 
		  *tr(fun), 
		  OCALLDYN(size(args))]; 
	  };

INS tr(muCallPrim(str name, list[MuExp] args)) = (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLPRIM(name, size(args))];

INS tr(muCallMuPrim(str name, list[MuExp] args)) =  (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLMUPRIM(name, size(args))];

INS tr(muCallJava(str name, str class, Symbol types, list[MuExp] args)) = [ *tr(args), CALLJAVA(name, class, types) ];
// Return

INS tr(muReturn()) = [RETURN0()];
INS tr(muReturn(MuExp exp)) = [*tr(exp), RETURN1()];
INS tr(muFailReturn()) = [ FAILRETURN() ];

// Coroutines

INS tr(muCreate(muFun(str fuid))) = [CREATE(fuid, 0)];
INS tr(muCreate(MuExp fun)) = [ *tr(fun), CREATEDYN(0) ];
INS tr(muCreate(muFun(str fuid), list[MuExp] args)) = [ *tr(args), CREATE(fuid, size(args)) ];
INS tr(muCreate(MuExp fun, list[MuExp] args)) = [ *tr(args), *tr(fun), CREATEDYN(size(args)) ];

INS tr(muInit(MuExp exp)) = [*tr(exp), INIT(0)];
INS tr(muInit(MuExp coro, list[MuExp] args)) = [*tr(args), *tr(coro),  INIT(size(args))];  // order!

INS tr(muHasNext(MuExp coro)) = [*tr(coro), HASNEXT()];

INS tr(muNext(MuExp coro)) = [*tr(coro), NEXT0()];
INS tr(muNext(MuExp coro, list[MuExp] args)) = [*tr(args), *tr(coro),  NEXT1()]; // order!

INS tr(muYield()) = [YIELD0()];
INS tr(muYield(MuExp exp)) = [*tr(exp), YIELD1()];

// Control flow

// If

INS tr(muIfelse(str label, MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)) {
    if(label == "") {
    	label = nextLabel();
    };
    elseLab = mkElse(label);
    continueLab = mkContinue(label);
    // Use the dummy label to backtrack in case of fail (continue-after-failure label);	
    dummyLab = mkFail(label); //dummyLab = nextLabel();
//  println("ifelse: elseLab = <elseLab>, continueLab = <continueLab>, dummyLab = <dummyLab>");
    return [ *tr_cond(cond, dummyLab, elseLab), 
             *(isEmpty(thenPart) ? LOADCON(111) : trblock(thenPart)),
             JMP(continueLab), 
             LABEL(elseLab),
             *(isEmpty(elsePart) ? LOADCON(222) : trblock(elsePart)),
             LABEL(continueLab)
           ];
}

// While

INS tr(muWhile(str label, MuExp cond, list[MuExp] body)) {
    if(label == ""){
    	label = nextLabel();
    }	
    continueLab = mkContinue(label);
    breakLab = mkBreak(label);
//    println("while: continueLab = <continueLab>, breakLab = <breakLab>");
    return [ *tr_cond(cond, continueLab, breakLab), 	 					
    		 *trvoidblock(body),			
    		 JMP(continueLab),
    		 LABEL(breakLab)		
    		];
}
// Do

INS tr(muDo(str label, list[MuExp] body, MuExp cond)) {
    if(label == ""){
    	label = nextLabel();
    }
    continueLab = mkContinue(label);
    breakLab = mkBreak(label);
    return [ LABEL(continueLab),
     		 *trvoidblock(body),	
             *tr_cond_do(cond, continueLab, breakLab),	
    		 JMP(continueLab),
    		 LABEL(breakLab)		
           ];
}

INS tr(muBreak(str label)) = [ JMP(mkBreak(label)) ];
INS tr(muContinue(str label)) = [ JMP(mkContinue(label)) ];
INS tr(muFail(str label)) = [ JMP(mkFail(label)) ];

// Multi/One/All outside conditional context
    
default INS tr(e: muMulti(MuExp exp)) = 
	 [ *tr(exp),
       INIT(0),
       NEXT0()
    ];
    //when bprintln("tr outer muMulti: <e>");
    
INS tr(e:muOne(list[MuExp] exps)) {
  bprintln("tr outer muOne: <e>");
  dummyLab = nextLabel();
  failLab = nextLabel();
  afterLab = nextLabel();
  return
     [ *tr_cond(muAll(exps), dummyLab, failLab),
       LOADCON(true),
       JMP(afterLab),
       LABEL(failLab),
       LOADCON(false),
       LABEL(afterLab)
     ];
}

INS tr(e:muAll(list[MuExp] exps)) { 
    println("tr outer muAll: <e>");
    
    startLab = nextLabel();
    //continueLab = nextLabel();
    failLab = nextLabel();
    currentFail = failLab;
    afterLab = nextLabel();
    
    lastMulti = -1;
    for(i <- index(exps)){
        if(muMulti(exp1) := exps[i]){
           lastMulti = i;
        }
    }
    
    code = [ JMP(startLab),
             LABEL(failLab),
             LOADCON(false),
             JMP(afterLab),
             LABEL(startLab)
           ];
    for(i <- index(exps)){
        exp = exps[i];
        if(muMulti(exp1) := exp){
           newFail = nextLabel();
           co = newLocal();
           code += [ *tr(exp1), 
          		     INIT(0), 
          		     STORELOC(co), 
          		     POP(),
          		     LABEL(newFail),
          		     LOADLOC(co), 
          		     HASNEXT(), 
          		     JMPFALSE(currentFail), 
          		     LOADLOC(co),
          		     NEXT0(), 
          		     JMPFALSE(currentFail)
          		   ];
          currentFail = newFail;
        } else {
          code += [ *tr(exp), 
          		    JMPFALSE(currentFail)
          		  ];
        } 
    }
    code += [ LOADCON(true),
              LABEL(afterLab)
    		 ];
    return code;   
}

// The above list of muExps is exhaustive, no other cases exist

default INS tr(e) { throw "Unknown node in the muRascal AST: <e>"; }

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
 * - failLab: location ot jump to whe no more solutions exist.
 *   (is created by the caller and only jumped to by code generated by tr_cond.)
 *
 * The generated code falls through to subsequent instructions when the condition is true, and jumps to failLab otherwise.
 */

// muOne: explore one successfull evaluation

INS tr_cond(e: muOne(list[MuExp] exps), str continueLab, str failLab){
    //println("tr_cond: <e>");
    code = [LABEL(continueLab)];
    for(exp <- exps){
        if(muMulti(exp1) := exp){
          code += [*tr(exp1), 
          		   INIT(0), 
          		   NEXT0(), 
          		   JMPFALSE(failLab)
          		  ];
        } else {
          code += [*tr(exp), 
          		   JMPFALSE(failLab)
          		  ];
        } 
    } 
    return code;   
}

// Special case for do_while:
// - continueLab is inserted by caller.

INS tr_cond_do(muOne(list[MuExp] exps), str continueLab, str failLab){
    code = [];
    for(exp <- exps){
        if(muMulti(exp1) := exp){
          code += [*tr(exp1), 
          		   INIT(0), 
          		   NEXT0(), 
          		   JMPFALSE(failLab)
          		  ];
        } else {
          code += [*tr(exp), 
          		   JMPFALSE(failLab)
          		  ];
        } 
    } 
    return code;   
}

// muAll: explore all sucessfull evaluations

INS tr_cond(e: muAll(list[MuExp] exps), str continueLab, str failLab){
    //println("tr_cond : <e>");
    code = [];
    lastMulti = -1;
    
    for(i <- index(exps)){
        if(muMulti(exp1) := exps[i]){
           lastMulti = i;
        }
    }
    startLab = nextLabel();
    currentFail = failLab;
    
    if(lastMulti == -1)
       code = [ JMP(startLab),
                LABEL(continueLab),
                JMP(failLab),
                LABEL(startLab)
              ];
 
    for(i <- index(exps)){
        exp = exps[i];
        if(muMulti(exp1) := exp){
          newFail = nextLabel();
          co = newLocal();
          code += [ *tr(exp1), 
          		    INIT(0), 
          		    STORELOC(co), 
          		    POP(),
           	        LABEL(newFail),
          			*((i == lastMulti) ? [LABEL(continueLab)] :[]),
          		    LOADLOC(co), 
          		    HASNEXT(), 
          		    JMPFALSE(currentFail), 
          		    LOADLOC(co),
          		    NEXT0(), 
          		    JMPFALSE(currentFail)
          		  ];
          currentFail = newFail;
        } else {
          code += [*tr(exp), 
          		   JMPFALSE(currentFail)
          		  ];
        } 
    }
    return code;
}

INS tr_cond(e: muMulti(MuExp exp), str continueLab, str failLab) =
    [ LABEL(continueLab),
      *tr(exp),
      INIT(0),
      NEXT0(),
      JMPFALSE(failLab)
    ];
    //when bprintln("tr_cond: <e>");

default INS tr_cond(MuExp exp, str continueLab, str failLab) = [ LABEL(continueLab), *tr(exp), JMPFALSE(failLab) ];
    //when bprintln("default tr_cond: <exp>");
    
