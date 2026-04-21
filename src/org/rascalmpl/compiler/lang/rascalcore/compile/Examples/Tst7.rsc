module lang::rascalcore::compile::Examples::Tst7
  

list[&T] tail([&T _, *&T t]) = t;

&T top([&T t, *&T _]) = t;
               
&T getFirstFrom([&T f, *&T _]) = f;

&T max([&T h, *&T t]) = (h | e > it ? e : it | e <- t);

&T <: int f(&T <: num _) = 1;   
   
  
// int f(int n, int m) = 3;                 
       
    
                     
                                     