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
module lang::rascalcore::compile::Examples::AssignmentTests

value main() = 42;

test bool assignIntViaExternal() {
    x = 0;
    
    int f(int n) { x = x + 1; return n; }
    
    void g(){
        x += f(9);
    }
    g();
    return x == 10;
}

test bool assignStrViaExternal() {
    x = "";
    
    str f(str n) { x = x + "a"; return n; }
    
    void g(){
        x += f("b");
    }
    g();
   return x == "ab";
}

test bool assignDateTimeViaExternal() { // TODO
    x =  $2022-09-03T09:51:21.097+00:00$;
    
    int f(int n) { x.year = 1000; return n; }
    
    void g(){
        x.hour = f(1);
    }
    g();
   return x == $1000-09-03T01:51:21.097+00:00$;
}


test bool assignLocViaExternal() {
    x = |file:///home/paulk/pico.trm|(0,1,<2,3>,<4,5>);
    
    int f(int n) { x.length = 100; return n; }
    
    void g(){
        x.offset = f(9);
    }
    g();
   return x == |file:///home/paulk/pico.trm|(9,100,<2,3>,<4,5>);
}

test bool assignTupleViaExternal1(){
    tuple[int x, int y, int z] facts = <0,1,2>;
    
    int f(int n) { facts.y = 100; return n; }
    
    void g(){
        facts.x = f(9);
    }
    
    g();
    return facts == <9,100,2>;
}

test bool assignTupleViaExternal2(){
    tuple[int x, int y, int z] facts = <0,1,2>;
    
    int f(int n) { facts[1] = 100; return n; }
    
    void g(){
        facts[0] = f(9);
    }
    
    g();
    return facts == <9,100,2>;
}

test bool assignListViaExternal1(){
    facts = [];
    
    int f(int n) { facts += 0; return n; }
    
    void g(){
        facts += f(9);
    }
    
    g();
    return facts ==  [0,9];
}

test bool assignListViaExternal2(){
    facts = [];
    
    list[int] f(list[int] n) { facts += [0]; return n; }
    
    void g(){
        facts += f([9]);
    }
    
    g();
    return facts ==  [0,9];
}

test bool assignListViaExternal3(){ // eval/comp differ
    facts = [1,2];
    
    int f(int n) { facts += [0]; return n; }
    
    void g(){
        facts[f(1)] = 9;
    }
    
    g();
    return facts == [1,9,0];   
}

test bool assignMapViaExternal(){
    facts = ();
    
    int f(int n) { facts[n] =100; return n; }
    
    void g(){
        facts[10] = f(9);
    }
    
    g();
    return facts == (10:9,9:100);
}

test bool assignSetViaExternal1() {
    facts = {};
    
    int f(int n) { facts += 100; return n; }
    
    void g(){
        facts += f(9);
    }
    
    g();
    return facts == {9,100};
}

test bool assignSetViaExternal2() {
    facts = {};
    
    set[int] f(set[int] n) { facts += {100}; return n; }
    
    void g(){
        facts += f({9});
    }
    
    g();
    return facts == {9, 100};
}

//// node
//
data D = d(int x, int y, int z);

test bool assignADTViaExternal(){
    facts = d(0,1,2);
    
    int f(int n) { facts.y = 100; return n; }
    
    void g(){
        facts.x = f(9);
    }
    
    g();
    return facts ==  d(9,100,2);
}