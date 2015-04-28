module experiments::vis2::sandbox::Examples
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

void ex(str title, Figure b, bool debug = false) = render(b, debug = debug, align = centerMid);

void ex(Figure b) = render(vcat(figs =[text("\\\"aap\\\""), b]));

public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ render(box1, align = centerMid); }  

public Figure box2 = box(fillColor="red", size=<200,200>, lineWidth=10);
void tbox2(){ render(box2); } 

public Figure box3 = box(fillColor="red", lineColor="lightgrey", lineWidth=30,  size=<200,200>);
void tbox3(){ render(box3, align = centerMid); }

Figure WB = box(fillColor="antiquewhite", size=<50,100>);
Figure RB = box(fillColor="red", size=<20,20>);

void tRB() {render(RB, align = bottomRight, debug = true);}

public Figure box4 =  box(fig=RB, align = bottomRight, fillColor="none", size=<200,200>, lineColor = "grey", lineWidth = 16);
void tbox4(){ render(box4, debug=false, align = topLeft);} 

public Figure box5 = box(fig=at(0, 0, WB), fillColor="blue", align = topLeft, size=<200,200>);
void tbox5(){ render(box5, debug = false); } 

public Figure box6 = box( fig=WB, fillColor="blue", size=<200,200>, gap=<10, 10>, align=topRight);
void tbox6(){ render(box6); } 

public Figure box7 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomRight);
void tbox7(){ render(box7); } 

public Figure box8 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomLeft);
void tbox8(){ render(box8); } 

public Figure box9 = box(fig=WB, lineWidth = 4, fillColor="lightblue", padding=<10,10, 20, 20>, align=topLeft);
    void tbox9(){ render(box9, debug = false); } 

public Figure box10 = box(fig=WB, fillColor="lightblue",  size=<200, 200>, gap=<10,10>, align=topRight);
void tbox10(){ render(box10); } 

public Figure box11 = box(fig=WB, fillColor="lightblue",  gap=<10,10>, align=topLeft);
    void tbox11(){ render(box11); } 

public Figure box12 = box(fig=WB, fillColor="lightblue", size=<200,200>, padding=<10,10,10, 10>, align=bottomRight);
void tbox12(){ render(box12); } 

public Figure box13 = box(fig=box(fig=RB, fillColor="white", size=<50,100>), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox13(){ render(box13); } 

public Figure box14 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox14(){render(box14); }

public Figure box15 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topRight), fillColor="white", size=<200,200>, gap=<10,20>, align=bottomLeft);
void tbox15(){ render(box15); }

public Figure box16 = box(fig=box(fig=RB, fillColor="white",  size=<50, 50>, align=bottomRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox16(){ render(box16); }

public Figure box17 = box(fig=box(fig=RB,fillColor="white",  size=<50, 50>, lineWidth = 1, align=bottomLeft), fillColor="blue", 
size=<200,200>, gap=<0,0>, align=bottomLeft, lineWidth = 0, lineColor="red");
void tbox17(){ render(box17, align = topLeft, debug = false); }

// public Figure box18 = box(fig=RB, fillColor="blue", grow=3);
//void tbox18(){ ex("box18", box18); }
//
//public Figure box19 = box(fig=text("Hello", fontSize=20), grow=2);
//void tbox19(){ ex("box19", box19); }

public Figure box20 = box(align = topLeft, lineWidth = 5, fig=box( lineWidth=20, lineColor="blue", fillColor = "white", size=<200,200>, lineOpacity = 0.5), fillColor = "white", lineColor="red");
void tbox20(){ render(box20, align = bottomRight); }

public Figure box21 = box(align = topLeft, fig=at(0, 0, box(align = topLeft, lineWidth=60, 
    lineColor="silver", fillColor = "green", lineOpacity=1.0, size=<200,200>)), 
         lineColor="red", fillColor = "none", lineWidth=2);
void tbox21(){ render(box21, align = topLeft, debug = false); }

public Figure newBox(str lc, Figure el) {
      return at(10, 10, box(align = topLeft, lineColor= lc, lineWidth = 20, fillColor = "white", fig = el));
      }
public Figure box22 = (at(0,0, box(size=<50, 200> , lineWidth=60, align = bottomRight, 
lineColor="silver", fillColor = "yellow", lineOpacity=0.5))|newBox(e, it)| e<-["green", "red", "blue", "grey", "magenta"]);
void tbox22(){ render(box22, align = bottomRight, debug = false); }

void boxes(){
	render( grid(gap=<0,0>, align = centerMid, 
					figArray=[ 
					           [box4, box5, WB],
						       [box5, box4, box6, box7],
						       [RB],
						        [box21]
						  ]), width = 1000, height = 1000, debug = false);
}

void tvcat() {render(box(fig=vcat(figs=[RB, box21])));}

void thcat() {render(hcat(figs=[WB, RB]));}

void tgrid() {render(grid(figArray=[[WB, RB], [RB, WB], [WB, RB, WB]]));}


Figures rgbFigs = [box(fillColor="red",size=<50,100>), box(fillColor="green", size=<200,200>), box(fillColor="blue", size=<10,10>)];

public Figure hcat1 = hcat(figs=rgbFigs, align=topLeft);    
void thcat1(){ ex("hcat1", hcat1); }

public Figure hcat2 = hcat(figs=rgbFigs, align=centerMid);  
void thcat2(){ ex("hcat2", hcat2); }

public Figure hcat3 = hcat(figs=rgbFigs, align=bottomMid);  
void thcat3(){ ex("hcat3", hcat3); }

public Figure hcat4 = hcat(figs=rgbFigs, align=bottomMid, gap=<10,10>);
void thcat4(){ ex("hcat4", hcat4); }

// hcat in box

Figure tlFigs = hcat(align=topLeft,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
				             
Figure ctFigs = hcat(align=centerMid, gap=<10,10>, fillColor="lightgrey",
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
public Figure box_hcat1 = box(fig=tlFigs,  fillColor="lightgrey",align = centerMid);
void tbox_hcat1(){ render(box_hcat1); }

public Figure box_hcat2 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=topLeft);
void tbox_hcat2(){ ex("box_hcat2", box_hcat2); }

public Figure box_hcat3 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=topRight);
void tbox_hcat3(){ ex("box_hcat3", box_hcat3); }

public Figure box_hcat4 = box(fig=tlFigs,fillColor="lightgrey", size=<400,400>, align=bottomRight);
void tbox_hcat4(){ ex("box_hcat4", box_hcat4); }

public Figure box_hcat5 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=bottomLeft);
void tbox_hcat5(){ ex("box_hcat5", box_hcat5); }

public Figure box_hcat6 = box(fig=ctFigs, fillColor="antiquewhite", size=<400,400>, align=topLeft);
void tbox_hcat6(){ ex(box_hcat6); }

public Figure box_hcat7 = box(fig=ctFigs, fillColor="antiquewhite", size=<400,400>, align=topRight);
void tbox_hcat7(){ ex("box_hcat7", box_hcat7); }

public Figure box_hcat8 = box(fig=ctFigs, fillColor="antiquewhite", size=<400,400>, align=bottomRight);
void tbox_hcat8(){ ex("box_hcat8", box_hcat8); }

public Figure box_hcat9 = box(fig=ctFigs, fillColor="antiquewhite", size=<400,400>, align=bottomLeft);
void tbox_hcat9(){ ex("box_hcat9", box_hcat9); }

public Figure hello = vcat(figs=[text("Hello World", fontWeight="bold", fontColor = "red")]);
void thello(){ ex("hello", hello); }

public Figure boxt_hcat6 = 
vcat(figs=[text("box(fig=ctFigs, fillColor=, size=\<400,400\>)"),
       box(fig=ctFigs, fillColor="antiquewhite", size=<400,400>, align=topLeft)
       ], align = topRight);

void tboxt_hcat6(){ ex("boxt_hcat6", boxt_hcat6); }

/********************** grid ******************************/

Figure RedBox = box(fillColor="red", size=<50,50>);
Figure BlueBox = box(fillColor="blue", size=<100,30>);
Figure GreenBox = box(fillColor="green", size=<40,60>);

void overlay1(){
	ex("overlay1", overlay(width= 200, height = 200, figs=[ at(100, 130, RedBox),
							    BlueBox, GreenBox]), debug = false);
}


void grid1(){
	ex("grid1", grid(figArray=[ [RedBox],
							    [BlueBox]
							  ], gap=<10,10>));
}

void grid2(){
	ex("grid2", grid(figArray=[ [RedBox, GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}

void grid3(){
	ex("grid3",  grid(align = topLeft,figArray=[ [box(fillColor="red", size=<50,50>, align=topLeft), GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}
void grid4(){
	ex("grid4",  grid(align = bottomRight,figArray=[ [box(fillColor="red", size=<50,50>, align=bottomRight), GreenBox],
							    [BlueBox, RedBox, RedBox]
							  ], gap=<10,10>));
}


Figure ell0 = ellipse(cx=85, cy = 45, rx = 80, ry = 40,  lineColor="red", lineWidth = 5, fillColor="none");
Figure ell1 = ellipse(cx=50, cy = 90, rx = 40, ry = 80, align= centerMid,  fig=vcat(figs=[
   circle(cx=13, cy= 13, r = 10, fillColor= "none", lineColor = "blue", lineWidth = 3)
   ,text("Hallo")
   ]), lineColor="black",lineWidth=10, fillColor="none" );
void tell0(){ ex("hello", hcat(figs=[ell0, ell1], gap = <10, 10>), debug = false); }

// ellipse

public Figure ellipse1 = ellipse(rx=100, ry=75, fillColor="lightgrey");
void tellipse1(){ ex("ellipse1", ellipse1, debug = true); }

public Figure ellipse2 = ellipse(rx=100, ry=75, fillColor="red");
void tellipse2(){ ex("ellipse2", ellipse2); }

public Figure ellipse3 = ellipse(lineWidth=20, lineColor= "lightgrey",  rx=100, ry=75, 
fillColor="red", align = bottomRight, fig=box(size=<50,80>, fillColor="yellow", fig=circle(r=10)));
void tellipse3(){ ex("ellipse3", ellipse3, debug = true); }

public Figure ellipse4 = box(lineColor="red", fillColor ="antiquewhite", 
      fig=ellipse(rx=100, ry=75, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tellipse4(){ ex("ellipse4", ellipse4); }

public Figure ellipse5 = box(padding=<0,0,0,0>, fillColor= "lighblue", lineColor="red", lineWidth = 5, 
     fig=at(10, 10, ellipse(rx=50, ry=38, lineWidth=10, lineColor="silver", lineOpacity=0.5)));
void tellipse5(){ ex("ellipse5", ellipse5, debug = false); }

public Figure ellipse6 = at(0, 0, ellipse(padding = <20, 20, 20,20>,
    lineColor="red", align = centerMid, lineWidth=10, fillColor = "white", fig=at(0,0,ellipse(align = topLeft, rx=100, ry=75, 
    lineWidth=20, padding = <10, 10, 10, 10>,
    lineColor="silver", fillColor = "yellow", lineOpacity=0.5))));


void tellipse6(){ render(ellipse6, debug = false, align = topLeft); }

public Figure newEllipse(str lc, Figure el) {
      return at(0, 0, ellipse(align = topLeft, lineColor= lc, lineWidth = 20, fillColor = "white", padding=<10,10,10,10>, 
      fig = el));
      }
public Figure ellipse8 = (idEllipse(100, 75) |newEllipse(e, it)| e<-["red","blue" ,"grey","magenta"]);
void tellipse8()  {render(ellipse8, debug = false, align = topLeft);}

void ellipses() {
   ex("ellipses", vcat(align = bottomRight, figs=[ellipse1, ellipse2, ellipse3, ellipse4, ellipse5, ellipse6]), debug = false);
}

public Figure ellipse7 = ellipse(lineColor="red", lineWidth = 6, fillColor ="antiquewhite", fig=ellipse(rx=100, ry=75, lineWidth=10, lineColor="silver", fillColor= "green"));
void tellipse7(){ ex("ellipse7", ellipse7, debug=false); }



// circle

public Figure circle1 = circle(r=100, fillColor = "red");
void tcircle1(){ ex("circle1", circle1); }

public Figure circle2 = circle(r=100, fillColor="red", fig= circle(r=50, fillColor="blue"));
void tcircle2(){ ex("circle2", circle2); }

public Figure circle3 = circle(fillColor="red", r=  100, align =  bottomRight, fig=box(size=<50,80>, fillColor="yellow"));
void tcircle3(){ ex("circle3", circle3); }

public Figure circle4 = box(lineColor="red", fig=circle(fillColor="pink", r=100, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tcircle4(){ ex("circle4", circle4); }

public Figure circle5 = box(lineColor="red", fig=circle(fillColor="lightGrey", r=100, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tcircle5(){ ex("circle5", circle5); }


public Figure circle6 = box(lineColor="red", lineWidth=15, fig=circle(fillColor="magenta", r=100, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tcircle6(){ ex("circle6", circle6); }

void circles() {
   ex("circles", vcat(align=bottomRight, figs=[circle1, circle2, circle3, circle4, circle5, circle6, box(size=<300, 300>, fillColor="antiqueWhite")]), debug = false);
}
/************** text *****************/

public Figure text0 = text("Hello");
void ttext0(){ ex("text0", text0); }

public Figure text1 = text("Hello", fontSize=14, lineWidth=1);
void ttext1(){ ex("text1", text1); }

public Figure text2 = text("Hello", fontSize=20);
void ttext2(){ ex("text2", text2); }

public Figure text3 = text("Hello", fontStyle="italic", fontSize=20);
void ttext3(){	ex("text3", text3); }

public Figure text4 = text("Hello", fontWeight="bold", fontSize=20);
void ttext4(){ ex("text4", text4); }

public Figure text5 = text("Hello", fontWeight="bold", fontStyle="italic", fontSize=20);
void ttext5(){ ex("text5", text5); }


public Figure text6 = text("Hello", fontSize=14, textDecoration="underline");
void ttext6(){ ex("text6", text6); }

public Figure text7 = text("Hello", fontSize=14, textDecoration="line-through");
void ttext7(){ ex("text7", text7); }

public Figure text8 = text("Hello", fontSize=14, textDecoration="overline");
void ttext8(){ ex("text8", text8); }


public Figure text9 = text("Hello", fontSize=14, fontColor="red");
void ttext9(){ ex("text9", text9); }

public Figure text10 = box(fig=text("Hello", fontColor="green", fontWeight="bold", fontStyle="italic",  fontSize=20), fillColor="yellow");
void ttext10(){ ex("text10", text10); }

public Figure text11 = hcat(figs=[ box(fig=text("Hello", fontColor="red"), fillColor="white"),
					  text("World")
					], fontSize=20);
void ttext11(){ ex("text11", text11); }

void texts(){
	ex("texts", grid(gap=<20,20>,align=bottomLeft,
	                 figArray=[
							[text0, text1, text2, text3, text4, text5],
							[text6, text7, text8, text9],
							[text10,text11]
							
						  ]), debug = false);
}	

public Figure pol1 = polygon(fillColor="none", lineWidth = 1, lineColor="black", points = [<15, 10>, <55, 10>, <45, 20>, <5, 20>]);
void tpol1(){ ex("pol1", pol1); }

Points ps = [
     <35.0, 37.5>, <37.9, 46.1>, <46.9, 46.1>, <39.7, 51.5>
     , <42.3, 60.1>, <35.0, 55.0>, <27.7, 60.1>, <30.3, 51.5>
    ,<23.1,46.1>,<32.1,46.1>
    ];

public Figure pol2 = polygon(fillColor="none", lineWidth = 1, lineColor="black", points = 
    [<5*(p.x-20), 5*(p.y-35)>|p<-ps]
    );
void tpol2(){ ex("pol1", pol2); }

/********************** polygon ******************************/

public Figure polygon1 = polygon(points=[<100,100>, <100,200>, <200,200>], lineWidth = 0, fillColor="green");
void tpolygon1(){ ex("polygon1", polygon1); }

public Figure polygon2 =  polygon(points=[<100,100>, <100,200>, <200,200>], fillColor="red", lineWidth=4, lineDashing=[1,1,1,1,1,1]);
void tpolygon2(){ ex("polygon2", polygon2); }

public Figure polygon3 =  polygon(points=[<100,10>, <40,198>, <190,78>, <10,78>, <160,198>], fillColor="green", lineWidth=4);
void tpolygon3(){ ex("polygon3", polygon3); }

public Figure polygon4 = polygon(points=[<200,10>,<250,190>, <160,210>], fillColor="pink", lineWidth=1);
void tpolygon4(){ ex("polygon4", polygon4); }

public Figure polygon5 = polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=4);
void tpolygon5(){ ex("polygon5", polygon5); }

public Figure polygon6 = box(fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=1));
void tpolygon6(){ ex("polygon6", polygon6); }

public Figure polygon7 =  box(fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=4));
void tpolygon7(){ ex("polygon7", polygon7); }

public Figure polygon8 = box(fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=10));
void tpolygon8(){ ex("polygon8", polygon8); }

public Figure polygon9 = box(lineColor="red", lineWidth=10, fig=polygon(points=[<350,75>, <379,161>, <469,161>, <397,215>, <423,301>, <350,250>, <277,301>, <303,215>, <231,161>, <321,161>], fillColor="yellow", lineWidth=10));
void tpolygon9(){ ex("polygon9", polygon9); }

void polygons() {
  ex("polygons",  grid(gap=<10,10>,
  					   figArray=[
							[polygon1, polygon2, polygon3, polygon4, polygon5],
                            [polygon6, polygon7, polygon8, polygon9]
                            ], align=topLeft));
}

public Figure ngon1 = ngon(n = 3, r=100);
void tngon1(){ ex("ngon1", ngon1); }

public Figure ngon2 = ngon(n = 4, r=100);
void tngon2(){ ex("ngon2", ngon2); }

public Figure ngon3 = ngon(n = 5, r=100);
void tngon3(){ ex("ngon3", ngon3); }

public Figure ngon4 = ngon(n = 6, r=100);
void tngon4(){ ex("ngon4", ngon4); }

public Figure ngon5 = ngon(n = 7, r=100);
void tngon5(){ ex("ngon5", ngon5); }

public Figure ngon6 = ngon(n = 8, r=100);
void tngon6(){ ex("ngon6", ngon6); }

public Figure ngon7 = box(fig= ngon(n = 3, fillColor="red", r = 100));
void tngon7(){	ex("ngon7", ngon7); }

public Figure ngon8 = box(fig= ngon(n = 4, fillColor="red", r = 100));
void tngon8(){	ex("ngon8", ngon8); }

public Figure ngon9 = box(fig= ngon(n = 5, fillColor="red", r = 100));
void tngon9(){	ex("ngon9", ngon9); }

public Figure ngon10 = box(fig= ngon(n = 6, fillColor="red", r = 100));
void tngon10(){	ex("ngon10", ngon10); }

public Figure ngon11 = box(fig= ngon(n = 7, fillColor="red", r = 100));
void tngon11(){	ex("ngon11", ngon11); }

public Figure ngon12 = box(fig= ngon(n = 8, fillColor="red", r = 100));
void tngon12(){	ex("ngon12", ngon12); }

public Figure ngon13 = ngon(r= 100, n = 5, fillColor="red", fig=box(fig = text("Hallo", fontSize=20, fontColor = "green"), fillColor="antiquewhite"));
void tngon13(){ ex("ngon13", ngon13); }

public Figure ngon14 = ngon(r = 100, n = 4, fillColor="red", fig=box(size=<125,90>, fillColor="yellow"));
void tngon14(){ ex("ngon14", ngon14); }

public Figure ngon15 = ngon(r= 100, n = 5, fillColor="red", fig=box(size=<125,90>, fillColor="yellow"));
void tngon15(){ ex("ngon15", ngon15); }

public Figure ngon16 = ngon(r = 100, n = 6, fillColor="blue", align = bottomRight, fig=box(size=<125,90>, fillColor="yellow"));
void tngon16(){ ex("ngon16", ngon16); }

public Figure ngon17 =  ngon(r=100, n = 7, fillColor="red", fig=box(size=<125,90>, fillColor="yellow"));
void tngon17(){ ex("ngon17", ngon17); }

public Figure ngon18 = ngon(r=100, n = 10, fillColor="red", fig=box(size=<125,90>, fillColor="yellow"));
void tngon18(){ ex("ngon18", ngon18); }

public Figure ngon19 = ngon(n = 5, padding =<10,10, 10, 10>, lineColor="red", lineWidth = 30, fillColor ="antiquewhite",
    fig=ngon(
       n = 5, r=100,  lineWidth=20, lineColor="silver", fillColor= "green"));
void tngon19(){ ex("ngon19", ngon19, debug=true); }

public Figure ngon20 = box(lineColor="red", lineWidth = 6, fillColor ="antiquewhite", fig=circle(r=100, lineWidth=2, lineColor="black", fillColor= "green"));
void tngon20(){ ex("ngon20", ngon20, debug=false); }

public Figure newNgon(str lc, Figure el) {
      return at(0, 0, ngon(n =7, align = topLeft, lineColor= lc, lineWidth = 10, fillColor = "white", padding=<5,5,5,5>, 
      fig = el));
      }

public Figure ngon22 = (idNgon(50) |newNgon(e, it)| e<-["antiquewhite", "yellow", "red","blue" ,"grey","magenta"]);
void tngon22()  {render(ngon22, debug = false, align = topLeft);}

void ngons(){
  ex("ngons" , grid(gap=<0,0>,
                    figArray=[ 
							[ngon1, ngon2, ngon3, ngon4, ngon5, ngon6],
							[ngon7, ngon8, ngon9, ngon10, ngon11, ngon12],
							[ngon13, ngon14, ngon15, ngon16, ngon17, ngon18]			    
  						 ], align=topLeft), debug = false);
}

Figure scrabbleField = box(fillColor="antiqueWhite", size=<20,20>, lineColor = "green", lineWidth=1);

list[list[Figure]] scrabbleFields = [[scrabbleField|int j<-[0..14]]|i<-[0..14]];

void scrabble() {
    ex("scrabble", grid(gap=<0, 0>, figArray=scrabbleFields), debug = false);
    }




public void main() {
       q = getKeywordParameters(box_hcat6);
       z = visit(q) {
             case str t => "\"<t>\""
             }
       println(z);
     } 
