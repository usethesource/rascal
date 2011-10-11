module demo::languages::Exp::Combined::Automatic::Eval

import ParseTree;   

lexical LAYOUT = [\t-\n\r\ ];                    

layout LAYOUTLIST = LAYOUT*  !>> [\t-\n\r\ ] ;  
    
lexical IntegerLiteral = [0-9]+;           

start syntax CExp =                         
                   con: IntegerLiteral   
                 | bracket "(" CExp ")"     
                 > left mul: CExp "*" CExp  
                 > left add: CExp "+" CExp  
                 ;                                          


       
public CExp parse(str txt) = parse(#CExp, txt);

data AExp = con(int n)
         | mul(AExp e1, AExp e2)
         | add(AExp e1, AExp e2)
         ;  