@bootstrapParser
module experiments::Compiler::Rascal2muRascal::RascalDeclaration

import IO;
import Map;
import Set;
import String;
import lang::rascal::\syntax::Rascal;
import ParseTree;

import experiments::Compiler::muRascal::AST;

import lang::rascal::types::AbstractName;
import lang::rascal::types::CheckerConfig;		// to be sure

import experiments::Compiler::Rascal2muRascal::ModuleInfo;
import experiments::Compiler::Rascal2muRascal::RascalType;
import experiments::Compiler::Rascal2muRascal::TypeUtils;
import experiments::Compiler::Rascal2muRascal::TypeReifier;
import experiments::Compiler::Rascal2muRascal::TmpAndLabel;

import experiments::Compiler::Rascal2muRascal::RascalExpression;
import experiments::Compiler::Rascal2muRascal::RascalPattern;
import experiments::Compiler::Rascal2muRascal::RascalStatement;


/********************************************************************/
/*                  Translate declarations in a module              */
/********************************************************************/
	
void translate(t: (Toplevel) `<Declaration decl>`) = translate(decl);

// -- variable declaration ------------------------------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> <Type tp> <{Variable ","}+ variables> ;`) {
	module_name = getModuleName();
    ftype = Symbol::func(Symbol::\value(),[Symbol::\list(Symbol::\value())]);
    enterFunctionScope(getFUID(module_name,"#<module_name>_init",ftype,0));
   	for(var <- variables){
   		addVariableToModule(muVariable("<var.name>"));
   		//variables_in_module += [];
   		if(var is initialized) 
   		addVariableInitializationToModule(mkAssign("<var.name>", var@\loc, translate(var.initial)));
   		//variable_initializations +=  mkAssign("<var.name>", var@\loc, translate(var.initial));
   	}
   	leaveFunctionScope();
}   	

// -- miscellaneous declarations that can be skipped since they are handled during type checking ------------------

void translate(d: (Declaration) `<Tags tags> <Visibility visibility> anno <Type annoType> <Type onType> @ <Name name> ;`) { /*skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> alias <UserType user> = <Type base> ;`)   { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> tag <Kind kind> <Name name> on <{Type ","}+ types> ;`)  { throw("tag"); }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> ;`)  { /* skip: translation has nothing to do here */ }
void translate(d: (Declaration) `<Tags tags> <Visibility visibility> data <UserType user> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ variants> ;`) { /* skip: translation has nothing to do here */ }

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
  println("r2mu: Compiling \uE007[](<fd@\loc><fd.signature.name>, <fd@\loc>");
  //setFunctionUID(fd@\loc);

  try {
  ttags =  translateTags(fd.tags);
  if(ignoreTest(ttags)){
  	return;
  }
  tmods = translateModifiers(fd.signature.modifiers);
  
  ftype = getFunctionType(fd@\loc);
  nformals = size(ftype.parameters);
  uid = getLoc2uid(fd@\loc);
  fuid = convert2fuid(uid);
 
  enterFunctionScope(fuid);
  
  tuple[str fuid,int pos] addr = uid2addr[uid];
  bool isVarArgs = (varArgs(_,_) := fd.signature.parameters);
  
  // Keyword parameters
  list[MuExp] kwps = translateKeywordParameters(fd.signature.parameters, fuid, getFormals(uid), fd@\loc);
 
  
  
  if(ttags["javaClass"]?){
     paramTypes = \tuple([param | param <- ftype.parameters]);
     params = [ muVar("<ftype.parameters[i]>", fuid, i) | i <- [ 0 .. nformals] ];
     
     keywordTypes = \tuple([]);
     KeywordFormals kwfs = fd.signature.parameters.keywordFormals;
     if(kwfs is \default) {
      	keywordTypes = \tuple([ label("<kwf.name>", translateType(kwf.\type)) | KeywordFormal kwf <- kwfs.keywordFormalList]);
      	params +=  [ muVar("map_of_keyword_values",fuid,nformals), muVar("map_of_default_values",fuid,nformals+1)];
     }
     if("<fd.signature.name>" == "typeOf"){		// Take note: special treatment of Types::typeOf
     	body = muCallPrim3("type2symbol", [ muCallPrim3("typeOf", params, fd@\loc), muCon(getGrammar()) ], fd@\loc);
     } else {
        body = muCallJava("<fd.signature.name>", ttags["javaClass"], paramTypes, keywordTypes, ("reflect" in ttags) ? 1 : 0, params);
     }
  }
 
  isPub = !fd.visibility is \private;
  tbody = translateFunction("<fd.signature.name>", fd.signature.parameters.formals.formals, isVarArgs, kwps, body, when_conditions);
 
  formals = [formal | formal <- fd.signature.parameters.formals.formals];
  //if(nformals > 0) println("formals[0] = <formals[0]>");
  
  absfpArg = nformals > 0 ? fingerprint(formals[0], false) : 0;
  //println("absfpArg = <absfpArg>");
  isConcreteArg = nformals > 0 ? isConcretePattern(formals[0]) : false;
  concfpArg = nformals > 0 && isConcreteArg ? fingerprint(formals[0], true) : 0;
  //println("concfpArg = <concfpArg>");
 
 
  //println("translateFunctionDeclaration, fd = <fd>");
  
  //println("translateFunctionDeclaration: <fuid>, <addr.fuid>, <moduleNames>,  addr.fuid in moduleNames = <addr.fuid in moduleNames>");
  
  addFunctionToModule(muFunction(fuid, 
  								 "<fd.signature.name>", 
  								 ftype, 
  								 (addr.fuid in moduleNames) ? "" : addr.fuid, 
  								 getFormals(uid), 
  								 getScopeSize(fuid), 
  								 isVarArgs, 
  								 isPub, 
  								 fd@\loc, 
  								 tmods, 
  								 ttags,
  								 isConcreteArg, 
  								 absfpArg,
  								 concfpArg,
  								 tbody));
  
  if("test" in tmods){
     params = ftype.parameters;
     addTestToModule(muCallPrim3("testreport_add", [muCon(fuid),  muCon(ignoreTest(ttags)), muCon(ttags["expected"] ? ""), muCon(fd@\loc)] + [ muCon(symbolToValue(\tuple([param | param <- params ]))) ], fd@\loc));
  }
  leaveFunctionScope();
  
  } catch e: {
        throw "EXCEPTION in translateFunctionDeclaration, compiling <fd.signature.name>: <e>";
  }
}

/********************************************************************/
/*                  Translate keyword parameters                    */
/********************************************************************/

public list[MuExp] translateKeywordParameters(Parameters parameters, str fuid, int pos, loc l) {
  list[MuExp] kwps = [];
  KeywordFormals kwfs = parameters.keywordFormals;
  if(kwfs is \default) {
      keywordParamsMap = getKeywords(l);
      kwps = [ muAssign("map_of_default_values", fuid, pos, muCallMuPrim("make_mmap_str_entry",[])) ];
      for(KeywordFormal kwf <- kwfs.keywordFormalList) {
          kwps += muCallMuPrim("mmap_str_entry_add_entry_type_ivalue", 
                                  [ muVar("map_of_default_values",fuid,pos), 
                                    muCon("<kwf.name>"), 
                                    muCallMuPrim("make_mentry_type_ivalue", [ muTypeCon(keywordParamsMap[convertName(kwf.name)]), 
                                                                             translate(kwf.expression) ]) ]);
      }
  }
  return kwps;
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
    
    println("rlib1 = <rlib1>, rlib2 = <rlib2>");
  
	if(exists(rlib2)){
	   addOverriddenLib(lib);
	   println("resolveLibOverriding <lib> =\> <lib>Compiled");
	   return "<lib>Compiled";
	} else {
		 addNotOverriddenLib(lib);
		println("resolveLibOverriding <lib> =\> <lib>");
		return lib;
	}
}

public map[str,str] translateTags(Tags tags){
   m = ();
   for(tg <- tags.tags){
     str name = "<tg.name>";
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

private bool ignoreTest(map[str, str] tags) = !isEmpty(domain(tags) & {"ignore", "Ignore", "ignoreCompiler", "IgnoreCompiler"});

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