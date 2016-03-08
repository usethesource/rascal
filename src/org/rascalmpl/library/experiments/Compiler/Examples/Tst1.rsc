module experiments::Compiler::Examples::Tst1

bool anonymousFunctionComposition() {
   return  ( int (int n) { return n + 1; }
           o int (int n) { return n * 2; }
           ) 
           (10) ;
} 

value main() =  anonymousFunctionComposition();