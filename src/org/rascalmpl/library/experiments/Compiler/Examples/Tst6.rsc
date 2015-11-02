module experiments::Compiler::Examples::Tst6


data Figure (real shrink = 1.0, str fillColor = "white", str lineColor = "black")  =  emptyFigure() 
  //| ellipse(Figure inner = emptyFigure()) 
  //| box(Figure inner = emptyFigure())
  //| volume(int width, int height, int depth, int area = width * height, int volume = area * depth)
  ;
  
value main() = emptyFigure(fillColor="red"); //.fillColor == "red";
//
//test bool keywordTest12() = emptyFigure(shrink=0.5,fillColor="red").fillColor == "red";
//
//test bool keywordTest13() = emptyFigure(shrink=0.5,fillColor="red", lineColor="black").fillColor == "red";
//
//test bool keywordTest14() = emptyFigure(lineColor="red", shrink=0.5).fillColor == "white";
//
//test bool keywordTest15() = ellipse().fillColor == "white";
// 
//test bool keywordTest16() = volume(2,3,4).area == 6 && volume(2,3,4).volume == 24;
//
//test bool keywordTest17() = volume(2,3,4,area=0).volume == 0;
//
//test bool keywordTest18() = volume(2,3,4,volume=0).area == 6;
//
//test bool keywordTest19() = ellipse(inner=emptyFigure(fillColor="red")).fillColor == "white";
//
//test bool keywordTest20() = ellipse(inner=emptyFigure(fillColor="red")).inner.fillColor == "red";