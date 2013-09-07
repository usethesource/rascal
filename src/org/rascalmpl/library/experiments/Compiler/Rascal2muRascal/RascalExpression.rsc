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

MuExp infix(str op, Expression e){
  lot = getOuterType(e.lhs);
  rot = getOuterType(e.rhs);
  if(isContainerType(lot))
     if(isContainerType(rot))
       return muCallPrim("<lot>_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
     else
       return muCallPrim("<lot>_<op>_elm", [*translate(e.lhs), *translate(e.rhs)]);
  else
    if(isContainerType(rot))
       return muCallPrim("elm_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
     else
       return muCallPrim("<lot>_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}
 
MuExp prefix(str op, Expression arg) = muCallPrim("<op>_<getOuterType(arg)>", [translate(arg)]);
MuExp postfix(str op, Expression arg) = muCallPrim("<getOuterType(arg)>_<op>", [translate(arg)]);

MuExp comparison(str op, Expression e) = muCallPrim("<op>", [*translate(e.lhs), *translate(e.rhs)]);

/*********************************************************************/
/*                  Expessions                                       */
/*********************************************************************/

// literals

MuExp translate((Literal) `<BooleanLiteral b>`) = "<b>" == "true" ? muCon(true) : muCon(false);
 
MuExp translate((Literal) `<IntegerLiteral n>`) = muCon(toInt("<n>"));

MuExp translate((Literal) `<StringLiteral n>`) = translateStringLiteral(n);

default MuExp translate((Literal) `<Literal s>`) =  muCon(readTextValueString("<s>"));

MuExp translate(e:(Expression)  `<Literal s>`) = translate(s);

// Other expressions

// Concrete
MuExp translate(e:(Expression) `<Concrete concret>`) { throw("Concrete"); }

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

// Visit
MuExp translate (e:(Expression) `<Label label> <Visit \visit>`) = translateVisit(label, \visit);

// Reducer
MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(init, result, generators);

// Reified type
MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }

// Call
MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){
   // ignore kw arguments for the moment
   MuExp receiver = translate(expression);
   list[MuExp] args = [ translate(a) | a <- arguments ];
   if(getOuterType(expression) == "str") {
       return muCallPrim("node_create", [receiver, *args]);
   }
   // TODO: overloading with constructors
   if(muConstr(str fuid) := receiver) {
       return muCall(receiver, args);
   }
   // Now overloading resolution...
   // Get the type of a receiver
   ftype = getType(expression@\loc);
   if(muOFun(str fuid) := receiver && fuid in overloadingResolver) {
       // Get the types of arguments
       list[Symbol] targs = [ getType(arg@\loc) | arg <- arguments ];
       // Generate a unique name for an overloaded function resolved for this specific use 
       str ofqname = fuid + "(<for(targ<-targs){><targ>;<}>)";
       // Resolve alternatives for this specific call
       int i = overloadingResolver[fuid];
       set[int] alts = overloadedFunctions[i];
       set[int] resolved = {};
       
       bool matches(Symbol t) = isFunctionType(ftype) ? t == ftype : t in ftype.overloads;
       
       for(int alt <- alts) {
           t = fuid2type[alt];
           if(matches(t)) {
               resolved += alt;
           }
       }
       
       bool exists = resolved in overloadedFunctions;
       if(!exists) {
           i = size(overloadedFunctions);
           overloadedFunctions += resolved;
       } else {
           i = indexOf(overloadedFunctions, resolved);
       }
       
       overloadingResolver[ofqname] = i;
       // TODO: New insight to the overloading and scoping semantics enables static resolution with respect to the 'scopeIn'
       // ***Note: r2mu translation does not care of whether the function is nested or not;
       //          now runtime system is responsible for this
       return muOCall(muOFun(ofqname), args);
   }
   if(muOFun(str fuid) := receiver && fuid notin overloadingResolver) {
      throw "The use of a function has to be managed via overloading resolver!";
   }
   // Push down additional information if the overloading resolution needs to be done at runtime
   return muOCall(receiver, isFunctionType(ftype) ? { ftype } : ftype.overloads , args);
}

// Any
MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = muOne([translate(g) | g <- generators ]);

// All
MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) = muAll([translate(g) | g <- generators ]);

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
 
 // Qualifid name
MuExp translate(q:(QualifiedName) `<QualifiedName v>`) = mkVar("<v>", v@\loc);

MuExp translate((Expression) `<QualifiedName v>`) = translate(v);

// Subscript
MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    if(ot notin {"map"}) {
    	op = "<getOuterType(exp)>_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    return muCallPrim(op, translate(exp) + [translate(s) | s <- subscripts]);
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
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon("<field>") | f <- fields];
    return muCallPrim("<getOuterType(expression)>_field_project", [ translate(expression), *fcode]);
}

// setAnnotation
MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression \value> ]`) =
    muCallPrim("annotation_set", [translate(expression), muCon("<name>"), translate(\value)]);

// getAnnotation
MuExp translate (e:(Expression) `<Expression expression> @ <Name name>`) =
    muCallPrim("annotation_get", [translate(expression), muCon("<name>")]);

// Is
MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim("is", [translate(expression), muCon("<name>")]);

// Has
MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) = 
    muCon(hasField(getType(expression@\loc), "<name>"));   

// Transitive closure
MuExp translate(e:(Expression) `<Expression argument> +`)   = postfix("transitiveClosure", argument);

// Transitive reflexive closure
MuExp translate(e:(Expression) `<Expression argument> *`)   = postfix("transitiveReflexiveClosure", argument);

// isDefined?
MuExp translate(e:(Expression) `<Expression argument> ?`)   { throw("isDefined"); }

// Not
MuExp translate(e:(Expression) `!<Expression argument>`)    = translateBool(e);

// Negate
MuExp translate(e:(Expression) `-<Expression argument>`)    = muCallPrim("negative", [translate(argument)]);

// Splice
MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice cannot occur outside set or list";
}

// AsType
MuExp translate(e:(Expression) `[ <Type \type> ] <Expression argument>`)  { throw("asType"); }

// Composition
MuExp translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix("compose", e);

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
MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix("notin", e);

// In
MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix("in", e);

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

// IfDefinedOtherwise
MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  { throw("ifDefinedOtherwise"); }

// NoMatch
MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  { throw("noMatch"); }

// Match
MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateBool(e);

// Enumerate
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
    muIfelse(translate(condition), [translate(thenExp)],  [translate(elseExp)]); 

// Default: should not happen
default MuExp translate(Expression e) {
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

MuExp translateBool(str fun, Expression lhs, Expression rhs){
  blhs = backtrackFree(lhs) ? "U" : "M";
  brhs = backtrackFree(rhs) ? "U" : "M";
  return muCallMuPrim("<fun>_<blhs>_<brhs>", [*translate(lhs), *translate(rhs)]);
}

MuExp translateBool(str fun, Expression lhs){
  blhs = backtrackFree(lhs) ? "U" : "M";
  return muCallMuPrim("<fun>_<blhs>", [translate(lhs)]);
}

MuExp translateBool(e:(Expression) `<Expression lhs> && <Expression rhs>`) = translateBool("AND", lhs, rhs);

MuExp translateBool(e:(Expression) `<Expression lhs> || <Expression rhs>`) = translateBool("OR", lhs, rhs);

MuExp translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) = translateBool("IMPLIES", lhs, rhs);

MuExp translateBool(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) = translateBool("EQUIVALENT", lhs, rhs);

MuExp translateBool(e:(Expression) `! <Expression lhs>`) = translateBool("NOT", lhs);
 
// Translate match operator
 
 MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`)  {
   println("translateBool: <pat> := <exp>");
   return muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [translatePat(pat), translate(exp)]));
}   
   
// Auxiliary functions for translating various constructs

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

MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <StringTemplate template> <StringTail tail>`) =  
    muCallPrim("str_addindented_str", [*translatePre(pre), translateTemplate(template), *translateTail(tail)]);
    
MuExp translateStringLiteral((StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) =
     muCallPrim("str_addindented_str", [*translatePre(pre), muCallPrim("value_to_string", [translate(expression)]), *translateTail(tail)]);

MuExp translateStringLiteral((StringLiteral)`<StringConstant constant>`) = muCon(readTextValueString("<constant>"));

list[MuExp] translatePre(PreStringChars pre) {
  content = "<pre>"[1..-1];
  return size(content) == 0 ? [] : [muCallPrim("str_remove_margins", [muCon(content)])];
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

MuExp translateMiddle((StringMiddle) `<MidStringChars mid>`)  =  muCallPrim("str_remove_margins", [muCon("<mid>"[1..-1])]);

MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <StringTemplate template> <StringMiddle tail>`) =
    muCallPrim("str_addindented_str", [ *translateMid(mid), translateTemplate(template), translateMiddle(tail) ]);

MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) =
    muCallPrim("str_addindented_str", [ *translateMid(mid), muCallPrim("value_to_string", [translate(expression)]), translateMiddle(tail)]);

list[MuExp] translateMid(MidStringChars mid) {
  content = "<mid>"[1..-1];
  return size(content) == 0 ? [] : [muCon(content)];
}    
/*
syntax StringTail
	= midInterpolated: MidStringChars mid Expression expression StringTail tail 
	| post: PostStringChars post 
	| midTemplate: MidStringChars mid StringTemplate template StringTail tail ;
*/
list[MuExp] translateTail((StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) =
    [ muCallPrim("str_addindented_str", [ *translateMid(mid), muCallPrim("value_to_string", [translate(expression)]), *translateTail(tail)]) ];
	
list[MuExp] translateTail((StringTail) `<PostStringChars post>`) {
  content = "<post>"[1..-1];
  return size(content) == 0 ? [] : [muCallPrim("str_remove_margins", [muCon(content)])];
}

list[MuExp] translateTail((StringTail) `<MidStringChars mid> <StringTemplate template> <StringTail tail>`) =
    [ muCallPrim("str_addindented_str", [  *translateMid(mid), translateTemplate(template), *translateTail(tail) ]) ];
   
// Translate a closure   
 
 MuExp translateClosure(Expression e, Parameters parameters, Statement* statements) {
 	uid = loc2uid[e@\loc];
	fuid = uid2str(uid);
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	nlocals = getScopeSize(fuid);
	bool isVarArgs = (varArgs(_,_) := parameters);
  	// TODO: keyword parameters
  	{Pattern ","}* formals = parameters.formals.formals;
  	list[MuExp] conditions = [];
  	int i = 0;
  	for(Pattern pat <- formals) {
        conditions += muMulti(muCreate(mkCallToLibFun("Library","MATCH",2), [ *translatePat(pat), muLoc("<i>",i) ]));
        i += 1;
    };
    MuExp body = muBlock([ translate(stat) | stat <- statements ]);
    if(!isEmpty(conditions)) {
        body = muIfelse(muOne(conditions), [ body ], [ muFailReturn() ]);
    }
	return (addr.fuid == uid2str(0)) ? muFun(fuid) : muFun(fuid, addr.fuid); // closures are not overloaded
}

// Translate a comprehension

MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname);
    return
    muBlock(
    [ muAssignTmp(tmp, muCallPrim("listwriter_open", [])),
      muWhile(loopname, muAll([translate(g) | g <-generators]), [muCallPrim("listwriter_add", [muTmp(tmp)] + [ translate(r) | r <- results])]), 
      muCallPrim("listwriter_close", [muTmp(tmp)]) 
    ]);
}

MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, muCallPrim("setwriter_open", [])),
      muWhile(loopname, muAll([translate(g) | g <-generators]), [muCallPrim("setwriter_add", [muTmp(tmp)] + [ translate(r) | r <- results])]), 
      muCallPrim("setwriter_close", [muTmp(tmp)]) 
    ]);
}

MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, muCallPrim("mapwriter_open", [])),
      muWhile(loopname, muAll([*translate(g) | g <-generators]), [muCallPrim("mapwriter_add", [muTmp(tmp)] + [ translate(from), translate(to)])]), 
      muCallPrim("mapwriter_close", [muTmp(tmp)]) 
    ]);
}

// Translate Reducer

MuExp translateReducer(init, result, generators){
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    pushIt(tmp);
    code = [ muAssignTmp(tmp, translate(init)), muWhile(loopname, muAll([translate(g) | g <-generators]), [muAssignTmp(tmp, translate(result))]), muTmp(tmp)];
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
       println("es = <es>");
       for(elem <- es){
           println("elem = <elem>");
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

MuExp translateVisit(label, \visit) { throw "visit"; }
