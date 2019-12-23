module lang::rascalcore::compile::Examples::Tst4

data D = d1(list[int] ranges);

value main(){
   list[D] ds = [];
   ranges = [r | d1(list[int] ranges) <- ds, r <- ranges];
   return ranges;
}