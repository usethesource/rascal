module lang::rascal::tests::functionality::CommonKeywordParameter4::B

extend lang::rascal::tests::functionality::CommonKeywordParameter4::C;
 
data D(int nn = -3, D m = d1(nn));  

data D(int mm = -4, D p = d2("b",nn=mm).m);   
