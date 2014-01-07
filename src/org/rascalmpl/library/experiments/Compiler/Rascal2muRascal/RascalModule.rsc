@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalModule

import lang::rascal::\syntax::Rascal;
import Prelude;
import util::Reflective;
import util::ValueUI;
import ParseTree;

import lang::rascal::types::AbstractName;
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

public str module_name;
public str function_uid;
public list[loc] imported_modules = [];
public list[MuFunction] functions_in_module = [];
public list[MuVariable] variables_in_module = [];
public list[MuExp] variable_initializations = [];
public list[MuExp] tests = [];

public loc Library = |rascal:///experiments/Compiler/muRascal2RVM/Library.mu|;

public void resetR2mu() {
 	module_name = "** undefined **";
    imported_modules = [];
	functions_in_module = [];
	variables_in_module = [];
	variable_initializations = [];
	tests = [];
	resetTmpAndLabel();
}

public str getModuleName() = module_name;

private void setFunctionUID(loc l) {
   inverted = config.definitions<1,0>;
   function_uid = toList(inverted[l])[0];
   //println("function_uid = <function_uid>");
}

public int getFunctionUID() = function_uid;

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
   	Configuration c = newConfiguration();
   	config = checkModule(M, c);
   	// Extract scoping information available from the configuration returned by the type checker  
   	extractScopes();  
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
   	  module_name = "<M.header.name>";
   	  imported_modules = [];
   	  functions_in_module = [];
   	  variables_in_module = [];
   	  variable_initializations = [];
   	  map[str,Symbol] types = ( fuid2str[uid] : \type | int uid <- config.store, 
   	  									   					( constructor(name, Symbol \type, containedIn, at) := config.store[uid]
   	  									   				      || production(name, Symbol \type, containedIn, at) := config.store[uid] ),
   	  									   				    !isEmpty(getSimpleName(name)),
   	  									   				    containedIn == 0
   	  						  );
   	  translate(M);
   	 
   	  modName = replaceAll("<M.header.name>","\\","");
   	 
   	  generate_tests(modName);
   	  
   	  // Overloading resolution...	  
   	  lrel[str,list[str],list[str]] overloaded_functions = [ < (of.scopeIn in moduleNames) ? "" : of.scopeIn, 
   	  														   [ fuid2str[fuid] | int fuid <- of.fuids, (fuid in functions) && (fuid notin defaultFunctions) ] 
   	  														   		+ [ fuid2str[fuid] | int fuid <- of.fuids, fuid in defaultFunctions ],
   	  														   [ fuid2str[fuid] | int fuid <- of.fuids, fuid in constructors ]
   	  											  			 > 
   	  															| tuple[str scopeIn,set[int] fuids] of <- overloadedFunctions ];
   	  
   	  return muModule(modName, imported_modules, types, functions_in_module, variables_in_module, variable_initializations, overloadingResolver, overloaded_functions, getGrammar(config));
   	}
   } catch Java("ParseError","Parse error"): {
   	   throw "Syntax errors in module <moduleLoc>";
   } finally {
   	   //println("r2mu: Cleaning up ...");
   	   resetR2mu();
   	   resetScopeExtraction();
   	   //println("r2mu: Cleaned up!");
   }
   throw "r2mu: cannot come here!";
}

void translate(m: (Module) `<Header header> <Body body>`) {
    for(imp <- header.imports) importModule(imp);
	for( tl <- body.toplevels) translate(tl);
}

void importModule((Import) `import <QualifiedName qname> ;`){
    name = replaceAll("<qname>", "::", "/");
    name = replaceAll(name, "\\","");
    //println("name = <name>");
    imported_modules += |rascal:///| + ("<name>" + ".rsc");
    //println("imported_modules = <imported_modules>");
}

void importModule((Import) `extend <QualifiedName qname> ;`){  // TODO implement extend properly
    name = replaceAll("<qname>", "::", "/");
    name = replaceAll(name, "\\","");
    //println("name = <name>");
    imported_modules += |rascal:///| + ("<name>" + ".rsc");
    //println("imported_modules = <imported_modules>");
}

void importModule((Import) `<SyntaxDefinition syntaxdef>`){ /* nothing to do */ }

default void importModule(Import imp){
    throw "Unimplemented import: <imp>";
}
	
void translate(t: (Toplevel) `<Declaration decl>`) = translate(decl);

// Toplevel Declaration: variable

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

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`) { /*skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { /* skip: translation has nothing to do here */ }

void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// FunctionDeclaration

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  println("r2mu: Compiling <signature.name>");
  setFunctionUID(fd@\loc);
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
  enterFunctionScope(fuid);
  
  // Keyword parameters
  list[MuExp] kwps = translateKeywordParameters(signature.parameters, fuid, getFormals(uid), fd@\loc);
 
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  if(ttags["javaClass"]?){
     paramTypes = \tuple([param | param <- ftype.parameters]);
     params = [ muVar("<ftype.parameters[i]>", fuid, i) | i <- [ 0 .. nformals] ];
     exp = muCallJava("<signature.name>", ttags["javaClass"], paramTypes, ("reflect" in ttags) ? 1 : 0, params);
     
     // TODO: we plan to introduce keyword patterns as formal parameters
     tbody = translateFunction(signature.parameters.formals.formals, isVarArgs, kwps, exp, []);
    
     functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									getFormals(uid), getScopeSize(fuid),
  									isVarArgs, fd@\loc, tmods, ttags, 
  									tbody);
  	
  	leaveFunctionScope();
  	
  } else {
    println("r2mu: <fuid> ignored");
  }
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  println("r2mu: Compiling <signature.name>");
  setFunctionUID(fd@\loc);
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
  // Keyword parameters
  list[MuExp] kwps = translateKeywordParameters(signature.parameters, fuid, getFormals(uid), fd@\loc);
  
  // TODO: we plan to introduce keyword patterns as formal parameters
  tbody = translateFunction(signature.parameters.formals.formals, isVarArgs, kwps, translate(expression), []);
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									getFormals(uid), getScopeSize(fuid), 
  									isVarArgs, fd@\loc, tmods, ttags, 
  									tbody);
  
  if("test" in tmods){
     params = ftype.parameters;
     // Switched from type constant
     //tests += muCallPrim("testreport_add", [muCon(fuid), muCon(ttags["ignore"]?), muCon(ttags["expected"] ? ""), muCon(fd@\loc), muTypeCon(\tuple([param | param <- params ])) ]);
     // to reified type
     tests += muCallPrim("testreport_add", [muCon(fuid),  muCon(ttags["ignore"] ?), muCon(ttags["expected"] ? ""), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
  
  leaveFunctionScope();
  
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  println("r2mu: Compiling <signature.name>");
  setFunctionUID(fd@\loc);
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
  // Keyword parameters
  list[MuExp] kwps = translateKeywordParameters(signature.parameters, fuid, getFormals(uid), fd@\loc);
  
  // TODO: we plan to introduce keyword patterns as formal parameters
  tbody = translateFunction(signature.parameters.formals.formals, isVarArgs, kwps, translate(expression), [exp | exp <- conditions]);
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									getFormals(uid), getScopeSize(fuid), 
  									isVarArgs, fd@\loc, tmods, ttags, 
  									tbody);
  
  if("test" in tmods){
     params = ftype.parameters;
     // Switched from type constant
     // tests += muCallPrim("testreport_add", [muCon(fuid),  muCon(ttags["ignore"]?), muCon(ttags["expected"] ? ""), muCon(fd@\loc), muTypeCon(\tuple([param | param <- params ])) ]);
     // to reified type
     tests += muCallPrim("testreport_add", [muCon(fuid),  muCon(ttags["ignore"]?), muCon(ttags["expected"] ? ""), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
  
  leaveFunctionScope();
  
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  println("r2mu: Compiling <signature.name>");
  setFunctionUID(fd@\loc);
  ftype = getFunctionType(fd@\loc);    
  nformals = size(ftype.parameters);
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  // Keyword parameters
  list[MuExp] kwps = translateKeywordParameters(signature.parameters, fuid, getFormals(uid), fd@\loc);
  
  // TODO: we plan to introduce keyword patterns as formal parameters 
  MuExp tbody = translateFunction(signature.parameters.formals.formals, isVarArgs, kwps, body.statements, []);
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									getFormals(uid), getScopeSize(fuid),
  									isVarArgs, fd@\loc, translateModifiers(signature.modifiers), translateTags(tags), 
  									tbody);
  					
   if("test" in tmods){
     params = ftype.parameters;
      // Switched from type constant
     // tests += muCallPrim("testreport_add", [muCon(fuid), muCon(ttags["ignore"]?), muCon(ttags["expected"] ? ""), muCon(fd@\loc), muTypeCon(\tuple([param | param <- params ])) ]);
     // to reified type
     tests += muCallPrim("testreport_add", [muCon(fuid),  muCon(ttags["ignore"]?), muCon(ttags["expected"] ? ""), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
  									
  leaveFunctionScope();
   
}



/* withThrows: FunctionModifiers modifiers Type type  Name name Parameters parameters "throws" {Type ","}+ exceptions */

//str translate(Signature s:(Signature) `<FunctionModifiers modifiers> <Type \type> <Name name> <Parameters parameters>`){
//  formals = parameters.formals.formals;
//  //keywordFormals = parameters.keywordFormals;
//  return intercalate(", ", [(Pattern) `<Type tp> <Name nm>` := f ? "var(\"<nm>\", <tp>)" : "pattern" | f <- formals]);
//}

map[str,str] translateTags(Tags tags){
   m = ();
   for(tg <- tags.tags){
     name = "<tg.name>";
     if(tg is \default)
        m[name] = "<tg.contents>"[1 .. -1];
     else if (tg is empty)
        m[name] = "";
     else
        m[name] = "<tg.expression>"[1 .. -1];
   }
   return m;
}

list[str] translateModifiers(FunctionModifiers modifiers){
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

list[MuExp] translateKeywordParameters(Parameters parameters, str fuid, int pos, loc l) {
  list[MuExp] kwps = [];
  KeywordFormals kwfs = parameters.keywordFormals;
  if(kwfs is \default) {
      keywordParamsMap = getKeywords(l);
      kwps = [ muAssign("map_of_default_values", fuid, pos, muCallMuPrim("make_map_str_entry",[])) ];
      for(KeywordFormal kwf <- kwfs.keywordFormalList) {
          kwps += muCallMuPrim("map_str_entry_add_entry_type_ivalue", 
                                  [ muVar("map_of_default_values",fuid,pos), 
                                    muCon("<kwf.name>"), 
                                    muCallMuPrim("make_entry_type_ivalue", [ muTypeCon(keywordParamsMap[convertName(kwf.name)]), 
                                                                             translate(kwf.expression) ]) ]);
      }
  }
  return kwps;
}

void generate_tests(str module_name){
   code = muBlock([ muCallPrim("testreport_open", []), *tests, muReturn(muCallPrim("testreport_close", [])) ]);
   ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
   name_testsuite = "<module_name>_testsuite";
   main_testsuite = getFUID(name_testsuite,name_testsuite,ftype,0);
   functions_in_module += muFunction(main_testsuite, ftype, "" /*in the root*/, 2, 2, false, |rascal:///|, [], (), code);
}
