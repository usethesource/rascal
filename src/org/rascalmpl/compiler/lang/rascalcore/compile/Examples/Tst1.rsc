module lang::rascalcore::compile::Examples::Tst1

  

//import IO;
//data Exception
//    = ArithmeticException(str msg);
//
//@doc{
//.Synopsis
//Test whether a set is empty.
//
//.Description
//Yields `true` if `s` is empty, and `false` otherwise.
//
//.Examples
//[source,rascal-shell]
//----
//import Set;
//isEmpty({1, 2, 3});
//isEmpty({});
//----}
//@javaClass{org.rascalmpl.library.Prelude}
//public java bool isEmpty(set[&T] st);
//
//
//public (&T <:num) sum(set[(&T <:num)] _:{}) {
//    throw ArithmeticException(
//        "For the emtpy set it is not possible to decide the correct precision to return.\n
//        'If you want to call sum on empty set, use sum({0.000}+st) or sum({0r} +st) or sum({0}+st) 
//        'to make the set non-empty and indicate the required precision for the sum of the empty set 
//        ");
//}
@doc{
.Synopsis
Sum the elements of a set.

.Examples
[source,rascal-shell]
----
import Set;
sum({3, 1, 4, 5});
sum({3, 1.5, 4, 5});
----}
public default int sum({int e, *int r}){
    return (e | it + i | i <- r);   
    } 

//
//test bool tst_sum(set[int] S) {
//    println("S = <S>");
//    
//    return  isEmpty(S) || sum(S) == (0 | it + x | x <- S);
//
//}
//
//value main() = tst_sum({3});


//--------------------------
//
//int f({}) = 0;
//default int f(set[int] S) = 10;
//
//value main() = f({2});