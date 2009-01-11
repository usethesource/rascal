module Factorial

import IO;
import Benchmark;

/*
 * Datatype NUM of numerals with zero (z) and successor (s).
 */

data NUM z | s(NUM);

/*
 * add -- rewrite rules for addition on numerals
 */

data NUM add(NUM L,NUM R);
rule a0 add(NUM N, z)          => N;
rule a1 add(z, NUM N)          => N;
rule a2 add(s(NUM N), NUM M)   => s(add(N, M));

/*
 * mul -- rewrite rules for multiplication on numerals.
 */
 
data NUM mul(NUM L, NUM R);

rule m0 mul(NUM N, z)          => z;
rule m1 mul(z, NUM N)          => z;
rule m2 mul(s(NUM N), NUM M)   => add(M, mul(N, M));

/*
 * fac1 -- rewrite rules for factorial on numerals
 */

data NUM fac1(NUM);

rule f1 fac1(z)                 => s(z);
rule f2 fac1(s(NUM N))          => mul(s(N), fac1(N));

public void measure1(int N)
{
	NUM result;
	
	start = currentTimeMillis();
	switch(N){
		case 1:  result = fac1(s(z));
		case 2:  result = fac1(s(s(z)));
		case 3:  result = fac1(s(s(s(z))));
		case 4:  result = fac1(s(s(s(s(z)))));
		case 5:  result = fac1(s(s(s(s(s(z))))));
		case 6:  result = fac1(s(s(s(s(s(s(z)))))));
		case 7:  result = fac1(s(s(s(s(s(s(s(z))))))));
		case 8:  result = fac1(s(s(s(s(s(s(s(s(z)))))))));
		case 9:  result = fac1(s(s(s(s(s(s(s(s(s(z))))))))));
		case 10: result = fac1(s(s(s(s(s(s(s(s(s(s(z)))))))))));
	}
	used = currentTimeMillis() - start;
		
	println("fac1(<N>) = <result>  (<used> millis)");

}

/*
 * fac2 -- rewrite rules for factorial on numerals using built-in int datatype
 */

data NUM int n | fac2(int);

rule f1 fac2(0)                => 1;
rule f2 fac2(int N)            => N * fac2(N - 1);

public void measure2(int N)
{
	start = currentTimeMillis();
	result = fac2(N);
	used = currentTimeMillis() - start;
		
	println("fac2(<N>) = <result>  (<used> millis)");

}

/*
 * fac3 -- function definition for factorial
 */

public int fac3(int N)
{
	if(N <= 0)
		return 1;
	else
		return N * fac3(N - 1);
}

public void measure3(int N)
{
	start = currentTimeMillis();
	result = fac3(N);
	used = currentTimeMillis() - start;
		
	println("fac3(<N>) = <result>  (<used> millis)");

}

public void measure(){
	measure1(5);
	//measure1(6);
	
	measure2(5);
	measure2(6);
	measure2(10);
	
	measure3(5);
	measure3(6);
	measure3(10);
}
	

