module test::Test2


		int cnt(NODE1 T) {
		   int C = 0;
		   visit(T) {
		      case int N: C = C + 1;
		    };
		    return C;
		}
		
		data NODE1 = f(value V) | f(value V1, value V2) | f(value V1, value V2, value V3) | g(value V1, value V2) | h(value V1, value V2);
		
public void test(){
	assert cnt(f(3)) == 1;
}
