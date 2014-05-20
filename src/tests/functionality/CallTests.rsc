 module tests::functionality::CallTests
 /*******************************************************************************
   * Copyright (c) 2009-2011 CWI
   * All rights reserved. This program and the accompanying materials
   * are made available under the terms of the Eclipse Public License v1.0
   * which accompanies this distribution, and is available at
   * http://www.eclipse.org/legal/epl-v10.html
   *
   * Contributors:
  
   *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
   *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
   *   * Paul Klint - Paul.Klint@cwi.nl - CWI
   *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
   *   * Bert Lisser - Bert.Lisser@cwi.nl - CWI
  *******************************************************************************/

import ParseTree;

import tests::functionality::CallTestsAux;

syntax XYZ = "x" | "y" | "z";

  // voidFun
  
  		public test bool assertTrue9() {void f(){ } f(); return true;}
  		
  		public test bool  assertTrue10() {
  		     int fac(int n){ return (n <= 0) ? 1 : (n * fac(n - 1));}
  		     return fac(0) == 1 && fac(1)==1 && fac(2)== 2 && 
  		       fac(3)==6 && fac(4)==24;
  		     }
  	
  
          int facQ(int n)  { 
             if(n == 0) { return 1;} 
             int z = facQ(n-1); return z*n; 
             }
  
  // facNotTailRec
  
  		public test bool  assertTrue11() {
  		      return facQ(0) == 1 && facQ(1)==1 && facQ(2)== 2 && 
  		       facQ(3)==6 && facQ(4)==24;	      
  		      }
  		      
  // formalsAreLocal
  
  		public test bool formalsAreLocal11(){
  		 int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
  		 return fac(0) == 1;
  	 	}
  	 	
  	 	public test bool formalsAreLocal12(){
  		 int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
  		 return fac(1) == 1;
  	 	}
  	 	
  	 	public test bool formalsAreLocal13(){
  		 int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
  		 return fac(2) == 2;
  	 	}
  	 	
  	 	public test bool formalsAreLocal14(){
  		 int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
  		 return fac(3) == 6;
  	 	}
  	 	
  	 	public test bool formalsAreLocal15(){
  		 int fac(int n) { if (n == 0) { return 1; } int z = n; int m = fac(n - 1); return z * m; }
  		 return fac(4) == 24;
  	 	}
  
  // higherOrder
  		
          public test bool  higherOrder1() {
            int add(int a, int b) { return a + b; };
            int doSomething(int (int a, int b) F) { return F(1,2); }; 
            int sub(int a, int b) { return a - b; }
            if (doSomething(add) != 3) return false;
            if (doSomething(sub) != -1) return false;
            return true;
  		}
  	
  // closures
  
  		public test bool  closures1() {
  		    int x = 1;
  		    int f(int (int i) g, int j) { return g(j);}
  		if (f(int (int i) { return i + 1; }, 0) != 1) return false;
  	    if (f(int (int i) { x = x * 2; return i + x; }, 1) != 3 || (x != 2))
  	       return false;
  	    return true;
  	    }
  	    
  // closuresVariables
  
         public test bool closureVariables1() = getX() == false;
   
         public test bool closureVariables2() {
  		    changeX(bool() { return true; });
  		    return getX();
  	     }
  	
  
   // varArgs0
   
   		public test bool varArgs01(){
   			int add(int i...) { return 0; }
   			return add() == 0;
   		}
   		public test bool varArgs02(){
   			int add(int i...) { return 0; }
   			return add([]) == 0;
   		}
   		public test bool varArgs02(){
   			int add(int i...) { return 0; }
   			return add(0) == 0;
   		}
   		public test bool varArgs03(){
   			int add(int i...) { return 0; }
   			return add([0]) == 0;
   		}
   		public test bool varArgs04(){
   			int add(int i...) { return 0; }
   			return add(0,1,2) == 0;
   		}
   		public test bool varArgs05(){
   			int add(int i...) { return 0; }
   			return add([0,1,2]) == 0;
   		}
 // varArgs1
   		
   		public test bool varArgs11(){
   			int add(int i...) { return i[0]; }
   			return add(0) == 0;
   		}
   		public test bool varArgs12(){
   			int add(int i...) { return i[0]; }
   			return add([0]) == 0;
   		}
   		public test bool varArgs11(){
   			int add(int i...) { return i[0]; }
   			return add(0,1,2) == 0;
   		}
   		public test bool varArgs11(){
   			int add(int i...) { return i[0]; }
   			return add([0,1,2]) == 0;
   		}
   		
   	// varArgs2
   	
   		public test bool varArgs21(){
   			int add(int i, int j...) { return i + j[0]; }
   			return add(1,2) == 3;
   		}
   		public test bool varArgs22(){
   			int add(int i, int j...) { return i + j[0]; }
   			return add(1,[2]) == 3;
   		}
   		public test bool varArgs23(){
   			int add(int i, int j...) { return i + j[0]; }
   			return add(1,2,3) == 3;
   		}
   		public test bool varArgs24(){
   			int add(int i, int j...) { return i + j[0]; }
   			return add(1,[2,3]) == 3;
   		}
   		
   // varArgs3
   		
   		public test bool varArgs31(){
            list[int] add(int i, int j...) = j;
            return add(1) == [];
        }
        
        public test bool varArgs32(){
            list[int] add(int i, int j...) = j;
            return add(1, 2) == [2];
        }
        
         public test bool varArgs33(){
            list[int] add(int i, int j...) = j;
            return add(1, 2, 3) == [2, 3];
        }
  
  // sideEffect
  	
  		public test bool  sideEffect1() {
  		 	 int called = 0;  								// changed order, since forward refs are no longer allowed
  		     void One() { called = called + 1; return; }
  		     One(); 
  		     One(); 
  		     One(); 
  		     return called == 3;
  		     }
  	
  // max1
  
  		public test bool  max1() { 
  		    int max(int a, int b) { return a > b ? a : b; } 
  		    return max(3,4) == 4;
  		    }
  		public test bool max2() { 
  		    int max(int a, int b) { return a > b ? a : b; } 
  		    real max(real a, real b) { return a > b ? a : b; }
  		    return max(3,4) == 4 && max(3.0,4.0) == 4.0;
  		    }
  		    
  		 /*changed: overloading is ambiguous */   
  		//public test bool max3() { 
  		//    int max(int a, int b) { return a > b ? a : b; } 
  		//    real max(real a, real b) { return a > b ? a : b; }
  		//    &T max(&T a, &T b) { return a > b ? a : b; }
  		//    return max(3,4) == 4 && max(3.0,4.0) == 4.0
  		//    && max("abc","def")=="def";
  		//    }

  	
  // ident
  		
  		 public test bool  ident1() {
  		             &T ident(&T x){ return x; }
  		            return ident(true) == true;
  		            }        
  		 public test bool  ident2() {
  		             &T ident(&T x){ return x; }
  		            return ident(4) == 4;
  		            }
  		 public test bool  ident3() {
  		             &T ident(&T x){ return x; }
  		            return ident(4.5) == 4.5;
  		            }
  		 public test bool  ident4() {
  		             &T ident(&T x){ return x; }
  		            return ident("abc") == "abc";
  		            }
  		  
  		  data DATA = f(int n);
  		            
  		  public test bool  ident5() {
  		             &T ident(&T x){ return x; }
  		            return ident(f(1)) == f(1);
  		           }
  		 public test bool  ident6() {
  		             &T ident(&T x){ return x; }
  		            return ident([1,2,3]) == [1,2,3];
  		            }
  		 public test bool  ident7() {
  		             &T ident(&T x){ return x; }
  		            return ident({1,2,3}) == {1,2,3};
  		            }
  		  public test bool  ident8() {
  		             &T ident(&T x){ return x; }
  		            return ident((1:10,2:20,3:30)) == (1:10,2:20,3:30);
  		            }
  
  
  // map
  
  	public test bool  map1() {
  		map[&K,&V] put(map[&K,&V] m, &K k, &V v) { m[k] = v; return m; }
  		return put((),1,"1") == (1:"1");
  	}
  
  data X = x() | y() | z();	
  
  // dispatchTest1
  
  public test bool  dispatchTest1() { 
  		public int f(x()) = 1;
  		public int f(y()) = 2;
  		public int f(z()) = 3;
  		return [f(x()),f(y()),f(z())] == [1,2,3];
  	}
  	
  public test bool  dispatchTest2() { 
  		public int f(x()) = 1;
  		public int f(y()) = 2;
  		public int f(z()) = 3;
  		public default int f(int x) = x;
  		return [f(x()),f(y()),f(z()), f(4)] == [1,2,3, 4];
  	}
  
  public test bool  dispatchTest3() { 
  		int f((XYZ) `x`) = 1;
  		int f((XYZ) `y`) = 2;
  		int f((XYZ) `z`) = 3;
  		
  		return [f((XYZ)`x`),f((XYZ)`y`),f((XYZ)`z`)] == [1,2,3];
  	}	
 
  //  keywordTest1
   
  	 public test bool  keywordTest11() { 
  		int incr(int x, int delta=1) = x + delta;
  		return incr(3) == 4 && incr(3, delta=2) == 5;
  	}
  
  	
  	public test bool  keywordTest1() { 
  		int sum(int x = 0, int y = 0) = x + y;
  		return sum() == 0 && sum(x=5, y=7) == 5+7 &&
  		sum(y=7,x=5)== 5+7;
  	}
  
  	public test bool keywordTest31(){
  		list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
  		return varargs(1,2,3,4) == [2,3,4];
  	}
  	public test bool keywordTest32(){
  		list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
  		return varargs(1,2,3,4,q="b") == [2,3,4];
  	}
  	public test bool keywordTest33(){
  		list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
  		return varargs(1,2,3,4,z=5) == [2,3,4];
  	}
  	public test bool keywordTest34(){
  		list[int] varargs(int x, int y ..., int z = 0, str q = "a") = y;
  		return varargs(1,2,3,4,q="b",z=5) == [2,3,4];
  	}
  
  data Figure (real shrink = 1.0, str fillColor = "white", str lineColor = "black")  =  emptyFigure() 
  | ellipse(Figure inner = emptyFigure()) 
  | box(Figure inner = emptyFigure());
 
  
  public test bool  keywordTest2() { 
  		if(!(emptyFigure().fillColor == "white")) return false;
  		if(!(emptyFigure(shrink=0.5).fillColor == "white")) return false;
  		if(!(emptyFigure(lineColor="red").fillColor == "white")) return false;
  		if(!(emptyFigure(lineColor="red", shrink=0.5).fillColor == "white")) return false;
  		
  		if(!(emptyFigure(fillColor="red").fillColor == "red")) return false;
  		if(!(emptyFigure(shrink=0.5,fillColor="red").fillColor == "red")) return false;
  		if(!(emptyFigure(shrink=0.5,fillColor="red", lineColor="black").fillColor == "red")) return false;
  		if(!(emptyFigure(lineColor="red", shrink=0.5).fillColor == "white")) return false;
  		
  		if(!(ellipse().fillColor == "white")) return false;
  		/* The following give NoSuchKey errors: TC info is missing or not found in the compiler */
  		/*TODO:TC*///if(!(ellipse(inner=emptyFigure(fillColor="red")).fillColor == "white")) return false;
  		/*TODO:TC*///if(!(ellipse(inner=emptyFigure(fillColor="red")).inner.fillColor == "red")) return false;
  		return true;
  	}
  
  data D = d(int x, int y = 3);
  
  data POINT = point(int x, int y, str color = "red");
  
  // keywordMatchTest1
  	
  		public test bool keywordMatchTest1() = point(_,_,color=_) := point(1,2);
  		public test bool keywordMatchTest2() = point(_,_,color="red") := point(1,2);
  		public test bool keywordMatchTest3() = point(_,_,color="green") !:= point(1,2, color="red");
  		public test bool keywordMatchTest4() = point(_,_,color="green") := point(1,2, color="green");
  		public test bool keywordMatchTest5() = point(1,2) := point(1,2);
  		public test bool keywordMatchTest6() = point(1,2) !:= point(1,3);
  		public test bool keywordMatchTest7() = point(1,2) := point(1,2,color="red");
  		public test bool keywordMatchTest8() = point(1,2,color="red") := point(1,2,color="red");
  		public test bool keywordMatchTest9() = point(1,2,color="green") !:= point(1,2);
  		public test bool keywordMatchTest10() =point(1,2,color="green") !:= point(1,2);
  
  data POINT1 = point1(int x, int y, int z = 3, list[str] colors = []);	
  
  // keywordMatchTest2
  
  		public test bool keywordMatchTest11() = point1(_, _, colors=["blue"]) := point1(1,2, colors=["blue"]);
  		
  	
  		public test bool keywordMatchTest12() =point1(_, _, colors=[*_,"blue",*_]) := point1(1,2, colors=["red","green","blue"]);
  		/*TODO:TC+COMP*///public test bool keywordMatchTest13() =point1(_, _, colors=[*_,*X,*_,*X, *_]) := point1(1,2, colors=["red","blue","green","blue"]);
 