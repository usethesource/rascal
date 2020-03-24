module lang::rascalcore::compile::Examples::Tst2



void foo(node tree){
  for (a <- tree) {
    if (node x := a);
    if (list[value] _ := a);
  }
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