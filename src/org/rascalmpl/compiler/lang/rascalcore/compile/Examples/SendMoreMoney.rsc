@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
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