@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::TestChecker

import String;
import Type;
import DateTime;
import Message;
import util::Reflective;
import IO;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::TypeSignature;
import lang::rascal::checker::ParserHelper;
import lang::rascal::types::CheckTypes;
import lang::rascal::\syntax::Rascal;

public CheckResult checkStatementsString(str statementsString, list[str] importedModules = [], list[str] initialDecls = [], list[str] syntaxDecls = []) {

	str moduleToCheck =
		"module CheckStatementsString
		'<for (im <- importedModules) {>
		'import <im>;<}>
		'<for (sd <- syntaxDecls) {>
		'<sd><}>
		'<for (id <- initialDecls) {>
		'<id><}>
		";
    moduleLoc = |test-modules:///CheckStatementsString.rsc|;
    writeFile(moduleLoc, moduleToCheck);
    println("<moduleLoc>: <lastModified(moduleLoc)>");
	c = newConfiguration();
	try {
		pt = parseModuleWithSpaces(moduleLoc);

		if (pt has top && Module m := pt.top) {
			c = checkModule(m, c);
		} else {
			c = addScopeError(c, "Unexpected parse result for module to check <pt>", |unknown:///|); 
		}
	} catch perror : {
		c = addScopeError(c, "Could not parse and prepare config for base module to check: <perror>", |unknown:///|);
	}
			
//	// Convert the string representations of the module name into
//	// the internal name format we use for Rascal qualified names.
//	list[RName] imports = [ ];
//	for (modString <- importedModules) {
//		tokenized = split("::", trim(modString));
//		if (size(tokenized) == 1)
//			imports += RSimpleName(head(tokenized));
//		else
//			imports += RCompoundName(tokenized);
//	}
//	
//    // Create an artificial module for the statements we are checking, along
//    // with a new type checking configuration to provide the checking context.
//	c = newConfiguration();
//	moduleName = RSimpleName("CheckStatementsString");
//	c = addModule(c, moduleName, |unknown:///|);
//	currentModuleId = head(c.stack);
//            
//	// Retrieve the module signature for each of the modules we are importing
//	map[RName,RSignature] sigMap = ( );
//	map[RName,int] moduleIds = ( );
//	map[RName,loc] moduleLocs = ( );
//	
//	for (modName <- imports) {
//		try {
//			dt1 = now();
//			modTree = getModuleParseTree(prettyPrintName(modName));
//			sigMap[modName] = getModuleSignature(modTree);
//			moduleLocs[modName] = modTree@\loc;
//			c = addModule(c,modName,modTree@\loc);
//			moduleIds[modName] = head(c.stack);
//			c = popModule(c);
//			c = pushTiming(c, "Generate signature for <prettyPrintName(modName)>", dt1, now());
//		} catch perror : {
//			c = addScopeError(c, "Cannot calculate signature for imported module <prettyPrintName(modName)>", |unknown:///|);
//		}
//	}
//    
//	// Add all the aliases, ADTs, and tags from each module without descending. This puts
//	// their names into the global namespace so they will be visible when we check the
//	// bodies below.
//	dt1 = now();
//	for (modName <- imports) {
//		sig = sigMap[modName];
//		c.stack = moduleIds[modName] + c.stack;
//		for (item <- sig.datatypes)
//			c = importADT(item.adtName, item.adtType, item.at, publicVis(), false, c);
//		for (item <- sig.aliases)
//			c = importAlias(item.aliasName, item.aliasType, item.aliasedType, item.at, publicVis(), false, c);
//		for (item <- sig.tags)
//			c = importTag(item.tagName, item.tagKind, item.taggedTypes, item.at, publicVis(), false, c);
//		c.stack = tail(c.stack);
//	}
//
//	// Now, descend into each alias, ADT, and tag, ensuring all parameters are correctly
//	// added and the aliased type is handled correctly.
//	for (modName <- imports) {
//		sig = sigMap[modName];
//		c.stack = currentModuleId + c.stack;
//		for (item <- sig.datatypes)
//			c = importADT(item.adtName, item.adtType, item.at, publicVis(), true, c);
//		for (item <- sig.aliases)
//			c = importAlias(item.aliasName, item.aliasType, item.aliasedType, item.at, publicVis(), true, c);
//		for (item <- sig.tags)
//			c = importTag(item.tagName, item.tagKind, item.taggedTypes, item.at, publicVis(), true, c);
//		c.stack = tail(c.stack);
//	}
//
//	// Add constructors next, ensuring they are visible for the imported functions.
//	// NOTE: This is one area where we could have problems. Once the checker is working
//	// correctly, TODO: calculate the types in the signature, so we don't risk clashes
//	// over constructor names (or inadvertent visibility of constructor names) that would
//	// not have been an issue before, when we did not have parameters with patterns.
//	for (modName <- imports) {
//		sig = sigMap[modName];
//		c.stack = currentModuleId + c.stack;
//		for (item <- sig.publicConstructors)
//			c = importConstructor(item.conName, item.adtType, item.argTypes, item.commonParams, item.keywordParams, item.adtAt, item.at, publicVis(), c);
//		c.stack = tail(c.stack);
//	}
//    
//	// Now, bring in all public names, including annotations, public vars, and public functions.
//	for (modName <- imports) {
//		sig = sigMap[modName];
//		c.stack = currentModuleId + c.stack;
//		for (item <- sig.publicVariables)
//			c = importVariable(item.variableName, item.variableType, item.at, publicVis(), c);
//		for (item <- sig.publicFunctions)
//			c = importFunction(item.functionName, item.sig, item.at, publicVis(), c);
//		for (item <- sig.annotations)
//			c = importAnnotation(item.annName, item.annType, item.onType, item.at, publicVis(), c);
//		c.stack = tail(c.stack);
//	}
//    
//	c = pushTiming(c, "Imported module signatures", dt1, now());
//
//	// Enter the context for the current module.
//	c.stack = currentModuleId + c.stack;
//
//	// Process syntax declarations.
//	list[Tree] sdecls = [ ];
//	for (d <- syntaxDecls) {
//		try {
//			sdecls += parseSyntaxDeclaration(d);
//		} catch perror : {
//			c = addScopeError(c, "Cannot parse syntax declaration <d>", |unknown:///|);
//		}
//	}
//
//	syntaxConfig = processSyntax(moduleName, sdecls);
//	for (item <- syntaxConfig.lexicalNonterminals + syntaxConfig.contextfreeNonterminals + syntaxConfig.layoutNonterminals + syntaxConfig.keywordNonterminals)
//		c = importNonterminal(item.sortName, item.sort, item.at, c);
//	for (prodItem <- syntaxConfig.publicProductions) {
//		// First, resolve names in the productions
//		<p,c> = resolveProduction(prodItem.prod, prodItem.at, c, false);
//		prodItem.prod = p;
//		c = importProduction(prodItem, c);
//	}
//
//	// Process the declarations. These are assumed to be at the module
//	// level for the artificial module we are working in. To avoid
//	// order dependencies, we process these just like normal declarations,
//	// first doing just the outer parts, then descending into the decl.
//	list[Tree] decls = [ ];
//	for (d <- initialDecls) {
//		try {
//			decls += parseDeclaration(d);
//		} catch perror : {
//			c = addScopeError(c, "Cannot parse declaration <d>", |unknown:///|);
//		}
//	}
//		
//	for (d <- decls) c = checkDeclaration(d, false, c);
//	for (d <- decls) c = checkDeclaration(d, true, c);

	// Now, parse each statement, then check them in turn, using the environment
	// build above (including all imports and declarations).	
	if (RSimpleName("CheckStatementsString") in c.modEnv) {
		rt = Symbol::\void();
		list[Tree] stmts = [ ];
		try {
			if ((Statement)`{ < Statement* sl > }` := parseStatement("{ <statementsString> }"))
				stmts = [ s | s <- sl ];			
		} catch perror : {
			c = addScopeError(c, "Cannot parse statement <statementsString>",  |unknown:///|);
		}
		
		// Re-enter module scope
		c.stack = c.modEnv[RSimpleName("CheckStatementsString")] + c.stack;
		
		for (Statement stmt <- stmts) < c, rt > = checkStmt(stmt, c);
		
		c.stack = tail(c.stack);

		return < c, rt >;
	}
	
	return < c, Symbol::\void() >;
}

public Symbol getTypeForName(Configuration c, str name) {
	splitName = split("::", trim(name));
	splitRN = (size(splitName)==1) ? RSimpleName(splitName[0]) : RCompoundName(splitName);
	if (splitRN in c.fcvEnv) return c.store[c.fcvEnv[splitRN]].rtype;
	throw "Name <prettyPrintName(splitRN)> not found";
}

public set[RName] getVariablesInScope(Configuration c) {
	return { n | l <- c.fcvEnv, i:variable(n,_,_,_,_) := c.store[c.fcvEnv[l]] };
}

public set[RName] getFunctionsInScope(Configuration c) {
	return { n | l <- c.fcvEnv, i:function(n,_,_,_,_,_,_,_) := c.store[c.fcvEnv[l]] };
}

public set[AbstractValue] getPatternVariableValues(Configuration c) {
	return { i | i:variable(_,_,_,n,_) <- c.store<1>, booleanScope(_,_) := c.store[n] };
}

public map[RName,Symbol] getPatternVariables(Configuration c) {
	return ( n : t | variable(n,t,_,_,_) <- getPatternVariableValues(c) );
}


public set[Message] getFailureMessages(CheckResult r) {
   if(failure(set[Message] msgs) := r.res){
      return msgs + getErrorMessages(r);
   }	  
   return getErrorMessages(r);
}

public set[Message] getErrorMessages(CheckResult r){
  return { m | m <- r.conf.messages, m is error };
}

public set[Message] getWarningMessages(CheckResult r){
  return { m | m <- r.conf.messages, m is warning };
}

public set[Message] getAllMessages(CheckResult r) = getFailureMessages(r) + getWarningMessages(r);

