@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalModule

import IO;
import Map;
import String;
import Set;
import List;
import Relation;
import util::Reflective;
//import util::ValueUI;

import ParseTree;
import lang::rascalcore::compile::RVM::Interpreter::CompileTimeError;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal2RVM::Relocate;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::CheckerConfig;

import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;

import lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration;
import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;

/*
 * Translate a Rascal module to muRascal.
 * The essential function is:
 * - r2mu		translate Rascal module to muRascal
 */

/********************************************************************/
/*                  Translate one module                            */
/********************************************************************/

@doc{Compile a parsed Rascal source module to muRascal}
MuModule r2mu(lang::rascal::\syntax::Rascal::Module M, Configuration config, PathConfig pcfg, loc reloc=|noreloc:///|, bool verbose = true, bool optimize = true, bool enableAsserts=false){
   try {
      resetModuleInfo(optimize, enableAsserts);
      module_name = "<M.header.name>";
      setModuleName(module_name);
      mtags = translateTags(M.header.tags);
      setModuleTags(mtags);
      if(ignoreTest(mtags)){
            return errorMuModule(getModuleName(), {info("Ignore tag suppressed compilation", M@\loc)}, M@\loc);
      }
     
      if(verbose) println("r2mu: entering ... <module_name>, enableAsserts: <enableAsserts>");
   	  
   	  // Extract scoping information available from the configuration returned by the type checker  
   	  extractScopes(config); 
   	  
   	  // Extract all declarations for the benefit of the type reifier
      extractDeclarationInfo(config);
   	 
   	  map[str,Symbol] types = 
   	  	( uid2str[uid] : \type | 
   	  	  int uid <- config.store, 
   	  	  ( AbstractValue::constructor(RName name, Symbol \type, KeywordParamMap keywordParams, int containedIn, _, loc at) := config.store[uid]
   	  	  || AbstractValue::production(RName name, Symbol \type, int containedIn, _, Production p, loc at) := config.store[uid] 
   	  	  ),
   	  	  //bprintln(config.store[uid]), bprintln(config.store[containedIn].at.path), bprintln(at.path),
   	  	  !isEmpty(getSimpleName(name)),
   	  	  containedIn == 0, 
   	  	  ( config.store[containedIn].at.path == at.path // needed due to the handling of 'extend' by the type checker
   	  	  || at.path  == "/ConsoleInput.rsc"             // TODO: hack to get the RascalShell working, since config.store[0].at.path
   	  	                                                 // "/experiments/Compiler/Compile.rc"???
   	  	  )
   	  	);
   	  //for(tname <- types) println("<tname>: <types[tname]>");	
   	  
   	  // Generate companion functions for 
   	  // (1) keyword fields in constructors
   	  // (2) common keyword fields in data declarations
      generateCompanions(M, config, verbose=verbose);
   	 
   	  translateModule(M);
   	 
   	  modName = replaceAll("<M.header.name>","\\","");
   	  
   	  //println("overloadedFunctions"); for(tp <- getOverloadedFunctions()) println(tp);
   	  // Overloading resolution...	  
   	  lrel[str name, Symbol funType, str scopeIn, list[str] ofunctions, list[str] oconstructors] overloaded_functions = 
   	  	[ < of.name, 
   	  	    of.funType, 
   	  	    (of.scopeIn in moduleNames) ? "" : of.scopeIn, 
   	  		sort([ uid2str[fuid] | int fuid <- of.fuids, isFunction(fuid) && !isDefaultFunction(fuid) ]) 
   	  		+ sort([ uid2str[fuid] | int fuid <- of.fuids, isDefaultFunction(fuid) ])
   	  		  // Replace call to a constructor with call to the constructor companion function if the constructor has keyword parameters
   	  		  + sort([ getCompanionForUID(fuid) | int fuid <- of.fuids, isConstructor(fuid), !isEmpty(getAllKeywordFields(fuid)) ]),
   	  		sort([ uid2str[fuid] | int fuid <- of.fuids, isConstructor(fuid), isEmpty(getAllKeywordFields(fuid))])
   	  	  > 
   	  	| tuple[str name, Symbol funType, str scopeIn, list[int] fuids] of <- getOverloadedFunctions() 
   	  	];  
   	  
   	  return relocMuModule(
   	            muModule(modName,
   	  				  getModuleTags(),
   	                  config.messages, 
   	  				  getImportsInModule(),
   	  				  getExtendsInModule(), 
   	  				  types, 
   	  				  getDefinitions(), 
   	  				  getFunctionsInModule(), 
   	  				  getVariablesInModule(), 
   	  				  getVariableInitializationsInModule(), 
   	  				  getModuleVarInitLocals(modName), 
   	  				  getOverloadingResolver(),
   	  				  overloaded_functions, 
   	  				  getGrammar(),
   	  				  {<prettyPrintName(rn1), prettyPrintName(rn2)> | <rn1, rn2> <- config.importGraph},
   	  				  M@\loc),
   	  				  reloc,
   	  				  pcfg.srcs);

   }
   catch ParseError(loc l): {
        if (verbose) println("Parse error in concrete syntax <l>; returning error module");
        return errorMuModule(getModuleName(), {error("Parse error in concrete syntax fragment", l)}, M@\loc);
   }
   catch CompileTimeError(Message m): {
        return errorMuModule(getModuleName(), {m}, M@\loc);
   }
   //catch value e: {
   //     return errorMuModule(getModuleName(), {error("Unexpected compiler exception <e>", M@\loc)}, M@\loc);
   //}
   finally {
   	   resetModuleInfo(optimize, enableAsserts);
   	   resetScopeExtraction();
   }
   throw "r2mu: cannot come here!";
}

Configuration relocConfig(Configuration config, loc reloc, list[loc] srcs){
        return visit(config) { case loc l => relocLoc(l, reloc, srcs) };
}

void generateCompanions(lang::rascal::\syntax::Rascal::Module M, Configuration config, bool verbose = true){

   set[str] seenCommonDataFields = {};  // remember for which common data fields we have already generated a companion
 
   // Generate companion functions  constructors with keyword fields or common keyword fields     
   // This enables evaluation of potentially non-constant default expressions and semantics of implicit keyword arguments
                 
   for(int uid <- config.store, 
       AbstractValue::constructor(RName name, Symbol \type, KeywordParamMap keywordParams, int scopeIn, _, constructorLoc) := config.store[uid], 
       //bprintln("<uid>: <name>: <constructorLoc>, <M@\loc> <constructorLoc < M@\loc>, <constructorLoc.path == M@\loc.path>"),
       //constructorLoc.path == M@\loc.path,
       allKwFields := getAllKeywordFields(uid), 
       //bprintln(allKwFields), 
       !isEmpty(allKwFields)) {
        //println("*** Creating companion for <uid>");
         
       map[RName,Symbol] allKWFieldsAndTypes = getAllKeywordFieldsAndTypes(uid);
        
       /*
        * Create companion for the creation of the constructor
        */
         
       str fuid = getCompanionForUID(uid);
       Symbol ftype = Symbol::func(getConstructorResultType(\type), [ t | Symbol::label(l,t) <- getConstructorArgumentTypes(\type) ], []);
       list[str] argNames = [ l | Symbol::label(l,t) <- getConstructorArgumentTypes(\type) ];
       tuple[str fuid,int pos] addr = uid2addr[uid];
       int nformals = size(\type.parameters) + 1;
       int defaults_pos = nformals;
        
       //println("enter function scope <fuid>");
       enterFunctionScope(fuid);
       
       kwTypes = Symbol::\tuple([ Symbol::label(getSimpleName(rname),allKWFieldsAndTypes[rname]) | rname <- allKWFieldsAndTypes ]);
       //println("kwTypes: <kwTypes>");
       MuExp body = muReturn1(muCall(muConstr(uid2str[uid]), [ muVar("arg<i>",fuid,i) | int i <- [0..size(\type.parameters)] ] 
                                                               + [ muVar("kwparams", fuid, size(\type.parameters)),
                                                                   muTypeCon(kwTypes) 
                                                               ]));                          
       leaveFunctionScope();
       addFunctionToModule(muFunction(fuid, name.name, ftype, argNames, kwTypes, (addr.fuid in moduleNames) ? "" : addr.fuid,nformals, nformals + 1, false, true, true, |std:///|, [], (), false, 0, 0, body));                                             
     
       /*
        * Create companion for computing the values of defaults
        */
        
      // println("*** Creating defaults companion for <uid>");
        
       str fuidDefaults = getCompanionDefaultsForUID(uid);

       tuple[str fuid,int pos] addrDefaults = uid2addr[uid];
       addrDefaults.fuid = fuidDefaults;
         
       //println("enter function scope <fuidDefaults>");
       enterFunctionScope(fuidDefaults);
         
       list[MuExp] kwps = [ muAssign("map_of_default_values", fuidDefaults, defaults_pos, muCallMuPrim("make_mmap_str_entry",[])) ];
       //allKWFieldsAndDefaults = getAllKeywordFieldDefaults(uid);
       allKWFieldsAndDefaultsInModule = getAllKeywordFieldDefaultsInModule(uid, M@\loc.path);
        
       for(<RName kwf, value defaultVal> <- allKWFieldsAndDefaultsInModule) {
           if(Expression defaultExpr := defaultVal /*&& (defaultExpr@\loc < constructorLoc)*/){
              kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
                                   [ muVar("map_of_default_values",fuidDefaults,defaults_pos), 
                                     muCon("<getSimpleName(kwf)>"), 
                                     muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(allKWFieldsAndTypes[kwf]), 
                                                                               translate(defaultExpr) ]) ]);
           }
       }
         
       MuExp bodyDefaults =  muBlock(kwps + [ muReturn1(muVar("map_of_default_values",fuidDefaults,defaults_pos)) ]);
       //println("Generating function <fuidDefaults>");
       //iprintln(bodyDefaults);
       
       leaveFunctionScope();
       addFunctionToModule(muFunction(fuidDefaults, name.name, ftype, argNames, Symbol::\tuple([]), (addrDefaults.fuid in moduleNames) ? "" : addrDefaults.fuid, nformals, nformals+1, false, true, true, |std:///|, [], (), false, 0, 0, bodyDefaults));                                             
       
       /*
        * Create companions for each common keyword field
        */
         
       //println("**** Create companions per common data field");
       //println("Number of default fields: <size(allKWFieldsAndDefaultsInModule)>");
       
       dataKWFieldsAndDefaults = [<kwf, defaultExpr> | <kwf, defaultVal> <- allKWFieldsAndDefaultsInModule, 
                                                      Expression defaultExpr := defaultVal,
                                                      !(defaultExpr@\loc < constructorLoc)];
       
      
       //println("Number of datadefault fields: <size(dataKWFieldsAndDefaults)>"); 
       for(int i <- index(dataKWFieldsAndDefaults)){
           for(int j <- [0 .. i+1]){
               <mainKwf, mainDefaultVal> = dataKWFieldsAndDefaults[j];

               //println("<i>, <j>: <mainKwf>");
               str fuidDefault = getCompanionDefaultsForADTandField(getADTName(getConstructorResultType(\type)), prettyPrintName(mainKwf));
               if(fuidDefault in seenCommonDataFields) continue;
               
               seenCommonDataFields += fuidDefault;
               
               tuple[str fuid,int pos] addrDefault = uid2addr[uid];
               addrDefault.fuid = fuidDefault;
               nformals = 1;      // the keyword map
               defaults_pos = nformals;
         
               //println("**** enter scope <fuidDefault>");
               enterFunctionScope(fuidDefault);
               
               ftype = Symbol::func(allKWFieldsAndTypes[mainKwf], [ Symbol::\void() ], []);
               
               kwps = [ muAssign("map_of_default_values", fuidDefault, defaults_pos, muCallMuPrim("make_mmap_str_entry",[])) ];
                 
               for(int k <- [0 .. j+1]) {
                   <kwf, defaultVal> = dataKWFieldsAndDefaults[k];
                   if(Expression defaultExpr := defaultVal){
                      if(k < j){
                         kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
                                           [ muVar("map_of_default_values",fuidDefault,defaults_pos), 
                                             muCon("<getSimpleName(kwf)>"), 
                                            muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(allKWFieldsAndTypes[kwf]), 
                                                                                      translate(defaultExpr) ]) ]);
                      } else {
                         kwps += muReturn1(translate(defaultExpr));
                      }
                   }
               }
         
               MuExp bodyDefault =  muBlock(kwps);
         
              // iprintln(bodyDefault);
         
               leaveFunctionScope();
               addFunctionToModule(muFunction(fuidDefault, prettyPrintName(mainKwf), ftype, argNames, Symbol::\tuple([]), (addrDefault.fuid in moduleNames) ? "" : addrDefault.fuid, nformals, nformals+1, false, true, true, |std:///|, [], (), false, 0, 0, bodyDefault));                                             
             }
       }
   }

}

void translateModule((Module) `<Header header> <Body body>`) {
    for(imp <- header.imports) importModule(imp);
	for( tl <- body.toplevels) translate(tl);
}

/********************************************************************/
/*                  Translate imports in a module                   */
/********************************************************************/

private void importModule((Import) `import <QualifiedName qname> ;`){
    addImportToModule(prettyPrintName(convertName(qname)));
}

private void importModule((Import) `extend <QualifiedName qname> ;`){
	moduleName = prettyPrintName(convertName(qname));
	addImportToModule(moduleName);
	addExtendToModule(moduleName);
}

private void importModule((Import) `<SyntaxDefinition syntaxdef>`){ /* nothing to do */ }

private default void importModule(Import imp){
    throw "Unimplemented import: <imp>";
}
