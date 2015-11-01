module experiments::Compiler::Examples::Tst6


data Figure (real shrink = 1.0, str fillColor = "white", str lineColor = "black")  =  emptyFigure() 
 // | ellipse(Figure inner = emptyFigure()) 
 // | box(Figure inner = emptyFigure())
//  | volume(int width, int height, int depth, int area = width * height, int volume = area * depth)
  ;

value main() = emptyFigure(shrink=0.5).fillColor == "white";

//data TYPESET = SET(str name);
//    
//value main() = {TYPESET t2} := {SET("c")};


//  testSet
            
//test bool testSet62() = {INTERSECT({TYPESET t1, *TYPESET rest}), TYPESET t2} :=  {INTERSECT({SET("a"), SET("b")}), SET("c")};


//data FK(int kw1 = 0) = h(int w = -1);
//
//value main() =  h();
  
