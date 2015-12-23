module experiments::vis2::sandbox::Poisson
import Prelude;
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import util::Math;

num sinI(int d) =  sin((toReal(d)/180)*PI());

num cosI(int d) =  cos((toReal(d)/180)*PI());


public list[Vertex] rline(int teta, int p, int n) {
    Vertex  p1 = move(-n*100*sinI(teta), n*100*cosI(teta));
    Vertex  p2 = line(n*100*sinI(teta), -n*100*cosI(teta));
    if (p!=0) {
         num x = teta==0?p: p/sinI(teta);
         num y = teta==90?p: p/cosI(teta);
         p1 = move(-n*x, (1+n)*y);  
         p2 = line((1+n)*x, -n*y); 
         }
    return [p1, p2];
    }
    
bool flip() = arbInt(2)==0?true:false;

int flipInt() = flip()?1:-1;
    
int p  = 100;
    
public Figure rl() = overlay(figs=[circle(r=p, cx = 200, cy = 200, lineColor="red")
        , shape( rline(arbInt(180), flipInt()*arbInt(p+1), 500)
        , scaleX=<<-200, 200>, <0, 400>>
        , scaleY=<<-200, 200>, <0, 400>>
        , size=<400, 400>
        )|int i<-[0, 1 .. 100]]);

public void prl() {Figure f = rl(); render(f, borderWidth = 10, borderStyle = "ridge", borderColor = "grey", size=<400, 400>);}