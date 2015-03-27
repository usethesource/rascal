module experiments::vis2::sandbox::Examples
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;

void ex(str title, Figure b) = render(b);

public Figure box1 = box(fillColor="red", size=<200,200>);
void tbox1(){ render(box1); }  

public Figure box2 = box(fillColor="red", size=<200,200>, lineWidth=10);
void tbox2(){ render(box2); } 

public Figure box3 = box(fillColor="red", lineColor="blue", lineWidth=10, size=<200,200>);
void tbox3(){ render(box3); }

Figure WB = box(fillColor="yellow", size=<50,100>);
Figure RB = box(fillColor="red", size=<20,20>);

void tRB() {render(RB, align = bottomRight);}

public Figure box4 =  box(fig=WB, fillColor="blue", size=<100,100>);
void tbox4(){ render(box4);} 

public Figure box5 = box(fig=WB, fillColor="blue", size=<200,200>, align=topLeft);
void tbox5(){ render(box5); } 

public Figure box6 = box( fig=WB, fillColor="blue", size=<200,200>, align=topRight);
void tbox6(){ render(box6); } 

public Figure box7 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomRight);
void tbox7(){ render(box7); } 

public Figure box8 = box(fig=WB, fillColor="blue", size=<200,200>, align=bottomLeft);
void tbox8(){ render(box8); } 

public Figure box9 = box(fig=WB, fillColor="lightblue", size=<110,110>, gap=<10,10>, align=topLeft);
    void tbox9(){ render(box9); } 

public Figure box10 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=topRight);
void tbox10(){ render(box10); } 

public Figure box11 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=bottomLeft);
    void tbox11(){ render(box11); } 

public Figure box12 = box(fig=WB, fillColor="lightblue", size=<200,200>, gap=<10,10>, align=bottomRight);
void tbox12(){ render(box12); } 

public Figure box13 = box(fig=box(fig=RB, fillColor="white", size=<50,100>), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox13(){ render(box13); } 

public Figure box14 = box(size=<250,250>, fig=box(fig=RB, fillColor="white", size=<50,100>, align=topLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox14(){render(box14); }

public Figure box15 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=topRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox15(){ render(box15); }

public Figure box16 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=bottomRight), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox16(){ render(box16); }

public Figure box17 = box(fig=box(fig=RB, fillColor="white", size=<50,100>, align=bottomLeft), fillColor="blue", size=<200,200>, gap=<10,10>, align=bottomLeft);
void tbox17(){ render(box17); }

// public Figure box18 = box(fig=RB, fillColor="blue", grow=3);
//void tbox18(){ ex("box18", box18); }
//
//public Figure box19 = box(fig=text("Hello", fontSize=20), grow=2);
//void tbox19(){ ex("box19", box19); }

public Figure box20 = box(size=<210, 210>, align = topLeft, fig=box( lineWidth=20, lineColor="blue", fillColor = "white", size=<200,200>, lineOpacity = 0.5), fillColor = "white", lineColor="red");
void tbox20(){ render(box20, align = bottomRight); }

public Figure box21 = box(size=<215, 215>,fig=box(lineWidth=20, lineColor="silver", fillColor = "green", lineOpacity=1.0, size=<200,200>), lineColor="red", fillColor = "none", lineWidth=15);
void tbox21(){ render(box21); }

void boxes(){
	render( grid(gap=<0,0>,
					figArray=[ [box4, box5, box6],
						       [box4, box5, box6, box7],
						       [box10],
						        [box21]
						  ]), width = 1000, height = 1000);
}

void tvcat() {render(vcat(figs=[WB, RB]));}

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
				             
Figure ctFigs = hcat(align=centerMid, gap=<10,10>,
					  figs = [ box(fillColor="red",size=<50,100>), 
				               box(fillColor="green", size=<200,200>), 
				               box(fillColor="blue", size=<10,10>)
				             ]);
public Figure box_hcat1 = box(fig=tlFigs,  fillColor="lightgrey",align = topLeft);
void tbox_hcat1(){ render(box_hcat1); }

public Figure box_hcat2 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=topLeft);
void tbox_hcat2(){ ex("box_hcat2", box_hcat2); }

public Figure box_hcat3 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=topRight);
void tbox_hcat3(){ ex("box_hcat3", box_hcat3); }

public Figure box_hcat4 = box(fig=tlFigs,fillColor="lightgrey", size=<400,400>, align=bottomRight);
void tbox_hcat4(){ ex("box_hcat4", box_hcat4); }

public Figure box_hcat5 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=bottomLeft);
void tbox_hcat5(){ ex("box_hcat5", box_hcat5); }

public Figure box_hcat6 = box(fig=tlFigs, fillColor="lightgrey", size=<400,400>, align=topLeft);
void tbox_hcat6(){ ex("box_hcat6", box_hcat6); }

public Figure box_hcat7 = box(fig=ctFigs, fillColor="lightgrey", size=<400,400>, align=topRight);
void tbox_hcat7(){ ex("box_hcat7", box_hcat7); }

public Figure box_hcat8 = box(fig=ctFigs, fillColor="lightgrey", size=<400,400>, align=bottomRight);
void tbox_hcat8(){ ex("box_hcat8", box_hcat8); }

public Figure box_hcat9 = box(fig=ctFigs, fillColor="lightgrey", size=<400,400>, align=bottomLeft);
void tbox_hcat9(){ ex("box_hcat9", box_hcat9); }
