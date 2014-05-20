module tests::functionality::CallTestsAux
     
bool() x = bool() { return false; } ;

public void changeX(bool() newX) { x = newX; }

public bool getX() = x();
  
       