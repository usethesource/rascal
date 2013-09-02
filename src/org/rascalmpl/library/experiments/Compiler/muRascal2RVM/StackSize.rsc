module experiments::Compiler::muRascal2RVM::StackSize

import Prelude;
import util::Math;
import experiments::Compiler::muRascal::AST;


int estimate(muLab(bool b)) = 1;

int estimate(list[MuExp] exps) = ( 0 | it + estimate(exp) | exp <- exps );

int estimate_args(list[MuExp] args) = 
  size(args) + (0 | max(it, estimate(arg)) | arg <- args);

// Translate a single muRascal expression

int estimate(muBool(bool b)) = 1;
int estimate(muInt(int n)) = 1;
int estimate(muCon("true")) = 1;
int estimate(muCon("false")) = 1;
default int estimate(muCon(value v)) = 1;

int estimate(muTypeCon(Symbol sym)) = 1;

int estimate(muFun(str fuid)) = 1;
int estimate(muFun(str fuid, str scopeIn)) = 1;

int estimate(muConstr(str fuid)) = 1;

int estimate(muVar(str id, str fuid, int pos)) = 1;
int estimate(muLoc(str id, int pos)) = 1;
int estimate(muTmp(str id)) = 1;

int estimate(muCallConstr(str fuid, list[MuExp] args)) = estimate_args(args);

int estimate(muCall(muFun(str fuid), list[MuExp] args)) = estimate_args(args);
int estimate(muCall(muConstr(str fuid), list[MuExp] args)) = estimate_args(args);
int estimate(muCall(MuExp fun, list[MuExp] args)) = max(estimate(fun), 1 + estimate_args(args));

int estimate(muCallPrim(str name, list[MuExp] args)) = estimate_args(args);
int estimate(muCallMuPrim(str name, list[MuExp] args)) =  estimate_args(args);

int estimate(muAssign(str id, str fuid, int pos, MuExp exp)) = estimate(exp);
int estimate(muAssignLoc(str id, int pos, MuExp exp)) = estimate(exp);
int estimate(muAssignTmp(str id, MuExp exp)) = estimate(exp);

int estimate(muIfelse(MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)) =
    max(max(estimate(cond), estimate(thenPart)), estimate(elsePart));

int estimate(muWhile(str label, MuExp cond, list[MuExp] body)) = 
    max(estimate(cond), estimate(body));
 
int estimate(muDo(str label, list[MuExp] body, MuExp cond)) = 
    max(estimate(body), estimate(cond));

int estimate(muBreak(str label)) = 0;
int estimate(muContinue(str label)) = 0;
int estimate(muFail(str label)) = 0;

int estimate(muFailReturn()) = 0;

int estimate(muCreate(muFun(str fuid))) = 1;
int estimate(muCreate(MuExp fun)) = estimate(fun);
int estimate(muCreate(muFun(str fuid), list[MuExp] args)) = estimate_args(args);

int estimate(muCreate(MuExp fun, list[MuExp] args)) = max(estimate(fun), 1 + estimate_args(args));

int estimate(muInit(MuExp exp)) = estimate(exp);
int estimate(muInit(MuExp coro, list[MuExp] args)) = max(estimate(coro), 1 + estimate_args(args));

int estimate(muNext(MuExp coro)) = estimate(coro);
int estimate(muNext(MuExp coro, list[MuExp] args)) = max(estimate(coro), 1 + estimate_args(args));

int estimate(muYield()) = 1;
int estimate(muYield(MuExp exp)) = estimate(exp);

int estimate(muReturn()) = 0;
int estimate(muReturn(MuExp exp)) = estimate(exp);

int estimate(muHasNext(MuExp coro)) = estimate(coro);

int estimate(muMulti(MuExp exp)) = estimate(exp);
    
int estimate(e:muOne(list[MuExp] exps)) = estimate(exps) + 1;

int estimate(e:muAll(list[MuExp] exps)) = estimate_args(exps) + 2;
    
int estimate(muLocDeref(str name, int pos)) = 1;
int estimate(muVarDeref(str name, str fuid, int pos)) = 1;

int estimate(muLocRef(str name, int pos)) = 1;
int estimate(muVarRef(str name, str fuid, int pos)) = 1;

int estimate(muAssignLocDeref(str id, int pos, MuExp exp)) = estimate(exp);
int estimate(muAssignVarDeref(str id, str fuid, int pos, MuExp exp)) = estimate(exp);

default int estimate(e) { throw "Unknown node in the muRascal AST: <e>"; }