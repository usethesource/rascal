module lang::rascalcore::compile::Examples::Tst1

//@javaClass{org.rascalmpl.library.Prelude}
//public java bool isEmpty(list[&T] lst);

public &T head([&T h, *&T _]) = h; 
public &T head(list[&T] _:[]) { throw "EmptyList()"; }

//public list[&T] tail([&T _, *&T t]) = t;
//public list[&T] tail(list[&T] _:[]) { throw "EmptyList()"; }

//public str intercalate(str sep, list[value] l) = 
//    (isEmpty(l)) ? "" : ( "<head(l)>" | it + "<sep><x>" | x <- tail(l) );

value FFF(list[value] l) = head(l);
