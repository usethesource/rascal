module lang::rascalcore::compile::Examples::Tst0

//@javaClass{org.rascalmpl.library.Prelude}
public bool isEmpty(list[&T] lst) = true;

//@javaClass{org.rascalmpl.library.Prelude}
//public java list[&T] shuffle(list[&T] l);
//
//@doc{
//.Synopsis
//Shuffle a list with a seed.
//
//.Description
//Returns a random (unbiased) shuffled list, every call with the same seed shuffles in the same order.
//
//.Examples
//[source,rascal-shell]
//----
//import List;
//shuffle([1,2,3,4]);
//shuffle([1,2,3,4]);
//shuffle([1,2,3,4], 1);
//shuffle([1,2,3,4], 1);
//----
//}
//@javaClass{org.rascalmpl.library.Prelude}
//public java list[&T] shuffle(list[&T] l, int seed);