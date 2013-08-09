module experiments::CoreRascal::Translation::Examples::Example1



//int inc(int n) = n + 1;

int fac(int n) = (n == 1) ? 1 : n * fac(n - 1);


int main() { 
    //int n = 57;
	//return n + 2;
	return fac(3);
}