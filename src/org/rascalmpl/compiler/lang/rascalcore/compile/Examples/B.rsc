@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
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
module  lang::rascalcore::compile::Examples::B
             
// //test bool assignmentNotOK1()
// void f(&T x) { &T y = 1; }

// //test bool makeSmallerNotOK() 
// &T <: num makeSmallerThan(&T <: num n) {
//       if (int i := n) {
//          return i;
//       }
//       return n;
// }

// //test bool returnHeadTailNotOK() 
// tuple[&T, list[&T]] headTail(list[&T] l) {
//        if ([&T h, *&T t] := l) {
//          return <h, t>;
//        }
//        return <0,[]>;
// }
  
// //test bool returnNotOK3()
// &T get(list[&T] _) = 1;

// //test bool returnNotOK4()
// list[&T] emptyList(list[&T] _) = [1];
    
test bool selfApplyCurry() {
    &S(&U) curry(&S(&T, &U) f, &T t) = &S (&U u) { 
      return f(t, u); 
    };

    int addition(int i, int j) = i + j;

    func = curry(curry, addition);

    assert int(int)(int) _ := func;

    func2 = func(1);

    assert int(int) _ := func2;

    return func2(1) == 2;
}
          
// data Wrap[&T] = wrap(&T val);

// &T id(&T arg) = arg;
        
// &T f(&T param) {
//     Wrap[&T] x = wrap(param);
                   
//     return id(x);  // "Types Wrap[&T] and &T do not match"
// }
           
           

// // data D = d(int n) | d(str s);

// // void f(D x){
// //     d(arg) := x;
// // }

// // syntax Body = "body";

// // alias Body = int;

// // Body f(Body b) = b;

// // data AType;

// // data MuExp = muFailReturn(AType tp);

// //  MuExp muReturn1(AType t, muFailReturn(AType t)){
// //     return muFailReturn(t);
// // }