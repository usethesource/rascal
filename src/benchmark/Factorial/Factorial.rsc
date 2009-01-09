module Factorial

import IO;
import Benchmark;

data NUM z | s(NUM);


data NUM add(NUM L,NUM R);
rule a0 add(NUM N, z)          => N;
rule a1 add(z, NUM N)          => N;
rule a2 add(s(NUM N), NUM M)   => s(add(N, M));

data NUM mul(NUM L, NUM R);

rule m0 mul(NUM N, z)          => z;
rule m1 mul(z, NUM N)          => z;
rule m2 mul(s(NUM N), NUM M)   => add(M, mul(N, M));

data NUM fac(NUM);

rule f1 fac(z)                 => s(z);
rule f2 fac(s(NUM N))          => mul(s(N), fac(N));

public void measure1(int select)
{
	start = currentTimeMillis();
	switch(select){
		case 1:  result = fac(s(z));
		case 2:  result = fac(s(s(z)));
		case 3:  result = fac(s(s(s(z))));
		case 4:  result = fac(s(s(s(s(z)))));
		case 5:  result = fac(s(s(s(s(s(z))))));
		case 6:  result = fac(s(s(s(s(s(s(z)))))));
		case 7:  result = fac(s(s(s(s(s(s(s(z))))))));
		case 8:  result = fac(s(s(s(s(s(s(s(s(z)))))))));
		case 9:  result = fac(s(s(s(s(s(s(s(s(s(z))))))))));
		case 10: result = fac(s(s(s(s(s(s(s(s(s(s(z)))))))))));
	}
	used = currentTimeMillis() - start;
		
	println("fac(<N>) = <result>  (<used> millis)");

}

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
