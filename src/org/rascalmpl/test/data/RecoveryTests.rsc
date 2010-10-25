module RecoveryTests

@should{return 0}
public int recoveryOfLocalVariable()
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

@should{return 3}
public int nestedRecoveryOfLocalVariable() 
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x = x + 1;
     	visit (l) {
     	   case int m: {
     	      x = x + 1;
     	      fail;
     	   }
     	}; 
      } 	
	};
    return x;
}

@should{return 12} // but why?
public int noNestedRecovery()
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

@should{return 0}
public int recoveryOfLocalVariableUsingIfThen()
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

@should{return 0}
public int recoveryOfGlobalVariable() 
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

data City = amsterdam();

rule a1 amsterdam() : { 
  gt = gt + 1;
  fail;
};

public int recoveryOfGlobalAfterFailingRule() {
	x = amsterdam();
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




