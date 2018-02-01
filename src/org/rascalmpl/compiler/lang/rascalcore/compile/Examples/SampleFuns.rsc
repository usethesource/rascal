
module lang::rascalcore::compile::Examples::SampleFuns

import List; 
import Exception;
  
data D = d1(int n) | d1(str s) | d2 (str s); // simple constructors

data D = d3(int n, str opt = "abc");

data D(int x = 3) = d4(str z);

int fun1(int n, int delta = 2) = n + delta;

int fun1(list[int] l) = size(l);    // overloaded via list type

int fun1(list[str] l) = 2 * size(l);

int fun1(list[str] l, int n) = n * size(l);

real fun1(real r) = 2.0 * r;

public (&T <:num) sum(list[(&T <:num)] _:[]) {
    throw ArithmeticException(
        "For the emtpy list it is not possible to decide the correct precision to return.\n
        'If you want to call sum on empty lists, use sum([0.000]+lst) or sum([0r] +lst) or sum([0]+lst) 
        'to make the list non-empty and indicate the required precision for the sum of the empty list
        ");
}

public default (&T <:num) sum([(&T <: num) hd, *(&T <: num) tl])
    = (hd | it + i | i <- tl);

@javaClass{org.rascalmpl.library.Prelude}
public java bool isEmpty(list[&T] lst);

//value main() = d1(24,x=33);