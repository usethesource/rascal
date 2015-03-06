module lang::rascal::tests::functionality::ConcreteTerms


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

@ignoreInterpreter{Not properly implemented}
test bool concreteTerm1() = s3.cond == e;
@ignoreInterpreter{Not properly implemented}
test bool concreteTerm2() = s3.thenPart[0] == s1;
@ignoreInterpreter{Not properly implemented}
test bool concreteTerm3() = s3.thenPart[1] == s2;
@ignoreInterpreter{Not properly implemented}
test bool concreteTerm4() = s3.elsePart[0] == s1;
 