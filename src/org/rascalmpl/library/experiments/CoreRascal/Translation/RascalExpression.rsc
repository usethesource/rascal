@bootstrapParser
module experiments::CoreRascal::Translation::RascalExpression

import Prelude;

import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

import experiments::CoreRascal::Translation::RascalPattern;

public Configuration config = newConfiguration();

// Get the type of an expression
Symbol getType(loc l) = config.locationTypes[l];

str getType(Expression e) = "<getType(e@\loc)>";

// Get the outermost type constructor of an expression
str getOuterType(Expression e) = "<getName(getType(e@\loc))>";

// Get the type of a declared function
set[Symbol] getFunctionType(str name) { 
   r = config.store[config.fcvEnv[RSimpleName(name)]].rtype;
   println("r = <r>"); 
   return overloaded(alts) := r ? alts : {r};
}

// Get the type of a declared function
int getVariableScope(str name) = config.store[config.fcvEnv[RSimpleName(name)]].containedIn;



// Generate code for completely type-resolved operators

str infix(str op, Expression e) = "<op>_<getOuterType(e.lhs)>_<getOuterType(e.rhs)>(<translate(e.lhs)>, <translate(e.rhs)>)";
str prefix(str op, Expression arg) = "<op>_<getOuterType(arg)>(<translate(arg)>)";
str postfix(str op, Expression arg) = "<op>_<getOuterType(arg)>(<translate(arg)>)";

/*********************************************************************/
/*                  Expessions                                       */
/*********************************************************************/

str translate(e:(Expression) `{ <Statement+ statements> }`)  { throw("nonEmptyBlock"); }

str translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

str translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`)  { throw("closure"); }

str translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) { throw("stepRange"); }

str translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) { throw("voidClosure"); }

str translate (e:(Expression) `<Label label> <Visit \visit>`) { throw("visit"); }

str translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) { throw("reducer"); }

str translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }

str translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
  // ignore kw arguments for the moment
   return "call(<translate(expression)>, [<intercalate(", ", [translate(a) | a <- arguments])>])";
}

// literals
str translate((BooleanLiteral) `<BooleanLiteral b>`) = "<b>" == true ? "\\true())" : "\\false())";
str translate((Expression) `<BooleanLiteral b>`) = translate(b);
 
str translate((IntegerLiteral) `<IntegerLiteral n>`) = "number(<n>)";
str translate((Expression) `<IntegerLiteral n>`) = translate(n);
 
str translate((StringLiteral) `<StringLiteral s>`) = "strCon(<s>)";
str translate((Expression) `<StringLiteral s>`) = translate(s);

str translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) { throw("any"); }

str translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) { throw("all"); }

str translate (e:(Expression) `<Comprehension comprehension>`) { throw("comprehension"); }

str translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) {
    elems = [ translate(elem) | elem <- es ];
    return "mkSet(<getType(e)>, [<intercalate(",", elems)>])";
}

str translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) {
    elems = [ translate(elem) | elem <- es ];
    return "mkList(<getType(e)>, [<intercalate(",", elems)>])";
}

str translate (e:(Expression) `# <Type \type>`) { throw("reifyType"); }

str translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) { throw("range"); }

str translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) { throw("tuple"); }

str translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) { throw("map"); }

str translate (e:(Expression) `it`) { throw("it"); }
 
str translate((QualifiedName) `<QualifiedName v>`) = "var(\"<v>\",  <getType(v@\loc)>)";
str translate((Expression) `<QualifiedName v>`) = translate(v);

str translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    op = "subscript_<getOuterType(exp)>_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    return "<op>(<translate(exp)>,<intercalate(", ", [translate(s) | s <- subscripts])>)";
}

str translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) { throw("slice"); }

str translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) { throw("sliceStep"); }

str translate (e:(Expression) `<Expression expression> . <Name field>`) { throw("fieldAccess"); }

str translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) { throw("fieldUpdate"); }

str translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) { throw("fieldProject"); }

str translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression \value> ]`) { throw("setAnnotation"); }

str translate (e:(Expression) `<Expression expression> @ <Name name>`) { throw("getAnnotation"); }

str translate (e:(Expression) `<Expression expression> is <Name name>`) { throw("is"); }

str translate (e:(Expression) `<Expression expression> has <Name name>`) { throw("has"); }

str translate(e:(Expression) `<Expression argument> +`)   = postfix("transitiveClosure", argument);

str translate(e:(Expression) `<Expression argument> *`)   = postfix("transitiveReflexiveClosure", argument);

str translate(e:(Expression) `<Expression argument> ?`)   { throw("isDefined"); }

str translate(e:(Expression) `!<Expression argument>`)    = prefix("negation", argument);

str translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

str translate(e:(Expression) `*<Expression argument>`)    { throw("splice"); }

str translate(e:(Expression) `[ <Type \type> ] <Expression argument>`)  { throw("asType"); }

str translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix("composition", e);

str translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

str translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

str translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

str translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("division", e);

str translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersection", e);

str translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("addition", e);

str translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtraction", e);

str translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("appendAfter", e);

str translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("insertBefore", e);

str translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("modulo", e);

str translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix("notIn", e);

str translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix("in", e);

str translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greaterThanOrEq", e);

str translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lessThanOrEq", e);

str translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("lessThan", e);

str translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greaterThan", e);

str translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = infix("equals", lhs, e);

str translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = infix("nonEquals", e);

str translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  { throw("ifDefinedOtherwise"); }

str translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  { throw("noMatch"); }

str translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e)  + ".start().resume()";

str translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`)     { throw("enumerator"); }

str translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e) + ".start().resume()";

str translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e) + ".start().resume()";

str translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e) + ".start().resume()";

str translate(e:(Expression) `<Expression lhs> || <Expression rhs>`)  = translateBool(e) + ".start().resume()";

str translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = 
    backtrackFree(condition) ?  "c = <translate(condition)>;
		 			            'if(c == \\true())
		 			            '   <translate(thenExp)>;
		 			            'else
		 			            '   <translate(elseExp)>;
		 			            '"
		 			         :  "c = <translateBool(condition)>.start();
		       			        'if(c.resume())
		       			        '    <translate(thenExp)>;
		 			            'else
		 			            '   <translate(elseExp)>;
		 			            '";    

default str translate(Expression e) = "default for Expression: <e>";


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

// Translate Boolean operators
str translateBool(e:(Expression) `<Expression lhs> && <Expression rhs>`) =
 backtrackFree(lhs) ?
   (backtrackFree(rhs) ? "coroutine () resume bool () { yield infix(\"and\", e); }"
                       : "coroutine () resume bool () {
		                 '  lhsAnd = <translate(lhs)>;
		 			     '  if(lhsAnd == \\true()){
		 			     '     rhsAnd = <translateBool(rhs)>.start();
		 			     '     while(rhsAnd.hasMore()){
		       			 '        if(rhsAnd.resume())
		       			 '           yield true;
		       		     '  }
		       		     '  return false;
		       		     '}")
		       		   :
   (backtrackFree(rhs) ? "coroutine () resume bool (){
                         '  lhsAnd = <translateBool(lhs)>.start();
		 			     '  rhsAnd = <translate(rhs)>;
		 			     '  while (lhsAnd.hasMore()){
		                 '     if(lhsAnd.resume())
		                 '        if(rhsAnd) 
		                 '           yield true;
		                 '        else
		                 '           return false;
		                 '  }
		                 '  return false;
		                 '}"
		               : "coroutine () resume bool () {
		                 '  lhsAnd = <translateBool(lhs)>.start();
		                 '  rhsAnd = <translateBool(rhs)>.start();
		                 '  while (lhsAnd.hasMore()){
		                 '     if(lhsAnd.resume()){
		                 '        while(rhsAnd.hasMore()){
		       			 '          if(rhsAnd.resume())
		       			 '             yield true;
		       		     '        }
		                 '     } else 
		       		     '       return false;
		                 '  }
		                 '  return false;
                         '}");
 
 // similar for or, and, not and other Boolean operators
 
 // Translate match operator
 str translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`)  =
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
 
 