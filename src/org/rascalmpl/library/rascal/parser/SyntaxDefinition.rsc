module RascalGrammar

import rascal::syntax::Rascal;
import Grammar;
import List;
import String;

// join the rules for the same non-terminal
rule merge   grammar(a,{p,q,a*}) => grammar(a,{or({p,q}), a*}) when sort(p) == sort(q);
	
// these rule flatten complex productions and ignore ordering under diff and assoc
rule or     or({a*, or({b*})})            => or({a*, b*}); 
rule xor    xor([a*,xor([b*],c*)])        => xor([a*,b*,c*]); 
rule xor    xor([a*,or({b}),c*])          => xor([a*,b,c*]); 
rule or     or({a*, xor([b])})            => or({a*, b}); 
rule assoc  assoc(a, {a*, or({b*})})      => assoc(a, {a*, b*}); 
rule assoc  assoc(a, {a*, xor([b*])})     => assoc(a, {a*, b*}); // ordering does not work under assoc
rule diff   diff(p, {a*, or({b*})})       => diff(p, {a*, b*});   
rule diff   diff(p, {a*, xor(b*)})        => diff(p, {a*, b*});  // ordering is irrelevant under diff
rule diff   diff(p, {a*, assoc(a, {b*})}) => diff(p, {a*, b*});  // assoc is irrelevant under diff

// move diff outwards
rule or     or({a*, diff(b, {c*})})       => diff(or({a*, b}), {c*});
rule xor    xor([a*, diff(b, {c*})])      => diff(or({xor([a*]), b}), {c*});
rule assoc  assoc(a, {a*, diff(b, {c*})}) => diff(assoc(a, {a*, b}), {c*});
rule diff   diff(p, {a*, diff(q, b*)})    => diff(or({p,q}), {a*, b*}); 
rule diff   diff(diff(a, {b*}), {c*})     => diff(a, {b*, c*});

// character class normalization
rule flip   range(from, to) => range(to, from) when to < from;

rule merge \char-class([a*,range(from1,to1),b*,range(from2,to2),c*]) =>
           \char-class([a*,range(min(from1,from2),max(to1,to2)),b*])
     when (from1 <= from2 - 1 && to1 >= from2 - 1) || (from2 <= from1 - 1 && to2 >= from1 - 1);

rule order \char-class([a*,range(n,m),b*,range(o,p),d*]) =>
           \char-class([a*,range(o,p),b*,range(n,m),d*])
     when p < n;

public Symbol sort(Production p) {
  if (/prod(_,rhs,_) := p) {
    return rhs;
  }
  throw "weird production <p>";
}

public Grammar module2grammar(Module mod) {
  return syntax2grammar(collect(mod));
}
  
public set[SyntaxDefinition] collect(Module mod) {
  set[Module] result = {};
  visit (mod) { case SyntaxDefinition s : result += s; }
  return result;
}  

public Grammar syntax2grammar(set[SyntaxDefinition] def) {
  return grammar({},{ def2prod(p) | sd <- def});
}

public Grammar def2prod(SyntaxDefinition def) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  set[Production] layouts = {};

  switch (def) {
    case `<Tags t> <Visibility v> start syntax <UserType u> = <Prod p>`  : 
      prods += prod2prod(user2symbol(u), p);
    case `<Tags t> <Visibility v> start layout <UserType u> = <Prod p>`  : 
      layouts += prod2prod(user2symbol(u), p);
    case `<Tags t> <Visibility v> syntax <UserType u> = <Prod p>`  : { 
      starts += user2symbol(u);
      prods += prod2prod(user2symbol(u), p);
    }
    default: throw "missed case: <def>";
  }

  return grammar(starts, layout(prods) + layouts + { prod([l],layout(),no-attrs()) | l <- layouts });
}

public set[Production] layout(set[Production] prods) {
  return visit (prods) {
    case p:prod(lhs,rhs,a:attrs(![a*,term(lex()),b*])) => prod(intermix(lhs),rhs,a)
  }
}

public list[Symbol] intermix(list[Symbol] syms) {
  return [s,l | s <- syms, iter-star(layout())] - [l]; 
}


public Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(n, ms));
    case `<ProdModifier* ms> <Symbol* args>` :
      return prod(args2symbols(args), nt, mods2attrs(ms));
    case `<Prod l> | <Prod r>` :
      return or({prod2prod(nt, l), prod2prod(nt, r)});
    case `<Prod l> < <Prod r>` :
      return xor([prod2prod(nt, l), prod2prod(nr, r)]);
    case `<Prod l> - <Prod r>` :
      return diff(prod2prod(nt, l), {prod2prod(nt, r)});
    case `left ( <Prod p> )` :
      return assoc(left(), prod2prod(nt, p));
    case `right ( <Prod p>)` :
      return assoc(right(), prod2prod(nt, p));
    case `non-assoc (<Prod p>)` :
      return assoc(\non-assoc(), prod2prod(nt, p));
    case `assoc(<Prod p>)` :
      return assoc(left(), prod2prod(nt, p));
    case `...`: throw "... operator is not yet implemented";
    case `: <Name n>`: throw "prod referencing is not yet implemented";
    default: throw "missed a case <p>";
  } 
}

public list[Symbol] args2symbols(Sym* args) {
  return [ arg2symbol(s) | Sym s <- args ];
}

public Symbol arg2symbol(Sym sym) {
  switch(sym) {
    case `<Name n>` : return sort("<n>");
    case `<StringLiteral l>` : return lit("<l>");
    case `<<Sym s>>` : return arg2symbol(s);
    case `<<Sym s> <Name n>` : return label("<n>", arg2symbol(s));
    case `<Sym s>?` : return opt(arg2symbol(s));
    case `<Sym s>??` : return opt(arg2symbol(s));
    case `<Sym s>*` : return iter-star(arg2symbol(s));
    case `<Sym s>+` : return iter(arg2symbol(s));
    case `<Sym s>*?` : return iter-star(arg2symbol(s));
    case `<Sym s>+?` : return iter(arg2symbol(s));
    case `<{<Sym s> <StringLiteral sep>}*>` : return \iter-star-sep(arg2symbol(s), lit("<sep>"));
    case `<{<Sym s> <StringLiteral sep>}+>` : return \iter-sep(arg2symbol(s), lit("<sep>"));
    case `<{<Sym s> <StringLiteral sep>}*?>` : return \iter-star-sep(arg2symbol(s), lit("<sep>"));
    case `<{<Sym s> <StringLiteral sep>}+?>` : return \iter-sep(arg2symbol(s), lit("<sep>"));
    case `<CharClass cc>` : return \char-class(cc);
    default: throw "missed a case <sym>";
  }
}

public list[CharRange] cc2ranges(Class cc) {
   switch(cc) {
     case `[<CharRange* ranges>]` : return [range(r) | r <- ranges];
     case `(<CharClass c>)`: return cc2ranges(cc2ranges(c));
     case `!<CharClass c>`: return complement(cc2ranges(c));
     case `<CharClass l> & <CharClass r>`: return intersection(cc2ranges(l),cc2ranges(r));
     case `<CharClass l> + <CharClass r>`: return union(cc2ranges(l),cc2ranges(r));
     case `<CharClass l> - <CharClass r>`: return difference(cc2ranges(l),cc2ranges(r));
     default: throw "missed a case <cc>";
   }
}

public CharRange range(Range r) {
  switch(r) {
    case (Range) `<Character c>` : return range(character(c),character(c));
    case (Range) `<Character l> - <Character r>`: return range(character(l),character(r));
    default: throw "missed a case <r>";
  }
}

public int character(Character c) {
  switch (c) {
    case /<ch:[^"'\\-[] ]>/        : return charAt(ch, 0); 
    case /\\<esc:[-[] ]>/          : return charAt(esc, 0);
    case /\\[u]+<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return toInt("0x<hex>");
    case /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    default: throw "missed a case <c>";
  }
}

public Attributes mods2attrs(Name cons, ProdModifier* mods) {
  return attrs([term(cons("<cons>"))]);
}

public Attributes mods2attrs(ProdModifier* mods) {
  return attrs([mod2attr(m) | `<ProdModifier* p1> <ProdModifier m> <ProdModifer* p2>` := mods]);
}

public Attribute mod2attr(ProdModifier m) {
  switch(m) {
    case `lex`: return term(lex());
    case `left`: return assoc(left());
    case `right`: return assoc(right());
    case `non-assoc`: return assoc(\non-assoc());
    case `assoc`: return assoc(assoc());
    case `bracket`: return bracket();
    default: throw "missed a case <m>";
  }
}

public Symbol user2symbol(UserType u) {
  switch (u) {
   case (UserType) `<Name n>` : return sort("<n>");
   default: throw "missed case: <u>";
  } 
}

list[CharRange] complement(list[CharRange] s) {
  return difference([range(0,0xFFFF)],s);
}

list[CharRange] intersection(list[CharRange] l, list[CharRange] r) {
  return union(difference(l,r),difference(r,l));
} 

list[CharRange] union(list[CharRange] l, list[CharRange] r) {
 return l + r;
}

// TODO: check this code, it was written too late at night
list[CharRange] difference(list[CharRange] l, list[CharRange] r) {
  if (l == [] || r == []) return l;

  <lhead,ltail> = takeOneFrom(r1);
  <rhead,rtail> = takeOneFrom(r2);

  // left beyond right
  // <-right-> --------
  // --------- <-left->
  if (lhead.start > rhead.end) 
    return difference(r1,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.start) 
    return difference(l,rtail);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.start >= rhead.start && lhead.end <= rhead.end) 
    return difference(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.start >= lhead.start && rhead.end <= lhead.end) 
    return difference([range(lhead.start,rhead.start),range(lhead.end,rhead.end)],rtail);

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return difference([range(lhead.start,rhead.start)],r); 
 
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.start > rhead.start)
    return difference([range(lhead.end,rhead.start)], r);

  return result;
}

