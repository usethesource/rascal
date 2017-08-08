module experiments::Compiler::muRascal::mu2mu

import List;
import Map;
import Node;
import Message;
import Type;
import IO;
import util::Reflective;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::CompileMuLibrary;

//MuModule transform(MuModule m, PathConfig pcfg) = m;

map[str, MuFunction] availableFunctions;

MuModule transform(MuModule m, PathConfig pcfg){
    MuModule muLib = getMuLibrary(pcfg);   
    muLibFuns = (f.qname : f | f <- muLib.functions);
    modFuns = (f.qname : f | f <- m.functions);
    
    availableFunctions = muLibFuns + modFuns;
              
    m.functions = [transform(f) | f <- m.functions];
    return m;
}

MuFunction transform(MuFunction f){
   if(f.uqname == "main"){
      iprintln(f);
      f.body = simplify(asMuExp(transformExps(f.qname, asList(f.body), [], [])));
      f = renameVariables(f, f.body);
      println("FINAL CODE transform:");
      iprintln(f);
   }
   return f;
}

list[MuExp] transformExps(str scope, list[MuExp] bexps, list[MuExp] trueCont, list[MuExp] falseCont){

    bexps1 = 
        for(MuExp bexp <- reverse(bexps)){
            bexp1 = transformExp(scope, bexp, trueCont, falseCont);
            trueCont = falseCont = bexp1;
            append bexp1;
        };
    return reverse(bexps1);

}

MuExp transformExp(str scope, MuExp bexp, list[MuExp] trueCont, list[MuExp] falseCont){
   println("transformExp: <bexp>
           'scope: <scope>
           'trueCont: <trueCont>
           'falseCont: <falseCont>");
   bexp = 
    bottom-up visit(bexp){
        
        case muReturn1(muIfelse(str label, MuExp cond, list[MuExp] thenPart, list[MuExp] elsePart)): {
            println("transformExp: muReturn1");
            insert transformExp(scope, muIfelse(label, cond, asList(muReturn1(asMuExp(thenPart))),
                                                             asList(muReturn1(asMuExp(elsePart)))),
                                       trueCont, falseCont);
        }
        case muApply(muApply(muFun1(str fun), list[MuExp] args1), list[MuExp] args2): {
            println("transformExp: muApply/muApply");
            insert muApply(muFun1(fun), args1 + args2);
        }
 
        case m: muIfelse(str label, cond: muMulti(a: muApply(muFun1(str fun), list[MuExp] args1)),
                                    list[MuExp] thenPart,list[MuExp] elsePart): {
            println("transformExp: Case if/multi/muApply");
            println(a);
            if(!availableFunctions[fun]?) fail;
            println("thenPart: <thenPart>");
            println("elsePart: <elsePart>");
            calledFun = availableFunctions[fun];
            calledFunName = calledFun.qname;
            e1 = asMuExp([simplify(expandFunction(scope, calledFunName, label, calledFun.body, args1, [*thenPart], [*elsePart]))]);
            
            //e1 = asMuExp([simplify(expandFunction(scope, calledFunName, calledFun.body, args1, [muAssign("RESULT", scope, 0, asMuExp(thenPart))],
            //                                                                                  [muAssign("RESULT", scope, 0, asMuExp(elsePart))])) , muVar("RESULT", scope, 0)] );
            println("transformExp: if/multi/muApply EXPANDED:");
            iprintln(e1);
            insert e1;
        }
        
        case m: muIfelse(str label, cond: muBoolMulti(str operator, list[MuExp] args1, loc src), list[MuExp] thenPart, list[MuExp] elsePart): {
            println("transformExp: Case if/muBoolMulti");
            iprintln(cond);
            e1 = transBoolMulti(scope, label, operator, args1, src, thenPart, elsePart);
            println("transformExp: if/muBoolMulti EXPANDED:");
            iprintln(e1);
            insert e1;
        ;
        }
            
        case m: muWhile(str label, cond: muBoolMulti(str operator, list[MuExp] args1, loc src), list[MuExp] body): {
            println("transformExp: Case while/muBoolMulti");
            iprintln(cond);
            e1 = transBoolMulti(scope, label, operator, args1, src, body, trueCont);
            println("transformExp: while/muBoolMulti EXPANDED:");
            iprintln(e1);
            insert e1;
        ;
        }
           
        case m: muWhile(str label, cond: muMulti(a: muApply(muFun1(str fun), list[MuExp] args1)),
                                    list[MuExp] body): {
            println("transformExps: Case while/multi/muApply");
            println(a);
            if(!availableFunctions[fun]?) fail;
            println(a);
            calledFun = availableFunctions[fun];
            calledFunName = calledFun.qname;
            e1 = asMuExp([simplify(expandFunction(scope, calledFunName, label, calledFun.body, args1, body, falseCont)) /*, muVar("RESULT", scope, 0)*/]);
            println("transformExps: while/multi/muApply EXPANDED:");
            iprintln(e1);
            insert e1;
        }
            
         case m: muWhile(str label, cond: muMulti(a: muApply(muFun2(str fun, str scope), list[MuExp] args1)),
                                    list[MuExp] body): {
            println("transformExps: Case while/multi/muApply/muFun2");
            println(a);
            if(!availableFunctions[fun]?) fail;
            calledFun = availableFunctions[fun];
            calledFunName = calledFun.qname;
            e1 = asMuExp([simplify(expandFunction(scope, calledFunName, label, calledFun.body, args1, body, falseCont))/*, muVar("RESULT", scope, 0)*/]);
            println("transformExps: while/multi/muApply/muFun2 EXPANDED:");
            iprintln(e1);
            insert e1;
        }
     }
     
      println("transformExp: returns:");
      iprintln(bexp);
      return bexp;
}

bool isConOrVar(muCon(_)) = true;
bool isConOrVar(muVar(_, _, _)) = true;
default bool isConOrVar(MuExp e) = false;

MuExp expandFunction(str scope, str calledFunName, str label, MuExp body, list[MuExp] args, list[MuExp] trueCont, list[MuExp] falseCont){ 
    switch(calledFunName){
        case "Library/MATCH_N":
            return expandMATCH_N(scope, args, trueCont, falseCont);
    }
    println("expandFunction: scope: <scope>; <calledFunName>: <args>");
    println("expandFunction: trueCont: <trueCont>");
    println("expandFunction: falseCont: <falseCont>");
    iprintln(body);
    
    if(muBlock([*exps, muExhaust()]) := body){
      body = muBlock(exps);
    }
    
    body = visit(body){
        case muGuard(muBool(true)) => muBlock([])
        //case muWhile(str l1, wcond, wbody) => muWhile(label, wcond, wbody) // only at top level?
        case e:muVar(str vname, calledFunName, int i) => args[i] when bprintln("1:<e>") && i < size(args) /* && (isConOrVar(args[i]) || muVarRef(str vname2, scope, int j) := args[i]) */
        //case e:muVar(str vname, calledFunName, int i) => muVar(vname, scope, i)
        //case muVarRef(str vname, scope, int i) => muVar(vname, scope, i)
        case e:muVarDeref(str vname, calledFunName, int i) => args[i] when bprintln(e) && i < size(args) && muVarRef(str vname2, scope, int j) := args[i]
        case muYield0() =>  asMuExp(trueCont) //muAssign("RESULT", scope, 0, asMuExp(trueCont))
        
        //case muYield1(MuExp v) => asMuExp([muAssign(vname, scope, i, v), muAssign("RESULT", scope, 0, expandFunction(scope, calledFunName, asMuExp(trueCont), [], [], []))]) when size(args) > 0 && muVarRef(str vname, scope, int i) := args[0]
        case muYield1(MuExp v): {
            for(arg <- args){
                if(muVarRef(str vname, scope, int i) := arg){
                    insert asMuExp([muAssign(vname, scope, i, v), *asList(trueCont)]);
                }
            }
        }
                     
        //case muYield1(MuExp v) => asMuExp([muAssign(vname, scope, i, v), expandFunction(scope, calledFunName, asMuExp(trueCont), [], [], [])/*, muVar(vname, scope, i)*/]) when size(args) > 0 && muVarRef(str vname, scope, int i) := args[0]
        //case muYield1(MuExp v) => asMuExp([muAssign(vname, scope, i, v), muAssign("RESULT", scope, 0, expandFunction(scope, calledFunName, asMuExp(trueCont), [], [], []))]) when size(args) > 0 && muVarRef(str vname, scope, int i) := args[0]
        //case muExhaust() => asMuExp([muAssign("RESULT", scope, 0, expandFunction(scope, calledFunName, asMuExp(falseCont), [], [], []))])
        case muExhaust() => asMuExp(falseCont)
        case muCall(muVar(str vname, calledFunName, int i), cargs) => expandFunction(scope, args[i], cargs, trueCont, falseCont) when i < size(args)
        case muCall(muFun1(str fname), cargs) => expandFunction(scope, fname, label, availableFunctions[fname].body, cargs, trueCont, falseCont)
        case w: muBlock([*exps1, muAssign(str covar, str scope1, int pos, muCreate1(muApply(muFun1(str fun), list[MuExp] cargs))), muWhile(wlabel, muNext1(muVar(covar, scope2, pos)), list[MuExp] wbody), *exps2]): {
            println("BLOCK: <w>");
           
            e = expandFunction(scope, calledFunName, substitute(availableFunctions[fun].body, cargs), cargs, wbody, []);
             println("substituted:");
             iprintln(e);
            insert muBlock([*exps1, e, *exps2]);
        }
        
         case w: muBlock([*exps1, muAssign(str pat, str scope1, int pos, muCreate1(muApply(muFun1(str fun), list[MuExp] cargs))), muWhile(wlabel, muNext1(muVar(covar, scope2, pos)), list[MuExp] wbody), *exps2]): {
            println("BLOCK2: <w>");
           
            e = expandFunction(scope, calledFunName, substitute(availableFunctions[fun].body, cargs), cargs, wbody, []);
             println("substituted:");
             iprintln(e);
            insert muBlock([*exps1, e, *exps2]);
        }
        //case MuExp e: println("unmatched: <e>");
        
    }
    
   if(muBlock([muGuard(gexp), *exps]) := body){
      body = muIfelse("", gexp, exps, []);
   }
    
    println("expandFunction: scope: <scope>; <calledFunName>: <args>\nRESULT:");
    iprintln(body);
    return body;
}


int nLabel = -1;
str nextLabel(){
    nLabel += 1;
    return "MU<nLabel>";
}

list[MuExp] asList(muBlock(*exps)) = exps;
list[MuExp] asList(list[MuExp] exps) = exps;
default list[MuExp] asList(MuExp exp) = [exp];

MuExp asMuExp(list[MuExp] exps) = muBlock(exps);
default MuExp asMuExp(MuExp exp) = exp;

MuExp transBoolMulti(str scope, str label, "ALL", list[MuExp] args, loc src, list[MuExp] trueCont, list[MuExp] falseCont){
       labels = [ i == 0 ? label : nextLabel() | i <- reverse(index(args)) ];
       println("transBoolMulti: <args>, <labels>");
       res =  ( trueCont | [ muWhile(labels[i], args[i], asList(it)), *(i == 0 ? falseCont : [muContinue(labels[i-1])])] 
                         | int i <- reverse(index(args)) );
       
       //res =  ( trueCont | muIfelse(nextLabel(), args[i], asList(it), falseCont) | int i <- reverse(index(args)) );
       println("transMultiBool: <scope>, ALL, <args>
               'trueCont:  <trueCont>
               'falseCont: <falseCont>
               'returns:");
       iprintln(res);
       return transformExp(scope, asMuExp(res), trueCont, falseCont);
   //return transformExps(scope, ( asMuExp(trueCont) | muWhile("", args[i], [it]) | int i <- reverse(index(args))), trueCont, falseCont);
}

MuExp transBoolMulti(str scope, "OR", list[MuExp] args, loc src, list[MuExp] trueCont, list[MuExp] falseCont){
       res =  ( falseCont | muIfelse(nextLabel(), args[i], trueCont, asList(it)) | int i <- reverse(index(args)) );
       println("transMultiBool: <scope>, ALL, <args>
               'trueCont:  <trueCont>
               'falseCont: <falseCont>
               'returns:   <res>");
       return transformExp(scope, res, trueCont, falseCont);
   //return transformExps(scope, ( asMuExp(trueCont) | muWhile("", args[i], [it]) | int i <- reverse(index(args))), trueCont, falseCont);
}

int whileLab = -1;

MuExp expandMATCH_N(str scope, list[MuExp] args, list[MuExp] trueCont, list[MuExp] falseCont){
    println("############### MATCH_N");
    if(muCallMuPrim("make_array", pats) := args[0]){
        subjects = args[1];
        plen = size(pats);
        println("pats:      <pats>
                'plen:      <plen>
                'subjects:  <subjects>
                'trueCont:  <trueCont>
                'falseCont: <falseCont>");
        
        asg_subjects = muAssign("subjects", "Library/MATCH_N", 1, subjects); 
        asg_slen = muAssign("slen", "Library/MATCH_N", 3,
            muCallMuPrim("size_array", [muVar("subjects","Library/MATCH_N",1)]));
        eq_plen_slen =  
            muCallMuPrim("equal_mint_mint", [ muCallMuPrim("mint", [muCon(plen)]),  muVar("slen","Library/MATCH_N",3)]);
               
        matches =  ( trueCont
                   | muIfelse("MATCH_N_<i>", muMulti(muApply(pats[i], [muCallMuPrim("subscript_array_mint", [muVar("subjects","Library/MATCH_N",1), muCallMuPrim("mint", [muCon(i)])])])),
                                 asList(it), falseCont) 
                   | int i <- reverse(index(pats)));
         
        //matches =  ( trueCont
        //           | muWhile("MATCH_N_<i>", muMulti(muApply(pats[i], [muCallMuPrim("subscript_array_mint", [muVar("subjects","Library/MATCH_N",1), muCallMuPrim("mint", [muCon(i)])])])),
        //                         asList(it)) 
        //           | int i <- reverse(index(pats)));
        return transformExp(scope, asMuExp([asg_subjects, asg_slen, muIfelse("", eq_plen_slen, asList(matches), falseCont)]), trueCont, falseCont);
    }
}

MuExp substitute(MuExp exp, list[MuExp] args){
    println("substitute: <exp>\nargs: <args>");
 //   return exp;
    return visit(exp){
        case e:muVar(str vname, calledFunName, int i) => args[i] when /*bprintln("1:<e>") &&*/ i < size(args) && (isConOrVar(args[i]) || muVarRef(str vname2, scope, int j) := args[i])
        //case e:muVar(str vname, calledFunName, int i) => muVar(vname, scope, i)
        //case muVarRef(str vname, scope, int i) => muVar(vname, scope, i)
        case e:muVarDeref(str vname, calledFunName, int i) => args[i] when /*bprintln(e) &&*/ i < size(args) && muVarRef(str vname2, scope, int j) := args[i]
        }
}

MuExp simplify(MuExp e){
    return innermost visit(e){
        case [*MuExp exps1, muBlock([*MuExp exps2]), *MuExp exps3] => [*exps1, *exps2, *exps3]
        case muBlock([exp]) => exp
    }
}

MuFunction renameVariables(MuFunction f, MuExp newBody){
    nlocals = f.nlocals;
    renaming = ();
    newBody = 
        visit(newBody){
        case e:muVar(str vname, str calledFunName, int i): {
            if(calledFunName == f.qname) fail;
            if(!renaming[e]?){
                renaming[e] = muVar(vname, f.qname, nlocals);
                nlocals += 1;
            }
            insert renaming[e];
        }
        case muAssign(str vname, str calledFunName, int i, MuExp v): {
            if(calledFunName == f.qname) fail;
            e = muVar(vname, calledFunName, i);
            if(!renaming[e]?){
                renaming[e] = muVar(vname, f.qname, nlocals);
                nlocals += 1;
            }
            re = renaming[e];
            insert muAssign(vname, f.qname, re.pos, v);
        }
    }
    println("renameVariables: nlocals old: <f.nlocals>, new: <nlocals>, nrenamed: <size(renaming)>, renaming: <renaming>");
    f.nlocals = nlocals;
    f.body = newBody;
    return f;
}