module test::Test

import IO;

public bool test(){

    list[int] L = [1,2,3,4,5];
    list[int] R = [];
    for(int i <- [1 .. 1000]){
        R = R + L;
    }
    list[int] R2 = [];
    list[int] L2 = [10,20,30,40,50];
    for(int i <- [1 .. 2000]){
        R2 = R2 + L2;
    }
    
    list[int] R3 = [];
    list[int] L2 = [10,20,30,40,50];
    for(int i <- [1 .. 3000]){
        R3 = R3 + L2;
    }
    
    return true;
}