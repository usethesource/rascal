module lang::rascalcore::compile::Examples::Tst2

public &T head([&T h, *&T _]) = h; 
public &T head(list[&T] _:[]) { throw "EmptyList()"; }

// Get the first n elements of a list
@javaClass{org.rascalmpl.library.Prelude}
public java list[&T] head(list[&T] lst, int n);

