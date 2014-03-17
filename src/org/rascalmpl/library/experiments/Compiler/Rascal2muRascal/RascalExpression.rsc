@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalExpression

import Prelude;
import lang::rascal::\syntax::Rascal;
import lang::rascal::types::TestChecker;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TypeInstantiation;
import lang::rascal::types::TypeExceptions;
import experiments::Compiler::Rascal2muRascal::TmpAndLabel;
import experiments::Compiler::Rascal2muRascal::RascalModule;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalStatement;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeReifier;
import experiments::Compiler::muRascal::AST;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::RVM::Interpreter::ParsingTools;
import experiments::Compiler::muRascal::MuAllMuOr;

/*
 * Translate a Rascal expression to muRascal using the function: MuExp translate(Expression e).
 */
 
/*********************************************************************/
/*                  Auxiliary functions                              */
/*********************************************************************/

//int size_exps({Expression ","}* es) = size([e | e <- es]);		     // TODO: should become library function
//int size_exps({Expression ","}+ es) = size([e | e <- es]);		     // TODO: should become library function
//int size_assignables({Assignable ","}+ es) = size([e | e <- es]);	 // TODO: should become library function
int size_keywordArguments(KeywordArguments keywordArguments) = 
    (keywordArguments is \default) ? size([kw | kw <- keywordArguments.keywordArgumentList]) : 0;

// Produces multi- or backtrack-free expressions
MuExp makeMu(str muAllOrMuOr, list[MuExp] exps) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMu(muAllOrMuOr,topFunctionScope(),exps);
    //functions_in_module = functions_in_module + res.functions;
    addFunctionsToModule(res.functions);
    return res.e;
}

MuExp makeMuMulti(MuExp exp) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMuMulti(exp,topFunctionScope());
    //functions_in_module = functions_in_module + res.functions;
    addFunctionsToModule(res.functions);
    return res.e;
}

MuExp makeMuOne(str muAllOrMuOr, list[MuExp] exps) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMuOne(muAllOrMuOr,topFunctionScope(),exps);
    //functions_in_module = functions_in_module + res.functions;
    addFunctionsToModule(res.functions);
    return res.e;
}

// Generate code for completely type-resolved operators

bool isContainerType(str t) = t in {"list", "map", "set", "rel", "lrel"};

bool areCompatibleContainerTypes({"list", "lrel"}) = true;
bool areCompatibleContainerTypes({"set", "rel"}) = true;
bool areCompatibleContainerTypes({str c}) = true;
default bool areCompatibleContainerTypes(set[str] s) = false;

str reduceContainerType("lrel") = "list";
str reduceContainerType("rel") = "set";
default str reduceContainerType(str c) = c;

str typedBinaryOp(str lot, str op, str rot) {
  //lot = reduceContainerType(lot);
  //rot = reduceContainerType(rot);
  if(lot == "value" || rot == "value" || lot == "parameter" || rot == "parameter"){
     return op;
  }
  if(isContainerType(lot))
     return areCompatibleContainerTypes({lot, rot}) ? "<lot>_<op>_<rot>" : "<lot>_<op>_elm";
  else
     return isContainerType(rot) ? "elm_<op>_<rot>" : "<lot>_<op>_<rot>";
}

MuExp infix(str op, Expression e) = 
  muCallPrim(typedBinaryOp(getOuterType(e.lhs), op, getOuterType(e.rhs)), 
             [*translate(e.lhs), *translate(e.rhs)]);

MuExp infix_elm_left(str op, Expression e){
   rot = getOuterType(e.rhs);
   //rot = reduceContainerType(rot);
   return muCallPrim("elm_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

MuExp infix_rel_lrel(str op, Expression e){
  lot = getOuterType(e.lhs);
  if(lot == "set") lot = "rel"; else if (lot == "list") lot = "lrel";
  rot = getOuterType(e.rhs);
  if(rot == "set") rot = "rel"; else if (rot == "list") rot = "lrel";
  return muCallPrim("<lot>_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

str typedUnaryOp(str ot, str op) = (ot == "value" || ot == "parameter") ? op : "<op>_<ot>";
 
MuExp prefix(str op, Expression arg) {
  return muCallPrim(typedUnaryOp(getOuterType(arg), op), [translate(arg)]);
}

MuExp postfix(str op, Expression arg) = muCallPrim(typedUnaryOp(getOuterType(arg), op), [translate(arg)]);

MuExp postfix_rel_lrel(str op, Expression arg) {
  ot = getOuterType(arg);
  if(ot == "set" ) ot = "rel"; else if(ot == "list") ot = "lrel";
  return muCallPrim("<ot>_<op>", [translate(arg)]);
}

set[str] numeric = {"int", "real", "rat", "num"};

MuExp comparison(str op, Expression e) {
  lot = reduceContainerType(getOuterType(e.lhs));
  rot = reduceContainerType(getOuterType(e.rhs));
  //println("comparison: op = <op>, lot = <lot>, rot = <rot>");
  if(lot == "value" || rot == "value"){
     lot = ""; rot = "";
  } else {
    if(lot in numeric) lot += "_"; else lot = "";
 
    if(rot in numeric) rot = "_" + rot; else rot = "";
  }
  lot = reduceContainerType(lot);
  rot = reduceContainerType(rot);
  return muCallPrim("<lot><op><rot>", [*translate(e.lhs), *translate(e.rhs)]);
}

// Determine constant expressions

bool isConstantLiteral((Literal) `<LocationLiteral src>`) = src.protocolPart is nonInterpolated;
bool isConstantLiteral((Literal) `<StringLiteral n>`) = n is nonInterpolated;
default bool isConstantLiteral(Literal l) = true;

bool isConstant(Expression e:(Expression)`{ <{Expression ","}* es> }`) = size(es) == 0 || all(elm <- es, isConstant(elm));
bool isConstant(Expression e:(Expression)`[ <{Expression ","}* es> ]`)  = size(es) == 0 ||  all(elm <- es, isConstant(elm));
bool isConstant(Expression e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) = size(mappings) == 0 || all(m <- mappings, isConstant(m.from), isConstant(m.to));

bool isConstant(e:(Expression) `\< <{Expression ","}+ elements> \>`) = size(elements) == 0 ||  all(elm <- elements, isConstant(elm));
bool isConstant((Expression) `<Literal s>`) = isConstantLiteral(s);
default bool isConstant(Expression e) = false;

value getConstantValue(Expression e) = readTextValueString("<e>");

/*********************************************************************/
/*                  Translate Literals                               */
/*********************************************************************/

// -- boolean literal  -----------------------------------------------

MuExp translate((Literal) `<BooleanLiteral b>`) = "<b>" == "true" ? muCon(true) : muCon(false);

// -- integer literal  -----------------------------------------------
 
MuExp translate((Literal) `<IntegerLiteral n>`) = muCon(toInt("<n>"));

// -- regular expression literal  ------------------------------------

MuExp translate((Literal) `<RegExpLiteral r>`) { throw "RexExpLiteral cannot occur in expression"; }

// -- string literal  ------------------------------------------------

MuExp translate((Literal) `<StringLiteral n>`) = translateStringLiteral(n);

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

private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <StringTemplate template> <StringTail tail>`) {
    str fuid = topFunctionScope();
	preResult = nextTmp();
	return muBlock( [ muAssignTmp(preResult, fuid, translateChars("<pre>")),
                      muCallPrim("template_addunindented", [ translateTemplate(template, computeIndent(pre), preResult, fuid), *translateTail(tail)])
                    ]);
}
    
private MuExp translateStringLiteral((StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    str fuid = topFunctionScope();
    preResult = nextTmp();
    return muBlock( [ muAssignTmp(preResult, fuid, translateChars("<pre>")),
					  muCallPrim("template_addunindented", [ translateTemplate(expression, computeIndent(pre), preResult, fuid), *translateTail(tail)])
					]   );
}
                    
private MuExp translateStringLiteral((StringLiteral)`<StringConstant constant>`) = muCon(readTextValueString("<constant>"));

private str removeMargins(str s)  = visit(s) { case /^[ \t]*'/m => "" };

private str computeIndent(str s) {
   lines = split("\n", removeMargins(s)); 
   return isEmpty(lines) ? "" : left("", size(lines[-1]));
} 

private str computeIndent(PreStringChars pre) = computeIndent(removeMargins("<pre>"[1..-1]));
private str computeIndent(MidStringChars mid) = computeIndent(removeMargins("<mid>"[1..-1]));

private MuExp translateChars(str s) = muCon(removeMargins(s[1..-1]));

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

public MuExp translateMiddle((StringMiddle) `<MidStringChars mid>`) = muCon(removeMargins("<mid>"[1..-1]));

public MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <StringTemplate template> <StringMiddle tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
   			          muCallPrim("template_addunindented", [ translateTemplate(template, computeIndent(mid), midResult, fuid), translateMiddle(tail) ])
   			        ]);
   	}

public MuExp translateMiddle((StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
                      muCallPrim("template_addunindented", [ translateTemplate(expression, computeIndent(mid), midResult, fuid), translateMiddle(tail) ])
                    ]);
}
  
/*
syntax StringTail
	= midInterpolated: MidStringChars mid Expression expression StringTail tail 
	| post: PostStringChars post 
	| midTemplate: MidStringChars mid StringTemplate template StringTail tail ;
*/

private list[MuExp] translateTail((StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return [ muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
                      muCallPrim("template_addunindented", [ translateTemplate(expression, computeIndent(mid), midResult, fuid), *translateTail(tail)])
                    ])
           ];
}
	
private list[MuExp] translateTail((StringTail) `<PostStringChars post>`) {
  content = removeMargins("<post>"[1..-1]);
  return size(content) == 0 ? [] : [muCon(content)];
}

private list[MuExp] translateTail((StringTail) `<MidStringChars mid> <StringTemplate template> <StringTail tail>`) {
    str fuid = topFunctionScope();
    midResult = nextTmp();
    return [ muBlock( [ muAssignTmp(midResult, fuid, translateChars("<mid>")),
                        muCallPrim("template_addunindented", [ translateTemplate(template, computeIndent(mid), midResult, fuid), *translateTail(tail) ])
                    ])
           ];
 }  
 
 private MuExp translateTemplate(Expression e, str indent, str preResult, str prefuid){
    str fuid = topFunctionScope();
    result = nextTmp();
    return muBlock([ muAssignTmp(result, fuid, muCallPrim("template_open", [muCon(indent), muTmp(preResult,prefuid)])),
    				 muAssignTmp(result, fuid, muCallPrim("template_add", [ muTmp(result,fuid), muCallPrim("value_to_string", [translate(e)]) ])),
                     muCallPrim("template_close", [muTmp(result,fuid)])
                   ]);
 }
 
// -- location literal  ----------------------------------------------

MuExp translate((Literal) `<LocationLiteral src>`) = translateLocationLiteral(src);
 
 /*
 syntax LocationLiteral
	= \default: ProtocolPart protocolPart PathPart pathPart ;
 */
 
 private MuExp translateLocationLiteral((LocationLiteral) `<ProtocolPart protocolPart> <PathPart pathPart>`) =
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

private MuExp translateProtocolPart((ProtocolPart) `<ProtocolChars protocolChars>`) = muCon("<protocolChars>"[1..]);
 
private MuExp translateProtocolPart((ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`) =
    muCallPrim("str_add_str", [muCon("<pre>"[1..-1]), translate(expression), translateProtocolTail(tail)]);
 
private MuExp  translateProtocolTail((ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`) =
   muCallPrim("str_add_str", [muCon("<mid>"[1..-1]), translate(expression), translateProtocolTail(tail)]);
   
private MuExp translateProtocolTail((ProtocolTail) `<PostProtocolChars post>`) = muCon("<post>"[1 ..]);

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

private MuExp translatePathPart((PathPart) `<PathChars pathChars>`) = muCon("<pathChars>"[..-1]);
private MuExp translatePathPart((PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`) =
   muCallPrim("str_add_str", [ muCon("<pre>"[..-1]), translate(expression), translatePathTail(tail)]);

private MuExp translatePathTail((PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`) =
   muCallPrim("str_add_str", [ muCon("<mid>"[1..-1]), translate(expression), translatePathTail(tail)]);
   
private MuExp translatePathTail((PathTail) `<PostPathChars post>`) = muCon("<post>"[1..-1]);

// -- all other literals  --------------------------------------------

default MuExp translate((Literal) `<Literal s>`) =  muCon(readTextValueString("<s>"));


/*********************************************************************/
/*                  Translate expressions                            */
/*********************************************************************/

// -- literal expression ---------------------------------------------

MuExp translate(e:(Expression)  `<Literal s>`) = translate(s);

// -- concrete syntax expression  ------------------------------------

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

//MuExp translateConcrete(e:(ConcreteHole) `\< <Sym symbol> <Name name> \>`){
//  println("***** ConcreteHole, name: <name>");
//  iprint(e);
//  return muCallPrim("list_subscript_int", [muCallPrim("adt_field_access", [mkVar("<name>", name@\loc), muCon("args")]), muCon(7)]);
//}

default MuExp translateConcrete(e: appl(Production cprod, list[Tree] cargs)){ 
    fragType = getType(e@\loc);
    println("translateConcrete, fragType = <fragType>");
    reifiedFragType = symbolToValue(fragType, config);
    println("translateConcrete, reified: <reifiedFragType>");
    Tree parsedFragment = parseFragment(getModuleName(), reifiedFragType, e, e@\loc, getGrammar(config));
    return translateConcreteParsed(parsedFragment);
}

default MuExp translateConcrete(t) = muCon(t);

MuExp translateConcreteParsed(e: appl(Production prod, list[Tree] args)){
   if(prod.def == label("hole", lex("ConcretePart"))){
       varloc = args[0].args[4].args[0]@\loc;		// TODO: refactor (see concrete patterns)
       <fuid, pos> = getVariableScope("ConcreteVar", varloc);
       return muVar("ConcreteVar", fuid, pos);
    }    
    return muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
                   [muCon(prod), muCallPrim("list_create", [translateConcreteParsed(arg) | arg <- args])]);
}

default MuExp translateConcreteParsed(Tree t) = muCon(t);

// -- block expression ----------------------------------------------

MuExp translate(e:(Expression) `{ <Statement+ statements> }`) = muBlock([translate(stat) | stat <- statements]);

// -- parenthesized expression --------------------------------------

MuExp translate(e:(Expression) `(<Expression expression>)`)   = translate(expression);

// -- closure expression --------------------------------------------

MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) = translateClosure(e, parameters, statements);

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) = translateClosure(e, parameters, statements);

// Translate a closure   
 
 MuExp translateClosure(Expression e, Parameters parameters, Tree cbody) {
 	uid = loc2uid[e@\loc];
	fuid = uid2str(uid);
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	bool isVarArgs = (varArgs(_,_) := parameters);
  	
  	// Keyword parameters
    list[MuExp] kwps = translateKeywordParameters(parameters, fuid, getFormals(uid), e@\loc);
    
    // TODO: we plan to introduce keyword patterns as formal parameters
    MuExp body = translateFunction(parameters.formals.formals, isVarArgs, kwps, cbody, []);
    
    tuple[str fuid,int pos] addr = uid2addr[uid];
    addFunctionToModule(muFunction(fuid, ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									  getFormals(uid), getScopeSize(fuid), 
  									  isVarArgs, e@\loc, [], (), 
  									  body));
  	
  	leaveFunctionScope();								  
  	
	return (addr.fuid == uid2str(0)) ? muFun(fuid) : muFun(fuid, addr.fuid); // closures are not overloaded
}

MuExp translateBoolClosure(Expression e){
    tuple[str fuid,int pos] addr = <topFunctionScope(),-1>;
	fuid = addr.fuid + "/non_gen_at_<e@\loc>()";
	
	enterFunctionScope(fuid);
	
    ftype = Symbol::func(Symbol::\bool(),[]);
	nformals = 0;
	nlocals = 0;
	bool isVarArgs = false;
  	
    MuExp body = muReturn(translate(e));
    addFunctionToModule(muFunction(fuid, ftype, addr.fuid, nformals, nlocals, isVarArgs, e@\loc, [], (), body));
  	
  	leaveFunctionScope();								  
  	
	return muFun(fuid, addr.fuid); // closures are not overloaded

}

// -- enumerator with range expression ------------------------------

MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`) {
    kind = getOuterType(first) == "int" && getOuterType(last) == "int" ? "_INT" : "";
    return muMulti(muApply(mkCallToLibFun("Library", "RANGE<kind>", 3), [ translatePat(pat), translate(first), translate(last)]));
 }

// -- enumerator with range and step expression ---------------------
    
MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`) {
     kind = getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int" ? "_INT" : "";
     return muMulti(muApply(mkCallToLibFun("Library", "RANGE_STEP<kind>", 4), [ translatePat(pat), translate(first), translate(second), translate(last)]));
}

// -- range expression ----------------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  loopname = nextLabel(); 
  writer = asTmp(loopname);
  var = nextTmp();
  patcode = muApply(mkCallToLibFun("Library","MATCH_VAR",2), [muTmpRef(var,fuid)]);

  kind = getOuterType(first) == "int" && getOuterType(last) == "int" ? "_INT" : "";
  rangecode = muMulti(muApply(mkCallToLibFun("Library", "RANGE<kind>", 3), [ patcode, translate(first), translate(last)]));
  
  return
    muBlock(
    [ muAssignTmp(writer, fuid, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMu("ALL", [ rangecode ]), [ muCallPrim("listwriter_add", [muTmp(writer,fuid), muTmp(var,fuid)])]),
      muCallPrim("listwriter_close", [muTmp(writer,fuid)]) 
    ]);
    
}

// -- range with step expression ------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  loopname = nextLabel(); 
  writer = asTmp(loopname);
  var = nextTmp();
  patcode = muApply(mkCallToLibFun("Library","MATCH_VAR",2), [muTmpRef(var,fuid)]);

  kind = getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int" ? "_INT" : "";
  rangecode = muMulti(muApply(mkCallToLibFun("Library", "RANGE_STEP<kind>", 4), [ patcode, translate(first), translate(second), translate(last)]));
  
  return
    muBlock(
    [ muAssignTmp(writer, fuid, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMu("ALL", [ rangecode ]), [ muCallPrim("listwriter_add", [muTmp(writer,fuid), muTmp(var,fuid)])]),
      muCallPrim("listwriter_close", [muTmp(writer,fuid)]) 
    ]);
}

// -- visit expression ----------------------------------------------

MuExp translate (e:(Expression) `<Label label> <Visit visitItself>`) = translateVisit(label, visitItself);



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
	enterFunctionScope(phi_fuid);
	
	MuExp body = translateVisitCases([ c | Case c <- \visit.cases ],phi_fuid);
	
	// ***Note: (see issue #434) 
	//    (1) All the variables introduced within a visit scope should become local variables of the phi-function
	//    (2) All the nested functions (a) introduced within a visit scope or (b) introduced within the phi's scope as part of translation are affected
	// TODO: It seems possible to perform this lifting during translation 
	rel[str fuid,int pos] decls = getAllVariablesAndFunctionsOfBlockScope(\visit@\loc);
	// Starting from the number of formal parameters (iSubject, matched, hasInsert)
	int pos_in_phi = 3;
	// Map from <scopeId,pos> to <phi_fuid,newPos>
	map[tuple[str,int],tuple[str,int]] mapping = ();
	for(<str fuid,int pos> <- decls, pos != -1) {
	    assert fuid == scopeId;
	    mapping[<scopeId,pos>] = <phi_fuid,pos_in_phi>;
	    pos_in_phi = pos_in_phi + 1;
	}
	body = lift(body,scopeId,phi_fuid,mapping);
	//functions_in_module = lift(functions_in_module,scopeId,phi_fuid,mapping);
	setFunctionsInModule(lift(getFunctionsInModule(),scopeId,phi_fuid,mapping));
	
	addFunctionToModule(muFunction(phi_fuid, phi_ftype, scopeId, 3, pos_in_phi, false, \visit@\loc, [], (), body));
	
	leaveFunctionScope();
	leaveVisit();
	
	if(fixpoint) {
		str phi_fixpoint_fuid = scopeId + "/" + "PHI_FIXPOINT_<i>";
		
		enterFunctionScope(phi_fixpoint_fuid);
		
		// Local variables of 'phi_fixpoint_fuid': 'iSubject', 'matched', 'hasInsert', 'changed', 'val'
		list[MuExp] body = [];
		body += muAssign("changed", phi_fixpoint_fuid, 3, muBool(true));
		body += muWhile(nextLabel(), muVar("changed",phi_fixpoint_fuid,3), 
						[ muAssign("val", phi_fixpoint_fuid, 4, muCall(muFun(phi_fuid,scopeId), [ muVar("iSubject",phi_fixpoint_fuid,0), muVar("matched",phi_fixpoint_fuid,1), muVar("hasInsert",phi_fixpoint_fuid,2) ])),
						  muIfelse(nextLabel(), makeMu("ALL", [ muCallPrim("equal",[ muVar("val",phi_fixpoint_fuid,4), muVar("iSubject",phi_fixpoint_fuid,0) ]) ]),
						  						[ muAssign("changed",phi_fixpoint_fuid,3,muBool(false)) ], 
						  						[ muAssign("iSubject",phi_fixpoint_fuid,0,muVar("val",phi_fixpoint_fuid,4)) ] )]);
		body += muReturn(muVar("iSubject",phi_fixpoint_fuid,0));
		
		leaveFunctionScope();
		
		addFunctionToModule(muFunction(phi_fixpoint_fuid, phi_ftype, scopeId, 3, 5, false, \visit@\loc, [], (), muBlock(body)));
	
	    // Local variables of the surrounding function
		str hasMatch = asTmp(nextLabel());
		str beenChanged = asTmp(nextLabel());
		return muBlock([ muAssignTmp(hasMatch,scopeId,muBool(false)),
						 muAssignTmp(beenChanged,scopeId,muBool(false)),
					 	 muCall(traverse_fun, [ muFun(phi_fixpoint_fuid,scopeId), translate(\visit.subject), muTmpRef(hasMatch,scopeId), muTmpRef(beenChanged,scopeId), muBool(rebuild) ]) 
				   	   ]);
	}
	
	// Local variables of the surrounding function
	str hasMatch = asTmp(nextLabel());
	str beenChanged = asTmp(nextLabel());
	return muBlock([ muAssignTmp(hasMatch,scopeId,muBool(false)), 
	                 muAssignTmp(beenChanged,scopeId,muBool(false)),
					 muCall(traverse_fun, [ muFun(phi_fuid,scopeId), translate(\visit.subject), muTmpRef(hasMatch,scopeId), muTmpRef(beenChanged,scopeId), muBool(rebuild) ]) 
				   ]);
}

@doc{Generates the body of a phi function}
MuExp translateVisitCases(list[Case] cases,str fuid) {
	// TODO: conditional
	if(size(cases) == 0) {
		return muReturn(muVar("subject",fuid,0));
	}
	
	c = head(cases);
	
	if(c is patternWithAction) {
		pattern = c.patternWithAction.pattern;
		typePat = getType(pattern@\loc);
		cond = muMulti(muApply(translatePat(pattern), [ muVar("subject",fuid,0) ]));
		ifname = nextLabel();
		enterBacktrackingScope(ifname);
		if(c.patternWithAction is replacing) {
			replacement = translate(c.patternWithAction.replacement.replacementExpression);
			list[MuExp] conditions = [];
			if(c.patternWithAction.replacement is conditional) {
				conditions = [ translate(e) | Expression e <- c.patternWithAction.replacement.conditions ];
			}
			replacementType = getType(c.patternWithAction.replacement.replacementExpression@\loc);
			tcond = muCallPrim("subtype", [ muTypeCon(replacementType), muCallPrim("typeOf", [ muVar("iSubject",fuid,0) ]) ]);
			list[MuExp] cbody = [ muAssignVarDeref("matched",fuid,1,muBool(true)), muAssignVarDeref("hasInsert",fuid,2,muBool(true)), replacement ];
        	exp = muIfelse(ifname, makeMu("ALL",[ cond,tcond,*conditions ]), [ muReturn(muBlock(cbody)) ], [ translateVisitCases(tail(cases),fuid) ]);
        	leaveBacktrackingScope();
        	return exp;
		} else {
			// Arbitrary
			statement = c.patternWithAction.statement;
			\case = translate(statement);
			insertType = topCaseType();
			clearCaseType();
			tcond = muCallPrim("subtype", [ muTypeCon(insertType), muCallPrim("typeOf", [ muVar("iSubject",fuid,0) ]) ]);
			list[MuExp] cbody = [ muAssignVarDeref("matched",fuid,1,muBool(true)) ];
			if(!(muBlock([]) := \case)) {
				cbody += \case;
			}
			cbody += muReturn(muVar("subject",fuid,0));
			exp = muIfelse(ifname, makeMu("ALL",[ cond,tcond ]), cbody, [ translateVisitCases(tail(cases),fuid) ]);
        	leaveBacktrackingScope();
			return exp;
		}
	} else {
		// Default
		return muBlock([ muAssignVarDeref("matched",fuid,1,muBool(true)), translate(c.statement), muReturn(muVar("iSubject",fuid,0)) ]);
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




// -- reducer expression --------------------------------------------

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(init, result, generators);

MuExp translateReducer(init, result, generators){
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    pushIt(tmp,fuid);
    code = [ muAssignTmp(tmp, fuid, translate(init)), muWhile(loopname, makeMuMulti(makeMu("ALL", [ translate(g) | g <- generators ])), [muAssignTmp(tmp,fuid,translate(result))]), muTmp(tmp,fuid)];
    popIt();
    return muBlock(code);
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) { throw("reifiedType"); }
//  muCon(symbolToValue(symbol, config)); // TODO

// -- call expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`){

   MuExp kwargs = translateKeywordArguments(keywordArguments);
      
   MuExp receiver = translate(expression);
   list[MuExp] args = [ translate(a) | a <- arguments ];
   if(getOuterType(expression) == "str") {
       return muCallPrim("node_create", [receiver, *args] + (size_keywordArguments(keywordArguments) > 0 ? [kwargs] : [/* muCon(()) */]));
   }
   
   if(getOuterType(expression) == "loc"){
       return muCallPrim("loc_with_offset_create", [receiver, *args]);
   }
   
   if(muFun(str _) := receiver || muFun(str _, str _) := receiver || muConstr(str _) := receiver) {
       return muCall(receiver, args + [ kwargs ]);
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
               if(/parameter(_,_) := t) { // In case of polymorphic function types
                   try {
                       if(isConstructorType(t) && isConstructorType(ftype)) {
                           bindings = match(\tuple([ a | Symbol arg <- getConstructorArgumentTypes(t),     label(_,Symbol a) := arg || Symbol a := arg ]),
                                            \tuple([ a | Symbol arg <- getConstructorArgumentTypes(ftype), label(_,Symbol a) := arg || Symbol a := arg ]),());
                           bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
                           return instantiate(t.\adt,bindings) == ftype.\adt;
                       }
                       if(isFunctionType(t) && isFunctionType(ftype)) {
                           bindings = match(getFunctionArgumentTypesAsTuple(t),getFunctionArgumentTypesAsTuple(ftype),());
                           bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
                           return instantiate(t.ret,bindings) == ftype.ret;
                       }
                       return false;
                   } catch invalidMatch(_,_,_): {
                       return false;
                   } catch invalidMatch(_,_): {
                       return false; 
                   } catch err: {
                       println("WARNING: Cannot match <ftype> against <t> for location: <expression@\loc>! <err>");
                   }
               }
               return t == ftype;
           }           
           if(isOverloadedType(ftype)) {
               if(/parameter(_,_) := t) { // In case of polymorphic function types
                   for(Symbol alt <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype))) {
                       try {
           	               if(isConstructorType(t) && isConstructorType(alt)) {
           	                   bindings = match(\tuple([ a | Symbol arg <- getConstructorArgumentTypes(t),   label(_,Symbol a) := arg || Symbol a := arg ]),
           	                                    \tuple([ a | Symbol arg <- getConstructorArgumentTypes(alt), label(_,Symbol a) := arg || Symbol a := arg ]),());
           	                   bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
           	                   return instantiate(t.\adt,bindings) == alt.\adt;
           	               }
           	               if(isFunctionType(t) && isFunctionType(alt)) {
           	                   bindings = match(getFunctionArgumentTypesAsTuple(t),getFunctionArgumentTypesAsTuple(alt),());
           	                   bindings = bindings + ( name : \void() | /parameter(str name,_) := t, name notin bindings );
           	                   return instantiate(t.ret,bindings) == alt.ret;
           	               }
           	               return false;
           	           } catch invalidMatch(_,_,_): {
           	               ;
                       } catch invalidMatch(_,_): {
                           ;
                       } catch err: {
                           println("WARNING: Cannot match <alt> against <t> for location: <expression@\loc>! <err>");
                       }
                   }
                   return false;
           	   }
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
       if(isEmpty(resolved)) {
           for(int alt <- of.alts) {
               t = fuid2type[alt];
               matches(t);
               println("ALT: <t> ftype: <ftype>");
           }
           throw "ERROR in overloading resolution: <ftype>; <expression@\loc>";
       }
       bool exists = <of.scopeIn,resolved> in overloadedFunctions;
       if(!exists) {
           i = size(overloadedFunctions);
           overloadedFunctions += <of.scopeIn,resolved>;
       } else {
           i = indexOf(overloadedFunctions, <of.scopeIn,resolved>);
       }
       
       overloadingResolver[ofqname] = i;
       return muOCall(muOFun(ofqname), args + [ kwargs ]);
   }
   if(isOverloadedFunction(receiver) && receiver.fuid notin overloadingResolver) {
      throw "The use of a function has to be managed via overloading resolver!";
   }
   // Push down additional information if the overloading resolution needs to be done at runtime
   return muOCall(receiver, 
   				  isFunctionType(ftype) ? Symbol::\tuple([ ftype ]) : Symbol::\tuple([ t | Symbol t <- getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype) ]), 
   				  args + [ kwargs ]);
}

MuExp translateKeywordArguments(KeywordArguments keywordArguments) {
   // Keyword arguments
   if(keywordArguments is \default){
      kwargs = [ muCon("<kwarg.name>"), translate(kwarg.expression)  | KeywordArgument kwarg <- keywordArguments.keywordArgumentList ];
      if(size(kwargs) > 0){
         return muCallPrim("map_create", kwargs);
      }
   }
   return muCon(());
   
   //str fuid = topFunctionScope();
   //list[MuExp] kwargs = [ muAssignTmp("map_of_keyword_arguments", fuid, muCallPrim("mapwriter_open",[])) ];
   //if(keywordArguments is \default) {
   //    for(KeywordArgument kwarg <- keywordArguments.keywordArgumentList) {
   //        kwargs += muCallPrim("mapwriter_add",[ muTmp("map_of_keyword_arguments",fuid), muCon("<kwarg.name>"), translate(kwarg.expression) ]);           
   //    }
   //}
   //return muBlock([ *kwargs, muCallPrim("mapwriter_close", [ muTmp("map_of_keyword_arguments",fuid) ]) ]);
}

// -- any expression ------------------------------------------------

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = makeMuOne("ALL",[ translate(g) | g <- generators ]);

// -- all expression ------------------------------------------------

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) {
  // First split generators with a top-level && operator
  generators1 = [*(((Expression) `<Expression e1> && <Expression e2>` := g) ? [e1, e2] : [g]) | g <- generators];
  isGen = [!backtrackFree(g) | g <- generators1];
  tgens = [];
  for(i <- index(generators1)) {
     gen = generators1[i];
     //println("all <i>: <gen>");
     if(isGen[i]){
	 	tgen = translate(gen);
	 	if(muMulti(exp) := tgen){ // Unwraps muMulti, if any
	 	   tgen = exp;
	 	}
	 	tgens += tgen;
	 } else {
	    tgens += translateBoolClosure(gen);
	 }
  }
  //gens = [isGen[i] ? translate(generators2[i]).exp // Unwraps muMulti 
  //                 : translateBoolClosure(generators2[i]) | i <- index(generators1)];
  return muCall(mkCallToLibFun("Library", "RASCAL_ALL", 2), [ muCallMuPrim("make_array", tgens), muCallMuPrim("make_array", [ muBool(b) | bool b <- isGen ]) ]);
}

// -- comprehension expression --------------------------------------

MuExp translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

private MuExp translateGenerators({Expression ","}+ generators){
   if(all(gen <- generators, backtrackFree(gen))){
      return makeMu("ALL",[translate(g) | g <-generators]);
   } else {
     return makeMu("ALL",[muCallPrim("rbool", [translate(g)]) | g <-generators]);
   }
}

private list[MuExp] translateComprehensionContribution(str kind, str tmp, str fuid, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muCallPrim("<kind>writer_splice", [muTmp(tmp,fuid), translate(exp)]);
	    } else {
	      append muCallPrim("<kind>writer_add", [muTmp(tmp,fuid), translate(r)]);
	    }
	  }
} 

private MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname);
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim("listwriter_open", [])),
      muWhile(loopname, makeMuMulti(makeMu("ALL",[ translate(g) | g <- generators ])), translateComprehensionContribution("list", tmp, fuid, [r | r <- results])),
      muCallPrim("listwriter_close", [muTmp(tmp,fuid)]) 
    ]);
}

private MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim("setwriter_open", [])),
      muWhile(loopname, makeMuMulti(makeMu("ALL",[ translate(g) | g <- generators ])), translateComprehensionContribution("set", tmp, fuid, [r | r <- results])),
      muCallPrim("setwriter_close", [muTmp(tmp,fuid)]) 
    ]);
}

private MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim("mapwriter_open", [])),
      muWhile(loopname, makeMuMulti(makeMu("ALL",[ translate(g) | g <- generators ])), [muCallPrim("mapwriter_add", [muTmp(tmp,fuid)] + [ translate(from), translate(to)])]), 
      muCallPrim("mapwriter_close", [muTmp(tmp,fuid)]) 
    ]);
}

// -- set expression ------------------------------------------------

MuExp translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) = translateSetOrList(es, "set");

// -- list expression -----------------------------------------------

MuExp translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`)  = translateSetOrList(es, "list");

// Translate SetOrList including spliced elements

private bool containSplices(es) = any(e <- es, e is splice);

private MuExp translateSetOrList(es, str kind){
 if(containSplices(es)){
       str fuid = topFunctionScope();
       writer = nextTmp();
       enterWriter(writer);
       code = [ muAssignTmp(writer, fuid, muCallPrim("<kind>writer_open", [])) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim("<kind>writer_splice", [muTmp(writer,fuid), translate(elem.argument)]);
            } else {
              code += muCallPrim("<kind>writer_add", [muTmp(writer,fuid), translate(elem)]);
           }
       }
       code += [ muCallPrim("<kind>writer_close", [ muTmp(writer,fuid) ]) ];
       leaveWriter();
       return muBlock(code);
    } else {
      if(size(es) == 0 || all(elm <- es, isConstant(elm))){
         return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      } else 
        return muCallPrim("<kind>_create", [ translate(elem) | elem <- es ]);
    }
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `# <Type tp>`) = muCon(symbolToValue(translateType(tp),config));

// -- tuple expression ----------------------------------------------

MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) {
    if(isConstant(e)){
      return muCon(readTextValueString("<e>"));
    } else
        return muCallPrim("tuple_create", [ translate(elem) | elem <- elements ]);
}

// -- map expression ------------------------------------------------

MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) {
   if(isConstant(e)){
     return muCon(readTextValueString("<e>"));
   } else 
     return muCallPrim("map_create", [ translate(m.from), translate(m.to) | m <- mappings ]);
}   

// -- it expression (in reducer) ------------------------------------

MuExp translate (e:(Expression) `it`) = muTmp(topIt().name,topIt().fuid);
 
 // -- qualified name expression -------------------------------------
 
MuExp translate((Expression) `<QualifiedName v>`) = translate(v);
 
MuExp translate(q:(QualifiedName) `<QualifiedName v>`) = mkVar("<v>", v@\loc);

// For the benefit of names in regular expressions

MuExp translate((Name) `<Name name>`) = mkVar("<name>", name@\loc);

// -- subscript expression ------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    if(ot in {"sort", "iter", "iter-star", "iter-seps", "iter-star-seps"}){
       op = "nonterminal_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    } else
    if(ot notin {"map", "rel", "lrel"}) {
       op += "_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    
    return muCallPrim(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts]);
}

// -- slice expression ----------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

// -- slice with step expression ------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) =
    muCallPrim("<getOuterType(expression)>_slice", [ translate(expression), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ]);

MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muCon("false") : translate(optExp.expression);

MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) =
    muCallPrim("<getOuterType(expression)>_slice", [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ]);

// -- field access expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) {
   tp = getType(expression@\loc);
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       return translate((Expression)`<Expression expression> \< <Name field> \>`);
   }
   op = isNonTerminalType(tp) ? "nonterminal" : getOuterType(expression);
   return muCallPrim("<op>_field_access", [ translate(expression), muCon("<field>") ]);
}

// -- field update expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) {
   
    tp = getType(expression@\loc);  
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTType(tp)){
        return muCallPrim("adt_field_update", [ translate(expression), muCon("<key>"), translate(replacement) ]);
    } else if(isLocType(tp)){
     	return muCallPrim("loc_field_update", [ translate(expression), muCon("<key>"), translate(replacement) ]);
    }
    if(tupleHasFieldNames(tp)){
    	  fieldNames = getTupleFieldNames(tp);
    }	
    return muCallPrim("<getOuterType(expression)>_update", [ translate(expression), muCon(indexOf(fieldNames, "<key>")), translate(replacement) ]);
}

// -- field project expression --------------------------------------

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

// -- set annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) =
    muCallPrim("annotation_set", [translate(expression), muCon("<name>"), translate(val)]);

// -- get annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression> @ <Name name>`) =
    muCallPrim("annotation_get", [translate(expression), muCon("<name>")]);

// -- is expression --------------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim("is", [translate(expression), muCon("<name>")]);

// -- has expression -----------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) = 
    muCon(hasField(getType(expression@\loc), "<name>"));   

// -- transitive closure expression ---------------------------------

MuExp translate(e:(Expression) `<Expression argument> +`)   = postfix_rel_lrel("transitive_closure", argument);

// -- transitive reflexive closure expression -----------------------

MuExp translate(e:(Expression) `<Expression argument> *`)   = postfix_rel_lrel("transitive_reflexive_closure", argument);

// -- isDefined expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression argument> ?`)  = generateIfDefinedOtherwise(muBlock([ translate(argument), muCon(true) ]),  muCon(false));

// -- isDefinedOtherwise expression ---------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`)  = generateIfDefinedOtherwise(translate(lhs), translate(rhs));

MuExp generateIfDefinedOtherwise(MuExp muLHS, MuExp muRHS) {
    str fuid = topFunctionScope();
    str varname = asTmp(nextLabel());
	// Check if evaluation of the expression throws a 'NoSuchKey' or 'NoSuchAnnotation' exception;
	// do this by checking equality of the value constructor names
	cond1 = muCallMuPrim("equal", [ muCon("UninitializedVariable"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children_and_keyword_params_as_map", [ muTmp(asUnwrapedThrown(varname),fuid) ]), muInt(0) ] ) ]);
	cond3 = muCallMuPrim("equal", [ muCon("NoSuchKey"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children_and_keyword_params_as_map", [ muTmp(asUnwrapedThrown(varname),fuid) ]), muInt(0) ] ) ]);
	cond2 = muCallMuPrim("equal", [ muCon("NoSuchAnnotation"),
									muCallMuPrim("subscript_array_mint", [ muCallMuPrim("get_name_and_children_and_keyword_params_as_map", [ muTmp(asUnwrapedThrown(varname),fuid) ]), muInt(0) ] ) ]);
	
	elsePart3 = muIfelse(nextLabel(), cond3, [ muRHS ], [ muThrow(muTmp(varname,fuid)) ]);
	elsePart2 = muIfelse(nextLabel(), cond2, [ muRHS ], [ elsePart3 ]);
	catchBody = muIfelse(nextLabel(), cond1, [ muRHS ], [ elsePart2 ]);
	return muTry(muLHS, muCatch(varname, fuid, Symbol::\adt("RuntimeException",[]), catchBody), 
			  		 	muBlock([]));
}

// -- not expression ------------------------------------------------

MuExp translate(e:(Expression) `!<Expression argument>`)    = translateBool(e);

// -- negate expression ---------------------------------------------

MuExp translate(e:(Expression) `-<Expression argument>`)    = prefix("negative", argument);

// -- splice expression ---------------------------------------------

MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice cannot occur outside set or list";
}
   
// -- asType expression ---------------------------------------------

MuExp translate(e:(Expression) `[ <Type typ> ] <Expression argument>`)  =
   muCallPrim("parse", [muCon(getModuleName()), muCon(type(symbolToValue(translateType(typ), config).symbol,getGrammar(config))), translate(argument)]);
   
// -- composition expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> o <Expression rhs>`)   = infix_rel_lrel("compose", e);

// -- product expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> * <Expression rhs>`)   = infix("product", e);

// -- join expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)   = infix("join", e);

// -- remainder expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)   = infix("remainder", e);

// -- division expression -------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)   = infix("divide", e);

// -- intersection expression ---------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> & <Expression rhs>`)   = infix("intersect", e);

// -- addition expression -------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)   = infix("add", e);

// -- subtraction expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)   = infix("subtract", e);

// -- insert before expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)   = infix("add", e);

// -- append after expression ---------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)   = infix("add", e);

// -- modulo expression ---------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)   = infix("mod", e);

// -- notin expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)   = infix_elm_left("notin", e);

// -- in expression -------------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`)   = infix_elm_left("in", e);

// -- greater equal expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = infix("greaterequal", e);

// -- less equal expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = infix("lessequal", e);

// -- less expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)  = infix("less", e);

// -- greater expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)  = infix("greater", e);

// -- equal expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)  = comparison("equal", e);

// -- not equal expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)  = comparison("notequal", e);


// -- no match expression -------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`)  = translateMatch(e);

// -- match expression ----------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`)     = translateMatch(e);

// -- enumerate expression ------------------------------------------

MuExp translate(e:(Expression) `<QualifiedName name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_AND_ASSIGN", 2), [muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Type tp> <Name name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_CHECK_AND_ASSIGN", 3), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`) =
    muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_AND_MATCH", 2), [translatePat(pat), translate(exp)]));

// -- implies expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`)  = translateBool(e);

// -- equivalent expression -----------------------------------------
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`)  = translateBool(e);

// -- and expression ------------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> && <Expression rhs>`)  = translateBool(e);

// -- or expression -------------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> || <Expression rhs>`)  = translateBool(e);
 
// -- conditional expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) =
	// ***Note that the label (used to backtrack) here is not important (no backtracking scope is pushed) 
	// as it is not allowed to have 'fail' in conditional expressions
	muIfelse(nextLabel(),translate(condition), [translate(thenExp)],  [translate(elseExp)]);

// -- any other expression (should not happn) ------------------------

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
    top-down visit(e){
    //case (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments keywordArguments>)`:
    //	return true;
    case (Expression) `all ( <{Expression ","}+ generators> )`: 
    	return true;
    case (Expression) `any ( <{Expression ","}+ generators> )`: 
    	return true;
    case (Expression) `<Pattern pat> \<- <Expression exp>`: 
    	return false;
    case (Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`: 
    	return false;
    case (Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`: 
    	return false;
    case (Expression) `<Pattern pat> := <Expression exp>`:
    	return false;
    	case (Expression) `<Pattern pat> !:= <Expression exp>`:
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
   
MuExp translateBool(e: (Expression) `<Pattern pat> !:= <Expression exp>`) = translateMatch(e);

// All other expressions are translated as ordinary expression

default MuExp translateBool(Expression e) {
   //println("translateBool, default: <e>");
   return translate(e);
}
   
// Translate Boolean operators

MuExp translateBoolBinaryOp(str fun, Expression lhs, Expression rhs){
    switch(fun){
    	case "and": return makeMu("ALL",[translate(lhs), translate(rhs)]);
    	case "or":  return makeMu("OR",[translate(lhs), translate(rhs)]);
    	case "implies": return makeMu("IMPLICATION",[ translate(lhs), translate(rhs) ]);
    	case "equivalent": return makeMu("EQUIVALENCE",[ translate(lhs), translate(rhs) ]);
    	default:
    		throw "translateBoolBinary: unknown operator <fun>";
    }
}

MuExp translateBoolNot(Expression lhs){
  if(backtrackFree(lhs)){
  	  return muCallMuPrim("not_mbool", [translateBool(lhs)]);
  	} else {
  	  return muCallMuPrim("not_mbool", [ makeMu("ALL",[translate(lhs)]) ]);
  	}
}

