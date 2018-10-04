module lang::rascalcore::compile::Examples::SendMoreMoney

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

value main()  = sendMoreMoney();