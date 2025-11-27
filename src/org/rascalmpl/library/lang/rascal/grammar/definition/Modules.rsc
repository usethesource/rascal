@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::grammar::definition::Modules

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Productions;
import lang::rascal::grammar::definition::Layout;
import lang::rascal::grammar::definition::Literals; 
import lang::rascal::grammar::definition::Names;
import Grammar;
import Set;

@memo
@synopsis{Converts internal module representation of Rascal interpreter to single grammar definition}
public Grammar modules2grammar(str main, map[str name, tuple[set[str] imports, set[str] extends, set[SyntaxDefinition] defs] \mod] mods) {
  // note that we ignore extends here because they're resolved by the interpreter at the moment by 
  // cloning definitions into the module that extends.
  def = \definition(main, (m:\module(m, 
                                    mods[m].imports, 
                                    mods[m].extends, 
                                    syntax2grammar(mods[m].defs)
                                    ) 
                          | m <- mods));
  return resolve(fuse(layouts(def)));
}

@memo
@synopsis{Converts concrete syntax definitions and fuses them into one single grammar definition}     
public Grammar modules2grammar(str main, set[Module] modules) {
  return resolve(fuse(layouts(modules2definition(main, modules))));
}

@synopsis{Converts concrete syntax definitions to abstract grammar definitions}
public GrammarDefinition modules2definition(str main, set[Module] modules) {
  return \definition(main, (\mod.name:\mod | m <- modules, \mod := module2grammar(m)));
}

@synopsis{Combines a set of modules into one big Grammar, projecting only the rules that
  are visible locally, or via import and extend.}
public Grammar fuse(GrammarDefinition def) {
  result = grammar({},());
  todo = {def.main};
  done = {};
  deps = dependencies(def);
  
  while (todo != {}) {
    <nm,todo> = takeOneFrom(todo);
    done += nm; 
    if(def.modules[nm]?){
        \mod = def.modules[nm];
        result = (compose(result, \mod.grammar) | compose(it, def.modules[i].grammar) | i <- deps[nm], def.modules[i]?);
        todo += (\mod.extends - done);
    }
  }
  
  return result;
}
 


public GrammarModule module2grammar(Module \mod) {
  <nm, imps, exts> = getModuleMetaInf(\mod);
  return \module(nm, imps, exts, syntax2grammar(collect(\mod)));
} 

public tuple[str, set[str], set[str]] getModuleMetaInf(Module \mod) {
  // TODO: implement module type parameters
  // Tags tags "module" QualifiedName name ModuleParameters params Import* imports
  switch (\mod) {
    case \default(parameters(_, QualifiedName name, _, Import* is),_) :
    return <deslash("<name>"), { "<i>" | \default(\default(QualifiedName i)) <- is } 
                    , { "<i>" | \extend(\default(QualifiedName i)) <- is }>;
    case \default(\default(_, QualifiedName name, Import* is), _) : 
    return <deslash("<name>"), { "<i>" |  \default(\default(QualifiedName i)) <- is } 
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

public Grammar imports2grammar(set[Import] imports) {
  return syntax2grammar({ s | \syntax(SyntaxDefinition s) <- imports});
}
 
private set[SyntaxDefinition] collect(Module \mod) {
  set[SyntaxDefinition] result = {};
  
  top-down-break visit (\mod) {
    case SyntaxDefinition s : result += s; 
    case Body b => b
  }
  return result;
}  
