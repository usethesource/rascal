module experiments::Compiler::muRascal::MuAllMuOr

import experiments::Compiler::muRascal::AST;
import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import Prelude;


/*
 * Interface: 
 *     - tuple[MuExp,list[MuFunction]] makeMu(str,str,list[MuExp]);
 *     - tuple[MuExp,list[MuFunction]] makeMulti(MuExp,str);
 *     - tuple[MuExp,list[MuFunction]] makeMuOne(str,str,list[MuExp]);
 */

// Produces multi- or backtrack-free expressions
/*
 * Reference implementation that uses the generic definitions of ALL and OR  
 */
/*
tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, [ e:muMulti(_) ]) = <e,[]>;
tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, [ e:muOne(MuExp exp) ]) = <makeMuMulti(e),[]>;
tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, [ MuExp e ]) = <e,[]> when !(muMulti(_) := e || muOne(_) := e);
default tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, list[MuExp] exps) {
    list[MuFunction] functions = [];
    assert(size(exps) >= 1);
    if(MuExp exp <- exps, muMulti(_) := exp) { // Multi expression
        return <muMulti(muCreate("Library/<muAllOrMuOr>(1)",
                                [ muCallMuPrim("make_array",[ { str gen_uid = "<fuid>/LAZY_EVAL_GEN_<nextLabel()>(0)";
                                                                tuple[MuExp e,list[MuFunction] functions] res = makeMuMulti(exp,fuid);
                                                                functions = functions + res.functions;
                                                                functions += muFunction(gen_uid, Symbol::\func(Symbol::\value(),[]), fuid, 0, 0, false, |rascal:///|, [], (), muReturn(res.e.exp));
                                                                muFun(gen_uid,fuid);
                                                              } | MuExp exp <- exps ]) ])),
                functions>;
    }
    if(muAllOrMuOr == "ALL") {
        return <( exps[0] | muIfelse(nextLabel(), it, [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] ),functions>;
    } 
    if(muAllOrMuOr == "OR"){
        return <( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),functions>;
    }
}
*/
/*
 * Alternative, fast implementation that generates a specialized definitions of ALL and OR
 */
tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, [ e:muMulti(_) ]) = <e,[]>;
tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, [ e:muOne(MuExp exp) ]) = <makeMuMulti(e),[]>;
tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, [ MuExp e ]) = <e,[]> when !(muMulti(_) := e || muOne(_) := e);
default tuple[MuExp,list[MuFunction]] makeMu(str muAllOrMuOr, str fuid, list[MuExp] exps) {
    assert(size(exps) >= 1);
    if(MuExp exp <- exps, muMulti(_) := exp) { // Multi expression
        list[MuExp] expressions = [];
        list[bool] backtrackfree = [];
        for(MuExp e <- exps) {
            if(muMulti(_) := e) {
                expressions += e.exp;
                backtrackfree += false;
            } else if(muOne(_) := e) {
                expressions += muNext(muInit(e.exp));
                backtrackfree += true;
            } else {
                expressions += e;
                backtrackfree += true;
            }
        }
        return generateMu(muAllOrMuOr, fuid, expressions, backtrackfree);
    }
    if(muAllOrMuOr == "ALL") {
        return <( exps[0] | muIfelse(nextLabel(), it, [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] ),[]>;
    } 
    if(muAllOrMuOr == "OR"){
        return <( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;
    }
}

tuple[MuExp,list[MuFunction]] makeMuMulti(e:muMulti(_), str fuid) = <e,[]>;
// TODO: should ONE be also passed a closure? I would say yes
tuple[MuExp,list[MuFunction]] makeMuMulti(e:muOne(MuExp exp), str fuid) = <muMulti(muCreate("Library/ONE(1)"),[ exp ]),[]>; // ***Note: multi expression that produces at most one solution
default tuple[MuExp,list[MuFunction]] makeMuMulti(MuExp exp, str fuid) {
    // Works because mkVar and mkAssign produce muVar and muAssign, i.e., specify explicitly function scopes computed by the type checker
    list[MuFunction] functions = [];
    str gen_uid = "<fuid>/GEN_<nextLabel()>(0)";
    functions += muCoroutine(gen_uid, fuid, 0, 0, [], muBlock([ muGuard(muCon(true)), muIfelse(nextLabel(), exp, [ muReturn() ], [ muExhaust() ]) ]));
    return <muMulti(muCreate(muFun(gen_uid))),functions>;
}

tuple[MuExp,list[MuFunction]] makeMuOne(str muAllOrMuOr, str fuid, [ e:muMulti(MuExp exp) ]) = <muOne(exp),[]>;
tuple[MuExp,list[MuFunction]] makeMuOne(str muAllOrMuOr, str fuid, [ e:muOne(MuExp exp) ]) = <e,[]>;
tuple[MuExp,list[MuFunction]] makeMuOne(str muAllOrMuOr, str fuid, [ MuExp e ]) = <e,[]> when !(muMulti(_) := e || muOne(_) := e);
default tuple[MuExp,list[MuFunction]] makeMuOne(str muAllOrMuOr, str fuid, list[MuExp] exps) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMu(muAllOrMuOr,fuid,exps);
    if(muMulti(exp) := res.e) {
        return <muOne(exp),res.functions>;
    }
    return res;
}

private tuple[MuExp,list[MuFunction]] generateMu("ALL", str fuid, list[MuExp] exps, list[bool] backtrackfree) {
    list[MuFunction] functions = [];
    str all_uid = "Library/<fuid>/ALL_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", all_uid, i) | int i <- index(exps) ];
    list[MuExp] body = [ muYield() ];
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muIfelse(nextLabel(), exps[j], body, [ muCon(222) ]) ];
        } else {
            body = [ muAssign("c_<j>", all_uid, j, muInit(exps[j])), muWhile(nextLabel(), muNext(localvars[j]), body), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(all_uid, fuid, 0, size(localvars), [], muBlock(body));
    return <muMulti(muCreate(muFun(all_uid))),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMu("OR", str fuid, list[MuExp] exps, list[bool] backtrackfree) {
    list[MuFunction] functions = [];
    str or_uid = "Library/<fuid>/OR_<getNextOr()>(0)";
    localvars = [ muVar("c_<i>", or_uid, i) | int i <- index(exps) ];
    list[MuExp] body = [];
    for(int i <- index(exps)) {
        if(backtrackfree[i]) {
            body += muIfelse(nextLabel(), exps[i], [ muYield() ], [ muCon(222) ]);
        } else {
            body = body + [ muAssign("c_<i>", or_uid, i, muInit(exps[j])), muWhile(nextLabel(), muNext(localvars[i]), [ muYield() ]), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(or_uid, fuid, 0, size(localvars), [], muBlock(body));
    return <muMulti(muCreate(muFun(or_uid))),functions>;
}
