module lang::rascalcore::check::ADTSummary
   
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

bool isManualLayout(AProduction p) = (\tag("manual"()) in p.attributes);

AGrammar getGrammar(loc scope, Solver s){
    facts = s.getTModel().facts;
    usedADTs = {unset(t, "label") | loc k <- facts, /*containedIn(k, scope),*/ /AType t:aadt(str name, list[AType] parameters, sr) := facts[k], sr != dataSyntax()};
    
    allStarts = {};
    allLayouts = {};
    allManualLayouts = {};
    definitions = ();
    //PM. maybe also generate prod(Symbol::empty(),[],{}) 
    for(adtType <- usedADTs){
        productions = {p | aprod(p) <- s.getAllTypesInType(adtType, scope)};
       
        definitions[adtType] = choice(adtType, productions);
        if(adtType.syntaxRole == layoutSyntax()){
            if(any(p <- productions, isManualLayout(p))){
               allManualLayouts += adtType;
            } else {
                allLayouts = {*allLayouts, adtType};
            }
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