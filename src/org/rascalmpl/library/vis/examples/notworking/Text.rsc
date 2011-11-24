@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::examples::Text

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

/*

// Text
public void txt1(){
	render(text("Een label"));
}

// Blue text of size 20
public void txt2(){
	render(text("A giant label", fontSize(20), fontColor("blue")));
}

// Unsized box with inner text
public void txt3(){
	render(box(text("A giant label", fontSize(20), fontColor("black")), gap(1)));
}

// Horizontal bottom-aligned composition of text of different size

public void txt4(){
	render(box(hcat([ text("Giant xyz 1", fontSize(20), fontColor("black")),
	 				  text("Giant xyz 2", fontSize(40), fontColor("blue")),
	 				  text("Giant xyz 3", fontSize(60), fontColor("red"))
	 			    ],
	 			   child(bottom())
	 			   ),
	 			gap(10)));
}

// Text rotated -90 degrees (counter clockwise)
public void txt5(){
	render(text("Een label", fontSize(20), textAngle(-90)));
}

// Vertical; composition of rotated text and a box

public void txt6(){
	render(vcat( [  box(width(100), height(200), fillColor("red")),
	                text("Een label", fontSize(20), textAngle(-90))
	             ],
	             child(center()), gap(10)));
}

// Overlay of box and text
//Note: the result is not pretty since the baseline of the text and the bottom of the box are aligned.
public void txt7(){
	render(overlay(
	              [box(width(150), height(200), fillColor("red")),
			       text( "Een label", fontSize(20))
			      ],
			      child(bottom(), right())
		));
}

// Vertical stack of text of various font sizes

public void txt8(){
   render(box(vcat([ text("A", fontSize(20), fontColor("black")),
	 				 text("BB", fontSize(40), fontColor("blue")),
	 				 text("CC", fontSize(60), fontColor("red"))
	 			   ],
	 			   child(bottom())),
	 		  gap(1)));
}

public void txt9(){
   words = [ text("aappp"), text("noot"), text("mies"), text("wim"), text("zus") ];
   
   render(grid(words, width(100), fillColor("black"), gap(40), child(bottom(), left())));
 }
 
public void txt10(){
   render(text("a\nbb\nccc\ndddd"));
}

public void txt10l(){
   render(text("a\nbb\nccc\ndddd", left()));
}

public void txt10r(){
   render(text("a\nbb\nccc\ndddd", right()));
}

// TODO: Fix alignment of rotated, multi-iline text.

public void txt11(){
   render(text("a\nbb\nccc\ndddd", textAngle(-90)));
}

public void txt11l(){
   render(text("a\nbb\nccc\ndddd", textAngle(-90), left()));
}

public void txt11r(){
   render(text("a\nbb\nccc\ndddd", textAngle(-90), right()));
}

 
 
 //TODO
 public void txt12(){
   words = [ text("aappp"), text("noot"), text("mies"), text("wim"), text("zus") ];
   
   render(grid(words, width(100), fillColor("black"), gap(40), child(bottom(), left()), textAngle(-90)));
 }

private map[str, int] leesplank = 
 ("aap" : 10, "noot" :5, "mies" : 7,
        "wim" : 5, "zus": 10, "jet": 40, 
        "teun" : 10, "vuur" : 20, "gijs" : 5,
        "lam" : 50, "kees" : 30, "bok" : 20,
        "weide" : 20,  "does" : 25, "hok" : 15,
        "duif" : 30, "schapen" : 35
         );
         
// Word cloud using hvcat
public void txt11wc(){
     words = [text("<name>", fontSize(2*leesplank[name])) | name <- leesplank];
     render(hvcat(words, width(400), fillColor("black"), gap(10), bottom(), left()));
}

// Word cloud using pack
public void txt12wc(){
     words = [text( "<name>", fontSize(2*leesplank[name])) | name <- leesplank];
     render(pack(words, width(400), fillColor("black"), gap(10), bottom(), left()));
}

// Word cloud using pack with rotated words
public void txt13wc(){
     words = [text("<name>", fontSize(2*leesplank[name]), (arbInt(3) == 2) ? textAngle(-90) : textAngle(0)) | name <- leesplank];
     render(pack(words, width(400), fillColor("black"), gap(10), bottom(), left()));
}

*/
