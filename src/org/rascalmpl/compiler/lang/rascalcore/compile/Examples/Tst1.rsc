module lang::rascalcore::compile::Examples::Tst1

public list[&T] sort(list[&T] lst) =
    sort(lst, bool (&T a, &T b) { return a < b; } );
    
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] sort(list[&T] l, bool (&T a, &T b) less) ;