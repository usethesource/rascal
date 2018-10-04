@bootstrapParser
module  lang::rascalcore::compile::Compile
 
import IO;
import ValueIO;
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
//import experiments::Compiler::RVM::Interpreter::CompileTimeError;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::RVM::AST;

import lang::rascalcore::compile::Rascal2muRascal::RascalModule;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::muRascal2RVM::mu2rvm;

extend lang::rascalcore::check::Checker;
import lang::rascalcore::check::TypePalConfig;

import lang::rascalcore::compile::muRascal::interpret::Eval;

public PathConfig getDefaultPathConfig() {
    return pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://typepal-examples/src|
               ]
               );
}

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

tuple[bool,loc] RVMModuleReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvm", pcfg);

loc RVMModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvm", pcfg);

tuple[bool,loc] RVMExecutableReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "rvmx", pcfg); 

loc RVMExecutableWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "rvmx", pcfg);

tuple[bool,loc] MuModuleReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "mu", pcfg); 
loc MuModuleWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "mu", pcfg);

tuple[bool,loc] ConfigReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tc", pcfg);
loc ConfigWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "tc", pcfg);

tuple[bool,loc] getMergedImportsReadLoc(str mainQualifiedName, PathConfig pcfg){
    merged_imports_qname = mainQualifiedName + "_imports";
    return getDerivedReadLoc(merged_imports_qname, "rvm", pcfg);
}

loc getMergedImportsWriteLoc(str mainQualifiedName, PathConfig pcfg){
    merged_imports_qname = mainQualifiedName + "_imports";
    return getDerivedWriteLoc(merged_imports_qname, "rvm", pcfg);
}

loc sourceOrImportsLoc(str qualifiedModuleName, PathConfig pcfg){
    try {
        return getModuleLocation(qualifiedModuleName, pcfg);
    } catch ex: {
        if(<true, importsLoc> := cachedImportsReadLoc(qualifiedModuleName, pcfg)){
           return importsLoc;
        }
        throw "No source or imports loc found for <qualifiedModuleName>";
    }
}

bool validRVM(str qualifiedModuleName, PathConfig pcfg) {
	<existsRvmLoc, rvmLoc> = RVMModuleReadLoc(qualifiedModuleName, pcfg);
	//println("exists(<rvmLoc>): <exists(rvmLoc)>");
	//println("lastModified(<rvmLoc>) \>= lastModified(<src>): <lastModified(rvmLoc) >= lastModified(src)>");
	res = existsRvmLoc && lastModified(rvmLoc) >= lastModified(sourceOrImportsLoc(qualifiedModuleName, pcfg));
	//println("validRVM(<src>) =\> <res>");
	return res;
}

value /*tuple[TModel, RVMModule]*/ compile1(str qualifiedModuleName, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose = true, bool optimize=true, bool enableAsserts=false){

    <tmodels, moduleLocs, modules> = rascalTModelForName(qualifiedModuleName, pcfg, rascalTypePalConfig(classicReifier=true));
    
	//Configuration config;
	int check_time = 0;
    int comp_time = 0;
    loc moduleLoc = moduleLocs[qualifiedModuleName];
    
    lang::rascal::\syntax::Rascal::Module M;
    if(modules[qualifiedModuleName]?){
        M = modules[qualifiedModuleName];
    } else {
        M = parseModule(moduleLoc);
    }
    
    rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
 //   try {
 //       moduleLoc = getModuleLocation(qualifiedModuleName, pcfg);
 //   } catch e: {
 //       <existsConfig, configLoc> = ConfigReadLoc(qualifiedModuleName, pcfg);
 //       
 //       if (verbose) {
 //         println("No source module for <qualifiedModuleName>");
 //         println("\<existsConfig, configLoc\> = \<<existsConfig>, <configLoc>\>");
 //       }
 //        
 //       if(existsConfig){
 //           config = readBinaryValueFile(#Configuration, configLoc);
 //           //rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, config.pathConfiguration);
 //           rvmModuleLoc = configLoc[extension="rvm"];
 //           if(exists(rvmModuleLoc)){
 //              rvmMod = readBinaryValueFile(#RVMModule, rvmModuleLoc);
 //              if(verbose) println("No source found for <qualifiedModuleName>, reusing existing binary");
 //              return <config, rvmMod>;
 //           }
 //       }
 //       rvmMod = errorRVMModule(qualifiedModuleName, {error("Module not found: <qualifiedModuleName>", |unknown:///|)}, |unknown:///|);
 //       try {
 //           writeBinaryValueFile(rvmModuleLoc, rvmMod);
 //       } catch IO(str msg): {
 //           println("CANNOT WRITE ERROR MODULE FOR <qualifiedModuleName>: <msg>");
 //       }
 //       return <newConfiguration(pcfg), rvmMod>;
 //   }
 //  	//try {
 //  	    if(verbose) println("rascal2rvm: <moduleLoc>");
 //  	    start_checking = cpuTime();
 //  		//M = parse(#start[Module], moduleLoc).top;
 //  		M = parseModule(moduleLoc);
 //  	    config  = checkModule(M, newConfiguration(pcfg), verbose=verbose);
 //  	    if(reloc != |noreloc:///|){
 //  	        configWriteLoc = ConfigWriteLoc(qualifiedModuleName, pcfg);
 //  	        writeBinaryValueFile(configWriteLoc, relocConfig(config, reloc, pcfg.srcs));
 //  	    }
 //  	    check_time = (cpuTime() - start_checking)/1000000;
 //  	//} catch e: {
 //  	//    throw e;
 //  	//}
 //  	errors = [ e | e:error(_,_) <- config.messages];
 //  	warnings = [ w | w:warning(_,_) <- config.messages ];
    tm = tmodels[qualifiedModuleName];
    errors = [ e | e:error(_,_) <- tm.messages];
   	if(size(errors) > 0) {
   		rvmMod = errorRVMModule("<M.header.name>", toSet(tm.messages), moduleLoc);
   		try {
            writeBinaryValueFile(rvmModuleLoc, rvmMod);
        } catch IO(str msg): {
            println("CANNOT WRITE ERROR MODULE FOR <M.header.name>: <msg>");
        }
   		
   	    return <tm, rvmMod>;
   	}
   	
   	try {
        //if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
        start_comp = cpuTime();
       	muMod = r2mu(M, tm, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
       	iprintln(muMod);
       	eres = eval(muMod);
       	println("\n#### EVAL =\> <eval(muMod)>\n");
       	return eres.val;
       	throw "STOPPED after eval";
    
//        muMod = transform(muMod, pcfg);
        
        rvmMod = mu2rvm(muMod, verbose=verbose, optimize=optimize); 
        comp_time = (cpuTime() - start_comp)/1000000;
        if(verbose) println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
        //if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
        writeBinaryValueFile(rvmModuleLoc, rvmMod);
        return <tm, rvmMod>;  
    } catch e: CompileTimeError(m): {
      rvmMod = errorRVMModule("<M.header.name>", {m}, moduleLoc);
        try {
            writeBinaryValueFile(rvmModuleLoc, rvmMod);
        } catch IO(str msg): {
            println("CANNOT WRITE ERROR MODULE FOR <M.header.name>: <msg>");
        }      
        return <config, rvmMod>;
    }
}

//MuModule r2mu(lang::rascal::\syntax::Rascal::Module M, TModel config, PathConfig pcfg) { println("in r2mu"); }

@doc{Compile a Rascal source module (given at a location) to RVM}
RVMModule compile(loc moduleLoc, PathConfig pcfg, loc reloc = |noreloc:///|, bool verbose=false, bool optimize=true, bool enableAsserts=false) =
    compile(getModuleName(moduleLoc, pcfg), pcfg, reloc=reloc, verbose = verbose, optimize=optimize, enableAsserts=enableAsserts);


@doc{Compile a Rascal source module (given as qualifiedModuleName) to RVM}
RVMModule compile(str qualifiedModuleName, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
	<cfg, rvmMod> = compile1(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
	// TODO: JV worrying side-effect noticed here:
	rvmMod1 = recompileDependencies(qualifiedModuleName, rvmMod, cfg, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    return rvmMod;
}

list[RVMModule] compileAll(loc moduleRoot, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return compile([ getModuleName(moduleLoc, pcfg) | moduleLoc <- find(moduleRoot, "rsc") ], pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}

list[RVMModule] compile(list[loc] moduleLocs, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return compile([ getModuleName(moduleLoc, pcfg) | moduleLoc <- moduleLocs ], pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}
 
str escapeQualifiedName(str qualifiedModuleName){
    reserved = getRascalReservedIdentifiers();
    return intercalate("::", [nm in reserved ? "\\<nm>" : nm | nm <- split("::", qualifiedModuleName)]);
}

list[RVMModule] compile(list[str] qualifiedModuleNames, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    uniq = uuidi();
    containerName = "Container<uniq < 0 ? -uniq : uniq>";
    containerLocation = |test-modules:///<containerName>.rsc|;
    container = "module <containerName>
                '<for(str m <- qualifiedModuleNames){>
                'import <escapeQualifiedName(m)>;<}>";
    writeFile(containerLocation, container);
    pcfg.srcs = |test-modules:///| + pcfg.srcs;
    
    rvmContainer = compile(containerName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    set[Message] messages = {};
    compiledModules =
        for(str qualifiedModuleName <- qualifiedModuleNames){
            rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
            if(exists(rvmModuleLoc)){
                try {
                   rvmMod = readBinaryValueFile(#RVMModule, rvmModuleLoc);
                   append rvmMod;
                }  catch IO(str msg): {
                   messages += error("Cannot read RVM module for <qualifiedModuleName>: <msg>", rvmModuleLoc);
                }
             }
    }
  
    if(!isEmpty(messages) && !isEmpty(compiledModules)){
        compiledModules[0].messages += messages;
    }
    
    for(loc moduleLoc <- files(pcfg.bin), contains(moduleLoc.path, containerName)){
        try {
            remove(moduleLoc);
        } catch e: /* ignore failure to remove file */;
    }
    return compiledModules;
}

@deprecated
RVMModule compile(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return compile(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}

@deprecated
RVMModule compile(str qualifiedModuleName, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return compile(qualifiedModuleName, pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin), reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}
@deprecated
list[RVMModule] compile(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    PathConfig pcfg =  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin);// TODO: type was added for new (experimental) type checker
    return [ compile(qualifiedModuleName, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
}

@deprecated
list[RVMModule] compile(list[str] qualifiedModuleNames, list[loc] srcs, list[loc] libs, loc boot, loc bin, loc reloc, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    PathConfig pcfg =  pathConfig(srcs=srcs, libs=libs, boot=boot, bin=bin); // TODO: type was added for new (experimental) type checker
    return [ compile(qualifiedModuleName, pcfg, reloc=reloc, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts) | qualifiedModuleName <- qualifiedModuleNames ];
}

RVMModule recompileDependencies(str qualifiedModuleName, RVMModule rvmMod, TModel cfg, PathConfig pcfg, bool verbose = false, bool optimize=true, bool enableAsserts=false){
    return rvmMod; // TODO
    errors = [ e | e:error(_,_) <- cfg.messages];
    warnings = [ w | w:warning(_,_) <- cfg.messages ];
   
    if(size(errors) > 0) {
        return rvmMod;
    }
    messages = {};
    
    dirtyModules = { prettyPrintName(dirty) | dirty <- cfg.dirtyModules };
   
    if(verbose){
       println("dirtyModules:");
       for(m1 <- dirtyModules) println("\t<m1>");
       
       println("importGraph:");
       for(<m1, m2> <- cfg.importGraph){
           println("\t<prettyPrintName(m1)> imports <prettyPrintName(m2)>");
       }
    }
        
    allDependencies = { prettyPrintName(rname) | rname <- carrier(cfg.importGraph) } - qualifiedModuleName;
    
    bool atLeastOneRecompiled = false;
    for(dependency <- allDependencies){
        if(dependency in dirtyModules || !validRVM(dependency, pcfg)){
           <cfg1, rvmMod1> = compile1(dependency, pcfg, optimize=optimize, enableAsserts=enableAsserts);
           atLeastOneRecompiled = true;
           messages += cfg1.messages;
        }
    }
    
    clearDirtyModules(qualifiedModuleName, pcfg);
    
    errors = [ e | e:error(_,_) <- messages];
    warnings = [ w | w:warning(_,_) <- messages ];
    
    if(size(errors) > 0) {
        return errorRVMModule(rvmMod.name, messages, getModuleLocation(qualifiedModuleName, pcfg));
    }
    if(atLeastOneRecompiled){
       mergedLoc = getMergedImportsWriteLoc(qualifiedModuleName, pcfg);
       try {
           if(verbose) println("Removing <mergedLoc>");
           remove(mergedLoc);
       } catch e: {
           println("Could not remove <mergedLoc>: <e>");
        }
    }
   
    return rvmMod ;
}

// Assumption: main declaration is the last one
lang::rascal::\syntax::Rascal::Declaration getMain(lang::rascal::\syntax::Rascal::Module m){
    if(m2: (Module) `<Header h> <Toplevel* pre> <Toplevel tl_main>` := m){
       return tl_main.declaration;
    }
    throw InternalCompilerError(error("getMain: cannot match toplevels", m@\loc));
}

Module removeMain(lang::rascal::\syntax::Rascal::Module m) {
    if(m2: (Module) `<Header h> <Toplevel* pre> <Toplevel mn>` := m){
       return (Module) `<Header h> <Toplevel* pre>`;
    }
    throw InternalCompilerError(error("removeMain: no main found", m@\loc));
}

//TModel noPreviousConfig = newConfiguration(pathConfig());
//TModel previousConfig = noPreviousConfig;

tuple[TModel, RVMModule] compile1Incremental(str qualifiedModuleName, bool reuseConfig, PathConfig pcfg, bool verbose = false, bool optimize=true, bool enableAsserts=false){

    Configuration config;
    lang::rascal::\syntax::Rascal::Module M;
    int check_time;
    int comp_time;
    
    moduleLoc = getModuleLocation(qualifiedModuleName, pcfg);
   
    if(verbose) println("rascal2rvm: Parsing and incremental checking <moduleLoc>");
    start_checking = cpuTime();
    
    M = parseModule(moduleLoc);
    // TODO: the rewritten module -- after calling removeMain -- will contain
    // locations from Compile.rsc; Figure out whether that is ok or not.
    //println("Original module at <moduleLoc>");
    //iprintln(M);
    
    if(!reuseConfig || previousConfig == noPreviousConfig){
        lang::rascal::\syntax::Rascal::Module M1 = removeMain(M);
        previousConfig = checkModule(M1, moduleLoc, newConfiguration(pcfg), verbose=verbose);
        previousConfig.stack = [0]; // make sure we are in the module scope
    } else {
      previousConfig.dirtyModules = {};
    }
    mainDecl = getMain(M);
    config  = checkDeclaration(mainDecl, true, previousConfig);
   
    check_time = (cpuTime() - start_checking)/1000000;
    
    errors = [ e | e:error(_,_) <- config.messages];
    warnings = [ w | w:warning(_,_) <- config.messages ];
   
    if(size(errors) > 0) {
        rvmMod = errorRVMModule("<M.header.name>", config.messages, moduleLoc);
        return <config, rvmMod>;
    }
    
    rvmModuleLoc = RVMModuleWriteLoc(qualifiedModuleName, pcfg);
    try {
        if(verbose) println("rascal2rvm: Compiling <moduleLoc>");
        start_comp = cpuTime();
        muMod = r2mu(M, config, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);  // never a reloc
        
 //       muMod = transform(muMod, pcfg);
        rvmMod = mu2rvm(muMod, verbose=verbose, optimize=optimize); 
        comp_time = (cpuTime() - start_comp)/1000000;
        if(verbose) println("Compiling <moduleLoc>: check: <check_time>, compile: <comp_time>, total: <check_time+comp_time> ms");
        if(verbose) println("compile: Writing RVMModule <rvmModuleLoc>");
        writeBinaryValueFile(rvmModuleLoc, rvmMod);
       
        return <config, rvmMod>;  
    } catch e: CompileTimeError(m): {
      rvmMod = errorRVMModule("<M.header.name>", {m}, moduleLoc);
        try {
            writeBinaryValueFile(rvmModuleLoc, rvmMod);
        } catch IO(str msg): {
            println("CANNOT WRITE ERROR MODULE FOR <M.header.name>: <msg>");
        }      
        return <config, rvmMod>;
    }
}  

RVMModule compileIncremental(str qualifiedModuleName, bool reuseConfig, PathConfig pcfg, bool verbose = false, bool optimize = true, bool enableAsserts=false){
    <cfg, rvmMod> = compile1Incremental(qualifiedModuleName, reuseConfig, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
    return recompileDependencies(qualifiedModuleName, rvmMod, cfg, pcfg, verbose=verbose, optimize=optimize, enableAsserts=enableAsserts);
}

set[str] getIncrementalVocabulary(){
    if(previousConfig == noPreviousConfig){
        return {};
    }
    return {name | /RSimpleName(str name) := previousConfig};
}  
