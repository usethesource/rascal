module experiments::Compiler::muRascal::Load

import experiments::Compiler::muRascal::Parse;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;
import experiments::Compiler::muRascal::Syntax;

MuModule load(loc l) = implodeMuRascal(parseMuRascal(l));
MuModule load(str src) = implodeMuRascal(parseMuRascal(src));

MuModule implodeMuRascal(Tree t) = preprocess(implode(#experiments::Compiler::muRascal::AST::Module, t));

/*
MuExp loadstr(str src) = ip(parse(#Exp, src));

MuExp ip(experiments::Compiler::muRascal::Syntax::Exp e){
    switch(e){
        //case (Exp) `<Label lid>`: return muLab("<lid>");
        //case (Exp) `<ModNamePart modName><FConst fid>::<Integer nformals>`:
        //    return preFunNN(ip(modName), ip(fig), ip(nformal));
        //case (Exp) `<FunNamePart+ funNames> <FConst fid>::<Integer nformals>`:
        //    return preFunN(ip(funNames), ip(fid), ip(nformals));
        //case (Exp) `cons <FConst cid>`:
        //    return muConstr(ip(cid));
        //case (Exp) `deref <Identifier id>`:
        //    return preLocDeref(ip(id));
        //case (Exp) `deref <FunNamePart+ funNames> <Identifier id>`:
        //    return preVarDeref(ip(funNames), ip(id));
        //case (Exp) `prim(<String name>)`:
        //    return preMuCallPrim1(ip(name));
        //case (Exp) `prim(<String name>, <{Exp ","}+ largs1> )`:
        //    return preMuCallPrim2(ip(name), ip(largs1)); 
        //case (Exp) `muprim(<String name>, <{Exp ","}+ largs1> )`:
        //    return muCallMuPrim(ip(name), ip(largs1)); 
        //case (Exp) `multi(<Exp exp>)`:
        //    return muMulti(ip(exp)); 
        //case (Exp) `one(<{Exp ","}+ exps >)`:
        //    return muOne2(ip(exps)); 
        //case (Exp) `all(<{Exp ","}+ exps >)`:
        //    return muAll(ip(exps));
        //case (Exp) `<Exp exp>(<{Exp ","}* largs0>)`:
        //    return muCall(ip(exp), ip(largs0));
        //case (Exp) `bind(<Exp exp>, <{Exp ","}+ largs1>)`:
        //    return muApply(ip(exp), ip(largs1));
        //case (Exp) `<Exp exp>[ <Exp index>]`:
        //    return preSubscript(ip(exp), ip(index));
        //case (Exp) `[<{Exp ","}* exps0>]`:
        //    return preList(ip(exps0));
        //case (Exp) `<Exp lhs> / <Exp rhs>`:
        //    return preDivision(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> * <Exp rhs>`:
        //    return preMultiplication(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> + <Exp rhs>`:
        //    return preAddition(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> - <Exp rhs>`:
        //    return preSubtraction(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> mod <Exp rhs>`:
        //    return preModulo(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> pow <Exp rhs>`:
        //    return prePower(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> \< <Exp rhs>`:
        //    return preLess(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> \<= <Exp rhs>`:
        //    return preLessEqual(ip(lhs), ip(rhs));   
        //case (Exp) `<Exp lhs> \> <Exp rhs>`:
        //    return preGreater(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> \>= <Exp rhs>`:
        //    return preGreaterEqual(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> == <Exp rhs>`:
        //    return preEqual(ip(lhs), ip(rhs));  
        //case (Exp) `<Exp lhs> != <Exp rhs>`:
        //    return preNotEqual(ip(lhs), ip(rhs));
        //case (Exp) `<Exp lhs> && <Exp rhs>`:
        //    return preAnd(ip(lhs), ip(rhs));  
        //case (Exp) `<Exp lhs> || <Exp rhs>`:
        //    return preOr(ip(lhs), ip(rhs));  
        //case (Exp) `<Exp lhs> is <TConst typeName>`:
        //    return preIs(ip(lhs), ip(typeName));
        //case (Exp) `<Exp exp1> [ <Exp index> ] = <Exp exp2>`:
        //    return preAssignSubscript(ip(exp1), ip(index), ip(exp2));
        //case (Exp) `[ <Identifier id1> , <Identifier id2> ] = <Exp exp>`:
        //    return preAssignLocList(ip(id1), ip(id2), ip(exp));
        //case (Exp) `<Identifier id> = <Exp exp>`:
        //    return preAssignLoc(ip(id), ip(exp));
        //case (Exp) `<FunNamePart+ funNames> <Identifier id> = <Exp exp>`:
        //    return preAssign(ip(funNames), ip(id), ip(Exp));
        //case (Exp) `deref <Identifier id> = <Exp exp>`:
        //    return preAssignLocDeref(ip(id), ip(exp));
        //case (Exp) `deref <FunNamePart+ funNames> <Identifier id> = <Exp exp>`:
        //    return preAssignVarDeref(ip(funNames), ip(id), ip(exp));
            
        //case (Exp) `if( <Exp exp1> ){ <{Exp (NoNLList Sep NoNLList)}+ thenPart>; } else {<{Exp (NoNLList Sep NoNLList)}+ elsePart>; }`:
        //    return preIfelse(ip(exp1), ip(thenPart), ip(elsePart));
            
            
         case (Exp) `while(<Exp cond>){<{Exp XSep}+ body>}`:
                return  preWhile(ip(cond), ip(body));
            
 /*                                  
               
        
            | preIfelse:                "if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}" "else" "{" {Exp (NoNLList Sep NoNLList)}+ elsePart ";"? "}"
            | preIfelse:                Label label ":" "if" "(" Exp exp1 ")" "{" {Exp (NoNLList Sep NoNLList)}+ thenPart ";"? "}" "else" "{" {Exp (NoNLList Sep NoNLList)}+ elsePart ";"? "}"
            | preWhile:                 "while" "(" Exp cond ")" "{" {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
            | preWhile:                 Label label ":" "while" "(" Exp cond ")" "{" {Exp (NoNLList Sep NoNLList)}+ body ";"? "}"
            
            | preTypeSwitch:            "typeswitch" "(" Exp exp ")" "{" (TypeCase ";"?)+ cases "default" ":" Exp default ";"? "}"
            
            | muCreate1:                "create" "(" Exp coro ")"
            | muCreate2:                "create" "(" Exp coro "," {Exp ","}+ largs1 ")"
            
            | muNext1:                  "next" "(" Exp coro ")"
            | muNext2:                  "next" "(" Exp coro "," {Exp ","}+ largs1 ")"
            
            > muReturn1:                "return" NoNLList Exp exp
            | muReturn2:                 () "return" NoNLList "(" Exp exp "," {Exp ","}+ exps1 ")"
            | muReturn0:                () () "return"
            
            | muYield1:                     "yield" NoNLList Exp exp
            | muYield2:                  () "yield" NoNLList "(" Exp exp "," {Exp ","}+ exps1 ")"
            | muYield0:                     () () "yield"
            
            | muExhaust:                "exhaust"
            
            // delimited continuations (experimental feature)
            | preContLoc:               "cont"
            | preContVar:               FunNamePart+ funNames "cont"
            | muReset:                  "reset" "(" Exp fun ")"
            | muShift:                  "shift" "(" Exp ebody ")"
            
            // call-by-reference: expressions that return a value location
            | preLocRef:                "ref" Identifier!fvar!rvar id
            | preVarRef:                "ref" FunNamePart+ funNames Identifier!fvar!rvar id
            
            | preBlock:                 "{" {Exp (NoNLList Sep NoNLList)}+ bexps ";"? "}"
            
            | bracket                   "(" Exp exp ")"
            ;

    }

}
*/