module experiments::Compiler::muRascal2RVM::StackSize

import util::Math;
import List;
import Type;
import experiments::Compiler::muRascal::AST;



public int estimate_stack_size(MuExp exp) = addSlack(estimate(exp));
	
public int estimate_stack_size(list[MuExp] exps) = addSlack(estimate_list(exps));

int addSlack(int e) = 10 + e + (e+40)/10;

private int estimate(muLab(str name)) = 1;

private int estimate(muBlock(list[MuExp] exps)) = ( 1 | max(it, estimate(e)) | e <- exps );

private int estimate_list(list[MuExp] exps) = ( 1 | max(it, estimate(e))  | e <- exps );

private int estimate_arg_list(list[MuExp] args) = ( 1 | max(it, i + estimate(args[i])) | i <- index(args) );

// Translate a single muRascal expression

private int estimate(muBool(bool b)) = 1;
private int estimate(muInt(int n)) = 1;
private default int estimate(muCon(value v)) = 1;

private int estimate(muTypeCon(Symbol sym)) = 1;

private int estimate(muFun1(str fuid)) = 1;
private int estimate(muFun2(str fuid, str scopeIn)) = 1;

private int estimate(muOFun(str fuid)) = 1;

private int estimate(muConstr(str fuid)) = 1;

private int estimate(muVar(str id, str fuid, int pos)) = 1;
private int estimate(muLoc(str id, int pos)) = 1;
private int estimate(muTmp(str id, str fuid)) = 1;
private int estimate(muResetLocs(list[int] positions)) = 1;

private int estimate(muLocKwp(str name)) = 1;
private int estimate(muVarKwp(str fuid, str name)) = 1;

private int estimate(muCallConstr(str fuid, list[MuExp] args)) = estimate_arg_list(args);

private int estimate(muCall(muFun1(str fuid), list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCall(muConstr(str fuid), list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCall(MuExp fun, list[MuExp] args)) = max(estimate(fun), 1 + estimate_arg_list(args));

private int estimate(muApply(muFun1(str fuid), list[MuExp] args)) = 1 + estimate_arg_list(args);
private int estimate(muApply(muConstr(str fuid), list[MuExp] args)) { throw "Partial application is not supported for constructor calls!"; }
private int estimate(muApply(MuExp fun, list[MuExp] args)) = estimate(fun) + estimate_arg_list(args);

private int estimate(muOCall3(muOFun(str fuid), list[MuExp] args, loc src)) = estimate_arg_list(args);
private int estimate(muOCall4(MuExp fun, Symbol types, list[MuExp] args, loc src)) = estimate(fun) + estimate_arg_list(args);

private int estimate(muCallPrim3(str name, list[MuExp] args, loc src)) = estimate_arg_list(args);
private int estimate(muCallMuPrim(str name, list[MuExp] args)) = estimate_arg_list(args);
private int estimate(muCallJava(str name, str class, Symbol parameterTypes,  Symbol keywordTypes, int reflect, list[MuExp] args)) = estimate_arg_list(args);

private int estimate(muAssign(str id, str fuid, int pos, MuExp exp)) = estimate(exp);
private int estimate(muAssignLoc(str id, int pos, MuExp exp)) = estimate(exp);
private int estimate(muAssignTmp(str id, str fuid, MuExp exp)) = estimate(exp);

private int estimate(muAssignLocKwp(str name, MuExp exp)) = estimate(exp);
private int estimate(muAssignKwp(str fuid, str name, MuExp exp)) = estimate(exp);

private int estimate(muIfelse(str label, MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)) =
    max(max(estimate(cond), estimate_list(thenPart)), estimate_list(elsePart));

private int estimate(muWhile(str label, MuExp cond, list[MuExp] body)) = 
    1 + max(estimate(cond), estimate_list(body));
 
private int estimate(muBreak(str label)) = 0;
private int estimate(muContinue(str label)) = 0;
private int estimate(muFail(str label)) = 0;

private int estimate(muTypeSwitch(MuExp exp, list[MuTypeCase] cases, MuExp defaultExp)) = 
	max((1 | max(it, estimate(cs.exp)) | cs <- cases), estimate(defaultExp));

private int estimate(muSwitch(MuExp exp, bool concretePatterns, list[MuCase] cases, MuExp defaultExp, MuExp result)) = 
	1 + max(max(estimate(exp), max((1 | max(it, estimate(cs.exp)) | cs <- cases), estimate(defaultExp))), estimate(result));
       
private int estimate(muFailReturn()) = 0;
private int estimate(muFilterReturn()) = 0;

private int estimate(muCreate1(MuExp exp)) = estimate(exp);
private int estimate(muCreate2(MuExp coro, list[MuExp] args)) = max(estimate(coro), 1 + estimate_arg_list(args));

private int estimate(muNext1(MuExp coro)) = estimate(coro);
private int estimate(muNext2(MuExp coro, list[MuExp] args)) = max(estimate(coro), 1 + estimate_arg_list(args));

private int estimate(muYield0()) = 1;
private int estimate(muYield1(MuExp exp)) = estimate(exp);
private int estimate(muYield2(MuExp exp, list[MuExp] exps)) = estimate_arg_list([ exp, *exps ]);

private int estimate(muExhaust()) = 1;

private int estimate(muGuard(MuExp exp)) = estimate(exp);

private int estimate(muReturn0()) = 0;
private int estimate(muReturn1(MuExp exp)) = estimate(exp);
private int estimate(muReturn2(MuExp exp, list[MuExp] exps)) = estimate_arg_list([ exp, *exps ]);

private int estimate(muMulti(MuExp exp)) = estimate(exp);

private int estimate(e:muOne1(MuExp exp)) = estimate(exp) + 1; 
    
private int estimate(muLocDeref(str name, int pos)) = 1;
private int estimate(muVarDeref(str name, str fuid, int pos)) = 1;

private int estimate(muLocRef(str name, int pos)) = 1;
private int estimate(muVarRef(str name, str fuid, int pos)) = 1;
private int estimate(muTmpRef(str name, str fuid)) = 1;

private int estimate(muAssignLocDeref(str id, int pos, MuExp exp)) = estimate(exp);
private int estimate(muAssignVarDeref(str id, str fuid, int pos, MuExp exp)) = estimate(exp);

private int estimate(muThrow(MuExp exp, loc src)) = estimate(exp);
private int estimate(muTry(MuExp tryBody, muCatch(str varname, str fuid, Symbol \type, MuExp catchBody), MuExp \finally)) = 
	max(max(estimate(tryBody),1 + 1 + estimate(catchBody)),estimate(\finally));

private int estimate(muContVar(str fuid)) = 1;
private int estimate(muReset(MuExp fun)) = estimate(fun);
private int estimate(muShift(MuExp body)) = estimate(body);

private default int estimate(MuExp e) { throw "estimate: Unknown node in the muRascal AST: <e>"; } 