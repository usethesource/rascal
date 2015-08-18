
module experiments::Compiler::Examples::Tst4

data NODE1 = nd(NODE1 left, NODE1 right) | n(int x) | s(str y);

NODE1 N1 = nd(n(3), s("abc"));

int cnt(NODE1 t) {
	int C = 0;
	top-down visit(t) {
		case int N: C = C + 1;
	}
	return C;
}

value main(list[value] args) = cnt(N1);