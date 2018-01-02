@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::grammar::definition::Attributes

import lang::rascal::\syntax::Rascal;
import lang::rascal::grammar::definition::Literals;
import ParseTree;
import IO;
import ValueIO;
import util::Maybe;

@doc{adds an attribute to all productions it can find}
Production attribute(Production p, Attr a) = p[attributes=p.attributes+{a}];

set[Attr] mods2attrs(ProdModifier* mods) = {x | ProdModifier m <- mods, just(x) := mod2attr(m)};

Maybe[Attr] mod2attr(ProdModifier m) {
  switch (m) { 
    /*deprecated TODO: remove after bootstrap */
    case ass:\associativity(Assoc _)              : 
      if (just(Associativity lra) := mod2assoc(ass)) { 
        return just(\assoc(lra)); 
      } else { 
        return nothing();
      }
    case \bracket()                             : return just(Attr::\bracket());
    case \tag(\default(Name n, TagString s))    : return just(\tag("<n>"("<s>")));
    case \tag(\empty(Name n))                   : return just(\tag("<n>"())); 
    case \tag(\expression(Name n, literal(string(nonInterpolated(StringConstant l)))))  
                                                : return just(\tag("<n>"("<unescapeLiteral(l)>")));
    case \tag(\expression(Name n, literal(Literal l)))
                                                : return just(\tag("<n>"("<unescapeLiteral("<l>")>")));
    case \tag(\expression(Name n, Expression e)): return just(\tag("<n>"( readTextValueString("<e>"))));                                       
    default                                     : return nothing(); 
  }
}

Maybe[Associativity] mods2assoc(ProdModifier* mods) = (nothing() | just(x) | ProdModifier m <- mods, just(x) := mod2assoc(m));

Maybe[Associativity] mod2assoc(\associativity(\left()))           = just(Associativity::\left());
Maybe[Associativity] mod2assoc(\associativity(\right()))          = just(Associativity::\right());
Maybe[Associativity] mod2assoc(\associativity(\associative()))    = just(Associativity::\left());
Maybe[Associativity] mod2assoc(\associativity(\nonAssociative())) = just(Associativity::\non-assoc());
default Maybe[Associativity] mod2assoc(ProdModifier _)            = nothing();
