module experiments::Compiler::Examples::Simple

value main(list[value] args){
 	int j = 0 ;
    while (j < 100){
       j = j + 1 ;
    }
    return j;
}