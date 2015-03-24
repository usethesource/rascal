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
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < 100000) {
            try {
                if (i % 5 == 0) {
                     throw ball();
                }
                if (i % 11 == 0) {
                     throw noball();
                }
                i = i + 1 ;
            }
            catch ball() : {
                i = i + 1 ;
                j = j + 1 ;
            }
            catch noball() : {
            	i = i + 1 ;
                k = k + 1 ;
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

int main(list[value] args){
//    return safeDivide(25,5);
    return playBall();
}
