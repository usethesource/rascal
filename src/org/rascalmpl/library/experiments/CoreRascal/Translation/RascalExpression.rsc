@bootstrapParser
module experiments::CoreRascal::Translation::RascalExpression

import Prelude;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

import experiments::CoreRascal::Translation::RascalPattern;

import experiments::CoreRascal::muRascal::AST;

public Configuration config = newConfiguration();
public map[int,tuple[int,int]] uid2addr = ();
public map[loc,int] loc2uid = ();

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

// Get the type of a declared function
set[Symbol] getFunctionType(str name) { 
   r = config.store[config.fcvEnv[RSimpleName(name)]].rtype; 
   return overloaded(alts) := r ? alts : {r};
}

int getFunctionScope(str name) = config.fcvEnv[RSimpleName(name)];

// Get the type of a declared function
tuple[int,int] getVariableScope(str name) = uid2addr[config.fcvEnv[RSimpleName(name)]];

MuExp mkVar(str name, loc l) {
  //println("mkVar: <name>");
  //println("l = <l>,\nloc2uid = <loc2uid>");
  n2a = uid2addr[loc2uid[l]];
  res = "<name>::<n2a[0]>::<n2a[1]>";
  //println("mkVar: <name> =\> <res>");
  return muVar(name, n2a[0], n2a[1]);
}

MuExp mkAssign(str name, loc l, MuExp exp) {
  //println("mkVar: <name>");
  //println("l = <l>,\nloc2uid = <loc2uid>");
  n2a = uid2addr[loc2uid[l]];
  res = "<name>::<n2a[0]>::<n2a[1]>";
  //println("mkVar: <name> =\> <res>");
  return muAssign(name, n2a[0], n2a[1], exp);
}

void extractScopes(){
   set[int] functionScopes = {};
   rel[int,int] containment = {};
   rel[int,int] declares = {};
   uid2addr = ();
   loc2uid = ();
   for(uid <- config.store){
      item = config.store[uid];
      switch(item){
        case function(_,_,_,inScope,_,src): { functionScopes += {uid}; 
                                              declares += {<inScope, uid>}; 
                                              containment += {<inScope, uid>}; 
                                              loc2uid[src] = uid;
                                              for(l <- config.uses[uid])
                                                  loc2uid[l] = uid;
                                             }
        case variable(_,_,_,inScope,src):   { declares += {<inScope, uid>}; loc2uid[src] = uid;
                                              for(l <- config.uses[uid])
                                                  loc2uid[l] = uid;
                                            }
        case blockScope(containedIn,src):   { containment += {<containedIn, uid>}; loc2uid[src] = uid;}
        case booleanScope(containedIn,src): { containment += {<containedIn, uid>}; loc2uid[src] = uid;}
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
   //for(uid <- uid2addr){
   //   println("<config.store[uid]> :  <uid2addr[uid]>");
  // }
   
   //for(l <- loc2uid)
   //    println("<l> : <loc2uid[l]>");
}



// Generate code for completely type-resolved operators



MuExp infix(str op, Expression e) = muCallPrim("<op>_<getOuterType(e.lhs)>_<getOuterType(e.rhs)>", translate(e.lhs), translate(e.rhs));
MuExp prefix(str op, Expression arg) = muCallPrim("<op>_<getOuterType(arg)>", translate(arg));
MuExp postfix(str op, Expression arg) = muCallPrim("<op>_<getOuterType(arg)>", translate(arg));

/*********************************************************************/
/*                  Expessions                                       */
/*********************************************************************/

MuExp translate(e:(Expression) `{ <Statement+ statements> }`)  { throw("nonEmptyBlock"); }

MuExp translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`)  { throw("closure"); }

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) { throw("stepRange"); }

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) { throw("voidClosure"); }

MuExp translate (e:(Expression) `<Label label> <Visit \visit>`) { throw("visit"); }

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) { throw("reducer"); }

MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
  // ignore kw arguments for the moment
   return muCall(translate(expression), [translate(a) | a <- arguments]);
}

// literals
MuExp translate((BooleanLiteral) `<BooleanLiteral b>`) = "<b>" == true ? constant(true) : constant(false);
MuExp translate((Expression) `<BooleanLiteral b>`) = translate(b);
 
MuExp translate((IntegerLiteral) `<IntegerLiteral n>`) = muConstant(toInt("<n>"));
MuExp translate((Expression) `<IntegerLiteral n>`) = translate(n);
 
MuExp translate((StringLiteral) `<StringLiteral s>`) = constant(s);
MuExp translate((Expression) `<StringLiteral s>`) = translate(s);

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) { throw("any"); }

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) { throw("all"); }

MuExp translate (e:(Expression) `<Comprehension comprehension>`) { throw("comprehension"); }

MuExp translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) {
    return callprim("make_set", [ translate(elem) | elem <- es ]);
}

MuExp translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) {
    return callprim("make_list", [ translate(elem) | elem <- es ]);
}

MuExp translate (e:(Expression) `# <Type \type>`) { throw("reifyType"); }

MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) { throw("range"); }

MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) { throw("tuple"); }

MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) { throw("map"); }

MuExp translate (e:(Expression) `it`) { throw("it"); }
 
MuExp translate((QualifiedName) `<QualifiedName v>`) = mkVar("<v>", v@\loc);
MuExp translate((Expression) `<QualifiedName v>`) = translate(v);

MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    op = "subscript_<getOuterType(exp)>_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    return callprim(op, [translate(s) | s <- subscripts]);
}

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) { throw("slice"); }

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) { throw("sliceStep"); }

MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) { throw("fieldAccess"); }

MuExp translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) { throw("fieldUpdate"); }

MuExp translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) { throw("fieldProject"); }

MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression \value> ]`) { throw("setAnnotation"); }

MuExp translate (e:(Expression) `<Expression expression> @ <Name name>`) { throw("getAnnotation"); }

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) { throw("is"); }

MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) { throw("has"); }

MuExp translate(e:(Expression) `<Expression argument> +`)   = postfix("transitiveClosure", argument);

MuExp translate(e:(Expression) `<Expression argument> *`)   = postfix("transitiveReflexiveClosure", argument);

MuExp translate(e:(Expression) `<Expression argument> ?`)   { throw("isDefined"); }

MuExp translate(e:(Expression) `!<Expression argument>`)    = prefix("negation", argument);

MuExp translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

MuExp translate(e:(Expression) `*<Expression argument>`)    { throw("splice"); }

MuExp translate(e:(Expression) `[ <Type \type> ] <Expression argument>`)  { throw("asType"); }

MuExp translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix("composition", e);

MuExp translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

MuExp translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

MuExp translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

MuExp translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("division", e);

MuExp translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersection", e);

MuExp translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("addition", e);

MuExp translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtraction", e);

MuExp translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("appendAfter", e);

MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("insertBefore", e);

MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("modulo", e);

MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix("notIn", e);

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix("in", e);

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greaterThanOrEq", e);

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lessThanOrEq", e);

MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("lessThan", e);

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greaterThan", e);

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = infix("equals", lhs, e);

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = infix("nonEquals", e);

MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  { throw("ifDefinedOtherwise"); }

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  { throw("noMatch"); }

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e)  + ".start().resume()";

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`)     { throw("enumerator"); }

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e) + ".start().resume()";

MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e) + ".start().resume()";

MuExp translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e) + ".start().resume()";
 
MuExp translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = 
    backtrackFree(condition) ?  muIfelse(translate(condition),
    								   translate(thenExp),
    								   translate(elseExp))
    					     :  muIfelse(next(init(translateBool(condition))),
    					     		   translate(thenExp),
    								   translate(elseExp));  

default MuExp translate(Expression e) = "\<\<MISSING CASE FOR EXPRESSION: <e>";


/*********************************************************************/
/*                  End of Expessions                                */
/*********************************************************************/

// Utilities for boolean operators
 
// Is an expression free of backtracking? 
bool backtrackFree(e:(Expression) `<Pattern pat> := <Expression exp>`) = false;
default bool backtrackFree(Expression e) = true;

// Get all variables that are introduced by a pattern.
tuple[set[tuple[str,str]],set[str]] getVars(Pattern p) {
  defs = {};
  uses = {};
  visit(p){
    case (Pattern) `<Type tp> <Name name>`: defs += <"<tp>", "<name>">;
    case (Pattern) `<QualifiedName name>`: uses += "<name>";
    case (Pattern) `*<QualifiedName name>`: uses += "<name>";
  };
  return <defs, uses>;
}

MuExp translateBool(str fun, Expression lhs, Expression rhs){
  blhs = backtrackFree(lhs) ? "n" : "b";
  brhs = backtrackFree(rhs) ? "n" : "b";
  return call("<fun>_<blhs>_<brhs>", translate(lhs), translate(rhs));
}

MuExp translateBool(e:(Expression) `<Expression lhs> && <Expression rhs>`) = translateBool("and", lhs, rhs);

MuExp translateBool(e:(Expression) `<Expression lhs> || <Expression rhs>`) = translateBool("or", lhs, rhs);

MuExp translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) = translateBool("implies", lhs, rhs);

MuExp translateBool(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) = translateBool("equivalent", lhs, rhs);


 // similar for or, and, not and other Boolean operators
 
 // Translate match operator
 MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`)  = [call("match", translatePat(pat), translate(exp))];
 /*
    "coroutine () resume bool(){
    '  matcher = <translatePat(pat)>;
    '  subject = <translate(exp)>;
    '  matcher.start(subject);
    '  while(matcher.hasMore()){
    '    if(matcher.resume())
    '       yield true;
    '    else
    '       return false;
    '  }
    '}";  
 */
 
 