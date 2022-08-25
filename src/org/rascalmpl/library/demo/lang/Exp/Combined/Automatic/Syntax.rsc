// tag::module[]
module demo::lang::Exp::Combined::Automatic::Syntax

lexical LAYOUT = [\t-\n\r\ ];                    

layout LAYOUTLIST = LAYOUT*  !>> [\t-\n\r\ ] ;  
    
lexical IntegerLiteral = [0-9]+;           

start syntax Exp =                         
                   con: IntegerLiteral   // <1>
                 | bracket "(" Exp ")"     
                 > left mul: Exp "*" Exp // <2>  
                 > left add: Exp "+" Exp // <3>   
                 ;
// end::module[]
