@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::agrammar::definition::Attributes
 
import lang::rascal::\syntax::Rascal;
import lang::rascalcore::agrammar::definition::Literals;
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
