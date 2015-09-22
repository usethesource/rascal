module experiments::Compiler::Examples::Tst5
data Figure (real shrink = 1.0, str fillColor = "white", str lineColor = "black")  =  emptyFigure() 
  | ellipse(Figure inner = emptyFigure()) 
  | box(Figure inner = emptyFigure())
  | volume(int width, int height, int depth, int area = width * height, int volume = area * depth)
  ;
   
//test bool keywordTest16() = volume(2,3,4).area == 6 && volume(2,3,4).volume == 24;

test bool keywordTest17() = volume(2,3,4,area=0).volume == 0;

value main() = volume(2,3,4,area=0).volume == 0;

//test bool keywordTest18() = volume(2,3,4,volume=0).area == 6;

//data D = d(int x, int y = 3);
//  
//data POINT = point(int x, int y, str color = "red");
//  
//// keywordMatchTest1
//    
//test bool keywordMatchTest1() = point(_,_,color=_) := point(1,2);
//test bool keywordMatchTest2() = point(_,_,color="red") := point(1,2);
