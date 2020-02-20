module lang::rascalcore::compile::Examples::Tst1

syntax A = "a";
syntax B = "b";
syntax C = "c";
syntax AB = AB;
syntax ABC = ABC;
   
AB f(ABC abc){
    switch(abc){
        case (ABC) `<A a>,<B b><C c>`: 
            return (AB) `<A a><B b>`;
    }
    return [AB] "ab";
}

//set[str] f(){
//    res = {};
//    res += 1;
//    return res;
//}