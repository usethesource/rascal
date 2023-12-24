@bootstrapParser
module  lang::rascalcore::compile::Compile
 
import Message;
import Map;
import String;
import util::Reflective;
import util::Benchmark;
import IO;
import ValueIO;

import lang::rascal::\syntax::Rascal;
 
import lang::rascalcore::compile::Rascal2muRascal::RascalModule;
extend lang::rascalcore::check::Checker;
import lang::rascalcore::compile::muRascal2Java::CodeGen;

import lang::rascalcore::compile::CompileTimeError;
import lang::rascalcore::compile::util::Names;

bool errorsPresent(TModel tmodel) = !isEmpty([ e | e:error(_,_) <- tmodel.messages ]);
bool errorsPresent(list[Message] msgs) = !isEmpty([ e | e:error(_,_) <- msgs ]);

data ModuleStatus;
list[Message] compile1(str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, TModel tm, ModuleStatus ms, PathConfig pcfg, CompilerConfig compilerConfig){
    //iprintln(tm, lineLimit=10000);
   
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
    
   	//try {
        //if(verbose) 
        println("Compile: <qualifiedModuleName> (<size(ms.parseTrees)> cached parse trees, <size(ms.tmodels)> cached tmodels)");
       	<tm, muMod> = r2mu(M, tm, compilerConfig);
   
        if(errorsPresent(tm)){
            return tm.messages;
        }
        
        imports =  { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == qualifiedModuleName };
        extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == qualifiedModuleName };
        tmodels = ();
        for(m <- imports + extends, tpl_uptodate() in ms.status[m]){
            <found, tpl, ms> = getTmodelForModule(m, ms, pcfg);
            tmodels[m] = tpl;
        }
        tmodels[qualifiedModuleName] = tm;
        
        <the_interface, the_class, the_test_class, constants> = muRascal2Java(muMod, tmodels, ms.moduleLocs);
     
        writeFile(interfaceFile, the_interface);
        writeFile(classFile, the_class);
        println("Written: <classFile>");
        
        if(!isEmpty(the_test_class)){
            writeFile(testClassFile, the_test_class);
        }
        
        writeBinaryValueFile(constantsFile, <size(constants), md5Hash(constants), constants>);
        println("Written: <constantsFile>"); 
           
        return tm.messages;
       
    //} catch _: CompileTimeError(Message m): {
    //    return tm.messages + [m];   
    //}
}

@doc{Compile a Rascal source module (given at a location) to Java}
list[Message] compile(loc moduleLoc, PathConfig pcfg, CompilerConfig compilerConfig) =
    compile(getModuleName(moduleLoc, pcfg), pcfg, compilerConfig);

@doc{Compile a Rascal source module (given as qualifiedModuleName) to Java}
list[Message] compile(str qualifiedModuleName, PathConfig pcfg, CompilerConfig compilerConfig){
    start_comp = cpuTime();   
    ms = rascalTModelForNames([qualifiedModuleName], pcfg, rascalTypePalConfig(rascalPathConfig = pcfg), compilerConfig, compile1);
   
    comp_time = (cpuTime() - start_comp)/1000000;
    /*if(verbose)*/ println("Compiling <qualifiedModuleName>: <comp_time> ms");
	
    return ms.messages[qualifiedModuleName] ? [];
}