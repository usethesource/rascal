@bootstrapParser
module lang::rascalcore::check::ADTandGrammar
   
extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::BasicRascalConfig;

import lang::rascalcore::grammar::definition::Layout;
import lang::rascalcore::grammar::definition::Keywords;
  
import lang::rascal::\syntax::Rascal;

import IO;
import Node;
import Set;

set[AType] addADTs(Solver s){
    facts = s.getFacts();
    usedADTs = { t | loc k <- facts, /AType t:aadt(str name, list[AType] parameters, sr) := facts[k]};
    //usedDataADTs = {unset(t, "label") | loc k <- facts, /AType t:aadt(str name, list[AType] parameters, sr) := facts[k], sr == dataSyntax()};
    s.putStore("ADTs", usedADTs);
    return usedADTs;
}

list[&T <: node ] unsetRec(list[&T <: node] args) = [unsetRec(a) | a <- args]; 

bool isManualLayout(AProduction p) = (p has attributes && \tag("manual"()) in p.attributes);

AGrammar addGrammar(loc scope, Solver s){
    try {
        facts = s.getFacts();
      
        usedProductions = {<p.def, p> | loc k <- facts, aprod(p) := facts[k] };
        
        allStarts = {};
        allLayouts = {};
        allManualLayouts = {};
        definitions = ();
        //PM. maybe also generate prod(Symbol::empty(),[],{}) 
        for(AType adtType <- domain(usedProductions)){
            //println("getGrammar: <adtType>");
            productions = usedProductions[adtType];
            //println("getGrammar: <productions>");
            definitions[adtType] = choice(adtType, productions);
            syntaxRole = (\start(AType t) := adtType) ? t.syntaxRole : adtType.syntaxRole;
            if(syntaxRole == layoutSyntax()){
                if(any(p <- productions, isManualLayout(p))){
                   allManualLayouts += adtType;
                } else {
                    allLayouts = {*allLayouts, adtType};
                }
            } else if(syntaxRole == keywordSyntax()){
                checkKeyword(adtType, productions, scope, {}, s);
            }
           
            //if(s.isStart){
            //    allStarts += adtType; 
            //    definitions[\start(a)] = choice(\start(adtType), { prod(\start(adtType), [adtType]) });
            //}
        }
        //println("allStarts: <allStarts>");
        //println("allLayouts: <allLayouts>");
        //iprintln(definitions);
        g = grammar(allStarts, definitions);
        
        if(isEmpty(allLayouts)){
            defaultLayout = aadt("$default$", [], layoutSyntax());
            definitions += (defaultLayout : choice(defaultLayout, {prod(defaultLayout, [])}));
            g = grammar(allStarts, definitions);
            g = layouts(g, defaultLayout, allManualLayouts);
       
        } else if(size(allLayouts) == 1){
            g = layouts(g, getOneFrom(allLayouts), allManualLayouts);
        } else { //TODO: resolve multiple layout
            println("$$$$$$ WARNING: Cannot yet handle multiple layout: <allLayouts>");
            //throw "Cannot yet handle multiple layout: <allLayouts>";
        }
        g = expandKeywords(g);
        s.putStore("grammar", g);
        return g;
    } catch TypeUnavailable(): {
        // protect against undefined entities in the grammar that have not yet been reported.
        return grammar({}, ());
    }
}
// A keyword production may only contain:
// - literals
// - other nonterminals that satisfy this rule.

 void checkKeyword(AType adtType, set[AProduction] productions, loc scope, set[AType] seenNTs, Solver s){
   // println("ckeckKeyword: <adtType>, <productions>, <seenNTs>");
    seenNTs += adtType;
    for(/p: prod(AType def, list[AType] asymbols) <- productions){
        for(AType sym <- asymbols){
            if(lit(_) := sym || aprod(prod(aadt(_,[],_),[lit(_)])) := sym){
                ; // good!
            } else if(isADTType(sym)){
                // also good, provided we have not already seen this same nonterminal (recursion guard)
                if(sym notin seenNTs){
                    checkKeyword(sym, {p2 | <id, aprod(p2)> <- s.getAllDefinedInType(sym, scope, dataOrSyntaxRoles)}, scope, seenNTs + sym, s);
                }
            } else if(aprod(prod(aadt(_,[],_),[sym2])) := sym && isADTType(sym2)){
                // also good, provided we have not already seen this same nonterminal (recursion guard)
                if(sym2 notin seenNTs){
                    checkKeyword(sym, {p2 | <id, aprod(p2)> <- s.getAllDefinedInType(sym, scope, dataOrSyntaxRoles)}, scope, seenNTs + sym2, s);
                }
            } else {
                s.report(warning(p.src, "Only literals allowed in keyword declaration, found %t via %q", sym, seenNTs));
            }
        }
    }
 }