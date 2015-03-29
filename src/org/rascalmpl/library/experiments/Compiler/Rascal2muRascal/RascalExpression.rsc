@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalExpression

import Prelude;
import IO;
import ValueIO;
import Node;
import Map;
import Set;
import String;
import ParseTree;
import util::Reflective;

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
import experiments::Compiler::muRascal::MuBoolExp;

import experiments::Compiler::Rascal2muRascal::RascalConstantCall;

/*
 * Translate a Rascal expression to muRascal using the function: 
 * - MuExp translate(Expression e).
 */
 
/*********************************************************************/
/*                  Auxiliary functions                              */
/*********************************************************************/

int size_keywordArguments((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArguments>`) = 
    (keywordArguments is \default) ? size([kw | KeywordArgument[Expression] kw <- keywordArguments.keywordArgumentList]) : 0;

// Produce a multi-valued or backtrack-free Boolean expression
MuExp makeBoolExp(str operator, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeBoolExp(operator,topFunctionScope(),exps,src);
    addFunctionsToModule(res.functions);
    return res.e;
}

// Produce a multi-valued Boolean expression
MuExp makeMultiValuedBoolExp(str operator, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeMultiValuedBoolExp(operator, topFunctionScope(),exps, src);
    addFunctionsToModule(res.functions);
    return res.e;
}

// Produce a single-valued Boolean expression
MuExp makeSingleValuedBoolExp(str operator, list[MuExp] exps, loc src) {
    tuple[MuExp e,list[MuFunction] functions] res = makeSingleValuedBoolExp(operator,topFunctionScope(),exps,src);
    addFunctionsToModule(res.functions);
    return res.e;
}

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
  if(lot == "value" || rot == "value" || lot == "parameter" || rot == "parameter"){
     return op;
  }
  if(isContainerType(lot))
     return areCompatibleContainerTypes({lot, rot}) ? "<lot>_<op>_<rot>" : "<lot>_<op>_elm";
  else
     return isContainerType(rot) ? "elm_<op>_<rot>" : "<lot>_<op>_<rot>";
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

// ----------- compose: exp o exp ----------------

private MuExp compose(Expression e){
  lhsType = getType(e.lhs@\loc);
  return isFunctionType(lhsType) || isOverloadedType(lhsType) ? translateComposeFunction(e) : infix_rel_lrel("compose", e);
}

private MuExp translateComposeFunction(Expression e){
  //println("composeFunction: <e>");
  lhsType = getType(e.lhs@\loc);
  rhsType = getType(e.rhs@\loc);
  resType = getType(e@\loc);
  
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
  
  Symbol comp_ftype;  
  int nargs;
  if(isFunctionType(resType)) {
     nargs = size(resType.parameters);
     comp_ftype = resType;
  } else {
     nargs = size(getOneFrom(resType.overloads).parameters);
     for(t <- resType.overloads){
         if(size(t.parameters) != nargs){
            throw "cannot handle composition/overloading for different arities";
         }
     }
     comp_ftype = Symbol::func(Symbol::\value(), [Symbol::\value() | int j <- [0 .. nargs]]);
  }
    
  enterFunctionScope(comp_fuid);
  kwargs = muCallMuPrim("make_mmap", []);
  rhsCall = muOCall4(rhsReceiver, \tuple([rhsType]), [muVar("parameter_<comp_name>", comp_fuid, j) | int j <- [0 .. nargs]] + [ kwargs ], e.rhs@\loc);
  body_exps =  [muReturn1(muOCall4(lhsReceiver, \tuple([lhsType]), [rhsCall, kwargs ], e.lhs@\loc))];
   
  leaveFunctionScope();
  fun = muFunction(comp_fuid, comp_name, comp_ftype, scopeId, nargs, 2, false, false, \e@\loc, [], (), muBlock(body_exps));
 
  int uid = declareGeneratedFunction(comp_fuid, comp_ftype);
  addFunctionToModule(fun);  
  addOverloadedFunctionAndResolver(ofqname, <comp_name, comp_ftype, getModuleName(), [uid]>);
 
  return muOFun(ofqname);
}

// ----------- addition: exp + exp ----------------

private  MuExp add(Expression e){
    lhsType = getType(e.lhs@\loc);
    return isFunctionType(lhsType) || isOverloadedType(lhsType) ? translateAddFunction(e) :infix("add", e);
}

private MuExp translateAddFunction(Expression e){
  //println("translateAddFunction: <e>");
  lhsType = getType(e.lhs@\loc);
  rhsType = getType(e.rhs@\loc);
  
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
  
  if(lot == "value" || rot == "value"){
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
                    
private MuExp translateStringLiteral((StringLiteral)`<StringConstant constant>`) = muCon(readTextValueString(removeMargins("<constant>")));

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
		return visit(s) { case /^[ \t]*'/m => "" /* case /^[ \t]+$/m => "" */};
	}
}

// --- computeIndent 

private str computeIndent(str s) {
   lines = split("\n", removeMargins(s)); 
   return isEmpty(lines) ? "" : left("", size(lines[-1]));
} 

private str computeIndent(PreStringChars pre) = computeIndent(removeMargins(deescape("<pre>"[1..-1])));
private str computeIndent(MidStringChars mid) = computeIndent(removeMargins(deescape("<mid>"[1..-1])));

private list[MuExp] translatePreChars(PreStringChars pre) {
   spre = removeMargins(deescape("<pre>"[1..-1]));
   return "<spre>" == "" ? [] : [ muCon(spre) ];
}	

private list[MuExp] translateMidChars(MidStringChars mid) {
  smid = removeMargins(deescape("<mid>"[1..-1]));
  return "<mid>" == "" ? [] : [ muCallPrim3("template_add", [ muCon(smid) ], mid@\loc) ];
}

str deescape(str s)  =  visit(s) { case /\\<c: [\" \' \< \> \\ b f n r t]>/m => c };

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
	mids = removeMargins(deescape("<mid>"[1..-1]));
	return mids == "" ? [] : [ muCallPrim3("template_add", [muCon(mids)], mid@\loc) ];
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
  content = removeMargins(deescape("<post>"[1..-1]));
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

default MuExp translate((Literal) `<Literal s>`) = 
    muCon(readTextValueString("<s>"));


/*********************************************************************/
/*                  Translate expressions                            */
/*********************************************************************/

// -- literal expression ---------------------------------------------

MuExp translate(e:(Expression)  `<Literal s>`) = 
    translate(s);

// -- concrete syntax expression  ------------------------------------

MuExp translate(e:(Expression) `<Concrete concrete>`) {
    return translateConcrete(concrete);
}

//public MuExp getConstructor(str cons) {
//   cons = unescape(cons);
//   uid = -1;
//   for(c <- getConstructors()){
//     //println("c = <c>, uid2name = <uid2name[c]>, uid2str = <convert2fuid(c)>");
//     if(cons == getSimpleName(getConfiguration().store[c].name)){
//        //println("c = <c>, <config.store[c]>,  <uid2addr[c]>");
//        uid = c;
//        break;
//     }
//   }
//   if(uid < 0)
//      throw("No definition for constructor: <cons>");
//   return muConstr(convert2fuid(uid));
//}

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
	fragType = getType(e@\loc);
    //println("translateConcrete, fragType = <fragType>");
    reifiedFragType = symbolToValue(fragType);
    return parseFragment(getModuleName(), reifiedFragType, e, e@\loc, getGrammar());
} 

public MuExp translateConcrete(e: appl(Production cprod, list[Tree] cargs)){ 
    //fragType = getType(e@\loc);
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
           translated_elems = muBlock([ muAssignTmp(writer, fuid, muCallPrim3("listwriter_open", [], my_src)),
                                        *translated_args,
                                        muCallPrim3("listwriter_close", [muTmp(writer,fuid)], my_src) 
                                      ]);
        } else {
           translated_args = [translateConcreteParsed(arg, my_src) | Tree arg <- args];
           if(allConstant(translated_args)){
        	  return muCon(appl(prod, [ce | muCon(ce) <- translated_args])[@\loc=my_src]);
           }
           translated_elems = muCallPrim3("list_create", translated_args, my_src);
        }
        return muCallPrim3("annotation_set", [muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
                                                    [muCon(prod), translated_elems, muTypeCon(Symbol::\void())]),
        								     muCon("loc"), 
        								     muCon(my_src)], e@\loc);
        //return muCall(muConstr("ParseTree/adt(\"Tree\",[])::appl(adt(\"Production\",[]) prod;list(adt(\"Tree\",[])) args;)"), 
        //              [muCon(prod), translated_elems, muTypeCon(Symbol::\void())]);
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
    muBlock([translate(stat) | stat <- statements]);

// -- parenthesized expression --------------------------------------

MuExp translate(e:(Expression) `(<Expression expression>)`) =
     translate(expression);

// -- closure expression --------------------------------------------

MuExp translate (e:(Expression) `<Type \type> <Parameters parameters> { <Statement+ statements> }`) =
    translateClosure(e, parameters, statements);

MuExp translate (e:(Expression) `<Parameters parameters> { <Statement* statements> }`) =
    translateClosure(e, parameters, statements);

// Translate a closure   
 
 private MuExp translateClosure(Expression e, Parameters parameters, Tree cbody) {
 	uid = loc2uid[e@\loc];
	fuid = convert2fuid(uid);
	
	enterFunctionScope(fuid);
	
    ftype = getClosureType(e@\loc);
	nformals = size(ftype.parameters);
	bool isVarArgs = (varArgs(_,_) := parameters);
  	
  	// Keyword parameters
    list[MuExp] kwps = translateKeywordParameters(parameters, fuid, getFormals(uid), e@\loc);
    
    // TODO: we plan to introduce keyword patterns as formal parameters
    MuExp body = translateFunction("CLOSURE", parameters.formals.formals, isVarArgs, kwps, cbody, []);
    
    tuple[str fuid,int pos] addr = uid2addr[uid];
    
    addFunctionToModule(muFunction(fuid, "CLOSURE", ftype, (addr.fuid in moduleNames) ? "" : addr.fuid, 
  									  getFormals(uid), getScopeSize(fuid), 
  									  isVarArgs, false, e@\loc, [], (), 
  									  body));
  	
  	leaveFunctionScope();								  
  	
	return (addr.fuid == convert2fuid(0)) ? muFun1(fuid) : muFun2(fuid, addr.fuid); // closures are not overloaded
}

private MuExp translateBoolClosure(Expression e){
    tuple[str fuid,int pos] addr = <topFunctionScope(),-1>;
	fuid = addr.fuid + "/non_gen_at_<e@\loc>()";
	
	enterFunctionScope(fuid);
	
    ftype = Symbol::func(Symbol::\bool(),[]);
	nformals = 0;
	nlocals = 0;
	bool isVarArgs = false;
  	
    MuExp body = muReturn1(translate(e));
    addFunctionToModule(muFunction(fuid, "CLOSURE", ftype, addr.fuid, nformals, nlocals, isVarArgs, false, false, e@\loc, [], (), body));
  	
  	leaveFunctionScope();								  
  	
	return muFun2(fuid, addr.fuid); // closures are not overloaded

}

// -- enumerator with range expression ------------------------------

MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> .. <Expression last> ]`) {
    kind = getOuterType(first) == "int" && getOuterType(last) == "int" ? "_INT" : "";
    return muMulti(muApply(mkCallToLibFun("Library", "RANGE<kind>"), [ translatePat(pat), translate(first), translate(last)]));
 }

// -- enumerator with range and step expression ---------------------
    
MuExp translate (e:(Expression) `<Pattern pat> \<- [ <Expression first> , <Expression second> .. <Expression last> ]`) {
     kind = getOuterType(first) == "int" && getOuterType(second) == "int" && getOuterType(last) == "int" ? "_INT" : "";
     return muMulti(muApply(mkCallToLibFun("Library", "RANGE_STEP<kind>"), [ translatePat(pat), translate(first), translate(second), translate(last)]));
}

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
    muBlock(
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
    muBlock(
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
private int iDescDescriptorPos = 6;

private int NumberOfPhiFormals = 7;

// Generated PHI_FIXPOINT functions
// iSubjectPos, matchedPos, hasInsert, begin, end, descriptor (as for PHI)
// Extra locals

private int changedPos = 7;
private int valPos = 8;

private int NumberOfPhiFixFormals = 7;
private int NumberOfPhiFixLocals = 9;


public MuExp translateVisit(Label label, lang::rascal::\syntax::Rascal::Visit \visit) {
	MuExp traverse_fun;
	bool fixpoint = false;
	
	if(\visit is defaultStrategy) {
		traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP");
	} else {
		switch("<\visit.strategy>") {
			case "bottom-up"      :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP");
			case "top-down"       :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN");
			case "bottom-up-break":   traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP_BREAK");
			case "top-down-break" :   traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN_BREAK");
			case "innermost"      : { traverse_fun = mkCallToLibFun("Library","TRAVERSE_BOTTOM_UP"); fixpoint = true; }
			case "outermost"      : { traverse_fun = mkCallToLibFun("Library","TRAVERSE_TOP_DOWN"); fixpoint = true; }
		}
	}
	
	bool rebuild = false;
	if( Case c <- \visit.cases, (c is patternWithAction && c.patternWithAction is replacing 
									|| hasTopLevelInsert(c)) ) {
		rebuild = true;
	}
	
	// Unique 'id' of a visit in the function body
	int i = nextVisit();
	
	previously_declared_functions = size(getFunctionsInModule());
	
	// Generate and add a nested function 'phi'
	str scopeId = topFunctionScope();
	str phi_fuid = scopeId + "/" + "PHI_<i>";
	Symbol phi_ftype = Symbol::func(Symbol::\value(), [Symbol::\value(),				// iSubject
	                                                   Symbol::\bool(),					// matched
	                                                   Symbol::\bool(),					// hasInsert
	                                                   Symbol::\bool(),					// leaveVisit
	                                                   Symbol::\int(),					// begin
	                                                   Symbol::\int(),					// end
	                                                   Symbol::\list(Symbol::\value())	// iDescDescriptor
	                                                  ]);
	
	enterVisit();
	enterFunctionScope(phi_fuid);
	cases = [ c | Case c <- \visit.cases ];
	
	concreteMatch = hasConcretePatternsOnly(cases);
	MuExp body = translateVisitCases(phi_fuid, getType(\visit.subject@\loc), concreteMatch, cases);
	
	tc = getTypesAndConstructorsInVisit(cases);
	reachable = getReachableTypes(getType(\visit.subject@\loc), tc.constructors, tc.types, concreteMatch);
	println("reachableTypesInVisit: <reachable>");
	
	descriptor = muCallMuPrim("make_descendant_descriptor", [muCon(phi_fuid), muCon(reachable), muCon(concreteMatch), muCon(getDefinitions())]);
	
	
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
	
	// Starting from the number of formal parameters (iSubject, matched, hasInsert, begin, end, descriptor)
	int pos_in_phi = NumberOfPhiFormals;
	
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
	
	addFunctionToModule(muFunction(phi_fuid, "PHI", phi_ftype, scopeId, NumberOfPhiFormals, pos_in_phi, false, false, \visit@\loc, [], (), body));
	
	leaveFunctionScope();
	leaveVisit();
	
	if(fixpoint) {
		str phi_fixpoint_fuid = scopeId + "/" + "PHI_FIXPOINT_<i>";
		
		enterFunctionScope(phi_fixpoint_fuid);
		
		// Local variables of 'phi_fixpoint_fuid': iSubject, matched, hasInsert, leaveVisit, begin, end, iReachableTypes, concreteMatch, changed, val
		list[MuExp] body_exps = [];
		body_exps += muAssign("changed", phi_fixpoint_fuid, changedPos, muBool(true));
		body_exps += muAssignVarDeref("leaveVisit", phi_fixpoint_fuid, leaveVisitPos, muBool(false));
		body_exps += muWhile(nextLabel(), muVar("changed", phi_fixpoint_fuid, changedPos), 
						[ muAssign("val", phi_fixpoint_fuid, valPos, 
						                  muCall(muFun2(phi_fuid,scopeId), [ muVar("iSubject", phi_fixpoint_fuid, iSubjectPos), 
						                                                     muVar("matched", phi_fixpoint_fuid, matchedPos), 
						                                                     muVar("hasInsert", phi_fixpoint_fuid, hasInsertPos),
						                                                     muVar("leaveVisit", phi_fixpoint_fuid, leaveVisitPos),
						                                                     muVar("begin", phi_fixpoint_fuid, beginPos),	
						                                                     muVar("end", phi_fixpoint_fuid, endPos),
						                                                     descriptor			                                                                                   
						                                                   ])),
						  muIfelse(nextLabel(), muVarDeref("leaveVisit", phi_fixpoint_fuid, leaveVisitPos), 
						                        [ muReturn1(muVar("val", phi_fixpoint_fuid, valPos)) ],
						                        [ muIfelse(nextLabel(), makeBoolExp("ALL", [ muCallPrim3("equal", 
						                        													[ muVar("val", phi_fixpoint_fuid, valPos), 
						                                                                              muVar("iSubject", phi_fixpoint_fuid, iSubjectPos)                                                                          
						                                                                            ], 
						                                                                            \visit@\loc)
						                                                              ], \visit@\loc ),
						  						     					[ muAssign("changed", phi_fixpoint_fuid,changedPos, muBool(false)) ], 
						  						     					[ muAssign("iSubject", phi_fixpoint_fuid, iSubjectPos, muVar("val", phi_fixpoint_fuid, valPos)) ] )])]);
		body_exps += muReturn1(muVar("iSubject", phi_fixpoint_fuid, iSubjectPos));
		
		leaveFunctionScope();
		
		addFunctionToModule(muFunction(phi_fixpoint_fuid, "PHI_FIXPOINT", phi_ftype, scopeId, NumberOfPhiFixFormals, NumberOfPhiFixLocals, false, false, \visit@\loc, [], (), muBlock(body_exps)));
	
		return traversalCall(traverse_fun, scopeId, phi_fixpoint_fuid, descriptor, translate(\visit.subject), rebuild);
	}
	
	return traversalCall(traverse_fun, scopeId, phi_fuid, descriptor, translate(\visit.subject), rebuild);
}


private MuExp traversalCall(MuExp traverse_fun, str scopeId, str phi_fuid, MuExp descriptor, MuExp subject, bool rebuild){
	// Local variables of the surrounding function
	str hasMatch = asTmp(nextLabel());
	str beenChanged = asTmp(nextLabel());
	str leaveVisit = asTmp(nextLabel());
	str begin = asTmp(nextLabel());
	str end = asTmp(nextLabel());
	str val = asTmp(nextLabel());
	return muBlock([ muAssignTmp(hasMatch,scopeId,muBool(false)), 
	                 muAssignTmp(beenChanged,scopeId,muBool(false)),
	                 muAssignTmp(leaveVisit,scopeId,muBool(false)),
	                 muAssignTmp(val, scopeId, muCall(traverse_fun, [ muFun2(phi_fuid,scopeId), 
					                        			subject, 
					                        			muTmpRef(hasMatch,scopeId), 
					                        			muTmpRef(beenChanged,scopeId), 
					                        			muTmpRef(leaveVisit,scopeId),
					                        			muTmpRef(begin,scopeId), 
					 	 								muTmpRef(end,scopeId),
					 	 								descriptor,
					                        			muBool(rebuild) ])),
					 muIfelse(nextLabel(), muTmp(leaveVisit, scopeId), 
						                   [ muReturn1(muTmp(val, scopeId)) ],
						                   [ muTmp(val, scopeId) ])
				   ]);
}
/*
 * The translated visit cases are placed in a function phi that is called by the traversal function.
 * Therefore, we have to distinguish two kinds of returns:
 * - a replacementReturn that returns a replacement value to the calling traversal function
 * - a leaveVisitReturn that should return to the calling traversal function and directly return from it.
 * This is implemented by wrapping all return values in a tuple of the form <isExitReturn, return value>
 * The isExitReturn has to be checked by the traversal function after each call to the phi function
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
						 case muInsert(e)  => muBlock([ muAssignVarDeref("hasInsert",fuid,hasInsertPos,muBool(true)), 
				  									    muReturn1(e) ])
	 					};
	return code;
}

private map[int, MuExp]  addPatternWithActionCode(str fuid, Symbol subjectType, PatternWithAction pwa, map[int, MuExp] table, int key){
	cond = muMulti(muApply(translatePatInVisit(pwa.pattern, fuid, subjectType), [ muVar("iSubject", fuid, iSubjectPos) ]));
	ifname = nextLabel();
	enterBacktrackingScope(ifname);
	if(pwa is replacing) {
		replacement = translate(pwa.replacement.replacementExpression);
		list[MuExp] conditions = [];
		if(pwa.replacement is conditional) {
			conditions = [ translate(e) | Expression e <- pwa.replacement.conditions ];
		}
		replacementType = getType(pwa.replacement.replacementExpression@\loc);
		tcond = muCallPrim3("subtype", [ muTypeCon(replacementType), 
		                                 muCallPrim3("typeOf", [ muVar("iSubject", fuid, iSubjectPos) ], pwa@\loc) ], pwa@\loc);
		list[MuExp] cbody = [ muAssignVarDeref("matched", fuid, matchedPos, muBool(true)), 
		                      muAssignVarDeref("hasInsert", fuid, hasInsertPos, muBool(true)), 
		                      replacement ];
    	table[key] = muIfelse(ifname, makeBoolExp("ALL",[ cond,tcond,*conditions ], pwa.pattern@\loc), 
    				          [ replacementReturn(muBlock(cbody)) ], 
    				          [ table[key] ? replacementReturn(muVar("iSubject", fuid, iSubjectPos)) ]);
    	leaveBacktrackingScope();
	} else {
		// Arbitrary
		case_statement = pwa.statement;
		\case = translateStatementInVisitCase(fuid, case_statement);
		insertType = topCaseType();
		clearCaseType();
		tcond = muCallPrim3("subtype", [ muTypeCon(insertType), muCallPrim3("typeOf", [ muVar("iSubject",fuid,iSubjectPos) ], pwa@\loc) ], pwa@\loc);
		list[MuExp] cbody = [ muAssignVarDeref("matched", fuid, matchedPos, muBool(true)) ];
		if(!(muBlock([]) := \case)) {
			cbody += \case;
		}
		cbody += replacementReturn(muVar("iSubject", fuid, iSubjectPos));
		table[key] = muIfelse(ifname, makeBoolExp("ALL",[ cond,tcond ], pwa.pattern@\loc), cbody, 
		                      [ table[key] ? replacementReturn(muVar("iSubject", fuid, iSubjectPos)) ]);
    	leaveBacktrackingScope();
	}
	return table;
}

private int fingerprintDefault = getFingerprint("default", false);

@doc{Generates the body of a phi function}
private MuExp translateVisitCases(str fuid, Symbol subjectType, bool useConcreteFingerprint, list[Case] cases) {
	// TODO: conditional
	
	map[int,MuExp] table = ();		// label + generated code per case
	
	default_code = replacementReturn(muVar("iSubject", fuid, iSubjectPos));
	
	for(c <- reverse(cases)){
		if(c is patternWithAction) {
		  if(!isSpoiler(c.patternWithAction.pattern)){
			 pwa = c.patternWithAction;
			 key = fingerprint(pwa.pattern, useConcreteFingerprint);
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
    for(c <- reverse(cases), c is patternWithAction, isSpoiler(c.patternWithAction.pattern)){
	  default_table = addPatternWithActionCode(fuid, subjectType, c.patternWithAction, default_table, fingerprintDefault);
   }
   
   //println("TABLE DOMAIN(<size(table)>): <domain(table)>");
   case_code = [ muCase(key, table[key]) | key <- table];
   default_code =  default_table[fingerprintDefault];
   return muSwitch(muVar("iSubject", fuid, iSubjectPos), useConcreteFingerprint, case_code, default_code, muVar("iSubject", fuid, iSubjectPos));
	
}

private tuple[set[Symbol] types, set[str] constructors] getTypesAndConstructorsInVisit(list[Case] cases){
	reachableTypes = {};
	reachableConstructors = {};
	for(c <- cases){
		if(c is patternWithAction){
			tc = getTypesAndConstructors(c.patternWithAction.pattern);
			reachableConstructors += tc.constructors;
			reachableTypes += tc.types;
		} else {
			return <{Symbol::\value()}, {}>;		// A default cases is present: everything can match
		}
	}
	return <reachableTypes, reachableConstructors>;
}

public bool hasConcretePatternsOnly(list[Case] cases){
	for(c <- cases){
		if(c is patternWithAction){
			if(!isConcretePattern((c.patternWithAction.pattern))){
				return false;
			}
		} else {
			;//return false;		// A default case is present: everything can match
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

private MuExp translatePatInVisit(Pattern pattern, str fuid, Symbol subjectType){
   if(subjectType == \str()){
      switch(pattern){
        case p:(Pattern) `<RegExpLiteral r>`: return translateRegExpLiteral(r, muVar("begin", fuid, beginPos), muVar("end", fuid, endPos));
        
      	case p:(Pattern) `<Literal lit>`: 
      		return muApply(mkCallToLibFun("Library","MATCH_SUBSTRING"), [translate(lit), muVar("begin", fuid, beginPos), muVar("end", fuid, endPos)]);
      	default: return translatePat(pattern);
      }
   }
   return translatePat(pattern);
}

// -- reducer expression --------------------------------------------

MuExp translate (e:(Expression) `( <Expression init> | <Expression result> | <{Expression ","}+ generators> )`) = translateReducer(e); //translateReducer(init, result, generators);

private MuExp translateReducer(Expression e){ //Expression init, Expression result, {Expression ","}+ generators){
    Expression init = e.init;
    Expression result = e.result;
    {Expression ","}+  generators = e.generators;
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    pushIt(tmp,fuid);
    code = [ muAssignTmp(tmp, fuid, translate(init)), muWhile(loopname, makeMultiValuedBoolExp("ALL", [ translate(g) | g <- generators ], e@\loc), [muAssignTmp(tmp,fuid,translate(result))]), muTmp(tmp,fuid)];
    popIt();
    return muBlock(code);
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `type ( <Expression symbol> , <Expression definitions >)`) {
	
    return muCallPrim3("reifiedType_create", [translate(symbol), translate(definitions)], e@\loc);
    
}

// -- call expression -----------------------------------------------

MuExp translate(e:(Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`){

   //println("translate: <e>");
   MuExp kwargs = translateKeywordArguments(keywordArguments);
      
   MuExp receiver = translate(expression);
   //println("receiver: <receiver>");
   list[MuExp] args = [ translate(a) | a <- arguments ];
   
   //println("BACK at translate <e>");
   
   if(getOuterType(expression) == "str"){
   		return muCallPrim3("node_create", [receiver, *args, *kwargs], e@\loc);
       //return muCallPrim3("node_create", [receiver, *args] + (size_keywordArguments(keywordArguments) > 0 ? [kwargs] : [/* muCon(()) */]), e@\loc);
   }
  
   if(getOuterType(expression) == "loc"){
       return muCallPrim3("loc_with_offset_create", [receiver, *args], e@\loc);
   }
   if(muFun1(str _) := receiver || muFun2(str _, str _) := receiver || muConstr(str _) := receiver) {
       return muCall(receiver, args + [ kwargs ]);
   }
   
   // Now overloading resolution...
   ftype = getType(expression@\loc); // Get the type of a receiver
                                     // and a version with type parameters uniquely renamed
   ftype_renamed = visit(ftype) { case parameter(str name, Symbol sym) => parameter("1" + name, sym) };
   
   if(isOverloadedFunction(receiver) && hasOverloadingResolver(receiver.fuid)){
       // Get the types of arguments
       list[Symbol] targs = [ getType(arg@\loc) | arg <- arguments ];
       // Generate a unique name for an overloaded function resolved for this specific use 
       str ofqname = receiver.fuid + "(<for(targ<-targs){><targ>;<}>)#<e@\loc.offset>_<e@\loc.length>";
       // Resolve alternatives for this specific call
       OFUN of = getOverloadedFunction(receiver.fuid);
       
       list[int] resolved = [];
       
       bool isVarArgs(Symbol ftype) = ftype@isVarArgs? ? ftype@isVarArgs : false;
       
       // match function use and def, taking varargs into account
       bool function_subtype(Symbol fuse, Symbol fdef){
       	list[Symbol] upar = fuse.parameters;
       	list[Symbol] dpar = fdef.parameters;
       	if(isVarArgs(fdef) && !isVarArgs(fuse)){
       		un = size(upar);
       		dn = size(dpar);
       		var_elm_type = dpar[-1][0];
       		i = un - 1;
       		while(i > 0){
       			if(subtype(upar[i], var_elm_type)){
       				i -= 1;
       			} else {
       				break;
       			}
       		}
       		upar = upar[0 .. i + 1] + dpar[-1];
       	}
       
       	return subtype(upar, dpar);
       }
       
       bool matches(Symbol t) {
       	   //println("matches: <ftype>, <t>");
           if(isFunctionType(ftype) || isConstructorType(ftype)) {
               if(/parameter(_,_) := t) { // In case of polymorphic function types
                   ftype_selected = ftype;
                   //common = {name | /parameter(str name, _) := t} & {name | /parameter(str name, _) := ftype};
                   //if(!isEmpty(common)){
                   //println("USING RENAMED");
                   // ftype_selected = ftype_renamed;
                   //}
                   try {
                       if(isConstructorType(t) && isConstructorType(ftype_selected)) {
                           bindings = match(\tuple([ a | Symbol arg <- getConstructorArgumentTypes(t),     label(_,Symbol a) := arg || Symbol a := arg ]),
                                            \tuple([ a | Symbol arg <- getConstructorArgumentTypes(ftype_selected), label(_,Symbol a) := arg || Symbol a := arg ]),());
                           bindings = bindings + ( name : Symbol::\void() | /parameter(str name,_) := t, name notin bindings );
                           return instantiate(t.\adt,bindings) == ftype_selected.\adt;
                       }
                       if(isFunctionType(t) && isFunctionType(ftype_selected)) {
                           //println("t:              <t>");
                           //println("ftype_selected: <ftype_selected>");
                 
                           bindings = match(getFunctionArgumentTypesAsTuple(t),getFunctionArgumentTypesAsTuple(ftype_selected),());
                           
                           //println("bindings1: <bindings>");
                           
                           bindings = bindings + ( name : Symbol::\void() | /parameter(str name,_) := t, name notin bindings );
                           
                           //println("bindings2: <bindings>");
                           //println("t.ret:               <t.ret> becomes <instantiate(t.ret,bindings)>");
                           //println("ftype_selected.ret:  <ftype_selected.ret> becomes <instantiate(ftype_selected.ret,bindings)>");
                           
                           bool res =  instantiate(t.ret,bindings) == ftype_selected.ret;
                           
                           //println("res: <res>");
                           
                           return res;
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
               //println("matches returns: <function_subtype(ftype, t)>");
               //return t == ftype;
               return function_subtype(ftype, t);
           }           
           if(isOverloadedType(ftype)) {
               if(/parameter(_,_) := t) { // In case of polymorphic function types
                   for(Symbol alt <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype))) {
                       try {
           	               if(isConstructorType(t) && isConstructorType(alt)) {
           	                   bindings = match(\tuple([ a | Symbol arg <- getConstructorArgumentTypes(t),   label(_,Symbol a) := arg || Symbol a := arg ]),
           	                                    \tuple([ a | Symbol arg <- getConstructorArgumentTypes(alt), label(_,Symbol a) := arg || Symbol a := arg ]),());
           	                   bindings = bindings + ( name : Symbol::\void() | /parameter(str name,_) := t, name notin bindings );
           	                   return instantiate(t.\adt,bindings) == alt.\adt;
           	               }
           	               if(isFunctionType(t) && isFunctionType(alt)) {
           	                   bindings = match(getFunctionArgumentTypesAsTuple(t),getFunctionArgumentTypesAsTuple(alt),());
           	                   bindings = bindings + ( name : Symbol::\void() | /parameter(str name,_) := t, name notin bindings );
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
               //return t in (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype));
               return any(Symbol sup <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype)), subtype(t.parameters, sup.parameters)); // TODO function_subtype
           }
           throw "Ups, unexpected type of the call receiver expression!";
       }
       
     
       //println("ftype = <ftype>, of.alts = <of.alts>");
       for(int alt <- of.alts) {
       	   assert uid2type[alt]? : "cannot find type of alt";
           t = uid2type[alt];
           if(matches(t)) {
           	   //println("alt <alt> matches");
               resolved += alt;
           }
       }
       //println("resolved = <resolved>");
       if(isEmpty(resolved)) {
           for(int alt <- of.alts) {
               t = uid2type[alt];
               matches(t);
               println("ALT: <t> ftype: <ftype>");
           }
           throw "ERROR in overloading resolution: <ftype>; <expression@\loc>";
       }
       
       if(size(resolved) == 1 && (isEmpty(args) || all(muCon(_) <- args))){
       		name = unescape("<expression>");
       		try {
       			res = translateConstantCall(name, args);
       			//println("REPLACED <name>, <args> BY CONSTANT");
       			return res;
       		} 
       		catch "NotConstant":  /* pass */;
       }
       	addOverloadedFunctionAndResolver(ofqname, <of.name, ftype, of.fuid,resolved>);      
       	return muOCall3(muOFun(ofqname), args + [ kwargs ], e@\loc);
   }
   if(isOverloadedFunction(receiver) && !hasOverloadingResolver(receiver.fuid)) {
      throw "The use of a function has to be managed via an overloading resolver!";
   }
   // Push down additional information if the overloading resolution needs to be done at runtime
   return muOCall4(receiver, 
   				  isFunctionType(ftype) ? Symbol::\tuple([ ftype ]) : Symbol::\tuple([ t | Symbol t <- (getNonDefaultOverloadOptions(ftype) + getDefaultOverloadOptions(ftype)) ]), 
   				  args + [ kwargs ],
   				  e@\loc);
}

private MuExp translateKeywordArguments((KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArguments>`) {
   // Keyword arguments
   if(keywordArguments is \default){
      kwargs = [ muCon(unescape("<kwarg.name>")), translate(kwarg.expression)  | /*KeywordArgument[Expression]*/ kwarg <- keywordArguments.keywordArgumentList ];
      if(size(kwargs) > 0){
         return muCallMuPrim("make_mmap", kwargs);
      }
   }
   return muCallMuPrim("make_mmap", []);
   
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

MuExp translate (e:(Expression) `any ( <{Expression ","}+ generators> )`) = makeSingleValuedBoolExp("ALL",[ translate(g) | g <- generators ], e@\loc);

// -- all expression ------------------------------------------------

MuExp translate (e:(Expression) `all ( <{Expression ","}+ generators> )`) = makeBoolExp("RASCAL_ALL",[ translate(g) | g <- generators ], e@\loc);

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
    loopname = nextLabel(); 
    tmp = asTmp(loopname);
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim3("listwriter_open", [], c@\loc)),
      muWhile(loopname, makeMultiValuedBoolExp("ALL",[ translate(g) | g <- generators ], c@\loc), translateComprehensionContribution("list", tmp, fuid, [r | r <- results])),
      muCallPrim3("listwriter_close", [muTmp(tmp,fuid)], c@\loc) 
    ]);
}

private MuExp translateComprehension(c: (Comprehension) `{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`) {
    //println("translateComprehension (set): <generators>");
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim3("setwriter_open", [], c@\loc)),
      muWhile(loopname, makeMultiValuedBoolExp("ALL",[ translate(g) | g <- generators ], c@\loc), translateComprehensionContribution("set", tmp, fuid, [r | r <- results])),
      muCallPrim3("setwriter_close", [muTmp(tmp,fuid)], c@\loc) 
    ]);
}

private MuExp translateComprehension(c: (Comprehension) `(<Expression from> : <Expression to> | <{Expression ","}+ generators> )`) {
    //println("translateComprehension (map): <generators>");
    str fuid = topFunctionScope();
    loopname = nextLabel(); 
    tmp = asTmp(loopname); 
    return
    muBlock(
    [ muAssignTmp(tmp, fuid, muCallPrim3("mapwriter_open", [], c@\loc)),
      muWhile(loopname, makeMultiValuedBoolExp("ALL",[ translate(g) | g <- generators ], c@\loc), [muCallPrim3("mapwriter_add", [muTmp(tmp,fuid)] + [ translate(from), translate(to)], c@\loc)]), 
      muCallPrim3("mapwriter_close", [muTmp(tmp,fuid)], c@\loc) 
    ]);
}

// -- set expression ------------------------------------------------

MuExp translate(Expression e:(Expression)`{ <{Expression ","}* es> }`) =
    translateSetOrList(e, es, "set");

// -- list expression -----------------------------------------------

MuExp translate(Expression e:(Expression)`[ <{Expression ","}* es> ]`) = 
    translateSetOrList(e, es, "list");

// Translate SetOrList including spliced elements

private bool containSplices({Expression ","}* es) =
    any(Expression e <- es, e is splice);

private MuExp translateSetOrList(Expression e, {Expression ","}* es, str kind){
 if(containSplices(es)){
       str fuid = topFunctionScope();
       writer = nextTmp();
       enterWriter(writer);
       code = [ muAssignTmp(writer, fuid, muCallPrim3("<kind>writer_open", [], e@\loc)) ];
       for(elem <- es){
           if(elem is splice){
              code += muCallPrim3("<kind>writer_splice", [muTmp(writer,fuid), translate(elem.argument)], elem.argument@\loc);
            } else {
              code += muCallPrim3("<kind>writer_add", [muTmp(writer,fuid), translate(elem)], elem@\loc);
           }
       }
       code += [ muCallPrim3("<kind>writer_close", [ muTmp(writer,fuid) ], e@\loc) ];
       leaveWriter();
       return muBlock(code);
    } else {
      //if(size(es) == 0 || all(elm <- es, isConstant(elm))){
      //   return kind == "list" ? muCon([getConstantValue(elm) | elm <- es]) : muCon({getConstantValue(elm) | elm <- es});
      //} else 
        return muCallPrim3("<kind>_create", [ translate(elem) | elem <- es ], e@\loc);
    }
}

// -- reified type expression ---------------------------------------

MuExp translate (e:(Expression) `# <Type tp>`) =
	muCon(symbolToValue(translateType(tp)));

// -- tuple expression ----------------------------------------------

MuExp translate (e:(Expression) `\< <{Expression ","}+ elements> \>`) {
    //if(isConstant(e)){
    //  return muCon(readTextValueString("<e>"));
    //} else
        return muCallPrim3("tuple_create", [ translate(elem) | elem <- elements ], e@\loc);
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
 
MuExp translate(q:(QualifiedName) `<QualifiedName v>`) =
    mkVar("<v>", v@\loc);

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
    if(ot in {"sort", "iter", "iter-star", "iter-seps", "iter-star-seps"}){
       op = "nonterminal_subscript_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    } else
    if(ot notin {"map", "rel", "lrel"}) {
       op += "_<intercalate("-", [getOuterType(s) | s <- subscripts])>";
    }
    if(isDefined){
    	op = "is_defined_<op>";
    	return muCallMuPrim("subscript_array_int", [ muCallPrim3(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts], e@\loc), muCon(0)]);
    }
    return muCallPrim3(op, translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts], e@\loc);
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
    return muBlock([
    		       muAssignTmp(varname, fuid, muCallPrim3("is_defined_<op>", translate(exp) + ["<s>" == "_" ? muCon("_") : translate(s) | s <- subscripts], lhs@\loc)),
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

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, OptionalExpression optLast) =
    muCallPrim3("<getOuterType(expression)>_slice", [ translate(expression), translateOpt(optFirst), muCon("false"), translateOpt(optLast) ], expression@\loc);

public MuExp translateOpt(OptionalExpression optExp) =
    optExp is noExpression ? muCon("false") : translate(optExp.expression);

private MuExp translateSlice(Expression expression, OptionalExpression optFirst, Expression second, OptionalExpression optLast) =
    muCallPrim3("<getOuterType(expression)>_slice", [  translate(expression), translateOpt(optFirst), translate(second), translateOpt(optLast) ], expression@\loc);

// -- field access expression ---------------------------------------

MuExp translate (e:(Expression) `<Expression expression> . <Name field>`) {
   tp = getType(expression@\loc);
   if(isTupleType(tp) || isRelType(tp) || isListRelType(tp) || isMapType(tp)) {
       return translate((Expression)`<Expression expression> \< <Name field> \>`);
   }
   op = isNonTerminalType(tp) ? "nonterminal" : getOuterType(expression);
   //if(op == "label") println("field_access: <tp>, <e>");
   return muCallPrim3("<op>_field_access", [ translate(expression), muCon(unescape("<field>")) ], e@\loc);
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
        return muCallPrim3("adt_field_update", [ translate(expression), muCon(unescape("<key>")), translate(replacement) ], e@\loc);
    } else if(isLocType(tp)){
     	return muCallPrim3("loc_field_update", [ translate(expression), muCon(unescape("<key>")), translate(replacement) ], e@\loc);
    }
    if(tupleHasFieldNames(tp)){
    	  fieldNames = getTupleFieldNames(tp);
    }	
    return muCallPrim3("<getOuterType(expression)>_update", [ translate(expression), muCon(indexOf(fieldNames, "<key>")), translate(replacement) ], e@\loc);
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
    return muCallPrim3("<getOuterType(expression)>_field_project", [ translate(expression), *fcode], e@\loc);
}

// -- set annotation expression -------------------------------------



MuExp translate (e:(Expression) `<Expression expression> [ @ <Name name> = <Expression val> ]`) =
    muCallPrim3("annotation_set", [translate(expression), muCon(unescape("<name>")), translate(val)], e@\loc);

// -- get annotation expression -------------------------------------

MuExp translate (e:(Expression) `<Expression expression> @ <Name name>`) =
    muCallPrim3("annotation_get", [translate(expression), muCon(unescape("<name>"))], e@\loc);

// -- is expression --------------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> is <Name name>`) =
    muCallPrim3("is", [translate(expression), muCon(unescape("<name>"))], e@\loc);

// -- has expression -----------------------------------------------

MuExp translate (e:(Expression) `<Expression expression> has <Name name>`) {
	
    outer = getOuterType(expression);
    //println("<e>: <outer>, <getType(expression@\loc)>");
    str op = "";
    switch(getOuterType(expression)){
    	case "adt": 	op = "adt";
    	case "sort":	op = "nonterminal";
    	case "lex":		op = "nonterminal";
    	default:
     		return muCon(hasField(getType(expression@\loc), unescape("<name>")));		
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
// This is applied to the computation of subscripts and annotation that are part of an isDefined or isDefinedElse expression.
// The remaining cases are handled by a general scheme that selectively catches any generated exceptions.

MuExp translate(e:(Expression) `<Expression argument> ?`) =
	translateIsDefined(argument);

private MuExp translateIsDefined(Expression exp){
	switch(exp){
		case (Expression) `( <Expression exp1> )`:
			return translateIsDefined(exp1);
		case (Expression) `<Expression exp1> [ <{Expression ","}+ subscripts> ]`: 
			return translateSubscript(exp, true);
		case (Expression) `<Expression expression> @ <Name name>`:
    		return muCallMuPrim("subscript_array_int", [ muCallPrim3("is_defined_annotation_get", [translate(expression), muCon(unescape("<name>"))], exp@\loc), muCon(0)]);
		default:
    		return translateIfDefinedOtherwise(muBlock([ translate(exp), muCon(true) ]),  muCon(false), exp@\loc);
    }
}

// -- isDefinedOtherwise expression ---------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ? <Expression rhs>`) {
	switch(lhs){
		case (Expression) `<Expression exp1> [ <{Expression ","}+ subscripts> ]`: 
			return translateSubscriptIsDefinedElse(lhs, rhs);
		case (Expression) `<Expression expression> @ <Name name>`: {
			str fuid = topFunctionScope();
    		str varname = asTmp(nextLabel());
    		return muBlock([
    		       muAssignTmp(varname, fuid, muCallPrim3("is_defined_annotation_get", [translate(expression), muCon(unescape("<name>"))], e@\loc)),
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
	return muTry(muLHS, muCatch(varname, fuid, Symbol::\adt("RuntimeException",[]), catchBody), 
			  		 	muBlock([]));
}

// -- not expression ------------------------------------------------

MuExp translate(e:(Expression) `!<Expression argument>`) = 
    translateBool(e);

// -- negate expression ---------------------------------------------

MuExp translate(e:(Expression) `-<Expression argument>`) =
    prefix("negative", argument);

// -- splice expression ---------------------------------------------

MuExp translate(e:(Expression) `*<Expression argument>`) {
    throw "Splice cannot occur outside set or list";
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

MuExp translate(e:(Expression) `<Expression lhs> \<\< <Expression rhs>`) =
    infix("add", e);

// -- modulo expression ---------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> mod <Expression rhs>`) =
    infix("mod", e);

// -- notin expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> notin <Expression rhs>`) =
    infix_elm_left("notin", e);

// -- in expression -------------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> in <Expression rhs>`) =
    infix_elm_left("in", e);

// -- greater equal expression --------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \>= <Expression rhs>`) = 
    infix("greaterequal", e);

// -- less equal expression -----------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \<= <Expression rhs>`) = 
    infix("lessequal", e);

// -- less expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \< <Expression rhs>`) = 
    infix("less", e);

// -- greater expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> \> <Expression rhs>`) = 
    infix("greater", e);

// -- equal expression ----------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> == <Expression rhs>`) = 
    comparison("equal", e);

// -- not equal expression ------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> != <Expression rhs>`) = 
    comparison("notequal", e);


// -- no match expression -------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> !:= <Expression rhs>`) = 
    translateMatch(e);

// -- match expression ----------------------------------------------

MuExp translate(e:(Expression) `<Pattern pat> := <Expression exp>`) =
    translateMatch(e);

// -- enumerate expression ------------------------------------------

MuExp translate(e:(Expression) `<QualifiedName name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    return muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_AND_ASSIGN"), [muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Type tp> <Name name> \<- <Expression exp>`) {
    <fuid, pos> = getVariableScope("<name>", name@\loc);
    elemType = translateType(tp);
    generatorType = getType(exp@\loc);
    if(generatorType == \list(elemType) || generatorType == \set(elemType)){
       return muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_AND_ASSIGN"), [muVarRef("<name>", fuid, pos), translate(exp)]));
    }
    return muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_CHECK_AND_ASSIGN"), [muTypeCon(translateType(tp)), muVarRef("<name>", fuid, pos), translate(exp)]));
}

MuExp translate(e:(Expression) `<Pattern pat> \<- <Expression exp>`) =
    muMulti(muApply(mkCallToLibFun("Library", "ENUMERATE_AND_MATCH"), [translatePat(pat), translate(exp)]));

// -- implies expression --------------------------------------------

MuExp translate(e:(Expression) `<Expression lhs> ==\> <Expression rhs>`) =
    translateBool(e);

// -- equivalent expression -----------------------------------------
MuExp translate(e:(Expression) `<Expression lhs> \<==\> <Expression rhs>`) = 
    translateBool(e);

// -- and expression ------------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression lhs> && <Expression rhs>`) =
    translateBool(e);

// -- or expression -------------------------------------------------

MuExp translate(Expression e:(Expression) `<Expression lhs> || <Expression rhs>`) =
    translateBool(e);
 
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
    //case (Expression) `<Expression expression> ( <{Expression ","}* arguments> <KeywordArguments[Expression] keywordArguments>)`:
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

private MuExp translateBool(e: (Expression) `<Expression lhs> && <Expression rhs>`) = 
	makeBoolExp("ALL",[translate(lhs), translate(rhs)], e@\loc);

private MuExp translateBool(e: (Expression) `<Expression lhs> || <Expression rhs>`) = 
	makeBoolExp("OR",[translate(lhs), translate(rhs)], e@\loc);

private MuExp translateBool(e: (Expression) `<Expression lhs> ==\> <Expression rhs>`) =
	makeBoolExp("IMPLICATION",[ translate(lhs), translate(rhs) ], e@\loc);

private MuExp translateBool(e: (Expression) `<Expression lhs> \<==\> <Expression rhs>`) = 
	makeBoolExp("EQUIVALENCE",[ translate(lhs), translate(rhs) ], e@\loc);

private MuExp translateBool((Expression) `! <Expression lhs>`) =
	backtrackFree(lhs) ? muCallMuPrim("not_mbool", [translateBool(lhs)])
  					   : muCallMuPrim("not_mbool", [ makeBoolExp("ALL",[translate(lhs)], lhs@\loc) ]);
 
private MuExp translateBool(e: (Expression) `<Pattern pat> := <Expression exp>`)  = translateMatch(e);
   
private MuExp translateBool(e: (Expression) `<Pattern pat> !:= <Expression exp>`) = translateMatch(e);

// All other expressions are translated as ordinary expression

private default MuExp translateBool(Expression e) {
   return translate(e);
}