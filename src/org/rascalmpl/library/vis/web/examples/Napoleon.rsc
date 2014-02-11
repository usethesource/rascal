@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module vis::web::examples::Napoleon
import Prelude;
import vis::web::markup::D3;
import util::Math;
import util::HtmlDisplay;

num e(list[num] matrix, int i, int j) = matrix[((2*i)/2)*2 + j%2];

data Pos = ab()|ac()|bc();
   

list[num] m(list[num] matrix, list[num] v) =  [e(matrix, 0, 0)*v[0]
+e(matrix, 0, 1)*v[1],  e(matrix, 1, 0)*v[0]
+e(matrix, 1, 1)*v[1]];

list[num] m(list[num] matrix, list[num] v, num x, num y, num d) {
    list[num] r = m(matrix, v);
    return [d*r[0]+x, d*r[1]+y];
}
 
list[num] center(num xa, num ya, num xb, num yb, list[num] xC)=
  [(xa+xb+xC[0])/3, (ya+yb+xC[1])/3];


str triangle(num xa, num ya, num xb, num yb,
num xc, num yc, str color) {
 
 list[num] nA = [];
 
     
 list[num] base(num xa, num ya, num xb, num yb, num phi, num ac) {
   num x = xb-xa;
   num y = yb-ya;
   num d = sqrt(x*x+y*y);
   num cosx = round(x/d, 0.00001);
   num siny = round(y/d, 0.00001);
   list[num] R = [cosx, -siny, siny, cosx];
   list[num] T = [cos(phi), ac*sin(phi)];
   list[num] xC = m(R, T, xa, ya, d);
   return xC;
   }
 
str drawTriangle(num xa, num ya, num xb, num yb,list[num] xC, Pos pos) {
   str r=Z(path_, (id_:"a", d_:"M <xa> <ya> L <xC[0]> <xC[1]> L <xb> <yb> Z"
       ));
   return r;
   } 
   
 str equi(num xa, num ya, num xb, num yb,  Pos pos, bool mirror) {
   list[num] xC = base(xa, ya, xb, yb, PI()/3, mirror?(-1):1);
    str r= drawTriangle(xa, ya, xb, yb, xC, pos);
    list[num] c = center(xa, ya, xb, yb, xC);
    nA+= c;   
    return r;
    }
    
 int nextX(int c, int v) = (c+v)%3;
 
 int nextY(int c, int v) = (c+2*v)%3;
 
 str fColor(int c) {
     switch(c) {
         case 0:return "red";
         case 1:return "blue";
         case 2:return "green";
         }
     return "";
     }
   
   list[num] standard(num xa, num ya, num xb, num yb, bool mirror) {
      return base(xa, ya, xb, yb, PI()/5, mirror?(-0.5):0.5);
   }

    list[num] xC = standard(xa, ya, xb, yb, false);
      num xc = xC[0];
      num yc = xC[1];
      list[str] equiTriangle = [equi(xa, ya, xb, yb,  ab(), true),
                equi(xb, yb, xc, yc,  bc(), true),
                equi(xa, ya, xc, yc,  ac(), false)];
      
      str coord = "<xa> <ya>, <xb> <yb>, <xc> <yc>";
      str r =   Z(polygon_, 
           (fill_:"<color>",
            stroke_:"black", stroke_width_:"1px" ,
            points_:"<coord>"
           ))+ equiTriangle[0] 
              +equiTriangle[1]
              +equiTriangle[2]
          ;
       list[num] x = [nA[1*2+0]-nA[0], nA[1*2+1]-nA[1]];
       list[num] y = [nA[2*2+0]-nA[0], nA[2*2+1]-nA[1]];
       for (k<-[0..25]) {
          list[num] p = [nA[0]+k*x[0], nA[1]+k*x[1]];
          int color1 = nextX(0, k);
          for (l<-[0..25]) {
              list[num] q = [p[0]+l*y[0], p[1]+l*y[1]];
              int color2 = nextY(color1, l);    
              r+=Z(g_,(transform_:"translate(<round(q[0]-nA[2*color2],0.0001)>, <round(q[1]-nA[2*color2+1],0.0001)>)"), equiTriangle[color2]);   
              r+= Z(circle_, (r_:"2", cx_:"<q[0]>",
                      cy_:"<q[1]>",fill_:fColor(color2))); 
              }
       }
       return  Z(rect_,(height_:"100%", width_:"100%", fill_:"pink"))+Z(g_,(transform_:"translate(50, -400)"), r);
    }
    
public void main() {
    int x = 50, y = 50;
    r = Z(svg_, (width_:"400", height_:"400",id_:"panel"),  triangle(50+x, 50+y, 135+x, 75+y, 125+x, 150+y, "pink"));   
     htmlDisplay(|file:///tmp/napoleon|, 
     // htmlDisplay(|project://chart/src/napoleon|,  
      html(
         toCss((
            "#panel":(border_:"5px groove"), 
            "path": (fill_:"floralwhite", stroke_:"black"), 
             "#b":(stroke_:"darkblue",stroke_width_:"1.0", fill_:"none")
             ))
        ,r)
    );
}