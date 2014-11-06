@license{
  Copyright (c) 2009-2014 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - mhills@cs.ecu.edu (ECU)}
@bootstrapParser
module lang::rascal::types::CheckModule

import lang::rascal::meta::ModuleInfo;
import lang::rascal::\syntax::Rascal;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::grammar::definition::Productions;
import analysis::graphs::Graph;
import util::Reflective;
import String;
import Set;

alias ImportGraph = Graph[RName];

public tuple[ImportGraph ig, map[RName,ImportsInfo] infomap] getImportGraphAndInfo(Module m, bool removeExtends=false, rel[RName mname, bool isext] extraImports={}) {
	// Set up the imports for the "top" module
	mname = convertName(m.header.name);
	minfoMap = ( mname : getImports(m) );
	
	// Add in any defaults that are not currently present
	for (<mn,ie> <- extraImports) {
		if (ie) {
			minfoMap[mname].extendedModules = minfoMap[mname].extendedModules + prettyPrintName(mn);
		} else {
			minfoMap[mname].importedModules = minfoMap[mname].importedModules + prettyPrintName(mn);		
		}
	}
	
	// Build an initial worklist based on the imports of m
	worklist = { convertNameString(wli) | wli <- (minfoMap[mname].importedModules + minfoMap[mname].extendedModules) };
	set[RName] worked = { mname };
	
	// Get the imports of everything transitively reachable through m
	while (!isEmpty(worklist)) {
		< wlName, worklist > = takeOneFrom(worklist);
		try {
			pt = getModuleParseTree(prettyPrintName(wlName));
			if (Module mImp := pt.top) {
				minfoMap[wlName] = getImports(mImp);
			} else {
				throw "Unexpected parse, pt is not a module";
			}
		} catch : {
			;
		}		
		if (wlName in minfoMap) {
			worked = worked + wlName;
			worklist = worklist + ( { convertNameString(wli) | wli <- (minfoMap[wlName].importedModules + minfoMap[wlName].extendedModules) } - worked );		
		}
	}
	
	if (removeExtends) {
		// Remove extends -- this will convert them all to the proper imports
		solve(minfoMap) {
			for (mi <- minfoMap, !isEmpty(minfoMap[mi].extendedModules)) {
				emods = minfoMap[mi].extendedModules;
				minfoMap[mi].extendedModules = { };
				for (em <- emods) {
					emname = convertNameString(em);
					minfoMap[mi].importedModules = minfoMap[mi].importedModules + minfoMap[emname].importedModules;
					minfoMap[mi].extendedModules = minfoMap[mi].extendedModules + minfoMap[emname].extendedModules; 
				}
			}
		}
	}
		
	// Build a graph based on imports information
	if (removeExtends) {
		return < { < mi, convertNameString(n) > | mi <- minfoMap, n <- minfoMap[mi].importedModules }, minfoMap >;
	} else {
		return < { < mi, convertNameString(n) > | mi <- minfoMap, n <- (minfoMap[mi].importedModules + minfoMap[mi].extendedModules) }, minfoMap >;
	}
}

alias TypeNameInfo = tuple[set[RName] dataNames, set[RName] aliasNames, set[RName] syntaxNames];

public TypeNameInfo extractTypeNames(Module m) {
	set[RName] dataNames = { };
	set[RName] aliasNames = { };
	set[RName] syntaxNames = { };
	
	void addSym(Symbol sym) {
      switch (sym) {
        case sort(str name) : 
          syntaxNames = syntaxNames + RSimpleName(name);
        case lex(str name) : 
          syntaxNames = syntaxNames + RSimpleName(name);
        case layouts(str name) : 
          syntaxNames = syntaxNames + RSimpleName(name);
        case keywords(str name) : 
          syntaxNames = syntaxNames + RSimpleName(name);
        case \parameterized-sort(str name, list[Symbol] parameters) : 
          syntaxNames = syntaxNames + RSimpleName(name);
        case \parameterized-lex(str name, list[Symbol] parameters) : 
          syntaxNames = syntaxNames + RSimpleName(name);
        case \start(Symbol ssym) : 
          addSym(ssym);
      }
	}
	
	for (ti <- m.header.imports, ti is \syntax) {
		rule = rule2prod(ti.\syntax);
		for (/pr:prod(_,_,_) <- rule.prods) {
		      sym = pr.def is label ? pr.def.symbol : pr.def;
		      addSym(sym);	
		}
	}
	
	for (ti <- m.body.toplevels) {
		if ((Declaration)`<Tags _> <Visibility _> data <UserType user>;` := ti.declaration) {
			dataNames = dataNames + convertName(user.name);
		} else if ((Declaration)`<Tags _> <Visibility _> data <UserType ut> <CommonKeywordParameters ckps> = <{Variant "|"}+ _>;` := ti.declaration) {
			dataNames = dataNames + convertName(ut.name);
		} else if ((Declaration)`<Tags _> <Visibility _> alias <UserType ut> = <Type base>;` := ti.declaration) {
			aliasNames = aliasNames + convertName(ut.name);
		} 
	}
	
	return < dataNames, aliasNames, syntaxNames >;
}

public set[RName] extractDataTypeNames(Module m) {
	return extractTypeNames(m).dataNames;	
}

public set[RName] extractAliasNames(Module m) {
	return extractTypeNames(m).aliasNames;	
}

public set[RName] extractSyntaxNames(Module m) {
	return extractTypeNames(m).syntaxNames;	
}
