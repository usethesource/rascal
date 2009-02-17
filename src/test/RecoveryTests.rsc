module RecoveryTests

public int recoveryOfLocalVariable()
@should{return 0}
{
	x = 0;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	x += 1; 
     	fail;
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
     	x += 1; 
     	if (n > 10) {
     	   x += 1; // another update
     	}
     } 	
	};
    return x;
}

public int gx = 0;

public int recoveryOfGlobalVariable() 
@should{return 0}
{
    global int gx;
	l = [1, 2, 3];
	visit (l) {
     case int n: {
     	gx += 1; 
     	fail;
     } 	
	};
    return gx;
}