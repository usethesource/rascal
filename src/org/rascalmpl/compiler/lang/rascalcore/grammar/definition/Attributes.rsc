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
import lang::rascalcore::check::ATypeBase;
import IO;
import ValueIO;
import util::Maybe;
 
@doc{adds an attribute to all productions it can find}
public AProduction attribute(AProduction p, AAttr a) = p[attributes=p.attributes+{a}];

// TODO: the result set is always empty it seems. FixMe!
public set[AAttr] mods2attrs(ProdModifier* mods) = {mod2attr(m) | ProdModifier m <- mods};
 
public AAttr mod2attr(ProdModifier m) {
  switch (m) {
    case \associativity(\left())                : return \aassoc(AAssociativity::aleft());
    case \associativity(\right())               : return \aassoc(AAssociativity::aright());
    case \associativity(\nonAssociative())      : return \aassoc(AAssociativity::\a-non-assoc());
    case \associativity(\associative())         : return \aassoc(AAssociativity::\aassoc());
    case \bracket()                             : return AAttr::\abracket();
    case \tag(\default(Name n, TagString s))    : return \atag("<n>"("<s>"));
    case \tag(\empty(Name n))                   : return \atag("<n>"()); 
    case \tag(\expression(Name n, literal(string(nonInterpolated(StringConstant l)))))  
                                                : return \atag("<n>"("<unescapeLiteral(l)>"));
    case \tag(\expression(Name n, literal(Literal l)))
                                                : return \atag("<n>"("<unescapeLiteral("<l>")>"));
    case \tag(\expression(Name n, Expression e))     
                                                : return \atag("<n>"( readTextValueString("<e>")));                                       
    default: { rprintln(m); throw "mod2attr, missed a case <m>"; }
  }
}

Maybe[AAssociativity] mods2assoc(ProdModifier* mods) = (nothing() | just(x) | ProdModifier m <- mods, just(AAssociativity x) := mod2assoc(m));

Maybe[AAssociativity] mod2assoc(\associativity(\left()))           = just(AAssociativity::aleft());
Maybe[AAssociativity] mod2assoc(\associativity(\right()))          = just(AAssociativity::aright());
Maybe[AAssociativity] mod2assoc(\associativity(\associative()))    = just(AAssociativity::aleft());
Maybe[AAssociativity] mod2assoc(\associativity(\nonAssociative())) = just(AAssociativity::\a-non-assoc());
default Maybe[AAssociativity] mod2assoc(ProdModifier _)            = nothing();
