module lang::rascal::tests::extends2::TYPEPAL

extend lang::rascal::tests::extends2::COLLECTOR;

str doCollect(int x){
    return collect(x);
}

str indirectDoCollect(int x , str(int) my_collect){
    return my_collect(x);
}
