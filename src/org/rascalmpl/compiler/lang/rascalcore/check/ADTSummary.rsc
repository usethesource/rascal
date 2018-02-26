module lang::rascalcore::check::ADTSummary

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::grammar::definition::Grammar;
import lang::rascalcore::grammar::definition::Layout;
import lang::rascalcore::grammar::definition::Productions;
  
extend analysis::typepal::TypePal;
extend lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;
import Node;

import Message;

import IO;
import Set;
import Map;

data ADTSummary = 
    adtSummary(str adtName, 
               list[AType] parameters, 
               set[AType] constructors = {}, 
               rel[AType, Expression] commonKeywordFields = {},     //<== should be set[Keyword]
               SyntaxRole syntaxRole = dataSyntax(), 
               bool isStart = false, 
               set[AProduction] productions = {});

list[&T <: node ] unsetRec(list[&T] args) = [unsetRec(a) | a <- args];   
        
tuple[list[Message], set[ADTSummary]] getADTSummaries(Key scope, TModel tm){   
    
    usedADTs =  {unset(expandUserTypes(t, scope), "label") | def <- tm.definitions, containedIn(def, scope), /AType t:auser(str name, parameters) := tm.definitions[def] }
              + {unset(t, "label") | k <- tm.facts, containedIn(k, scope), /AType t:aadt(str name, parameters, sr) := tm.facts[k]};
   
    //println("usedADTs: <usedADTs>");
    name2ADT = (<a.adtName, unsetRec(a.parameters)> : a | a <- usedADTs );
    
    map[tuple[str, list[AType]], ADTSummary] name2summ = ();
    msgs = [];
    allStarts = {};
    for(u <- usedADTs){
        try {
            defs = getDefinitions(u.adtName, scope, dataOrSyntaxIds);
            uConstructors = {*def.defInfo.constructors | def <- defs};
            //println("uConstructors: <uConstructors>");
            uConstructors = visit(uConstructors) { case uu:auser(nm,params,label=l) => name2ADT[<nm,unsetRec(params)>][label=l] };      
            
            uCommonKeywordFields = {*def.defInfo.commonKeywordFields | def <- defs};
            uCommonKeywordFields = visit(uCommonKeywordFields) { case uu:auser(nm,params,label=l) => name2ADT[<nm,unsetRec(params)>][label=l] };
            
            uProductions = {*def.defInfo.productions | def <- defs};
            //println("before uProductions: <uProductions>");
            uProductions = visit(uProductions) { case uu:auser(nm,params,label=l) => name2ADT[<nm,unsetRec(params)>][label=l] };
            //println("after uProductions: <uProductions>");

            for(p <- uProductions){
                msgs += validateProduction(p);
            }  
  
            uIsStart = any(def <- defs, def.defInfo.isStart );
            
            if(name2summ[<u.adtName, u.parameters>]?){
                ADTSummary summ = name2summ[u.adtName];
                summ.productions=uProductions;
                summ.constructors=uConstructors;
                summ.commonKeywordFields = uCommonKeywordFields;
                if(uIsStart) summ.isStart = true;
                if(u.syntaxRole != dataSyntax()) summ.syntaxRole = u.syntaxRole;
                name2summ[<u.adtName, u.parameters>] = summ;
            } else {
                ADTSummary summ = adtSummary(u.adtName, u.parameters, productions=uProductions, constructors=uConstructors, commonKeywordFields=uCommonKeywordFields);
                if(uIsStart) summ.isStart = true;
                if(u.syntaxRole != dataSyntax()) summ.syntaxRole = u.syntaxRole;
                name2summ[<u.adtName,u.parameters>] = summ;
            }
         } catch TypeUnavailable(): println("<u.adtName> unavailable") ;
           catch AmbiguousDefinition(foundDefs): println("Ambiguous definition for <u.adtName>: <foundDefs>");
    }
    return <msgs, range(name2summ)>;
}

AGrammar getGrammar(set[ADTSummary] adtSummaries){
    allStarts = {};
    allLayouts = {};
    definitions = ();
    //PM. maybe also generate prod(Symbol::empty(),[],{})
    for(s <- adtSummaries){
        if(s.syntaxRole != dataSyntax()){
            a = aadt(s.adtName, s.parameters, s.syntaxRole);
            definitions[a] = choice(a, s.productions);
            if(s.syntaxRole == layoutSyntax()){
                allLayouts = {*allLayouts, a};
            }
           
            if(s.isStart){
                allStarts += a; 
                definitions[\start(a)] = choice(\start(a), { prod(\start(a), [a]) });
            }
        }
    }
    //println("allStarts: <allStarts>");
    //println("allLayouts: <allLayouts>");
    //iprintln(definitions);
    g = grammar(allStarts, definitions);
    
    if(isEmpty(allLayouts)){
        defaultLayout = aadt("$default$", [], layoutSyntax());
        definitions += (defaultLayout : choice(defaultLayout, {prod(defaultLayout, [])}));
        g = grammar(allStarts, definitions);
        g = layouts(g, defaultLayout, {});
    } else if(size(allLayouts) == 1){
        g = layouts(g, getOneFrom(allLayouts), {});
    } else {
        throw "Cannot yet handle multiple layout: <allLayouts>";
    }
    return g;
}