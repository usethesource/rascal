module rascal::parser::Definition
   
// This module contains the mapping from concrete grammars to abstract grammars.
// It also normalizes the abstract grammars:
//   - to fill out the production rules down to the character level
//   - to normalize the production combinators into an easy-to-use canonical form
  
import rascal::syntax::RascalForImportExtraction;
import rascal::parser::Grammar;
import List;
import String;
import ParseTree;
import IO;  
import Integer;

// join the rules for the same non-terminal
rule merge   grammar(a,{p,q,a*}) => grammar(a,{or({p,q}), a*}) when sort(p) == sort(q);
	
// these rule flatten complex productions and ignore ordering under diff and assoc  
rule or     choice({set[Production] a, choice(set[Production] b)})                    => choice(a+b); 
rule xor    first([list[Production] a,first(list[Production] b),list[Production] c])  => first(a+b+c); 
rule xor    first([list[Production] a,choice({Production b}),list[Production] c])     => first(a+[b]+c); 
rule or     choice({set[Production] a, first([Production b])})        => choice(a+{b}); 
rule assoc  assoc(Associativity as, {set[Production] a, choice(set[Production] b)}) => assoc(as, a+b); 
rule assoc  assoc(Associativity as, {set[Production] a, first(list[Production] b)}) => assoc(as, a + { e | e <- b}); // ordering does not work under assoc
rule diff   diff(Production p, {set[Production] a, choice(set[Production] b)})   => diff(p, a+b);   
rule diff   diff(Production p, {set[Production] a, first(list[Production] b)})   => diff(p, a + { e | e <- b});  // ordering is irrelevant under diff
rule diff   diff(Production p, {set[Production] a, \assoc(a, set[Production] b)}) => diff(p, a + b);  // assoc is irrelevant under diff

// move diff outwards
rule empty  diff(Production p,{})                    => p;
rule or     choice({set[Production] a, diff(b, set[Production] c)})   => diff(choice(a+{b}), c);
rule xor    first([list[Production] a, diff(b, set[Production] c),list[Production] d]) => 
               diff(first(a+[b]+d), c);
rule ass    \assoc(Associativity as, {set[Production] a, diff(b, set[Production] c)}) => diff(\assoc(as, a + {b}), c);
rule diff   diff(Production p, {set[Production] a, diff(q, set[Production] b)})   => diff(choice({p,q}), a+b); 
rule diff   diff(diff(Production a, set[Production] b), set[Production] c)        => diff(a, b+c);
   
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

public Grammar syntax2grammar(set[SyntaxDefinition] defs) {
  set[Production] prods = {};
  set[Symbol] starts = {};
  set[Production] layouts = {};
    
  for (def <- defs) switch (def) { 
    case (SyntaxDefinition) `start syntax <UserType u> = <Prod p>;`  : {
       Symbol top = user2symbol(u);
       starts += start(top);
       println("start at <u>");
       prods += prod([\iter-star(layout()), top, \iter-star(layout())],start(top),\no-attrs());
       prods += prod2prod(user2symbol(u), p);
    }
    case (SyntaxDefinition) `layout <UserType u> = <Prod p>;`  : 
      layouts += prod2prod(user2symbol(u), p);
    case (SyntaxDefinition) `syntax <UserType u> = <Prod p>;`  : {
      println("rule for <u>");
      prods += prod2prod(user2symbol(u), p);
    }
    default: throw "missed case: <def>";
  }

  return grammar(starts, layout(prods) 
                       + layouts    
                       + {prod([rhs],layout(),no-attrs()) | prod(_,Symbol rhs,_) <- layouts }
                       + {prod(str2syms(s),lit(s),attrs([term("literal"())])) | /lit(s) <- prods}
                       + {prod(cistr2syms(s),lit(s),attrs([term("ciliteral"())])) | /cilit(s) <- prods}
                );
} 

public set[Production] layout(set[Production] prods) {
  // return prods;
  return visit (prods) {
    case prod(list[Symbol] lhs,Symbol rhs,attrs(list[Attr] as)) =>
           prod(intermix(lhs),rhs,attrs(as)) 
    when start(_) !:= rhs,
         term("lex"()) notin as  
    case prod(list[Symbol] lhs,Symbol rhs,\no-attrs()) =>
           prod(intermix(lhs),rhs,\no-attrs()) 
    when start(_) !:= rhs
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
  if (syms == []) return syms;
  return tail([\iter-star(layout()), s | s <- syms]);
}

public Production prod2prod(Symbol nt, Prod p) {
  switch(p) {
    case (Prod) `<ProdModifier* ms> <Name n> : <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(n, ms));
    case (Prod) `<ProdModifier* ms> <Sym* args>` :
      return prod(args2symbols(args), nt, mods2attrs(ms));
    case (Prod) `<Prod l> | <Prod r>` :
      return choice({prod2prod(nt, l), prod2prod(nt, r)});
    case (Prod) `<Prod l> > <Prod r>` :
      return first([prod2prod(nt, l), prod2prod(nr, r)]);
    case (Prod) `<Prod l> - <Prod r>` :
      return diff(prod2prod(nt, l), {prod2prod(nt, r)});
    case (Prod) `left (<Prod p>)` :
      return \assoc(left(), {prod2prod(nt, p)});
    case (Prod) `right (<Prod p>)` :
      return \assoc(right(), {prod2prod(nt, p)});
    case (Prod) `non-assoc (<Prod p>)` :
      return \assoc(\non-assoc(), {prod2prod(nt, p)});
    case (Prod) `assoc(<Prod p>)` :
      return \assoc(left(), {prod2prod(nt, p)});
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
    case (Sym) `<StringConstant l>` : return lit(unescape(l));
    case (Sym) `<<Sym s>>`         : return arg2symbol(s);
    case (Sym) `<<Sym s> <Name n>>	` : return label("<n>", arg2symbol(s));
    case (Sym) `<Sym s> ?`  : return opt(arg2symbol(s));
    case (Sym) `<Sym s> ??` : return opt(arg2symbol(s));
    case (Sym) `<Sym s> *`  : return \iter-star(arg2symbol(s));
    case (Sym) `<Sym s> +`  : return iter(arg2symbol(s));
    case (Sym) `<Sym s> *?` : return \iter-star(arg2symbol(s));
    case (Sym) `<Sym s> +?` : return iter(arg2symbol(s));
    case (Sym) `{<Sym s> <StringConstant sep>} *`  : return \iter-star-sep(arg2symbol(s), lit(unescape(sep)));
    case (Sym) `{<Sym s> <StringConstant sep>} +`  : return \iter-sep(arg2symbol(s), lit(unescape(sep)));
    case (Sym) `{<Sym s> <StringConstant sep>} *?` : return \iter-star-sep(arg2symbol(s), lit(unescape(sep)));
    case (Sym) `{<Sym s> <StringConstant sep>} +?` : return \iter-sep(arg2symbol(s), lit(unescape(sep)));
    case (Sym) `<Class cc>` : return \char-class(cc2ranges(cc));
    default: throw "missed a case <sym>";
  }
}
  
public str unescape(StringConstant s) {
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
  println("char: <c>");
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

public Attributes mods2attrs(Name name, ProdModifier* mods) {
  println("calling mods2attrs <name> <mods>");
  return attrs([term(cons("<name>"))]);
}

public Attributes mods2attrs(ProdModifier* mods) {
  println("calling mods2attrs <mods>");
  return attrs([mod2attr(m) | ProdModifier m <- mods]);
}

public Attr mod2attr(ProdModifier m) {
  println("mod2attr on <m>");
  switch(m) {
    case (ProdModifier) `lex`: return term("lex"());
    case (ProdModifier) `left`: return \assoc(left());
    case (ProdModifier) `right`: return \assoc(right());
    case (ProdModifier) `non-assoc`: return \assoc(\non-assoc());
    case (ProdModifier) `assoc`: return \assoc(\assoc());
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
 