@bootstrapParser
module lang::rascal::grammar::definition::Attributes

import lang::rascal::syntax::RascalRascal;
import lang::rascal::grammar::definition::Literals;
import ParseTree;
import IO;

@doc{adds an attribute to all productions it can find}
public Production attribute(Production p, Attr a) {
  if (p.rhs == sort("SyntaxDefinition")) println("adding <a> to <p>");
  return visit (p) {
    case prod(lhs,rhs,\no-attrs()) => prod(lhs, rhs, attrs([a]))
    case prod(lhs,rhs,attrs(list[Attr] l)) => prod(lhs, rhs, attrs([l, a]))
  }
}

public Attributes mods2attrs(Name name, ProdModifier* mods) {
  return attrs([term("cons"("<name>"))] + [ mod2attr(m) | m <- mods]);
}

public Attributes mods2attrs(ProdModifier* mods) {
  res = attrs([mod2attr(m) | ProdModifier m <- mods]);
  return (res == attrs([])) ? \no-attrs() : res;
}
 
public Attr mod2attr(ProdModifier m) {
  switch (m) {
    case (ProdModifier) `left`: return \assoc(\left());
    case (ProdModifier) `right`: return \assoc(\right());
    case (ProdModifier) `non-assoc`: return \assoc(\non-assoc());
    case (ProdModifier) `assoc`: return \assoc(\assoc());
    case (ProdModifier) `bracket`: return \bracket();
    case (ProdModifier) `@ <Name n> = <StringConstant s>` : return \term("<n>"(unescape(s)));
    case (ProdModifier) `@ <Name n> = <Literal l>` : return \term("<n>"("<l>"));
    case (ProdModifier) `@ <Name n>` : return \term("<n>"());
    case (ProdModifier) `@ <Name n> <TagString s>` : return \term("<n>"("<s>"));
    default: throw "missed a case <m>";
  }
}