module experiments::CoreRascal::muRascal::mu2rvm

import Prelude;

import experiments::CoreRascal::muRascalVM::AST;

import experiments::CoreRascal::muRascal::AST;

alias INS = list[Instruction];


// Unique label generator

int nlabel = -1;
str nextLabel() { nlabel += 1; return "L<nlabel>"; }


// Translate a muRascal module

RVMProgram mu2rvm(muModule(str name, list[MuDefinition] definitions, MuExp initialization)){
   return rvm((def.name : FUNCTION(def.name, def.scope, def.nformal, def.nlocal, 10, tr(def.body)) | def <- definitions), []);
}


// Translate a muRascal function

// Translate muRascal expressions

INS tr(list[MuExp] args) { println(args); return [*tr(arg) | arg <- args];}

INS tr(muConstant(value c)) = [LOADCON(c)];

INS tr(muVar(str id, int scope, int pos)) = [LOADVAR(scope, pos)];

INS tr(muCall(MuExp fun, list[MuExp] args)) =  [*tr(args), *tr(fun), CALLDYN()];

INS tr(muCall(str name, list[MuExp] args)) = [*tr(args), CALL(name)];

INS tr(muCallPrim(str name, MuExp arg)) = [*tr(arg), CALLPRIM(name)];

INS tr(muCallPrim(str name, MuExp arg1, MuExp arg2)) = [*tr(arg1), *tr(arg2), CALLPRIM(name)];

INS tr(muAssign(str id, int scope, int pos, MuExp exp)) = [*tr(exp), STOREVAR(scope, pos)];

INS tr(muIfelse(MuExp exp1, MuExp exp2, MuExp exp3)) {
    lab_else = nextLabel();
    lab_after = nextLabel();
    res = [*tr(exp1), JMPFALSE(lab_else), *tr(exp2), JMP(lab_after), LABEL(lab_else), *tr(exp3), LABEL(lab_after)];
    return res;
}

INS tr(muWhile(MuExp exp1, MuExp exp2)) {
    lab_while = nextLabel();
    lab_after = nextLabel();
    return [LABEL(lab_while), *tr(exp1), JMPFALSE(lab_after), *tr(exp2), JMP(lab_while), LABEL(lab_after)];
}

INS tr(muCreate(str name)) = [CREATE(name)];
INS tr(muCreate(MuExp exp)) = [*tr(exp1),CREATEDYN()];

INS tr(muNext(MuExp exp)) = [*tr(exp1),NEXT0()];
INS tr(muNext(MuExp exp1, MuExp exp2)) = [*tr(exp1), * tr(exp2), NEXT1()];

INS tr(muYield()) = [YIELD0()];
INS tr(muNext(MuExp exp)) = [*tr(exp), YIELD1()];

INS tr(muReturn()) = [RETURN0()];
INS tr(muReturn(MuExp exp)) = [*tr(exp), RETURN1()];

INS tr(muHasNext(MuExp exp)) = [*tr(exp), HASNEXT()];

INS tr(muBlock(list[MuExp] exps)) {
  if(size(exps) == 1)
     return tr(exps[0]);
  ins = [*tr(exp), POP() | exp <- exps];
  if(size(ins) > 0){
     ins = ins[0 .. -1];
  }
  return ins;
}





