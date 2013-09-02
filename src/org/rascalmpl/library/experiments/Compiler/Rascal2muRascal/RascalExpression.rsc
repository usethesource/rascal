@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalExpression

import Prelude;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;

import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeReifier;

import experiments::Compiler::muRascal::AST;

import experiments::Compiler::Rascal2muRascal::TypeUtils;


int size_exps({Expression ","}* es) = size([e | e <- es]);		// TODO: should become library function
int size_assignables({Assignable ","}+ es) = size([e | e <- es]);	// TODO: should become library function

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

list[MuExp] comparison(str op, Expression e) = [muCallPrim("<op>", [*translate(e.lhs), *translate(e.rhs)])];

/*********************************************************************/
/*                  Expessions                                       */
/*********************************************************************/

// literals

list[MuExp] translate((Literal) `<BooleanLiteral b>`) = [ "<b>" == "true" ? muCon(true) : muCon(false) ];
 
list[MuExp] translate((Literal) `<IntegerLiteral n>`) = [muCon(toInt("<n>"))];

default list[MuExp] translate((Literal) `<Literal s>`) =  [ muCon(readTextValueString("<s>")) ];

list[MuExp] translate(e:(Expression)  `<Literal s>`) = translate(s);

// Other expressions

// Block
list[MuExp] translate(e:(Expression) `{ <Statement+ statements> }`) = [*translate(stat) | stat <- statements];

// Parenthesized expression
list[MuExp] translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

// Closure
list[MuExp] translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) = translateClosure(e, parameters, statements);

list[MuExp] translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) = translateClosure(e, parameters, statements);

// Enumerator with range

list[MuExp] translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`) =
    [ muMulti(muCreate(mkCallToLibFun("Library", "RANGE", 3), [ *translatePat(pat), *translate(first), *translate(last)])) ];
    
list[MuExp] translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`) =
     [ muMulti(muCreate(mkCallToLibFun("Library", "RANGE_STEP", 4), [  *translatePat(pat), *translate(first), *translate(second), *translate(last)])) ];

// Visit
list[MuExp] translate (e:(Expression) `<Label label> <Visit \visit>`) = translateVisit(label, \visit);

// Reducer
list[MuExp] translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(init, result, generators);

// Reified type
list[MuExp] translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }

// Call
list[MuExp] translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
   // ignore kw arguments for the moment
   MuExp receiver = translate(expression)[0];
   list[MuExp] args = [ *translate(a) | a <- arguments ];
   return [ muCall(receiver, args) ];
}

// Any
list[MuExp] translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = [ muOne([*translate(g) | g <- generators ]) ];

// All
list[MuExp] translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) = [ muAll([*translate(g) | g <- generators ]) ];

// Comprehension
list[MuExp] translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

// Set
list[MuExp] translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) = translateSetOrList(es, "set");

// List
list[MuExp] translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`)  = translateSetOrList(es, "list");

// Reified type
list[MuExp] translate (e:(Expression) `# <Type tp>`) = [muCon(symbolToValue(translateType(tp),config))];

// Tuple
list[MuExp] translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) =
    [ muCallPrim("tuple_create", [ *translate(elem) | elem <- elements ]) ];

// Map
list[MuExp] translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) =
   [ muCallPrim("map_create", [ *translate(m.from), *translate(m.to) | m <- mappings ]) ];

// It in reducer
list[MuExp] translate (e:(Expression) `it`) = [ muTmp(topIt()) ];
 
 // Qualifid name
list[MuExp] translate((QualifiedName) `<QualifiedName v>`) = [ mkVar("<v>", v@\loc) ];

list[MuExp] translate((Expression) `<QualifiedName v>`) = translate(v);

// Subscript
list[MuExp] translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    if(ot notin {"list", "map"}) {
    	op = "subscript_<getOuterType(exp)>_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    return [ muCallPrim(op, translate(exp) + [*translate(s) | s <- subscripts]) ];
}

// Slice
list[MuExp] translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

list[MuExp] translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

// Field access
list[MuExp] translate (e:(Expression) `<Expression expression> . <Name field>`) =
    [ muCallPrim("<getOuterType(expression)>_field_access", [ *translate(expression), muCon("<field>") ]) ];

// Field update
list[MuExp] translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) =
    [ muCallPrim("<getOuterType(expression)>_field_update", [ *translate(expression), muCon("<key>"), *translate(replacement) ]) ];

// Field project
list[MuExp] translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) {
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon("<field>") | f <- fields];
    return [ muCallPrim("<getOuterType(expression)>_field_project", [ *translate(expression),*fcode]) ];
}

// setAnnotation
list[MuExp] translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression \value> ]`) { throw("setAnnotation"); }

// getAnnotation
list[MuExp] translate (e:(Expression) `<Expression expression> @ <Name name>`) { throw("getAnnotation"); }

// Is
list[MuExp] translate (e:(Expression) `<Expression expression> is <Name name>`) { throw("is"); }

// Has
list[MuExp] translate (e:(Expression) `<Expression expression> has <Name name>`) { throw("has"); }

// Transitive closure
list[MuExp] translate(e:(Expression) `<Expression argument> +`)   = postfix("transitiveClosure", argument);

// Transitive reflexive closure
list[MuExp] translate(e:(Expression) `<Expression argument> *`)   = postfix("transitiveReflexiveClosure", argument);

// isDefined?
list[MuExp] translate(e:(Expression) `<Expression argument> ?`)   { throw("isDefined"); }

// Not
list[MuExp] translate(e:(Expression) `!<Expression argument>`)    = translateBool(e);

// Negate
list[MuExp] translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

// Splice
list[MuExp] translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice cannot occur outside set or list";
}

// AsType
list[MuExp] translate(e:(Expression) `[ <Type \type> ] <Expression argument>`)  { throw("asType"); }

// Composition
list[MuExp] translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix("composition", e);

// Product
list[MuExp] translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

// Join
list[MuExp] translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

// Remainder
list[MuExp] translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

// Division
list[MuExp] translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("division", e);

// Intersection
list[MuExp] translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersection", e);

//Addition
list[MuExp] translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("addition", e);

// Subtraction
list[MuExp] translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtraction", e);

// Insert Before
list[MuExp] translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("addition", e);

// Append After
list[MuExp] translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("addition", e);

// Modulo
list[MuExp] translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("modulo", e);

// Notin
list[MuExp] translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix("notin", e);

// In
list[MuExp] translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix("in", e);

// Greater Equal
list[MuExp] translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greaterequal", e);

// Less Equal
list[MuExp] translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lessequal", e);

// Less
list[MuExp] translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);

// Greater
list[MuExp] translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);

// Equal
list[MuExp] translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = comparison("equal", e);

// NotEqual
list[MuExp] translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = comparison("notequal", e);

// IfDefinedOtherwise
list[MuExp] translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  { throw("ifDefinedOtherwise"); }

// NoMatch
list[MuExp] translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  { throw("noMatch"); }

// Match
list[MuExp] translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e);

// Enumerate
list[MuExp] translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`) =
    [ muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_AND_MATCH", 2), [*translatePat(pat), *translate(exp)])) ];

// Implies
list[MuExp] translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e);

// Equivalent
list[MuExp] translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e);

// And
list[MuExp] translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e);

// Or
list[MuExp] translate(e:(Expression) `<Expression lhs> || <Expression rhs>`)  = translateBool(e);
 
// Conditional Expression
list[MuExp] translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = 
    [ muIfelse(translate(condition)[0], translate(thenExp),  translate(elseExp)) ]; 

// Default: should not happen
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


// Translate Boolean expression

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
   [ muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [*translatePat(pat), *translate(exp)])) ];
   
// Auxiliary functions for translating various constructs
   
// Translate a closure   
 
 list[MuExp] translateClosure(Expression e, Parameters parameters, Statement* statements) {
 	uid = loc2uid[e@\loc];
	fuid = uid2str(uid);
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	nlocals = getScopeSize(fuid);
	body = [ *translate(stat) | stat <- statements ];
	functions_in_module += [ muFunction(fuid, nformals, nlocals, e@\loc, [], (), body) ];
	tuple[str fuid,int pos] addr = uid2addr[uid];
	return [ (addr.fuid == uid2str(0)) ? muFun(fuid) : muFun(fuid, addr.fuid) ];
}

// Translate a comprehension

list[MuExp] translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname);
    return
    [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])),
      muWhile(loopname, muAll([*translate(g) | g <-generators]), [muCallPrim("listwriter_add", [muTmp(tmp)] + [ *translate(r) | r <- results])]), 
      muCallPrim("listwriter_close", [muTmp(tmp)]) 
    ];
}

list[MuExp] translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    [ muAssignTmp(tmp, muCallPrim("setwriter_open", [])),
      muWhile(loopname, muAll([*translate(g) | g <-generators]), [muCallPrim("setwriter_add", [muTmp(tmp)] + [ *translate(r) | r <- results])]), 
      muCallPrim("setwriter_close", [muTmp(tmp)]) 
    ];
}

list[MuExp] translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    [ muAssignTmp(tmp, muCallPrim("mapwriter_open", [])),
      muWhile(loopname, muAll([*translate(g) | g <-generators]), [muCallPrim("mapwriter_add", [muTmp(tmp)] + [ *translate(from), *translate(to)])]), 
      muCallPrim("mapwriter_close", [muTmp(tmp)]) 
    ];
}

// Translate Reducer

list[MuExp] translateReducer(init, result, generators){
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    pushIt(tmp);
    code = [ muAssignTmp(tmp, translate(init)[-1]), muWhile(loopname, muAll([*translate(g) | g <-generators]), [muAssignTmp(tmp, translate(result)[-1])]), muTmp(tmp)];
    popIt();
    return code;
}

// Translate SetOrList including spliced elements

private bool containSplices(es) = any(e <- es, e is splice);

list[MuExp] translateSetOrList(es, str kind){
 if(containSplices(es)){
       writer = nextTmp();
       enterWriter(writer);
       code = [ muAssignTmp(writer, muCallPrim("<kind>writer_open", [])) ];
       println("es = <es>");
       for(elem <- es){
           println("elem = <elem>");
           if(elem is splice){
              code += muCallPrim("<kind>writer_splice", [muTmp(writer), *translate(elem.argument)]);
            } else {
              code += muCallPrim("<kind>writer_add", [muTmp(writer), *translate(elem)]);
           }
       }
       code += [ muCallPrim("<kind>writer_close", [ muTmp(writer) ]) ];
       leaveWriter();
       return code;
    } else {
      return [ muCallPrim("<kind>_create", [ *translate(elem) | elem <- es ]) ];
    }
}

// Translate Slice

list[MuExp] translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) { throw "translateSlice"; }

list[MuExp] translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast)  { throw "translateSlice"; }

// Translate Visit

list[MuExp] translateVisit(label, \visit) { throw "visit"; }
