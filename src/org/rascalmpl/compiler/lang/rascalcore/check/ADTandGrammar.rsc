@bootstrapParser
module lang::rascalcore::check::ADTandGrammar
   
extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::BasicRascalConfig;

import lang::rascalcore::grammar::definition::Layout;
import lang::rascalcore::grammar::definition::Keywords;
//import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
  
import lang::rascal::\syntax::Rascal;

import IO;
import Node;
import Set;
import ListRelation;
import Message;

void addADTsAndCommonKeywordFields(Solver s){
    addADTs(s);
    addCommonKeywordFields(s);
}

void addADTs(Solver s){
    facts = s.getFacts();
    usedADTs = { t | loc k <- facts, /AType t:aadt(str name, list[AType] parameters, sr) := facts[k] };
    s.putStore("ADTs", usedADTs);
}

void addCommonKeywordFields(Solver s){
    set[Define] definitions = s.getAllDefinitions();
    commonKeywordFields = [];
    
    // Collect common keywords and check double declarations
  
    rel[AType,str,KeywordFormal] commonKeywordFieldNames = {};
    for(Define def <- definitions, def.idRole == dataId()){
        adtType = s.getType(def);
        commonKeywordNames = commonKeywordFieldNames[adtType]<0>;
        for(kwf <- def.defInfo.commonKeywordFields){
            fieldName = "<kwf.name>";
            commonKeywordFields += <adtType, kwf>;
            commonKeywordFieldNames += <adtType, fieldName, kwf>;
            if(fieldName in commonKeywordNames){
                msgs = [ Message::error("Double declaration of common keyword Field `<fieldName>` for data type `<adtType.adtName>`", getLoc(kwf2))
                       | kwf2 <- commonKeywordFieldNames[adtType]<1>, "<kwf2.name>" == fieldName
                       ];
                s.addMessages(msgs);
            }
        }
    }
    s.putStore("CommonKeywordFields", commonKeywordFields);
    
    // Warn for overlapping declarations of common keyword fields and ordinary fields
      
    adt_common_keyword_fields_name_and_kwf = ( adtType : ( "<kwf.name>" : kwf | kwf <- commonKeywordFields[adtType] ? []) | adtType <- domain(commonKeywordFields) );
    
    for(Define def <- definitions, def.idRole == constructorId()){
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
    }
    
    lrel[AType,AType,Define] adt_constructors = [];
    for(Define def <- definitions, def.idRole == constructorId()){
        consType = s.getType(def);
        if(consType.label == "type") continue; // TODO: where is the duplicate?
        conses_so_far = adt_constructors[consType.adt];
        for(<AType c, Define cdef> <- conses_so_far, c.label == consType.label, cdef.defined != def.defined, comparable(c.fields, consType.fields)){
            iprintln(definitions, lineLimit=10000);
            msgs = [ Message::error("Duplicate/comparable constructor `<consType.label>` of data type `<consType.adt.adtName>`", def.defined),
                     Message::error("Duplicate/comparable constructor `<consType.label>` of data type `<consType.adt.adtName>`", cdef.defined)
                   ];
            s.addMessages(msgs);
        }
        adt_constructors += <consType.adt, consType, def>;   
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