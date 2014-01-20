module experiments::Compiler::muRascal2RVM::StackSize

import Prelude;
import util::Math;
import experiments::Compiler::muRascal::AST;


public int estimate_stack_size(MuExp exp) = estimate(exp) + 1;
public int estimate_stack_size(list[MuExp] exps) = estimate_list(exps) + 1;

private int estimate(muLab(bool b)) = 1;

private int estimate(muBlock(list[MuExp] exps)) = ( 1 | max(it, estimate(exp)) | exp <- exps );

private int estimate_list(list[MuExp] exps) = ( 1 | max(it, estimate(exp))  | exp <- exps );

private int estimate_arg_list(list[MuExp] args) = ( 1 | max(it, i + estimate(args[i])) | i <- index(args) );

// Translate a single muRascal expression

private int estimate(muBool(bool b)) = 1;
private int estimate(muInt(int n)) = 1;
private int estimate(muCon("true")) = 1;
private int estimate(muCon("false")) = 1;
private default int estimate(muCon(value v)) = 1;

private int estimate(muTypeCon(Symbol sym)) = 1;

private int estimate(muFun(str fuid)) = 1;
private int estimate(muFun(str fuid, str scopeIn)) = 1;

private int estimate(muOFun(str fuid)) = 1;

private int estimate(muConstr(str fuid)) = 1;

private int estimate(muVar(str id, str fuid, int pos)) = 1;
private int estimate(muLoc(str id, int pos)) = 1;
private int estimate(muTmp(str id, str fuid)) = 1;

private int estimate(muLocKwp(str name)) = 1;
private int estimate(muVarKwp(str fuid, str name)) = 1;

private int estimate(muCallConstr(str fuid, list[MuExp] args)) = estimate_arg_list(args);

private int estimate(muCall(muFun(str fuid), list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCall(muConstr(str fuid), list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCall(MuExp fun, list[MuExp] args)) = max(estimate(fun), 1 + estimate_arg_list(args));

private int estimate(muOCall(muOFun(str fuid), list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muOCall(MuExp fun, Symbol types, list[MuExp] args)) = max(estimate(fun), 1 + estimate_arg_list(args));

private int estimate(muCallPrim(str name, list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCallMuPrim(str name, list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCallJava(str name, str class, Symbol argTypes, int reflect, list[MuExp] args)) = estimate_arg_list(args);

private int estimate(muAssign(str id, str fuid, int pos, MuExp exp)) = estimate(exp);
private int estimate(muAssignLoc(str id, int pos, MuExp exp)) = estimate(exp);
private int estimate(muAssignTmp(str id, str fuid, MuExp exp)) = estimate(exp);

private int estimate(muAssignLocKwp(str name, MuExp exp)) = estimate(exp);
private int estimate(muAssignKwp(str fuid, str name, MuExp exp)) = estimate(exp);

private int estimate(muIfelse(str label, MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)) =
    max(max(estimate(cond), estimate_list(thenPart)), estimate_list(elsePart));

private int estimate(muWhile(str label, MuExp cond, list[MuExp] body)) = 
    max(estimate(cond), estimate_list(body));
 
private int estimate(muDo(str label, list[MuExp] body, MuExp cond)) = 
    max(estimate_list(body), estimate(cond));

private int estimate(muBreak(str label)) = 0;
private int estimate(muContinue(str label)) = 0;
private int estimate(muFail(str label)) = 0;

private int estimate(muTypeSwitch(MuExp exp, list[MuTypeCase] cases, MuExp \default)) = 
(1 | max(it, estimate(cs.exp)) | cs <- cases);
       
private int estimate(muFailReturn()) = 0;
private int estimate(muFilterReturn()) = 0;

private int estimate(muCreate(muFun(str fuid))) = 1;
private int estimate(muCreate(MuExp fun)) = estimate(fun);
private int estimate(muCreate(muFun(str fuid), list[MuExp] args)) = estimate_arg_list(args);

private int estimate(muCreate(MuExp fun, list[MuExp] args)) = max(estimate(fun), 1 + estimate_arg_list(args));

private int estimate(muInit(MuExp exp)) = estimate(exp);
private int estimate(muInit(MuExp coro, list[MuExp] args)) = max(estimate(coro), 1 + estimate_arg_list(args));

private int estimate(muNext(MuExp coro)) = estimate(coro);
private int estimate(muNext(MuExp coro, list[MuExp] args)) = max(estimate(coro), 1 + estimate_arg_list(args));

private int estimate(muYield()) = 1;
private int estimate(muYield(MuExp exp)) = estimate(exp);
private int estimate(muYield(MuExp exp, list[MuExp] exps)) = estimate_arg_list([ exp, *exps ]);

private int estimate(muExhaust()) = 1;

private int estimate(muGuard(MuExp exp)) = estimate(exp);

private int estimate(muReturn()) = 0;
private int estimate(muReturn(MuExp exp)) = estimate(exp);
private int estimate(muReturn(MuExp exp, list[MuExp] exps)) = estimate_arg_list([ exp, *exps ]);

private int estimate(muHasNext(MuExp coro)) = estimate(coro);

private int estimate(muMulti(MuExp exp)) = estimate(exp);
    
private int estimate(e:muOne(list[MuExp] exps)) = estimate_arg_list(exps) + 1;
private int estimate(e:muOne(MuExp exp)) = estimate(exp) + 1;

private int estimate(e:muAll(list[MuExp] exps)) = estimate_arg_list(exps) + 2;
private int estimate(e:muOr(list[MuExp] exps)) = estimate_arg_list(exps) + 2;
    
private int estimate(muLocDeref(str name, int pos)) = 1;
private int estimate(muVarDeref(str name, str fuid, int pos)) = 1;

private int estimate(muLocRef(str name, int pos)) = 1;
private int estimate(muVarRef(str name, str fuid, int pos)) = 1;
private int estimate(muTmpRef(str name, str fuid)) = 1;

private int estimate(muAssignLocDeref(str id, int pos, MuExp exp)) = estimate(exp);
private int estimate(muAssignVarDeref(str id, str fuid, int pos, MuExp exp)) = estimate(exp);

private int estimate(muThrow(MuExp exp)) = estimate(exp);
private int estimate(muTry(MuExp tryBody, muCatch(str varname, str fuid, Symbol \type, MuExp catchBody), MuExp \finally)) = max(max(estimate(tryBody),1 + 1 + estimate(catchBody)),estimate(\finally));

private default int estimate(e) { throw "Unknown node in the muRascal AST: <e>"; }
