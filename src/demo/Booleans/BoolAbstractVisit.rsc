module demo::Booleans::BoolAbstractVisit

data Bool = btrue;
data Bool = bfalse;
data Bool = band(Bool left, Bool right);
data Bool = bor(Bool left, Bool right);  

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

bool test(){
	
   return reduce(bor(band(btrue,btrue),band(btrue, bfalse))) == btrue;
}