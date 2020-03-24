module lang::rascalcore::compile::Examples::Tst2


int goed(){
    return 1;
    goed();
}
void err0(){
    return ;
    err0();
}

void err1(){
    throw "BOO";
    err1();
}

void err2(){
    throw;
    err2();
}

//data Symbol = \int();
//data Production;
//bool f(type(symbol,definitions)) = true;

//data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
//value main(){
//    type[&T] x = type(\int(), ());
//    if(type(Symbol symbol, map[Symbol,Production] definitions) := x) return symbol;
//    
//    return false;
//}