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
import util::UUID;
import util::Math;

public CheckResult checkStatementsString(str statementsString, PathConfig pcfg, list[str] importedModules = [], list[str] initialDecls = [], list[str] syntaxDecls = []) {
    str modName = "CheckStatementsString";
    
	str moduleToCheck =
		"@bootstrapParser
		'module <modName>
		'<for (im <- importedModules) {>
		'import <im>;<}>
		'<for (sd <- syntaxDecls) {>
		'<sd><}>
		'<for (id <- initialDecls) {>
		'<id><}>
		";
    moduleLoc = |test-modules:///<modName>.rsc|;
    writeFile(moduleLoc, moduleToCheck);

	c = newConfiguration(pcfg);
	try {
		pt = parseModuleWithSpaces(moduleLoc);

		if (pt has top && lang::rascal::\syntax::Rascal::Module m := pt.top) {
			c = checkModule(m, c);
		} else {
			c = addScopeError(c, "Unexpected parse result for module to check <pt>", moduleLoc); 
		}
	} catch perror : {
		c = addScopeError(c, "Could not parse and prepare config for base module to check: <perror>", moduleLoc);
	}
			
	// Now, parse each statement, then check them in turn, using the environment
	// build above (including all imports and declarations).	
	if (RSimpleName(modName) in c.modEnv) {
		rt = Symbol::\void();
		list[Tree] stmts = [ ];
		try {
			if ((Statement)`{ < Statement* sl > }` := parseStatement("{ <statementsString> }"))
				stmts = [ s | s <- sl ];			
		} catch perror : {
			c = addScopeError(c, "Cannot parse statement <statementsString>",  |unknown:///|);
		}
		
		// Re-enter module scope
		c.stack = c.modEnv[RSimpleName(modName)] + c.stack;
		
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

