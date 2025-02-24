@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascalcore::agrammar::definition::Literals

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ATypeBase;
import String;

public AGrammar literals(AGrammar g) {
    lts = {literal(s) | /lit(s) <- g} + {ciliteral(s) | /acilit(s) <- g};
  return compose(g, grammar({}, lts));
}

public AProduction literal(str s) = prod(alit(s),str2syms(s));
public AProduction ciliteral(str s) = prod(acilit(s), cistr2syms(s));

public list[AType] str2syms(str x) {
  if (x == "") return [];
  return [\achar-class([arange(c,c)]) | i <- [0..size(x)], int c:= charAt(x,i)]; 
}

list[AType] cistr2syms(str x) {
  return for (i <- [0..size(x)], int c:= charAt(x,i)) {
     if (c >= 101 && c <= 132) // A-Z
        append \achar-class([arange(c,c),arange(c+40,c+40)]);
     else if (c >= 141 && c <= 172) // a-z
        append \achar-class([arange(c,c),arange(c-40,c-40)]);
     else 
        append \achar-class([arange(c,c)]);
  } 
}

public str unescapeLiteral(CaseInsensitiveStringConstant s) = "<for (StringCharacter ch <- s.chars) {><character(ch)><}>";

public str unescapeLiteral(StringConstant s) = "<for (StringCharacter ch <- s.chars) {><character(ch)><}>";

public str character(StringCharacter c) {
  switch (c) {
    case [StringCharacter] /^<ch:[^"'\\\>\<]>/        : return "<ch>";
    case [StringCharacter] /^\\n/ : return "\n";
    case [StringCharacter] /^\\t/ : return "\t";
    case [StringCharacter] /^\\b/ : return "\b";
    case [StringCharacter] /^\\r/ : return "\r";
    case [StringCharacter] /^\\f/ : return "\f";
    case [StringCharacter] /^\\\>/ : return "\>";
    case [StringCharacter] /^\\\</ : return "\<";
    case [StringCharacter] /^\\<esc:["'\\ ]>/        : return "<esc>";
    case [StringCharacter] /^\\u<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return stringChar(toInt("0x<hex>"));
    case [StringCharacter] /^\\U<hex:[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]>/ : return stringChar(toInt("0x<hex>"));
    case [StringCharacter] /^\\a<hex:[0-7][0-9a-fA-F]>/ : return stringChar(toInt("0x<hex>")); 
    case [StringCharacter] /^\n[ \t]* \'/            : return "\n";
    default: throw "character, missed a case <c>";
  }
}

public str unescapeLiteral(str s) {
  return visit (s) {
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

