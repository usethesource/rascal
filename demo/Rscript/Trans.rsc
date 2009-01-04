module Trans

// Compute transitive closure using solve

public rel[int,int] trans(rel[int,int] R){

	rel[int,int] R1 =  R;

	with
     	rel[int,int] T = R1;
	solve
		T = T + (T o R1);

	return T;
}

public bool testTrans()
{
	return trans({<1,2>, <2,3>, <3,4>}) ==  {<1,2>, <1,3>,<1,4>,<2,3>,<2,4>,<3,4>};
}

