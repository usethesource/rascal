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
  lhsType = getType(e.lhs);
  rhsType = getType(e.rhs);
  resType = getType(e);
  
  MuExp lhsReceiver = visit(translate(e.lhs)) { case str s => replaceAll(s, "::" , "_") };
  MuExp rhsReceiver = visit(translate(e.rhs)) { case str s => replaceAll(s, "::" , "_") };
  
  println("rhsReceiver: <rhsReceiver>");
  
  str ofqname = replaceAll("<lhsReceiver.fuid>_o_<rhsReceiver.fuid>", "::", "_");  // name of composition
  
  if(hasOverloadingResolver(ofqname)){
    return muOFun(ofqname);
  }
  
  // Unique 'id' of a visit in the function body
  //int i = nextVisit();  // TODO: replace by generated function counter
    
  // Generate and add a function COMPOSED_FUNCTIONS_<i>
  str scopeId = topFunctionScope();
  str comp_name = "COMPOSED_FUNCTION_<e@\loc.begin.line>_<e@\loc.end.line>";
  str comp_fuid = scopeId + "_" + comp_name;
  
  AType comp_ftype = avoid(); 
  int nargs = 0;
  if(isFunctionType(resType)) {
     nargs = size(resType.formals);
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
  //TODO kwargs
  rhsCall = muOCall3(rhsReceiver, atuple(atypeList([rhsType])), [muVar(resType.formals[j].label, comp_fuid, j, resType.formals[j]) | int j <- [0 .. nargs]], e.rhs@\loc);
  body_exps =  [muReturn1(muOCall3(lhsReceiver, atuple(atypeList([lhsType])), [rhsCall], e.lhs@\loc))];
  
  
  //kwargs = muCallMuPrim("copy_and_update_keyword_mmap", [muCon(nargs)]);
  //rhsCall = muOCall4(rhsReceiver, atuple(atypeList([rhsType])), [muVar("parameter_<comp_name>", comp_fuid, j) | int j <- [0 .. nargs]] + [ kwargs ], e.rhs@\loc);
  //body_exps =  [muReturn1(muOCall4(lhsReceiver, atuple(atypeList([lhsType])), [rhsCall, kwargs ], e.lhs@\loc))];
   
  leaveFunctionScope();
  body_code = muBlock(body_exps);
  fun = muFunction(comp_fuid, comp_name, comp_ftype, ["a", "b"], [],  scopeId, nargs, 2, false, false, true, getExternalRefs(body_code, comp_fuid), \e@\loc, [], (), body_code);
 
  loc uid = declareGeneratedFunction(comp_name, comp_fuid, comp_ftype, e@\loc);
  addFunctionToModule(fun);  
  addOverloadedFunctionAndResolver(ofqname, <comp_name, comp_ftype, getModuleName(), [ofqname], []>);
 
  return muOFun(comp_fuid);
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

private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <StringTemplate stemplate> <StringTail tail>`) {
    preIndent = computeIndent(pre);
    str fuid = topFunctionScope();
    template = muTmpTemplate(nextTmp("template"), fuid);
	return muValueBlock( [ muConInit(template, muTemplate(translatePreChars(pre))),
                           *translateTemplate(template, preIndent, stemplate),
                           *translateTail(template, preIndent, tail),
                           muTemplateClose(template)
                         ]);
}
    
private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    preIndent = computeIndent(pre);
    str fuid = topFunctionScope();
    template = muTmpTemplate(nextTmp("template"), fuid);
    return muValueBlock( [ muConInit(template, muTemplate(translatePreChars(pre))),
    				       *translateExpInStringLiteral(template, preIndent, expression),
    				       *translateTail(template, preIndent, tail),
    				       muTemplateClose(template)
					     ]);
}
                    
private MuExp translateStringLiteral(s: (StringLiteral)`<StringConstant constant>`) =
	muCon(readTextValueString(removeMargins("<constant>")));

// --- translateExpInStringLiteral

private list[MuExp] translateExpInStringLiteral(MuExp template, str indent, Expression expression){   
    if(indent == ""){
    	return [ muTemplateAdd(template, translate(expression)) ];
    }	
	return [ muTemplateBeginIndent(template, indent),
    	     muTemplateAdd(template, translate(expression)),
    		 muTemplateEndIndent(template, indent)
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

private str translatePreChars(PreStringChars pre) {
   spre = removeMargins("<pre>"[1..-1]);
   return "<spre>" == "" ? "" : deescape(spre);
}	

private list[MuExp] translateMidChars(MuExp template, MidStringChars mid) {
  smid = removeMargins("<mid>"[1..-1]);
  return "<mid>" == "" ? [] : [ muTemplateAdd(template, deescape(smid)) ];	//?
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

public list[MuExp] translateMiddle(MuExp template, str indent, (StringMiddle) `<MidStringChars mid>`) {
	mids = removeMargins("<mid>"[1..-1]);
	return mids == "" ? [] : [ muTemplateAdd(template, deescape(mids)) ];	// ?
}

public list[MuExp] translateMiddle(MuExp template, str indent, s: (StringMiddle) `<MidStringChars mid> <StringTemplate stemplate> <StringMiddle tail>`) {
	midIndent = computeIndent(mid);
    return [ *translateMidChars(template, mid),
   			 *translateTemplate(template, indent + midIndent, stemplate),
   			 *translateMiddle(template, indent, tail)
   		   ];
   	}

public list[MuExp] translateMiddle(MuExp template, str indent, s: (StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) {
	midIndent = computeIndent(mid);
    return [ *translateMidChars(template, mid),
    		 *translateExpInStringLiteral(template, midIndent, expression),
             *translateMiddle(template, indent + midIndent, tail)
           ];
}

// --- translateTail

private list[MuExp] translateTail(MuExp template, str indent, s: (StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) {
    midIndent = computeIndent(mid);
    return [ muBlock( [ *translateMidChars(template, mid),
    					*translateExpInStringLiteral(template, midIndent, expression),
                        *translateTail(template, indent + midIndent, tail)
                    ])
           ];
}
	
private list[MuExp] translateTail(MuExp template, str indent, (StringTail) `<PostStringChars post>`) {
  content = removeMargins("<post>"[1..-1]);
  return size(content) == 0 ? [] : [muTemplateAdd(template, deescape(content))];
}

private list[MuExp] translateTail(MuExp template, str indent, s: (StringTail) `<MidStringChars mid> <StringTemplate stemplate> <StringTail tail>`) {
    midIndent = computeIndent(mid);
    return [ muBlock( [ *translateMidChars(template, mid),
                        *translateTemplate(template, indent + midIndent, stemplate),
                        *translateTail(template, indent + midIndent,tail)
                    ])
           ];
 } 
 
 // --- translateTemplate 
 
 private list[MuExp] translateTemplate(MuExp template, str indent, Expression expression){
 	return translateExpInStringLiteral(template, indent, expression);
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
//TODO
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
           translated_elems = muValueBlock([ muConInit(muTmpWriter(writer, fuid), muCallPrim3("listwriter_open", [], my_src)),
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
  									  isVarArgs, false, false, getExternalRefs(body, fuid), e@\loc, [], (),
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
//    addFunctionToModule(muFunction(fuid, "CLOSURE", ftype, [], atuple(atypeList([])), addr.fuid, nformals, nlocals, isVarArgs, false, true, getExternalRefs(body, fuid), e@\loc, [], (), body));
//  	
//  	leaveFunctionScope();								  
//  	
//	return muFun2(fuid, addr.fuid); // closures are not overloaded
//
//}

// -- range expression ----------------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  writer = muTmpWriter(nextTmp("range_writer"), fuid);
  elem = muTmp(nextTmp("range_elem"), fuid, alub(getType(first), getType(last)));
  
  return
    muValueBlock([ muConInit(writer, muCallPrim3("listwriter_open", [], e@\loc)),
                   muForRange("", elem, translate(first), muCon(0), translate(last), muCallPrim3("listwriter_add", [writer, elem], e@\loc)),
                   muCallPrim3("listwriter_close", [writer], e@\loc)
                 ]);
}

// -- range with step expression ------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  writer = muTmpWriter(nextTmp("range_writer"), fuid);
  elemType = lubList([getType(first), getType(second), getType(last)]);
  elem = muTmp(nextTmp("range_elem"), fuid, elemType);
  
  return
    muValueBlock([ muConInit(writer, muCallPrim3("listwriter_open", [], e@\loc)),
                   muForRange("", elem, translate(first), translate(second), translate(last), muCallPrim3("listwriter_add", [writer, elem], e@\loc)),
                   muCallPrim3("listwriter_close", [writer], e@\loc)
                 ]);
}

// -- visit expression ----------------------------------------------

MuExp translate (e:(Expression) `<Label label> <Visit visitItself>`) = translateVisit(label, visitItself);

public MuExp translateVisit(Label label, lang::rascal::\syntax::Rascal::Visit \visit) {	
    str fuid = topFunctionScope();
    
    enterVisit();
    subjectType = getType(\visit.subject);
    switchname = getLabel(label);
    switchval = muTmp(asTmp(switchname), fuid, subjectType);
    
	
	bool isStringSubject = false; //subjectType == \str();
	
	cases = [ c | Case c <- \visit.cases ];
	
	useConcreteFingerprint = hasConcretePatternsOnly(cases) 
	                         && isConcreteType(subjectType); // || subjectType == adt("Tree",[]));
//TODO: add descendant info	
	reachable_syms = { avalue() };
	reachable_prods = {};
	if(optimizing()){
	   tc = getTypesAndConstructorsInVisit(cases);
	   <reachable_syms, reachable_prods> = getReachableTypes(subjectType, tc.constructors, tc.types, useConcreteFingerprint);
	   //println("reachableTypesInVisit: <reachable_syms>, <reachable_prods>");
	}
	
	ddescriptor = descendantDescriptor(useConcreteFingerprint/*, reachable_syms, reachable_prods, getDefinitions()*/);
		
	bool direction = true;
	bool progress = true;
	bool fixedpoint = true;
	bool rebuild = false;
	
	if( Case c <- \visit.cases, (c is patternWithAction && c.patternWithAction is replacing 
									|| hasTopLevelInsert(c)) ) {
		rebuild = true;
	}
	
	if(\visit is defaultStrategy) {
		                              direction = true;  progress = true;  fixedpoint = false; 
	} else {
		switch("<\visit.strategy>") {
			case "bottom-up"      : { direction = true;  progress = true;  fixedpoint = false; }
			case "top-down"       : { direction = false; progress = true;  fixedpoint = false; }
			case "bottom-up-break": { direction = true;  progress = false; fixedpoint = false; }
			case "top-down-break" : { direction = false; progress = false; fixedpoint = false; }
			case "innermost"      : { direction = true;  progress = true;  fixedpoint = true;  }
			case "outermost"      : { direction = false; progress = true;  fixedpoint = true;  }
		}
	}
	
	vdescriptor = visitDescriptor(direction, fixedpoint, progress, rebuild, ddescriptor);
	<case_code, default_code> = translateSwitchCases(switchval, fuid, useConcreteFingerprint, cases, muSucceedVisitCase());
	
    visitCode = muVisit(muConInit(switchval, translate(\visit.subject)), case_code, default_code, vdescriptor);
    leaveVisit();
    return visit(visitCode) { case muReturn0() => muReturn0FromVisit() case muReturn1(MuExp e) => muReturn1FromVisit(e) };
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

// -- reducer expression --------------------------------------------

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(e); //translateReducer(init, result, generators);

private MuExp translateReducer(Expression e){ //Expression init, Expression result, {Expression ","}+ generators){
    str fuid = topFunctionScope();
    reducer = muTmp(nextTmp("reducer"), fuid);
    pushIt(reducer,fuid);
    code =  [ muVarInit(reducer, translate(e.init)), translateConds([g | g <- e.generators], muAssign(reducer, translate(e.result)), muBlock([])),  reducer ];
    popIt();
    return muValueBlock(code);
}

// -- reified type expression ---------------------------------------
//TODO
MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) {
	
    return muCallPrim3("reifiedType_create", [translate(symbol), translate(definitions)], e@\loc);
    
}

// -- call expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`){

   println("translate: <e>");
  
   MuExp kwargs = translateKeywordArguments(keywordArguments);
   ftype = getType(expression); // Get the type of a receiver
   MuExp receiver = translate(expression);
   //println("receiver: <receiver>");
   list[MuExp] args = [ translate(a) | Expression a <- arguments ];
   
   if(getOuterType(expression) == "astr"){
   		return muCallPrim3("anode_create", [receiver, *args, *kwargs], e@\loc);
   }
  
   if(getOuterType(expression) == "aloc"){
       return muCallPrim3("aloc_with_offset_create", [receiver, *args], e@\loc);
   }
   if(muFun1(_) := receiver || muConstr(AType _) := receiver || muConstrCompanion(str _) := receiver) {
        return muCall(receiver,  hasKeywordParameters(ftype) ? args + [ kwargs ] : args);
   }
   
   // Now overloading resolution...
 
                                     // and a version with type parameters uniquely renamed
   //println("ftype : <ftype>");
       
   if(!isOverloadedAType(ftype) && (isEmpty(args) || all(muCon(_) <- args))){
   		str fname = unescape("<expression>");
   		try {
   			return translateConstantCall(fname, args);
   		} 
   		catch "NotConstant":  /* pass */;
   }
   
   	return muOCall3(receiver, ftype, hasKeywordParameters(ftype) ? args + [ kwargs ] : args, e@\loc);
   
  
   //// Push down additional information if the overloading resolution needs to be done at runtime
   //return muOCall4(receiver, 
   //				  isFunctionType(ftype) ? atuple(atypeList([ ftype ])) : atuple(atypeList([ t | AType t <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype)) ])), 
   //				  args + [ kwargs ],
   //				  e@\loc);
}

private MuExp translateKeywordArguments((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArguments>`) {
   // Version that does not propagates values of set keyword parameters downwards and is compatible with the interpreter
    
   kwargs = [];
   if(keywordArguments is \default){
      kwargs = [ <unescape("<kwarg.name>"), translate(kwarg.expression)>  | kwarg <- keywordArguments.keywordArgumentList ];
   }
   return muKwpActuals(kwargs);
}

// -- any expression ------------------------------------------------

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    str whileName = nextLabel();
      
    any_found = muTmpBool(nextTmp("any_found"), fuid);
    enterLoop(whileName,fuid);
    exit = muBlock([]);
    code = muBlock([muAssign(any_found, muCon(true)), muBreak(whileName)]);
    for(gen <- reverse([ g | g <- generators])){
        if((Expression) `<Pattern pat> \<- <Expression exp>` := gen){
            btscope = nextTmp("ANY");
            if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){     
                elemType = alub(getType(first), getType(last));
                elem = muTmp(nextTmp("elem"), fuid, elemType);
                code = muForRange(btscope, elem, translate(first), muCon(0), translate(last), translatePat(pat, elemType, elem, "", code, muFail(btscope)));
            } else 
            if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
                elemType = alub(alub(getType(first), getType(second)), getType(last));
                elem = muTmp(nextTmp("elem"), fuid, elemType);
                code = muForRange(btscope, elem, translate(first), translate(second), translate(last), translatePat(pat, elemType, elem, "", code, muFail(btscope)));
            } else {
                elemType = getElementType(getType(exp));
                elem = muTmp(nextTmp("elem"), fuid, elemType);
                code = muForAll/*Any*/(btscope, elem, translate(exp), translatePat(pat, elemType,  elem, "", code, muFail(btscope)));
            }
        } else {
            code = translateBool(gen, "", code, exit);
        }
    }
      
    code = muValueBlock([ muVarInit(any_found, muCon(false)),
                          muDoWhile(whileName, code, muCon(false)),
                          any_found ]);
    leaveLoop();
    return code;
}

MuExp translateBool (e:(Expression) `any ( <{Expression ","}+ generators> )`, str btscope, MuExp trueCont, MuExp falseCont)
    = muIfelse(translate(e), trueCont, falseCont);


// -- all expression ------------------------------------------------

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    str whileName = nextLabel();
      
    all_true = muTmpBool(nextTmp("all_true"), fuid);
    enterLoop(whileName,fuid);
    exit = muBlock([muAssign(all_true, muCon(false)), muBreak(whileName)]);
    code = muContinue(whileName);
    for(gen <- reverse([ g | g <- generators])){
        if((Expression) `<Pattern pat> \<- <Expression exp>` := gen){
            btscope = nextTmp("ALL");
            if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
                elemType = alub(getType(first), getType(last));
                elem = muTmp(nextTmp("elem"), fuid, elemType);
                code = muForRange(btscope, elem, translate(first), muCon(0), translate(last), translatePat(pat, elemType, elem, "", code, exit));
            } else 
            if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
                elemType = alub(alub(getType(first), getType(second)), getType(last));
                elem = muTmp(nextTmp("elem"), fuid, elemType);
                code = muForRange(btscope, elem, translate(first), translate(second), translate(last), translatePat(pat, elemType, tmp, "", code, exit));
            } else {
                elemType = getElementType(getType(exp));
                elem = muTmp(nextTmp("elem"), fuid, elemType);
                code = muForAll(btscope, elem, translate(exp), translatePat(pat, elemType, elem, "", code, exit));
            }
            //code = muBlock([code, muBreak(whileName)]);
        } else {
            code = translateBool(gen, "", code, exit);
        }
    }
      
    code = muValueBlock([ muVarInit(all_true, muCon(true)),
                          muDoWhile(whileName, code, muCon(false)),
                          all_true ]);
    leaveLoop();
    return code;
}

MuExp translateBool (e:(Expression) `all ( <{Expression ","}+ generators> )`, str btscope, MuExp trueCont, MuExp falseCont)
    = muIfelse(translate(e), trueCont, falseCont);

// -- comprehension expression --------------------------------------

MuExp translate (e:(Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

private list[MuExp] translateComprehensionContribution(str kind, MuExp writer, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muCallPrim3("<kind>writer_splice", [writer, translate(exp)], exp@\loc);
	    } else {
	      append muCallPrim3("<kind>writer_add", [writer, translate(r)], r@\loc);
	    }
	  }
} 

private MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    //println("translateComprehension (list): <generators>");
    str fuid = topFunctionScope();
    writer = muTmpWriter(nextTmp("listwriter"), fuid);

    return
        muValueBlock([ muConInit(writer, muCallPrim3("listwriter_open", [], c@\loc)),
                       translateConds([ g | Expression g <- generators ], muBlock(translateComprehensionContribution("list", writer, [r | r <- results])), muBlock([])),
                       muCallPrim3("listwriter_close", [writer], c@\loc) 
                     ]);
}

private MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    //println("translateComprehension (set): <generators>");
    str fuid = topFunctionScope();
    writer = muTmpWriter(nextTmp("setwriter"), fuid);
    
    return
        muValueBlock([ muConInit(writer, muCallPrim3("setwriter_open", [], c@\loc)),
                      translateConds([ g | Expression g <- generators ], muBlock(translateComprehensionContribution("set", writer, [r | r <- results])), muBlock([])),
                      muCallPrim3("setwriter_close", [writer], c@\loc) 
                    ]);
}

private MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    //println("translateComprehension (map): <generators>");
    str fuid = topFunctionScope();
    writer = muTmpWriter(nextTmp("mapwriter"), fuid);
    
    return
        muValueBlock([ muConInit(writer, muCallPrim3("mapwriter_open", [], c@\loc)),
                       translateConds([ g | Expression g <- generators ], muCallPrim3("mapwriter_add", [writer] + [ translate(from), translate(to)], c@\loc), muBlock([])),
                       muCallPrim3("mapwriter_close", [writer], c@\loc) 
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
       writer = muTmpWriter(nextTmp("writer"), fuid);
       elmType = getElementType(getType(e));
       
       kindwriter_open_code = muCallPrim3("<kind>writer_open", [], e@\loc);
       
       enterWriter(writer.name);
       code = [ muConInit(writer, kindwriter_open_code) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim3("<kind>writer_splice", [writer, translate(elem.argument)], elem.argument@\loc);
            } else {
              code += muCallPrim3("<kind>writer_add", [writer, translate(elem)], elem@\loc);
           }
       }
       code += [ muCallPrim3("<kind>writer_close", [ writer ], e@\loc) ];
       leaveWriter();
       return muValueBlock(code);
    } else {
      //if(size(es) == 0 || all(elm <- es, isConstant(elm))){
      //   return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      //} else 
        return muCallPrim3("<kind>_create", [ translate(elem) | Expression elem <- es ], e@\loc);
    }
}

// -- reified type expression ---------------------------------------
//TODO
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
    muIfelse(mkVar("<v>", v@\loc), trueCont, falseCont);

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
    return muValueBlock([ muVarInit(muTmp(varname, fuid), muCallPrim3("is_defined_<op>", translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | Expression s <- subscripts], lhs@\loc)),
    			          muIfelse(muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
    						       muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)]),
    						       translate(rhs))
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
   fieldType = getType(field);
 
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       //return translate((Expression)`<Expression expression> \< <Name field> \>`);
       return translateProject(expression, [(Field)`<Name field>`], e@\loc);
   }
   if(isNonTerminalType(tp)){
      return muFieldAccess("nonterminal", getConstructorType(tp, fieldType), translate(expression), unescape("<field>"));
   }
   op = getOuterType(expression);
   if(op == "aadt"){
        <ctype, isKwp> = getConstructorInfo(tp, fieldType);
       return isKwp ? muKwpFieldAccess(op, ctype, translate(expression), unescape("<field>"))
                    : muFieldAccess(op, ctype, translate(expression), unescape("<field>"));
    }
    return muFieldAccess(op, getConstructorType(tp, fieldType), translate(expression), unescape("<field>"));   
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
        return muFieldUpdate("aadt", translate(expression), unescape("<key>"), translate(replacement));
    } else if(isLocType(tp)){
     	return muFieldUpdate("aloc", translate(expression), unescape("<key>"), translate(replacement));
    } else if(isNodeType(tp)){
        return muFieldUpdate("anode", translate(expression), unescape("<key>"), translate(replacement));
    }
    if(tupleHasFieldNames(tp)){
    	  fieldNames = getTupleFieldNames(tp);
    }	
    //TODO
    return muFieldUpdate("<getOuterType(expression)>", translate(expression), indexOf(fieldNames, unescape("<key>")), translate(replacement));
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
    		return translateIfDefinedOtherwise(muValueBlock([ translate(exp), muCon(true) ]),  muCon(false), exp@\loc);
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
    		return muValueBlock([ muVarInit(muTmp(varname, fuid), muCallPrim3("is_defined_annotation_get", [translate(expression), muCon(unescape("<name>"))], e@\loc)),
    			                  muIfelse(muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
    						               muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)]),
    						               translate(rhs))
    			  ]);
    		}
    			
    	case (Expression) `<Expression expression> . <Name field>`:	{
    	   str fuid = topFunctionScope();
           str varname = asTmp(nextLabel());
    	   return muValueBlock([ muVarInit(muTmp(varname, fuid), muCallPrim3("is_defined_adt_field_access_get", [translate(expression), muCon(unescape("<field>"))], e@\loc)),
                                 muIfelse(muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
                                          muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)]),
                                          translate(rhs))
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
       return muValueBlock([ muVarInit(muTmp(varname, fuid), muCallPrim3("is_defined_adt_field_access_get", args[0..2], lhs_src)),
                             muIfelse(muCallMuPrim("subscript_array_int",  [muTmp(varname,fuid), muCon(0)]),
                                      muCallMuPrim("subscript_array_int", [muTmp(varname,fuid), muCon(1)]),
                                      muRHS)
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
	
	catchBody = muIfelse(cond, muRHS, muThrow(muTmp(varname,fuid), src));
	return muTry(muLHS, muCatch(varname, fuid, aadt("RuntimeException",[], dataSyntax()), catchBody), 
			  		   muBlock([]));
}

// -- not expression ------------------------------------------------

MuExp translate(e:(Expression) `!<Expression argument>`) {
    if(backtrackFree(argument)){
        return muCallPrim3("not_abool", [translate(argument)], e@\loc);
    }
    return translateConds("", [argument], muFail(""), muSucceed(""));
}
    
MuExp translateBool(e:(Expression) `!<Expression argument>`, str btscope, MuExp trueCont, MuExp falseCont) {
    return translateConds(btscope, [argument], falseCont, trueCont);
    }

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
    muIfelse(infix_elm_left("in", e), muCon(false), muCon(true));
    
MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    muIfelse(infix_elm_left("in", e),  falseCont, trueCont);

// -- in expression -------------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`) =
    infix_elm_left("in", e);

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    muIfelse(infix_elm_left("in", e),  trueCont, falseCont);

// -- greater equal expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = 
    infix("greaterequal", e);
 
 MuExp translateBool(e:(Expression) `<Expression lhs> \>= <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(infix("greaterequal", e),  trueCont, falseCont);

// -- less equal expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = 
    infix("lessequal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> \<= <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(infix("lessequal", e), trueCont, falseCont);

// -- less expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`) = 
    infix("less", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \< <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(infix("less", e),  trueCont, falseCont);

// -- greater expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`) = 
    infix("greater", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \> <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(infix("greater", e),  trueCont, falseCont);

// -- equal expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`) = 
    muIfelse(comparison("equal", e), muCon(true), muCon(false));
    //comparison("equal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> == <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(comparison("equal", e), trueCont, falseCont);

// -- not equal expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`) = 
    comparison("notequal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> != <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    muIfelse(comparison("equal", e),  falseCont, trueCont);
    
// -- no match expression -------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`) { 
    btscope = nextTmp("NOMATCH");
    enterBacktrackingScope(btscope);
    code = muEnter(btscope, translateMatch(e, btscope, muSucceed(btscope), muFail(btscope)));
    leaveBacktrackingScope();
    return code;
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> !:= <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    translateMatch(e, btscope, trueCont, falseCont);

// -- match expression --------------------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`){
    btscope = nextTmp("MATCH");
    enterBacktrackingScope(btscope);
    code = muEnter(btscope, translateMatch(e, btscope, muSucceed(btscope), muFail(btscope)));
    leaveBacktrackingScope();
    return code;
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont) =
    translateMatch(e, btscope, trueCont, falseCont);
    
// -- generator expression ----------------------------------------------------
    
MuExp translateGenerator(Pattern pat, Expression exp, str btscope, MuExp trueCont, MuExp falseCont){
    str fuid = topFunctionScope();
    elem = muTmp(nextTmp("elem"), fuid, getElementType(getType(exp)));
    old_btscope = btscope;
    new_btscope = nextTmp("ENUM");
    trueCont = updateBTScope(trueCont,btscope, new_btscope);
    falseCont = updateBTScope(falseCont, btscope, new_btscope);
    btscope = new_btscope;
    enterLoop(btscope, fuid);
    enterBacktrackingScope(btscope);
    code = muBlock([]);
    // enumerator with range expression
    if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
        code = muForRange(btscope, elem, translate(first), muCon(0), translate(last), translatePat(pat, alub(getType(first), getType(last)), elem, "", trueCont, falseCont));
    } else
    // enumerator with range and step expression
    if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
       code = muForRange(btscope, elem, translate(first), translate(second), translate(last), translatePat(pat, alub(alub(getType(first), getType(second)), getType(last)), elem, "", trueCont, falseCont));
    } else {
    //btscope = nextTmp("ENUM");
    // generic enumerator
        code = muForAll(btscope, elem, translate(exp), translatePat(pat, getElementType(getType(exp)),  elem, "", trueCont, muFail(btscope)));
    }
    leaveBacktrackingScope();
    leaveLoop();
    return code;
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`){
    btscope = nextTmp("ENUM");
    return muEnter(btscope, translateGenerator(pat, exp, btscope, muSucceede(btscope), muFail(btscope)));
}

MuExp translateBool(e:(Expression) `<Pattern pat> \<- <Expression exp>`, str btscope, MuExp trueCont, MuExp falseCont)
    = translateGenerator(pat, exp, btscope, trueCont, falseCont);

// -- and expression ------------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression lhs> && <Expression rhs>`) {
    if(backtrackFree(e)){
        return muIfelse(translate(lhs), translate(rhs), muCon(false));
    }
    btscope = nextTmp("AND");
    enterBacktrackingScope(btscope);
    code = muEnter(btscope, translateBool(lhs, btscope, 
                                               translateBool(rhs, btscope, muSucceed(btscope), muFail(btscope)), 
                                               muFail(btscope)));
    leaveBacktrackingScope();
    return code;    
}

MuExp translateBool(e: (Expression) `<Expression lhs> && <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont){
    return translateBool(lhs, btscope, translateBool(rhs, btscope, trueCont, falseCont), falseCont);
}

// -- or expression -------------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`) {
   if(backtrackFree(e)){
        return muIfelse(translate(lhs), muCon(true), translate(rhs));
   }
   btscope = nextTmp("OR");
   enterBacktrackingScope(btscope);
                                                                                      
   code = muEnter(btscope, translateBool(lhs, btscope, muSucceed(btscope), 
                                                       translateBool(rhs, btscope, muSucceed(btscope), 
                                                                                   muFail(btscope))));   
   leaveBacktrackingScope();
   return code;                                                                               
}
    
MuExp translateBool(e: (Expression) `<Expression lhs> || <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont)
    = translateBool(lhs, btscope, trueCont, translateBool(rhs, btscope, trueCont, falseCont));  
    
// -- implies expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) {
    if(backtrackFree(e)){
        return muIfelse(translate(lhs), translate(rhs), muCon(true));
    }
    btscope = nextTmp("IMPLIES");
    enterBacktrackingScope(btscope);
    code = translateConds(btscope, [lhs], translate(rhs), muSucceed(btscope)); 
    leaveBacktrackingScope();
    return code;  
}
    
MuExp translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) =
    translateBool(lhs, btscope, translateBool(rhs, btscope, trueCont, falseCont), trueCont);

// -- equivalent expression -----------------------------------------

//MuExp forAll(muForAny(str label, MuExp var, MuExp iterable, MuExp body))
//    = muForAll(label, var, iterable, body);
//MuExp forAll(muEnter(str label, muForAny(label, MuExp var, MuExp iterable, MuExp body)))
//    = muEnter(label, muForAll(label, var, iterable, body));
//    
//default MuExp forAll(MuExp exp) = exp; 
   
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) {
    if(backtrackFree(e)){
        return muIfelse(translate(lhs), translate(rhs), muCallPrim3("not_abool", [translate(rhs)], e@\loc));
    }
   btscopelhs = nextTmp("EQUIV_LHS");
   btscoperhs = nextTmp("EQUIV_RHS");
                                               
   return forAll(translateBool(lhs, btscopelhs, forAll(muEnter(btscopelhs, translateBool(rhs, btscopelhs, muSucceed(btscopelhs), muFail(btscopelhs)))), 
                                                forAll(muEnter(btscoperhs, translateBool(rhs, btscoperhs, muFail(btscoperhs),    muSucceed(btscoperhs))))));

   //TODO leave/enter BTscope?
}

MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`, str btscope, MuExp trueCont, MuExp falseCont) = 
    translateBool(lhs, btscope, translateBool(rhs, btscope, trueCont, falseCont), translateBool(rhs, btscope, falseCont, trueCont));
 
// -- conditional expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) {
	// ***Note that the label (used to backtrack) here is not important (no backtracking scope is pushed) 
	// as it is not allowed to have 'fail' in conditional expressions
	return translateBool(condition, "", translate(thenExp),  translate(elseExp));
}

// -- any other expression (should not happn) ------------------------

default MuExp translate(Expression e) {
	btscope = nextTmp("EXP");
    return muEnter(btscope, translateBool(e, btscope, muSucceed(btscope), muFail(btscope)));
}

default MuExp translateBool(Expression e, str btscope, MuExp trueCont, MuExp falseCont) {
    throw "TRANSLATEBOOL, MISSING CASE FOR EXPRESSION: <e>";
}

// Is an expression free of backtracking? 

bool backtrackFree(Expression e){
    top-down visit(e){
    //case (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`:
    //  return true;
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
    case (Expression) `<Expression e1> || <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);
    case (Expression) `<Expression e1> && <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression e1> \<==\> <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression e1> ==\> <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    }
    return true;
}

/*********************************************************************/
/*                  End of Expessions                                */
/*********************************************************************/