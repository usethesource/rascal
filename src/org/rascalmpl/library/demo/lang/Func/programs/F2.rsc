module demo::lang::Func::programs::F2

public str F2 =
"fact(n) = if n \<= 1 then 
             n := 1
	      else 
	         n := n * fact(n-1)
	      end;
	      n";
