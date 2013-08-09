@bootstrapParser
module experiments::CoreRascal::Translation::RascalModule

import lang::rascal::\syntax::Rascal;
import Prelude;
import util::Reflective;
import util::ValueUI;
import ParseTree;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import experiments::CoreRascal::Translation::RascalExpression;
import experiments::CoreRascal::Translation::RascalStatement;

import experiments::CoreRascal::muRascal::AST;

list[loc] libSearchPath = [|std:///|, |eclipse-std:///|];

loc Example1 = |std:///experiments/CoreRascal/Translation/Examples/Example1.rsc|;


MuModule r2mu(loc moduleLoc){
   try {
   	Module M = parseModule(moduleLoc, libSearchPath);
   	config = checkModule(M.top, newConfiguration());  // .top is needed to remove start! Ugly!
   	//text(config);
   	extractScopes();
   	errors = [ e | e:error(_,_) <- config.messages];
   	if(size(errors) > 0)
   	  println("Module contains errors:\n<for(e <- errors){><e>\n<}>");
   	else {
   	  defs = translate(M.top);
   	  return muModule("<M.top.header.name>", defs, muEmpty());
   	  }
   	} catch Java("ParseError","Parse error"): {
   	    println("Syntax errors in module <Example1>");
   	} 
}

list[MuDefinition] translate(m: (Module) `<Header header> <Body body>`) =
    [ *translate(tl) | tl <- body.toplevels];
  
list[MuDefinition] translate(t: (Toplevel) `<Declaration decl>`) = translate(decl);

// Toplevel Declaration: variable

list[MuExp] translate(d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
	return
   		for(var <- variables){
   			if(var is initialized) 
   				append mkAssign("<var.name>",var@\loc, translate(var.initial));
   		}
}   	

str translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`) { throw("annotation"); }
str translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { throw("alias"); }
str translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }
str translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  { throw("dataAbstract"); }
/*
	| @Foldable \data : Tags tags Visibility visibility "data" UserType user CommonKeywordParameters commonKeywordParameters"=" {Variant "|"}+ variants ";"
*/

list[MuDefinition] translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// FunctionDeclaration

str translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   { throw("abstract"); }

list[MuDefinition] translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  ftypes = getFunctionType("<signature.name>");
  if({ ftype } := ftypes){
	  formals = signature.parameters.formals.formals;
	  lformals = [f | f <- formals];
	  scope = getFunctionScope("<signature.name>");
	  return [muFunction("<signature.name>", scope, size(lformals), getScopeSize(scope), [muReturn(translate(expression)[0])])];
  } else
      throw "overloaded function <signature.name>: <ftypes>";
}

list[MuDefinition] translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  ftypes = getFunctionType("<signature.name>");
  if({ ftype } := ftypes){
	  formals = signature.parameters.formals.formals;
	  lformals = [f | f <- formals];
	  tbody = [*translate(stat) | stat <- body.statements ];
	  scope = getFunctionScope("<signature.name>");
	  return [ muFunction("<signature.name>", scope, size(lformals), getScopeSize(scope), tbody) ];
  } else
      throw "overloaded function <signature.name>: <ftypes>"; 
}

str translateFun( Signature signature, str body){
  ftypes = getFunctionType("<signature.name>");
  if({ ftype } := ftypes){
	  formals = signature.parameters.formals.formals;
	  lformals = [f | f <- formals];
	  tformals = [(Pattern) `<Type tp> <Name nm>` := lformals[i] ? mkVar("<nm>",nm@\loc) : "pattern"  | i <- index(lformals)];
	  tbody = "<for(stat <- body.statements){><translate(stat)>;<}>";
	  return "\n// <fd>\n<mkVar("<signature.name>",fd@\loc)> = lambda([<intercalate(", ", tformals)>]){<tbody>}";
  } else
      throw "overloaded function <signature.name>: <ftypes>"; 

}

str translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions> ;`)   { throw("conditional"); }


/* withThrows: FunctionModifiers modifiers Type type  Name name Parameters parameters "throws" {Type ","}+ exceptions */

str translate(Signature s:(Signature) `<FunctionModifiers modifiers> <Type \type> <Name name> <Parameters parameters>`){
  formals = parameters.formals.formals;
  //keywordFormals = parameters.keywordFormals;
  return intercalate(", ", [(Pattern) `<Type tp> <Name nm>` := f ? "var(\"<nm>\", <tp>)" : "pattern" | f <- formals]);
}
