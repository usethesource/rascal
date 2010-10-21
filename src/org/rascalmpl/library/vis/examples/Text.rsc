module vis::examples::Text

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

// Text
public void txt1(){
	render(text("Een label"));
}

// Blue text of size 20
public void txt2(){
	render(text([fontSize(20), fontColor("blue")], "A giant label"));
}

// Unsized box with inner text
public void txt3(){
	render(box([gap(1)], text([fontSize(20), fontColor("black")], "A giant label")));
}

// Horizontal bottom-aligned composition of text of different size

public void txt4(){
	render(box([gap(10)], 
	           hcat([bottom()],
	               [ text([fontSize(20), fontColor("black")], "Giant xyz 1"),
	 				 text([fontSize(40), fontColor("blue")], "Giant xyz 2"),
	 				 text([fontSize(60), fontColor("red")], "Giant xyz 3")
	 			   ])));
}

// Text rotated -90 degrees (counter clockwise)
public void txt5(){
	render(text([ fontSize(20), textAngle(-90)], "Een label"));
}

// Vertical; composition of rotated text and a box

public void txt6(){
	render(vcat([center(), gap(10)],
	               [
	                box([ width(100), height(200), fillColor("red")]),
	                text([fontSize(20), textAngle(-90)], "Een label")
	                ]));
}

// Overlay of box and text
//Note: the result is not pretty since the baseline of the text and the bottom of the box are aligned.
public void txt7(){
	render(overlay([bottom(), right()],
	              [box([ width(150), height(200), fillColor("red") ]),
			       text([fontSize(20)], "Een label")
			      ]
		));
}

// Vertical stack of text of various font sizes

public void txt8(){
   render(box([gap(1)], 
	           vcat([bottom()],
	               [ text([fontSize(20), fontColor("black")], "A"),
	 				 text([fontSize(40), fontColor("blue")], "BB"),
	 				 text([fontSize(60), fontColor("red")], "CC")
	 			   ])));
}

public void txt9(){
   words = [ text("aappp"), text("noot"), text("mies"), text("wim"), text("zus") ];
   
   render(grid([width(100), fillColor("black"), gap(40), bottom(), left()], words));
 }
 //TODO
 public void txt10(){
   words = [ text("aappp"), text("noot"), text("mies"), text("wim"), text("zus") ];
   
   render(grid([width(100), fillColor("black"), gap(40), bottom(), left(), textAngle(-90)], words));
 }

private map[str, int] leesplank = 
 ("aap" : 10, "noot" :5, "mies" : 7,
        "wim" : 5, "zus": 10, "jet": 40, 
        "teun" : 10, "vuur" : 20, "gijs" : 5,
        "lam" : 50, "kees" : 30, "bok" : 20,
        "weide" : 20,  "does" : 25, "hok" : 15,
        "duif" : 30, "schapen" : 35
         );
         
// Word cloud using align
public void txt11(){
     words = [text([ fontSize(2*leesplank[name])], "<name>") | name <- leesplank];
     render(align([width(400), fillColor("black"), gap(10), bottom(), left()], words));
}

// Word cloud using pack
public void txt12(){
     words = [text([ fontSize(2*leesplank[name])], "<name>") | name <- leesplank];
     render(pack([width(400), fillColor("black"), gap(10), bottom(), left()], words));
}

// Word cloud using pack with rotated words
public void txt13(){
     words = [text([ fontSize(2*leesplank[name]), (arbInt(3) == 2) ? textAngle(-90) : textAngle(0)], "<name>") | name <- leesplank];
     render(pack([width(400), fillColor("black"), gap(10), bottom(), left()], words));
}
