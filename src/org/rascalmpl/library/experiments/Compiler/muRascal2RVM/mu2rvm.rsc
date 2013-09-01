module experiments::Compiler::muRascal2RVM::mu2rvm

import Prelude;

import experiments::Compiler::RVM::AST;

import experiments::Compiler::muRascal::Syntax;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;


alias INS = list[Instruction];

// Unique label generator

int nlabel = -1;
str nextLabel() { nlabel += 1; return "L<nlabel>"; }

// Systematic label generation related to loops

str mkContinue(str loopname) = "CONTINUE_<loopname>";
str mkBreak(str loopname) = "BREAK_<loopname>";
str mkFail(str loopname) = "FAIL_<loopname>";
str mkFail(str loopname) = "DUMMY_<loopname>";

int functionScope = 0;
int libraryScope = 1000000;
int nlocal = 0;

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

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;
public loc LibraryPrecompiled = |std:///experiments/Compiler/muRascal2RVM/Library.muast|;

map[str,Declaration] parseLibrary(){
    println("mu2rvm: Recompiling library.mu");
 	libModule = parse(Library);
 	funMap = ();
 
  	for(fun <- libModule.functions){
    	funMap += (fun.name : FUNCTION(fun.name, libraryScope, fun.nformal, fun.nlocal, defaultStackSize, trblock(fun.body)));
        libraryScope += 1;
  	}
  
  	writeTextValueFile(LibraryPrecompiled, funMap);
    println("mu2rvm: Written compiled version of Library.mu");
  	
  	return funMap;
}

// Translate a muRascal module

RVMProgram mu2rvm(muModule(str name, list[Symbol] types, list[MuFunction] functions, list[MuVariable] variables, list[MuExp] initializations), bool listing=false){
  funMap = ();
  nLabel = -1;
  temporaries = ();
   
  println("mu2rvm: Compiling module <name>");
  
  if(exists(LibraryPrecompiled) && lastModified(LibraryPrecompiled) > lastModified(Library)){
     try {
  	       funMap = readTextValueFile(#map[str,Declaration], LibraryPrecompiled);
  	       println("mu2rvm: Using precompiled version of Library.mu");
  	 } catch:
  	       funMap = parseLibrary();
  } else {
    funMap = parseLibrary();
  }
  
  library_names = domain(funMap);
  // println("<size(library_names)> functions in muRascal library:\n<library_names>");
 
  for(fun <- functions){
    functionScope = fun.scope;
    nlocal = fun.nlocal;
    code = trblock(fun.body);
    funMap += (fun.name : FUNCTION(fun.name, fun.scope, fun.nformal, nlocal, defaultStackSize, code));
  }
  
  funMap += ("#module_init_main" : FUNCTION("#module_init_main", libraryScope, 1, size(variables) + 1, 10, 
  									[*tr(initializations), 
  									 LOADLOC(0), 
  									 CALL("main", 1), 
  									 RETURN1(),
  									 HALT()
  									]));
  									
 
 funMap += ("#module_init_testsuite" : FUNCTION("#module_init_testsuite", libraryScope, 1, size(variables) + 1, 10, 
  									[*tr(initializations), 
  									 LOADLOC(0), 
  									 CALL("testsuite", 1), 
  									 RETURN1(),
  									 HALT()
  									]));
  res = rvm(types, funMap, []);
  if(listing){
    for(fname <- funMap /*, fname notin library_names*/)
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

INS tr(muFun(str name)) = [LOADFUN(name)];

INS tr(muFun(str name, int scope)) = [LOAD_NESTED_FUN(name, scope)];

INS tr(muConstr(str name)) = [LOADCONSTR(name)];

INS tr(muVar(str id, int scope, int pos)) = [scope == functionScope ? LOADLOC(pos) : LOADVAR(scope, pos)];
INS tr(muLoc(str id, int pos)) = [LOADLOC(pos)];
INS tr(muTmp(str id)) = [LOADLOC(getTmp(id))];

INS tr(muCallConstr(str cname, list[MuExp] args)) = [ *tr(args), CALLCONSTR(cname, size(args)) ];

INS tr(muCall(muFun(str fname), list[MuExp] args)) = [*tr(args), CALL(fname, size(args))];
INS tr(muCall(muConstr(str cname), list[MuExp] args)) = [*tr(args), CALLCONSTR(cname, size(args))];
INS tr(muCall(MuExp fun, list[MuExp] args)) = [*tr(args), *tr(fun), CALLDYN(size(args))];

INS tr(muCallPrim(str name, list[MuExp] args)) = (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLPRIM(name, size(args))];

INS tr(muCallMuPrim(str name, list[MuExp] args)) =  (name == "println") ? [*tr(args), PRINTLN(size(args))] : [*tr(args), CALLMUPRIM(name, size(args))];

INS tr(muAssign(str id, int scope, int pos, MuExp exp)) = [*tr(exp), scope == functionScope ? STORELOC(pos) : STOREVAR(scope, pos)];
INS tr(muAssignLoc(str id, int pos, MuExp exp)) = [*tr(exp), STORELOC(pos) ];
INS tr(muAssignTmp(str id, MuExp exp)) = [*tr(exp), STORELOC(getTmp(id)) ];

INS tr(muIfelse(MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)) {
    elseLab = mkFail(nextLabel());
    continueLab = mkContinue(nextLabel());	
    dummyLab = nextLabel();	
//    println("ifelse: elseLab = <elseLab>, continueLab = <continueLab>, dummyLab = <dummyLab>");
    return [ *tr_cond(cond, dummyLab, elseLab), 
             *trblock(thenPart), 
             JMP(continueLab), 
             LABEL(elseLab),
             *trblock(elsePart),
             LABEL(continueLab)
           ];
}

INS tr(muWhile(str label, MuExp cond, list[MuExp] body)) {
    if(label == "")
    	label = nextLabel();
    continueLab = mkContinue(label);
    breakLab = mkBreak(label);
//    println("while: continueLab = <continueLab>, breakLab = <breakLab>");
    return [ *tr_cond(cond, continueLab, breakLab), 	 					
    		 *trvoidblock(body),			
    		 JMP(continueLab),
    		 LABEL(breakLab)		
    		];
}

INS tr(muDo(str label, list[MuExp] body, MuExp cond)) {
    if(label == "")
    	label = nextLabel();
    continueLab = mkContinue(label);
    dummyLab = mkDummy(label);
    breakLab = mkBreak(label);
    return [ LABEL(continueLab),
     		 *trvoidblock(body),	
             *tr_cond(cond, dummyLab, breakLab),	
    		 JMP(continueLab),
    		 LABEL(breakLab)		
           ];
}

INS tr(muBreak(str label)) = [ JMP(mkBreak(label)) ];
INS tr(muContinue(str label)) = [ JMP(mkContinue(label)) ];
INS tr(muFail(str label)) = [ JMP(mkFail(label)) ];

INS tr(muFailReturn()) = [ FAILRETURN() ];

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
    
INS tr(e:muOne(list[MuExp] exps)) {
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

INS tr(e:muAll(list[MuExp] exps)) {  // TODO: not complete yet
    continueLab = nextLabel();
    failLab = nextLabel();
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
    code += [LABEL(continueLab)];
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
     		    JMPTRUE(continueLab)
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
INS tr(muVarDeref(str name, int scope, int pos)) = [ scope == functionScope ? LOADLOCDEREF(pos) : LOADVARDEREF(scope, pos) ];

INS tr(muLocRef(str name, int pos)) = [ LOADLOCREF(pos) ];
INS tr(muVarRef(str name, int scope, int pos)) = [ scope == functionScope ? LOADLOCREF(pos) : LOADVARREF(scope, pos) ];

INS tr(muAssignLocDeref(str id, int pos, MuExp exp)) = [ *tr(exp), STORELOCDEREF(pos) ];
INS tr(muAssignVarDeref(str id, int scope, int pos, MuExp exp)) = [ *tr(exp), scope == functionScope ? STORELOCDEREF(pos) : STOREVARDEREF(scope, pos) ];

default INS tr(e) { throw "Unknown node in the muRascal AST: <e>"; }

// Does an expression produce a value? (needed for cleaning up the stack)

bool producesValue(muWhile(str label, MuExp cond, list[MuExp] body)) = false;
bool producesValue(muReturn()) = false;
bool producesValue(muNext(MuExp coro)) = false;
default bool producesValue(MuExp exp) = true;

// Translate a condition.
// The contract of tr_cond is as follows:
// - continueLab: continue searching for more solutions for this condition
//   (is created by the caller, but inserted in the code generated by tr_cond)
// - failLab: location ot jump to whe no more solutions exist.
//   (is created by the caller and only jumped to by code generated by tr_cond.)
//
// The generated code falls through to subsequent instructions when the condition is true, and jumps to failLab otherwise.

// muOne: explore one successfull evaluation

INS tr_cond(muOne(list[MuExp] exps), str continueLab, str failLab){
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

// muAll: explore all sucessfull evaluations

INS tr_cond(muAll(list[MuExp] exps), str continueLab, str failLab){
    code = [];
    lastMulti = -1;
    generators = ();
    for(i <- index(exps)){
        if(muMulti(exp1) := exps[i]){
           lastMulti = i;
           co = newLocal();
           generators[i] = co;
           code += [ *tr(exp1), 
          		     INIT(0), 
          		     STORELOC(co), 
          		     POP()
          		   ];
        }
    }
    if(size(code) == 0){
       startLab = nextLabel();
       code += [ JMP(startLab),
       	         LABEL(continueLab),
                 JMP(failLab),
                 LABEL(startLab) 
               ];
    }
    currentFail = failLab;
 
    for(i <- index(exps)){
        exp = exps[i];
        if(muMulti(exp1) := exp){
          newFail = nextLabel();
          co = generators[i];
          code += [ LABEL(newFail),
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

INS tr_cond(muMulti(MuExp exp), str continueLab, str failLab) =
    [ LABEL(continueLab),
      *tr(exp),
      INIT(0),
      NEXT0(),
      JMPFALSE(failLab)
    ];

default INS tr_cond(MuExp exp, str continueLab, str failLab) = [ LABEL(continueLab), *tr(exp), JMPFALSE(failLab) ];
