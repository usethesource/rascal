module Bool-abstract-visit

data Bool btrue;
data Bool bfalse;
data Bool band(Bool L, Bool R);
data Bool bor(Bool L, Bool R);  

public Bool reduce(Bool B) {
    return innermost visit(B) {
      case band(btrue, Bool B1) => B1		// Use variables
      case band(bfalse, Bool B1)=> bfalse   //TODO: should become B1!
      
      case bor(btrue, btrue)    => btrue    // Use a truth table
      case bor(btrue, bfalse)   => btrue
      case bor(bfalse, btrue)   => btrue
      case bor(bfalse, bfalse)  => bfalse
    };
}

public bool testBool-abstract-visit(){
	
   return reduce(bor(band(btrue,btrue),band(btrue, bfalse))) == btrue;
}