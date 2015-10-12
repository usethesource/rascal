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

public loc cachedImports(loc src, loc bindir) = getDerivedLocation(src, "imps", bindir = bindir);

public CachedImportInfo getCachedImports(loc src, loc bindir) = readBinaryValueFile(#CachedImportInfo, cachedImports(src,bindir));

public void writeCachedImports(loc src, loc bindir, datetime dt, ImportsInfo imps) {
	l = cachedImports(src,bindir);
	if (!exists(l.parent)) mkDirectory(l.parent);
	writeBinaryValueFile(l, ciInfo(dt,imps), compression=false); 
}

alias ImportGraph = Graph[RName];

public tuple[ImportGraph ig, map[RName,ImportsInfo] infomap] getImportGraphAndInfo(Module m, bool removeExtends=false, rel[RName mname, bool isext] defaultImports={}, loc bindir = |home:///bin|) {
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
	
	return getImportGraphAndInfo(mname, minfoMap, removeExtends=removeExtends, defaultImports=defaultImports, bindir=bindir);
}

public tuple[ImportGraph ig, map[RName,ImportsInfo] infomap] getImportGraphAndInfo(RName mname, map[RName,ImportsInfo] minfoMap, bool removeExtends=false, rel[RName mname, bool isext] defaultImports={}, loc bindir = |home:///bin|) {
	// Build an initial worklist based on the imports of module mname
	worklist = { convertNameString(wli) | wli <- (minfoMap[mname].importedModules + minfoMap[mname].extendedModules) };
	set[RName] worked = { mname };
	
	// Get the imports of everything transitively reachable through m
	while (!isEmpty(worklist)) {
		< wlName, worklist > = takeOneFrom(worklist);
		try {
			wlLoc = getModuleLocation(prettyPrintName(wlName));
					
			if (exists(cachedImports(wlLoc,bindir))) {
				ci = getCachedImports(wlLoc, bindir);
				if (ci.dt == lastModified(wlLoc)) {
					minfoMap[wlName] = ci.ii;
				}
			}
			
			if (wlName notin minfoMap) {
				dt = lastModified(wlLoc);
				pt = getModuleParseTree(prettyPrintName(wlName));
				if (Module mImp := pt.top) {
					minfoMap[wlName] = getImports(mImp);

					for (<mn,ie> <- defaultImports, mn != wlName, prettyPrintName(mn) notin minfoMap[wlName].extendedModules, prettyPrintName(mn) notin minfoMap[wlName].importedModules) {
						if (ie) {
							minfoMap[wlName].extendedModules = minfoMap[wlName].extendedModules + prettyPrintName(mn);
						} else {
							minfoMap[wlName].importedModules = minfoMap[wlName].importedModules + prettyPrintName(mn);		
						}
					}

					writeCachedImports(wlLoc, bindir, dt, minfoMap[wlName]); 
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
