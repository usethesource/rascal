module lang::rascalcore::compile::Examples::Tst1


keyword Keyword =
  "continue" 
  | "package" 
  | "short" 
  | "boolean" 
  ;
lexical ID =
    // Yes, this would be more correct, but REALLY slow at the moment
    //JavaLetter JavaLetterDigits* 
    //
    // therefore we go the ascii route:
  [$ A-Z _ a-z] [$ 0-9 A-Z _ a-z]* 
  ; 
  
keyword IDKeywords =
  "null" 
  | Keyword 
  | "true" 
  | "false" 
  ;
  
syntax Identifier =
   id: [$ A-Z _ a-z] !<< ID \ IDKeywords !>> [$ 0-9 A-Z _ a-z] 
  ;
  
syntax ExpressionName = Identifier ;  

keyword ElemValKeywords =
  LeftHandSide "=" Expression 
  ;
  
syntax LeftHandSide = ExpressionName;

syntax Expression = AssignmentExpression
                  ;
syntax AssignmentExpression =  Assignment
                            ;
 syntax Assignment = LeftHandSide AssignmentOperator Expression ;

syntax AssignmentOperator = "=" 
                          | "*="  
                          | "/="  
                          | "%="  
                          | "+="  
                          | "-="  
                          | "\<\<="  
                          | "\>\>="  
                          | "\>\>\>="  
                          | "&="
                          | "^="  
                          | "|=" 
                          ;                         
                              
//import analysis::m3::Core;
////import lang::rascalcore::compile::Examples::Tst2;
//
//value foo(M3 model)
//{ return model.modifiers;  
//}