module lang::rascalcore::compile::Examples::Tst1

//    
//@javaClass{org.rascalmpl.library.Prelude}
//public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;


value main() = f(1, 0, bool (&T a, &T b) { return a < b; });


int f(int x, int y, bool(int x, int y) less) = less(x, y) ? x : y;