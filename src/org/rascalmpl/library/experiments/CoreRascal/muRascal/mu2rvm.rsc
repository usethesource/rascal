module experiments::CoreRascal::muRascal::mu2rvm

import experiments::CoreRascal::muRascalVM::AST;

import experiments::CoreRascal::muRascal::AST;

alias INS = list[Instruction];


// Unique label generator

int nLabel = -1;
str nextLabel() { nlabel += 1; return "L<nlabel>"; }


// Translation functions

INS trargs(list[MuExp] args) = [*tr(arg) | arg <- args];

INS tr(constant(value c)) = [loadCon(c)];

INS tr(var(str id, int scope, int pos)) = [loadvar(scope, pos)];

INS tr(call(Exp fun, list[MuExp] args)) =  [*trargs(args), *tr(fun), calldyn()];

INS tr(call(str name, list[MuExp] args)) = [*trargs(args), call(name)];

INS tr(callprim(str name, list[MuExp] args)) = [*trargs(args), callprim(name)];

INS tr(assign(str id, int scope, int pos, MuExp exp)) = [*tr(exp), storevar(scope, pos)];

INS tr(ifelse(MuExp exp1, MuExp exp2, MuExp exp3)) {
    lab_else = nextLabel();
    lab_after = nextLabel();
    return [*tr(exp1), jmpfalse(lab_else), *tr(exp2), jmp(lab_after), label(lab_else), *tr(exp3), label(lab_after)];
}

INS tr(\while(MuExp exp1, MuExp exp2)) {
    lab_while = nextLabel();
    lab_after = nextLabel();
    return [label(lab_while), *tr(exp1), jmpfalse(lab_after), *tr(exp2), jmp(lab_while), label(lab_after)];
}

INS tr(create(str name)) = [create(name)];
INS tr(create(MuExp exp)) = [*tr(exp1),createdyn()];

INS tr(next(MuExp exp)) = [*tr(exp1),next0()];
INS tr(next(MuExp exp1, MuExp exp2)) = [*tr(exp1), * tr(exp2), next1()];

INS tr(yield()) = [yield0()];
INS tr(next(MuExp exp)) = [*tr(exp), yield1()];

INS tr(ret()) = [return0()];
INS tr(ret(MuExp exp)) = [*tr(exp), return1()];

INS tr(hasNext(MuExp exp)) = [*tr(exp), hasNext()];

INS tr(block(list[MuExp] exps)) {
  ins = [*tr(exp), pop() | exp <- exps];
  if(size(ins > 0)){
     ins = ins[0 .. -1];
  }
  return ins;
}

// Typed interface with muRascalVM

Instruction loadcon(value val) = instruction("loadcon", [val]);
Instruction loadloc(int pos) = instruction("loadloc", [pos]);
Instruction loadvar(int scope, int pos) = instruction("loadvar", [scope, pos]);
Instruction storevar(int scope, int pos) = instruction("storevar", [scope, pos]);
Instruction calldyn() = instruction("calldyn", []);
Instruction call(str name) = instruction("call", [name]);
Instruction callprim(str name) = instruction("callprim", [name]);

Instruction jmp(str label) =  instruction("jmp", [label]);
Instruction jmptrue(str label) =  instruction("jmptrue", [label]);
Instruction jmpfalse(str label) =  instruction("jmpfalse", [label]);

Instruction create(str fun) =  instruction("create", [fun]);
Instruction createdyn() =  instruction("createdyn", []);

Instruction next0() =  instruction("next0", []);
Instruction next1() =  instruction("next1", []);

Instruction yield0() =  instruction("yield0", []);
Instruction yield1() =  instruction("yield1", []);

Instruction return0() =  instruction("ret0", []);
Instruction return1() =  instruction("ret1", []);






