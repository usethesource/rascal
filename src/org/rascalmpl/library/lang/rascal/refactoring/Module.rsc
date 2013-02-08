@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@bootstrapParser
module lang::rascal::refactoring::Module

import lang::rascal::\syntax::Rascal;

public Module renameModule(Module m, str new) {
   if ((QualifiedName) `<{Name "::"}* prefix>::<Name _>` := m.header.name) {
     m.header.name = (QualifiedName) `<{Name "::"}* prefix>::<[Name] new>`;
     return m;
   }
   
   throw "can not rename <m.name> to <new>";
}

public Module renameImport(Module m, str old, str new) {
  return visit (m) {
    case (Import) `import <[QualifiedName] old>;` => (Import) `import <[QualifiedName] new>;`
  }
}

public Module removeImport(Module m, str imp) {
  while ((Import*) `<Import* pre> import <[QualifiedName] imp>; <Import* post>` := m.header.imports) {
    m.header.imports = (Import*) `<Import* pre>
<Import* post>`;
  }
  return m;
}

public Module addImport(Module m, str imp) {
  existing = m.header.imports;
  m.header.imports = (Import*) `import <[QualifiedName] imp>;
<Import* existing>`;
  return m;
}

public Module createModule(str name) {
  return (Module) `module <[QualifiedName] name>`;
}
