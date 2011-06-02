@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser
module lang::rascal::grammar::definition::Modules

import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Layout;
import lang::rascal::grammar::definition::Literals;
import lang::rascal::grammar::definition::Names;
import Grammar;

@doc{Converts concrete syntax definitions and fuses them into one single grammar definition}     
public Grammar modules2grammar(str main, set[Module] modules) {
  return resolve(fuse(layouts(modules2definition(main, modules))));
}

@doc{Converts concrete syntax definitions to abstract grammar definitions}
public GrammarDefinition modules2definition(str main, set[Module] modules) {
  return \definition(main, (mod.name:mod | m <- modules, mod := module2grammar(m)));
}

@doc{Combines a set of modules into one big Grammar.}
public Grammar fuse(GrammarDefinition def) {
  return (grammar({},()) | compose(it, def.modules[name].grammar) | name <- def.modules);
}
 
@doc{
  Compose two grammars, by adding the rules of g2 to the rules of g1.
  The start symbols of g1 will be the start symbols of the resulting grammar.
}
public Grammar compose(Grammar g1, Grammar g2) {
  set[Production] empty = {};
  for (s <- g2.rules)
    if (g1.rules[s]?)
      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
    else
      g1.rules[s] = g2.rules[s];
  return g1;
}    

public rel[str, str] extends(GrammarDefinition def) {
  return {<m,e> | m <- def.modules, \module(_, _, exts , _) := def.modules[m], e <- exts}+;
}

public rel[str,str] imports(GrammarDefinition def) {
  return {<m,i> | m <- def.modules, \module(_, imps, _ , _) := def.modules[m], i <- imps};
}

public GrammarModule module2grammar(Module mod) {
  <name, imps, exts> = getModuleMetaInf(mod);
  return \module(name, imps, exts, syntax2grammar(collect(mod)));
} 

public tuple[str, set[str], set[str]] getModuleMetaInf(mod) {
  // TODO: implement module type parameters
  // Tags tags "module" QualifiedName name ModuleParameters params Import* imports
  switch (mod) {
    case (Module) `<Tags _> module <QualifiedName name> <ModuleParameters _> <Import* is> <Body _>` :
    return <"<name>", { "<i>" | (Import) `import <QualifiedName  i>;` <- is } 
                    , { "<i>" | (Import) `extend <QualifiedName  i>;` <- is }>; 
    case (Module) `<Tags _> module <QualifiedName name> <Import* is> <Body _>`:
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