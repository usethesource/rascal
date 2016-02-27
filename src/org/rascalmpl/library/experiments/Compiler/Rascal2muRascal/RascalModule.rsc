@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalModule

import IO;
import Map;
import String;
import Set;
import List;
import Relation;
import util::Reflective;
//import util::ValueUI;

import ParseTree;
import Type;

import lang::rascal::\syntax::Rascal;
import experiments::Compiler::muRascal::AST;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::CheckerConfig;

import experiments::Compiler::Rascal2muRascal::ModuleInfo;
import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::Rascal2muRascal::RascalDeclaration;
import experiments::Compiler::Rascal2muRascal::RascalExpression;

/*
 * Translate a Rascal module to muRascal.
 * The essential function is:
 * - r2mu		translate Rascal module to muRascal
 */

/********************************************************************/
/*                  Translate one module                            */
/********************************************************************/

@doc{Compile a parsed Rascal source module to muRascal}
MuModule r2mu(lang::rascal::\syntax::Rascal::Module M, Configuration config, bool verbose = true){
   try {
      resetModuleInfo();
      module_name = "<M.header.name>";
      setModuleName(module_name);
      setModuleTags(translateTags(M.header.tags));
      if(verbose) println("r2mu: entering ... <module_name>");
   	  
   	  // Extract scoping information available from the configuration returned by the type checker  
   	  extractScopes(config); 
   	  
   	  // Extract all declarations for the benefit of the type reifier
      extractDeclarationInfo(config);
   	 
   	  map[str,Symbol] types = 
   	  	( uid2str[uid] : \type | 
   	  	  int uid <- config.store, 
   	  	  ( AbstractValue::constructor(RName name, Symbol \type, KeywordParamMap keywordParams, int containedIn, loc at) := config.store[uid]
   	  	  || AbstractValue::production(RName name, Symbol \type, int containedIn, Production p, loc at) := config.store[uid] 
   	  	  ),
   	  	  !isEmpty(getSimpleName(name)),
   	  	  containedIn == 0, 
   	  	  config.store[containedIn].at.path == at.path // needed due to the handling of 'extend' by the type checker
   	  	);
   	  	
   	  // Generate companion functions for 
   	  // (1) keyword fields in constructors
   	  // (2) common keyword fields in data declarations
      generateCompanions(config, verbose=verbose);
   	 				  
   	  translateModule(M);
   	 
   	  modName = replaceAll("<M.header.name>","\\","");
   	 
   	  generate_tests(modName, M@\loc);
   	  
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
   	  
   	  return muModule(modName,
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
   	  				  M@\loc);

   } 
   //catch Java("ParseError","Parse error"): {
   //	   return errorMuModule(getModuleName(), {error("Syntax errors in module <M.header.name>", M@\loc)}, M@\loc);
   //} 
   catch e: {
        return errorMuModule(getModuleName(), {error("Unexpected exception <e>", M@\loc)}, M@\loc);
   }
   finally {
   	   resetModuleInfo();
   	   resetScopeExtraction();
   }
   throw "r2mu: cannot come here!";
}

void generateCompanions(Configuration config, bool verbose = true){

   set[str] seenCommonDataFields = {};  // record for which common data fields we have generated a companion
 
   // Generate companion functions  constructors with keyword fields or common keyword fields     
   // This enables evaluation of potentially non-constant default expressions and semantics of implicit keyword arguments
                
   for(int uid <- config.store, AbstractValue::constructor(RName name, Symbol \type, KeywordParamMap keywordParams, 0, constructorLoc) := config.store[uid], allKwFields := getAllKeywordFields(uid), !isEmpty(allKwFields)) {
       //println("*** Creating companion for <uid>");
         
       map[RName,Symbol] allKWFieldsAndTypes = getAllKeywordFieldsAndTypes(uid);
        
       /*
        * Create companion for the creation of the constructor
        */
         
       str fuid = getCompanionForUID(uid);
       Symbol ftype = Symbol::func(getConstructorResultType(\type), [ t | Symbol::label(l,t) <- getConstructorArgumentTypes(\type) ]);
       tuple[str fuid,int pos] addr = uid2addr[uid];
       int nformals = size(\type.parameters) + 1;
       int defaults_pos = nformals;
        
       //println("enter function scope <fuid>");
       enterFunctionScope(fuid);
         
       MuExp body = muReturn1(muCall(muConstr(uid2str[uid]), [ muVar("<i>",fuid,i) | int i <- [0..size(\type.parameters)] ] 
                                                               + [ muVar("kwparams", fuid, size(\type.parameters)),
                                                                   muTypeCon(Symbol::\tuple([ Symbol::label(getSimpleName(rname),allKWFieldsAndTypes[rname]) | rname <- allKWFieldsAndTypes ])) 
                                                               ]));                          
       leaveFunctionScope();
       addFunctionToModule(muFunction(fuid, name.name, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid,nformals, nformals + 1, false, true, |std:///|, [], (), false, 0, 0, body));                                             
     
       /*
        * Create companion for computing the values of defaults
        */
        
       //println("*** Creating defaults companion for <uid>");
        
       str fuidDefaults = getCompanionDefaultsForUID(uid);

       tuple[str fuid,int pos] addrDefaults = uid2addr[uid];
       addrDefaults.fuid = fuidDefaults;
         
       //println("enter function scope <fuidDefaults>");
       enterFunctionScope(fuidDefaults);
         
       list[MuExp] kwps = [ muAssign("map_of_default_values", fuidDefaults, defaults_pos, muCallMuPrim("make_mmap_str_entry",[])) ];
       allKWFieldsAndDefaults = getAllKeywordFieldDefaults(uid);
        
       for(<RName kwf, value defaultVal> <- allKWFieldsAndDefaults) {
           if(Expression defaultExpr := defaultVal /*&& (defaultExpr@\loc < constructorLoc)*/){
              kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
                                   [ muVar("map_of_default_values",fuidDefaults,defaults_pos), 
                                     muCon("<getSimpleName(kwf)>"), 
                                     muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(allKWFieldsAndTypes[kwf]), 
                                                                               translate(defaultExpr) ]) ]);
           }
       }
         
       MuExp bodyDefaults =  muBlock(kwps + [ muReturn1(muVar("map_of_default_values",fuidDefaults,defaults_pos)) ]);
       
       leaveFunctionScope();
       addFunctionToModule(muFunction(fuidDefaults, name.name, ftype, (addrDefaults.fuid in moduleNames) ? "" : addrDefaults.fuid, nformals, nformals+1, false, true, |std:///|, [], (), false, 0, 0, bodyDefaults));                                             
       
       /*
        * Create companions for each common keyword field
        */
         
       //println("**** Create companions per common data field");
       //println("Number of default fields: <size(allKWFieldsAndDefaults)>");
       
       dataKWFieldsAndDefaults = [<kwf, defaultExpr> | <kwf, defaultVal> <- allKWFieldsAndDefaults, 
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
               
               ftype = Symbol::func(allKWFieldsAndTypes[mainKwf], [ Symbol::\void() ]);
               
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
         
               //iprintln(bodyDefault);
         
               leaveFunctionScope();
               addFunctionToModule(muFunction(fuidDefault, prettyPrintName(mainKwf), ftype, (addrDefault.fuid in moduleNames) ? "" : addrDefault.fuid, nformals, nformals+1, false, true, |std:///|, [], (), false, 0, 0, bodyDefault));                                             
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
    addImportToModule("<qname>");
}

private void importModule((Import) `extend <QualifiedName qname> ;`){
	moduleName = "<qname>";
	addImportToModule(moduleName);
	addExtendToModule(moduleName);
}

private void importModule((Import) `<SyntaxDefinition syntaxdef>`){ /* nothing to do */ }

private default void importModule(Import imp){
    throw "Unimplemented import: <imp>";
}

/********************************************************************/
/*                  Translate the tests in a module                 */
/********************************************************************/
 
private void generate_tests(str module_name, loc src){
   testcode = getTestsInModule();
   if(!isEmpty(testcode)){
      code = muBlock([ muCallPrim3("testreport_open", [], src), *testcode, muReturn1(muCallPrim3("testreport_close", [], src)) ]);
      ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
      name_testsuite = "<module_name>_testsuite";
      main_testsuite = getFUID(name_testsuite,name_testsuite,ftype,0);
      addFunctionToModule(muFunction(main_testsuite, "testsuite", ftype, "" /*in the root*/, 2, 2, false, true, src, [], (), false, 0, 0, code));
   }
}
