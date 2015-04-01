module experiments::Compiler::Examples::MyExceptions

data Exception = divide_by_zero();
data Exception = ball();
data Exception = noball();

value playBall() {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < 5000) {
            try {
                if (i % 5 == 0) {
                     throw ball();
                }
                if (i % 11 == 0) {
                     throw noball();
                }
                throw i ;
                if ( i == 4999 )
                	throw "Leave" ;
            }
            catch ball() : {
                i = i + 1 ;
                j = j + 1 ;
            }
            catch noball() : {
            	i = i + 1 ;
                k = k + 1 ;
            }
            catch int x : {
            	i = x + 1 ;
            }
      }
      return <i, j, k> ;
}

int f(int i) {
	throw i ;
}
value playBallBug() {
        int i = 1;
        int y = 0 ;
        while (i < 50) {
            try {
				y = 1 + f(i) ;
            }
            catch int x : {
            	i = x + 1 ;
            }
      }
      return <i> ;
}

int divide(int x, int y) throws divide_by_zero { 
	if(y == 0){ 
		throw divide_by_zero(); 
	} else { 
		return x / y; 
	} 
} 
  	
int safeDivide(int x, int y){ 
	try 
		return divide(x,y); 
	catch:  
 		return 101010101; 
}

int f2(int x) {
	if ( x == 1 ) throw 1;
	throw "not one" ;
}

value nested(int i) {
	int a = 0 ;
	try {
		try {
		     a = 1 + f2(i) ;
		}
		catch int x:{
			return x;
		}
	}
	catch str y: {
		return y ;
	}
}

value main(list[value] args){
//    return playBallBug();
	return < nested(0), nested(1) > ;
    return safeDivide(25,0);
	return playBall();
}