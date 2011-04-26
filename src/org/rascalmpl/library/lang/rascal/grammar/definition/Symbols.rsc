@bootstrapParser
module lang::rascal::grammar::definition::Symbols

import lang::rascal::grammar::definition::Literals;
import lang::rascal::syntax::RascalRascal;
import ParseTree;

private list[Symbol] args2symbols(Sym* args) {
  return [sym2symbol(s) | Sym s <- args];
}

private list[Symbol] separgs2symbols({Sym ","}+ args) {
  return [sym2symbol(s) | Sym s <- args];
}
   
public Symbol sym2symbol(Sym sym) {
  switch (sym) {
    case (Sym) `^` : 
      return \start-of-line();
    case (Sym) `$` : 
      return \end-of-line();
    case (Sym) `<Sym s> >> <Sym r>` : 
      return follow(sym2symbol(s, isLex, layoutName), {sym2symbol(r, isLex, layoutName)});
    case (Sym) `<Sym s> !>> <Sym r>` : 
      return \not-follow(sym2symbol(s, isLex, layoutName), {sym2symbol(r, isLex, layoutName)});
    case (Sym) `<Sym s> << <Sym r>` : 
      return precede(sym2symbol(r, isLex, layoutName), {sym2symbol(s, isLex, layoutName)});
    case (Sym) `<Sym s> !<< <Sym r>` : 
      return \not-precede(sym2symbol(r, isLex, layoutName), {sym2symbol(s, isLex, layoutName)});
    case (Sym) `<Sym s> != <Sym r>` : 
      return \reserve(sym2symbol(s, isLex, layoutName), {sym2symbol(r, isLex, layoutName)});
    case (Sym) `@ <IntegerLiteral i>` : 
      return \at-column(toInt("<i>")); 
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
      return \parameterized-sort("<n>",separgs2symbols(syms,isLex,layoutName));
    case (Sym) `<Sym s> <NonterminalLabel n>` : 
      return label("<n>", sym2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> ?`  : 
      return opt(sym2symbol(s,isLex,layoutName));
    case (Sym) `<Class cc>` : 
      return cc2ranges(cc);
    case (Sym) `&<Nonterminal n>` : 
      return \parameter("<n>");
    case (Sym) `()` : 
      return \empty();
    case (Sym) `( <Sym first> | <{Sym "|"}+ alts>)` : 
      return alt({sym2symbol(first,isLex,layoutName)} + {sym2symbol(elem,isLex,layoutName) | elem <- alts});
    case (Sym) `<Sym s> *`  : 
      return \iter-star(sym2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> +`  : 
      return \iter(sym2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> *?` : 
      return \iter-star(sym2symbol(s,isLex,layoutName));
    case (Sym) `<Sym s> +?` : 
      return \iter(sym2symbol(s,isLex,layoutName));
    case (Sym) `{<Sym s> <StringConstant sep>} *`  : 
      return \iter-star-seps(sym2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} +`  : 
      return \iter-seps(sym2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} *?` : 
      return \iter-star-seps(sym2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `{<Sym s> <StringConstant sep>} +?` : 
      return \iter-seps(sym2symbol(s,isLex,layoutName), [lit(unescape(sep))]);
    case (Sym) `(<Sym first> <Sym+ sequence>)` : 
      return seq([sym2symbol(first,isLex,layoutName)] + [sym2symbol(elem,isLex,layoutName) | elem <- sequence]);
    default: 
      throw "missed a case <sym>";
  }
}