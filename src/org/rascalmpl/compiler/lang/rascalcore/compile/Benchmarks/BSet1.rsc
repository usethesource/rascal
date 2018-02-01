module lang::rascalcore::compile::Benchmarks::BSet1

value main(){
    for(i <- [1 .. 200000]){
       a = {1,2,3,4,5,6,7,8,9,10};
       b = {11,12,13,14,15,16,17,18,19,20};
       c = a + b;
       d = c - a;
       e = c - b;
    }
    return 0;
}