module experiments::Compiler::Examples::Matching

bool tst1() = 1 := 1;
bool tst2() = !(1 := 2);

value main(list[value] args){

vec = [ 1 := 1, !(1 := 2)];
 for(int i <- index(vec)){
    if(!vec[i])
 }

}