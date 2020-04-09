module lang::rascalcore::compile::Examples::Tst1
                
                
 int f(int n) { return n += 1; }      
          
//data D = d1(int n) | d2(int n, int m);
//
//set[int] f(list[D] ds)
//    = { n | d <- ds, d1(int n) := d || d2(int n, _) := d };
//
