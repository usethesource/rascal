module experiments::CoreRascal::Translation::Examples::Example1

int x = 1;
int z =  x + 1;
int q = 3;
//int inc(int n) = n + 1;

int fac(int n) = (n <= 1) ? q : n * fac(n - 1);


int main() { 
//	return inc(3);
//    int n = 57;
//	return n + 2;
	return fac(z);
return z;
}