module lang::rascalcore::compile::Examples::Tst3

import util::Math;
@javaImport{import java.lang.System;}
@javaClass{org.rascalmpl.library.util.Benchmark}
public java int cpuTime();
 
int fac(0) = 1;
default int fac(int n) = n * fac(n-1);

int fac2(int n) = n <= 0 ? 1 : n * fac2(n-1);

public set[list[int]] sendMoreMoney(){
   ds = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

   res = {[S,E,N,D,M,O,R,Y] | 
           int S <- ds, 
           int E <- ds - {S}, 
           int N <- ds - {S, E},
           int D <- ds - {S, E, N},
           int M <- ds - {S, E, N, D},
           int O <- ds - {S, E, N, D, M},
           int R <- ds - {S, E, N, D, M, O},
           int Y <- ds - {S, E, N, D, M, O, R},
           S != 0, M != 0,
                       (S * 1000 + E * 100 + N * 10 + D) +
                       (M * 1000 + O * 100 + R * 10 + E) ==
           (M * 10000 + O * 1000 + N * 100 + E * 10 + Y)};
    return res;
}
 
str bottles(0)     = "no more bottles"; 
str bottles(1)     = "1 bottle";
default str bottles(int n) = "<n> bottles"; 

public str sing() =
  "<for(n <- [99 .. 1]){>
  '<bottles(n)> of beer on the wall, <bottles(n)> of beer.
  'Take one down, pass it around, <bottles(n-1)> of beer on the wall
  'No more bottles of beer on the wall, no more bottles of beer.
  'Go to the store and buy some more, 99 bottles of beer on the wall.
  <}>";

int main() {
   
    before = cpuTime();
    for(int i <- [1..10]){
        // = fac(30);
        //res2 = fac2(30);
        //res3 = sing();
        res4 = sendMoreMoney();
        //res5 = trans(R);
    }
    after = cpuTime();
    return (after - before); // nano to milli
}
    