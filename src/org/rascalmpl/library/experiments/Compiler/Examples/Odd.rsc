module experiments::Compiler::Examples::Odd

value main(list[value] args){
   res = [];
   for(i <- [0,1,2,3,4,5,6,7,8,9,10,11,12,13], i % 2 == 1)
      res = res + [i];
    return res;
}