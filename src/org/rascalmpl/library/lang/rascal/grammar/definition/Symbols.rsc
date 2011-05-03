@bootstrapParser
module lang::rascal::grammar::definition::Symbols

import lang::rascal::grammar::definition::Literals;
import lang::rascal::syntax::RascalRascal;
import ParseTree;


public Symbol sym2symbol(Sym sym) {
  switch (sym) {
    case (Sym) `<Nonterminal n>`          : 
      return sort("<n>");
    case (Sym) `keyword[<Nonterminal n>]` : 
      return keywords("<n>");
    case (Sym) `layout[<Nonterminal n>]` : 
      return layouts("<n>");
    case (Sym) `start[<Nonterminal n>]` : 
      return start(sort("<n>"));
    case (Sym) `<StringConstant l>` : 
      return lit(unescape(l));
    case (Sym) `<CaseInsensitiveStringConstant l>`: 
      return cilit(unescape(l));
    case (Sym) `<ParameterizedNonterminal n>[<{Sym ","}+ syms>]` : 
      return \parameterized-sort("<n>",separgs2symbols(syms));
    case (Sym) `<Sym s> <NonterminalLabel n>` : 
      return label("<n>", sym2symbol(s));
    case (Sym) `<Sym s> ?`  : 
      return opt(sym2symbol(s));
    case (Sym) `<Class cc>` : 
      return cc2ranges(cc);
    case (Sym) `&<Nonterminal n>` : 
      return \parameter("<n>");
    case (Sym) `()` : 
      return \empty();
    case (Sym) `( <Sym first> | <{Sym "|"}+ alts>)` : 
      return alt({sym2symbol(first)} + {sym2symbol(elem) | elem <- alts});
    case (Sym) `<Sym s>*`  : 
      return \iter-star(sym2symbol(s));
    case (Sym) `<Sym s>+`  : 
      return \iter(sym2symbol(s));
    case (Sym) `<Sym s> *?` : 
      return \iter-star(sym2symbol(s));
    case (Sym) `<Sym s> +?` : 
      return \iter(sym2symbol(s));
    case (Sym) `{<Sym s> <Sym sep>}*`  : 
      return \iter-star-seps(sym2symbol(s), [sym2symbol(sep)]);
    case (Sym) `{<Sym s> <Sym sep>}+`  : 
      return \iter-seps(sym2symbol(s), [sym2symbol(sep)]);
    case (Sym) `(<Sym first> <Sym+ sequence>)` : 
      return seq([sym2symbol(first)] + [sym2symbol(elem) | elem <- sequence]);
    case (Sym) `^` : 
      return \start-of-line();
    case (Sym) `$` : 
      return \end-of-line();
    case (Sym) `<Sym s> >> <Sym r>` : 
      return follow(sym2symbol(s), {sym2symbol(r)});
    case (Sym) `<Sym s> !>> <Sym r>` : 
      return \not-follow(sym2symbol(s), {sym2symbol(r)});
    case (Sym) `<Sym s> << <Sym r>` : 
      return precede(sym2symbol(r), {sym2symbol(s)});
    case (Sym) `<Sym s> !<< <Sym r>` : 
      return \not-precede(sym2symbol(r), {sym2symbol(s)});
    case (Sym) `<Sym s> != <Sym r>` :  //  case (Sym) `<Sym s> \ <Sym r>` :
      return \reserve(sym2symbol(s), {sym2symbol(r)});
    case (Sym) `@ <IntegerLiteral i>` : 
      return \at-column(toInt("<i>")); 
    default: 
      throw "missed a case <sym>";
  }
}

public list[Symbol] args2symbols(Sym* args) {
  return [sym2symbol(s) | Sym s <- args];
}

private list[Symbol] separgs2symbols({Sym ","}+ args) {
  return [sym2symbol(s) | Sym s <- args];
}

// flattening rules
public Symbol \seq([list[Symbol] a, \seq(list[Symbol] b), list[Symbol] c]) = \seq(a + b + c);

public Symbol \alt({set[Symbol] a, \alt(set[Symbol] b)}) = \alt(a + b);

public Symbol \follow(\follow(Symbol s, set[Symbol] a), set[Symbol] b) = \follow(s, a + b);

public Symbol \not-follow(\not-follow(Symbol s, set[Symbol] a), set[Symbol] b) = \not-follow(s, a + b);

public Symbol \precede(\precede(Symbol s, set[Symbol] a), set[Symbol] b) = \precede(s, a + b);

public Symbol \not-precede(\not-precede(Symbol s, set[Symbol] a), set[Symbol] b) = \not-precede(s, a + b);

public Symbol \reserve(\reserve(Symbol s, set[Symbol] a), set[Symbol] b) = reserve(s, a + b);