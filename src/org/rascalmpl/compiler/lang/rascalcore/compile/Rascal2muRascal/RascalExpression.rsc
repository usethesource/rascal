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

import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
//import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;
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
    return muCallPrim3(op, getType(e), [getType(e.lhs), getType(e.rhs)], [translate(e.lhs), translate(e.rhs)], e@\loc);
}    
private MuExp unary(str op, Expression e, Expression arg) = 
    muCallPrim3(op, getType(e), [getType(arg)], [translate(arg)], e@\loc);

// ----------- compose: exp o exp ----------------

private MuExp compose(Expression e){
  lhsType = getType(e.lhs);
  return isFunctionType(lhsType) || isOverloadedAType(lhsType) ? translateComposeFunction(e) : infix("compose", e);
}

str getFunctionName(af:afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = af.label;
str getFunctionName(ovl: overloadedAType(rel[loc, IdRole, AType] overloads)) = getFirstFrom(overloads)[2].label;
str getFunctionName(AType t) {
    throw "getFunctionName: <t>";
}

private MuExp translateComposeFunction(Expression e){
  lhsType = getType(e.lhs);
  rhsType = getType(e.rhs);
  resType = getType(e);
  
  return muComposedFun(translate(e.lhs), translate(e.rhs), lhsType, rhsType, resType);
}

//private MuExp translateComposeFunction(Expression e){
//  lhsType = getType(e.lhs);
//  rhsType = getType(e.rhs);
//  resType = getType(e);
//  
//  MuExp lhsReceiver = visit(translate(e.lhs)) { case str s => replaceAll(s, "::" , "_") };
//  MuExp rhsReceiver = visit(translate(e.rhs)) { case str s => replaceAll(s, "::" , "_") };
//  
//  //println("rhsReceiver: <rhsReceiver>");
//  str compResolverName = "$<getFunctionName(lhsType)>_COMP_<getFunctionName(rhsType)>_<e@\loc.begin.line>A<e@\loc.offset>L<e@\loc.length>"; // name of compositiosition
//    
//  // Generate and add a function COMPOSE_<...>
//  str scopeId = topFunctionScope();
//  str comp_name = "$COMPOSE_<e@\loc.begin.line>A<e@\loc.offset>L<e@\loc.length>";
//  str comp_fuid = scopeId + "_" + comp_name;
//  
//  AType comp_ftype = avoid(); 
//  int nargs = 0;
//  if(isFunctionType(resType)) {
//      nargs = size(resType.formals);
//      comp_ftype = resType;
//  } else if(overloadedAType(rel[loc, IdRole, AType] overloads) := resType){
//      comp_ftype = getFirstFrom(overloads)[2];
//      nargs = getArity(comp_ftype);
//      for(<l, role, tp> <- overloads){
//        if(getArity(tp) != nargs){
//            throw "cannot handle composition/overloading for different arities";
//        }
//        comp_ftype = alub(comp_ftype, tp);
//     }
//  } else {
//    throw "cannot handle result type <resType>";
//  }
//  
//  if(hasOverloadingResolver(compResolverName)){
//    return muOFun(compResolverName, comp_ftype);
//  }
//    
//  enterFunctionScope(comp_fuid);
//  //TODO kwargs
//  formals = [muVar("$<j>" /*resType.formals[j].label*/, comp_fuid, j, comp_ftype.formals[j]) | int j <- [0 .. nargs]];
//  rhsCall = muOCall3(rhsReceiver, rhsType, formals, [], e.rhs@\loc);
//  
//  body_exps =  [muReturn1(comp_ftype.ret, muOCall3(lhsReceiver, lhsType, [rhsCall], [], e.lhs@\loc))];
//  //body_exps =  [muReturn1(lhsType.ret, muOCall3(lhsReceiver, atuple(atypeList([lhsType])), [rhsCall], [], e.lhs@\loc))];
//  
//  
//  //kwargs = muCallMuPrim("copy_and_update_keyword_mmap", [muCon(nargs)]);
//  //rhsCall = muOCall4(rhsReceiver, atuple(atypeList([rhsType])), [muVar("parameter_<comp_name>", comp_fuid, j) | int j <- [0 .. nargs]] + [ kwargs ], e.rhs@\loc);
//  //body_exps =  [muReturn1(muOCall4(lhsReceiver, atuple(atypeList([lhsType])), [rhsCall, kwargs ], e.lhs@\loc))];
//   
//  leaveFunctionScope();
//  body_code = muBlock(body_exps);
//  fun = muFunction(compResolverName, compResolverName, comp_ftype, formals, [], scopeId, false, false, false, getExternalRefs(body_code, comp_fuid), {}, {}, e@\loc, [], (), body_code);
//  //iprintln(e@\loc);
//  loc uid = declareGeneratedFunction(comp_name, comp_fuid, comp_ftype, e@\loc);
//  addFunctionToModule(fun);  
//  addDefineAndType(<currentFunctionDeclaration(), comp_name, functionId(), e@\loc, defType(comp_ftype)>, comp_ftype);
//  addOverloadedFunctionAndResolver(compResolverName, <compResolverName, comp_ftype, compResolverName/*getModuleName()*/, [e@\loc], []>);
// 
//  return muOFun(compResolverName, comp_ftype);
//}

// ----------- addition: exp + exp ----------------

private  MuExp add(Expression e){
    lhsType = getType(e.lhs);
    return isFunctionType(lhsType) || isOverloadedAType(lhsType) ? translateAddFunction(e) :infix("add", e);
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
  lactuals = [muVar("$<j>", add_fuid, j, lhsFormals[j]) | int j <- [0 .. nargs]];
  lhsCall = muOCall3(lhsReceiver, lhsType, lactuals, [], e.lhs@\loc);
  
  rhsFormals = getFormals(rhsType);
  nargs = size(rhsFormals);
  ractuals = [muVar("$<j>" , add_fuid, j, rhsFormals[j]) | int j <- [0 .. nargs]];
  rhsCall = muOCall3(rhsReceiver, rhsType, ractuals, [], e.rhs@\loc);
  result = muTmpIValue(nextTmp("$result"), add_fuid, resType);
  
  body = muBlock([
            muConInit(result, lhsCall),
            muIf(muIsInitialized(result), muReturn1(resType, result)),
            muReturn1(resType, rhsCall)
            ]);
 
  leaveFunctionScope();
  funType = afunc(resType, lhsFormals, []);
  fun = muFunction(add_name, add_name, funType, lactuals, [], scopeId, false, false, false, getExternalRefs(body, add_fuid), {}, {}, e@\loc, [], (), body);
  //iprintln(fun);
  loc uid = declareGeneratedFunction(add_name, add_fuid, funType, e@\loc);
  addFunctionToModule(fun);  
  addDefineAndType(<currentFunctionDeclaration(), add_name, functionId(), e@\loc, defType(funType)>, funType);
 
  return muOFun([uid], funType);
}

//private MuExp translateAddFunction(Expression e){
//  lhsType = getType(e.lhs);
//  rhsType = getType(e.rhs);
//  resType = getType(e);
//
//  MuExp lhsReceiver = translate(e.lhs);
//  
//  lhsOFunctions = lhsOConstructors = [];
//  if(muFun(loc src, AType tp) := lhsReceiver){
//    lhsOFunctions = [src];
//  } else if(muOFun(str resolverName, AType tp) := lhsReceiver){
//    lhsOf = getOverloadedFunction(resolverName);
//    lhsOFunctions = lhsOf.ofunctions;
//    lhsOConstructors = lhsOf.oconstructors;
//  }
// 
//  MuExp rhsReceiver = translate(e.rhs);
//  
//  rhsOFunctions = rhsOConstructors = [];
//  if(muFun(loc src, AType tp) := rhsReceiver){
//    rhsOFunctions = [src];
//  } else if(muOFun(str resolverName, AType tp) := rhsReceiver){
//    rhsOf = getOverloadedFunction(resolverName);
//    rhsOFunctions = rhsOf.ofunctions;
//    rhsOConstructors = rhsOf.oconstructors;
//  }
// 
//  str addResolverName = "$<getFunctionName(lhsType)>_ADD_<getFunctionName(rhsType)>_<e@\loc.begin.line>A<e@\loc.offset>L<e@\loc.length>";  // name of addition
// 
//  OFUN addOFun = <addResolverName, lhsType, addResolverName, lhsOFunctions + rhsOFunctions, lhsOConstructors + rhsOConstructors>; // add all alternatives
// 
//  addOverloadedFunctionAndResolver(addResolverName, addOFun); 
//  return muOFun(addResolverName, resType);
//}


MuExp comparison(str op, Expression e)
    = infix(op, e);

/*********************************************************************/
/*                  Translate Literals                               */
/*********************************************************************/


// -- boolean literal  -----------------------------------------------

MuExp translate((Literal) `<BooleanLiteral b>`, BTSCOPES btscopes) = 
    "<b>" == "true" ? muCon(true) : muCon(false);
    
MuExp translateBool((Literal) `<BooleanLiteral b>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = 
    "<b>" == "true" ? trueCont : falseCont;

// -- integer literal  -----------------------------------------------
 
MuExp translate((Literal) `<IntegerLiteral n>`) = 
    muCon(toInt("<n>"));

// -- regular expression literal  ------------------------------------

MuExp translate((Literal) `<RegExpLiteral r>`) { 
    throw "RexExpLiteral cannot occur in expression"; 
}

// -- string literal  ------------------------------------------------

MuExp translate((Literal) `<StringLiteral n>`) {
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
                           *translateTail(template, preIndent, tail),
                           muTemplateClose(template)
                         ]);
}
    
private MuExp translateStringLiteral(s: (StringLiteral) `<PreStringChars pre> <Expression expression> <StringTail tail>`) {
    preIndent = computeIndent(pre);
    str fuid = topFunctionScope();
    template = muTmpTemplate(nextTmp("template"), fuid);
    return muValueBlock( astr(),
                         [ muConInit(template, muTemplate(translatePreChars(pre))),
    				       *translateExpInStringLiteral(template, preIndent, expression),
    				       *translateTail(template, preIndent, tail),
    				       muTemplateClose(template)
					     ]);
}
                    
private MuExp translateStringLiteral(s: (StringLiteral)`<StringConstant constant>`) {
    return muCon(/*deescape(*/readTextValueString(removeMargins("<constant>"))/*)*/);
}

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
   			 translateTemplate(template, indent + midIndent, stemplate),
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
                        translateTemplate(template, indent + midIndent, stemplate),
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
     muCallPrim3("create_loc", aloc(), [astr()], [muCallPrim3("add", astr(), [astr(), astr()], [translateProtocolPart(protocolPart), translatePathPart(pathPart)], l@\loc)], l@\loc);
 
private MuExp translateProtocolPart((ProtocolPart) `<ProtocolChars protocolChars>`) = muCon("<protocolChars>"[1..]);
 
private MuExp translateProtocolPart(p: (ProtocolPart) `<PreProtocolChars pre> <Expression expression> <ProtocolTail tail>`) =
    muCallPrim3("add", astr(), [astr(), astr(), astr()], [muCon("<pre>"[1..-1]), translate(expression), translateProtocolTail(tail)], p@\loc);
 
private MuExp  translateProtocolTail(p: (ProtocolTail) `<MidProtocolChars mid> <Expression expression> <ProtocolTail tail>`) =
   muCallPrim3("add", astr(), [astr(), astr(), astr()], [muCon("<mid>"[1..-1]), translate(expression), translateProtocolTail(tail)], p@\loc);
   
private MuExp translateProtocolTail((ProtocolTail) `<PostProtocolChars post>`) = muCon("<post>"[1 ..]);

private MuExp translatePathPart((PathPart) `<PathChars pathChars>`) = muCon("<pathChars>"[..-1]);

private MuExp translatePathPart(p: (PathPart) `<PrePathChars pre> <Expression expression> <PathTail tail>`) =
   muCallPrim3("add", astr(), [astr(), astr(), astr()], [ muCon("<pre>"[..-1]), translate(expression), translatePathTail(tail)], p@\loc);

private MuExp translatePathTail(p: (PathTail) `<MidPathChars mid> <Expression expression> <PathTail tail>`) =
   muCallPrim3("add", astr(), [astr(), astr(), astr()], [ muCon("<mid>"[1..-1]), translate(expression), translatePathTail(tail)], p@\loc);
   
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
    
MuExp translateBool(e:(Expression)  `<Literal s>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = 
    translateBool(s, btscopes, trueCont, falseCont);


// -- concrete syntax expression  ------------------------------------

// this detects that the concrete string has been parsed correctly and we can 
// switch to compiling the tree values to muRascal expressions:
MuExp translate(e:appl(prod(Symbol::label("parsed",Symbol::lex("Concrete")), [_],_),[Tree concrete]))
  = translateConcrete(concrete);
  
// this detects that a parse error has occurred earlier and we generate an exception here:  
MuExp translate(e:appl(prod(label("typed",lex("Concrete")), [_],_),_))
  = muValueBlock([muThrow(muCon("(compile-time) parse error in concrete syntax", e@\loc))]);   

// these three constant parts of trees are directly mapped to constants:
private MuExp translateConcrete(t:appl(prod(lit(_),_, _), _)) = muCon(t);
private MuExp translateConcrete(t:appl(prod(cilit(_),_, _), _)) = muCon(t);
private MuExp translateConcrete(t:appl(prod(layouts(_),_, _), _)) = muCon(t);
  
// this is a pattern variable, which we substitute with a reference to a muVariable:  
private MuExp translateConcrete(t:appl(prod(Symbol::label("$MetaHole", Symbol _),[_], _), [ConcreteHole hole])) {
    <fuid, pos> = getVariableScope("<hole.name>", getConcreteHoleVarLoc(t));
    return muVar("<hole.name>", fuid, pos, getType(hole.symbol@\loc));    
}

// Four cases of lists are detected to be able to implement splicing
// splicing is different for separated lists from normal lists
private MuExp translateConcrete(t:appl(p:regular(s:iter(Symbol elem)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteList(elem, args, t@\loc), t@\loc);

private MuExp translateConcrete(t:appl(p:regular(s:\iter-star(Symbol elem)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteList(elem, args, t@\loc), t@\loc); 
   
private MuExp translateConcrete(t:appl(p:regular(s:\iter-seps(Symbol elem, list[Symbol] seps)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteSeparatedList(elem, seps, args, t@\loc), t@\loc);

private MuExp translateConcrete(t:appl(p:regular(s:\iter-star-seps(Symbol elem, list[Symbol] seps)), list[Tree] args))
  = muTreeAppl(muCon(p), translateConcreteSeparatedList(elem, seps, args, t@\loc), t@\loc); 

private MuExp translateConcrete(char(int i)) =  muTreeChar(i);

// this is a normal parse tree node:
private default MuExp translateConcrete(t:appl(Production p, list[Tree] args)) 
  = muTreeAppl(muCon(p), [translateConcrete(a) | a <- args], (t@\loc?) ? t@\loc : |unknown:///|);


bool isListPlusVar(Symbol elem, appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter(elem)))}), [_])) = true;
bool isListPlusVar(Symbol elem, appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter-seps(elem,_)))}), [_])) = true;
default bool isListPlusVar(Symbol _, Tree _) = false;

bool isListStarVar(Symbol elem, t:appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter-star(elem)))}), [_])) = true;
bool isListStarVar(Symbol elem, t:appl(prod(label("$MetaHole", _),[sort("ConcreteHole")], {\tag("holeType"(Symbol::\iter-star-seps(elem,_)))}), [_])) = true;
default bool isListStarVar(Symbol _,Tree _) = false;

bool isListVar(Symbol elem, Tree x) = isListPlusVar(elem, x) || isListStarVar(elem, x);

private AType aTree = aadt("Tree", [], dataSyntax());

private MuExp translateConcreteList(Symbol eltType, []) = muCon([]);

private MuExp translateConcreteList(Symbol eltType, list[Tree] elems:![], loc src) {
    str fuid = topFunctionScope();
       
    writer = muTmpListWriter(nextTmp("writer"), fuid);   
    
    enterWriter(writer.name);
    
    code = for (Tree elem <- elems) {
       if (isListVar(eltType, elem)) {
          append muCallPrim3("splice_list", avoid(), [avalue(), aTree], [writer, muTreeGetArgs(translateConcrete(elem))], src);
       }
       else {
          append muCallPrim3("add_list_writer", avoid(), [avalue(), aTree], [writer, translateConcrete(elem)], src);
       }
    }
   
    code = [muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], src)), *code];
    leaveWriter();
   
    return muValueBlock(\alist(aTree), [*code, muCallPrim3("close_list_writer", alist(aTree), [avalue()], [writer], src)]);
}

private MuExp translateConcreteSeparatedList(Symbol _, list[Symbol] _, [], loc _) = muCon([]);

private MuExp translateConcreteSeparatedList(Symbol eltType, list[Symbol] _, [Tree single], loc src) {
    str fuid = topFunctionScope();
       
    writer = muTmpListWriter(nextTmp("writer"), fuid);   
    
    enterWriter(writer.name);
    
    code = [muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], src))];
       
    if (isListVar(eltType, single)) {
       varExp = muTreeGetArgs(translateConcrete(single));
       code += [muCallPrim3("splice_list", avoid(), [avalue(), aTree], [writer, varExp], src)];
    }
    else {
       code += [muCallPrim3("add_list_writer", avoid(), [avalue(), aTree], [writer, translateConcrete(single/*elem*/)], src)];
    }
    leaveWriter();
        
    return muValueBlock(\alist(aTree), [*code, muCallPrim3("close_list_writer", alist(aTree), [avalue()], [writer], src)]);    
}

private MuExp translateConcreteSeparatedList(Symbol eltType, list[Symbol] sepTypes, list[Tree] elems:[_,_,*_], loc src) {
    str fuid = topFunctionScope();
       
    writer = muTmpListWriter(nextTmp("writer"), fuid);   
    
    enterWriter(writer.name);
    
    code = [muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], src))];
    
    // first we compile all element except the final separators and the final element:
    while ([Tree first, *Tree sepTrees, Tree second, *Tree more] := elems && size(sepTypes) == size(sepTrees)) {
      varExp = translateConcrete(first);

      // first we splice or add the first element:
      if (isListVar(eltType, first)) {
        code += [muCallPrim3("splice_list", avoid(), [avalue(), aTree], [writer, muTreeGetArgs(varExp)], first@\loc?|unknown:///|)];
      }
      else {
        code += [muCallPrim3("add_list_writer", avoid(), [avalue(), aTree], [writer, varExp], first@\loc?|unknown:///|)];
      }
      
      sepCode    = [muCallPrim3("add_list_writer", avoid(), [avalue(), aTree], [writer, muCon(e)], e@\loc?|unknown:///|) | e <- sepTrees];
      secondVarExp = translateConcrete(second);
       
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
          code += [muCallPrim3("splice_list", avoid(), [avalue(), aTree], [writer, muTreeGetArgs(secondVarExp)], second@\loc?|unknown:///|)];
        }
        else {
          code += [muCallPrim3("add_list_writer", avoid(), [avalue(), aTree], [writer, secondVarExp], second@\loc?|unknown:///|)];
        }
      }
      elems = [second, *more];
    }
     
   
    leaveWriter();
   
    return muValueBlock(\alist(aTree), [*code, muCallPrim3("close_list_writer", alist(aTree), [avalue()], [writer], src)]);   
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

MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) =
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
    
    // TODO: we plan to introduce keyword patterns as formal parameters
    <formalVars, funBody> = translateFunction(fuid, parameters.formals.formals, ftype, muBlock([translate(stat, ()) | stat <- cbody]), false, []);
    
    addFunctionToModule(muFunction("$CLOSURE_<uid.begin.line>A<uid.offset>", 
                                   "$CLOSURE_<uid.begin.line>A<uid.offset>", 
                                   ftype, 
                                   formalVars,
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
                 [ muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], e@\loc)),
                   muForRange("", elem, translate(first), muCon(0), translate(last), muCallPrim3("add_list_writer", avoid(), [resultType, elemType], [writer, elem], e@\loc)),
                   muCallPrim3("close_list_writer", resultType, [avalue()], [writer], e@\loc)
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
                 [ muConInit(writer, muCallPrim3("open_list_writer", alist(elemType), [], [], e@\loc)),
                   muForRange("", elem, translate(first), translate(second), translate(last), muCallPrim3("add_list_writer", avoid(), [elemType], [writer, elem], e@\loc)),
                   muCallPrim3("close_list_writer", avoid(), [avalue()], [writer], e@\loc)
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
	                         && isConcreteType(subjectType); // || subjectType == adt("Tree",[]));
	
	reachable_syms = { avalue() };
	reachable_prods = {};
	if(optimizing()){
	   tc = getTypesAndConstructorsInVisit(cases);
	   <reachable_syms, reachable_prods> = getReachableTypes(subjectType, tc.constructors, tc.types, useConcreteFingerprint);
	   println("reachableTypesInVisit: <reachable_syms>, <reachable_prods>");
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

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(e);

private MuExp translateReducer(Expression e){
    redName = nextTmp("REDUCER");
    str fuid = topFunctionScope();
    
    //btfree = all(Expression c <- e.generators, backtrackFree(c));
    //enterBacktrackingScope(redName);
    
    conds = [g | g <- e.generators];
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
//TODO
MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) {
    return muCallPrim3("create_reifiedType", avalue(), [avalue(), avalue()], [translate(symbol), translate(definitions)], e@\loc);   
}

// -- call expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`){  
   lrel[str,MuExp] kwargs = translateKeywordArguments(keywordArguments);
   ftype = getType(expression); // Get the type of a receiver
   MuExp receiver = translate(expression);
   //println("receiver: <receiver>");
   list[MuExp] args = [ translate(a) | Expression a <- arguments ];
   
   // TODO: JURGEN removed this because we have Type::typeOf to implement this.
   //if("<expression>" == "typeOf"){
   //     return muCallPrim3("typeOf", aadt("AType", [], dataSyntax()), [avalue()], args, e@\loc);
   //}
   
   if(getOuterType(expression) == "astr"){
   		return muCallPrim3("create_node", getType(e), [ getType(arg) | arg <- arguments ], [receiver, *args, muKwpActuals(kwargs)], e@\loc);
   }
  
   if(getOuterType(expression) == "aloc"){
       return muCallPrim3(size(args) == 2 ? "create_loc_with_offset" : "create_loc_with_offset_and_begin_end", aloc(), [aloc()], [receiver, *args], e@\loc);
   }
   if(muFun(_,_) := receiver || muConstr(AType _) := receiver) {
        return muCall(receiver, ftype, args, kwargs);
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
   return muOCall3(receiver, ftype, args, kwargs, e@\loc);
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
    for(gen <- generators){
        <btscope1, btscopes> = getBTInfo(gen, btscope1, btscopes);
    }
    return registerBTScope(e, <btscope1.enter, btscope1.resume, btscope.resume>, btscopes);
}

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    str whileName = nextLabel("ANY_LOOP");
    gens = normalizeAnd([ g | g <- generators]);
    any_found = muTmpIValue(nextTmp("any_found"), fuid, abool());
    enterLoop(whileName,fuid);
    my_enter = nextTmp("ANY");
    my_btscopes = getBTScopesAnd(gens, my_enter, ());
    my_fail = getEnter(gens[0], my_btscopes)+"_FOR";
    exit = muBlock([]);
    code = muBlock([muAssign(any_found, muCon(true)), muBreak(whileName)]);
    for(gen <- reverse(gens)){
        if((Expression) `<Pattern pat> \<- <Expression exp>` := gen){
            enter_gen = getEnter(gen, my_btscopes)+"_FOR";
            if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){     
                elemType = alub(getType(first), getType(last));
                elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
                code = muForRange(enter_gen, elem, translate(first), muCon(0), translate(last), translatePat(pat, elemType, elem, my_btscopes, code, muFail(my_fail)));
            } else 
            if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
                elemType = alub(alub(getType(first), getType(second)), getType(last));
                elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
                code = muForRange(enter_gen, elem, translate(first), translate(second), translate(last), translatePat(pat, elemType, elem, my_btscopes, code, muFail(my_fail)));
            } else {
                elemType = getElementType(getType(exp));
                if(isVoidType(elemType)){
                    code = muCon(false);
                } else {
                    elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
                    code = muForAll(enter_gen, elem, getType(exp), translate(exp), translatePat(pat, elemType, elem, my_btscopes, code, muFail(my_fail)));
                }
            }
        } else {
            code = translateBool(gen, my_btscopes, code, exit);
        }
    }
      
    code = muValueBlock(abool(),
                        [ muVarInit(any_found, muCon(false)),
                          muDoWhile(whileName, code, muCon(false)),
                          any_found ]);
    leaveLoop();
    return code;
}

MuExp translateBool (e:(Expression) `any ( <{Expression ","}+ generators> )`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e), trueCont, falseCont);


// -- all expression ------------------------------------------------

BTINFO getBTInfo(e:(Expression) `all ( <{Expression ","}+ generators> )`, BTSCOPE btscope, BTSCOPES btscopes){
    BTSCOPE btscope1 = <"<btscope.enter>_ALL", btscope.resume,  btscope.resume>;
    for(gen <- generators){
        <btscope1, btscopes> = getBTInfo(gen, btscope1, btscopes);
    }
    return registerBTScope(e, <btscope1.enter, btscope1.resume, btscope.resume>, btscopes);
}

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    str whileName = nextLabel("ALL_LOOP");
    gens = normalizeAnd([ g | g <- generators]);
    all_true = muTmpIValue(nextTmp("all_true"), fuid, abool());
    enterLoop(whileName,fuid);
   
    my_btscopes = getBTScopesAnd(gens, nextTmp("ALL"), ());
    enter_gen = getEnter(gens[0], my_btscopes);
    exit = muBlock([muAssign(all_true, muCon(false)), muBreak(enter_gen+"_FOR")]);
    code = muContinue(enter_gen+"_FOR");
    for(gen <- reverse(gens)){
        if((Expression) `<Pattern pat> \<- <Expression exp>` := gen){
           enter_gen = getEnter(gen, my_btscopes)+"_FOR";
            if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
                elemType = alub(getType(first), getType(last));
                elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
                code = muForRange(enter_gen, elem, translate(first), muCon(0), translate(last), translatePat(pat, elemType, elem, my_btscopes, code, exit));
            } else 
            if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
                elemType = alub(alub(getType(first), getType(second)), getType(last));
                elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
                code = muForRange(enter_gen, elem, translate(first), translate(second), translate(last), translatePat(pat, elemType, elem, my_btscopes, code, exit));
            } else {
                elemType = getElementType(getType(exp));
                if(isVoidType(elemType)){
                    code = muCon(true);
                } else {
                    elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
                    code = muForAll(enter_gen, elem, getType(exp), translate(exp), translatePat(pat, elemType, elem, my_btscopes, code, exit));
                }
            }
        } else {
            code = translateBool(gen, my_btscopes, code, exit);
        }
    }
      
    code = muValueBlock(abool(),
                        [ muVarInit(all_true, muCon(true)),
                          muDoWhile(whileName, code, muCon(false)),
                          all_true ]);
    leaveLoop();
    return code;
}

MuExp translateBool (e:(Expression) `all ( <{Expression ","}+ generators> )`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e), trueCont, falseCont);

// -- comprehension expression --------------------------------------

MuExp translate ((Expression) `<Comprehension comprehension>`) = translateComprehension(comprehension);

private list[MuExp] translateComprehensionContribution(str kind, AType resultType, MuExp writer, list[Expression] results){
  return 
	  for( r <- results){
	    if((Expression) `* <Expression exp>` := r){
	       append muCallPrim3("splice_<kind>", resultType, [avalue(), getType(r)], [writer, translate(exp)], exp@\loc);
	    } else {
	      append muCallPrim3("add_<kind>_writer", resultType, [avalue(), getType(r)], [writer, translate(r)], r@\loc);
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
                     [ muConInit(writer, muCallPrim3("open_list_writer", avalue(), [], [], c@\loc)),
                       translateAndConds(btscopes, conds, muBlock(translateComprehensionContribution("list", resultType, writer, [r | r <- results])), muBlock([])),
                       muCallPrim3("close_list_writer", getType(c), [avalue()], [writer], c@\loc) 
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
                     [ muConInit(writer, muCallPrim3("open_set_writer", avalue(), [], [], c@\loc)),
                      translateAndConds(btscopes, conds, muBlock(translateComprehensionContribution("set", resultType, writer, [r | r <- results])), muBlock([])),
                      muCallPrim3("close_set_writer", getType(c), [avalue()], [writer], c@\loc) 
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
                     [ muConInit(writer, muCallPrim3("open_map_writer", avalue(), [], [], c@\loc)),
                       translateAndConds(btscopes, conds, muCallPrim3("add_map_writer", avoid(), [getType(from), getType(to)], [writer] + [ translate(from), translate(to)], c@\loc), muBlock([])),
                       muCallPrim3("close_map_writer", resultType, [avalue()], [writer], c@\loc) 
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
      
       
       kindwriter_open_code = muCallPrim3("open_<kind>_writer", avalue(), [], [], e@\loc);
       
       enterWriter(writer.name);
       code = [ muConInit(writer, kindwriter_open_code) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim3("splice_<kind>", avoid(), [avalue(), getType(elem)], [writer, translate(elem.argument)], elem.argument@\loc);
            } else {
              code += muCallPrim3("add_<kind>_writer", avoid(), [avalue(), elmType], [writer, translate(elem)], elem@\loc);
           }
       }
       code += [ muCallPrim3("close_<kind>_writer", getType(e), [avalue()], [ writer ], e@\loc) ];
       leaveWriter();
       return muValueBlock(getType(e), code);
    } else {
      //if(size(es) == 0 || all(elm <- es, isConstant(elm))){
      //   return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      //} else 
        return muCallPrim3("create_<kind>", getType(e), [elmType], [ translate(elem) | Expression elem <- es ], e@\loc);
    }
}

// -- reified type expression --------------------------------------

MuExp translate (e: (Expression) `# <Type tp>`) {
	t = translateType(tp);
	return muATypeCon(t, collectNeededDefs(t));
}	

// -- tuple expression ----------------------------------------------

MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) {
    //if(isConstant(e)){
    //  return muCon(readTextValueString("<e>"));
    //} else
        return muCallPrim3("create_tuple", getType(e), [ getType(elem) | Expression elem <- elements], [ translate(elem) | Expression elem <- elements ], e@\loc);
}

// -- map expression ------------------------------------------------

MuExp translate (e:(Expression) `( <{Mapping[Expression] ","}* mappings> )`) {
   //if(isConstant(e)){
   //  return muCon(readTextValueString("<e>"));
   //} else 
   mapType = getType(e);
   return muCallPrim3("create_map", mapType, [mapType.keyType, mapType.valType], [ translate(m.from), translate(m.to) | m <- mappings ], e@\loc);
}   

// -- it expression (in reducer) ------------------------------------

MuExp translate (e:(Expression) `it`) = 
    muTmpIValue(topIt().name,topIt().fuid, getType(e));
 
// -- qualified name expression -------------------------------------
 
MuExp translate((Expression) `<QualifiedName v>`) = 
    translate(v);
    
MuExp translateBool((Expression) `<QualifiedName v>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) = 
    translateBool(v, btscopes, trueCont, falseCont);
 
MuExp translate((QualifiedName) `<QualifiedName v>`) =
    mkVar("<v>", v@\loc);

MuExp translateBool((QualifiedName) `<QualifiedName v>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) =
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
   access = muCallPrim3(op, avalue()/*getType(e)*/, getType(exp) + [getType(s) | s <- subscripts],
                       translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | Expression s <- subscripts], e@\loc);
   
   return access;
}

// -- slice expression ----------------------------------------------

MuExp translate ((Expression) `<Expression expression> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, optLast);

// -- slice with step expression ------------------------------------

MuExp translate ((Expression) `<Expression expression> [ <OptionalExpression optFirst> , <Expression second> .. <OptionalExpression optLast> ]`) =
	translateSlice(expression, optFirst, second, optLast);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) {
    ot = getType(expression);
    return muCallPrim3("slice", ot, [ot], [ translate(expression), translateOpt(optFirst), muNoValue(), translateOpt(optLast) ], expression@\loc);
}

public MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muNoValue() : translate(optExp.expression);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) {
    ot = getType(expression);
    return muCallPrim3("slice", ot, [ot], [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ], expression@\loc);
}

// -- field access expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) {
   tp = getType(expression);
   fieldType = getType(field);
   ufield = unescape("<field>");
   
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       return translateProject(e, expression, [(Field)`<Name field>`], e@\loc, false);
   }
   
   if(isNonTerminalType(tp)){
      return muGetField(fieldType, tp, translate(expression), ufield);
   }
   
   op = getOuterType(expression);
  
    if(op == "aadt"){
        <consType, isKwp> = getConstructorInfo(tp, fieldType, ufield);
          return isKwp ? muGetKwField(consType, tp, translate(expression), ufield)
                       : muGetField(getType(e), consType, translate(expression), ufield);
    }
    
    return muGetField(getType(e), getType(expression), translate(expression), ufield);   
}

// -- field update expression ---------------------------------------

MuExp translate ((Expression) `<Expression expression> [ <Name key> = <Expression replacement> ]`) {
    tp = getType(expression);  
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    } else if(isLocType(tp)){
     	return muSetField(tp, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    } else if(isNodeType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<key>"), translate(replacement));
    }
    if(tupleHasFieldNames(tp)){
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
 
    if(isNodeType(tp)){
        fieldName = ["<f>" | f <- fields][0];
        if(isGuarded){
            return muGuardedGetField(avalue(), tp, translate(base), fieldName);
         } else {
           return muGetField(avalue(), tp, translate(base), fieldName);
         }
    }
    
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isLocType(tp)){
        return isGuarded ? muGuardedGetField(tp, getType(base), translate(base), unescape("<fields[0]>"))
                         : muGetField(tp, getType(base), translate(base), unescape("<fields[0]>"));
    }
    //println("translateProject: <e>");
    if(tupleHasFieldNames(tp)){
       	fieldNames = getTupleFieldNames(tp);
    }
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon(indexOf(fieldNames, unescape("<f>"))) | f <- fields];
      
    return muCallPrim3((isGuarded ? "guarded_" : "") + "field_project", getType(e), [getType(base)], [ translate(base), *fcode], src);
}

// -- set annotation expression -------------------------------------

// Deprecated
MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) {
    tp = getType(expression);  
    list[str] fieldNames = [];
    if(isRelType(tp)){
       tp = getSetElementType(tp);
    } else if(isListType(tp)){
       tp = getListElementType(tp);
    } else if(isMapType(tp)){
       tp = getMapFieldsAsTuple(tp);
    } else if(isADTType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<name>"), translate(val));
    } else if(isLocType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<name>"), translate(val));
    } else if(isNodeType(tp)){
        return muSetField(tp, getType(expression), translate(expression), unescape("<name>"), translate(val));
    }
    if(tupleHasFieldNames(tp)){
          fieldNames = getTupleFieldNames(tp);
    }   
    //TODO
    return muSetField(tp, getType(expression), translate(expression), indexOf(fieldNames, unescape("<name>")), translate(val));
    
}

// -- get annotation expression -------------------------------------
//Deprecated
MuExp translate (e:(Expression) `<Expression expression>@<Name name>`) =
    muGetAnno(translate(expression), getType(e), unescape("<name>"));

//// Deprecated
//MuExp translate (e:(Expression) `<Expression expression>@<Name name>`) {
//   
//   tp = getType(expression);
//   fieldType = getType(name);
//   
//   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
//       return translateProject(e, expression, [(Field)`<Name name>`], e@\loc, false);
//   }
//   //if(isNonTerminalType(tp)){
//   //   return muGetField("nonterminal", getConstructorType(tp, fieldType), translate(expression), unescape("<field>"));
//   //}
//   op = getOuterType(expression);
//   ufield = unescape("<name>");
//   if(op == "aadt"){
//        <consType, isKwp> = getConstructorInfo(tp, fieldType, ufield);
//          return isKwp ? muGetKwField(consType, tp, translate(expression), ufield)
//                       : muGetField(getType(e), consType, translate(expression), ufield);
//    }
//    
//    return muGetField(getType(e), getType(expression), translate(expression), ufield);  
//    
//    }

// -- is expression --------------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim3("is", abool(), [getType(expression)], [translate(expression), muCon(unescape("<name>"))], e@\loc);

MuExp translateBool(e:(Expression) `<Expression expression> is <Name name>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e),  trueCont, falseCont);
    
// -- has expression -----------------------------------------------

MuExp translate ((Expression) `<Expression expression> has <Name name>`) {
    uname = unescape("<name>");
    outer = getOuterType(expression);
    tp = getType(expression);
    if(outer == "anode"){
      // Always compute existence of field at run time
      return muHasField(translate(expression), tp, uname, {}); 
    } else if (outer == "aadt"){
        commonKwFields = getCommonKeywordFieldsNameAndType()[tp] ? ();  
        if(commonKwFields[uname]?){
            return muCon(true); // If desired field is a common keyword field, all constructors have it
        }
        
        // Determine set of constructors with the desired field                                    
        constructors = getConstructorsMap()[tp] ? {};
        consesWithField = {c | c:acons(AType adt, list[AType] fields, list[Keyword] kwFields) <- constructors,
                               (!isEmpty(fields) && any(f <- fields, f.label == uname)) ||
                               (!isEmpty(kwFields) && any(kwf <- kwFields, kwf.fieldType.label == uname))
                           };
        //if(isEmpty(consesWithField)){
        //    return muCon(false);    // It is statically known that there is no constructor with desired field
        //}

        // Compute result at runtime, guided by the set of constructors that do have the desired field
        return muHasField(translate(expression), tp, uname, consesWithField); 
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

MuExp translate((Expression) `<Expression argument> ?`) =
	translateIsDefined(argument);

MuExp translateBool((Expression) `<Expression argument> ?`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translateIsDefined(argument), trueCont, falseCont);
	
private MuExp translateIsDefined(Expression exp)
    = muIsDefinedValue(translateGuarded(exp));
	
MuExp translateGuarded((Expression) `( <Expression exp1> )`)
    = translateGuarded(exp1);
 
MuExp translateGuarded(exp: (Expression) `<Expression exp1> [ <{Expression ","}+ subscripts> ]`)
    = translateSubscript(exp, true);

MuExp translateGuarded(exp: (Expression) `<Expression expression> \< <{Field ","}+ fields> \>`)
    = translateProject(exp, expression, [f | f <- fields], exp@\loc, true);

MuExp translateGuarded(exp: (Expression) `<Expression expression>@<Name name>`)
    //= muGuardedGetField(getType(exp), getType(expression), translate(expression), unescape("<name>"));
    = muGuardedGetAnno(translate(expression), getType(exp), unescape("<name>"));

MuExp translateGuarded(exp: (Expression) `<Expression expression> . <Name field>`)
    = translateProject(exp, expression, [(Field)`<Name field>`], exp@\loc, true);
  //  = muGuardedGetField(getType(exp), getType(expression), translate(expression),  unescape("<field>"));

// -- isDefinedOtherwise expression ---------------------------------

MuExp translate(e: (Expression) `<Expression lhs> ? <Expression rhs>`) {
      str fuid = topFunctionScope();
      guarded = muTmpGuardedIValue(nextLabel("guarded"), fuid);
      return muValueBlock(getType(e),
                          [ muVarInit(guarded, translateGuarded(lhs)),
                            muIfExp(muIsDefinedValue(guarded),  muGetDefinedValue(guarded, getType(lhs)), translate(rhs))
                          ]);
}

public MuExp translateIfDefinedOtherwise(MuExp muLHS, MuExp muRHS, loc src) {
    str fuid = topFunctionScope();
    guarded = muTmpGuardedIValue(nextLabel("guarded"), fuid);
    lhsType = avalue();
    if( muGetField(AType resultType, AType baseType, MuExp baseExp, str fieldName) := muLHS){
        muLHS = muGuardedGetField(resultType, baseType, baseExp, fieldName);
        lshType = resultType;
    } else if(muGetKwField(AType resultType, AType consType, MuExp exp, str fieldName) := muLHS){
        muLHS = muGuardedGetField(resultType, consType has adt ? consType.adt : consType, exp, fieldName);
        lshType = resultType;
    } else if(muCallPrim3("subscript", AType result, list[AType] details, list[MuExp] exps, loc src) := muLHS){
        muLHS = muCallPrim3("guarded_subscript", result, details, exps, src);
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

MuExp translate(e:(Expression) `!<Expression exp>`) {
    //if(backtrackFree(exp)){
        return muCallPrim3("not", abool(), [abool()], [translate(exp)], e@\loc);
    //}
    //btscopes = getBTScopes(e, nextTmp("NOT"));
    //code = translateAndConds(btscopes, [exp], muFail(getFail(exp, btscopes)), muSucceed(getEnter(exp, btscopes)));
    //return code;
}
    
MuExp translateBool((Expression) `!<Expression argument>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) {
    return translateAndConds(btscopes, [argument], falseCont, trueCont);
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
    return muCallPrim3("parse", resultType,
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
    //= comparison("equal", e);
   = muIfExp(comparison("equal", e), muCon(true), muCon(false));

MuExp translateBool(e:(Expression) `<Expression lhs> == <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(comparison("equal", e), trueCont, falseCont);

// -- not equal expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)
    = muIfExp(comparison("equal", e), muCon(false), muCon(true));

MuExp translateBool(e:(Expression) `<Expression lhs> != <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(comparison("equal", e),  falseCont, trueCont);
 
// -- match expression --------------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <my_btscope, btscopes1> = getBTInfo(pat, btscope, btscopes);
    my_btscope.enter = btscope.enter;
    <btscope2, btscopes2> = getBTInfo(exp, my_btscope, btscopes1);
    return registerBTScope(e, my_btscope, btscopes2);
}

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPES btscopes){
    //iprintln(btscopes);
    my_btscope = btscopes[getLoc(e)];
    //return translateMatch(pat, exp, btscopes, muSucceed(my_btscope.resume), muFail(my_btscope.\fail));
    return muEnter(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail)));
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = translateMatch(pat, exp, btscopes, trueCont, falseCont);
    
// -- no match expression -------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <my_btscope, btscopes1> = getBTInfo(pat, btscope, btscopes);
    my_btscope.enter = btscope.enter;
    <btscope2, btscopes2> = getBTInfo(exp, my_btscope, btscopes1);
    return registerBTScope(e, <my_btscope.enter, my_btscope.\fail, btscope.resume>, btscopes2);
}
    
MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression exp>`,  BTSCOPES btscopes) { 
    my_btscope = btscopes[getLoc(e)];
   //iprintln(btscopes);
    //println("my_btscope = <my_btscope>");
   //return muEnter(my_btscope.enter, translateMatch(pat, exp, btscopes, muFail(my_btscope.\fail), muSucceed(my_btscope.enter)));
   //return muCallPrim3("not", abool(), [abool()], [translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail))], e@\loc);
    
    code = muEnter(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail)));
    //iprintln(code);
    code = negate(code, []);
    //iprintln(code);
    return code;
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    my_btscope = btscopes[getLoc(e)];
    return negate(muEnter(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail))), []);
    //code = translateMatch(pat, exp, btscopes, falseCont, trueCont);
    //return code;
}
    
// -- generator expression ----------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    <btscope_exp, btscopes> = getBTInfo(exp, btscope, btscopes);
    <btscope_pat, btscopes> = getBTInfo(pat, "<btscope.enter>_GEN", btscopes);
    return registerBTScope(e, btscope_pat, btscopes);
}
    
MuExp translateGenerator(Pattern pat, Expression exp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    elemType = getElementType(getType(exp));
    if(isVoidType(elemType)) return falseCont;
    
    str fuid = topFunctionScope();
    elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
    
    enterGen = getEnter(pat, "GEN", btscopes);
    resumeGen = getResume(pat, "GEN", btscopes);
    enterLoop(enterGen, fuid);
    code = muBlock([]);
    // enumerator with range expression
    if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
        code = muForRange(enterGen, elem, translate(first), muCon(0), translate(last), translatePat(pat, alub(getType(first), getType(last)), elem, btscopes, trueCont, falseCont));
    } else
    // enumerator with range and step expression
    if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
       code = muForRange(enterGen, elem, translate(first), translate(second), translate(last), translatePat(pat, alub(alub(getType(first), getType(second)), getType(last)), elem, btscopes, trueCont, falseCont));
    } else {
    // generic enumerator
        code = muForAll(enterGen, elem, getType(exp), translate(exp), translatePat(pat, elemType,  elem, btscopes, trueCont, muFail(resumeGen)) );
    }
    leaveLoop();
    //iprintln(code);
    return code;
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`){
    btscopes = getBTScopes(e, nextTmp("GEN"));
    return translate(e, btscopes);
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPES btscopes){
    t = muSucceed(getEnter(e, btscopes));
    f = muFail(getResume(e, btscopes));
    return translateGenerator(pat, exp, btscopes, muSucceed(getEnter(e, btscopes)), muFail(getResume(e, btscopes)));
}

MuExp translateBool(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    return  translateGenerator(pat, exp, btscopes, trueCont, falseCont);
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
    //enter = "<enter>_AND";
    BTSCOPE c_btscope = <enter, enter, enter>;
    for(c <- normalizeAnd(conds)){
        <c_btscope, btscopes> = getBTInfo(c, c_btscope, btscopes);
    }
    //iprintln(btscopes);
    return btscopes;
}

BTSCOPES getBTScopesParams(list[Pattern] args, str enter){
    //enter = "<enter>_Params";
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
    //iprintln(btscopes);
   
    if(backtrackFree(e)){
        return muIfExp(translate(lhs), translate(rhs), muCon(false));
    }
    enterLhs = getEnter(e, btscopes);
    failRhs = getFail(rhs, btscopes);
  
    code = muEnter(enterLhs, translateAndConds(btscopes, [lhs, rhs], muSucceed(enterLhs), muFail(enterLhs)));// <<<<
    //iprintln(code);
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
            //print("cond: "); println(cond);
            //print("trueCont: "); iprintln(trueCont);
            trueCont = muIfExp(translate(cond), trueCont, falseCont);
            //println("trueCont:"); iprintln(trueCont);
        }
        return trueCont;
    }
    for(i <- reverse(index(conds))){
        cont = i == 0 ? falseCont : muFail(getResume(conds[i-1], btscopes));   // <<<
        //iprintln(cont);
        trueCont = normalize(translateBool(conds[i], btscopes, trueCont, cont));
    }
    
    return muEnter(getEnter(conds[0], btscopes), trueCont);
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
    lhs_enter = btscope.enter; //"<btscope.enter>_OR_LHS";
    lhs_btscope = <lhs_enter, lhs_enter, lhs_enter>;
    <lhs_btscope, btscopes1> = getBTInfo(lhs, lhs_btscope, btscopes);
    //iprintln(btscopes1);
    
    rhs_enter = btscope.enter; //"<btscope.enter>_OR_RHS";
    rhs_btscope = <rhs_enter, rhs_enter, rhs_enter>;
    <rhs_btscope, btscopes2> = getBTInfo(rhs, rhs_btscope, btscopes1);
    //iprintln(btscopes2);
    res =  registerBTScope(e, lhs_btscope, btscopes2);
   //iprintln(res);
    return res;
}

// ---- translate or expression

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`){
    btscopes = getBTScopes(e, nextTmp("OR"));
    return translate(e, btscopes);
}

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`, BTSCOPES btscopes) {
   if(backtrackFree(lhs)){
        return muIfExp(translate(lhs), muCon(true), translate(rhs));
   }
   code = muEnter(getEnter(e, btscopes), translateOrConds(btscopes, lhs, rhs, muSucceed(getResume(lhs, btscopes)), muFail(getFail(rhs, btscopes))));                                                                 
   return code;                                                                               
}
    
MuExp translateBool((Expression) `<Expression lhs> || <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
   if(backtrackFree(lhs) && backtrackFree(rhs)){
        return muIfExp(translate(lhs), trueCont, muIfExp(translate(rhs), trueCont, falseCont));
   }
    return translateOrConds(btscopes, lhs, rhs, trueCont, falseCont);
}  

// ---- translateOrConds

MuExp translateOrConds(BTSCOPES btscopes, Expression lhs, Expression rhs, MuExp trueCont, MuExp falseCont){
    //if(backtrackFree(lhs) && backtrackFree(rhs)){
    //    return muIfExp(translate(lhs), trueCont, translateBool(rhs, btscopes, trueCont, falseCont));
    //}
    trueCont1  = redirect(trueCont, getResume(rhs, btscopes), getResume(lhs, btscopes));
    trueCont2  = redirect(trueCont, getResume(lhs, btscopes), getResume(rhs, btscopes));
    //iprintln(falseCont);
    //println("resume lhs: <getResume(lhs, btscopes)>");
    //println("enter rhs: <getEnter(rhs, btscopes)>");
    falseCont2 = redirect(falseCont, getResume(lhs, btscopes), getEnter(rhs, btscopes)); // <<<< getResume?
    //iprintln(falseCont2);
     
    enter_lhs = getEnter(lhs, btscopes);
    enter_rhs = getEnter(rhs, btscopes);
    lhs_code = muEnter(enter_lhs, translateBool(lhs, btscopes, trueCont1, muBlock([]))); 
    rhs_code = muEnter(enter_rhs, translateBool(rhs, btscopes, trueCont2, falseCont2));
    
    return muBlock([lhs_code, rhs_code]);
}

MuExp redirect(MuExp exp, from, to){
    return visit(exp){
           case muFail(from)    => muFail(to)
           case muSucceed(from) => muSucceed(to)
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
//                                      +-----------+
//            +------------------------>| trueCont1 |
//            |                         +-----------+
//            |
//        +---F--+----------------------------------+
//        |      |                 +-----------+    |
//  ----->| lhs  R           +---->| falseCont |    |
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
        return muIfExp(translate(lhs), translate(rhs), muCon(true));
    }
    enterLhs = getEnter(lhs, "IMPLIES", btscopes);
    code = muEnter(enterLhs, translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(enterLhs), muFail(getResume(e, "IMPLIES", btscopes))),
                                                                          muSucceed(enterLhs))); 
    
    //code = muEnter(getEnter(lhs, btscopes), translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getResume(rhs, btscopes)), muFail(getFail(rhs, btscopes))),
    //                                                                     muSucceed(getResume(rhs, btscopes)))); 
    return code;  
}
    
MuExp translateBool(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    trueCont2 = redirect(trueCont, getResume(lhs, btscopes), getResume(rhs, btscopes));
    falseCont2 = redirect(falseCont, getResume(rhs, btscopes), getResume(e, btscopes));
    return muEnter(getEnter(e, btscopes), translateBool(lhs, btscopes, translateBool(rhs, btscopes, trueCont2, falseCont2), trueCont));
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

MuExp translate(Expression e:(Expression) `<Expression lhs> ==\> <Expression rhs>`){ 
    return translate(e, getBTScopes(e, nextTmp("EQUIV")));
}
   
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`, BTSCOPES btscopes) {
    if(backtrackFree(e)){
        str fuid = topFunctionScope();
        rhs_val = muTmpIValue(nextTmp("rhs_val"), fuid, abool());
        return muValueBlock(abool(), [ muConInit(rhs_val, translate(rhs)),
                                       muIfExp(translate(lhs), 
                                               rhs_val,
                                               muCallPrim3("not", abool(), [abool()], [rhs_val], e@\loc))
                                      ]);
    }  
   enterLhs = getEnter(lhs, "EQUIV", btscopes);
   code = muEnter(enterLhs, translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getResume(rhs, btscopes)), muFail(getFail(rhs, btscopes))),
                                                         translateBool(rhs, btscopes, muFail(enterLhs), muSucceed(enterLhs)))); 
                                                         
   //code = muEnter(enterLhs, translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getResume(rhs, btscopes)), muFail(getFail(rhs, btscopes))),
   //                                                      translateBool(rhs, btscopes, muFail(getFail(rhs, btscopes)), muSucceed(getResume(rhs, btscopes))))); 
   //
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

MuExp translate((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) {
	btscopes = getBTScopes(condition, nextTmp("COND"));
	res = translateBool(condition, btscopes,  translate(thenExp), translate(elseExp));
	//iprintln(res);
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

MuExp translate((Expression) `<Concrete con>`) = translateConcrete(con.args[0]);

// -- any other expression that may require backtracking ------------

default MuExp translate(Expression e) {
	btscopes = getBTScopes(e, nextTmp("EXP"));
	return backtrackFree(e) ? translate(e, btscopes)
	                        : muEnter(getEnter(e, btscopes), translateBool(e, btscopes, muSucceed(getEnter(e,btscopes)), muFail(getFail(e, btscopes))));
  
    //return translateBool(e, btscopes, muSucceed(getFail(e, btscopes)), muFail(getResume(e, btscopes)));
    //return muEnter(getEnter(e, btscopes), translateBool(e, btscopes, muSucceed(getEnter(e,btscopes)), muFail(getResume(e, btscopes))));
}

default MuExp translateBool(Expression e, str btscope, MuExp trueCont, MuExp falseCont) {
    println(e);
    throw "TRANSLATEBOOL, MISSING CASE FOR EXPRESSION: <e>";
}

// Is an expression free of backtracking? 

// TODO: add more cases?

bool backtrackFree(Expression e){
    top-down visit(e){
    
    case (Expression) `all ( <{Expression ","}+ generators> )`: 
        return true;
    case (Expression) `any ( <{Expression ","}+ generators> )`: 
        return true;
    case Comprehension comprehension:
        return true;
    case (Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`:
        return true; 
    case (Expression) `<Pattern pat> \<- <Expression exp>`: 
        return false;
    case (Expression) `<Pattern pat> := <Expression exp>`:
        return backtrackFree(pat);
    case (Expression) `<Pattern pat> !:= <Expression exp>`:
        return backtrackFree(pat);
    case (Expression) `!<Expression exp>`:
        return backtrackFree(exp);
    case (Expression) `<Expression e1> || <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);
    case (Expression) `<Expression e1> && <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression e1> \<==\> <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression e1> ==\> <Expression e2>`:
        return backtrackFree(e1) && backtrackFree(e2);  
    case (Expression) `<Expression cond> ? <Expression thenExp> : <Expression elseExp>`:
        return backtrackFree(cond) && backtrackFree(thenExp) && backtrackFree(elseExp);
    }
    return true;
}

/*********************************************************************/
/*                  End of Expessions                                */
/*********************************************************************/