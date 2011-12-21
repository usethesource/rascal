module demo::lang::Func::programs::F1

public str F1 =
"fact(n) = let
	        x = n
          in
	        if x \<= 1 then 
	           x 
	        else 
		       x * fact(x-1)
	        end
	      end";
