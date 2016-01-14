module experiments::Compiler::Examples::Tst1
    
test bool nestedRange1() {
   return [ i | int i <- [10..12] ] == [10..12];
}