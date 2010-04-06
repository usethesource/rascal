module rascal::parser::Definition
   
// This module contains the mapping from concrete grammars to abstract grammars.
// It also normalizes the abstract grammars:
//   - to fill out the production rules down to the character level
//   - to normalize the production combinators into an easy-to-use canonical form
  
import rascal::syntax::RascalForImportExtraction;
import rascal::parser::Grammar;
import rascal::parser::Regular;
import List;
import String;
import ParseTree;
import IO;  
import Integer;

// join the rules for the same non-terminal
rule merge   grammar(a,{p,q,a*}) => grammar(a,{choice(sort(p), {p,q}), a*}) when sort(p) == sort(q);
	
// these rule flatten complex productions and ignore ordering under diff and assoc  
rule or     choice(Symbol s, {set[Production] a, choice(Symbol t, set[Production] b)})                    => choice(s,a+b); 
rule xor    first(Symbol s, [list[Production] a,first(Symbol t, list[Production] b),list[Production] c])  => first(s,a+b+c); 
rule xor    first(Symbol s, [list[Production] a,choice(Symbol t, {Production b}),list[Production] c])     => first(a+[b]+c); 
rule or     choice(Symbol s, {set[Production] a, first(Symbol t, [Production b])})        => choice(s, a+{b}); 
rule assoc  assoc(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) => assoc(s, as, a+b); 
rule assoc  assoc(Symbol s, Associativity as, {set[Production] a, first(Symbol t, list[Production] b)}) => assoc(s, as, a + { e | e <- b}); // ordering does not work under assoc
rule diff   diff(Symbol s, Production p, {set[Production] a, choice(Symbol t, set[Production] b)})   => diff(s, p, a+b);   
rule diff   diff(Symbol s, Production p, {set[Production] a, first(Symbol t, list[Production] b)})   => diff(s, p, a + { e | e <- b});  // ordering is irrelevant under diff
rule diff   diff(Symbol s, Production p, {set[Production] a, \assoc(Symbol t, a, set[Production] b)}) => diff(s, p, a + b);  // assoc is irrelevant under diff

// move diff outwards
rule empty  diff(_,Production p,{})                    => p;
rule or     choice(Symbol s, {set[Production] a, diff(Symbol t, b, set[Production] c)})   => diff(s, choice(s, a+{b}), c);
rule xor    first(Symbol s, [list[Production] a, diff(Symbol t, b, set[Production] c),list[Production] d]) => 
               diff(s, first(a+[b]+d), c);
rule ass    \assoc(Symbol s, Associativity as, {set[Production] a, diff(Symbol t, b, set[Production] c)}) => diff(s, \assoc(s, as, a + {b}), c);
rule diff   diff(Symbol s, Production p, {set[Production] a, diff(Symbol t, Production q, set[Production] b)})   => diff(s, choice(s, {p,q}), a+b); 
rule diff   diff(Symbol s, diff(Symbol t, Production a, set[Production] b), set[Production] c)        => diff(s, a, b+c);
   
rule simpl  attrs([]) => \no-attrs();  

// character class normalization
private data CharRange = \empty-range();
  
rule empty range(int from, int to) => \empty-range() when to < from;
rule empty \char-class([list[CharRange] a,\empty-range(),list[CharRange] b]) => \char-class(a+b);

rule merge \char-class([list[CharRange] a,range(int from1, int to1),list[CharRange] b,range(int from2, int to2),list[CharRange] c]) =>
           \char-class(a+[range(min(from1,from2),max(to1,to2))]+b+c)
     when (from1 <= from2 && to1 >= from2 - 1) 
       || (from2 <= from1 && to2 >= from1 - 1)
       || (from1 >= from2 && to1 <= to2)
       || (from2 >= from1 && to2 <= to1);
    
rule order \char-class([list[CharRange] a,range(int n,int m),list[CharRange] b, range(int o, int p), list[CharRange] c]) =>
           \char-class(a + [range(o,p)]+b+[range(n,m)]+c)
     when p < n;

public Symbol sort(Production p) {
  if (/prod(_,rhs,_) := p || /regular(rhs,_) := p) {
    return rhs;
  }
  throw "weird production <p>";
}

public Grammar module2grammar(Module mod) {
  return syntax2grammar(collect(mod));
}  
   
private set[SyntaxDefinition] collect(Module mod) {
  set[SyntaxDefinition] result = {};
  visit (mod) { case SyntaxDefinition s : result += s; }
  return result;
}  
  
private Grammar syntax2grammar(set[SyntaxDefinition] defs) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  set[Production] layouts = {};
    
  for (def <- defs) switch (def) { 
    case (SyntaxDefinition) `start syntax <UserType u> = <Prod p>;`  : {
       Symbol top = user2symbol(u);
       starts += start(top);
       prods += prod2prod(user2symbol(u), p);
    }
    case (SyntaxDefinition) `layout <UserType u> = <Prod p>;`  : 
      layouts += prod2prod(user2symbol(u), p);
    case (SyntaxDefinition) `syntax <UserType u> = <Prod p>;`  : {
      prods += prod2prod(user2symbol(u), p);
    }
    default: throw "missed case: <def>";
  }

  return grammar(starts, layout(prods) 
                       + layouts  
                       + {regular(\iter-star(layout()),\no-attrs())}
                       + {prod([\iter-star(layout()), top, \iter-star(layout())],start(top),\no-attrs()) | start(top) <- starts} 
                       + {prod([rhs],layout(),\no-attrs()) | /prod(_,Symbol rhs,_) <- layouts }
                       + {prod(str2syms(s),lit(s),attrs([term("literal"())])) | /lit(s) <- prods+layouts}
                       + {prod(cistr2syms(s),lit(s),attrs([term("ciliteral"())])) | /cilit(s) <- prods+layouts}
                       + makeRegularStubs(prods+layouts)
                );
} 


  
private set[Production] layout(set[Production] prods) {
  return visit (prods) {
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) => prod(intermix(lhs),rhs,attrs(as)) 
      when start(_) !:= rhs, term("lex"()) notin as  
    case prod(list[Symbol] lhs,Symbol rhs,\no-attrs()) => prod(intermix(lhs),rhs,\no-attrs()) 
      when start(_) !:= rhs
  }
}  

private list[Symbol] str2syms(str x) {
  return [\char-class([range(c,c)]) | i <- [0..size(x)-1], int c:= charAt(x,i)]; 
}

private list[Symbol] cistr2syms(str x) {
  return for (i <- [0..size(x)-1], int c:= charAt(x,i)) {
     if (c >= 101 && c <= 132) // A-Z
        append \char-class([range(c,c),range(c+40,c+40)]);
     else if (c >= 141 && c <= 172) // a-z
        append \char-class([range(c,c),range(c-40,c-40)]);
     else 
        append \char-class([range(c,c)]);
  } 
}

private list[Symbol] intermix(list[Symbol] syms) {
  if (syms == []) return syms;
  return tail([\iter-star(layout()), s | s <- syms]);
}


private Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(args2symbols(args, hasLex(ms)), nt, mods2attrs(n, ms));
    case (Prod) `<ProdModifier* ms> <Sym* args>` :
      return prod(args2symbols(args, hasLex(ms)), nt, mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice(sort(prod2prod(nt,l)),{prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` :
      return first(sort(prod2prod(nt,l)),[prod2prod(nt, l), prod2prod(nr, r)]);
    case (Prod) `<Prod l> - <Prod r>` :
      return diff(sort(prod2prod(nt,l)), prod2prod(nt, l), {prod2prod(nt, r)});
    case (Prod) `left (<Prod p>)` :
      return \assoc(sort(prod2prod(nt,p)), \left(), {prod2prod(nt, p)});
    case (Prod) `right (<Prod p>)` :
      return \assoc(sort(prod2prod(nt,p)), \right(), {prod2prod(nt, p)});
    case (Prod) `non-assoc (<Prod p>)` :
      return \assoc(sort(prod2prod(nt,p)), \non-assoc(), {prod2prod(nt, p)});
    case (Prod) `assoc(<Prod p>)` :
      return \assoc(sort(prod2prod(nt,p)), \left(), {prod2prod(nt, p)});
    case `...`: throw "... operator is not yet implemented";
    case `: <Name n>`: throw "prod referencing is not yet implemented";
    default: throw "missed a case <p>";
  } 
}

private bool hasLex(ProdModifier* ms) {
  return /(ProdModifier) `lex` := ms;
}

private list[Symbol] args2symbols(Sym* args, bool isLex) {
  return [ arg2symbol(s, isLex) | Sym s <- args ];
}

private list[Symbol] separgs2symbols({Sym ","}+ args, bool isLex) {
  return [ arg2symbol(s, isLex) | Sym s <- args ];
}
   
private Symbol arg2symbol(Sym sym, bool isLex) {
  switch (sym) {
    case (Sym) `<Nonterminal n>`          : return sort("<n>");
    case (Sym) `<StringConstant l>` : return lit(unescape(l));
    case (Sym) `<Nonterminal n>[<{Sym ","}+ syms>]` : return \parametrized-sort("<n>",separgs2symbols(syms,isLex));
    case (Sym) `<Sym s> <NonterminalLabel n>` : return label("<n>", arg2symbol(s,isLex));
    case (Sym) `<Sym s> ?`  : return opt(arg2symbol(s,isLex));
    case (Sym) `<Sym s> ??` : return opt(arg2symbol(s,isLex));
    case (Sym) `<Class cc>` : return \char-class(cc2ranges(cc));
  }  
  
  if (isLex) switch (sym) {
    case (Sym) `<Sym s> *`  : return \iter-star(arg2symbol(s,isLex));
    case (Sym) `<Sym s> +`  : return \iter(arg2symbol(s,isLex));
    case (Sym) `<Sym s> *?` : return \iter-star(arg2symbol(s,isLex));
    case (Sym) `<Sym s> +?` : return \iter(arg2symbol(s,isLex));
    case (Sym) `{<Sym s> <StringConstant sep>} *`  : return \iter-star-sep(arg2symbol(s,isLex), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} +`  : return \iter-sep(arg2symbol(s,isLex), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} *?` : return \iter-star-sep(arg2symbol(s,isLex), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} +?` : return \iter-sep(arg2symbol(s,isLex), [lit(unescape(sep))]);
    default: throw "missed a case <sym>";
  } 
  else switch (sym) {  
    case (Sym) `<Sym s> *`  : return \iter-star-sep(arg2symbol(s,isLex),[layout()]);
    case (Sym) `<Sym s> +`  : return \iter-sep(arg2symbol(s,isLex),[layout()]);
    case (Sym) `<Sym s> *?` : return \iter-star-sep(arg2symbol(s,isLex),[layout()]);
    case (Sym) `<Sym s> +?` : return \iter-sep(arg2symbol(s,isLex),[layout()]);
    case (Sym) `{<Sym s> <StringConstant sep>} *`  : return \iter-star-sep(arg2symbol(s,isLex), [layout(),lit(unescape(sep)),layout()]);
    case (Sym) `{<Sym s> <StringConstant sep>} +`  : return \iter-sep(arg2symbol(s,isLex), [layout(),lit(unescape(sep)),layout()]);
    case (Sym) `{<Sym s> <StringConstant sep>} *?` : return \iter-star-sep(arg2symbol(s,isLex), [layout(),lit(unescape(sep)),layout()]);
    case (Sym) `{<Sym s> <StringConstant sep>} +?` : return \iter-sep(arg2symbol(s,isLex), [layout(),lit(unescape(sep)),layout()]);
    default: throw "missed a case <sym>";  
  }
}
  
private str unescape(StringConstant s) {
   if ([StringConstant] /\"<rest:.*>\"/ := s) {
     return visit (rest) {
       case /\\b/ => "\b"
       case /\\f/ => "\f"
       case /\\n/ => "\n"
       case /\\t/ => "\t"
       case /\\r/ => "\r"  
       case /\\\"/ => "\""  
       case /\\\'/ => "\'"
       case /\\\\/ => "\\"
       case /\\\</ => "\<"   
       case /\\\>/ => "\>"    
     };      
   }
   throw "unexpected string format: <s>";
}

private list[CharRange] cc2ranges(Class cc) {
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
      
private CharRange range(Range r) {
  switch (r) {
    case (Range) `<Character c>` : return range(character(c),character(c));
    case (Range) `<Character l> - <Character r>`: return range(character(l),character(r));
    default: throw "missed a case <r>";
  }
} 

private int character(Character c) {
  switch (c) {
    case [Character] /<ch:[^"'\-\[\] ]>/        : return charAt(ch, 0); 
    case [Character] /\\<esc:["'\-\[\] ]>/        : return charAt(esc, 0);
    case [Character] /\\[u]+<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return toInt("0x<hex>");
    case [Character] /\\<oct:[0-7]>/           : return toInt("0<oct>");
    case [Character] /\\<oct:[0-7][0-7]>/      : return toInt("0<oct>");
    case [Character] /\\<oct:[0-3][0-7][0-7]>/ : return toInt("0<oct>");
    default: throw "missed a case <c>";
  }
}

private Attributes mods2attrs(Name name, ProdModifier* mods) {
  return attrs([term(cons("<name>"))]);
}

private Attributes mods2attrs(ProdModifier* mods) {
  return attrs([mod2attr(m) | ProdModifier m <- mods]);
}
 
private Attr mod2attr(ProdModifier m) {
  switch (m) {
    case (ProdModifier) `lex`: return term("lex"());
    case (ProdModifier) `left`: return \assoc(left());
    case (ProdModifier) `right`: return \assoc(right());
    case (ProdModifier) `non-assoc`: return \assoc(\non-assoc());
    case (ProdModifier) `assoc`: return \assoc(\assoc());
    case (ProdModifier) `bracket`: return bracket();
    default: throw "missed a case <m>";
  }
}
  
private Symbol user2symbol(UserType u) {
  switch (u) {
   case (UserType) `<Name n>` : return sort("<n>");
   default: throw "missed case: <u>";
  } 
}

private list[CharRange] complement(list[CharRange] s) {
  return difference([range(0,0xFFFF)],s);
}

private list[CharRange] intersection(list[CharRange] l, list[CharRange] r) {
  return union(difference(l,r),difference(r,l));
} 

private list[CharRange] union(list[CharRange] l, list[CharRange] r) {
 return l + r;
}

private list[CharRange] difference(list[CharRange] l, list[CharRange] r) {
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
 