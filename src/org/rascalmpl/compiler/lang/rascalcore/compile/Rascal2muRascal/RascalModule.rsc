@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::compile::Rascal2muRascal::RascalModule

import IO;
import String;
import Set;
import List;
import util::Reflective;

import ParseTree;
import lang::rascalcore::compile::CompileTimeError;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::compile::muRascal::AST;

//extend lang::rascalcore::check::Checker;
extend analysis::typepal::TypePal;
import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::compile::Rascal2muRascal::ModuleInfo;
import lang::rascalcore::compile::Rascal2muRascal::TmpAndLabel;
import lang::rascalcore::compile::Rascal2muRascal::RascalType;
import lang::rascalcore::compile::Rascal2muRascal::TypeUtils;
import lang::rascalcore::compile::Rascal2muRascal::ConcreteSyntax;

import lang::rascalcore::compile::Rascal2muRascal::RascalDeclaration;
import lang::rascalcore::compile::Rascal2muRascal::RascalExpression;



/*
 * Translate a Rascal module to muRascal.
 * The essential function is:
 * - r2mu		translate Rascal module to muRascal
 */

/********************************************************************/
/*                  Translate one module                            */
/********************************************************************/

@doc{Compile a parsed Rascal source module to muRascal}
tuple[TModel, MuModule] r2mu(lang::rascal::\syntax::Rascal::Module M, TModel tmodel, RascalCompilerConfig compilerConfig){
   try {
      resetModuleInfo(compilerConfig);
      module_scope = M@\loc;
      setModuleScope(module_scope);
      //setModuleScope(convert2fuid(module_scope));
      module_name = "<M.header.name>";
      setModuleName(module_name);
      mtags = translateTags(M.header.tags);
      setModuleTags(mtags);
      if(ignoreTest(mtags)){
            e = info("Ignore tag suppressed compilation", M@\loc);
            tmodel.messages += [e];
            return <tmodel, errorMuModule(getRascalModuleName(), {e}, M@\loc)>;
      }
     
      //if(verbose) println("r2mu: entering ... <module_name>, enableAsserts: <enableAsserts>");
   	  
   	  // Extract scoping information available from the tmodel returned by the type checker  
   	  extractScopes(tmodel); 
   	 
   	  if (<Module newModule, TModel newModel> := parseConcreteFragments(M, tmodel, getGrammar())) {
   	    M = newModule;
   	    tmodel = newModel;
   	  }
   	  
   	  translateModule(M);
   	  
   	  generateAllFieldGetters(module_scope);
   	 
   	  modName = replaceAll("<M.header.name>","\\","");
                      
   	  return < getTModel()[messages=tmodel.messages],
   	            /*relocMuModule(*/
   	            muModule(modName,
   	  				  getModuleTags(),
   	                  toSet(tmodel.messages), 
   	  				  getImportsInModule(),
   	  				  getExtendsInModule(), 
   	  				  getADTs(), 
   	  				  getConstructors(),
   	  				  getFunctionsInModule(), 
   	  				  getVariablesInModule(), 
   	  				  getVariableInitializationsInModule(),   
   	  				  getCommonKeywordFieldsNameAndType(),
   	  				  getGrammar(),
   	  				  M@\loc) /*,   
   	  				  reloc,
   	  				  pcfg.srcs)*/
   	  	      >;

   }
   catch ParseError(loc l): {
        if (compilerConfig.verbose) { println("Parse error in concrete syntax <l>; returning error module"); }
        msg = error("Parse error in concrete syntax fragment", l);
        tmodel.messages += [msg];
        return <tmodel, errorMuModule(getRascalModuleName(), {msg}, M@\loc)>;
   }
   catch CompileTimeError(Message m): {
        tmodel.messages += [m];
        return <tmodel, errorMuModule(getRascalModuleName(), {m}, M@\loc)>;
   }
   //catch value e: {
   //     return <tmodel, errorMuModule(getRascalModuleName(), {error("Unexpected compiler exception <e>", M@\loc)}, M@\loc)>;
   //}
   finally {
   	   resetModuleInfo(compilerConfig);
   	   resetScopeExtraction();
   }
}

//TModel relocConfig(TModel tmodel, loc reloc, list[loc] srcs){
//        return visit(tmodel) { case loc l => relocLoc(l, reloc, srcs) };
//}

void translateModule((Module) `<Header header> <Body body>`) {
    for(imp <- header.imports) importModule(imp);
	for( tl <- body.toplevels) translateToplevel(tl);
}

/********************************************************************/
/*                  Translate imports in a module                   */
/********************************************************************/

private void importModule((Import) `import <QualifiedName qname> ;`){
    addImportToModule("<qname>"); //TODO
    // addImportToModule(prettyPrintName(convertName(qname)));
}

private void importModule((Import) `extend <QualifiedName qname> ;`){
	moduleName = "<qname>";    //TODO
	//moduleName = prettyPrintName(convertName(qname));
	addImportToModule(moduleName);
	addExtendToModule(moduleName);
}

private void importModule((Import) `<SyntaxDefinition syntaxdef>`){ /* nothing to do */

}

private default void importModule(Import imp){
    throw "Unimplemented import: <imp>";
}
