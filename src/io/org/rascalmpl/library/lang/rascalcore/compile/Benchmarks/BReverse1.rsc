module lang::rascalcore::compile::Benchmarks::BReverse1

list[int] reverse1(list[int] lst) = lst == [] ? [] : (([int x] := lst) ? [x] : lst[-1] + reverse1(lst[..-1]));

value main(){
    for(i <- [1 .. 5000]){
       z= reverse1([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]);
    }
    return 0;
}