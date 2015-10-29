@license{
  Copyright (c) 2009-2015 CWI
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
import IO;
import ValueIO;


data CachedImportInfo = ciInfo(datetime dt, ImportsInfo ii);

public tuple[bool,loc] cachedImportsReadLoc(str qualfiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualfiedModuleName, "imps", pcfg);
public loc cachedImportsWriteLoc(str qualfiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualfiedModuleName, "imps", pcfg);


public CachedImportInfo getCachedImports(str qualfiedModuleName, PathConfig pcfg) {
    if(<true, l> := cachedImportsReadLoc(qualfiedModuleName,pcfg)){
       return readBinaryValueFile(#CachedImportInfo, l);
    }
    throw "getCachedImports: no CachedImports found for <qualfiedModuleName>";
}    

public void writeCachedImports(str qualfiedModuleName, PathConfig pcfg, datetime dt, ImportsInfo imps) {
	l = cachedImportsWriteLoc(qualfiedModuleName, pcfg);
	if (!exists(l.parent)) mkDirectory(l.parent);
	writeBinaryValueFile(l, ciInfo(dt,imps), compression=false); 
}

alias ImportGraph = Graph[RName];

public tuple[ImportGraph ig, map[RName,ImportsInfo] infomap] getImportGraphAndInfo(Module m, PathConfig pcfg, bool removeExtends=false, rel[RName mname, bool isext] defaultImports={}) {
	// Set up the imports for the "top" module
	mname = convertName(m.header.name);
	minfoMap = ( mname : getImports(m) );
	
	// Add in any defaults that are not currently present
	for (<mn,ie> <- defaultImports, mn != mname, prettyPrintName(mn) notin minfoMap[mname].extendedModules, prettyPrintName(mn) notin minfoMap[mname].importedModules) {
		if (ie) {
			minfoMap[mname].extendedModules = minfoMap[mname].extendedModules + prettyPrintName(mn);
		} else {
			minfoMap[mname].importedModules = minfoMap[mname].importedModules + prettyPrintName(mn);		
		}
	}
	
	return getImportGraphAndInfo(mname, minfoMap, pcfg, removeExtends=removeExtends, defaultImports=defaultImports);
}

public tuple[ImportGraph ig, map[RName,ImportsInfo] infomap] getImportGraphAndInfo(RName mname, map[RName,ImportsInfo] minfoMap, PathConfig pcfg, bool removeExtends=false, rel[RName mname, bool isext] defaultImports={}) {
	// Build an initial worklist based on the imports of module mname
	worklist = { convertNameString(wli) | wli <- (minfoMap[mname].importedModules + minfoMap[mname].extendedModules) };
	set[RName] worked = { mname };
	
	// Get the imports of everything transitively reachable through m
	while (!isEmpty(worklist)) {
		< wlName, worklist > = takeOneFrom(worklist);
		try {
		    ppWlName = prettyPrintName(wlName);
			wlLoc = getModuleLocation(ppWlName,pcfg);
					
			if (<true, _> := cachedImportsReadLoc(ppWlName,pcfg)) {
				ci = getCachedImports(ppWlName, pcfg);
				if (ci.dt == lastModified(wlLoc)) {
					minfoMap[wlName] = ci.ii;
				}
			}
			
			if (wlName notin minfoMap) {
				dt = lastModified(wlLoc);
				pt = getModuleParseTree(ppWlName, pcfg);
				if (Module mImp := pt.top) {
					minfoMap[wlName] = getImports(mImp);

					for (<mn,ie> <- defaultImports, mn != wlName, prettyPrintName(mn) notin minfoMap[wlName].extendedModules, prettyPrintName(mn) notin minfoMap[wlName].importedModules) {
						if (ie) {
							minfoMap[wlName].extendedModules = minfoMap[wlName].extendedModules + prettyPrintName(mn);
						} else {
							minfoMap[wlName].importedModules = minfoMap[wlName].importedModules + prettyPrintName(mn);		
						}
					}

					writeCachedImports(ppWlName, pcfg, dt, minfoMap[wlName]); 
				} else {
					throw "Unexpected parse, pt is not a module";
				}
			}
		} catch x : {
			; // TODO: Add code here to handle cases where we could not find an imported module, generally we can leave this to the checker though...
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
