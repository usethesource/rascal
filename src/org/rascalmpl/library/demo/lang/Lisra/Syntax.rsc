// tag::module[]
module demo::lang::Lisra::Syntax

layout Whitespace      = [\t-\n\r\ ]*; 
lexical IntegerLiteral = [0-9]+ !>> [0-9];
lexical AtomExp        = (![0-9()\t-\n\r\ ])+ !>>  ![0-9()\t-\n\r\ ];

start syntax LispExp            // TODO: remove constructor names (needed for compiler)
      = int_lit: IntegerLiteral
      | atom_exp: AtomExp
      | par_exp: "(" LispExp* ")"
      ;
// end::module[]      
