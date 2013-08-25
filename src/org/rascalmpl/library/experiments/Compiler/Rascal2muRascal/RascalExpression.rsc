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
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TypeUtils;

int size_exps({Expression ","}* es) = size([e | e <- es]);	// TODO: should become library function

// Generate code for completely type-resolved operators

bool isContainerType(str t) = t in {"list", "map", "set"};

list[MuExp] infix(str op, Expression e){
  lot = getOuterType(e.lhs);
  rot = getOuterType(e.rhs);
  if(isContainerType(lot))
     if(isContainerType(rot))
       return [muCallPrim("<op>_<lot>_<rot>", [*translate(e.lhs), *translate(e.rhs)])];
     else
       return [muCallPrim("<op>_<lot>_elm", [*translate(e.lhs), *translate(e.rhs)])];
  else
    if(isContainerType(rot))
       return [muCallPrim("<op>_elm_<rot>", [*translate(e.lhs), *translate(e.rhs)])];
     else
       return [muCallPrim("<op>_<lot>_<rot>", [*translate(e.lhs), *translate(e.rhs)])];
}
 
list[MuExp] prefix(str op, Expression arg) = [muCallPrim("<op>_<getOuterType(arg)>", translate(arg))];
list[MuExp] postfix(str op, Expression arg) = [muCallPrim("<op>_<getOuterType(arg)>", translate(arg))];

list[MuExp] eq_neq(str op, Expression e) = [muCallPrim("<op>", [*translate(e.lhs), *translate(e.rhs)])];

/*********************************************************************/
/*                  Expessions                                       */
/*********************************************************************/

// literals

list[MuExp] translate((Literal) `<BooleanLiteral b>`) = [ "<b>" == "true" ? muCon(true) : muCon(false) ];
 
list[MuExp] translate((Literal) `<IntegerLiteral n>`) = [muCon(toInt("<n>"))];

default list[MuExp] translate((Literal) `<Literal s>`) =  [ muCon(readTextValueString("<s>")) ];

list[MuExp] translate(e:(Expression)  `<Literal s>`) = translate(s);

// Other expressions

list[MuExp] translate(e:(Expression) `{ <Statement+ statements> }`) = [*translate(stat) | stat <- statements];

list[MuExp] translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

list[MuExp] translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) = translateClosure(e, parameters, statements);

list[MuExp] translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) = translateClosure(e, parameters, statements);

list[MuExp] translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`) =
    [ muMulti(muCreate(muFun("RANGE"), [ *translatePat(pat), *translate(first), *translate(last)])) ];
    
list[MuExp] translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`) =
     [ muMulti(muCreate(muFun("RANGE_STEP"), [  *translatePat(pat), *translate(first), *translate(second), *translate(last)])) ];

list[MuExp] translate (e:(Expression) `<Label label> <Visit \visit>`) { throw("visit"); }

list[MuExp] translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) { throw("reducer"); }

list[MuExp] translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }

list[MuExp] translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
   // ignore kw arguments for the moment
   MuExp receiver = translate(expression)[0];
   list[MuExp] args = [ *translate(a) | a <- arguments ];
   return [ muCall(receiver, args) ];
}

list[MuExp] translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = [ muOne([*translate(g) | g <- generators ]) ];

list[MuExp] translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) = [ muAll([*translate(g) | g <- generators ]) ];

list[MuExp] translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

list[MuExp] translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) {
    return [ muCallPrim("make_set", [ *translate(elem) | elem <- es ]) ];
}

list[MuExp] translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) =
    [ muCallPrim("make_list", [ *translate(elem) | elem <- es ]) ];

list[MuExp] translate (e:(Expression) `# <Type tp>`) = [muCon(symbolToValue(translateType(tp),config))];



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

list[MuExp] translate(e:(Expression) `!<Expression argument>`)    = translateBool(e);

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

list[MuExp] translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("addition", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("addition", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("modulo", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix("notIn", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix("in", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greater_equal", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("less_equal", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = eq_neq("equal", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = eq_neq("not_equal", e);

list[MuExp] translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  { throw("ifDefinedOtherwise"); }

list[MuExp] translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  { throw("noMatch"); }

list[MuExp] translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e);

list[MuExp] translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`) =
    [ muMulti(muCreate(muFun("ENUMERATE_AND_MATCH"), [*translatePat(pat), *translate(exp)])) ];

list[MuExp] translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e);

list[MuExp] translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e);

list[MuExp] translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e);

list[MuExp] translate(e:(Expression) `<Expression lhs> || <Expression rhs>`)  = translateBool(e);
 
list[MuExp] translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = 
    [ muIfelse(translate(condition)[0], translate(thenExp),  translate(elseExp)) ]; 

default list[MuExp] translate(Expression e) {
	throw "MISSING CASE FOR EXPRESSION: <e>";
}


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
  return [ muCallMuPrim("<fun>_<blhs>_<brhs>", [*translate(lhs), *translate(rhs)]) ];
}

list[MuExp] translateBool(str fun, Expression lhs){
  blhs = backtrackFree(lhs) ? "U" : "M";
  return [ muCallMuPrim("<fun>_<blhs>", translate(lhs)) ];
}

list[MuExp] translateBool(e:(Expression) `<Expression lhs> && <Expression rhs>`) = translateBool("AND", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `<Expression lhs> || <Expression rhs>`) = translateBool("OR", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) = translateBool("IMPLIES", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) = translateBool("EQUIVALENT", lhs, rhs);

list[MuExp] translateBool(e:(Expression) `! <Expression lhs>`) = translateBool("NOT", lhs);
 
// Translate match operator
 
 list[MuExp] translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`)  = 
   [ muMulti(muCreate(muFun("MATCH"), [*translatePat(pat), *translate(exp)])) ];
   
// Translate a closure   
 
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

// Translate a comprehension

list[MuExp] translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    name = nextTmp(); 
    return
    [ muAssignTmp(name, muCallPrim("make_listwriter", [])),
      muWhile(muAll([*translate(g) | g <-generators]), [muCallPrim("add_listwriter", [muTmp(name)] + [ *translate(r) | r <- results])]), 
      muCallPrim("done_listwriter", [muTmp(name)]) 
    ];
}
