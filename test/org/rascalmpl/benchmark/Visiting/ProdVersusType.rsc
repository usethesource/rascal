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
module Visiting::ProdVersusType

import util::Benchmark;
import IO;
import ParseTree;
import lang::rascal::\syntax::Rascal;
import util::Reflective;

int NUM_RUNS = 100;

int TypeMatch(Tree tr) {
    int i = 0;
    s = cpuTime();
    for (_ <- [0..NUM_RUNS]) {
        top-down-break visit (tr) {
            case TypeVar _: i = i + 1;
        }
    }
    println("TypeMatch elapsed: <(cpuTime() - s)/1000000> ms");
    return i;
}

int ProdMatch(Tree tr) {
    int i = 0;
    s = cpuTime();
    for (_ <- [0..NUM_RUNS]) {
        top-down-break visit (tr) {
            case (TypeVar) `&<Name _>`: i = i + 1;
            case (TypeVar) `&<Name _> \<: <Type _>`: i = i + 1;
        }
    }
    println("ProdMatch elapsed: <(cpuTime() - s)/1000000> ms");
    return i;
}
 
 void warmup(start[Module] m){
    for(_ <- [0..5]){
        ProdMatch(m);
        TypeMatch(m);
    }
 }
value main(){
    m = parseModuleWithSpaces(|std:///List.rsc|);
    warmup(m);
    println("ProdMatch:");
    a = ProdMatch(m);
    println("TypeMatch:");
    b = TypeMatch(m);
    return <a,b>;
}