module lang::rascalcore::check::Test4

import lang::rascal::\syntax::Rascal;
import Grammar;
import ParseTree;
import List;
import IO;

@doc{intermixes the actively visible layout definition in each module into the relevant syntax definitions}
GrammarDefinition \layouts(GrammarDefinition def) {
  deps = extends(def) + imports(def);
  for (str name <- def.modules) {
    def.modules[name].grammar 
      = layouts(def.modules[name].grammar, 
                activeLayout(name, deps[name], def), 
                allLayouts(deps[name] + {name}, def)
               );
  }
  return def;
}

@doc{collects for a set of modules the names of all layout sorts and returns them as sorts for later processing} 
set[Symbol] allLayouts(set[str] defs, GrammarDefinition def) 
  = {sort(l) | m <- defs, /prod(layouts(str l),_,_) := def.modules[m]} 
  + {sort(l) | m <- defs, /prod(label(_,layouts(str l)),_,_) := def.modules[m]} 
  ;

// TODO: The following two functions were defined local to activeLayout
// but this gives an not yet explained validation error  for the
// function ids in the corresponding overloaded function
bool isManual(set[Attr] as) = (\tag("manual"()) in as);
bool isDefault(Symbol s) = (s == layouts("$default$"));
   
@doc{computes which layout definitions are visible in a certain given module.
     if a module contains a layout definition, this overrides any imported layout definition
     if a module does not contain a layout definition, it will collect the definitions from all imports (not recursively),
     and also collect the definitions from all extends (recursively).
     the static checker should check whether multiple visible layout definitions are active, because this function
     will just produce an arbitrary one if there are multiple definitions
}
Symbol activeLayout(str name, set[str] deps, GrammarDefinition def) {

  
  if (/prod(l:layouts(_),_,as) := def.modules[name], !isDefault(l), !isManual(as)) 
    return l;
  else if (/prod(label(_,l:layouts(_)),_,as) := def.modules[name], !isDefault(l), !isManual(as)) 
    return l;  
  else if (i <- deps, /prod(l:layouts(_),_,as) := def.modules[i], !isDefault(l), !isManual(as)) 
    return l;
   else if (i <- deps, /prod(label(_,l:layouts(_)),_,as) := def.modules[i], !isDefault(l), !isManual(as)) 
    return l;  
  else 
    return layouts("$default$"); 
}  

@doc{intersperses layout symbols in all non-lexical productions}
public Grammar \layouts(Grammar g, Symbol l, set[Symbol] others) {
  return top-down-break visit (g) {
    case prod(\start(y),[Symbol x],as) => prod(\start(y),[l, x, l],  as)
    case prod(sort(s),list[Symbol] lhs,as) => prod(sort(s),intermix(lhs, l, others),as)
    case prod(\parameterized-sort(s,n),list[Symbol] lhs,as) => prod(\parameterized-sort(s,n),intermix(lhs, l, others),as)
    case prod(label(t,sort(s)),list[Symbol] lhs,as) => prod(label(t,sort(s)),intermix(lhs, l, others),as)
    case prod(label(t,\parameterized-sort(s,n)),list[Symbol] lhs,as) => prod(label(t,\parameterized-sort(s,n)),intermix(lhs, l, others),as) 
  }
} 

list[Symbol] intermix(list[Symbol] syms, Symbol l, set[Symbol] others) {
  if (syms == []) 
    return syms;
    
  syms = [ sym is layouts ? sym : regulars(sym, l, others) | sym <- syms ];
  others += {l};
  
  // Note that if a user manually put a layouts symbol, then this code makes sure not to override it and
  // not to surround it with new layout symbols  
  while ([*Symbol pre, Symbol sym1, Symbol sym2, *Symbol pst] := syms, !(sym1 in others || sym2 in others)) {
      syms = [*pre, sym1, l, sym2, *pst];
  }
  
  return syms;
}

private Symbol regulars(Symbol s, Symbol l, set[Symbol] others) {
  return visit(s) {
    case \iter(Symbol n) => \iter-seps(n, [l])
    case \iter-star(Symbol n) => \iter-star-seps(n, [l]) 
    case \iter-seps(Symbol n, [Symbol sep]) => \iter-seps(n,[l,sep,l]) when !(sep in others), !(seq([a,_,b]) := sep && (a in others || b in others))
    case \iter-star-seps(Symbol n,[Symbol sep]) => \iter-star-seps(n, [l, sep, l]) when !(sep in others), !(seq([a,_,b]) := sep && (a in others || b in others))
    case \seq(list[Symbol] elems) => \seq(intermix(elems, l, others))
  }
}