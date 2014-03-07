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
        return <muMulti(muApply("Library/<muAllOrMuOr>(1)",
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
    if(muAllOrMuOr == "IMPLICATION") {
        return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCon(true) ], [ exps[i] is muOne ? muNext(muInit(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;
    }
    if(muAllOrMuOr == "EQUIVALENCE") {
        return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCallMuPrim("not_mbool", [ exp is muOne ? muNext(muInit(exp)) : exp ]) ], [ exp is muOne ? muNext(muInit(exp_copy)) : exp_copy ]) | int i <- [ 1..size(exps) ], MuExp exp := exps[i], MuExp exp_copy := newLabels(exp) ),[]>;
    }
}

tuple[MuExp,list[MuFunction]] makeMuMulti(e:muMulti(_), str fuid) = <e,[]>;
// TODO: should ONE be also passed a closure? I would say yes
tuple[MuExp,list[MuFunction]] makeMuMulti(e:muOne(MuExp exp), str fuid) = <muMulti(muApply("Library/ONE(1)"),[ exp ]),[]>; // ***Note: multi expression that produces at most one solution
default tuple[MuExp,list[MuFunction]] makeMuMulti(MuExp exp, str fuid) {
    // Works because mkVar and mkAssign produce muVar and muAssign, i.e., specify explicitly function scopes computed by the type checker
    list[MuFunction] functions = [];
    str gen_uid = "<fuid>/GEN_<nextLabel()>(0)";
    functions += muCoroutine(gen_uid, fuid, 0, 0, [], muBlock([ muGuard(muCon(true)), muIfelse(nextLabel(), exp, [ muReturn() ], [ muExhaust() ]) ]));
    return <muMulti(muApply(muFun(gen_uid, fuid),[])),functions>;
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
    return <muMulti(muApply(muFun(all_uid, fuid),[])),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMu("OR", str fuid, list[MuExp] exps, list[bool] backtrackfree) {
    list[MuFunction] functions = [];
    str or_uid = "Library/<fuid>/OR_<getNextOr()>(0)";
    list[MuExp] body = [];
    for(int i <- index(exps)) {
        if(backtrackfree[i]) {
            body += muIfelse(nextLabel(), exps[i], [ muYield() ], [ muCon(222) ]);
        } else {
            body = body + [ muCall(exps[i],[]) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(or_uid, fuid, 0, 0, [], muBlock(body));
    return <muMulti(muApply(muFun(or_uid, fuid),[])),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMu("IMPLICATION", str fuid, list[MuExp] exps, list[bool] backtrackfree) {
    list[MuFunction] functions = [];
    str impl_uid = "Library/<fuid>/IMPLICATION_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", impl_uid, i) | int i <- index(exps) ];
    list[MuExp] body = [ muYield() ];
    bool first = true;
    int k = size(exps);
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muAssign("hasNext", impl_uid, k, muCon(false)),
                     muIfelse(nextLabel(), exps[j], [ muAssign("hasNext", impl_uid, k, muCon(true)) ] + body, [ muCon(222) ]), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", impl_uid, k) ]),[ muYield() ],[ muCon(222) ]) 
                   ];
        } else {
            body = [ muAssign("hasNext", impl_uid, k, muCon(false)),
                     muAssign("c_<j>", impl_uid, j, muInit(exps[j])), muWhile(nextLabel(), muNext(localvars[j]), [ muAssign("hasNext", impl_uid, k, muCon(true)) ] + body), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", impl_uid, k) ]),[ muYield() ],[ muCon(222) ])
                   ];
        }
        first = false;
        k = k + 1;
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(impl_uid, fuid, 0, k, [], muBlock(body));
    return <muMulti(muApply(muFun(impl_uid, fuid),[])),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMu("EQUIVALENCE", str fuid, list[MuExp] exps, list[bool] backtrackfree) {
    list[MuFunction] functions = [];
    str equiv_uid = "Library/<fuid>/EQUIVALENCE_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", equiv_uid, i) | int i <- index(exps) ];
    list[MuExp] body = [ muYield() ];
    bool first = true;
    int k = size(exps);
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muAssign("hasNext", equiv_uid, k, muCon(false)),
                     muIfelse(nextLabel(), exps[j], [ muAssign("hasNext", equiv_uid, k, muCon(true)) ] + body, [ muCon(222) ]), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", equiv_uid, k) ]),[ muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ backtrackfree[j + 1] ? newLabels(exps[j + 1]) : muNext(muInit(newLabels(exps[j + 1]))) ]), [ muYield() ], [ muCon(222) ]) ],[ muCon(222) ]) 
                   ];
        } else {
            body = [ muAssign("hasNext", equiv_uid, k, muCon(false)),
                     muAssign("c_<j>", equiv_uid, j, muInit(exps[j])), muWhile(nextLabel(), muNext(localvars[j]), [ muAssign("hasNext", equiv_uid, k, muCon(true)) ] + body), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", equiv_uid, k) ]),[ muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ backtrackfree[j + 1] ? newLabels(exps[j + 1]) : muNext(muInit(newLabels(exps[j + 1]))) ]), [ muYield() ], [ muCon(222) ]) ],[ muCon(222) ]) 
                   ];
        }
        first = false;
        k = k + 1;
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(equiv_uid, fuid, 0, k, [], muBlock(body));
    return <muMulti(muApply(muFun(equiv_uid, fuid), [])),functions>;
}

private MuExp newLabels(MuExp exp) {
    map[str,str] labels = ();
    return top-down visit(exp) {
                        case muIfelse(str label, cond, thenPart, elsePart): { labels[label] = nextLabel(); insert muIfelse(labels[label],cond,thenPart,elsePart); }
                        case muWhile(str label, cond, whileBody): { labels[label] = nextLabel(); insert muWhile(labels[label],cond,whileBody); }
                        case muBreak(str label): insert muBreak(labels[label]);
                        case muContinue(str label): insert muContinue(labels[label]);
                        case muFail(str label): insert muFail(labels[label]); 
                    };
}
