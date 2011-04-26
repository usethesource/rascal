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
import lang::rascal::grammar::Layout;
import lang::rascal::grammar::Literals;

@doc{Converts concrete syntax definitions and fuses them into one single grammar definition}     
public Grammar modules2grammar(str main, set[Module] modules) {
  return fuseDefinition(layouts(modules2definition(main, modules)));
}

@doc{Converts concrete syntax definitions to abstract grammar definitions}
public GrammarDefinition modules2definition(str main, set[Module] modules) {
  return \definition(main, (mod.name:mod | m <- modules, mod := module2grammar(m)));
}

@doc{Combines a set of modules into one big Grammar.}
public Grammar fuseDefinition(GrammarDefinition def) {
  grammar = grammar({},());
  for (name <- def.modules) 
    grammar = compose(grammar, def.modules[name].grammar);
  return grammar;
}

public rel[str, str] extends(GrammarDefinition def) {
  return {<m,e> | m <- def, \module(_, _, exts , _) := def.modules[m], e <- exts}+;
}

public rel[str,str] imports(GrammarDefinition def) {
  return {<m,i> | m <- def, \module(_, imps, _ , _) := def.modules[m], i <- imps};
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