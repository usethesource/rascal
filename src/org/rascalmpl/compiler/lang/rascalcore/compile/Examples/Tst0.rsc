module lang::rascalcore::compile::Examples::Tst0

import String;

//@javaClass{org.rascalmpl.library.Prelude}
//public java int size(list[&T] lst);
//
//@javaClass{org.rascalmpl.library.Type}
//public java bool eq(value x, value y);
//
//bool isEqual(list[&T] A, list[&T] B) { 
//    if(size(A) == size(B)){
//        for(int i <-[0 .. size(A)]){
//            if(!eq(A[i],B[i])) 
//                return false;
//        }
//        return true;
//    }
//    return false;
//}   
//
//test bool notEqual2(list[&T] A, list[&T] B) = (A != B) ? !isEqual(A,B) : isEqual(A,B);

test bool tstToLowerCase(str S) = !(/[A-Z]/ := "abc");

//value main() = tstToLowerCase("ABC");