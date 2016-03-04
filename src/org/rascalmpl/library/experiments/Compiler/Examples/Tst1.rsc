module experiments::Compiler::Examples::Tst1

test bool tst(){
    list[int] inputs = [0,1,2,3,4,5,6,7,8,9]; 
    list[int] outputs1 = [ int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int m: return m*(m-1); } }            /* renamed n to m*/
                           ( int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int m: return (m-1) + (m-2); } }    /* renamed n to m*/
                           (i)) 
                         | int i <- inputs ]; 
    list[int] outputs2 = [ (int (int n) { switch(n) { case 0: return 1; case 1: return 1; case int m: return m*(m-1); } }           /* renamed n to m*/
                          o int (int n) { switch(n) { case 0: return 0; case 1: return 1; case int m: return (m-1) + (m-2); } })    /* renamed n to m*/
                            (i) 
                         | int i <- inputs ]; 
    return outputs2 == outputs2; 
} 
