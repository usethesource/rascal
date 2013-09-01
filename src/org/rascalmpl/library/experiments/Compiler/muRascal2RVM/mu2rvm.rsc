module experiments::Compiler::muRascal2RVM::mu2rvm

import Prelude;

import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::TypeUtils;

alias INS = list[Instruction];

// Unique label generator

int nlabel = -1;
str nextLabel() { nlabel += 1; return "L<nlabel>"; }

str functionScope = "";
int nlocal = 0;

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

// Translate a muRascal module
RVMProgram mu2rvm(muModule(str name, list[Symbol] types, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initializations), bool listing=false){
  funMap = ();
  nLabel = -1;
  temporaries = ();
  
  for(fun <- functions) {
    functionScope = fun.qname;
    nlocal = fun.nlocals;
    code = trblock(fun.body);
    funMap += (fun.qname : FUNCTION(fun.qname, fun.nformals, nlocal, 20, code));
  }
  
  main_fun = getUID(name,[],"main",1);
  module_init_fun = getUID(name,[],"#module_init",0);
  if(!funMap[main_fun]?) {
  	main_fun = getFUID(name,"main",Symbol::func(Symbol::\value(),[Symbol::\list(\value())]),0);
  	module_init_fun = getFUID(name,"#module_init",Symbol::func(Symbol::\value(),[]),0);
  }
  funMap += (module_init_fun : FUNCTION(module_init_fun, 0, size(variables) + 1, 10, 
  									[*tr(initializations), 
  									 LOADLOC(0), 
  									 CALL(main_fun), 
  									 RETURN1(),
  									 HALT()
  									]));
  res = rvm(types, funMap, []);
  if(listing){
    for(fname <- funMap, fname != module_init_fun, fname notin libFuns)
  		iprintln(funMap[fname]);
  }
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

INS tr(muFun(str fuid)) = [LOADFUN(fuid)];
INS tr(muFun(str fuid, str scopeIn)) = [LOAD_NESTED_FUN(fuid, scopeIn)];

INS tr(muConstr(str fuid)) = [LOADCONSTR(fuid)];

INS tr(muVar(str id, str fuid, int pos)) = [fuid == functionScope ? LOADLOC(pos) : LOADVAR(fuid, pos)];
INS tr(muLoc(str id, int pos)) = [LOADLOC(pos)];
INS tr(muTmp(str id)) = [LOADLOC(getTmp(id))];

//Instruction mkCall(str name) = CALL(name); 

INS tr(muCallConstr(str fuid, list[MuExp] args)) = [ *tr(args), CALLCONSTR(fuid) ];

INS tr(muCall(muFun(str fuid), list[MuExp] args)) = [*tr(args), CALL(fuid)];
INS tr(muCall(muConstr(str fuid), list[MuExp] args)) = [*tr(args), CALLCONSTR(fuid)];
INS tr(muCall(MuExp fun, list[MuExp] args)) = [*tr(args), *tr(fun), CALLDYN()];

INS tr(muCallPrim(str name, list[MuExp] args)) = (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLPRIM(name, size(args))];

INS tr(muCallMuPrim(str name, list[MuExp] args)) =  (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLMUPRIM(name, size(args))];

INS tr(muAssign(str id, str fuid, int pos, MuExp exp)) = [*tr(exp), fuid == functionScope ? STORELOC(pos) : STOREVAR(fuid, pos)];
INS tr(muAssignLoc(str id, int pos, MuExp exp)) = [*tr(exp), STORELOC(pos) ];
INS tr(muAssignTmp(str id, MuExp exp)) = [*tr(exp), STORELOC(getTmp(id)) ];

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

INS tr(muCreate(muFun(str fuid))) = [CREATE(fuid, 0)];
INS tr(muCreate(MuExp fun)) = [ *tr(fun), CREATEDYN(0) ];
INS tr(muCreate(muFun(str fuid), list[MuExp] args)) = [ *tr(args), CREATE(fuid, size(args)) ];
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
    
INS tr(e:muOne(list[MuExp] exps)) {
  fail_lab = nextLabel();
  more_lab = nextLabel();
  after_lab = nextLabel();
  return
     [ *tr_cond(muAll(exps), more_lab, fail_lab),
       LOADCON(true),
       JMP(after_lab),
       LABEL(fail_lab),
       LOADCON(false),
       LABEL(after_lab)
     ];
}

INS tr(e:muAll(list[MuExp] exps)) {  // TODO: not complete yet
    failLab = nextLabel();
    moreLab = nextLabel();
    afterLab = nextLabel();
    generators = []; 
    multis = ();
    code = [];
    for(i <- index(exps)){
        if(muMulti(exp1) := exps[i]){
           gen = newLocal();
           generators += gen;
           multis[i] = gen;
           code += [*tr(exp1), 
          		    INIT(0), 
          		    STORELOC(gen), 
          		    POP()
          		   ];
        }
    }
    code += [LABEL(moreLab)];
    for(i <- index(exps)){
        exp = exps[i];
        if(muMulti(exp1) := exp){
          gen = multis[i];
          code += [LOADLOC(gen), 
          		   HASNEXT(), 
          		   JMPFALSE(failLab), 
          		   LOADLOC(gen),
          		   NEXT0(), 
          		   JMPFALSE(failLab)
          		  ];
        } else {
          code += [*tr(exp), 
          		   JMPFALSE(failLab)
          		  ];
        } 
    }
    for(gen <- generators){
      code += [ LOADLOC(gen), 
     		    HASNEXT(),
     		    JMPTRUE(moreLab)
     		  ];
    }
    code += [ LOADCON(true),
              JMP(afterLab),
              LABEL(failLab),
              LOADCON(false),
              LABEL(afterLab)
    		 ];
    return code;   
}
    
    
INS tr(muLocDeref(str name, int pos)) = [ LOADLOCDEREF(pos) ];
INS tr(muVarDeref(str name, str fuid, int pos)) = [ fuid == functionScope ? LOADLOCDEREF(pos) : LOADVARDEREF(fuid, pos) ];

INS tr(muLocRef(str name, int pos)) = [ LOADLOCREF(pos) ];
INS tr(muVarRef(str name, str fuid, int pos)) = [ fuid == functionScope ? LOADLOCREF(pos) : LOADVARREF(fuid, pos) ];

INS tr(muAssignLocDeref(str id, int pos, MuExp exp)) = [ *tr(exp), STORELOCDEREF(pos) ];
INS tr(muAssignVarDeref(str id, str fuid, int pos, MuExp exp)) = [ *tr(exp), fuid == functionScope ? STORELOCDEREF(pos) : STOREVARDEREF(fuid, pos) ];

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
    code = lastMulti == -1 ? [] : [LABEL(moreLab)];
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
