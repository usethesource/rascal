module experiments::Compiler::Examples::Tst1

 data F = f() | f(int n) | g(int n) | deep(F f);
 anno int F @ pos;
  	
  	
  @expected{UninitializedVariable}
  	// public void annotationError2(){
  	public test bool annotationError2() {	
  	  F X;
  	  X @ pos += 1;
  	  return false;
  	  }
  	  
  @expected{UninitializedVariable}
  	public test bool uninitializedVariable1() {	
  	  F X;
  	  X @ pos -= 1;
  	  return false;
  	  }
  	  
  @expected{UninitializedVariable}
  	public test bool uninitializedVariable2() {	
  	  F X;
  	  X @ pos *= 1;
  	  return false;
  	  }	
  	  
  @expected{UninitializedVariable}
      public test bool uninitializedVariable3() {	
  	  F X;
  	  X @ pos /= 1;
  	  return false;
  	  }			
  //	}
