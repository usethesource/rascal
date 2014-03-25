module experiments::Compiler::Examples::KWP6

data Figure (str fillColor = "white")  =  emptyFigure();

 public value main(list[value] args) = emptyFigure().fillColor == "white";