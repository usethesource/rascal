@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration

import IO;
import Map;
import List;
import Set;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;

import lang::rascalcore::compile::CompileTimeError;

import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;

import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::TypeReifier;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;

import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;
import lang::rascalcore::compile::Rascal2muRascal::RascalPattern;
import lang::rascalcore::compile::Rascal2muRascal::RascalStatement;


/********************************************************************/
/*                  Translate declarations in a module              */
/********************************************************************/
	
void translate((Toplevel) `<Declaration decl>`) = translate(decl);

// -- variable declaration ------------------------------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
	str module_name = getModuleName();
    ftype = afunc(avalue(),[avalue()], []);
    enterFunctionScope(getFUID(module_name,"#<module_name>_init",ftype,0));
   	for(var <- variables){
   		addVariableToModule(muModuleVar(getType(tp), "<var.name>"));
   		//variables_in_module += [];
   		if(var is initialized) 
   		addVariableInitializationToModule(mkAssign("<var.name>", var.name@\loc, translate(var.initial)));
   		//variable_initializations +=  mkAssign("<var.name>", var@\loc, translate(var.initial));
   	}
   	leaveFunctionScope();
}   	

// -- miscellaneous declarations that can be skipped since they are handled during type checking ------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType>@<Name name> ;`) { /*skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }

void translate(d : (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> ;`) { /* skip: translation has nothing to do here */ }

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) {
    for(variant <- variants){
       /*
        * Create getters for each keyword field
        */
       uid = variant@\loc;
       consType = getType(uid);
       consName = consType.label;
             
       for(<kwtype, defaultExpr> <- consType.kwFields){
            str kwFieldName = kwtype.label;
            str fuid = getGetterForKwpField(uid, kwFieldName);
            
            str getterName = "$get_<consType.adt.adtName>_<consName>_<kwFieldName>";
            getterType = afunc(kwtype, [consType], []);
            consVar = muVar(consName, fuid, 0, consType);
          
            defExprCode = fixFieldReferences(translate(defaultExpr), consType, consVar);
            body = muReturn1(muIfelse(muIsKwpDefined(consVar, kwFieldName), muGetKwpFromConstructor(consVar, kwtype, kwFieldName), defExprCode));
            addFunctionToModule(muFunction(fuid, getterName, getterType, [], [], "", 1, 1, false, true, false, [], |std:///|, [], (), body));               
       }
    }
 }
 
 MuExp fixFieldReferences(MuExp exp, AType consType, MuExp consVar)
    = visit(exp){
        case muVar(str fieldName, _, -1, AType tp) => muGetField(consType, tp, consVar, fieldName)
     };
    
void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// -- function declaration ------------------------------------------

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  translateFunctionDeclaration(fd, [], []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  Statement stat = (Statement) `<Expression expression>;`;
  translateFunctionDeclaration(fd, [stat], []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  Statement stat = (Statement) `<Expression expression>;`;
  translateFunctionDeclaration(fd, [stat], [exp | exp <- conditions]); 
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  translateFunctionDeclaration(fd, [stat | stat <- body.statements], []);
}

private void translateFunctionDeclaration(FunctionDeclaration fd, list[Statement] body, list[Expression] when_conditions){
  //println("r2mu: Compiling \uE007[<fd.signature.name>](<fd@\loc>)");
  funsrc = fd@\loc;
  enterFunctionDeclaration(funsrc);

  try {
      ttags =  translateTags(fd.tags);
      tmods = translateModifiers(fd.signature.modifiers);
      if(ignoreTest(ttags)){
          // The type checker does not generate type information for ignored functions
          addFunctionToModule(muFunction("/ignored-<fd@\loc.offset>()", 
                                         "<fd.signature.name>", 
                                         avoid(),
                                         [],
                                         [],
                                         "", 
                                         0, 
                                         0, 
                                         false, 
                                         true,
                                         false,
                                         [],
                                         fd@\loc, 
                                         tmods, 
                                         ttags,
                                         muReturn1(muCon(false))));
          	return;
      }
     
      ftype = getFunctionType(funsrc);
      argNames = getNestedParameterNames(funsrc); //getParameterNames(fd.signature.parameters.formals.formals);
      nformals = size(ftype.formals);
      fuid = convert2fuid(funsrc);
           
      enterFunctionScope(fuid);
      
      bool isVarArgs = ftype.varArgs;
      
      //// Keyword parameters
      lrel[str name, AType atype, MuExp defaultExp]  kwps = translateKeywordParameters(fd.signature.parameters/*, fuid, getFormals(funsrc), fd@\loc*/);
      
      mubody = muBlock([]);
      if(ttags["javaClass"]?){
         paramTypes = atuple(atypeList([param | param <- ftype.formals]));
         params = [ muVar(ftype.formals[i].label, fuid, i, ftype.formals[i]) | i <- [ 0 .. nformals] ];
         keywordTypes = avoid();
         
         //TODO
         //if(kwfs is \default) {
         // 	params +=  [ muVar("map_of_keyword_values",fuid,nformals), muVar("map_of_default_values",fuid,nformals+1)];
         //}
         if("<fd.signature.name>" == "typeOf"){		// Take note: special treatment of Types::typeOf
         	mubody = muCallPrim3("type2symbol", [ muCallPrim3("typeOf", params, fd@\loc), muCon(getGrammar()) ], fd@\loc);
         } else {
            mubody = muCallJava("<fd.signature.name>", ttags["javaClass"], ftype, ("reflect" in ttags) ? 1 : 0, params, fuid);
         }
      } else {
        mubody = muBlock([ translate(stat) | stat <- body ]);
      }
     
      isPub = !fd.visibility is \private;
      isMemo = ttags["memo"]?; 
      iprintln(body);
      tbody = translateFunction("<fd.signature.name>", fd.signature.parameters.formals.formals, ftype, mubody, isMemo, when_conditions);
     
      //formals = [formal | formal <- fd.signature.parameters.formals.formals];
      
      addFunctionToModule(muFunction(fuid, 
      								 "<fd.signature.name>", 
      								 ftype,
      								 argNames,
      								 kwps,
      								 "", //(addr.fuid in moduleNames) ? "" : addr.fuid, 
      								 getFormals(funsrc), 
      								 getScopeSize(funsrc),
      								 isVarArgs, 
      								 isPub,
      								 isMemo,
      								 getExternalRefs(tbody, fuid),
      								 fd@\loc, 
      								 tmods, 
      								 ttags,
      								 tbody));
      
      leaveFunctionScope();
      leaveFunctionDeclaration();
  } catch e: CompileTimeError(m): {
      throw e;  
  } catch Ambiguity(loc src, str stype, str string): {
      throw CompileTimeError(error("Ambiguous code", src));
  }
  //catch e: {
  //      throw "EXCEPTION in translateFunctionDeclaration, compiling <fd.signature.name>: <e>";
  //}
}

str getParameterName(list[Pattern] patterns, int i) = getParameterName(patterns[i], i);

str getParameterName((Pattern) `<QualifiedName qname>`, int i) = "<qname>";
str getParameterName((Pattern) `<QualifiedName qname> *`, int i) = "<qname>";
str getParameterName((Pattern) `<Type tp> <Name name>`, int i) = "<name>";
str getParameterName((Pattern) `<Name name> : <Pattern pattern>`, int i) = "<name>";
str getParameterName((Pattern) `<Type tp> <Name name> : <Pattern pattern>`, int i) = "<name>";
default str getParameterName(Pattern p, int i) = "$<i>";

list[str] getParameterNames({Pattern ","}* formals){
     abs_formals = [f | f <- formals];
     return[ getParameterName(abs_formals, i) | i <- index(abs_formals) ];
}

list[MuExp] getExternalRefs(MuExp exp, str fuid)
    = toList({ v | /v:muVar(str name, str fuid2, int pos, AType atype) := exp, fuid2 != fuid });
    
/********************************************************************/
/*                  Translate keyword parameters                    */
/********************************************************************/

lrel[str name, AType atype, MuExp defaultExp] translateKeywordParameters(Parameters parameters) {
  KeywordFormals kwfs = parameters.keywordFormals;
  kwmap = [];
  if(kwfs is \default && {KeywordFormal ","}+ keywordFormalList := parameters.keywordFormals.keywordFormalList){
      keywordParamsMap = getKeywords(parameters);
      kwmap = [ <"<kwf.name>", keywordParamsMap["<kwf.name>"], translate(kwf.expression)> | KeywordFormal kwf <- keywordFormalList ];
  }
  return kwmap;
}

/********************************************************************/
/*                  Translate function body                         */
/********************************************************************/

MuExp returnFromFunction(MuExp body, AType ftype, list[MuExp] formalVars, bool isMemo) {
  if(ftype.ret == avoid()){
    return body;
  } else {
      res = muReturn1(body);
      if(isMemo){
         res = visit(res){
            case muReturn1(e) => muMemoReturn(ftype, formalVars, body)
         }
      }
      return res;   
  }
}
         
MuExp functionBody(MuExp body, AType ftype, list[MuExp] formalVars, bool isMemo){
    if(isMemo){
        str fuid = topFunctionScope();
        result = muTmpIValue(nextTmp("result"), fuid, avalue());
        return muCheckMemo(ftype, formalVars, body);
    } else {
        return body;
    }
}
     
//MuExp translateFormals(list[Pattern] formals, AType ftype, bool isMemo, int i, MuExp body, list[Expression] when_conditions, loc src){
//   isVarArgs = ftype.varArgs;
//   if(isEmpty(formals)) {
//      if(isEmpty(when_conditions)){
//        return returnFromFunction(body, ftype, isMemo, src);
//      } else {
//        ifname = nextLabel();
//        enterBacktrackingScope(ifname);
//        mubody = translateConds(ifname, [cond | Expression cond <- when_conditions ], returnFromFunction(body, ftype, isMemo, src),  muFailReturn());
//        leaveBacktrackingScope();
//        return mubody;
//      }
//   }
//   pat = formals[0];
//   
//   if(pat is literal){
//     // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
//      ifname = nextLabel();
//      enterBacktrackingScope(ifname);
//      
//      patTest =  pat.literal is regExp ? muMulti(muApply(translatePat(pat, getType(pat@\loc)), [muVar("$<i>",topFunctionScope(),i) ]))
//                                       : muEqual(muVar(getParameterName(formals, i), topFunctionScope(),i, getType(formals[i])), translate(pat.literal));
//      
//      exp = muIfelse(patTest, translateFormals(tail(formals), ftype, isMemo, i + 1, body, when_conditions, src),
//                              muFailReturn()
//                  );
//      leaveBacktrackingScope();
//      return exp;
//   } else {
//      Name name = pat.name;
//      tp = pat.\type;
//      fuid = getVariableScope("<name>", name@\loc);
//      pos = getPositionInScope("<name>", name@\loc);
//      // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
//      ifname = nextLabel();
//      enterBacktrackingScope(ifname);
//                          
//      exp = muBlock([ muCheckArgTypeAndCopy("<name>", i, (isVarArgs && size(formals) == 1) ? alist(translateType(tp)) : translateType(tp), pos),
//                      translateFormals(tail(formals), ftype, isMemo, i + 1, body, when_conditions, src)
//                    ]);
//      leaveBacktrackingScope();
//      return exp;
//    }
//}

MuExp translateFunction(str fname, {Pattern ","}* formals, AType ftype, MuExp body, bool isMemo, list[Expression] when_conditions){
  //bool simpleArgs = true;
  //for(pat <- formals){
  //    if(!(pat is typedVariable || pat is literal))
  //      simpleArgs = false;
  //}
  //if(simpleArgs) { //TODO: should be: all(pat <- formals, (pat is typedVariable || pat is literal))) {
  //   return functionBody(muIfelse( muCon(true), muBlock([ translateFormals([formal | formal <- formals], ftype, isMemo, 0, /*kwps,*/ body, when_conditions, formals@\loc)]), muFailReturn()),
  //                       isMemo, formals@\loc);
  //} else {
     // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
     enterBacktrackingScope(fname);
     // TODO: account for a variable number of arguments
     formalsList = [f | f <- formals];
  
     formalVars = [muVar(getParameterName(formalsList, i), topFunctionScope(), i, getType(formalsList[i])) | i <- index(formalsList) ];
     conditions = (returnFromFunction(body, ftype, formalVars, isMemo)
                  | translatePat(formalsList[i], getType(formalsList[i]),formalVars[i], fname, it, muFailReturn(), subjectAssigned=true) 
                  | i <- index(formalsList));
     mubody = functionBody(conditions, ftype, formalVars, isMemo);
     leaveBacktrackingScope();
     return mubody;
  //}
}

/********************************************************************/
/*                  Translate tags in a function declaration        */
/********************************************************************/

// Some library functions need special tratement when called from compiled code.
// Therefore we provide special treatment for selected Java classes. 
// A Java class X.java can be extended with a class XCompiled.java
// and all calls are then first routed to XCompiled.java that can selectively override methods.
// The compiler checks for the existence of a class XCompiled.java

private str resolveLibOverriding(str lib){
   getVariableInitializationsInModule();
   
	if(lib in getNotOverriddenlibs()) return lib;
	
	if(lib in getOverriddenlibs()) return "<lib>Compiled";

    rlib1 = replaceFirst(lib, "org.rascalmpl.library.", "");
    rlib2 = |std:///| + "<replaceAll(rlib1, ".", "/")>Compiled.class";
  
	if(exists(rlib2)){
	   addOverriddenLib(lib);
	   //println("resolveLibOverriding <lib> =\> <lib>Compiled");
	   return "<lib>Compiled";
	} else {
	     addNotOverriddenLib(lib);
		//println("resolveLibOverriding <lib> =\> <lib>");
		return lib;
	}
}

public map[str,str] translateTags(Tags tags){
   m = ();
   for(tg <- tags.tags){
     str name = "<tg.name>";
     if(name == "license")
       continue;
     if(tg is \default){
        cont = "<tg.contents>"[1 .. -1];
        m[name] = name == "javaClass" ? resolveLibOverriding(cont) : cont;
     } else if (tg is empty)
        m[name] = "";
     else
        m[name] = "<tg.expression>"[1 .. -1];
   }
   return m;
}

//private bool ignoreCompilerTest(map[str, str] tags) = !isEmpty(domain(tags) & {"ignoreCompiler", "IgnoreCompiler"});

bool ignoreTest(map[str, str] tags) = !isEmpty(domain(tags) & {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

/********************************************************************/
/*       Translate the modifiers in a function declaration          */
/********************************************************************/

private list[str] translateModifiers(FunctionModifiers modifiers){
   lst = [];
   for(m <- modifiers.modifiers){
     if(m is \java) 
       lst += "java";
     else if(m is \test)
       lst += "test";
     else
       lst += "default";
   }
   return lst;
} 