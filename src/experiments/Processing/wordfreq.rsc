module experiments::Processing::wordfreq

import Processing;
import TreeMap;
import IO;
import Map;

/*
 * Data collection
 */
 
/*
 * Two data sets
 */

private list[str] leesplank = [
   "aap aap aap aap aap aap aap aap aap aap",
   "noot noot noot",
   "mies",
   "wim wim wim",
   "zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus zus",
   "jet jet jet jet jet jet jet jet jet jet jet jet jet jet"
   ];

public list[str] Jabberwocky = [
	"Jabberwocky by Lewis Carroll",
	"",
	"'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe.",
	"",
	"\"Beware the Jabberwock, my son!",
	"The jaws that bite, the claws that catch!",
	"Beware the Jubjub bird, and shun",
	"The frumious Bandersnatch!\"",
	"",
	"'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe.",
	"",
	"\"Beware the Jabberwock, my son!",
	"The jaws that bite, the claws that catch!",
	"Beware the Jubjub bird, and shun",
	"The frumious Bandersnatch!\"",
	"",
	"He took his vorpal sword in hand:",
	"Long time the manxome foe he sought—",
	"So rested he by the Tumtum tree,",
	"And stood awhile in thought.",
	"",
	"And as in uffish thought he stood,",
	"The Jabberwock, with eyes of flame,",
	"Came whiffling through the tulgey wood",
	"And burbled as it came!",
	"",
	"One, two! One, two! and through and through",
	"The vorpal blade went snicker-snack!",
	"He left it dead, and with its head",
	"He went galumphing back.",
	"",
	"\"And hast thou slain the Jabberwock?",
	"Come to my arms, my beamish boy!",
	"O frabjous day! Callooh! Callay!",
	"He chortled in his joy.",
	"",
	"'Twas brillig, and the slithy toves",
	"Did gyre and gimble in the wabe;",
	"All mimsy were the borogoves,",
	"And the mome raths outgrabe."
];

private map[str,int] collectData(list[str] lines){
   map[str, int] facts = ();
   for(l <- lines){
      for(/<word:\w+>/<- l)
       try {
         facts[word] = facts[word] + 1;   //TODO += does not work
       } catch NoSuchKey(value key):{
       		facts[word] = 1;
       }
   }
   return facts;
}

/*
 * Visualization
 */
 
/*
 * Draw an item in a treemap.
 */

public void drawItem(int x, int y, int w, int h, str word){
	fill(100);
	rect(x, y, w, h);
	fill(0);  // black
	
	// Draw the word if space permits this
	if(w > textWidth(word) + 6){
	    if(h > textAscent() + 6){
			textAlign(CENTER, CENTER);
			text(word, x + w/2, y + h/2);
		}
	}
}

/*
 * Data mapping used by the visualization
 */
 
private map[str,int] facts = ();

/*
 * Standard setup function for the treemap visualization
 * (Implicitly called by start)
 */
 
public void setup() {
	size(1024, 768);                 // size of the screen
	font = createFont("Serif", 13);  // create and set font
	textFont(font);
	stroke(255);                     // all strokes will be white
	                                 // create the treemap
	                                 // Discussion should this also be an implict function?
	treemap(facts, 0, 0, width(), height(), drawItem);
	noLoop();                        // only draw once
}

/* 
 * Standard draw function for the treemap visualization
 * (Implicitly called by start)
 */

public void draw() { 
    drawTreeMap();                   // draw the treemap
}

/*
 * Main function
 */

public void main(){
    //facts = collectData(leesplank);
  
    facts = collectData(Jabberwocky);
    
    /*
     * Start the visualization
     * Uses the locally defined functions like setup and draw
     * (and others for mouse handling).
     * This is how Processing does things, but is very implicit.
     
     * Discussion: possible alternatives are:
     * - separate register functions, e.g. registerSetup(setup)
     * - start has a list of (upto 8!) function parameters
     * - starts has a map argument: start(("setup" : setup))
     * - other
     */
	start();
}
