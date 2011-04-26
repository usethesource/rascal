@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser

module lang::rascal::grammar::definition::Layout

import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::definition::Modules;
import Grammar;



@doc{computes which layout definitions are visible in a certain given module.
     if a module contains a layout definition, this overrides any imported layout definition
     if a module does not contain a layout definition, it will collect the definitions from all imports (not recursively),
     and also collect the definitions from all extends (recursively).
     the static checker should check whether multiple visible layout definitions are active, because this function
     will just produce an arbitrary one if there are multiple definitions
}
public Symbol activeLayout(str name, GrammarDefinition def) {
  GrammarModule mod = def.modules[name];
  
  if (/prod(_,l:layouts(_),_) := mod) 
    return l;
  else if ({l, _*} := extendedLayouts(mod.extends, def)) 
    return l;
  else if ({l, _*} := importedLayouts(mod.imports, def))
    return l;
  else 
    return layouts(empty()); 
}  

private set[Symbol] importedLayouts(set[str] imports, GrammarDefinition def) {
  return {l | i <- imports, m <- def.modules[i], /prod(_,l:layouts(_),_) := m};
}

private set[Symbol] importedLayouts(set[str] extends, GrammarDefinition def) {
  return importedLayouts(extendsClosure(extends, def), def);
}

@doc{intersperses layout symbols in all non-lexical productions}
public set[Production] \layouts(Grammar g, Symbol l) {
  return top-down-break visit (g) {
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) => prod(intermix(lhs, l),rhs,attrs(as)) 
      when start(_) !:= rhs, \lex() notin as  
    case prod(list[Symbol] lhs,Symbol rhs,\no-attrs()) => prod(intermix(lhs, l),rhs,\no-attrs()) 
      when start(_) !:= rhs
  }
}

private list[Symbol] intermix(list[Symbol] syms, Symbol l) {
  if (syms == []) 
    return syms;
  return tail([l, s | s <- syms]);
}
