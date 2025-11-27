@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - mhills@cs.ecu.edu (ECU)}
@bootstrapParser
module lang::rascal::meta::ModuleInfo

import lang::rascal::\syntax::Rascal;

data ImportsInfo = importsInfo(set[str] importedModules, set[str] extendedModules);

public ImportsInfo getImports((Module)`<Header h> <Body _>`) {
	set[str] importedModules = { };
	set[str] extendedModules = { };
	
	for (i <- h.imports, i has \module) {
		if (i is \extend) {
			extendedModules = extendedModules + "<i.\module.name>";
		} else {
			importedModules = importedModules + "<i.\module.name>";
		}
	}
	
	return importsInfo(importedModules, extendedModules);
}
