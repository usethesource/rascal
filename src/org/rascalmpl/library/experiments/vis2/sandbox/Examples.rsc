module experiments::vis2::sandbox::Examples
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import Prelude;

void ex(str title, Figure b, bool debug = false) = render(b, debug = debug, align = centerMid);


void ex(Figure b) = render(vcat(figs =[text("\\\"aap\\\""), b]));

// -----------------------------------------------------------------------------------

Figure WB = box(fillColor="antiquewhite", lineWidth = 6, lineColor = "grey", 
       size=<50,100>);
Figure RB = box(fillColor="red", size=<20,20>);

public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ render(box1, align = centerMid); }  

public Figure box2 = box(fillColor="yellow", size=<200,200>, lineColor = "red", 
        lineWidth=10);
void tbox2(){ render(box2); } 

public Figure box3 = box(fillColor="red", lineColor="lightgrey", lineWidth=30,  size=<200,200>);
void tbox3(){ render(box3, align = centerMid); }

void tRB() {render(RB, align = bottomRight, debug = true);}

public Figure box4 =  box(fig=RB, align = topLeft, fillColor="lightblue" , size=<200,200>
   , lineColor = "grey", lineWidth = 16);
void tbox4(){ render(box4, debug=false, align = bottomRight);} 

public Figure box5 =  box(fig=RB, align = bottomRight, fillColor="lightblue" , size=<200,200>
   , lineColor = "grey", lineWidth = 16);
void tbox5(){ render(box5, debug=false, align =centerMid);}

public Figure box6 =  box(fig=RB, align = centerMid, fillColor="lightblue" , size=<200,200>
   , lineColor = "grey", lineWidth = 16);
void tbox6(){ render(box6, debug=false, align = bottomRight);} 


public Figure box7 = box(fig=WB, fillColor="lightblue", lineWidth=2, lineColor="red");
void tbox7(){ render(box7); } 

public Figure box8 = box(fig=WB, fillColor="lightblue", lineWidth=10, lineColor="red");
void tbox8(){ render(box8); } 

public Figure box9 = box(fig=WB, lineWidth = 60, lineColor="red", fillColor="lightblue", padding=<0,0, 0, 0>, align=bottomRight);
    void tbox9(){ render(box9, debug = false, align = topLeft); } 

public Figure box10 = box(fig=WB, fillColor="lightblue",  padding=<10,10,10, 10>);
void tbox10(){ render(box10); } 

public Figure box11 = box(fig=at(10, 10, WB), fillColor="lightblue");
    void tbox11(){ render(box11); } 

public Figure box12 = box(align = topLeft, fig=at(0, 0, box(align = topLeft, lineWidth=60, 
    lineColor="silver", fillColor = "green", lineOpacity=1.0, size=<200,200>)), 
         lineColor="red", fillColor = "none", lineWidth=2);
void tbox12(){ render(box12, align = topLeft, debug = false); }


void boxes(){
	render( grid(gap=<0,0>, align = centerMid, 
					figArray=[ 
					           [box1, box2],
						       [RB, box4, box5, box6],
						       [WB,  box7, box8, box9],
						       [box10, box11],
						       [box12]
						  ]), width = 1000, height = 1000, debug = false);
}

// -----------------------------------------------------------------------------------

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


// --------------------------------------------------------------------------------


void tvcat() {render(box(fig=vcat(figs=[RB, box21])));}

void thcat() {render(hcat(figs=[WB, RB]));}

void tgrid() {render(grid(figArray=[[WB, RB], [RB, WB], [WB, RB, WB]]));}

Figures rgbFigs = [box(fillColor="red",size=<50,100>), box(fillColor="green", size=<200,200>), box(fillColor="blue", size=<10,10>)];

public Figure hcat1 = box(align = topLeft, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=topLeft));    
void thcat1(){ render(hcat1); }
void thcatf1(loc l){ writeFile(l, toHtmlString(hcat1,debug = false, align = topLeft)); }

public Figure hcat2 = box(align = topMid, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=topMid));  
void thcat2(){ ex("hcat2", hcat2); }

public Figure hcat3 = box(align = topRight, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=topRight));  
void thcat3(){ ex("hcat3", hcat3); }

public Figure hcat4 = box(align = centerLeft, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=centerLeft));    
void thcat4(){ render(hcat4, align = topLeft); }

public Figure hcat5 = box(align = centerMid, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=centerMid));  
void thcat5(){ ex("hcat5", hcat5); }

public Figure hcat6 = box(align =  centerRight, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=centerRight));  
void thcat6(){ ex("hcat6", hcat6); }

public Figure hcat7 = box(align = bottomLeft, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=bottomLeft));    
void thcat7(){ render(hcat7, align = topLeft); }

public Figure hcat8 = box(align = bottomMid, size=<300, 300>,   fig = hcat(figs=rgbFigs, align=bottomMid));  
void thcat8(){ ex("hcat8", hcat8); }

public Figure hcat9 = box(align = bottomRight, size=<300, 300>,  fig = hcat(figs=rgbFigs, align=bottomRight));  
void thcat9(){ ex("hcat9", hcat9); }

// hcat in box

Figure tlFigs = hcat(align=topLeft, padding=<0, 0, 0, 0>
 //  , borderWidth = 4, borderStyle="groove" , borderColor = "lightgrey"
					  , figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);

				             
Figure ctFigs = hcat(align=centerMid, gap=<10,10>, fillColor="lightgrey", 
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);

// public Figure hcat0 = hcat(figs= tlFigs, align=topLeft); 
Figure hcat10 = box(lineWidth = 2, lineColor="brown", fig = hcat(figs=rgbFigs));
     
void thcat10(){ ex("hcat10", hcat10); } 

void tfhcat0(loc l)= writeFile(l,
     toHtmlString(box(fig=Figs,  lineWidth = 10, lineColor= "brown"), debug = false));
     
public Figure hcat11 = 
       box(padding=<0, 0, 0, 0>, lineWidth = 10, fillColor = "antiquewhite", lineColor = "blue"
       ,fig= ellipse(padding=<0, 0, 0, 0>
             ,fig=hcat(figs=rgbFigs) 
       ,lineWidth = 10, lineColor= "brown", fillColor="lightgrey",align = centerMid
       )
 )
;

void tfhcat11(loc l)= writeFile(l,
     toHtmlString(hcat11, debug = false));
void thcat11(){ render(hcat11, debug = false);}

public Figure hcat12 = box(lineColor="yellow", lineWidth = 10, fig = 
       box(fig=hcat(figs=rgbFigs), fillColor="lightgrey", lineWidth = 20,  lineColor="brown", align=topLeft));
void hcat12(){ ex("hcat12", hcat12); }

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

void hcats(){
	ex("hcats", vcat(figs=[grid(gap=<0,0>
	   , borderWidth = 4 , borderColor = "brown", borderStyle = "ridge" 
	                 ,figArray=[
							[hcat1, hcat2, hcat3],
							[hcat4, hcat5, hcat6],
							[hcat7, hcat8, hcat9]], align=topLeft),
							 hcat10,
							 hcat12,	 
							 hcat11]					
						  ));
}

// vcat

public Figure vcat1 = box(align = topLeft, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=topLeft));    
void tvcat1(){ render(vcat1); }
void tvcatf1(loc l){ writeFile(l, toHtmlString(vcat1,debug = false, align = topLeft)); }

public Figure vcat2 = box(align = topMid, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=topMid));  
void tvcat2(){ ex("vcat2", vcat2); }

public Figure vcat3 = box(align = topRight, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=topRight));  
void tvcat3(){ ex("vcat3", vcat3); }

public Figure vcat4 = box(align = centerLeft, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=centerLeft));    
void tvcat4(){ render(vcat4, align = topLeft); }

public Figure vcat5 = box(align = centerMid, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=centerMid));  
void tvcat5(){ ex("vcat5", vcat5); }

public Figure vcat6 = box(align =  centerRight, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=centerRight));  
void tvcat6(){ ex("vcat6", vcat6); }

public Figure vcat7 = box(align = bottomLeft, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=bottomLeft));    
void tvcat7(){ render(vcat7, align = topLeft); }

public Figure vcat8 = box(align = bottomMid, size=<300, 350>,   fig = vcat(figs=rgbFigs, align=bottomMid));  
void tvcat8(){ ex("vcat8", vcat8); }

public Figure vcat9 = box(align = bottomRight, size=<300, 350>,  fig = vcat(figs=rgbFigs, align=bottomRight));  
void tvcat9(){ ex("vcat9", vcat9); }


Figure vcat10 = box(lineWidth = 2, lineColor="brown", fig = vcat(figs=rgbFigs));
     
void tvcat10(){ ex("vcat10", vcat10); } 

void tfvcat10(loc l)= writeFile(l,
     toHtmlString(box(fig=tlFigs,  lineWidth = 10, lineColor= "brown"), debug = false));
     
public Figure vcat11 = 
       box(padding=<0, 0, 0, 0>, lineWidth = 10, fillColor = "antiquewhite", lineColor = "blue"
       ,fig= ellipse(padding=<0, 0, 0, 0>
       ,fig=vcat(figs=rgbFigs) 
       ,lineWidth = 10, lineColor= "brown", fillColor="lightgrey",align = centerMid
       )
 )
;

void tfvcat11(loc l)= writeFile(l,
     toHtmlString(vcat11, debug = false));
void tvcat11(){ render(vcat11, debug = false);}

public Figure vcat12 = box(lineColor="yellow", lineWidth = 10, fig = 
       box(fig=vcat(figs=rgbFigs), fillColor="lightgrey", lineWidth = 20,  lineColor="brown", align=topLeft));
void vcat12(){ ex("vcat12", vcat12); }

void vcats(){
	ex("vcats", vcat(figs=[grid(gap=<0,0>
	   //, borderWidth = 4 , borderColor = "brown", borderStyle = "ridge" 
	                 ,figArray=[
							[vcat1, vcat2, vcat3],
							[vcat4, vcat5, vcat6],
							[vcat7, vcat8, vcat9]], align=topLeft),
							 vcat10,
							 vcat12,	 
							 vcat11]					
						  ));
}	

// vcat in box

Figure vtlFigs = vcat(align=topLeft, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
				             
Figure vctFigs = vcat(align=centerMid, gap=<0,0>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);

public Figure box_vcat1 = box(fig=vtlFigs, fillColor="grey");
void tbox_vcat1(){ ex("box_vcat1", box_vcat1); }

public Figure box_vcat2 =  box(fig=vtlFigs, size=<400,400>, align=topLeft, fillColor="grey");
void tbox_vcat2(){ ex("box_vcat2", box_vcat2); }

public Figure box_vcat3 = box(fig=vtlFigs, size=<400,400>, align=topRight, fillColor="grey");
void tbox_vcat3(){ ex("box_vcat3", box_vcat3); }

public Figure box_vcat4 = box(fig=vtlFigs, size=<400,400>, align=bottomRight, fillColor="grey");
void tbox_vcat4(){ ex("box_vcat4", box_vcat4); }

public Figure box_vcat5 = box(fig=vtlFigs, size=<400,400>, align=bottomLeft, fillColor="grey");
void tbox_vcat5(){ ex("box_vcat5", box_vcat5); }

public Figure box_vcat6 = box(fig=vctFigs, size=<400,400>, align=topLeft, fillColor="grey");
void tbox_vcat6(){ ex("box_vcat6", box_vcat6); }

public Figure box_vcat7 = box(fig=vctFigs, size=<400,400>, align=topRight, fillColor="grey");
void tbox_vcat7(){ ex("box_vcat7", box_vcat7); }

public Figure box_vcat8 = box(fig=vctFigs, size=<400,400>, align=bottomRight, fillColor="grey");
void tbox_vcat8(){ ex("box_vcat8", box_vcat8); }

public Figure box_vcat9 = box(fig=vctFigs, size=<400,400>, align=bottomLeft, fillColor="grey");
void tbox_vcat9(){ ex("box_vcat9", box_vcat9);
}

public Figure box_vcat10 = ellipse(lineWidth=10, lineColor = "blue", fillColor = "antiquewhite"
      , padding = <52, 52, 52, 52>
      , fig = 
      box(lineWidth = 10, lineColor="brown", fig=vctFigs, align=bottomLeft, 
      fillColor="lightgrey")
      )
      ;
      
void tbox_vcat10(){ ex("box_vcat10", box_vcat10);
}

/*
void vcats(){
	ex("vcats", grid(gap=<10,10>, 
	                 figArray=[
							[vcat1, vcat2, vcat3, vcat4, box_vcat1],
							[box_vcat2,box_vcat3,box_vcat4,box_vcat5],
							[box_vcat6,box_vcat7,box_vcat8, box_vcat9],
							[box_vcat10]
							
						  ], align=centerMid));
}
*/	


/********************** grid ******************************/

Figure RedBox = box(fillColor="red", lineWidth = 3, size=<50,50>);
Figure BlueBox = box(fillColor="blue", lineWidth = 3,size=<30,70>);
Figure GreenBox = box(fillColor="green", lineWidth = 3, size=<40,40>);

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

// ellipse ------------------------------------------------------------
public Figure ellipse1 = box(lineWidth = 2, fig=ellipse(rx=100, ry=75,   fillColor="lightblue"));
void tellipse1(){ ex("ellipse1", ellipse1); }

void tfellipse1(loc l)  = writeFile(l, toHtmlString(ellipse1));

public Figure ellipse2 = box(lineWidth = 2,  fig=ellipse(rx=100, lineWidth = 10, lineColor = "antiquewhite", ry=75, 
      fillColor="lightgrey"));
void tellipse2(){ ex("ellipse2", ellipse2); }

public Figure ellipse2a = box(lineWidth = 10, lineColor = "lightblue", 
          fig=ellipse(rx=100, lineWidth = 10, lineColor = "antiquewhite", ry=75, 
      fillColor="lightgrey"));
void tellipse2a(){ ex("ellipse2a", ellipse2a); }

public Figure ellipse3 = ellipse(lineWidth=20, lineColor= "lightgrey",  rx=100, ry=75, 
fillColor="red", align = topLeft, fig=box(size=<50,80>, lineWidth = 1,fillColor="yellow", fig=circle(r=10)));
void tellipse3(){ ex("ellipse3", ellipse3); }

public Figure ellipse4 = ellipse(lineWidth=20, lineColor= "lightgrey",  rx=100, ry=75, 
fillColor="red", align = centerMid, fig=box(size=<50,80>, lineWidth = 1, fillColor="yellow", fig=circle(r=10)));
void tellipse4(){ ex("ellipse4", ellipse4); }


public Figure ellipse5 = ellipse(lineWidth=20, lineColor= "lightgrey",  rx=100, ry=75, 
fillColor="red", align = bottomRight, fig=box(size=<50,80>, lineWidth = 1, fillColor="yellow", fig=circle(r=10)));
void tellipse5(){ ex("ellipse5", ellipse4); }

public Figure ellipse6 = at(0, 0, ellipse(padding = <0, 0, 0,0>,
    lineColor="red", align = centerMid, lineWidth=9, fillColor = "white", fig=at(0,0,ellipse(align = topLeft, rx=100, ry=75, 
    lineWidth=19, padding = <0, 0, 0, 0>,
    lineColor="silver", fillColor = "yellow", lineOpacity=0.5))));


void tellipse6(){ render(ellipse6, debug = false, align = topLeft); }


void ellipses() {
   render(grid(align = centerMid, 
      figArray=[[ellipse1, ellipse2, ellipse2a], [ellipse3, ellipse4, ellipse5], [ellipse6]]), debug = false);
}
//-----------------------------------------------------------------------------------

public Figure ellipse7 = ellipse(lineColor="red", lineWidth = 6, fillColor ="antiquewhite", fig=ellipse(rx=100, ry=75, lineWidth=10, lineColor="silver", fillColor= "green"));
void tellipse7(){ ex("ellipse7", ellipse7, debug=false); }


// circle ---------------------------------------------------------------------

public Figure circle1 = circle(r=100, fillColor = "red");
void tcircle1(){ ex("circle1", circle1); }

public Figure circle2 = circle(r=100, fillColor="red", fig= circle(r=50, fillColor="blue"));
void tcircle2(){ ex("circle2", circle2); }

public Figure circle3 = circle(fillColor="red", r=  100, align =  bottomRight, fig=box(size=<50,80>, fillColor="yellow"));
void tcircle3(){ ex("circle3", circle3); }

public Figure circle4 = box(lineColor="red", fig=circle(fillColor="pink", r=100, lineWidth=1, lineColor="silver", lineOpacity=0.5));
void tcircle4(){ ex("circle4", circle4); }

public Figure circle5 = box(lineColor="red", fig=circle(fillColor="lightGrey", r=100, lineWidth=10, lineColor="silver"));
void tcircle5(){ ex("circle5", circle5); }


public Figure circle6 = box(lineColor="red", lineWidth=1, fig=circle(fillColor="lightblue", r=100, lineWidth=10, lineColor="silver"));
void tcircle6(){ ex("circle6", circle6); }

public Figure circle7 = box(lineColor="red", lineWidth=5, fig=box(fillColor="black", width=200, height = 100, lineWidth=0, lineColor="silver"));
void tcircle7(){ ex("circle7", circle7); }

void circles() {
   ex("circles", vcat(align=bottomRight, figs=[circle1, circle2, circle3, circle4, circle5, circle6]), debug = false);
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

public Figure ngon14 = ngon(r = 120, n = 4, fillColor="red", fig=box(size=<125,90>, fillColor="yellow"));
void tngon14(){ ex("ngon14", ngon14); }

public Figure ngon15 = ngon(r= 100, n = 5, fillColor="red", fig=box(size=<125,90>, fillColor="yellow"));
void tngon15(){ ex("ngon15", ngon15); }

public Figure ngon16 = ngon(r = 100, n = 6, fillColor="blue",  fig=box(size=<125,90>, fillColor="yellow"));
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


void ngons(){
  ex("ngons" , grid(gap=<0,0>,
                    figArray=[ 
							[ngon1, ngon2, ngon3, ngon4, ngon5, ngon6],
							[ngon7, ngon8, ngon9, ngon10, ngon11, ngon12],
							[ngon13, ngon14, ngon15, ngon16, ngon17, ngon18]			    
  						 ], align=topLeft), debug = false);
}

Figure plotg(num(num) g, list[num] d) {
     Figure f = shape( [move(d[0], g(d[0]))]+[line(x, g(x))|x<-tail(d)]
         scaleX=<<-1,1>,<20,320>>,  scaleY=<<0,1>,<100,300>>,
         size=<400, 400>,shapeCurved=  true, lineColor = "red"
         , startMarker = box(width=10, height = 10,  fillColor = "blue", lineWidth = 0)
         , midMarker = ngon(n=3, r=4, fillColor = "purple", lineWidth = 0)
         , endMarker = ngon(n=3, r=10, fillColor = "green", lineWidth = 0)
         );
     return f;
     }
     
num(num) gg(num a) = num(num x) {return a*x*x;};

num g1(num x) = x*x;

Figure shape0 = plotg(gg(0.5), [-1,-0.9..1.1]);

void tshape0() {render(shape0);}

void tfshape0(loc f ) {writeFile(f, toHtmlString(plotg(gg(0.5), [-1+0.2,-0.8+0.2..1.1])));}



Figure shapes0() {
   list[num ] d =  [-1,-0.99..1.01];
   return box(fig=overlay(width= 400, height = 400, figs=[plotg(gg(a),d )|a<-[0.3,0.35..2.1]]));
   }
   
void tshapes0() {render(shapes0());} 

/********************** shape ******************************/

public Figure shape1 = shape([line(100,100), line(100,200), line(200,200)], shapeClosed=true);
void tshape1(){	ex("shape1", shape1); }
void ftshape1(loc f){writeFile(f, toHtmlString(shape1));}

public Figure shape2 = shape([line(30,100), line(100, 100), line(200,80)], shapeClosed=true);
void tshape2(){ ex("shape2", shape2); }

public Figure shape3 = hcat(figs=[ shape([line(100,100), line(100, 200), line(200,200)], shapeClosed=true, fillColor="red"),
	
							 shape([line(100,100), line(100, 200), line(200,200)], shapeClosed=true, fillColor="blue")
							 ]);
void tshape3(){ ex("shape3", shape3); }

public Figure shape4 = shape([line(0,0), line(50, 50), line(80,50), line(100,0) ], shapeClosed = true,  fillColor = "yellow");
void tshape4(){	ex("shape4", shape4); }

public Figure shape5 = shape([line(0,0), line(50, 50), line(80,50), line(100,0) ], shapeCurved=true, shapeClosed = true, fillColor = "yellow");
void tshape5(){	ex("shape5", shape5); }

public Figure shape6 = box(lineColor="red", fig=shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="evenodd", fillColor = "grey"));
void tshape6(){	ex("shape6", shape6); }

public Figure shape7 = box(lineColor="red", lineWidth=10, fig=shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      shapeClosed=true, fillRule="evenodd", fillColor = "grey"));
					                      
void tshape7(){	ex("shape7", shape7); }

// SVG Essentials, p95.

public Figure fillRule1 = grid(fillColor="antiquewhite",
						figArray=[ [ shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
						                 startMarker=ngon(n=3, r=10 ,fillColor="red"),     
										 endMarker=ngon(n=3, r =10,fillColor="green"),
					                     shapeClosed=true, fillEvenOdd=false, fillColor = "grey"),
					           
					                 shape([line(0,0), line(0, 60), line(60,60), line(60, 0), move(15,15), line(15,45), line(45,45), line(45, 15)],
					                       startMarker=ngon(n=3, r=10 ,fillColor="red"),     
										   endMarker=ngon(n=3, r =10,fillColor="green"), 	// clockwise/counter clockwise
					                       shapeClosed=true, fillEvenOdd=false, fillColor = "grey")
					               ],
					               
					               [ shape([line(0,0), line(60, 0), line(60,60), line(0,60), move(15,15), line(45, 15), line(45,45), line(15,45)],  // clockwise/clockwise
					                      startMarker=ngon(n=3, r=10 ,fillColor="red"),     
										  endMarker=ngon(n=3, r =10,fillColor="green"),
					                      shapeClosed=true, fillEvenOdd=true, fillColor = "grey"),
					           
					                 shape([line(0,0), line(0, 60), line(60,60), line(60,0), move(15,15), line(15,45), line(45,45), line(45, 15)], 	// clockwise/counter clockwise
					                       startMarker=ngon(n=3, r=10 ,fillColor="red"),     
										   endMarker=ngon(n=3, r =10,fillColor="green"),
					                       shapeClosed=true, fillEvenOdd=true, fillColor = "grey")
					               ] ]);
void tfillRule1(){ ex("fillRule1", fillRule1); }

void tfill(loc l) = writeFile(l, toHtmlString(fillRule1));

void shapes(){
	ex("shapes", grid(gap=<10,10>,
					figArray=[ [shape1, shape2, shape3, shape4, shape5 ],
							   [shape6, shape7, fillRule1],
							   [shape0]
							 ]));

}

// ------------------------------------  TO DO  --------------------------------

public Figure hflex1 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);

void thflex1(){	ex("hflex1", hflex1); }

public Figure hflex2 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red", height=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void thflex2(){	ex("hflex2", hflex2); }

public Figure hflex3 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red", width=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void thflex3(){	ex("hflex3", hflex3); }	

public Figure hflex4 = hcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void thflex4(){	ex("hflex4", hflex4); }

public Figure hflex5 = hcat(size = <400,400>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void thflex5(){ ex("hflex5", hflex5); }						               

// vcat flex

public Figure vflex1 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);

void tvflex1(){ ex("vflex1", vflex1); }

public Figure vflex2 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red", height=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void tvflex2(){ ex("vflex2", vflex2); }

public Figure vflex3 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red", width=10), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue", size=<50,50>)
				                   ]);
void tvflex3(){	ex("vflex3",vflex3); }	

public Figure vflex4 = vcat(size = <200,200>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<100,100>), 
				                     box(fillColor="blue")
				                   ]);
void tvflex4(){ ex("vflex4", vflex4); }

public Figure vflex5 = vcat(size = <400,400>,
					        figs = [ box(fillColor="red"), 
				                     box(fillColor="green", size=<200,100>), 
				                     box(fillColor="blue")
				                   ]);
void tvflex5(){	ex("vflex5", vflex5);
}	


// hvcat flex
Figure hvf0() = hcat(size=<400,600>, hgap = 5, 
					   figs= [ 
					          vcat(width=250, height=400, vgap= 10, 
	                                figs= [ 
	                                       box(fillColor="red"), 
				                           box(fillColor="green", width=100), 
				                           box(fillColor="blue")
				                          ]),
				               vcat(// size = <400,400>,
					               figs = [ box(fillColor="yellow", height=50), 
				                            box(fillColor="purple"), 
				                            box(fillColor="orange")
				                          ])
				            ]);

void hvflex1(){
	render(hvf0(), size=<800, 800>, align = topLeft);
}

void hvf()	= writeFile(|file:///ufs/bertl/html/u.html|, toHtmlString(hvf0()));
	

 

// grid flex
Figure gfl0() = grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", width=50), box(fillColor="blue")],
				                  [box(fillColor="yellow", height=50), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]);

void gflex1(){
	ex("gflex1", gfl0());
}

void gfl(loc l )	= writeFile(l, toHtmlString(gfl0()));

void gflex2(){
	ex("gflex2", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", size=<50,50>), box(fillColor="blue")],
				                  [box(fillColor="yellow", height=50), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

void gflex3(){
	ex("gflex3", grid(size=<600,600>,
					  figArray= [ [box(fillColor="red"),               box(fillColor="green", size=<50,50>, align=topRight), box(fillColor="blue")],
				                  [box(fillColor="yellow", size=<50,50>, align=bottomLeft), box(fillColor="purple"),          box(fillColor="mediumspringgreen") ]
				                ]));
}

// ------------------------------------------------------------------------------ 

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
