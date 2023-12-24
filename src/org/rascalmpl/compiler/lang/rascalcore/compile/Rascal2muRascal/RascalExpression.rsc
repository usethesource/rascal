@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalExpression

import IO;
import ValueIO;
import String;
import Location;
import Node;
import Map;
import Set;
import ParseTree;
import util::Reflective;
import Exception;
import lang::rascalcore::compile::CompileTimeError;

import lang::rascal::\syntax::Rascal;

import analysis::typepal::AType;
import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::BacktrackFree;
import lang::rascalcore::check::NameUtils;

import lang::rascalcore::compile::Rascal2muRascal::Common;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
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

private MuExp infix(str op, Expression e) { 
    return muPrim(op, getType(e), [getType(e.lhs), getType(e.rhs)], [translate(e.lhs), translate(e.rhs)], e@\loc);
}    
private MuExp unary(str op, Expression e, Expression arg) = 
    muPrim(op, getType(e), [getType(arg)], [translate(arg)], e@\loc);

// ----------- compose: exp o exp ----------------

private MuExp compose(Expression e){
  lhsType = getType(e.lhs);
  return isFunctionAType(lhsType) || isOverloadedAType(lhsType) ? translateComposeFunction(e) : infix("compose", e);
}

//str getFunctionName(af:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = af.alabel;
//str getFunctionName(ovl: overloadedAType(rel[loc, IdRole, AType] overloads)) = getFirstFrom(overloads)[2].alabel;
//str getFunctionName(AType t) {
//    throw "getFunctionName: <t>";
//}

private MuExp translateComposeFunction(Expression e){
  lhsType = getType(e.lhs);
  rhsType = getType(e.rhs);
  resType = getType(e);
  //if(overloadedAType(rel[loc, IdRole, AType] lhsOverloads) := lhsType){
  //  if(overloadedAType(rel[loc, IdRole, AType] rhsOverloads) := rhsType){
  //      lhsType = overloadedAType({ltup | ltup:<loc ll, IdRole lidr, AType latype> <- lhsOverloads, 
  //                                        any(rtup:<loc rl, IdRole ridr, AType ratype> <- rhsOverloads, 
  //                                            comparable(getFunctionArgumentTypes(latype)[0], getResult(ratype)))}); 
  //  } else {
  //      rhsResult = getResult(lhsType);
  //      lhsType = overloadedAType({ltup | ltup:<loc ll, IdRole lidr, AType latype> <- lhsOverloads, 
  //                                        comparable(getFunctionArgumentTypes(latype)[0], rhsResult)}); 
  //  }
  //} else if(overloadedAType(rel[loc, IdRole, AType] overloads) := rhsType){
  //  leftArgType = getFunctionArgumentTypes(lhsType)[0];
  //  rhsType = overloadedAType({tup | tup:<loc l, IdRole idr, AType atype> <- overloads, comparable(leftArgType, getResult(atype))}); 
  //}
  
  return muComposedFun(translate(e.lhs), translate(e.rhs), lhsType, rhsType, resType);
}

// ----------- addition: exp + exp ----------------

private  MuExp add(Expression e){
    lhsType = getType(e.lhs);
    return isFunctionAType(lhsType) || isOverloadedAType(lhsType) ? translateAddFunction(e) : infix("add", e);
}

private MuExp translateAddFunction(Expression e){
  lhsType = getType(e.lhs);
  rhsType = getType(e.rhs);
  eType = getType(e);
  resType = getResult(eType);

  // Generate and add a function ADD<...>
  str scopeId = topFunctionScope();
  str add_name = "$ADD<e@\loc.begin.line>A<e@\loc.offset>L<e@\loc.length>";
  str add_fuid = scopeId + "_" + add_name;

  MuExp lhsReceiver = translate(e.lhs); 
  MuExp rhsReceiver = translate(e.rhs);

  enterFunctionScope(add_fuid);
  lhsFormals = getFormals(lhsType);
  nargs = size(lhsFormals);
  lactuals = [muVar("$<j>", add_fuid, j, lhsFormals[j], formalId()) | int j <- [0 .. nargs]];
  lhsCall = muOCall(lhsReceiver, lhsType, lactuals, [], e.lhs@\loc);
  
  rhsFormals = getFormals(rhsType);
  nargs = size(rhsFormals);
  ractuals = [muVar("$<j>" , add_fuid, j, rhsFormals[j], formalId()) | int j <- [0 .. nargs]];
  rhsCall = muOCall(rhsReceiver, rhsType, ractuals, [], e.rhs@\loc);
  
  body = muReturnFirstSucceeds(["$<i>" | int i <- [0 .. nargs]], [muReturn1(resType, lhsCall), muReturn1(resType, rhsCall)]);
 
  leaveFunctionScope();
  funType = afunc(resType, lhsFormals, []);
  fun = muFunction(add_name, add_name, funType, lactuals, [], [], scopeId, false, false, false, getExternalRefs(body, add_fuid), {}, {}, e@\loc, [], (), body);
  loc uid = declareGeneratedFunction(add_name, add_fuid, funType, e@\loc);
  addFunctionToModule(fun);  
  addDefineAndType(<currentFunctionDeclaration(), add_name, add_name, functionId(), /*1000+e@\loc.offset,*/ e@\loc, defType(funType)>, funType); // TODO: replace arbitrary number
 
  return muOFun([uid], funType);
}

/*********************************************************************/
/*                  Translate Literals                               */
/*********************************************************************/

MuExp translate(Literal lit)
    = translateLiteral(lit);

// -- boolean literal  -----------------------------------------------

MuExp translate((Literal) `<BooleanLiteral b>`, BTSCOPES _btscopes) = 
    "<b>" == "true" ? muCon(true) : muCon(false);
    
MuExp translateBool((Literal) `<BooleanLiteral b>`, BTSCOPES _btscopes, MuExp trueCont, MuExp falseCont) = 
    "<b>" == "true" ? trueCont : falseCont;

// -- integer literal  -----------------------------------------------
 
MuExp translateLiteral((Literal) `<IntegerLiteral n>`) = 
    muCon(toInt("<n>"));

// -- regular expression literal  ------------------------------------

MuExp translateLiteral((Literal) `<RegExpLiteral r>`) { 
    throw "RexExpLiteral cannot occur in expression"; 
}

// -- string literal  ------------------------------------------------

MuExp translateLiteral((Literal) `<StringLiteral n>`) {
    return translateStringLiteral(n);
}

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
	return muValueBlock( astr(),
	                     [ muConInit(template, muTemplate(translatePreChars(pre))),
                           translateTemplate(template, preIndent, stemplate),
                           translateTail(template, preIndent, tail),
                           muTemplateClose(template)
                         ]);
}
    
private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    preIndent = computeIndent(pre);
    str fuid = topFunctionScope();
    template = muTmpTemplate(nextTmp("template"), fuid);
    return muValueBlock( astr(),
                         [ muConInit(template, muTemplate(translatePreChars(pre))),
    				       translateExpOrTemplateInStringLiteral(template, preIndent, expression),
    				       translateTail(template, preIndent, tail),
    				       muTemplateClose(template)
					     ]);
}
                    
private MuExp translateStringLiteral(s: (StringLiteral)`<StringConstant constant>`) {
    return muCon(/*deescape(*/readTextValueString(removeMargins("<constant>"))/*)*/);
}

// --- translateExpOrTemplateInStringLiteral

private MuExp translateExpOrTemplateInStringLiteral(MuExp template, str indent, Expression expression){    /* Expression | StringTemplate */
    if(indent == ""){
    	return muBlock([ muTemplateAdd(template, getType(expression), translate(expression)) ]);
    }	
	return muBlock([ muTemplateBeginIndent(template, indent),
    	             muTemplateAdd(template, getType(expression), translate(expression)),
    		         muTemplateEndIndent(template, indent)
    	           ]);
}

private MuExp translateExpOrTemplateInStringLiteral(MuExp template, str indent, StringTemplate stemplate){    /* Expression | StringTemplate */
    if(indent == ""){
        return muBlock([ muTemplateAdd(template, astr(), translate(stemplate)) ]);
    }   
    return muBlock([ muTemplateBeginIndent(template, indent),
                     muTemplateAdd(template, astr(), translate(stemplate)),
                     muTemplateEndIndent(template, indent)
                   ]);
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

private MuExp translateMidChars(MuExp template, MidStringChars mid) {
  smid = removeMargins("<mid>"[1..-1]);
  return "<mid>" == "" ? muBlock([]) : muTemplateAdd(template, astr(), deescape(smid));	//?
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

public MuExp translateMiddle(MuExp template, str indent, (StringMiddle) `<MidStringChars mid>`) {
	mids = removeMargins("<mid>"[1..-1]);
	return mids == "" ? muBlock([]) : muTemplateAdd(template, astr(), deescape(mids));	// ?
}

public MuExp translateMiddle(MuExp template, str indent, s: (StringMiddle) `<MidStringChars mid> <StringTemplate stemplate> <StringMiddle tail>`) {
	midIndent = computeIndent(mid);
    return muBlock([ translateMidChars(template, mid),
   		        	 translateTemplate(template, indent + midIndent, stemplate),
   			         translateMiddle(template, indent, tail)
   		           ]);
   	}

public MuExp translateMiddle(MuExp template, str indent, s: (StringMiddle) `<MidStringChars mid> <Expression expression> <StringMiddle tail>`) {
	midIndent = computeIndent(mid);
    return muBlock([ translateMidChars(template, mid),
    		         translateExpOrTemplateInStringLiteral(template, midIndent, expression),
                     translateMiddle(template, indent + midIndent, tail)
                   ]);
}

// --- translateTail

private MuExp translateTail(MuExp template, str indent, s: (StringTail) `<MidStringChars mid> <Expression expression> <StringTail tail>`) {
    midIndent = computeIndent(mid);
    return muBlock([ translateMidChars(template, mid),
    			     translateExpOrTemplateInStringLiteral(template, midIndent, expression),
                     translateTail(template, indent + midIndent, tail)
                   ]);
}
	
private MuExp translateTail(MuExp template, str indent, (StringTail) `<PostStringChars post>`) {
  content = removeMargins("<post>"[1..-1]);
  return size(content) == 0 ? muBlock([]) : muTemplateAdd(template, astr(), deescape(content));
}

private MuExp translateTail(MuExp template, str indent, s: (StringTail) `<MidStringChars mid> <StringTemplate stemplate> <StringTail tail>`) {
    midIndent = computeIndent(mid);
    return muBlock([ translateMidChars(template, mid),
                     translateTemplate(template, indent + midIndent, stemplate),
                     translateTail(template, indent + midIndent,tail)
                   ]);
 } 
 
 // --- translateTemplate 
 
 private default MuExp translateTemplate(MuExp template, str indent, StringTemplate stemplate){
    return translateExpOrTemplateInStringLiteral(template, indent, stemplate);
 }
 
 private default MuExp translateTemplate(MuExp template, str indent, Expression expression){
    return translateExpOrTemplateInStringLiteral(template, indent, expression);
 }
 
// -- location literal  ----------------------------------------------

MuExp translateLiteral((Literal) `<LocationLiteral src>`) = 
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
     muPrim("create_loc", aloc(), [astr()], [muPrim("add", astr(), [astr(), astr()], [translateProtocolPart(protocolPart), translatePathPart(pathPart)], l@\loc)], l@\loc);
 
private MuExp translateProtocolPart((ProtocolPart) `<ProtocolChars protocolChars>`) = muCon("<protocolChars>"[1..]);
 
private MuExp translateProtocolPart(p: (ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`) =
    muPrim("add", astr(), [astr(), astr(), astr()], [muCon("<pre>"[1..-1]), translate(expression), translateProtocolTail(tail)], p@\loc);
 
private MuExp  translateProtocolTail(p: (ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`) =
   muPrim("add", astr(), [astr(), astr(), astr()], [muCon("<mid>"[1..-1]), translate(expression), translateProtocolTail(tail)], p@\loc);
   
private MuExp translateProtocolTail((ProtocolTail) `<PostProtocolChars post>`) = muCon("<post>"[1 ..]);

private MuExp translatePathPart((PathPart) `<PathChars pathChars>`) = muCon("<pathChars>"[..-1]);

private MuExp translatePathPart(p: (PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`) =
   muPrim("add", astr(), [astr(), astr(), astr()], [ muCon("<pre>"[..-1]), translate(expression), translatePathTail(tail)], p@\loc);

private MuExp translatePathTail(p: (PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`) =
   muPrim("add", astr(), [astr(), astr(), astr()], [ muCon("<mid>"[1..-1]), translate(expression), translatePathTail(tail)], p@\loc);
   
private MuExp translatePathTail((PathTail) `<PostPathChars post>`) = muCon("<post>"[1..-1]);

// -- all other literals  --------------------------------------------

default MuExp translateLiteral((Literal) `<Literal s>`) {
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
    translateLiteral(s);
    
MuExp translateBool(e:(Expression)  `<Literal s>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = 
    translateBool(s, btscopes, trueCont, falseCont);

// -- concrete syntax expression  ------------------------------------

// this detects that the concrete string has been parsed correctly and we can 
// switch to compiling the tree values to muRascal expressions:
MuExp translate(e:appl(prod(Symbol::label("parsed",Symbol::lex("Concrete")), [_],_),[Tree concrete])){
  return translateConcreteExpression(concrete);
}  
  
// this detects that a parse error has occurred earlier and we generate an exception here:  
MuExp translate(e:appl(prod(label("typed",lex("Concrete")), [_],_),_))
  = muValueBlock(avalue(), [muThrow(muCon("(compile-time) parse error in concrete syntax"), e@\loc)]);   

// these three constant parts of trees are directly mapped to constants:
private MuExp translateConcreteExpression(t:appl(prod(lit(_),_, _), _)) = muCon(t);
private MuExp translateConcreteExpression(t:appl(prod(cilit(_),_, _), _)) = muCon(t);
private MuExp translateConcreteExpression(t:appl(prod(layouts(_),_, _), _)) = muCon(t);
  
// this is a pattern variable, which we substitute with a reference to a muVariable:  
private MuExp translateConcreteExpression(t:appl(prod(Symbol::label("$MetaHole", Symbol _),[_], _), [ConcreteHole hole])) {
    <fuid, pos> = getVariableScope("<hole.name>", getConcreteHoleVarLoc(t));
    
    return mkVar(unescape("<hole.name>"), hole.name@\loc);
    //return muVar("<hole.name>", fuid, pos, getType(hole.symbol@\loc));    
}

// Four cases of lists are detected to be able to implement splicing
// splicing is different for separated lists from normal lists
private MuExp translateConcreteExpression(t:appl(p:Production::regular(s:Symbol::iter(Symbol elem)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteExpressionList(elem, args, t@\loc), t@\loc);

private MuExp translateConcreteExpression(t:appl(p:Production::regular(s:Symbol::\iter-star(Symbol elem)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteExpressionList(elem, args, t@\loc), t@\loc); 
   
private MuExp translateConcreteExpression(t:appl(p:Production::regular(s:Symbol::\iter-seps(Symbol elem, list[Symbol] seps)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteExpressionSeparatedList(elem, seps, args, t@\loc), t@\loc);

private MuExp translateConcreteExpression(t:appl(p:Production::regular(s:Symbol::\iter-star-seps(Symbol elem, list[Symbol] seps)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteExpressionSeparatedList(elem, seps, args, t@\loc), t@\loc); 

private MuExp translateConcreteExpression(char(int i)) =  muTreeChar(i);

// this is a normal parse tree node:
private default MuExp translateConcreteExpression(t:appl(Production p, list[Tree] args)) 
  = muTreeAppl(muCon(p), [translateConcreteExpression(a) | a <- args], (t@\loc?) ? t@\loc : |unknown:///|); // TODO: t@\loc does not work here, why?????


bool isListPlusVar(Symbol elem, appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter(elem)))}), [_])) = true;
bool isListPlusVar(Symbol elem, appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter-seps(elem,_)))}), [_])) = true;
default bool isListPlusVar(Symbol _, Tree _) = false;

bool isListStarVar(Symbol elem, t:appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter-star(elem)))}), [_])) = true;
bool isListStarVar(Symbol elem, t:appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter-star-seps(elem,_)))}), [_])) = true;
default bool isListStarVar(Symbol _,Tree _) = false;

bool isListVar(Symbol elem, Tree x) = isListPlusVar(elem, x) || isListStarVar(elem, x);

private MuExp translateConcreteExpressionList(Symbol eltType, list[Tree] elems, loc src) {
    if(elems == []){
        return muCon([]);
    } else {
        str fuid = topFunctionScope();
           
        writer = muTmpListWriter(nextTmp("writer"), fuid);   
        
        enterWriter(writer.name);
        
        code = for (Tree elem <- elems) {
           if (isListVar(eltType, elem)) {
              append muPrim("splice_list", avoid(), [avalue(), treeType], [writer, muTreeGetArgs(translateConcreteExpression(elem))], src);
           }
           else {
              append muPrim("add_list_writer", avoid(), [avalue(), treeType], [writer, translateConcreteExpression(elem)], src);
           }
        }
       
        code = [muConInit(writer, muPrim("open_list_writer", avalue(), [], [], src)), *code];
        leaveWriter();
       
        return muValueBlock(\alist(treeType), [*code, muPrim("close_list_writer", alist(treeType), [avalue()], [writer], src)]);
    }
}

private MuExp translateConcreteExpressionSeparatedList(Symbol _, list[Symbol] _, [], loc _) = muCon([]);

private MuExp translateConcreteExpressionSeparatedList(Symbol eltType, list[Symbol] _, [Tree single], loc src) {
    str fuid = topFunctionScope();
       
    writer = muTmpListWriter(nextTmp("writer"), fuid);   
    
    enterWriter(writer.name);
    
    code = [muConInit(writer, muPrim("open_list_writer", avalue(), [], [], src))];
       
    if (isListVar(eltType, single)) {
       varExp = muTreeGetArgs(translateConcreteExpression(single));
       code += [muPrim("splice_list", avoid(), [avalue(), treeType], [writer, varExp], src)];
    }
    else {
       code += [muPrim("add_list_writer", avoid(), [avalue(), treeType], [writer, translateConcreteExpression(single/*elem*/)], src)];
    }
    leaveWriter();
        
    return muValueBlock(\alist(treeType), [*code, muPrim("close_list_writer", alist(treeType), [avalue()], [writer], src)]);    
}

private MuExp translateConcreteExpressionSeparatedList(Symbol eltType, list[Symbol] sepTypes, list[Tree] elems:[_,_,*_], loc src) {
    str fuid = topFunctionScope();
       
    writer = muTmpListWriter(nextTmp("writer"), fuid);   
    
    enterWriter(writer.name);
    
    code = [muConInit(writer, muPrim("open_list_writer", avalue(), [], [], src))];
    
    // first we compile all element except the final separators and the final element:
    while ([Tree first, *Tree sepTrees, Tree second, *Tree more] := elems && size(sepTypes) == size(sepTrees)) {
      varExp = translateConcreteExpression(first);

      // first we splice or add the first element:
      if (isListVar(eltType, first)) {
        code += [muPrim("splice_list", avoid(), [avalue(), treeType], [writer, muTreeGetArgs(varExp)], first@\loc?|unknown:///|)];
      }
      else {
        code += [muPrim("add_list_writer", avoid(), [avalue(), treeType], [writer, varExp], first@\loc?|unknown:///|)];
      }
      
      sepCode    = [muPrim("add_list_writer", avoid(), [avalue(), treeType], [writer, muCon(e)], e@\loc?|unknown:///|) | e <- sepTrees];
      secondVarExp = translateConcreteExpression(second);
       
      // then separators are optionally added:  
      if (isListStarVar(eltType, first) && isListStarVar(eltType, second)) {
           
            if (more == []) {
              // if they are both star variables, and this is the last element then they both must be non-empty:
              sepCode = [muIfExp(muNotNativeBool(muEqual(muTreeGetArgs(varExp), muCon([]))),
                         muValueBlock(avoid(), sepCode),
                         muBlock([]))];
            
              sepCode = [muIfExp(muNotNativeBool(muEqual(muTreeGetArgs(secondVarExp), muCon([]))),
                          muValueBlock(avoid(), sepCode),
                          muBlock([]))];
            }
            else {
              // only print the separators if the first list is non-empty
              sepCode = [muIfExp(muNotNativeBool(muEqual(muTreeGetArgs(varExp), muCon([]))),
                         muValueBlock(avoid(), sepCode),
                         muBlock([]))];
            }
              
            code += sepCode;
      }
      else {
        // one of first or second is not a star var, so we always need the separators
        code += sepCode;
      }    
       
      if (more == []) {
        // the last element must be printed, or sliced now:
        if (isListVar(eltType, second)) {
          code += [muPrim("splice_list", avoid(), [avalue(), treeType], [writer, muTreeGetArgs(secondVarExp)], second@\loc?|unknown:///|)];
        }
        else {
          code += [muPrim("add_list_writer", avoid(), [avalue(), treeType], [writer, secondVarExp], second@\loc?|unknown:///|)];
        }
      }
      elems = [second, *more];
    }
     
   
    leaveWriter();
   
    return muValueBlock(\alist(treeType), [*code, muPrim("close_list_writer", alist(treeType), [avalue()], [writer], src)]);   
}


// -- block expression ----------------------------------------------

MuExp translate(e:(Expression) `{ <Statement+ statements> }`) = 
    muBlock([translate(stat, ()) | Statement stat <- statements]);

// -- parenthesized expression --------------------------------------

BTINFO getBTInfo(e: (Expression) `(<Expression expression>)`, BTSCOPE btscope, BTSCOPES btscopes){
   BTINFO btinfo = getBTInfo(expression, btscope, btscopes);
   return registerBTScope(e, btinfo.btscope, btinfo.btscopes);
}

MuExp translate((Expression) `(<Expression expression>)`) {
    return translate(expression);
}
     
MuExp translateBool((Expression) `(<Expression expression>)`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) =
     translateBool(expression, btscopes, trueCont, falseCont);

// -- closure expression --------------------------------------------

MuExp translate (e:(Expression) `<Type tp> <Parameters parameters> { <Statement+ statements> }`) =
    translateClosure(e, parameters, [stat | stat <- statements]);

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) =
    translateClosure(e, parameters, [stat | stat <- statements]);

// Translate a closure   
 
 private MuExp translateClosure(Expression e, Parameters parameters, list[Statement] cbody) {
 	uid = e@\loc;
	fuid = convert2fuid(uid);
	surrounding = topFunctionScope();
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.formals);
	bool isVarArgs = ftype.varArgs;
  	
  	// Keyword parameters
     lrel[str name, AType atype, MuExp defaultExp]  kwps = translateKeywordParameters(parameters);
    
    enterSignatureSection();
    // TODO: we plan to introduce keyword patterns as formal parameters
    <formalVars, funBody> = translateFunction(fuid, parameters.formals.formals, ftype, muBlock([translate(stat, ()) | stat <- cbody]), false, []);
    typeVarsInParams = getFunctionTypeParameters(ftype);
      
    leaveSignatureSection();
    addFunctionToModule(muFunction("$CLOSURE_<uid.begin.line>A<uid.offset>", 
                                   "$CLOSURE_<uid.begin.line>A<uid.offset>", 
                                   ftype, 
                                   formalVars,
                                   [],
                                   kwps,
                                   surrounding, 
  								   isVarArgs, 
  								   false,
  								   false,
  								   getExternalRefs(funBody, fuid),  // << TODO
  								   getLocalRefs(funBody),
  								   {},
  								   e@\loc,
  								   [],
  								   (),
  								   funBody));
  	
  	leaveFunctionScope();								  
  	return muFun(uid, ftype); // TODO!
}

// -- range expression ----------------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  writer = muTmpListWriter(nextTmp("range_writer"), fuid);
  elemType = alub(getType(first), getType(last));
  elem = muTmpIValue(nextTmp("range_elem"), fuid,elemType);
  resultType = getType(e);
  
  return
    muValueBlock(resultType,
                 [ muConInit(writer, muPrim("open_list_writer", avalue(), [], [], e@\loc)),
                   muForRange("", elem, translate(first), muCon(0), translate(last), muPrim("add_list_writer", avoid(), [resultType, elemType], [writer, elem], e@\loc), muBlock([])),
                   muPrim("close_list_writer", resultType, [avalue()], [writer], e@\loc)
                 ]);
}

// -- range with step expression ------------------------------------

MuExp translate (e:(Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]`) {
  str fuid = topFunctionScope();
  writer = muTmpListWriter(nextTmp("range_writer"), fuid);
  elemType = lubList([getType(first), getType(second), getType(last)]);
  elem = muTmpIValue(nextTmp("range_elem"), fuid, elemType);
  
  return
    muValueBlock(getType(e),
                 [ muConInit(writer, muPrim("open_list_writer", alist(elemType), [], [], e@\loc)),
                   muForRange("", elem, translate(first), translate(second), translate(last), muPrim("add_list_writer", avoid(), [elemType], [writer, elem], e@\loc), muBlock([])),
                   muPrim("close_list_writer", avoid(), [avalue()], [writer], e@\loc)
                 ]);
}

// -- visit expression ----------------------------------------------

MuExp translate (e:(Expression) `<Label label> <Visit visitItself>`) = translateVisit(label, visitItself, ());

public MuExp translateVisit(Label label, lang::rascal::\syntax::Rascal::Visit \visit, BTSCOPES btscopes) {	
    str fuid = topFunctionScope();
    subjectType = getType(\visit.subject);
    enterVisit(subjectType);
   
    visitName = getLabel(label, "VISIT");
    visitSubject = muTmpIValue("$<visitName>_subject", fuid, avalue());    
	visitResult = muTmpIValue("$<visitName>_result", fuid, avalue());    
    
	bool isStringSubject = false; //subjectType == \str();
	
	cases = [ c | Case c <- \visit.cases ];
	
	useConcreteFingerprint = hasConcretePatternsOnly(cases) 
	                         && isConcreteType(subjectType); // || subjectType == adt("Tree",[],_));
	
	reachable_syms = { avalue() };
	reachable_prods = {};
	
	if(optimizeVisit()){
	   tc = getTypesAndConstructorsInVisit(cases);
	   <reachable_syms1, reachable_prods1> = getReachableTypes(subjectType, tc.constructors, tc.types, useConcreteFingerprint);
	   reachable_syms = reachable_syms1;
	   reachable_prods = reachable_prods1;
	   //println("reachableTypesInVisit, <size(reachable_syms)>: <reachable_syms>, <size(reachable_prods)>: <reachable_prods>");
	}
	
	ddescriptor = descendantDescriptor(useConcreteFingerprint, reachable_syms, reachable_prods, getReifiedDefinitions());
		
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
	<case_code, default_code> = translateSwitchCases(visitName, visitSubject, fuid, useConcreteFingerprint, cases, muSucceedVisitCase(visitName), btscopes);
    
    visitCode = muVisit(visitName, muConInit(visitSubject, translate(\visit.subject)), case_code, default_code, vdescriptor);
    leaveVisit();
    return visit(visitCode) { case muReturn0() => muReturn0FromVisit() case muReturn1(AType t, MuExp e) => muReturn1FromVisit(t, e) };
}

tuple[set[AType] types, set[str] constructors] getTypesAndConstructorsInVisit(list[Case] cases){
	reachableTypes1 = {};// TODO: renamed for new (experimental) type checker
	reachableConstructors = {};
	for(c <- cases){
		if(c is patternWithAction){
			tc = getTypesAndConstructorNames(c.patternWithAction.pattern);
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
		case (Statement) `insert <DataTarget _> <Statement _>`: return true;
		case Visit _: ;
	}
	return false;
}

// -- reducer expression --------------------------------------------

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(e);

private MuExp translateReducer(Expression e){
    redName = nextTmp("REDUCER");
    str fuid = topFunctionScope();
    
    //btfree = all(Expression c <- e.generators, backtrackFree(c));
    //enterBacktrackingScope(redName);
    
    conds = normalizeAnd([g | g <- e.generators]);
    btscopes = getBTScopesAnd(conds, redName, ());
    
    reducerTmp = muTmpIValue(nextTmp("reducer"), fuid, getType(e));
    pushIt(reducerTmp);
    code =  muValueBlock(getType(e), [ muVarInit(reducerTmp, translate(e.init)), translateAndConds(btscopes, conds, muAssign(reducerTmp, translate(e.result)), muBlock([])),  reducerTmp ]);
    popIt();
    //if(!btfree){
    //    code = updateBTScope(code, redName, getResumptionScope(redName));
    //}
    //
    //leaveBacktrackingScope(redName);
    return code;
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) {
    return muPrim("create_reifiedType", avalue(), [avalue(), avalue()], [translate(symbol), translate(definitions)], e@\loc);   
}

// -- call expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`){  
   lrel[str,MuExp] kwargs = translateKeywordArguments(keywordArguments);
   ftype = getType(expression); // Get the type of a receiver
   MuExp receiver = translate(expression);
   list[MuExp] args = [ translate(a) | Expression a <- arguments ];
   exp_type = getOuterType(expression);
   if(exp_type == "astr"){
   		return muPrim("create_node", getType(e), [ getType(arg) | arg <- arguments ], [receiver, *args, muKwpActuals(kwargs)], e@\loc);
   }
  
   if(exp_type == "aloc"){
       if(size(args) == 2){
            return muPrim("create_loc_with_offset", aloc(), [aloc(), aint(), aint()], [receiver, *args], e@\loc);
       } else {
         return muPrim("create_loc_with_offset_and_begin_end", aloc(), [aloc(),aint(), aint(), atuple(atypeList([aint(), aint()])), atuple(atypeList([aint(), aint()]))], [receiver, *args], e@\loc);
       }
   }
   str fname = unescape("<expression>");
   if(!isOverloadedAType(ftype) || fname in { "choice", "priority", "associativity"}){
        // Try to reduce non-overloaded function call (and three selected overloaded ones) to a constant
   		try {
   			return translateConstantCall(fname, args); //TODO: kwargs?
   		} 
   		catch "NotConstant":  /* not a constant, generate an ordinary call instead */;
   }
   return muOCall(receiver, ftype, args, kwargs, e@\loc);
}

private lrel[str,MuExp] translateKeywordArguments((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArguments>`) {
   // Version that does not propagates values of set keyword parameters downwards and is compatible with the interpreter
    
   kwargs = [];
   if(keywordArguments is \default){
      kwargs = [ <unescape("<kwarg.name>"), translate(kwarg.expression)>  | kwarg <- keywordArguments.keywordArgumentList ];
   }
   return kwargs;
}

MuExp translateBool(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
   = muIfExp(translate(e), trueCont, falseCont);
   
// -- any expression ------------------------------------------------

BTINFO getBTInfo(e:(Expression) `any ( <{Expression ","}+ generators> )`, BTSCOPE btscope, BTSCOPES btscopes){
    BTSCOPE btscope1 = <"<btscope.enter>_ANY", btscope.resume,  btscope.resume>;
    ngenerators = normalizeAnd([g | g <- generators]);
    for(gen <- ngenerators){
        <btscope1, btscopes> = getBTInfo(gen, btscope1, btscopes);
    }
    return registerBTScope(e, <btscope1.enter, btscope1.resume, btscope.resume>, btscopes);
}
    
MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`){
    gens = normalizeAnd([ g | g <- generators ]);
    return translateQuantor(gens, quantorAll = false);
}

MuExp translateBool (e:(Expression) `any ( <{Expression ","}+ generators> )`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e), trueCont, falseCont);


// -- all expression ------------------------------------------------

BTINFO getBTInfo(e:(Expression) `all ( <{Expression ","}+ generators> )`, BTSCOPE btscope, BTSCOPES btscopes){
    BTSCOPE btscope1 = <"<btscope.enter>_ALL", btscope.resume,  btscope.resume>;
    ngenerators = normalizeAnd([g | g <- generators]);
    for(gen <- ngenerators){
        <btscope1, btscopes> = getBTInfo(gen, btscope1, btscopes);
    }
    return registerBTScope(e, <btscope1.enter, btscope1.resume, btscope.resume>, btscopes);
}

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`){
    gens = normalizeAnd([ g | g <- generators ]);
    my_btscopes = getBTScopesAnd(gens, nextTmp("ALL"), ());
    return translateQuantor(gens);
}

MuExp translateBool (e:(Expression) `all ( <{Expression ","}+ generators> )`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e), trueCont, falseCont);

// -- all/any, !all/!any quantors -------------------------------------------------

MuExp translateQuantor(list[Expression] generators, bool quantorAll = true, bool negateGenerators = false){
    conds = normalizeAnd([ g | g <- generators]);
    done = muTmpIValue(nextTmp("done"), topFunctionScope(), abool());
    enter_quantor = nextTmp(quantorAll ? "ALL" : "ANY");
    my_btscopes = getBTScopesAnd(conds, enter_quantor, ());
    if(quantorAll){
     return negateGenerators ? translateQuantorNotAll(conds, my_btscopes, done) 
                             : translateQuantorAll(conds, my_btscopes, done);
    }
    return negateGenerators ? translateQuantorNotAny(conds, my_btscopes, done) 
                            : translateQuantorAny(conds, my_btscopes, done);
}

MuExp translateQuantorAll(list[Expression] conds, BTSCOPES my_btscopes, MuExp done){
    enter_gen = getEnter(conds[0], my_btscopes);
    trueCont = muContinue(enter_gen);
    falseCont = muBlock([]);
    
    if(all(cond <- conds, backtrackFree(cond))){
        for(cond <- reverse(conds)){
            trueCont = translateBool(cond, my_btscopes, trueCont, falseCont);
        }
        return muValueBlock(abool(), [ muVarInit(done, muCon(true)), trueCont ]);
    }
    for(i <- reverse(index(conds))){
        cont = i == 0 ? falseCont : muBlock([ muAssign(done, muCon(false)), muBreak(getFail(conds[i-1], my_btscopes))]);
        trueCont = translateBool(conds[i], my_btscopes, trueCont, cont);
    }
    
    return muValueBlock(abool(), [ muVarInit(done, muCon(true)), muExists(enter_gen, trueCont), done ]);
}

MuExp translateQuantorNotAll(list[Expression] conds, BTSCOPES my_btscopes, MuExp done){
    enter_gen = getEnter(conds[0], my_btscopes);
    trueCont = falseCont = muBlock([]); 
    
    if(all(cond <- conds, backtrackFree(cond))){
        for(cond <- reverse(conds)){
            trueCont = translateBool(cond, my_btscopes, trueCont, falseCont);
        }
        return muValueBlock(abool(), [ muVarInit(done, muCon(true)), trueCont ]);
    }
    for(i <- reverse(index(conds))){
        cont = i == 0 ? falseCont : muBlock([ muAssign(done, muCon(true)), muSucceed(enter_gen)]);
        trueCont = translateBool(conds[i], my_btscopes, trueCont, cont);
    }
    
    return muValueBlock(abool(), [ muVarInit(done, muCon(false)), muExists(enter_gen, trueCont), done ]);
}

MuExp translateQuantorAny(list[Expression] conds, BTSCOPES my_btscopes, MuExp done){
    enter_gen = getEnter(conds[0], my_btscopes);
    trueCont = muBlock([ muAssign(done, muCon(true)), muSucceed(enter_gen) ]);
    falseCont = muBlock([]);
    
    if(all(cond <- conds, backtrackFree(cond))){
        for(cond <- reverse(conds)){
            trueCont = translateBool(cond, my_btscopes, trueCont, falseCont);
        }
        return muValueBlock(abool(), [ muVarInit(done, muCon(true)), trueCont ]);
    }
    for(i <- reverse(index(conds))){
        cont = i == 0 ? falseCont : muFail(getFail(conds[i-1], my_btscopes), comment="any quantor"); 
        trueCont = translateBool(conds[i], my_btscopes, trueCont, cont);
    }
    
    return muValueBlock(abool(), [ muVarInit(done, muCon(false)), muExists(enter_gen, trueCont), done ]);
}

MuExp translateQuantorNotAny(list[Expression] conds, BTSCOPES my_btscopes, MuExp done){
    enter_gen = getEnter(conds[0], my_btscopes);
    trueCont = muBlock([ muAssign(done, muCon(false)), muSucceed(enter_gen) ]);
    falseCont = muBlock([]);
    
    if(all(cond <- conds, backtrackFree(cond))){
        for(cond <- reverse(conds)){
            trueCont = translateBool(cond, my_btscopes, trueCont, falseCont);
        }
        return muValueBlock(abool(), [ muVarInit(done, muCon(true)), trueCont ]);
    }
    for(i <- reverse(index(conds))){
        cont = i == 0 ? falseCont : muFail(getFail(conds[i-1], my_btscopes), comment="not any");
        trueCont = translateBool(conds[i], my_btscopes, trueCont, cont);
    }
    
    return muValueBlock(abool(), [ muVarInit(done, muCon(true)), muExists(enter_gen, trueCont), done ]);
}

// -- comprehension expression --------------------------------------

MuExp translate ((Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

private list[MuExp] translateComprehensionContribution(str kind, AType resultType, MuExp writer, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muPrim("splice_<kind>", resultType, [avalue(), getType(r)], [writer, translate(exp)], exp@\loc);
	    } else {
	      append muPrim("add_<kind>_writer", resultType, [avalue(), getType(r)], [writer, translate(r)], r@\loc);
	    }
	  }
} 

private MuExp translateComprehension(c: (Comprehension) `[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`) {
    //println("translateComprehension (list): <generators>");
    str fuid = topFunctionScope();
    writer = muTmpListWriter(nextTmp("listwriter"), fuid);
    resultType = getType(c);
    conds = normalizeAnd([ g | Expression g <- generators ]);
    btscopes = getBTScopesAnd(conds, nextTmp("LCOMP"), ());
    //iprintln(btscopes);
    return 
        muValueBlock(resultType,
                     [ muConInit(writer, muPrim("open_list_writer", avalue(), [], [], c@\loc)),
                       translateAndConds(btscopes, conds, muBlock(translateComprehensionContribution("list", resultType, writer, [r | r <- results])), muBlock([])),
                       muPrim("close_list_writer", getType(c), [avalue()], [writer], c@\loc) 
                     ]);
}

private MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    //println("translateComprehension (set): <generators>");
    str fuid = topFunctionScope();
    writer = muTmpSetWriter(nextTmp("setwriter"), fuid);
    resultType = getType(c);
    conds = normalizeAnd([ g | Expression g <- generators ]);
    btscopes = getBTScopesAnd(conds, nextTmp("SCOMP"), ());
    //iprintln(btscopes);
    return
        muValueBlock(resultType,
                     [ muConInit(writer, muPrim("open_set_writer", avalue(), [], [], c@\loc)),
                      translateAndConds(btscopes, conds, muBlock(translateComprehensionContribution("set", resultType, writer, [r | r <- results])), muBlock([])),
                      muPrim("close_set_writer", getType(c), [avalue()], [writer], c@\loc) 
                    ]);
}

private MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    //println("translateComprehension (map): <generators>");
    str fuid = topFunctionScope();
    writer = muTmpMapWriter(nextTmp("mapwriter"), fuid);
    resultType = getType(c);
    conds = normalizeAnd([ g | Expression g <- generators ]);
    btscopes = getBTScopesAnd(conds, nextTmp("MCOMP"), ());
    return
        muValueBlock(resultType,
                     [ muConInit(writer, muPrim("open_map_writer", avalue(), [], [], c@\loc)),
                       translateAndConds(btscopes, conds, muPrim("add_map_writer", avoid(), [getType(from), getType(to)], [writer] + [ translate(from), translate(to)], c@\loc), muBlock([])),
                       muPrim("close_map_writer", resultType, [avalue()], [writer], c@\loc) 
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
 elmType = getElementType(getType(e));
 if(containsSplices(es)){
       str fuid = topFunctionScope();
       writer = kind == "list" ? muTmpListWriter(nextTmp("writer"), fuid) : muTmpSetWriter(nextTmp("writer"), fuid);;
      
       
       kindwriter_open_code = muPrim("open_<kind>_writer", avalue(), [], [], e@\loc);
       
       enterWriter(writer.name);
       code = [ muConInit(writer, kindwriter_open_code) ];
       for(elem <- es){
           if(elem is splice){
              code += muPrim("splice_<kind>", avoid(), [avalue(), getType(elem)], [writer, translate(elem.argument)], elem.argument@\loc);
            } else {
              code += muPrim("add_<kind>_writer", avoid(), [avalue(), elmType], [writer, translate(elem)], elem@\loc);
           }
       }
       code += [ muPrim("close_<kind>_writer", getType(e), [avalue()], [ writer ], e@\loc) ];
       leaveWriter();
       return muValueBlock(getType(e), code);
    } else {
      //if(size(es) == 0 || all(elm <- es, isConstant(elm))){
      //   return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      //} else 
        return muPrim("create_<kind>", getType(e), [elmType], [ translate(elem) | Expression elem <- es ], e@\loc);
    }
}

// -- reified type expression --------------------------------------

MuExp translate (e: (Expression) `# <Type tp>`) {
	t = translateType(tp);
	res = muATypeCon(t, collectNeededDefs(t));
	//println("<e> ==\>"); iprintln(res);
	return res;
}	

// -- tuple expression ----------------------------------------------

MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) {
    //if(isConstant(e)){
    //  return muCon(readTextValueString("<e>"));
    //} else
        return muPrim("create_tuple", getType(e), [ getType(elem) | Expression elem <- elements], [ translate(elem) | Expression elem <- elements ], e@\loc);
}

// -- map expression ------------------------------------------------

MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) {
   //if(isConstant(e)){
   //  return muCon(readTextValueString("<e>"));
   //} else 
   mapType = getType(e);
   return muPrim("create_map", mapType, [mapType.keyType, mapType.valType], [ translate(m.from), translate(m.to) | m <- mappings ], e@\loc);
}   

// -- it expression (in reducer) ------------------------------------

MuExp translate (e:(Expression) `it`) = 
    muTmpIValue(topIt().name,topIt().fuid, getType(e));
    
MuExp translateBool(e:(Expression) `it`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = 
    muTmpIValue(topIt().name,topIt().fuid, getType(e));
 
// -- qualified name expression -------------------------------------
 
MuExp translate((Expression) `<QualifiedName v>`) = 
    translate(v);
    
MuExp translateBool((Expression) `<QualifiedName v>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = 
    translateBool(v, btscopes, trueCont, falseCont);
 
MuExp translate((QualifiedName) `<QualifiedName v>`) =
    mkVar("<v>", v@\loc);

MuExp translateBool((QualifiedName) `<QualifiedName v>`, BTSCOPES _btscopes, MuExp trueCont, MuExp falseCont) =
    muIfExp(mkVar("<v>", v@\loc), trueCont, falseCont);

// For the benefit of names in regular expressions

MuExp translate((Name) `<Name name>`) =
    mkVar(unescape("<name>"), name@\loc);

// -- subscript expression ------------------------------------------
// Comes in 2 flavours:
// - ordinary: translateSubscript with isDefined=false
// - as part of an isDefined expression: translateSubscript with isGuarded=true

MuExp translate(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`) =
	translateSubscript(e, false);


private MuExp translateSubscript(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`, bool isGuarded){
   op = isGuarded ? "guarded_subscript" : "subscript";
   access = muPrim(op, avalue() /*getType(e)*/, getType(exp) + [getType(s) | s <- subscripts],
                       translate(exp) + [isWildCard("<s>") ? muCon("_") : translate(s) | Expression s <- subscripts], e@\loc);
   
   return access;
}

MuExp translateBool(Expression e:(Expression) `<Expression exp> [ <{Expression ","}+ subscripts> ]`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return muIfExp(translate(e), trueCont, falseCont);
}

// -- slice expression ----------------------------------------------

MuExp translate ((Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

// -- slice with step expression ------------------------------------

MuExp translate ((Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) {
    ot = getType(expression);
    return muPrim("slice", ot, [ot], [ translate(expression), translateOpt(optFirst), muNoValue(), translateOpt(optLast) ], expression@\loc);
}

public MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muNoValue() : translate(optExp.expression);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) {
    ot = getType(expression);
    return muPrim("slice", ot, [ot], [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ], expression@\loc);
}

// -- field access expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) {
   orgtp = getType(expression);
   tp = stripStart(orgtp);
   fieldType = getType(field);
   ufield = unescape("<field>");
   
   if(isTupleAType(tp) || isRelAType(tp) || isListRelAType(tp) || isMapAType(tp)) {
       return translateProject(e, expression, [(Field)`<Name field>`], e@\loc, false);
   }
   
   if(isADTAType(tp)){
        <definingModule, consType, isKwp> = getConstructorInfo(orgtp, fieldType, ufield);
        return isKwp ? muGetKwField(consType, tp, translate(expression), ufield, definingModule)
                     : muGetField(getType(e), consType, translate(expression), ufield);
    } else 
    if(asubtype(tp, treeType)){
        <definingModule, consType, isKwp> = getConstructorInfo(treeType, fieldType, ufield);
        return isKwp ? muGetKwField(consType, treeType, translate(expression), ufield, definingModule)
                     : muGetField(getType(e), treeType, translate(expression), ufield); // Can this ever happen?
    }
    
    
    return muGetField(getType(e), getType(expression), translate(expression), ufield);   
}

MuExp translateBool(e:(Expression) `<Expression expression> . <Name field>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return muIfExp(translate(e), trueCont, falseCont);
}

// -- field update expression ---------------------------------------

MuExp translate ((Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) {
    tp = getType(expression);  
    list[str] fieldNames = [];
    if(isRelAType(tp)){
       tp = getSetElementType(tp);
    } else if(isListAType(tp)){
       tp = getListElementType(tp);
    } else if(isMapAType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTAType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    } else if(isLocAType(tp)){
     	return muSetField(tp, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    } else if(isNodeAType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    } else if(asubtype(tp, treeType)){
        return muSetField(treeType, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    }
    if(isTupleAType(tp) && tupleHasFieldNames(tp)){
    	  fieldNames = getTupleFieldNames(tp);
    }	
    //TODO
    return muSetField(tp, getType(expression), translate(expression), indexOf(fieldNames, unescape("<key>")), translate(replacement));
}

// -- field project expression --------------------------------------

MuExp translate (e:(Expression) `<Expression expression> \< <{Field ","}+ fields> \>`) =
  translateProject(e, expression, [f | f <- fields], e@\loc, false);

MuExp translateProject(Expression e, Expression base, list[Field] fields, loc src, bool isGuarded){
    tp = getType(base); 
 
    if(isNodeAType(tp)){
        fieldName = ["<f>" | f <- fields][0];
        if(isGuarded){
            return muGuardedGetField(avalue(), tp, translate(base), fieldName);
         } else {
           return muGetField(avalue(), tp, translate(base), fieldName);
         }
    }
    
    list[str] fieldNames = [];
    if(isRelAType(tp)){
       tp = getSetElementType(tp);
    } else if(isListAType(tp)){
       tp = getListElementType(tp);
    } else if(isMapAType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isLocAType(tp)){
        return isGuarded ? muGuardedGetField(tp, getType(base), translate(base), unescape("<fields[0]>"))
                         : muGetField(tp, getType(base), translate(base), unescape("<fields[0]>"));
    }
    //println("translateProject: <e>");
    if(tupleHasFieldNames(tp)){
       	fieldNames = getTupleFieldNames(tp);
    }
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon(indexOf(fieldNames, unescape("<f>"))) | f <- fields];
      
    return muPrim((isGuarded ? "guarded_" : "") + "field_project", getType(e), [getType(base)], [ translate(base), *fcode], src);
}

// -- set annotation expression -------------------------------------

// Deprecated
MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) {
    tp = getType(expression);  
    list[str] fieldNames = [];
    uname = unescape("<name>");
    if(isRelAType(tp)){
       tp = getSetElementType(tp);
    } else if(isListAType(tp)){
       tp = getListElementType(tp);
    } else if(isMapAType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTAType(tp)){
        if(asubtype(tp, treeType) && uname == "loc"){ //TODO: remove when loc anno has been removed
            uname = "src";
        }
        return muSetField(tp, getType(expression), translate(expression), uname, translate(val));
    } else if(isLocAType(tp)){
        return muSetField(tp, getType(expression), translate(expression), uname, translate(val));
    } else if(isNodeAType(tp)){
        return muSetField(tp, getType(expression), translate(expression), uname, translate(val));
    }
    if(tupleHasFieldNames(tp)){
          fieldNames = getTupleFieldNames(tp);
    }   
    //TODO
    return muSetField(tp, getType(expression), translate(expression), indexOf(fieldNames, uname), translate(val));
    
}

// -- get annotation expression -------------------------------------
//Deprecated
MuExp translate (e:(Expression) `<Expression expression>@<Name name>`) {
    tp = getType(expression);  
    uname = unescape("<name>");
    //if(asubtype(tp, treeType) && uname == "loc"){ //TODO: remove when loc anno has been removed
    //    uname = "src"; // rename loc to src
    //}
    //return muGetField(getType(e), tp, translate(expression), uname);   
    return muGetAnno(translate(expression), getType(e), uname);
}

// -- is expression --------------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muPrim("is", abool(), [getType(expression)], [translate(expression), muCon(unescape("<name>"))], e@\loc);

MuExp translateBool(e:(Expression) `<Expression expression> is <Name name>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e),  trueCont, falseCont);
    
// -- has expression -----------------------------------------------

MuExp translate ((Expression) `<Expression expression> has <Name name>`) {
    uname = unescape("<name>");
    tp = getType(expression);
    if(isSyntaxType(tp)){
        return muHasField(translate(expression), tp, uname, {}); 
    } else if (isADTAType(tp)){
        commonKwFields = getCommonKeywordFieldsNameAndType()[tp] ? ();  
        if(commonKwFields[uname]?){
            return muCon(true); // If desired field is a common keyword field, all constructors have it
        }
        
        // Determine set of constructors with the desired field                                    
        constructors = getConstructorsMap()[tp] ? {};
        consesWithField = {c | c:acons(AType _adt, list[AType] fields, list[Keyword] kwFields) <- constructors,
                               (!isEmpty(fields) && any(f <- fields, f.alabel == uname)) ||
                               (!isEmpty(kwFields) && any(kwf <- kwFields, kwf.fieldType.alabel == uname))
                           };
        //if(isEmpty(consesWithField)){
        //    return muCon(false);    // It is statically known that there is no constructor with desired field
        //}

        // Compute result at runtime, guided by the set of constructors that do have the desired field
        return muHasField(translate(expression), tp, uname, consesWithField); 
        
     } else if(isNodeAType(tp)){
      // Always compute existence of field at run time
      return muHasField(translate(expression), tp, uname, {}); 
    } else { // all other cases: compute result statically
        return muCon(hasField(tp, uname));
    }		    
}

MuExp translateBool(e: (Expression) `<Expression expression> has <Name name>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    =  muIfExp(translate(e),  trueCont, falseCont);

// -- transitive closure expression ---------------------------------

MuExp translate(e:(Expression) `<Expression argument> +`) =
    unary("transitive_closure", e, argument);

// -- transitive reflexive closure expression -----------------------

MuExp translate(e:(Expression) `<Expression argument> *`) = 
    unary("transitive_reflexive_closure", e, argument);

// -- isDefined expression ------------------------------------------
    
// The isDefined and isDefinedElse expression are implemented using the following strategy:
// - try to avoid recomputation
// - try to avoid throwing exceptions

MuExp translate((Expression) `<Expression argument> ?`) {
	return translateIsDefined(argument);
}

MuExp translateBool((Expression) `<Expression argument> ?`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translateIsDefined(argument), trueCont, falseCont);
	
private MuExp translateIsDefined(Expression exp){
    tg = translateGuarded(exp);
    return muIsVarKwpDefined(_) := tg ? tg : muIsDefinedValue(tg);  
}

MuExp translateGuarded((Expression) `( <Expression exp1> )`)
    = translateGuarded(exp1);
 
MuExp translateGuarded(exp: (Expression) `<Expression exp1> [ <{Expression ","}+ subscripts> ]`)
    = translateSubscript(exp, true);

MuExp translateGuarded(exp: (Expression) `<Expression expression> \< <{Field ","}+ fields> \>`)
    = translateProject(exp, expression, [f | f <- fields], exp@\loc, true);

MuExp translateGuarded(exp: (Expression) `<Expression expression>@<Name name>`){
   tp = getType(expression);
   uname = unescape("<name>");
   //if(asubtype(tp, treeType) && uname == "loc"){ //TODO: remove when loc anno has been removed
   //     uname = "src";
   //     return  muGuardedGetField(getType(exp), getType(expression), translate(expression), uname);
   //}
   return muGuardedGetAnno(translate(expression), getType(exp), uname);
}

MuExp translateGuarded(exp: (Expression) `<Expression expression> . <Name field>`)
    = translateProject(exp, expression, [(Field)`<Name field>`], exp@\loc, true);
    //= muGuardedGetField(getType(exp), getType(expression), translate(expression),  unescape("<field>"));

MuExp translateGuarded(exp: (Expression) `<Name name>`)
    = muIsVarKwpDefined(mkVar("<name>", name@\loc));

    
// -- isDefinedOtherwise expression ---------------------------------

MuExp translate(e: (Expression) `<Expression lhs> ? <Expression rhs>`) {
      str fuid = topFunctionScope();
      guarded = muTmpGuardedIValue(nextLabel("guarded"), fuid);
      return muValueBlock(getType(e),
                          [ muVarInit(guarded, translateGuarded(lhs)),
                            muIfExp(muIsDefinedValue(guarded),  muGetDefinedValue(guarded, getType(lhs)), translate(rhs))
                          ]);
}

MuExp translateBool(e:(Expression) `<Expression lhs> ? <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = translateIfDefinedOtherwise(muBlock([translate(lhs), trueCont]), muBlock([translate(rhs), falseCont]), e@\loc);
    

public MuExp translateIfDefinedOtherwise(MuExp muLHS, MuExp muRHS, loc _src) {
    str fuid = topFunctionScope();
    guarded = muTmpGuardedIValue(nextLabel("guarded"), fuid);
    lhsType = avalue();
    if( muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName) := muLHS){
        muLHS = muGuardedGetField(resultType, baseType, baseExp, fieldName);
        lshType = resultType;
    } else if(muGetKwField(AType resultType, AType consType, MuExp exp, str fieldName, str moduleName) := muLHS){
        muLHS = muGuardedGetKwField(resultType, consType has adt ? consType.adt : consType, exp, fieldName, moduleName);
        lshType = resultType;
    } else if(muPrim("subscript", AType result, list[AType] details, list[MuExp] exps, loc src) := muLHS){
        muLHS = muPrim("guarded_subscript", result, details, exps, src);
        lhsType = result;
    } 
    else if(muGetAnno(MuExp exp, AType resultType, str annoName) := muLHS){
        muLHS = muGuardedGetAnno(exp, resultType, annoName);
        lhsType = resultType;
    }
    
    return muValueBlock(lhsType,
                        [ muVarInit(guarded, muLHS),
                          muIfExp(muIsDefinedValue(guarded),  muGetDefinedValue(guarded, lhsType), muRHS)
                        ]);
}

// -- not expression ------------------------------------------------

BTINFO getBTInfo(Expression e:(Expression) `!<Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <exp_btscope, btscopes1> = getBTInfo(exp, btscope, btscopes);
    return registerBTScope(e, exp_btscope, btscopes1);
}

Expression stripParens((Expression) `(<Expression exp>)`) = stripParens(exp);
default Expression stripParens(Expression exp) = exp;

MuExp translate(e:(Expression) `!<Expression exp>`) {
    exp = stripParens(exp);
    switch(exp){
        case (Expression) `! <Expression exp2>`:
            return translate(exp2);
        case (Expression) `<Expression _> in <Expression _>`:
            return infix("notin", exp);
        case (Expression) `<Expression _> notin <Expression _>`:
            return infix("in", exp);
        case (Expression) `<Expression _> == <Expression _>`:
            return infix("notequal", exp);
        case (Expression) `<Expression _> != <Expression _>`:
            return infix("equal", exp);
        case (Expression) `<Pattern pat> := <Expression exp2>`:
            return translateNoMatchOp(exp, pat, exp2,  getBTScopes(exp, nextTmp("MATCH")));
        case (Expression) `<Pattern pat> !:= <Expression exp2>`:
            return translateMatchOp(exp, pat, exp2,  getBTScopes(exp, nextTmp("NOMATCH")));  
        case (Expression) `<Pattern pat> \<- <Expression exp2>`:{
            my_btscopes = getBTScopes(exp, nextTmp("NOGEN"));
            return translateNoGenOp(exp, pat, exp2, my_btscopes);  
            }
        case (Expression) `any ( <{Expression ","}+ generators> )`:
            return translateQuantor([ g | g <- generators ], quantorAll = false, negateGenerators = true);
        case (Expression) `all ( <{Expression ","}+ generators> )`:
            return translateQuantor([ g | g <- generators ], quantorAll = true, negateGenerators = true);
        default:
            return muNot(translate(exp));    
    }
}
    
MuExp translateBool((Expression) `!<Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    exp = stripParens(exp);
    switch(exp){
        case (Expression) `! <Expression exp2>`:
            return muIfExp(translate(exp2), trueCont, falseCont);
        case (Expression) `<Expression _> in <Expression _>`:
            return muIfExp(infix("notin", exp), trueCont, falseCont);
        case (Expression) `<Expression _> notin <Expression _>`:
            return muIfExp(infix("in", exp), trueCont, falseCont);
        case (Expression) `<Expression _> == <Expression _>`:
            return muIfExp(infix("notequal", exp), trueCont, falseCont);
        case (Expression) `<Expression _> != <Expression _>`:
            return muIfExp(infix("equal", exp), trueCont, falseCont);
        case (Expression) `<Pattern pat> := <Expression exp2>`:
            return muIfExp(translateNoMatchOp(exp, pat, exp2,  btscopes), trueCont, falseCont);
        case (Expression) `<Pattern pat> !:= <Expression exp2>`:
            return muIfExp(translateMatchOp(exp, pat, exp2,  btscopes), trueCont, falseCont);  
        case (Expression) `<Pattern pat> \<- <Expression exp2>`:
            return translateNoGenOp(exp, pat, exp2, btscopes, trueCont, falseCont); 
        case (Expression) `any ( <{Expression ","}+ generators> )`:
            return muIfExp(translateQuantor([ g | g <- generators ], quantorAll = false, negateGenerators = true), trueCont, falseCont);
        case (Expression) `all ( <{Expression ","}+ generators> )`:
            return muIfExp(translateQuantor([ g | g <- generators ], quantorAll = true, negateGenerators = true), trueCont, falseCont);
        default:
            return muIfExp(translate(exp), falseCont, trueCont); 
    }
}

// -- negate expression ---------------------------------------------

MuExp translate(e:(Expression) `-<Expression argument>`) 
    = unary("negative", e, argument);

// -- splice expression ---------------------------------------------

MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice `<e>` cannot occur outside set or list at <e@\loc>";
}
   
// -- asType expression ---------------------------------------------

MuExp translate(e:(Expression) `[ <Type typ> ] <Expression argument>`)  {
    resultType = translateType(typ);  
    return muPrim("parse", resultType,
                                [ avalue(), astr(), aloc()],
                                [ muATypeCon(resultType, collectNeededDefs(resultType)),
   					              translate(argument),
   					              muCon(argument@\loc)
   					            ], 
   					            argument@\loc);
}

// -- composition expression ----------------------------------------

MuExp translate(e: (Expression) `<Expression lhs> o <Expression rhs>`) 
    = compose(e);

// -- product expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> * <Expression rhs>`) 
    = infix("product", e);

// -- join expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> join <Expression rhs>`)
    = infix("join", e);

// -- remainder expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> % <Expression rhs>`)
    = infix("remainder", e);

// -- division expression -------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> / <Expression rhs>`)
    = infix("divide", e);

// -- intersection expression ---------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> & <Expression rhs>`) 
    = infix("intersect", e);

// -- addition expression -------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> + <Expression rhs>`)
    = add(e);

// -- subtraction expression ----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> - <Expression rhs>`)
    = infix("subtract", e);

// -- insert before expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>\> <Expression rhs>`)
    = infix("add", e);

// -- append after expression ---------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`)
    = infix("add", e);

// -- modulo expression ---------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`)
    = infix("mod", e);

// -- notin expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)
    = infix("notin", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> notin <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) 
    = muIfExp(infix("notin", e),  trueCont, falseCont);

// -- in expression -------------------------------------------------
    
MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`) 
    = infix("in", e);

MuExp translateBool(e:(Expression) `<Expression lhs> in <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("in", e),  trueCont, falseCont);

// -- greater equal expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`)
    = infix("greaterequal", e);
 
 MuExp translateBool(e:(Expression) `<Expression lhs> \>= <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("greaterequal", e),  trueCont, falseCont);

// -- less equal expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`)
    = infix("lessequal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> \<= <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("lessequal", e), trueCont, falseCont);

// -- less expression ----------------------------------------------
    
MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)
    = infix("less", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \< <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("less", e),  trueCont, falseCont);

// -- greater expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)
    = infix("greater", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \> <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("greater", e),  trueCont, falseCont);

// -- equal expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)
    = infix("equal", e);
    //= muIfExp(infix("equal", e), muCon(true), muCon(false));

MuExp translateBool(e:(Expression) `<Expression lhs> == <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("equal", e), trueCont, falseCont);

// -- not equal expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)
    = infix("notequal", e);
   // = muIfExp(infix("notequal", e), muCon(true), muCon(false));

MuExp translateBool(e:(Expression) `<Expression lhs> != <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("notequal", e),  trueCont, falseCont);
 
// -- match expression --------------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <my_btscope, btscopes1> = getBTInfo(pat, btscope, btscopes);
    my_btscope.enter = btscope.enter;
    <btscope2, btscopes2> = getBTInfo(exp, my_btscope, btscopes1);
    return registerBTScope(e, my_btscope, btscopes2);
}

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPES btscopes)
    = translateMatchOp(e, pat, exp, btscopes);

MuExp translateMatchOp(Expression e, Pattern pat, Expression exp, BTSCOPES btscopes){
    my_btscope = btscopes[getLoc(e)];
    //if(backtrackFree(pat))
    //    return translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail));
    //else 
        return muExists(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter, comment="match operator"), muFail(my_btscope.\fail, comment="match operator")));
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return translateMatch(pat, exp, btscopes, trueCont, falseCont);
}
    
// -- no match expression -------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <my_btscope, btscopes1> = getBTInfo(pat, btscope, btscopes);
    my_btscope.enter = btscope.enter;
    <btscope2, btscopes2> = getBTInfo(exp, my_btscope, btscopes1);
    return registerBTScope(e, my_btscope, btscopes2);
}

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPES btscopes)
    = translateNoMatchOp(e, pat, exp, btscopes);
    
MuExp translateNoMatchOp(Expression e, Pattern pat, Expression exp,  BTSCOPES btscopes) { 
    my_btscope = btscopes[getLoc(e)];     
    return muNot(muExists(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail))));
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    my_btscope = btscopes[getLoc(e)];
    return muNot(translateMatch(pat, exp, btscopes, trueCont, falseCont));
    //return muNot(muExists(my_btscope.enter, translateMatch(pat, exp, btscopes, trueCont, falseCont)));
}
    
// -- generator expression ----------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <btscope_exp, btscopes> = getBTInfo(exp, btscope, btscopes);
    //<btscope_pat, btscopes> = getBTInfo(pat, "<btscope.enter>_GEN<l>", btscopes);
    <btscope_pat, btscopes> = getBTInfo(pat, "<btscope.enter>_GEN<getLoc(e).offset>", btscopes);
    return registerBTScope(e, btscope_pat, btscopes);
}
    
MuExp translateGenerator(Expression current, Pattern pat, Expression exp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    expType = getType(exp);
    elemType = getElementType(expType);
    if(isVoidAType(elemType)) return falseCont;
    
    str fuid = topFunctionScope();
    elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
    
    l = getLoc(current).offset;
    enterGen = getEnter(pat, "GEN<l>", btscopes);
    resumeGen = getResume(pat, "GEN<l>", btscopes);
    enterLoop(enterGen, fuid);
    code = muBlock([]);
    // -- enumerator with range expression
    if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
        code = muForRange(enterGen, elem, translate(first), muCon(0), translate(last), translatePat(pat, alub(getType(first), getType(last)), elem, btscopes, trueCont, muFail(resumeGen)/*falseCont*/), falseCont);
    } else
    // -- enumerator with range and step expression
    if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
       code = muForRange(enterGen, elem, translate(first), translate(second), translate(last), translatePat(pat, alub(alub(getType(first), getType(second)), getType(last)), elem, btscopes, trueCont, muFail(resumeGen)/*falseCont*/), falseCont);
    } else 
    // -- a syntactic list or optional
    if(isIterType(expType) || isOptType(expType)){
        delta = getIterOrOptDelta(expType); // take care of skipping layout and separators
        expVar = muTmpIValue(nextTmp("exp"), fuid, expType);
        lastVar = muTmpInt(nextTmp("last"), fuid);
        ivar = muTmpInt(nextTmp("i"), fuid);
        body = muBlock([ muConInit(elem, muIterSubscript(expVar, expType, ivar)),
                         translatePat(pat, elemType, elem, btscopes, trueCont, muFail(resumeGen))
                       ]);
        code = muBlock([ muConInit(expVar, translate(exp)),
                         muConInit(lastVar, muSubNativeInt(muSize(expVar, expType), muCon(1))),
                         muForRangeInt(enterGen, ivar, 0, delta, lastVar, body, falseCont)
                       ]);
    } else {
    // -- generic enumerator
        code = muForAll(enterGen, elem, getType(exp), translate(exp), translatePat(pat, elemType,  elem, btscopes, trueCont, muFail(resumeGen)), falseCont);
    }
    leaveLoop();
    return code;
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`){
    l = getLoc(e).offset;
    btscopes = getBTScopes(e, nextTmp("GEN<l>"));
    return translate(e, btscopes);
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPES btscopes){
    l = getLoc(e).offset;
    return translateGenerator(e, pat, exp, btscopes, muSucceed(getEnter(e, "GEN<l>", btscopes)),  muFail(getResume(e, "GEN<l>", btscopes)));
}

MuExp translateBool(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return  translateGenerator(e, pat, exp, btscopes, trueCont, falseCont);
}

MuExp translateNoGenOp(Expression e, Pattern pat, Expression exp, BTSCOPES btscopes){
    my_enter = getEnter(e, "NOGEN", btscopes);
    return muExists(my_enter, translateGenerator(e, pat, exp, btscopes, muSucceed(getEnter(e, "NOGEN", btscopes)),  muFail(getResume(e, "NOGEN", btscopes))));
}
MuExp translateNoGenOp(Expression e, Pattern pat, Expression exp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return translateGenerator(e, pat, exp, btscopes, falseCont, trueCont);
}
    
/*****************************************************************************/
/*                  Boolean Operators                                        */
/*****************************************************************************/

// ==== and expression ========================================================

//    Truth table
//       x  |   y   | x && y
//    ------|-------|--------
//    true  | true  | true
//    true  | false | false
//    false | true  | false
//    false | false | false
//
//                                      +-----------+
//            +------------------------>| falseCont |
//            |                         +-----------+
//            |
//        +---F--+----------------------------------+
//        |      |                                  |
//  ----->| lhs  R <---------+                      |
//        |      |           |                      |
//        |------+           |                      |
//        |   |           +--F--+----------------+  |
//        |   |           |     |                |  |
//        |   +---------->| rhs R <------+       |  |
//        |               |     |        |       |  |
//        |               +-----+        |       |  |
//        |               |  |           |       |  |
//        |               |  |     +-----F----+  |  |
//        |               |  +---->| trueCont |  |  |
//        |               |        +----------+  |  |
//        |               +----------------------+  |
//        |                                         |
//        +-----------------------------------------+                                      

// ---- getBTInfo

BTINFO getBTInfo(Expression e:(Expression) `<Expression lhs> && <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes){     
    <lhs_btscope, btscopes1> = getBTInfo(lhs, btscope, btscopes);
    <rhs_btscope, btscopes2> = getBTInfo(rhs, lhs_btscope, btscopes1);
    return registerBTScope(e, <btscope.enter, rhs_btscope.resume, lhs_btscope.\fail>, btscopes2);
}

BTSCOPES getBTScopesAnd(list[Expression] conds, str enter, BTSCOPES btscopes){
    BTSCOPE c_btscope = <enter, enter, enter>;
    norm_conds = normalizeAnd(conds);
    for(int i <- index(norm_conds)){
        c = norm_conds[i];
        <c_btscope, btscopes> = getBTInfo(c, c_btscope, btscopes);
    }
    return btscopes;
}

BTSCOPES getBTScopesParams(list[Pattern] args, str enter){
    btscopes = ();
    BTSCOPE c_btscope = <enter, enter, enter>;
    for(c <- args){
        <c_btscope, btscopes> = getBTInfo(c, c_btscope, btscopes);
    }
    return btscopes;
}

// ---- translate and 

MuExp translate(Expression e:(Expression) `<Expression lhs> && <Expression rhs>`){ 
    return translate(e, getBTScopes(e, nextTmp("AND")));
}

MuExp translate(Expression e:(Expression) `<Expression lhs> && <Expression rhs>`, BTSCOPES btscopes) {
    if(backtrackFree(e)){
        return translateBool(lhs, btscopes, translate(rhs), muCon(false));
    }
    enterLhs = getEnter(e, btscopes);
    failLhs = getFail(e, btscopes);
  
    code = muExists(enterLhs, translateAndConds(btscopes, [lhs, rhs], muSucceed(enterLhs), muFail(enterLhs)));// <<<<
    return code;    
}

MuExp translateBool(Expression e: (Expression) `<Expression lhs> && <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return translateAndConds(btscopes, [lhs, rhs], trueCont, falseCont);
}

list[Expression] normalizeAnd((Expression) `<Expression lhs> && <Expression rhs>`)
    = [*normalizeAnd(lhs), *normalizeAnd(rhs)];

default list[Expression] normalizeAnd(Expression e) = [e];

list[Expression] normalizeAnd(list[Expression] exps)
    = [ *normalizeAnd(e) | e <- exps ];

// ---- translateAndConds

MuExp identity(MuExp exp) = exp;

MuExp translateAndConds(BTSCOPES btscopes, list[Expression] conds, MuExp trueCont, MuExp falseCont, MuExp(MuExp) normalize = identity){
    if(isEmpty(conds)) return normalize(trueCont);
    conds = normalizeAnd(conds);
  
    falseCont = normalize(falseCont);
    
    if(all(cond <- conds, backtrackFree(cond))){
        for(cond <- reverse(conds)){
            trueCont = translateBool(cond, btscopes, trueCont, falseCont);
        }
        return normalize(trueCont);
    }
    for(i <- reverse(index(conds))){
        cont = i == 0 ? falseCont : muFail(getResume(conds[i-1], btscopes), comment="translateAndConds, i=0");   // <<<
        trueCont = normalize(translateBool(conds[i], btscopes, trueCont, cont));
    }
    
    enter = getEnter(conds[0], btscopes);
    res = muExists(getEnter(conds[0], btscopes), trueCont);
    //iprintln(res);
    return res;
}

// ==== or expression =========================================================

//    Truth table
//       x  |   y   | x || y
//    ------|-------|--------
//    true  | true  | true
//    true  | false | true
//    false | true  | true
//    false | false | false
//
//                                                                   +-----------+ 
//           +-------------------------------+        +------------->| falseCont |                              
//           |                               |        |              +-----------+
//           |                               |        |
//        +--F--+-----------------------+    |     +--F--+-----------------------+
//        |     |                       |    |     |     |                       |
//  ----->| lhs R <------------+        |    +---->| rhs R<------------+         |
//        |     |              |        |          |     |             |         |
//        |-----+              |        |          |-----+             |         |
//        |  |           +-----F-----+  |          |  |           +-----F-----+  |
//        |  |           |           |  |          |  |           |           |  |
//        |  +---------->| trueCont1 |  |          |  +---------->| trueCont2 |  |
//        |              |           |  |          |              |           |  |
//        |              +-----------+  |          |              +-----------+  |
//        +-----------------------------+          +-----------------------------+
//                     

// ---- getBTInfo

BTINFO getBTInfo(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes){
    conds = normalizeOr(e);
    BTSCOPE c_btscope = btscope;
    
    for(c <- conds){
        <c_btscope, btscopes> = getBTInfo(c, btscope, btscopes);
    }
    res = registerBTScope(e, <btscope.enter, c_btscope.resume, btscope.\fail>, btscopes);
    return res;    
}

// ---- translate or expression

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`){
    btscopes = getBTScopes(e, nextTmp("OR"));
    //println("OR:"); iprintln(btscopes);
    return translate(e, btscopes);
}

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`, BTSCOPES btscopes) {
   if(backtrackFree(lhs)){
        return muIfExp(translate(lhs), muCon(true), translate(rhs));
   }
   code = muExists(getEnter(e, btscopes), translateOrConds(btscopes, [lhs, rhs], muSucceed(getResume(lhs, btscopes)), muFail(getFail(rhs, btscopes))));                                                                 
   return code;                                                                               
}
    
MuExp translateBool((Expression) `<Expression lhs> || <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
   if(backtrackFree(lhs) && backtrackFree(rhs)){
        return muIfExp(translate(lhs), trueCont, muIfExp(translate(rhs), trueCont, falseCont));
   }
    return translateOrConds(btscopes, [lhs, rhs], trueCont, falseCont);
}  

// ---- translateOrConds

list[Expression] normalizeOr((Expression) `<Expression lhs> || <Expression rhs>`)
    = [*normalizeOr(lhs), *normalizeOr(rhs)];

default list[Expression] normalizeOr(Expression e) = [e];

list[Expression] normalizeOr(list[Expression] exps)
    = [ *normalizeOr(e) | e <- exps ];
    
MuExp translateOrConds(BTSCOPES btscopes, list[Expression] conds, MuExp trueCont, MuExp falseCont, MuExp(MuExp) normalize = identity){
    iprintln(btscopes);
    if(isEmpty(conds)) return normalize(trueCont);
    conds = normalizeOr(conds);
    
    falseCont = normalize(falseCont);
    
    nconds = size(conds);
    
    result = falseCont;
    failTrueCont = getFailScope(trueCont);
    for(i <- reverse(index(conds))){
        cond = conds[i];
        if(backtrackFree(cond)){
            if(i == nconds-1){
                 code = muIfExp(translate(cond), trueCont, result);
                 result = code;
            } else {
                trueCont1 = visit(trueCont) { case muFail(failTrueCont) => muBlock([]) };
                code =  muIfExp(translate(cond), trueCont1, result);
                result = code;
            }
        } else {
            falseCont1 = result; // i < nconds - 1 ? result : muFail(getEnter(conds[i+1], btscopes), comment="translateOrConds, i==nconds-1");   // <<<
            trueCont1 = visit(trueCont) { case muFail(failTrueCont) => muBlock([]) };
            trueCont1  = redirect(trueCont, getFailScope(trueCont), getResume(conds[i], btscopes));
            
            code = normalize(translateBool(conds[i], btscopes, trueCont1, falseCont1));
            result = code;
        }
    }
    return result;
}

str getFailScope(MuExp exp){
    seen_scopes = {};
    top-down visit(exp){
           case muExists(str label, MuExp body): { seen_scopes += label; }
           case muFail(label): if(label notin seen_scopes) return label;
    };
    return "";
}

MuExp redirect(MuExp exp, from, to){
    return visit(exp){
           case mf: muFail(from) => muFail(to, comment="redirected <from> to <to>; <mf.comment>")
           case ms: muSucceed(from) => muSucceed(to, comment="redirected <from> to <to>; <ms.comment>")
           case mc: muContinue(from) =>  muContinue(to)
    };
}
  
// ==== implies expression ====================================================

//    Truth table
//       x  |    y  | x ==> y
//    ------|-------|--------
//    true  | true  | true
//    true  | false | false
//    false | true  | true
//    false | false | true
//
//               +-----------+
//            +->| trueCont1 |
//            |  +----F------+
//            |       |
//        +---F--+----+-----------------------------+
//        |      |    |            +-----------+    |
//  ----->| lhs  R<---+      +---->| falseCont |    |
//        |      |           |     +-----------+    |
//        |------+           |                      |
//        |   |           +--F--+----------------+  |
//        |   |           |     |                |  |
//        |   +---------->| rhs R <------+       |  |
//        |               |     |        |       |  |
//        |               +-----+        |       |  |
//        |               |  |           |       |  |
//        |               |  |     +-----F----+  |  |
//        |               |  +---->| trueCont2|  |  |
//        |               |        +----------+  |  |
//        |               +----------------------+  |
//        |                                         |
//        +-----------------------------------------+     

// ---- getBTInfo

BTINFO getBTInfo(Expression e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes){
    <lhs_btscope, btscopes1> = getBTInfo(lhs, btscope, btscopes);
    <rhs_btscope, btscopes2> = getBTInfo(rhs, lhs_btscope, btscopes1);
    res =  registerBTScope(e, <btscope.enter, rhs_btscope.resume, lhs_btscope.\fail>, btscopes2);
    //iprintln(btscopes2);
    return res;
}

// ---- translate implies expression

MuExp translate(Expression e:(Expression) `<Expression lhs> ==\> <Expression rhs>`){ 
    return translate(e, getBTScopes(e, nextTmp("IMPLIES")));
}

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPES btscopes) {
    if(backtrackFree(lhs)){
        return translateBool(lhs, btscopes, translate(rhs), muCon(true));
    }
    trueCont1 = muBlock([muComment("trueCont1"), muSucceed(getEnter(lhs, btscopes))]);
    trueCont2 = muBlock([muComment("trueCont2"), muFail(getResume(rhs, btscopes))]);
    enterLhs = getEnter(lhs, "IMPLIES", btscopes);
    enterRhs = getEnter(rhs, "IMPLIES", btscopes);
    
    code = translateBool(lhs, btscopes, translateBool(rhs, btscopes, muBlock([muComment("falseCont rhs"), muFail(getResume(rhs, btscopes))]), trueCont1),
                                                           muBlock([muComment("falseCont lhs"), muFail(getResume(rhs, btscopes))])); 
 
 
    //code = muExists(enterLhs, translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(enterLhs), muFail(getResume(e, "IMPLIES", btscopes))),
    //                                                                      muSucceed(enterLhs))); 
    
    
    return code;  
}
    
MuExp translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    trueCont2 = redirect(trueCont, getResume(lhs, btscopes), getResume(rhs, btscopes));
    falseCont2 = redirect(falseCont, getResume(rhs, btscopes), getResume(e, btscopes));
    return muExists(getEnter(e, btscopes), translateBool(lhs, btscopes, translateBool(rhs, btscopes, trueCont2, falseCont2), trueCont));
}

// ==== equivalent expression =================================================

//    Truth table
//       x  |    y  | x <==> y
//    ------|-------|--------
//    true  | true  | true
//    true  | false | false
//    false | true  | false
//    false | false | true
//
//                                                                        +----------+
//           +-------------------------------------------+        +------>| trueCont2|
//           |                                           |        |       +----------+
//           |                                           |        |
//        +--F--+-----------------------------------+    |     +--F--+-------------------+
//        |      |                                  |    |     |     |                   |
//  ----->| lhs  R <---------+                      |    +---->|rhs2 R                   |
//        |      |           |                      |          |     |                   |
//        |------+           |                      |          +-----+                   |
//        |   |           +--F--+----------------+  |          |  |                      |
//        |   |           |     |                |  |          |  |       + ----------+  |
//        |   +---------->|rhs1 R <------+       |  |          |  +------>| falseCont |  |
//        |               |     |        |       |  |          |          +-----------+  |
//        |               +-----+        |       |  |          +-------------------------+
//        |               |  |           |       |  |
//        |               |  |     +-----F----+  |  |
//        |               |  +---->| trueCont1|  |  |
//        |               |        +----------+  |  |
//        |               +----------------------+  |
//        |                                         |
//        +-----------------------------------------+     

BTINFO getBTInfo(Expression e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes){
    <lhs_btscope, btscopes1> = getBTInfo(lhs, btscope, btscopes);
    <rhs_btscope, btscopes2> = getBTInfo(rhs, lhs_btscope, btscopes1);
    return registerBTScope(e, <btscope.enter, rhs_btscope.resume, lhs_btscope.\fail>, btscopes2);
}

MuExp translate(Expression e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`){ 
    return translate(e, getBTScopes(e, nextTmp("EQUIV")));
}
   
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`, BTSCOPES btscopes) {
    if(backtrackFree(e)){
        str fuid = topFunctionScope();
        rhs_val = muTmpIValue(nextTmp("rhs_val"), fuid, abool());
        return muValueBlock(abool(), [ muConInit(rhs_val, translate(rhs)),
                                       muIfExp(translate(lhs), 
                                               rhs_val,
                                               muPrim("not", abool(), [abool()], [rhs_val], e@\loc))
                                     ]);
   }  
   
   enterLhs = getEnter(lhs, "EQUIV", btscopes);
   code = muExists(enterLhs, translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getResume(rhs, btscopes)), muFail(getFail(rhs, btscopes))),
                                                          translateBool(rhs, btscopes, muFail(enterLhs), muSucceed(enterLhs)))); 
                                                         
   return code;
}

MuExp translateBool((Expression) `<Expression lhs> \<==\> <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) { 
    return translateBool(lhs, btscopes, translateBool(rhs, btscopes, trueCont, falseCont), translateBool(rhs, btscopes, trueCont, falseCont));  
}
 
// -- conditional expression ----------------------------------------

BTINFO getBTInfo(Expression e:(Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <btscope1, btscopes1> = getBTInfo(condition, btscope, btscopes);
    return registerBTScope(e, btscope1, btscopes1);
}

MuExp translate(e: (Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) {
	btscopes = getBTScopes(condition, nextTmp("COND"));
	res = translateBool(condition, btscopes,  translate(thenExp), translate(elseExp));
	return res;
}

bool isConditional((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = true;
bool isConditional((Expression) `( <Expression exp> )`) = isConditional(exp);
default bool isConditional(Expression e) = false;

MuExp translateBool((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return translateBool(condition, btscopes, translateBool(thenExp, btscopes, trueCont, translateBool(elseExp, btscopes, trueCont, falseCont)), 
                                              translateBool(elseExp, btscopes, trueCont, falseCont));
}

// -- concrete syntax

MuExp translate((Expression) `<Concrete con>`) { 
    return translateConcreteExpression(con.args[0]);
}

// -- any other expression that may require backtracking ------------

default MuExp translate(Expression e) {
	btscopes = getBTScopes(e, nextTmp("EXP"));
	return backtrackFree(e) ? translate(e, btscopes)
	                        : muExists(getEnter(e, btscopes), translateBool(e, btscopes, muSucceed(getEnter(e,btscopes)), muFail(getFail(e, btscopes))));

    //return translateBool(e, btscopes, muSucceed(getFail(e, btscopes)), muFail(getResume(e, btscopes)));
    //return muExists(getEnter(e, btscopes), translateBool(e, btscopes, muSucceed(getEnter(e,btscopes)), muFail(getResume(e, btscopes))));
}

default MuExp translateBool(Expression e, BTSCOPES _btscopes, MuExp _trueCont, MuExp _falseCont) {
    println(e);
    throw "TRANSLATEBOOL, MISSING CASE FOR EXPRESSION: <e>";
}

/*********************************************************************/
/*                  End of Expessions                                */
/*********************************************************************/