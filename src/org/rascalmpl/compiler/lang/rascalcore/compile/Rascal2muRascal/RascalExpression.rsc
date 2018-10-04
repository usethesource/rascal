@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalExpression

import IO;
import ValueIO;
import String;
import Node;
import Map;
import Set;
import ParseTree;
import util::Reflective;
import Exception;
//import lang::rascalcore::compile::RVM::Interpreter::CompileTimeError;

import lang::rascal::\syntax::Rascal;

import lang::rascalcore::compile::muRascal::AST;
import lang::rascalcore::compile::muRascal::MuBoolExp;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::RascalConstantCall;

import lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration;
import lang::rascalcore::compile::Rascal2muRascal::RascalPattern;
import lang::rascalcore::compile::Rascal2muRascal::RascalStatement;
import lang::rascalcore::compile::Rascal2muRascal::RascalConstantCall;

//import lang::rascalcore::compile::RVM::Interpreter::ParsingTools;

/*
 * Translate a Rascal expression to muRascal using the function: 
 * - MuExp translate(Expression e).
 */
 
/*********************************************************************/
/*                  Auxiliary functions                              */
/*********************************************************************/

//// Produce a multi-valued or backtrack-free Boolean expression
//MuExp makeBoolExp(str operator, list[MuExp] exps, loc src) {
//    tuple[MuExp e,list[MuFunction] functions] res = makeBoolExp(operator,topFunctionScope(),exps,src);
//    addFunctionsToModule(res.functions);
//    return res.e;
//}
//
//// Produce a multi-valued Boolean expression
//MuExp makeMultiValuedBoolExp(str operator, list[MuExp] exps, loc src) {
//    tuple[MuExp e,list[MuFunction] functions] res = makeMultiValuedBoolExp(operator, topFunctionScope(),exps, src);
//    addFunctionsToModule(res.functions);
//    return res.e;
//}
//
//// Produce a single-valued Boolean expression
//MuExp makeSingleValuedBoolExp(str operator, list[MuExp] exps, loc src) {
//    tuple[MuExp e,list[MuFunction] functions] res = makeSingleValuedBoolExp(operator,topFunctionScope(),exps,src);
//    addFunctionsToModule(res.functions);
//    return res.e;
//}

// Generate code for completely type-resolved operators

private bool isContainerType(str t) = t in {"list", "map", "set", "rel", "lrel"};

private bool areCompatibleContainerTypes({"list", "lrel"}) = true;
private bool areCompatibleContainerTypes({"set", "rel"}) = true;
private bool areCompatibleContainerTypes({str c}) = true;
private default bool areCompatibleContainerTypes(set[str] s) = false;

private str reduceContainerType("lrel") = "list";
private str reduceContainerType("rel") = "set";
private default str reduceContainerType(str c) = c;

public str typedBinaryOp(str lot, str op, str rot) {
  if(lot == "value" || rot == "value" || lot == "parameter" || rot == "parameter" || lot == "void" || rot == "void"){
     return op;
  }
  if(isContainerType(lot)){
     if(areCompatibleContainerTypes({lot, rot})){
       return op in {"join", "compose"} ? "<lot>_<op>_<rot>" : "<reduceContainerType(lot)>_<op>_<reduceContainerType(rot)>";
     } else {
       return "<reduceContainerType(lot)>_<op>_elm";
     }
  } else
     return isContainerType(rot) ? "elm_<op>_<reduceContainerType(rot)>" : "<reduceContainerType(lot)>_<op>_<reduceContainerType(rot)>";
}

private MuExp infix(str op, Expression e) = 
  muCallPrim3(typedBinaryOp(getOuterType(e.lhs), op, getOuterType(e.rhs)), 
             [*translate(e.lhs), *translate(e.rhs)],
             e@\loc);

private MuExp infix_elm_left(str op, Expression e){
   rot = getOuterType(e.rhs);
   return muCallPrim3("elm_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)], e@\loc);
}

private MuExp infix_rel_lrel(str op, Expression e){
  lot = getOuterType(e.lhs);
  if(lot == "set") lot = "rel"; else if (lot == "list") lot = "lrel";
  rot = getOuterType(e.rhs);
  if(rot == "set") rot = "rel"; else if (rot == "list") rot = "lrel";
  return muCallPrim3("<lot>_<op>_<rot>", [*translate(e.lhs), *translate(e.rhs)], e@\loc);
}

private bool isStaticallyImpreciseType(AType tp) =
    tp == avoid() || tp == avalue() || aparameter(_,_) := tp;

// ----------- compose: exp o exp ----------------

private MuExp compose(Expression e){
  lhsType = getType(e.lhs);
  return isFunctionType(lhsType) || isOverloadedAType(lhsType) ? translateComposeFunction(e) : infix_rel_lrel("compose", e);
}

private MuExp translateComposeFunction(Expression e){
  //println("composeFunction: <e>");
  lhsType = getType(e.lhs);
  rhsType = getType(e.rhs);
  resType = getType(e);
  
  MuExp lhsReceiver = translate(e.lhs);
  MuExp rhsReceiver = translate(e.rhs);
  
  //println("lhsReceiver: <lhsReceiver>");
  //println("rhsReceiver: <rhsReceiver>");
  
  str ofqname = "<lhsReceiver.fuid>_o_<rhsReceiver.fuid>#<e@\loc.offset>_<e@\loc.length>";  // name of composition
  
  if(hasOverloadingResolver(ofqname)){
    return muOFun(ofqname);
  }
  
  // Unique 'id' of a visit in the function body
  //int i = nextVisit();  // TODO: replace by generated function counter
    
  // Generate and add a function COMPOSED_FUNCTIONS_<i>
  str scopeId = topFunctionScope();
  str comp_name = "COMPOSED_FUNCTIONS_<e@\loc.offset>_<e@\loc.length>";
  str comp_fuid = scopeId + "/" + comp_name;
  
  AType comp_ftype;  
  int nargs;
  if(isFunctionType(resType)) {
     nargs = size(resType.parameters);
     comp_ftype = resType;
  } else {
     nargs = size(getFirstFrom(resType.overloads).parameters);
     for(t <- resType.overloads){
         if(size(t.formals) != nargs){
            throw "cannot handle composition/overloading for different arities";
         }
     }
     comp_ftype = afunc(avalue(), [avalue() | int j <- [0 .. nargs]], []);
  }
    
  enterFunctionScope(comp_fuid);
  //kwargs = muCallMuPrim("make_mmap", []);
  kwargs = muCallMuPrim("copy_and_update_keyword_mmap", [muCon(nargs)]);
  rhsCall = muOCall4(rhsReceiver, atuple(atypeList([rhsType])), [muVar("parameter_<comp_name>", comp_fuid, j) | int j <- [0 .. nargs]] + [ kwargs ], e.rhs@\loc);
  body_exps =  [muReturn1(muOCall4(lhsReceiver, atuple(atypeList([lhsType])), [rhsCall, kwargs ], e.lhs@\loc))];
   
  leaveFunctionScope();
  fun = muFunction(comp_fuid, comp_name, comp_ftype, ["a", "b"], /*atuple(atypeList([])),*/ scopeId, nargs, 2, false, false, true, \e@\loc, [], (), false, 0, 0, muBlock(body_exps));
 
  int uid = declareGeneratedFunction(comp_name, comp_fuid, comp_ftype, e@\loc);
  addFunctionToModule(fun);  
  addOverloadedFunctionAndResolver(ofqname, <comp_name, comp_ftype, getModuleName(), [uid]>);
 
  return muOFun(ofqname);
}

// ----------- addition: exp + exp ----------------

private  MuExp add(Expression e){
    lhsType = getType(e.lhs);
    return isFunctionType(lhsType) /*|| isOverloadedType(lhsType)*/ ? translateAddFunction(e) :infix("add", e);
}

private MuExp translateAddFunction(Expression e){
  //println("translateAddFunction: <e>");
  lhsType = getType(e.lhs);
  rhsType = getType(e.rhs);
  
  str2uid = invertUnique(uid2str);

  MuExp lhsReceiver = translate(e.lhs);
  OFUN lhsOf;
  
  if(hasOverloadingResolver(lhsReceiver.fuid)){
    lhsOf = getOverloadedFunction(lhsReceiver.fuid);
  } else {
    uid = str2uid[lhsReceiver.fuid];
    lhsOf = <lhsReceiver.fuid, lhsType, topFunctionScope(), [uid]>;
    addOverloadedFunctionAndResolver(lhsReceiver.fuid, lhsOf);
  }
 
  MuExp rhsReceiver = translate(e.rhs);
  OFUN rhsOf;
  
  if( hasOverloadingResolver(rhsReceiver.fuid)){
    rhsOf = getOverloadedFunction(rhsReceiver.fuid);
  } else {
    uid = str2uid[rhsReceiver.fuid];
    rhsOf = <rhsReceiver.fuid, rhsType, topFunctionScope(), [uid]>;
    addOverloadedFunctionAndResolver(rhsReceiver.fuid, rhsOf);
  }
  
   str ofqname = "<lhsReceiver.fuid>_+_<rhsReceiver.fuid>#<e@\loc.offset>_<e@\loc.length>";  // name of addition
 
  OFUN compOf = <ofqname, lhsType, lhsOf[2], lhsOf[3] + rhsOf[3]>; // add all alternatives
  
 
 
  addOverloadedFunctionAndResolver(ofqname, compOf); 
  return muOFun(ofqname);
}

private str typedUnaryOp(str ot, str op) = (ot == "value" || ot == "parameter") ? op : "<op>_<ot>";
 
private MuExp prefix(str op, Expression arg) {
  return muCallPrim3(typedUnaryOp(getOuterType(arg), op), [translate(arg)], arg@\loc);
}

private MuExp postfix(str op, Expression arg) = muCallPrim3(typedUnaryOp(getOuterType(arg), op), [translate(arg)], arg@\loc);

private MuExp postfix_rel_lrel(str op, Expression arg) {
  ot = getOuterType(arg);
  if(ot == "set" ) ot = "rel"; else if(ot == "list") ot = "lrel";
  return muCallPrim3("<ot>_<op>", [translate(arg)], arg@\loc);
}

private set[str] numeric = {"int", "real", "rat", "num"};

MuExp comparison(str op, Expression e) {
 
  lot = reduceContainerType(getOuterType(e.lhs));
  rot = reduceContainerType(getOuterType(e.rhs));
  
  if(lot == "value" || rot == "value" || lot == "void" || rot == "void" || lot == "parameter" || rot == "parameter"){
     lot = ""; rot = "";
  } else {
    if(lot in numeric) lot += "_"; else lot = "";
 
    if(rot in numeric) rot = "_" + rot; else rot = "";
  }
  lot = reduceContainerType(lot);
  rot = reduceContainerType(rot);
  return muCallPrim3("<lot><op><rot>", [*translate(e.lhs), *translate(e.rhs)], e@\loc);
}

/*********************************************************************/
/*                  Translate Literals                               */
/*********************************************************************/

// -- boolean literal  -----------------------------------------------

MuExp translate((Literal) `<BooleanLiteral b>`) = 
    "<b>" == "true" ? muCon(true) : muCon(false);
    
MuExp translateBool((Literal) `<BooleanLiteral b>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    "<b>" == "true" ? trueCont : falseCont;

// -- integer literal  -----------------------------------------------
 
MuExp translate((Literal) `<IntegerLiteral n>`) = 
    muCon(toInt("<n>"));

// -- regular expression literal  ------------------------------------

MuExp translate((Literal) `<RegExpLiteral r>`) { 
    throw "RexExpLiteral cannot occur in expression"; 
}

// -- string literal  ------------------------------------------------

MuExp translate((Literal) `<StringLiteral n>`) = 
    translateStringLiteral(n);

/* Recap of relevant rules from Rascal grammar:

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

// -- translateStringLiteral

private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <StringTemplate template> <StringTail tail>`) {
    preIndent = computeIndent(pre);
	return muBlock( [ muCallPrim3("template_open", translatePreChars(pre), pre@\loc),
                      *translateTemplate(preIndent, template),
                      *translateTail(preIndent, tail),
                      muCallPrim3("template_close", [], tail@\loc)
                    ]);
}
    
private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    preIndent = computeIndent(pre);
    return muBlock( [ muCallPrim3("template_open", translatePreChars(pre), pre@\loc),
    				  *translateExpInStringLiteral(preIndent, expression),
    				  *translateTail(preIndent, tail),
    				  muCallPrim3("template_close", [], tail@\loc)
					]   );
}
                    
private MuExp translateStringLiteral(s: (StringLiteral)`<StringConstant constant>`) =
	muCon(readTextValueString(removeMargins("<constant>")));

// --- translateExpInStringLiteral

private list[MuExp] translateExpInStringLiteral(str indent, Expression expression){   
    if(indent == ""){
    	return [ muCallPrim3("template_add", [translate(expression)], expression@\loc)];
    }	
	return [ muCallPrim3("template_indent", [muCon(indent)], expression@\loc),
    	     muCallPrim3("template_add", [translate(expression)], expression@\loc),
    		 muCallPrim3("template_unindent", [muCon(indent)], expression@\loc)
    	   ];
}
// --- removeMargins

private str removeMargins(str s) {
	if(findFirst(s, "\n") < 0){
		return s;
	} else {
		res = visit(s) { case /^[ \t]*\\?'/m => ""  /*case /^[ \t]+$/m => ""*/};
	    //println("RascalExpression::removeMargins: <s> =\> <res>");
		return res;
	}
}

// --- computeIndent 

private str computeIndent(str s) {
   removed = removeMargins(s);
   if(endsWith(s, "\n")){
      return "";
   }
   lines = split("\n", removed); 
   return isEmpty(lines) ? "" : left("", size(lines[-1]));
} 

private str computeIndent(PreStringChars pre) = computeIndent(removeMargins(/*deescape(*/"<pre>"[1..-1]/*)*/));
private str computeIndent(MidStringChars mid) = computeIndent(removeMargins(/*deescape(*/"<mid>"[1..-1]/*)*/));

private list[MuExp] translatePreChars(PreStringChars pre) {
   spre = removeMargins("<pre>"[1..-1]);
   return "<spre>" == "" ? [] : [ muCon(deescape(spre)) ];
}	

private list[MuExp] translateMidChars(MidStringChars mid) {
  smid = removeMargins("<mid>"[1..-1]);
  return "<mid>" == "" ? [] : [ muCallPrim3("template_add", [ muCon(deescape(smid)) ], mid@\loc) ];	//?
}
                     
/* Recap of relevant rules from Rascal grammar:

   syntax StringTemplate
	   = ifThen    : "if"    "(" {Expression ","}+ conditions ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	   | ifThenElse: "if"    "(" {Expression ","}+ conditions ")" "{" Statement* preStatsThen StringMiddle thenString Statement* postStatsThen "}" "else" "{" Statement* preStatsElse StringMiddle elseString Statement* postStatsElse "}" 
	   | \for       : "for"   "(" {Expression ","}+ generators ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" 
	   | doWhile   : "do"    "{" Statement* preStats StringMiddle body Statement* postStats "}" "while" "(" Expression condition ")" 
	   | \while     : "while" "(" Expression condition ")" "{" Statement* preStats StringMiddle body Statement* postStats "}" ;
	
   syntax StringMiddle
	   = mid: MidStringChars mid 
	   | template: MidStringChars mid StringTemplate template StringMiddle tail 
	   | interpolated: MidStringChars mid Expression expression StringMiddle tail ;
	
   syntax StringTail
        = midInterpolated: MidStringChars mid Expression expression StringTail tail 
        | post: PostStringChars post 
        | midTemplate: MidStringChars mid StringTemplate template StringTail tail ;
*/

// --- translateMiddle

public list[MuExp] translateMiddle(str indent, (StringMiddle) `<MidStringChars mid>`) {
	mids = removeMargins("<mid>"[1..-1]);
	return mids == "" ? [] : [ muCallPrim3("template_add", [muCon(deescape(mids))], mid@\loc) ];	// ?
}

public list[MuExp] translateMiddle(str indent, s: (StringMiddle) `<MidStringChars mid> <StringTemplate template> <StringMiddle tail>`) {
	midIndent = computeIndent(mid);
    return [ *translateMidChars(mid),
   			 *translateTemplate(indent + midIndent, template),
   			 *translateMiddle(indent, tail)
   		   ];
   	}

public list[MuExp] translateMiddle(str indent, s: (StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) {
	midIndent = computeIndent(mid);
    return [ *translateMidChars(mid),
    		 *translateExpInStringLiteral(midIndent, expression),
             *translateMiddle(indent + midIndent, tail)
           ];
}

// --- translateTail

private list[MuExp] translateTail(str indent, s: (StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) {
    midIndent = computeIndent(mid);
    return [ muBlock( [ *translateMidChars(mid),
    					*translateExpInStringLiteral(midIndent, expression),
                        *translateTail(indent + midIndent, tail)
                    ])
           ];
}
	
private list[MuExp] translateTail(str indent, (StringTail) `<PostStringChars post>`) {
  content = removeMargins("<post>"[1..-1]);
  return size(content) == 0 ? [] : [muCallPrim3("template_add", [ muCon(deescape(content)) ], post@\loc)];
}

private list[MuExp] translateTail(str indent, s: (StringTail) `<MidStringChars mid> <StringTemplate template> <StringTail tail>`) {
    midIndent = computeIndent(mid);
    return [ muBlock( [ *translateMidChars(mid),
                        *translateTemplate(indent + midIndent, template),
                        *translateTail(indent + midIndent,tail)
                    ])
           ];
 } 
 
 // --- translateTemplate 
 
 private list[MuExp] translateTemplate(str indent, Expression expression){
 	return translateExpInStringLiteral(indent, expression);
 }
 
// -- location literal  ----------------------------------------------

MuExp translate((Literal) `<LocationLiteral src>`) = 
    translateLocationLiteral(src);
 
/* Recap of relevant rules from Rascal grammar:
   syntax LocationLiteral
	   = \default: ProtocolPart protocolPart PathPart pathPart ;
	
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
        = "\>" URLChars "|" ;
 */
 
private MuExp translateLocationLiteral(l: (LocationLiteral) `<ProtocolPart protocolPart> <PathPart pathPart>`) =
     muCallPrim3("loc_create", [muCallPrim3("str_add_str", [translateProtocolPart(protocolPart), translatePathPart(pathPart)], l@\loc)], l@\loc);
 
private MuExp translateProtocolPart((ProtocolPart) `<ProtocolChars protocolChars>`) = muCon("<protocolChars>"[1..]);
 
private MuExp translateProtocolPart(p: (ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`) =
    muCallPrim3("str_add_str", [muCon("<pre>"[1..-1]), translate(expression), translateProtocolTail(tail)], p@\loc);
 
private MuExp  translateProtocolTail(p: (ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`) =
   muCallPrim3("str_add_str", [muCon("<mid>"[1..-1]), translate(expression), translateProtocolTail(tail)], p@\loc);
   
private MuExp translateProtocolTail((ProtocolTail) `<PostProtocolChars post>`) = muCon("<post>"[1 ..]);

private MuExp translatePathPart((PathPart) `<PathChars pathChars>`) = muCon("<pathChars>"[..-1]);

private MuExp translatePathPart(p: (PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`) =
   muCallPrim3("str_add_str", [ muCon("<pre>"[..-1]), translate(expression), translatePathTail(tail)], p@\loc);

private MuExp translatePathTail(p: (PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`) =
   muCallPrim3("str_add_str", [ muCon("<mid>"[1..-1]), translate(expression), translatePathTail(tail)], p@\loc);
   
private MuExp translatePathTail((PathTail) `<PostPathChars post>`) = muCon("<post>"[1..-1]);

// -- all other literals  --------------------------------------------

default MuExp translate((Literal) `<Literal s>`) {
    try {
        return muCon(readTextValueString("<s>"));
    } catch e: {
        throw CompileTimeError(error("<e>", s@\loc));
    }
}

/*********************************************************************/
/*                  Translate expressions                            */
/*********************************************************************/

// -- literal expression ---------------------------------------------

MuExp translate(e:(Expression)  `<Literal s>`) = 
    translate(s);
    
MuExp translateBool(e:(Expression)  `<Literal s>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    translateBool(s, btscope, trueCont, falseCont);


// -- concrete syntax expression  ------------------------------------

MuExp translate(e:(Expression) `<Concrete concrete>`) {
    return translateConcrete(concrete);
}

/* Recap of relevant rules from Rascal grammar:

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
    
   Recap from ParseTree declaration:
  
   data Tree 
        = appl(Production prod, list[Tree] args)
        | cycle(Symbol symbol, int cycleLength) 
        | amb(set[Tree] alternatives)  
        | char(int character)
        ;
*/


public Tree parseConcrete(e: appl(Production cprod, list[Tree] cargs)){
	fragType = getType(e);
    //println("translateConcrete, fragType = <fragType>");
    reifiedFragType = symbolToValue(fragType);
    // TODO: getGrammar uses a global variable. Add as parameter to the call stack instead
    try {
        return parseFragment(getModuleName(), getModuleTags(), reifiedFragType, e, e@\loc, getGrammar());
    } catch ParseError(loc src): {
        throw CompileTimeError(error("Parse error in concrete fragment or pattern", src));
    } catch Ambiguity(loc src, str stype, str string): {
        throw CompileTimeError(error("Ambiguity in concrete fragment or pattern (of type <stype>)", src));
    }
}  

public MuExp translateConcrete(e: appl(Production cprod, list[Tree] cargs)){ 
    fragType = getType(e);
    //println("translateConcrete, fragType = <fragType>");
    //reifiedFragType = symbolToValue(fragType);
    //println("translateConcrete, reified: <reifiedFragType>");
    //Tree parsedFragment = parseFragment(getModuleName(), reifiedFragType, e, e@\loc, getGrammar());
    Tree parsedFragment = parseConcrete(e);
    //println("parsedFragment, before"); iprintln(parsedFragment);
    return translateConcreteParsed(parsedFragment, parsedFragment@\loc);
}

private default MuExp translateConcrete(lang::rascal::\syntax::Rascal::Concrete c) = muCon(c);

private MuExp translateConcreteParsed(Tree e, loc src){
   if(t:appl(Production prod, list[Tree] args) := e){
       my_src = e@\loc ? src;
       //iprintln("translateConcreteParsed:"); iprintln(e);
       if(isConcreteHole(t)){
           varloc = getConcreteHoleVarLoc(t);
           //println("varloc = <getType(varloc)>");
           <fuid, pos> = getVariableScope("ConcreteVar", varloc);
           
           return muVar("ConcreteVar", fuid, pos);
        } 
        MuExp translated_elems;
        if(any(arg <- args, isConcreteListVar(arg))){ 
           //println("splice in concrete list");      
           str fuid = topFunctionScope();
           writer = nextTmp();
        
           translated_args = [ muCallPrim3(isConcreteListVar(arg) ? "listwriter_splice_concrete_list_var" : "listwriter_add", 
                                          [muTmp(writer,fuid), translateConcreteParsed(arg, my_src)], my_src)
                             | Tree arg <- args
                             ];
           translated_elems = muBlockWithTmps(
                                      [<writer, fuid>],
                                      [],
                                      [ muAssignTmp(writer, fuid, muCallPrim3("listwriter_open", [], my_src)),
                                        *translated_args,
                                        muCallPrim3("listwriter_close", [muTmp(writer,fuid)], my_src) 
                                      ]);
        } else {
           translated_args = [translateConcreteParsed(arg, my_src) | Tree arg <- args];
           if(allConstant(translated_args)){
        	  return muCon(appl(prod, [ce | muCon(Tree ce) <- translated_args])[@\loc=my_src]);
           }
           translated_elems = muCallPrim3("list_create", translated_args, my_src);
        }
        return muCallPrim3("annotation_set", [muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
                                                    [muCon(prod), translated_elems, muCallMuPrim("make_mmap", []), muTypeCon(avoid())]),
        								     muCon("loc"), 
        								     muCon(my_src)], e@\loc);
        //return muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
        //              [muCon(prod), translated_elems, muTypeCon(avoid())]);
    } else {
        return muCon(e);
    }
}

private bool isConcreteListVar(e: appl(Production prod, list[Tree] args)){
   if(isConcreteHole(e)){
      varloc = getConcreteHoleVarLoc(e);
      varType = getType(varloc);
      typeName = getName(varType);
      return typeName in {"iter", "iter-star", "iter-seps", "iter-star-seps"};
   }
   return false;
}

private default bool isConcreteListVar(Tree t) = false;

//default MuExp translateConcreteParsed(Tree t) = muCon(t);

// -- block expression ----------------------------------------------

MuExp translate(e:(Expression) `{ <Statement+ statements> }`) = 
    muBlock([translate(stat) | Statement stat <- statements]);

// -- parenthesized expression --------------------------------------

MuExp translate(e:(Expression) `(<Expression expression>)`) =
     translate(expression);
     
MuExp translateBool(e:(Expression) `(<Expression expression>)`, str btscope, MuExp trueCont, MuExp falseCont) =
     translateBool(expression, btscope, trueCont, falseCont);

// -- closure expression --------------------------------------------

MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) =
    translateClosure(e, parameters, statements);

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) =
    translateClosure(e, parameters, statements);

// Translate a closure   
 
 private MuExp translateClosure(Expression e, Parameters parameters, Tree cbody) {
 	uid = e@\loc;
	fuid = convert2fuid(uid);
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.formals);
	bool isVarArgs = ftype.varArgs;
  	
  	// Keyword parameters
    list[MuExp] kwps = translateKeywordParameters(parameters, fuid, getFormals(uid), e@\loc);
    
    // TODO: we plan to introduce keyword patterns as formal parameters
    MuExp body = translateFunction("CLOSURE", parameters.formals.formals, isVarArgs, kwps, cbody, false, []);
    
    fuid = convert2fuid(uid);
    
    addFunctionToModule(muFunction(fuid, "CLOSURE", ftype, getParameterNames(parameters.formals.formals), /*atuple(atypeList([])),*/ /*(addr.fuid in moduleNames) ? "" : addr.*/fuid, 
  									  getFormals(uid), getScopeSize(uid), 
  									  isVarArgs, false, false, e@\loc, [], (), 
  									  false, 0, 0,
  									  body));
  	
  	leaveFunctionScope();								  
  	return muFun1(fuid); // TODO!
	//return (addr.fuid == convert2fuid(0)) ? muFun1(fuid) : muFun2(fuid, addr.fuid); // closures are not overloaded
}

//private MuExp translateBoolClosure(Expression e){
//    tuple[str fuid,int pos] addr = <topFunctionScope(),-1>;
//	fuid = addr.fuid + "/non_gen_at_<e@\loc>()";
//	
//	enterFunctionScope(fuid);
//	
//    ftype = afunc(abool(),[],[]);
//	nformals = 0;
//	nlocals = 0;
//	bool isVarArgs = false;
//  	
//    MuExp body = muReturn1(translate(e));
//    addFunctionToModule(muFunction(fuid, "CLOSURE", ftype, [], atuple(atypeList([])), addr.fuid, nformals, nlocals, isVarArgs, false, true, e@\loc, [], (), false, 0, 0, body));
//  	
//  	leaveFunctionScope();								  
//  	
//	return muFun2(fuid, addr.fuid); // closures are not overloaded
//
//}

// -- range expression ----------------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) {
  //println("range: <e>");
  str fuid = topFunctionScope();
  loopname = nextLabel(); 
  writer = asTmp(loopname);
  var = nextTmp();
  patcode = muApply(mkCallToLibFun("Library","MATCH_VAR"), [muTmpRef(var,fuid)]);

  kind = getOuterType(first) == "int" && getOuterType(last) == "int" ? "_INT" : "";
  rangecode = muMulti(muApply(mkCallToLibFun("Library", "RANGE<kind>"), [ patcode, translate(first), translate(last)]));
  
  return
    muBlockWithTmps(
    [ <writer, fuid> ],
    [ <var, fuid> ],
    [ muAssignTmp(writer, fuid, muCallPrim3("listwriter_open", [], e@\loc)),
      muWhile(loopname, makeBoolExp("ALL", [ rangecode ], e@\loc), [ muCallPrim3("listwriter_add", [muTmp(writer,fuid), muTmp(var,fuid)], e@\loc)]),
      muCallPrim3("listwriter_close", [muTmp(writer,fuid)], e@\loc) 
    ]);
    
}

// -- range with step expression ------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  loopname = nextLabel(); 
  writer = asTmp(loopname);
  var = nextTmp();
  patcode = muApply(mkCallToLibFun("Library","MATCH_VAR"), [muTmpRef(var,fuid)]);

  kind = getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int" ? "_INT" : "";
  rangecode = muMulti(muApply(mkCallToLibFun("Library", "RANGE_STEP<kind>"), [ patcode, translate(first), translate(second), translate(last)]));
  
  return
    muBlockWithTmps(
    [ <writer, fuid> ],
    [ <var, fuid> ],
    [ muAssignTmp(writer, fuid, muCallPrim3("listwriter_open", [], e@\loc)),
      muWhile(loopname, makeBoolExp("ALL", [ rangecode ], e@\loc), [ muCallPrim3("listwriter_add", [muTmp(writer,fuid), muTmp(var,fuid)], e@\loc)]),
      muCallPrim3("listwriter_close", [muTmp(writer,fuid)], e@\loc) 
    ]);
}

// -- visit expression ----------------------------------------------

MuExp translate (e:(Expression) `<Label label> <Visit visitItself>`) = translateVisit(label, visitItself);

// Translate Visit
// 
// The global translation scheme is to translate each visit to a function PHI that contains
// all cases translted to a switch.
// For the fixedpoint strategies innermost and outermost, a wrapper function PHI_FIXPOINT is generated that
// carries out the fixed point computation. PHI and PHIFIXPOINT have 7 common formal parameters, and the latter has
// two extra local variables:
//
// PHI:	iSubject		PHI_FIXPOINT:	iSubject
//		matched							matched
//		hasInsert						hasInsert
//		leaveVisit						leaveVisit
//		begin							begin
//		end								end
//		descriptor						descriptor
//										changed
//										val

//,PHI functions

private int iSubjectPos = 0;
private int matchedPos = 1;
        int hasInsertPos = 2;		// Used in translation of insert statement
private int leaveVisitPos = 3;
private int beginPos = 4;
private int endPos = 5;

private int NumberOfPhiFormals = 6;
private int replacementPos = 6;
private int NumberOfPhiLocals = 7;

public MuExp translateVisit(Label label, lang::rascal::\syntax::Rascal::Visit \visit) {	

	subjectType = getType(\visit.subject);
	bool isStringSubject = false; //subjectType == \str();
	
	if(isStringSubject){		// Only phi function for string subjects need 'begin' and 'end' parameter
		NumberOfPhiFormals = 4;
		replacementPos = 4;
		NumberOfPhiLocals = 5;
	} else {
		NumberOfPhiFormals = 6;
		replacementPos = 6;
		NumberOfPhiLocals = 7;
	}
	
	// Unique 'id' of a visit in the function body
	int i = nextVisit();
	
	previously_declared_functions = size(getFunctionsInModule());
	
	// Generate and add a nested function 'phi'
	str scopeId = topFunctionScope();
	str phi_fuid = scopeId + "/" + "PHI_<i>";
	
	phi_args = [avalue(),	// iSubject
	            abool(),	// matched
	            abool(),	// hasInsert
	            abool()		// leaveVisit
	           ] +
	           (isStringSubject ? 
	           [aint(),		// begin
	            aint()		// end
	           ]
	           :
	           []);
	
	phi_argNames = ["iSubject", "matched", "hasInsert", "leaveVisit", "begin", "end"];
	AType phi_ftype = afunc(avalue(), phi_args, []);
	
	enterVisit();
	enterFunctionScope(phi_fuid);
	cases = [ c | Case c <- \visit.cases ];
	
	
	concreteMatch = hasConcretePatternsOnly(cases) 
	                && isConcreteType(subjectType); // || subjectType == adt("Tree",[]));
	
	//println("visit: <subjectType>, <concreteMatch>");
	MuExp body = translateVisitCases(phi_fuid, subjectType, concreteMatch, cases);
	
	reachable_syms = { avalue() };
	reachable_prods = {};
	if(optimizing()){
	   tc = getTypesAndConstructorsInVisit(cases);
	   <reachable_syms, reachable_prods> = getReachableTypes(subjectType, tc.constructors, tc.types, concreteMatch);
	   //println("reachableTypesInVisit: <reachable_syms>, <reachable_prods>");
	}
	
	descriptor = muCallPrim3("make_descendant_descriptor", [muCon(phi_fuid), muCon(reachable_syms), muCon(reachable_prods), muCon(concreteMatch), muCon(getDefinitions())], \visit.subject@\loc);
		
	bool direction = true;
	bool progress = true;
	bool fixedpoint = true;
	bool rebuild = true;
	
	if( Case c <- \visit.cases, (c is patternWithAction && c.patternWithAction is replacing 
									|| hasTopLevelInsert(c)) ) {
		rebuild = true;
	}
	
	if(\visit is defaultStrategy) {
		direction = true; progress = true; fixedpoint = false; 
	} else {
		switch("<\visit.strategy>") {
			case "bottom-up"      :   { 
										direction = true;  progress = true; fixedpoint = false;
									  }
			case "top-down"       :   { 
										direction = false; progress = true; fixedpoint = false; 
									  }
			case "bottom-up-break":   {
										direction = true; progress = false; fixedpoint = false; 
									  }
			case "top-down-break" :   { 
										direction = false; progress = false; fixedpoint = false;
									  }
			case "innermost"      :   { 
										direction = true; progress = true; fixedpoint = true; 
									  }
			case "outermost"      :   { 
			                            direction = false; progress = true; fixedpoint = true; 
			                          }
		}
	}
	
	// ***Note: (fixes issue #434) 
	//    (1) All the variables introduced within a visit scope should become local variables of the phi-function
	//    (2) All the nested functions (a) introduced within a visit scope or 
	//                                 (b) introduced within the phi's scope as part of translation
	//        are affected
	// It is possible to perform this lifting during translation 
	rel[str fuid,int pos] decls = getAllVariablesAndFunctionsOfBlockScope(\visit@\loc);
	
	//println("getAllVariablesAndFunctionsOfBlockScope:");
	//for(tup <- decls){
	//	println(tup);
	//}
	
	// Starting from the number of formal parameters (iSubject, matched, hasInsert, begin, end, descriptor) and local replacementPos
	int pos_in_phi = NumberOfPhiLocals;
	
	// Map from <scopeId,pos> to <phi_fuid,newPos>
	map[tuple[str,int],tuple[str,int]] mapping = ();
	for(<str fuid,int pos> <- decls, pos != -1) {
	    assert fuid == scopeId : "translateVisit: fuid != scopeId";
	    mapping[<scopeId,pos>] = <phi_fuid, pos_in_phi>;
	    pos_in_phi = pos_in_phi + 1;
	}
	
	//println("mapping");
	//for(k <- mapping){ println("\t<k>: <mapping[k]>"); }
	//println("lifting, scopeId = <scopeId>, phi_fuid = <phi_fuid>");
	//println("previously_declared_functions = <previously_declared_functions>, added by visit: <size(getFunctionsInModule()) - previously_declared_functions>");
	
	
	body = lift(body,scopeId,phi_fuid,mapping);

    all_functions = getFunctionsInModule();
    
    //println("all_functions");
    //for(f <- all_functions){
    //	println("<f.qname>, <f.src>, inside visit: <f.src < \visit@\loc>");
    //
    //}
    //print("sizes: <size(all_functions[ .. previously_declared_functions])>, <size(all_functions[previously_declared_functions..])>");
    
    lifted_functions = lift(all_functions[previously_declared_functions..], scopeId, phi_fuid, mapping);
    
    //println("size lifted_functions: <size(lifted_functions)>");
    
	setFunctionsInModule(all_functions[ .. previously_declared_functions] + lifted_functions);
	
	//setFunctionsInModule(lift(all_functions, scopeId, phi_fuid, mapping));
	
	//println("size functions after lift: <size(getFunctionsInModule())>");
	
	addFunctionToModule(muFunction(phi_fuid, "PHI", phi_ftype, phi_argNames, /*atuple(atypeList([])),*/ scopeId, NumberOfPhiFormals, pos_in_phi, false, false, true, \visit@\loc, [], (), false, 0, 0, body));
	
	leaveFunctionScope();
	leaveVisit();
	
	return traversalCall(scopeId, phi_fuid, descriptor, translate(\visit.subject), direction, progress, fixedpoint, rebuild);
}


private MuExp traversalCall(str scopeId, str phi_fuid, MuExp descriptor, MuExp subject, bool direction, bool progress, bool fixedpoint, bool rebuild){
	// Local variables of the surrounding function
	str hasMatch = asTmp(nextLabel());
	str beenChanged = asTmp(nextLabel());
	str leaveVisit = asTmp(nextLabel());
	str begin = asTmp(nextLabel());
	str end = asTmp(nextLabel());
	str val = asTmp(nextLabel());  // TODO: remove?
	
	return 
	   muBlockWithTmps(
	       [  <val, scopeId> ],
	       [ <hasMatch,scopeId>,
	         <beenChanged,scopeId>,
	         <leaveVisit,scopeId>,
	         <begin,scopeId>,
	         <end,scopeId>
	       ],
	       [ muVisit(direction, 
				   progress,
				   fixedpoint,
				   rebuild,
				   descriptor,
	               muFun2(phi_fuid,scopeId), 
				   subject, 
				   muTmpRef(hasMatch,scopeId), 
				   muTmpRef(beenChanged,scopeId), 
				   muTmpRef(leaveVisit,scopeId),
				   muTmpRef(begin,scopeId), 
				   muTmpRef(end,scopeId))
		   ]);
}
/*
 * The translated visit cases are placed in a function phi that is called by the traversal function.
 * Therefore, we have to distinguish two kinds of returns:
 * - a replacementReturn that returns a replacement value to the calling traversal function
 * - a leaveVisitReturn that should return to the calling traversal function and directly return from it.
 * This is implemented by setting a leaveVisit flag that has to be checked by the traversal function after each call to the phi function
 */
private MuExp replacementReturn(MuExp e) = muReturn1(e);
 
private MuExp leaveVisitReturn(str fuid, MuExp e) =
	muBlock([
		muAssignVarDeref("leaveVisit", fuid, leaveVisitPos, muBool(true)),
		muReturn1(e)
		]);

private MuExp translateStatementInVisitCase(str fuid, Statement stat){
	code = translate(stat);
	code = visit(code) { case muReturn0()  => leaveVisitReturn(fuid, muCon(false))
						 case muReturn1(e) => leaveVisitReturn(fuid, e)
						 case muInsert(e): { 
						 	ifname = nextLabel();
						    replcond = muCallPrim3("subtype_value_value", [ muVar("replacement", fuid, replacementPos),
		                                                                    muVar("iSubject", fuid, iSubjectPos)
		                                                                  ], stat@\loc);
		                                                        
		                    //replcond = muCallPrim3("subtype", [ muCallPrim3("typeOf", [ muVar("replacement", fuid, replacementPos) ], stat@\loc), 
                      //                                          muCallPrim3("typeOf", [ muVar("iSubject", fuid, iSubjectPos) ], stat@\loc) ], stat@\loc);
		                                                        
						    insert muBlock([ muAssign("replacement", fuid, replacementPos, e),
    				                         muIfelse(ifname, replcond,
    				                                  [ muAssignVarDeref("matched", fuid, matchedPos, muBool(true)), 
		                                                muAssignVarDeref("hasInsert", fuid, hasInsertPos, muBool(true)),      				          
    				                                    muReturn1(muVar("replacement", fuid, replacementPos)) ],
    				                                  [ /*muCon(666)*/ ])
    				                       ]);
    				     }
					
	 					};
	return code;
}

private map[int, MuExp]  addPatternWithActionCode(str fuid, AType subjectType, PatternWithAction pwa, map[int, MuExp] table, int key){
	cond = muMulti(muApply(translatePatInVisit(pwa.pattern, fuid, subjectType), [ muVar("iSubject", fuid, iSubjectPos) ]));
	ifname = nextLabel();
	ifname2 = nextLabel();
	enterBacktrackingScope(ifname);
	if(pwa is replacing) {
		replacement = translate(pwa.replacement.replacementExpression);
		list[MuExp] conditions = [];
		if(pwa.replacement is conditional) {
			conditions = [ translate(e) | Expression e <- pwa.replacement.conditions ];
		}
		
		// TODO: We (should) use the static type of the current subject here
		
		// e.g. muTypeCon(getType(pwa.pattern@\loc)) but that maybe too large.
		
		replcond = muCallPrim3("subtype_value_value", [ muVar("replacement", fuid, replacementPos), 
		                                                muVar("iSubject", fuid, iSubjectPos)
		                                              ], pwa@\loc);
		//replcond = muCallPrim3("subtype", [ muCallPrim3("typeOf", [ muVar("replacement", fuid, replacementPos) ], pwa.replacement.replacementExpression@\loc), 
  //                                          muCallPrim3("typeOf", [ muVar("iSubject", fuid, iSubjectPos) ], pwa@\loc) 
  //                                        ], pwa@\loc);
		
		                      
    	table[key] = muBlock([ muIfelse(ifname, makeBoolExp("ALL",[ cond, *conditions ], pwa.pattern@\loc), 
    				                    [ muAssign("replacement", fuid, replacementPos, replacement),
    				                      muIfelse(ifname2, replcond,
    				                               [ muAssignVarDeref("matched", fuid, matchedPos, muBool(true)), 
		                                             muAssignVarDeref("hasInsert", fuid, hasInsertPos, muBool(true)),      				          
    				                                 replacementReturn(muVar("replacement", fuid, replacementPos)) ],
    				                               [ /*muCon(666)*/ ])
    				                     ], 
    				                     [ /*muCon(777)*/ ]),  
    				            table[key] ? replacementReturn(muVar("iSubject", fuid, iSubjectPos))
    				          ]);
    	leaveBacktrackingScope();
	} else {
		// Arbitrary
		case_statement = pwa.statement;
		\case = translateStatementInVisitCase(fuid, case_statement);
		clearCaseType();
		list[MuExp] cbody = [];
		if(!(muBlock([]) := \case)) {
			cbody += \case;
		}
		table[key] = muBlock([ muIfelse(ifname, makeBoolExp("ALL",[ cond ], pwa.pattern@\loc), cbody, [ /*muCon(666)*/ ]),
		                       table[key] ? replacementReturn(muVar("iSubject", fuid, iSubjectPos))
		                     ]);
    	leaveBacktrackingScope();
	}
	return table;
}

private int fingerprintDefault = 0; //getFingerprint("default", false);

@doc{Generates the body of a phi function}
private MuExp translateVisitCases(str fuid, AType subjectType, bool useConcreteFingerprint, list[Case] cases) {
	// TODO: conditional
	
	map[int,MuExp] table = ();		// label + generated code per case
	
	default_code = replacementReturn(muVar("iSubject", fuid, iSubjectPos));
	
	for(c <- reverse(cases)){
		if(c is patternWithAction) {
		   pwa = c.patternWithAction;
		   key = fingerprint(pwa.pattern, useConcreteFingerprint);
		  if(!isSpoiler(c.patternWithAction.pattern, key)){
			 table = addPatternWithActionCode(fuid, subjectType, pwa, table, key);
		  }
		} else {
			// Default
			default_code = muBlock([ muAssignVarDeref("matched", fuid, matchedPos, muBool(true)), 
			                translate(c.statement), 
			                replacementReturn(muVar("iSubject", fuid, iSubjectPos)) ]);
		}
	}
	default_table = (fingerprintDefault : default_code);
    for(c <- reverse(cases), c is patternWithAction, isSpoiler(c.patternWithAction.pattern, fingerprint(c.patternWithAction.pattern, useConcreteFingerprint))){
	  default_table = addPatternWithActionCode(fuid, subjectType, c.patternWithAction, default_table, fingerprintDefault);
   }
   
   //println("TABLE DOMAIN(<size(table)>): <domain(table)>");
   case_code = [ muCase(key, table[key]) | key <- table];
   default_code =  default_table[fingerprintDefault];
   fetchSubject = (subjectType == astr()) ? muCallMuPrim("substring_str_mint_mint", [ muVar("iSubject", fuid, iSubjectPos),
   																	   				   muVarDeref("begin", fuid, beginPos),
   																	   				   muVarDeref("end", fuid, endPos)
                                                                     				 ]) 
                                          : muVar("iSubject", fuid, iSubjectPos)
                                          ;
   return muSwitch(fetchSubject, useConcreteFingerprint, case_code, default_code/*, muVar("iSubject", fuid, iSubjectPos)*/);
	
}

private tuple[set[AType] types, set[str] constructors] getTypesAndConstructorsInVisit(list[Case] cases){
	reachableTypes1 = {};// TODO: renamed for new (experimental) type checker
	reachableConstructors = {};
	for(c <- cases){
		if(c is patternWithAction){
			tc = getTypesAndConstructors(c.patternWithAction.pattern);
			reachableConstructors += tc.constructors;
			reachableTypes1 += tc.types;
		} else {
			return <{avalue()}, {}>;		// A default cases is present: everything can match
		}
	}
	return <reachableTypes1, reachableConstructors>;
}

public bool hasConcretePatternsOnly(list[Case] cases){
	for(c <- cases){
		if(c is patternWithAction){
			if(!isConcretePattern((c.patternWithAction.pattern))){
				return false;
			}
		} else {
			;//return false;		// A default case is present: everything can match TODO:??
		}
	}
	return true;
}

private bool hasTopLevelInsert(Case c) {
	top-down-break visit(c) {
		case (Statement) `insert <DataTarget dt> <Statement stat>`: return true;
		case Visit v: ;
	}
	return false;
}
/*
default MuExp translatePat(p:(Pattern) `<Literal lit>`) = translateLitPat(lit);

MuExp translateLitPat(Literal lit) = muApply(mkCallToLibFun("Library","MATCH_LITERAL"), [translate(lit)]);

// -- regexp pattern -------------------------------------------------

MuExp translatePat(p:(Pattern) `<RegExpLiteral r>`) = translateRegExpLiteral(r);

*/

private MuExp translatePatInVisit(Pattern pattern, str fuid, AType subjectType){
   if(subjectType == astr()){
      switch(pattern){
        case p:(Pattern) `<RegExpLiteral r>`: return translateRegExpLiteral(r, muVar("begin", fuid, beginPos), muVar("end", fuid, endPos));
        
      	case p:(Pattern) `<Literal lit>`: 
      		return muApply(mkCallToLibFun("Library","MATCH_SUBSTRING"), [translate(lit), muVar("begin", fuid, beginPos), muVar("end", fuid, endPos)]);
      	default: return translatePat(pattern, astr());
      }
   }
   return translatePat(pattern, avalue());
}

// -- reducer expression --------------------------------------------

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(e); //translateReducer(init, result, generators);

private MuExp translateReducer(Expression e){ //Expression init, Expression result, {Expression ","}+ generators){
    str fuid = topFunctionScope();
    str reducer = nextTmp("reducer"); 
    pushIt(reducer,fuid);
    code =  [ muAssignTmp(reducer, fuid, translate(e.init)), translateConds([g | g <- e.generators], muAssignTmp(reducer, fuid, translate(e.result)), muBlock([])),  muTmp(reducer,fuid) ];
    popIt();
    return muBlockWithTmps([<reducer, fuid>], [], code);
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) {
	
    return muCallPrim3("reifiedType_create", [translate(symbol), translate(definitions)], e@\loc);
    
}

// -- call expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`){

   println("translate: <e>");
  
   MuExp kwargs = translateKeywordArguments(keywordArguments);
   
   MuExp receiver = translate(expression);
   //println("receiver: <receiver>");
   list[MuExp] args = [ translate(a) | Expression a <- arguments ];
   
   if(getOuterType(expression) == "str"){
   		return muCallPrim3("node_create", [receiver, *args, *kwargs], e@\loc);
   }
  
   if(getOuterType(expression) == "loc"){
       return muCallPrim3("loc_with_offset_create", [receiver, *args], e@\loc);
   }
   if(muFun1(str _) := receiver || muFun2(str _, str _) := receiver || muConstr(str _) := receiver) {
       return muCall(receiver, args + [ kwargs ]);
   }
   
   // Now overloading resolution...
   ftype = getType(expression); // Get the type of a receiver
                                     // and a version with type parameters uniquely renamed
   //println("ftype : <ftype>");
       
   if(!isOverloadedAType(ftype) && (isEmpty(args) || all(muCon(_) <- args))){
   		str fname = unescape("<expression>");
   		try {
   			return translateConstantCall(fname, args);
   		} 
   		catch "NotConstant":  /* pass */;
   }
   	//addOverloadedFunctionAndResolver(ofqname, <of.name, ftype, of.fuid, resolved>);      
   	return muOCall3(receiver, args + [ kwargs ], e@\loc);
   
  
   //// Push down additional information if the overloading resolution needs to be done at runtime
   //return muOCall4(receiver, 
   //				  isFunctionType(ftype) ? atuple(atypeList([ ftype ])) : atuple(atypeList([ t | AType t <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype)) ])), 
   //				  args + [ kwargs ],
   //				  e@\loc);
}

private MuExp translateKeywordArguments((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArguments>`) {
   // Keyword arguments
   
   // Version that propagates values of set keyword parameters downwards
   
   // defPos = getFormals(currentFunctionDeclaration()) - 1;
   // //println("translateKeywordArguments: <keywordArguments>, <defPos>");
   // if(keywordArguments is \default){
   //   kwargs = [ muCon(unescape("<kwarg.name>")), translate(kwarg.expression)  | /*KeywordArgument[Expression]*/ kwarg <- keywordArguments.keywordArgumentList ];
   //   if(size(kwargs) > 0){
   //      return muCallMuPrim("copy_and_update_keyword_mmap", muCon(defPos) + kwargs );
   //   }
   //}
   //return muCallMuPrim("copy_and_update_keyword_mmap", [muCon(defPos)]);
   
    // Version that does not propagates values of set keyword parameters downwards and is compatible with the interpreter
    
   kwargs = [];
   if(keywordArguments is \default){
      kwargs = [ <unescape("<kwarg.name>"), translate(kwarg.expression)>  | kwarg <- keywordArguments.keywordArgumentList ];
   }
   return muKwpActuals(kwargs);
}

// -- any expression ------------------------------------------------

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = makeSingleValuedBoolExp("ALL",[ translate(g) | Expression g <- generators ], e@\loc);

// -- all expression ------------------------------------------------

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) = makeBoolExp("RASCAL_ALL",[ translate(g) | Expression g <- generators ], e@\loc);

// -- comprehension expression --------------------------------------

MuExp translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

private list[MuExp] translateComprehensionContribution(str kind, str tmp, str fuid, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muCallPrim3("<kind>writer_splice", [muTmp(tmp,fuid), translate(exp)], exp@\loc);
	    } else {
	      append muCallPrim3("<kind>writer_add", [muTmp(tmp,fuid), translate(r)], r@\loc);
	    }
	  }
} 

private MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    //println("translateComprehension (list): <generators>");
    str fuid = topFunctionScope();
    writer = nextTmp("listwriter");

    return
        muBlockWithTmps(
        [ <writer, fuid> ],
        [ ],
        [ muAssignTmp(writer, fuid, muCallPrim3("listwriter_open", [], c@\loc)),
          translateConds([ g | Expression g <- generators ], muBlock(translateComprehensionContribution("list", writer, fuid, [r | r <- results])), muBlock([])),
          muCallPrim3("listwriter_close", [muTmp(writer,fuid)], c@\loc) 
        ]);
}

private MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    //println("translateComprehension (set): <generators>");
    str fuid = topFunctionScope();
    writer = nextTmp("setwriter");
    
    return
        muBlockWithTmps(
        [ <writer, fuid> ],
        [ ],
        [ muAssignTmp(writer, fuid, muCallPrim3("setwriter_open", [], c@\loc)),
          translateConds([ g | Expression g <- generators ], muBlock(translateComprehensionContribution("set", writer, fuid, [r | r <- results])), muBlock([])),
          muCallPrim3("setwriter_close", [muTmp(writer,fuid)], c@\loc) 
        ]);
}

private MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    //println("translateComprehension (map): <generators>");
    str fuid = topFunctionScope();
    writer = nextTmp("mapwriter"); 
    
    return
        muBlockWithTmps(
        [ <writer, fuid > ],
        [ ],
        [ muAssignTmp(writer, fuid,  muCallPrim3("mapwriter_open", [], c@\loc)),
          translateConds([ g | Expression g <- generators ], muCallPrim3("mapwriter_add", [muTmp(writer,fuid)] + [ translate(from), translate(to)], c@\loc), muBlock([])),
          muCallPrim3("mapwriter_close", [muTmp(writer,fuid)], c@\loc) 
        ]);
}

// -- set expression ------------------------------------------------

MuExp translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) =
    translateSetOrList(e, es, "set");

// -- list expression -----------------------------------------------

MuExp translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) = 
    translateSetOrList(e, es, "list");

// Translate SetOrList including spliced elements

private bool containsSplices({Expression ","}* es) =
    any(Expression e <- es, e is splice);

private MuExp translateSetOrList(Expression e, {Expression ","}* es, str kind){
 if(containsSplices(es)){
       str fuid = topFunctionScope();
       writer = nextTmp();
       elmType = getElementType(getType(e));
       
       kindwriter_open_code = muCallPrim3("<kind>writer_open", [], e@\loc);
       
       enterWriter(writer);
       code = [ muAssignTmp(writer, fuid, kindwriter_open_code) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim3("<kind>writer_splice", [muTmp(writer,fuid), translate(elem.argument)], elem.argument@\loc);
            } else {
              code += muCallPrim3("<kind>writer_add", [muTmp(writer,fuid), translate(elem)], elem@\loc);
           }
       }
       code += [ muCallPrim3("<kind>writer_close", [ muTmp(writer,fuid) ], e@\loc) ];
       leaveWriter();
       return muBlockWithTmps([<writer, fuid>], [ ], code);
    } else {
      //if(size(es) == 0 || all(elm <- es, isConstant(elm))){
      //   return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      //} else 
        return muCallPrim3("<kind>_create", [ translate(elem) | Expression elem <- es ], e@\loc);
    }
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `# <Type tp>`) {
	//println("#<tp>, translateType: <e>");
	//iprintln("<translateType(tp)>");
	//iprintln("symbolToValue(translateType(tp)) = <symbolToValue(translateType(tp)).definitions>");
	return muCon(symbolToValue(translateType(tp)));
}	

// -- tuple expression ----------------------------------------------

MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) {
    //if(isConstant(e)){
    //  return muCon(readTextValueString("<e>"));
    //} else
        return muCallPrim3("tuple_create", [ translate(elem) | Expression elem <- elements ], e@\loc);
}

// -- map expression ------------------------------------------------

MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) {
   //if(isConstant(e)){
   //  return muCon(readTextValueString("<e>"));
   //} else 
     return muCallPrim3("map_create", [ translate(m.from), translate(m.to) | m <- mappings ], e@\loc);
}   

// -- it expression (in reducer) ------------------------------------

MuExp translate (e:(Expression) `it`) = 
    muTmp(topIt().name,topIt().fuid);
 
// -- qualified name expression -------------------------------------
 
MuExp translate((Expression) `<QualifiedName v>`) = 
    translate(v);
    
MuExp translateBool((Expression) `<QualifiedName v>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    translateBool(v, btscope, trueCont, falseCont);
 
MuExp translate(q:(QualifiedName) `<QualifiedName v>`) =
    mkVar("<v>", v@\loc);

MuExp translateBool(q:(QualifiedName) `<QualifiedName v>`, str btscope, MuExp trueCont, MuExp falseCont) =
    muIfelse(btscope, mkVar("<v>", v@\loc), trueCont, falseCont);

// For the benefit of names in regular expressions

MuExp translate((Name) `<Name name>`) =
    mkVar(unescape("<name>"), name@\loc);

// -- subscript expression ------------------------------------------
// Comes in 3 flavours:
// - ordinary: translateSubscript with isDefined=false
// - as part of an isDefined expression: translateSubscript with isDefined=true
// - as part of an isDefined else expression: translateSubscriptIsDefinedElse

MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`) =
	translateSubscript(e, false);


private MuExp translateSubscript(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`, bool isDefined){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    list_of_subscripts = [ s | s <- subscripts ]; // redundant
    nsubscripts = size(list_of_subscripts);
    if(ot in {"sort", "iter", "iter-star", "iter-seps", "iter-star-seps"}){
       op = "nonterminal_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    } else
    if(ot == "set"){
       op = "rel_subscript";
    } else
    if(ot notin {"map", "rel", "lrel"}) {
       op += "_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    } else 
    if(ot == "lrel" && nsubscripts == 1 && getOuterType(list_of_subscripts[0]) == "int"){
    	op = "list_subscript_int";
    }
    if(op == "rel_subscript"){
       relType = getType(exp); 
       relArity = size(relType.symbols);
       // Convention: 0: noset; 1: set; 2: wildcard
       subsKind = for(int i <- [0 .. nsubscripts]){
                     subs = list_of_subscripts[i];
                     if("<subs>" == "_") 
                        append 2;
                     else 
                        append (aset(elmType) := getType(subs) && comparable(elmType, relType.symbols[i])) ? 1 : 0;
                   };
      relType = getType(exp); 
      relArity = size(relType.symbols);
    
      generalCase = nsubscripts > 1 || 2 in subsKind;
      
      op = generalCase ? "rel_subscript" 
                       : ("rel" + ((relArity == 2) ? "2" : "") 
                                + "_subscript1"
                                + (subsKind == [0] ? "_noset" : "_set"));
      
      //println("<generalCase>, <subsKind> <op>");
      return muCallPrim3(op, translate(exp) + (generalCase ? [muCon(subsKind)] : []) + ["<s>" == "_" ? muCon("_") : translate(s) | Expression s <- subscripts], e@\loc);
    }
    
    opCode = muCallPrim3(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts], e@\loc);
    
    if(isDefined){
    	op = "is_defined_<op>";
    	return muCallMuPrim("subscript_array_int", [ muCallPrim3(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | Expression s <- subscripts], e@\loc), muCon(0)]);
    }
    return muCallPrim3(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | Expression s <- subscripts], e@\loc);
}

private MuExp translateSubscriptIsDefinedElse(Expression lhs:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`, Expression rhs){
    ot = getOuterType(exp);
    op = "<ot>_subscript";
    if(ot in {"sort", "iter", "iter-star", "iter-seps", "iter-star-seps"}){
       op = "nonterminal_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    } else
    if(ot notin {"map", "rel", "lrel"}) {
       op += "_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    str fuid = topFunctionScope();
    str varname = asTmp(nextLabel());
    return muBlockWithTmps(
                  [ <varname, fuid > ],
                  [ ],
                  [
    		       muAssignTmp(varname, fuid, muCallPrim3("is_defined_<op>", translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | Expression s <- subscripts], lhs@\loc)),
    			   muIfelse(nextLabel(), 
    						muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
    						[muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)])],
    						[translate(rhs)])
    			  ]);
}

// -- slice expression ----------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

// -- slice with step expression ------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) {
    ot = getOuterType(expression);

    if(ot in {"iter", "iter-star", "iter-star-seps", "iter-seps"}){
        min_size = contains(ot, "star") ? 0 : 1;
        return muCallPrim3("concrete_list_slice", [ translate(expression), translateOpt(optFirst), muCon("false"), translateOpt(optLast), muCon(min_size) ], expression@\loc);
    }
    if(ot == "lrel") ot = "list";
    return muCallPrim3("<ot>_slice", [ translate(expression), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ], expression@\loc);
}

public MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muCon("false") : translate(optExp.expression);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) {
    ot = getOuterType(expression);
    if(ot in {"iter", "iter-star", "iter-star-seps", "iter-seps"}){
        min_size = contains(ot, "star") ? 0 : 1;
        return muCallPrim3("concrete_list_slice", [ translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast), muCon(min_size) ], expression@\loc);
    }
    if(ot == "lrel") ot = "list";
    return muCallPrim3("<ot>_slice", [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ], expression@\loc);
}

// -- field access expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) {
   tp = getType(expression);
 
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       //return translate((Expression)`<Expression expression> \< <Name field> \>`);
       return translateProject(expression, [(Field)`<Name field>`], e@\loc);
   }
   if(isNonTerminalType(tp)){
      return muCallPrim3("nonterminal_field_access", [ translate(expression), muCon(unescape("<field>")) ], e@\loc);
   }
   op = getOuterType(expression);
   if(op == "adt"){
       cde = getConstantConstructorDefaultExpressions(expression@\loc);
       return muCallPrim3("<op>_field_access", [ translate(expression), muCon(unescape("<field>")), muCon(cde) ], e@\loc);
    }
    return muCallPrim3("<op>_field_access", [ translate(expression), muCon(unescape("<field>")) ], e@\loc);   
}

// -- field update expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) {
   
    tp = getType(expression);  
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTType(tp)){
        return muCallPrim3("adt_field_update", [ translate(expression), muCon(unescape("<key>")), translate(replacement) ], e@\loc);
    } else if(isLocType(tp)){
     	return muCallPrim3("loc_field_update", [ translate(expression), muCon(unescape("<key>")), translate(replacement) ], e@\loc);
    } else if(isNodeType(tp)){
        return muCallPrim3("node_field_update", [ translate(expression), muCon(unescape("<key>")), translate(replacement) ], e@\loc);
    }
    if(tupleHasFieldNames(tp)){
    	  fieldNames = getTupleFieldNames(tp);
    }	
    return muCallPrim3("<getOuterType(expression)>_update", [ translate(expression), muCon(indexOf(fieldNames, unescape("<key>"))), translate(replacement) ], e@\loc);
}

// -- field project expression --------------------------------------

MuExp translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) =
  translateProject(expression, [f | f <- fields], e@\loc);

MuExp translateProject(Expression expression, list[Field] fields, loc src){
    tp = getType(expression); 
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
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon(indexOf(fieldNames, unescape("<f>"))) | f <- fields];
    //fcode = [(f is index) ? muCon(toInt("<f>")) : muCon("<f>") | f <- fields];
    ot = getOuterType(expression);
    if(ot == "list") ot = "lrel"; else if(ot == "set") ot = "rel";
    
    return muCallPrim3("<ot>_field_project", [ translate(expression), *fcode], src);
}

// -- set annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) =
    muCallPrim3("annotation_set", [translate(expression), muCon(unescape("<name>")), translate(val)], e@\loc);

// -- get annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression>@<Name name>`) =
    muCallPrim3("annotation_get", [translate(expression), muCon(unescape("<name>"))], e@\loc);

// -- is expression --------------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim3("is", [translate(expression), muCon(unescape("<name>"))], e@\loc);

// -- has expression -----------------------------------------------

list[str] getNames(list[AType] s){
    return [nm | /label(str nm, _) := s];   // TODO
}

// For each constructor map its name + positional fieldNames to list of kw names
map[list[str], list[str]] makeHasTable(type[value] reif){
    choices = reif.definitions[reif.symbol];
    return (getNames([def]) + getNames(symbols) : getNames(kwTypes) | /acons(AType def, list[AType] symbols, list[Keyword] kwTypes) := choices);
}

MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) {
	
    outer = getOuterType(expression);
    //println("<e>: <outer>, <getType(expression)>");
    str op = "";
    switch(getOuterType(expression)){
    	case "adt": 	op = "adt";
    	case "sort":	op = "nonterminal";
    	case "lex":		op = "nonterminal";
    	case "nonterminal":
    					op = "nonterminal";
    	case "node":    op = "node";
    	default:
     		return muCon(hasField(getType(expression), unescape("<name>")));		
    }
    if(op == "adt"){	
        reif = symbolToValue(getType(expression));
        return muCallPrim3("<op>_has_field", [translate(expression), muCon(unescape("<name>")), muCon(makeHasTable(reif))], e@\loc);  
    }
    return muCallPrim3("<op>_has_field", [translate(expression), muCon(unescape("<name>"))], e@\loc);				    
}
// -- transitive closure expression ---------------------------------

MuExp translate(e:(Expression) `<Expression argument> +`) =
    postfix_rel_lrel("transitive_closure", argument);

// -- transitive reflexive closure expression -----------------------

MuExp translate(e:(Expression) `<Expression argument> *`) = 
    postfix_rel_lrel("transitive_reflexive_closure", argument);

// -- isDefined expression ------------------------------------------
// The isDefined and isDefinedElse expression are implemented using the following strategy:
// - try to avoid recomputation
// - try to avoid throwing exceptions
// This is applied to the computation of subscripts and annotations that are part of an isDefined or isDefinedElse expression.
// The remaining cases are handled by a general scheme that selectively catches any generated exceptions.

MuExp translate(e:(Expression) `<Expression argument> ?`) =
	translateIsDefined(argument);

private MuExp translateIsDefined(Expression exp){
	switch(exp){
	
		case (Expression) `( <Expression exp1> )`:
			return translateIsDefined(exp1);
			
		case (Expression) `<Expression exp1> [ <{Expression ","}+ subscripts> ]`: 
		    if(getOuterType(exp1) notin {"rel", "lrel", "nonterminal"}){
			 return translateSubscript(exp, true);
			} else {
			 fail;
			}
			
		case (Expression) `<Expression expression>@<Name name>`:
    		return muCallMuPrim("subscript_array_int", [ muCallPrim3("is_defined_annotation_get", [translate(expression), muCon(unescape("<name>"))], exp@\loc), muCon(0)]);
		
		case (Expression) `<Expression expression> . <Name field>`:
		    if(getOuterType(expression) notin {"tuple"}){
		       return muCallMuPrim("subscript_array_int", [ muCallPrim3("is_defined_<getOuterType(expression)>_field_access_get", [translate(expression), muCon(unescape("<field>"))], exp@\loc), muCon(0)]);
		    } else {
		      fail;
		    }
		    
		default:
    		return translateIfDefinedOtherwise(muBlock([ translate(exp), muCon(true) ]),  muCon(false), exp@\loc);
    }
}

// -- isDefinedOtherwise expression ---------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`) {
	switch(lhs){
	
		case (Expression) `<Expression exp1> [ <{Expression ","}+ subscripts> ]`: 
			return translateSubscriptIsDefinedElse(lhs, rhs);
			
		case (Expression) `<Expression expression>@<Name name>`: {
			str fuid = topFunctionScope();
    		str varname = asTmp(nextLabel());
    		return muBlockWithTmps(
    		      [ < varname, fuid > ],
    		      [ ],
    		      [
    		       muAssignTmp(varname, fuid, muCallPrim3("is_defined_annotation_get", [translate(expression), muCon(unescape("<name>"))], e@\loc)),
    			   muIfelse(nextLabel(), 
    						muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
    						[muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)])],
    						[translate(rhs)])
    			  ]);
    		}
    			
    	case (Expression) `<Expression expression> . <Name field>`:	{
    	   str fuid = topFunctionScope();
           str varname = asTmp(nextLabel());
    	   return muBlockWithTmps(
    	          [ < varname, fuid > ],
    	          [ ],
    	          [
                   muAssignTmp(varname, fuid, muCallPrim3("is_defined_adt_field_access_get", [translate(expression), muCon(unescape("<field>"))], e@\loc)),
                   muIfelse(nextLabel(), 
                            muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
                            [muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)])],
                            [translate(rhs)])
                  ]);
            }   
               		
		default:
    		return translateIfDefinedOtherwise(translate(lhs), translate(rhs), e@\loc);
	}
}

public MuExp translateIfDefinedOtherwise(MuExp muLHS, MuExp muRHS, loc src) {
    str fuid = topFunctionScope();
    str varname = asTmp(nextLabel());
    
    if(muCallPrim3("adt_field_access", args, lhs_src) := muLHS){    // field defined or keyword field set?
       return muBlockWithTmps(
              [ < varname, fuid > ],
              [ ],
              [
               muAssignTmp(varname, fuid, muCallPrim3("is_defined_adt_field_access_get", args[0..2], lhs_src)),
               muIfelse(nextLabel(), 
                        muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
                        [muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)])],
                        [muRHS])
              ]);             
    }
    
	// Check if evaluation of the expression throws one of a few specific exceptions;
	// do this by checking equality of the value constructor names
	
	cond = muCallPrim3("elm_in_set", [ muCallMuPrim("get_name", [ muTmp(asUnwrappedThrown(varname),fuid) ]),
									   muCon({"UninitializedVariable",
									          "NoSuchKey",
									          "NoSuchAnnotation",
											  "IndexOutOfBounds",
											  "NoSuchField"})
								      ], src);
	
	catchBody = muIfelse(nextLabel(), cond, [ muRHS ], [ muThrow(muTmp(varname,fuid), src) ]);
	return muTry(muLHS, muCatch(varname, fuid, aadt("RuntimeException",[], dataSyntax()), catchBody), 
			  		   muBlock([]));
}

// -- not expression ------------------------------------------------

MuExp translate(e:(Expression) `!<Expression argument>`) = 
    muIfelse("", translate(argument), muCon(false), muCon(true));
    
MuExp translateBool(e:(Expression) `!<Expression argument>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIf(btscope, translate(argument), "", falseCont, trueCont);

// -- negate expression ---------------------------------------------

MuExp translate(e:(Expression) `-<Expression argument>`) =
    prefix("negative", argument);

// -- splice expression ---------------------------------------------

MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice `<e>` cannot occur outside set or list at <e@\loc>";
}
   
// -- asType expression ---------------------------------------------

MuExp translate(e:(Expression) `[ <Type typ> ] <Expression argument>`)  =
 muCallPrim3("parse", [muCon(getModuleName()), 
   					    muCon(type(symbolToValue(translateType(typ)).symbol,getGrammar())), 
   					    translate(argument)], 
   					    argument@\loc);
  
// -- composition expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> o <Expression rhs>`) = 
    compose(e);

// -- product expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> * <Expression rhs>`) =
    infix("product", e);

// -- join expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> join <Expression rhs>`) =
    infix("join", e);

// -- remainder expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> % <Expression rhs>`) =
    infix("remainder", e);

// -- division expression -------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> / <Expression rhs>`) =
    infix("divide", e);

// -- intersection expression ---------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> & <Expression rhs>`) =
    infix("intersect", e);

// -- addition expression -------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> + <Expression rhs>`) =
    add(e);

// -- subtraction expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> - <Expression rhs>`) =
    infix("subtract", e);

// -- insert before expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`) =
    infix("add", e);

// -- append after expression ---------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`) {
   op = "add";
   lot = getOuterType(e.lhs);
   return muCallPrim3("<lot>_<op>_elm", [*translate(e.lhs), *translate(e.rhs)], e@\loc);
}

// -- modulo expression ---------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`) =
    infix("mod", e);

// -- notin expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`) =
    muIf(infix_elm_left("in", e), muCon(false), muCon(true));
    
MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    muIfelse(btscope, infix_elm_left("in", e),  falseCont, trueCont);

// -- in expression -------------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`) =
    infix_elm_left("in", e);

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    muIfelse(btscope, infix_elm_left("in", e),  trueCont, falseCont);

// -- greater equal expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = 
    infix("greaterequal", e);
 
 MuExp translateBool(e:(Expression) `<Expression lhs> \>= <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(btscope, infix("greaterequal", e),  trueCont, falseCont);

// -- less equal expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = 
    infix("lessequal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> \<= <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(btscope, infix("lessequal", e), trueCont, falseCont);

// -- less expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`) = 
    infix("less", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \< <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(btscope, infix("less", e),  trueCont, falseCont);

// -- greater expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`) = 
    infix("greater", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \> <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(btscope, infix("greater", e),  trueCont, falseCont);

// -- equal expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`) = 
    comparison("equal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> == <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(btscope, comparison("equal", e), trueCont, falseCont);

// -- not equal expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`) = 
    comparison("notequal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> != <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(btscope, comparison("equal", e),  falseCont, trueCont);
    
// -- no match expression -------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`) = 
    translateMatch(e, muCon(true), muCon(false));
    
MuExp translateBool(e:(Expression) `<Pattern pat> !:= <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    translateMatch(e, btscope, trueCont, falseCont);

// -- match expression --------------------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`) =
    translateMatch(e, muCon(true), muCon(false));
    
MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont) =
    translateMatch(e, btscope, trueCont, falseCont);
    
// -- generator expression ----------------------------------------------------
    
MuExp translateGenerator(Pattern pat, Expression exp, str btscope, MuExp trueCont, MuExp falseCont){
    str fuid = topFunctionScope();
    tmp = nextTmp();
    // enumerator with range expression
    if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
        return muForRange(btscope, tmp, fuid, translate(first), muCon(0), translate(last), translatePat(pat, alub(getType(first), getType(last)), muTmp(tmp, fuid), "", trueCont, falseCont));
    }
    // enumerator with range and step expression
    if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
       return muForRange(btscope, tmp, fuid, translate(first), translate(second), translate(last), translatePat(pat, alub(alub(getType(first), getType(second)), getType(last)), muTmp(tmp, fuid), "", trueCont, falseCont));
    }
    
    // generic enumerator
    return muForEach(btscope, tmp, fuid, translate(exp), translatePat(pat, getElementType(getType(exp)),  muTmp(tmp, fuid), "", trueCont, falseCont));
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`)
    = translateGenerator(pat, exp, "", muCon(true), muCon(false));

MuExp translateBool(e:(Expression) `<Pattern pat> \<- <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont)
    = translateGenerator(pat, exp, btscope, trueCont, falseCont);

// -- implies expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) =
   muIfelse("", translate(lhs), translate(rhs), muCon(true));
    
MuExp translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
   translateBool(lhs, btscope, translateBool(rhs, "", trueCont, falseCont), trueCont);

// -- equivalent expression -----------------------------------------
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) = 
   translateBool(lhs, "", translateBool(rhs, "", muCon(true), muCon(false)), translateBool(rhs, "", muCon(false), muCon(true)));

MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    translateBool(lhs, btscope, translateBool(rhs, "", trueCont, falseCont), translateBool(rhs, "", falseCont, trueCont));

// -- and expression ------------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression lhs> && <Expression rhs>`) =
    translateBool(lhs, "", translateBool(rhs, "", muCon(true), muCon(false)), muCon(false));

MuExp translateBool(e: (Expression) `<Expression lhs> && <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont)
    = translateBool(lhs, btscope, translateBool(rhs, "", trueCont, falseCont), falseCont);

// -- or expression -------------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`) =
    translateBool(lhs, "", muCon(true), translateBool(rhs, "", muCon(true), muCon(false)));
    
MuExp translateBool(e: (Expression) `<Expression lhs> || <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont)
    = translateBool(lhs, btscope, trueCont, translateBool(rhs, "", trueCont, falseCont));  
 
// -- conditional expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) =
	// ***Note that the label (used to backtrack) here is not important (no backtracking scope is pushed) 
	// as it is not allowed to have 'fail' in conditional expressions
	translateBool(condition, "", translate(thenExp),  translate(elseExp));

// -- any other expression (should not happn) ------------------------

default MuExp translate(Expression e) {
	throw "TRANSLATE, MISSING CASE FOR EXPRESSION: <e>";
}

default MuExp translateBool(Expression e, str btscope, MuExp trueCont, MuExp falseCont) {
    throw "TRANSLATEBOOL, MISSING CASE FOR EXPRESSION: <e>";
}

/*********************************************************************/
/*                  End of Ordinary Expessions                       */
/*********************************************************************/