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
            // If only overloaded due to different time stamp, use most recent.
            <ok, def> = findMostRecentDef(foundDefs);
            if(ok){        
                return {extractedTModel.definitions[def]};
            }
               throw AmbiguousDefinition(foundDefs);
          }
        }
     } catch NoSuchKey(k):
            throw TypeUnavailable();
       catch NoKey(): {
            //println("getDefinitions: <id> in scope <scope> ==\> TypeUnavailable2");
            throw TypeUnavailable();
       }
}  
        
tuple[TModel, set[ADTSummary]] getADTSummaries(Key scope, TModel tm){ 
    //println("getADTSummaries: <scope>, <size(tm.definitions)> definitions");
    //iprintln(tm, lineLimit=10000);
      
    tm.defines = { visit(def) { case AType t: auser(str name, list[AType] parameters) : { try { insert expandUserTypes(t, def.scope); } catch TypeUnavailable(): { println("Not expanded: <t> in <def>"); } } }
                 | Define def <- tm.defines
                 }; 
    tm.definitions = ( def.defined : def | Define def <- tm.defines);                
    usedADTs = {unset(t, "label") | loc k <- tm.facts, /*containedIn(k, scope),*/ /AType t:aadt(str name, list[AType] parameters, sr) := tm.facts[k]};
    
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
    tm.messages += msgs;
    return <tm, range(name2summ)>;
}

bool isManualLayout(AProduction p) = (\tag("manual"()) in p.attributes);

AGrammar getGrammar(set[ADTSummary] adtSummaries){
    allStarts = {};
    allLayouts = {};
    allManualLayouts = {};
    definitions = ();
    //PM. maybe also generate prod(Symbol::empty(),[],{}) 
    for(s <- adtSummaries){
        if(s.syntaxRole != dataSyntax()){
            a = aadt(s.adtName, s.parameters, s.syntaxRole);
            definitions[a] = choice(a, s.productions);
            if(s.syntaxRole == layoutSyntax()){
                if(any(p <- s.productions, isManualLayout(p))){
                   allManualLayouts += a;
                } else {
                    allLayouts = {*allLayouts, a};
                }
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
        g = layouts(g, defaultLayout, allManualLayouts);
   
    } else if(size(allLayouts) == 1){
        g = layouts(g, getOneFrom(allLayouts), allManualLayouts);
    } else { //TODO: resolve multiple layout
        println("$$$$$$ WARNING: Cannot yet handle multiple layout: <allLayouts>");
        //throw "Cannot yet handle multiple layout: <allLayouts>";
    }
    return expandKeywords(g);
}