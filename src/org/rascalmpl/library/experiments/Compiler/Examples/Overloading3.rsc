module experiments::Compiler::Examples::Overloading3

data D = d(str s) | d(int n);

public D d(0) = d(-1);
public D d("0") = d("-1");

public value main(list[value] args) {
	x = d(0);
	y = d("0");
	k = d(1);
	z = d("1");
	return <x,y,k,z>; // <d(-1), d("-1"), d(1), d("1")>
}