module experiments::Compiler::Examples::OverloadingMatch

data D = d(str s) | d(int n) | d();

public default D d(str s) = d();

public D d(0) = d(-1);
public D d("0") = d("-1");

public value main(list[value] args) {
	int n = 0;
	if( D::d(int v) := d(0) ) {
		n = v;
	} 
	return n;
}
