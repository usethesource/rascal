module experiments::vis2::sandbox::Main
import Prelude;
import experiments::vis2::sandbox::IFigure;


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

public void main() {
    clearWidget();
    Figure fig2 = rect("asbak",  fillColor = "antiquewhite", width = 50, height = 50, align = centerMid);
    Figure fig1 = rect("bord", event = on("click", funFill2), fillColor = "red", width = 40, height = 40, align = topRight, fig = fig2);
    Figure fig0 = rect("tafel",  event = on("mouseout", funFill1), fillColor= "yellow"
                      ,width = 60, height = 60, align = bottomRight, fig = fig1
       
    );  
    Figure f0 = rect("a0",  fillColor = "antiquewhite", width = 20, height = 20, align = leftMid); 
    Figure f1 = rect("a1",  event = on("click", funFill2), fillColor = "red", fillColor = "red", width = 100, height = 100, align = topRight);
    Figure f2 = rect("a2",  fillColor = "blue", width = 50, height = 50, align = centerMid);                                                          
    render(fig0 , fillColor= "white", width = 200, height = 200);
    // render(hcat("aap", f0, f1, f2, cellWidth=200, cellHeight=200), width = 1000, height = 1000);
    }
    
 