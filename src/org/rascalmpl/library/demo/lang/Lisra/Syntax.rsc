module demo::lang::Lisra::Syntax

layout Whitespace      = [\t-\n\r\ ]*; 
lexical IntegerLiteral = [0-9]+ !>> [0-9];
lexical AtomExp        = (![0-9()\t-\n\r\ ])+ !>>  ![0-9()\t-\n\r\ ];

start syntax LispExp
      = IntegerLiteral
      | AtomExp
      | "(" LispExp* ")"
      ;