@bootstrapParser
module  lang::rascalcore::compile::Compile
 
import IO;
//import ValueIO;
import Message;
import String;
import ParseTree;
import util::Reflective;
import util::Benchmark;
import util::FileSystem;
import util::UUID;
import Map;
import Set;
import Relation;
import Exception;
import lang::rascalcore::compile::CompileTimeError;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::compile::Rascal2muRascal::RascalModule;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;


extend lang::rascalcore::check::TypePalConfig;
extend lang::rascalcore::check::Checker;

import lang::rascalcore::compile::muRascal2Java::CodeGen;

alias RVMModule = value;

public PathConfig getDefaultPathConfig() {
    return pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://typepal-examples/src|,
                |project://rascal-codegen-ideas/src|,
                |test-modules:///|
               ]
               );
}

loc generatedDir = |project://rascal-codegen-ideas/generated|;

list[Message] compile1(str qualifiedModuleName, loc moduleLoc,  lang::rascal::\syntax::Rascal::Module M, TModel tm, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose = true, bool optimize=true, bool enableAsserts=false){
    targetDir = generatedDir + module2package(qualifiedModuleName);
    className = module2class(qualifiedModuleName);
   
    errors = [ e | e:error(_,_) <- tm.messages];
    if(!isEmpty(errors)){
        return errors;
    }
    last_mod = lastModified(targetDir + "<className>.java");
    if(rel[str,datetime, PathRole] bom := tm.store[key_bom]){
        if(all(dt <- bom<1>, dt <= last_mod)){
            return <errors, tmodels>;
        }
    }
   	
   	try {
        //if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
     
       	muMod = r2mu(M, tm, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);

        <package, className, the_interface, the_class> = muRascal2Java(muMod, tmodels, moduleLocs);
       
        writeFile(targetDir + "<className>.java", the_class);
        writeFile(targetDir + "$<className>.java", the_interface);
     
      
        return true;
       
    } catch e: CompileTimeError(m): {
        errors += errors(m, moduleLoc);   
        return <errors, tmodels>;
    }
}

@doc{Compile a Rascal source module (given at a location) to Java}
list[Message] compile(loc moduleLoc, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose=false, bool optimize=true, bool enableAsserts=false) =
    compile(getModuleName(moduleLoc, pcfg), pcfg, reloc=reloc, verbose = verbose, optimize=optimize, enableAsserts=enableAsserts);


@doc{Compile a Rascal source module (given as qualifiedModuleName) to Java}
list[Message] compile(str qualifiedModuleName, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = true, bool optimize=true, bool enableAsserts=false){
    start_check = cpuTime();   
    <tmodels, moduleLocs, modules> = rascalTModelForName(qualifiedModuleName, pcfg, rascalTypePalConfig(classicReifier=true));
    check_time = (cpuTime() - start_check)/1000000;
    errors = [];
    start_comp = cpuTime();
    for(mname <- modules){
       errors += compile1(mname, moduleLocs[mname], modules[mname], tmodels[mname], pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    }
    
    comp_time = (cpuTime() - start_comp)/1000000;
    if(verbose) println("Compiling <qualifiedModuleName>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
	
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
