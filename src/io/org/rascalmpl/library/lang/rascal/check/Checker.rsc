@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascal::check::Checker

import IO;
import ValueIO;
import String;
import util::Benchmark;

import lang::rascal::\syntax::Rascal;

extend analysis::typepal::TypePal;
extend analysis::typepal::TestFramework;

extend lang::rascal::check::Declaration;
extend lang::rascal::check::Expression;
extend lang::rascal::check::Statement;
extend lang::rascal::check::Pattern;
extend lang::rascal::check::Operators;

extend lang::rascal::check::AType;
extend lang::rascal::check::ATypeUtils;

import Relation;
import util::Reflective;

start syntax Modules
    = Module+ modules;


Tree mkTree(int n) = [DecimalIntegerLiteral] "<for(int i <- [0 .. n]){>6<}>"; // Create a unique tree to identify predefined names
 
void rascalPreCollectInitialization(Tree tree, FRBuilder frb){
    //data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
    // | aadt(str adtName, list[AType] parameters, list[Keyword] common)
    // | acons(AType adt, str consName, list[NamedField] fields, list[Keyword] kwFields)
    // alias Keyword = tuple[AType fieldType, str fieldName, Expression defaultExp];
    //alias NamedField   = tuple[AType fieldType, str fieldName];
    
    typeType = aadt("Type", [aparameter("T", avalue())], []);
    SymbolType = aadt("Symbol", [], []);
    ProductionType = aadt("Production", [], []);
    frb.define("Type", dataId(), mkTree(1), defType(typeType));
    symbolField = <SymbolType, "symbol">;
    definitionsField = <amap(SymbolType, ProductionType), "definitions">;
    
    frb.define("type", constructorId(), mkTree(2), defType(acons(typeType, "type", [symbolField, definitionsField], [])));
}

// Enhance FRModel before validation
FRModel rascalPreValidation(FRModel m){
    // add transitive edges for extend
    extendPlus = {<from, to> | <Key from, extendPath(), Key to> <- m.paths}+;
    extended = domain(extendPlus);
    m.paths += { <from, extendPath(), to> | <Key from, Key to> <- extendPlus};
    m.paths += { <c, importPath(), a> | < Key c, importPath(), Key b> <- m.paths,  <b , extendPath(), Key a> <- m.paths};
    //println("rascalPreValidation");
    //iprintln(m.paths);
    return m;
}

// Enhance FRModel after validation
FRModel rascalPostValidation(FRModel m){
    // Check that all user defined types are defined with the same number of type parameters
    userDefs = {<userName, size(params), def> | <def, di> <- m.defines[_,_,{dataId(), aliasId()}], di has atype, (aadt(str userName, params, _) := di.atype || aalias(str userName, params, _) := di.atype)};
    for(userName <- userDefs<0>){
        nparams = userDefs[userName]<0>;
        if(size(nparams) != 1){
            for(def <- userDefs[userName,_]){
                m.messages += { error("Type <fmt(userName)> defined with <fmt(nparams)> type parameters", def) };
            }
        }
    }
    
   
    return m;
}

// ----  Examples & Tests --------------------------------

private start[Module] sampleModule(str name) = parse(#start[Module], |home:///git/TypePal/src/rascal/<name>.rsc|);
private start[Modules] sampleModules(str name) = parse(#start[Modules], |home:///git/TypePal/src/rascal/<name>.mrsc|);

public PathConfig getDefaultPathConfig() = pathConfig(   
        srcs = [|project://rascal/src/org/rascalmpl/library|,
                |project://TypePal/src|
               ]);
               
FRModel rascalModelFromStr(str text){
    startTime = cpuTime();
    pt = parse(#start[Modules], text).top;
    return rascalModel(pt, startTime, inline=true);
}

FRModel rascalModelFromLoc(loc src){
    startTime = cpuTime();
    pt = parse(#start[Modules], src).top;
    return rascalModel(pt, startTime, inline=true);
}

FRModel rascalModelFromName(str mname, bool debug=false){
    startTime = cpuTime();
    pcfg = getDefaultPathConfig();
    mloc = getModuleLocation(mname, pcfg);
    pt = parse(#start[Modules], mloc).top;
    m = rascalModel(pt, startTime, debug=debug);
    tplLoc = getDerivedWriteLoc(mname, "tpl", pcfg);
    m1 = m;
    m1.calculators = ();
    //m1.facts = ();
    m1.openFacts = {};
    m1. openReqs = {};
    m1.tvScopes = ();
    //m1.messages = {};
    m1.defines = {};
    m1.definitions = (k : m1.definitions[k]| k <- m1.definitions && DefInfo di := m1.definitions[k].defInfo && !(di has getAType || di has getATypes));
    //iprintln(m1);
    writeBinaryValueFile(tplLoc, m1);
    //iprintln(m);
    return m;
}

FRModel rascalModel(Tree pt, int startTime, bool debug=false, bool inline=false){
    afterParseTime = cpuTime();
    frb = newFRBuilder(pt);
    // When inline, all modules are in a single file; don't read imports from file
    if(!inline) frb.store("pathconfig", getDefaultPathConfig()); 
    rascalPreCollectInitialization(pt, frb);
    collect(pt, frb);
    frm = frb.build();
    afterExtractTime = cpuTime();   
    frm = resolvePath(frm, lookupFun=lookupWide);
    //iprintln(frm.paths);
    frm = rascalPreValidation(frm);
    frm = validate(frm, lookupFun=lookupWide, debug=debug);
    frm = rascalPostValidation(frm);
    afterValidateTime = cpuTime();
    if(!inline){
        println("parse:    <(afterParseTime - startTime)/1000000> ms
                'extract:  <(afterExtractTime - afterParseTime)/1000000> ms
                'validate: <(afterValidateTime - afterExtractTime)/1000000> ms
                'total:    <(afterValidateTime - startTime)/1000000> ms");
    }
    return frm;
}

set[Message] validateModules(str mname, bool debug=false) {
    return rascalModelFromName(mname, debug=debug).messages;
}

void testModules(str names...) {
    runTests([|project://TypePal/src/rascal/tests/<name>.ttl| | str name <- names], rascalModelFromStr);
}

value main(){
    return validateModules("rascal::Big");
}