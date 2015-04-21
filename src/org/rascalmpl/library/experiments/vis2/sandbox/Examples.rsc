module experiments::vis2::sandbox::Examples
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

void ex(str title, Figure b, bool debug = true) = render(b, debug = debug);

void ex(Figure b) = render(vcat(figs =[text("\\\"aap\\\""), b]));

public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ render(box1); }  

public Figure box2 = box(fillColor="red", size=<200,200>, lineWidth=10);
void tbox2(){ render(box2); } 

public Figure box3 = box(fillColor="red", lineColor="blue", lineWidth=10, align = bottomMid, size=<200,200>);
void tbox3(){ render(box3, align = bottomMid); }

Figure WB = box(fillColor="yellow", size=<50,100>);
Figure RB = box(fillColor="red", size=<20,20>);

void tRB() {render(RB, align = bottomRight);}

public Figure box4 =  box(fig=WB, fillColor="blue", size=<100,100>);
void tbox4(){ render(box4);} 

public Figure box5 = box(fig=WB, fillColor="blue", size=<200,200>, align=topLeft);
void tbox5(){ render(box5); } 

public Figure box6 = box( fig=WB, fillColor="blue", size=<200,200>, gap=<10, 10>, align=topRight);
void tbox6(){ render(box6); } 

public Figure box7 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomRight);
void tbox7(){ render(box7); } 

public Figure box8 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomLeft);
void tbox8(){ render(box8); } 

public Figure box9 = box(fig=WB, fillColor="lightblue", gap=<10,10>, align=topLeft);
    void tbox9(){ render(box9); } 

public Figure box10 = box(fig=WB, fillColor="lightblue",  size=<200, 200>, gap=<10,10>, align=topRight);
void tbox10(){ render(box10); } 

public Figure box11 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=bottomLeft);
    void tbox11(){ render(box11); } 

public Figure box12 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=bottomRight);
void tbox12(){ render(box12); } 

public Figure box13 = box(fig=box(fig=RB, fillColor="white", size=<50,100>), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox13(){ render(box13); } 

public Figure box14 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox14(){render(box14); }

public Figure box15 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topRight), fillColor="white", size=<200,200>, gap=<10,20>, align=bottomLeft);
void tbox15(){ render(box15); }

public Figure box16 = box(fig=box(fig=RB, fillColor="white",  size=<50, 50>, align=bottomRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox16(){ render(box16); }

public Figure box17 = box(fig=box(fig=RB, fillColor="white",  size=<50, 50>, align=bottomLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomRight);
void tbox17(){ render(box17); }

// public Figure box18 = box(fig=RB, fillColor="blue", grow=3);
//void tbox18(){ ex("box18", box18); }
//
//public Figure box19 = box(fig=text("Hello", fontSize=20), grow=2);
//void tbox19(){ ex("box19", box19); }

public Figure box20 = box(align = topLeft, lineWidth = 5, fig=box( lineWidth=20, lineColor="blue", fillColor = "white", size=<200,200>, lineOpacity = 0.5), fillColor = "white", lineColor="red");
void tbox20(){ render(box20, align = bottomRight); }

public Figure box21 = box(align = topLeft, fig=box(align = topLeft, lineWidth=60, lineColor="silver", fillColor = "green", lineOpacity=1.0, size=<200,200>), lineColor="red", fillColor = "none", lineWidth=15);
void tbox21(){ render(box21, align = centerMid); }

void boxes(){
	render( grid(gap=<0,0>, align = centerMid, 
					figArray=[ 
					           [box4, box5, WB],
						       [box5, box4, box6, box7],
						       [RB],
						        [box21]
						  ]), width = 1000, height = 1000);
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

Figure ell0 = ellipse(cx=85, cy = 45, rx = 80, ry = 40,  lineColor="red", lineWidth = 5, fillColor="none");
Figure ell1 = ellipse(cx=50, cy = 90, rx = 40, ry = 80, align= centerMid,  fig=vcat(figs=[
   circle(cx=13, cy= 13, r = 10, fillColor= "none", lineColor = "blue", lineWidth = 3)
   ,text("Hallo")
   ]), lineColor="black",lineWidth=10, fillColor="none" );
void tell0(){ ex("hello", hcat(figs=[ell0, ell1], gap = <10, 10>), debug = false); }

// ellipse

public Figure ellipse1 = ellipse(cx=100, cy=100, rx=100, ry=75);
void tellipse1(){ ex("ellipse1", ellipse1); }

public Figure ellipse2 = ellipse(cx=100, cy=100, rx=100, ry=75, fillColor="red");
void tellipse2(){ ex("ellipse2", ellipse2); }

public Figure ellipse3 = ellipse(cx=100, cy=100,  rx=100, ry=75, fillColor="red", fig=box(size=<50,80>, fillColor="yellow"));
void tellipse3(){ ex("ellipse3", ellipse3, debug = true); }

public Figure ellipse4 = box(lineColor="red", fig=ellipse(cx=101, cy=100, rx=100, ry=75, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tellipse4(){ ex("ellipse4", ellipse4); }

public Figure ellipse5 = box(lineColor="red", fig=ellipse(cx=110, cy=100, rx=100, ry=75, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tellipse5(){ ex("ellipse5", ellipse5); }

public Figure ellipse6 = box(lineColor="red", lineWidth=15, fig=ellipse(cx=110, cy=100, rx=100, ry=75, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tellipse6(){ ex("ellipse6", ellipse6); }

void ellipses() {
   ex("ellipses", vcat(figs=[ellipse1, ellipse2, ellipse3, ellipse4, ellipse5, ellipse6]), debug = false);
}

// circle

public Figure circle1 = circle(r=100, cx = 101, cy = 101);
void tcircle1(){ ex("circle1", circle1); }

public Figure circle2 = circle(r=100, cx = 101, cy = 101, fillColor="red");
void tcircle2(){ ex("circle2", circle2); }

public Figure circle3 = circle(fillColor="red", r=  100, cx = 101, cy = 101, fig=box(size=<50,80>, fillColor="yellow"));
void tcircle3(){ ex("circle3", circle3); }

public Figure circle4 = box(lineColor="red", fig=circle(cx=101, cy=101, r=100, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tcircle4(){ ex("circle4", circle4); }

public Figure circle5 = box(lineColor="red", fig=circle(cx=110, cy=110, r=100, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tcircle5(){ ex("circle5", circle5); }


public Figure circle6 = box(lineColor="red", lineWidth=15, fig=circle(cx=115, cy=115, r=100, lineWidth=10, lineColor="silver", lineOpacity=0.5));
void tcircle6(){ ex("circle6", circle6); }

void circles() {
   ex("circles", vcat(align=bottomRight, figs=[circle1, circle2, circle3, circle4, circle5, circle6, box(size=<300, 300>)]), debug = false);
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



public void main() {
       q = getKeywordParameters(box_hcat6);
       z = visit(q) {
             case str t => "\"<t>\""
             }
       println(z);
     } 
