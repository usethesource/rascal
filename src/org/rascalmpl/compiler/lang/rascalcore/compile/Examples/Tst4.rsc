module lang::rascalcore::compile::Examples::Tst4

int outer1(){
    int f(int n) = n;
    return f(3);
}

int outer2(){
    int f(int n) = n;
    return f(4);
}

//data Tree;
//anno set[int] Tree@messages;
//
//data TModel(list[int] messages = []);
//
//list[int] f(TModel tm) = tm.messages;