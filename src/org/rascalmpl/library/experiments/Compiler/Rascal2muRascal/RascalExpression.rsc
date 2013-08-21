@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalExpression

import Prelude;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalStatement;

import experiments::Compiler::muRascal::AST;

public Configuration config = newConfiguration();

public map[int,tuple[int,int]] uid2addr = ();
public map[loc,int] loc2uid = ();

public set[int] functionScopes = {};
public set[int] constructorScopes = {};
public set[int] variableScopes = {};

public void resetScopeExtraction() {
	uid2addr = ();
	loc2uid = ();
	functionScopes = {};
	constructorScopes = {};
	variableScopes = {};
}

// Get the type of an expression
Symbol getType(loc l) = config.locationTypes[l];

str getType(Expression e) = "<getType(e@\loc)>";

// Get the outermost type constructor of an expression
str getOuterType(Expression e) {
 tp = "<getName(getType(e@\loc))>";
 if(tp in {"int", "real", "rat"})
 	tp = "num";
 return tp;
}

/* 
* CHANGED: 
* Getting a function type by name is problematic in case of nested functions,
* as fcvEnv does not contain nested functions;
* Additionally, it does not allow getting types of functions that build an overloaded function;   
*/
// Get the type of a declared function
set[Symbol] getFunctionType(str name) { 
   r = config.store[config.fcvEnv[RSimpleName(name)]].rtype; 
   return overloaded(alts) := r ? alts : {r};
}

// Alternatively, the type of a function can be looked up by @loc
Symbol getFunctionType(loc l) { 
   int uid = loc2uid[l];
   fun = config.store[uid];
   if(function(_,Symbol rtype,_,_,_,_) := fun) {
       return rtype;
   } else {
       throw "Looked up a function, but got: <fun> instead";
   }
}

Symbol getClosureType(loc l) {
   int uid = loc2uid[l];
   cls = config.store[uid];
   if(closure(Symbol rtype,_,_) := cls) {
       return rtype;
   } else {
       throw "Looked up a closure, but got: <cls> instead";
   }
}

int getFunctionScope(str name) = config.fcvEnv[RSimpleName(name)];

int getScopeSize(int scope){
  int n = 0;
  for(<scope, int pos> <- range(uid2addr))
    n += 1;
  return n;
}

// Get the type of a declared function
//tuple[int,int] getVariableScope(str name) = uid2addr[config.fcvEnv[RSimpleName(name)]];

MuExp mkVar(str name, loc l) {
  //println("mkVar: <name>");
  //println("l = <l>,\nloc2uid = <loc2uid>");
  
  tuple[int scope, int pos] addr = uid2addr[loc2uid[l]];
  
  res = "<name>::<addr.scope>::<addr.pos>";
  println("mkVar: <name> =\> <res>; isFun: <loc2uid[l] in functionScopes>; isConstr: <loc2uid[l] in constructorScopes>");
  
  if(loc2uid[l] in functionScopes) {
  	// distinguishes between root and nested scopes
  	return (addr.scope == 0) ? muFun(name) : muFun(name, addr.scope);
  }
  if(loc2uid[l] in constructorScopes) {
  	return muConstr(name);
  }
  return muVar(name, addr.scope, addr.pos);
}

tuple[int,int] getVariableScope(str name, loc l) {
  return uid2addr[loc2uid[l]];
}


/* */

MuExp mkAssign(str name, loc l, MuExp exp) {
  println("mkAssign: <name>");
  println("l = <l>,\nloc2uid = <loc2uid>");
  addr = uid2addr[loc2uid[l]];
  res = "<name>::<addr[0]>::<addr[1]>";
  //println("mkVar: <name> =\> <res>");
  return muAssign(name, addr[0], addr[1], exp);
}

void extractScopes(){
   rel[int,int] containment = {};
   rel[int,int] declares = {};
   uid2addr = ();
   loc2uid = ();
   for(uid <- config.store){
      item = config.store[uid];
      switch(item){
        case function(_,_,_,inScope,_,src): { 
        									  functionScopes += {uid}; 
                                              declares += {<inScope, uid>}; 
                                              // containment += {<inScope, uid>}; 
                                              loc2uid[src] = uid;
                                              for(l <- config.uses[uid])
                                                  loc2uid[l] = uid;
                                            }
        case variable(_,_,_,inScope,src):   { 
        									  variableScopes += {uid};
        									  declares += {<inScope, uid>}; 
        									  loc2uid[src] = uid;
                                              for(l <- config.uses[uid])
                                                  loc2uid[l] = uid;
                                            }
        case constructor(_,_,inScope,src):  { 
        									  constructorScopes += {uid};
        									  declares += {<inScope, uid>};
        									  loc2uid[src] = uid;
        									  for(l <- config.uses[uid])
        									      loc2uid[l] = uid;
        									}
        case blockScope(containedIn,src):   { containment += {<containedIn, uid>}; loc2uid[src] = uid;}
        case booleanScope(containedIn,src): { containment += {<containedIn, uid>}; loc2uid[src] = uid;}
        
        case closure(_,inScope,src):        {
                                              functionScopes += {uid};
                                              declares += {<inScope, uid>};
        									  loc2uid[src] = uid;
        									}
      }
    }
    //println("containment = <containment>");
    //println("functionScopes = <functionScopes>");
    //println("declares = <declares>");
   
    containmentPlus = containment+;
    //println("containmentPlus = <containmentPlus>");
    
    topdecls = toList(declares[0]);
    //println("topdecls = <topdecls>");
    for(i <- index(topdecls)){
            uid2addr[topdecls[i]] = <0, i>;
    }
    for(fuid <- functionScopes){
        innerScopes = {fuid} + containmentPlus[fuid];
        decls = toList(declares[innerScopes]);
        //println("Scope <fuid> has inner scopes = <innerScopes>");
        //println("Scope <fuid> declares <decls>");
        for(i <- index(decls)){
            uid2addr[decls[i]] = <fuid, i>;
        }
    }
    println("uid2addr:");
   for(uid <- uid2addr){
      println("<config.store[uid]> :  <uid2addr[uid]>");
   }
   
   println("loc2uid:");
   for(l <- loc2uid)
       println("<l> : <loc2uid[l]>");
}


int size_exps({Expression ","}* es) = size([e | e <- es]);	// TODO: should become library function



// Generate code for completely type-resolved operators



list[MuExp] infix(str op, Expression e) = [muCallPrim("<op>_<getOuterType(e.lhs)>_<getOuterType(e.rhs)>", [*translate(e.lhs), *translate(e.rhs)])];
list[MuExp] prefix(str op, Expression arg) = [muCallPrim("<op>_<getOuterType(arg)>", translate(arg))];
list[MuExp] postfix(str op, Expression arg) = [muCallPrim("<op>_<getOuterType(arg)>", translate(arg))];

/*********************************************************************/
/*                  Expessions                                       */
/*********************************************************************/

list[MuExp] translate(e:(Expression) `{ <Statement+ statements> }`)  { throw("nonEmptyBlock"); }

list[MuExp] translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

list[MuExp] translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) = translateClosure(e, parameters, statements);

list[MuExp] translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) = translateClosure(e, parameters, statements);

list[MuExp] translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) { throw("stepRange"); }

list[MuExp] translate (e:(Expression) `<Label label> <Visit \visit>`) { throw("visit"); }

list[MuExp] translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) { throw("reducer"); }

list[MuExp] translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }

list[MuExp] translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
   // ignore kw arguments for the moment
   MuExp receiver = translate(expression)[0];
   list[MuExp] args = [ *translate(a) | a <- arguments ];
   return [ muCall(receiver, args) ];
}

// literals
list[MuExp] translate((BooleanLiteral) `<BooleanLiteral b>`) = [ "<b>" == "true" ? muCon(true) : muCon(false) ];
list[MuExp] translate((Expression) `<BooleanLiteral b>`) = translate(b);
 
list[MuExp] translate((IntegerLiteral) `<IntegerLiteral n>`) = [muCon(toInt("<n>"))];
list[MuExp] translate((Expression) `<IntegerLiteral n>`) = translate(n);
 

list[MuExp] translate((StringLiteral) `<StringLiteral s>`) = [ muCon("<s>") ];

list[MuExp] translate((Expression) `<StringLiteral s>`) = translate(s);

list[MuExp] translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) { throw("any"); }

list[MuExp] translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) { throw("all"); }

list[MuExp] translate (e:(Expression) `<Comprehension comprehension>`) { throw("comprehension"); }

list[MuExp] translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) {
    return [ muCallPrim("make_set", [ *translate(elem) | elem <- es ]) ];
}

list[MuExp] translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) =
    [ muCallPrim("make_list", [ *translate(elem) | elem <- es ]) ];

list[MuExp] translate (e:(Expression) `# <Type \type>`) { throw("reifyType"); }

list[MuExp] translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) { throw("range"); }

list[MuExp] translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) =
    [ muCallPrim("make_tuple", [ *translate(elem) | elem <- elements ]) ];

list[MuExp] translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) { throw("map"); }

list[MuExp] translate (e:(Expression) `it`) { throw("it"); }
 
list[MuExp] translate((QualifiedName) `<QualifiedName v>`) = [ mkVar("<v>", v@\loc) ];

list[MuExp] translate((Expression) `<QualifiedName v>`) = translate(v);

list[MuExp] translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    op = "subscript_<getOuterType(exp)>_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    return [ muCallPrim(op, [translate(s) | s <- subscripts]) ];
}

list[MuExp] translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) { throw("slice"); }

list[MuExp] translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) { throw("sliceStep"); }

list[MuExp] translate (e:(Expression) `<Expression expression> . <Name field>`) { throw("fieldAccess"); }

list[MuExp] translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) { throw("fieldUpdate"); }

list[MuExp] translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) { throw("fieldProject"); }

list[MuExp] translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression \value> ]`) { throw("setAnnotation"); }

list[MuExp] translate (e:(Expression) `<Expression expression> @ <Name name>`) { throw("getAnnotation"); }

list[MuExp] translate (e:(Expression) `<Expression expression> is <Name name>`) { throw("is"); }

list[MuExp] translate (e:(Expression) `<Expression expression> has <Name name>`) { throw("has"); }

list[MuExp] translate(e:(Expression) `<Expression argument> +`)   = postfix("transitiveClosure", argument);

list[MuExp] translate(e:(Expression) `<Expression argument> *`)   = postfix("transitiveReflexiveClosure", argument);

list[MuExp] translate(e:(Expression) `<Expression argument> ?`)   { throw("isDefined"); }

list[MuExp] translate(e:(Expression) `!<Expression argument>`)    = prefix("negation", argument);

list[MuExp] translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

list[MuExp] translate(e:(Expression) `*<Expression argument>`)    { throw("splice"); }

list[MuExp] translate(e:(Expression) `[ <Type \type> ] <Expression argument>`)  { throw("asType"); }

list[MuExp] translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix("composition", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("division", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersection", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("addition", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtraction", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("appendAfter", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("insertBefore", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("modulo", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix("notIn", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix("in", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greater_equal", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("less_equal", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = infix("equals", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = infix("nonEquals", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  { throw("ifDefinedOtherwise"); }

list[MuExp] translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  { throw("noMatch"); }

list[MuExp] translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e);

list[MuExp] translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`)     { throw("enumerator"); }

list[MuExp] translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e);

list[MuExp] translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e);
 
list[MuExp] translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = 
    [ muIfelse(translate(condition)[0], translate(thenExp),  translate(elseExp)) ]; 

default list[MuExp] translate(Expression e) = "\<\<MISSING CASE FOR EXPRESSION: <e>";


/*********************************************************************/
/*                  End of Expessions                                */
/*********************************************************************/

// Utilities for boolean operators
 
// Is an expression free of backtracking? 

bool backtrackFree(e:(Expression) `<Pattern pat> := <Expression exp>`) = backtrackFree(pat);
bool backtrackFree(e:(Expression) `<Pattern pat> \<- <Expression exp>`) = false;

default bool backtrackFree(Expression e) = true;


list[MuExp] translateBool(str fun, Expression lhs, Expression rhs){
  blhs = backtrackFree(lhs) ? "U" : "M";
  brhs = backtrackFree(rhs) ? "U" : "M";
  return [ muCall("<fun>_<blhs>_<brhs>", [*translate(lhs), *translate(rhs)]) ];
}

list[MuExp] translateBool(e:(Expression) `<Expression lhs> && <Expression rhs>`) = translateBool("AND", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `<Expression lhs> || <Expression rhs>`) = translateBool("OR", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) = translateBool("IMPLIES", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) = translateBool("EQUIVALENT", lhs, rhs);


 // similar for or, and, not and other Boolean operators
 
 // Translate match operator
 
 list[MuExp] translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`)  = 
   [ muMulti(muCreate(muFun("MATCH"), [*translatePat(pat), *translate(exp)])) ];
 
 list[MuExp] translateClosure(Expression e, Parameters parameters, Statement* statements) {
	scope = loc2uid[e@\loc];
    name = "closure_<scope>";
	ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	nlocals = getScopeSize(scope);
	body = [ *translate(stat) | stat <- statements ];
	functions_in_module += [ muFunction(name, scope, nformals, nlocals, body) ];
	tuple[int scope, int pos] addr = uid2addr[scope];
	return [ (addr.scope == 0) ? muFun(name) : muFun(name, addr.scope) ];
}

 
