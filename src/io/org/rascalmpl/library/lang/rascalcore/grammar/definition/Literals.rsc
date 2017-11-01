@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascalcore::grammar::definition::Literals

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Grammar;
//import ParseTree;
import lang::rascalcore::check::AType;
import String;
import IO;

public Grammar literals(Grammar g) {
  return compose(g, grammar({}, {literal(s) | /lit(s) <- g} + {ciliteral(s) | /cilit(s) <- g}));
}

public AProduction literal(str s) = prod(lit(s),str2syms(s),{});
public AProduction ciliteral(str s) = prod(cilit(s),cistr2syms(s),{});

public list[AType] str2syms(str x) {
  if (x == "") return [];
  return [\char-class([range(c,c)]) | i <- [0..size(x)], int c:= charAt(x,i)]; 
}

list[AType] cistr2syms(str x) {
  return for (i <- [0..size(x)], int c:= charAt(x,i)) {
     if (c >= 101 && c <= 132) // A-Z
        append \char-class([range(c,c),range(c+40,c+40)]);
     else if (c >= 141 && c <= 172) // a-z
        append \char-class([range(c,c),range(c-40,c-40)]);
     else 
        append \char-class([range(c,c)]);
  } 
}

public str unescape(CaseInsensitiveStringConstant s) = "<for (StringCharacter ch <- s.chars) {><character(ch)><}>";

public str unescape(StringConstant s) = "<for (StringCharacter ch <- s.chars) {><character(ch)><}>";

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

public str unescape(str s) {
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

