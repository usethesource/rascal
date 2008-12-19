module Trans

// Compute transitive closure using solve

int main(){

	rel[int,int] R1 =  {<1,2>, <2,3>, <3,4>};

	with
     	rel[int,int] T = R1;
	solve
		T = T + (T o R1);

	return T;
}


