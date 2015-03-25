module experiments::Compiler::Examples::MyExceptions

//import Exception;
//import List;
//import Set;
//import Map;
//import IO;
//import util::Math;


data Exception = divide_by_zero();

data Exception = ball();
data Exception = noball();

value playBall() {
        int i = 1;
        int j = 0;
        int k = 0;
        while (i < 50) {
            try {
                if (i % 5 == 0) {
                     throw ball();
                }
                if (i % 11 == 0) {
                     throw noball();
                }
                throw i ;
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
value playBallBug() {
        int i = 1;
        int j = 0;
        int k = 0;
        while (i < 50) {
            try {
                throw i ;
            }
            catch int x : {
            	i = x + 1 ;
            }
      }
      return <i, j, k> ;
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

value main(list[value] args){
//    return safeDivide(25,5);
    return playBallBug();
}
