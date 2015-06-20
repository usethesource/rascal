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

//@doc{Compile a Rascal source module (given as string) to muRascal}
//MuModule r2mu(str moduleStr){
//	return r2mu(parse(#start[Module], moduleStr).top); // .top is needed to remove start! Ugly!
//}
//
//@doc{Compile a Rascal source module (given at a location) to muRascal}
//MuModule r2mu(loc moduleLoc){
//    //println(readFile(moduleLoc));   
//   	return r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
//}
//
//MuModule r2mu(lang::rascal::\syntax::Rascal::Module M){
//   	Configuration config;
//   	try {
//   	    config  = checkModule(M, newConfiguration());
//   	} catch e: {
//   	    throw e;
//   	}
//   	// Uncomment to dump the type checker configuration:
//   	//text(config);
//   	errors = [ e | e:error(_,_) <- config.messages];
//   	warnings = [ w | w:warning(_,_) <- config.messages ];
//   
//   	if(size(errors) > 0) {
//   	    return errorMuModule("<M.header.name>", config.messages, M@\loc);
//   	 }
//   	 
//   	 return r2mu(M, config);
//}

@doc{Compile a parsed Rascal source module to muRascal}
MuModule r2mu(lang::rascal::\syntax::Rascal::Module M, Configuration config){
   try {
    resetModuleInfo();
    module_name = "<M.header.name>";
    setModuleName(module_name);
    println("r2mu: entering ... <module_name>");
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
   	 
   	 // Constructor functions are generated in case of constructors with keyword parameters
   	 // (this enables evaluation of potentially non-constant default expressions and semantics of implicit keyword arguments)						  
   	 for(int uid <- config.store, AbstractValue::constructor(RName name, Symbol \type, KeywordParamMap keywordParams, 0, _) := config.store[uid], !isEmpty(config.dataKeywordDefaults[uid])) {
   	     // ***Note: the keywordParams field excludes the common keyword parameters 
   	     map[RName,Symbol] allKeywordParams = ();
   	     for(<RName rname, _> <- config.dataKeywordDefaults[uid]) { // All the keyword parameters
   	         int adt = toMapUnique(invert(config.adtConstructors))[uid];
   	         allKeywordParams[rname] = config.adtFields[<adt,getSimpleName(rname)>];
   	     }
   	     str fuid = getCompanionForUID(uid);
   	     Symbol ftype = Symbol::func(getConstructorResultType(\type), [ t | Symbol::label(l,t) <- getConstructorArgumentTypes(\type) ]);
   	     tuple[str fuid,int pos] addr = uid2addr[uid];
   	     int nformals = size(\type.parameters) + 1;
   	     int defaults_pos = nformals;
   	     
   	     enterFunctionScope(fuid);
   	     
   	     list[MuExp] kwps = [ muAssign("map_of_default_values", fuid, defaults_pos, muCallMuPrim("make_mmap_str_entry",[])) ];
   	     list[MuExp] kwargs = [];
         for(RName kwf <- allKeywordParams) {
             if(Expression kw_default_expr := getOneFrom(config.dataKeywordDefaults[uid,kwf])){
	             kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
	                                  [ muVar("map_of_default_values",fuid,defaults_pos), 
	                                    muCon("<getSimpleName(kwf)>"), 
	                                    muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(allKeywordParams[kwf]), 
	                                                                              translate(kw_default_expr) ]) ]);
	             kwargs = kwargs + [ muCon("<getSimpleName(kwf)>"), muVarKwp(fuid,getSimpleName(kwf)) ];
             } else {
             	throw "Keyword default expression for <kwf> of incorrect type";
             }
         }
         MuExp body = 
         	muBlock(kwps 
         			+ kwargs 
         			+ [ muReturn1(muCall(muConstr(uid2str[uid]),[ muVar("<i>",fuid,i) | int i <- [0..size(\type.parameters)] ] 
                    + [ muCallMuPrim("make_mmap", kwargs), 
                    muTypeCon(Symbol::\tuple([ Symbol::label(getSimpleName(rname),allKeywordParams[rname]) | rname <- allKeywordParams ])) ])) ]);
                                                
         leaveFunctionScope();
         addFunctionToModule(muFunction(fuid,name.name,ftype,(addr.fuid in moduleNames) ? "" : addr.fuid,nformals,nformals + 1,false,true,|std:///|,[],(),false,0,0,body));   	                                       
   	 }
   	 				  
   	  translateModule(M);
   	 
   	  modName = replaceAll("<M.header.name>","\\","");
   	 
   	  generate_tests(modName, M@\loc);
   	  
   	  //println("overloadedFunctions"); for(tp <- getOverloadedFunctions()) println(tp);
   	  // Overloading resolution...	  
   	  lrel[str name, Symbol funType, str scopeIn, list[str] ofunctions, list[str] oconstructors] overloaded_functions = 
   	  	[ < of.name, of.funType, (of.scopeIn in moduleNames) ? "" : of.scopeIn, 
   	  		[ uid2str[fuid] | int fuid <- of.fuids, isFunction(fuid) && !isDefaultFunction(fuid) ] 
   	  		+ [ uid2str[fuid] | int fuid <- of.fuids, isDefaultFunction(fuid) ]
   	  		  // Replace call to a constructor with call to the constructor companion function if the constructor has keyword parameters
   	  		+ [ getCompanionForUID(fuid) | int fuid <- of.fuids, isConstructor(fuid), !isEmpty(config.dataKeywordDefaults[fuid]) ],
   	  		[ uid2str[fuid] | int fuid <- of.fuids, isConstructor(fuid), isEmpty(config.dataKeywordDefaults[fuid]) ]
   	  	  > 
   	  	| tuple[str name, Symbol funType, str scopeIn, list[int] fuids] of <- getOverloadedFunctions() 
   	  	];  
   	  
   	  return muModule(modName,
   	  				  translateTags(M.header.tags),
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

void translateModule((Module) `<Header header> <Body body>`) {
    for(imp <- header.imports) importModule(imp);
	for( tl <- body.toplevels) translate(tl);
}

/********************************************************************/
/*                  Translate imports in a module                   */
/********************************************************************/

private void importModule((Import) `import <QualifiedName qname> ;`){
    addImportToModule(getModuleLocation(qualifiedNameToPath(qname)));
}

private void importModule((Import) `extend <QualifiedName qname> ;`){
	moduleLoc = getModuleLocation(qualifiedNameToPath(qname));
	addImportToModule(moduleLoc);
	addExtendToModule(moduleLoc);
}

private void importModule((Import) `<SyntaxDefinition syntaxdef>`){ /* nothing to do */ }

private default void importModule(Import imp){
    throw "Unimplemented import: <imp>";
}

/********************************************************************/
/*                  Translate the tests in a module                 */
/********************************************************************/
 
private void generate_tests(str module_name, loc src){
   code = muBlock([ muCallPrim3("testreport_open", [], src), *getTestsInModule(), muReturn1(muCallPrim3("testreport_close", [], src)) ]);
   ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
   name_testsuite = "<module_name>_testsuite";
   main_testsuite = getFUID(name_testsuite,name_testsuite,ftype,0);
   addFunctionToModule(muFunction(main_testsuite, "testsuite", ftype, "" /*in the root*/, 2, 2, false, true, src, [], (), false, 0, 0, code));
}
