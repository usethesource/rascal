module lang::rascalcore::compile::Examples::Tst1

    
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;


value main() = sort([1,3,2], bool (&T a, &T b) { return a < b; });
