@bootstrapParser
module  lang::rascalcore::compile::Compile

import Message;
import String;
import util::Reflective;
import util::Benchmark;
import IO;
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

list[Message] compile1(str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, ModuleStatus ms, RascalCompilerConfig compilerConfig){    
    pcfg = ms.pathConfig;
    <found, tm, ms> = getTModelForModule(qualifiedModuleName, ms);
   
    if(errorsPresent(tm)){
        return tm.messages;
    }
    className = asBaseClassName(qualifiedModuleName);
    interfaceName = asBaseInterfaceName(qualifiedModuleName);
    genSourcesDir = getDerivedSrcsDir(qualifiedModuleName, pcfg);  
    interfaceFile =  genSourcesDir + "<interfaceName>.java";
    classFile = genSourcesDir + "<className>.java";
    testClassFile = genSourcesDir + "<className>Tests.java";
    
    resourcesDir = getDerivedResourcesDir(qualifiedModuleName, pcfg);
    constantsFile = resourcesDir + "<className>.constants";
    
    <tplFound, tplFile> = getTPLReadLoc(qualifiedModuleName, pcfg);
   
    if(tplFound && exists(classFile) && lastModified(classFile) > lastModified(tplFile)){
        println("Reusing compiled Java file for: <qualifiedModuleName>");
        return tm.messages;
    }
    
    //jobStep("RascalCompiler", "Compiling <qualifiedModuleName>");// TODO: monitor
    //if(compilerConfig.verbose) { println("Compiling .. <qualifiedModuleName>"); }
    
    <tm, muMod> = r2mu(M, tm, compilerConfig);
   
    if(errorsPresent(tm)){
        return tm.messages;
    }
        
    imports =  { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == qualifiedModuleName };
    extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == qualifiedModuleName };
    tmodels = ();
    for(m <- imports + extends, tpl_uptodate() in ms.status[m]){
        <found, tpl, ms> = getTModelForModule(m, ms);
        tmodels[m] = tpl;
    }
    tmodels[qualifiedModuleName] = tm;
        
    <the_interface, the_class, the_test_class, constants> = muRascal2Java(muMod, tmodels, ms.moduleLocs);
     
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

@doc{Compile a Rascal source module (given at a location) to Java}
list[Message] compile(loc moduleLoc, RascalCompilerConfig compilerConfig) {

    pcfg = compilerConfig.typepalPathConfig;
    msgs = validatePathConfigForCompiler(pcfg, moduleLoc);
    if(!isEmpty(msgs)){
        return msgs;
    }
    moduleName = "**unknown**";
    try {
        moduleName = getModuleName(moduleLoc, pcfg);
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
    
    jobStart("RascalCompiler");// TODO: monitor
    jobStep("RascalCompiler", "Compiling <qualifiedModuleName>");// TODO: monitor
    if(compilerConfig.verbose) { println("Compiling .. <qualifiedModuleName>"); }
    
    start_comp = cpuTime();   
    ms = rascalTModelForNames([qualifiedModuleName], compilerConfig, compile1);
    
    //iprintln(convertTModel2PhysicalLocs(ms.tmodels[qualifiedModuleName]), lineLimit=10000);
   
    comp_time = (cpuTime() - start_comp)/1000000;
   
    jobStep("RascalCompiler", "Compiled <qualifiedModuleName> in <comp_time> ms [total]");// TODO: monitor
    if(compilerConfig.verbose) { println("Compiled ... <qualifiedModuleName> in <comp_time> ms [total]"); }
    jobEnd("RascalCompiler");// TODO: monitor
	
    return ms.messages[qualifiedModuleName] ? [];
}