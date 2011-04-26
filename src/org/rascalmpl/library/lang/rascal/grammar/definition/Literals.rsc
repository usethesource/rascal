@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@bootstrapParser
module lang::rascal::grammar::definition::Literals

import lang::rascal::syntax::RascalRascal;
import Grammar;
import ParseTree;

public Grammar literals(Grammar g) {
  return compose(g, grammar({}, {prod(str2syms(s),lit(s),attrs([\literal()])) | /lit(s) <- g}
                              + {prod(cistr2syms(s),cilit(s),attrs([\ciliteral()])) | /cilit(s) <- g}));
}

public list[Symbol] str2syms(str x) {
  // TODO: escaping?
  if (x == "") return [];
  return [\char-class([range(c,c)]) | i <- [0..size(x)-1], int c:= charAt(x,i)]; 
}

private list[Symbol] cistr2syms(str x) {
  return for (i <- [0..size(x)-1], int c:= charAt(x,i)) {
     if (c >= 101 && c <= 132) // A-Z
        append \char-class([range(c,c),range(c+40,c+40)]);
     else if (c >= 141 && c <= 172) // a-z
        append \char-class([range(c,c),range(c-40,c-40)]);
     else 
        append \char-class([range(c,c)]);
  } 
}

public str unescape(StringConstant s) {
   if ([StringConstant] /\"<rest:.*>\"/ := s) {
     return visit (rest) {
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
   throw "unexpected string format: <s>";
}

