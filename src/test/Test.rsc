module Test
import IO;

data NUM int n | fac2(int);

rule f1 fac2(0)                => 1;
rule f2 fac2(int N)            => N * fac2(N - 1);

public void test(int S)
{
	int N = S;
	
	M = fac2(N);
	
	println("S = <S>, N = <N>, M = <M>");

}