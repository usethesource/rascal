package org.meta_environment.rascal.std;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Tag Cloud by Wray Bowling (adapted)
 */

public class TagCloud {
	static ProcessingTagCloud myTagCloud;
	
	
	public static void tagcloud(IMap m){
		myTagCloud = new ProcessingTagCloud(m);
		myTagCloud.init();
	}
}

class ProcessingTagCloud extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263194045347912896L;
	
	PFont font;
	//java.lang.String fontFile = "ComicSans-48.vlw";
	float baseline_ratio = (float) 0.28;
	int large_font = 48;
	int small_font = 12;

	static java.lang.String[] tags;
	static int[] tagtally;
	static int most;
	static int least;

	float spring = (float) 0.02;
	static int numWords;
	static Tag[] Cloud;

	public ProcessingTagCloud(IMap m){
        System.err.println("ProcessingTagCloud");
		numWords = m.size();
		tags = new java.lang.String[numWords];
		tagtally = new int[numWords];
		Cloud = new Tag[numWords];
		
		most = 0;
		least = java.lang.Integer.MAX_VALUE;
		
		Iterator<Entry<IValue,IValue>> iter = m.entryIterator();
		int k = 0;
		 System.err.println("ProcessingTagCloud 2");
		while (iter.hasNext()) {
			Entry<IValue,IValue> entry = iter.next();
			java.lang.String key = ((IString) entry.getKey()).getValue();
			int val = ((IInteger) entry.getValue()).intValue();
			if(val > most)
					most = val;
			if(val < least)
					least = val;
            tags[k] = key;
            tagtally[k] = val;
            k++;
            System.err.println("ProcessingTagCloud 3");
		}
		 System.err.println("ProcessingTagCloud 4");
	}

	@Override
	public void setup() 
	{
		System.err.println("in setup");
		size(640, 320);
		smooth();
		font = createFont("ComicSans", 48);
		textFont(font);
		for (int i = 0; i < numWords; i++) {
			Cloud[i] = new Tag(Cloud, i, tags[i], tagtally[i], width/2, height/2);
		}
	}

	@Override
	public void draw() 
	{
		background(0);
		stroke(255,20);
		line(0,0,width,height);
		line(width,0,0,height);
		line(0, height/2 - 24,width, height/2 - 24);
		line(0, height/2 + 24,width, height/2 + 24);
		for (int i = 0; i < numWords; i++) {
			Cloud[i].collide();
			Cloud[i].move();
			Cloud[i].display();  
		}
	}

	class Tag extends PApplet {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1942214316002265820L;
		float x, y;
		float word_width;
		int font_size;

		float vx = 0;
		float vy = 0;
		int id;
		Tag[] others;

		Tag(Tag[] others, int id, java.lang.String word, int occurance, int x, int y) {
			this.x = x + random(-100, 100);
			this.y = y + random(-100, 100);
			font_size = Math.round(map(occurance, least, most, small_font, large_font));
			font = createFont("ComicSans", font_size);
			textFont(font);
			this.word_width = textWidth(word);

			this.id = id;
			this.others = others;
		} 

		/* I mucked with this function pretty heavily, but to no avail :( */

		void collide() {
			for (int i = id + 1; i < numWords; i++) {
				float dx = others[i].x + others[i].word_width/2 - x + word_width/2;
				float dy = others[i].y + others[i].font_size/2 - y + font_size/2;

				float minDistX = word_width + others[i].word_width;
				float minDistY = font_size + others[i].font_size;

				if ( (dx < minDistX) && (dy < minDistY) ){ 
					float angle = atan2(dy, dx);
					float targetX = x + cos(angle) * minDistX;
					float ax = (targetX - others[i].x) * spring;
					vx -= ax;
					others[i].vx += ax;

					float targetY = y + minDistY;
					float ay = (targetY - others[i].y) * spring;
					vy -= ay;
					others[i].vy += ay;
				}

			}   
		}

		void move() {
			vx *= 0.1;
			vy *= 0.75;
			x += vx;
			y += vy;
			if (x + word_width/2 > width) {
				x = width - word_width/2;
				vx *= -0.5; 
			}
			else if (x - word_width/2 < 0) {
				x = word_width/2;
				vx *= -0.5;
			}
			if (y + font_size/2 > height) {
				y = height - font_size/2;
				vy *= -0.5; 
			} 
			else if (y - font_size/2 < 0) {
				y = font_size/2;
				vy *= -0.5;
			}
		}

		void display() {
			fill(255,30);
			rectMode(CENTER);
			rect(x,y,word_width,font_size); 
			fill(255);
			textFont(font, font_size);
			text(tags[id], Math.round(x - word_width/2), Math.round(y + font_size * baseline_ratio));
		}
	} 

}
