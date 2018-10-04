module lang::rascalcore::compile::muRascal::MuBoolExp

import List;
import IO;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import Type;

bool debug = false;

/*
 * makeBoolExp: given Boolean operator and a list of expressions, return an expression that applies the operator to all arguments.
 * 		When possible, a backtrack free solution is provided that gives a single answer.
 *      Otherwise, a multi-expression is returned.
 *		Possible operators are:
 *			- "ALL": yields true for all true combinations of arguments
 *		    - "RASCAL_ALL": returns true if all combinations of arguments yield true.
 *			- OR, "EQUIVALENCE", "IMPLICATION": usual Boolean operator (with backtracking)
 * makeSingleValuedBoolExp: 
 *		as makeBoolExp, but always forces a single result (when available).
 * makeMultiValuedBoolExp:
 *		turns any Boolean expression into a multi-valued Boolean expression
 *
 * Interface: 
 *     - tuple[MuExp,list[MuFunction]] makeBoolExp(str,str,list[MuExp],loc);
 *     - tuple[MuExp,list[MuFunction]] makeSingleValuedBoolExp(str,str,list[MuExp],loc);
 *	   - tuple[MuExp,list[MuFunction]] makeMultiValuedBoolExp(MuExp exp, str fuid, loc src) 
 */

// Produces multi- or backtrack-free expressions
/*
 * Reference implementation that uses the generic definitions of ALL and OR  
 */
/*
tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, [ e:muMulti(_) ], loc src) = <e,[]>;
tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, [ e:muOne1(MuExp exp) ], loc src) = <makeMultiValuedBoolExp(e),src,[]>;
tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, [ MuExp e ], loc src) = <e,[]> when !(muMulti(_) := e || muOne1(_) := e);
default tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, list[MuExp] exps, loc src) {
    list[MuFunction] functions = [];
    assert(size(exps) >= 1) : "makeMu: number of expressions should be >= 1";
    if(MuExp exp <- exps, muMulti(_) := exp) {
        if(operator == "ALL" || operator == "OR") {
            // Uses the generic definitions of ALL and OR
        	return <muMulti(muApply(muFun1("Library/<operator>"),
            	                    [ muCallMuPrim("make_array",[ { str gen_uid = "<fuid>/LAZY_EVAL_GEN_<nextLabel()>(0)";
                	                                                tuple[MuExp e,list[MuFunction] functions] res = makeMultiValuedBoolExp(exp,fuid, loc src);
                    	                                            functions = functions + res.functions;
                        	                                        functions += muFunction(gen_uid, Symbol::\func(Symbol::\value(), [], []), fuid, 0, 0, false, false, false, src, [], (), false, 0, 0, muReturn(res.e.exp));
                            	                                    muFun2(gen_uid,fuid);
                                	                              } | MuExp exp <- exps ]) ])),
                	functions>;
        } else if(operator == "IMPLICATION" || operator == "EQUIVALENCE") {
            // Generates the specialized code; TODO: also define these two as library coroutines 
            list[MuExp] expressions = [];
        	list[bool] backtrackfree = [];
        	for(MuExp e <- exps) {
            	if(muMulti(_) := e) {
                	expressions += e.exp;
                	backtrackfree += false;
            	} else if(muOne1(_) := e) {
                	expressions += muNext1(muCreate1(e.exp));
                	backtrackfree += true;
            	} else {
                	expressions += e;
                	backtrackfree += true;
            	}
        	}
        	return generateMuCode(operator, fuid, expressions, backtrackfree,src);;
        }
    }
    if(operator == "ALL") {
        return <( exps[0] | muIfelse(nextLabel(), it, [ exps[i] is muOne ? muNext1(muCreate1(exps[i])) : exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] ),functions>;
    } 
    if(operator == "OR"){
        return <( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ exps[i] is muOne ? muNext1(muCreate1(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),functions>;
    }
    if(operator == "IMPLICATION") {
        return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCon(true) ], [ exps[i] is muOne ? muNext1(muCreate1(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;
    }
    if(operator == "EQUIVALENCE") {
        return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCallMuPrim("not_mbool", [ exp is muOne ? muNext1(muCreate1(exp)) : exp ]) ], [ exp is muOne ? muNext1(muCreate1(exp_copy)) : exp_copy ]) | int i <- [ 1..size(exps) ], MuExp exp := exps[i], MuExp exp_copy := newLabels(exp) ),[]>;
    }
    
}
*/

/*
 * Alternative, fast implementation that generates a specialized definitions of ALL and OR
 */
 
bool isMuOne(MuExp e) = e is muOne1 || e is muOne1;

tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, [ e:muMulti(_) ], loc src) = <e,[]> when operator != "RASCAL_ALL";

tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, [ e:muOne1(MuExp exp) ], loc src) = makeMultiValuedBoolExp(e, fuid, src);

tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, [ MuExp e ], loc src) = <e,[]> when !(muMulti(_) := e || muOne1(MuExp _) := e);

default tuple[MuExp,list[MuFunction]] makeBoolExp(str operator, str fuid, list[MuExp] exps, loc src) {
	if(debug)println("makeBoolExp, default: <operator>, <fuid>, <exps>, <src>");
    assert(size(exps) >= 1) : "makeBoolExp: number of expressions should be \>= 1";
    if(MuExp exp <- exps, muMulti(_) := exp) { // Multi expression
        list[MuExp] expressions = [];
        list[bool] backtrackfree = [];
        for(MuExp e <- exps) {
            if(muMulti(_) := e) {
                expressions += e.exp;
                backtrackfree += false;
            } else if(muOne1(MuExp e1) := e) {
                if(debug)println("muOne: e1 = <e1>");
                expressions += muNext1(muCreate1(e.exp));
                backtrackfree += true;
            } else {
                expressions += e;
                backtrackfree += true;
            }
        }
        return generateMuCode(operator, fuid, expressions, backtrackfree, src);
    }
   
    switch(operator){
    case "ALL":
        return <( exps[0] | muIfelse(nextLabel(), it, [ exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] ),[]>;
        //return <( exps[0] | muIfelse(nextLabel(), it, [ isMuOne(exps[i]) ? muNext1(muCreate1(exps[i])) : exps[i] ], [ muCon(false) ]) | int i <- [ 1..size(exps) ] ),[]>;
    case "OR":
        return <( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;
        //return <( exps[0] | muIfelse(nextLabel(), it, [ muCon(true) ], [ isMuOne(exps[i]) ? muNext1(muCreate1(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;

    case "IMPLICATION":
        return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCon(true) ], [ exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;
        //return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCon(true) ], [ isMuOne(exps[i]) ? muNext1(muCreate1(exps[i])) : exps[i] ]) | int i <- [ 1..size(exps) ] ),[]>;
    
    case "EQUIVALENCE":
        return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCallMuPrim("not_mbool", [ exp ]) ], [ exp_copy ]) | int i <- [ 1..size(exps) ], MuExp exp := exps[i], MuExp exp_copy := newLabels(exp) ),[]>;
        //return <( exps[0] | muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ it ]), [ muCallMuPrim("not_mbool", [ isMuOne(exp) ? muNext1(muCreate1(exp)) : exp ]) ], [ isMuOne(exp) ? muNext1(muCreate1(exp_copy)) : exp_copy ]) | int i <- [ 1..size(exps) ], MuExp exp := exps[i], MuExp exp_copy := newLabels(exp) ),[]>;
    }
}

tuple[MuExp,list[MuFunction]] makeMultiValuedBoolExp(str operator, str fuid, list[MuExp] exps, loc src) {
    <e1, functions1> = makeBoolExp(operator, fuid, exps, src);
	<e2, functions2> = makeMultiValuedBoolExp(e1, fuid, src);
	return <e2, functions1 + functions2>;

}
private tuple[MuExp,list[MuFunction]] makeMultiValuedBoolExp(e:muMulti(_), str fuid, loc src) = <e,[]>;

// TODO: should ONE be also passed a closure? I would say yes
private tuple[MuExp,list[MuFunction]] makeMultiValuedBoolExp(e:muOne1(MuExp exp), str fuid, loc src) = <muMulti(muApply(mkCallToLibFun("Library", "ONE"),[ exp ])),[]>; // ***Note: multi expression that produces at most one solution


private default tuple[MuExp,list[MuFunction]] makeMultiValuedBoolExp(MuExp exp, str fuid, loc src) {
    // Works because mkVar and mkAssign produce muVar and muAssign, i.e., specify explicitly function scopes computed by the type checker
    list[MuFunction] functions = [];
    str gen_uid = "<fuid>/GEN_<nextLabel()>(0)";
    functions += muCoroutine(gen_uid, "GEN", fuid, 0, 0, src, [], muBlock([ muGuard(muCon(true)), muIfelse(nextLabel(), exp, [ muReturn0() ], [ muExhaust() ]) ]));
    return <muMulti(muApply(muFun2(gen_uid, fuid),[])),functions>;
}

tuple[MuExp,list[MuFunction]] makeSingleValuedBoolExp(str operator, str fuid, [ e:muMulti(MuExp exp) ], loc src) = <muOne1(exp),[]>;

tuple[MuExp,list[MuFunction]] makeSingleValuedBoolExp(str operator, str fuid, [ e:muOne1(MuExp exp) ], loc src) = <e,[]>;

tuple[MuExp,list[MuFunction]] makeSingleValuedBoolExp(str operator, str fuid, [ MuExp e ], loc src) = <e,[]> when !(muMulti(_) := e || muOne1(MuExp _) := e);

default tuple[MuExp,list[MuFunction]] makeSingleValuedBoolExp(str operator, str fuid, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeBoolExp(operator,fuid,exps,src);
    if(muMulti(exp) := res.e) {
        return <muOne1(exp),res.functions>;
    }
    return res;
}

// TODO: It is better to check this at the source level
private bool isEnumerator(MuExp e) = /"Library/ENUMERATE_AND_ASSIGN" := e || /"Library/ENUMERATE_CHECK_AND_ASSIGN"  := e;

private tuple[MuExp,list[MuFunction]] generateMuCode("ALL", str fuid, list[MuExp] exps, list[bool] backtrackfree, loc src) {
    if(debug)println("generateMuCode: ALL, <fuid>, <exps> <backtrackfree>");
    list[MuFunction] functions = [];
    str all_uid = "<fuid>/ALL_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", all_uid, i) | int i <- index(exps) ];
    list[MuExp] body = [ muYield0() ];
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muIfelse(nextLabel(), exps[j], body, [ muCon(222) ]) ];
        } else {
            body = [ muAssign("c_<j>", all_uid, j, muCreate1(exps[j])), muWhile(nextLabel(), muNext1(localvars[j]), body), muCon(222) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(all_uid, "ALL", fuid, 0, size(localvars), src, [], muBlock(body));
    return <muMulti(muApply(muFun2(all_uid, fuid),[])),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMuCode("RASCAL_ALL", str fuid, list[MuExp] exps, list[bool] backtrackfree, loc src) {
	if(debug)println("generateMuCode: RASCAL_ALL, <exps>");
    list[MuFunction] functions = [];
    str all_uid = "<fuid>/RASCAL_ALL_<getNextAll()>(0)";
    localvars = [ muVar("c_<i>", all_uid, i) | int i <- index(exps) ];
    list[MuExp] body = [ ];
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muIfelse(nextLabel(), exps[j], body, [ muReturn1(muCon(false)) ]) ];
        } else {
        	if(isEnumerator(exps[j])){
        		body = [ muAssign("c_<j>", all_uid, j, muCreate1(exps[j])), 
        		 		 muWhile(nextLabel(), muNext1(localvars[j]), body)
        		 	   ];
        	} else {
	        	k = size(localvars);
	        	localvars += muVar("b_<k>", all_uid, k);		// true after at least one successful iteration through loop
	            body = [ muAssign("c_<j>", all_uid, j, muCreate1(exps[j])), 
	                     muAssign("b_<k>", all_uid, k, muCon(false)),  
	                     muWhile(nextLabel(), muNext1(localvars[j]), 
	                             [ muAssign("b_<k>", all_uid, k, muCon(true)),  
	                               *body
	                             ]), 
	                     muIfelse(nextLabel(), muVar("b_<k>", all_uid, k),
	                                           [ muCon(666) ],
	                      					   [ muReturn1(muCon(false)) ])
	                   ];
             }      
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muReturn1(muCon(true)) ];

    functions += muFunction(all_uid, "RASCAL_ALL", afunc(aint(), [], []), [], atuple(atypeList([])), fuid, 0, size(localvars), false, false, true, src, [], (), false, 0, 0, muBlock(body));
    return <muCall(muFun2(all_uid, fuid),[]),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMuCode("OR", str fuid, list[MuExp] exps, list[bool] backtrackfree, loc src) {
    if(debug)println("generateMuCode: OR, <fuid>, <exps> <backtrackfree>");
    list[MuFunction] functions = [];
    str or_uid = "<fuid>/OR_<getNextOr()>(0)";
    list[MuExp] body = [];
    for(int i <- index(exps)) {
        if(backtrackfree[i]) {
            body += muIfelse(nextLabel(), exps[i], [ muYield0() ], [ muCon(222) ]);
        } else {
            body = body + [ muCall(exps[i],[]) ];
        }
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(or_uid, "OR", fuid, 0, 0, src, [], muBlock(body));
    return <muMulti(muApply(muFun2(or_uid, fuid),[])),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMuCode("IMPLICATION", str fuid, list[MuExp] exps, list[bool] backtrackfree, loc src) {
    if(debug)println("generateMuCode: IMPLICATION, <fuid>, <exps> <backtrackfree>");
    list[MuFunction] functions = [];
    str impl_uid = "<fuid>/IMPLICATION_<getNextAll()>(0)";
    localTmps = [ muTmp("c_<i>", impl_uid) | int i <- index(exps) ];
    list[MuExp] body = [ muYield0() ];
    bool first = true;
    int k = size(exps);
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muAssign("hasNext", impl_uid, k, muCon(false)),
                     muIfelse(nextLabel(), exps[j], [ muAssign("hasNext", impl_uid, k, muCon(true)) ] + body, [ muCon(222) ]), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", impl_uid, k) ]),[ muYield0() ],[ muCon(222) ]) 
                   ];
        } else {
            body = [ muAssign("hasNext", impl_uid, k, muCon(false)),
                     muAssignTmp("c_<j>", impl_uid, muCreate1(exps[j])), muWhile(nextLabel(), muNext1(localTmps[j]), [ muAssign("hasNext", impl_uid, k, muCon(true)) ] + body), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", impl_uid, k) ]),[ muYield0() ],[ muCon(222) ])
                   ];
        }
        first = false;
        k = k + 1;
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(impl_uid, "IMPLICATION", fuid, 0, k, src, [], muBlockWithTmps([ <nm, fd> |  muTmp(nm,fd) <- localTmps ], [], body));
    return <muMulti(muApply(muFun2(impl_uid, fuid),[])),functions>;
}

private tuple[MuExp,list[MuFunction]] generateMuCode("EQUIVALENCE", str fuid, list[MuExp] exps, list[bool] backtrackfree, loc src) {
    if(debug)println("generateMuCode: EQUIVALENCE, <fuid>, <exps> <backtrackfree>");
    list[MuFunction] functions = [];
    str equiv_uid = "<fuid>/EQUIVALENCE_<getNextAll()>(0)";
    localTmps = [ muTmp("c_<i>", equiv_uid) | int i <- index(exps) ];
    list[MuExp] body = [ muYield0() ];
    bool first = true;
    int k = size(exps);
    for(int i <- index(exps)) {
        int j = size(exps) - 1 - i;
        if(backtrackfree[j]) {
            body = [ muAssign("hasNext", equiv_uid, k, muCon(false)),
                     muIfelse(nextLabel(), exps[j], [ muAssign("hasNext", equiv_uid, k, muCon(true)) ] + body, [ muCon(222) ]), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", equiv_uid, k) ]),[ muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ backtrackfree[j + 1] ? newLabels(exps[j + 1]) : muNext1(muCreate1(newLabels(exps[j + 1]))) ]), [ muYield0() ], [ muCon(222) ]) ],[ muCon(222) ]) 
                   ];
        } else {
            body = [ muAssign("hasNext", equiv_uid, k, muCon(false)),
                     muAssignTmp("c_<j>", equiv_uid, muCreate1(exps[j])), muWhile(nextLabel(), muNext1(localTmps[j]), [ muAssign("hasNext", equiv_uid, k, muCon(true)) ] + body), 
                     first ? muCon(222) : muIfelse(nextLabel(),muCallMuPrim("not_mbool",[ muVar("hasNext", equiv_uid, k) ]),[ muIfelse(nextLabel(), muCallMuPrim("not_mbool",[ backtrackfree[j + 1] ? newLabels(exps[j + 1]) : muNext1(muCreate1(newLabels(exps[j + 1]))) ]), [ muYield0() ], [ muCon(222) ]) ],[ muCon(222) ]) 
                   ];
        }
        first = false;
        k = k + 1;
    }
    body = [ muGuard(muCon(true)) ] + body + [ muExhaust() ];
    functions += muCoroutine(equiv_uid, "EQUIVALENCE", fuid, 0, k, src, [], muBlockWithTmps([ <nm, fd> |  muTmp(nm,fd) <- localTmps ], [], body));
    return <muMulti(muApply(muFun2(equiv_uid, fuid), [])),functions>;
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
