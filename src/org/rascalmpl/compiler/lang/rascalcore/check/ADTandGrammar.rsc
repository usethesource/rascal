@bootstrapParser
module lang::rascalcore::check::ADTandGrammar
   
extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::grammar::definition::Layout;
import lang::rascalcore::grammar::definition::Keywords;
//import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
  
import lang::rascal::\syntax::Rascal;

import IO;
import Node;
import Set;
import ListRelation;
import Relation;
import Message;

void addADTsAndCommonKeywordFields(Solver s){
    addADTs(s);
    addCommonKeywordFields(s);
}

void addADTs(Solver s){
    facts = s.getFacts();
    usedADTs = { t | loc k <- facts, /AType t:aadt(str name, list[AType] parameters, sr) := facts[k] };
    s.push(key_ADTs, usedADTs);
}

void addCommonKeywordFields(Solver s){
    set[Define] definitions = s.getAllDefines();
    commonKeywordFields = [];
    
    // Collect common keywords and check double declarations
  
    rel[AType,str,KeywordFormal] commonKeywordFieldNames = {};
    for(Define def <- definitions, def.idRole == dataId()){
        try {
            adtType = s.getType(def);
            commonKeywordNames = commonKeywordFieldNames[adtType]<0>;
            for(kwf <- def.defInfo.commonKeywordFields){
                fieldName = "<kwf.name>";
                commonKeywordFields += <adtType, kwf>;
                commonKeywordFieldNames += <adtType, fieldName, kwf>;
                // TODO: reconsider this
                //if(fieldName in commonKeywordNames){
                //    msgs = [ Message::error("Double declaration of common keyword Field `<fieldName>` for data type `<adtType.adtName>`", getLoc(kwf2))
                //           | kwf2 <- commonKeywordFieldNames[adtType]<1>, "<kwf2.name>" == fieldName
                //           ];
                //    s.addMessages(msgs);
                //}
            }
        } catch TypeUnavailable():
            ;//s.addMessages([ Message::error("Unavailable type in declaration of `<def.id>`", def.defined) ]);
      
    }
    s.push(key_common_keyword_fields, commonKeywordFields);
    
    // Warn for overlapping declarations of common keyword fields and ordinary fields
      
    adt_common_keyword_fields_name_and_kwf = ( adtType : ( "<kwf.name>" : kwf | kwf <- commonKeywordFields[adtType] ? []) | adtType <- domain(commonKeywordFields) );
    
    for(Define def <- definitions, def.idRole == constructorId()){
        try {
            consType = s.getType(def);
            commonFieldNames = domain(adt_common_keyword_fields_name_and_kwf[consType.adt] ? []);
            for(fld <- consType.fields){
               if(fld.label in commonFieldNames){
                    kwf = adt_common_keyword_fields_name_and_kwf[consType.adt][fld.label];
                    msgs = [ Message::warning("Common keyword field `<fld.label>` of data type `<consType.adt.adtName>` overlaps with field of constructor `<consType.label>`", getLoc(kwf)),
                             Message::warning ("Field `<fld.label>` of constructor `<consType.label>` overlaps with common keyword field of data type `<consType.adt.adtName>`", def.defined)
                           ];
                    s.addMessages(msgs);
                }
            }
        } catch TypeUnavailable():
            ;//s.addMessages([ Message::error("Unavailable type in declaration of `<def.id>`", def.defined) ]);
         
    }
    
    lrel[AType,AType,Define] adt_constructors = [];
    for(Define def <- definitions, def.idRole == constructorId()){
        try {
            consType = s.getType(def);
            if(consType.label == "type") continue; // TODO: where is the duplicate?
            conses_so_far = adt_constructors[consType.adt];
            //for(<AType c, Define cdef> <- conses_so_far, c.label == consType.label, cdef.defined != def.defined, comparable(c.fields, consType.fields)){
            //    msgs = [ Message::error("Duplicate/comparable constructor `<consType.label>` of data type `<consType.adt.adtName>`", def.defined),
            //             Message::error("Duplicate/comparable constructor `<consType.label>` of data type `<consType.adt.adtName>`", cdef.defined)
            //           ];
            //    s.addMessages(msgs);
            //}
            adt_constructors += <consType.adt, consType, def>;   
        } catch TypeUnavailable():
            ;//s.addMessages([ Message::error("Unavailable type in declaration of `<def.id>`", def.defined) ]);
        
    }
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
        
        seenNTsForKeywordCheck = {};
        //PM. maybe also generate prod(Symbol::empty(),[],{}) 
        for(AType adtType <- domain(usedProductions)){
            if(\start(adtType2) := adtType){
                allStarts += adtType2; 
                definitions[adtType] = choice(adtType, { prod(adtType, [adtType2]) });
                adtType = adtType2;
            }
            syntaxRole = adtType.syntaxRole;
            //println("getGrammar: <adtType>");
            productions = usedProductions[adtType];
            //println("getGrammar: <productions>");
            definitions[adtType] = choice(adtType, productions);
           
            if(syntaxRole == layoutSyntax()){
                if(any(p <- productions, isManualLayout(p))){
                   allManualLayouts += adtType;
                } else {
                    allLayouts = {*allLayouts, adtType};
                }
            } else if(syntaxRole == keywordSyntax()){
                seenNTsForKeywordCheck = checkKeyword(adtType, productions, scope, {} /*seenNTsForKeywordCheck*/, s);
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
        } else { // Warn for  multiple layout definitions
            allLayoutNames = {ladt.adtName | ladt <- allLayouts};
            for(AType ladt <- allLayouts){
                otherLayoutNames = {"`<lname>`" | str lname <- (allLayoutNames - ladt.adtName)};
                for(p <- definitions[ladt].alternatives){
                    s.report(warning(p.src, 
                                    size(otherLayoutNames) == 1 ? "Multiple layout definitions: layout %q can interfere with layout %v"
                                                                : "Multiple layout definitions: layout %q can interfere with layouts %v"
                                    , ladt.adtName, otherLayoutNames));
                }
            }
        }
        g = expandKeywords(g);
        s.push(key_grammar, g);
        return g;
    } catch TypeUnavailable(): {
        // protect against undefined entities in the grammar that have not yet been reported.
        return grammar({}, ());
    }
}
// A keyword production may only contain:
// - literals
// - other nonterminals that satisfy this rule.

 set[AType] checkKeyword(AType adtType, set[AProduction] productions, loc scope, set[AType] seenNTs, Solver s){
    seenNTs += adtType;
    syntaxRole = (\start(AType t) := adtType) ? t.syntaxRole : adtType.syntaxRole;
    for(/p: prod(AType def, list[AType] asymbols) <- productions){
        if(syntaxRole == keywordSyntax() && size(asymbols) != 1){
            s.report(warning(p.src, size(asymbols) == 0 ? "One symbol needed in keyword declaration"
                                                        : "Keyword declaration should consist of one symbol"));
            continue;
        }
        for(AType sym <- asymbols){
            if(lit(_) := sym || aprod(prod(aadt(_,[],_),[lit(_)])) := sym){
                ; // good!
            } else if(isADTType(sym)){
                // also good, provided we have not already seen this same nonterminal (recursion guard)
                if(sym notin seenNTs){
                    seenNTs = checkKeyword(sym, {p2 | <id, aprod(p2)> <- s.getAllDefinedInType(sym, scope, dataOrSyntaxRoles)}, scope, seenNTs + sym, s);
                }
            } else if(aprod(prod(aadt(_,[],_),[sym2])) := sym && isADTType(sym2)){
                // also good, provided we have not already seen this same nonterminal (recursion guard)
                if(sym2 notin seenNTs){
                    seenNTs = checkKeyword(sym, {p2 | <id, aprod(p2)> <- s.getAllDefinedInType(sym, scope, dataOrSyntaxRoles)}, scope, seenNTs + sym2, s);
                }
            } else {
                syntaxRole = (\start(AType t) := adtType) ? t.syntaxRole : adtType.syntaxRole;
                if(syntaxRole == keywordSyntax()){
                    s.report(warning(p.src, "Only literals allowed in keyword declaration, found %t", sym));
                }
            }
        }
    }
    return seenNTs;
 }