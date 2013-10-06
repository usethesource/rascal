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

public list[loc] imported_modules = [];
public list[MuFunction] functions_in_module = [];
public list[MuVariable] variables_in_module = [];
public list[MuExp] variable_initializations = [];
public list[MuExp] tests = [];

public loc Library = |std:///experiments/Compiler/muRascal2RVM/Library.mu|;

public void resetR2mu() {
    imported_modules = [];
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
   	  imported_modules = [];
   	  functions_in_module = [];
   	  variables_in_module = [];
   	  variable_initializations = [];
   	  map[str,Symbol] types = ( fuid2str[uid] : \type | int uid <- config.store, 
   	  									   					constructor(name, Symbol \type, containedIn, at) := config.store[uid]
   	  													 || production(name, Symbol \type, containedIn, at) := config.store[uid]
   	  						  );
   	  translate(M);
   	 
   	  generate_tests("<M.header.name>");
   	  
   	  // Overloading resolution...	  
   	  lrel[str,list[str],list[str]] overloaded_functions = [ < (of.scopeIn in moduleNames) ? "" : of.scopeIn, 
   	  														   [ fuid2str[fuid] | int fuid <- of.fuids, (fuid in functions) && (fuid notin defaultFunctions) ] 
   	  														   		+ [ fuid2str[fuid] | int fuid <- of.fuids, fuid in defaultFunctions ],
   	  														   [ fuid2str[fuid] | int fuid <- of.fuids, fuid in constructors ]
   	  											  			 > 
   	  															| tuple[str scopeIn,set[int] fuids] of <- overloadedFunctions ];
   	  
   	  return muModule("<M.header.name>", imported_modules, types, functions_in_module, variables_in_module, variable_initializations, overloadingResolver, overloaded_functions);
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
    //println("name = <name>");
    imported_modules += |std:///| + ("<name>" + ".rsc");
    //println("imported_modules = <imported_modules>");
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

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`) { /*skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { /* skip: translation has nothing to do here */ }

void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// FunctionDeclaration

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  println("r2mu: Compiling <signature.name>");
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
 //TODO: keyword parameters
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  if(ttags["javaClass"]?){
     paramTypes = \tuple([param | param <- ftype.parameters]);
     params = [ muLoc("<ftype.parameters[i]>", i) | i <- [ 0 .. nformals] ];
     exp = muCallJava("<signature.name>", ttags["javaClass"], paramTypes, params);
     tbody = translateFunction(signature.parameters.formals.formals, exp, []);
    
     functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									nformals, getScopeSize(fuid), fd@\loc, tmods, ttags, tbody);
  } else {
    println("r2mu: <fuid> ignored");
  }
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  println("r2mu: Compiling <signature.name>");
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
 //TODO: keyword parameters
  tbody = translateFunction(signature.parameters.formals.formals, expression, []);
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									nformals, getScopeSize(fuid), fd@\loc, tmods, ttags, tbody);
  
  if("test" in tmods){
     params = ftype.parameters;
     tests += muCallPrim("testreport_add", [muCon(fuid), muCon(fd@\loc)] + [ muTypeCon(\tuple([param | param <- params ])) ]);
     // Maybe we should still transfer the reified type
     //tests += muCallPrim("testreport_add", [muCon(fuid), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
  
  leaveFunctionScope();
  
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  println("r2mu: Compiling <signature.name>");
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  
 //TODO: keyword parameters
  tbody = translateFunction(signature.parameters.formals.formals, expression, [exp | exp <- conditions]);
  tmods = translateModifiers(signature.modifiers);
  ttags =  translateTags(tags);
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									nformals, getScopeSize(fuid), fd@\loc, tmods, ttags, tbody);
  
  if("test" in tmods){
     params = ftype.parameters;
     tests += muCallPrim("testreport_add", [muCon(fuid), muCon(fd@\loc)] + [ muTypeCon(\tuple([param | param <- params ])) ]);
     // Maybe we should still transfer the reified type
     //tests += muCallPrim("testreport_add", [muCon(fuid), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]), config)) ]);
  }
  
  leaveFunctionScope();
  
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  println("r2mu: Compiling <signature.name>");
  ftype = getFunctionType(fd@\loc);    
  nformals = size(ftype.parameters);
  bool isVarArgs = (varArgs(_,_) := signature.parameters);
  //TODO: keyword parameters
  MuExp tbody = translateFunction(signature.parameters.formals.formals, body.statements, []);
  uid = loc2uid[fd@\loc];
  fuid = uid2str(uid);
  
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									nformals, getScopeSize(fuid), fd@\loc, translateModifiers(signature.modifiers), translateTags(tags), tbody);
  									
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

void generate_tests(str module_name){
   code = muBlock([ muCallPrim("testreport_open", []), *tests, muReturn(muCallPrim("testreport_close", [])) ]);
   ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
   name_testsuite = "<module_name>_testsuite";
   main_testsuite = getFUID(name_testsuite,name_testsuite,ftype,0);
   functions_in_module += muFunction(main_testsuite, ftype, "" /*in the root*/, 1, 1, |rascal:///|, [], (), code);
}
