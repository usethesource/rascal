module lang::rascalcore::compile::Examples::Tst1


//data X = x();
//data X(int right = 10, int rightsq = right * right);
//
//value main() //test bool Right_x_right5() 
//    = x(right = 20)?;

int f() = 4/0;

value main() = f()?;

//import List;
//
//public bool isSorted(list[int] L) = !any(int i <- index(L), int j <- index(L), i < j && elementAt(L,i) > elementAt(L,j));
//
//value main() = isSorted([1, 2, 3]);

