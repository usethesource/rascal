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
module lang::rascalcore::compile::Examples::Tst3

import Type;
import IO;
layout Layout = " "*;

syntax AB = "a" | "b";
syntax ABStar = AB* abs;
syntax ABPlus = AB+ abs;

// void main(){
//     pt = ([ABStar]"a a").abs;
//     println("typeOf pt:"); iprintln(typeOf(pt));
//     println("pt:"); iprintln(pt);
// }

int size(&E* l) {
    println("size1:"); iprintln(typeOf(l)) ; iprintln(l);
    return (0 | it + 1 | _ <- l);
}

// value main() = size(([ABStar]"a a").abs) == 2;


int size({&E &S}* l){
    println("size2:");  iprintln(typeOf(l))  ; iprintln(l);
    return (0 | it + 1 | _ <- l);
}

//value main() /*test bool sizeABPlus2()*/ = size(([ABPlus]"a b").abs) == 2;

value main() /*test bool sizeABPlus3()*/ = size(([ABPlus]"a b a").abs) == 3;