module Test

import IO;
import Exception;

public bool test(){

    try {
	 readFile("XXX");
    
    } 
    
    catch (Exception E){
        if(NoSuchFile(str Name) := E)
        
    		println("yes");
    	else
    	    println("no");
    }
    
    catch (value V){
      println("catch <V>");
    }
    return true;
}