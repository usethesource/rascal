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
import lang::rascalcore::check::NameUtils;

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
            body = muReturn1(kwtype, muIfelse(muIsKwpDefined(consVar, kwFieldName), muGetKwpFromConstructor(consVar, kwtype, kwFieldName), defExprCode));
            addFunctionToModule(muFunction(fuid, getterName, getterType, [], [], "", 1, 1, false, true, false, [], getModuleScope(), [], (), body));               
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
  translateFunctionDeclaration(fd, [], [], addReturn=true);
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

private void translateFunctionDeclaration(FunctionDeclaration fd, list[Statement] body, list[Expression] when_conditions, bool addReturn=false){
  //println("r2mu: Compiling \uE007[<fd.signature.name>](<fd@\loc>)");
  
  inScope = topFunctionScope();
  funsrc = fd@\loc;
  enterFunctionDeclaration(funsrc);

  try {
      ttags =  translateTags(fd.tags);
      tmods = translateModifiers(fd.signature.modifiers);
      if(ignoreTest(ttags)){
          // The type checker does not generate type information for ignored functions
          addFunctionToModule(muFunction("/ignored-<fd@\loc.offset>()", 
                                         prettyPrintName(fd.signature.name), 
                                         avoid(),
                                         [],
                                         [],
                                         inScope, 
                                         0, 
                                         0, 
                                         false, 
                                         true,
                                         false,
                                         [],
                                         fd@\loc, 
                                         tmods, 
                                         ttags,
                                         muReturn1(abool(), muCon(false))));
          	return;
      }
     
      ftype = getFunctionType(funsrc);
      resultType = ftype.ret;
      //formals = getNestedParameters(funsrc); //getParameterNames(fd.signature.parameters.formals.formals);
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
         //if("<fd.signature.name>" == "typeOf"){		// Take note: special treatment of Types::typeOf
        // 	mubody = muCallPrim3("type2symbol", [ muCallPrim3("typeOf", params, fd@\loc), muCon(getGrammar()) ], fd@\loc);
        // } else {
            mubody = muCallJava("<fd.signature.name>", ttags["javaClass"], ftype, ("reflect" in ttags) ? 1 : 0, params, fuid);
        // }
      } else if(addReturn){
            if((Statement) `<Expression expression>;` := body[0] && (Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>` := expression){
                mubody = translateBool(condition, "", muReturn1(resultType, translate(thenExp)), muReturn1(resultType, translate(elseExp)));
            } else {
                mubody = translate(body[0]);
            }
      } else {
        mubody = muBlock([ translate(stat) | stat <- body ]);
      }
      
     
      isPub = !fd.visibility is \private;
      isMemo = ttags["memo"]?; 
      iprintln(body);
      <formalVars, tbody> = translateFunction(prettyPrintName(fd.signature.name), fd.signature.parameters.formals.formals, ftype, mubody, isMemo, when_conditions);
      if(resultType != avoid() && !endsWithReturn(tbody)){
        iprintln(tbody);
        tbody = muBlock([ tbody, muFailReturn(ftype) ]);
      }
      
      addFunctionToModule(muFunction(fuid, 
      								 prettyPrintName(fd.signature.name), 
      								 ftype,
      								 formalVars,
      								 kwps,
      								 inScope,
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
      res = muReturn1(ftype.ret, body);
      if(isMemo){
         res = visit(res){
            case muReturn1(t, e) => muMemoReturn(ftype, formalVars, e)
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

tuple[list[MuExp] formalVars, MuExp funBody] translateFunction(str fname, {Pattern ","}* formals, AType ftype, MuExp body, bool isMemo, list[Expression] when_conditions){
     // Create a loop label to deal with potential backtracking induced by the formal parameter patterns  
     
     enterBacktrackingScope(fname);
     formalsList = [f | f <- formals];
     str fuid = topFunctionScope();
     
     formalVars = [hasParameterName(formalsList, i) ? muVar(pname, fuid, getPositionInScope(pname, getParameterNameAsTree(formalsList, i)@\loc), getType(formalsList[i]))
                                                     : muVar(pname, fuid, i, getType(formalsList[i]))   
                   | i <- index(formalsList), pname := getParameterName(formalsList, i) 
                   ];
    
     when_body = returnFromFunction(translateConds(fname, when_conditions, body, muFailReturn(ftype)), ftype, formalVars, isMemo);
     params_when_body = ( when_body
                        | translatePat(formalsList[i], getType(formalsList[i]),formalVars[i], fname, it, muFailReturn(ftype), subjectAssigned=hasParameterName(formalsList, i) ) 
                        | i <- reverse(index(formalsList)));
     funCode = functionBody(params_when_body, ftype, formalVars, isMemo);
     //funCode = muBlock([functionBody(params_when_body, ftype, formalVars, isMemo), muFailReturn(ftype)]);
     leaveBacktrackingScope();
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