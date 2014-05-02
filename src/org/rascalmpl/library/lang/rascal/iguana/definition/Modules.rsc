@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::iguana::definition::Modules

import lang::rascal::\syntax::Rascal;
import lang::rascal::iguana::definition::Productions;
import lang::rascal::iguana::definition::Layout;
import lang::rascal::iguana::definition::Literals; 
import lang::rascal::iguana::definition::Names;
import Grammar;
import Set;

@memo
@doc{Converts internal module representation of Rascal interpreter to single iguana definition}
public Grammar modules2iguana(str main, map[str name, tuple[set[str] imports, set[str] extends, set[SyntaxDefinition] defs] \mod] mods) {
  // note that we ignore extends here because they're resolved by the interpreter at the moment by 
  // cloning definitions into the module that extends.
  def = \definition(main, (m:\module(m, 
                                    mods[m].imports, 
                                    mods[m].extends, 
                                    syntax2iguana(mods[m].defs)
                                    ) 
                          | m <- mods));
  return fuse(layouts(resolve(def)));
}

@memo
@doc{Converts concrete syntax definitions and fuses them into one single iguana definition}     
public Grammar modules2iguana(str main, set[Module] modules) {
  return fuse(layouts(resolve(modules2definition(main, modules))));
}

@doc{Converts concrete syntax definitions to abstract iguana definitions}
public GrammarDefinition modules2definition(str main, set[Module] modules) {
  return \definition(main, (\mod.name:\mod | m <- modules, \mod := module2iguana(m)));
}

@doc{
  Combines a set of modules into one big Grammar, projecting only the rules that
  are visible locally, or via import and extend.
}
public Grammar fuse(GrammarDefinition def) {
  result = iguana({},(),());
  todo = {def.main};
  done = {};
  
  while (todo != {}) {
    <name,todo> = takeOneFrom(todo);
    \mod = def.modules[name];
    done += name; 
    result = (compose(result, \mod.iguana) | compose(it, def.modules[i].iguana) | i <- \mod.imports + \mod.extends);
    todo += (\mod.extends - done);
  }
  
  return result;
}
 


public GrammarModule module2iguana(Module \mod) {
  <name, imps, exts> = getModuleMetaInf(\mod);
  return \module(name, imps, exts, syntax2iguana(collect(\mod)));
} 

public tuple[str, set[str], set[str]] getModuleMetaInf(Module \mod) {
  // TODO: implement module type parameters
  // Tags tags "module" QualifiedName name ModuleParameters params Import* imports
  switch (\mod) {
    case \default(parameters(_, QualifiedName name, _, Import* is),_) :
    return <deslash("<name>"), { "<i>" | \import(\default(QualifiedName i)) <- is } 
                    , { "<i>" | \extend(\default(QualifiedName i)) <- is }>;
    case \default(\default(_, QualifiedName name, Import* is), _) : 
    return <deslash("<name>"), { "<i>" |  \import(\default(QualifiedName i)) <- is } 
                    , { "<i>" | \extend(\default(QualifiedName i)) <- is }>; 
  }
  
  throw "unexpected module syntax <\mod>";
} 

public set[SyntaxDefinition] getModuleSyntaxDefinitions(Module \mod) = collect(\mod);
 
str deslash(str input) {
  return visit(input) {
    case /\\/ => ""
  }
}

public Grammar imports2iguana(set[Import] imports) {
  return syntax2iguana({ s | \syntax(SyntaxDefinition s) <- imports});
}
 
private set[SyntaxDefinition] collect(Module \mod) {
  set[SyntaxDefinition] result = {};
  
  top-down-break visit (\mod) {
    case SyntaxDefinition s : result += s; 
    case Body b => b
  }
  return result;
}  
