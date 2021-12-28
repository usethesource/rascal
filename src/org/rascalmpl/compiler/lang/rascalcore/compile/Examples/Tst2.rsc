module lang::rascalcore::compile::Examples::Tst2
   
extend examples::CommonLex;     //<1>

syntax Decl
    = "var" Id "=" Integer ";"  //<3>
    ;
   
syntax Exp 
   = Id                             //<4>
   | Integer                        //<5>
   ;   

keyword Reserved
    = "var" | "if" | "then" | "else"
    ;    
    
value main() = (Decl) `var <Id x> = 3;` := [Decl] "var x = 3;";