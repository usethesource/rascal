module demo::lang::Func::programs::F0

public str F0 =
"fact(n) = if n \<= 1 then
             1 
          else 
             n * fact(n-1)
          end";
