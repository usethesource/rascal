module experiments::vis2::sandbox::Shapes
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;



public Figure diamond(int width, int height, str txt, str color) = 
overlay(width=width, height = height, figs=[
   // box(fig=
   polygon(lineWidth = 1, width = width, height = height, points=[<0, 0.5>, <0.5, 0>, <1, 0.5>, <0.5, 1>], scaleX=<<0, 1>, <0, width>>, scaleY=<<0, 1>, <0, height>>, fillColor=color)
   // )
     ,box(width = width, height = height, lineWidth = 0, fig = text(txt), fillColor="none")
   ])
   ;

public void tdiamond() = render(diamond(200, 100, "aap", "lightyellow"), lineWidth = 0);

public void fdiamond(loc l) = writeFile(l, toHtmlString(diamond(400, 200, "aap", "lightyellow")));


public Figure hArrow(int width, height, str color)  {
    num d = 0.05;
    num x = 0.2;
    list[tuple[num, num]] p  = [<0, 0.5-d>, <0,0.5+d>, <1-x, 0.5+d>, <1-x, 1>, <1, 0.5>, <1-x, 0>, <1-x, 0.5-d>];
    return 
    // box(lineWidth = 0, fig=
    polygon(lineWidth = 0, width = width, height = height, points=p, scaleX=<<0, 1>, <0, width>>, scaleY=<<0, 1>, <0, height>>, fillColor=color)
    // )
    ;
    }
    
public Figure vArrow(int width, height, str color)  {
    num d = 0.05;
    num x = 0.2;
    list[tuple[num, num]] p  = [<0.5-d, 0>, <0.5+d, 0>, <0.5+d, 1-x>, <1, 1-x>, <0.5, 1>, <0, 1-x>, <0.5-d, 1-x>];
    return // box(lineWidth = 0, fig=
    polygon(lineWidth = 0, width = width, height = height, points=p, scaleX=<<0, 1>, <0, width>>, scaleY=<<0, 1>, <0, height>>, fillColor=color)
    // )
    ;
    }

public void thArrow() = render(hArrow(100, 25, "lime"), lineWidth = 0);

public void tvArrow() = render(vArrow(25, 100, "brown"), lineWidth = 0);

public Figure action(str lab) = box(grow=1.2, fig = text(lab), fillColor = "lightgreen", rounded=<10, 10>, lineWidth = 0);

public Figure begin(int width, int height, str lab) = box(size=<width, height>, fig = htmlText(lab), fillColor = "lightpink", rounded=<10, 10>, lineWidth = 0);

public Figure end(int width, int height, str lab) = box(size=<width, height>, fig = text(lab), fillColor = "lightgreen", rounded=<10, 10>, lineWidth = 0);

public Figure decision() {
   int width = 150;
   int height = 100;
   int h = 20;
   int w = 50;
   int h1 =50;
   int w1 = 20; 
   int h0 = 40;
   int offs = h0+h1;
   return overlay(figs=[
                        begin(width, h0, "Lamp doesn\'t work")
                      , at((width-w1)/2, h0, vArrow(w1, h1, "brown"))
                      , at(0, offs, diamond(width, height, "Lamp\<br\> plugged in?", "lightyellow"))
                      , at(width, offs+(height-h)/2, hArrow(w, h, "brown"))
                      , at((width-w1)/2,offs+height, vArrow(w1, h1, "brown"))
                      , at(0, offs+height+h1, diamond(width, height, "Bulb\<br\>burned out?", "lightyellow"))
                      , at(width, offs+height+ h1 + (height-h)/2, hArrow(w, h, "brown"))
                      , at((width-w1)/2, offs+2*height+ h1, vArrow(w1, h1, "brown"))
                      , at(width+w, offs+(height-h)/2, action("Plugin lamp"))
                      , at(width+w, offs+height+h1+(height-h)/2, action("Replace bulb"))
                      , at(0,  offs+2*height+ 2*h1, end(width, h0, "Repair Lamp"))
                      ]);
   }

public void tdecision() = render(decision());


