module Test2
import IO;

public bool test(){

    switch({1,2,3,4,5,6}){
    
    case {1, set[int] X, 2} : {
    		println("X = <X>");
    		fail;
    	}
    case {1, int X, 3,4,5,6}: {
    		println(" X = <X>");
    		fail;
    	}
    	
    case {1, int X, 3,4,5}: {
    		println(" X = <X>");
    		fail;
    	}
    
	case {1, int Q, set[int] X, 2, set[int] Y, 3, int Z} : {
			println("Q = <Q>, X = <X>; Y = <Y>, Z = <Z>");
			fail;
		}
	}
	
	return true;
}
