module lang::rascalcore::check::GetGrammar
   
extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::grammar::definition::Layout;
import lang::rascalcore::grammar::definition::Productions;
import lang::rascalcore::grammar::definition::Keywords;
  
extend analysis::typepal::TypePal;
extend lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;
import Node;

import IO;
import Set;
import Map;
import Exception;

list[&T <: node ] unsetRec(list[&T <: node] args) = [unsetRec(a) | a <- args]; 

bool isManualLayout(AProduction p) = (p has attributes && \tag("manual"()) in p.attributes);

AGrammar getGrammar(loc scope, Solver s){
    facts = s.getFacts();
    usedADTs = {unset(t, "label") | loc k <- facts, /AType t:aadt(str name, list[AType] parameters, sr) := facts[k], sr != dataSyntax()};
    
    allStarts = {};
    allLayouts = {};
    allManualLayouts = {};
    definitions = ();
    //PM. maybe also generate prod(Symbol::empty(),[],{}) 
    for(AType adtType <- usedADTs){
        //println("getGrammar: <adtType>");
        productions = {p | <id, aprod(p)> <- s.getAllDefinedInType(adtType, scope, dataOrSyntaxIds)};
        definitions[adtType] = choice(adtType, productions);
        if(adtType.syntaxRole == layoutSyntax()){
            if(any(p <- productions, isManualLayout(p))){
               allManualLayouts += adtType;
            } else {
                allLayouts = {*allLayouts, adtType};
            }
        } else if(adtType.syntaxRole == keywordSyntax()){
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
    return expandKeywords(g);
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
                    checkKeyword(sym, {p2 | <id, aprod(p2)> <- s.getAllDefinedInType(sym, scope, dataOrSyntaxIds)}, scope, seenNTs + sym, s);
                }
            } else if(aprod(prod(aadt(_,[],_),[sym2])) := sym && isADTType(sym2)){
                // also good, provided we have not already seen this same nonterminal (recursion guard)
                if(sym2 notin seenNTs){
                    checkKeyword(sym, {p2 | <id, aprod(p2)> <- s.getAllDefinedInType(sym, scope, dataOrSyntaxIds)}, scope, seenNTs + sym2, s);
                }
            } else {
                s.report(warning(p.src, "Only literals allowed in keyword declaration, found %t via %q", sym, seenNTs));
            }
        }
    }
 }