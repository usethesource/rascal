@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
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

