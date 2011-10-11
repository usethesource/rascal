module demo::lang::Exp::Concrete::WithLayout::Syntax

lexical LAYOUT = [\t-\n\r\ ];                    /*1*/

layout LAYOUTLIST = LAYOUT*  !>> [\t-\n\r\ ] ;   /*2*/
    
lexical IntegerLiteral = [0-9]+;           

start syntax Exp =                         
                   IntegerLiteral          
                 | bracket "(" Exp ")"     
                 > left Exp "*" Exp        
                 > left Exp "+" Exp        
                 ;