@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::check::Checker

import IO;
import ValueIO;
import String;
import util::Benchmark;

import lang::rascal::\syntax::Rascal;

extend analysis::typepal::TypePal;
extend analysis::typepal::TestFramework;

extend lang::rascalcore::check::Declaration;
extend lang::rascalcore::check::Expression;
extend lang::rascalcore::check::Statement;
extend lang::rascalcore::check::Pattern;
extend lang::rascalcore::check::Operators;

import lang::rascalcore::check::Import;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;

extend lang::rascalcore::check::TypePalConfig;

extend lang::rascalcore::check::ADTSummary;
import lang::rascalcore::grammar::ParserGenerator;

import Set;
import Relation;
import util::Reflective;

start syntax Modules
    = Module+ modules;
    
str parserPackage = "org.rascalmpl.core.library.rascalcore.grammar.tests.generated_parsers";
//str parserPackage = "org.rascalmpl.core.java.parser.object";

Tree mkTree(int n) = [DecimalIntegerLiteral] "<for(int i <- [0 .. n]){>6<}>"; // Create a unique tree to identify predefined names
 
void rascalPreCollectInitialization(Tree tree, TBuilder tb){
    init_Import();
    
    tb.enterScope(tree);
        
        if(tb.getConfig().classicReifier){
            //data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
            typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
            SymbolType = aadt("Symbol", [], dataSyntax());
            ProductionType = aadt("Production", [], dataSyntax());
            symbolField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
            definitionsField = amap(SymbolType, ProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, ProductionType)>;
            tb.define("type", constructorId(), mkTree(2), defType(acons(typeType, /*"type",*/ [symbolField, definitionsField], [], label="type")));
            // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
        } else {
            //data type[&T] = type(AType symbol, map[AType,AProduction] definitions);
            typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
            SymbolType = aadt("AType", [], dataSyntax());
            AProductionType = aadt("AProduction", [], dataSyntax());
            atypeField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
            definitionsField = amap(SymbolType, AProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, AProductionType)>;
            tb.define("type", constructorId(), mkTree(2), defType(acons(typeType, /*"type",*/ [atypeField, definitionsField], [], label="type")));
            // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
        }
    tb.leaveScope(tree);
}

// Enhance TModel before validation
TModel rascalPreValidation(TModel m){
    // add transitive edges for extend
    extendPlus = {<from, to> | <Key from, extendPath(), Key to> <- m.paths}+;
    extended = domain(extendPlus);
    m.paths += { <from, extendPath(), to> | <Key from, Key to> <- extendPlus};
    m.paths += { <c, importPath(), a> | < Key c, importPath(), Key b> <- m.paths,  <b , extendPath(), Key a> <- m.paths};
    //println("rascalPreValidation");
    //iprintln(m.paths);
    return m;
}

// Enhance TModel after validation
TModel rascalPostValidation(TModel m){
    // Check that all user defined types are defined with the same number of type parameters
    userDefs = {<userName, size(params), def> | <def, di> <- m.defines[_,_,{dataId(), aliasId()}], di has atype, (aadt(str userName, params, _) := di.atype || aalias(str userName, params, _) := di.atype)};
    for(userName <- userDefs<0>){
        nparams = userDefs[userName]<0>;
        if(size(nparams) != 1){
            for(def <- userDefs[userName,_]){
                m.messages += [ error("Type <fmt(userName)> defined with <fmt(nparams)> type parameters", def) ];
            }
        }
    }
    return m;
}

// ----  Examples & Tests --------------------------------

public PathConfig getDefaultPathConfig() = pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://typepal-examples/src|
               ]);
               
TModel rascalTModelsFromStr(str text){
    startTime = cpuTime();
    pt = parse(#start[Modules], text).top;
    return rascalTModel(pt, startTime, inline=true);
}

TModel rascalTModelsFromTree(Tree pt){
    startTime = cpuTime();
    return rascalTModel(pt, startTime, inline=true);
}

TModel rascalTModelFromName(str mname, bool debug=false){
    startTime = cpuTime();
    pcfg = getDefaultPathConfig();
    
    /***** turn this off during development of type checker *****/
    
    //<valid, tm> = getIfValid(mname, pcfg);
    //if(valid) return tm;
    
    /***********************************************************/
     
    try {
        mloc = getModuleLocation(mname, pcfg);
        mloc.query = "ts=<lastModified(mloc)>";                         
        pt = parseModuleWithSpaces(mloc).top;
        tm = rascalTModel(pt, startTime, debug=debug);
        if(isEmpty(tm.messages)){
            <msgs, adtSummaries> = getADTSummaries(getLoc(pt), tm);
            tm.messages += msgs;
            g = getGrammar(adtSummaries);
            iprintln(g);
            pname = parserName(mname);
            <msgs, parserClass> = newGenerate(parserPackage, pname, g); 
            tm.messages += msgs;
            msgs = saveParser(pname, parserClass, |project://rascal-core/src/org/rascalmpl/core/library/rascalcore/grammar/tests/generated_parsers|);
            tm.messages += msgs;
        }
        saveModules(mname, pcfg, tm); 
        return tm;
    } catch ParseError(loc src): {
        return tmodel()[messages = [ error("Parse error", src)  ]];
    } catch Message msg: {
     return tmodel()[messages = [ error("During validation: <msg>", msg.src) ]];
    } catch value e: {
        return tmodel()[messages = [ error("During validation: <e>", |global-scope:///|) ]];
    }    
}

TModel rascalTModel(Tree pt, int startTime, bool debug=false, bool inline=false){
    afterParseTime = cpuTime();
    tb = newTBuilder(pt, config=rascalTypePalConfig(classicReifier=true));
    tb.push(patternContainer, "toplevel");
    // When inline, all modules are in a single file; don't read imports from file
    if(!inline) tb.push("pathconfig", getDefaultPathConfig()); 
    rascalPreCollectInitialization(pt, tb);
    collect(pt, tb);
   
    tm = tb.build();
    afterExtractTime = cpuTime();   
    tm = rascalPreValidation(tm);
    tm = validate(tm, debug=debug);
    tm = rascalPostValidation(tm);
    
    afterValidateTime = cpuTime();
    
    if(!inline){
        println("parse:    <(afterParseTime - startTime)/1000000> ms
                'extract:  <(afterExtractTime - afterParseTime)/1000000> ms
                'validate: <(afterValidateTime - afterExtractTime)/1000000> ms
                'total:    <(afterValidateTime - startTime)/1000000> ms");
    }
    return tm;
}

list[Message] validateModules(str mname, bool debug=false) {
    return rascalTModelFromName(mname, debug=debug).messages;
}

void testModules(str names...) {
    if(isEmpty(names)) names = allTests;
    runTests([|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/check/tests/<name>.ttl| | str name <- names], #Modules, rascalTModelsFromTree, verbose=true);
}

list[str] allTests = ["adt", "alias", "assignment", "datadecl", "exp", "fields", "fundecl", 
                     "imports", "operators", "pat", "scope", "stats", "syntax1", "syntax2"];