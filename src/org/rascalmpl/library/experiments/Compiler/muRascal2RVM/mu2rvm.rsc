module experiments::Compiler::muRascal2RVM::mu2rvm

import Prelude;

import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;


//import experiments::Compiler::muRascal2RVM::Library;

alias INS = list[Instruction];

// Unique label generator

int nlabel = -1;
str nextLabel() { nlabel += 1; return "L<nlabel>"; }

int functionScope = 0;
int nlocal = 0;

int newLocal() {
    n = nlocal;
    nlocal += 1;
    return n;
}

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;

// Translate a muRascal module

RVMProgram mu2rvm(muModule(str name, list[Symbol] types, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initializations), bool listing=false){
  funMap = ();
  nLabel = -1;
  libraryScope = 1000000; 
  
  libModule = parse(Library);
 
  for(fun <-libModule.functions){
     funMap += (fun.name : FUNCTION(fun.name, libraryScope, fun.nformal, fun.nlocal, 20, trblock(fun.body)));
     libraryScope += 1;
  }
 
  for(fun <- functions){
    functionScope = fun.scope;
    nlocal = fun.nlocal;
    code = trblock(fun.body);
    funMap += (fun.name : FUNCTION(fun.name, fun.scope, fun.nformal, nlocal, 20, code));
  }
  
  funMap += ("#module_init" : FUNCTION("#module_init", libraryScope, 0, size(variables) + 1, 10, 
  									[*tr(initializations), 
  									 LOADLOC(size(variables)), 
  									 CALL("main"), 
  									 RETURN1(),
  									 HALT()
  									]));
  res = rvm(types, funMap, []);
  if(listing)
  	iprintln(res);
  return res;
}

// Translate a muRascal function

// Translate lists of muRascal expressions

INS  tr(list[MuExp] exps) = [ *tr(exp) | exp <- exps ];

INS tr_and_pop(MuExp exp) = producesValue(exp) ? [*tr(exp), POP()] : tr(exp);


INS trvoidblock(list[MuExp] exps) {
  if(size(exps) == 0)
     return [];
  ins = [*tr_and_pop(exp) | exp <- exps];
  return ins;
}

INS trblock(list[MuExp] exps) {
  if(size(exps) == 0){
     return [LOADCON(666)]; // TODO: throw "Non void block cannot be empty";
  }
  ins = [*tr_and_pop(exp) | exp <- exps[0..-1]];
  return ins + tr(exps[-1]);
}

// Translate a single muRascal expression

INS tr(muCon("true")) = [LOADCON(true)];
INS tr(muCon("false")) = [LOADCON(false)];

default INS tr(muCon(value c)) = [LOADCON(c)];

INS tr(muBool(bool b)) = [LOADBOOL(b)];
INS tr(muInt(int n)) = [LOADINT(n)];


INS tr(muTypeCon(Symbol sym)) = [LOADTYPE(sym)];

INS tr(muFun(str name)) = [LOADFUN(name)];

INS tr(muFun(str name, int scope)) = [LOAD_NESTED_FUN(name, scope)];

INS tr(muConstr(str name)) = [LOADCONSTR(name)];

INS tr(muVar(str id, int scope, int pos)) = [scope == functionScope ? LOADLOC(pos) : LOADVAR(scope, pos)];
INS tr(muLoc(str id, int pos)) = [LOADLOC(pos)];

Instruction mkCall(str name) = CALL(name); 

INS tr(muCallConstr(str cname, list[MuExp] args)) = [ *tr(args), CALLCONSTR(cname) ];

INS tr(muCall(muFun(str fname), list[MuExp] args)) = [*tr(args), CALL(fname)];
INS tr(muCall(muConstr(str cname), list[MuExp] args)) = [*tr(args), CALLCONSTR(cname)];
INS tr(muCall(MuExp fun, list[MuExp] args)) = [*tr(args), *tr(fun), CALLDYN()];

INS tr(muCallPrim(str name, list[MuExp] args)) = (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLPRIM(name, size(args))];

INS tr(muCallMuPrim(str name, list[MuExp] args)) =  (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLMUPRIM(name, size(args))];

INS tr(muAssign(str id, int scope, int pos, MuExp exp)) = [*tr(exp), scope == functionScope ? STORELOC(pos) : STOREVAR(scope, pos)];
INS tr(muAssignLoc(str id, int pos, MuExp exp)) = [*tr(exp), STORELOC(pos) ];

INS tr(muIfelse(MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)) {
    lab_else = nextLabel();
    lab_after = nextLabel();		
    return [ *tr_cond(cond, lab_after, lab_else), 
             *trblock(thenPart), 
             JMP(lab_after), 
             LABEL(lab_else),
             *trblock(elsePart),
             LABEL(lab_after)
            ];
}

default INS tr(muWhile(MuExp cond, list[MuExp] body)) {
    lab_while = nextLabel();
    lab_after = nextLabel();
    return [//LABEL(lab_while),
    		*tr_cond(cond, lab_while, lab_after), 	 					
    		*trvoidblock(body),			
    		JMP(lab_while),
    		LABEL(lab_after)		
    		];
}

INS tr(muCreate(muFun(str name))) = [CREATE(name)];
INS tr(muCreate(MuExp fun)) = [ *tr(fun), CREATEDYN(0) ];
INS tr(muCreate(muFun(str name), list[MuExp] args)) = [ *tr(args), CREATE(name, size(args)) ];
INS tr(muCreate(MuExp fun, list[MuExp] args)) = [ *tr(args), *tr(fun), CREATEDYN(size(args)) ];

INS tr(muInit(MuExp exp)) = [*tr(exp), INIT(0)];
INS tr(muInit(MuExp coro, list[MuExp] args)) = [*tr(args), *tr(coro),  INIT(size(args))];  // order!

INS tr(muNext(MuExp coro)) = [*tr(coro), NEXT0()];
INS tr(muNext(MuExp coro, list[MuExp] args)) = [*tr(args), *tr(coro),  NEXT1()]; // order!

INS tr(muYield()) = [YIELD0()];
INS tr(muYield(MuExp exp)) = [*tr(exp), YIELD1()];

INS tr(muReturn()) = [RETURN0()];
INS tr(muReturn(MuExp exp)) = [*tr(exp), RETURN1()];

INS tr(muHasNext(MuExp coro)) = [*tr(coro), HASNEXT()];

INS tr(muMulti(MuExp exp)) = 
	 [ *tr(exp),
       INIT(0),
       NEXT0()
    ];
    
INS tr(muLocDeref(str name, int pos)) = [ LOADLOCDEREF(pos) ];
INS tr(muVarDeref(str name, int scope, int pos)) = [ scope == functionScope ? LOADLOCDEREF(pos) : LOADVARDEREF(scope, pos) ];

INS tr(muLocRef(str name, int pos)) = [ LOADLOCREF(pos) ];
INS tr(muVarRef(str name, int scope, int pos)) = [ scope == functionScope ? LOADLOCREF(pos) : LOADVARREF(scope, pos) ];

INS tr(muAssignLocDeref(str id, int pos, MuExp exp)) = [ *tr(exp), STORELOCDEREF(pos) ];
INS tr(muAssignVarDeref(str id, int scope, int pos, MuExp exp)) = [ *tr(exp), scope == functionScope ? STORELOCDEREF(pos) : STOREVARDEREF(scope, pos) ];

default INS tr(e) { throw "Unknown node in the muRascal AST: <e>"; }

// Does an expression produce a value? (needed for cleaning up the stack)

bool producesValue(muWhile(MuExp cond, list[MuExp] body)) = false;
bool producesValue(muReturn()) = false;
bool producesValue(muNext(MuExp coro)) = false;
default bool producesValue(MuExp exp) = true;

// Translate a condition, given a failure continuation.

// muOne: explore one successfull evaluation

INS tr_cond(muOne(list[MuExp] exps), str moreLab, str failLab){
    code = [LABEL(moreLab)];
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

INS tr_cond(muAll(list[MuExp] exps), str moreLab, str failLab){

    lastMulti = -1;
    for(i <- index(exps)){
        if(muMulti(_) := exps[i])
           lastMulti = i;
    }
    currentFail = failLab;
    code = lastMulti == -1 ? [LABEL(moreLab)]  : [];
    for(i <- index(exps)){
        exp = exps[i];
        if(muMulti(exp1) := exp){
          newFail = nextLabel();
          co = newLocal();
          code += [*tr(exp1), 
          		   INIT(0), 
          		   STORELOC(co), 
          		   POP(),
          		   *((i == lastMulti) ? [LABEL(moreLab)] : []),
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
          code += [*tr(exp), 
          		   JMPFALSE(currentFail)
          		  ];
        } 
    }
    return code;
}

INS tr_cond(muMulti(MuExp exp), str moreLab, str failLab) =
    [ LABEL(moreLab),
       *tr(exp),
       INIT(0),
       NEXT0(),
       JMPFALSE(failLab)
    ];

default INS tr_cond(MuExp exp, str moreLab, str failLab) = [ LABEL(moreLab), *tr(exp), JMPFALSE(failLab) ];
