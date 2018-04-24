module lang::rascalcore::check::Test2 

data Figure (str fillColor = "white")  =  emptyFigure()
  ;
  
void keywordTest7() {
    f = emptyFigure();
    f.fillColor = "red";
}