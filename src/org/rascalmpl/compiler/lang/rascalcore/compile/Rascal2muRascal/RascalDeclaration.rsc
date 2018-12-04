@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration

import IO;
import Map;
import List;
import Set;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;

//import lang::rascalcore::compile::RVM::Interpreter::CompileTimeError;

import lang::rascalcore::compile::muRascal::AST;

//import lang::rascal::types::AbstractName;
//import lang::rascal::types::CheckerConfig;		// to be sure

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
	
void translate(t: (Toplevel) `<Declaration decl>`) = translate(decl);

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
          
            defExprCode = fixFieldReferences(translate(defaultExpr), consType, consVar, fuid);
            body = muReturn1(muIfelse("", muIsKwpDefined(consVar, kwFieldName), muGetKwpFromConstructor(consVar, kwtype, kwFieldName), defExprCode));
            addFunctionToModule(muFunction(fuid, getterName, getterType, [], [], "", 1, 1, false, true, true, [], |std:///|, [], (), body));               
       }
    }
 }
 
 MuExp fixFieldReferences(MuExp exp, AType consType, MuExp consVar, str fuid)
    = visit(exp){
        case muVar(str fieldName, _, -1, AType tp) => muFieldAccess("aadt", consType, consVar, fieldName)
     };
     


void translate(d: (Declaration) `<FunctionDeclaration functionDeclaration>`) = translate(functionDeclaration);

// -- function declaration ------------------------------------------

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> ;`)   {
  translateFunctionDeclaration(fd, muBlock([]), []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> ;`){
  translateFunctionDeclaration(fd, expression, []);
}

void translate(fd: (FunctionDeclaration) `<Tags tags> <Visibility visibility> <Signature signature> = <Expression expression> when <{Expression ","}+ conditions>;`){
  translateFunctionDeclaration(fd, expression, [exp | exp <- conditions]); 
}

void translate(fd: (FunctionDeclaration) `<Tags tags>  <Visibility visibility> <Signature signature> <FunctionBody body>`){
  translateFunctionDeclaration(fd, body.statements, []);
}

private void translateFunctionDeclaration(FunctionDeclaration fd, node body, list[Expression] when_conditions){
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
                                         //atuple(atypeList([])),
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
                                         false, 
                                         0,
                                         0,
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
      lrel[str name, AType atype, MuExp defaultExp]  kwps = translateKeywordParameters(fd.signature.parameters, fuid, getFormals(funsrc), fd@\loc);
      
      if(ttags["javaClass"]?){
         paramTypes = atuple(atypeList([param | param <- ftype.formals]));
         params = [ muVar(argNames[i], fuid, i, ftype.formals[i]) | i <- [ 0 .. nformals] ];
         keywordTypes = avoid();
         
         //TODO
         //if(kwfs is \default) {
         // 	params +=  [ muVar("map_of_keyword_values",fuid,nformals), muVar("map_of_default_values",fuid,nformals+1)];
         //}
         if("<fd.signature.name>" == "typeOf"){		// Take note: special treatment of Types::typeOf
         	body = muCallPrim3("type2symbol", [ muCallPrim3("typeOf", params, fd@\loc), muCon(getGrammar()) ], fd@\loc);
         } else {
            body = muCallJava("<fd.signature.name>", ttags["javaClass"], ftype, ("reflect" in ttags) ? 1 : 0, params, fuid);
         }
      }
     
      isPub = !fd.visibility is \private;
      isMemo = ttags["memo"]?; 
   
      tbody = translateFunction("<fd.signature.name>", fd.signature.parameters.formals.formals, isVarArgs, body, isMemo, when_conditions);
     
      formals = [formal | formal <- fd.signature.parameters.formals.formals];
      
      simpleArgs = true;
      for(pat <- formals){
        if(!(pat is typedVariable || pat is literal)){
            simpleArgs = false;
        }
      }
  
      //if(nformals > 0) println("formals[0] = <formals[0]>");
      
      //absfpArg = nformals > 0 ? fingerprint(formals[0], false) : 0;
      //println("absfpArg = <absfpArg>");
      //isConcreteArg = nformals > 0 ? isConcretePattern(formals[0]) : false;
      //concfpArg = nformals > 0 && isConcreteArg ? fingerprint(formals[0], true) : 0;
      //println("concfpArg = <concfpArg>");
     
     
      //println("translateFunctionDeclaration, fd = <fd>");
      
      //println("translateFunctionDeclaration: <fuid>, <addr.fuid>, <moduleNames>,  addr.fuid in moduleNames = <addr.fuid in moduleNames>");
      
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
      								 simpleArgs,
      								 getExternalRefs(tbody, fuid),
      								 fd@\loc, 
      								 tmods, 
      								 ttags,
      								 //isConcreteArg, 
      								 //absfpArg,
      								 //concfpArg,
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

list[str] getParameterNames({Pattern ","}* formals){
     abs_formals = [f | f <- formals];
     int arity = size(abs_formals);
     names =
     for(int i <- [0 .. arity]){
         str argName = "$<i>";
         
         if((Pattern)`<Type pt> <Name pn>` := abs_formals[i]){
              argName = "<pn>";
         } else if((Pattern)`<Name pn>` := abs_formals[i]){
              argName = "<pn>";
         } else if((Pattern) `<Name name> : <Pattern pattern>` := abs_formals[i]){
            argName = "<name>";
         } else if((Pattern) `<Type tp> <Name name> : <Pattern pattern>` := abs_formals[i]){
            argName = "<name>";
         }
         append argName; 
     }
     return names; 
}

list[MuExp] getExternalRefs(MuExp exp, str fuid)
    = toList({ v | /v:muVar(str name, str fuid2, int pos, AType atype) := exp, fuid2 != fuid });

/********************************************************************/
/*                  Translate keyword parameters                    */
/********************************************************************/

lrel[str name, AType atype, MuExp defaultExp] translateKeywordParameters(Parameters parameters, str fuid, int pos, loc l) {
  KeywordFormals kwfs = parameters.keywordFormals;
  kwmap = [];
  if(kwfs is \default) {
      keywordParamsMap = getKeywords(l);
      kwmap = [ <"<kwf.name>", keywordParamsMap["<kwf.name>"], translate(kwf.expression)> | KeywordFormal kwf <- kwfs.keywordFormalList ];
  }
  return kwmap;
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

private bool ignoreCompilerTest(map[str, str] tags) = !isEmpty(domain(tags) & {"ignoreCompiler", "IgnoreCompiler"});

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