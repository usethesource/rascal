module lang::rascalcore::compile::Examples::Tst1
 
set[int] f(){
    res = {};
    for(i <- [1..10]){
        res += 1;
    }
    return res;
}