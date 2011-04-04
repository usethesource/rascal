@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::checker::Import

import lang::rascal::\old-syntax::RascalForImportExtraction;

public set[str] importedModules(Module m) {
  return { "<i>" | /(Import) `import <QualifiedName i>;` := m };
}  

public Module linkImportedModules(Module m, map[str, loc] links) {
  return visit(m) {
    case QualifiedName i:
       if ("<i>" in links)
         insert i[@link=links["<i>"]];
  }
} 
