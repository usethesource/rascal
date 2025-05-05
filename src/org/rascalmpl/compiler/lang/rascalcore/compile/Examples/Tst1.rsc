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
module lang::rascalcore::compile::Examples::Tst1
layout Layout = " "*;
   
syntax AB = "a" | "b";
//syntax ABPlus = AB+ abs;
syntax ABStar = AB* abs;
// syntax ABPlusSep = {AB ","}+ abs;
// syntax ABStarSep = {AB ","}* abs;

int size(&E* l) = (0 | it + 1 | _ <- l);
    

value main() // test bool sizeABStar2() 
  = size(([ABStar]"a a").abs) == 2;

// test bool sizeABPlus2() = size(([ABPlus]"a b").abs) == 2;

// test bool sizeABPlus3() = size(([ABPlus]"a b a").abs) == 3;

int size({&E &S}* l) = (0 | it + 1 | _ <- l);

// @ignoreInterpreter{Not implemented}
// test bool sizeABStarSep0() = size(([ABStarSep]"").abs) == 0;
// @ignoreInterpreter{Not implemented}
// test bool sizeABStarSep1() = size(([ABStarSep]"a").abs) == 1;
// @ignoreInterpreter{Not implemented}
// test bool sizeABStarSep2() = size(([ABStarSep]"a, b").abs) == 2;