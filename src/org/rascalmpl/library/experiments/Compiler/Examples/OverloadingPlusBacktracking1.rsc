module experiments::Compiler::Examples::OverloadingPlusBacktracking1

@javaClass{org.rascalmpl.library.Prelude}
public java int size(list[int] lst);

public int f([*int x,*int y]) {
	if(size(x) == size(y)) {
		return -1000;
	}
	fail;
}
public default int f(list[int] l) = 0;

public int g([1,2,3,4,5,6]) {
	return -2000;
}
public default int g(list[int] l) = -3000;

public int h(list[int] _) {
	fail;
}
public default int h(list[int] l) = -3000;

public value main(list[value] args) {
	return f([1,2,3,4,5,6]) 
		   + g([1,2,3,4,5,6]) 
		   + g([1,2,3,4,5]) 
		   + h([1,2,3,4,5,6]);
}
