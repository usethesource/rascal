module lang::rascalcore::compile::Examples::Tst2
 
data C = c(int i);     
//bool() x = bool() { return false; } ;

//public void changeX(bool() newX) { x = newX; }

//public bool getX() = x();

C c(int i) {
  if (i == 0 || i mod 5 != 0) 
    fail c;
  else
    return c(i / 5);
}      