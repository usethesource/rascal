@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::rascal::grammar::definition::Literals

import lang::rascal::\syntax::Rascal;
import Grammar;
import ParseTree;
import String;

public Grammar literals(Grammar g) {
  return compose(g, grammar({}, {literal(s) | /lit(s) <- g} + {ciliteral(s) | /cilit(s) <- g}));
}

public Production literal(str s) = prod(lit(s),str2syms(s),{});
public Production ciliteral(str s) = prod(cilit(s),cistr2syms(s),{});

public list[Symbol] str2syms(str x) {
  if (x == "") return [];
  return [\char-class([range(c,c)]) | i <- [0..size(x)], int c:= charAt(x,i)]; 
}

list[Symbol] cistr2syms(str x) {
  return for (i <- [0..size(x)], int c:= charAt(x,i)) {
     if (c >= 65 && c <= 90) // A-Z
        append \char-class([range(c,c),range(c+32,c+32)]);
     else if (c >= 97 && c <= 122) // a-z
        append \char-class([range(c,c),range(c-32,c-32)]);
     else 
        append \char-class([range(c,c)]);
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

