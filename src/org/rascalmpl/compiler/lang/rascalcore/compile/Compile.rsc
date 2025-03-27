@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module  lang::rascalcore::compile::Compile

import Message;
import String;
import util::Reflective;
import util::Benchmark;
import IO;
import Set;
import ValueIO;
import util::Monitor; 

import lang::rascal::\syntax::Rascal;
 
extend lang::rascalcore::check::Checker;
import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::compile::Rascal2muRascal::RascalModule;

import lang::rascalcore::compile::muRascal2Java::CodeGen;

import lang::rascalcore::compile::CompileTimeError;
import lang::rascalcore::compile::util::Names;


bool errorsPresent(TModel tmodel) = !isEmpty([ e | e:error(_,_) <- tmodel.messages ]);
bool errorsPresent(list[Message] msgs) = !isEmpty([ e | e:error(_,_) <- msgs ]);

data ModuleStatus;

list[Message] compile1(str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, map[str,TModel] transient_tms, ModuleStatus ms, RascalCompilerConfig compilerConfig){    
    pcfg = ms.pathConfig;
    //<found, tm, ms> = getTModelForModule(qualifiedModuleName, ms);
    tm = transient_tms[qualifiedModuleName];
    //iprintln(tm, lineLimit=10000);
    if(errorsPresent(tm)){
        return tm.messages;
    }
    
    className = asBaseClassName(qualifiedModuleName);
    interfaceName = asBaseInterfaceName(qualifiedModuleName);
    
    genSourcesDir = getGeneratedSrcsDir(qualifiedModuleName, pcfg);  
    
    
    interfaceFile =  genSourcesDir + "<interfaceName>.java";
    classFile = genSourcesDir + "<className>.java";
    genTestSourcesDir = getGeneratedTestSrcsDir(qualifiedModuleName, pcfg);  
    testClassFile = genTestSourcesDir + "<className>Tests.java";
    
    resourcesDir = getGeneratedResourcesDir(qualifiedModuleName, pcfg);
    constantsFile = resourcesDir + "<className>.constants";
    
    <tplFound, tplFile> = getTPLReadLoc(qualifiedModuleName, pcfg);
   
    if(tplFound && exists(classFile) && lastModified(classFile) > lastModified(tplFile)){
        println("Reusing compiled Java file for: <qualifiedModuleName>");
        return tm.messages;
    }
    
    <tm, muMod> = r2mu(M, tm, compilerConfig);
   
    if(errorsPresent(tm)){
        for(msg:error(_,_) <- tm.messages){
            println(msg);
        }
        return tm.messages;
    }
        
    imports = { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == qualifiedModuleName };
    extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == qualifiedModuleName };
    tmodels = ();
    for(m <- imports + extends, tpl_uptodate() in ms.status[m]){
        if(m in transient_tms){
            tmodels[m] = transient_tms[m];
        } else {
            <found, tpl, ms> = getTModelForModule(m, ms);
            tmodels[m] = tpl;
        }
    }
    tmodels[qualifiedModuleName] = tm;
        
    <the_interface, the_class, the_test_class, constants> = muRascal2Java(muMod, tmodels, ms.moduleLocs, pcfg);
     
    writeFile(interfaceFile, the_interface);
    writeFile(classFile, the_class);
    if(compilerConfig.logWrittenFiles) println("Written: <classFile>");
        
    if(!isEmpty(the_test_class)){
        writeFile(testClassFile, the_test_class);
        if(compilerConfig.logWrittenFiles) println("Written: <testClassFile>"); 
    }
      
    writeBinaryValueFile(constantsFile, <size(constants), md5Hash(constants), constants>);
    if(compilerConfig.logWrittenFiles) println("Written: <constantsFile>"); 
           
    return tm.messages;
}

list[Message] compile(list[loc] moduleLocs, RascalCompilerConfig compilerConfig) {
    return [info("not yet implemented", moduleLocs[0])];
}

@doc{Compile a Rascal source module (given at a location) to Java}
list[Message] compile(loc moduleLoc, RascalCompilerConfig compilerConfig) {

    pcfg = compilerConfig.typepalPathConfig;
    msgs = validatePathConfigForCompiler(pcfg, moduleLoc);
    if(!isEmpty(msgs)){
        return msgs;
    }
    moduleName = "**unknown**";
    try {
        moduleName = getRascalModuleName(moduleLoc, pcfg);
    } catch str e: {
        return [ error("Cannot find name for location, reason: <e>", moduleLoc) ];
    }
    return compile(moduleName, compilerConfig);
}

@doc{Compile a Rascal source module (given as qualifiedModuleName) to Java}
list[Message] compile(str qualifiedModuleName, RascalCompilerConfig compilerConfig){
    pcfg = compilerConfig.typepalPathConfig;
    msgs = validatePathConfigForCompiler(pcfg, |unknown:///|);
    if(!isEmpty(msgs)){
        return msgs;
    }

    if(compilerConfig.verbose) { println("Compiling .. <qualifiedModuleName>"); }
    
    start_comp = cpuTime();   
    ms = rascalTModelForNames([qualifiedModuleName], compilerConfig, compile1);
      
    comp_time = (cpuTime() - start_comp)/1000000;
    if(compilerConfig.verbose) { println("Compiled ... <qualifiedModuleName> in <comp_time> ms [total]"); }
	
    return toList(ms.messages[qualifiedModuleName] ? {});
}

void main(
    PathConfig pcfg = getProjectPathConfig(|cwd:///|), 
    list[loc] \modules = |unknown://|,
    bool logPathConfig            = false,
    bool logImports               = false,
    bool verbose                  = false,
    bool logWrittenFiles          = false,
    bool warnUnused               = true,
    bool warnUnusedFormals        = true,
    bool warnUnusedVariables      = true,
    bool warnUnusedPatternFormals = true,
    bool infoModuleChecked        = false
    ) {
    pcfg.resources = pcfg.bin;

    rascalConfig = rascalCompilerConfig(pcfg,
        logPathConfig            = logPathConfig,
        verbose                  = verbose,
        logWrittenFiles          = logWrittenFiles,
        warnUnused               = warnUnused,
        warnUnusedFormals        = warnUnusedFormals,
        warnUnusedVariables      = warnUnusedVariables,
        warnUnusedPatternFormals = warnUnusedPatternFormals,
        infoModuleChecked        = infoModuleChecked
    );
        
    messages = compile(\modules, rascalConfig);
    println(write(messages));

    return (error(_,_) <- messages || error(_) <- messages) ? 1 : 0;
}