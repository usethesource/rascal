@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalModule

import lang::rascal::\syntax::Rascal;
import Prelude;
import util::Reflective;
import util::ValueUI;
import ParseTree;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TypeUtils;


public list[MuFunction] functions_in_module = [];
public list[MuVariable] variables_in_module = [];
public list[MuExp] variable_initializations = [];
public int tmpVar = -1;

public void resetR2mu() {
	functions_in_module = [];
	variables_in_module = [];
	variable_initializations = [];
	tmpVar = -1;
}

public str nextTmp(){
	tmpVar += 1;
    return "TMP<tmpVar>";
}

// Compile a Rascal source module (given as string) to muRascal

MuModule r2mu(str moduleStr){
	return r2mu(parse(#start[Module], moduleStr).top); // .top is needed to remove start! Ugly!
}

// Compile a Rascal source module (given at a location) to muRascal

MuModule r2mu(loc moduleLoc){
    println(readFile(moduleLoc));
   	return r2mu(parse(#start[Module], moduleLoc).top); // .top is needed to remove start! Ugly!
}

// Compile a parsed Rascal source module to muRascal

MuModule r2mu(lang::rascal::\syntax::Rascal::Module M){
   try {
   	Configuration c = newConfiguration();
   	config = checkModule(M, c);  
   	extractScopes();
   	errors = [ e | e:error(_,_) <- config.messages];
   	warnings = [ w | w:warning(_,_) <- config.messages ];
   	if(size(errors) > 0) {
   	  for(e <- errors) {
   	  	println(e);
   	  }
   	  throw "Module contains errors!";
   	} else {
   	  if(size(warnings) > 0) {
   	  	for(w <- warnings) {
   	  		println(w);
   	  	}
   	  }
   	  functions_in_module = [];
   	  variables_in_module = [];
   	  variable_initializations = [];
   	  list[Symbol] types = [ \type | int uid <- config.store, 
   	  									constructor(name, Symbol \type, containedIn, at) := config.store[uid]
   	  									|| production(name, Symbol \type, containedIn, at) := config.store[uid]
   	  									|| datatype(name, Symbol \type, containedIn, ats) := config.store[uid]
   	  									|| sorttype(name, Symbol \type, containedIn, ats) := config.store[uid]
   	  									|| \alias(name, Symbol \type, containedIn, at) := config.store[uid] ];
   	  translate(M);
   	  return muModule("<M.header.name>", types, functions_in_module, variables_in_module, variable_initializations);
   	  }
   	} catch Java("ParseError","Parse error"): {
   	    throw "Syntax errors in module <moduleLoc>";
   	} finally {
   		println("r2mu: Cleaning up ...");
   		resetR2mu();
   		resetScopeExtraction();
   	}
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
   		   variable_initializations +=  mkAssign("<var.name>", var@\loc, translate(var.initial)[0]);
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
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  scope = loc2uid[fd@\loc];
  tbody = translate(expression);
  functions_in_module += [muFunction("<signature.name>", scope, nformals, getScopeSize(scope), [*tbody[0 .. -1], muReturn(tbody[-1])])];
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  ftype = getFunctionType(fd@\loc);    
  nformals = size(ftype.parameters);
  tbody = [ *translate(stat) | stat <- body.statements ];
  scope = loc2uid[fd@\loc];
  functions_in_module += [muFunction("<signature.name>", scope, nformals, getScopeSize(scope), tbody)]; 
}

str translateFun(FunctionDeclaration fd, Signature signature, FunctionBody body){
  ftype = getFunctionType(fd@\loc);
  formals = signature.parameters.formals.formals;
  lformals = [f | f <- formals];
  tformals = [(Pattern) `<Type tp> <Name nm>` := lformals[i] ? mkVar("<nm>",nm@\loc) : "pattern"  | i <- index(lformals)];
  tbody = "<for(stat <- body.statements){><translate(stat)>;<}>";
  return "\n// <fd>\n<mkVar("<signature.name>",fd@\loc)> = lambda([<intercalate(", ", tformals)>]){<tbody>}";
}

str translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions> ;`)   { throw("conditional"); }


/* withThrows: FunctionModifiers modifiers Type type  Name name Parameters parameters "throws" {Type ","}+ exceptions */

str translate(Signature s:(Signature) `<FunctionModifiers modifiers> <Type \type> <Name name> <Parameters parameters>`){
  formals = parameters.formals.formals;
  //keywordFormals = parameters.keywordFormals;
  return intercalate(", ", [(Pattern) `<Type tp> <Name nm>` := f ? "var(\"<nm>\", <tp>)" : "pattern" | f <- formals]);
}
