module lang::rascalcore::compile::Examples::Tst1
    
data Formula 
  = \true()
  | \or(Formula lhs, Formula rhs)
  | \or(set[Formula] args)
  //| \if(Formula lhs, Formula rhs)
  //| \fi(Formula lhs, Formula rhs)
  //| \iff(Formula lhs, Formula rhs)
  ;

Formula simplify(or({\true(), Formula _}))   = \true();


data F = 
         f(str s, int n)
       | f(int n, str s)
       ;

str g(f(str s, n)) 
    = n;


//set[int] g(set[set[int]] ns)
//    = { *x | st <- ns, {1, *int x} := st || {2, *int x} := st };