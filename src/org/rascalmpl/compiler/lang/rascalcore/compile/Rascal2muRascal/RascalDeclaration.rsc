@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration

import IO;
import Map;
import List;
import ListRelation;
import Set;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;

import lang::rascalcore::compile::CompileTimeError;

import lang::rascalcore::compile::muRascal::AST;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::NameUtils;
import lang::rascalcore::compile::util::Names;

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
	str module_name = getUnqualifiedName(getModuleName());
    ftype = afunc(avalue(),[avalue()], []);
    enterFunctionScope("<module_name>_init");
   	for(var <- variables){
   	    unescapedVarName = unescapeName("<var.name>");
   		addVariableToModule(muModuleVar(getType(tp), unescapedVarName));
   		//variables_in_module += [];
   		if(var is initialized) {
   		   init_code =  translate(var.initial);
   		   asg = muAssign( muVar(unescapedVarName, getModuleName(), -1, getType(tp)), init_code);
   		   addVariableInitializationToModule(asg);
   		}
   	}
   	leaveFunctionScope();
}   	

// -- miscellaneous declarations that can be skipped since they are handled during type checking ------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType>@<Name name> ;`) { /*skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }

void translate(d : (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> ;`) { /* skip: translation has nothing to do here */ }


void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) {
    /* all getters are generated by generateKeywordFieldGetters */
 }
 
 MuExp fixFieldReferences(MuExp exp, AType consType, MuExp consVar)
    = visit(exp){
        case muVar(str fieldName, _, -1, AType tp)        => muGetField(tp, consType, consVar, fieldName)
        case muVarKwp(str fieldName, str scope, AType tp) => muGetKwField(tp, consType, consVar, fieldName)
     };
    
void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);


void generateKeywordFieldGetters(map[AType, set[AType]] adt_constructors, map[AType, list[Keyword]] adt_common_keyword_fields){
    for(adtType <- adt_constructors){
        generateGettersForAdt(adtType, adt_constructors[adtType], adt_common_keyword_fields[adtType] ? []);
    }
}

private void generateGettersForAdt(AType adtType, set[AType] constructors, list[Keyword] common_keyword_fields){

    adtName = adtType.adtName;
    /*
     * Create getters for common keyword fields of this data type
     */
    seen = {};
    for(<kwType, defaultExpr> <- common_keyword_fields, kwType notin seen){
        seen += kwType;
        str kwFieldName = kwType.label;
        str fuid = getGetterNameForKwpField(adtType, kwFieldName);
        str getterName = unescapeAndStandardize("$get_<adtName>_<kwFieldName>");
       
        getterType = afunc(kwType, [adtType], []);
        adtVar = muVar(getterName, fuid, 0, adtType);
        
        defExprCode = fixFieldReferences(translate(defaultExpr), adtType, adtVar);
        body = muReturn1(kwType, muIfelse(muIsKwpDefined(adtVar, kwFieldName), muGetKwFieldFromConstructor(kwType, adtVar, kwFieldName), defExprCode));
        addFunctionToModule(muFunction(fuid, getterName, getterType, [adtVar], [], "", false, true, false, {}, {}, {}, getModuleScope(), [], (), body));               
    }
    
    /*
     * Create getters for constructor specific keyword fields.
     */
    
    kwfield2cons = [];
       
    for(consType <- constructors){
       /*
        * Create constructor=specific getters for each keyword field
        */
       consName = consType.label;
       
       for(<kwType, defaultExpr> <- consType.kwFields){
            str kwFieldName = kwType.label;
            kwfield2cons += <kwFieldName, kwType, consType>;
            str fuid = getGetterNameForKwpField(consType, kwFieldName);
            str getterName = unescapeAndStandardize("$get_<adtName>_<consName>_<kwFieldName>");
            
            getterType = afunc(kwType, [consType], []);
            consVar = muVar(consName, fuid, 0, consType);
            
            defExprCode = fixFieldReferences(translate(defaultExpr), consType, consVar);
            body = muReturn1(kwType, muIfelse(muIsKwpDefined(consVar, kwFieldName), muGetKwFieldFromConstructor(kwType, consVar, kwFieldName), defExprCode));
            addFunctionToModule(muFunction(fuid, getterName, getterType, [consVar], [], "", false, true, false, {}, {}, {}, getModuleScope(), [], (), body));               
       }
    }
    
     /*
      * Create generic getters for all keyword fields
      */
    
    for(str kwFieldName <- domain(kwfield2cons)){
        conses = kwfield2cons[kwFieldName];
        str fuid = getGetterNameForKwpField(adtType, kwFieldName);
        str getterName = unescapeAndStandardize("$get_<adtName>_<kwFieldName>");
            
        returnType = lubList(conses<0>);
        getterType = afunc(returnType, [adtType], []);
        adtVar = muVar(adtName, fuid, 0, adtType);
        body = muBlock([ muIf(muHasNameAndArity(adtType, consType, muCon(consType.label), size(consType.fields), adtVar),
                              muReturn1(kwType, muGetKwField(kwType, consType, adtVar, kwFieldName)))
                       | <kwType, consType> <- conses
                       ]
                       + muFailReturn(returnType)
                      );
        addFunctionToModule(muFunction(fuid, getterName, getterType, [adtVar], [], "", false, true, false, {}, {}, {}, getModuleScope(), [], (), body));               
    }
    
    /* 
     * Create generic getters for all ordinary fields
     */
    
    field2cons = [];
       
    for(consType <- constructors){
        consName = consType.label;
        for(fieldType <- consType.fields){
            str fieldName = fieldType.label;
            field2cons += <fieldType, consType>;
        }
    }
    
    for(fieldType <- domain(field2cons)){
        fieldName = fieldType.label;
        conses = field2cons[fieldType];
        str fuid = getGetterNameForKwpField(adtType, fieldName);
        str getterName = unescapeAndStandardize("$get_<adtName>_<fieldName>");
        
        getterType = afunc(fieldType, [adtType], []);
        adtVar = muVar(adtName, fuid, 0, adtType);
        body = muBlock([ muIf(muHasNameAndArity(adtType, consType, muCon(consType.label), size(consType.fields), adtVar),
                              muReturn1(fieldType, muGetFieldFromConstructor(fieldType, consType, adtVar, fieldName)))
                       | consType <- conses
                       ]
                       + muBuiltinRuntimeExceptionThrow("noSuchField", [muCon(fieldName)]) 
                                        
                      );
        addFunctionToModule(muFunction(fuid, getterName, getterType, [adtVar], [], "", false, true, false, {}, {}, {}, getModuleScope(), [], (), body));            
    }
 }


// -- function declaration ------------------------------------------

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  translateFunctionDeclaration(fd, [], []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  Statement stat = (Statement) `<Expression expression>;`;
  translateFunctionDeclaration(fd, [stat], [], addReturn=true);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  Statement stat = (Statement) `<Expression expression>;`;
  translateFunctionDeclaration(fd, [stat], [exp | exp <- conditions], addReturn=true); 
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  translateFunctionDeclaration(fd, [stat | stat <- body.statements], []);
}

private void translateFunctionDeclaration(FunctionDeclaration fd, list[Statement] body, list[Expression] when_conditions, bool addReturn = false){
  println("r2mu: Compiling \uE007[<fd.signature.name>](<fd@\loc>)");
  
  inScope = topFunctionScope();
  funsrc = fd@\loc;
  enterFunctionDeclaration(funsrc);

  try {
      ttags =  translateTags(fd.tags);
      tmods = translateModifiers(fd.signature.modifiers);
      if(ignoreTest(ttags)){
          // The type checker does not generate type information for ignored functions
           addFunctionToModule(muFunction("$ignored_<prettyPrintName(fd.signature.name)>_<fd@\loc.offset>", 
                                         prettyPrintName(fd.signature.name), 
                                         afunc(abool(),[],[]),
                                         [],
                                         [],
                                         inScope, 
                                         false, 
                                         true,
                                         false,
                                         {},
                                         {},
                                         {},
                                         fd@\loc, 
                                         tmods, 
                                         ttags,
                                         muReturn1(abool(), muCon(false))));
          	return;
      }
     
      ftype = getFunctionType(funsrc);
      resultType = ftype.ret;
      nformals = size(ftype.formals);
      fuid = convert2fuid(funsrc);
           
      enterFunctionScope(fuid);
      
      bool isVarArgs = ftype.varArgs;
      
      //// Keyword parameters
      lrel[str name, AType atype, MuExp defaultExp]  kwps = translateKeywordParameters(fd.signature.parameters);
      
      mubody = muBlock([]);
      if(ttags["javaClass"]?){
         paramTypes = atuple(atypeList([param | param <- ftype.formals]));
         params = [ muVar(ftype.formals[i].label, fuid, i, ftype.formals[i]) | i <- [ 0 .. nformals] ];
         keywordTypes = avoid();
         
         //TODO
         //if(kwfs is \default) {
         // 	params +=  [ muVar("map_of_keyword_values",fuid,nformals), muVar("map_of_default_values",fuid,nformals+1)];
         //}
         
         // JURGEN: TODO remove commented out code; I removed the special treatment of typeOf here
         //if("<fd.signature.name>" == "typeOf"){		// Take note: special treatment of Types::typeOf
        	//mubody = muCallPrim3("typeOf", aadt("AType", [], dataSyntax()), [avalue()], params, fd@\loc);
        	////mubody = muCallPrim3("type2symbol", [ muCallPrim3("typeOf", params, fd@\loc), muCon(getGrammar()) ], fd@\loc);
         //} else {
            mubody = muReturn1(resultType, muCallJava("<fd.signature.name>", ttags["javaClass"], ftype, ("reflect" in ttags) ? 1 : 0, params, fuid));
        //}
      } else if(!isEmpty(body)){
            if(size(body) == 1 && addReturn){
                mubody = translateReturn(body[0], ());
             } else {
                mubody = muBlock([ translate(stat, ()) | stat <- body ]);
             }
      }
     
      isPub = !fd.visibility is \private;
      isMemo = ttags["memo"]?; 
      <formalVars, tbody> = translateFunction(prettyPrintName(fd.signature.name), fd.signature.parameters.formals.formals, ftype, mubody, isMemo, when_conditions);
      //if(resultType != avoid() && !ttags["javaClass"]?){
      //  tbody = muReturn1(resultType, tbody);
      //}
      
      //iprintln(tbody);
      
      addFunctionToModule(muFunction(prettyPrintName(fd.signature.name), 
                                     fuid, 
      								 ftype,
      								 formalVars,
      								 kwps,
      								 inScope,
      								 isVarArgs, 
      								 isPub,
      								 isMemo,
      								 getExternalRefs(tbody, fuid),
      								 getLocalRefs(tbody),
      								 getKeywordParameterRefs(tbody, fuid),
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

Tree getParameterNameAsTree(list[Pattern] patterns, int i) = getParameterNameAsTree(patterns[i], i);

Tree getParameterNameAsTree((Pattern) `<QualifiedName qname>`, int i) = qname;
Tree getParameterNameAsTree((Pattern) `<QualifiedName qname> *`, int i) = qname;
Tree getParameterNameAsTree((Pattern) `<Type tp> <Name name>`, int i) = name;
Tree getParameterNameAsTree((Pattern) `<Name name> : <Pattern pattern>`, int i) = name;
Tree getParameterNameAsTree((Pattern) `<Type tp> <Name name> : <Pattern pattern>`, int i) = name;

bool hasParameterName(list[Pattern] patterns, int i) = hasParameterName(patterns[i], i);

bool hasParameterName((Pattern) `<QualifiedName qname>`, int i) = "<qname>" != "_";
bool hasParameterName((Pattern) `<QualifiedName qname> *`, int i) = "<qname>" != "_";
bool hasParameterName((Pattern) `<Type tp> <Name name>`, int i) = "<name>" != "_";
bool hasParameterName((Pattern) `<Name name> : <Pattern pattern>`, int i) = "<name>" != "_";
bool hasParameterName((Pattern) `<Type tp> <Name name> : <Pattern pattern>`, int i) = "<name>" != "_";
default bool hasParameterName(Pattern p, int i) = false;

set[MuExp] getAssignedInVisit(list[MuCase] cases, MuExp def)
    = { v | exp <- [c.exp | c <- cases] + def, /muAssign(v:muVar(str name, str fuid2, int pos, AType atype), MuExp _) := exp};
    
set[MuExp] getLocalRefs(MuExp exp)
  = { *getAssignedInVisit(cases, defaultExp) | /muVisit(str _, MuExp _, list[MuCase] cases, MuExp defaultExp, VisitDescriptor _) := exp };

set[MuExp] getExternalRefs(MuExp exp, str fuid)
    = { v | /v:muVar(str name, str fuid2, int pos, AType atype) := exp, fuid2 != fuid, fuid2 != "" };

set[MuExp] getKeywordParameterRefs(MuExp exp, str fuid)
    = { v | /v:muVarKwp(str name, str fuid2, AType atype) := exp, fuid2 != fuid };
    
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

MuExp returnFromFunction(MuExp body, AType ftype, list[MuExp] formalVars, bool isMemo, bool addReturn=false) {
  if(ftype.ret == avoid()){ 
    res = body;
    if(isMemo){
         res = visit(res){
            case muReturn0() => muMemoReturn0(ftype, formalVars)
         }
         res = muBlock([res,  muMemoReturn0(ftype, formalVars)]);
      }
     return res;
  } else {
      res = addReturn ? muReturn1(ftype.ret, body) : body;
      if(isMemo){
         res = visit(res){
            case muReturn1(t, e) => muMemoReturn1(ftype, formalVars, e)
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

tuple[list[MuExp] formalVars, MuExp funBody] translateFunction(str fname, {Pattern ","}* formals, AType ftype, MuExp body, bool isMemo, list[Expression] when_conditions, bool addReturn=false){
     // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
     
     list[Pattern] formalsList = [f | f <- formals];
     str fuid = topFunctionScope();
     bt = nextTmp("FUNCTION_<fname>");
     my_btscopes = getBTScopesParams(formalsList, fname);
    // iprintln(body);
   
    
     
     formalVars = [ hasParameterName(formalsList, i) && !isUse(formalsList[i]@\loc) ? muVar(pname, fuid, getPositionInScope(pname, getParameterNameAsTree(formalsList, i)@\loc), getType(formalsList[i]))
                                                                                    : muVar(pname, fuid, -i, getType(formalsList[i]))   
                  | i <- index(formalsList),  pname := getParameterName(formalsList, i) 
                  ];
     
     //iprintln(body);
     when_body = returnFromFunction(body, ftype, formalVars, isMemo, addReturn=addReturn);
     //iprintln(when_body);
     if(!isEmpty(when_conditions)){
        when_body = translateAndConds((), when_conditions, when_body, muFailReturn(ftype));
     }
     params_when_body = ( when_body
                        | translatePat(formalsList[i], getType(formalsList[i]), formalVars[i], my_btscopes, it, muFailReturn(ftype), subjectAssigned=hasParameterName(formalsList, i) ) 
                        | i <- reverse(index(formalsList)));
                        
     funCode = functionBody(isVoidType(ftype.ret) || !addReturn ? params_when_body : muReturn1(ftype.ret, params_when_body), ftype, formalVars, isMemo);
     funCode = visit(funCode) { case muFail(fname) => muFailReturn(ftype) };
     
     funCode = removeDeadCode(funCode);
    
     //iprintln(funCode);
     alwaysReturns = ftype.returnsViaAllPath || isVoidType(getResult(ftype));
     formalsBTFree = isEmpty(formalsList) || all(f <- formalsList, backtrackFree(f));
     if(!formalsBTFree || (formalsBTFree && !alwaysReturns)){
        funCode = muBlock([muEnter(fname, funCode), muFailReturn(ftype)]);
     }
     //if(!isVoidType(getResult(ftype))){
     //   funCode = muBlock([funCode, muFailReturn(ftype)]);
     //}
      
     funCode = removeDeadCode(funCode);

     return <formalVars, funCode>;
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
        m[name] = "<tg.expression>";
   }
   return m;
}

bool ignoreCompiler(map[str,str] tagsMap)
    = !isEmpty(domain(tagsMap) &  {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

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