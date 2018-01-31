@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascalcore::grammar::definition::Attributes

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Literals;
import lang::rascalcore::check::AType;
import IO;
import ValueIO;
import util::Maybe;

@doc{adds an attribute to all productions it can find}
public AProduction attribute(AProduction p, Attr a) = p[attributes=p.attributes+{a}];

// TODO: the result set is always empty it seems. FixMe!
public set[Attr] mods2attrs(ProdModifier* mods) = {mod2attr(m) | ProdModifier m <- mods};
 
public Attr mod2attr(ProdModifier m) {
  switch (m) {
    case \associativity(\left())                : return \assoc(Associativity::\left());
    case \associativity(\right())               : return \assoc(Associativity::\right());
    case \associativity(\nonAssociative())      : return \assoc(\non-assoc());
    case \associativity(\associative())         : return \assoc(\assoc());
    case \bracket()                             : return \bracket();
    case \tag(\default(Name n, TagString s))    : return \tag("<n>"("<s>"));
    case \tag(\empty(Name n))                   : return \tag("<n>"()); 
    case \tag(\expression(Name n, literal(string(nonInterpolated(StringConstant l)))))  
                                                : return \tag("<n>"("<unescapeLiteral(l)>"));
    case \tag(\expression(Name n, literal(Literal l)))
                                                : return \tag("<n>"("<unescapeLiteral("<l>")>"));
    case \tag(\expression(Name n, Expression e))     
                                                : return \tag("<n>"( readTextValueString("<e>")));                                       
    default: { rprintln(m); throw "mod2attr, missed a case <m>"; }
  }
}

Maybe[Associativity] mods2assoc(ProdModifier* mods) = (nothing() | just(x) | ProdModifier m <- mods, just(x) := mod2assoc(m));

Maybe[Associativity] mod2assoc(\associativity(\left()))           = just(Associativity::\left());
Maybe[Associativity] mod2assoc(\associativity(\right()))          = just(Associativity::\right());
Maybe[Associativity] mod2assoc(\associativity(\associative()))    = just(Associativity::\left());
Maybe[Associativity] mod2assoc(\associativity(\nonAssociative())) = just(Associativity::\non-assoc());
default Maybe[Associativity] mod2assoc(ProdModifier _)            = nothing();
