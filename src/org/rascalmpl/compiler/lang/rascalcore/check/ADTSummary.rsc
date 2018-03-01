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
import Exception;

data ADTSummary = 
    adtSummary(str adtName, 
               list[AType] parameters, 
               set[AType] constructors = {}, 
               rel[AType, Expression] commonKeywordFields = {},     //<== should be set[Keyword]
               SyntaxRole syntaxRole = dataSyntax(), 
               bool isStart = false, 
               set[AProduction] productions = {});

list[&T <: node ] unsetRec(list[&T <: node] args) = [unsetRec(a) | a <- args]; 

// A copy of getDefinitions but with extra TModel argument
// TODO: reconsider this
set[Define] getDefinitions(str id, Key scope, set[IdRole] idRoles, TModel tm){
    try {
        foundDefs = lookupFun(tm, use(id, anonymousOccurrence, scope, idRoles));
        if({def} := foundDefs){
           return {tm.definitions[def]};
        } else {
          if(mayOverloadFun(foundDefs, tm.definitions)){
            return {tm.definitions[def] | def <- foundDefs};
          } else {
               throw AmbiguousDefinition(foundDefs);
          }
        }
     } catch NoSuchKey(k):
            throw TypeUnavailable();
       catch NoKey(): {
            println("getDefinitions: <id> in scope <scope> ==\> TypeUnavailable2");
            throw TypeUnavailable();
       }
}  
        
tuple[list[Message], set[ADTSummary]] getADTSummaries(Key scope, TModel tm){ 
    println("getADTSummaries: <scope>, <size(tm.definitions)> definitions");
    iprintln(tm);
    tm.definitions = ( key : visit(tm.definitions[key]) { case AType t: auser(str name, list[AType] parameters) : { try { insert expandUserTypes(t, def.scope); } catch TypeUnavailable(): { println("Not expanded: <t> in (<key>: <def>)"); } } }
                     | key <- tm.definitions, def := tm.definitions[key]); 
    //tm.definitions = visit(tm.definitions) { case AType t: auser(str name, list[AType] parameters) : { try { insert expandUserTypes(t, scope); } catch TypeUnavailable(): { println("Not expanded: <t>"); } } };
    usedADTs = {unset(t, "label") | k <- tm.facts, containedIn(k, scope), /AType t:aadt(str name, list[AType] parameters, sr) := tm.facts[k]};
    
   //println("usedADTs: <usedADTs>");
    
    map[tuple[str, list[AType]], ADTSummary] name2summ = ();
    msgs = [];
    allStarts = {};
    for(u <- usedADTs){
        try {
            defs = getDefinitions(u.adtName, scope, dataOrSyntaxIds, tm);
            uConstructors = {*def.defInfo.constructors | def <- defs};           
            uCommonKeywordFields = {*def.defInfo.commonKeywordFields | def <- defs};  
            uProductions = {*def.defInfo.productions | def <- defs};

            for(p <- uProductions){
                msgs += validateProduction(p);
            }  

            uIsStart = any(def <- defs, def.defInfo.isStart );
            
            if(name2summ[<u.adtName, u.parameters>]?){
                ADTSummary summ = name2summ[<u.adtName, u.parameters>];
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
        assert s has syntaxRole: "ADTSummary3";
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