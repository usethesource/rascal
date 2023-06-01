@bootstrapParser
module  lang::rascalcore::compile::Compile
 
import Message;
import String;
import util::Reflective;
import util::Benchmark;
import IO;
import ValueIO;

import lang::rascal::\syntax::Rascal;
 
import lang::rascalcore::compile::Rascal2muRascal::RascalModule;
//import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
extend lang::rascalcore::check::Checker;
import lang::rascalcore::compile::muRascal2Java::CodeGen;
//import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::compile::CompileTimeError;
import lang::rascalcore::compile::util::Names;
//import lang::rascalcore::compile::util::ConcreteSyntax;

list[Message] compile1(str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, map[str,TModel] tmodels, map[str, loc] moduleLocs, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose = true, bool optimize=true, bool enableAsserts=true){
    tm = tmodels[qualifiedModuleName];
    //iprintln(tm, lineLimit=10);
    
    bool errorsPresent(TModel tmodel) = !isEmpty([ e | e:error(_,_) <- tmodel.messages ]);
   
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
        println("Reusing: <qualifiedModuleName>");
        return tm.messages;
    }
    
   	try {
        //if(verbose) 
        println("Compile: <qualifiedModuleName>");
       	<tm, muMod> = r2mu(M, tm, /*reloc=reloc,*/ verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
   
        if(errorsPresent(tm)){
            return tm.messages;
        }
        tmodels[qualifiedModuleName] = tm;
        
        <the_interface, the_class, the_test_class, constants> = muRascal2Java(muMod, tmodels, moduleLocs);
     
        writeFile(interfaceFile, the_interface);
        writeFile(classFile, the_class);
        println("Written: <classFile>");
        
        if(!isEmpty(the_test_class)){
            writeFile(testClassFile, the_test_class);
        }
        
        writeBinaryValueFile(constantsFile, constants);
        println("Written: <constantsFile>"); 
           
        return tm.messages;
       
    } catch _: CompileTimeError(Message m): {
        return errors + [m];   
    }
}

@doc{Compile a Rascal source module (given at a location) to Java}
list[Message] compile(loc moduleLoc, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose=true, bool optimize=true, bool enableAsserts=false) =
    compile(getModuleName(moduleLoc, pcfg), pcfg, reloc=reloc, verbose = verbose, optimize=optimize, enableAsserts=enableAsserts);

@doc{Compile a Rascal source module (given as qualifiedModuleName) to Java}
list[Message] compile(str qualifiedModuleName, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=true){
    start_check = cpuTime();   
    <tmodels, moduleLocs, modules> =  rascalTModelForNames([qualifiedModuleName], pcfg, rascalTypePalConfig()/*[logSolverSteps=true]*/);
       
    //iprintln(tmodels[qualifiedModuleName], lineLimit=10000);
    //return tmodels[qualifiedModuleName].messages;
    check_time = (cpuTime() - start_check)/1000000;
    errors = [];
    start_comp = cpuTime();
    for(mname <- modules){
       errors += compile1(mname, modules[mname], tmodels, moduleLocs, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    }
    
    comp_time = (cpuTime() - start_comp)/1000000;
    /*if(verbose)*/ println("Compiling <qualifiedModuleName>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
	
    return errors;
}
//
//list[RVMModule] compileAll(loc moduleRoot, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    return compile([ getModuleName(moduleLoc, pcfg) | moduleLoc <- find(moduleRoot, "rsc") ], pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
//}
//
//list[RVMModule] compile(list[loc] moduleLocs, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    return compile([ getModuleName(moduleLoc, pcfg) | moduleLoc <- moduleLocs ], pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
//}
// 
//str escapeQualifiedName(str qualifiedModuleName){
//    reserved = getRascalReservedIdentifiers();
//    return intercalate("::", [nm in reserved ? "\\<nm>" : nm | nm <- split("::", qualifiedModuleName)]);
//}
//
//list[RVMModule] compile(list[str] qualifiedModuleNames, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    uniq = uuidi();
//    containerName = "Container<uniq < 0 ? -uniq : uniq>";
//    containerLocation = |test-modules:///<containerName>.rsc|;
//    container = "module <containerName>
//                '<for(str m <- qualifiedModuleNames){>
//                'import <escapeQualifiedName(m)>;<}>";
//    writeFile(containerLocation, container);
//    pcfg.srcs = |test-modules:///| + pcfg.srcs;
//    
//    rvmContainer = compile(containerName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
//    set[Message] messages = {};
//    compiledModules =
//        for(str qualifiedModuleName <- qualifiedModuleNames){
//            rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
//            if(exists(rvmModuleLoc)){
//                try {
//                   rvmMod = readBinaryValueFile(#RVMModule, rvmModuleLoc);
//                   append rvmMod;
//                }  catch IO(str msg): {
//                   messages += error("Cannot read RVM module for <qualifiedModuleName>: <msg>", rvmModuleLoc);
//                }
//             }
//    }
//  
//    if(!isEmpty(messages) && !isEmpty(compiledModules)){
//        compiledModules[0].messages += messages;
//    }
//    
//    for(loc moduleLoc <- files(pcfg.bin), contains(moduleLoc.path, containerName)){
//        try {
//            remove(moduleLoc);
//        } catch e: /* ignore failure to remove file */;
//    }
//    return compiledModules;
//}
//
//@deprecated
//RVMModule compile(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    return compile(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
//}
//
//@deprecated
//RVMModule compile(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    return compile(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
//}
//@deprecated
//list[RVMModule] compile(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    PathConfig pcfg =  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);// TODO: type was added for new (experimental) type checker
//    return [ compile(qualifiedModuleName, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
//}
//
//@deprecated
//list[RVMModule] compile(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    PathConfig pcfg =  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin); // TODO: type was added for new (experimental) type checker
//    return [ compile(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
//}
//
//RVMModule recompileDependencies(str qualifiedModuleName, RVMModule rvmMod, TModel cfg, PathConfig pcfg, bool verbose = false, bool optimize=true, bool enableAsserts=false){
//    return rvmMod; // TODO
//    errors = [ e | e:error(_,_) <- cfg.messages];
//    warnings = [ w | w:warning(_,_) <- cfg.messages ];
//   
//    if(size(errors) > 0) {
//        return rvmMod;
//    }
//    messages = {};
//    
//    dirtyModules = { prettyPrintName(dirty) | dirty <- cfg.dirtyModules };
//   
//    if(verbose){
//       println("dirtyModules:");
//       for(m1 <- dirtyModules) println("\t<m1>");
//       
//       println("importGraph:");
//       for(<m1, m2> <- cfg.importGraph){
//           println("\t<prettyPrintName(m1)> imports <prettyPrintName(m2)>");
//       }
//    }
//        
//    allDependencies = { prettyPrintName(rname) | rname <- carrier(cfg.importGraph) } - qualifiedModuleName;
//    
//    bool atLeastOneRecompiled = false;
//    for(dependency <- allDependencies){
//        if(dependency in dirtyModules || !validRVM(dependency, pcfg)){
//           <cfg1, rvmMod1> = compile1(dependency, pcfg, optimize=optimize, enableAsserts=enableAsserts);
//           atLeastOneRecompiled = true;
//           messages += cfg1.messages;
//        }
//    }
//    
//    clearDirtyModules(qualifiedModuleName, pcfg);
//    
//    errors = [ e | e:error(_,_) <- messages];
//    warnings = [ w | w:warning(_,_) <- messages ];
//    
//    if(size(errors) > 0) {
//        return errorRVMModule(rvmMod.name, messages, getModuleLocation(qualifiedModuleName, pcfg));
//    }
//    if(atLeastOneRecompiled){
//       mergedLoc = getMergedImportsWriteLoc(qualifiedModuleName, pcfg);
//       try {
//           if(verbose) println("Removing <mergedLoc>");
//           remove(mergedLoc);
//       } catch e: {
//           println("Could not remove <mergedLoc>: <e>");
//        }
//    }
//   
//    return rvmMod ;
//}
