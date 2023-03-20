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
import Map;

void addADTsAndCommonKeywordFields(Solver s){
    addADTs(s);
    addCommonKeywordFields(s);
}

bool commonTypeParameter(list[AType] params1, list[AType] params2){
    n1 = size(params1);
    n2 = size(params2);
    if(n1 == 0 || n2 == 0) return false;
    return n1 == n2 && all(i <- [0..n1], params1[i] == params2[i] || isTypeParameter(params1[i]) && isTypeParameter(params2[i]));
}

list[AType] removeLabels(list[AType] params) = [ unset(p, "alabel") | p <- params ];

void addADTs(Solver s){
    facts = s.getFacts();
    defines = s.getAllDefines();
    definedADTs = { unset(t, "alabel") | def <- defines, /AType t:aadt(str _, list[AType] _, _) := def };
    usedADTs = { unset(t, "alabel") | loc k <- facts, /AType t:aadt(str _, list[AType] parameters, _) := facts[k], !isEmpty(parameters), any(p <- parameters, !isTypeParameter(p)) };  
    ADTs = { a[parameters=removeLabels(a.parameters)] | a <- definedADTs + usedADTs };
    
    // remove versions with type parameter on same position.

    solve(ADTs){
        if(any(a1 <- ADTs, a2 <- ADTs, a1 != a2, a1.adtName == a2.adtName, commonTypeParameter(a1.parameters, a2.parameters))){
            ADTs -= a2;
        }
    }
    s.push(key_ADTs, ADTs);
}

void addCommonKeywordFields(Solver s){
    set[Define] definitions = s.getAllDefines();
    lrel[AType, KeywordFormal] commonKeywordFields = [];
    
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
    //println("commonKeywordFields");
    //for(<tp, dflt> <- commonKeywordFields) println("<tp>, <dflt>");
    s.push(key_common_keyword_fields, commonKeywordFields);
    
    // Warn for overlapping declarations of common keyword fields and ordinary fields
      
    map[AType, map[str, KeywordFormal]] adt_common_keyword_fields_name_and_kwf = ( adtType : ( "<kwf.name>" : kwf | kwf <- commonKeywordFields[adtType] ? []) | adtType <- domain(commonKeywordFields) );
    
    for(Define def <- definitions, def.idRole == constructorId()){
        try {
            consType = s.getType(def);
            set[str] commonFieldNames = domain(adt_common_keyword_fields_name_and_kwf[consType.adt] ? ());
            for(fld <- consType.fields){
               if(fld.alabel in commonFieldNames){
                    kwf = adt_common_keyword_fields_name_and_kwf[consType.adt][fld.alabel];
                    msgs = [ Message::warning("Common keyword field `<fld.alabel>` of data type `<consType.adt.adtName>` overlaps with field of constructor `<consType.alabel>`", getLoc(kwf)),
                             Message::warning ("Field `<fld.alabel>` of constructor `<consType.alabel>` overlaps with common keyword field of data type `<consType.adt.adtName>`", def.defined)
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
            if(consType.alabel == "type") continue; // TODO: where is the duplicate?
            conses_so_far = adt_constructors[consType.adt];
            //for(<AType c, Define cdef> <- conses_so_far, c.alabel == consType.alabel, cdef.defined != def.defined, comparable(c.fields, consType.fields)){
            //    msgs = [ Message::error("Duplicate/comparable constructor `<consType.alabel>` of data type `<consType.adt.adtName>`", def.defined),
            //             Message::error("Duplicate/comparable constructor `<consType.alabel>` of data type `<consType.adt.adtName>`", cdef.defined)
            //           ];
            //    s.addMessages(msgs);
            //}
            adt_constructors += <consType.adt, consType, def>;   
        } catch TypeUnavailable():
            ;//s.addMessages([ Message::error("Unavailable type in declaration of `<def.id>`", def.defined) ]);
        
    }
}

list[&T <: node ] unsetRec(list[&T <: node] args) = [unsetRec(a) | a <- args]; 

bool isManualLayout(AProduction p) = (p has attributes && atag("manual"()) in p.attributes);

TModel addGrammar(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,TModel] tmodels){
    try {
        rel[AType,AProduction] definedProductions = {};
        allStarts = {};
        tm = tmodels[qualifiedModuleName];
        for(m <- {qualifiedModuleName, *imports, *extends}, tmodels[m]?){
            facts = tmodels[m].facts;
            prodLocs1 = { k | loc k <- facts, aprod(_) := facts[k] };
            
            // filter out productions contained in priority/associativity declarations
            prodLocs2 = { k | k <- prodLocs1, !any(l <- prodLocs1, isStrictlyContainedIn(k, l)) };

            definedProductions += {<p.def, p> | loc k <- prodLocs2, aprod(p) := facts[k] };
            //definedProductions += {<p1.def, p1> | loc k <- prodLocs2, aprod(p) := facts[k], p1 := p[def=unset(p.def)] };/*syn*/
 
            allStarts += { t | loc k <- facts, \start(t) := facts[k] };
        }
        
        rel[AType,AProduction] allProductions = definedProductions;
        
        allLayouts = {};
        allManualLayouts = {};
        map[AType,AProduction] syntaxDefinitions = ();
        
        for(AType adtType <- domain(allProductions)){
            if(\start(adtType2) := adtType){
                adtType = adtType2;
            }
            productions = allProductions[adtType];
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
        
        tm = checkKeywords(allProductions, tm);
        
        // Check layout
    
        if(size(allLayouts) > 1) { // Warn for  multiple layout definitions
            allLayoutNames = {ladt.adtName | AType ladt <- allLayouts};
            for(AType ladt <- allLayouts){
                otherLayoutNames = {"`<lname>`" | str lname <- (allLayoutNames - ladt.adtName)};
                for(p <- syntaxDefinitions[ladt].alternatives){
                    tm.messages += [warning(interpolate("Multiple layout definitions: layout %q can interfere with layout %v", AType(Tree t) { return tm.facts[getLoc(t)]; }, [ladt.adtName, otherLayoutNames]),
                                            p.src)];
                }
            }
        }
        
        definedLayout = aadt("$default$", [], layoutSyntax());
       //syntaxDefinitions += (definedLayout : choice(definedLayout, {prod(definedLayout, [])}));
        if(isEmpty(allLayouts)){
        
            syntaxDefinitions += (AType::layouts("$default$"): choice(AType::layouts("$default$"), {prod(AType::layouts("$default$"), [])}));
        //    syntaxDefinitions += (definedLayout : choice(definedLayout, {prod(definedLayout, [])}));
        } else 
        if(size(allLayouts) >= 1){
            definedLayout = getOneFrom(allLayouts);
        } 
        
        //syntaxDefinitions += (definedLayout : choice(definedLayout, {prod(definedLayout, [])}));
        
        // Add start symbols
        
        for(AType adtType <- allStarts){
            syntaxDefinitions[\start(adtType)] = choice(\start(adtType), { prod(\start(adtType), [definedLayout, adtType[alabel="top"], definedLayout]) });
        }
        
        // Add auxiliary rules for instantiated syntactic ADTs outside the grammar rules
        facts = tm.facts;
        allADTs = { unset(adt, "alabel") | loc k <- facts, /AType adt:aadt(str _, list[AType] _, _) := facts[k] }; 
        
        instantiated_in_grammar = { unset(adt, "alabel") | /adt:aadt(str _, list[AType] parameters, SyntaxRole _) := syntaxDefinitions,
                                          !isEmpty(parameters), all(p <- parameters, !isTypeParameter(p)) 
                                  };
        
        instantiated = { unset(adt, "alabel") | AType adt <- allADTs, !isEmpty(adt.parameters), all(p <- adt.parameters, !isTypeParameter(p))  
                       };
        instantiated_outside = instantiated - instantiated_in_grammar;
        parameterized_uninstantiated_ADTs = { unset(adt, "alabel") | adt <- allADTs, adt.syntaxRole != dataSyntax(), params := getADTTypeParameters(adt), 
                                                  !isEmpty(params), all(p <- params, isTypeParameter(p)) 
                                          };
       
        AType uninstantiate(AType t){
            iparams = getADTTypeParameters(t);
            for(uadt <- parameterized_uninstantiated_ADTs){
                uadtParams = getADTTypeParameters(uadt);
                if(t.adtName == uadt.adtName && size(iparams) == size(uadtParams)){
                    return uadt;                  
                }
            }
            return t;
        }
       
        if(!isEmpty(instantiated_outside)){
            for(adt <- instantiated_outside, adt.syntaxRole != dataSyntax()){
                iparams = getADTTypeParameters(adt);
                uadt = uninstantiate(adt);
                auxNT = aadt("$<adt.adtName><for(p <- iparams){>_<p.adtName><}>", [], adt.syntaxRole);
                rule = choice(auxNT, {prod(auxNT, [adt]) });
                syntaxDefinitions += (auxNT : rule);
            }
        }
                                
        // Construct the grammar
        
        g = grammar(allStarts, syntaxDefinitions);
        g = layouts(g, definedLayout, allManualLayouts);
        g = expandKeywords(g);
        g.rules += (AType::aempty():choice(AType::aempty(), {prod(AType::aempty(),[])}));
        tm = tmlayouts(tm, definedLayout, allManualLayouts);
        tm.store[key_grammar] = [g];
        return tm;
    } catch TypeUnavailable(): {
        // protect against undefined entities in the grammar that have not yet been reported.
        return tmodels[qualifiedModuleName];
    }
}

@doc{intersperses layout symbols in all non-lexical productions}
public TModel tmlayouts(TModel t, AType l, set[AType] others) {
  
  res = top-down-break visit (t) {
    case AType atype => regulars(atype , l, others)
  }
  return res;
} 

// A keyword production may only contain:
// - literals or ciliterals
// - other nonterminals that satisfy this rule.

bool isValidKeywordProd(AType sym, set[AType] allLiteral){
    if(  alit(_) := sym 
      || acilit(_) := sym
      || aprod(prod(aadt(_,[],_),[alit(_)])) := sym 
      || aprod(prod(aadt(_,[],_),[acilit(_)])) := sym
      ){
        return true;
    }
    if(aprod(prod(a:aadt(_,[],_),_)) := sym && a in allLiteral){
       return true;
    }
    return false;
}

TModel checkKeywords(rel[AType, AProduction] allProductions, TModel tm){
    allLiteral = {};
    solve(allLiteral){
        forADT:
        for(AType adtType <- domain(allProductions), adtType notin allLiteral){
            for(prod(AType _, list[AType] asymbols) <- allProductions[adtType]){
                for(sym <- asymbols){
                    if(!isValidKeywordProd(sym, allLiteral)){
                       continue forADT;
                    }
                }
                allLiteral += adtType;
            }
        }
    }
    for(AType adtType <- domain(allProductions), ((\start(AType t) := adtType) ? t.syntaxRole : adtType.syntaxRole) == keywordSyntax()){
        for(p:prod(AType _, list[AType] asymbols) <- allProductions[adtType]){
            if(size(asymbols) != 1){
                tm.messages += [warning(size(asymbols) == 0 ? "One symbol needed in keyword declaration, found none" : "Keyword declaration should consist of one symbol", p.src)];
            }
            for(sym <- asymbols){
                if(alit(_) := sym || acilit(_) := sym) continue;
                if(!isADTType(sym) || isADTType(sym) && sym notin allLiteral){
                    tm.messages += [warning(interpolate("Only literals allowed in keyword declaration, found %t", AType(Tree t) { return tm.facts[getLoc(t)]; }, [sym]), p.src) ];
                }
            }
        }
    }
    return tm;
}