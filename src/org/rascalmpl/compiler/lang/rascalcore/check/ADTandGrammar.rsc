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
import Location;
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

TModel addGrammar(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,TModel] tmodels){
    try {
        usedProductions = {};
        allStarts = {};
        tm = tmodels[qualifiedModuleName];
        for(m <- {qualifiedModuleName, *imports, *extends}){
            facts = tmodels[m].facts;
            prodLocs1 = { k | loc k <- facts, aprod(p) := facts[k] };
            
            // filter out productions contained in priority/associativity declarations
            prodLocs2 = { k | k <- prodLocs1, !any(l <- prodLocs1, isStrictlyContainedIn(k, l)) };

            usedProductions += {<p.def, p> | loc k <- prodLocs2, aprod(p) := facts[k] };
            allStarts += { t | loc k <- facts, \start(t) := facts[k] };
        }
        
        allLayouts = {};
        allManualLayouts = {};
        syntaxDefinitions = ();
        
        //PM. maybe also generate prod(Symbol::empty(),[],{})
        for(AType adtType <- domain(usedProductions)){
            if(\start(adtType2) := adtType){
                adtType = adtType2;
            }
            productions = usedProductions[adtType];
            syntaxDefinitions[adtType] = choice(adtType, productions);
            
            if(adtType.syntaxRole == layoutSyntax()){
                if(any(p <- productions, isManualLayout(p))){
                   allManualLayouts += adtType;
                } else {
                    allLayouts = {*allLayouts, adtType};
                }
            } 
        }
        
        // Check keyword rules
        
        tm = checkKeywords(usedProductions, tm);
        
        // Check layout
    
        if(size(allLayouts) > 1) { // Warn for  multiple layout definitions
            allLayoutNames = {ladt.adtName | ladt <- allLayouts};
            for(AType ladt <- allLayouts){
                otherLayoutNames = {"`<lname>`" | str lname <- (allLayoutNames - ladt.adtName)};
                for(p <- syntaxDefinitions[ladt].alternatives){
                    tm.messages += [warning(interpolate("Multiple layout definitions: layout %q can interfere with layout %v", AType(Tree t) { return tm.facts[getLoc(t)]; }, [ladt.adtName, otherLayoutNames]),
                                            p.src)];
                }
            }
        }
        
        definedLayout = aadt("$default$", [], layoutSyntax());
        if(isEmpty(allLayouts)){
            syntaxDefinitions += (definedLayout : choice(definedLayout, {prod(definedLayout, [])}));
        } else if(size(allLayouts) >= 1){
            definedLayout = getOneFrom(allLayouts);
        } 
        
        // Add start symbols
        
        for(adtType <- allStarts){
            syntaxDefinitions[\start(adtType)] = choice(\start(adtType), { prod(\start(adtType), [definedLayout, adtType[label="top"], definedLayout]) });
        }
        
        g = grammar(allStarts, syntaxDefinitions);
        g = expandParameterizedNonTerminals(g, tm);
        g = layouts(g, definedLayout, allManualLayouts);
        g = expandKeywords(g);
        g.rules += (AType::empty():choice(AType::empty(), {prod(AType::empty(),[])}));
        tm.store[key_grammar] = [g];
        iprintln(g);
        return tm;
    } catch TypeUnavailable(): {
        // protect against undefined entities in the grammar that have not yet been reported.
        return tmodels[qualifiedModuleName];
    }
}

// A keyword production may only contain:
// - literals
// - other nonterminals that satisfy this rule.

TModel checkKeywords(rel[AType, AProduction] usedProductions, TModel tm){
    allLiteral = {};
    solve(allLiteral){
        forADT:
        for(AType adtType <- domain(usedProductions), adtType notin allLiteral){
            for(prod(AType def, list[AType] asymbols) <- usedProductions[adtType]){
                for(sym <- asymbols){
                    if(!(lit(_) := sym || aprod(prod(aadt(_,[],_),[lit(_)])) := sym || (aprod(prod(a:aadt(_,[],_),_)) := sym && a in allLiteral))){
                        continue forADT;
                    }
                }
                allLiteral += adtType;
            }
        }
    }
    for(AType adtType <- domain(usedProductions), bprintln(adtType), ((\start(AType t) := adtType) ? t.syntaxRole : adtType.syntaxRole) == keywordSyntax()){
        for(p:prod(AType def, list[AType] asymbols) <- usedProductions[adtType]){
            if(size(asymbols) != 1){
                tm.messages += [warning(size(asymbols) == 0 ? "One symbol needed in keyword declaration" : "Keyword declaration should consist of one symbol", p.src)];
            }
            for(sym <- asymbols){
                if(!isADTType(sym) || isADTType(sym) && sym notin allLiteral){
                    tm.messages += [warning(interpolate("Only literals allowed in keyword declaration, found %t", AType(Tree t) { return tm.facts[getLoc(t)]; }, [sym]), p.src) ];
                }
            }
        }
    }
    return tm;
}

AGrammar expandParameterizedNonTerminals(AGrammar grammar, TModel tm){
    ADTs = {};
    if([*set[AType] adts] := tm.store[key_ADTs]){
        ADTs = adts[0];
    } else {
        throw "`ADTs` has incorrect format in store";
    }
    
    prods = {grammar.rules[nt] | nt <- grammar.rules};
    
    // First we collect all the parametrized definitions
    defs = { p | p <- prods, bprintln(p), AType adt := unset(p.def, "label"), params := getADTTypeParameters(adt), !isEmpty(params)};
    result = prods - defs;
  
    // Then we collect all the uses of parameterized sorts in the other productions
    uses = { unset(u, "label") | /u:aadt(str name, list[AType] parameters, sr) <- result, sr != dataSyntax(), !isEmpty(parameters)};
    
    instantiated_adts  = { unset(adt, "label") | adt <- ADTs, params := getADTTypeParameters(adt), !isEmpty(params), all(p<-params, !isRascalTypeParam(p)) };
    
    uses += instantiated_adts;
    instantiated = {};
    while (uses != {}) {
        instances = {};
        for (u <- uses, def <- defs, bprintln(def), bprintln(u), def.def.adtName == u.adtName) {
           name = u.adtName;
           actuals = u.parameters;
           formals = def.def.parameters;
           instantiated += {u};
           substs = (formals[i]:actuals[i] | int i <- index(actuals) & index(formals));
           instances = {*instances, visit (def) {
                                        case AType par:\aparameter(_,_) : if(par.label?) insert substs[unset(par, "label")][label=par.label]?par;
                                                                          else insert substs[par]?par;
                                    }}; 
        }
  
        // now, we may have created more uses of parameterized symbols, by instantiating nested parameterized symbols
        uses = { s | /AType s <- instances, isADTType(s), params := getADTTypeParameters(s), !isEmpty(params), s notin instantiated};
        result += instances;
  }
  grammar.rules = (r.def : choice(r.def, r.alternatives) | r <- result, bprintln(r));
  return grammar;
}
