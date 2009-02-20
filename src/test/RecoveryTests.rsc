module RecoveryTests

public int recoveryOfLocalVariable()
@should{return 0}
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1; 
     	fail;
     } 	
	};
    return x;
}

public int nestedRecoveryOfLocalVariable() 
@should{return 3}
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1;
     	visit (l) {
     	   case int n: {
     	      x = x + 1;
     	      fail;
     	   }
     	}; 
      } 	
	};
    return x;
}

public int noNestedRecovery()
@should{return 12} // but why?
{
	int x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1;
     	visit (l) {
     	   case int i: {
     	      x = x + 1;
     	   }
     	}; 
      } 	
	};
    return x;
}

public int recoveryOfLocalVariableUsingIfThen()
@should{return 0}
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1; 
     	if (n > 10) {
     	   x = x + 1; // another update
     	} else
     		fail;
      } 	
	};
    return x;
}

public int gx = 0;

public int recoveryOfGlobalVariable() 
@should{return 0}
{
    //global int gx;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	gx = gx + 1; 
     	fail;
     } 	
	};
    return gx;
}

public int gt = 0;

data City = amsterdam;

rule a1 amsterdam : { 
  gt = gt + 1;
  fail;
};

public int recoveryOfGlobalAfterFailingRule() {
	x = amsterdam;
	return gt;
}

public bool meddle() {
  gt = 123;
  return true;
}

public int recoveryOfGlobalDuringComprehension() {
	aset = {1, 2, 3};
	another = { x | int x <- aset, meddle() };
	return gt;
}




