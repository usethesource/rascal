@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalModule

import lang::rascal::\syntax::Rascal;
import Prelude;
import util::Reflective;
import util::ValueUI;
import ParseTree;

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


public list[MuFunction] functions_in_module = [];
public list[MuVariable] variables_in_module = [];
public list[MuExp] variable_initializations = [];
public list[MuExp] tests = [];

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;

public void resetR2mu() {
	functions_in_module = [];
	variables_in_module = [];
	variable_initializations = [];
	tests = [];
	resetTmpAndLabel();
}

@doc{Compile a Rascal source module (given as string) to muRascal}
MuModule r2mu(str moduleStr){
	return r2mu(parse(#start[Module], moduleStr).top); // .top is needed to remove start! Ugly!
}

@doc{Compile a Rascal source module (given at a location) to muRascal}
MuModule r2mu(loc moduleLoc){
    //println(readFile(moduleLoc));
   	return r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
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
   	  functions_in_module = [];
   	  variables_in_module = [];
   	  variable_initializations = [];
   	  // TODO: think of types that have to be actually imported
   	  list[Symbol] types = [ \type | int uid <- config.store, 
   	  									   constructor(name, Symbol \type, containedIn, at) := config.store[uid]
   	  									|| production(name, Symbol \type, containedIn, at) := config.store[uid]
   	  									|| datatype(name, Symbol \type, containedIn, ats) := config.store[uid]
   	  									|| sorttype(name, Symbol \type, containedIn, ats) := config.store[uid]
   	  									|| \alias(name, Symbol \type, containedIn, at) := config.store[uid] ];
   	  translate(M);
   	  generate_tests("<M.header.name>");
   	  
   	  //println("Overloading resolution:");
   	  //for(str fuid <- overloadingResolver) {
   	  //	println("Resolver: <fuid> - <overloadingResolver[fuid]>");
   	  //	println("	Overloaded functions");
   	  //	for(int uid <- overloadedFunctions[overloadingResolver[fuid]]) {
   	  //		println("		<uid> - <fuid2str[uid]>");
   	  //	}
   	  //}
   	  //
   	  //println("Uses");
   	  //for(l <- config.uses) {
   	  //	println("<l[0]> - <l[1]>");
   	  //}
   	  //
   	  //iprintln(functions_in_module);
   	  //
   	  //throw "Testing overloading";
   	  
   	  // Overloading resolution...	  
   	  lrel[str,list[str]] overloaded_functions = [ < (of.scopeIn in moduleNames) ? "" : of.scopeIn, 
   	  												[ fuid2str[fuid] | int fuid <- of.fuids, fuid notin defaultFunctions ] + [ fuid2str[fuid] | int fuid <- of.fuids, fuid in defaultFunctions ]
   	  											  > 
   	  													| tuple[str scopeIn,set[int] fuids] of <- overloadedFunctions ];
   	  
   	  return muModule("<M.header.name>", types, functions_in_module, variables_in_module, variable_initializations, overloadingResolver, overloaded_functions);
   	}
   } catch Java("ParseError","Parse error"): {
   	   throw "Syntax errors in module <moduleLoc>";
   } finally {
   	   println("r2mu: Cleaning up ...");
   	   resetR2mu();
   	   resetScopeExtraction();
   	   println("r2mu: Cleaned up!");
   }
   throw "r2mu: cannot come here!";
}

void translate(m: (Module) `<Header header> <Body body>`) {
	for( tl <- body.toplevels) translate(tl);
}
	
void translate(t: (Toplevel) `<Declaration decl>`) = translate(decl);

// Toplevel Declaration: variable

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
   	for(var <- variables){
   		variables_in_module += [muVariable("<var.name>")];
   		if(var is initialized) 
   		   variable_initializations +=  mkAssign("<var.name>", var@\loc, translate(var.initial));
   	}
}   	

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`) { throw("annotation"); }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { /* skip: translation has nothing to do here */ }

void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// FunctionDeclaration

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   { throw("abstract"); }

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  println("r2mu: Compiling <signature.name>");
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
 //TODO: keyword parameters
  tbody = translateFunction(signature.parameters.formals.formals, translate(expression));
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									nformals, getScopeSize(fuid), fd@\loc, tmods, ttags, tbody);
  
  if("test" in tmods){
  println("ftype = <ftype>");
     params = ftype.parameters;
     tests += muCallPrim("testreport_add", [muCon(fuid), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  println("r2mu: Compiling <signature.name>");
  ftype = getFunctionType(fd@\loc);    
  nformals = size(ftype.parameters);
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  //TODO: keyword parameters
  MuExp tbody = translateFunction(signature.parameters.formals.formals, muBlock([ translate(stat) | stat <- body.statements ]));
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  tuple[str fuid,int pos] addr = uid2addr[uid];
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									nformals, getScopeSize(fuid), fd@\loc, translateModifiers(signature.modifiers), translateTags(tags), tbody); 
}

//str translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions> ;`)   { throw("conditional"); }


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
        m[name] = "<tg.contents>";
     else if (tg is empty)
        m[name] = "";
     else
        m[name] = "<tg.expression>";
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

void generate_tests(str module_name){
   code = muBlock([ muCallPrim("testreport_open", []), *tests, muCallPrim("testreport_close", []), muReturn() ]);
   ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
   main_testsuite = getFUID(module_name,"testsuite",ftype,0);
   println("main_testsuite = <main_testsuite>");
   functions_in_module += muFunction(main_testsuite, ftype, "" /*in the root*/, 1, 1, |rascal:///|, [], (), code);
}
