module demo::lang::Func::Test

import demo::lang::Func::Load;
import demo::lang::Func::Eval0;
import demo::lang::Func::Eval1;
import demo::lang::Func::Eval2; 
import demo::lang::Func::Eval3;


public str fact0 = "fact(n) = 
				     if n \<= 1 then 
				       1 
				     else 
				       n * fact(n-1)
			         end";


public str fact1 = "fact(n) = 
				    let
					    x = n
					  in
					    if x \<= 1 then 
					      x 
					    else 
					      x * fact(x-1)
					    end
					  end";
					  

public str fact2 = "fact(n) = 
					    if n \<= 1 then 
					      n := 1
					    else 
					      n := n * fact(n-1)
					    end;
					    n";
					    
public str fact3 = "swap(a, b) =
					  let 
					    temp = *a
					  in
					    *a := *b;
					    *b := temp
					  end
					
					fact(n) = 
					 let
					    x = 1,
					    y = 0
					  in
					    if n \<= 1 then 
					      x := 1
					    else 
					      x := n * fact(n-1)
					    end;
					    swap(&x, &y);
					    y
					  end";
					  
					  
public test bool evalFact0() = eval0("fact", [10], load(fact0)) == 3628800;
public test bool evalFact1() = eval1("fact", [10], load(fact1)) == 3628800;
public test bool evalFact2() = eval2("fact", [10], load(fact2)) == <("n":3628800),3628800>;
public test bool evalFact3() = eval3("fact", [10], load(fact3)) == <[10],3628800>;



