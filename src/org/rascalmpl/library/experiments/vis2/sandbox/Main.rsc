module experiments::vis2::sandbox::Main
import Prelude;
import experiments::vis2::sandbox::IFigure;
import experiments::vis2::sandbox::Figure;


void  fun0(str event, str id) {println("fun0:<id>");setInt("aap", getInt("aap")+1);
                               setInt("noot", getInt("noot")*2);
                               }
void  fun1(str event, str id) {setInt("noot", getInt("noot")*2);}
void  fun2(str event, str id) {setInt("mies", getInt("mies")* getInt("mies"));}

void  funq(str event, str id) {setInt(id, getInt(id)+2);}

void funFill1(str event, str id) {
        if (getStr(id)=="yellow") 
            setStr(id, "blue"); else setStr(id,"yellow");
        }
        
void funFill2(str event, str id) {
        if (getStr(id)=="red") 
           setStr(id, "green"); else setStr(id,"red");
        }
       
/*
public void main() {
    clearWidget();
    println(centerMid);
    IFigure fig2 = _rect("asbak",  fillColor = "antiquewhite", width = 50, height = 50, align = centerMid);
    IFigure fig1 = _rect("bord", event = on("click", funFill2), fillColor = "red", width = 40, height = 40, align = topRight, fig = fig2);
    IFigure fig0 = _rect("tafel",  event = on("mouseout", funFill1), fillColor= "yellow"
                      ,width = 60, height = 60, align = bottomRight, fig = fig1
       
    );  
    IFigure f0 = _rect("a0",  fillColor = "antiquewhite", width = 20, height = 20, align = leftMid); 
    IFigure f1 = _rect("a1",  event = on("click", funFill2), fillColor = "red", fillColor = "red", width = 100, height = 100, align = topRight);
    IFigure f2 = _rect("a2",  fillColor = "blue", width = 50, height = 50, align = centerMid);                                                          
     _render(_hcat("aap", fig0) , fillColor= "white", width = 200, height = 200);
    // println([x|x<-widget]);
    // _render(_grid("noot", figArray= [[fig0],[f0, f1, f2]]), width = 1000, height = 1000);
    }
 */
 
 public void main() {
     Figure WB = box(id="wb", fillColor="white", width = 50, height = 50);
     Figure RB = box(id = "rb", fillColor="red", size=<20,20>, align=topLeft );
     Figure box4 =  box(id="box4", fig = WB, fillColor="blue", width = 90, height = 90);
     Figure box5 = box(id="box5", fig=RB, fillColor="blue",  width = 50, height = 50, align=topLeft);
     // render(box4, width = 200, height = 200, fillColor = "white");
     render(box5, width = 500, height = 500);
     render(hcat(figs =[box4, box5], id="hcat"), width = 500, height = 500);
     }
 