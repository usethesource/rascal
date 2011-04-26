@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser
module lang::rascal::grammar::Modules

import lang::rascal::grammar::Productions;

// reading in definitions     
public Grammar modules2grammar(str main, set[Module] modules) {
  return fuseDefinition(modules2definition(main, modules));
}

public GrammarDefinition modules2definition(str main, set[Module] modules) {
  return \definition(main, {module2grammar(m) | m <- modules});
}

public Grammar fuseDefinition(GrammarDefinition def) {
  grammar = grammar({},());
  for (/ \module(_,_,g) := def) 
    grammar = compose(grammar, g);
  return grammar;
}

@doc{
  Converts the syntax definitions of a module to a grammar.
  Note that this function does not implement the imports of a module
} 
public GrammarModule module2grammar(Module mod) {
  <name, imports, extends> = getModuleMetaInf(mod);
  return \module(name, imports, extends, syntax2grammar(collect(mod)));
} 

public tuple[str, set[str]] getModuleMetaInf(mod) {
  if ((Module) `module <QualifiedName name> <ModuleParameters _> <Import* is> <Body _>` := mod) {
    return <"<name>", { "<i>" | (Import) `import <QualifiedName  i>;` <- is } 
                    , { "<i>" | (Import) `extend <QualifiedName  i>;` <- is }>; 
  }
  if ((Module) `module <QualifiedName name> <Import* is> <Body _>` := mod) {
    return <"<name>", { "<i>" | (Import) `import <QualifiedName  i>;` <- is } 
                    , { "<i>" | (Import) `extend <QualifiedName  i>;` <- is }>; 
  }
  
  throw "unexpected module syntax <mod>";
} 
 
public Grammar imports2grammar(set[Import] imports) {
  return syntax2grammar({ s | (Import) `<SyntaxDefinition s>` <- imports});
}
 
private set[SyntaxDefinition] collect(Module mod) {
  set[SyntaxDefinition] result = {};
  
  top-down-break visit (mod) {
    case SyntaxDefinition s : result += s; 
    case Body b => b
  }
  return result;
}  