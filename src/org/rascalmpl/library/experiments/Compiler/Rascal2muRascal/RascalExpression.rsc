@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalExpression
import Prelude;

import lang::rascal::\syntax::Rascal;

import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;

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

// Create (and flatten) a muAll

MuExp makeMuAll([*exps1, muAll(list[MuExp] exps2), exps3]) = makeMuAll(exps1 + exps2 + exps3);
default MuExp makeMuAll(list[MuExp] exps) = muAll(exps);

// Create (and flatten) a muOne

MuExp makeMuOne([*exps1, muOne(list[MuExp] exps2), exps3]) = makeMuOne(exps1 + exps2 + exps3);
default MuExp makeMuOne(exp) = muOne(exp);

// Generate code for completely type-resolved operators

bool isContainerType(str t) = t in {"list", "map", "set", "rel", "lrel"};

bool areCompatibleContainerTypes({"list", "lrel"}) = true;
bool areCompatibleContainerTypes({"set", "rel"}) = true;
bool areCompatibleContainerTypes({str c}) = true;
default bool areCompatibleContainerTypes(set[str] s) = false;


str typedInfixOp(str lot, str op, str rot) {
  if(lot == "value" || rot == "value" || lot == "parameter" || rot == "parameter"){
     return op;
  }
  if(isContainerType(lot))
     return areCompatibleContainerTypes({lot, rot}) ? "<lot>_<op>_<rot>" : "<lot>_<op>_elm";
  else
     return isContainerType(rot) ? "elm_<op>_<rot>" : "<lot>_<op>_<rot>";
}

MuExp infix(str op, Expression e) = 
  muCallPrim(typedInfixOp(getOuterType(e.lhs), op, getOuterType(e.rhs)), 
             [*translate(e.lhs), *translate(e.rhs)]);

MuExp infix_elm_left(str op, Expression e){
   rot = getOuterType(e.rhs);
   return muCallPrim("elm_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

MuExp infix_rel_lrel(str op, Expression e){
  lot = getOuterType(e.lhs);
  if(lot == "set") lot = "rel"; else if (lot == "list") lot = "lrel";
  rot = getOuterType(e.rhs);
  if(rot == "set") rot = "rel"; else if (rot == "list") rot = "lrel";
  return muCallPrim("<lot>_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}
 
MuExp prefix(str op, Expression arg) = muCallPrim("<op>_<getOuterType(arg)>", [translate(arg)]);

MuExp postfix(str op, Expression arg) = muCallPrim("<getOuterType(arg)>_<op>", [translate(arg)]);

MuExp postfix_rel_lrel(str op, Expression arg) {
  ot = getOuterType(arg);
  if(ot == "set" ) ot = "rel"; else if(ot == "list") ot = "lrel";
  return muCallPrim("<ot>_<op>", [translate(arg)]);
}

set[str] numeric = {"int", "real", "rat", "num"};

MuExp comparison(str op, Expression e) {
  lot = getOuterType(e.lhs);
  rot = getOuterType(e.rhs);
  println("comparison: op = <op>, lot = <lot>, rot = <rot>");
  if(lot == "value" || rot == "value"){
     lot = ""; rot = "";
  } else {
    if(lot in numeric) lot += "_"; else lot = "";
 
     if(rot in numeric) rot = "_" + rot; else rot = "";
  }
  
  return muCallPrim("<lot><op><rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

/*********************************************************************/
/*                  Expressions                                       */
/*********************************************************************/

// literals

MuExp translate((Literal) `<BooleanLiteral b>`) = "<b>" == "true" ? muCon(true) : muCon(false);
 
MuExp translate((Literal) `<IntegerLiteral n>`) = muCon(toInt("<n>"));

MuExp translate((Literal) `<RegExpLiteral r>`) { throw "RexExpLiteral cannot occur in expression"; }

MuExp translate((Literal) `<StringLiteral n>`) = translateStringLiteral(n);

MuExp translate((Literal) `<LocationLiteral src>`) = translateLocationLiteral(src);

default MuExp translate((Literal) `<Literal s>`) =  muCon(readTextValueString("<s>"));

MuExp translate(e:(Expression)  `<Literal s>`) = translate(s);

// Other expressions

// Concrete
MuExp translate(e:(Expression) `<Concrete concrete>`) {
  return translateConcrete(concrete);
}

MuExp getConstructor(str cons) {
   uid = -1;
   for(c <- constructors){
     //println("c = <c>, uid2name = <uid2name[c]>, uid2str = <uid2str(c)>");
     if(cons == getSimpleName(config.store[c].name)){
        //println("c = <c>, <config.store[c]>,  <uid2addr[c]>");
        uid = c;
        break;
     }
   }
   if(uid < 0)
      throw("No definition for constructor: <cons>");
   return muConstr(fuid2str[uid]);
}
/*
data Tree 
     = appl(Production prod, list[Tree] args)
     | cycle(Symbol symbol, int cycleLength) 
     | amb(set[Tree] alternatives)  
     | char(int character)
     ;
     
lexical Concrete 
  = typed: "(" LAYOUTLIST l1 Sym symbol LAYOUTLIST l2 ")" LAYOUTLIST l3 "`" ConcretePart* parts "`";

lexical ConcretePart
  = @category="MetaSkipped" text   : ![`\<\>\\\n]+ !>> ![`\<\>\\\n]
  | newline: "\n" [\ \t \u00A0 \u1680 \u2000-\u200A \u202F \u205F \u3000]* "\'"
  | @category="MetaVariable" hole : ConcreteHole hole
  | @category="MetaSkipped" lt: "\\\<"
  | @category="MetaSkipped" gt: "\\\>"
  | @category="MetaSkipped" bq: "\\`"
  | @category="MetaSkipped" bs: "\\\\"
  ;
  
syntax ConcreteHole 
  = \one: "\<" Sym symbol Name name "\>"
  ;
*/

MuExp translateConcrete(e:(ConcreteHole) `\< <Sym symbol> <Name name> \>`){
  println("***** ConcreteHole, name: <name>");
  iprint(e);
  return muCallPrim("list_subscript_int", [muCallPrim("adt_field_access", [mkVar("<name>", name@\loc), muCon("args")]), muCon(7)]);
}

default MuExp translateConcrete(e: appl(Production prod, list[Tree] args)){
   //MuExp receiver =  getConstructor("appl");
   //return muCall(receiver, [muCon(prod), muCallPrim("list_create",  [ translateConcrete(a) | a <- args ])]);
    return muCallPrim("parse_fragment", [muCon(getModuleName()), muCon(e), muCon(e@\loc)]);
}

default MuExp translateConcrete(t) = muCon(t);

// Block
MuExp translate(e:(Expression) `{ <Statement+ statements> }`) = muBlock([translate(stat) | stat <- statements]);

// Parenthesized expression
MuExp translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

// Closure
MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) = translateClosure(e, parameters, statements);

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) = translateClosure(e, parameters, statements);

// Enumerator with range

MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`) =
    muMulti(muCreate(mkCallToLibFun("Library", "RANGE", 3), [ translatePat(pat), translate(first), translate(last)]));
    
MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`) =
     muMulti(muCreate(mkCallToLibFun("Library", "RANGE_STEP", 4), [ translatePat(pat), translate(first), translate(second), translate(last)]));

// Range

MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) {
   kind = (getOuterType(first) == "int" && getOuterType(last) == "int") ? "int" : "real";
   return muCallPrim("range_create_<kind>", [translate(first), translate(last)]);
}

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) {
   kind = (getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int") ? "int" : "real";
   return muCallPrim("range_step_create_<kind>", [translate(first),  translate(second), translate(last)]);
}

// Visit
MuExp translate (e:(Expression) `<Label label> <Visit visitItself>`) = translateVisit(label, visitItself);

// Reducer
MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(init, result, generators);

// Reified type
MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }
//  muCon(symbolToValue(symbol, config));

// Call
MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
   // ignore kw arguments for the moment
   MuExp receiver = translate(expression);
   list[MuExp] args = [ translate(a) | a <- arguments ];
   if(getOuterType(expression) == "str") {
       return muCallPrim("node_create", [receiver, *args]);
   }
   
   if(getOuterType(expression) == "loc"){
       return muCallPrim("loc_with_offset_create", [receiver, *args]);
   }
   
   if(muFun(str _) := receiver || muFun(str _, str _) := receiver || muConstr(str _) := receiver) {
       return muCall(receiver, args);
   }
   
   // Now overloading resolution...
   ftype = getType(expression@\loc); // Get the type of a receiver
   if(isOverloadedFunction(receiver) && receiver.fuid in overloadingResolver) {
       // Get the types of arguments
       list[Symbol] targs = [ getType(arg@\loc) | arg <- arguments ];
       // Generate a unique name for an overloaded function resolved for this specific use 
       str ofqname = receiver.fuid + "(<for(targ<-targs){><targ>;<}>)";
       // Resolve alternatives for this specific call
       int i = overloadingResolver[receiver.fuid];
       tuple[str scopeIn,set[int] alts] of = overloadedFunctions[i];
       set[int] resolved = {};
       
       bool matches(Symbol t) {
           if(isFunctionType(ftype) || isConstructorType(ftype)) { 
               return t == ftype;
           }           
           if(isOverloadedType(ftype)) {
               return t in (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype));
           }
           throw "Ups, unexpected type of the call receiver expression!";
       }
       
       for(int alt <- of.alts) {
           t = fuid2type[alt];
           if(matches(t)) {
               resolved += alt;
           }
       }
       
       bool exists = <of.scopeIn,resolved> in overloadedFunctions;
       if(!exists) {
           i = size(overloadedFunctions);
           overloadedFunctions += <of.scopeIn,resolved>;
       } else {
           i = indexOf(overloadedFunctions, <of.scopeIn,resolved>);
       }
       
       overloadingResolver[ofqname] = i;
       return muOCall(muOFun(ofqname), args);
   }
   if(isOverloadedFunction(receiver) && receiver.fuid notin overloadingResolver) {
      throw "The use of a function has to be managed via overloading resolver!";
   }
   // Push down additional information if the overloading resolution needs to be done at runtime
   return muOCall(receiver, 
   				  isFunctionType(ftype) ? Symbol::\tuple([ ftype ]) : Symbol::\tuple([ t | Symbol t <- getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype) ]), 
   				  args);
}

// Any
MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = makeMuOne([translate(g) | g <- generators ]);

// All
MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) = makeMuAll([translate(g) | g <- generators ]);

// Comprehension
MuExp translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

// Set
MuExp translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) = translateSetOrList(es, "set");

// List
MuExp translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`)  = translateSetOrList(es, "list");

// Reified type
MuExp translate (e:(Expression) `# <Type tp>`) = muCon(symbolToValue(translateType(tp),config));

// Tuple
MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) =
    muCallPrim("tuple_create", [ translate(elem) | elem <- elements ]);

// Map
MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) =
   muCallPrim("map_create", [ translate(m.from), translate(m.to) | m <- mappings ]);

// It in reducer
MuExp translate (e:(Expression) `it`) = muTmp(topIt());
 
 // Qualified name
MuExp translate(q:(QualifiedName) `<QualifiedName v>`) = mkVar("<v>", v@\loc);

MuExp translate((Expression) `<QualifiedName v>`) = translate(v);

// For the benefit of names in regular expressions

MuExp translate((Name) `<Name name>`) = mkVar("<name>", name@\loc);

// Subscript
MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    if(ot notin {"map", "rel", "lrel"}) {
    	   op = "<getOuterType(exp)>_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    return muCallPrim(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts]);
}

// Slice
MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

// Field access
MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) =
    muCallPrim("<getOuterType(expression)>_field_access", [ translate(expression), muCon("<field>") ]);

// Field update
MuExp translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) =
    muCallPrim("<getOuterType(expression)>_field_update", [ translate(expression), muCon("<key>"), translate(replacement) ]);

// Field project
MuExp translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) {
    tp = getType(expression@\loc);   
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    }
    if(tupleHasFieldNames(tp)){
    	fieldNames = getTupleFieldNames(tp);
    }	
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon(indexOf(fieldNames, "<f>")) | f <- fields];
    //fcode = [(f is index) ? muCon(toInt("<f>")) : muCon("<f>") | f <- fields];
    return muCallPrim("<getOuterType(expression)>_field_project", [ translate(expression), *fcode]);
}

// setAnnotation
MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) =
    muCallPrim("annotation_set", [translate(expression), muCon("<name>"), translate(val)]);

// getAnnotation
MuExp translate (e:(Expression) `<Expression expression> @ <Name name>`) {
println("getAnnotation: <e>");
    return muCallPrim("annotation_get", [translate(expression), muCon("<name>")]);
    }

// Is
MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim("is", [translate(expression), muCon("<name>")]);

// Has
MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) = 
    muCon(hasField(getType(expression@\loc), "<name>"));   

// Transitive closure
MuExp translate(e:(Expression) `<Expression argument> +`)   = postfix_rel_lrel("transitive_closure", argument);

// Transitive reflexive closure
MuExp translate(e:(Expression) `<Expression argument> *`)   = postfix_rel_lrel("transitive_reflexive_closure", argument);

// isDefined?
MuExp translate(e:(Expression) `<Expression argument> ?`)  = generateIfDefinedOtherwise(muBlock([ translate(argument), muCon(true) ]),  muCon(false));

// IfDefinedOtherwise
MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  = generateIfDefinedOtherwise(translate(lhs), translate(rhs));

MuExp generateIfDefinedOtherwise(MuExp muLHS, MuExp muRHS) {

    str varname = asTmp(nextLabel());
	// Check if evaluation of the expression throws a 'NoSuchKey' or 'NoSuchAnnotation' exception;
	// do this by checking equality of the value constructor names
	cond1 = muCallMuPrim("equal", [ muCon("UninitializedVariable"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children", [ muTmp(asUnwrapedThrown(varname)) ]), muInt(0) ] ) ]);
	cond3 = muCallMuPrim("equal", [ muCon("NoSuchKey"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children", [ muTmp(asUnwrapedThrown(varname)) ]), muInt(0) ] ) ]);
	cond2 = muCallMuPrim("equal", [ muCon("NoSuchAnnotation"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children", [ muTmp(asUnwrapedThrown(varname)) ]), muInt(0) ] ) ]);
	
	elsePart3 = muIfelse(nextLabel(), muAll([cond3]), [ muRHS ], [ muThrow(muTmp(varname)) ]);
	elsePart2 = muIfelse(nextLabel(), muAll([cond2]), [ muRHS ], [ elsePart3 ]);
	catchBody = muIfelse(nextLabel(), muAll([cond1]), [ muRHS ], [ elsePart2 ]);
	return muTry(muLHS, muCatch(varname, Symbol::\adt("RuntimeException",[]), catchBody), 
			  		 	muBlock([]));
}

// Not
MuExp translate(e:(Expression) `!<Expression argument>`)    = translateBool(e);

// Negate
MuExp translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

// Splice
MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice cannot occur outside set or list";
}
   
// AsType
MuExp translate(e:(Expression) `[ <Type typ> ] <Expression argument>`)  =
   muCallPrim("parse", [muCon(getModuleName()), muCon(type(symbolToValue(translateType(typ), config).symbol,getGrammar(config))), translate(argument)]);
   

// Composition
MuExp translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix_rel_lrel("compose", e);

// Product
MuExp translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

// Join
MuExp translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

// Remainder
MuExp translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

// Division
MuExp translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("divide", e);

// Intersection
MuExp translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersect", e);

//Addition
MuExp translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("add", e);

// Subtraction
MuExp translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtract", e);

// Insert Before
MuExp translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("add", e);

// Append After
MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("add", e);

// Modulo
MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("mod", e);

// Notin
MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix_elm_left("notin", e);

// In
MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix_elm_left("in", e);

// Greater Equal
MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greaterequal", e);

// Less Equal
MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lessequal", e);

// Less
MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);

// Greater
MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);

// Equal
MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = comparison("equal", e);

// NotEqual
MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = comparison("notequal", e);



// NoMatch
MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  = translateMatch(e);

// Match
MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateMatch(e);

// Enumerate

MuExp translate(e:(Expression) `<QualifiedName name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_AND_ASSIGN", 2), [muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Type tp> <Name name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_CHECK_AND_ASSIGN", 3), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`) =
    muMulti(muCreate(mkCallToLibFun("Library", "ENUMERATE_AND_MATCH", 2), [translatePat(pat), translate(exp)]));

// Implies
MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e);

// Equivalent
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e);

// And
MuExp translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e);

// Or
MuExp translate(e:(Expression) `<Expression lhs> || <Expression rhs>`)  = translateBool(e);
 
// Conditional Expression
MuExp translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = 
	// Label (used to backtrack) here is not important as it is not allowed to have 'fail' in conditional expressions 
    muIfelse(nextLabel(),makeMuAll([translate(condition)]), [translate(thenExp)],  [translate(elseExp)]); 

// Default: should not happen
default MuExp translate(Expression e) {
	throw "MISSING CASE FOR EXPRESSION: <e>";
}

/*********************************************************************/
/*                  End of Ordinary Expessions                       */
/*********************************************************************/

/*********************************************************************/
/*                  BooleanExpessions                                */
/*********************************************************************/
 
// Is an expression free of backtracking? 

bool backtrackFree(Expression e){
    visit(e){
    case (Expression) `<Pattern pat> \<- <Expression exp>`: 
    	return false;
    case (Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`: 
    	return false;
    case (Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`: 
    	return false;
    }
    return true;
}

// Boolean expressions

MuExp translateBool((Expression) `<Expression lhs> && <Expression rhs>`) = translateBoolBinaryOp("and", lhs, rhs);

MuExp translateBool((Expression) `<Expression lhs> || <Expression rhs>`) = translateBoolBinaryOp("or", lhs, rhs);

MuExp translateBool((Expression) `<Expression lhs> ==\> <Expression rhs>`) = translateBoolBinaryOp("implies", lhs, rhs);

MuExp translateBool((Expression) `<Expression lhs> \<==\> <Expression rhs>`) = translateBoolBinaryOp("equivalent", lhs, rhs);

MuExp translateBool((Expression) `! <Expression lhs>`) = translateBoolNot(lhs);
 
 MuExp translateBool(e: (Expression) `<Pattern pat> := <Expression exp>`)  = translateMatch(e);
//   muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [translatePat(pat), translate(exp)]));
   
MuExp translateBool(e: (Expression) `<Pattern pat> !:= <Expression exp>`) = translateMatch(e);
//    muCallMuPrim("not_mbool", [makeMuAll([muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [translatePat(pat), translate(exp)]))]) ]);

// All other expressions are translated as ordinary expression

default MuExp translateBool(Expression e) {
   println("translateBool, default: <e>");
   return translate(e);
}
   
// Translate Boolean operators

MuExp translateBoolBinaryOp(str fun, Expression lhs, Expression rhs){
  if(backtrackFree(lhs) && backtrackFree(rhs)) {
     return muCallMuPrim("<fun>_mbool_mbool", [translateBool(lhs), translateBool(rhs)]);
  } else {
    switch(fun){
    	case "and": return makeMuAll([translate(lhs), translate(rhs)]);
    	case "or":  // a or b == !(!a and !b)
    				return muCallMuPrim("not_mbool", [makeMuAll([muCallMuPrim("not_mbool", [translate(lhs)]),  muCallMuPrim("not_mbool", [translate(lhs)])])]);
    	case "implies":
    				// a ==> b
    	            return makeMuAll([muCallMuPrim("implies_mbool_mbool", [makeMuAll([translate(lhs)]), makeMuAll([translate(rhs)])])]);
    	case "equivalent":
    				// a <==> b
    				return makeMuAll([muCallMuPrim("equivalent_mbool_mbool", [makeMuAll([translate(lhs)]), makeMuAll([translate(rhs)])])]);
    	default:
    		throw "translateBoolBinary: unknown operator <fun>";
    }
  }
}

MuExp translateBoolNot(Expression lhs){
  if(backtrackFree(lhs)){
  	  return muCallMuPrim("not_mbool", [translateBool(lhs)]);
  	} else {
  	  return muCallMuPrim("not_mbool", [ makeMuAll([translate(lhs)]) ]);
  	}
}

/*********************************************************************/
/*      Auxiliary functions for translating various constructs       */
/*********************************************************************


// Translate a string literals and string templates

/*
syntax StringLiteral
	= template: PreStringChars pre StringTemplate template StringTail tail 
	| interpolated: PreStringChars pre Expression expression StringTail tail 
	| nonInterpolated: StringConstant constant ;
	
lexical PreStringChars
	= [\"] StringCharacter* [\<] ;
	
lexical MidStringChars
	=  [\>] StringCharacter* [\<] ;
	
lexical PostStringChars
	= @category="Constant" [\>] StringCharacter* [\"] ;
*/	

MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <StringTemplate template> <StringTail tail>`) {
	preResult = nextTmp();
	return muBlock( [ muAssignTmp(preResult, translatePre(pre)),
                      muCallPrim("template_addunindented", [ translateTemplate(template, preResult), *translateTail(tail)])
                    ]);
}
    
MuExp translateStringLiteral((StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    preResult = nextTmp();
    return muBlock( [ muAssignTmp(preResult, translatePre(pre)),
					  muCallPrim("template_addunindented", [ translateTemplate(expression, preResult), *translateTail(tail)])
					]   );
}
                    
MuExp translateStringLiteral((StringLiteral)`<StringConstant constant>`) = muCon(readTextValueString("<constant>"));

MuExp translatePre(PreStringChars pre) {
  content = "<pre>"[1..-1];
  return muCon(content);  //[muCallPrim("str_remove_margins", [muCon(content)])];
}

/*
syntax StringTemplate
	= ifThen    : "if"    "(" {Expression ","}+ conditions ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| ifThenElse: "if"    "(" {Expression ","}+ conditions ")" "{" Statement* preStatsThen StringMiddle thenString Statement* postStatsThen "}" "else" "{" Statement* preStatsElse StringMiddle elseString Statement* postStatsElse "}" 
	| \for       : "for"   "(" {Expression ","}+ generators ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	| doWhile   : "do"    "{" Statement* preStats StringMiddle body Statement* postStats "}" "while" "(" Expression condition ")" 
	| \while     : "while" "(" Expression condition ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" ;
*/
	

/*
  syntax StringMiddle
	= mid: MidStringChars mid 
	| template: MidStringChars mid StringTemplate template StringMiddle tail 
	| interpolated: MidStringChars mid Expression expression StringMiddle tail ;
*/

MuExp translateMiddle((StringMiddle) `<MidStringChars mid>`)  =  muCon("<mid>"[1..-1]); // muCallPrim("str_remove_margins", [muCon("<mid>"[1..-1])]);

MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <StringTemplate template> <StringMiddle tail>`) {
    midResult = nextTmp();
    return muBlock( [ muAssignTmp(midResult, translateMid(mid)),
   			          muCallPrim("template_addunindented", [ translateTemplate(template, midResult), translateMiddle(tail) ])
   			        ]);
   	}

MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) {
    midResult = nextTmp();
    return muBlock( [ muAssignTmp(midResult, translateMid(mid)),
                      muCallPrim("template_addunindented", [ translateTemplate(expression, midResult), translateMiddle(tail) ])
                    ]);
}

MuExp translateMid(MidStringChars mid) {
  content = "<mid>"[1..-1];
  return muCon(content);
}    
/*
syntax StringTail
	= midInterpolated: MidStringChars mid Expression expression StringTail tail 
	| post: PostStringChars post 
	| midTemplate: MidStringChars mid StringTemplate template StringTail tail ;
*/

list[MuExp] translateTail((StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) {
    midResult = nextTmp();
    return [ muBlock( [ muAssignTmp(midResult, translateMid(mid)),
                      muCallPrim("template_addunindented", [ translateTemplate(expression, midResult), *translateTail(tail)])
                    ])
           ];
}
	
list[MuExp] translateTail((StringTail) `<PostStringChars post>`) {
  content = "<post>"[1..-1];
  return size(content) == 0 ? [] : [muCon(content)]; //[muCallPrim("str_remove_margins", [muCon(content)])];
}

list[MuExp] translateTail((StringTail) `<MidStringChars mid> <StringTemplate template> <StringTail tail>`) {
    midResult = nextTmp();
    return [ muBlock( [ muAssignTmp(midResult, translateMid(mid)),
                        muCallPrim("template_addunindented", [ translateTemplate(template, midResult), *translateTail(tail) ])
                    ])
           ];
 }  
 
 MuExp translateTemplate(Expression e, str pre){
    result = nextTmp();
    return muBlock([ muAssignTmp(result, muCallPrim("template_open", [muTmp(pre)])),
    				 muAssignTmp(result, muCallPrim("template_add", [ muTmp(result), muCallPrim("value_to_string", [translate(e)]) ])),
                     muCallPrim("template_close", [muTmp(result)])
                   ]);
 }
 
 // Translate location templates
 
 /*
 syntax LocationLiteral
	= \default: ProtocolPart protocolPart PathPart pathPart ;
 */
 
 MuExp translateLocationLiteral((LocationLiteral) `<ProtocolPart protocolPart> <PathPart pathPart>`) =
     muCallPrim("loc_create", [muCallPrim("str_add_str", [translateProtocolPart(protocolPart), translatePathPart(pathPart)])]);
 
 /*
 syntax ProtocolPart
	= nonInterpolated: ProtocolChars protocolChars 
	| interpolated: PreProtocolChars pre Expression expression ProtocolTail tail ;
	
lexical PreProtocolChars
	= "|" URLChars "\<" ;
	
 lexical MidProtocolChars
	= "\>" URLChars "\<" ;
	
lexical ProtocolChars
	= [|] URLChars "://" !>> [\t-\n \r \ \u00A0 \u1680 \u2000-\u200A \u202F \u205F \u3000];

syntax ProtocolTail
	= mid: MidProtocolChars mid Expression expression ProtocolTail tail 
	| post: PostProtocolChars post ;

lexical PostProtocolChars
	= "\>" URLChars "://" ;	
*/

 MuExp translateProtocolPart((ProtocolPart) `<ProtocolChars protocolChars>`) = muCon("<protocolChars>"[1..]);
 
 MuExp translateProtocolPart((ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`) =
    muCallPrim("str_add_str", [muCon("<pre>"[1..-1]), translate(expression), translateProtocolTail(tail)]);
 
 // ProtocolTail
 MuExp  translateProtocolTail((ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`) =
   muCallPrim("str_add_str", [muCon("<mid>"[1..-1]), translate(expression), translateProtocolTail(tail)]);
   
MuExp translateProtocolTail((ProtocolTail) `<PostProtocolChars post>`) = muCon("<post>"[1 ..]);

/*
syntax PathPart
	= nonInterpolated: PathChars pathChars 
	| interpolated: PrePathChars pre Expression expression PathTail tail ;

lexical PathChars
	= URLChars [|] ;
		
 syntax PathTail
	= mid: MidPathChars mid Expression expression PathTail tail 
	| post: PostPathChars post ;

lexical PrePathChars
	= URLChars "\<" ;

lexical MidPathChars
	= "\>" URLChars "\<" ;
	
lexical PostPathChars
	=  "\>" URLChars "|" ;
*/

MuExp translatePathPart((PathPart) `<PathChars pathChars>`) = muCon("<pathChars>"[..-1]);
MuExp translatePathPart((PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`) =
   muCallPrim("str_add_str", [ muCon("<pre>"[..-1]), translate(expression), translatePathTail(tail)]);

// PathTail
MuExp translatePathTail((PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`) =
   muCallPrim("str_add_str", [ muCon("<mid>"[1..-1]), translate(expression), translatePathTail(tail)]);
   
MuExp translatePathTail((PathTail) `<PostPathChars post>`) = muCon("<post>"[1..-1]);

 
// Translate a closure   
 
 MuExp translateClosure(Expression e, Parameters parameters, Statement+ statements) {
 	uid = loc2uid[e@\loc];
	fuid = uid2str(uid);
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	nlocals = getScopeSize(fuid);
	bool isVarArgs = (varArgs(_,_) := parameters);
  	// TODO: keyword parameters
    
    MuExp body = translateFunction(parameters.formals.formals, statements, []);
    tuple[str fuid,int pos] addr = uid2addr[uid];
    functions_in_module += muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									  nformals, nlocals, e@\loc, [], (), body);
  	
  	leaveFunctionScope();								  
  	
	return (addr.fuid == uid2str(0)) ? muFun(fuid) : muFun(fuid, addr.fuid); // closures are not overloaded
}

// Translate comprehensions

MuExp translateGenerators({Expression ","}+ generators){
   if(all(gen <- generators, backtrackFree(gen))){
      return makeMuAll([translate(g) | g <-generators]);
   } else {
     return makeMuAll([muCallPrim("rbool", [translate(g)]) | g <-generators]);
   }
}

list[MuExp] translateComprehensionContribution(str kind, str tmp, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muCallPrim("<kind>writer_splice", [muTmp(tmp), translate(exp)]);
	    } else {
	      append muCallPrim("<kind>writer_add", [muTmp(tmp), translate(r)]);
	    }
	  }
} 

MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname);
    return
    muBlock(
    [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMuAll([translate(g) | g <-generators]), translateComprehensionContribution("list", tmp, [r | r <- results])),
      muCallPrim("listwriter_close", [muTmp(tmp)]) 
    ]);
}

MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, muCallPrim("setwriter_open", [])),
      muWhile(loopname, makeMuAll([translate(g) | g <-generators]), translateComprehensionContribution("set", tmp, [r | r <- results])),
      muCallPrim("setwriter_close", [muTmp(tmp)]) 
    ]);
}

MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, muCallPrim("mapwriter_open", [])),
      muWhile(loopname, makeMuAll([*translate(g) | g <-generators]), [muCallPrim("mapwriter_add", [muTmp(tmp)] + [ translate(from), translate(to)])]), 
      muCallPrim("mapwriter_close", [muTmp(tmp)]) 
    ]);
}

// Translate Reducer

MuExp translateReducer(init, result, generators){
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    pushIt(tmp);
    code = [ muAssignTmp(tmp, translate(init)), muWhile(loopname, makeMuAll([translate(g) | g <-generators]), [muAssignTmp(tmp, translate(result))]), muTmp(tmp)];
    popIt();
    return muBlock(code);
}

// Translate SetOrList including spliced elements

private bool containSplices(es) = any(e <- es, e is splice);

MuExp translateSetOrList(es, str kind){
 if(containSplices(es)){
       writer = nextTmp();
       enterWriter(writer);
       code = [ muAssignTmp(writer, muCallPrim("<kind>writer_open", [])) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim("<kind>writer_splice", [muTmp(writer), translate(elem.argument)]);
            } else {
              code += muCallPrim("<kind>writer_add", [muTmp(writer), translate(elem)]);
           }
       }
       code += [ muCallPrim("<kind>writer_close", [ muTmp(writer) ]) ];
       leaveWriter();
       return muBlock(code);
    } else {
      return muCallPrim("<kind>_create", [ translate(elem) | elem <- es ]);
    }
}

// Translate Slice

MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) =
    muCallPrim("<getOuterType(expression)>_slice", [ translate(expression), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ]);

MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muCon("false") : translate(optExp.expression);

MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) =
    muCallPrim("<getOuterType(expression)>_slice", [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ]);

// Translate Visit
MuExp translateVisit(label,\visit) {
	MuExp traverse_fun;
	bool fixpoint = false;
	
	if(\visit is defaultStrategy) {
		traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP",5);
	} else {
		switch("<\visit.strategy>") {
			case "bottom-up"      :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP",      5);
			case "top-down"       :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN",       5);
			case "bottom-up-break":   traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP_BREAK",5);
			case "top-down-break" :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN_BREAK", 5);
			case "innermost"      : { traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP",      5); fixpoint = true; }
			case "outermost"      : { traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN",       5); fixpoint = true; }
		}
	}
	
	bool rebuild = false;
	if( Case c <- \visit.cases, (c is patternWithAction && c.patternWithAction is replacing 
									|| hasTopLevelInsert(c)) ) {
		println("Rebuilding visit!");
		rebuild = true;
	}
	
	// Unique 'id' of a visit in the function body
	int i = nextVisit();
	
	// Generate and add a nested function 'phi'
	str scopeId = topFunctionScope();
	str phi_fuid = scopeId + "/" + "PHI_<i>";
	Symbol phi_ftype = Symbol::func(Symbol::\value(), [Symbol::\value(),Symbol::\value()]);
	
	enterVisit();	
	functions_in_module += muFunction(phi_fuid, phi_ftype, scopeId, 3, 3, \visit@\loc, [], (), 
										translateVisitCases([ c | Case c <- \visit.cases ]));
	leaveVisit();
	
	if(fixpoint) {
		str phi_fixpoint_fuid = scopeId + "/" + "PHI_FIXPOINT_<i>";
		
		list[MuExp] body = [];
		body += muAssignLoc("changed", 3, muBool(true));
		body += muWhile(nextLabel(), muLoc("changed",3), 
						[ muAssignLoc("val", 4, muCall(muFun(phi_fuid,scopeId), [ muLoc("iSubject",0), muLoc("matched",1), muLoc("hasInsert",2) ])),
						  muIfelse(nextLabel(), muCallPrim("equal",[ muLoc("val",4), muLoc("iSubject",0) ]),
						  						[ muAssignLoc("changed",3, muBool(false)) ], 
						  						[ muAssignLoc("iSubject",0, muLoc("val",4)) ] )]);
		body += muReturn(muLoc("iSubject",0));
		
		functions_in_module += muFunction(phi_fixpoint_fuid, phi_ftype, scopeId, 3, 5, \visit@\loc, [], (), muBlock(body));
	
		str hasMatch = asTmp(nextLabel());
		str beenChanged = asTmp(nextLabel());
		return muBlock([ muAssignTmp(hasMatch, muBool(false)),
						 muAssignTmp(beenChanged, muBool(false)),
					 	 muCall(traverse_fun, [ muFun(phi_fixpoint_fuid,scopeId), translate(\visit.subject), muTmpRef(hasMatch), muTmpRef(beenChanged), muBool(rebuild) ]) 
				   	   ]);
	}
	
	str hasMatch = asTmp(nextLabel());
	str beenChanged = asTmp(nextLabel());
	return muBlock([ muAssignTmp(hasMatch, muBool(false)), 
	                 muAssignTmp(beenChanged, muBool(false)),
					 muCall(traverse_fun, [ muFun(phi_fuid,scopeId), translate(\visit.subject), muTmpRef(hasMatch), muTmpRef(beenChanged), muBool(rebuild) ]) 
				   ]);
}

@doc{Generates the body of a phi function}
MuExp translateVisitCases(list[Case] cases) {
	// TODO: conditional
	if(size(cases) == 0) {
		return muReturn(muLoc("subject",0));
	}
	
	c = head(cases);
	
	if(c is patternWithAction) {
		pattern = c.patternWithAction.pattern;
		typePat = getType(pattern@\loc);
		cond = muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [ translatePat(pattern), muLoc("subject",0) ]));
		ifname = nextLabel();
		enterBacktrackingScope(ifname);
		if(c.patternWithAction is replacing) {
			replacement = translate(c.patternWithAction.replacement.replacementExpression);
			replacementType = getType(c.patternWithAction.replacement.replacementExpression@\loc);
			tcond = muCallPrim("subtype", [ muTypeCon(replacementType), muCallPrim("typeOf", [ muLoc("iSubject",0) ]) ]);
			list[MuExp] cbody = [ muAssignLocDeref("matched",1,muBool(true)), muAssignLocDeref("hasInsert",2,muBool(true)), replacement ];
        	exp = muIfelse(ifname, muAll([cond,tcond]), [ muReturn(muBlock(cbody)) ], [ translateVisitCases(tail(cases)) ]);
        	leaveBacktrackingScope();
        	return exp;
		} else {
			// Arbitrary
			statement = c.patternWithAction.statement;
			\case = translate(statement);
			insertType = topCaseType();
			clearCaseType();
			tcond = muCallPrim("subtype", [ muTypeCon(insertType), muCallPrim("typeOf", [ muLoc("iSubject",0) ]) ]);
			list[MuExp] cbody = [ muAssignLocDeref("matched",1,muBool(true)) ];
			if(!(muBlock([]) := \case)) {
				cbody += \case;
			}
			cbody += muReturn(muLoc("subject",0));
			exp = muIfelse(ifname, muAll([cond,tcond]), cbody, [ translateVisitCases(tail(cases)) ]);
        	leaveBacktrackingScope();
			return exp;
		}
	} else {
		// Default
		return muBlock([ muAssignLocDeref("matched", 1, muBool(true)), translate(c.statement), muReturn(muLoc("iSubject",0)) ]);
	}
}

private bool hasTopLevelInsert(Case c) {
	println("Look for an insert...");
	top-down-break visit(c) {
		case (Statement) `insert <DataTarget dt> <Statement stat>`: return true;
		case Visit v: ;
	}
	println("Insert has not been found, non-rebuilding visit!");
	return false;
}
