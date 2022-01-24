module lang::rascalcore::compile::Examples::Tst2


data S =  a()
        | b()
        | c()
        | d()
        | par() 
        | \pl(list[S] parameters)
        ;

bool abcpl2(S x) { 
   y = (  //a() := x 
          //|| 
             b() := x
          || c() := x 
          || pl([par(), *_]) := x
         );
   return y;
} 