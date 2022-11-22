@license{
  Copyright (c) 2009-2020 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module lang::rascal::tests::concrete::Terms

import ParseTree;

layout Whitespace = [\ ]*;
lexical IntegerLiteral = [0-9]+; 
lexical Identifier = [a-z]+;

syntax Exp 
  = IntegerLiteral  
  | Identifier        
  | bracket "(" Exp ")"     
  > left Exp "*" Exp        
  > left Exp "+" Exp  
  | Exp "==" Exp      
  ;

syntax Stat 
   = Identifier ":=" Exp
   | "if" Exp cond "then" {Stat ";"}* thenPart "else" {Stat ";"}* elsePart "fi"
   ;
   
Exp e = [Exp] "x + 1";
Stat s1 = [Stat] "a := 2 * 3";
Stat s2 = [Stat] "b := 4 + 5";
Stat s3 = (Stat) `if <Exp e> then <Stat s1>;<Stat s2> else <Stat s1> fi`;


test bool concreteTerm1() = s3.cond == e;

test bool concreteTerm2() = s3.thenPart[0] == s1;

test bool concreteTerm3() = s3.thenPart[1] == s2;

test bool concreteTerm4() = s3.elsePart[0] == s1;

test bool testAssociativity() = /amb(_) !:= parse(#Exp, "x + x + x", allowAmbiguity=true);

test bool testPriority() = /amb(_) !:= parse(#Exp, "x + x * x", allowAmbiguity=true);

 
