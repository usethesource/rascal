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
import lang::rascalcore::compile::CompileTimeError;

import lang::rascal::\syntax::Rascal;

import analysis::typepal::AType;
import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

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
    return muCallPrim3(op, getType(e), [getType(e.lhs), getType(e.rhs)], [*translate(e.lhs), *translate(e.rhs)], e@\loc);
}    
private MuExp unary(str op, Expression e, Expression arg) = 
    muCallPrim3(op, getType(e), [getType(arg)], [translate(arg)], e@\loc);

// ----------- compose: exp o exp ----------------

private MuExp compose(Expression e){
  lhsType = getType(e.lhs);
  return isFunctionType(lhsType) || isOverloadedAType(lhsType) ? translateComposeFunction(e) : infix("compose", e);
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
  fun = muFunction(comp_fuid, comp_name, comp_ftype, ["a", "b"], [],  scopeId, nargs, 2, false, false, false, getExternalRefs(body_code, comp_fuid), \e@\loc, [], (), body_code);
 
  loc uid = declareGeneratedFunction(comp_name, comp_fuid, comp_ftype, e@\loc);
  addFunctionToModule(fun);  
  //addOverloadedFunctionAndResolver(ofqname, <comp_name, comp_ftype, getModuleName(), [ofqname], []>);
 
  return muOFun(comp_fuid);
}

// ----------- addition: exp + exp ----------------

private  MuExp add(Expression e){
    lhsType = getType(e.lhs);
    return isFunctionType(lhsType) /*|| isOverloadedType(lhsType)*/ ? translateAddFunction(e) :infix("add", e);
}

private MuExp translateAddFunction(Expression e){
    return muBlock([]); // TODO
//  //println("translateAddFunction: <e>");
//  lhsType = getType(e.lhs);
//  rhsType = getType(e.rhs);
//  
//  str2uid = invertUnique(uid2str);
//
//  MuExp lhsReceiver = translate(e.lhs);
//  OFUN lhsOf;
//  
//  if(hasOverloadingResolver(lhsReceiver.fuid)){
//    lhsOf = getOverloadedFunction(lhsReceiver.fuid);
//  } else {
//    uid = str2uid[lhsReceiver.fuid];
//    lhsOf = <lhsReceiver.fuid, lhsType, topFunctionScope(), [uid]>;
//    addOverloadedFunctionAndResolver(lhsReceiver.fuid, lhsOf);
//  }
// 
//  MuExp rhsReceiver = translate(e.rhs);
//  OFUN rhsOf;
//  
//  if( hasOverloadingResolver(rhsReceiver.fuid)){
//    rhsOf = getOverloadedFunction(rhsReceiver.fuid);
//  } else {
//    uid = str2uid[rhsReceiver.fuid];
//    rhsOf = <rhsReceiver.fuid, rhsType, topFunctionScope(), [uid]>;
//    addOverloadedFunctionAndResolver(rhsReceiver.fuid, rhsOf);
//  }
//  
//   str ofqname = "<lhsReceiver.fuid>_+_<rhsReceiver.fuid>#<e@\loc.offset>_<e@\loc.length>";  // name of addition
// 
//  OFUN compOf = <ofqname, lhsType, lhsOf[2], lhsOf[3] + rhsOf[3]>; // add all alternatives
//  
// 
// 
//  addOverloadedFunctionAndResolver(ofqname, compOf); 
//  return muOFun(ofqname);
}

MuExp comparison(str op, Expression e)
    = infix(op, e);

/*********************************************************************/
/*                  Translate Literals                               */
/*********************************************************************/

// -- boolean literal  -----------------------------------------------

BTINFO getBTInfo(e:(Expression) `<BooleanLiteral b>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

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
	return muValueBlock( astr(),
	                     [ muConInit(template, muTemplate(translatePreChars(pre))),
                           *translateTemplate(template, preIndent, stemplate),
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
                        *translateTemplate(template, indent + midIndent, stemplate, ()),
                        *translateTail(template, indent + midIndent,tail)
                    ])
           ];
 } 
 
 // --- translateTemplate 
 
 private list[MuExp] translateTemplate(MuExp template, str indent, Expression expression, BTSCOPES btscopes){
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
    return cargs[0]; // TODO
	//fragType = getType(e);
 //   //println("translateConcrete, fragType = <fragType>");
 //   reifiedFragType = symbolToValue(fragType);
 //   // TODO: getGrammar uses a global variable. Add as parameter to the call stack instead
 //   try {
 //       return parseFragment(getModuleName(), getModuleTags(), reifiedFragType, e, e@\loc, getGrammar());
 //   } catch ParseError(loc src): {
 //       throw CompileTimeError(error("Parse error in concrete fragment or pattern", src));
 //   } catch Ambiguity(loc src, str stype, str string): {
 //       throw CompileTimeError(error("Ambiguity in concrete fragment or pattern (of type <stype>)", src));
 //   }
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
    return muBlock([]); // TODO
   //if(t:appl(Production prod, list[Tree] args) := e){
   //    my_src = e@\loc ? src;
   //    //iprintln("translateConcreteParsed:"); iprintln(e);
   //    if(isConcreteHole(t)){
   //        varloc = getConcreteHoleVarLoc(t);
   //        //println("varloc = <getType(varloc)>");
   //        <fuid, pos> = getVariableScope("ConcreteVar", varloc);
   //        
   //        return muVar("ConcreteVar", fuid, pos);
   //     } 
   //     MuExp translated_elems;
   //     if(any(arg <- args, isConcreteListVar(arg))){ 
   //        //println("splice in concrete list");      
   //        str fuid = topFunctionScope();
   //        writer = nextTmp();
   //     
   //        translated_args = [ muCallPrim3(isConcreteListVar(arg) ? "listwriter_splice_concrete_list_var" : "listwriter_add", 
   //                                       [muTmpIValue(writer,fuid), translateConcreteParsed(arg, my_src)], my_src)
   //                          | Tree arg <- args
   //                          ];
   //        translated_elems = muValueBlock([ muConInit(muTmpListWriter(writer, fuid), muCallPrim3("listwriter_open", [], my_src)),
   //                                          *translated_args,
   //                                          muCallPrim3("listwriter_close", [muTmpIValue(writer,fuid)], my_src) 
   //                                        ]);
   //     } else {
   //        translated_args = [translateConcreteParsed(arg, my_src) | Tree arg <- args];
   //        if(allConstant(translated_args)){
   //     	  return muCon(appl(prod, [ce | muCon(Tree ce) <- translated_args])[@\loc=my_src]);
   //        }
   //        translated_elems = muCallPrim3("list_create", translated_args, my_src);
   //     }
   //     return muCallPrim3("annotation_set", [muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
   //                                                 [muCon(prod), translated_elems, muCallMuPrim("make_mmap", []), muTypeCon(avoid())]),
   //     								     muCon("loc"), 
   //     								     muCon(my_src)], e@\loc);
   //     //return muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
   //     //              [muCon(prod), translated_elems, muTypeCon(avoid())]);
   // } else {
   //     return muCon(e);
   // }
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
	//fuid = nextLabel("  SURE");
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.formals);
	bool isVarArgs = ftype.varArgs;
  	
  	// Keyword parameters
     lrel[str name, AType atype, MuExp defaultExp]  kwps = translateKeywordParameters(parameters);
    
    // TODO: we plan to introduce keyword patterns as formal parameters
    <formalVars, funBody> = translateFunction(fuid, parameters.formals.formals, ftype, muBlock([translate(stat, ()) | stat <- cbody]), false, []);
    
    addFunctionToModule(muFunction("$CLOSURE<uid.offset>", 
                                   "$CLOSURE<uid.offset>", 
                                   ftype, 
                                   formalVars,
                                   kwps,
                                   surrounding, 
  								   nformals,
  								   getScopeSize(uid), 
  								   isVarArgs, 
  								   false,
  								   false,
  								   getExternalRefs(funBody, fuid),  // << TODO
  								   e@\loc,
  								   [],
  								   (),
  								   funBody));
  	
  	leaveFunctionScope();								  
  	return muFun1(uid); // TODO!
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
    
    enterVisit();
    subjectType = getType(\visit.subject);
    switchname = getLabel(label);
    switchval = muTmpIValue(asTmp(switchname), fuid, avalue()/*subjectType*/);
    
	
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
	<case_code, default_code> = translateSwitchCases(switchval, fuid, useConcreteFingerprint, cases, muSucceedVisitCase(), btscopes);
	
    visitCode = muVisit(muConInit(switchval, translate(\visit.subject)), case_code, default_code, vdescriptor);
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
    return muBlock([]); // TODO
	//
 //   return muCallPrim3("reifiedType_create", [translate(symbol), translate(definitions)], e@\loc);
 //   
}

// -- call expression -----------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`){  
   lrel[str,MuExp] kwargs = translateKeywordArguments(keywordArguments);
   ftype = getType(expression); // Get the type of a receiver
   MuExp receiver = translate(expression);
   //println("receiver: <receiver>");
   list[MuExp] args = [ translate(a) | Expression a <- arguments ];
   
   if("<expression>" == "typeOf"){
        return muCallPrim3("typeOf", aadt("AType", [], dataSyntax()), [avalue()], args, e@\loc);
   }
   
   if(getOuterType(expression) == "astr"){
   		return muCallPrim3("create_node", getType(e), [ getType(arg) | arg <- arguments ], [receiver, *args, muKwpActuals(kwargs)], e@\loc);
   }
  
   if(getOuterType(expression) == "aloc"){
       return muCallPrim3(size(args) == 2 ? "create_loc_with_offset" : "create_loc_with_offset_and_begin_end", aloc(), [aloc()], [receiver, *args], e@\loc);
   }
   if(muFun1(_) := receiver || muConstr(AType _) := receiver) {
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

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    str whileName = nextLabel();
    gens = normalizeAnd([ g | g <- generators]);
    any_found = muTmpIValue(nextTmp("any_found"), fuid, abool());
    enterLoop(whileName,fuid);
    my_enter = nextTmp("ANY");
    my_btscopes = getBTScopesAnd(gens, my_enter, ());
    my_fail = getEnter(gens[0], my_btscopes);
    exit = muBlock([]);
    code = muBlock([muAssign(any_found, muCon(true)), muBreak(whileName)]);
    for(gen <- reverse(gens)){
        if((Expression) `<Pattern pat> \<- <Expression exp>` := gen){
            enter_gen = getEnter(gen, my_btscopes);
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

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) {
    str fuid = topFunctionScope();
    str forName = nextLabel();
    gens = normalizeAnd([ g | g <- generators]);
    all_true = muTmpIValue(nextTmp("all_true"), fuid, abool());
    enterLoop(forName,fuid);
    exit = muBlock([muAssign(all_true, muCon(false)), muBreak(forName)]);
   
    my_btscopes = getBTScopesAnd(gens, nextTmp("ALL"), ());
    code = muContinue(getResume(gens[0], my_btscopes));
    for(gen <- reverse(gens)){
        if((Expression) `<Pattern pat> \<- <Expression exp>` := gen){
           enter_gen = getEnter(gen, my_btscopes);
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
                          muDoWhile(forName, code, muCon(false)),
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
    enter = nextTmp("LCOMP");
    btscopes = getBTScopesAnd(conds, enter, ());
    iprintln(btscopes);
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
    iprintln(btscopes);
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

// -- reified type expression ---------------------------------------
//TODO
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
   
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       return translateProject(e, expression, [(Field)`<Name field>`], e@\loc, false);
   }
   //if(isNonTerminalType(tp)){
   //   return muGetField("nonterminal", getConstructorType(tp, fieldType), translate(expression), unescape("<field>"));
   //}
   op = getOuterType(expression);
   ufield = unescape("<field>");
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
           return muGetField(avalue(), tp, translate(base), fieldsName);
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
    println("translateProject: <e>");
    if(tupleHasFieldNames(tp)){
       	fieldNames = getTupleFieldNames(tp);
    }
    fcode = [(f is index) ? muCon(toInt("<f>")) : muCon(indexOf(fieldNames, unescape("<f>"))) | f <- fields];
      
    return muCallPrim3((isGuarded ? "guarded_" : "") + "field_project", getType(e), [getType(base)], [ translate(base), *fcode], src);
}

// -- set annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) =
    muSetAnno(translate(expression), getType(e), unescape("<name>"), translate(val));

// -- get annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression>@<Name name>`) =
    muGetAnno(translate(expression), getType(e), unescape("<name>"));

// -- is expression --------------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim3("is", abool(), [getType(expression)], [translate(expression), muCon(unescape("<name>"))], e@\loc);

MuExp translateBool(e:(Expression) `<Expression expression> is <Name name>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(translate(e),  trueCont, falseCont);
    
// -- has expression -----------------------------------------------

MuExp translate ((Expression) `<Expression expression> has <Name name>`) {
    if(getOuterType(expression) notin {"aadt", "anode"}){   // static cases: compute result here
        return muCon(hasField(getType(expression), unescape("<name>")));
    } else {                                                // dynamic cases: compute result at runtime
        return muHasField(translate(expression), getType(expression), unescape("<name>"));
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

BTINFO getBTInfo(e:(Expression) `<Expression argument> ?`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);
    
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
    } else if(muCallPrim3("subscript", AType result, list[AType] details, list[MuExp] exps, loc src) := muLHS){
        muLHS = muCallPrim3("guarded_subscript", result, details, exps, src);
        lhsType = result;
    } else if(muGetAnno(MuExp exp, AType resultType, str annoName) := muLHS){
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
    return registerBTScope(e, <exp_btscope.enter, exp_btscope.resume, exp_btscope.\fail>, btscopes1);
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

MuExp translate(e:(Expression) `[ <Type typ> ] <Expression argument>`)  =
    muCon(false); // TODO
 //muCallPrim3("parse", [muCon(getModuleName()), 
 //  					    muCon(type(symbolToValue(translateType(typ)).symbol,getGrammar())), 
 //  					    translate(argument)], 
 //  					    argument@\loc);
 // 
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

BTINFO getBTInfo(e:(Expression) `<Expression lhs> notin <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`)
    = infix("notin", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> notin <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont) 
    = muIfExp(infix("notin", e),  trueCont, falseCont);

// -- in expression -------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> in <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);
    
MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`) 
    = infix("in", e);

MuExp translateBool(e:(Expression) `<Expression lhs> in <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("in", e),  trueCont, falseCont);

// -- greater equal expression --------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> \>= <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`)
    = infix("greaterequal", e);
 
 MuExp translateBool(e:(Expression) `<Expression lhs> \>= <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("greaterequal", e),  trueCont, falseCont);

// -- less equal expression -----------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> \<= <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`)
    = infix("lessequal", e);

MuExp translateBool(e:(Expression) `<Expression lhs> \<= <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("lessequal", e), trueCont, falseCont);

// -- less expression ----------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> \< <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);
    
MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`)
    = infix("less", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \< <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("less", e),  trueCont, falseCont);

// -- greater expression --------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> \> <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`)
    = infix("greater", e);
    
MuExp translateBool(e:(Expression) `<Expression lhs> \> <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(infix("greater", e),  trueCont, falseCont);

// -- equal expression ----------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> == <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`)
    = muIfExp(comparison("equal", e), muCon(true), muCon(false));

MuExp translateBool(e:(Expression) `<Expression lhs> == <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(comparison("equal", e), trueCont, falseCont);

// -- not equal expression ------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Expression lhs> != <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes)
    = registerBTScope(e, <btscope.enter, btscope.resume, btscope.resume>, btscopes);

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`)
    = muIfExp(comparison("equal", e), muCon(false), muCon(true));

MuExp translateBool(e:(Expression) `<Expression lhs> != <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = muIfExp(comparison("equal", e),  falseCont, trueCont);
 
// -- match expression --------------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes)
    = getBTInfoMatch(e, pat, exp, btscope, btscopes);

BTINFO getBTInfoMatch(Expression e, Pattern pat, Expression exp, BTSCOPE btscope, BTSCOPES btscopes){
    //enter = "<btscope.enter>_MATCH";
    //BTSCOPE my_btscope = <enter, enter, btscope.\resume>;
    //<btscope1, btscopes1> = getBTInfo(pat, my_btscope, btscopes);
    //<btscope2, btscopes2> = getBTInfo(exp, btscope1, btscopes1);
    //return registerBTScope(e, <btscope.enter, btscope1.resume, btscope.resume>, btscopes2);
    
     <btscope1, btscopes1> = getBTInfo(pat, btscope, btscopes);
    <btscope2, btscopes2> = getBTInfo(exp, btscope1, btscopes1);
    return registerBTScope(e, <btscope.enter, btscope1.resume, btscope.resume>, btscopes2);
    
}

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPES btscopes){
    iprintln(btscopes);
    my_btscope = btscopes[getLoc(e)];
    //return translateMatch(pat, exp, btscopes, muSucceed(my_btscope.resume), muFail(my_btscope.\fail));
    return muEnter(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail)));
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> := <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = translateMatch(pat, exp, btscopes, trueCont, falseCont);
    
// -- no match expression -------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    return getBTInfoMatch(e, pat, exp, btscope, btscopes);
}
    
//MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression exp>`)
//    = translate(e, getBTScopes(e, nextTmp("NOMATCH")));

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression exp>`,  BTSCOPES btscopes) { 
    my_btscope = btscopes[getLoc(e)];
   //return muCallPrim3("not", abool(), [abool()], [translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail))], e@\loc);
    
    return negate(my_btscope.enter, muEnter(my_btscope.enter, translateMatch(pat, exp, btscopes, muSucceed(my_btscope.enter), muFail(my_btscope.\fail))));
}
    
MuExp translateBool(e:(Expression) `<Pattern pat> !:= <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    my_btscope = btscopes[getLoc(e)];
    code = translateMatch(pat, exp, btscopes, muFail(my_btscope.\fail), muSucceed(my_btscope.enter));
    return code;
}
    
// -- generator expression ----------------------------------------------------

BTINFO getBTInfo(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPE btscope, BTSCOPES btscopes){
    enter = "<btscope.enter>_GEN";
    BTSCOPE my_btscope = <enter, enter, btscope.\resume>;
    <btscope1, btscopes1> = getBTInfo(pat, my_btscope, btscopes);
    <btscope2, btscopes2> = getBTInfo(exp, btscope1, btscopes1);
    return registerBTScope(e, <my_btscope.enter, btscope1.resume, btscope.resume>, btscopes2);
    
   //return getBTInfoMatch(e, pat, exp, btscope, btscopes);
}
    
MuExp translateGenerator(Pattern pat, Expression exp, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont){
    elemType = getElementType(getType(exp));
    if(isVoidType(elemType)) return falseCont;
    
    str fuid = topFunctionScope();
    elem = muTmpIValue(nextTmp("elem"), fuid, elemType);
    
    enter = getEnter(pat, btscopes);
    
    enterLoop(enter, fuid);
    //enterBacktrackingScope(my_btscope);
    code = muBlock([]);
    // enumerator with range expression
    if((Expression) `[ <Expression first> .. <Expression last> ]` := exp){
        code = muForRange(enter, elem, translate(first), muCon(0), translate(last), translatePat(pat, alub(getType(first), getType(last)), elem, btscopes, trueCont, falseCont));
    } else
    // enumerator with range and step expression
    if((Expression) `[ <Expression first> , <Expression second> .. <Expression last> ]` := exp){
       code = muForRange(enter, elem, translate(first), translate(second), translate(last), translatePat(pat, alub(alub(getType(first), getType(second)), getType(last)), elem, btscopes, trueCont, falseCont));
    } else {
    // generic enumerator
        code = muForAll(enter, elem, getType(exp), translate(exp), translatePat(pat, elemType,  elem, btscopes, trueCont, muFail(getResume(pat, btscopes))));
    }
    leaveLoop();
    iprintln(code);
    return code;
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`){
    btscopes = getBTScopes(e, nextTmp("GEN"));
    return translate(e, btscopes);
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPES btscopes){
    return translateGenerator(pat, exp, btscopes, muSucceed(getEnter(e, btscopes)), muFail(getResume(e, btscopes)));
}

MuExp translateBool(e:(Expression) `<Pattern pat> \<- <Expression exp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = translateGenerator(pat, exp, btscopes, trueCont, falseCont);
    
/*****************************************************************************/
/*                  Boolean Operators                                        */
/*****************************************************************************/
BTSCOPES getBTScopes(Expression e, str enter) =  getBTInfo(e, <enter, enter, enter>, ()).btscopes;
BTSCOPES getBTScopes(Expression e, BTSCOPE btscope) =  getBTInfo(e, btscope, ()).btscopes;
BTSCOPES getBTScopes(Expression e, str enter, BTSCOPES btscopes) =  getBTInfo(e, <enter, enter, enter>, btscopes).btscopes;

default BTINFO getBTInfo(Expression e, BTSCOPE btscope, BTSCOPES btscopes) {
    if(!btscopes[getLoc(e)]?){
        btscopes[getLoc(e)] = btscope;
    }
    return <btscopes[getLoc(e)], btscopes>;
}

str getResume(BTSCOPES btscopes){
    r2l_scopes = reverse(sort(domain(btscopes)));
    resume = btscopes[r2l_scopes[-1]].resume;
    for(l <- r2l_scopes){
        btscope = btscopes[l];
        resume = btscope.resume;
        if(btscope.enter !=  btscope.resume){
            return btscope.resume;
        }
    }
    return resume;
}

bool haveEntered(str enter, BTSCOPES btscopes){
    return enter in range(btscopes)<0>;
}

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
    iprintln(btscopes);
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
    iprintln(btscopes);
   
    if(backtrackFree(e)){
        return muIfExp(translate(lhs), translate(rhs), muCon(false));
    }
    my_enter = getEnter(e, btscopes);
    code = muEnter(my_enter, translateAndConds(btscopes, [lhs, rhs], muSucceed(my_enter), muFail(getFail(rhs, btscopes))));// <<<<
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

MuExp translateAndConds(BTSCOPES btscopes, list[Expression] conds, MuExp trueCont, MuExp falseCont, MuExp(MuExp) normalize = identity){
    if(isEmpty(conds)) return normalize(trueCont);
    conds = normalizeAnd(conds);
    falseCont = normalize(falseCont);
    
    if(all(cond <- conds, backtrackFree(cond))){
        for(cond <- reverse(conds)){
            trueCont = muIfExp(translate(cond), trueCont, falseCont);
        }
        return trueCont;
    }
    for(i <- reverse(index(conds))){
        cont = /*i == 0 ? falseCont :*/ muFail(getFail(conds[i], btscopes));   // <<<
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
//                                                    +------------->| falseCont |                              
//                                                    |              +-----------+
//                                                    |
//        +--F--+-----------------------+          +--F--+-----------------------+
//        |     |                       |          |     |                       |
//  ----->| lhs R <------------+        |    +---->| rhs R<------------+         |
//        |     |              |        |    |     |     |             |         |
//        |-----+              |        |    |     |-----+             |         |
//        |  |           +-----F-----+  |    |     |  |           +-----F-----+  |
//        |  |           |           |  |    |     |  |           |           |  |
//        |  +---------->| trueCont1 |  |    |     |  +---------->| trueCont2 |  |
//        |              |           |  |    |     |              |           |  |
//        |              +-----------+  |    |     |              +-----------+  |
//        +-----------------------------+    |     +-----------------------------+
//                      |                    |
//                      +--------------------+

// ---- getBTInfo

BTINFO getBTInfo(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes){
    lhs_enter = "<btscope.enter>_OR_LHS";
    lhs_btscope = <lhs_enter, lhs_enter, lhs_enter>;
    <lhs_btscope, btscopes1> = getBTInfo(lhs, lhs_btscope, btscopes);
    rhs_enter = "<btscope.enter>_OR_RHS";
    rhs_btscope = <rhs_enter, rhs_enter, rhs_enter>;
    <rhs_btscope, btscopes2> = getBTInfo(rhs, rhs_btscope, btscopes1);
    iprintln(btscopes2);
    res =  registerBTScope(e, lhs_btscope, btscopes2);
    iprintln(res);
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
   //if(backtrackFree(lhs)){
   //     return muIfExp(translate(lhs), muCon(true), translate(rhs));
   //}
    return translateOrConds(btscopes, lhs, rhs, trueCont, falseCont);
}  

// ---- translateOrConds

MuExp redirect(MuExp exp, from, to){
    return visit(exp){
           case muFail(from)    => muFail(to)
           case muSucceed(from) => muSucceed(to)
    };
}

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
    iprintln(falseCont2);
     
    lhs_code = muEnter(getEnter(lhs, btscopes), translateBool(lhs, btscopes, trueCont1, muBlock([])));
    rhs_code = muEnter(getEnter(rhs, btscopes), translateBool(rhs, btscopes, trueCont2, falseCont2));
    return muBlock([lhs_code, rhs_code]);
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
//            +------------------------>| trueCont |
//            |                         +-----------+
//            |
//        +---F--+----------------------------------+
//        |      |                   +----------+   |
//  ----->| lhs  R           +----->| falseCont |   |
//        |      |           |      +-----------+   |
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

BTINFO getBTInfo(Expression e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPE btscope, BTSCOPES btscopes){
    <lhs_btscope, btscopes1> = getBTInfo(lhs, btscope, btscopes);
    <rhs_btscope, btscopes2> = getBTInfo(rhs, lhs_btscope, btscopes1);
    return registerBTScope(e, <btscope.enter, rhs_btscope.resume, lhs_btscope.\fail>, btscopes2);
}

// ---- translate implies expression

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPES btscopes) {
    if(backtrackFree(lhs)){
        return muIfExp(translate(lhs), translate(rhs), muCon(true));
    }
    
     code = muEnter(getEnter(lhs, btscopes), translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getEnter(lhs, btscopes)), muFail(getResume(rhs, btscopes))),
                                                                          muSucceed(getEnter(lhs, btscopes)))); 
    
    //code = muEnter(getEnter(lhs, btscopes), translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getResume(rhs, btscopes)), muFail(getFail(rhs, btscopes))),
    //                                                                     muSucceed(getResume(rhs, btscopes)))); 
    return code;  
}
    
MuExp translateBool((Expression) `<Expression lhs> ==\> <Expression rhs>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = translateBool(lhs, btscopes, translateBool(rhs, btscopes, trueCont, falseCont), trueCont);

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
   
   code = muEnter(getEnter(lhs, btscopes), translateBool(lhs, btscopes, translateBool(rhs, btscopes, muSucceed(getResume(rhs, btscopes)), muFail(getFail(rhs, btscopes))),
                                                                        translateBool(rhs, btscopes, muFail(getFail(rhs, btscopes)),      muSucceed(getResume(rhs, btscopes))))); 
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
	return translateBool(condition, btscopes, translate(thenExp), translate(elseExp));
}

bool isConditional((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`) = true;
bool isConditional((Expression) `( <Expression exp> )`) = isConditional(exp);
default bool isConditional(Expression e) = false;

MuExp translateBool((Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`, BTSCOPES btscopes, MuExp trueCont, MuExp falseCont)
    = translateBool(condition, btscopes, muBlock([translate(thenExp), trueCont]),  muBlock([translate(elseExp), falseCont]));

// -- any other expression that may require backtracking ------------

default MuExp translate(Expression e) {
	btscopes = getBTScopes(e, nextTmp("EXP"));
	return translate(e, btscopes);
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