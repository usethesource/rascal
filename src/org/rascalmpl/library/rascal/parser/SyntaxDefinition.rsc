module rascal::parser::SyntaxDefinition

import rascal::syntax::RascalForImportExtraction;
import rascal::parser::Grammar;
import List;
import String;
import ParseTree;

// join the rules for the same non-terminal
rule merge   grammar(a,{p,q,a*}) => grammar(a,{or({p,q}), a*}) when sort(p) == sort(q);
	
// these rule flatten complex productions and ignore ordering under diff and assoc
rule or     choice({a*, choice({b*})})    => choice({a*, b*}); 
rule xor    first([a*,first([b*]),c*])    => first([a*,b*,c*]); 
rule xor    first([a*,choice({b}),c*])    => first([a*,b,c*]); 
rule or     choice({a*, first([b])})      => choice({a*, b}); 
rule assoc  assoc(a, {a*, choice({b*})})  => assoc(a, {a*, b*}); 
rule assoc  assoc(a, {a*, first([b*])})   => assoc(a, {a*, b*}); // ordering does not work under assoc
rule diff   diff(p, {a*, choice({b*})})   => diff(p, {a*, b*});   
rule diff   diff(p, {a*, first(b*)})      => diff(p, {a*, b*});  // ordering is irrelevant under diff
rule diff   diff(p, {a*, assoc(a, {b*})}) => diff(p, {a*, b*});  // assoc is irrelevant under diff

// move diff outwards
rule empty  diff(p,{})                    => p;
rule or     choice({a*, diff(b, {c*})})   => diff(choice({a*, b}), {c*});
rule xor    first([a*, diff(b, {c*}),d*]) => diff(first([a*,b,d*]), {c*});
rule assoc  assoc(a, {a*, diff(b, {c*})}) => diff(assoc(a, {a*, b}), {c*});
rule diff   diff(p, {a*, diff(q, {b*})})  => diff(choice({p,q}), {a*, b*}); 
rule diff   diff(diff(a, {b*}), {c*})     => diff(a, {b*, c*});

// character class normalization
private data CharRange = \empty-range();

rule empty range(from, to) => \empty-range() when to < from;
rule empty \char-class([a*,\empty-range(),b*]) => \char-class([a*,b*]);

rule merge \char-class([a*,range(from1,to1),b*,range(from2,to2),c*]) =>
           \char-class([a*,range(min(from1,from2),max(to1,to2)),b*,c*])
     when (from1 <= from2 && to1 >= from2 - 1) 
       || (from2 <= from1 && to2 >= from1 - 1)
       || (from1 >= from2 && to1 <= to2)
       || (from2 >= from1 && to2 <= to1);

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
  set[SyntaxDefinition] result = {};
  visit (mod) { case SyntaxDefinition s : result += s; }
  return result;
}  

public Grammar syntax2grammar(set[SyntaxDefinition] def) {
  return grammar({},{ def2prod(sd) | sd <- def});
}

public Grammar def2prod(SyntaxDefinition def) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  set[Production] layouts = {};

  switch (def) {
    case (SyntaxDefinition) `<Tags t> <Visibility v> start syntax <UserType u> = <Prod p>;`  : 
      prods += prod2prod(user2symbol(u), p);
    case (SyntaxDefinition) `<Tags t> <Visibility v> layout <UserType u> = <Prod p>;`  : 
      layouts += prod2prod(user2symbol(u), p);
    case (SyntaxDefinition) `<Tags t> <Visibility v> syntax <UserType u> = <Prod p>;`  : { 
      starts += user2symbol(u);
      prods += prod2prod(user2symbol(u), p);
    }
    default: throw "missed case: <def>";
  }

  return grammar(starts, layout(prods) 
                       + layouts 
                       + { prod([l],layout(),no-attrs()) | l <- layouts }
                       + {prod(str2syms(x),lit(s),attrs([term(literal())])) | /lit(s) <- prods}
                       + {prod(cistr2syms(x),lit(s),attrs([term(literal())])) | /cilit(s) <- prods}
                );
}

public set[Production] layout(set[Production] prods) {
  return visit (prods) {
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) => 
         prod(intermix(lhs),rhs,attrs(as)) when [list[Attr] a,term(lex()), list[Attr] b] !:= as
  }
}

public list[Symbol] str2syms(str x) {
  return [\char-class([range(c,c)]) | i <- [0..size(x)-1], int c:= charAt(x,i)]; 
}

public list[Symbol] cistr2syms(str x) {
  return for (i <- [0..size(x)-1], int c:= charAt(x,i)) {
     if (c >= 101 && c <= 132) // A-Z
        append \char-class([range(c,c),range(c+40,c+40)]);
     else if (c >= 141 && c <= 172) // a-z
        append \char-class([range(c,c),range(c-40,c-40)]);
     else 
        append \char-class([range(c,c)]);
  } 
}

public list[Symbol] intermix(list[Symbol] syms) {
  return [s,l | s <- syms, iter-star(layout())] - [l]; 
}

public Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(n, ms));
    case (Prod) `<ProdModifier* ms> <Symbol* args>` :
      return prod(args2symbols(args), nt, mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice({prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` :
      return first([prod2prod(nt, l), prod2prod(nr, r)]);
    case (Prod) `<Prod l> - <Prod r>` :
      return diff(prod2prod(nt, l), {prod2prod(nt, r)});
    case (Prod) `left (<Prod p>)` :
      return assoc(left(), prod2prod(nt, p));
    case (Prod) `right (<Prod p>)` :
      return assoc(right(), prod2prod(nt, p));
    case (Prod) `non-assoc (<Prod p>)` :
      return assoc(\non-assoc(), prod2prod(nt, p));
    case (Prod) `assoc(<Prod p>)` :
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
    case (Sym) `<Name n>`          : return sort("<n>");
    case (Sym) `<StringConstant l>` : return lit("<l>");
    case (Sym) `<<Sym s>>`         : return arg2symbol(s);
    case (Sym) `<<Sym s> <Name n>>	` : return label("<n>", arg2symbol(s));
    case (Sym) `<Sym s> ?`  : return opt(arg2symbol(s));
    case (Sym) `<Sym s> ??` : return opt(arg2symbol(s));
    case (Sym) `<Sym s> *`  : return iter-star(arg2symbol(s));
    case (Sym) `<Sym s> +`  : return iter(arg2symbol(s));
    case (Sym) `<Sym s> *?` : return iter-star(arg2symbol(s));
    case (Sym) `<Sym s> +?` : return iter(arg2symbol(s));
    case (Sym) `{<Sym s> <StringConstant sep>} *`  : return \iter-star-sep(arg2symbol(s), lit("<sep>"));
    case (Sym) `{<Sym s> <StringConstant sep>} +`  : return \iter-sep(arg2symbol(s), lit("<sep>"));
    case (Sym) `{<Sym s> <StringConstant sep>} *?` : return \iter-star-sep(arg2symbol(s), lit("<sep>"));
    case (Sym) `{<Sym s> <StringConstant sep>} +?` : return \iter-sep(arg2symbol(s), lit("<sep>"));
    case (Sym) `<Class cc>` : return \char-class(cc2ranges(cc));
    default: throw "missed a case <sym>";
  }
}
  
public list[CharRange] cc2ranges(Class cc) {
   switch(cc) {
     case (Class) `[<Range* ranges>]` : return [range(r) | r <- ranges];
     case (Class) `(<Class c>)`: return cc2ranges(cc2ranges(c));
     case (Class) `! <Class c>`: return complement(cc2ranges(c));
     case (Class) `<Class l> & <Class r>`: return intersection(cc2ranges(l),cc2ranges(r));
     case (Class) `<Class l> + <Class r>`: return union(cc2ranges(l),cc2ranges(r));
     case (Class) `<Class l> - <Class r>`: return difference(cc2ranges(l),cc2ranges(r));
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
    case /\\<esc:["'-[] ]>/        : return charAt(esc, 0);
    case /\\[u]+<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return toInt("0x<hex>");
    case /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    default: throw "missed a case <c>";
  }
}

public Attributes mods2attrs(Name name, ProdModifier* mods) {
  return attrs([term(cons("<name>"))]);
}

public Attributes mods2attrs(ProdModifier* mods) {
  return attrs([mod2attr(m) | `<ProdModifier* p1> <ProdModifier m> <ProdModifer* p2>` := mods]);
}

public Attr mod2attr(ProdModifier m) {
  switch(m) {
    case (ProdModifier) `lex`: return term(lex());
    case (ProdModifier) `left`: return assoc(left());
    case (ProdModifier) `right`: return assoc(right());
    case (ProdModifier) `non-assoc`: return assoc(\non-assoc());
    case (ProdModifier) `assoc`: return assoc(assoc());
    case (ProdModifier) `bracket`: return bracket();
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

list[CharRange] difference(list[CharRange] l, list[CharRange] r) {
  if (l == [] || r == []) return l;

  <lhead,ltail> = takeOneFrom(l);
  <rhead,rtail> = takeOneFrom(r);

  if (lhead == \empty-range()) 
    return difference(ltail, r);

  if (rhead == \empty-range()) 
    return difference(l, rtail);

  // left beyond right
  // <-right-> --------
  // --------- <-left->
  if (lhead.start > rhead.end) 
    return difference(l,rtail); 

  // left before right
  // <-left-> ----------
  // -------- <-right->
  if (lhead.end < rhead.start) 
    return [lhead] + difference(ltail,r);

  // inclusion of left into right
  // <--------right------->
  // ---------<-left->-----
  if (lhead.start >= rhead.start && lhead.end <= rhead.end) 
    return difference(ltail,r); 

  // inclusion of right into left
  // -------<-right->------->
  // <---------left--------->
  if (rhead.start >= lhead.start && rhead.end <= lhead.end) 
    return [range(lhead.start,rhead.start-1)] 
         + difference([range(rhead.end+1,lhead.end)]+ltail,rtail);

  // overlap on left side of right
  // <--left-------->----------
  // ---------<-----right----->
  if (lhead.end < rhead.end) 
    return [range(lhead.start,rhead.start-1)] + difference(ltail,r); 
   
  // overlap on right side of right
  // -------------<---left---->
  // <----right------->--------
  if (lhead.start > rhead.start)
    return difference([range(rhead.end+1,lhead.end)]+ltail, rtail);

  throw "did not expect to end up here! <l> - <r>";
}
