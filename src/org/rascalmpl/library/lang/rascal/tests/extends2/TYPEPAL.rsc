module lang::rascalcore::compile::Examples::TYPEPAL

extend lang::rascalcore::compile::Examples::COLLECTOR;

str doCollect(int x){
    return collect(x);
}

str indirectDoCollect(int x , str(int) my_collect){
    return my_collect(x);
}