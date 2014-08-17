@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalModule

import lang::rascal::\syntax::Rascal;
import Prelude;
import util::Reflective;
import util::ValueUI;
import ParseTree;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::muRascal::Implode;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

/*
 * Translate a Rascal module to muRascal.
 * The essential functions are:
 * - r2mu		translate Rascal module
 * - translate	translate a declaration in a a Rascal module
 * - translateFunctionDeclaration
 */
 
 // Global state maintained when translating a Rascal module
 
private str module_name;								//  name of current module
private str function_uid;							// uid of current function
private list[loc] imported_modules = [];				// imported modules of current module
private list[MuFunction] functions_in_module = [];	// functions declared in current module
private list[MuVariable] variables_in_module = [];	// variables declared in current module
private list[MuExp] variable_initializations = [];	// initialized variables declared in current module
private list[MuExp] tests = [];						// tests declared in current module

													// location of the muRascal library
//public loc Library = |rascal:///experiments/Compiler/muRascal2RVM/Library.mu|;

private set[str] overriddenLibs = {};				// Java libraries overriden for compiler
private set[str] notOverriddenLibs = {};			// Java libraries not overridden for compiler

// Access functions

public str getModuleName() = module_name;

private void setFunctionUID(loc l) {
   inverted = config.definitions<1,0>;
   function_uid = toList(inverted[l])[0];
   //println("function_uid = <function_uid>");
}

public int getFunctionUID() = function_uid;

public list[MuFunction] getFunctionsInModule() = functions_in_module;

public void addFunctionToModule(MuFunction fun) {
   functions_in_module += fun;
}

public void addFunctionsToModule(list[MuFunction] funs) {
   functions_in_module += funs;
}

public void setFunctionsInModule(list[MuFunction] funs) {
   functions_in_module = funs;
}

// Reset global state

private void resetR2mu() {
 	module_name = "** undefined **";
    imported_modules = [];
	functions_in_module = [];
	variables_in_module = [];
	variable_initializations = [];
	tests = [];
	resetTmpAndLabel();
	overriddenLibs = {};
    notOverriddenLibs = {};
}

/********************************************************************/
/*                  Translate one module                            */
/********************************************************************/

@doc{Compile a Rascal source module (given as string) to muRascal}
MuModule r2mu(str moduleStr){
	return r2mu(parse(#start[Module], moduleStr).top); // .top is needed to remove start! Ugly!
}

@doc{Compile a Rascal source module (given at a location) to muRascal}
MuModule r2mu(loc moduleLoc){
    println(readFile(moduleLoc));   
   	muMod = r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
   	return muMod;
}

@doc{Compile a parsed Rascal source module to muRascal}
MuModule r2mu(lang::rascal::\syntax::Rascal::Module M){
   try {
    println("r2mu: entering ...");
   	Configuration c = newConfiguration();
   	config = checkModule(M, c);  
   	//text(config);
   	errors = [ e | e:error(_,_) <- config.messages];
   	warnings = [ w | w:warning(_,_) <- config.messages ];
   	if(size(errors) > 0) {
   	  for(e <- errors) {
   	  	println(e);
   	  }
   	  throw "Module contains errors!";
   	} else {
   	  // If no static errors...
   	  if(size(warnings) > 0) {
   	  	for(w <- warnings) {
   	  		println(w);
   	  	}
   	  }
   	  // Extract scoping information available from the configuration returned by the type checker  
   	  extractScopes(); 
   	  module_name = "<M.header.name>";
   	  imported_modules = [];
   	  functions_in_module = [];
   	  variables_in_module = [];
   	  variable_initializations = [];
   	  map[str,Symbol] types = ( fuid2str[uid] : \type | int uid <- config.store, 
   	  									   					( constructor(name, Symbol \type, keywordParams, containedIn, at) := config.store[uid]
   	  									   				      || production(name, Symbol \type, containedIn, at) := config.store[uid] ),
   	  									   				    !isEmpty(getSimpleName(name)),
   	  									   				    containedIn == 0, config.store[containedIn].at.path == at.path // needed due to the handling of 'extend' by the type checker
   	  						  );
   	 
   	 // Constructor functions are generated in case of constructors with keyword parameters
   	 // (this enables evaluation of potentially non-constant default expressions and semantics of implicit keyword arguments)						  
   	 for(int uid <- config.store, constructor(name, Symbol \type, keywordParams, 0, _) := config.store[uid], !isEmpty(config.dataKeywordDefaults[uid])) {
   	     // ***Note: the keywordParams field excludes the common keyword parameters 
   	     map[RName,Symbol] keywordParams = ();
   	     for(<RName rname, _> <- config.dataKeywordDefaults[uid]) { // All the keyword parameters
   	         int adt = toMapUnique(invert(config.adtConstructors))[uid];
   	         keywordParams[rname] = config.adtFields[<adt,getSimpleName(rname)>];
   	     }
   	     str fuid = fuid2str[uid] + "::companion";
   	     Symbol ftype = Symbol::func(getConstructorResultType(\type),[ t | Symbol::label(l,t) <- getConstructorArgumentTypes(\type) ]);
   	     tuple[str fuid,int pos] addr = uid2addr[uid];
   	     int nformals = size(\type.parameters) + 1;
   	     int defaults_pos = nformals;
   	     
   	     enterFunctionScope(fuid);
   	     
   	     list[MuExp] kwps = [ muAssign("map_of_default_values", fuid, defaults_pos, muCallMuPrim("make_mmap_str_entry",[])) ];
   	     list[MuExp] kwargs = [];
         for(RName kwf <- keywordParams) {
             kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
                                  [ muVar("map_of_default_values",fuid,defaults_pos), 
                                    muCon("<getSimpleName(kwf)>"), 
                                    muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(keywordParams[kwf]), 
                                                                             translate(getOneFrom(config.dataKeywordDefaults[uid,kwf])) ]) ]);
             kwargs = kwargs + [ muCon("<getSimpleName(kwf)>"), muVarKwp(fuid,getSimpleName(kwf)) ];
         }
         MuExp body = muBlock(kwps + kwargs + [ muReturn(muCall(muConstr(fuid2str[uid]),[ muVar("<i>",fuid,i) | int i <- [0..size(\type.parameters)] ] 
                                            + [ muCallMuPrim("make_mmap", kwargs), 
                                                muTypeCon(Symbol::\tuple([ Symbol::label(getSimpleName(rname),keywordParams[rname]) | rname <- keywordParams ])) ])) ]);
                                                
         leaveFunctionScope();
         
         functions_in_module += muFunction(fuid,name.name,ftype,(addr.fuid in moduleNames) ? "" : addr.fuid,nformals,nformals + 1,false,|rascal:///|,[],(),body);   	                                       
   	 }
   	 				  
   	  translateModule(M);
   	 
   	  modName = replaceAll("<M.header.name>","\\","");
   	 
   	  generate_tests(modName);
   	  
   	  // Overloading resolution...	  
   	  lrel[str,list[str],list[str]] overloaded_functions = [ < (of.scopeIn in moduleNames) ? "" : of.scopeIn, 
   	  														   [ fuid2str[fuid] | int fuid <- of.fuids, (fuid in functions) && (fuid notin defaultFunctions) ] 
   	  														   	+ [ fuid2str[fuid] | int fuid <- of.fuids, fuid in defaultFunctions ]
   	  														   	// Replace call to a constructor with call to the constructor function if the constructor has keyword parameters
   	  														   	+ [ fuid2str[fuid] + "::companion" | int fuid <- of.fuids, fuid in constructors, !isEmpty(config.dataKeywordDefaults[fuid]) ],
   	  														   [ fuid2str[fuid] | int fuid <- of.fuids, fuid in constructors, isEmpty(config.dataKeywordDefaults[fuid]) ]
   	  											  			 > 
   	  															| tuple[str scopeIn,set[int] fuids] of <- overloadedFunctions ];    
   	  return muModule(modName, imported_modules, types, functions_in_module, variables_in_module, variable_initializations, overloadingResolver, overloaded_functions, getGrammar(config));
   	}
   } catch Java("ParseError","Parse error"): {
   	   throw "Syntax errors in module <moduleLoc>";
   } 
   finally {
   	   //println("r2mu: Cleaning up ...");
   	   resetR2mu();
   	   resetScopeExtraction();
   	   //println("r2mu: Cleaned up!");
   }
   throw "r2mu: cannot come here!";
}

void translateModule(m: (Module) `<Header header> <Body body>`) {
    for(imp <- header.imports) importModule(imp);
	for( tl <- body.toplevels) translate(tl);
}

/********************************************************************/
/*                  Translate imports in a module                   */
/********************************************************************/

private void importModule((Import) `import <QualifiedName qname> ;`){
    name = replaceAll("<qname>", "::", "/");
    name = replaceAll(name, "\\","");
    //println("name = <name>");
    imported_modules += |rascal:///| + ("<name>" + ".rsc");
    //println("imported_modules = <imported_modules>");
}

private void importModule((Import) `extend <QualifiedName qname> ;`){  // TODO implement extend properly
    name = replaceAll("<qname>", "::", "/");
    name = replaceAll(name, "\\","");
    //println("name = <name>");
    imported_modules += |rascal:///| + ("<name>" + ".rsc");
    //println("imported_modules = <imported_modules>");
}

private void importModule((Import) `<SyntaxDefinition syntaxdef>`){ /* nothing to do */ }

private default void importModule(Import imp){
    throw "Unimplemented import: <imp>";
}

/********************************************************************/
/*                  Translate declarations in a module              */
/********************************************************************/
	
void translate(t: (Toplevel) `<Declaration decl>`) = translate(decl);

// -- variable declaration ------------------------------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
    ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
    enterFunctionScope(getFUID(module_name,"#<module_name>_init",ftype,0));
   	for(var <- variables){
   		variables_in_module += [muVariable("<var.name>")];
   		if(var is initialized) 
   		   variable_initializations +=  mkAssign("<var.name>", var@\loc, translate(var.initial));
   	}
   	leaveFunctionScope();
}   	

// -- miscellaneous declarations that can be skipped ------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`) { /*skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { /* skip: translation has nothing to do here */ }

void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// -- function declaration ------------------------------------------

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  translateFunctionDeclaration(fd, muBlock([]), []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  translateFunctionDeclaration(fd, expression, []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  translateFunctionDeclaration(fd, expression, [exp | exp <- conditions]);
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  translateFunctionDeclaration(fd, body.statements, []);
}

private void translateFunctionDeclaration(FunctionDeclaration fd, node body, list[Expression] when_conditions){
  println("r2mu: Compiling <fd.signature.name>");
  setFunctionUID(fd@\loc);
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := fd.signature.parameters);
  
  // Keyword parameters
  list[MuExp] kwps = translateKeywordParameters(fd.signature.parameters, fuid, getFormals(uid), fd@\loc);
 
  tmods = translateModifiers(fd.signature.modifiers);
  ttags =  translateTags(fd.tags);
  
  if(ttags["javaClass"]?){
     paramTypes = \tuple([param | param <- ftype.parameters]);
     params = [ muVar("<ftype.parameters[i]>", fuid, i) | i <- [ 0 .. nformals] ];
     body = muCallJava("<fd.signature.name>", ttags["javaClass"], paramTypes, ("reflect" in ttags) ? 1 : 0, params);
     //tbody = translateFunction(fd.signature.parameters.formals.formals, isVarArgs, kwps, exp, when_conditions);
  	
  }
  tbody = translateFunction(fd.signature.parameters.formals.formals, isVarArgs, kwps, body, when_conditions);
  
  functions_in_module += muFunction(fuid, "<fd.signature.name>", ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									getFormals(uid), getScopeSize(fuid), 
  									isVarArgs, fd@\loc, tmods, ttags, 
  									tbody);
  
  if("test" in tmods){
     params = ftype.parameters;
     // Switched from type constant
     //tests += muCallPrim("testreport_add", [muCon(fuid), muCon(ttags["ignore"]?), muCon(ttags["expected"] ? ""), muCon(fd@\loc), muTypeCon(\tuple([param | param <- params ])) ]);
     // to reified type
     tests += muCallPrim("testreport_add", [muCon(fuid),  muCon(ignoreTest(ttags)), muCon(ttags["expected"] ? ""), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
  leaveFunctionScope();
}

/********************************************************************/
/*                  Translate keyword parameters                    */
/********************************************************************/

public list[MuExp] translateKeywordParameters(Parameters parameters, str fuid, int pos, loc l) {
  list[MuExp] kwps = [];
  KeywordFormals kwfs = parameters.keywordFormals;
  if(kwfs is \default) {
      keywordParamsMap = getKeywords(l);
      kwps = [ muAssign("map_of_default_values", fuid, pos, muCallMuPrim("make_mmap_str_entry",[])) ];
      for(KeywordFormal kwf <- kwfs.keywordFormalList) {
          kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
                                  [ muVar("map_of_default_values",fuid,pos), 
                                    muCon("<kwf.name>"), 
                                    muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(keywordParamsMap[convertName(kwf.name)]), 
                                                                             translate(kwf.expression) ]) ]);
      }
  }
  return kwps;
}

/********************************************************************/
/*                  Translate tags in a function declaration        */
/********************************************************************/

// Some library functions need special tratement when called from compiled code.
// Therefore we provide special treatment for selected Java classes. 
// A Java class X.java can be extended with a class XCompiled.java
// and all calls are then first routed to XCompiled.java that can selectively override methods.
// The compiler checks for the existence of a class XCompiled.java

private str resolveLibOverriding(str lib){
   
	if(lib in notOverriddenLibs) return lib;
	
	if(lib in overriddenLibs) return "<lib>Compiled";

    rlib1 = replaceFirst(lib, "org.rascalmpl.library.", "");
    rlib2 = |rascal:///| + "<replaceAll(rlib1, ".", "/")>Compiled.java";
  
	if(exists(rlib2)){
	   overriddenLibs += lib;
	   return "<lib>Compiled";
	} else {
		notOverriddenLibs += lib;
		return lib;
	}
}

private map[str,str] translateTags(Tags tags){
   m = ();
   for(tg <- tags.tags){
     name = "<tg.name>";
     if(tg is \default){
        cont = "<tg.contents>"[1 .. -1];
        m[name] = name == "javaClass" ? resolveLibOverriding(cont) : cont;
     } else if (tg is empty)
        m[name] = "";
     else
        m[name] = "<tg.expression>"[1 .. -1];
   }
   return m;
}

private bool ignoreTest(map[str, str] tags) = !isEmpty(domain(tags) & {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

/********************************************************************/
/*       Translate the modifiers in a function declaration          */
/********************************************************************/

private list[str] translateModifiers(FunctionModifiers modifiers){
   lst = [];
   for(m <- modifiers.modifiers){
     if(m is \java) 
       lst += "java";
     else if(m is \test)
       lst += "test";
     else
       lst += "default";
   }
   return lst;
}

/********************************************************************/
/*                  Translate the tests in a module                 */
/********************************************************************/

private void generate_tests(str module_name){
   code = muBlock([ muCallPrim("testreport_open", []), *tests, muReturn(muCallPrim("testreport_close", [])) ]);
   ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
   name_testsuite = "<module_name>_testsuite";
   main_testsuite = getFUID(name_testsuite,name_testsuite,ftype,0);
   functions_in_module += muFunction(main_testsuite, "testsuite", ftype, "" /*in the root*/, 2, 2, false, |rascal:///|, [], (), code);
}
