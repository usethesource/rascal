module experiments::Compiler::Examples::Tst1

import shapes::Figure;
import shapes::FigureServer;

value main(){
   b = box(fillColor="red");
   renderSave(b);
   b = box(fillColor="red");
   renderSave(b, |home:///b.png|, width=100, height=100);
}