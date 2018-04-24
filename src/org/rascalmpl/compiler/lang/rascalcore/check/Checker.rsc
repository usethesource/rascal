@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::check::Checker
              
/*
 * TODO:
 * - Integrate with parser generator
 * - Fix reference in grammar rules (it seems that production label get lost somewhere and reference cannot be found)
 * - Support for reified types
 *
 * Potential additions:
 * - Unused imports/extends
 * - Unused private functions
 * - Non-void functions have a return along every control path
 * - Unreachable code
 */
 
import IO;
import ValueIO;
import String;
import util::Benchmark;

import lang::rascal::\syntax::Rascal;
   
extend analysis::typepal::TypePal;
//extend analysis::typepal::TestFramework;

//import analysis::typepal::Utils;

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
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Literals;


import Set;
import Relation;
import util::Reflective;
import util::FileSystem;

start syntax Modules
    = Module+ modules;
    
//str parserPackage = "org.rascalmpl.core.library.lang.rascalcore.grammar.tests.generated_parsers";
//str parserPackage = "org.rascalmpl.core.java.parser.object";

Tree mkTree(int n) = [DecimalIntegerLiteral] "<for(int i <- [0 .. n]){>6<}>"; // Create a unique tree to identify predefined names
 
void rascalPreCollectInitialization(Tree tree, Collector c){
    init_Import();
    c.enterScope(tree);
        
        if(c.getConfig().classicReifier){
            //data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
            typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
            SymbolType = aadt("Symbol", [], dataSyntax());
            ProductionType = aadt("Production", [], dataSyntax());
            symbolField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
            definitionsField = amap(SymbolType, ProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, ProductionType)>;
            c.define("type", constructorId(), mkTree(2), defType(acons(typeType, /*"type",*/ [symbolField, definitionsField], [], label="type")));
            // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
        } else {
            //data type[&T] = type(AType symbol, map[AType,AProduction] definitions);
            typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
            SymbolType = aadt("AType", [], dataSyntax());
            AProductionType = aadt("AProduction", [], dataSyntax());
            atypeField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
            definitionsField = amap(SymbolType, AProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, AProductionType)>;
            c.define("type", constructorId(), mkTree(2), defType(acons(typeType, /*"type",*/ [atypeField, definitionsField], [], label="type")));
            // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
        }
    c.leaveScope(tree);
}

// Enhance TModel before running Solver
TModel rascalPreSolver(Tree pt, TModel m){
    // add transitive edges for extend
    extendPlus = {<from, to> | <loc from, extendPath(), loc to> <- m.paths}+;
    extended = domain(extendPlus);
    m.paths += { <from, extendPath(), to> | <loc from, loc to> <- extendPlus};
    m.paths += { <c, importPath(), a> | < loc c, importPath(), loc b> <- m.paths,  <b , extendPath(), loc a> <- m.paths};
    //println("rascalPreValidation");
    //iprintln(m.paths);
    return m;
}

void rascalPostSolver(Tree pt, Solver s){
    //messages = s.getTModel().messages;
   // if(isEmpty(messages)){
             ;//<tm, adtSummaries> = getADTSummaries(getLoc(pt), tm, s);
             //g = getGrammar(getLoc(pt), s);
            
 //           pname = "DefaultParser";
 //           if(Module m := pt) { 
 //               mname = "<m.header.name>";
 //               pname = parserName(mname);
 //           }
 //
 //           <msgs, parserClass> = newGenerate(parserPackage, pname, g); 
 //           tm.messages += msgs;
 //           msgs = saveParser(pname, parserClass, |project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/grammar/tests/generated_parsers|);
 //           tm.messages += msgs;
  // }

}

// ----  Examples & Tests --------------------------------

public PathConfig getDefaultPathConfig() = pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://typepal-examples/src|
               ]
               );

data ProfileData = profile(loc file = |unknown:///|, int parser = 0, int collector = 0, int solver = 0, int save = 0);
 
void report(ProfileData pd){
    text = "<pd.parser? ? "parser: <pd.parser> ms;" : ""> <pd.collector? ? "collector: <pd.collector> ms;" : ""> <pd.solver? ? "solver: <pd.solver> ms;" : ""> <pd.save? ? "save: <pd.save> ms;" : ""> total: <pd.parser + pd.collector + pd.solver + pd.save> ms";
    if(pd.file != |unknown:///|) text += " (<pd.file>)";
    println(text);
}
                              
TModel rascalTModelsFromStr(str text){
    startTime = cpuTime();
    pt = parse(#start[Modules], text).top;
    return rascalTModel(pt, inline=true)[1];
}

TModel rascalTModelsFromTree(Tree pt){
    startTime = cpuTime();
    return rascalTModel(pt, inline=true)[1];
}

TModel rascalTModelFromName(str mname, PathConfig pcfg, bool debug=true){
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    try {
        mloc = getModuleLocation(mname, pcfg);
        return rascalTModelFromLoc(mloc, pcfg, debug=debug)[1];
    } catch value e: {
        return tmodel()[messages = [ error("During validation: <e>", mloc) ]];
    }
}

tuple[ProfileData, TModel] rascalTModelFromLoc(loc mloc, PathConfig pcfg, bool debug=false){     
    try {
        startTime = cpuTime();   
        mname = getModuleName(mloc, pcfg);
        
        /***** turn this off during development of type checker *****/
        //<valid, tm> = getIfValid(mname, pcfg);
        //if(valid) {
        //    println("*** reusing up-to-date TModel of <mname>");
        //    vtime = (cpuTime() - startTime)/1000000;
        //    prof = profile(file=mloc,solver=vtime);
        //    return <prof, tm>;
        //}
        /***********************************************************/
        
       // mloc = timestamp(mloc);     
                       
        pt = parseModuleWithSpaces(mloc).top;
        pTime = (cpuTime() - startTime)/1000000;
        <prof, tm> = rascalTModel(pt, pcfg = pcfg, debug=debug);
        prof.file = mloc;
        prof.parser = pTime;
        startTime = cpuTime(); 
        saveModules(mname, pcfg, tm); 
        prof.save = (cpuTime() - startTime)/1000000;
        return <prof, tm>;
    } catch ParseError(loc src): {
        return <profile(), tmodel()[messages = [ error("Parse error", src)  ]]>;
    } catch Message msg: {
     return <profile(), tmodel()[messages = [ error("During validation: <msg>", msg.at) ]]>;
    } catch value e: {
        return <profile(), tmodel()[messages = [ error("During validation: <e>", mloc) ]]>;
    }    
}

tuple[ProfileData, TModel] rascalTModel(Tree pt, PathConfig pcfg = getDefaultPathConfig(), bool debug=false, bool inline=false){
    startTime = cpuTime();
    c = newCollector(pt, config=rascalTypePalConfig(classicReifier=true), debug=debug);
    c.push(patternContainer, "toplevel");
    // When inline, all modules are in a single file; don't read imports from file
    if(!inline) c.push("pathconfig", pcfg); 
    rascalPreCollectInitialization(pt, c);
    collect(pt, c);
 
    tm = c.run();
    afterExtractTime = cpuTime();   
    tm = rascalPreSolver(pt, tm);
    s = newSolver(pt, tm, debug=debug);
    tm = s.run();
 //   tm = rascalPostValidation(tm);
    
    afterValidateTime = cpuTime();
    
    ProfileData prof = profile(file=getLoc(pt));
    
    if(!inline){
        prof.collector = (afterExtractTime - startTime)/1000000;
        prof.solver = (afterValidateTime - afterExtractTime)/1000000;
    }
   
    return <prof, tm>;
}

// name of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

ModuleMessages check(str mname, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <mname>"); iprintln(pcfg1);
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    try {
        tm = rascalTModelFromName(mname, pcfg);
        mloc = getModuleLocation(mname, pcfg);
        return program(mloc, toSet(tm.messages));
    } catch value e: {
        return program(mloc, {error("During validation: <e>", mloc)});
    }
}

ModuleMessages check(loc file, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <file>"); iprintln(pcfg1);
    <prof, tm> = rascalTModelFromLoc(file, pcfg);
    report(prof);
    return program(file, toSet(tm.messages));
}

list[ModuleMessages] check(list[str] mnames, PathConfig pcfg){
    return [ check(mname, pcfg) | mname <- mnames ];
}

list[ModuleMessages] check(list[loc] files, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <files>"); iprintln(pcfg1);
    p = e = v = s = 0;
    mms = [];
    pds = [];
    for(file <- files){
        <pd, tm> = rascalTModelFromLoc(file, pcfg);
        report(pd);
        pds += pd;
        mms += program(file, toSet(tm.messages));
        p += pd.parser; e += pd.collector; v += pd.solver; s += pd.save;
    }
    println("SUMMARY");
    for(pd <- pds) report(pd);
    println("TOTAL");
    report(profile(parser=p, collector=e, solver=v,save=s));
    return mms;
}

list[ModuleMessages] checkAll(loc root, PathConfig pcfg){
     return [ check(moduleLoc, pcfg) | moduleLoc <- find(root, "rsc") ]; 
}

list[Message] validateModules(str mname, bool debug=false) {
    return rascalTModelFromName(mname, getDefaultPathConfig(), debug=debug).messages;
}

void testModules(str names...) {
    if(isEmpty(names)) names = allTests;
    runTests([|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/check/tests/<name>.ttl| | str name <- names], #Modules, rascalTModelsFromTree, verbose=false);
}

list[str] allTests = ["adt", "adtparam", "alias", "assignment", "datadecl", "exp", "fields", "fundecl", 
                     "imports", "operators", "pat", "scope", "splicepats", "stats"/*,"syntax1", "syntax2", "syntax3"*/];